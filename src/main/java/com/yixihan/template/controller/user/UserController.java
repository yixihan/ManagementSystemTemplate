package com.yixihan.template.controller.user;


import com.yixihan.template.common.annotation.HasAnyPermission;
import com.yixihan.template.common.enums.PermissionEnums;
import com.yixihan.template.controller.BaseController;
import com.yixihan.template.service.user.UserService;
import com.yixihan.template.vo.req.user.UserModifyReq;
import com.yixihan.template.vo.req.user.UserQueryReq;
import com.yixihan.template.vo.resp.base.ApiResp;
import com.yixihan.template.vo.resp.base.PageVO;
import com.yixihan.template.vo.resp.user.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author yixihan
 * @since 2024-05-24
 */
@Tag(name = "用户 OpenAPI")
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    @Resource
    private UserService userService;

    @Operation(summary = "搜索用户")
    @HasAnyPermission(permissionCode = {
            PermissionEnums.ADMIN_USER_LIST
    })
    @PostMapping(value = "/query", produces = APPLICATION_JSON_VALUE)
    public ApiResp<PageVO<UserVO>> queryUser(@RequestBody UserQueryReq req) {
        return run(userService::queryUser, req);
    }

    @Operation(summary = "获取用户信息 - 通过邮件")
    @HasAnyPermission(permissionCode = {
            PermissionEnums.ADMIN_USER_LIST
    })
    @GetMapping(value = "/load/email/{email}", produces = APPLICATION_JSON_VALUE)
    public ApiResp<UserVO> loadUserByEmail(@PathVariable("email") String email) {
        return run(userService::getUserVOByEmail, email);
    }

    @Operation(summary = "获取用户信息 - 通过手机号")
    @HasAnyPermission(permissionCode = {
            PermissionEnums.ADMIN_USER_LIST
    })
    @GetMapping(value = "/load/mobile/{mobile}", produces = APPLICATION_JSON_VALUE)
    public ApiResp<UserVO> loadUserByMobile(@PathVariable("mobile") String mobile) {
        return run(userService::getUserVOByMobile, mobile);
    }

    @Operation(summary = "获取用户信息 - 通过用户名")
    @HasAnyPermission(permissionCode = {
            PermissionEnums.ADMIN_USER_LIST
    })
    @GetMapping(value = "/load/userName/{userName}", produces = APPLICATION_JSON_VALUE)
    public ApiResp<UserVO> loadUserByName(@PathVariable("userName") String userName) {
        return run(userService::getUserVOByName, userName);
    }

    @Operation(summary = "获取用户信息 - 通过用户 id")
    @HasAnyPermission(permissionCode = {
            PermissionEnums.ADMIN_USER_LIST
    })
    @GetMapping(value = "/load/userId/{userId}", produces = APPLICATION_JSON_VALUE)
    public ApiResp<UserVO> loadUserById(@PathVariable("userId") Long userId) {
        return run(userService::getUserVOById, userId);
    }

    @Operation(summary = "修改用户信息")
    @HasAnyPermission(permissionCode = {
            PermissionEnums.ADMIN_USER_MODIFY,
            PermissionEnums.ADMIN_USER_LIST
    })
    @PostMapping(value = "/modify", produces = APPLICATION_JSON_VALUE)
    public ApiResp<UserVO> removeRole(@RequestBody UserModifyReq req) {
        return run(userService::modifyUser, req);
    }

    @Operation(summary = "删除用户")
    @HasAnyPermission(permissionCode = {
            PermissionEnums.ADMIN_USER_MODIFY,
            PermissionEnums.ADMIN_USER_LIST
    })
    @DeleteMapping(value = "/remove/{userId}", produces = APPLICATION_JSON_VALUE)
    public ApiResp<Void> removeRole(@PathVariable("userId") Long userId) {
        return run(userService::removeUser, userId);
    }
}
