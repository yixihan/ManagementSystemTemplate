package com.yixihan.template.controller.third;

import com.yixihan.template.auth.annotation.HasAnyPermission;
import com.yixihan.template.controller.BaseController;
import com.yixihan.template.model.third.ObjectStorage;
import com.yixihan.template.service.third.ObjectStorageService;
import com.yixihan.template.vo.req.third.OsCertificateReq;
import com.yixihan.template.vo.resp.base.ApiResp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

/**
 * 对象存储 OpenAPI
 *
 * @author yixihan
 * @date 2024-06-11 10:10
 */
@Tag(name = "对象存储 OpenAPI")
@RestController
@RequestMapping("/os")
public class OsController extends BaseController {

    @Resource
    private ObjectStorageService objectStorageService;

    @Operation(summary = "上传文件 - 获取凭证")
    @HasAnyPermission(allowAnonymousUser = true)
    @PostMapping(value = "/certificate", produces = APPLICATION_JSON_VALUE)
    public ApiResp<ObjectStorage> certificate(@RequestBody OsCertificateReq req) {
        return run(objectStorageService::certificate, req);
    }

    @Operation(summary = "上传文件 - 获取凭证 - 回调")
    @HasAnyPermission(allowAnonymousUser = true)
    @PostMapping(value = "/certificate/callback", produces = APPLICATION_JSON_VALUE)
    public ApiResp<Void> certificateCallback(@RequestBody ObjectStorage os) {
        return run(objectStorageService::certificateCallback, os);
    }

    @Operation(summary = "上传文件 - 服务器直传")
    @HasAnyPermission(allowAnonymousUser = true)
    @PostMapping(value = "/upload", produces = APPLICATION_JSON_VALUE, consumes = MULTIPART_FORM_DATA_VALUE)
    public ApiResp<Long> upload(@RequestParam MultipartFile file) {
        return run(objectStorageService::uploadFile, file);
    }
}
