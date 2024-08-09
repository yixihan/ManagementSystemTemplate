package com.yixihan.template.model.third;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.yixihan.template.model.BaseModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;

/**
 * 模板表
 *
 * @author yixihan
 * @date 2024-05-29 14:46
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(description = "模板表")
@SuppressWarnings("unused")
public class Template extends BaseModel {

    @Serial
    private static final long serialVersionUID = 1208375875778951575L;

    @Schema(description = "主键 id")
    @TableId(value = "template_id", type = IdType.AUTO)
    private Long templateId;

    @Schema(description = "模板 code")
    private String templateCode;

    @Schema(description = "模板内容")
    private String content;

    public static final String TEMPLATE_ID = "template_id";

    public static final String TEMPLATE_CODE = "template_code";

    public static final String CONTENT = "content";

    public static final String CREATE_DATE = "create_date";

    public static final String UPDATE_DATE = "update_date";

    public static final String VERSION = "version";

    public static final String DEL_FLAG = "del_flag";
}
