package com.yixihan.template.mapper.user;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yixihan.template.model.user.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.yixihan.template.vo.req.user.UserQueryReq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author yixihan
 * @since 2024-05-21
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    Page<User> queryUser(@Param("params") UserQueryReq params,
                         @Param("page") Page<User> page);
}
