#!/usr/bin/env bash
# 在本机构建三端产物并打成 Release 包：deploy/out/exhibition-<版本>.tar.gz
# 依赖：Node 22 + pnpm 9、JDK 17+、Maven
# 用法：deploy/scripts/release.sh [版本号，默认 git describe]
source "$(dirname "$0")/common.sh"

ROOT=$(cd "$(dirname "$0")/../.." && pwd)
VERSION=${1:-$(git -C "$ROOT" describe --tags --always --dirty)}
NAME="exhibition-$VERSION"
OUT="$ROOT/deploy/out"
STAGE="$OUT/$NAME"

node_major=$(node -p 'process.versions.node.split(".")[0]')
[ "$node_major" -ge 22 ] || die "需要 Node 22+，当前 $(node -v)"
command -v pnpm >/dev/null || die "未找到 pnpm"
command -v mvn >/dev/null || die "未找到 mvn"

pnpm_install() {
  if [ -f pnpm-lock.yaml ]; then pnpm install --frozen-lockfile; else pnpm install; fi
}

log "构建后端 admin.jar"
mvn -q -B -f "$ROOT/admin/pom.xml" -DskipTests package

log "构建管理后台"
(cd "$ROOT/exhibition-admin" && pnpm_install && pnpm build)

log "构建官网"
(cd "$ROOT/exhibition-webside" && pnpm_install && pnpm build)

log "组装 $NAME"
rm -rf "$STAGE" && mkdir -p "$STAGE/app"
cp "$ROOT/admin/target/admin.jar" "$STAGE/app/admin.jar"
cp -R "$ROOT/exhibition-admin/dist" "$STAGE/app/admin-dist"
cp -R "$ROOT/exhibition-webside/.output" "$STAGE/app/webside"
cp "$ROOT/deploy/docker-compose.yml" "$ROOT/deploy/docker-compose.https.yml" \
   "$ROOT/deploy/.env.example" "$ROOT/deploy/README.md" "$STAGE/"
cp -R "$ROOT/deploy/nginx" "$ROOT/deploy/scripts" "$STAGE/"
rm -f "$STAGE/scripts/release.sh"
echo "$VERSION" > "$STAGE/VERSION"

COPYFILE_DISABLE=1 tar --no-xattrs -czf "$OUT/$NAME.tar.gz" -C "$OUT" "$NAME"
rm -rf "$STAGE"
log "完成：$OUT/$NAME.tar.gz（$(du -h "$OUT/$NAME.tar.gz" | cut -f1)）"
log "发布：gh release create $VERSION $OUT/$NAME.tar.gz --title $VERSION"
