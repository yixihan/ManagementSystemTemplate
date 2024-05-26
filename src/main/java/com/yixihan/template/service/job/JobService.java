package com.yixihan.template.service.job;

import com.yixihan.template.model.job.Job;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 任务表 服务类
 * </p>
 *
 * @author yixihan
 * @since 2024-05-24
 */
public interface JobService extends IService<Job> {

     Job getJobByJobCode(String jobCode);
}
