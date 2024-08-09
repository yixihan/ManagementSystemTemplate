package com.yixihan.template.job;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.yixihan.template.common.constant.NumberConstant;
import com.yixihan.template.model.job.JobHis;
import com.yixihan.template.model.job.JobInfo;
import com.yixihan.template.service.job.JobHisService;
import com.yixihan.template.service.job.JobInfoService;
import com.yixihan.template.vo.req.job.JobParam;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 自动清理 JobHis 任务
 *
 * @author yixihan
 * @date 2024-08-09 22:58
 */
@Slf4j
@Component
public class AutoCleanJobHisJob implements Job {

    @Resource
    private JobRunner jobRunner;

    @Resource
    private JobInfoService jobInfoService;

    @Resource
    private JobHisService jobHisService;

    @Override
    public String jobCode() {
        return "AUTO_CLEAN_JOB_HIS";
    }

    @Override
    public String jobName() {
        return "Auto Clean Job History";
    }

    @Override
    public String jobDescription() {
        return "Auto Clean Job History";
    }

    @Override
    public String jobSchedule() {
        return "0.30 am, daily";
    }

    @Override
    @Scheduled(cron = "0 30 0 * * *")
    public void execute() {
        jobRunner.runJob(this);
    }

    @Override
    public void run(JobParam param) {
        /*
        清理 JobHis 逻辑
        1. 超过 180 天的 jobHis
        2. 单个 job 的执行记录 > 1w 条, 则清理至 1w 条
         */
        // 超过 180 天的 jobHis
        Set<Long> jobHisIdList = jobHisService.lambdaQuery()
                .le(JobHis::getCreateDate, DateUtil.offsetMonth(new Date(), -6))
                .list()
                .stream()
                .map(JobHis::getPK)
                .collect(Collectors.toSet());

        // 单个 job 的执行记录 > 1w 条的 jobHis
        List<JobInfo> jobInfoList = jobInfoService.list();

        for (JobInfo jobInfo : jobInfoList) {
            Long cnt = jobHisService.lambdaQuery()
                    .eq(JobHis::getJobId, jobInfo.getPK())
                    .count();
            if (cnt <= NumberConstant.NUM_10000) {
                continue;
            }

            jobHisIdList.addAll(jobHisService.getNeedCleanJobHis(jobInfo.getJobId(), cnt - NumberConstant.NUM_10000));
        }

        if (CollUtil.isNotEmpty(jobHisIdList)) {
            // 这儿需要物理删除, 故手写 sql. 分段, 每段 1000 条批量删除
            for (List<Long> itemIdList : CollUtil.split(jobHisIdList, 1000)) {
                jobHisService.cleanJobHis(itemIdList);
            }
        }

    }
}
