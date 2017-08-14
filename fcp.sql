/*
Navicat MySQL Data Transfer

Source Server         : fcp
Source Server Version : 50616
Source Host           : localhost:3309
Source Database       : fcp

Target Server Type    : MYSQL
Target Server Version : 50599
File Encoding         : 65001

Date: 2017-08-14 23:39:29
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `tb_admindiv`
-- ----------------------------
DROP TABLE IF EXISTS `tb_admindiv`;
CREATE TABLE `tb_admindiv` (
`f_no`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '区划编号' ,
`f_name`  varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '区划名称' ,
`f_parentNo`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上级区划编号' ,
`f_level`  smallint(6) NULL DEFAULT NULL COMMENT '区划级别。\r\n1：省级（省、自治区、直辖市）；\r\n2：地级（地级市、地区）；\r\n3：县级（县、县级市、市辖区）；\r\n4：乡级（乡、镇、街道、类似乡级单位）；\r\n5：村级（村民委员会、居民委员会、类似村民委员会、类似居民委员会）；' ,
`f_kind`  int(11) NULL DEFAULT NULL COMMENT '区划类型。\r\n111：表示主城区；\r\n112：表示城乡结合区；\r\n121：表示镇中心区；\r\n122：表示镇乡结合区；\r\n123：表示特殊区域；\r\n210：表示乡中心区；\r\n220：表示村庄；\'' ,
PRIMARY KEY (`f_no`)
)
ENGINE=MyISAM
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='行政区划'

;

-- ----------------------------
-- Table structure for `tb_alarm`
-- ----------------------------
DROP TABLE IF EXISTS `tb_alarm`;
CREATE TABLE `tb_alarm` (
`f_id`  bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '记录ID' ,
`f_deviceNo`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备编号' ,
`f_terminalNo`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '终端编号' ,
`f_isAlarm`  tinyint(4) NULL DEFAULT NULL COMMENT '是否报警' ,
`f_pressure`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '压力值' ,
`f_time`  datetime NULL DEFAULT NULL COMMENT '发生时间' ,
`f_state`  tinyint(4) NULL DEFAULT NULL ,
`f_sendTime`  datetime NULL DEFAULT NULL COMMENT '发送报警时间' ,
`f_toPhone`  varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发送到的电话号码，多个之间以英文逗号,隔开' ,
`f_deviceSignal`  int(11) NULL DEFAULT NULL COMMENT '设备信号强度' ,
PRIMARY KEY (`f_id`)
)
ENGINE=MyISAM
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='报警信息记录'
AUTO_INCREMENT=2929

;

-- ----------------------------
-- Table structure for `tb_device`
-- ----------------------------
DROP TABLE IF EXISTS `tb_device`;
CREATE TABLE `tb_device` (
`f_no`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '设备编号' ,
`f_terminalNo`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '终端编号' ,
`f_name`  varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备名称' ,
`f_lastSignal`  int(11) NULL DEFAULT NULL COMMENT '最近信号强度' ,
`f_lastAlarmTime`  datetime NULL DEFAULT NULL COMMENT '最近一次报警时间' ,
`f_lastPressure`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最近电池压力' ,
`f_lastIsAlarm`  tinyint(4) NULL DEFAULT NULL COMMENT '最近一次消息是否报警' ,
`f_longitude`  double NULL DEFAULT NULL COMMENT '设备经度' ,
`f_latitude`  double NULL DEFAULT NULL COMMENT '设备维度' ,
`f_alarmPhone`  varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '报警电话' ,
`f_address`  varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备位置' ,
`f_visible`  tinyint(4) NULL DEFAULT NULL COMMENT '是否显示。1：显示；0：不显示' ,
PRIMARY KEY (`f_no`, `f_terminalNo`)
)
ENGINE=MyISAM
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='设备'

;

-- ----------------------------
-- Table structure for `tb_terminal`
-- ----------------------------
DROP TABLE IF EXISTS `tb_terminal`;
CREATE TABLE `tb_terminal` (
`f_no`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '终端编号' ,
`f_lastOnlineState`  tinyint(4) NULL DEFAULT 0 COMMENT '最近一次终端在线状态。1：在线；0：离线' ,
`f_lastSignal`  int(11) NULL DEFAULT NULL COMMENT '最近一次信号强度' ,
`f_lastOnlineTime`  datetime NULL DEFAULT NULL COMMENT '最近一次上线时间' ,
`f_longitude`  double NULL DEFAULT NULL COMMENT '终端经度' ,
`f_latitude`  double NULL DEFAULT NULL COMMENT '终端维度' ,
`f_adminDivNo`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '行政区划编号' ,
`f_address`  varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '终端位置' ,
`f_visible`  tinyint(4) NULL DEFAULT NULL COMMENT '是否显示。1：显示；0：不显示' ,
`f_alarmPhone`  varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '报警电话' ,
`f_name`  varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '终端名称' ,
PRIMARY KEY (`f_no`)
)
ENGINE=MyISAM
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='终端'

;

-- ----------------------------
-- Table structure for `tb_terminalonlinercd`
-- ----------------------------
DROP TABLE IF EXISTS `tb_terminalonlinercd`;
CREATE TABLE `tb_terminalonlinercd` (
`f_id`  bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '记录ID' ,
`f_terminalNo`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '终端编号' ,
`f_time`  datetime NULL DEFAULT NULL COMMENT '状态改变时间' ,
`f_state`  tinyint(4) NULL DEFAULT NULL COMMENT '在线状态。1：上线；0：离线。' ,
`f_terminalSignal`  int(11) NULL DEFAULT NULL COMMENT '终端信号强度' ,
PRIMARY KEY (`f_id`)
)
ENGINE=MyISAM
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='终端上下线记录'
AUTO_INCREMENT=941

;

-- ----------------------------
-- Table structure for `tb_test`
-- ----------------------------
DROP TABLE IF EXISTS `tb_test`;
CREATE TABLE `tb_test` (
`f_id`  bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT ,
`f_or`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' ,
`f_and`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`f_not`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
PRIMARY KEY (`f_id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=18

;

-- ----------------------------
-- Auto increment value for `tb_alarm`
-- ----------------------------
ALTER TABLE `tb_alarm` AUTO_INCREMENT=2929;

-- ----------------------------
-- Auto increment value for `tb_terminalonlinercd`
-- ----------------------------
ALTER TABLE `tb_terminalonlinercd` AUTO_INCREMENT=941;

-- ----------------------------
-- Auto increment value for `tb_test`
-- ----------------------------
ALTER TABLE `tb_test` AUTO_INCREMENT=18;
