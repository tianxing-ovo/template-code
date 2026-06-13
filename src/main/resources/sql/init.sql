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
    my_datetime datetime     null comment '日期时间',
    my_date     date         null comment '日期',
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
    id            int auto_increment comment '任务主键' primary key,
    user_id       int          not null comment '用户ID',
    file_name     varchar(255) not null comment '导出文件名',
    export_status int default 0 comment '导出状态'
) engine = InnoDB
  default charset = utf8mb4 comment ='数据导出任务表';