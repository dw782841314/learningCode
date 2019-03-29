-- 菜单
DROP TABLE jw_menu;
CREATE TABLE jw_menu (
  menu_id varchar2(50) NOT NULL,
  parent_id varchar2(50) NOT NULL,
  name varchar2(50),
  url varchar2(200),
  perms varchar2(500),
  type NUMBER(2, 0),
  icon varchar2(50),
  order_num int,
  PRIMARY KEY (menu_id)
);

-- 部门
DROP TABLE jw_dept;
CREATE TABLE jw_dept (
  dept_id varchar2(50) NOT NULL,
  parent_id varchar2(50) NOT NULL,
  name varchar2(50),
  order_num NUMBER(10, 0) NOT NULL,
  del_flag NUMBER(2, 0) DEFAULT 0 NOT NULL,
  PRIMARY KEY (dept_id)
);

-- 系统用户
DROP TABLE jw_user;
CREATE TABLE jw_user (
  user_id varchar2(50) NOT NULL,
  username varchar2(50) NOT NULL,
  password varchar2(100),
  salt varchar2(20),
  email varchar2(100),
  mobile varchar2(100),
  status NUMBER(2, 0) NOT NULL,
  dept_id varchar2(50),
  create_time timestamp,
  PRIMARY KEY (user_id)
);
CREATE UNIQUE INDEX index_username on jw_user(username);


-- 角色
DROP TABLE jw_role;
CREATE TABLE jw_role (
  role_id varchar2(50) NOT NULL,
  role_name varchar2(100),
  remark varchar2(100),
  dept_id varchar2(50) NOT NULL,
  create_time timestamp,
  PRIMARY KEY (role_id)
);

-- 用户与角色对应关系
DROP TABLE jw_user_role;
CREATE TABLE jw_user_role (
  id varchar2(50) NOT NULL,
  user_id varchar2(50) NOT NULL,
  role_id varchar2(50) NOT NULL,
  PRIMARY KEY (id)
);

-- 角色与菜单对应关系
DROP TABLE jw_role_menu;
CREATE TABLE jw_role_menu (
  id varchar2(50) NOT NULL,
  role_id varchar2(50) NOT NULL,
  menu_id varchar2(50) NOT NULL,
  PRIMARY KEY (id)
);


-- 角色与部门对应关系
DROP TABLE jw_role_dept;
CREATE TABLE jw_role_dept (
  id varchar2(50) NOT NULL,
  role_id varchar2(50) NOT NULL,
  dept_id varchar2(50) NOT NULL,
  PRIMARY KEY (id)
);

-- 系统配置信息
DROP TABLE jw_config;
CREATE TABLE jw_config (
  id varchar2(50) NOT NULL,
  param_key varchar2(50),
  param_value varchar2(4000),
  status NUMBER(2, 0) DEFAULT 1 NOT NULL,
  remark varchar2(500),
  PRIMARY KEY (id)
);
CREATE UNIQUE INDEX index_param_key on jw_config(param_key);

-- 数据字典
DROP TABLE jw_dict;
CREATE TABLE jw_dict (
  id varchar2(50) NOT NULL,
  name varchar2(100) NOT NULL,
  type varchar2(100) NOT NULL,
  code varchar2(100) NOT NULL,
  value varchar2(1000) NOT NULL,
  order_num NUMBER(10, 0) DEFAULT 0 NOT NULL,
  remark varchar2(255),
  del_flag NUMBER(2, 0) DEFAULT 0 NOT NULL,
  PRIMARY KEY (id)
);
CREATE UNIQUE INDEX index_type_code on jw_dict(type, code);

-- 系统日志
DROP TABLE jw_log;
CREATE TABLE jw_log (
  id varchar2(50) NOT NULL,
  username varchar2(50),
  operation varchar2(50),
  method varchar2(200),
  params clob,
  time NUMBER(20, 0) NOT NULL,
  ip varchar2(64),
  create_date timestamp,
  PRIMARY KEY (id)
);


-- 初始数据
INSERT INTO jw_user (user_id, username, password, salt, email, mobile, status, dept_id, create_time) VALUES ('1', 'admin', 'e1153123d7d180ceeb820d577ff119876678732a68eef4e6ffc0b1f06a01f91b', 'YzcmCZNvbXocrsz9dm8e', 'root@renren.io', '13612345678', '1', '1', CURRENT_DATE);
INSERT INTO jw_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('1', '0', '系统管理', NULL, NULL, '0', 'fa fa-cog', '0');
INSERT INTO jw_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('2', '1', '管理员管理', 'modules/sys/user.html', NULL, '1', 'fa fa-user', '1');
INSERT INTO jw_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('3', '1', '角色管理', 'modules/sys/role.html', NULL, '1', 'fa fa-user-secret', '2');
INSERT INTO jw_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('4', '1', '菜单管理', 'modules/sys/menu.html', NULL, '1', 'fa fa-th-list', '3');
INSERT INTO jw_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('5', '1', 'SQL监控', 'druid/sql.html', NULL, '1', 'fa fa-bug', '4');
-- 初始化菜单数据
INSERT INTO jw_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('6', '1', '定时任务', 'modules/job/schedule.html', NULL, '1', 'fa fa-tasks', '5');
INSERT INTO jw_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('7', '6', '查看', NULL, 'sys:schedule:list,sys:schedule:info', '2', NULL, '0');
INSERT INTO jw_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('8', '6', '新增', NULL, 'sys:schedule:save', '2', NULL, '0');
INSERT INTO jw_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('9', '6', '修改', NULL, 'sys:schedule:update', '2', NULL, '0');
INSERT INTO jw_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('10', '6', '删除', NULL, 'sys:schedule:delete', '2', NULL, '0');
INSERT INTO jw_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('11', '6', '暂停', NULL, 'sys:schedule:pause', '2', NULL, '0');
INSERT INTO jw_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('12', '6', '恢复', NULL, 'sys:schedule:resume', '2', NULL, '0');
INSERT INTO jw_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('13', '6', '立即执行', NULL, 'sys:schedule:run', '2', NULL, '0');
INSERT INTO jw_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('14', '6', '日志列表', NULL, 'sys:schedule:log', '2', NULL, '0');

