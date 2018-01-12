/*
SQLyog Ultimate v11.33 (64 bit)
MySQL - 5.5.52-MariaDB : Database - bda_db
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`bda_db` /*!40100 DEFAULT CHARACTER SET utf8 */;

/*Table structure for table `SYS_AUDIT_LOG` */

DROP TABLE IF EXISTS `SYS_AUDIT_LOG`;

CREATE TABLE `SYS_AUDIT_LOG` (
  `logId` int(20) NOT NULL AUTO_INCREMENT COMMENT '序号',
  `empno` varchar(20) DEFAULT NULL COMMENT '工号',
  `audittype` varchar(20) DEFAULT NULL COMMENT '操作类型',
  `auditmodule` varchar(200) DEFAULT NULL COMMENT '操作模块名称',
  `audittable` varchar(200) DEFAULT NULL COMMENT '操作的表名称',
  `logtime` bigint(20) DEFAULT NULL COMMENT '操作时间',
  `state` int(5) DEFAULT NULL COMMENT '操作状态:1:操作成功；2：操作失败',
  `des` varchar(2000) DEFAULT NULL COMMENT '详细描述',
  `attachment` varchar(200) DEFAULT NULL COMMENT '附件地址',
  PRIMARY KEY (`logId`)
) ENGINE=InnoDB AUTO_INCREMENT=208779 DEFAULT CHARSET=utf8;

/*Data for the table `SYS_AUDIT_LOG` */

/*Table structure for table `SYS_AUTH` */

DROP TABLE IF EXISTS `SYS_AUTH`;

