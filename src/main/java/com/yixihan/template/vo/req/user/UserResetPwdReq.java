package com.yixihan.template.vo.req.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户重置密码-req
 *
 * @author yixihan
 * @date 2024-05-30 11:05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "用户重置密码-req")
public class UserResetPwdReq {

    @Schema(description = "用户旧密码")
    private String oldPassword;

    @Schema(description = "用户新密码")
    private String newPassword;

    @Schema(description = "用户邮箱")
    private String email;

    @Schema(description = "用户手机号")
    private String mobile;

    @Schema(description = "验证码")
    private String code;

    @Schema(description = "重置方式")
    private String type;
}
