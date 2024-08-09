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
     * 用户注册
     *
     * @param req 请求参数
     * @param autoRegisterFlag 是否为自动注册
     */
    void register(UserRegisterReq req, boolean autoRegisterFlag);
}
