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
    index role_code_idx (`role_code`) using btree,
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
    index permission_code_idx (`permission_code`) using btree,
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
    index user_role_user_id_idx (`user_id`) using btree,
    index user_role_role_id_idx (`role_id`) using btree
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
    index role_permission_role_id_idx (`role_id`) using btree,
    index role_permission_permission_id_idx (`permission_id`) using btree
) comment '角色-权限关联表';
