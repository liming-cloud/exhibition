#!/usr/bin/env bash
# 清理服务器上的旧部署（主机 nginx + systemd 后端 + pm2 官网 + 主机 MySQL），只保留 Docker 这一套
# 用法：
#   cleanup-legacy.sh plan       只读：列出将要处理的内容（默认）
#   cleanup-legacy.sh stop       备份到 /root/legacy-backup-<时间>/ 后停用旧服务（可 rollback）
#   cleanup-legacy.sh rollback   恢复旧服务（仅 stop 之后、purge 之前有效）
#   cleanup-legacy.sh purge      确认新环境正常后，删除旧文件并卸载软件包（不可逆）
# stop 需要旧库 root 密码：LEGACY_DB_PASSWORD 环境变量，未设置时交互输入
source "$(dirname "$0")/common.sh"

# ---- 旧部署清单（2026-11 盘点） ----
LEGACY_SERVICES=(admin nginx mysqld pm2-root)
LEGACY_PM2_APP=webside-app
LEGACY_UNIT_FILES=(/etc/systemd/system/admin.service)
LEGACY_UPLOAD_DIR=/Users/sunliming/work/text
LEGACY_PATHS=(/bash /exhibition /Users/sunliming /etc/nginx /etc/letsencrypt /var/lib/mysql /var/log/mysqld.log /var/log/nginx /root/.pm2 /root/.local/share/pnpm)
LEGACY_PACKAGES=('nginx*' 'mysql-community-*' 'java-17-openjdk*' nodejs)
# 保留：/root/.acme.sh 与其 cron（证书续期）、Docker、云厂商 agent

ACTION=${1:-plan}
[ "$(id -u)" = 0 ] || die "请用 root 执行"
LATEST_BACKUP=$(ls -1dt /root/legacy-backup-* 2>/dev/null | head -1 || true)

installed_packages() {
  local p
  for p in "${LEGACY_PACKAGES[@]}"; do rpm -qa "$p"; done | sort -u
}

