package com.yixihan.template.job;

import com.yixihan.template.vo.req.job.JobParam;

/**
 * job 接口
 *
 * @author yixihan
 * @date 2024-05-24 09:59
 */
@SuppressWarnings("unused")
public interface JobInterface {

    String jobCode();

    String jobName();

    String jobDescription();

    String jobSchedule();

    void execute();

    void run(JobParam param);
}
