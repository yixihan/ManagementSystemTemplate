package com.yixihan.template.vo.req.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户修改参数
 *
 * @author yixihan
 * @date 2024-05-28 14:10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Schema(description = "用户修改参数")
public class UserModifyReq implements Serializable {

    @Serial
    private static final long serialVersionUID = 8849313937678696904L;

    @Schema(description = "用户 id")
    private Long userId;

    @Schema(description = "用户名")
    private String userName;

    @Schema(description = "用户邮箱")
    private String userEmail;

    @Schema(description = "用户手机号")
    private String userMobile;
}
