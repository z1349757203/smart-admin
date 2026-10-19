CREATE TABLE t_adv_bc (
  id bigint NOT NULL AUTO_INCREMENT COMMENT '自增id',
  department_id bigint(20) NOT NULL COMMENT '部门id',
  enterprise_id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '企业ID',
  platform_id bigint NOT NULL DEFAULT '0' COMMENT '厂商平台ID',
  bc_id varchar(32) NOT NULL COMMENT '商务中心ID',
  bc_name varchar(50) NOT NULL COMMENT '商务中心名称',
  company varchar(50) NOT NULL COMMENT '商务中心公司名称',
  currency varchar(50) DEFAULT NULL COMMENT '商务中心支付币种',
  registered_area varchar(50) DEFAULT NULL COMMENT '商务中心注册地代码',
  timezone varchar(50) DEFAULT NULL COMMENT '商务中心所在时区',
  type varchar(50) DEFAULT NULL COMMENT '商务中心类型（NORMAL普通商务中心 DIRECT非自助直客商务中心 AGENCY非自助代理商商务中心 SELF_SERVICE自助直客商务中心 SELF_SERVICE_AGENCY自助代理商商务中心）',
  bc_status VARCHAR(30) NOT NULL COMMENT '商务中心状态(REVIEWING审核中，DENY已拒绝，ENABLE已通过，PUNISH已禁用)'
  deleted_flag tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除状态',
  create_by bigint NOT NULL COMMENT '创建人ID',
  create_name varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '创建人名称',
  create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  last_modify_by bigint DEFAULT NULL COMMENT '创建人ID',
  last_modify_name varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '修改人',
  last_modify_time datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (id) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1707641509819514883 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='商务中心BC表';

CREATE TABLE t_adv_account (
  id bigint NOT NULL AUTO_INCREMENT COMMENT '自增id',
  department_id bigint(20) NOT NULL COMMENT '部门id',
  enterprise_id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '企业ID',
  platform_id bigint NOT NULL DEFAULT '0' COMMENT '厂商平台ID',
  bc_id varchar(32) DEFAULT NULL COMMENT '商务中心ID',
  deleted_flag tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除状态',
  create_by bigint NOT NULL COMMENT '创建人ID',
  create_name varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '创建人名称',
  create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  last_modify_by bigint DEFAULT NULL COMMENT '创建人ID',
  last_modify_name varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '修改人',
  last_modify_time datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (id) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1707641509819514883 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='广告账户表';


