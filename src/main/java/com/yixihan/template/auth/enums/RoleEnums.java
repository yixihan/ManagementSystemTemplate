package com.yixihan.template.auth.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 角色枚举
 *
 * @author yixihan
 * @date 2024-06-03 10:34
 */
@Getter
@AllArgsConstructor
public enum RoleEnums {

    ADMIN("ADMIN", "管理员"),
    USER("USER", "用户"),


    ;


    private final String role;

    private final String desc;

}
