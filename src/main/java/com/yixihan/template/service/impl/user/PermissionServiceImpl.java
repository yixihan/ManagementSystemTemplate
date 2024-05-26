package com.yixihan.template.service.impl.user;

import com.yixihan.template.model.user.Permission;
import com.yixihan.template.mapper.user.PermissionMapper;
import com.yixihan.template.service.user.PermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 权限表 服务实现类
 * </p>
 *
 * @author yixihan
 * @since 2024-05-21
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

}
