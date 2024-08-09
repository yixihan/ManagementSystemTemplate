package com.yixihan.template.service.auth;

import com.yixihan.template.vo.req.user.UserLoginReq;
import com.yixihan.template.vo.req.user.UserResetPwdReq;
import com.yixihan.template.vo.resp.user.AuthVO;
import com.yixihan.template.vo.resp.user.PermissionVO;
import com.yixihan.template.vo.resp.user.RoleVO;
import org.aspectj.lang.ProceedingJoinPoint;

import java.util.List;

/**
 * 认证服务
 *
 * @author yixihan
 * @date 2024-08-09 20:11
 */
public interface AuthService {

    /**
     * 权限认证
     *
     * @param token user token
     * @return {@link AuthVO}
     */
    AuthVO authentication(String token);

    /**
     * 登录
     *
     * @param req user login req
     * @return {@link AuthVO}
     */
    AuthVO login(UserLoginReq req);

    /**
     * 重置密码
     *
     * @param req user rest password req
     */
    void resetPassword(UserResetPwdReq req);

    /**
     * 登出
     */
    void logout();

    /**
     * 获取登录用户信息
     *
     * @return {@link AuthVO}
     */
    AuthVO getLoginUser();

    /**
     * 获取登录用户角色信息
     *
     * @return List of {@link RoleVO}
     */
    List<RoleVO> getLoginUserRole();

    /**
     * 获取登录用户权限信息
     *
     * @return List of {@link PermissionVO}
     */
    List<PermissionVO> getLoginUserPermission();

    /**
     * 权限认证 - aop 切片
     *
     * @param joinPoint aop join point
     * @return Object
     */
    Object hasAnyAuthorityCheck(ProceedingJoinPoint joinPoint);
}
