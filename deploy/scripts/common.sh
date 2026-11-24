# 公共变量与函数，由其他脚本 source
set -euo pipefail

EXH_HOME="${EXH_HOME:-/opt/exhibition}"

log() { printf '\033[1;34m>>>\033[0m %s\n' "$*"; }
die() { printf '\033[1;31m错误：\033[0m%s\n' "$*" >&2; exit 1; }

# 对当前版本执行 docker compose（统一项目名、目录与 .env）
dc() {
  [ -e "$EXH_HOME/current" ] || die "$EXH_HOME/current 不存在，请先用 install.sh 安装"
  docker compose -p exhibition --project-directory "$EXH_HOME/current" --env-file "$EXH_HOME/.env" "$@"
}

# 等待后端经 nginx 可访问
wait_healthy() {
  local port
  port=$(sed -n 's/^HTTP_PORT=//p' "$EXH_HOME/.env"); port=${port:-80}
  for _ in $(seq 1 60); do
    if curl -fs -o /dev/null "http://127.0.0.1:$port/api/actuator/health"; then
      log "服务已就绪：http://127.0.0.1:$port/"
      return 0
    fi
    sleep 3
  done
  dc ps
  die "等待服务就绪超时，请查看 dc logs"
}
