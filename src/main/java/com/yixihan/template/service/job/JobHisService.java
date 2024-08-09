package com.yixihan.template.service.job;

import com.yixihan.template.model.job.JobHis;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 任务执行记录表 服务类
 * </p>
 *
 * @author yixihan
 * @since 2024-05-24
 */
public interface JobHisService extends IService<JobHis> {

    /**
     * 获取需要删除的 jobHis - 单个 job 的执行记录 > 1w 条
     *
     * @param jobId job id
     * @param cnt 需删除的数量, 用于 limit
     * @return List of need clean job his id
     */
    List<Long> getNeedCleanJobHis(Long jobId, Long cnt);

    /**
     * 删除 jobHis
     *
     * @param jobHisIdList List of need clean job his id
     */
    void cleanJobHis(List<Long> jobHisIdList);

}
