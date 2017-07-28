/*
Navicat MySQL Data Transfer

Source Server         : fcp
Source Server Version : 50616
Source Host           : localhost:3309
Source Database       : fcp

Target Server Type    : MYSQL
Target Server Version : 50616
File Encoding         : 65001

Date: 2017-07-28 09:53:05
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `tb_admindiv`
-- ----------------------------
DROP TABLE IF EXISTS `tb_admindiv`;
CREATE TABLE `tb_admindiv` (
  `f_no` varchar(50) NOT NULL DEFAULT '' COMMENT '区划编号',
  `f_name` varchar(200) DEFAULT NULL COMMENT '区划名称',
  `f_parentNo` varchar(50) DEFAULT NULL COMMENT '上级区划编号',
  `f_level` smallint(6) DEFAULT NULL COMMENT '区划级别。\r\n1：省级（省、自治区、直辖市）；\r\n2：地级（地级市、地区）；\r\n3：县级（县、县级市、市辖区）；\r\n4：乡级（乡、镇、街道、类似乡级单位）；\r\n5：村级（村民委员会、居民委员会、类似村民委员会、类似居民委员会）；',
  `f_kind` int(11) DEFAULT NULL COMMENT '区划类型。\r\n111：表示主城区；\r\n112：表示城乡结合区；\r\n121：表示镇中心区；\r\n122：表示镇乡结合区；\r\n123：表示特殊区域；\r\n210：表示乡中心区；\r\n220：表示村庄；''',
  PRIMARY KEY (`f_no`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='行政区划';

-- ----------------------------
-- Records of tb_admindiv
-- ----------------------------

-- ----------------------------
-- Table structure for `tb_alarm`
-- ----------------------------
DROP TABLE IF EXISTS `tb_alarm`;
CREATE TABLE `tb_alarm` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `f_deviceNo` varchar(50) DEFAULT NULL COMMENT '设备编号',
  `f_terminalNo` varchar(50) DEFAULT NULL COMMENT '终端编号',
  `f_isAlarm` tinyint(4) DEFAULT NULL COMMENT '是否报警',
  `f_pressure` varchar(50) DEFAULT NULL COMMENT '压力值',
  `f_time` datetime DEFAULT NULL COMMENT '发生时间',
  `f_state` tinyint(4) DEFAULT NULL,
  `f_sendTime` datetime DEFAULT NULL COMMENT '发送报警时间',
  `f_toPhone` varchar(500) DEFAULT NULL COMMENT '发送到的电话号码，多个之间以英文逗号,隔开',
  `f_deviceSignal` int(11) DEFAULT NULL COMMENT '设备信号强度',
  PRIMARY KEY (`f_id`)
) ENGINE=MyISAM AUTO_INCREMENT=40 DEFAULT CHARSET=utf8 COMMENT='报警信息记录';

-- ----------------------------
-- Records of tb_alarm
-- ----------------------------

-- ----------------------------
-- Table structure for `tb_device`
-- ----------------------------
DROP TABLE IF EXISTS `tb_device`;
CREATE TABLE `tb_device` (
  `f_no` varchar(50) NOT NULL DEFAULT '' COMMENT '设备编号',
  `f_name` varchar(200) DEFAULT NULL COMMENT '设备名称',
  `f_lastSignal` int(11) DEFAULT NULL COMMENT '最近信号强度',
  `f_lastAlarmTime` datetime DEFAULT NULL COMMENT '最近一次报警时间',
  `f_longitude` double DEFAULT NULL COMMENT '设备经度',
  `f_latitude` double DEFAULT NULL COMMENT '设备维度',
  `f_alarmPhone` varchar(1000) DEFAULT NULL COMMENT '报警电话',
  `f_address` varchar(1000) DEFAULT NULL COMMENT '设备位置',
  `f_terminalNo` varchar(50) DEFAULT NULL COMMENT '终端编号',
  `f_visible` tinyint(4) DEFAULT NULL COMMENT '是否显示。1：显示；0：不显示',
  PRIMARY KEY (`f_no`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='设备';

-- ----------------------------
-- Records of tb_device
-- ----------------------------

-- ----------------------------
-- Table structure for `tb_terminal`
-- ----------------------------
DROP TABLE IF EXISTS `tb_terminal`;
CREATE TABLE `tb_terminal` (
  `f_no` varchar(50) NOT NULL DEFAULT '' COMMENT '终端编号',
  `f_lastOnlineState` tinyint(4) DEFAULT '0' COMMENT '最近一次终端在线状态。1：在线；0：离线',
  `f_lastSignal` int(11) DEFAULT NULL COMMENT '最近一次信号强度',
  `f_lastOnlineTime` datetime DEFAULT NULL COMMENT '最近一次上线时间',
  `f_longitude` double DEFAULT NULL COMMENT '终端经度',
  `f_latitude` double DEFAULT NULL COMMENT '终端维度',
  `f_adminDivNo` varchar(50) DEFAULT NULL COMMENT '行政区划编号',
  `f_address` varchar(1000) DEFAULT NULL COMMENT '终端位置',
  `f_visible` tinyint(4) DEFAULT NULL COMMENT '是否显示。1：显示；0：不显示',
  `f_alarmPhone` varchar(1000) DEFAULT NULL COMMENT '报警电话',
  `f_name` varchar(200) DEFAULT NULL COMMENT '终端名称',
  PRIMARY KEY (`f_no`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='终端';

-- ----------------------------
-- Records of tb_terminal
-- ----------------------------

-- ----------------------------
-- Table structure for `tb_terminalonlinercd`
-- ----------------------------
DROP TABLE IF EXISTS `tb_terminalonlinercd`;
CREATE TABLE `tb_terminalonlinercd` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `f_terminalNo` varchar(50) DEFAULT NULL COMMENT '终端编号',
  `f_time` datetime DEFAULT NULL COMMENT '状态改变时间',
  `f_state` tinyint(4) DEFAULT NULL COMMENT '在线状态。1：上线；0：离线。',
  `f_terminalSignal` int(11) DEFAULT NULL COMMENT '终端信号强度',
  PRIMARY KEY (`f_id`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='终端上下线记录';

-- ----------------------------
-- Records of tb_terminalonlinercd
-- ----------------------------
