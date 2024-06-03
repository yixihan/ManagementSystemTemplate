package com.yixihan.template.service.user;

import com.yixihan.template.model.user.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yixihan.template.vo.req.user.RoleModifyReq;
import com.yixihan.template.vo.req.user.RoleQueryReq;
import com.yixihan.template.vo.resp.base.PageVO;
import com.yixihan.template.vo.resp.user.PermissionVO;
import com.yixihan.template.vo.resp.user.RoleVO;

import java.util.List;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author yixihan
 * @since 2024-05-21
 */
public interface RoleService extends IService<Role> {

    /**
     * 获取用户的角色信息
     *
     * @param userId 用户 id
     * @return {@code List<RoleVO>}
     */
    List<RoleVO> getUserRoleList(Long userId);

    /**
     * 获取用户的权限信息
     *
     * @param userId 用户 id
     * @return {@code List<PermissionVO>}
     */
    List<PermissionVO> getUserPermissionList(Long userId);

    /**
     * 获取用户角色 id
     *
     * @return 用户角色 id
     */
    Long getUserRoleId();

    /**
     * 新增角色
     *
     * @param req 请求参数
     * @return {@link RoleVO}
     */
    RoleVO addRole(RoleModifyReq req);

    /**
     * 修改角色
     *
     * @param req 请求参数
     * @return {@link RoleVO}
     */
    RoleVO modifyRole(RoleModifyReq req);

    /**
     * 删除角色
     *
     * @param roleId 角色 id
     */
    void removeRole(Long roleId);

    /**
     * 查询角色
     *
     * @param req 请求参数
     * @return {@link RoleVO}
     */
    PageVO<RoleVO> queryRole(RoleQueryReq req);

    /**
     * 角色详情
     *
     * @param roleId 角色 id
     * @return {@link RoleVO}
     */
    RoleVO roleDetail(Long roleId);
}
