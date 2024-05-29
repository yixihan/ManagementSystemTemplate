# create database
drop database if exists `management_template`;
create database if not exists `management_template`;

use `management_template`;

# user table
# 用户盐: 用于哈希加密密码
drop table if exists `user`;
create table if not exists `user`
(
    `id`            bigint(18) unsigned auto_increment comment '用户 id',
    `user_name`     varchar(50)      not null comment '用户名',
    `user_password` varchar(255)     not null comment '用户密码',
    `user_sale`     char(10)         not null comment '用户盐',
    `user_email`    varchar(100)     null comment '用户邮箱',
    `user_mobile`   char(11)         null comment '用户手机号',
# other filed ...
    `create_date`   datetime         not null comment '创建时间',
    `update_date`   datetime         not null comment '更新时间',
    `version`       int(11) unsigned not null default 1 comment '乐观锁',
    `del_flag`      tinyint(1)       not null default 0 comment '逻辑删除',

    primary key (`id`),
    index user_name_idx (`user_name`) using btree,
    index user_email_idx (`user_email`) using btree,
    index user_mobile_idx (`user_mobile`) using btree
) comment '用户表';

# role table
drop table if exists `role`;
create table if not exists `role`
(
    `id`          bigint(18) unsigned auto_increment comment '角色 id',
    `role_code`   varchar(50)      not null comment '角色 code',
    `role_name`   varchar(50)      not null comment '角色名',
    `status`      varchar(10)      not null default 'VALID' comment '状态[有效: VALID, 无效: INVALID]',
    `create_date` datetime         not null comment '创建时间',
    `update_date` datetime         not null comment '更新时间',
    `version`     int(11) unsigned not null default 1 comment '乐观锁',
    `del_flag`    tinyint(1)       not null default 0 comment '逻辑删除',

    primary key (`id`),
    unique index role_code_idx (`role_code`) using btree,
    index role_name_idx (`role_name`) using btree,
    index role_status_idx (`status`) using btree
) comment '角色表';

# permission table
drop table if exists `permission`;
create table if not exists `permission`
(
    `id`              bigint(18) unsigned auto_increment comment '权限 id',
    `permission_code` varchar(50)      not null comment '权限 code',
    `permission_name` varchar(50)      not null comment '权限名',
    `status`          varchar(10)      not null default 'VALID' comment '状态[有效: VALID, 无效: INVALID]',
    `create_date`     datetime         not null comment '创建时间',
    `update_date`     datetime         not null comment '更新时间',
    `version`         int(11) unsigned not null default 1 comment '乐观锁',
    `del_flag`        tinyint(1)       not null default 0 comment '逻辑删除',

    primary key (`id`),
    unique index permission_code_idx (`permission_code`) using btree,
    index permission_name_idx (`permission_name`) using btree,
    index permission_status_idx (`status`) using btree
) comment '权限表';

# user-role association table
drop table if exists `user_role`;
create table if not exists `user_role`
(
    `id`          bigint(18) unsigned auto_increment comment '权限 id',
    `user_id`     bigint(18) unsigned comment '用户 id',
    `role_id`     bigint(18) unsigned comment '角色 id',
    `create_date` datetime         not null comment '创建时间',
    `update_date` datetime         not null comment '更新时间',
    `version`     int(11) unsigned not null default 1 comment '乐观锁',
    `del_flag`    tinyint(1)       not null default 0 comment '逻辑删除',

    primary key (`id`),
    index user_id_idx (`user_id`) using btree,
    index role_id_idx (`role_id`) using btree
) comment '用户-角色关联表';

# role-permission association table
drop table if exists `role_permission`;
create table if not exists `role_permission`
(
    `id`            bigint(18) unsigned auto_increment comment '权限 id',
    `role_id`       bigint(18) unsigned comment '角色 id',
    `permission_id` bigint(18) unsigned comment '权限 id',
    `create_date`   datetime         not null comment '创建时间',
    `update_date`   datetime         not null comment '更新时间',
    `version`       int(11) unsigned not null default 1 comment '乐观锁',
    `del_flag`      tinyint(1)       not null default 0 comment '逻辑删除',

    primary key (`id`),
    index role_id_idx (`role_id`) using btree,
    index permission_id_idx (`permission_id`) using btree
) comment '角色-权限关联表';

drop table if exists job;
create table if not exists job
(
    `id`                bigint(18) unsigned auto_increment comment '任务 id',
    `job_code`          varchar(100)     not null comment '任务 code',
    `job_name`          varchar(100)     not null comment '任务 name',
    `job_desc`          varchar(100)     not null comment '任务描述',
    `job_schedule`      varchar(30)      not null comment '任务执行周期[Adhoc: 无执行周期]',
    `job_status`        varchar(10)      not null comment '任务状态[有效: VALID, 无效: INVALID]',
    `last_execute_date` datetime         null comment '任务上次执行时间',
    `create_date`       datetime         not null comment '创建时间',
    `update_date`       datetime         not null comment '更新时间',
    `version`           int(11) unsigned not null default 1 comment '乐观锁',
    `del_flag`          tinyint(1)       not null default 0 comment '逻辑删除',

    primary key (`id`),
    unique index job_code_idx (`job_code`) using btree
) comment '任务表';

drop table if exists `job_his`;
create table if not exists `job_his`
(
    `id`          bigint(18) unsigned auto_increment comment '任务执行记录 id',
    `job_id`      bigint(18) unsigned not null comment '任务 id',
    `job_code`    varchar(100)        not null comment '任务 code',
    `job_name`    varchar(100)        not null comment '任务 name',
    `job_desc`    varchar(100)        not null comment '任务描述',
    `job_status`  varchar(10)         not null comment '任务状态[PENDING: 准备中, RUNNING: 执行中, FAILED: 失败, SUCCESS: 成功]',
    `job_param`   text                null comment '任务参数',
    `start_date`  datetime            null comment '任务描述',
    `finish_date` datetime            null comment '任务描述',
    `err_msg`     varchar(500)        null comment '任务描述',
    `err_trace`   text                null comment '任务上次执行时间',
    `create_date` datetime            not null comment '创建时间',
    `update_date` datetime            not null comment '更新时间',
    `version`     int(11) unsigned    not null default 1 comment '乐观锁',
    `del_flag`    tinyint(1)          not null default 0 comment '逻辑删除',

    primary key (`id`),
    index job_id_idx (`job_id`) using btree,
    index job_status_idx (`job_status`) using btree,
    index start_date_idx (`start_date`) using btree,
    index finish_date_idx (`finish_date`) using btree
) comment '任务执行记录表';

drop table if exists `template`;
create table if not exists `template`
(
    `id`            bigint(18) unsigned auto_increment comment '任务执行记录 id',
    `template_code` varchar(10)      not null comment '模板类型[EMAIL: 邮件, SMS: sms]',
    `content`       text             null comment '模板内容',
    `create_date`   datetime         not null comment '创建时间',
    `update_date`   datetime         not null comment '更新时间',
    `version`       int(11) unsigned not null default 1 comment '乐观锁',
    `del_flag`      tinyint(1)       not null default 0 comment '逻辑删除',

    primary key (`id`),
    unique index type_idx (`template_code`) using btree
) comment '模板表';