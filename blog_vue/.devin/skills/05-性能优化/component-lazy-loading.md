# Component Lazy Loading

组件懒加载技能，优化应用加载性能。

## 基础懒加载

### defineAsyncComponent
```vue
<script setup>
import { defineAsyncComponent } from 'vue'

// 基础懒加载
const LazyComponent = defineAsyncComponent(() =>
  import('./HeavyComponent.vue')
)

// 带错误处理的懒加载
const LazyComponentWithError = defineAsyncComponent({
  loader: () => import('./HeavyComponent.vue'),
  loadingComponent: LoadingComponent,
  errorComponent: ErrorComponent,
  delay: 200,
  timeout: 3000
})
</script>
```

### 路由懒加载
```typescript
// router/index.ts
const routes = [
  {
    path: '/dashboard',
    component: () => import('@/views/Dashboard.vue')
  },
  {
    path: '/users',
    component: () => import('@/views/Users.vue')
  }
]
```

## 高级懒加载

### 条件懒加载
```vue
<template>
  <div>
    <button @click="showComponent = true">加载组件</button>
    
    <LazyComponent v-if="showComponent" />
  </div>
</template>

<script setup>
import { ref, defineAsyncComponent } from 'vue'

const showComponent = ref(false)

const LazyComponent = defineAsyncComponent(() =>
  import('./LazyComponent.vue')
)
</script>
```

### 交互式懒加载
```vue
<template>
  <div>
    <button @click="loadComponent">按需加载</button>
    
    <component :is="loadedComponent" v-if="loadedComponent" />
  </div>
</template>

<script setup>
import { ref, shallowRef } from 'vue'

const loadedComponent = shallowRef(null)

const loadComponent = async () => {
  const component = await import('./DynamicComponent.vue')
  loadedComponent.value = component.default
}
</script>
```

## 懒加载策略

### 预加载策略
```typescript
// 空闲时预加载
const preloadComponent = (componentPath: string) => {
  if ('requestIdleCallback' in window) {
    requestIdleCallback(() => {
      import(componentPath)
    })
  } else {
    setTimeout(() => {
      import(componentPath)
    }, 2000)
  }
}

// 预加载关键组件
preloadComponent('@/views/Dashboard.vue')
preloadComponent('@/components/Chart.vue')
```

### 视口懒加载
```vue
<template>
  <div>
    <LazyComponent v-if="isVisible" />
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { defineAsyncComponent } from 'vue'

const LazyComponent = defineAsyncComponent(() =>
  import('./LazyComponent.vue')
)

const isVisible = ref(false)
let observer: IntersectionObserver | null = null

onMounted(() => {
  observer = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
      if (entry.isIntersecting) {
        isVisible.value = true
        observer?.disconnect()
      }
    })
  })
  
  observer.observe(document.querySelector('.lazy-component-trigger')!)
})

onUnmounted(() => {
  observer?.disconnect()
})
</script>
```

## 性能监控

### 懒加载性能分析
```typescript
// 监控组件加载时间
const monitorComponentLoad = async (componentPath: string) => {
  const startTime = performance.now()
  
  try {
    const component = await import(componentPath)
    const endTime = performance.now()
    
    console.log(`组件 ${componentPath} 加载时间: ${endTime - startTime}ms`)
    
    return component
  } catch (error) {
    console.error(`组件 ${componentPath} 加载失败:`, error)
    throw error
  }
}
```

### 加载状态管理
```vue
<template>
  <div>
    <div v-if="loading" class="loading-state">
      <LoadingSpinner />
      <p>加载中...</p>
    </div>
    
    <div v-else-if="error" class="error-state">
      <ErrorIcon />
      <p>加载失败</p>
      <button @click="retry">重试</button>
    </div>
    
    <LazyComponent v-else />
  </div>
</template>

<script setup>
import { ref, defineAsyncComponent } from 'vue'

const loading = ref(true)
const error = ref(false)

const LazyComponent = defineAsyncComponent({
  loader: () => import('./LazyComponent.vue'),
  loadingComponent: {
    template: '<div class="loading">加载中...</div>'
  },
  errorComponent: {
    template: '<div class="error">加载失败</div>'
  },
  delay: 200,
  timeout: 5000,
  onError: () => {
    loading.value = false
    error.value = true
  },
  onLoad: () => {
    loading.value = false
  }
})

const retry = () => {
  loading.value = true
  error.value = false
  // 重新加载逻辑
}
</script>
```

## 最佳实践

### 懒加载原则
1. 大型组件优先懒加载
2. 路由组件全部懒加载
3. 条件渲染组件懒加载
4. 第三方组件懒加载
5. 非关键功能懒加载

### 性能优化
1. 合理设置延迟时间
2. 提供加载状态反馈
3. 处理加载失败情况
4. 预加载关键组件
5. 监控加载性能

### 用户体验
1. 提供明显的加载指示
2. 优雅的错误处理
3. 平滑的加载过渡
4. 合理的超时设置
5. 支持重试机制
