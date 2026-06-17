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

-- 创建临时风险表
create table if not exists temp
(
    id           int auto_increment comment '主键ID' primary key,
    service_name varchar(255)  null comment '服务名称',
    risk_type    varchar(50)   null comment '风险类型',
    person       varchar(1000) null comment '相关人员列表'
) engine = InnoDB
  default charset = utf8mb4 comment ='临时风险表';

-- 创建数据导出任务表
create table if not exists export_task
(
    id            bigint auto_increment primary key comment '任务主键ID',
    user_id       int          not null comment '提交任务的用户ID',
    file_name     varchar(255) not null comment '导出文件名',
    file_path     varchar(512) null comment '文件下载接口相对路径或OSS下载Url',
    file_size     bigint       null     default 0 comment '文件大小',
    total_records int          null     default 0 comment '导出的总数据条数',
    export_status tinyint      not null default 0 comment '导出状态',
    fail_reason   varchar(512) null comment '任务失败时的原因描述',
    create_time   datetime     not null default current_timestamp comment '任务创建时间',
    update_time   datetime     not null default current_timestamp on update current_timestamp comment '任务最后修改时间'
) engine = InnoDB
  default charset = utf8mb4 comment ='数据导出任务表';