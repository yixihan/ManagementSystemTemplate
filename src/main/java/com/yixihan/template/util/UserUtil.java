package com.yixihan.template.util;

import cn.hutool.extra.spring.SpringUtil;
import com.yixihan.template.auth.service.AuthService;
import com.yixihan.template.vo.resp.user.AuthVO;
import com.yixihan.template.vo.resp.user.PermissionVO;
import com.yixihan.template.vo.resp.user.RoleVO;

import java.util.List;

/**
 * 用户工具类
 *
 * @author yixihan
 * @date 2024-06-04 10:28
 */
@SuppressWarnings("unused")
public class UserUtil {

    /**
     * 获取登录用户信息
     *
     * @return {@link AuthVO}
     */
    public static AuthVO getLoginUser() {
        return SpringUtil.getBean(AuthService.class).getLoginUser();
    }

    /**
     * 获取登录用户角色信息
     *
     * @return {@link RoleVO}
     */
    public static List<RoleVO> getLoginUserRole() {
        return SpringUtil.getBean(AuthService.class).getLoginUserRole();
    }

    /**
     * 获取登录用户权限信息
     *
     * @return {@link PermissionVO}
     */
    public static List<PermissionVO> getLoginUserPermission() {
        return SpringUtil.getBean(AuthService.class).getLoginUserPermission();
    }

    /**
     * 判断登录用户是否有指定角色
     *
     * @param roleId roleId
     */
    public static Boolean containsRole(Long roleId) {
        return getLoginUserRole().stream().map(RoleVO::getRoleId).anyMatch(it -> it.equals(roleId));
    }

    /**
     * 判断登录用户是否有指定角色
     *
     * @param roleCode roleCode
     */
    public static Boolean containsRole(String roleCode) {
        return getLoginUserRole().stream().map(RoleVO::getRoleCode).anyMatch(it -> it.equals(roleCode));
    }

    /**
     * 判断登录用户是否有指定权限
     *
     * @param permissionId permissionId
     */
    public static Boolean containsPermission(Long permissionId) {
        return getLoginUserPermission().stream().map(PermissionVO::getPermissionId).anyMatch(it -> it.equals(permissionId));
    }

    /**
     * 判断登录用户是否有指定权限
     *
     * @param permissionCode permissionCode
     */
    public static Boolean containsPermission(String permissionCode) {
        return getLoginUserPermission().stream().map(PermissionVO::getPermissionCode).anyMatch(it -> it.equals(permissionCode));
    }
}
