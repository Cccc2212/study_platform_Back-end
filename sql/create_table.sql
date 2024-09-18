-- auto-generated definition
create table user
(
    id           bigint auto_increment
        primary key,
    username     varchar(256)                       null comment '用户昵称',
    userAccount  varchar(256)                       null comment '账号',
    avatarUrl    varchar(1024)                      null comment '用户头像',
    gender       tinyint                            null comment '性别',
    userPassword varchar(512)                       not null comment '密码',
    email        varchar(512)                       null comment '邮箱',
    userStatus   int      default 0                 null comment '状态 0-正常',
    phone        varchar(128)                       null comment '电话',
    createTime   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint  default 0                 not null comment '是否删除',
    userRole     int      default 0                 not null comment '用户角色 0-普通用户 1-管理员',
    platformCode varchar(512)                       null comment '平台编号'
)
    comment '用户表';

-- auto-generated definition
create table nav
(
    id     bigint auto_increment
        primary key,
    label  varchar(255) not null comment '标签名',
    navKey varchar(100) not null comment 'key值',
    url    varchar(255) null comment '网址'
)
    comment '导航表';


