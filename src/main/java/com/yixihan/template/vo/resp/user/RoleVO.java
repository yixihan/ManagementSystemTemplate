package com.yixihan.template.vo.resp.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * role vo
 *
 * @author yixihan
 * @date 2024-05-26 10:46
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "role vo")
public class RoleVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 785316133055350760L;

    @Schema(description = "角色 id")
    private Long roleId;

    @Schema(description = "角色 code")
    private String roleCode;

    @Schema(description = "角色名")
    private String roleName;

    @Schema(description = "状态[有效: VALID, 无效: INVALID]")
    private String status;

    @Schema(description = "权限列表")
    private List<PermissionVO> permissionList;
}
