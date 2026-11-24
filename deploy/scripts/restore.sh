#!/usr/bin/env bash
# 导入数据库导出文件与上传文件到当前运行的环境（会覆盖同名表）
# 用法：restore.sh <exhibition.sql> [uploads.tgz]
source "$(dirname "$0")/common.sh"

SQL=${1:?用法：restore.sh <exhibition.sql> [uploads.tgz]}
UPLOADS=${2:-}
[ -f "$SQL" ] || die "找不到 $SQL"

log "导入数据库 $SQL"
dc exec -T mysql sh -c 'exec mysql -uroot -p"$MYSQL_ROOT_PASSWORD"' < "$SQL"

if [ -n "$UPLOADS" ]; then
  [ -f "$UPLOADS" ] || die "找不到 $UPLOADS"
  log "恢复上传文件 $UPLOADS"
  dc exec -T admin-api sh -c 'tar -xzf - -C "$UPLOAD"' < "$UPLOADS"
fi

dc restart admin-api
wait_healthy
log "数据核对（行数）"
for t in exhibition_detail global_area cases contact_info carousel; do
  printf '  %-20s %s\n' "$t" "$(dc exec -T mysql sh -c "exec mysql -uroot -p\"\$MYSQL_ROOT_PASSWORD\" -N -e 'SELECT COUNT(*) FROM exhibition.$t'" 2>/dev/null)"
done
