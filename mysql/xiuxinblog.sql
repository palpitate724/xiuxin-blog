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

 Date: 08/09/2026 15:42:45
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for art
-- ----------------------------
DROP TABLE IF EXISTS `art`;
CREATE TABLE `art`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '文章id 唯一 自增',
  `artname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文章名 不为空',
  `userid` bigint NOT NULL COMMENT '作者id 外键',
  `catid` bigint NOT NULL COMMENT '分类id 外键',
  `fenmianurl` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '封面url',
  `sum` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '摘要',
  `cont` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '正文',
  `cjiantime` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `deleted` int NULL DEFAULT 0 COMMENT '数据状态 0 or 1 默认 0',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_article_user`(`userid` ASC) USING BTREE,
  INDEX `fk_article_category`(`catid` ASC) USING BTREE,
  CONSTRAINT `fk_article_category` FOREIGN KEY (`catid`) REFERENCES `cat` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_article_user` FOREIGN KEY (`userid`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文章表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of art
-- ----------------------------

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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文章标签映射表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of art_tag
-- ----------------------------

-- ----------------------------
-- Table structure for cat
-- ----------------------------
DROP TABLE IF EXISTS `cat`;
CREATE TABLE `cat`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '分类 id 唯一 自增',
  `catname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分类名 不为空',
  `cjiantime` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `deleted` int NULL DEFAULT 0 COMMENT '数据状态 0 or 1 默认0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2097215085381066755 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cat
-- ----------------------------
INSERT INTO `cat` VALUES (2097182023557529602, '测试', '2026-09-08 12:35:32', 0);
INSERT INTO `cat` VALUES (2097182329976602625, '菲律宾', '2026-09-08 12:36:45', 0);
INSERT INTO `cat` VALUES (2097182344509865986, '欧罗巴岛', '2026-09-08 12:36:49', 0);
INSERT INTO `cat` VALUES (2097182350537080834, '利比里亚', '2026-09-08 12:36:50', 0);
INSERT INTO `cat` VALUES (2097182354127405057, '海地', '2026-09-08 12:36:51', 0);
INSERT INTO `cat` VALUES (2097182357268938754, '秘鲁', '2026-09-08 12:36:52', 0);
INSERT INTO `cat` VALUES (2097182362025279490, '哥斯达黎加', '2026-09-08 12:36:53', 0);
INSERT INTO `cat` VALUES (2097182366156668929, '罗马教廷（梵蒂冈城）', '2026-09-08 12:36:54', 0);
INSERT INTO `cat` VALUES (2097182538605477889, '尼加拉瓜', '2026-09-08 12:37:35', 0);
INSERT INTO `cat` VALUES (2097182543043051521, '马里', '2026-09-08 12:37:36', 0);
INSERT INTO `cat` VALUES (2097182556888449025, '哥伦比亚', '2026-09-08 12:37:39', 0);
INSERT INTO `cat` VALUES (2097182562840166402, '叙利亚', '2026-09-08 12:37:41', 0);
INSERT INTO `cat` VALUES (2097215085381066754, '巴西', '2026-09-08 14:46:55', 0);

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色id 唯一 自增',
  `rolename` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名 描述 不为空',
  `cjiantime` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `deleted` int NULL DEFAULT 0 COMMENT '数据状态 0 or 1 默认0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (0, 'USER', '2026-09-07 20:23:32', 0);
INSERT INTO `role` VALUES (1, 'AUTHOR', '2026-09-07 20:31:19', 0);
INSERT INTO `role` VALUES (2, 'ADMIN', '2026-09-07 20:23:39', 0);
INSERT INTO `role` VALUES (3, 'SUPER_ADMIN', '2026-09-07 20:31:53', 0);

-- ----------------------------
-- Table structure for tag
-- ----------------------------
DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '标签 id 唯一 自增',
  `tagname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标签名 不为空',
  `cjiantime` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `deleted` int NULL DEFAULT 0 COMMENT '数据状态 0 or 1 默认0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2097218883898036226 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '标签表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tag
-- ----------------------------
INSERT INTO `tag` VALUES (2097218740687720449, '杏子', '2026-09-08 15:01:26', 0);
INSERT INTO `tag` VALUES (2097218804558581761, '杨桃', '2026-09-08 15:01:42', 0);
INSERT INTO `tag` VALUES (2097218835973918722, '龙眼', '2026-09-08 15:01:49', 0);
INSERT INTO `tag` VALUES (2097218839425830913, '无花果', '2026-09-08 15:01:50', 0);
INSERT INTO `tag` VALUES (2097218842252791810, '牛油果', '2026-09-08 15:01:51', 0);
INSERT INTO `tag` VALUES (2097218858711240705, '杏脯', '2026-09-08 15:01:55', 0);
INSERT INTO `tag` VALUES (2097218861970214913, '油桃', '2026-09-08 15:01:55', 0);
INSERT INTO `tag` VALUES (2097218864855896066, '葡萄', '2026-09-08 15:01:56', 0);
INSERT INTO `tag` VALUES (2097218867989041154, '豌豆苗', '2026-09-08 15:01:57', 0);
INSERT INTO `tag` VALUES (2097218871726166018, '樱桃', '2026-09-08 15:01:58', 0);
INSERT INTO `tag` VALUES (2097218874603458562, '柚子', '2026-09-08 15:01:58', 0);
INSERT INTO `tag` VALUES (2097218877803712514, '青柠', '2026-09-08 15:01:59', 0);
INSERT INTO `tag` VALUES (2097218881280790529, '番石榴', '2026-09-08 15:02:00', 0);
INSERT INTO `tag` VALUES (2097218883898036225, '荔枝', '2026-09-08 15:02:01', 0);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户id 唯一 自增',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名 不为空',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户密码 不为空 加密存储',
  `touxiangurl` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户头像url',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户邮箱',
  `cjiantime` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  `deleted` int NULL DEFAULT 0 COMMENT '数据状态 0 or 1 默认0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2097228643959463938 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (2096961799558176770, 'test', '$2a$10$X/OIJofDf/KKtInh75xxfu.9/5Q.mAlGqcte8rxWbrLx4gGaCGsH6', 'user/cca21f1d9696417199e1236411f9fdd4.png', 'vdak5x46@sina.com', '2026-09-07 22:00:27', 0);
INSERT INTO `user` VALUES (2097228643959463937, 'admin', '$2a$10$m00vuuKX7fRzcgHOff75Iu8xkbZqZ0kbYz6lM26TFIRXZPQs0E55O', 'user/009d4114edcb465b95a816635f1f92de.png', 'v9rmdm93@yahoo.com.cn', '2026-09-08 15:40:48', 0);

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
) ENGINE = InnoDB AUTO_INCREMENT = 2097228644567638018 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户角色中间表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES (2096961799621091329, 0, 2096961799558176770);
INSERT INTO `user_role` VALUES (2097228644567638017, 0, 2097228643959463937);

SET FOREIGN_KEY_CHECKS = 1;
