package com.yixihan.template.model.user;

import java.io.Serial;

import com.yixihan.template.model.BaseModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 用户表
 *
 * @author yixihan
 * @since 2024-05-21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(description = "用户表")
public class User extends BaseModel {

    @Serial
    private static final long serialVersionUID = -8287720458629270352L;

    @Schema(description = "用户名")
    private String userName;

    @Schema(description = "用户密码")
    private String userPassword;

    @Schema(description = "用户盐")
    private String userSalt;

    @Schema(description = "用户邮箱")
    private String userEmail;

    @Schema(description = "用户手机号")
    private String userMobile;

    public static final String ID = "id";

    public static final String USER_NAME = "user_name";

    public static final String USER_PASSWORD = "user_password";

    public static final String USER_SALT = "user_salt";

    public static final String USER_EMAIL = "user_email";

    public static final String USER_MOBILE = "user_mobile";

    public static final String CREATE_DATE = "create_date";

    public static final String UPDATE_DATE = "update_date";

    public static final String VERSION = "version";

    public static final String DEL_FLAG = "del_flag";

}
