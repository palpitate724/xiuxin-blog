import { createRouter, createWebHistory } from 'vue-router'

import user from '../views/user.vue'
import zhuye from '../views/zhuye.vue'

const routes = [
  {
    path: '/',
    name: 'user',
    component: user
  },
  {
    path: '/zhuye',
    name: 'zhuye',
    component: zhuye
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
