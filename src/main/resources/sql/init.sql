-- 创建数据库
create database if not exists template_code character set utf8mb4 collate utf8mb4_general_ci;

-- 选择数据库
use template_code;

-- 创建用户表
create table if not exists user
(
    id          int auto_increment comment '用户主键' primary key,
    name        varchar(50)  null comment '姓名',
    age         int          null comment '年龄',
    sex         tinyint      null comment '性别',
    password    json         null comment '密码列表',
    province    varchar(50)  null comment '省份',
    address     varchar(255) null comment '地址',
    city        varchar(50)  null comment '城市',
    description varchar(255) null comment '描述',
    create_time datetime     null comment '创建时间',
    update_time datetime     null comment '更新时间',
    role        varchar(50)  null comment '角色'
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
    id            bigint auto_increment comment '任务主键ID' primary key,
    user_id       int          not null comment '提交任务的用户ID',
    file_name     varchar(255) not null comment '导出文件名',
    file_path     varchar(512) null comment '文件下载接口相对路径或OSS下载Url',
    file_size     bigint       null default 0 comment '文件大小',
    total_records int          null default 0 comment '导出的总数据条数',
    export_status tinyint      not null default 0 comment '导出状态',
    fail_reason   varchar(512) null comment '任务失败时的原因描述',
    create_time   datetime     not null default current_timestamp comment '任务创建时间',
    update_time   datetime     not null default current_timestamp on update current_timestamp comment '任务最后修改时间'
) engine = InnoDB
  default charset = utf8mb4 comment ='数据导出任务表';