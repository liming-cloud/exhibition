# 展会官网

基于Nuxt3开发的展会官网项目，提供展会信息、商旅服务、补贴政策等功能。

## 功能特点

- 响应式设计，支持多端访问
- 展会信息全球地图可视化
- 实时展会数据更新
- 在线咨询服务
- 补贴政策查询
- 市场资讯浏览

## 技术栈

- Nuxt3
- Vue3
- TypeScript
- TailwindCSS
- Element Plus
- Pinia
- Leaflet
- Vue3-Carousel

## 开发环境要求

- Node.js 16.x 或更高版本
- PNPM 7.x 或更高版本

## 安装和运行

1. 克隆项目
```bash
git clone [项目地址]
cd exhibition-webside
```

2. 安装依赖
```bash
pnpm install
```

3. 开发环境运行
```bash
pnpm dev
```

4. 生产环境构建
```bash
pnpm build
```

5. 预览生产构建
```bash
pnpm preview
```

## 项目结构

```
exhibition-webside/
├── assets/           # 静态资源
├── components/       # 组件
├── composables/      # 组合式函数
├── layouts/          # 布局
├── pages/           # 页面
├── public/          # 公共文件
├── stores/          # 状态管理
└── types/           # 类型定义
```

## 环境变量

项目使用以下环境变量：

- `NUXT_PUBLIC_API_BASE`: API基础URL
- `NUXT_APP_TITLE`: 应用标题
- `NUXT_APP_DESCRIPTION`: 应用描述

## 开发指南

1. 代码规范
   - 使用ESLint进行代码检查
   - 使用Prettier进行代码格式化
   - 提交前运行 `pnpm lint` 检查代码

2. 组件开发
   - 遵循Vue3组合式API规范
   - 使用TypeScript进行类型检查
   - 组件名使用PascalCase命名

3. 样式开发
   - 使用TailwindCSS工具类
   - 遵循移动优先原则
   - 使用CSS变量进行主题定制

## 部署

1. 构建生产版本
```bash
pnpm build
```

2. 将 `.output` 目录部署到服务器

## 贡献指南

1. Fork 项目
2. 创建特性分支
3. 提交变更
4. 推送到分支
5. 创建Pull Request

## 许可证

[MIT License](LICENSE)
