package com.yixihan.template.service.user;

import com.yixihan.template.model.user.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author yixihan
 * @since 2024-05-21
 */
public interface UserService extends IService<User> {

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
}
