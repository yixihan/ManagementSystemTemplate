package com.yixihan.template.model;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 基础 Model
 *
 * @author yixihan
 * @date 2024-05-21 14:04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(description = "基础 Model")
public class BaseModel implements Serializable {

    @Serial
    private static final long serialVersionUID = 3291058324355093826L;

    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createDate;

    @Schema(description = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateDate;

    @Schema(description = "乐观锁")
    private Integer version;

    @Schema(description = "逻辑删除")
    @TableLogic
    private Boolean delFlag;
}
