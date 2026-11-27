-- =====================================================================
-- 回滚 001 · 恢复展会 time/year/month，删除新增的日期与时间字段（Issue #44）
--
-- 与旧版后端一起回退。跨天展会的 `time` 写成「开始 至 结束」，单日写成「开始」。
-- 新增的 create_time、update_time 会被删除，记录的时间不保留。
-- =====================================================================

SET NAMES utf8mb4;

ALTER TABLE `exhibition_detail`
  ADD COLUMN `time` varchar(30) DEFAULT NULL COMMENT '时间' AFTER `location`,
  ADD COLUMN `year` varchar(10) DEFAULT NULL COMMENT '年份' AFTER `time`,
  ADD COLUMN `month` varchar(10) DEFAULT NULL COMMENT '月份' AFTER `year`;

UPDATE `exhibition_detail`
SET `time`  = IF(`start_date` = `end_date`, DATE_FORMAT(`start_date`, '%Y-%m-%d'),
                 CONCAT(DATE_FORMAT(`start_date`, '%Y-%m-%d'), ' 至 ', DATE_FORMAT(`end_date`, '%Y-%m-%d'))),
    `year`  = DATE_FORMAT(`start_date`, '%Y'),
    `month` = DATE_FORMAT(`start_date`, '%m');

ALTER TABLE `exhibition_detail`
  DROP CHECK `chk_exhibition_dates`,
  DROP COLUMN `start_date`,
  DROP COLUMN `end_date`,
  DROP COLUMN `create_time`,
  DROP COLUMN `update_time`;

ALTER TABLE `information` DROP COLUMN `create_time`, DROP COLUMN `update_time`;
ALTER TABLE `cases` DROP COLUMN `create_time`, DROP COLUMN `update_time`;
ALTER TABLE `subsidies` DROP COLUMN `create_time`, DROP COLUMN `update_time`;
