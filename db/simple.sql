create table if not exists tb_permission
(
    id int(11) unsigned auto_increment
    primary key,
    type int(11) unsigned not null comment '权限类型；1为菜单，2为操作，3...',
    value varchar(100) not null comment '权限标识码',
    name varchar(50) not null comment '权限名称',
    created_time datetime(3) null,
    updated_time datetime(3) null,
    path varchar(100) null,
    `order` int null comment '排序',
    extra varchar(200) null comment '可以用于保存json字符串，供前端使用',
    parent_id int null,
    constraint value
    unique (value)
);

INSERT INTO tb_permission (id, type, value, name, created_time, updated_time, path, `order`, extra, parent_id) VALUES (3, 1, 'sys', '系统管理', null, null, '/sys', null, null, -1);
INSERT INTO tb_permission (id, type, value, name, created_time, updated_time, path, `order`, extra, parent_id) VALUES (5, 1, 'sys:user', '用户管理', null, null, '/sys/user', null, null, 3);
INSERT INTO tb_permission (id, type, value, name, created_time, updated_time, path, `order`, extra, parent_id) VALUES (12, 1, 'sys:permission', '权限管理', null, null, '/sys/permission', null, null, 3);
INSERT INTO tb_permission (id, type, value, name, created_time, updated_time, path, `order`, extra, parent_id) VALUES (13, 1, 'sys:role', '角色管理', null, null, '/sys/role', null, null, 3);
INSERT INTO tb_permission (id, type, value, name, created_time, updated_time, path, `order`, extra, parent_id) VALUES (16, 2, 'user:del', '删除用户', null, null, '', null, null, 5);
INSERT INTO tb_permission (id, type, value, name, created_time, updated_time, path, `order`, extra, parent_id) VALUES (17, 2, 'user:save', '新增用户', null, null, '', null, null, 5);
INSERT INTO tb_permission (id, type, value, name, created_time, updated_time, path, `order`, extra, parent_id) VALUES (18, 2, 'user:update', '更新用户', null, null, '', null, null, 5);
INSERT INTO tb_permission (id, type, value, name, created_time, updated_time, path, `order`, extra, parent_id) VALUES (21, 2, 'permission:del', '删除权限', null, null, '', null, null, 12);
INSERT INTO tb_permission (id, type, value, name, created_time, updated_time, path, `order`, extra, parent_id) VALUES (22, 2, 'permission:update', '更新权限', null, null, '', null, null, 12);
INSERT INTO tb_permission (id, type, value, name, created_time, updated_time, path, `order`, extra, parent_id) VALUES (23, 2, 'permission:save', '新增权限', null, null, '', null, null, 12);
INSERT INTO tb_permission (id, type, value, name, created_time, updated_time, path, `order`, extra, parent_id) VALUES (26, 2, 'role:save', '新增角色', null, null, null, null, null, 13);
INSERT INTO tb_permission (id, type, value, name, created_time, updated_time, path, `order`, extra, parent_id) VALUES (27, 2, 'role:del', '删除角色', null, null, null, null, null, 13);
INSERT INTO tb_permission (id, type, value, name, created_time, updated_time, path, `order`, extra, parent_id) VALUES (28, 2, 'role:update', '更新角色', null, null, null, null, null, 13);
INSERT INTO tb_permission (id, type, value, name, created_time, updated_time, path, `order`, extra, parent_id) VALUES (29, 1, 'sys:test1', '测试菜单123', null, null, '/sys/test1', null, null, 3);

create table if not exists tb_role
(
    id int(11) unsigned auto_increment
    primary key,
    name varchar(30) not null comment '角色名称',
    description varchar(100) null comment '角色描述',
    created_time datetime(3) null,
    updated_time datetime(3) null,
    constraint name
    unique (name)
);

INSERT INTO tb_role (id, name, description, created_time, updated_time) VALUES (3, '测试角色1', '123', null, null);
INSERT INTO tb_role (id, name, description, created_time, updated_time) VALUES (4, '管理员', '', null, null);

create table if not exists tb_role_permission
(
    id int auto_increment
    primary key,
    role_id int not null,
    permission_id int not null
);

INSERT INTO tb_role_permission (id, role_id, permission_id) VALUES (41, 4, 3);
INSERT INTO tb_role_permission (id, role_id, permission_id) VALUES (42, 4, 12);
INSERT INTO tb_role_permission (id, role_id, permission_id) VALUES (43, 4, 13);
INSERT INTO tb_role_permission (id, role_id, permission_id) VALUES (44, 4, 5);
INSERT INTO tb_role_permission (id, role_id, permission_id) VALUES (47, 4, 16);
INSERT INTO tb_role_permission (id, role_id, permission_id) VALUES (48, 4, 17);
INSERT INTO tb_role_permission (id, role_id, permission_id) VALUES (49, 4, 18);
INSERT INTO tb_role_permission (id, role_id, permission_id) VALUES (52, 4, 28);
INSERT INTO tb_role_permission (id, role_id, permission_id) VALUES (54, 3, 3);
INSERT INTO tb_role_permission (id, role_id, permission_id) VALUES (55, 3, 5);
INSERT INTO tb_role_permission (id, role_id, permission_id) VALUES (56, 3, 12);
INSERT INTO tb_role_permission (id, role_id, permission_id) VALUES (57, 3, 13);
INSERT INTO tb_role_permission (id, role_id, permission_id) VALUES (58, 3, 29);
INSERT INTO tb_role_permission (id, role_id, permission_id) VALUES (65, 4, 21);
INSERT INTO tb_role_permission (id, role_id, permission_id) VALUES (66, 4, 22);
INSERT INTO tb_role_permission (id, role_id, permission_id) VALUES (67, 4, 23);
INSERT INTO tb_role_permission (id, role_id, permission_id) VALUES (68, 4, 26);
INSERT INTO tb_role_permission (id, role_id, permission_id) VALUES (69, 4, 27);

create table if not exists tb_user
(
    id int(11) unsigned auto_increment
    primary key,
    username varchar(64) not null comment '用户名',
    password varchar(64) not null comment '用户密码',
    avatar varchar(255) null comment '用户头像，为一串url地址，默认为空。',
    created_time datetime(3) null,
    updated_time datetime(3) null,
    nickname varchar(64) null,
    email varchar(100) null,
    state int(1) default 1 null comment '1正常，2禁用',
    constraint username
    unique (username)
);

INSERT INTO tb_user (id, username, password, avatar, created_time, updated_time, nickname, email, state) VALUES (1, 'admin', '202cb962ac59075b964b07152d234b70', null, null, null, 'admin', 'aojiaodage@gmail.com', 1);
INSERT INTO tb_user (id, username, password, avatar, created_time, updated_time, nickname, email, state) VALUES (8, 'test1', '202cb962ac59075b964b07152d234b70', '', null, null, 'test1', 'abc@123.com', 1);

create table if not exists tb_user_role
(
    id int auto_increment
    primary key,
    user_id int not null,
    role_id int not null
);

INSERT INTO tb_user_role (id, user_id, role_id) VALUES (7, 1, 4);
INSERT INTO tb_user_role (id, user_id, role_id) VALUES (8, 8, 3);