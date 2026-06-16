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
    name                    varchar(50)          null comment '姓名',
    username                varchar(50)          null comment '用户名',
    age                     int                  null comment '年龄',
    sex                     tinyint              null comment '性别',
    password                varchar(100)         null comment '密码',
    hobbies                 json                 null comment '兴趣爱好列表',
    province                varchar(50)          null comment '省份',
    address                 varchar(255)         null comment '地址',
    city                    varchar(50)          null comment '城市',
    description             varchar(255)         null comment '描述',
    create_time             datetime             null comment '创建时间',
    update_time             datetime             null comment '更新时间',
    role                    varchar(50)          null comment '角色',
    account_non_expired     tinyint(1) default 1 null comment '账户未过期',
    account_non_locked      tinyint(1) default 1 null comment '账户未锁定',
    credentials_non_expired tinyint(1) default 1 null comment '密码未过期',
    enabled                 tinyint(1) default 1 null comment '账号启用'
) engine = InnoDB
  default charset = utf8mb4 comment ='用户表';