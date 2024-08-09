package com.yixihan.template.job;

import com.yixihan.template.common.enums.PermissionEnums;
import com.yixihan.template.common.enums.CommonStatusEnums;
import com.yixihan.template.model.user.Permission;
import com.yixihan.template.service.user.PermissionService;
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
 * 初始化权限 任务
 *
 * @author yixihan
 * @date 2024-05-23 14:37
 */
@Slf4j
@Component
public class InitializePermissionJob implements Job {

    @Resource
    private PermissionService service;

    @Resource
    private JobRunner jobRunner;

    @Override
    public String jobCode() {
        return "Initialize_Permission_Code";
    }

    @Override
    public String jobName() {
        return "Initialize Permission Code";
    }

    @Override
    public String jobDescription() {
        return "Initialize Permission Code";
    }

    @Override
    public String jobSchedule() {
        return "Manually";
    }

    @Override
    @Scheduled(initialDelay = 10, timeUnit = TimeUnit.SECONDS, fixedDelay = Long.MAX_VALUE)
    public void execute() {
        jobRunner.runJob(this);
    }

    @Override
    public void run(JobParam param) {
        initPermissionCode();
    }

    private void initPermissionCode() {
        PermissionEnums[] permissionCodes = PermissionEnums.values();
        List<Permission> oldPermsiionList = service.list();
        Set<String> oldPermissionCodeList = oldPermsiionList.stream().map(Permission::getPermissionCode).collect(Collectors.toSet());
        List<Permission> newPermissionList = new ArrayList<>(permissionCodes.length);

        for (PermissionEnums code : permissionCodes) {
            // permission code 数据库中不存在, 则新增
            if (!validateRepetitiveCode(code, oldPermissionCodeList)) {
                Permission permission = new Permission();
                permission.setPermissionCode(code.getCode());
                permission.setPermissionName(code.getName());
                permission.setStatus(CommonStatusEnums.VALID.name());
                newPermissionList.add(permission);
                log.info("add new permission code: {}-{}", code.getCode(), code.getName());
            }
        }
        service.saveBatch(newPermissionList);
    }

    private boolean validateRepetitiveCode(PermissionEnums code, Set<String> oldPermissionCodeList) {
        return oldPermissionCodeList.contains(code.getCode());
    }
}
