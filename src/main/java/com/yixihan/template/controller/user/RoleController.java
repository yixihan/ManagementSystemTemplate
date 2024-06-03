package com.yixihan.template.controller.user;


import com.yixihan.template.auth.annotation.HasAnyPermission;
import com.yixihan.template.auth.enums.PermissionEnums;
import com.yixihan.template.controller.BaseController;
import com.yixihan.template.service.user.RoleService;
import com.yixihan.template.vo.req.user.RoleModifyReq;
import com.yixihan.template.vo.req.user.RoleQueryReq;
import com.yixihan.template.vo.resp.base.ApiResp;
import com.yixihan.template.vo.resp.base.PageVO;
import com.yixihan.template.vo.resp.user.RoleVO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author yixihan
 * @since 2024-05-24
 */
@RestController
@RequestMapping("/role")
public class RoleController extends BaseController {

    @Resource
    private RoleService roleService;

    @Operation(summary = "添加角色")
    @HasAnyPermission(permissionCode = PermissionEnums.ADMIN_ROLE_MODIFY)
    @PostMapping(value = "/add", produces = APPLICATION_JSON_VALUE)
    public ApiResp<RoleVO> addRole(@RequestBody RoleModifyReq req) {
        return run(roleService::addRole, req);
    }

    @Operation(summary = "修改角色")
    @HasAnyPermission(permissionCode = PermissionEnums.ADMIN_ROLE_MODIFY)
    @PostMapping(value = "/modify", produces = APPLICATION_JSON_VALUE)
    public ApiResp<RoleVO> modifyRole(@RequestBody RoleModifyReq req) {
        return run(roleService::modifyRole, req);
    }

    @Operation(summary = "删除角色")
    @HasAnyPermission(permissionCode = PermissionEnums.ADMIN_ROLE_MODIFY)
    @DeleteMapping(value = "/remove", produces = APPLICATION_JSON_VALUE)
    public ApiResp<Void> removeRole(@RequestParam("roleId") Long roleId) {
        return run(roleService::removeRole, roleId);
    }

    @Operation(summary = "查询角色")
    @HasAnyPermission(permissionCode = PermissionEnums.ADMIN_ROLE_LIST)
    @PostMapping(value = "/query", produces = APPLICATION_JSON_VALUE)
    public ApiResp<PageVO<RoleVO>> queryRole(@RequestBody RoleQueryReq req) {
        return run(roleService::queryRole, req);
    }

    @Operation(summary = "角色详情")
    @HasAnyPermission(permissionCode = PermissionEnums.ADMIN_ROLE_LIST)
    @GetMapping(value = "/detail", produces = APPLICATION_JSON_VALUE)
    public ApiResp<RoleVO> roleDetail(@RequestParam("roleId") Long roleId) {
        return run(roleService::roleDetail, roleId);
    }
}
