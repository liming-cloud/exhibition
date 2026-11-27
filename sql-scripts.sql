-- =====================================================================
-- Incremental SQL Script for Exhibition Project Revamp
--
-- This script applies schema changes and data migrations for:
-- 1. Adding the 'contact_info' table for contact person details.
-- 2. Adding the 'exhibition_registration' table for event sign-ups.
-- 3. Modifying 'exhibition_detail.exhibition_type' to use ENUM.
-- 4. Migrating existing data for 'exhibition_type' and 'contact_progress'.
-- 5. Adding default contact information and sample exhibition data.
--
-- IMPORTANT: Backup your database before running this script.
-- Execute sections sequentially.
-- =====================================================================

-- ===========================================
-- Section 1: Create New Tables
-- ===========================================
SELECT 'Section 1: Creating new tables...' AS 'Log';

-- Create 'contact_info' table
CREATE TABLE IF NOT EXISTS `contact_info` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `address` VARCHAR(255) NULL COMMENT '公司地址',
  `phone` VARCHAR(50) NULL COMMENT '联系电话',
  `mobile` VARCHAR(50) NULL COMMENT '手机号码',
  `contact_person` VARCHAR(100) NULL COMMENT '联系人',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='联系信息配置表';
SELECT 'Table `contact_info` created or already exists.' AS 'Log';

