/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50051
Source Host           : localhost:3306
Source Database       : jsj_lab_db

Target Server Type    : MYSQL
Target Server Version : 50051
File Encoding         : 65001

Date: 2018-06-29 21:50:37
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `admin`
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `username` varchar(20) NOT NULL default '',
  `password` varchar(32) default NULL,
  PRIMARY KEY  (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES ('a', 'a');

-- ----------------------------
-- Table structure for `t_classinfo`
-- ----------------------------
DROP TABLE IF EXISTS `t_classinfo`;
CREATE TABLE `t_classinfo` (
  `classNo` varchar(20) NOT NULL COMMENT 'classNo',
  `specialName` varchar(20) NOT NULL COMMENT '所属专业',
  `className` varchar(20) NOT NULL COMMENT '班级名称',
  `bornDate` varchar(20) default NULL COMMENT '成立日期',
  `mainTeacher` varchar(20) NOT NULL COMMENT '班主任',
  PRIMARY KEY  (`classNo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_classinfo
-- ----------------------------
INSERT INTO `t_classinfo` VALUES ('JSJ201801', '计算机科学与技术', '2018级计算机1班', '2018-02-01', '李明杰');
INSERT INTO `t_classinfo` VALUES ('JSJ201802', '计算机科学与技术', '2018级计算机2班', '2018-03-01', '王中军');

-- ----------------------------
-- Table structure for `t_device`
-- ----------------------------
DROP TABLE IF EXISTS `t_device`;
CREATE TABLE `t_device` (
  `deviceNo` varchar(20) NOT NULL COMMENT 'deviceNo',
  `deviceName` varchar(20) NOT NULL COMMENT '设备名称',
  `devicePhoto` varchar(60) NOT NULL COMMENT '设备图片',
  `devicePrice` float NOT NULL COMMENT '设备单价',
  `stockCount` int(11) NOT NULL COMMENT '设备数量',
  `madePlace` varchar(50) NOT NULL COMMENT '生产厂家',
  `outDate` varchar(20) default NULL COMMENT '出厂日期',
  `deviceMemo` varchar(800) default NULL COMMENT '设备备注',
  PRIMARY KEY  (`deviceNo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_device
-- ----------------------------
INSERT INTO `t_device` VALUES ('SB001', '万用表', 'upload/b226bc17-5961-4a9c-8a67-00a8a7b979d0.jpg', '33.5', '20', '成都天然电子公司', '2018-02-08', '很灵敏');
INSERT INTO `t_device` VALUES ('SB002', '定时器秒表', 'upload/c9a8c760-310f-4545-85cb-08037f9d6f99.jpg', '10', '100', '最科技有限公司', '2018-03-01', '屏幕 大字体 多功能定时器性价比');

-- ----------------------------
-- Table structure for `t_devicelend`
-- ----------------------------
DROP TABLE IF EXISTS `t_devicelend`;
CREATE TABLE `t_devicelend` (
  `deviceLendId` int(11) NOT NULL auto_increment COMMENT '设备借用id',
  `deviceObj` varchar(20) NOT NULL COMMENT '借用设备',
  `teacherObj` varchar(20) NOT NULL COMMENT '借用的老师',
  `lendUse` varchar(60) NOT NULL COMMENT '借用用途',
  `lendTime` varchar(20) default NULL COMMENT '借用时间',
  `returnTime` varchar(20) default NULL COMMENT '归还时间',
  PRIMARY KEY  (`deviceLendId`),
  KEY `deviceObj` (`deviceObj`),
  KEY `teacherObj` (`teacherObj`),
  CONSTRAINT `t_devicelend_ibfk_1` FOREIGN KEY (`deviceObj`) REFERENCES `t_device` (`deviceNo`),
  CONSTRAINT `t_devicelend_ibfk_2` FOREIGN KEY (`teacherObj`) REFERENCES `t_teacher` (`teacherNo`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_devicelend
-- ----------------------------
INSERT INTO `t_devicelend` VALUES ('1', 'SB001', 'TH001', '测量电压电容', '2018-02-21 15:31:19', '2018-02-24 15:31:27');
INSERT INTO `t_devicelend` VALUES ('2', 'SB001', 'TH002', '测量电压电容', '2018-03-01 16:26:47', '--');
INSERT INTO `t_devicelend` VALUES ('3', 'SB002', 'TH001', '做加速度实验', '2018-03-05 10:41:50', '--');

-- ----------------------------
-- Table structure for `t_experiment`
-- ----------------------------
DROP TABLE IF EXISTS `t_experiment`;
CREATE TABLE `t_experiment` (
  `experimentId` int(11) NOT NULL auto_increment COMMENT '实验项目id',
  `experimentName` varchar(20) NOT NULL COMMENT '实验项目名称',
  `classObj` varchar(20) NOT NULL COMMENT '上课班级',
  `labObj` varchar(20) NOT NULL COMMENT '上课实验室',
  `experimentContent` varchar(8000) NOT NULL COMMENT '实验内容',
  `experimentDate` varchar(20) default NULL COMMENT '实验日期',
  `experimentTime` varchar(20) NOT NULL COMMENT '实验时间',
  PRIMARY KEY  (`experimentId`),
  KEY `classObj` (`classObj`),
  KEY `labObj` (`labObj`),
  CONSTRAINT `t_experiment_ibfk_1` FOREIGN KEY (`classObj`) REFERENCES `t_classinfo` (`classNo`),
  CONSTRAINT `t_experiment_ibfk_2` FOREIGN KEY (`labObj`) REFERENCES `t_labinfo` (`labNumber`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_experiment
-- ----------------------------
INSERT INTO `t_experiment` VALUES ('1', '示波器实验', 'JSJ201801', 'SYS001', '<p>学习掌握示波器的使用</p>', '2018-02-28', '下午3点-4点');
INSERT INTO `t_experiment` VALUES ('2', '加速度实验', 'JSJ201801', 'SYS002', '<p>用秒表测试地球重力加速度！<br/></p>', '2018-03-07', '下午3点半');

-- ----------------------------
-- Table structure for `t_labclass`
-- ----------------------------
DROP TABLE IF EXISTS `t_labclass`;
CREATE TABLE `t_labclass` (
  `classId` int(11) NOT NULL auto_increment COMMENT '类别编号',
  `className` varchar(20) NOT NULL COMMENT '类别名称',
  PRIMARY KEY  (`classId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_labclass
-- ----------------------------
INSERT INTO `t_labclass` VALUES ('1', '电子实验室');
INSERT INTO `t_labclass` VALUES ('2', '光学实验室');

-- ----------------------------
-- Table structure for `t_labinfo`
-- ----------------------------
DROP TABLE IF EXISTS `t_labinfo`;
CREATE TABLE `t_labinfo` (
  `labNumber` varchar(20) NOT NULL COMMENT 'labNumber',
  `labName` varchar(20) NOT NULL COMMENT '实验室名称',
  `labClassObj` int(11) NOT NULL COMMENT '实验室类别',
  `labArea` float NOT NULL COMMENT '实验室面积',
  `labPhoto` varchar(60) NOT NULL COMMENT '实验室图片',
  `labAddress` varchar(20) NOT NULL COMMENT '实验室地址',
  `labState` varchar(20) NOT NULL COMMENT '实验室状态',
  `labDesc` varchar(5000) NOT NULL COMMENT '实验室介绍',
  PRIMARY KEY  (`labNumber`),
  KEY `labClassObj` (`labClassObj`),
  CONSTRAINT `t_labinfo_ibfk_1` FOREIGN KEY (`labClassObj`) REFERENCES `t_labclass` (`classId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_labinfo
-- ----------------------------
INSERT INTO `t_labinfo` VALUES ('SYS001', '电子实验室1', '1', '90', 'upload/595cb8f0-426b-4b1e-a2ba-638dd7feba20.jpg', '6教学楼A栋2楼', '空闲中', '<p>这里可以做各种电子实验！</p>');
INSERT INTO `t_labinfo` VALUES ('SYS002', '电子实验室2', '1', '100', 'upload/85802bc3-99f4-4d91-a453-4bf3d9828ca2.jpg', '7教学楼B栋2楼', '空闲中', '<p>电子实验室2</p><p>电子实验室2</p>');

-- ----------------------------
-- Table structure for `t_laborder`
-- ----------------------------
DROP TABLE IF EXISTS `t_laborder`;
CREATE TABLE `t_laborder` (
  `orderId` int(11) NOT NULL auto_increment COMMENT '预约编号',
  `labObj` varchar(20) NOT NULL COMMENT '预约实验室',
  `teacherObj` varchar(20) NOT NULL COMMENT '预约的老师',
  `orderDate` varchar(20) default NULL COMMENT '预约日期',
  `orderTime` varchar(20) NOT NULL COMMENT '预约时间',
  `purpose` varchar(60) NOT NULL COMMENT '预约用途',
  `shenHeState` varchar(20) NOT NULL COMMENT '审核状态',
  `reply` varchar(800) default NULL COMMENT '审核回复',
  PRIMARY KEY  (`orderId`),
  KEY `labObj` (`labObj`),
  KEY `teacherObj` (`teacherObj`),
  CONSTRAINT `t_laborder_ibfk_1` FOREIGN KEY (`labObj`) REFERENCES `t_labinfo` (`labNumber`),
  CONSTRAINT `t_laborder_ibfk_2` FOREIGN KEY (`teacherObj`) REFERENCES `t_teacher` (`teacherNo`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_laborder
-- ----------------------------
INSERT INTO `t_laborder` VALUES ('1', 'SYS001', 'TH001', '2018-02-28', '下午3点到4点', '做示波器实验', '审核通过', '准许');
INSERT INTO `t_laborder` VALUES ('2', 'SYS001', 'TH001', '2018-03-06', '下午2点到3点', '做电阻实验', '待审核', '--');
INSERT INTO `t_laborder` VALUES ('3', 'SYS002', 'TH001', '2018-03-07', '下午3点半', '做加速度实验', '审核通过', '可以做实验');

-- ----------------------------
-- Table structure for `t_notice`
-- ----------------------------
DROP TABLE IF EXISTS `t_notice`;
CREATE TABLE `t_notice` (
  `noticeId` int(11) NOT NULL auto_increment COMMENT '公告id',
  `title` varchar(80) NOT NULL COMMENT '标题',
  `content` varchar(5000) NOT NULL COMMENT '公告内容',
  `publishDate` varchar(20) default NULL COMMENT '发布时间',
  PRIMARY KEY  (`noticeId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_notice
-- ----------------------------
INSERT INTO `t_notice` VALUES ('1', '计算机实验室网站开通了', '<p>老师可以来这里预约实验室了哈！学生可以查看你们自己班级的实验上课信息哈！</p>', '2018-02-22 15:32:25');

-- ----------------------------
-- Table structure for `t_student`
-- ----------------------------
DROP TABLE IF EXISTS `t_student`;
CREATE TABLE `t_student` (
  `studentNo` varchar(30) NOT NULL COMMENT 'studentNo',
  `password` varchar(30) NOT NULL COMMENT '登录密码',
  `classObj` varchar(20) NOT NULL COMMENT '所在班级',
  `name` varchar(20) NOT NULL COMMENT '姓名',
  `gender` varchar(4) NOT NULL COMMENT '性别',
  `birthDate` varchar(20) default NULL COMMENT '出生日期',
  `studentPhoto` varchar(60) NOT NULL COMMENT '学生照片',
  `telephone` varchar(20) NOT NULL COMMENT '联系电话',
  `email` varchar(50) NOT NULL COMMENT '邮箱',
  `address` varchar(80) default NULL COMMENT '家庭地址',
  `regTime` varchar(20) default NULL COMMENT '注册时间',
  PRIMARY KEY  (`studentNo`),
  KEY `classObj` (`classObj`),
  CONSTRAINT `t_student_ibfk_1` FOREIGN KEY (`classObj`) REFERENCES `t_classinfo` (`classNo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_student
-- ----------------------------
INSERT INTO `t_student` VALUES ('STU001', '123', 'JSJ201801', '双鱼林', '男', '2018-02-01', 'upload/10957b83-dc7d-49b1-9d20-4bba9c74d6fb.jpg', '13539842343', 'syl@163.com', '四川南充滨江路13号', '2018-02-22 15:26:21');
INSERT INTO `t_student` VALUES ('STU002', '123', 'JSJ201802', '李光书', '男', '2018-03-01', 'upload/c0641fac-5535-4c25-8b63-279a391a3f1f.jpg', '13539842343', 'guangshu@163.com', '福建建瓯', '2018-03-03 15:11:19');

-- ----------------------------
-- Table structure for `t_teacher`
-- ----------------------------
DROP TABLE IF EXISTS `t_teacher`;
CREATE TABLE `t_teacher` (
  `teacherNo` varchar(20) NOT NULL COMMENT 'teacherNo',
  `password` varchar(20) NOT NULL COMMENT '登录密码',
  `teacherName` varchar(20) NOT NULL COMMENT '教师姓名',
  `teacherSex` varchar(4) NOT NULL COMMENT '教师性别',
  `teacherPhoto` varchar(60) NOT NULL,
  `inDate` varchar(20) default NULL COMMENT '入职日期',
  `telephone` varchar(20) NOT NULL COMMENT '联系电话',
  `teacherMemo` varchar(800) default NULL COMMENT '教师备注',
  PRIMARY KEY  (`teacherNo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_teacher
-- ----------------------------
INSERT INTO `t_teacher` VALUES ('TH001', '123', '王晓静', '女', 'upload/1107e699-ac90-45d3-abf3-40dad612733a.jpg', '2018-02-07', '13948083492', '<p>电子技术老师a</p>');
INSERT INTO `t_teacher` VALUES ('TH002', '123', '李仁明', '男', 'upload/a07dada9-bdeb-4c83-9260-94c114871018.jpg', '2018-03-01', '13098949343', '<p>good老师</p>');

-- ----------------------------
-- Table structure for `t_teachtask`
-- ----------------------------
DROP TABLE IF EXISTS `t_teachtask`;
CREATE TABLE `t_teachtask` (
  `taskId` int(11) NOT NULL auto_increment COMMENT '任务id',
  `title` varchar(60) NOT NULL COMMENT '任务标题',
  `content` varchar(8000) NOT NULL COMMENT '任务内容',
  `teacherObj` varchar(20) NOT NULL COMMENT '发布的老师',
  `addTime` varchar(20) default NULL COMMENT '发布时间',
  PRIMARY KEY  (`taskId`),
  KEY `teacherObj` (`teacherObj`),
  CONSTRAINT `t_teachtask_ibfk_1` FOREIGN KEY (`teacherObj`) REFERENCES `t_teacher` (`teacherNo`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_teachtask
-- ----------------------------
INSERT INTO `t_teachtask` VALUES ('1', '完成常用电子设备使用', '<p>常见的电子设备，比如万用表，示波器，需要精通使用！</p>', 'TH001', '2018-02-22 15:32:15');
