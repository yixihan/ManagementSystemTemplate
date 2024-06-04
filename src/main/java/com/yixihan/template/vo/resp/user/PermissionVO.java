package com.yixihan.template.vo.resp.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * permission vo
 *
 * @author yixihan
 * @date 2024-05-26 10:51
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "permission vo")
public class PermissionVO implements Serializable {

    @Serial
    private static final long serialVersionUID = -2819886465768388087L;

    @Schema(description = "权限 id")
    private Long permissionId;

    @Schema(description = "权限 code")
    private String permissionCode;

    @Schema(description = "权限名")
    private String permissionName;

    @Schema(description = "状态[有效: VALID, 无效: INVALID]")
    private String status;
}