plan() {
  log "服务（systemctl is-active / is-enabled）"
  for s in "${LEGACY_SERVICES[@]}"; do
    printf '  %-10s %s / %s\n' "$s" "$(systemctl is-active "$s" 2>/dev/null || true)" "$(systemctl is-enabled "$s" 2>/dev/null || true)"
  done
  log "pm2 进程"
  if command -v pm2 >/dev/null; then pm2 jlist 2>/dev/null | grep -o '"name":"[^"]*"' | sort -u | sed 's/^/  /' || true; else echo "  （pm2 未安装）"; fi
  log "将删除的路径"
  for p in "${LEGACY_UNIT_FILES[@]}" "${LEGACY_PATHS[@]}"; do
    [ -e "$p" ] && printf '  %-32s %s\n' "$p" "$(du -sh "$p" 2>/dev/null | cut -f1)"
  done
  log "将卸载的软件包（含依赖它们的包，不自动清理其他依赖）"
  mapfile -t pkgs < <(installed_packages)
  if [ ${#pkgs[@]} -gt 0 ]; then
    dnf remove --noautoremove --assumeno "${pkgs[@]}" 2>/dev/null | grep -E '^ [a-zA-Z]' | awk '{print "  " $1 " " $3}' || true
  fi
  log "最近的备份：${LATEST_BACKUP:-无}"
}

stop() {
  local backup="/root/legacy-backup-$(date +%Y%m%d-%H%M%S)"
  mkdir -p "$backup" && chmod 700 "$backup"
  log "备份到 $backup"
  if [ -z "${LEGACY_DB_PASSWORD:-}" ]; then read -rsp "旧 MySQL root 密码：" LEGACY_DB_PASSWORD; echo; fi
  MYSQL_PWD="$LEGACY_DB_PASSWORD" mysqldump -uroot --single-transaction --default-character-set=utf8mb4 \
    --set-gtid-purged=OFF --databases exhibition > "$backup/exhibition.sql"
  [ -d "$LEGACY_UPLOAD_DIR" ] && tar -czf "$backup/uploads.tgz" -C "$LEGACY_UPLOAD_DIR" .
  crontab -l > "$backup/crontab.txt" 2>/dev/null || true
  local existing=()
  for p in /bash /etc/nginx /etc/letsencrypt /root/.pm2/dump.pm2 "${LEGACY_UNIT_FILES[@]}"; do [ -e "$p" ] && existing+=("$p"); done
  tar -czf "$backup/legacy-files.tgz" --exclude=/bash/acme.sh "${existing[@]}"

  log "停用旧服务"
  if command -v pm2 >/dev/null; then pm2 stop "$LEGACY_PM2_APP" >/dev/null 2>&1 || true; fi
  for s in "${LEGACY_SERVICES[@]}"; do systemctl disable --now "$s" 2>/dev/null || true; done

  # stop 时 MySQL 已停，再整体打包数据目录作为兜底
  [ -d /var/lib/mysql ] && tar -czf "$backup/mysql-datadir.tgz" -C /var/lib mysql
  ls -la "$backup"
  log "旧服务已停用。下一步：install.sh 安装新版本，再执行 restore.sh $backup/exhibition.sql $backup/uploads.tgz"
}

rollback() {
  if docker ps --format '{{.Names}}' 2>/dev/null | grep -q '^exhibition-nginx-1$'; then
    die "新环境仍在运行并占用 80/443，请先执行：EXH_HOME=$EXH_HOME bash -c 'source $(dirname "$0")/common.sh; dc down'"
  fi
  [ -d /bash ] || die "旧文件已被 purge，无法自动回滚，请从 ${LATEST_BACKUP:-备份} 手工恢复"
  for s in mysqld admin nginx pm2-root; do systemctl enable --now "$s"; done
  pm2 start "$LEGACY_PM2_APP" >/dev/null 2>&1 || pm2 resurrect
  log "旧服务已恢复"
}

purge() {
  [ -n "$LATEST_BACKUP" ] && [ -s "$LATEST_BACKUP/exhibition.sql" ] || die "未找到 stop 生成的备份，请先执行 stop"
  log "检查新环境"
  dc ps --status running --services | grep -qx nginx || die "新环境 nginx 未运行"
  local rows
  rows=$(dc exec -T mysql sh -c 'exec mysql -uroot -p"$MYSQL_ROOT_PASSWORD" -N -e "SELECT COUNT(*) FROM exhibition.exhibition_detail"' 2>/dev/null) \
    || die "新环境数据库中没有 exhibition 数据，请先执行 restore.sh"
  log "新环境展会数据 $rows 条，备份位于 $LATEST_BACKUP"
  plan
  if [ "${CONFIRM:-}" != PURGE ]; then
    read -rp "以上内容将被永久删除/卸载，输入 PURGE 确认：" CONFIRM
    [ "$CONFIRM" = PURGE ] || die "已取消"
  fi

  if command -v pm2 >/dev/null; then
    pm2 delete all >/dev/null 2>&1 || true
    pm2 unstartup systemd >/dev/null 2>&1 || true
    pm2 kill >/dev/null 2>&1 || true
  fi
  for s in "${LEGACY_SERVICES[@]}"; do systemctl disable --now "$s" 2>/dev/null || true; done
  rm -f "${LEGACY_UNIT_FILES[@]}" /etc/systemd/system/pm2-root.service
  systemctl daemon-reload

  mapfile -t pkgs < <(installed_packages)
  [ ${#pkgs[@]} -gt 0 ] && dnf -y remove --noautoremove "${pkgs[@]}"

  rm -rf "${LEGACY_PATHS[@]}"
  rmdir /Users 2>/dev/null || true
  sed -i.bak '/PNPM_HOME\|share\/pnpm/d' /root/.bashrc
  log "清理完成。备份保留在 $LATEST_BACKUP，确认无误后可自行删除"
}

case "$ACTION" in
  plan|stop|rollback|purge) "$ACTION" ;;
  *) die "未知操作 $ACTION（plan|stop|rollback|purge）" ;;
esac
