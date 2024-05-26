package com.yixihan.template.mapper.user;

import com.yixihan.template.model.user.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 权限表 Mapper 接口
 * </p>
 *
 * @author yixihan
 * @since 2024-05-21
 */
@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {

    List<Permission> getRolePermission(@Param("roleId") Long roleId);
}
