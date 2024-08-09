package com.yixihan.template.service.user.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yixihan.template.common.enums.CommonStatusEnums;
import com.yixihan.template.model.user.Permission;
import com.yixihan.template.mapper.user.PermissionMapper;
import com.yixihan.template.service.user.PermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yixihan.template.common.util.Assert;
import com.yixihan.template.common.util.PageUtil;
import com.yixihan.template.common.util.Panic;
import com.yixihan.template.vo.req.user.PermissionModifyReq;
import com.yixihan.template.vo.req.user.PermissionQueryReq;
import com.yixihan.template.vo.resp.base.PageVO;
import com.yixihan.template.vo.resp.user.PermissionVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Override
    public void validatePermissionId(List<Long> permissionIdList) {
        if (CollUtil.isEmpty(permissionIdList)) {
            return;
        }
        Long count = lambdaQuery()
                .in(Permission::getPermissionId, permissionIdList)
                .count();

        if (count == permissionIdList.size()) {
            return;
        }

        Set<Long> permissionIdSet = lambdaQuery()
                .select(Permission::getPermissionId)
                .in(Permission::getPermissionId, permissionIdList)
                .list()
                .stream()
                .map(Permission::getPermissionId)
                .collect(Collectors.toSet());

        for (Long id : permissionIdList) {
            if (!permissionIdSet.contains(id)) {
                Panic.noSuchEntry(id);
            }
        }
    }

    @Override
    public void validatePermissionId(Long permissionId) {
        validatePermissionId(CollUtil.toList(permissionId));
    }

    @Override
    public PermissionVO modifyPermission(PermissionModifyReq req) {
        Assert.notNull(req);
        Assert.notNull(req.getPermissionId());
        Assert.isEnum(req.getStatus(), CommonStatusEnums.class);

        Permission permission = getById(req.getPermissionId());
        if (ObjUtil.isNull(permission)) {
            Panic.noSuchEntry(req.getPermissionId());
        }

        permission.setStatus(req.getStatus());
        updateById(permission);

        return permissionToVO(permission);
    }

    @Override
    public PageVO<PermissionVO> queryPermission(PermissionQueryReq req) {
        Assert.notNull(req);

        Page<Permission> page = lambdaQuery()
                .eq(StrUtil.isNotBlank(req.getPermissionCode()), Permission::getPermissionCode, req.getPermissionCode())
                .eq(StrUtil.isNotBlank(req.getPermissionName()), Permission::getPermissionName, req.getPermissionName())
                .in(CollUtil.isNotEmpty(req.getStatus()), Permission::getStatus, req.getStatus())
                .page(PageUtil.toPage(req));

        return PageUtil.pageToPageVO(page, this::permissionToVO);
    }

    @Override
    public PermissionVO permissionDetail(Long permissionId) {
        Assert.notNull(permissionId);

        Permission permission = getById(permissionId);
        if (ObjUtil.isNull(permission)) {
            Panic.noSuchEntry(permissionId);
        }
        return permissionToVO(permission);
    }

    private PermissionVO permissionToVO(Permission permission) {
        PermissionVO vo = new PermissionVO();
        vo.setPermissionId(permission.getPermissionId());
        vo.setPermissionCode(permission.getPermissionCode());
        vo.setPermissionName(permission.getPermissionName());
        vo.setStatus(permission.getStatus());
        return vo;
    }
}