-- Create 'exhibition_registration' table
CREATE TABLE IF NOT EXISTS `exhibition_registration` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `exhibition_id` INT NULL COMMENT '关联的展会ID',
  `company_or_personal_name` VARCHAR(255) NULL COMMENT '公司/个人名称',
  `contact_name` VARCHAR(100) NULL COMMENT '联系人姓名',
  `contact_phone` VARCHAR(50) NULL COMMENT '联系电话',
  `contact_progress` ENUM(
    'UNCONTACTED',  -- 未联系
    'CONTACTED',    -- 已联系
    'REPLIED',      -- 已回复
    'CLOSED',       -- 已成交
    'CANCELLED'     -- 已取消
  ) NOT NULL DEFAULT 'UNCONTACTED' COMMENT '联系进度',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  INDEX `idx_exhibition_id` (`exhibition_id`),
  INDEX `idx_contact_progress` (`contact_progress`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='展会报名信息表';
SELECT 'Table `exhibition_registration` created or already exists.' AS 'Log';


-- ===========================================
-- Section 2: Modify 'exhibition_detail' Table
-- ===========================================
SELECT 'Section 2: Modifying `exhibition_detail` table...' AS 'Log';

-- Step 2.1: Data migration for 'exhibition_detail.exhibition_type'
SELECT 'Migrating data for `exhibition_detail`.`exhibition_type`...' AS 'Log';
-- Ensure 'exhibition_type' column exists and has a string-compatible type before these UPDATEs.
-- These UPDATEs prepare the data for conversion to ENUM.

UPDATE `exhibition_detail` SET `exhibition_type` = 'ENERGY'
WHERE `exhibition_type` IS NULL OR `exhibition_type` = '';

UPDATE `exhibition_detail` SET `exhibition_type` = 'ENERGY'
WHERE `exhibition_type` IN ('综合展会', '电子科技', '建筑建材')
   OR `name` LIKE '%能源%' OR `name` LIKE '%电力%' OR `name` LIKE '%新能源%' OR `name` LIKE '%光伏%' OR `name` LIKE '%风能%';

UPDATE `exhibition_detail` SET `exhibition_type` = 'ELEVATOR'
WHERE `exhibition_type` IN ('机械工业')
   OR `name` LIKE '%电梯%' OR `name` LIKE '%扶梯%' OR `name` LIKE '%升降%' OR `name` LIKE '%垂直%';

UPDATE `exhibition_detail` SET `exhibition_type` = 'MEDICAL'
WHERE `exhibition_type` IN ('食品饮料', '纺织服装')
   OR `name` LIKE '%医疗%' OR `name` LIKE '%医学%' OR `name` LIKE '%健康%' OR `name` LIKE '%药品%' OR `name` LIKE '%医院%';

-- Fallback for any values not mapped to ENUM candidates.
UPDATE `exhibition_detail` SET `exhibition_type` = 'ENERGY'
WHERE CAST(`exhibition_type` AS CHAR) NOT IN ('ENERGY', 'ELEVATOR', 'MEDICAL');

-- Step 2.2: Modify 'exhibition_type' column to ENUM
SELECT 'Altering `exhibition_detail`.`exhibition_type` to ENUM...' AS 'Log';
ALTER TABLE `exhibition_detail`
MODIFY COLUMN `exhibition_type` ENUM(
    'ENERGY',    -- 能源展
    'ELEVATOR',  -- 电梯展
    'MEDICAL'    -- 医疗展
) NOT NULL DEFAULT 'ENERGY' COMMENT '展会类型：能源展/电梯展/医疗展';
SELECT 'Column `exhibition_detail`.`exhibition_type` modified.' AS 'Log';


-- ===========================================
-- Section 3: Data Migration for 'exhibition_registration.contact_progress'
-- ===========================================
SELECT 'Section 3: Migrating data for `exhibition_registration`.`contact_progress`...' AS 'Log';
-- This section migrates string values in 'contact_progress' to ENUM compatible values.
-- Relevant if 'exhibition_registration' table existed with 'contact_progress' as VARCHAR/TEXT.

UPDATE `exhibition_registration` SET `contact_progress` = 'UNCONTACTED'
WHERE `contact_progress` = '未联系' OR `contact_progress` = '' OR `contact_progress` IS NULL;
UPDATE `exhibition_registration` SET `contact_progress` = 'CONTACTED' WHERE `contact_progress` = '已联系';
UPDATE `exhibition_registration` SET `contact_progress` = 'REPLIED' WHERE `contact_progress` = '已回复';
UPDATE `exhibition_registration` SET `contact_progress` = 'CLOSED' WHERE `contact_progress` = '已成交';
UPDATE `exhibition_registration` SET `contact_progress` = 'CANCELLED' WHERE `contact_progress` = '已取消';

-- Fallback for any other non-standard string values to the default ENUM value.
UPDATE `exhibition_registration` SET `contact_progress` = 'UNCONTACTED'
WHERE CAST(`contact_progress` AS CHAR) NOT IN ('UNCONTACTED', 'CONTACTED', 'REPLIED', 'CLOSED', 'CANCELLED');
SELECT 'Data migration for `exhibition_registration`.`contact_progress` complete.' AS 'Log';


-- ===========================================
-- Section 4: Initial Data Population
-- ===========================================
SELECT 'Section 4: Populating initial data...' AS 'Log';

-- Insert default contact information (if not already present)
-- Using INSERT IGNORE to avoid error if a row with ID 1 (or any unique key violation) already exists.
-- GET /contact/info 不再写库，没有数据时返回空对象，默认联系信息只在这里初始化；phone 按 JSON 数组存储（后端用 JacksonTypeHandler 读取）
INSERT IGNORE INTO `contact_info` (`id`, `address`, `phone`, `contact_person`, `create_time`, `update_time`)
VALUES (
    1,
    '北京市海淀区三里河路9号住建部大院',
    '["010-53630591/2/3"]',
    '林琳',
    NOW(),
    NOW()
);
SELECT 'Default contact information populated (if table was empty or ID 1 was available).' AS 'Log';

-- Insert sample exhibition data (with ON DUPLICATE KEY UPDATE)
SELECT 'Inserting/Updating sample exhibition data...' AS 'Log';
INSERT INTO `exhibition_detail` (`name`, `location`, `time`, `year`, `month`, `sort`, `content`, `country`, `country_name`, `exhibition_type`, `create_time`, `update_time`) VALUES
('2024北京国际能源展览会', '北京国家会议中心', '2024-05-15 至 2024-05-17', '2024', '05', 1, '专业的能源行业展览会', 1, '中国', 'ENERGY', NOW(), NOW()),
('2024上海国际电梯展览会', '上海新国际博览中心', '2024-06-10 至 2024-06-12', '2024', '06', 2, '国际知名电梯行业展会', 1, '中国', 'ELEVATOR', NOW(), NOW()),
('2024广州国际医疗设备展览会', '广州琶洲国际会展中心', '2024-07-20 至 2024-07-22', '2024', '07', 3, '医疗设备与技术专业展会', 1, '中国', 'MEDICAL', NOW(), NOW())
ON DUPLICATE KEY UPDATE
  `name` = VALUES(`name`), `location` = VALUES(`location`), `time` = VALUES(`time`), `year` = VALUES(`year`),
  `month` = VALUES(`month`), `sort` = VALUES(`sort`), `content` = VALUES(`content`), `country` = VALUES(`country`),
  `country_name` = VALUES(`country_name`), `exhibition_type` = VALUES(`exhibition_type`), `update_time` = NOW();
SELECT 'Sample exhibition data processed.' AS 'Log';


-- ===========================================
-- Section 5: Verification (Optional)
-- ===========================================
SELECT 'Section 5: Verification queries (optional)...' AS 'Log';

SELECT 'Verifying `exhibition_detail`.`exhibition_type` distribution:' AS 'Info';
SELECT `exhibition_type`, COUNT(*) AS `count` FROM `exhibition_detail` GROUP BY `exhibition_type` ORDER BY `count` DESC;

SELECT 'Verifying `exhibition_registration`.`contact_progress` distribution:' AS 'Info';
SELECT `contact_progress`, COUNT(*) AS `count` FROM `exhibition_registration` GROUP BY `contact_progress` ORDER BY `count` DESC;

SELECT 'Verifying `contact_info` data:' AS 'Info';
SELECT * FROM `contact_info`;

SELECT 'Verifying column definitions:' AS 'Info';
SELECT
    TABLE_NAME, COLUMN_NAME, DATA_TYPE, COLUMN_TYPE, COLUMN_DEFAULT, IS_NULLABLE, COLUMN_COMMENT
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = DATABASE()
  AND TABLE_NAME IN ('exhibition_detail', 'exhibition_registration', 'contact_info')
ORDER BY TABLE_NAME, ORDINAL_POSITION;

-- ===========================================
SELECT '========= Incremental SQL Script Execution Finished ==========' AS 'Log';
-- =========================================== 


-- =====================================================================
-- Section 6: Change ENUM fields to VARCHAR and store enum codes
-- IMPORTANT: Backup your database before running this section.
-- This section assumes enum codes are relatively short (e.g., VARCHAR(50)).
-- =====================================================================
SELECT 'Section 6: Modifying ENUM columns to VARCHAR for code storage...' AS 'Log';

-- Step 6.1: Modify 'exhibition_detail.exhibition_type'
-- First, create a temporary column to store the string representation of the ENUM
ALTER TABLE `exhibition_detail` ADD COLUMN `exhibition_type_temp` VARCHAR(50);
SELECT 'Temporary column `exhibition_type_temp` added to `exhibition_detail`.' AS 'Log';

-- Copy the string value of the ENUM to the temporary column
UPDATE `exhibition_detail` SET `exhibition_type_temp` = CAST(`exhibition_type` AS CHAR);
SELECT 'Data copied from `exhibition_type` to `exhibition_type_temp`.' AS 'Log';

-- Drop the old ENUM column
ALTER TABLE `exhibition_detail` DROP COLUMN `exhibition_type`;
SELECT 'Original `exhibition_type` ENUM column dropped.' AS 'Log';

-- Rename the temporary column to the original column name, now as VARCHAR
ALTER TABLE `exhibition_detail` CHANGE COLUMN `exhibition_type_temp` `exhibition_type` VARCHAR(50) NOT NULL DEFAULT 'ENERGY' COMMENT '展会类型编码：ENERGY/ELEVATOR/MEDICAL';
SELECT 'Column `exhibition_type` recreated as VARCHAR(50).' AS 'Log';

-- Step 6.2: Modify 'exhibition_registration.contact_progress'
-- First, create a temporary column
ALTER TABLE `exhibition_registration` ADD COLUMN `contact_progress_temp` VARCHAR(50);
SELECT 'Temporary column `contact_progress_temp` added to `exhibition_registration`.' AS 'Log';

-- Copy the string value of the ENUM to the temporary column
UPDATE `exhibition_registration` SET `contact_progress_temp` = CAST(`contact_progress` AS CHAR);
SELECT 'Data copied from `contact_progress` to `contact_progress_temp`.' AS 'Log';

-- Drop the old ENUM column
ALTER TABLE `exhibition_registration` DROP COLUMN `contact_progress`;
SELECT 'Original `contact_progress` ENUM column dropped.' AS 'Log';

-- Rename the temporary column, now as VARCHAR
ALTER TABLE `exhibition_registration` CHANGE COLUMN `contact_progress_temp` `contact_progress` VARCHAR(50) NOT NULL DEFAULT 'UNCONTACTED' COMMENT '联系进度编码: UNCONTACTED/CONTACTED/REPLIED/CLOSED/CANCELLED';
SELECT 'Column `contact_progress` recreated as VARCHAR(50).' AS 'Log';

-- Step 6.3: Verification (Optional)
SELECT 'Verifying column definitions after ENUM to VARCHAR conversion:' AS 'Info';
SELECT 
    TABLE_NAME, COLUMN_NAME, DATA_TYPE, CHARACTER_MAXIMUM_LENGTH, COLUMN_DEFAULT, IS_NULLABLE, COLUMN_COMMENT 
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_SCHEMA = DATABASE() 
  AND TABLE_NAME IN ('exhibition_detail', 'exhibition_registration') 
  AND COLUMN_NAME IN ('exhibition_type', 'contact_progress')
ORDER BY TABLE_NAME, COLUMN_NAME;

SELECT '========= Section 6 Execution Finished ==========' AS 'Log'; 