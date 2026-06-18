set names utf8mb4;

-- 创建数据库
create database if not exists template_code character set utf8mb4 collate utf8mb4_general_ci;

-- 选择数据库
use template_code;

-- 删除消息表0
drop table if exists message_0;
-- 删除消息表1
drop table if exists message_1;

-- 创建消息表0
create table message_0
(
    id          bigint       not null primary key comment '主键',
    title       varchar(100) not null default '' comment '标题',
    content     varchar(500) null comment '内容',
    create_time datetime     not null default current_timestamp comment '创建时间'
) engine = InnoDB
  default charset = utf8mb4 comment ='消息分表0';

-- 创建消息表1
create table message_1
(
    id          bigint       not null primary key comment '主键',
    title       varchar(100) not null default '' comment '标题',
    content     varchar(500) null comment '内容',
    create_time datetime     not null default current_timestamp comment '创建时间'
) engine = InnoDB
  default charset = utf8mb4 comment ='消息分表1';
