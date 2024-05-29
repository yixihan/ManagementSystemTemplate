package com.yixihan.template.service.user.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.yixihan.template.model.user.Role;
import com.yixihan.template.mapper.user.RoleMapper;
import com.yixihan.template.model.user.UserRole;
import com.yixihan.template.service.user.PermissionService;
import com.yixihan.template.service.user.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yixihan.template.service.user.UserRoleService;
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
                .eq(Role::getRoleCode, "USER")
                .one()
                .getId();
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
}
