package com.yixihan.template.service.user;

import com.yixihan.template.vo.req.user.UserRegisterReq;

/**
 * 用户注册 服务
 *
 * @author yixihan
 * @date 2024-05-29 15:48
 */
public interface RegisterService {

    /**
     * 用户注册 - 手机号注册
     *
     * @param req 请求参数
     */
    void registerByMobile(UserRegisterReq req);

    /**
     * 用户注册 - 邮箱注册
     *
     * @param req 请求参数
     */
    void registerByEmail(UserRegisterReq req);

    /**
     * 用户注册 - 密码注册
     *
     * @param req 请求参数
     */
    void registerByPassword(UserRegisterReq req);
}
