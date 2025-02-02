# 数据库
create database if not exists db_security;

# 用户表
create table db_security.tb_security_user
(
    user_id     bigint       not null comment '用户ID',
    username    varchar(50)  not null comment '用户名',
    password    varchar(200) not null comment '密码',
    status      int          not null comment '状态',
    create_time datetime     not null comment '创建时间',
    create_user bigint       not null comment '创建用户',
    update_time datetime     not null comment '更新时间',
    update_user bigint       not null comment '更新用户',
    constraint tb_security_user_pk
        unique (user_id),
    constraint tb_security_user_pk_2
        unique (username)
);
insert into db_security.tb_security_user (user_id, username, password, status, create_time, create_user, update_time, update_user)
values (1000000000000000000, 'admin', '$2a$10$.iY8uNd4q3zODPaaBSVxWe7ahDyOVpIaf8hFX2DWqC7qWrZ4sRaPC', 0, '2025-01-01 00:00:00', 1000000000000000000, '2025-01-01 00:00:00', 1000000000000000000);

# 角色表
create table db_security.tb_security_role
(
    role_id     bigint      not null comment '角色ID',
    role_name   varchar(50) not null comment '角色名',
    status      int         not null comment '状态',
    create_time datetime    not null comment '创建时间',
    create_user bigint      not null comment '创建用户',
    update_time datetime    not null comment '更新时间',
    update_user bigint      not null comment '更新用户',
    constraint tb_security_role_pk
        unique (role_id),
    constraint tb_security_role_pk_2
        unique (role_name)
);
insert into db_security.tb_security_role (role_id, role_name, status, create_time, create_user, update_time, update_user)
values (2000000000000000000, 'admin', 0, '2025-01-01 00:00:00', 1000000000000000000, '2025-01-01 00:00:00', 1000000000000000000),
       (2000000000000000001, 'custom', 0, '2025-01-01 00:00:00', 1000000000000000000, '2025-01-01 00:00:00', 1000000000000000000),
       (2000000000000000002, 'merchant', 0, '2025-01-01 00:00:00', 1000000000000000000, '2025-01-01 00:00:00', 1000000000000000000);

# 权限表
create table db_security.tb_security_permission
(
    permission_id   bigint      not null comment '权限ID',
    permission_name varchar(50) not null comment '权限名',
    status          int         not null comment '状态',
    create_time     datetime    not null comment '创建时间',
    create_user     bigint      not null comment '创建用户',
    update_time     datetime    not null comment '更新时间',
    update_user     bigint      not null comment '更新用户',
    constraint tb_security_permission_pk
        unique (permission_id),
    constraint tb_security_permission_pk_2
        unique (permission_name)
);
insert into db_security.tb_security_permission (permission_id, permission_name, status, create_time, create_user, update_time, update_user)
values (3000000000000000000, 'hasAdmin', 0, '2025-01-01 00:00:00', 1000000000000000000, '2025-01-01 00:00:00', 1000000000000000000),
       (3000000000000000001, 'hasCustom', 0, '2025-01-01 00:00:00', 1000000000000000000, '2025-01-01 00:00:00', 1000000000000000000),
       (3000000000000000002, 'hasMerchant', 0, '2025-01-01 00:00:00', 1000000000000000000, '2025-01-01 00:00:00', 1000000000000000000);

# 用户角色表
create table db_security.tb_security_user_role
(
    user_role_id bigint   not null comment '用户角色表ID',
    user_id      bigint   not null comment '用户ID',
    role_id      bigint   not null comment '角色ID',
    create_time  datetime not null comment '创建时间',
    create_user  bigint   not null comment '创建用户',
    update_time  datetime not null comment '更新时间',
    update_user  bigint   not null comment '更新用户',
    constraint tb_security_user_role_pk
        unique (user_id, role_id)
);

insert into db_security.tb_security_user_role (user_role_id, user_id, role_id, create_time, create_user, update_time, update_user)
values (4000000000000000000, 1000000000000000000, 2000000000000000000, '2025-01-01 00:00:00', 1000000000000000000, '2025-01-01 00:00:00', 1000000000000000000),
       (4000000000000000001, 1000000000000000000, 2000000000000000001, '2025-01-01 00:00:00', 1000000000000000000, '2025-01-01 00:00:00', 1000000000000000000),
       (4000000000000000002, 1000000000000000000, 2000000000000000002, '2025-01-01 00:00:00', 1000000000000000000, '2025-01-01 00:00:00', 1000000000000000000);

# 角色权限表
create table db_security.tb_security_role_permission
(
    role_permission_id bigint   not null comment '角色权限表ID',
    role_id            bigint   not null comment '角色ID',
    permission_id      bigint   not null comment '权限ID',
    create_time        datetime not null comment '创建时间',
    create_user        bigint   not null comment '创建用户',
    update_time        datetime not null comment '更新时间',
    update_user        bigint   not null comment '更新用户',
    constraint tb_security_role_permission_pk
        unique (role_id, permission_id)
);
insert into db_security.tb_security_role_permission (role_permission_id, role_id, permission_id, create_time, create_user, update_time, update_user)
values (5000000000000000000, 2000000000000000000, 3000000000000000000, '2025-01-01 00:00:00', 1000000000000000000, '2025-01-01 00:00:00', 1000000000000000000),
       (5000000000000000001, 2000000000000000001, 3000000000000000001, '2025-01-01 00:00:00', 1000000000000000000, '2025-01-01 00:00:00', 1000000000000000000),
       (5000000000000000002, 2000000000000000002, 3000000000000000002, '2025-01-01 00:00:00', 1000000000000000000, '2025-01-01 00:00:00', 1000000000000000000);
