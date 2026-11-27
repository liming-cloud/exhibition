# 数据库脚本

| 文件 | 用途 |
|---|---|
| `schema.sql` | 完整表结构，唯一来源；新建空库时执行 |
| `migrations/NNN-*.sql` | 已有库的增量迁移，按编号顺序执行，每份只执行一次 |
| `migrations/NNN-*.rollback.sql` | 对应迁移的回滚 |

## 线上执行顺序

1. 备份：`mysqldump --databases exhibition > backup-<时间>.sql`（Docker 部署见 `deploy/README.md`）
2. 依次执行未执行过的迁移：`mysql exhibition < migrations/001-dates-and-create-time.sql`
3. 部署对应版本的后端、管理端、官网
4. 出问题时：先回退应用版本，再执行对应的 `.rollback.sql`

| 迁移 | Issue | 需要的应用版本 |
|---|---|---|
| 001-dates-and-create-time | #44 | 含 #44 的版本（展会改用 startDate / endDate） |

改表时：同时修改 `schema.sql`，并新增一份迁移和回滚。
