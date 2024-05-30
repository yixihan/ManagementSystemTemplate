package com.yixihan.template.vo.req.third;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 验证码校验-req
 *
 * @author yixihan
 * @date 2024-05-29 10:54
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "验证码校验-req")
public class CodeValidateReq {

    @Schema(description = "手机号")
    private String mobile;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "发送类型")
    private String type;

    @Schema(description = "验证码")
    private String code;
}
