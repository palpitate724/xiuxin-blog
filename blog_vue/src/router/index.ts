import { createRouter, createWebHistory } from 'vue-router'

import user from '../views/user.vue'
import login from '../components/user/login.vue'
import logo from '../components/user/logo.vue'
import signup from '../components/user/signup.vue'

import zhuye from '../views/zhuye.vue'

const routes = [
  {
    path: '/',
    name: 'user',
    component: user,
    children: [
      {
        path: 'login',
        name: 'login',
        component: login
      },
      {
        path: 'logo',
        name: 'logo',
        component: logo
      },
      {
        path: 'signup',
        name: 'signup',
        component: signup
      }
    ]
  },
  {
    path: '/zhuye',
    name: 'zhuye',
    component: zhuye,
    children: []
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
