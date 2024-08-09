package com.yixihan.template.service.auth;

import com.yixihan.template.vo.resp.user.AuthVO;

/**
 * 认证-缓存服务
 *
 * @author yixihan
 * @date 2024-08-09 20:19
 */
public interface AuthCacheService {

    /**
     * 存放用户登录信息
     *
     * @param token jwtToken
     * @param auth  登录信息
     */
    void put(String token, AuthVO auth);

    /**
     * 取出用户登录信息
     *
     * @param token jwtToken
     * @return 登录信息
     */
    AuthVO get(String token);

    /**
     * 判断缓存中是否有用户信息
     *
     * @param token jwtToken
     * @return 有 : true | 无 : false
     */
    Boolean contains(String token);

    /**
     * 删除用户登录信息
     *
     * @param token jwtToken
     */
    void del(String token);
}
