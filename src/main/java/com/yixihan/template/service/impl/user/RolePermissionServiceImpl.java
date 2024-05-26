package com.yixihan.template.service.impl.user;

import com.yixihan.template.model.user.RolePermission;
import com.yixihan.template.mapper.user.RolePermissionMapper;
import com.yixihan.template.service.user.RolePermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色-权限关联表 服务实现类
 * </p>
 *
 * @author yixihan
 * @since 2024-05-21
 */
@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission> implements RolePermissionService {

}