CREATE TABLE `SYS_AUTH` (
  `authcode` varchar(50) NOT NULL COMMENT '功能代码',
  `authname` varchar(100) NOT NULL COMMENT '功能名称',
  `authimage` varchar(50) DEFAULT NULL COMMENT '功能图标',
  `des` varchar(100) DEFAULT NULL COMMENT '备注',
  `status` char(1) DEFAULT '1' COMMENT '是否有效0:无效 1：有效',
  `createtime` varchar(20) DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`authcode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;

/*Data for the table `SYS_AUTH` */

insert  into `SYS_AUTH`(`authcode`,`authname`,`authimage`,`des`,`status`,`createtime`) values ('add','新增',NULL,'新增','1','2016-05-06 15:04:37'),('delete','删除',NULL,'删除内容','1','2015-10-10 13:23:59'),('modify','修改',NULL,'插入及修改','1','2015-10-10 13:25:03'),('resetpwd','重置密码',NULL,'重置密码','1','2015-11-05 11:38:33'),('selectrole','选择角色',NULL,'选择角色','1','2015-10-12 17:48:49'),('updateMenuAuth','更新菜单功能',NULL,'更新菜单功能','1','2017-10-09 13:56:04'),('updateRoleMenuAuth','更新角色权限',NULL,'更新角色权限','1','2017-10-09 14:17:16'),('updateUserRole','更新用户角色',NULL,'更新用户角色','1','2017-10-09 13:54:35'),('uploadFile','文件上传',NULL,'上传','1','2015-05-02 12:15:25'),('view','浏览',NULL,'查看内容','1','2015-10-10 13:23:53'),('viewRoleMenuAuth','查看角色权限',NULL,'查看角色权限','1','2017-10-09 15:42:27');

/*Table structure for table `SYS_ICON` */

DROP TABLE IF EXISTS `SYS_ICON`;

CREATE TABLE `SYS_ICON` (
  `image` varchar(100) NOT NULL DEFAULT '' COMMENT 'icon名称',
  `classid` varchar(100) DEFAULT NULL COMMENT 'css样式类',
  PRIMARY KEY (`image`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;

/*Data for the table `SYS_ICON` */

insert  into `SYS_ICON`(`image`,`classid`) values ('1.gif','icon-inner-1'),('10.gif','icon-inner-10'),('121.png','icon-inner-121'),('122.png','icon-inner-122'),('123.png','icon-inner-123'),('124.png','icon-inner-124'),('125.png','icon-inner-125'),('126.png','icon-inner-126'),('13.png','icon-inner-13'),('14.gif','icon-inner-14'),('15.gif','icon-inner-15'),('2.gif','icon-inner-2'),('20.gif','icon-inner-20'),('21.gif','icon-inner-21'),('23.gif','icon-inner-23'),('25.gif','icon-inner-25'),('27.gif','icon-inner-27'),('28.gif','icon-inner-28'),('29.gif','icon-inner-29'),('3.gif','icon-inner-3'),('30.gif','icon-inner-30'),('32.gif','icon-inner-32'),('33.gif','icon-inner-33'),('36.gif','icon-inner-36'),('37.gif','icon-inner-37'),('39.gif','icon-inner-39'),('4.gif','icon-inner-4'),('40.gif','icon-inner-40'),('42.gif','icon-inner-42'),('45.gif','icon-inner-45'),('46.gif','icon-inner-46'),('47.gif','icon-inner-47'),('49.gif','icon-inner-49'),('5.gif','icon-inner-5'),('50.gif','icon-inner-50'),('53.gif','icon-inner-53'),('55.gif','icon-inner-55'),('56.gif','icon-inner-56'),('57.gif','icon-inner-57'),('58.gif','icon-inner-58'),('59.gif','icon-inner-59'),('6.gif','icon-inner-6'),('60.gif','icon-inner-60'),('61.gif','icon-inner-61'),('62.gif','icon-inner-62'),('63.gif','icon-inner-63'),('64.gif','icon-inner-64'),('65.gif','icon-inner-65'),('66.png','icon-inner-66'),('67.gif','icon-inner-67'),('69.gif','icon-inner-69'),('7.gif','icon-inner-7'),('70.gif','icon-inner-70'),('71.gif','icon-inner-71'),('72.gif','icon-inner-72'),('74.gif','icon-inner-74'),('75.gif','icon-inner-75'),('76.gif','icon-inner-76'),('77.gif','icon-inner-77'),('78.gif','icon-inner-78'),('79.gif','icon-inner-79'),('8.gif','icon-inner-8'),('80.gif','icon-inner-80'),('81.gif','icon-inner-81'),('82.gif','icon-inner-82'),('83.gif','icon-inner-83');

/*Table structure for table `SYS_MENU` */

DROP TABLE IF EXISTS `SYS_MENU`;

CREATE TABLE `SYS_MENU` (
  `menuid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '菜单代码',
  `menuname` varchar(100) NOT NULL COMMENT '菜单名称',
  `parentid` bigint(20) DEFAULT NULL COMMENT '父菜单编号',
  `url` varchar(255) DEFAULT NULL COMMENT '菜单url',
  `relative` char(1) DEFAULT '1' COMMENT '绝对路径还是相对路径1:相对路径,2:绝对路径。',
  `image` varchar(255) DEFAULT NULL COMMENT '菜单图片',
  `visible` char(1) DEFAULT '1' COMMENT '是否可见0:不可见,1:可见',
  `tooltip` varchar(100) DEFAULT NULL COMMENT '菜单提示',
  `createtime` varchar(20) DEFAULT NULL COMMENT '更新时间',
  `operator` varchar(50) DEFAULT NULL COMMENT '操作人ID',
  `level` int(11) DEFAULT NULL COMMENT '菜单层级',
  `nodesort` int(11) DEFAULT NULL COMMENT '菜单排序',
  PRIMARY KEY (`menuid`)
) ENGINE=InnoDB AUTO_INCREMENT=1746 DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;

/*Data for the table `SYS_MENU` */

