package com.yixihan.template.model.user;

import java.io.Serial;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.yixihan.template.model.BaseModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 角色表
 *
 * @author yixihan
 * @since 2024-05-21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(description = "角色表")
@SuppressWarnings("unused")
public class Role extends BaseModel {


    @Serial
    private static final long serialVersionUID = -6139564426835489854L;

    @Schema(description = "主键 id")
    @TableId(value = "role_id", type = IdType.AUTO)
    private Long roleId;

    @Schema(description = "角色 code")
    private String roleCode;

    @Schema(description = "角色名")
    private String roleName;

    @Schema(description = "状态[有效: VALID, 无效: INVALID]")
    private String status;

    public static final String ROLE_ID = "role_id";

    public static final String ROLE_CODE = "role_code";

    public static final String ROLE_NAME = "role_name";

    public static final String STATUS = "status";

    public static final String CREATE_DATE = "create_date";

    public static final String UPDATE_DATE = "update_date";

    public static final String VERSION = "version";

    public static final String DEL_FLAG = "del_flag";

}
