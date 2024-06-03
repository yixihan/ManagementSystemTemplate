package com.yixihan.template.auth.job;

import cn.hutool.core.util.ObjUtil;
import com.yixihan.template.auth.enums.RoleEnums;
import com.yixihan.template.enums.CommonStatusEnums;
import com.yixihan.template.job.JobInterface;
import com.yixihan.template.job.JobRunner;
import com.yixihan.template.model.user.Permission;
import com.yixihan.template.model.user.Role;
import com.yixihan.template.model.user.RolePermission;
import com.yixihan.template.service.user.PermissionService;
import com.yixihan.template.service.user.RolePermissionService;
import com.yixihan.template.service.user.RoleService;
import com.yixihan.template.vo.req.job.JobParam;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 初始化角色 任务
 *
 * @author yixihan
 * @date 2024-06-03 10:32
 */
@Slf4j
@Component
public class InitializeRoleJob implements JobInterface {

    @Resource
    private JobRunner jobRunner;

    @Resource
    private RoleService roleService;

    @Resource
    private RolePermissionService rolePermissionService;

    @Resource
    private PermissionService permissionService;

    @Override
    public String jobCode() {
        return "INITIAL_ROLE_JOB";
    }

    @Override
    public String jobName() {
        return "Initial Role Job";
    }

    @Override
    public String jobDescription() {
        return "Initial Role Job";
    }

    @Override
    public String jobSchedule() {
        return "Adhoc";
    }

    @Override
    @Scheduled(initialDelay = 10, timeUnit = TimeUnit.SECONDS, fixedDelay = Long.MAX_VALUE)
    public void execute() {
        jobRunner.runJob(this);
    }

    @Override
    public void run(JobParam param) {
        RoleEnums[] roleCodes = RoleEnums.values();
        List<Role> oldRoleList = roleService.list();
        Set<String> oldRoleCodeList = oldRoleList.stream().map(Role::getRoleCode).collect(Collectors.toSet());
        List<Role> newRoleList = new ArrayList<>(roleCodes.length);
        boolean adminFlag = false;

        for (RoleEnums code : roleCodes) {
            // role code 数据库中不存在, 则新增
            if (!validateRepetitiveCode(code, oldRoleCodeList)) {
                Role role = new Role();
                role.setRoleCode(code.getRole());
                role.setRoleName(code.getRole().charAt(0) + code.getRole().substring(1).toLowerCase());
                role.setStatus(CommonStatusEnums.VALID.getCode());
                log.info("add new role code: {}", code.getRole());
                newRoleList.add(role);
                if (RoleEnums.ADMIN.equals(code)) {
                    adminFlag = true;
                }
            }
        }
        roleService.saveBatch(newRoleList);
        if (adminFlag) {
            List<Long> permissionIdList = permissionService.list().stream().map(Permission::getId).toList();
            Role admin = newRoleList.stream().filter(o -> RoleEnums.ADMIN.getRole().equals(o.getRoleCode())).findFirst().orElse(new Role());
            if (ObjUtil.isNotNull(admin.getId())) {
                List<RolePermission> rolePermissionList = new ArrayList<>(permissionIdList.size());
                permissionIdList.forEach(permissionId -> {
                    RolePermission rolePermission = new RolePermission();
                    rolePermission.setPermissionId(permissionId);
                    rolePermission.setRoleId(admin.getId());
                    rolePermissionList.add(rolePermission);
                });
                rolePermissionService.saveBatch(rolePermissionList);
            }
        }
    }

    private boolean validateRepetitiveCode(RoleEnums code, Set<String> oldRoleCodeList) {
        return oldRoleCodeList.contains(code.getRole());
    }
}


