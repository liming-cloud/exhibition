import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'

const routes: Array<RouteRecordRaw> = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/',
    component: () => import('@/views/layout/index.vue'),
    redirect: '/carousel',
    children: [
      {
        path: 'carousel',
        name: 'Carousel',
        component: () => import('@/views/carousel/index.vue'),
        meta: { title: '轮播图管理' }
      },
      {
        path: 'news',
        name: 'News',
        component: () => import('@/views/news/index.vue'),
        meta: { title: '资讯管理' }
      },
      {
        path: 'exhibition',
        name: 'Exhibition',
        component: () => import('@/views/exhibition/index.vue'),
        meta: { title: '展会信息管理' }
      },
      {
        path: 'subsidy',
        name: 'Subsidy',
        component: () => import('@/views/subsidy/index.vue'),
        meta: { title: '补贴政策管理' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const isAuthenticated = localStorage.getItem('isAuthenticated')
  if (to.path !== '/login' && !isAuthenticated) {
    next('/login')
  } else {
    next()
  }
})

export default router 