package com.yixihan.template.job;

import com.yixihan.template.vo.req.job.JobParam;

/**
 * job 接口
 *
 * @author yixihan
 * @date 2024-05-24 09:59
 */
@SuppressWarnings("unused")
public interface Job {

    String jobCode();

    String jobName();

    String jobDescription();

    /**
     * job 执行周期, 文字化描述, 手动触发则置为 Manually
     * @return job 执行周期
     */
    String jobSchedule();

    /**
     * job 触发器, 此方法上写 定时注解
     * <br>
     * 手动触发: {@code @Scheduled(initialDelay = 10, timeUnit = TimeUnit.SECONDS, fixedDelay = Long.MAX_VALUE)} <br>
     * 定时触发: {@code @Scheduled(cron = "0/1 * * * * ?")} <br>
     */
    void execute();

    /**
     * job 执行具体逻辑
     * @param param job param
     */
    void run(JobParam param);
}
