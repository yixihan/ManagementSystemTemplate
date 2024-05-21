package com.yixihan.template.model;

import java.io.Serial;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 角色-权限关联表
 *
 * @author yixihan
 * @since 2024-05-21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(description = "角色-权限关联表")
public class RolePermission extends BaseModel {

    @Serial
    private static final long serialVersionUID = 1167892432001088865L;

    @Schema(description = "角色 id")
    private Long roleId;

    @Schema(description = "权限 id")
    private Long permissionId;

    public static final String ID = "id";

    public static final String ROLE_ID = "role_id";

    public static final String PERMISSION_ID = "permission_id";

    public static final String CREATE_DATE = "create_date";

    public static final String UPDATE_DATE = "update_date";

    public static final String VERSION = "version";

    public static final String DEL_FLAG = "del_flag";

}
