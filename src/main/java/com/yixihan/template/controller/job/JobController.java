package com.yixihan.template.controller.job;


import com.yixihan.template.common.annotation.HasAnyPermission;
import com.yixihan.template.common.enums.PermissionEnums;
import com.yixihan.template.controller.BaseController;
import com.yixihan.template.model.job.JobInfo;
import com.yixihan.template.model.job.JobHis;
import com.yixihan.template.service.job.JobInfoService;
import com.yixihan.template.vo.req.job.JobHisQueryReq;
import com.yixihan.template.vo.req.job.JobParam;
import com.yixihan.template.vo.req.job.JobQueryReq;
import com.yixihan.template.vo.req.job.JobUpdateReq;
import com.yixihan.template.vo.resp.base.ApiResp;
import com.yixihan.template.vo.resp.base.PageVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * <p>
 * 任务 OpenAPI
 * </p>
 *
 * @author yixihan
 * @since 2024-05-24
 */
@Tag(name = "任务 OpenAPI")
@RestController
@RequestMapping("/job")
public class JobController extends BaseController {

    @Resource
    private JobInfoService jobInfoService;

    @Operation(summary = "分页查询 job")
    @HasAnyPermission(permissionCode = {PermissionEnums.ADMIN_JOB_LIST})
    @PostMapping(value = "/query", produces = APPLICATION_JSON_VALUE)
    public ApiResp<PageVO<JobInfo>> queryJob(@RequestBody JobQueryReq req) {
        return run(jobInfoService::queryJob, req);
    }

    @Operation(summary = "分页查询 job 执行记录")
    @HasAnyPermission(permissionCode = {PermissionEnums.ADMIN_JOB_LIST})
    @PostMapping(value = "/his/query", produces = APPLICATION_JSON_VALUE)
    public ApiResp<PageVO<JobHis>> queryJobHis(@RequestBody JobHisQueryReq req) {
        return run(jobInfoService::queryJobHis, req);
    }

    @Operation(summary = "手动执行 job")
    @HasAnyPermission(permissionCode = {PermissionEnums.ADMIN_JOB_EXECUTE})
    @PutMapping(value = "/trigger", produces = APPLICATION_JSON_VALUE)
    public ApiResp<Void> triggerJob(@RequestBody JobParam req) {
        return run(jobInfoService::triggerJob, req);
    }

    @Operation(summary = "更新 job 状态")
    @HasAnyPermission(permissionCode = {PermissionEnums.ADMIN_JOB_MODIFY})
    @PostMapping(value = "/update", produces = APPLICATION_JSON_VALUE)
    public ApiResp<JobInfo> updateJob(@RequestBody JobUpdateReq req) {
        return run(jobInfoService::updateJob, req);
    }

}