insert  into `SYS_MENU`(`menuid`,`menuname`,`parentid`,`url`,`relative`,`image`,`visible`,`tooltip`,`createtime`,`operator`,`level`,`nodesort`) values (1,'系统管理',0,'/sys','1','glyphicon glyphicon-cloud','1',NULL,'2017-09-30 10:09:21','admin',1,9),(2,'菜单管理',1,'sys/menu/sys_menu.jsp','1','glyphicon glyphicon-list','1',NULL,'2017-09-30 10:10:26','admin',2,9),(4,'权限管理',1,'sys/auth/sys_auth.jsp','1','glyphicon glyphicon-indent-right','1',NULL,'2017-10-09 12:09:44','admin',2,1),(5,'角色管理',1,'sys/role/sys_role.jsp','1','glyphicon glyphicon-user','1',NULL,'2017-09-30 10:09:42','admin',2,2),(6,'用户管理',1,'sys/user/sys_user.jsp','1','glyphicon glyphicon-user','1',NULL,'2017-09-30 10:10:10','admin',2,4),(502,'借记卡账户信息查询',501,'judicial/account/account_info.jsp','1','2.gif','1','借记卡账户信息查询','2016-03-18 15:03:45','admin',2,1),(601,'借记卡交易明细查询',501,'judicial/deal/deal_detail.jsp','1','20.gif','1','借记卡交易明细及对手查询','2016-02-24 15:13:42','admin',2,2),(1711,'信用卡账户信息查询',501,'creditCard/account/creditCardAccount.jsp','1','10.gif','1','信用卡账户信息查询','2016-02-24 15:14:06','admin',2,3),(1712,'信用卡交易流水查询',501,'creditCard/deal/creditCardDeal.jsp','1','39.gif','1','信用卡交易流水查询','2016-02-24 15:12:45','admin',2,4),(1729,'卡账对应关系查询',501,'judicial/CardAcct/cardAcctRelation.jsp','1','25.gif','1','卡账对应关系查询','2016-09-06 14:34:06','admin',2,5),(1745,'测试1',1,'sys/test/mail_compose.jsp','1','glyphicon glyphicon-asterisk','1',NULL,'2017-11-08 15:59:50','admin',2,3);

/*Table structure for table `SYS_MENU_AUTH` */

DROP TABLE IF EXISTS `SYS_MENU_AUTH`;

