package com.yixihan.template.service.user;

import com.yixihan.template.model.user.Permission;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yixihan.template.vo.req.user.PermissionModifyReq;
import com.yixihan.template.vo.req.user.PermissionQueryReq;
import com.yixihan.template.vo.resp.base.PageVO;
import com.yixihan.template.vo.resp.user.PermissionVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 权限表 服务类
 * </p>
 *
 * @author yixihan
 * @since 2024-05-21
 */
public interface PermissionService extends IService<Permission> {

    /**
     * 获取 role 对应的权限列表
     *
     * @param roleIdList role id list
     * @return {@code Map<Long, List<PermissionVO>>}
     */
    Map<Long, List<PermissionVO>> getRolePermission(List<Long> roleIdList);

    /**
     * 获取 role 对应的权限列表
     *
     * @param roleId role id
     * @return {@code List<PermissionVO>}
     */
    List<PermissionVO> getRolePermission(Long roleId);

    // ===== basic CRUD
    /**
     * 修改权限
     *
     * @param req 请求参数
     * @return {@link PermissionVO}
     */
    PermissionVO modifyPermission(PermissionModifyReq req);

    /**
     * 查询权限
     *
     * @param req 请求参数
     * @return {@link PermissionVO}
     */
    PageVO<PermissionVO> queryPermission(PermissionQueryReq req);

    /**
     * 权限详情
     *
     * @param permissionId 权限 id
     * @return {@link PermissionVO}
     */
    PermissionVO permissionDetail(Long permissionId);

    // ===== validate

    /**
     * 校验权限 id 是否存在
     *
     * @param permissionIdList 权限 id
     */
    void validatePermissionId(List<Long> permissionIdList);

    /**
     * 校验权限 id 是否存在
     *
     * @param permissionId 权限 id
     */
    void validatePermissionId(Long permissionId);
}
