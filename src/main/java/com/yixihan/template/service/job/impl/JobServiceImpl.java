package com.yixihan.template.service.job.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yixihan.template.enums.CommonStatusEnums;
import com.yixihan.template.job.JobInterface;
import com.yixihan.template.job.JobRunner;
import com.yixihan.template.model.job.Job;
import com.yixihan.template.mapper.job.JobMapper;
import com.yixihan.template.model.job.JobHis;
import com.yixihan.template.service.job.JobHisService;
import com.yixihan.template.service.job.JobService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yixihan.template.util.PageUtil;
import com.yixihan.template.util.Panic;
import com.yixihan.template.vo.req.job.JobHisQueryReq;
import com.yixihan.template.vo.req.job.JobParam;
import com.yixihan.template.vo.req.job.JobQueryReq;
import com.yixihan.template.vo.req.job.JobUpdateReq;
import com.yixihan.template.vo.resp.base.PageVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 任务表 服务实现类
 * </p>
 *
 * @author yixihan
 * @since 2024-05-24
 */
@Slf4j
@Service
public class JobServiceImpl extends ServiceImpl<JobMapper, Job> implements JobService {

    @Resource
    private JobHisService jobHisService;

    static Map<String, JobInterface> jobInterfaceMap;

    @Override
    public Job getJobByJobCode(String jobCode) {
        if (StrUtil.isBlank(jobCode)) {
            return null;
        }
        return lambdaQuery()
                .eq(Job::getJobCode, jobCode)
                .one();
    }

    @Override
    public PageVO<Job> queryJob(JobQueryReq req) {
        Page<Job> page = lambdaQuery()
                .eq(StrUtil.isNotBlank(req.getJobCode()), Job::getJobCode, req.getJobCode())
                .eq(StrUtil.isNotBlank(req.getJobName()), Job::getJobCode, req.getJobName())
                .in(CollUtil.isNotEmpty(req.getJobStatus()), Job::getJobStatus, req.getJobStatus())
                .orderByDesc(Job::getLastExecuteDate)
                .page(PageUtil.toPage(req));
        return PageUtil.pageToPageVO(page);
    }

    @Override
    public void triggerJob(JobParam param) {
        initJobInterfaceList();
        JobInterface jobInterface = jobInterfaceMap.get(param.getJobCode());
        Job job = getJobByJobCode(param.getJobCode());

        if (ObjUtil.isNull(jobInterface)) {
            Panic.noSuchJob(StrUtil.format("job code[{}] is not found", param.getJobCode()));
        }
        if (CommonStatusEnums.INVALID.name().equals(job.getJobStatus())) {
            Panic.invalidStatus(param.getJobCode());
        }

        SpringUtil.getBean(JobRunner.class).runJob(jobInterface, param);
    }

    @Override
    public Job updateJob(JobUpdateReq req) {
        Job job = getById(req.getJobId());

        if (ObjUtil.isNull(job)) {
            Panic.noSuchEntry(req.getJobId());
        }
        if (!CommonStatusEnums.INVALID.name().equals(req.getJobStatus()) && !CommonStatusEnums.VALID.name().equals(req.getJobStatus())) {
            Panic.invalidParam(req.getJobStatus());
        }

        job.setJobStatus(req.getJobStatus());
        updateById(job);
        return job;
    }

    @Override
    public PageVO<JobHis> queryJobHis(JobHisQueryReq req) {
        if (ObjUtil.isNull(req.getJobId())) {
            return PageUtil.emptyPage();
        }
        Page<JobHis> page = jobHisService.lambdaQuery()
                .eq(ObjUtil.isNotNull(req.getJobId()), JobHis::getJobId, req.getJobId())
                .between(ObjUtil.isAllNotEmpty(req.getStartDate(), req.getEndDate()), JobHis::getStartDate, req.getStartDate(), req.getEndDate())
                .orderByDesc(JobHis::getStartDate)
                .page(PageUtil.toPage(req));
        return PageUtil.pageToPageVO(page);
    }

    private void initJobInterfaceList() {
        if (CollUtil.isEmpty(jobInterfaceMap)) {
            synchronized (JobServiceImpl.class) {
                if (CollUtil.isEmpty(jobInterfaceMap)) {
                    jobInterfaceMap = new HashMap<>();
                    for (Map.Entry<String, JobInterface> entry : SpringUtil.getBeansOfType(JobInterface.class).entrySet()) {
                        jobInterfaceMap.put(entry.getValue().jobCode(), entry.getValue());
                    }
                }
            }
        }
    }
}
