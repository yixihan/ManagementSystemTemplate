package com.yixihan.template.vo.req.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 权限-修改-req
 *
 * @author yixihan
 * @date 2024-06-04 09:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "权限-修改-req")
public class PermissionModifyReq {

    @Schema(description = "权限 id")
    private Long permissionId;

    @Schema(description = "状态[有效: VALID, 无效: INVALID]")
    private String status;
}
