package com.yixihan.template.service.job.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yixihan.template.common.enums.CommonStatusEnums;
import com.yixihan.template.job.Job;
import com.yixihan.template.job.JobRunner;
import com.yixihan.template.model.job.JobInfo;
import com.yixihan.template.mapper.job.JobInfoMapper;
import com.yixihan.template.model.job.JobHis;
import com.yixihan.template.service.job.JobHisService;
import com.yixihan.template.service.job.JobInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yixihan.template.common.util.Assert;
import com.yixihan.template.common.util.PageUtil;
import com.yixihan.template.common.util.Panic;
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
public class JobInfoServiceImpl extends ServiceImpl<JobInfoMapper, JobInfo> implements JobInfoService {

    @Resource
    private JobHisService jobHisService;

    static Map<String, Job> jobInterfaceMap;

    @Override
    public JobInfo getJobByJobCode(String jobCode) {
        Assert.notBlank(jobCode);

        return lambdaQuery()
                .eq(JobInfo::getJobCode, jobCode)
                .one();
    }

    @Override
    public PageVO<JobInfo> queryJob(JobQueryReq req) {
        Page<JobInfo> page = lambdaQuery()
                .eq(StrUtil.isNotBlank(req.getJobCode()), JobInfo::getJobCode, req.getJobCode())
                .eq(StrUtil.isNotBlank(req.getJobName()), JobInfo::getJobName, req.getJobName())
                .in(CollUtil.isNotEmpty(req.getJobStatus()), JobInfo::getJobStatus, req.getJobStatus())
                .orderByDesc(JobInfo::getLastExecuteDate)
                .page(PageUtil.toPage(req));
        return PageUtil.pageToPageVO(page);
    }

    @Override
    public void triggerJob(JobParam param) {
        initJobInterfaceList();
        Job job = jobInterfaceMap.get(param.getJobCode());
        JobInfo jobInfo = getJobByJobCode(param.getJobCode());

        if (ObjUtil.isNull(job)) {
            Panic.noSuchJob();
        }
        if (CommonStatusEnums.INVALID.name().equals(jobInfo.getJobStatus())) {
            Panic.invalidStatus(param.getJobCode());
        }

        SpringUtil.getBean(JobRunner.class).runJob(job, param);
    }

    @Override
    public JobInfo updateJob(JobUpdateReq req) {
        Assert.notNull(req.getJobId());
        Assert.isEnum(req.getJobStatus(), CommonStatusEnums.class);

        JobInfo jobInfo = getById(req.getJobId());

        if (ObjUtil.isNull(jobInfo)) {
            Panic.noSuchEntry(JobInfo.class, req.getJobId());
        }
        if (!CommonStatusEnums.INVALID.name().equals(req.getJobStatus()) && !CommonStatusEnums.VALID.name().equals(req.getJobStatus())) {
            Panic.invalidParam(req.getJobStatus());
        }

        jobInfo.setJobStatus(req.getJobStatus());
        updateById(jobInfo);
        return jobInfo;
    }

    @Override
    public PageVO<JobHis> queryJobHis(JobHisQueryReq req) {
        Assert.notNull(req.getJobId());

        Page<JobHis> page = jobHisService.lambdaQuery()
                .eq(ObjUtil.isNotNull(req.getJobId()), JobHis::getJobId, req.getJobId())
                .between(ObjUtil.isAllNotEmpty(req.getStartDate(), req.getEndDate()), JobHis::getStartDate, req.getStartDate(), req.getEndDate())
                .orderByDesc(JobHis::getStartDate)
                .page(PageUtil.toPage(req));
        return PageUtil.pageToPageVO(page);
    }

    /**
     * 初始化 - 获取所有 job
     */
    private void initJobInterfaceList() {
        if (CollUtil.isEmpty(jobInterfaceMap)) {
            synchronized (JobInfoServiceImpl.class) {
                if (CollUtil.isEmpty(jobInterfaceMap)) {
                    jobInterfaceMap = new HashMap<>();
                    for (Map.Entry<String, Job> entry : SpringUtil.getBeansOfType(Job.class).entrySet()) {
                        jobInterfaceMap.put(entry.getValue().jobCode(), entry.getValue());
                    }
                }
            }
        }
    }
}
