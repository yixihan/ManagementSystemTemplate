package com.yixihan.template.mapper;

import com.yixihan.template.model.RolePermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 角色-权限关联表 Mapper 接口
 * </p>
 *
 * @author yixihan
 * @since 2024-05-21
 */
@Mapper
public interface RolePermissionMapper extends BaseMapper<RolePermission> {

}
