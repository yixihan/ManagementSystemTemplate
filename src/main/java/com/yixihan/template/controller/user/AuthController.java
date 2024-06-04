package com.yixihan.template.controller.user;

import com.yixihan.template.auth.annotation.HasAnyPermission;
import com.yixihan.template.auth.service.AuthService;
import com.yixihan.template.controller.BaseController;
import com.yixihan.template.service.user.RegisterService;
import com.yixihan.template.vo.req.user.UserLoginReq;
import com.yixihan.template.vo.req.user.UserRegisterReq;
import com.yixihan.template.vo.req.user.UserResetPwdReq;
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
@RequestMapping("/auth")
public class AuthController extends BaseController {

    @Resource
    private AuthService authService;

    @Resource
    private RegisterService registerService;

    @Operation(summary = "登录")
    @HasAnyPermission(allowAnonymousUser = true)
    @PostMapping(value = "/login", produces = APPLICATION_JSON_VALUE)
    public ApiResp<AuthVO> login(@RequestBody UserLoginReq req) {
        return run(authService::login, req);
    }

    @Operation(summary = "注册")
    @HasAnyPermission(allowAnonymousUser = true)
    @PostMapping(value = "/register", produces = APPLICATION_JSON_VALUE)
    public ApiResp<Void> registerByMobile(@RequestBody UserRegisterReq req) {
        return run(registerService::register, req);
    }

    @Operation(summary = "重置密码")
    @HasAnyPermission(allowAnonymousUser = true)
    @PostMapping(value = "/reset/password", produces = APPLICATION_JSON_VALUE)
    public ApiResp<Void> resetPassword(@RequestBody UserResetPwdReq req) {
        return run(authService::resetPassword, req);
    }

    @Operation(summary = "登出")
    @HasAnyPermission(allowAnonymousUser = true)
    @PutMapping(value = "/logout", produces = APPLICATION_JSON_VALUE)
    public ApiResp<Void> logout() {
        return run(authService::logout);
    }
}
