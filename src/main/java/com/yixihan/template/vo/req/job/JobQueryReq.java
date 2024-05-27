package com.yixihan.template.vo.req.job;

import com.yixihan.template.vo.req.base.PageReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.util.List;

/**
 * 查询 job-req
 *
 * @author yixihan
 * @date 2024-05-26 14:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Schema(description = "查询 job-req")
public class JobQueryReq extends PageReq {

    @Serial
    private static final long serialVersionUID = -3545158529371752151L;

    @Schema(description = "任务 code")
    private String jobCode;

    @Schema(description = "任务 name")
    private String jobName;

    @Schema(description = "任务状态[有效: VALID, 无效: INVALID]")
    private List<String> jobStatus;
}
