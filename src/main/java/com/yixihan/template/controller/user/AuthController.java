package com.yixihan.template.controller.user;

import com.yixihan.template.auth.annotation.HasAnyPermission;
import com.yixihan.template.auth.service.AuthService;
import com.yixihan.template.service.third.PictureService;
import com.yixihan.template.vo.req.user.UserLoginReq;
import com.yixihan.template.vo.req.user.UserLoginValidateReq;
import com.yixihan.template.vo.resp.base.ApiResp;
import com.yixihan.template.vo.resp.user.AuthVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * 认证 OpenAPI
 *
 * @author yixihan
 * @date 2024-05-28 13:56
 */
@Tag(name = "认证 OpenAPI")
@RestController
@RequestMapping("/open/auth")
public class AuthController {

    @Resource
    private AuthService authService;

    @Resource
    private PictureService pictureService;

    @Operation(summary = "登录")
    @HasAnyPermission(allowAnonymousUser = true)
    @PostMapping(value = "/login", produces = APPLICATION_JSON_VALUE)
    public ApiResp<AuthVO> login(@RequestBody UserLoginReq req) {
        return ApiResp.succ(authService.authentication(req));
    }

    @Operation(summary = "获取验证码")
    @HasAnyPermission(allowAnonymousUser = true)
    @GetMapping(value = "/validate/code", produces = APPLICATION_JSON_VALUE)
    public ApiResp<String> getLoginValidateCode(@RequestBody UserLoginValidateReq req) {
        return ApiResp.succ(authService.getLoginValidateCode(req));
    }

    @Operation(summary = "获取验证码(图片)")
    @HasAnyPermission(allowAnonymousUser = true)
    @GetMapping(value = "/validate/picture", produces = APPLICATION_JSON_VALUE)
    public void getValidatePicture(@RequestParam("uuid") String uuid, HttpServletResponse response) {
        pictureService.generateValidatePicture(uuid, response);
    }
}
