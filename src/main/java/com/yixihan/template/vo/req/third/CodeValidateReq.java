package com.yixihan.template.vo.req.third;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
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
@Schema(description = "验证码校验-req")
public class CodeValidateReq {

    @Schema(description = "手机号")
    @Size(min = 11, max = 11, message = "手机号格式不正确")
    private String mobile;

    @Schema(description = "邮箱")
    @Email(message = "邮箱格式不正确")
    @NotBlank
    private String email;

    @Schema(description = "发送类型")
    private String type;

    @Schema(description = "验证码")
    private String code;
}
