package com.yixihan.template.service.user.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.yixihan.template.common.constant.NumberConstant;
import com.yixihan.template.common.enums.CodeTypeEnums;
import com.yixihan.template.common.enums.ExceptionEnums;
import com.yixihan.template.common.enums.AuthTypeEnums;
import com.yixihan.template.common.exception.BizException;
import com.yixihan.template.model.user.User;
import com.yixihan.template.model.user.UserRole;
import com.yixihan.template.service.third.CodeService;
import com.yixihan.template.service.user.RegisterService;
import com.yixihan.template.service.user.RoleService;
import com.yixihan.template.service.user.UserRoleService;
import com.yixihan.template.service.user.UserService;
import com.yixihan.template.common.util.Assert;
import com.yixihan.template.common.util.MD5Util;
import com.yixihan.template.common.util.ValidationUtil;
import com.yixihan.template.vo.req.third.CodeValidateReq;
import com.yixihan.template.vo.req.user.UserRegisterReq;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户注册 服务
 *
 * @author yixihan
 * @date 2024-05-29 15:49
 */
@Slf4j
@Service
public class RegisterServiceImpl implements RegisterService {

    @Resource
    private UserService userService;

    @Resource
    private UserRoleService userRoleService;

    @Resource
    private RoleService roleService;

    @Resource
    private CodeService codeService;

    /**
     * 注册用户
     *
     * @param req 请求参数
     */
    @Transactional(rollbackFor = BizException.class)
    public void register(UserRegisterReq req) {
        Assert.isEnum(req.getType(), AuthTypeEnums.class);
        switch (AuthTypeEnums.valueOf(req.getType())) {
            case PASSWORD -> registerByPassword(req);
            case MOBILE -> registerByMobile(req);
            case EMAIL -> registerByEmail(req);
            default -> throw new BizException(ExceptionEnums.PARAMS_VALID_ERR);
        }
    }

    /**
     * 注册用户 - 通过手机号
     *
     * @param req 请求参数
     */
    private void registerByMobile(UserRegisterReq req) {
        Assert.isTrue(ValidationUtil.validateMobile(req.getMobile()));
        Assert.isFalse(userService.validateUserMobile(req.getMobile()));
        Assert.notBlank(req.getCode());

        codeService.validateSms(CodeValidateReq.builder()
                .mobile(req.getMobile())
                .type(CodeTypeEnums.REGISTER.getType())
                .code(req.getCode()).build());

        registerComm(req);
    }

    /**
     * 注册用户 - 通过邮箱
     *
     * @param req 请求参数
     */
    private void registerByEmail(UserRegisterReq req) {
        Assert.isTrue(ValidationUtil.validateEmail(req.getEmail()));
        Assert.isFalse(userService.validateUserEmail(req.getEmail()));
        Assert.notBlank(req.getCode());

        codeService.validateEmail(CodeValidateReq.builder()
                .email(req.getEmail())
                .type(CodeTypeEnums.REGISTER.getType())
                .code(req.getCode()).build());

        registerComm(req);
    }

    /**
     * 注册用户 - 通过密码
     *
     * @param req 请求参数
     */
    private void registerByPassword(UserRegisterReq req) {
        if (StrUtil.isNotBlank(req.getEmail())) {
            Assert.isTrue(ValidationUtil.validateEmail(req.getEmail()));
        }
        if (StrUtil.isNotBlank(req.getMobile())) {
            Assert.isTrue(ValidationUtil.validateMobile(req.getMobile()));
        }
        Assert.isTrue(ValidationUtil.validateUserName(req.getUserName()));
        Assert.isTrue(ValidationUtil.validatePassword(req.getPassword()));

        registerComm(req);
    }

    /**
     * 注册用户 - 通用方法
     *
     * @param req 请求参数
     */
    private void registerComm(UserRegisterReq req) {
        User user = new User();
        user.setUserName(req.getUserName());
        user.setUserMobile(req.getMobile());
        user.setUserEmail(req.getEmail());

        // 生成用户名+密码
        if (StrUtil.isBlank(user.getUserName())) {
            user.setUserName(StrUtil.format("用户_{}", RandomUtil.randomNumbers(NumberConstant.NUM_10)));
        }
        user.setUserPassword(RandomUtil.randomString(NumberConstant.NUM_10));

        // 密码加密
        String salt = RandomUtil.randomString(NumberConstant.NUM_10);
        String password = MD5Util.md5(user.getUserPassword(), salt);
        user.setUserPassword(password);
        user.setUserSalt(salt);

        // 保存 user
        Assert.isTrue(userService.save(user), ExceptionEnums.FAILED_TYPE_BUSINESS);

        // 新增用户角色
        UserRole userRole = new UserRole();
        userRole.setUserId(user.getUserId());
        userRole.setRoleId(roleService.getUserRoleId());
        Assert.isTrue(userRoleService.save(userRole), ExceptionEnums.FAILED_TYPE_BUSINESS);
    }
}
