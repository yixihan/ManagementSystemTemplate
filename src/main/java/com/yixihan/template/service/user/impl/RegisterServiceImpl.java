package com.yixihan.template.service.user.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.yixihan.template.constant.NumberConstant;
import com.yixihan.template.enums.CodeTypeEnums;
import com.yixihan.template.enums.ExceptionEnums;
import com.yixihan.template.exception.BizException;
import com.yixihan.template.model.user.User;
import com.yixihan.template.model.user.UserRole;
import com.yixihan.template.service.third.CodeService;
import com.yixihan.template.service.user.RegisterService;
import com.yixihan.template.service.user.RoleService;
import com.yixihan.template.service.user.UserRoleService;
import com.yixihan.template.service.user.UserService;
import com.yixihan.template.util.Assert;
import com.yixihan.template.util.MD5Util;
import com.yixihan.template.util.ValidationUtil;
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

    @Override
    @Transactional(rollbackFor = BizException.class)
    public void registerByMobile(UserRegisterReq req) {
        Assert.isTrue(ValidationUtil.validateMobile(req.getMobile()));
        Assert.isFalse(userService.validateUserMobile(req.getMobile()));

        CodeValidateReq validateReq = new CodeValidateReq();
        validateReq.setMobile(req.getMobile());
        validateReq.setType(CodeTypeEnums.REGISTER.getType());
        validateReq.setCode(req.getCode());
        codeService.validateSms(validateReq);

        register(req);
    }

    @Override
    @Transactional(rollbackFor = BizException.class)
    public void registerByEmail(UserRegisterReq req) {
        Assert.isTrue(ValidationUtil.validateEmail(req.getEmail()));
        Assert.isFalse(userService.validateUserEmail(req.getEmail()));

        CodeValidateReq validateReq = new CodeValidateReq();
        validateReq.setMobile(req.getEmail());
        validateReq.setType(CodeTypeEnums.REGISTER.getType());
        validateReq.setCode(req.getCode());
        codeService.validateEmail(validateReq);

        register(req);
    }

    @Override
    @Transactional(rollbackFor = BizException.class)
    public void registerByPassword(UserRegisterReq req) {
        Assert.isTrue(ValidationUtil.validateEmail(req.getEmail()));
        Assert.isTrue(ValidationUtil.validateUserName(req.getPassword()));

        register(req);
    }

    /**
     * 注册用户-通用方法
     *
     * @param req 注册请求参数
     * @throws BizException 注册失败则抛出
     */
    @Transactional(rollbackFor = BizException.class)
    protected void register(UserRegisterReq req) throws BizException {
        User user = new User();
        user.setUserName(req.getUserName());
        user.setUserMobile(req.getMobile());
        user.setUserEmail(req.getEmail());

        // 生成用户名+密码
        if (StrUtil.isBlank(user.getUserName())) {
            user.setUserName(StrUtil.format("用户_{}", RandomUtil.randomNumbers(NumberConstant.NUM_5)));
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
        userRole.setUserId(user.getId());
        userRole.setRoleId(roleService.getUserRoleId());
        Assert.isTrue(userRoleService.save(userRole), ExceptionEnums.FAILED_TYPE_BUSINESS);
    }
}
