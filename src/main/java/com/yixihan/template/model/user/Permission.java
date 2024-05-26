package com.yixihan.template.model.user;

import java.io.Serial;

import com.yixihan.template.model.BaseModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 权限表
 *
 * @author yixihan
 * @since 2024-05-21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(description = "权限表")
public class Permission extends BaseModel {

    @Serial
    private static final long serialVersionUID = -2648980633635961328L;

    @Schema(description = "权限 code")
    private String permissionCode;

    @Schema(description = "权限名")
    private String permissionName;

    @Schema(description = "状态[有效: VALID, 无效: INVALID]")
    private String status;

    public static final String ID = "id";

    public static final String PERMISSION_CODE = "permission_code";

    public static final String PERMISSION_NAME = "permission_name";

    public static final String STATUS = "status";

    public static final String CREATE_DATE = "create_date";

    public static final String UPDATE_DATE = "update_date";

    public static final String VERSION = "version";

    public static final String DEL_FLAG = "del_flag";

}
