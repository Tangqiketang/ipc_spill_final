/*
 Navicat Premium Data Transfer

 Source Server         : 10.0.40.91-抛洒物算法最终服务器
 Source Server Type    : MySQL
 Source Server Version : 50732
 Source Host           : 10.0.40.91:3306
 Source Schema         : test1_sharding

 Target Server Type    : MySQL
 Target Server Version : 50732
 File Encoding         : 65001

 Date: 01/02/2021 14:09:35
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
-- ----------------------------
-- Table structure for ipc_camera
-- ----------------------------
DROP TABLE IF EXISTS `ipc_camera`;
CREATE TABLE `ipc_camera`  (
                               `id` int(11) NOT NULL AUTO_INCREMENT,
                               `camera_status` tinyint(4) NULL DEFAULT NULL,
                               `camera_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                               `camera_company` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
                               `camera_model_type` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
                               `camera_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
                               `create_time` datetime(0) NULL DEFAULT NULL,
                               `update_time` datetime(0) NULL DEFAULT NULL,
                               PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ipc_camera
-- ----------------------------
INSERT INTO `ipc_camera` VALUES (2, 0, 'asfd', 'xxxx', '1', '', NULL, NULL);

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission`  (
                                   `id` int(4) NOT NULL AUTO_INCREMENT COMMENT '主键id',
                                   `parent_id` int(4) NULL DEFAULT NULL COMMENT '父id',
                                   `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单标题',
                                   `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '路径',
                                   `component` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组件',
                                   `component_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组件名字',
                                   `menu_type` int(11) NULL DEFAULT NULL COMMENT '菜单类型(0:一级菜单; 1:子菜单:2:按钮权限)',
                                   `sort_no` int(4) NULL DEFAULT NULL COMMENT '菜单排序',
                                   `icon` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单图标',
                                   `is_leaf` tinyint(1) NULL DEFAULT NULL COMMENT '是否叶子节点:    1:是   0:不是',
                                   `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
                                   `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
                                   `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                   `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
                                   `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
                                   `del_flag` int(1) NULL DEFAULT 0 COMMENT '删除状态 0正常 1已删除',
                                   `status` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '按钮权限状态(0无效1有效)',
                                   PRIMARY KEY (`id`) USING BTREE,
                                   INDEX `index_prem_pid`(`parent_id`) USING BTREE,
                                   INDEX `index_prem_is_leaf`(`is_leaf`) USING BTREE,
                                   INDEX `index_prem_sort_no`(`sort_no`) USING BTREE,
                                   INDEX `index_menu_type`(`menu_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '菜单权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO `sys_permission` VALUES (1, NULL, '用户管理', '/user/manage', '/user/manage', NULL, 0, 1, NULL, 0, NULL, NULL, '2020-11-10 13:50:03', NULL, NULL, 0, NULL);
INSERT INTO `sys_permission` VALUES (2, NULL, '终端管理', '/terminal/manage', '/terminal/manage', NULL, 0, 2, NULL, 0, NULL, NULL, '2020-11-10 13:50:03', NULL, NULL, 0, NULL);
INSERT INTO `sys_permission` VALUES (3, NULL, '升级管理', '/update/manage', '/update/manage', NULL, 0, 3, NULL, 0, NULL, NULL, '2020-11-10 13:50:03', NULL, NULL, 0, NULL);
INSERT INTO `sys_permission` VALUES (6, NULL, '系统管理', '/system/manage', '/system/manage', NULL, 0, 4, NULL, 0, NULL, NULL, '2020-11-10 13:50:03', NULL, NULL, 0, NULL);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
                             `id` int(3) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键id',
                             `role_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色名称',
                             `role_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色编码',
                             `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
                             `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
                             `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                             `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
                             `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
                             PRIMARY KEY (`id`) USING BTREE,
                             UNIQUE INDEX `uniq_sys_role_role_code`(`role_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '管理员', '#SUPERADMIN', '管理员', 'admin', '2020-11-10 10:14:26', NULL, NULL);
INSERT INTO `sys_role` VALUES (2, '普通用户', '#NORMALUSER', '普通用户', 'admin', '2020-11-10 14:22:38', NULL, NULL);
INSERT INTO `sys_role` VALUES (3, '终端', '#TERMINAL', '终端', 'admin', '2020-11-12 09:08:34', NULL, NULL);

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission`  (
                                        `id` int(4) NOT NULL AUTO_INCREMENT,
                                        `role_id` int(4) NULL DEFAULT NULL COMMENT '角色id',
                                        `permission_id` int(4) NULL DEFAULT NULL COMMENT '权限id',
                                        PRIMARY KEY (`id`) USING BTREE,
                                        INDEX `index_group_role_per_id`(`role_id`, `permission_id`) USING BTREE,
                                        INDEX `index_group_role_id`(`role_id`) USING BTREE,
                                        INDEX `index_group_per_id`(`permission_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
INSERT INTO `sys_role_permission` VALUES (1, 2, 6);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
                             `id` bigint(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键id',
                             `secid` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                             `username` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '登录账号',
                             `realname` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '真实姓名',
                             `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
                             `salt` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'md5密码盐',
                             `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像',
                             `birthday` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '生日',
                             `sex` tinyint(1) NULL DEFAULT NULL COMMENT '性别(0-默认未知,1-男,2-女)',
                             `phone` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '电话',
                             `org_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '机构编码',
                             `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '性别(1-正常,2-冻结)',
                             `del_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态(0-正常,1-已删除)',
                             `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
                             `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                             `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
                             `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
                             PRIMARY KEY (`id`) USING BTREE,
                             UNIQUE INDEX `index_user_name`(`username`) USING BTREE,
                             UNIQUE INDEX `uniq_sys_user_phone`(`phone`) USING BTREE,
                             UNIQUE INDEX `index_id`(`id`) USING BTREE,
                             INDEX `index_user_status`(`status`) USING BTREE,
                             INDEX `index_user_del_flag`(`del_flag`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, '0000000001', '15158134652', 'admin', '7ab36c94c0b108aa126349c77abde895', 'xCyDfWPUVy', NULL, NULL, NULL, '15158134652', NULL, 1, 0, 'admin', '2020-11-10 13:42:18', NULL, NULL);
INSERT INTO `sys_user` VALUES (3, '0000000003', '13000000000', NULL, '66c850020d3cdefa8076cfb9d321ce9e', 'wNidkZg0jo', NULL, NULL, NULL, '13000000000', '', 1, 0, NULL, '2020-11-10 17:24:25', NULL, NULL);
INSERT INTO `sys_user` VALUES (4, '0000000004', '13888888888', NULL, '1f54d299b9e7f3d43dd383ee5adc3b2c', '5T7Jue5d1B', NULL, NULL, NULL, '13888888888', '', 1, 0, NULL, '2020-11-11 15:39:20', NULL, NULL);
INSERT INTO `sys_user` VALUES (5, '0000000005', '13888888881', NULL, '0f59f4b7d3ef4539c355df3e12572d6e', 'aP6ggqcZNh', NULL, NULL, NULL, '13888888881', '', 1, 0, NULL, '2020-11-11 15:41:13', NULL, NULL);
INSERT INTO `sys_user` VALUES (6, '0000000006', '13888888882', NULL, '900f8997b6537efddfa3a062e59e8e92', '9UW0UrFYFr', NULL, NULL, NULL, '13888888882', '', 1, 0, NULL, '2020-11-11 15:45:24', NULL, NULL);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
                                  `id` bigint(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键id',
                                  `user_id` bigint(10) UNSIGNED NULL DEFAULT NULL COMMENT '用户id',
                                  `role_id` int(3) NULL DEFAULT NULL COMMENT '角色id',
                                  PRIMARY KEY (`id`) USING BTREE,
                                  INDEX `index2_groupuu_user_id`(`user_id`) USING BTREE,
                                  INDEX `index2_groupuu_ole_id`(`role_id`) USING BTREE,
                                  INDEX `index2_groupuu_useridandroleid`(`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1, 1);
INSERT INTO `sys_user_role` VALUES (2, 3, 3);
INSERT INTO `sys_user_role` VALUES (3, 4, 2);
INSERT INTO `sys_user_role` VALUES (4, 5, 2);
INSERT INTO `sys_user_role` VALUES (5, 6, 2);
INSERT INTO `sys_user_role` VALUES (6, 7, 2);


-- ----------------------------
-- Table structure for sys_user_wechat
-- ----------------------------
CREATE TABLE `sys_user_wechat` (
                                   `id` bigint(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
                                   `username` varchar(100) DEFAULT NULL COMMENT '手机号',
                                   `open_id` varchar(100) DEFAULT NULL COMMENT '微信openid',
                                   PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户手机号与openid映射表';


-- ----------------------------
-- Table structure for t_order
-- ----------------------------
DROP TABLE IF EXISTS `t_order`;
CREATE TABLE `t_order`  (
                            `id` bigint(20) NOT NULL,
                            `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                            `create_time` datetime(0) NULL DEFAULT NULL,
                            `user_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                            PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_order
-- ----------------------------
INSERT INTO `t_order` VALUES (1356065479446073346, 'tt', '2021-02-01 10:22:59', NULL);
INSERT INTO `t_order` VALUES (1356065531610632194, 'tt', '2021-02-01 10:23:12', NULL);

-- ----------------------------
-- Table structure for t_order_2021_01
-- ----------------------------
DROP TABLE IF EXISTS `t_order_2021_01`;
CREATE TABLE `t_order_2021_01`  (
                                    `id` bigint(20) NOT NULL,
                                    `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                                    `create_time` datetime(0) NULL DEFAULT NULL,
                                    `user_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                                    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_order_2021_01
-- ----------------------------
INSERT INTO `t_order_2021_01` VALUES (1347428216969445378, 'tt', '2021-01-08 14:21:35', NULL);

-- ----------------------------
-- Table structure for t_order_2021_02
-- ----------------------------
DROP TABLE IF EXISTS `t_order_2021_02`;
CREATE TABLE `t_order_2021_02`  (
                                    `id` bigint(20) NOT NULL,
                                    `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                                    `create_time` datetime(0) NULL DEFAULT NULL,
                                    `user_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                                    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_order_2021_02
-- ----------------------------
INSERT INTO `t_order_2021_02` VALUES (1356073499668332545, 'tt', '2021-02-01 10:54:51', NULL);

SET FOREIGN_KEY_CHECKS = 1;