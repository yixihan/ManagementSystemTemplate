package com.yixihan.template.service.user.impl;

import cn.hutool.core.util.ObjUtil;
import com.yixihan.template.model.user.Role;
import com.yixihan.template.model.user.RolePermission;
import com.yixihan.template.mapper.user.RolePermissionMapper;
import com.yixihan.template.service.user.RolePermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 角色-权限关联表 服务实现类
 * </p>
 *
 * @author yixihan
 * @since 2024-05-21
 */
@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission> implements RolePermissionService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRolePermission(Role role, List<Long> permissionIdList) {
        if (ObjUtil.isNotNull(role.getId())) {
            List<RolePermission> rolePermissionList = new ArrayList<>(permissionIdList.size());
            permissionIdList.forEach(permissionId -> {
                RolePermission rolePermission = new RolePermission();
                rolePermission.setPermissionId(permissionId);
                rolePermission.setRoleId(role.getId());
                rolePermissionList.add(rolePermission);
            });
            saveBatch(rolePermissionList);
        }
    }

}
