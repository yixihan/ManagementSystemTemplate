package com.yixihan.template.vo.req.third.sms;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 短信验证码校验-req
 *
 * @author yixihan
 * @date 2024-05-29 10:54
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "短信验证码校验-req")
public class SmsValidateCodeReq {

    @Schema(description = "手机号")
    private String mobile;

    @Schema(description = "发送类型")
    private String mobileType;

    @Schema(description = "验证码")
    private String code;
}
