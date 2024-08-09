package com.yixihan.template.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 权限枚举
 * <br/>
 * 在此类添加新的权限, 项目启动会自动添加到数据库中
 *
 * @author yixihan
 * @date 2024-05-23 10:44
 */
@Getter
@AllArgsConstructor
public enum PermissionEnums {

    ADMIN("ADMIN", "Admin"),


    ADMIN_ROLE("ADMIN_ROLE", "Admin Role"),
    ADMIN_ROLE_MODIFY("ADMIN_ROLE_MODIFY", "Modify Role"),
    ADMIN_ROLE_LIST("ADMIN_ROLE_LIST", "List Role"),

    ADMIN_PERMISSION("ADMIN_PERMISSION", "Admin Permission"),
    ADMIN_PERMISSION_MODIFY("ADMIN_PERMISSION_MODIFY", "Modify Permission"),
    ADMIN_PERMISSION_LIST("ADMIN_PERMISSION_LIST", "List Permission"),

    ADMIN_USER("ADMIN_USER", "Admin User"),
    ADMIN_USER_MODIFY("ADMIN_USER_MODIFY", "Modify User"),
    ADMIN_USER_LIST("ADMIN_USER_LIST", "List User"),

    ADMIN_JOB("ADMIN_JOB", "Admin Job"),
    ADMIN_JOB_MODIFY("ADMIN_JOB_MODIFY", "Modify Job"),
    ADMIN_JOB_LIST("ADMIN_JOB_LIST", "List Job"),
    ADMIN_JOB_EXECUTE("ADMIN_JOB_EXECUTE", "Execute Job"),
    ;
    private final String code;

    private final String name;
}
