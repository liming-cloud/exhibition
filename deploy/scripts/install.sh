#!/usr/bin/env bash
# 安装（或升级到）一个 Release 包并启动，本机与服务器通用
# 用法：install.sh <exhibition-xxx.tar.gz 路径或下载地址>
#   EXH_HOME 指定安装目录，默认 /opt/exhibition（本机建议 ~/exhibition-local）
source "$(dirname "$0")/common.sh"

SRC=${1:?用法：install.sh <Release 包路径或 URL>}
command -v docker >/dev/null || die "未安装 Docker（服务器请先执行 server-init.sh）"
mkdir -p "$EXH_HOME/releases"

if [[ "$SRC" =~ ^https?:// ]]; then
  log "下载 $SRC"
  TMP=$(mktemp); trap 'rm -f "$TMP"' EXIT
  curl -fL --retry 3 -o "$TMP" "$SRC"
  SRC=$TMP
fi

NAME=$(tar -tzf "$SRC" | head -1 | cut -d/ -f1)
[[ "$NAME" == exhibition-* ]] || die "不是有效的 Release 包"
log "解压 $NAME"
rm -rf "$EXH_HOME/releases/$NAME"
tar -xzf "$SRC" -C "$EXH_HOME/releases"

if [ ! -f "$EXH_HOME/.env" ]; then
  log "首次安装，生成 $EXH_HOME/.env（随机数据库密码）"
  sed "s/^DB_PASSWORD=.*/DB_PASSWORD=$(openssl rand -hex 16)/" \
    "$EXH_HOME/releases/$NAME/.env.example" > "$EXH_HOME/.env"
  chmod 600 "$EXH_HOME/.env"
fi

PREV=$(readlink "$EXH_HOME/current" 2>/dev/null || true)
ln -sfn "releases/$NAME" "$EXH_HOME/current"
log "切换版本：${PREV:-无} -> releases/$NAME"

dc pull -q
dc up -d --remove-orphans --force-recreate
wait_healthy

# 只保留最近 3 个版本
for old in $(ls -1dt "$EXH_HOME"/releases/exhibition-* | tail -n +4); do rm -rf "$old"; done
log "安装完成。回滚：EXH_HOME=$EXH_HOME $0 <旧版本包>，或 ln -sfn ${PREV:-<旧版本目录>} $EXH_HOME/current 后执行 dc up -d"
