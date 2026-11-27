# 部署说明

只有一套部署方式：**Release 包 + Docker Compose**，本机和服务器完全一致。

- Release 包（`exhibition-<版本>.tar.gz`）只包含构建产物和运行配置：`app/admin.jar`、`app/webside`（Nuxt 产物）、`app/admin-dist`（后台静态文件）、compose、nginx 配置、运维脚本。
- 运行时只使用官方基础镜像：`mysql:8.0.40`、`eclipse-temurin:17-jre`、`node:22-alpine`、`nginx:1.27-alpine`。服务器上不拉代码、不构建。

| 服务 | 说明 |
|---|---|
| `nginx` | 唯一对外入口：`/` 官网，`/admin/` 后台，`/api/` 后端（去掉前缀转发）；服务器上额外监听 443 |
| `webside` | 官网 Nuxt SSR，`NUXT_API_BASE` 指向后端 |
| `admin-api` | Spring Boot 后端；上传文件存在 `upload-data` 卷（容器内路径沿用旧服务器路径，数据库里存的是这个绝对路径） |
| `mysql` | MySQL 8.0.40，`mysql-data` 卷，不对外暴露端口 |

## 安装目录

```
$EXH_HOME/                 服务器 /opt/exhibition，本机建议 ~/exhibition-local
├── .env                   密码与端口（首次安装自动生成，权限 600）
├── certs/                 HTTPS 证书（仅服务器，由 acme.sh 安装和续期）
├── releases/exhibition-*  最近 3 个版本
└── current -> releases/…  当前版本
```

## 打包与发布（本机）

需要 Node 22 + pnpm 9、JDK 17、Maven。

```bash
deploy/scripts/release.sh v1.0.0                 # 生成 deploy/out/exhibition-v1.0.0.tar.gz
gh release create v1.0.0 deploy/out/exhibition-v1.0.0.tar.gz --title v1.0.0
```

## 安装 / 升级 / 回滚

```bash
export EXH_HOME=~/exhibition-local               # 服务器省略，默认 /opt/exhibition
# 本机首次安装前可先写 $EXH_HOME/.env，设置 HTTP_PORT=8088 避免占用 80
scripts/install.sh <Release 包路径或下载地址>     # 安装或升级，完成后会等待健康检查通过
scripts/install.sh <旧版本包>                     # 回滚 = 安装旧版本
scripts/restore.sh exhibition.sql uploads.tgz    # 导入数据库导出文件与上传文件（覆盖同名表）
```

日常命令（`source scripts/common.sh` 后可用 `dc`，等同于对当前版本执行 docker compose）：

```bash
dc ps
dc logs -f admin-api
dc exec mysql sh -c 'mysqldump -uroot -p"$MYSQL_ROOT_PASSWORD" --databases exhibition' > backup.sql
```

## 服务器首次切换（替换旧部署）

旧部署是主机上的 nginx + systemd 后端 + pm2 官网 + MySQL。按下面顺序切换，停机时间大约是第 3～5 步（几分钟）。

1. **初始化**（不影响线上）：安装 Docker、配置镜像加速、添加 2G swap、把证书安装到 `/opt/exhibition/certs`，并生成启用 HTTPS 的 `.env`。
   ```bash
   DOMAIN=topexpo.com.cn scripts/server-init.sh
   ```
2. **预检**（只读）：`scripts/cleanup-legacy.sh plan`，核对要停用、删除和卸载的内容。
3. **停旧**：`scripts/cleanup-legacy.sh stop`。先备份到 `/root/legacy-backup-<时间>/`（数据库导出、上传文件、/bash、nginx 配置、MySQL 数据目录），再停用旧服务。
4. **装新**：`scripts/install.sh https://github.com/liming-cloud/exhibition/releases/download/<版本>/exhibition-<版本>.tar.gz`
5. **导数据**：`scripts/restore.sh /root/legacy-backup-<时间>/exhibition.sql /root/legacy-backup-<时间>/uploads.tgz`
   - 旧库结构比新版本旧，导入后按 `sql/README.md` 依次执行迁移（Release 包里带 `sql/`），例如：
     `dc exec -T mysql sh -c 'mysql -uroot -p"$MYSQL_ROOT_PASSWORD" exhibition' < sql/migrations/001-dates-and-create-time.sql`
6. **验证**：访问 https://topexpo.com.cn/ 和 /admin/，检查展会、案例、图片。
   - 有问题就回滚：先 `dc down`，再执行 `scripts/cleanup-legacy.sh rollback`。
7. **清理**（不可逆）：`scripts/cleanup-legacy.sh purge`。会检查新环境已在运行且有数据，然后要求输入 `PURGE` 确认，删除旧文件并卸载 nginx、mysql-community、java-17、nodejs、pm2/pnpm。保留 acme.sh 证书续期和备份目录。

`deploy/local-data/`（线上数据导出）、`deploy/out/`（构建产物）和 `.env` 都已加入 .gitignore，不要提交。
