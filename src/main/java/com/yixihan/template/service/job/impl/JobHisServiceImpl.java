package com.yixihan.template.service.job.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.yixihan.template.model.job.JobHis;
import com.yixihan.template.mapper.job.JobHisMapper;
import com.yixihan.template.service.job.JobHisService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 任务执行记录表 服务实现类
 * </p>
 *
 * @author yixihan
 * @since 2024-05-24
 */
@Service
public class JobHisServiceImpl extends ServiceImpl<JobHisMapper, JobHis> implements JobHisService {

    @Override
    public List<Long> getNeedCleanJobHis(Long jobId, Long cnt) {
        if (ObjUtil.isNull(jobId)) {
            return List.of();
        }
        return baseMapper.getNeedCleanJobHis(jobId, cnt);
    }

    @Override
    public void cleanJobHis(List<Long> jobHisIdList) {
        if (CollUtil.isEmpty(jobHisIdList)) {
            return;
        }
        baseMapper.cleanJobHis(jobHisIdList);
    }
}
