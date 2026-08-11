-- 为存量用户表增加手机号/邮箱验证码登录字段
-- 执行前请先备份数据库；如果字段或索引已存在，请跳过对应语句。

use code_mother;

alter table user
    add column phone varchar(32) null comment '手机号' after userPassword,
    add column email varchar(256) null comment '邮箱' after phone;

create unique index uk_phone on user (phone);
create unique index uk_email on user (email);
