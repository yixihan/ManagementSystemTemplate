package com.yixihan.template.service.user.impl;

import com.yixihan.template.model.user.User;
import com.yixihan.template.mapper.user.UserMapper;
import com.yixihan.template.service.user.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yixihan.template.util.Assert;
import com.yixihan.template.util.ValidationUtil;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author yixihan
 * @since 2024-05-21
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public boolean validateUserEmail(String email) {
        Assert.notBlank(email);
        Assert.isTrue(ValidationUtil.validateEmail(email));

        return lambdaQuery()
                .eq(User::getUserEmail, email)
                .count() > 0;
    }

    @Override
    public boolean validateUserMobile(String mobile) {
        Assert.notBlank(mobile);
        Assert.isTrue(ValidationUtil.validateMobile(mobile));

        return lambdaQuery()
                .eq(User::getUserMobile, mobile)
                .count() > 0;
    }

    @Override
    public User getUserByMobile(String mobile) {
        Assert.notBlank(mobile);
        Assert.isTrue(ValidationUtil.validateMobile(mobile));

        return lambdaQuery()
                .eq(User::getUserMobile, mobile)
                .one();
    }

    @Override
    public User getUserByEmail(String email) {
        Assert.notBlank(email);
        Assert.isTrue(ValidationUtil.validateEmail(email));

        return lambdaQuery()
                .eq(User::getUserEmail, email)
                .one();
    }

    @Override
    public User getUserByName(String userName) {
        Assert.notBlank(userName);

        return lambdaQuery()
                .eq(User::getUserName, userName)
                .one();
    }
}
