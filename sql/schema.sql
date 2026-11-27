-- =====================================================================
-- 雅博展览 · 完整表结构（MySQL 8.0）
--
-- 这是表结构的唯一来源：取自 2026-11-24 线上导出，并已包含 sql/migrations/ 下全部迁移。
-- 新建空库：直接执行本文件。已有的库：按 sql/README.md 顺序执行迁移，不要执行本文件。
-- 改表时同时改本文件和新增一份迁移（附回滚）。
-- 根目录的 sql-scripts.sql 是早期增量脚本，线上已执行过，只作历史参考。
-- =====================================================================

SET NAMES utf8mb4;

CREATE TABLE IF NOT EXISTS `carousel` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(30) NOT NULL,
  `image_url` text,
  `link` varchar(50) DEFAULT NULL,
  `sort` int DEFAULT NULL,
  `status` int DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='轮播图';

CREATE TABLE IF NOT EXISTS `cases` (
  `id` int NOT NULL AUTO_INCREMENT,
  `cover_url` text COMMENT '封面图',
  `title` varchar(100) DEFAULT NULL COMMENT '标题简介',
  `content` text COMMENT '内容',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='项目案例';

CREATE TABLE IF NOT EXISTS `contact_info` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `company_profile` text COMMENT '公司简介',
  `address` varchar(500) DEFAULT NULL COMMENT '公司地址',
  `phone` text COMMENT '联系电话 (JSON 数组格式)',
  `contact_person` varchar(100) DEFAULT NULL COMMENT '联系人',
  `working_hours` varchar(255) DEFAULT NULL COMMENT '工作时间',
  `holiday_arrangement` varchar(255) DEFAULT NULL COMMENT '节假日安排',
  `email` varchar(100) DEFAULT NULL COMMENT '业务咨询邮箱',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='联系信息配置表';

CREATE TABLE IF NOT EXISTS `exhibition_detail` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL COMMENT '名称',
  `location` varchar(100) DEFAULT NULL COMMENT '位置',
  `start_date` date NOT NULL COMMENT '开始日期',
  `end_date` date NOT NULL COMMENT '结束日期',
  `sort` int DEFAULT NULL COMMENT '排序',
  `content` text COMMENT '内容',
  `country` int DEFAULT NULL COMMENT '展会国家',
  `country_name` varchar(50) DEFAULT NULL COMMENT '国家名称',
  `exhibition_type` varchar(10) NOT NULL DEFAULT 'ENERGY' COMMENT '''展会类型：能源展/电梯展/医疗展''',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  CONSTRAINT `chk_exhibition_dates` CHECK (`end_date` >= `start_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='展会';

CREATE TABLE IF NOT EXISTS `exhibition_registration` (
  `id` int NOT NULL AUTO_INCREMENT,
  `exhibition_id` int DEFAULT NULL COMMENT '关联的展会ID',
  `exhibition_name` varchar(200) DEFAULT NULL,
  `company_or_personal_name` varchar(255) DEFAULT NULL COMMENT '公司/个人名称',
  `contact_name` varchar(100) DEFAULT NULL COMMENT '联系人姓名',
  `contact_phone` varchar(50) DEFAULT NULL COMMENT '联系电话',
  `contact_progress` varchar(20) NOT NULL DEFAULT 'UNCONTACTED' COMMENT '联系进度',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_exhibition_id` (`exhibition_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='展会报名信息表';

CREATE TABLE IF NOT EXISTS `global_area` (
  `id` int NOT NULL AUTO_INCREMENT,
  `continent_code` varchar(10) DEFAULT NULL COMMENT '大洲编码',
  `continent_name` varchar(10) DEFAULT NULL COMMENT '大洲名称',
  `country_code` varchar(10) DEFAULT NULL COMMENT '城市编码',
  `country_name` varchar(50) DEFAULT NULL COMMENT '城市名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='世界国家信息';

CREATE TABLE IF NOT EXISTS `information` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL COMMENT '标题简介',
  `content` text COMMENT '内容',
  `cover_url` text NOT NULL COMMENT '封面图',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='资讯';

CREATE TABLE IF NOT EXISTS `subsidies` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL COMMENT '标题简介',
  `content` text NOT NULL COMMENT '内容',
  `price` decimal(10,0) NOT NULL COMMENT '补贴金额',
  `start_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '开始日期',
  `end_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '结束日期',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='补贴';
