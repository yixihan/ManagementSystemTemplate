package com.yixihan.template.vo.req.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户注册-req
 *
 * @author yixihan
 * @date 2024-05-29 15:22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "用户注册-req")
public class UserRegisterReq {

    @Schema(description = "用户名")
    @NotBlank
    private String userName;

    @Schema(description = "用户密码")
    @NotBlank
    private String password;

    @Schema(description = "用户邮箱")
    @Email(message = "邮箱格式不正确")
    private String email;

    @Schema(description = "用户手机号")
    @NotBlank
    private String mobile;

    @Schema(description = "验证码")
    @NotBlank
    private String code;
}
