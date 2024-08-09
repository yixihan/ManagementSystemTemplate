package com.yixihan.template.service.user.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yixihan.template.common.util.PageUtil;
import com.yixihan.template.common.util.Panic;
import com.yixihan.template.model.user.User;
import com.yixihan.template.mapper.user.UserMapper;
import com.yixihan.template.service.user.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yixihan.template.common.util.Assert;
import com.yixihan.template.common.util.ValidationUtil;
import com.yixihan.template.vo.req.user.UserModifyReq;
import com.yixihan.template.vo.req.user.UserQueryReq;
import com.yixihan.template.vo.resp.base.PageVO;
import com.yixihan.template.vo.resp.user.UserVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(readOnly = true)
    public User getUserByMobile(String mobile) {
        Assert.notBlank(mobile);
        Assert.isTrue(ValidationUtil.validateMobile(mobile));

        if (!validateUserMobile(mobile)) {
            Panic.logic(StrUtil.format("can not find user by mobile[{}]", mobile));
        }
        return lambdaQuery()
                .eq(User::getUserMobile, mobile)
                .one();
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        Assert.notBlank(email);
        Assert.isTrue(ValidationUtil.validateEmail(email));

        if (!validateUserEmail(email)) {
            Panic.logic(StrUtil.format("can not find user by email[{}]", email));
        }
        return lambdaQuery()
                .eq(User::getUserEmail, email)
                .one();
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserByName(String userName) {
        Assert.notBlank(userName);

        if (!validateUserName(userName)) {
            Panic.logic(StrUtil.format("can not find user by userName[{}]", userName));
        }
        return lambdaQuery()
                .eq(User::getUserName, userName)
                .one();
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long userId) {
        Assert.notNull(userId);

        if (!validateUserId(userId)) {
            Panic.logic(StrUtil.format("can not find user by userId[{}]", userId));
        }
        return getById(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public UserVO getUserVOByMobile(String mobile) {
        return BeanUtil.toBean(getUserByMobile(mobile), UserVO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public UserVO getUserVOByEmail(String email) {
        return BeanUtil.toBean(getUserByEmail(email), UserVO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public UserVO getUserVOByName(String userName) {
        return BeanUtil.toBean(getUserByName(userName), UserVO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public UserVO getUserVOById(Long userId) {
        return BeanUtil.toBean(getUserById(userId), UserVO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public PageVO<UserVO> queryUser(UserQueryReq req) {
        Assert.notNull(req);

        Page<User> page = baseMapper.queryUser(req, PageUtil.toPage(req));

        return PageUtil.pageToPageVO(page, o -> BeanUtil.toBean(o, UserVO.class));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserVO modifyUser(UserModifyReq req) {
        Assert.notNull(req);
        Assert.notNull(req.getUserId());

        if (!validateUserId(req.getUserId())) {
            Panic.noSuchEntry(User.class, req.getUserId());
        }

        if (StrUtil.isNotBlank(req.getUserName()) && validateUserName(req.getUserName())) {
            Panic.logic("userName[{}] has been used by other users", req.getUserName());
        }
        if (StrUtil.isNotBlank(req.getUserEmail()) && validateUserName(req.getUserEmail())) {
            Panic.logic("email[{}] has been used by other users", req.getUserEmail());
        }
        if (StrUtil.isNotBlank(req.getUserMobile()) && validateUserName(req.getUserMobile())) {
            Panic.logic("mobile[{}] has been used by other users", req.getUserMobile());
        }

        // 获取乐观锁
        Integer version = getUserById(req.getUserId()).getVersion();
        lambdaUpdate()
                .eq(User::getUserId, req.getUserId())
                .eq(User::getVersion, version)
                .set(StrUtil.isNotBlank(req.getUserName()), User::getUserName, req.getUserName())
                .set(StrUtil.isNotBlank(req.getUserEmail()), User::getUserEmail, req.getUserEmail())
                .set(StrUtil.isNotBlank(req.getUserMobile()), User::getUserMobile, req.getUserMobile())
                .update();
        return getUserVOById(req.getUserId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeUser(Long userId) {
        Assert.notNull(userId);

        if (!validateUserId(userId)) {
            Panic.noSuchEntry(User.class, userId);
        }

        removeById(userId);
    }

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
    public boolean validateUserName(String userName) {
        Assert.notBlank(userName);
        Assert.isTrue(ValidationUtil.validateUserName(userName));

        return lambdaQuery()
                .eq(User::getUserName, userName)
                .count() > 0;
    }

    @Override
    public boolean validateUserId(Long userId) {
        Assert.notNull(userId);

        return lambdaQuery()
                .eq(User::getUserId, userId)
                .count() > 0;
    }
}
