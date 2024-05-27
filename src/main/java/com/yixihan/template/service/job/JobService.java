package com.yixihan.template.service.job;

import com.yixihan.template.model.job.Job;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yixihan.template.model.job.JobHis;
import com.yixihan.template.vo.req.job.JobHisQueryReq;
import com.yixihan.template.vo.req.job.JobParam;
import com.yixihan.template.vo.req.job.JobQueryReq;
import com.yixihan.template.vo.req.job.JobUpdateReq;
import com.yixihan.template.vo.resp.base.PageVO;

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

    PageVO<Job> queryJob(JobQueryReq req);

    void triggerJob(JobParam param);

    Job updateJob(JobUpdateReq req);

    PageVO<JobHis> queryJobHis(JobHisQueryReq req);
}
