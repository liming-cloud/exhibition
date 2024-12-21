# 展会管理系统

基于 Vue 3 + TypeScript + Vite + Element Plus 开发的展会管理系统。

## 功能特性

- 轮播图管理：支持图片上传、排序和状态管理
- 市场资讯管理：支持图文内容发布、编辑和状态管理
- 补贴信息管理：支持补贴信息发布、时间管理和状态控制
- 展会管理：支持展会信息管理、文档上传和状态跟踪

## 开发环境

- Node.js >= 16
- pnpm >= 8

## 项目设置

```bash
# 安装依赖
pnpm install

# 启动开发服务器
pnpm dev

# 构建生产版本
pnpm build
```

## 项目结构

```
src/
├── assets/        # 静态资源
├── components/    # 公共组件
├── layout/        # 布局组件
├── router/        # 路由配置
├── stores/        # 状态管理
└── views/         # 页面组件
    ├── carousel/  # 轮播图管理
    ├── news/      # 市场资讯管理
    ├── policy/    # 补贴信息管理
    └── exhibition/# 展会管理
```

## 开发规范

- 使用 TypeScript 编写代码
- 使用 Element Plus 组件库
- 遵循 Vue 3 组合式 API 风格
- 使用 ESLint 进行代码检查

## 默认账号

- 用户名：admin
- 密码：123456
