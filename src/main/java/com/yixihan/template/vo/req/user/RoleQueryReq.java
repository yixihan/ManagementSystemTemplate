package com.yixihan.template.vo.req.user;

import com.yixihan.template.vo.req.base.PageReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.util.List;

/**
 * 角色-查询-req
 *
 * @author yixihan
 * @date 2024-06-03 14:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Schema(description = "角色-查询-req")
public class RoleQueryReq extends PageReq {

    @Serial
    private static final long serialVersionUID = -3793378405264651263L;

    @Schema(description = "角色 code")
    private String roleCode;

    @Schema(description = "角色名")
    private String roleName;

    @Schema(description = "状态[有效: VALID, 无效: INVALID]")
    private List<String> status;
}
