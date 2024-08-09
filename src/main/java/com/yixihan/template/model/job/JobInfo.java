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
 * 任务表
 *
 * @author yixihan
 * @since 2024-05-24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(description = "任务表")
@SuppressWarnings("unused")
public class JobInfo extends BaseModel {

    @Serial
    private static final long serialVersionUID = 4363012969957824062L;

    @Schema(description = "主键 id")
    @TableId(value = "job_id", type = IdType.AUTO)
    private Long jobId;

    @Schema(description = "任务 code")
    private String jobCode;

    @Schema(description = "任务 name")
    private String jobName;

    @Schema(description = "任务描述")
    private String jobDesc;

    @Schema(description = "任务执行周期[Adhoc: 无执行周期]")
    private String jobSchedule;

    @Schema(description = "任务状态[有效: VALID, 无效: INVALID]")
    private String jobStatus;

    @Schema(description = "任务上次执行时间")
    private Date lastExecuteDate;

    public static final String JOB_ID = "jobId";

    public static final String JOB_CODE = "job_code";

    public static final String JOB_NAME = "job_name";

    public static final String JOB_DESC = "job_desc";

    public static final String LAST_EXECUTE_DATE = "last_execute_date";

    public static final String CREATE_DATE = "create_date";

    public static final String UPDATE_DATE = "update_date";

    public static final String VERSION = "version";

    public static final String DEL_FLAG = "del_flag";

    @Override
    public Long getPK() {
        return jobId;
    }

    @Override
    public void setPK(Long pk) {
        this.jobId = pk;
    }
}
