package com.yixihan.template.auth.service;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjUtil;
import com.yixihan.template.auth.annotation.HasAnyPermission;
import com.yixihan.template.auth.enums.PermissionEnums;
import com.yixihan.template.enums.ExceptionEnums;
import com.yixihan.template.exception.AuthException;
import com.yixihan.template.model.user.User;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;

/**
 * 认证服务
 *
 * @author yixihan
 * @date 2024-05-23 11:24
 */
@Slf4j
@Service
public class AuthService {
    public User authentication(String token) {

        return null;
    }

    public Boolean authorization(String token, String uri) {
        return Boolean.FALSE;

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
