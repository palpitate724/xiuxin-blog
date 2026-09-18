/*
 Navicat Premium Dump SQL

 Source Server         : student
 Source Server Type    : MySQL
 Source Server Version : 80040 (8.0.40)
 Source Host           : localhost:3306
 Source Schema         : xiuxinblog

 Target Server Type    : MySQL
 Target Server Version : 80040 (8.0.40)
 File Encoding         : 65001

 Date: 18/09/2026 12:02:42
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for art
-- ----------------------------
DROP TABLE IF EXISTS `art`;
CREATE TABLE `art`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '文章id 唯一 自增',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文章名 不为空',
  `userid` bigint NOT NULL COMMENT '作者id 外键',
  `catid` bigint NOT NULL COMMENT '分类id 外键',
  `objectname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '封面url',
  `sum` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '摘要',
  `cont` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '正文',
  `cjiantime` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `deleted` int NULL DEFAULT 0 COMMENT '数据状态 0 or 1 默认 0',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_article_user`(`userid` ASC) USING BTREE,
  INDEX `fk_article_category`(`catid` ASC) USING BTREE,
  CONSTRAINT `fk_article_category` FOREIGN KEY (`catid`) REFERENCES `cat` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_article_user` FOREIGN KEY (`userid`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 2100140728355823619 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文章表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for art_tag
-- ----------------------------
DROP TABLE IF EXISTS `art_tag`;
CREATE TABLE `art_tag`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '映射id 自增',
  `artid` bigint NOT NULL COMMENT '文章id 外键',
  `tagid` bigint NOT NULL COMMENT '标签id 外键',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_arttag_article`(`artid` ASC) USING BTREE,
  INDEX `fk_arttag_tag`(`tagid` ASC) USING BTREE,
  CONSTRAINT `fk_arttag_article` FOREIGN KEY (`artid`) REFERENCES `art` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_arttag_tag` FOREIGN KEY (`tagid`) REFERENCES `tag` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 2100140728355823624 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文章标签映射表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cat
-- ----------------------------
DROP TABLE IF EXISTS `cat`;
CREATE TABLE `cat`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '分类 id 唯一 自增',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分类名 不为空',
  `cjiantime` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `deleted` int NULL DEFAULT 0 COMMENT '数据状态 0 or 1 默认0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2100086548022865922 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色id 唯一 自增',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名 描述 不为空',
  `cjiantime` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `deleted` int NULL DEFAULT 0 COMMENT '数据状态 0 or 1 默认0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tag
-- ----------------------------
DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '标签 id 唯一 自增',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标签名 不为空',
  `cjiantime` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `deleted` int NULL DEFAULT 0 COMMENT '数据状态 0 or 1 默认0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2100115593116717058 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '标签表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户id 唯一 自增',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名 不为空',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户密码 不为空 加密存储',
  `objectname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户头像url',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户邮箱',
  `cjiantime` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  `deleted` int NULL DEFAULT 0 COMMENT '数据状态 0 or 1 默认0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2099430109910614019 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '映射id 自增',
  `roleid` bigint NOT NULL DEFAULT 1 COMMENT '角色id 外键',
  `userid` bigint NOT NULL COMMENT '用户id 外键',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_userrole_role`(`roleid` ASC) USING BTREE,
  INDEX `fk_userrole_user`(`userid` ASC) USING BTREE,
  CONSTRAINT `fk_userrole_role` FOREIGN KEY (`roleid`) REFERENCES `role` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_userrole_user` FOREIGN KEY (`userid`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 2099430109981917186 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户角色中间表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
