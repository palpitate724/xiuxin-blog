# Vue Router 最佳实践

Vue Router 路由配置和使用的最佳实践。

## 路由配置

### 基础配置
```typescript
// router/index.ts
import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    name: 'Home',
    component: () => import('@/views/Home.vue')
  },
  {
    path: '/about',
    name: 'About',
    component: () => import('@/views/About.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
```

### 路由懒加载
```typescript
// ✅ 推荐：使用动态导入实现懒加载
const routes: RouteRecordRaw[] = [
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: () => import('@/views/Dashboard.vue')
  }
]

// ❌ 不推荐：静态导入
import Dashboard from '@/views/Dashboard.vue'
const routes: RouteRecordRaw[] = [
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: Dashboard
  }
]
```

## 路由结构

### 嵌套路由
```typescript
const routes: RouteRecordRaw[] = [
  {
    path: '/admin',
    component: () => import('@/layouts/AdminLayout.vue'),
    children: [
      {
        path: '',
        redirect: '/admin/dashboard'
      },
      {
        path: 'dashboard',
        name: 'AdminDashboard',
        component: () => import('@/views/admin/Dashboard.vue')
      },
      {
        path: 'users',
        name: 'AdminUsers',
        component: () => import('@/views/admin/Users.vue')
      }
    ]
  }
]
```

### 路由模块化
```typescript
// router/modules/dashboard.ts
export default {
  path: '/dashboard',
  name: 'Dashboard',
  component: () => import('@/views/Dashboard.vue'),
  meta: {
    requiresAuth: true,
    title: '仪表盘'
  }
}

// router/modules/user.ts
export default {
  path: '/user',
  name: 'User',
  component: () => import('@/views/User.vue'),
  meta: {
    requiresAuth: true,
    title: '用户管理'
  }
}

// router/index.ts
import dashboard from './modules/dashboard'
import user from './modules/user'

const routes: RouteRecordRaw[] = [
  dashboard,
  user
]
```

## 路由守卫

### 全局前置守卫
```typescript
router.beforeEach((to, from, next) => {
  // 设置页面标题
  document.title = `${to.meta.title || 'App'} - My App`
  
  // 权限检查
  if (to.meta.requiresAuth && !isAuthenticated()) {
    next('/login')
  } else {
    next()
  }
})
```

### 路由独享守卫
```typescript
const routes: RouteRecordRaw[] = [
  {
    path: '/admin',
    component: AdminLayout,
    beforeEnter: (to, from, next) => {
      if (isAdmin()) {
        next()
      } else {
        next('/unauthorized')
      }
    }
  }
]
```

### 组件内守卫
```vue
<script setup>
import { onBeforeRouteLeave, onBeforeRouteUpdate } from 'vue-router'

onBeforeRouteLeave((to, from, next) => {
  if (hasUnsavedChanges) {
    const answer = window.confirm('确定要离开吗？未保存的更改将丢失。')
    if (answer) {
      next()
    } else {
      next(false)
    }
  } else {
    next()
  }
})

onBeforeRouteUpdate((to, from, next) => {
  // 路由参数变化时执行
  fetchData(to.params.id)
  next()
})
</script>
```

## 路由参数

### 动态路由
```typescript
const routes: RouteRecordRaw[] = [
  {
    path: '/user/:id',
    name: 'UserDetail',
    component: () => import('@/views/UserDetail.vue'),
    props: true
  }
]
```

### 查询参数
```vue
<script setup>
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

// 获取查询参数
const page = computed(() => Number(route.query.page) || 1)
const search = computed(() => route.query.search || '')

// 设置查询参数
const updateQuery = (params: any) => {
  router.push({
    query: {
      ...route.query,
      ...params
    }
  })
}
</script>
```

## 路由元信息

### 权限控制
```typescript
const routes: RouteRecordRaw[] = [
  {
    path: '/admin',
    component: AdminLayout,
    meta: {
      requiresAuth: true,
      roles: ['admin'],
      title: '管理后台'
    }
  }
]

router.beforeEach((to, from, next) => {
  const userRoles = getUserRoles()
  const requiredRoles = to.meta.roles as string[]
  
  if (requiredRoles && !requiredRoles.some(role => userRoles.includes(role))) {
    next('/unauthorized')
  } else {
    next()
  }
})
```

