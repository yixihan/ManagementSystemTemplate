package com.yixihan.template.service.user.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yixihan.template.auth.enums.RoleEnums;
import com.yixihan.template.enums.CommonStatusEnums;
import com.yixihan.template.model.user.Role;
import com.yixihan.template.mapper.user.RoleMapper;
import com.yixihan.template.model.user.UserRole;
import com.yixihan.template.service.user.PermissionService;
import com.yixihan.template.service.user.RolePermissionService;
import com.yixihan.template.service.user.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yixihan.template.service.user.UserRoleService;
import com.yixihan.template.util.Assert;
import com.yixihan.template.util.PageUtil;
import com.yixihan.template.vo.req.user.RoleModifyReq;
import com.yixihan.template.vo.req.user.RoleQueryReq;
import com.yixihan.template.vo.resp.base.PageVO;
import com.yixihan.template.vo.resp.user.PermissionVO;
import com.yixihan.template.vo.resp.user.RoleVO;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author yixihan
 * @since 2024-05-21
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Resource
    private PermissionService permissionService;

    @Resource
    private UserRoleService userRoleService;

    @Resource
    private RolePermissionService rolePermissionService;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "userRoleCache", key = "#userId")
    public List<RoleVO> getUserRoleList(Long userId) {
        if (ObjUtil.isNull(userId)) {
            return List.of();
        }

        List<Long> roleIdList = getUserRoleIdList(userId);

        if (CollUtil.isEmpty(roleIdList)) {
            return List.of();
        }
        List<RoleVO> roleList = lambdaQuery()
                .in(Role::getId, roleIdList)
                .list()
                .stream()
                .map(it -> BeanUtil.toBean(it, RoleVO.class))
                .toList();
        Map<Long, List<PermissionVO>> permissionMap = permissionService.getRolePermission(roleIdList);

        for (RoleVO role : roleList) {
            role.setPermissionList(permissionMap.get(role.getId()));
        }

        return roleList;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "userPermissionCache", key = "#userId")
    public List<PermissionVO> getUserPermissionList(Long userId) {
        if (ObjUtil.isNull(userId)) {
            return List.of();
        }

        List<Long> roleIdList = getUserRoleIdList(userId);

        if (CollUtil.isEmpty(roleIdList)) {
            return List.of();
        }

        Set<PermissionVO> permissionSet = new HashSet<>();
        for (Map.Entry<Long, List<PermissionVO>> entry : permissionService.getRolePermission(roleIdList).entrySet()) {
            permissionSet.addAll(entry.getValue());
        }

        return new ArrayList<>(permissionSet);
    }

    @Override
    public Long getUserRoleId() {
        return lambdaQuery()
                .eq(Role::getRoleCode, RoleEnums.USER)
                .one()
                .getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RoleVO addRole(RoleModifyReq req) {
        Assert.isNull(req.getRoleId());
        Assert.notBlank(req.getRoleCode());
        Assert.notBlank(req.getRoleName());
        if (StrUtil.isBlank(req.getStatus())) {
            req.setStatus(CommonStatusEnums.VALID.getCode());
        }

        Role role = reqToRole(req);
        save(role);
        return roleToRoleVO(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RoleVO modifyRole(RoleModifyReq req) {
        Assert.notNull(req.getRoleId());
        Assert.notBlank(req.getRoleCode());
        Assert.notBlank(req.getRoleName());
        Assert.notBlank(req.getStatus());

        Role role = reqToRole(req);
        updateById(role);

        if (CollUtil.isNotEmpty(req.getPermissionIdList())) {
            permissionService.validatePermissionId(req.getPermissionIdList());
            rolePermissionService.saveRolePermission(role, req.getPermissionIdList());
        }

        return roleToRoleVO(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeRole(Long roleId) {
        Assert.notNull(roleId);

        removeById(roleId);
    }

    @Override
    @Transactional(readOnly = true)
    public PageVO<RoleVO> queryRole(RoleQueryReq req) {
        Page<Role> page = lambdaQuery()
                .eq(StrUtil.isNotBlank(req.getRoleCode()), Role::getRoleCode, req.getRoleCode())
                .eq(StrUtil.isNotBlank(req.getRoleName()), Role::getRoleName, req.getRoleName())
                .in(CollUtil.isNotEmpty(req.getStatus()), Role::getStatus, req.getStatus())
                .orderByDesc(Role::getUpdateDate)
                .page(PageUtil.toPage(req));

        return PageUtil.pageToPageVO(page, this::roleToRoleVO);
    }

    @Override
    @Transactional(readOnly = true)
    public RoleVO roleDetail(Long roleId) {
        Role role = getOptById(roleId).orElse(new Role());
        return roleToRoleVO(role);
    }

    private List<Long> getUserRoleIdList(Long userId) {
        if (ObjUtil.isNull(userId)) {
            return List.of();
        }

        return userRoleService.lambdaQuery()
                .select(UserRole::getRoleId)
                .eq(UserRole::getUserId, userId)
                .list()
                .stream()
                .map(UserRole::getRoleId)
                .toList();
    }

    private Role reqToRole(RoleModifyReq req) {
        Role role = new Role();
        role.setId(req.getRoleId());
        role.setRoleCode(role.getRoleCode());
        role.setRoleName(role.getRoleName());
        role.setStatus(req.getStatus());
        return role;
    }

    private RoleVO roleToRoleVO(Role role) {
        RoleVO vo = new RoleVO();
        vo.setId(role.getId());
        vo.setRoleCode(role.getRoleCode());
        vo.setRoleName(role.getRoleName());
        vo.setStatus(role.getStatus());
        vo.setPermissionList(permissionService.getRolePermission(role.getId()));
        return vo;
    }
}