INSERT INTO jw_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('15', '2', '查看', NULL, 'sys:user:list,sys:user:info', '2', NULL, '0');
INSERT INTO jw_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('16', '2', '新增', NULL, 'sys:user:save,sys:role:select', '2', NULL, '0');
INSERT INTO jw_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('17', '2', '修改', NULL, 'sys:user:update,sys:role:select', '2', NULL, '0');
INSERT INTO jw_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('18', '2', '删除', NULL, 'sys:user:delete', '2', NULL, '0');
INSERT INTO jw_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('19', '3', '查看', NULL, 'sys:role:list,sys:role:info', '2', NULL, '0');
INSERT INTO jw_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('20', '3', '新增', NULL, 'sys:role:save,sys:menu:perms', '2', NULL, '0');
INSERT INTO jw_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('21', '3', '修改', NULL, 'sys:role:update,sys:menu:perms', '2', NULL, '0');
INSERT INTO jw_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('22', '3', '删除', NULL, 'sys:role:delete', '2', NULL, '0');
INSERT INTO jw_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('23', '4', '查看', NULL, 'sys:menu:list,sys:menu:info', '2', NULL, '0');
INSERT INTO jw_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('24', '4', '新增', NULL, 'sys:menu:save,sys:menu:select', '2', NULL, '0');
INSERT INTO jw_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('25', '4', '修改', NULL, 'sys:menu:update,sys:menu:select', '2', NULL, '0');
INSERT INTO jw_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('26', '4', '删除', NULL, 'sys:menu:delete', '2', NULL, '0');
INSERT INTO jw_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('27', '1', '参数管理', 'modules/sys/config.html', 'sys:config:list,sys:config:info,sys:config:save,sys:config:update,sys:config:delete', '1', 'fa fa-sun-o', '6');
INSERT INTO jw_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('29', '1', '系统日志', 'modules/sys/log.html', 'sys:log:list', '1', 'fa fa-file-text-o', '7');
INSERT INTO jw_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('31', '1', '部门管理', 'modules/sys/dept.html', NULL, '1', 'fa fa-file-code-o', '1');
INSERT INTO jw_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('32', '31', '查看', NULL, 'sys:dept:list,sys:dept:info', '2', NULL, '0');
INSERT INTO jw_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('33', '31', '新增', NULL, 'sys:dept:save,sys:dept:select', '2', NULL, '0');
INSERT INTO jw_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('34', '31', '修改', NULL, 'sys:dept:update,sys:dept:select', '2', NULL, '0');
INSERT INTO jw_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('35', '31', '删除', NULL, 'sys:dept:delete', '2', NULL, '0');

INSERT INTO jw_menu(menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('36', '1', '字典管理', 'modules/sys/dict.html', NULL, 1, 'fa fa-bookmark-o', 6);
INSERT INTO jw_menu(menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('37', '36', '查看', NULL, 'sys:dict:list,sys:dict:info', 2, NULL, 6);
INSERT INTO jw_menu(menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('38', '36', '新增', NULL, 'sys:dict:save', 2, NULL, 6);
INSERT INTO jw_menu(menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('39', '36', '修改', NULL, 'sys:dict:update', 2, NULL, 6);
INSERT INTO jw_menu(menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('40', '36', '删除', NULL, 'sys:dict:delete', 2, NULL, 6);
INSERT INTO jw_menu (menu_id, parent_id, name, url, perms, type, icon, order_num) VALUES ('30', '1', '文件上传', 'modules/oss/oss.html', 'sys:oss:all', '1', 'fa fa-file-image-o', '6');


INSERT INTO jw_dept (dept_id, parent_id, name, order_num, del_flag) VALUES ('1', '0', '人人开源集团', '0', '0');
INSERT INTO jw_dept (dept_id, parent_id, name, order_num, del_flag) VALUES ('2', '1', '长沙分公司', '1', '0');
INSERT INTO jw_dept (dept_id, parent_id, name, order_num, del_flag) VALUES ('3', '1', '上海分公司', '2', '0');
INSERT INTO jw_dept (dept_id, parent_id, name, order_num, del_flag) VALUES ('4', '3', '技术部', '0', '0');
INSERT INTO jw_dept (dept_id, parent_id, name, order_num, del_flag) VALUES ('5', '3', '销售部', '1', '0');

INSERT INTO jw_dict(id, name, type, code, value, order_num, remark, del_flag) VALUES ('1', '性别', 'sex', '0', '女', 0, NULL, 0);
INSERT INTO jw_dict(id, name, type, code, value, order_num, remark, del_flag) VALUES ('2', '性别', 'sex', '1', '男', 1, NULL, 0);
INSERT INTO jw_dict(id, name, type, code, value, order_num, remark, del_flag) VALUES ('3', '性别', 'sex', '2', '未知', 3, NULL, 0);


