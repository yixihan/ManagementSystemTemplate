package com.yixihan.template.auth.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.yixihan.template.auth.annotation.HasAnyPermission;
import com.yixihan.template.auth.cache.AuthCacheService;
import com.yixihan.template.auth.constant.AuthConstant;
import com.yixihan.template.auth.enums.PermissionEnums;
import com.yixihan.template.enums.CodeTypeEnums;
import com.yixihan.template.enums.ExceptionEnums;
import com.yixihan.template.enums.AuthTypeEnums;
import com.yixihan.template.exception.AuthException;
import com.yixihan.template.exception.BizException;
import com.yixihan.template.model.user.User;
import com.yixihan.template.service.third.CodeService;
import com.yixihan.template.service.user.RoleService;
import com.yixihan.template.service.user.UserService;
import com.yixihan.template.util.*;
import com.yixihan.template.vo.req.third.CodeValidateReq;
import com.yixihan.template.vo.req.user.UserLoginReq;
import com.yixihan.template.vo.resp.user.AuthVO;
import com.yixihan.template.vo.resp.user.PermissionVO;
import com.yixihan.template.vo.resp.user.RoleVO;
import com.yixihan.template.vo.resp.user.UserVO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 认证服务
 *
 * @author yixihan
 * @date 2024-05-23 11:24
 */
@Slf4j
@Service
public class AuthService {

    @Resource
    private UserService userService;

    @Resource
    private RoleService roleService;

    @Resource
    private AuthCacheService cacheService;

    @Resource
    private CodeService codeService;

    @Resource
    private HttpServletRequest request;

    public AuthVO authentication(String token) {
        // 从 token 种获取 userId
        Long userId = JwtUtil.analysis(token, AuthConstant.USER_ID, Long.class);

        // 从数据库种获取 user
        User user = userService.getById(userId);

        // 账户不存在
        Assert.notNull(user, ExceptionEnums.ACCOUNT_NOT_FOUND);

        // token 过期
        Assert.isTrue(JwtUtil.validateDate(token), ExceptionEnums.TOKEN_EXPIRED);

        // token 错误
        Assert.isTrue(JwtUtil.validateToken(token, user.getUserPassword()), ExceptionEnums.PASSWORD_ERR);

        // 获取用户角色信息
        List<RoleVO> userRoleList = roleService.getUserRoleList(userId);

        AuthVO authInfo = new AuthVO(BeanUtil.toBean(user, UserVO.class), userRoleList, token);

        // 存储进 redis
        cacheService.put(token, authInfo);
        // 存储进 上下文
        AppContext.getInstance().setLoginUser(authInfo);

        return authInfo;
    }

    /**
     * 登录
     *
     * @param req 请求参数
     * @return {@link AuthVO}
     */
    public AuthVO login(UserLoginReq req) {
        Assert.isEnum(req.getLoginType(), AuthTypeEnums.class);
        switch (AuthTypeEnums.valueOf(req.getLoginType())) {
            case EMAIL -> {
                return loginByEmail(req);
            }
            case MOBILE -> {
                return loginByMobile(req);
            }
            case PASSWORD -> {
                return loginByPwd(req);
            }
            case TOKEN -> {
                return loginByToken(req);
            }
            default -> throw new AuthException("unknown login in type");
        }
    }

    /**
     * 登录 - 通过密码登录
     *
     * @param req 请求参数
     * @return {@link AuthVO}
     */
    private AuthVO loginByPwd(UserLoginReq req) {
        // 参数校验
        Assert.isTrue(ValidationUtil.validateUserName(req.getUserName()));
        Assert.isTrue(ValidationUtil.validatePassword(req.getPassword()));
        Assert.notBlank(req.getValidateCode());

        // 验证码校验
        codeService.validate(req.getUuid(), req.getValidateCode());

        // 获取用户信息
        User user = userService.getUserByName(req.getUserName());

        return loginComm(req, user);
    }

    /**
     * 登录 - 通过邮箱登录
     *
     * @param req 请求参数
     * @return {@link AuthVO}
     */
    private AuthVO loginByEmail(UserLoginReq req) {
        // 参数校验
        Assert.isTrue(ValidationUtil.validateEmail(req.getUserEmail()));
        Assert.notBlank(req.getValidateCode());

        // 验证码校验
        codeService.validateEmail(CodeValidateReq.builder().type(CodeTypeEnums.LOGIN.getType()).email(req.getUserEmail()).code(req.getValidateCode()).build());

        // 获取用户信息
        User user = userService.getUserByEmail(req.getUserEmail());

        return loginComm(req, user);
    }

    /**
     * 登录 - 通过手机号登录
     *
     * @param req 请求参数
     * @return {@link AuthVO}
     */
    private AuthVO loginByMobile(UserLoginReq req) {
        // 参数校验
        Assert.isTrue(ValidationUtil.validateMobile(req.getUserMobile()));
        Assert.notBlank(req.getValidateCode());

        // 验证码校验
        codeService.validateSms(CodeValidateReq.builder().type(CodeTypeEnums.LOGIN.getType()).mobile(req.getUserMobile()).code(req.getValidateCode()).build());

        // 获取用户信息
        User user = userService.getUserByMobile(req.getUserMobile());

        return loginComm(req, user);
    }