CREATE TABLE `SYS_MENU_AUTH` (
  `menuid` bigint(20) NOT NULL DEFAULT '0' COMMENT '菜单代码',
  `authcode` varchar(50) NOT NULL DEFAULT '' COMMENT '权限代码',
  PRIMARY KEY (`menuid`,`authcode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;

/*Data for the table `SYS_MENU_AUTH` */

insert  into `SYS_MENU_AUTH`(`menuid`,`authcode`) values (1,'view'),(2,'add'),(2,'delete'),(2,'modify'),(2,'updateMenuAuth'),(2,'view'),(4,'add'),(4,'delete'),(4,'modify'),(4,'view'),(5,'add'),(5,'delete'),(5,'modify'),(5,'updateRoleMenuAuth'),(5,'view'),(5,'viewRoleMenuAuth'),(6,'add'),(6,'delete'),(6,'modify'),(6,'resetpwd'),(6,'selectrole'),(6,'updateUserRole'),(6,'view'),(502,'view'),(601,'view'),(1711,'uploadFile'),(1711,'view'),(1712,'uploadFile'),(1712,'view'),(1729,'view'),(1735,'add'),(1745,'view');

/*Table structure for table `SYS_ORG` */

DROP TABLE IF EXISTS `SYS_ORG`;

CREATE TABLE `SYS_ORG` (
  `orgcode` varchar(20) DEFAULT NULL COMMENT '机构编码',
  `orgname` varchar(50) DEFAULT NULL COMMENT '机构名称',
  `pcode` varchar(20) DEFAULT NULL COMMENT '父级机构',
  `status` int(1) DEFAULT NULL COMMENT '机构状态',
  `org_level` int(1) DEFAULT NULL COMMENT '机构层级',
  `update_date` varchar(20) DEFAULT NULL COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `SYS_ORG` */

insert  into `SYS_ORG`(`orgcode`,`orgname`,`pcode`,`status`,`org_level`,`update_date`) values ('10000000','云中心','0',1,1,'2016-01-06 09:13:22'),('11100300','研发一部','10000000',1,2,'2016-01-06 09:13:22'),('11100400','研发二部','10000000',1,2,'2016-01-06 09:13:22'),('11100500','研发三部','10000000',1,2,'2016-01-06 09:13:22'),('11100600','研发四部','10000000',1,2,'2016-01-06 09:13:22'),('11100900','研发五部','10000000',1,2,'2016-01-06 09:13:22'),('11101000','运营一部','10000000',1,2,'2016-01-06 09:13:22'),('11101200','运营二部','10000000',1,2,'2016-01-06 09:13:22'),('11101300','业务一部','10000000',1,2,'2016-01-06 09:13:22'),('11101800','安全保卫部','10000000',1,2,'2016-01-06 09:13:22');

/*Table structure for table `SYS_ORG_ROLE` */

DROP TABLE IF EXISTS `SYS_ORG_ROLE`;

CREATE TABLE `SYS_ORG_ROLE` (
  `orgcode` varchar(20) NOT NULL COMMENT '机构代码',
  `rolecode` varchar(50) NOT NULL COMMENT '角色代码',
  PRIMARY KEY (`orgcode`,`rolecode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `SYS_ORG_ROLE` */

insert  into `SYS_ORG_ROLE`(`orgcode`,`rolecode`) values ('10000000','flex_head'),('11100300','flex_head'),('11100300','metadata'),('11100300','serectData'),('11100300','sys'),('11100300','user'),('11100400','flex_head'),('11100400','metadata'),('11100400','sys'),('11100500','flex_head'),('11100500','metadata'),('11100600','flex_acc'),('11100600','flex_audit'),('11100600','judicial'),('11100600','operations'),('11100900','judicial'),('11100900','operations'),('11101000','metadata'),('11101000','serectData'),('11101000','sys'),('11101200','flex_disc'),('11101200','flex_head'),('11101200','flex_info'),('11101200','metadata'),('11101200','operations'),('11101300','metadata'),('11101300','operations'),('11101800','sys'),('11101800','test');

/*Table structure for table `SYS_PARM` */

DROP TABLE IF EXISTS `SYS_PARM`;

CREATE TABLE `SYS_PARM` (
  `parm_code` int(11) NOT NULL AUTO_INCREMENT COMMENT '参数编号',
  `parm_type` varchar(100) NOT NULL COMMENT '参数类别',
  `parm_name` varchar(100) NOT NULL COMMENT '参数名',
  `parm_value` varchar(200) NOT NULL COMMENT '参数值',
  `parm_desc` varchar(1000) DEFAULT NULL COMMENT '参数描述',
  `operator` varchar(100) DEFAULT NULL COMMENT '操作人',
  `optime` varchar(100) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`parm_code`,`parm_type`,`parm_name`)
) ENGINE=InnoDB AUTO_INCREMENT=119 DEFAULT CHARSET=utf8;

/*Data for the table `SYS_PARM` */

insert  into `SYS_PARM`(`parm_code`,`parm_type`,`parm_name`,`parm_value`,`parm_desc`,`operator`,`optime`) values (45,'DB_TYPE','ORACLE','ORACLE','元数据类型','admin','2016-06-15 15:07:09'),(47,'DB_TYPE','MYSQL','MYSQL','元数据类型','admin','2016-06-15 15:07:01'),(49,'path','propertiesPath','/home/yk/ykcp/resources/formal_domain/hive/load/DDLResources/','发布步骤hive装数配置文件存储路径','admin','2017-07-17 16:00:50'),(50,'CONNECT_TYPE','JDBC','JDBC','源系统抽数连接类型','admin','2017-08-02 16:38:34'),(52,'path','scriptPath_test','/home/yk/ykcp/resources/test_domain/scripts/','脚本文件存储路径（测试）','admin','2017-07-17 16:01:21'),(53,'path','hiveHQLPath','/home/yk/ykcp/resources/formal_domain/hive/hql/','发布步骤hive表结构更新hql文件存储路径','admin','2017-07-17 16:00:57'),(55,'path','fileUploadPath','/home/yk/ykcp/resources/webdocs/Hemis/workorder/','平台操作的工单附件存储路径','admin','2017-07-06 12:00:32'),(56,'path','fileDownloadPath','/home/yk/ykcp/resources/webdocs/export_data/','导出的文件存储路径','admin','2017-07-19 11:33:05'),(57,'path','scriptPath','/home/yk/ykcp/resources/formal_domain/scripts/','脚本文件存储路径','admin','2017-07-17 16:01:28'),(59,'path','propertiesPath_test','/home/yk/ykcp/resources/test_domain/hive/load/DDLResources/','测试步骤hive装数配置文件存储路径','admin','2017-07-17 16:01:05'),(60,'path','hiveHQLPath_test','/home/yk/ykcp/resources/test_domain/hive/hql/','测试步骤hive表结构更新hql文件存储路径','admin','2017-07-17 16:01:12'),(61,'DB_TYPE','TXT','TXT','元数据类型','admin','2016-06-15 15:06:52'),(74,'path','schedulExcelPath','/home/yk/ykcp/resources/webdocs/Hemis/schedule/','存放调度excel的路径','admin','2017-07-06 11:54:49'),(80,'SIZE_LIMT','d_max_size','1073741824','页面可下载文件最大大小为1G','admin','2016-05-19 16:08:36'),(87,'fileBackup','localPath','/home/yk/ykcp/resources/webdocs/upload/file_backup/','共享存储文件路径','admin','2017-07-21 10:43:26'),(88,'fileBackup','hdfsPath','/user/yk/ykcp/filebackup/','文件备份到HDFS的路径','admin','2017-07-21 10:43:18'),(89,'fileBackup','downloadPath','/home/yk/ykcp/resources/webdocs/download/','HDFS上下载的文件存储路径','admin','2017-07-21 10:43:31'),(93,'deviceId','D00001','D00001','终端ID','admin','2017-07-03 11:46:45'),(94,'deviceId','D00002','D00002','终端ID','admin','2017-07-03 11:46:26'),(95,'deviceId','D00003','D00003','终端ID','admin','2017-07-03 11:47:11'),(96,'sysCode','S00001','S00001','系统编号','admin','2017-07-03 11:48:03'),(97,'sysCode','S00002','S00002','系统编号','admin','2017-07-03 11:48:35'),(98,'sysCode','S00003','S00003','系统编号','admin','2017-07-03 11:48:56'),(99,'tradeCode','T00001','T00001','业务编码','admin','2017-07-03 11:49:46'),(100,'tradeCode','T00002','T00002','业务编码','admin','2017-07-03 11:50:06'),(101,'tradeCode','T00003','T00003','业务编码','admin','2017-07-03 11:50:28'),(102,'IMPALA','URL','10.20.20.7:21000','IMPALA连接信息','admin','2017-07-06 12:59:26'),(103,'path','metaDataComparePath','/home/yk/ykcp/app/MetaDataCompare','元数据抽取对比程序路径','admin','2017-07-17 16:01:33'),(104,'path','hiveLoadPath','/home/yk/ykcp/resources/formal_domain/hive/load/','HIVE装数程序路径','admin','2017-07-17 16:01:41'),(105,'path','hiveLoadPath_test','/home/yk/ykcp/resources/test_domain/hive/load/','HIVE装数程序路径（测试）','admin','2017-07-17 16:01:47'),(106,'HiveConfig','HISTORY_DATA_SAVED_DAY','30','hdfs中文件存放天数','admin','2017-07-17 16:19:27'),(107,'path','downloadMappingPath','http://10.20.20.7:9777/','下载的文件路径映射','admin','2017-08-01 15:59:42'),(109,'DB_TYPE','SQLSERVER','SQLSERVER','元数据类型','admin','2017-08-03 12:00:45'),(115,'path','hivePath','/user/hive/warehouse/yk_db.db/','HIVE发布库路径','admin','2017-08-16 15:36:04'),(116,'path','mroutPath','/user/mr/tmp/output/','MR输出路径','admin','2017-08-16 15:42:03'),(117,'path','hbaseProperties','/home/yk/ykcp/app/ws/hbaseProperties/','HBASE配置文件落地路径','admin','2017-08-17 12:06:41'),(118,'path','hbaseshellPath','/home/yk/ykcp/app/ws/hbase_shell','HBASE数据清理脚本路径','admin','2017-08-16 16:02:17');

/*Table structure for table `SYS_RETRIEVE_LOG` */

DROP TABLE IF EXISTS `SYS_RETRIEVE_LOG`;

CREATE TABLE `SYS_RETRIEVE_LOG` (
  `logid` int(10) NOT NULL AUTO_INCREMENT COMMENT '序号',
  `loglevel` varchar(10) DEFAULT NULL COMMENT '日志级别:"1"表示一般日志；"2"表示警告日志；"3"表示错误日志',
  `type` varchar(10) DEFAULT NULL COMMENT '业务类型：ETL：ETL脚本日志；WS：WS服务日志；ESB:ESB服务日志；HIVE:HIVE表结构更新日志',
  `logdata` varchar(5000) DEFAULT NULL COMMENT '日志数据',
  `logtime` bigint(20) DEFAULT NULL COMMENT '日志时间',
  `importance` varchar(10) DEFAULT NULL COMMENT '重要程度：1：重要；0：非重要',
  PRIMARY KEY (`logid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `SYS_RETRIEVE_LOG` */

/*Table structure for table `SYS_ROLE` */

DROP TABLE IF EXISTS `SYS_ROLE`;

CREATE TABLE `SYS_ROLE` (
  `rolecode` varchar(50) NOT NULL COMMENT '角色代码',
  `rolename` varchar(100) NOT NULL COMMENT '角色名称',
  `des` varchar(100) DEFAULT NULL COMMENT '备注',
  `createtime` varchar(20) DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`rolecode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;

/*Data for the table `SYS_ROLE` */

insert  into `SYS_ROLE`(`rolecode`,`rolename`,`des`,`createtime`) values ('admin','超级管理员','超级管理员','2016-03-11 10:08:28'),('flex_acc','会计结算部_查询专员','会计结算部_查询专员','2016-03-09 11:40:11'),('flex_audit','稽核审计部_查询专员','稽核审计部_查询专员','2016-03-09 11:37:53'),('flex_disc','纪检监察室_查询专员1','纪检监察室_查询专员','2016-03-09 11:39:50'),('flex_head','历史数据查询专员（总行）','历史数据查询专员（总行）','2016-03-09 11:37:20'),('flex_info','信息技术部_查询专员','信息技术部_查询专员','2016-03-09 11:38:17'),('judicial','司法查询专员','司法查询专员','2016-03-09 11:31:21'),('metadata','数据归档管理员','数据归档管理员','2016-03-09 11:30:07'),('operations','机房管理员','1','2016-03-09 11:28:06'),('serectData','涉密数据管理员','涉密数据管理员','2016-03-09 11:30:33'),('sys','系统管理员','系统管理员','2016-03-09 11:22:28'),('test','测试角色','测试角色描述','2017-08-07 16:54:53'),('user','用户管理员','用户管理员','2016-03-09 11:23:33');

/*Table structure for table `SYS_ROLE_MENU_AUTH` */

DROP TABLE IF EXISTS `SYS_ROLE_MENU_AUTH`;

CREATE TABLE `SYS_ROLE_MENU_AUTH` (
  `authcode` varchar(50) NOT NULL DEFAULT '' COMMENT '权限代码',
  `menuid` bigint(20) NOT NULL DEFAULT '0' COMMENT '菜单代码',
  `rolecode` varchar(50) NOT NULL DEFAULT '' COMMENT '角色代码',
  PRIMARY KEY (`authcode`,`menuid`,`rolecode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;

/*Data for the table `SYS_ROLE_MENU_AUTH` */

insert  into `SYS_ROLE_MENU_AUTH`(`authcode`,`menuid`,`rolecode`) values ('add',2,'test'),('add',4,'admin'),('add',4,'sys'),('add',4,'test'),('add',4,'user'),('add',5,'admin'),('add',5,'sys'),('add',5,'test'),('add',5,'user'),('add',6,'admin'),('add',6,'user'),('delete',4,'admin'),('delete',4,'sys'),('delete',4,'test'),('delete',4,'user'),('delete',5,'admin'),('delete',5,'sys'),('delete',5,'test'),('delete',5,'user'),('delete',6,'admin'),('delete',6,'test'),('delete',6,'user'),('modify',2,'test'),('modify',4,'admin'),('modify',4,'sys'),('modify',4,'test'),('modify',4,'user'),('modify',5,'admin'),('modify',5,'sys'),('modify',5,'test'),('modify',5,'user'),('modify',6,'admin'),('modify',6,'test'),('modify',6,'user'),('resetpwd',6,'admin'),('resetpwd',6,'test'),('resetpwd',6,'user'),('selectrole',6,'admin'),('selectrole',6,'test'),('selectrole',6,'user'),('uploadFile',1711,'judicial'),('uploadFile',1712,'judicial'),('view',1,'admin'),('view',1,'serectData'),('view',1,'sys'),('view',1,'test'),('view',1,'user'),('view',2,'test'),('view',4,'test'),('view',5,'test'),('view',6,'test'),('view',502,'judicial'),('view',601,'judicial'),('view',1711,'judicial'),('view',1712,'judicial'),('view',1729,'judicial'),('viewRoleMenuAuth',5,'test');

/*Table structure for table `SYS_USER` */

DROP TABLE IF EXISTS `SYS_USER`;

CREATE TABLE `SYS_USER` (
  `userid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户代码',
  `usercode` varchar(20) NOT NULL COMMENT '用户账号',
  `username` varchar(50) NOT NULL COMMENT '用户名称',
  `userpwd` varchar(100) NOT NULL COMMENT '用户密码',
  `orgcode` varchar(50) NOT NULL COMMENT '机构代码',
  `status` char(1) DEFAULT '1' COMMENT '状态0：锁定1：激活',
  `des` varchar(255) DEFAULT NULL COMMENT '备注',
  `createtime` varchar(20) DEFAULT NULL COMMENT '创建时间',
  `lastlogintime` varchar(20) DEFAULT NULL COMMENT '最后一次登录时间',
  `empid` varchar(20) DEFAULT NULL COMMENT '员工编号',
  PRIMARY KEY (`userid`),
  UNIQUE KEY `INDEX_SYS_USER_UNIQUE` (`usercode`)
) ENGINE=InnoDB AUTO_INCREMENT=675 DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;

/*Data for the table `SYS_USER` */

insert  into `SYS_USER`(`userid`,`usercode`,`username`,`userpwd`,`orgcode`,`status`,`des`,`createtime`,`lastlogintime`,`empid`) values (1,'admin','admin','21232F297A57A5A743894A0E4A801FC3','10000000','1','系统管理员1','2016-06-17 10:22:02','2017-11-13 15:57:57',NULL),(651,'test_11','测试用户','58F2CE605ED243CA60A51E727649C169','11100600','1','测试用户1','2016-06-17 14:35:16','2017-09-27 09:09:40',NULL),(652,'test_22','test_222','9C18E0E0747E5EC6C91BBB5ED53722F1','11100600','1','test_222','2016-06-17 14:41:28','2017-08-09 15:50:29',NULL),(660,'test2','测试用户','AD0234829205B9033196BA818F7A872B','43111100','1','测试用户','2016-10-08 16:16:09',NULL,'100326'),(665,'Annie','Annie','0A420BEB77B963B9A14066F08B7865E8','11100300','1','test','2017-08-03 09:41:34','2017-08-03 15:20:03',NULL),(668,'maqing','mq','36D04A9D74392C727B1A9BF97A7BCBAC','11100300','1','','2017-08-03 11:15:14','2017-08-10 14:59:46',NULL),(669,'tangting','tangting','F928DBDC923D192C1554B5E4E347C360','11101800','1','2','2017-08-03 14:31:39','2017-08-03 15:53:23',NULL),(670,'weryan','wey','23ED723E8D737641D6F0BBF2714F8698','11101000','1','6776','2017-08-03 14:57:06',NULL,'76'),(671,'test111','test111','4061863CAF7F28C0B0346719E764D561','11100500','1','111','2017-08-04 18:42:18',NULL,'12356'),(672,'ykces','cs','11092D4D6E411468C0C655D1A25A21FE','11100900','0','测试一下备注看看是否能保存吧','2017-08-07 14:12:41','2017-08-08 17:53:41','123455'),(673,'1111','11111','B59C67BF196A4758191E42F76670CEBA','10000000','1','11111111','2017-10-09 14:34:49',NULL,'111111111');

/*Table structure for table `SYS_USER_ROLE` */

DROP TABLE IF EXISTS `SYS_USER_ROLE`;

CREATE TABLE `SYS_USER_ROLE` (
  `usercode` varchar(50) NOT NULL COMMENT '用户代码',
  `rolecode` varchar(50) NOT NULL COMMENT '角色代码',
  PRIMARY KEY (`usercode`,`rolecode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;

/*Data for the table `SYS_USER_ROLE` */

insert  into `SYS_USER_ROLE`(`usercode`,`rolecode`) values ('1111','flex_head'),('admin','admin'),('Annie','metadata'),('Annie','serectData'),('Annie','sys'),('maqing','flex_head'),('maqing','metadata'),('maqing','serectData'),('maqing','sys'),('maqing','user'),('tangting','sys'),('test_11','judicial'),('test_22','flex_audit'),('weryan','metadata'),('weryan','serectData'),('ykces','operations');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
