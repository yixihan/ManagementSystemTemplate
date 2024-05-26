package com.yixihan.template.service.impl.user;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.yixihan.template.model.user.Permission;
import com.yixihan.template.mapper.user.PermissionMapper;
import com.yixihan.template.service.user.PermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yixihan.template.vo.resp.user.PermissionVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 权限表 服务实现类
 * </p>
 *
 * @author yixihan
 * @since 2024-05-21
 */
@Slf4j
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "rolePermissionCache", key = "#roleIdList")
    public Map<Long, List<PermissionVO>> getRolePermission(List<Long> roleIdList) {
        if (CollUtil.isEmpty(roleIdList)) {
            return Map.of();
        }

        Map<Long, List<PermissionVO>> permissionMap = new HashMap<>(roleIdList.size());
        for (Long roleId : roleIdList) {
            permissionMap.put(roleId, getRolePermission(roleId));
        }

        return permissionMap;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "rolePermissionCache", key = "#roleId")
    public List<PermissionVO> getRolePermission(Long roleId) {
        if (ObjUtil.isNull(roleId)) {
            return List.of();
        }
        List<Permission> permissionList = baseMapper.getRolePermission(roleId);
        return BeanUtil.copyToList(permissionList, PermissionVO.class);
    }
}
