package com.yixihan.template.vo.req.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户登录参数
 *
 * @author yixihan
 * @date 2024-05-28 14:10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "用户登录参数")
public class UserLoginReq implements Serializable {

    @Serial
    private static final long serialVersionUID = 8849313937678696904L;

    @Schema(description = "登录方式[PASS: 密码登录, MOBILE: 手机号登录, EMAIL: 邮箱登录]")
    private String loginType;

    @Schema(description = "用户名")
    private String userName;

    @Schema(description = "用户手机号")
    private String userMobile;

    @Schema(description = "用户邮箱")
    private String userEmail;

    @Schema(description = "用户密码")
    private String password;

    @Schema(description = "用户验证码")
    private String validateCode;
}
