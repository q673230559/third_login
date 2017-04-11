/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50615
Source Host           : localhost:3306
Source Database       : third_login

Target Server Type    : MYSQL
Target Server Version : 50615
File Encoding         : 65001

Date: 2017-04-11 14:40:37
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `account` varchar(255) DEFAULT NULL,
  `passwd` varchar(255) DEFAULT NULL,
  `thirdId` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `portraitData` varchar(0) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', '1', '1', '1', null, null);
