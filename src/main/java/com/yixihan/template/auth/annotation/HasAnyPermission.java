package com.yixihan.template.auth.annotation;

import com.yixihan.template.auth.enums.PermissionEnums;

import java.lang.annotation.*;

/**
 * 权限注解
 * <p>
 * 可加在类/方法上
 * </p>
 *
 * @author yixihan
 * @date 2024-05-23 10:43
 */
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface HasAnyPermission {

    /**
     * 是否允许匿名用户访问
     */
    boolean allowAnonymousUser() default false;

    /**
     * 访问所需的权限 code
     */
    PermissionEnums[] permissionCode() default {};
}
