#!/usr/bin/env bash
# 服务器一次性初始化（可重复执行）：安装 Docker、配置镜像加速、添加 swap、安装 HTTPS 证书
# 适用：Alibaba Cloud Linux 3 / RHEL 8 系
# 用法：DOMAIN=topexpo.com.cn server-init.sh
source "$(dirname "$0")/common.sh"

DOMAIN=${DOMAIN:?请设置 DOMAIN，例如 DOMAIN=topexpo.com.cn}
CERT_DIR="$EXH_HOME/certs"
ACME="$HOME/.acme.sh/acme.sh"
[ "$(id -u)" = 0 ] || die "请用 root 执行"

if ! command -v docker >/dev/null; then
  log "安装 Docker CE（阿里云镜像源）"
  dnf -y install dnf-plugins-core
  dnf config-manager --add-repo https://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
  # Alibaba Cloud Linux 3 的 $releasever 为 3，docker-ce 源需按 EL8 访问
  sed -i 's/\$releasever/8/g' /etc/yum.repos.d/docker-ce.repo
  dnf -y install docker-ce docker-ce-cli containerd.io docker-compose-plugin
fi

if [ ! -f /etc/docker/daemon.json ]; then
  log "配置 Docker 镜像加速与日志轮转"
  mkdir -p /etc/docker
  cat > /etc/docker/daemon.json <<'JSON'
{
  "registry-mirrors": ["https://docker.m.daocloud.io"],
  "log-driver": "json-file",
  "log-opts": { "max-size": "20m", "max-file": "3" }
}
JSON
fi
systemctl enable --now docker
systemctl restart docker

if [ -z "$(swapon --show)" ]; then
  log "添加 2G swap"
  fallocate -l 2G /swapfile && chmod 600 /swapfile && mkswap /swapfile && swapon /swapfile
  grep -q '^/swapfile' /etc/fstab || echo '/swapfile none swap defaults 0 0' >> /etc/fstab
fi

log "安装 $DOMAIN 证书到 $CERT_DIR（acme.sh 续期后自动重载 nginx 容器）"
[ -x "$ACME" ] || die "未找到 $ACME"
mkdir -p "$CERT_DIR"
"$ACME" --install-cert -d "$DOMAIN" \
  --key-file "$CERT_DIR/privkey.pem" \
  --fullchain-file "$CERT_DIR/fullchain.pem" \
  --reloadcmd "docker exec exhibition-nginx-1 nginx -s reload || true"

mkdir -p "$EXH_HOME"
if [ ! -f "$EXH_HOME/.env" ]; then
  log "生成 $EXH_HOME/.env（HTTPS）"
  cat > "$EXH_HOME/.env" <<ENV
DB_PASSWORD=$(openssl rand -hex 16)
HTTP_PORT=80
COMPOSE_FILE=docker-compose.yml:docker-compose.https.yml
CERT_DIR=$CERT_DIR
ENV
  chmod 600 "$EXH_HOME/.env"
fi
docker version --format 'Docker {{.Server.Version}}'
log "初始化完成"
