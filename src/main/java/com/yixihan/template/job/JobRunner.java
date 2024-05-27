package com.yixihan.template.job;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.json.JSONUtil;
import com.yixihan.template.enums.CommonStatusEnums;
import com.yixihan.template.enums.JobStatusEnums;
import com.yixihan.template.model.job.Job;
import com.yixihan.template.model.job.JobHis;
import com.yixihan.template.service.job.JobHisService;
import com.yixihan.template.service.job.JobService;
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
    private JobService jobService;

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
        Job job;
        JobHis jobHis;

        try {
            // 获取 job & 初始化 jobHis
            job = getJob(jobInterface);
            if (CommonStatusEnums.INVALID.name().equals(job.getJobStatus())) {
                log.info("job[{}] is invalid, jump over", jobInterface.jobName());
                return;
            }
            jobHis = initJobHis(job, param);
        } catch (Throwable e) {
            log.error("job[{}] init err, msg: {}", jobInterface.jobName(), e.getMessage());
            return;
        }

        try {
            log.info("job[{}] start run", jobInterface.jobName());
            Date startDate = new Date();
            job.setLastExecuteDate(startDate);
            jobHis.setStartDate(startDate);
            // 更新 job & jobHis
            jobHisService.updateById(jobHis);
            jobService.updateById(job);

            // 执行 job
            jobInterface.run(param);

            // job 执行成功
            Date finishDate = new Date();
            jobHis.setJobStatus(JobStatusEnums.SUCCESS.name());
            jobHis.setFinishDate(finishDate);
            jobHisService.updateById(jobHis);
            log.info("job[{}] run success, cost time: {} ms", job.getJobName(), DateUtil.between(startDate, finishDate, DateUnit.MS));
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
     * @return {@link Job}
     */
    private Job getJob(JobInterface jobInterface) {
        Job job = jobService.getJobByJobCode(jobInterface.jobCode());
        if (ObjUtil.isNull(job)) {
            job = new Job();
            job.setJobCode(jobInterface.jobCode());
            job.setJobName(jobInterface.jobName());
            job.setJobDesc(jobInterface.jobDescription());
            job.setJobSchedule(jobInterface.jobSchedule());
            job.setJobStatus(CommonStatusEnums.VALID.name());
            jobService.save(job);
        }
        return job;
    }

    /**
     * 初始化 jobHis 信息
     *
     * @param job   job 信息
     * @param param job 运行参数
     * @return {@link JobHis}
     */
    private JobHis initJobHis(Job job, JobParam param) {
        JobHis jobHis = new JobHis();
        jobHis.setJobId(job.getId());
        jobHis.setJobCode(job.getJobCode());
        jobHis.setJobName(job.getJobName());
        jobHis.setJobDesc(job.getJobDesc());
        jobHis.setJobStatus(JobStatusEnums.PENDING.name());
        jobHis.setJobParam(JSONUtil.toJsonStr(param));
        jobHisService.save(jobHis);
        return jobHis;
    }
}
