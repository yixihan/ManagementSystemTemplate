package com.yixihan.template.controller.user;

import com.yixihan.template.auth.annotation.HasAnyPermission;
import com.yixihan.template.auth.service.AuthService;
import com.yixihan.template.service.user.RegisterService;
import com.yixihan.template.vo.req.user.UserLoginReq;
import com.yixihan.template.vo.req.user.UserRegisterReq;
import com.yixihan.template.vo.resp.base.ApiResp;
import com.yixihan.template.vo.resp.user.AuthVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
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
    private RegisterService registerService;

    @Operation(summary = "登录")
    @HasAnyPermission(allowAnonymousUser = true)
    @PostMapping(value = "/login", produces = APPLICATION_JSON_VALUE)
    public ApiResp<AuthVO> login(@RequestBody UserLoginReq req) {
        return ApiResp.succ(authService.authentication(req));
    }

    @Operation(summary = "注册 - 手机号注册")
    @HasAnyPermission(allowAnonymousUser = true)
    @PostMapping(value = "/register/mobile", produces = APPLICATION_JSON_VALUE)
    public ApiResp<Void> registerByMobile(@RequestBody UserRegisterReq req) {
        registerService.registerByMobile(req);
        return ApiResp.succ();
    }

    @Operation(summary = "注册 - 邮箱注册")
    @HasAnyPermission(allowAnonymousUser = true)
    @PostMapping(value = "/register/email", produces = APPLICATION_JSON_VALUE)
    public ApiResp<Void> registerByEmail(@RequestBody UserRegisterReq req) {
        registerService.registerByEmail(req);
        return ApiResp.succ();
    }

    @Operation(summary = "注册 - 密码注册")
    @HasAnyPermission(allowAnonymousUser = true)
    @PostMapping(value = "/register/password", produces = APPLICATION_JSON_VALUE)
    public ApiResp<Void> registerByPassword(@RequestBody UserRegisterReq req) {
        registerService.registerByPassword(req);
        return ApiResp.succ();
    }
}