### 页面配置
```typescript
const routes: RouteRecordRaw[] = [
  {
    path: '/products',
    component: Products,
    meta: {
      title: '产品列表',
      description: '浏览我们的产品',
      keywords: '产品,商城',
      layout: 'DefaultLayout'
    }
  }
]
```

## 编程式导航

### 基础导航
```typescript
import { useRouter } from 'vue-router'

const router = useRouter()

// 导航到指定路由
router.push('/home')

// 带参数导航
router.push({ name: 'User', params: { id: 123 } })

// 带查询参数
router.push({ path: '/search', query: { q: 'vue' } })

// 替换当前路由
router.replace('/home')

// 前进/后退
router.go(1)
router.go(-1)
```

### 导航守卫处理
```typescript
const navigateWithGuard = async (to: string) => {
  try {
    await router.push(to)
  } catch (error) {
    if (error.name === 'NavigationDuplicated') {
      // 忽略重复导航错误
      console.log('重复导航被忽略')
    } else {
      console.error('导航错误:', error)
    }
  }
}
```

## 路由过渡

### 基础过渡
```vue
<template>
  <router-view v-slot="{ Component }">
    <transition name="fade" mode="out-in">
      <component :is="Component" />
    </transition>
  </router-view>
</template>

<style>
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
```

### 根据路由过渡
```vue
<template>
  <router-view v-slot="{ Component, route }">
    <transition :name="route.meta.transition || 'fade'" mode="out-in">
      <component :is="Component" :key="route.path" />
    </transition>
  </router-view>
</template>
```

## 路由缓存

### keep-alive
```vue
<template>
  <router-view v-slot="{ Component }">
    <keep-alive :include="cachedViews">
      <component :is="Component" />
    </keep-alive>
  </router-view>
</template>

<script setup>
const cachedViews = ref(['Dashboard', 'UserProfile'])
</script>
```

## 错误处理

### 404 处理
```typescript
const routes: RouteRecordRaw[] = [
  // ...其他路由
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/views/NotFound.vue')
  }
]
```

### 路由错误处理
```typescript
router.onError((error) => {
  console.error('路由错误:', error)
  
  if (error.name === 'NavigationDuplicated') {
    // 忽略重复导航
    return
  }
  
  // 处理其他错误
  router.push('/error')
})
```

## 性能优化

### 预加载
```typescript
// 预加载路由组件
const preloadRoute = (routeName: string) => {
  const route = router.resolve({ name: routeName })
  if (route.matched.length > 0) {
    route.matched.forEach(record => {
      if (record.components) {
        Object.values(record.components).forEach(component => {
          if (typeof component === 'function') {
            component()
          }
        })
      }
    })
  }
}

// 在空闲时预加载
requestIdleCallback(() => {
  preloadRoute('Dashboard')
})
```

### 路由懒加载优化
```typescript
// 使用 webpack 魔法注释
const routes: RouteRecordRaw[] = [
  {
    path: '/dashboard',
    component: () => import(/* webpackChunkName: "dashboard" */ '@/views/Dashboard.vue')
  }
]
```

## 最佳实践

### 路由设计原则
1. 路由结构清晰，层级合理
2. 使用懒加载减少初始加载时间
3. 合理使用路由守卫进行权限控制
4. 路由参数和查询参数使用恰当
5. 提供友好的错误页面

### 代码组织
1. 路由配置模块化
2. 路由守卫集中管理
3. 路由元信息统一规范
4. 路由组件按功能分组

### 性能考虑
1. 大型应用使用路由懒加载
2. 合理使用 keep-alive 缓存
3. 预加载重要路由
4. 避免过度嵌套路由

### 安全考虑
1. 敏感路由需要权限验证
2. 路由参数需要验证
3. 防止 XSS 攻击
4. 合理使用路由守卫
