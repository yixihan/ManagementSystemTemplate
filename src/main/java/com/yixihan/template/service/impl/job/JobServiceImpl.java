package com.yixihan.template.service.impl.job;

import cn.hutool.core.util.StrUtil;
import com.yixihan.template.model.job.Job;
import com.yixihan.template.mapper.job.JobMapper;
import com.yixihan.template.service.job.JobService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 任务表 服务实现类
 * </p>
 *
 * @author yixihan
 * @since 2024-05-24
 */
@Service
public class JobServiceImpl extends ServiceImpl<JobMapper, Job> implements JobService {

    @Override
    public Job getJobByJobCode(String jobCode) {
        if (StrUtil.isBlank(jobCode)) {
            return null;
        }
        return lambdaQuery()
                .eq(Job::getJobCode, jobCode)
                .one();
    }
}
