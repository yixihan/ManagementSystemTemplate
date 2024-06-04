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
 * 权限-查询-req
 *
 * @author yixihan
 * @date 2024-06-04 09:44
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Schema(description = "权限-查询-req")
public class PermissionQueryReq extends PageReq {

    @Serial
    private static final long serialVersionUID = 1167020406654354505L;

    @Schema(description = "权限 code")
    private String permissionCode;

    @Schema(description = "权限名字")
    private String permissionName;

    @Schema(description = "状态[有效: VALID, 无效: INVALID]")
    private List<String> status;
}
