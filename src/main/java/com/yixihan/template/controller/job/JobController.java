package com.yixihan.template.controller.job;


import com.yixihan.template.model.job.Job;
import com.yixihan.template.model.job.JobHis;
import com.yixihan.template.service.job.JobService;
import com.yixihan.template.vo.req.job.JobHisQueryReq;
import com.yixihan.template.vo.req.job.JobParam;
import com.yixihan.template.vo.req.job.JobQueryReq;
import com.yixihan.template.vo.req.job.JobUpdateReq;
import com.yixihan.template.vo.resp.base.ApiResp;
import com.yixihan.template.vo.resp.base.PageVO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * <p>
 * 任务表 前端控制器
 * </p>
 *
 * @author yixihan
 * @since 2024-05-24
 */
@RestController
@RequestMapping("/open/job")
public class JobController {

    @Resource
    private JobService jobService;

    @Schema(description = "分页查询 job")
    @PostMapping(value = "/query", produces = APPLICATION_JSON_VALUE)
    public ApiResp<PageVO<Job>> queryJob(@RequestBody JobQueryReq req) {
        return ApiResp.succ(jobService.queryJob(req));
    }

    @Schema(description = "分页查询 job 执行记录")
    @PostMapping(value = "/his/query", produces = APPLICATION_JSON_VALUE)
    public ApiResp<PageVO<JobHis>> queryJobHis(@RequestBody JobHisQueryReq req) {
        return ApiResp.succ(jobService.queryJobHis(req));
    }

    @Schema(description = "手动执行 job")
    @PutMapping(value = "/trigger", produces = APPLICATION_JSON_VALUE)
    public ApiResp<Void> triggerJob(@RequestBody JobParam req) {
        jobService.triggerJob(req);
        return ApiResp.succ();
    }

    @Schema(description = "更新 job 状态")
    @PostMapping(value = "/update", produces = APPLICATION_JSON_VALUE)
    public ApiResp<Job> updateJob(@RequestBody JobUpdateReq req) {
        return ApiResp.succ(jobService.updateJob(req));
    }

}
