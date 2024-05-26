package com.yixihan.template.auth.aspect;

import com.yixihan.template.auth.service.AuthService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * description
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

    @Around(value = "@annotation(com.yixihan.template.auth.annotation.HasAnyPermission)")
    Object checkPermission(ProceedingJoinPoint joinPoint) {
        return authService.hasAnyAuthorityCheck(joinPoint);
    }
}
