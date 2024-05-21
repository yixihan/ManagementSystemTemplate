package com.yixihan.template.service.impl;

import com.yixihan.template.model.User;
import com.yixihan.template.mapper.UserMapper;
import com.yixihan.template.service.UserService;
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
