package com.yixihan.template.service.user;

import com.yixihan.template.model.user.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yixihan.template.vo.req.user.UserModifyReq;
import com.yixihan.template.vo.req.user.UserQueryReq;
import com.yixihan.template.vo.resp.base.PageVO;
import com.yixihan.template.vo.resp.user.UserVO;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author yixihan
 * @since 2024-05-21
 */
public interface UserService extends IService<User> {

    // ===== basic CRUD

    /**
     * 获取用户 - 通过手机号
     *
     * @param mobile 手机号
     * @return {@link User}
     */
    User getUserByMobile(String mobile);

    /**
     * 获取用户 - 通过邮箱
     *
     * @param email 邮箱
     * @return {@link User}
     */
    User getUserByEmail(String email);

    /**
     * 获取用户 - 通过用户名
     *
     * @param userName 用户名
     * @return {@link User}
     */
    User getUserByName(String userName);

    /**
     * 获取用户 - 通过用户 id
     *
     * @param userId 用户 id
     * @return {@link User}
     */
    User getUserById(Long userId);

    /**
     * 获取用户 - 通过手机号
     *
     * @param mobile 手机号
     * @return {@link UserVO}
     */
    UserVO getUserVOByMobile(String mobile);

    /**
     * 获取用户 - 通过邮箱
     *
     * @param email 邮箱
     * @return {@link UserVO}
     */
    UserVO getUserVOByEmail(String email);

    /**
     * 获取用户 - 通过用户名
     *
     * @param userName 用户名
     * @return {@link UserVO}
     */
    UserVO getUserVOByName(String userName);

    /**
     * 获取用户 - 通过用户 id
     *
     * @param userId 用户 id
     * @return {@link UserVO}
     */
    UserVO getUserVOById(Long userId);

    /**
     * 查询用户
     *
     * @param req user query req
     * @return page of {@link UserVO}
     */
    PageVO<UserVO> queryUser(UserQueryReq req);

    /**
     * 更新用户
     *
     * @param req user modify req
     * @return {@link UserVO}
     */
    UserVO modifyUser(UserModifyReq req);

    /**
     * 删除用户
     *
     * @param userId userId
     */
    void removeUser(Long userId);

    // ===== validate

    /**
     * 校验用户是否存在 - 通过邮箱
     *
     * @param email 邮箱
     */
    boolean validateUserEmail(String email);

    /**
     * 校验用户是否存在 - 通过手机号
     *
     * @param mobile 手机号
     */
    boolean validateUserMobile(String mobile);

    /**
     * 校验用户是否存在 - 通过用户名
     *
     * @param userName 用户名
     */
    boolean validateUserName(String userName);

    /**
     * 校验用户是否存在 - 通过用户名
     *
     * @param userId 用户 id
     */
    boolean validateUserId(Long userId);
}
