package com.yixihan.template.vo.req.third;

import io.swagger.v3.oas.annotations.media.Schema;
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
    private String mobile;

    @Schema(description = "发送类型[REGISTER: 注册, LOGIN: 登录, PASSWORD: 修改密码, COMMON: 通用]")
    private String type;
}
