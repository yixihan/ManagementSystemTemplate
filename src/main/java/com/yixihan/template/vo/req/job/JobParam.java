package com.yixihan.template.vo.req.job;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * job 运行参数
 *
 * @author yixihan
 * @date 2024-05-24 10:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "任务执行参数")
public class JobParam implements Serializable {

    @Serial
    private static final long serialVersionUID = -7970458253563174303L;

    @Schema(description = "任务 code")
    private String jobCode;

    @Schema(description = "任务执行时间")
    private Date jobExecuteDate;

    @Schema(description = "其余参数, json 格式")
    private String details;
}
