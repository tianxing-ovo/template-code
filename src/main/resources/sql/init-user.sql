set names utf8mb4;

-- 创建数据库
create database if not exists template_code character set utf8mb4 collate utf8mb4_general_ci;

-- 选择数据库
use template_code;

-- 删除用户表
drop table if exists user;

-- 创建用户表
create table user
(
    id                      int auto_increment primary key comment '用户主键',
    name                    varchar(50)  not null default '' comment '姓名',
    username                varchar(50)  not null comment '用户名',
    age                     int          not null default 0 comment '年龄',
    sex                     tinyint      not null default 0 comment '性别 (0-未知, 1-男, 2-女)',
    password                varchar(100) not null comment '密码',
    hobbies                 json         null comment '兴趣爱好列表',
    province                varchar(50)  not null default '' comment '省份',
    address                 varchar(255) not null default '' comment '地址',
    city                    varchar(50)  not null default '' comment '城市',
    description             varchar(255) null comment '描述',
    create_time             datetime     not null default current_timestamp comment '创建时间',
    update_time             datetime     not null default current_timestamp on update current_timestamp comment '更新时间',
    role                    varchar(50)  not null default 'ROLE_USER' comment '角色',
    account_non_expired     tinyint(1)   not null default 1 comment '账户未过期',
    account_non_locked      tinyint(1)   not null default 1 comment '账户未锁定',
    credentials_non_expired tinyint(1)   not null default 1 comment '密码未过期',
    enabled                 tinyint(1)   not null default 1 comment '账号启用',
    unique key uk_username (username)
) engine = InnoDB
  default charset = utf8mb4 comment ='用户表';