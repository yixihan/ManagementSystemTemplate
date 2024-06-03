package com.yixihan.template.vo.req.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 角色-修改-req
 *
 * @author yixihan
 * @date 2024-06-03 14:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "角色-修改-req")
public class RoleModifyReq {

    @Schema(description = "角色 id, 修改时必传")
    private Long roleId;

    @Schema(description = "角色 code")
    private String roleCode;

    @Schema(description = "角色名")
    private String roleName;

    @Schema(description = "状态[有效: VALID, 无效: INVALID]")
    private String status;

    @Schema(description = "权限列表")
    private List<Long> permissionIdList;
}
