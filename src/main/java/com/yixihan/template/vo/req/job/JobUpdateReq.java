package com.yixihan.template.vo.req.job;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 更新 job-req
 *
 * @author yixihan
 * @date 2024-05-27 09:27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Schema(description = "更新 job-req")
public class JobUpdateReq {

    @Schema(description = "job id")
    private Long jobId;

    @Schema(description = "任务状态[有效: VALID, 无效: INVALID]")
    private String jobStatus;
}
