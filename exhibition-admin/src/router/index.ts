import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/login/index.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/admin',
    name: 'Layout',
    component: () => import('../layout/index.vue'),
    redirect: '/admin/carousel',
    meta: { requiresAuth: true },
    children: [
      {
        path: 'carousel',
        name: 'Carousel',
        component: () => import('../views/carousel/index.vue'),
        meta: { title: '轮播图管理', requiresAuth: true }
      },
      {
        path: 'news',
        name: 'News',
        component: () => import('../views/news/index.vue'),
        meta: { title: '市场资讯管理', requiresAuth: true }
      },
      {
        path: 'policy',
        name: 'Policy',
        component: () => import('../views/policy/index.vue'),
        meta: { title: '补贴信息管理', requiresAuth: true }
      },
      {
        path: 'exhibition',
        name: 'Exhibition',
        component: () => import('../views/exhibition/index.vue'),
        meta: { title: '展会管理', requiresAuth: true }
      },
      {
        path: 'cases',
        name: 'Cases',
        component: () => import('../views/cases/index.vue'),
        meta: { title: '案例管理', requiresAuth: true }
      }
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/login'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  // 设置页面标题
  document.title = `${to.meta.title || '展会管理系统'}`
  
  const token = localStorage.getItem('token')
  const requiresAuth = to.matched.some(record => record.meta.requiresAuth)

  if (requiresAuth && !token) {
    // 需要认证但没有token，跳转到登录页
    next({
      path: '/login',
      query: { redirect: to.fullPath }
    })
  } else if (to.path === '/login' && token) {
    // 已登录但访问登录页，跳转到首页
    next('/admin')
  } else {
    next()
  }
})

export default router 