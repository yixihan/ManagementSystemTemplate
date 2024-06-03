package com.yixihan.template.service.user;

import com.yixihan.template.model.user.Role;
import com.yixihan.template.model.user.RolePermission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 角色-权限关联表 服务类
 * </p>
 *
 * @author yixihan
 * @since 2024-05-21
 */
public interface RolePermissionService extends IService<RolePermission> {

    /**
     * 保存角色权限
     *
     * @param role             角色
     * @param permissionIdList 权限列表
     */
    void saveRolePermission(Role role, List<Long> permissionIdList);

}
