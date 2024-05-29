package com.yixihan.template.service.user.impl;

import com.yixihan.template.model.user.User;
import com.yixihan.template.mapper.user.UserMapper;
import com.yixihan.template.service.user.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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

}
