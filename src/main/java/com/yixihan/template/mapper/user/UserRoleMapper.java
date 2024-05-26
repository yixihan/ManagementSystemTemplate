package com.yixihan.template.mapper.user;

import com.yixihan.template.model.user.UserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户-角色关联表 Mapper 接口
 * </p>
 *
 * @author yixihan
 * @since 2024-05-21
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

}
