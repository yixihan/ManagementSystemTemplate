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
 * 用户-角色关联表
 *
 * @author yixihan
 * @since 2024-05-21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(description = "用户-角色关联表")
public class UserRole extends BaseModel {

    @Serial
    private static final long serialVersionUID = 9023661422152389382L;

    @Schema(description = "用户 id")
    private Long userId;

    @Schema(description = "角色 id")
    private Long roleId;

    public static final String ID = "id";

    public static final String USER_ID = "user_id";

    public static final String ROLE_ID = "role_id";

    public static final String CREATE_DATE = "create_date";

    public static final String UPDATE_DATE = "update_date";

    public static final String VERSION = "version";

    public static final String DEL_FLAG = "del_flag";

}
