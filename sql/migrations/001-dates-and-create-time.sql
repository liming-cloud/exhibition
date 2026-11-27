-- =====================================================================
-- 001 · 展会开始/结束日期、内容表创建时间（Issue #44）
--
-- 1. information / cases / subsidies / exhibition_detail 新增 create_time、update_time
--    （已有数据取执行本脚本的时间）
-- 2. exhibition_detail 新增 start_date、end_date，从 `time` 迁移：
--    `time` 为单日「2025-01-14」时 开始 = 结束；为「开始 至 结束」时分别取两个日期
-- 3. 删除 exhibition_detail 的 `time`、`year`、`month`
--
-- 执行前先备份；与新版后端一起上线（旧版后端读不到 time/year/month）。
-- 回滚：001-dates-and-create-time.rollback.sql
-- =====================================================================

-- 客户端字符集不是 utf8mb4 时中文注释会写成乱码
SET NAMES utf8mb4;

-- 1. 创建时间、更新时间
ALTER TABLE `information`
  ADD COLUMN `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  ADD COLUMN `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间';

ALTER TABLE `cases`
  ADD COLUMN `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  ADD COLUMN `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间';

ALTER TABLE `subsidies`
  ADD COLUMN `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  ADD COLUMN `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间';

ALTER TABLE `exhibition_detail`
  ADD COLUMN `start_date` date NULL COMMENT '开始日期' AFTER `location`,
  ADD COLUMN `end_date` date NULL COMMENT '结束日期' AFTER `start_date`,
  ADD COLUMN `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  ADD COLUMN `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间';

-- 2. 迁移展会日期
UPDATE `exhibition_detail`
SET `start_date` = CAST(REGEXP_SUBSTR(`time`, '[0-9]{4}-[0-9]{2}-[0-9]{2}', 1, 1) AS DATE),
    `end_date`   = CAST(COALESCE(REGEXP_SUBSTR(`time`, '[0-9]{4}-[0-9]{2}-[0-9]{2}', 1, 2),
                                 REGEXP_SUBSTR(`time`, '[0-9]{4}-[0-9]{2}-[0-9]{2}', 1, 1)) AS DATE);

-- 检查：下面应返回 0 行。有结果说明 `time` 里没有可识别的日期，先手工补好 start_date、end_date 再继续；
-- 不处理的话，下一句改 NOT NULL 会报错停下，不会删掉旧字段。
SELECT `id`, `name`, `time` FROM `exhibition_detail`
WHERE `start_date` IS NULL OR `end_date` IS NULL OR `end_date` < `start_date`;

-- 3. 日期改必填、加约束，删除旧字段
ALTER TABLE `exhibition_detail`
  MODIFY COLUMN `start_date` date NOT NULL COMMENT '开始日期',
  MODIFY COLUMN `end_date` date NOT NULL COMMENT '结束日期',
  ADD CONSTRAINT `chk_exhibition_dates` CHECK (`end_date` >= `start_date`);

ALTER TABLE `exhibition_detail`
  DROP COLUMN `time`,
  DROP COLUMN `year`,
  DROP COLUMN `month`;
