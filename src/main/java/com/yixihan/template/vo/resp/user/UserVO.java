package com.yixihan.template.vo.resp.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * user vo
 *
 * @author yixihan
 * @date 2024-05-26 10:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "user vo")
public class UserVO implements Serializable {

    @Serial
    private static final long serialVersionUID = -5631728608175150683L;

    @Schema(description = "用户 id")
    private Long id;

    @Schema(description = "用户名")
    private String userName;

    @Schema(description = "用户邮箱")
    private String userEmail;

    @Schema(description = "用户手机号")
    private String userMobile;
}
