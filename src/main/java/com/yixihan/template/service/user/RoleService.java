package com.yixihan.template.service.user;

import com.yixihan.template.model.user.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yixihan.template.vo.resp.user.PermissionVO;
import com.yixihan.template.vo.resp.user.RoleVO;

import java.util.List;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author yixihan
 * @since 2024-05-21
 */
public interface RoleService extends IService<Role> {

    /**
     * 获取用户的角色信息
     * @param userId 用户 id
     * @return {@code List<RoleVO>}
     */
    List<RoleVO> getUserRoleList(Long userId);

    /**
     * 获取用户的权限信息
     * @param userId 用户 id
     * @return {@code List<PermissionVO>}
     */
    List<PermissionVO> getUserPermissionList(Long userId);

    /**
     * 获取用户角色 id
     * @return 用户角色 id
     */
    Long getUserRoleId();

}
