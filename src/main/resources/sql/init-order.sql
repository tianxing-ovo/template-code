set names utf8mb4;

-- 创建数据库
create database if not exists template_code character set utf8mb4 collate utf8mb4_unicode_ci;

-- 选择数据库
use template_code;

-- 删除订单表
drop table if exists orders;

-- 创建订单表
create table orders
(
    id           bigint auto_increment primary key comment '主键ID',
    order_no     varchar(50)                                                     not null comment '订单号',
    product_name varchar(100)                                                    not null comment '商品名称',
    price        decimal(10, 2)                                                  not null comment '商品单价',
    quantity     int                                                             not null comment '购买数量',
    total_amount decimal(10, 2)                                                  not null comment '订单总金额',
    status       tinyint   default 0                                             not null comment '状态',
    create_time  timestamp default current_timestamp                             not null comment '创建时间',
    update_time  timestamp default current_timestamp on update current_timestamp not null comment '更新时间'
) engine = InnoDB
  default charset = utf8mb4
  collate = utf8mb4_unicode_ci comment ='订单表';

-- 插入订单数据
insert into orders (order_no, product_name, price, quantity, total_amount, status)
values ('ORD20260614001', 'AI 编程键盘', 299.00, 1, 299.00, 1),
       ('ORD20260614002', '无线降噪耳机', 599.00, 2, 1198.00, 0),
       ('ORD20260614003', '程序员人体工学椅', 1299.00, 1, 1299.00, 2);
