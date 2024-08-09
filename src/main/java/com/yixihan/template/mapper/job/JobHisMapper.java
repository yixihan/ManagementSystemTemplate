package com.yixihan.template.mapper.job;

import com.yixihan.template.model.job.JobHis;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 任务执行记录表 Mapper 接口
 * </p>
 *
 * @author yixihan
 * @since 2024-05-24
 */
@Mapper
public interface JobHisMapper extends BaseMapper<JobHis> {

    /**
     * 获取需要删除的 jobHis - 单个 job 的执行记录 > 1w 条
     *
     * @param jobId job id
     * @param cnt 需删除的数量, 用于 limit
     * @return List of need clean job his id
     */
    List<Long> getNeedCleanJobHis(@Param("jobId") Long jobId,
                                  @Param("cnt") Long cnt);

    /**
     * 删除 jobHis
     *
     * @param jobHisIdList List of need clean job his id
     */
    void cleanJobHis(@Param("list") List<Long> jobHisIdList);

}
