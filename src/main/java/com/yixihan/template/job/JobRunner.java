package com.yixihan.template.job;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.json.JSONUtil;
import com.yixihan.template.common.enums.CommonStatusEnums;
import com.yixihan.template.common.enums.JobStatusEnums;
import com.yixihan.template.model.job.JobInfo;
import com.yixihan.template.model.job.JobHis;
import com.yixihan.template.service.job.JobHisService;
import com.yixihan.template.service.job.JobInfoService;
import com.yixihan.template.vo.req.job.JobParam;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;

/**
 * Job 执行器
 *
 * @author yixihan
 * @date 2024-05-24 10:55
 */
@Slf4j
@Service
public class JobRunner {

    @Resource
    private JobInfoService jobInfoService;

    @Resource
    private JobHisService jobHisService;

    /**
     * 运行 job
     *
     * @param jobInterface job
     */
    @Transactional(isolation = REPEATABLE_READ, rollbackFor = Exception.class)
    public void runJob(JobInterface jobInterface) {
        runJob(jobInterface, null);
    }

    /**
     * 运行 job
     *
     * @param jobInterface job
     * @param param        job 运行参数
     */
    @Transactional(isolation = REPEATABLE_READ, rollbackFor = Throwable.class)
    public void runJob(JobInterface jobInterface, JobParam param) {
        JobInfo jobInfo;
        JobHis jobHis;

        try {
            // 获取 jobInfo & 初始化 jobHis
            jobInfo = getJob(jobInterface);
            if (CommonStatusEnums.INVALID.name().equals(jobInfo.getJobStatus())) {
                log.info("job[{}] is invalid, jump over", jobInterface.jobName());
                return;
            }
            jobHis = initJobHis(jobInfo, param);
        } catch (Throwable e) {
            log.error("jobInfo[{}] init err, msg: {}", jobInterface.jobName(), e.getMessage());
            return;
        }

        try {
            log.info("jobInfo[{}] start run", jobInterface.jobName());
            Date startDate = new Date();
            jobInfo.setLastExecuteDate(startDate);
            jobHis.setStartDate(startDate);
            // 更新 jobInfo & jobHis
            jobHisService.updateById(jobHis);
            jobInfoService.updateById(jobInfo);

            // 执行 job
            jobInterface.run(param);

            // jobInfo 执行成功
            Date finishDate = new Date();
            jobHis.setJobStatus(JobStatusEnums.SUCCESS.name());
            jobHis.setFinishDate(finishDate);
            jobHisService.updateById(jobHis);
            log.info("job[{}] run success, cost time: {} ms", jobInfo.getJobName(), DateUtil.between(startDate, finishDate, DateUnit.MS));
        } catch (Throwable e) {
            log.error("job[{}] run err, msg: {}", jobInterface.jobName(), e.getMessage());

            // 保存错误信息
            jobHis.setJobStatus(JobStatusEnums.FAILED.name());
            jobHis.setErrMsg(e.getMessage());
            jobHis.setErrTrace(ExceptionUtil.stacktraceToOneLineString(e.getCause()));
            jobHisService.updateById(jobHis);
        }
    }

    /**
     * 从数据库中获取 job 信息, 若获取不到, 则新增
     *
     * @param jobInterface job
     * @return {@link JobInfo}
     */
    private JobInfo getJob(JobInterface jobInterface) {
        JobInfo jobInfo = jobInfoService.getJobByJobCode(jobInterface.jobCode());
        if (ObjUtil.isNull(jobInfo)) {
            jobInfo = new JobInfo();
            jobInfo.setJobCode(jobInterface.jobCode());
            jobInfo.setJobName(jobInterface.jobName());
            jobInfo.setJobDesc(jobInterface.jobDescription());
            jobInfo.setJobSchedule(jobInterface.jobSchedule());
            jobInfo.setJobStatus(CommonStatusEnums.VALID.name());
            jobInfoService.save(jobInfo);
        }
        return jobInfo;
    }

    /**
     * 初始化 jobHis 信息
     *
     * @param jobInfo jobInfo 信息
     * @param param   jobInfo 运行参数
     * @return {@link JobHis}
     */
    private JobHis initJobHis(JobInfo jobInfo, JobParam param) {
        JobHis jobHis = new JobHis();
        jobHis.setJobId(jobInfo.getJobId());
        jobHis.setJobCode(jobInfo.getJobCode());
        jobHis.setJobName(jobInfo.getJobName());
        jobHis.setJobDesc(jobInfo.getJobDesc());
        jobHis.setJobStatus(JobStatusEnums.PENDING.name());
        jobHis.setJobParam(JSONUtil.toJsonStr(param));
        jobHisService.save(jobHis);
        return jobHis;
    }
}
