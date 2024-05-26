package com.yixihan.template.job;

import com.yixihan.template.vo.req.job.JobParam;

/**
 * description
 *
 * @author yixihan
 * @date 2024-05-24 09:59
 */
public interface JobInterface {

    String jobCode();

    String jobName();

    String jobDescription();

    String jobSchedule();

    void execute();

    void run(JobParam param);
}
