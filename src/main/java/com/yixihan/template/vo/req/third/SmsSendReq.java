package com.yixihan.template.vo.req.third;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 短信发送-req
 *
 * @author yixihan
 * @date 2024-05-29 10:51
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "短信发送-req")
public class SmsSendReq {

    @Schema(description = "手机号")
    @NotBlank(message = "手机号不能为空")
    private String mobile;

    @Schema(description = "发送类型[REGISTER: 注册, LOGIN: 登录, PASSWORD: 修改密码, COMMON: 通用]")
    @NotBlank(message = "发送类型不能为空")
    private String type;
}
