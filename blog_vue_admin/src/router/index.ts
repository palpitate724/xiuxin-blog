import type { RouteRecordRaw } from 'vue-router'
import { createRouter, createWebHistory } from 'vue-router'

// ============================================================================
// 路由结构: 两个模块 — zhuye + user
// ============================================================================
// zhuye 模块: admin 布局壳 (header + sidebar + router-view)
//   /          → 重定向到 /zhuye
//   /zhuye      → zhuye.vue (admin 布局)
//   /zhuye/yibiaopan → yibiaopan.vue (仪表盘)
//   /zhuye/art      → art.vue (文章管理)
//   /zhuye/cat       → cat.vue (分类管理)
//   /zhuye/tag       → tag.vue (标签管理)
//
// user 模块: 用户管理 (独立页面, 无 admin 布局)
//   /user          → uesrls.vue (用户管理 / 显示 Login 组件)
// ============================================================================

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    redirect: '/zhuye',
  },
  // ── zhuye 模块 (管理后台布局) ──
  {
    path: '/zhuye',
    component: () => import('@/views/zhuye.vue'),
    children: [
      {
        path: '',
        redirect: { name: 'yibiaopan' },
      },
      {
        path: 'yibiaopan',
        name: 'yibiaopan',
        component: () => import('@/components/zhuye/yibiaopan.vue'),
      },
      {
        path: 'art',
        name: 'art',
        component: () => import('@/components/zhuye/art.vue'),
      },
      {
        path: 'cat',
        name: 'cat',
        component: () => import('@/components/zhuye/cat.vue'),
      },
      {
        path: 'tag',
        name: 'tag',
        component: () => import('@/components/zhuye/tag.vue'),
      },
    ],
  },
  // ── user 模块 (用户管理 / 登录) ──
  {
    path: '/user',
    name: 'user',
    component: () => import('@/views/uesrls.vue'),
  },
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
  // 导航时滚动至页面顶部
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition
    }
    if (to.hash) {
      return { el: to.hash, behavior: 'smooth' }
    }
    return { top: 0 }
  },
})

export default router
