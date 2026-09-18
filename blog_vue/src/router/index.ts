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
  routes
})

export default router
