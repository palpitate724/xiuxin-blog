import { createRouter, createWebHistory } from 'vue-router'

import user from '../views/user.vue'
import zhuye from '../views/zhuye.vue'
import article from '../views/article.vue'

const routes = [
  {
    path: '/',
    name: 'zhuye',
    component: zhuye
  },
  {
    path: '/user',
    name: 'user',
    component: user
  },
  {
    path: '/article/:id',
    name: 'article',
    component: article
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  // 导航时滚动至页面顶部（封面区域）
  scrollBehavior(_to, _from, savedPosition) {
    // 恢复历史滚动位置
    if (savedPosition) {
      return savedPosition
    }
    // 路由跳转时定位至页面顶部（封面展示位置）
    return { top: 0 }
  }
})

export default router
