-- ===========================================
-- 展会类型和联系进度枚举字段修改SQL脚本
-- 执行顺序：按照序号1-10依次执行
-- ===========================================

-- 1. 检查当前表结构
DESCRIBE exhibition_detail;
DESCRIBE exhibition_registration;

-- 2. 备份现有数据（可选，生产环境建议执行）
-- CREATE TABLE exhibition_detail_backup AS SELECT * FROM exhibition_detail;
-- CREATE TABLE exhibition_registration_backup AS SELECT * FROM exhibition_registration;

-- 3. 检查现有数据中的展会类型分布
SELECT 'Current exhibition_type distribution' as info, exhibition_type, COUNT(*) as count 
FROM exhibition_detail 
WHERE exhibition_type IS NOT NULL
GROUP BY exhibition_type
ORDER BY count DESC;

-- 4. 检查现有数据中的联系进度分布
SELECT 'Current contact_progress distribution' as info, contact_progress, COUNT(*) as count 
FROM exhibition_registration 
WHERE contact_progress IS NOT NULL
GROUP BY contact_progress
ORDER BY count DESC;

-- 5. 数据迁移：将现有展会类型映射到新的三种类型
-- 先将所有数据设为默认值
UPDATE exhibition_detail SET exhibition_type = 'ENERGY' WHERE exhibition_type IS NULL;

-- 根据展会名称或现有类型进行智能映射（可根据实际情况调整）
UPDATE exhibition_detail SET exhibition_type = 'ENERGY' 
WHERE exhibition_type IN ('综合展会', '电子科技', '建筑建材') 
   OR name LIKE '%能源%' OR name LIKE '%电力%' OR name LIKE '%新能源%' OR name LIKE '%光伏%' OR name LIKE '%风能%';

UPDATE exhibition_detail SET exhibition_type = 'ELEVATOR' 
WHERE exhibition_type IN ('机械工业') 
   OR name LIKE '%电梯%' OR name LIKE '%扶梯%' OR name LIKE '%升降%' OR name LIKE '%垂直%';

UPDATE exhibition_detail SET exhibition_type = 'MEDICAL' 
WHERE exhibition_type IN ('食品饮料', '纺织服装') 
   OR name LIKE '%医疗%' OR name LIKE '%医学%' OR name LIKE '%健康%' OR name LIKE '%药品%' OR name LIKE '%医院%';

-- 如果没有匹配到，设为默认的能源展
UPDATE exhibition_detail SET exhibition_type = 'ENERGY' 
WHERE exhibition_type NOT IN ('ENERGY', 'ELEVATOR', 'MEDICAL');

-- 6. 联系进度数据迁移
UPDATE exhibition_registration SET contact_progress = 'UNCONTACTED' WHERE contact_progress = '未联系' OR contact_progress IS NULL;
UPDATE exhibition_registration SET contact_progress = 'CONTACTED' WHERE contact_progress = '已联系';
UPDATE exhibition_registration SET contact_progress = 'REPLIED' WHERE contact_progress = '已回复';
UPDATE exhibition_registration SET contact_progress = 'CLOSED' WHERE contact_progress = '已成交';
UPDATE exhibition_registration SET contact_progress = 'CANCELLED' WHERE contact_progress = '已取消';

-- 设置默认值
UPDATE exhibition_registration SET contact_progress = 'UNCONTACTED' 
WHERE contact_progress NOT IN ('UNCONTACTED', 'CONTACTED', 'REPLIED', 'CLOSED', 'CANCELLED');

-- 7. 修改展会表的展会类型字段为枚举类型（只包含三种类型）
ALTER TABLE exhibition_detail 
MODIFY COLUMN exhibition_type ENUM(
    'ENERGY',    -- 能源展
    'ELEVATOR',  -- 电梯展
    'MEDICAL'    -- 医疗展
) NOT NULL DEFAULT 'ENERGY' COMMENT '展会类型：能源展/电梯展/医疗展';

-- 8. 修改展会报名表的联系进度字段为枚举类型
ALTER TABLE exhibition_registration 
MODIFY COLUMN contact_progress ENUM(
    'UNCONTACTED',  -- 未联系
    'CONTACTED',    -- 已联系
    'REPLIED',      -- 已回复
    'CLOSED',       -- 已成交
    'CANCELLED'     -- 已取消
) NOT NULL DEFAULT 'UNCONTACTED' COMMENT '联系进度';

-- 9. 插入示例数据（可选）
-- 插入三种类型的示例展会数据
INSERT INTO exhibition_detail (name, location, time, year, month, sort, content, country, country_name, exhibition_type) VALUES
('2024北京国际能源展览会', '北京国家会议中心', '2024-05-15 至 2024-05-17', '2024', '05', 1, '专业的能源行业展览会', 1, '中国', 'ENERGY'),
('2024上海国际电梯展览会', '上海新国际博览中心', '2024-06-10 至 2024-06-12', '2024', '06', 2, '国际知名电梯行业展会', 1, '中国', 'ELEVATOR'),
('2024广州国际医疗设备展览会', '广州琶洲国际会展中心', '2024-07-20 至 2024-07-22', '2024', '07', 3, '医疗设备与技术专业展会', 1, '中国', 'MEDICAL')
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- 10. 验证修改结果
SELECT '=== 验证展会类型分布 ===' as verification;
SELECT exhibition_type, COUNT(*) as count 
FROM exhibition_detail 
GROUP BY exhibition_type
ORDER BY count DESC;

SELECT '=== 验证联系进度分布 ===' as verification;
SELECT contact_progress, COUNT(*) as count 
FROM exhibition_registration 
GROUP BY contact_progress
ORDER BY count DESC;

-- 验证字段类型是否正确修改
SELECT 
    TABLE_NAME,
    COLUMN_NAME,
    DATA_TYPE,
    COLUMN_TYPE,
    COLUMN_DEFAULT,
    IS_NULLABLE,
    COLUMN_COMMENT
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_SCHEMA = DATABASE() 
  AND TABLE_NAME IN ('exhibition_detail', 'exhibition_registration')
  AND COLUMN_NAME IN ('exhibition_type', 'contact_progress')
ORDER BY TABLE_NAME, COLUMN_NAME;

-- 执行完成提示
SELECT '=== 数据库枚举字段修改完成 ===' as completion_message; 