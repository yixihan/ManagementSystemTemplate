package com.yixihan.template.auth.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjUtil;
import com.yixihan.template.auth.annotation.HasAnyPermission;
import com.yixihan.template.auth.cache.AuthCacheService;
import com.yixihan.template.auth.constant.AuthConstant;
import com.yixihan.template.auth.enums.PermissionEnums;
import com.yixihan.template.enums.ExceptionEnums;
import com.yixihan.template.exception.AuthException;
import com.yixihan.template.model.user.User;
import com.yixihan.template.service.user.RoleService;
import com.yixihan.template.service.user.UserService;
import com.yixihan.template.util.AppContext;
import com.yixihan.template.util.Assert;
import com.yixihan.template.util.JwtUtil;
import com.yixihan.template.vo.resp.user.AuthVO;
import com.yixihan.template.vo.resp.user.PermissionVO;
import com.yixihan.template.vo.resp.user.RoleVO;
import com.yixihan.template.vo.resp.user.UserVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 认证服务
 *
 * @author yixihan
 * @date 2024-05-23 11:24
 */
@Slf4j
@Service
public class AuthService {

    @Resource
    private UserService userService;

    @Resource
    private RoleService roleService;

    @Resource
    private AuthCacheService cacheService;

    public AuthVO authentication(String token) {
        // 从 token 种获取 userId
        Long userId = JwtUtil.analysis(token, AuthConstant.USER_ID, Long.class);

        // 从数据库种获取 user
        User user = userService.getById(userId);

        // 账户不存在
        Assert.notNull(user, ExceptionEnums.ACCOUNT_NOT_FOUND);

        // token 过期
        Assert.isTrue(JwtUtil.validateDate(token), ExceptionEnums.TOKEN_EXPIRED);

        // token 错误
        Assert.isTrue(JwtUtil.validateToken(token, user.getUserPassword()), ExceptionEnums.PASSWORD_ERR);

        // 获取用户角色信息
        List<RoleVO> userRoleList = roleService.getUserRoleList(userId);

        AuthVO authInfo = new AuthVO(
                BeanUtil.toBean(user, UserVO.class),
                userRoleList,
                token
        );

        // 存储进 redis
        cacheService.put(token, authInfo);
        // 存储进 上下文
        AppContext.getInstance().setLoginUser(authInfo);

        return authInfo;
    }

    public Object hasAnyAuthorityCheck(ProceedingJoinPoint joinPoint) {
        if (ObjUtil.isNull(joinPoint)) {
            return null;
        }

        try {
            HasAnyPermission permission = getHasAnyPermission(joinPoint);

            // api 无该注解, 直接放行
            if (ObjUtil.isNull(permission)) {
                return joinPoint.proceed();
            }
            // api 允许匿名用户访问, 直接放行
            if (permission.allowAnonymousUser()) {
                return joinPoint.proceed();
            }

            PermissionEnums[] permissionCodes = permission.permissionCode();
            if (ArrayUtil.isNotEmpty(permissionCodes)) {
                boolean permitted = hasPermissions(permissionCodes);
                if (!permitted) {
                    log.error("Access Denied to {}", joinPoint.getSignature().toLongString());
                    throw new AuthException(ExceptionEnums.ACCESS_DENIED);
                }
            }

            return joinPoint.proceed();
        } catch (
                Throwable e) {
            throw new AuthException(e);
        }
    }

    /**
     * 校验是否有权限访问 api
     *
     * @param permissionCodes permission code
     */
    private boolean hasPermissions(PermissionEnums[] permissionCodes) {
        if (ArrayUtil.isEmpty(permissionCodes)) {
            return true;
        }

        // 从数据库中取出用户的 permission code, 对比是否全有
        Set<String> userPermissionSet = roleService.getUserPermissionList(AppContext.getInstance().getLoginUser().getUser().getId())
                .stream()
                .map(PermissionVO::getPermissionCode)
                .collect(Collectors.toSet());

        for (PermissionEnums permissionCode : permissionCodes) {
            if (!userPermissionSet.contains(permissionCode.getCode())) {
                return false;
            }
        }

        return true;
    }

    /**
     * 获取 api 的 {@link HasAnyPermission} 注解
     *
     * @param joinPoint joinPoint
     * @return {@link HasAnyPermission}
     * @throws NoSuchMethodException no Such Method
     */
    private static HasAnyPermission getHasAnyPermission(JoinPoint joinPoint) throws NoSuchMethodException {
        Signature signature = joinPoint.getSignature();
        Method method = ((MethodSignature) signature).getMethod();
        Class<?> targetClass = joinPoint.getTarget().getClass();
        Method targetMethod = targetClass.getDeclaredMethod(signature.getName(), method.getParameterTypes());
        return targetMethod.getAnnotation(HasAnyPermission.class);
    }
}
