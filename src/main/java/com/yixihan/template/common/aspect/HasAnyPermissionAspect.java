package com.yixihan.template.common.aspect;

import com.yixihan.template.service.auth.AuthService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 权限注解 切片处理器
 *
 * @author yixihan
 * @date 2024-05-23 14:13
 */
@Slf4j
@Aspect
@Component
public class HasAnyPermissionAspect {

    @Resource
    private AuthService authService;

    @Around(value = "@annotation(com.yixihan.template.common.annotation.HasAnyPermission)")
    Object checkPermission(ProceedingJoinPoint joinPoint) {
        return authService.hasAnyAuthorityCheck(joinPoint);
    }
}
