package com.yixihan.template.vo.req.user;

import io.swagger.v3.oas.annotations.media.Schema;
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
    private String userName;

    @Schema(description = "用户密码")
    private String password;

    @Schema(description = "用户邮箱")
    private String email;

    @Schema(description = "用户手机号")
    private String mobile;

    @Schema(description = "验证码")
    private String code;

    @Schema(description = "注册方式")
    private String type;

    @Schema(description = "图片验证码 uuid")
    private String Uuid;
}
