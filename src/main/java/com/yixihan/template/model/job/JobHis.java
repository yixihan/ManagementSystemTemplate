package com.yixihan.template.model.job;

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

import java.util.Date;

/**
 * 任务执行记录表
 *
 * @author yixihan
 * @since 2024-05-24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(description = "任务执行记录表")
@SuppressWarnings("unused")
public class JobHis extends BaseModel {

    @Serial
    private static final long serialVersionUID = -1711655616538944894L;

    @Schema(description = "主键 id")
    @TableId(value = "job_his_id", type = IdType.AUTO)
    private Long jobHisId;

    @Schema(description = "任务 id")
    private Long jobId;

    @Schema(description = "任务 code")
    private String jobCode;

    @Schema(description = "任务 name")
    private String jobName;

    @Schema(description = "任务描述")
    private String jobDesc;

    @Schema(description = "任务状态[PENDING: 准备中, RUNNING: 执行中, FAILED: 失败, SUCCESS: 成功]")
    private String jobStatus;

    @Schema(description = "任务参数")
    private String jobParam;

    @Schema(description = "任务开始时间")
    private Date startDate;

    @Schema(description = "任务完成时间")
    private Date finishDate;

    @Schema(description = "错误信息")
    private String errMsg;

    @Schema(description = "错误栈信息")
    private String errTrace;

    public static final String JOB_HIS_ID = "job_his_id";

    public static final String JOB_ID = "job_id";

    public static final String JOB_CODE = "job_code";

    public static final String JOB_NAME = "job_name";

    public static final String JOB_DESC = "job_desc";

    public static final String JOB_STATUS = "job_status";

    public static final String START_DATE = "start_date";

    public static final String FINISH_DATE = "finish_date";

    public static final String ERR_MSG = "err_msg";

    public static final String ERR_TRACE = "err_trace";

    public static final String CREATE_DATE = "create_date";

    public static final String UPDATE_DATE = "update_date";

    public static final String VERSION = "version";

    public static final String DEL_FLAG = "del_flag";

    @Override
    public Long getPK() {
        return jobHisId;
    }

    @Override
    public void setPK(Long pk) {
        this.jobHisId = pk;
    }
}
