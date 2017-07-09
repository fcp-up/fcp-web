/*
Navicat MySQL Data Transfer

Source Server         : fcp
Source Server Version : 50616
Source Host           : localhost:3309
Source Database       : fcp

Target Server Type    : MYSQL
Target Server Version : 50616
File Encoding         : 65001

Date: 2017-07-09 18:19:44
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `tb_alarm`
-- ----------------------------
DROP TABLE IF EXISTS `tb_alarm`;
CREATE TABLE `tb_alarm` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `f_deviceNo` varchar(50) DEFAULT NULL,
  `f_centerNo` varchar(50) DEFAULT NULL,
  `f_isAlarm` tinyint(4) DEFAULT NULL,
  `f_pressure` varchar(50) DEFAULT NULL,
  `f_crtTime` datetime DEFAULT NULL,
  `f_state` tinyint(4) DEFAULT NULL,
  `f_sendTime` datetime DEFAULT NULL,
  `f_toTelephone` varchar(500) DEFAULT '发送到的电话号码，多个之间以英文逗号,隔开',
  PRIMARY KEY (`f_id`)
) ENGINE=MyISAM AUTO_INCREMENT=20 DEFAULT CHARSET=utf8 COMMENT='报警信息记录';

-- ----------------------------
-- Records of tb_alarm
-- ----------------------------
INSERT INTO `tb_alarm` VALUES ('1', '1', null, '1', '123', '2017-07-02 23:21:38', null, null, '发送到的电话号码，多个之间以英文逗号,隔开');
INSERT INTO `tb_alarm` VALUES ('2', '1', null, '1', '123', '2017-07-02 23:22:01', null, null, '发送到的电话号码，多个之间以英文逗号,隔开');
INSERT INTO `tb_alarm` VALUES ('3', '1', null, '1', '123', '2017-07-03 01:10:19', null, null, '发送到的电话号码，多个之间以英文逗号,隔开');
INSERT INTO `tb_alarm` VALUES ('4', '1', null, '1', '123', '2017-07-03 01:12:05', null, null, '发送到的电话号码，多个之间以英文逗号,隔开');
INSERT INTO `tb_alarm` VALUES ('5', '1', null, '1', '123', '2017-07-03 01:21:58', null, null, '发送到的电话号码，多个之间以英文逗号,隔开');
INSERT INTO `tb_alarm` VALUES ('6', '123', null, '1', '232', '2017-07-08 12:30:10', null, null, '发送到的电话号码，多个之间以英文逗号,隔开');
INSERT INTO `tb_alarm` VALUES ('7', '225', null, '1', '8', '2017-07-08 14:31:59', null, null, '发送到的电话号码，多个之间以英文逗号,隔开');
INSERT INTO `tb_alarm` VALUES ('8', null, null, '1', '222', '2017-07-08 16:41:38', null, null, '发送到的电话号码，多个之间以英文逗号,隔开');
INSERT INTO `tb_alarm` VALUES ('9', '232', null, '1', '12421', '2017-07-08 16:43:19', null, null, '发送到的电话号码，多个之间以英文逗号,隔开');
INSERT INTO `tb_alarm` VALUES ('10', '225', null, '1', '8', '2017-07-08 16:46:13', null, null, '发送到的电话号码，多个之间以英文逗号,隔开');
INSERT INTO `tb_alarm` VALUES ('11', '209', null, '1', '8', '2017-07-08 16:46:52', null, null, '发送到的电话号码，多个之间以英文逗号,隔开');
INSERT INTO `tb_alarm` VALUES ('12', '209', null, '1', '8', '2017-07-08 16:49:56', null, null, '发送到的电话号码，多个之间以英文逗号,隔开');
INSERT INTO `tb_alarm` VALUES ('13', '209', null, '1', '8', '2017-07-08 16:52:54', null, null, '发送到的电话号码，多个之间以英文逗号,隔开');
INSERT INTO `tb_alarm` VALUES ('14', '161', null, '1', '8', '2017-07-08 16:53:31', null, null, '发送到的电话号码，多个之间以英文逗号,隔开');
INSERT INTO `tb_alarm` VALUES ('15', '177', null, '1', '8', '2017-07-08 16:55:31', null, null, '发送到的电话号码，多个之间以英文逗号,隔开');
INSERT INTO `tb_alarm` VALUES ('16', '176', null, '1', '8', '2017-07-08 16:56:32', null, null, '发送到的电话号码，多个之间以英文逗号,隔开');
INSERT INTO `tb_alarm` VALUES ('17', '119', null, '1', '8', '2017-07-08 17:04:40', null, null, '发送到的电话号码，多个之间以英文逗号,隔开');
INSERT INTO `tb_alarm` VALUES ('18', '119', null, '1', '8', '2017-07-08 17:07:17', null, null, '发送到的电话号码，多个之间以英文逗号,隔开');
INSERT INTO `tb_alarm` VALUES ('19', '118', null, '1', '8', '2017-07-08 17:10:39', null, null, '发送到的电话号码，多个之间以英文逗号,隔开');

-- ----------------------------
-- Table structure for `tb_device`
-- ----------------------------
DROP TABLE IF EXISTS `tb_device`;
CREATE TABLE `tb_device` (
  `f_no` varchar(50) NOT NULL DEFAULT '',
  PRIMARY KEY (`f_no`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='设备';

-- ----------------------------
-- Records of tb_device
-- ----------------------------

-- ----------------------------
-- Table structure for `tb_phone`
-- ----------------------------
DROP TABLE IF EXISTS `tb_phone`;
CREATE TABLE `tb_phone` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `f_phoneNo` varchar(50) NOT NULL DEFAULT '',
  PRIMARY KEY (`f_id`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='电话号码';

-- ----------------------------
-- Records of tb_phone
-- ----------------------------
INSERT INTO `tb_phone` VALUES ('4', '13888041700');
INSERT INTO `tb_phone` VALUES ('3', '15812098130');

-- ----------------------------
-- Table structure for `tb_terminal`
-- ----------------------------
DROP TABLE IF EXISTS `tb_terminal`;
CREATE TABLE `tb_terminal` (
  `f_no` varchar(50) NOT NULL DEFAULT '',
  `f_onlineState` tinyint(4) DEFAULT '0' COMMENT '终端在线状态。1：在线；0：离线',
  PRIMARY KEY (`f_no`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='终端';

-- ----------------------------
-- Records of tb_terminal
-- ----------------------------
INSERT INTO `tb_terminal` VALUES ('412', null);
INSERT INTO `tb_terminal` VALUES ('325', '0');
INSERT INTO `tb_terminal` VALUES ('3434', '1');

-- ----------------------------
-- Table structure for `tb_terminalonlinercd`
-- ----------------------------
DROP TABLE IF EXISTS `tb_terminalonlinercd`;
CREATE TABLE `tb_terminalonlinercd` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `f_terminalNo` varchar(50) DEFAULT NULL COMMENT '终端编号',
  `f_time` datetime DEFAULT NULL COMMENT '状态改变时间',
  `f_state` tinyint(4) DEFAULT NULL COMMENT '在线状态。1：上线；0：离线。',
  PRIMARY KEY (`f_id`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='终端上下线记录';

-- ----------------------------
-- Records of tb_terminalonlinercd
-- ----------------------------
INSERT INTO `tb_terminalonlinercd` VALUES ('1', '412', '2017-07-08 16:58:23', null);
INSERT INTO `tb_terminalonlinercd` VALUES ('2', '325', '2017-07-08 17:02:40', '1');
INSERT INTO `tb_terminalonlinercd` VALUES ('3', '3434', '2017-07-08 17:04:45', '1');
