package com.yixihan.template.vo.req.job;

import com.yixihan.template.vo.req.base.PageReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.util.Date;

/**
 * 查询 job 执行记录 -req
 *
 * @author yixihan
 * @date 2024-05-27 09:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Schema(description = "查询 job 执行记录 -req")
public class JobHisQueryReq extends PageReq {

    @Serial
    private static final long serialVersionUID = 1452470000939208012L;

    @Schema(description = "任务 id")
    private Long jobId;

    @Schema(description = "开始时间")
    private Date startDate;

    @Schema(description = "结束时间")
    private Date endDate;
}
