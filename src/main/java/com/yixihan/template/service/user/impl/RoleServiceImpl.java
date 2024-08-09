package com.yixihan.template.service.user.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yixihan.template.auth.enums.RoleEnums;
import com.yixihan.template.common.enums.CommonStatusEnums;
import com.yixihan.template.common.util.Panic;
import com.yixihan.template.model.user.Role;
import com.yixihan.template.mapper.user.RoleMapper;
import com.yixihan.template.model.user.UserRole;
import com.yixihan.template.service.user.PermissionService;
import com.yixihan.template.service.user.RolePermissionService;
import com.yixihan.template.service.user.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yixihan.template.service.user.UserRoleService;
import com.yixihan.template.common.util.Assert;
import com.yixihan.template.common.util.PageUtil;
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
import java.util.stream.Collectors;

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

        // 获取用户角色 id 列表 - 查询 user_role 表
        List<Long> roleIdList = getUserRoleIdList(userId);

        if (CollUtil.isEmpty(roleIdList)) {
            return List.of();
        }

        // 获取用户角色列表
        List<RoleVO> roleList = lambdaQuery()
                .in(Role::getRoleId, roleIdList)
                .list()
                .stream()
                .map(it -> BeanUtil.toBean(it, RoleVO.class))
                .toList();
        // 获取角色权限列表
        Map<Long, List<PermissionVO>> permissionMap = permissionService.getRolePermission(roleIdList);
        // 数据组装
        for (RoleVO role : roleList) {
            role.setPermissionList(permissionMap.get(role.getRoleId()));
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

        // 获取用户角色 id 列表 - 查询 user_role 表
        List<Long> roleIdList = getUserRoleIdList(userId);

        if (CollUtil.isEmpty(roleIdList)) {
            return List.of();
        }

        Set<PermissionVO> permissionSet = new HashSet<>();
        // 获取用户权限列表
        for (Map.Entry<Long, List<PermissionVO>> entry : permissionService.getRolePermission(roleIdList).entrySet()) {
            permissionSet.addAll(entry.getValue());
        }

        return new ArrayList<>(permissionSet);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getUserRoleId() {
        return lambdaQuery()
                .eq(Role::getRoleCode, RoleEnums.USER)
                .one()
                .getRoleId();
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
        return BeanUtil.toBean(role, RoleVO.class);
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

        return BeanUtil.toBean(role, RoleVO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeRole(Long roleId) {
        Assert.notNull(roleId);

        // 删除用户角色表该角色的数据
        List<Long> userRoleIdList = userRoleService.lambdaQuery()
                .select(UserRole::getUserRoleId)
                .eq(UserRole::getRoleId, roleId)
                .list()
                .stream()
                .map(UserRole::getUserRoleId)
                .toList();

        userRoleService.removeBatchByIds(userRoleIdList);
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

        return PageUtil.pageToPageVO(page, o -> BeanUtil.toBean(o, RoleVO.class));
    }

    @Override
    @Transactional(readOnly = true)
    public RoleVO roleDetail(Long roleId) {
        Role role = getById(roleId);

        if (ObjUtil.isNull(role)) {
            Panic.noSuchEntry(Role.class, roleId);
        }
        return BeanUtil.toBean(role, RoleVO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public void validateRoleId(List<Long> roleIdList) {
        if (CollUtil.isEmpty(roleIdList)) {
            return;
        }

        // 直接根据数量对比
        Long count = lambdaQuery()
                .in(Role::getPK, roleIdList)
                .count();
        if (count == roleIdList.size()) {
            return;
        }

        // 逐个对比
        Set<Long> roleIdSet = lambdaQuery()
                .select(Role::getPK)
                .in(Role::getPK, roleIdList)
                .list()
                .stream()
                .map(Role::getPK)
                .collect(Collectors.toSet());

        for (Long id : roleIdList) {
            if (!roleIdSet.contains(id)) {
                Panic.noSuchEntry(Role.class, id);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public void validateRoleId(Long roleId) {
        validateRoleId(CollUtil.toList(roleId));
    }

    /**
     * 获取用户角色 id 列表 - 查询 user_role 关联表
     * @param userId user id
     * @return List of role id
     */
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

    /**
     * {@link RoleModifyReq} to {@link Role}
     * @param req role modify req
     * @return role model
     */
    private Role reqToRole(RoleModifyReq req) {
        Role role = new Role();
        role.setRoleId(req.getRoleId());
        role.setRoleCode(role.getRoleCode());
        role.setRoleName(role.getRoleName());
        role.setStatus(req.getStatus());
        return role;
    }
}
