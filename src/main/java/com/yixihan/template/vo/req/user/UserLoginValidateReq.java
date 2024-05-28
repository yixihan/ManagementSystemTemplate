package com.yixihan.template.vo.req.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户登录-获取验证码参数
 *
 * @author yixihan
 * @date 2024-05-28 14:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "用户登录-获取验证码参数")
public class UserLoginValidateReq {

    @Schema(description = "登录方式[PASS: 密码登录, MOBILE: 手机号登录, EMAIL: 邮箱登录]")
    private String loginType;

    @Schema(description = "唯一标识[uuid: 密码登录, mobile: 手机号登录, email: 邮箱登录")
    private String uuid;
}
