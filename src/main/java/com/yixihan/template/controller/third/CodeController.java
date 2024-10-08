package com.yixihan.template.controller.third;

import com.yixihan.template.common.annotation.HasAnyPermission;
import com.yixihan.template.controller.BaseController;
import com.yixihan.template.service.third.CodeService;
import com.yixihan.template.service.third.PictureService;
import com.yixihan.template.vo.req.third.CodeValidateReq;
import com.yixihan.template.vo.req.third.SendCodeReq;
import com.yixihan.template.vo.resp.base.ApiResp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * 验证码 OpenAPI
 *
 * @author yixihan
 * @date 2024-05-29 15:01
 */
@Tag(name = "验证码 OpenAPI")
@RestController
@RequestMapping("/code")
public class CodeController extends BaseController {

    @Resource
    private CodeService codeService;

    @Resource
    private PictureService pictureService;

    @Operation(summary = "获取验证码 - 邮件")
    @HasAnyPermission(allowAnonymousUser = true)
    @PostMapping(value = "/email/code", produces = APPLICATION_JSON_VALUE)
    public ApiResp<String> getEmailCode(@RequestBody SendCodeReq req) {
        return run(codeService::sendEmail, req);
    }

    @Operation(summary = "获取验证码 - 手机号")
    @HasAnyPermission(allowAnonymousUser = true)
    @PostMapping(value = "/sms/code", produces = APPLICATION_JSON_VALUE)
    public ApiResp<String> getSmsCode(@RequestBody SendCodeReq req) {
        return run(codeService::sendSms, req);
    }

    @Operation(summary = "获取验证码 - 图片")
    @HasAnyPermission(allowAnonymousUser = true)
    @GetMapping(value = "/picture/code", produces = APPLICATION_JSON_VALUE)
    public void getPictureCode(@RequestParam("uuid") String uuid, HttpServletResponse response) {
        run(pictureService::generateValidatePicture, uuid, response);
    }

    @Operation(summary = "校验验证码 - 邮件")
    @HasAnyPermission(allowAnonymousUser = true)
    @GetMapping(value = "/email/validate", produces = APPLICATION_JSON_VALUE)
    public ApiResp<Void> validateEmailCode(@RequestBody CodeValidateReq req) {
        return run(codeService::validateEmail, req);
    }

    @Operation(summary = "校验验证码 - 手机号")
    @HasAnyPermission(allowAnonymousUser = true)
    @GetMapping(value = "/sms/validate", produces = APPLICATION_JSON_VALUE)
    public ApiResp<Void> validateSmsCode(@RequestBody CodeValidateReq req) {
        return run(codeService::validateSms, req);
    }

    @Operation(summary = "校验验证码 - 图片")
    @HasAnyPermission(allowAnonymousUser = true)
    @GetMapping(value = "/picture/validate", produces = APPLICATION_JSON_VALUE)
    public ApiResp<Void> validatePictureCode(@RequestParam("uuid") String uuid, @RequestParam("code") String code) {
        return run(codeService::validate, uuid, code);
    }
}
