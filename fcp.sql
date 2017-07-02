/*
Navicat MySQL Data Transfer

Source Server         : fcp
Source Server Version : 50616
Source Host           : localhost:3309
Source Database       : fcp

Target Server Type    : MYSQL
Target Server Version : 50616
File Encoding         : 65001

Date: 2017-07-02 13:08:41
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `tb_alarm`
-- ----------------------------
DROP TABLE IF EXISTS `tb_alarm`;
CREATE TABLE `tb_alarm` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `f_devId` varchar(50) DEFAULT NULL,
  `f_centerNo` varchar(50) DEFAULT NULL,
  `f_isAlarm` tinyint(4) DEFAULT NULL,
  `f_pressure` varchar(50) DEFAULT NULL,
  `f_crtTime` datetime DEFAULT NULL,
  `f_status` tinyint(4) DEFAULT NULL,
  `f_sendTime` datetime DEFAULT NULL,
  PRIMARY KEY (`f_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_alarm
-- ----------------------------

-- ----------------------------
-- Table structure for `tb_phone`
-- ----------------------------
DROP TABLE IF EXISTS `tb_phone`;
CREATE TABLE `tb_phone` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `f_phoneNo` varchar(50) NOT NULL DEFAULT '',
  PRIMARY KEY (`f_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_phone
-- ----------------------------