    /**
     * 登录 - 通过token登录
     *
     * @param req 请求参数
     * @return {@link AuthVO}
     */
    private AuthVO loginByToken(UserLoginReq req) {
        // 参数校验
        Assert.notBlank(req.getToken());

        // 获取用户信息
        Long userId = JwtUtil.analysis(req.getToken(), AuthConstant.USER_ID, Long.class);
        User user = userService.getById(userId);

        return loginComm(req, user);
    }

    /**
     * 登录 - 通用方法
     *
     * @param req  请求参数
     * @param user user
     * @return {@link AuthVO}
     */
    private AuthVO loginComm(UserLoginReq req, User user) {
        Assert.notNull(user, new AuthException(ExceptionEnums.ACCOUNT_NOT_FOUND));

        if (AuthTypeEnums.PASSWORD.getType().equals(req.getLoginType())) {
            // 密码方式登录, 加密用户输入的密码
            String md5Password = MD5Util.md5(req.getPassword(), user.getUserSalt());

            // 将加密后的密码存入 user
            user.setUserPassword(md5Password);
        }

        // 生成 JwtToken
        Map<String, Object> payload = new HashMap<>(16);
        payload.put(AuthConstant.USER_ID, user.getId());
        payload.put(AuthConstant.USER_NAME, user.getUserName());
        String token = JwtUtil.createJwtToken(user.getUserPassword(), payload);

        // 登录
        return authentication(token);
    }

    /**
     * <p>权限认证处理</p>
     * <li>1. 接口无 {@link HasAnyPermission} 注解: 接口无权限控制, 所有人皆可访问</li>
     * <li>2. 接口 {@link HasAnyPermission} 注解 {@code allowAnonymousUser} 设置为 {@code true}: 接口可匿名访问</li>
     * <li>3. 接口 {@link HasAnyPermission} 注解 {@code allowAnonymousUser} 设置为 {@code false}: 接口不可匿名访问, 校验 token 是否正确</li>
     * <li>4. 接口 {@link HasAnyPermission} 注解 {@code permissionCode} 为空: 接口无权限控制, 登录用户皆可访问</li>
     * <li>5. 接口 {@link HasAnyPermission} 注解 {@code permissionCode} 不为空: 接口有权限控制, 校验登录用户全新 </li>
     *
     * @param joinPoint joinPoint
     */
    public Object hasAnyAuthorityCheck(ProceedingJoinPoint joinPoint) {
        if (ObjUtil.isNull(joinPoint)) {
            return null;
        }

        try {
            HasAnyPermission permission = getHasAnyPermission(joinPoint);

            // 1. api 无该注解, 直接放行
            if (ObjUtil.isNull(permission)) {
                return joinPoint.proceed();
            }
            // 2. api 允许匿名用户访问, 直接放行
            if (permission.allowAnonymousUser()) {
                return joinPoint.proceed();
            }

            // 3. 校验 token 是否正确
            // 获取并注入HttpServletRequest
            String token = request.getHeader(AuthConstant.JWT_TOKEN);
            if (StrUtil.isBlank(token)) {
                Panic.noAuth(ExceptionEnums.TOKEN_EXPIRED);
            }
            if (ObjUtil.isNull(authentication(token))) {
                Panic.noAuth(ExceptionEnums.TOKEN_ERR);
            }

            PermissionEnums[] permissionCodes = permission.permissionCode();

            // 4. permissionCode 为空
            if (ArrayUtil.isEmpty(permissionCodes)) {
                return joinPoint.proceed();
            }

            // 5. 校验 permissionCode
            boolean permitted = hasPermissions(permissionCodes);
            if (!permitted) {
                log.error("Access Denied to {}", joinPoint.getSignature().toLongString());
                Panic.noAuth(ExceptionEnums.NO_METHOD_ROLE);
            }

            return joinPoint.proceed();
        } catch (Throwable e) {
            throw new BizException(e);
        }
    }

    /**
     * 校验是否有权限访问 api
     *
     * @param permissionCodes permission code
     */
    private boolean hasPermissions(PermissionEnums[] permissionCodes) {
        if (ArrayUtil.isEmpty(permissionCodes)) {
            return true;
        }

        // 从数据库中取出用户的 permission code, 对比是否全有
        Set<String> userPermissionSet = roleService.getUserPermissionList(AppContext.getInstance().getLoginUser().getUser().getId()).stream().map(PermissionVO::getPermissionCode).collect(Collectors.toSet());

        for (PermissionEnums permissionCode : permissionCodes) {
            if (!userPermissionSet.contains(permissionCode.getCode())) {
                return false;
            }
        }

        return true;
    }

    /**
     * 获取 api 的 {@link HasAnyPermission} 注解
     *
     * @param joinPoint joinPoint
     * @return {@link HasAnyPermission}
     * @throws NoSuchMethodException no Such Method
     */
    private static HasAnyPermission getHasAnyPermission(JoinPoint joinPoint) throws NoSuchMethodException {
        Signature signature = joinPoint.getSignature();
        Method method = ((MethodSignature) signature).getMethod();
        Class<?> targetClass = joinPoint.getTarget().getClass();
        Method targetMethod = targetClass.getDeclaredMethod(signature.getName(), method.getParameterTypes());
        return targetMethod.getAnnotation(HasAnyPermission.class);
    }
}
