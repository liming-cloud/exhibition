#!/usr/bin/env bash
# 给静态生成的原型站点逐页截图（桌面 1440 + 手机 375，整页），结果放在站点的 screenshots/ 下并生成 index.html 画廊
# 用法：screenshots.sh <静态站点目录>（站点按 /exhibition/ 子路径生成）
set -euo pipefail

SITE=$(cd "$1" && pwd)
OUT="$SITE/screenshots"
BASE=http://localhost:4173/exhibition
mkdir -p "$OUT"

# 按 GitHub Pages 的子路径在本地起静态服务
ROOT=$(mktemp -d)
ln -s "$SITE" "$ROOT/exhibition"
python3 -m http.server 4173 -d "$ROOT" >/dev/null 2>&1 &
SERVER=$!
trap 'kill $SERVER' EXIT
sleep 1

npx -y playwright@1 install --with-deps chromium >/dev/null

# 名称|路径
PAGES=(
  "01-首页|/"
  "02-展会列表|/exhibitions"
  "03-展会详情|/exhibitions/1"
  "04-市场简讯|/news"
  "05-市场简讯详情|/news/1"
  "06-成功案例|/cases"
  "07-案例详情|/cases/1"
  "08-补贴政策|/subsidies"
  "09-补贴详情|/subsidies/1"
  "10-关于我们|/about"
  "11-联系我们|/contact"
  "12-设计规范|/styleguide"
)

ROWS=""
for item in "${PAGES[@]}"; do
  name=${item%%|*}
  path=${item#*|}
  for size in 1440,900 375,812; do
    file="$name-${size%%,*}.png"
    npx -y playwright@1 screenshot --full-page --viewport-size="$size" --wait-for-timeout=2500 \
      "$BASE$path" "$OUT/$file" >/dev/null
  done
  # 手机截图宽度超过 375 说明页面有横向溢出
  width=$(python3 -c 'import struct,sys; f=open(sys.argv[1],"rb"); f.read(16); print(struct.unpack(">I", f.read(4))[0])' "$OUT/$name-375.png")
  warn=""
  if [ "$width" -gt 375 ]; then
    echo "::warning::$name 手机端横向溢出：页面宽 ${width}px"
    warn="<span class=\"warn\">手机端横向溢出（${width}px）</span>"
  fi
  ROWS+="<section><h2>$name <a href=\"..$path\" target=\"_blank\">打开页面</a>$warn</h2>"
  ROWS+="<div class=\"pair\"><img src=\"$name-1440.png\" loading=\"lazy\"><img class=\"m\" src=\"$name-375.png\" loading=\"lazy\"></div></section>"
done

cat > "$OUT/index.html" <<EOF
<!doctype html>
<html lang="zh-CN"><head><meta charset="utf-8"><title>原型截图</title>
<style>
body{font-family:-apple-system,"PingFang SC","Microsoft YaHei",sans-serif;margin:24px;background:#f4f4f5;color:#18181b}
h1{font-size:22px}h2{font-size:16px;margin:32px 0 12px}h2 a{font-size:13px;margin-left:12px}
.warn{color:#dc2626;font-size:13px;margin-left:12px}.pair{display:flex;gap:16px;align-items:flex-start}.pair img{width:72%;border:1px solid #d4d4d8;background:#fff}.pair img.m{width:22%}
</style></head><body>
<h1>官网原型截图（桌面 1440 / 手机 375）</h1>
<p>生成时间：$(TZ=Asia/Shanghai date '+%Y-%m-%d %H:%M')，提交 ${GITHUB_SHA:-local}。图片为整页截图，点「打开页面」可直接操作。</p>
$ROWS
</body></html>
EOF
echo "截图完成：$(ls "$OUT"/*.png | wc -l) 张"
