package com.yixihan.template.controller.user;


import com.yixihan.template.auth.annotation.HasAnyPermission;
import com.yixihan.template.auth.enums.PermissionEnums;
import com.yixihan.template.controller.BaseController;
import com.yixihan.template.service.user.PermissionService;
import com.yixihan.template.vo.req.user.PermissionModifyReq;
import com.yixihan.template.vo.req.user.PermissionQueryReq;
import com.yixihan.template.vo.resp.base.ApiResp;
import com.yixihan.template.vo.resp.base.PageVO;
import com.yixihan.template.vo.resp.user.PermissionVO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * <p>
 * 权限表 前端控制器
 * </p>
 *
 * @author yixihan
 * @since 2024-05-24
 */
@RestController
@RequestMapping("/permission")
public class PermissionController extends BaseController {

    @Resource
    private PermissionService permissionService;

    @Operation(summary = "修改权限")
    @HasAnyPermission(permissionCode = PermissionEnums.ADMIN_PERMISSION_MODIFY)
    @PostMapping(value = "/modify", produces = APPLICATION_JSON_VALUE)
    public ApiResp<PermissionVO> modifyPermission(@RequestBody PermissionModifyReq req) {
        return run(permissionService::modifyPermission, req);
    }

    @Operation(summary = "查询权限")
    @HasAnyPermission(permissionCode = PermissionEnums.ADMIN_PERMISSION_LIST)
    @PostMapping(value = "/query", produces = APPLICATION_JSON_VALUE)
    public ApiResp<PageVO<PermissionVO>> queryPermission(@RequestBody PermissionQueryReq req) {
        return run(permissionService::queryPermission, req);
    }

    @Operation(summary = "权限详情")
    @HasAnyPermission(permissionCode = PermissionEnums.ADMIN_PERMISSION_LIST)
    @GetMapping(value = "/detail", produces = APPLICATION_JSON_VALUE)
    public ApiResp<PermissionVO> permissionDetail(@RequestParam("permissionId") Long permissionId) {
        return run(permissionService::permissionDetail, permissionId);
    }

}
