# Vue Expert Performance

Vue3 性能剖析和优化专业技能。

## 性能分析工具

### Vue DevTools Performance
```
1. 打开 Vue DevTools
2. 选择 Performance 标签
3. 点击开始录制
4. 执行用户操作
5. 停止录制并分析
```

### Chrome DevTools Performance
```
1. 打开 Chrome DevTools
2. 选择 Performance 标签
3. 点击录制
4. 执行操作
5. 分析火焰图和性能指标
```

## 渲染性能优化

### 虚拟滚动
```vue
<script setup>
import { useVirtualList } from '@vueuse/core'

const largeData = ref(Array.from({ length: 10000 }, (_, i) => ({
  id: i,
  name: `Item ${i}`
})))

const { list, containerProps, wrapperProps } = useVirtualList(
  largeData,
  { itemHeight: 50 }
)
</script>

<template>
  <div v-bind="containerProps" style="height: 400px; overflow: auto;">
    <div v-bind="wrapperProps">
      <div
        v-for="{ data, index } in list"
        :key="data.id"
        :style="{ height: '50px' }"
      >
        {{ data.name }}
      </div>
    </div>
  </div>
</template>
```

### 列表优化
```vue
<template>
  <!-- ✅ 推荐：使用唯一的 key -->
  <div v-for="item in items" :key="item.id">
    {{ item.name }}
  </div>
  
  <!-- ❌ 不推荐：使用索引作为 key -->
  <div v-for="(item, index) in items" :key="index">
    {{ item.name }}
  </div>
  
  <!-- ✅ 推荐：v-memo 避免不必要的重新渲染 -->
  <div v-for="item in items" :key="item.id" v-memo="[item.id, item.selected]">
    {{ item.name }}
  </div>
</template>
```

### 计算属性优化
```typescript
// ✅ 推荐：使用计算属性缓存结果
const expensiveValue = computed(() => {
  return heavyCalculation(data.value)
})

// ❌ 不推荐：在模板中直接调用方法
<template>
  <div>{{ heavyCalculation(data) }}</div>
</template>

// ✅ 推荐：复杂计算使用 watchEffect
const result = ref()
watchEffect(() => {
  result.value = heavyCalculation(data.value)
})
```

## 响应式优化

### 避免过度响应式
```typescript
// ✅ 推荐：静态数据不需要响应式
const staticConfig = {
  apiUrl: 'https://api.example.com',
  timeout: 5000
}

// ❌ 不推荐：不必要的响应式
const staticConfig = ref({
  apiUrl: 'https://api.example.com',
  timeout: 5000
})

// ✅ 推荐：使用 shallowRef 减少响应式开销
const largeObject = shallowRef(bigData)
```

### 响应式性能监控
```typescript
import { watchEffect, onMounted } from 'vue'

let effectCount = 0

onMounted(() => {
  watchEffect(() => {
    effectCount++
    console.log(`Effect 执行次数: ${effectCount}`)
  })
})
```

## 组件优化

### 组件懒加载
```typescript
// ✅ 推荐：路由懒加载
const routes = [
  {
    path: '/dashboard',
    component: () => import('@/views/Dashboard.vue')
  }
]

// ✅ 推荐：组件懒加载
const HeavyComponent = defineAsyncComponent(() =>
  import('@/components/HeavyComponent.vue')
)

// ✅ 推荐：异步组件带加载状态
const AsyncComponent = defineAsyncComponent({
  loader: () => import('@/components/AsyncComponent.vue'),
  loadingComponent: LoadingComponent,
  errorComponent: ErrorComponent,
  delay: 200,
  timeout: 3000
})
```

### 组件缓存
```vue
<template>
  <!-- ✅ 推荐：使用 keep-alive 缓存组件 -->
  <keep-alive :include="cachedComponents">
    <router-view />
  </keep-alive>
  
  <!-- ✅ 推荐：条件缓存 -->
  <keep-alive :include="['UserProfile', 'Dashboard']">
    <component :is="currentComponent" />
  </keep-alive>
</template>

<script setup>
const cachedComponents = ref(['UserProfile', 'Dashboard'])
</script>
```

### 函数式组件
```vue
<!-- ✅ 推荐：简单组件使用函数式组件 -->
<script setup>
const FunctionalComponent = (props, { slots }) => {
  return h('div', { class: 'functional' }, slots.default?.())
}
</script>
```

## 状态管理优化

### Pinia 性能优化
```typescript
// ✅ 推荐：使用 getter 缓存计算结果
export const useUserStore = defineStore('user', () => {
  const users = ref<User[]>([])
  
  const activeUsers = computed(() => {
    return users.value.filter(user => user.isActive)
  })
  
  return {
    users,
    activeUsers
  }
})

// ✅ 推荐：避免在 store 中存储大量数据
export const useProductStore = defineStore('product', () => {
  const products = ref<Product[]>([])
  const productCache = new Map<number, Product>()
  
  const getProduct = (id: number) => {
    if (productCache.has(id)) {
      return productCache.get(id)
    }
    
    const product = products.value.find(p => p.id === id)
    if (product) {
      productCache.set(id, product)
    }
    
    return product
  }
  
  return {
    products,
    getProduct
  }
})
```

## 网络请求优化

### 请求缓存
```typescript
// composables/useCachedFetch.ts
const cache = new Map<string, any>()

export function useCachedFetch<T>(url: string) {
  const data = ref<T | null>(null)
  const loading = ref(false)
  
  if (cache.has(url)) {
    data.value = cache.get(url)
  } else {
    const fetchData = async () => {
      loading.value = true
      try {
        const response = await fetch(url)
        data.value = await response.json()
        cache.set(url, data.value)
      } finally {
        loading.value = false
      }
    }
    
    fetchData()
  }
  
  return { data, loading }
}
```

### 请求防抖
```typescript
// composables/useDebouncedFetch.ts
export function useDebouncedFetch<T>(
  fetchFn: () => Promise<T>,
  delay: number = 300
) {
  const data = ref<T | null>(null)
  const loading = ref(false)
  let timeoutId: number | null = null
  
  const execute = () => {
    if (timeoutId) {
      clearTimeout(timeoutId)
    }
    
    timeoutId = window.setTimeout(async () => {
      loading.value = true
      try {
        data.value = await fetchFn()
      } finally {
        loading.value = false
      }
    }, delay)
  }
  
  return { data, loading, execute }
}
```

## 内存优化

### 内存泄漏检测
```typescript
// 检查组件卸载时的内存泄漏
onUnmounted(() => {
  // 清理事件监听器
  window.removeEventListener('resize', handleResize)
  
  // 清理定时器
  clearInterval(timerId)
  
  // 清理订阅
  subscription.unsubscribe()
  
  // 清理引用
  largeData.value = null
})
```

### 对象池
```typescript
// 使用对象池减少垃圾回收
class ObjectPool<T> {
  private pool: T[] = []
  private factory: () => T
  private reset: (obj: T) => void
  
  constructor(factory: () => T, reset: (obj: T) => void) {
    this.factory = factory
    this.reset = reset
  }
  
  acquire(): T {
    return this.pool.pop() || this.factory()
  }
  
  release(obj: T): void {
    this.reset(obj)
    this.pool.push(obj)
  }
}

// 使用对象池
const particlePool = new ObjectPool(
  () => ({ x: 0, y: 0, vx: 0, vy: 0 }),
  (particle) => {
    particle.x = 0
    particle.y = 0
    particle.vx = 0
    particle.vy = 0
  }
)
```

## 打包优化

### 代码分割
```typescript
// vite.config.ts
export default defineConfig({
  build: {
    rollupOptions: {
      output: {
        manualChunks: {
          'vue-vendor': ['vue', 'vue-router', 'pinia'],
          'ui-vendor': ['element-plus'],
          'utils': ['lodash', 'axios']
        }
      }
    }
  }
})
```

### Tree Shaking
```typescript
// ✅ 推荐：按需导入
import { debounce } from 'lodash-es'

// ❌ 不推荐：导入整个库
import _ from 'lodash'

// ✅ 推荐：按需导入组件
import { Button, Input } from 'element-plus'

// ❌ 不推荐：导入整个 UI 库
import ElementPlus from 'element-plus'
```

### 压缩优化
```typescript
// vite.config.ts
export default defineConfig({
  build: {
    minify: 'terser',
    terserOptions: {
      compress: {
        drop_console: true,
        drop_debugger: true
      }
    }
  }
})
```

## 运行时性能监控

### 性能指标收集
```typescript
// utils/performance.ts
export function collectPerformanceMetrics() {
  const metrics = {
    // 页面加载时间
    pageLoadTime: performance.timing.loadEventEnd - performance.timing.navigationStart,
    
    // DOM 解析时间
    domParseTime: performance.timing.domComplete - performance.timing.domLoading,
    
    // 资源加载时间
    resourceLoadTime: performance.timing.domContentLoadedEventEnd - performance.timing.domContentLoadedEventStart,
    
    // 首次内容绘制
    firstContentfulPaint: 0,
    
    // 首次有意义绘制
    firstMeaningfulPaint: 0
  }
  
  // 获取 Web Vitals
  if ('PerformanceObserver' in window) {
    const observer = new PerformanceObserver((list) => {
      for (const entry of list.getEntries()) {
        if (entry.name === 'FCP') {
          metrics.firstContentfulPaint = entry.startTime
        }
        if (entry.name === 'FMP') {
          metrics.firstMeaningfulPaint = entry.startTime
        }
      }
    })
    
    observer.observe({ entryTypes: ['paint'] })
  }
  
  return metrics
}
```

### FPS 监控
```typescript
// utils/fpsMonitor.ts
export function useFPSMonitor() {
  const fps = ref(60)
  const frames = ref(0)
  let lastTime = performance.now()
  
  const measureFPS = () => {
    frames.value++
    const currentTime = performance.now()
    
    if (currentTime >= lastTime + 1000) {
      fps.value = Math.round((frames.value * 1000) / (currentTime - lastTime))
      frames.value = 0
      lastTime = currentTime
    }
    
    requestAnimationFrame(measureFPS)
  }
  
  onMounted(() => {
    measureFPS()
  })
  
  return fps
}
```

## 性能优化策略

### 加载优化
1. 路由懒加载
2. 组件懒加载
3. 图片懒加载
4. 资源预加载
5. CDN 加速

### 渲染优化
1. 虚拟滚动
2. 列表优化
3. 计算属性缓存
4. 避免不必要的响应式
5. 合理使用 v-memo

### 内存优化
1. 及时清理引用
2. 使用对象池
3. 避免内存泄漏
4. 合理使用缓存
5. 大数据分页处理

### 网络优化
1. 请求缓存
2. 请求防抖节流
3. 数据压缩
4. HTTP/2
5. Service Worker

## 性能基准

### 性能目标
```
首屏加载时间: < 2s
首次内容绘制: < 1s
可交互时间: < 3s
FPS: > 30
内存使用: < 100MB
```

### 性能监控
```typescript
// 定期性能检查
setInterval(() => {
  const metrics = collectPerformanceMetrics()
  
  if (metrics.pageLoadTime > 2000) {
    console.warn('页面加载时间过长:', metrics.pageLoadTime)
  }
  
  if (fps.value < 30) {
    console.warn('FPS 过低:', fps.value)
  }
}, 5000)
```

## 最佳实践

### 性能优化清单
- [ ] 实现路由懒加载
- [ ] 使用虚拟滚动处理长列表
- [ ] 优化计算属性使用
- [ ] 避免过度响应式
- [ ] 实现组件缓存
- [ ] 优化网络请求
- [ ] 实现请求缓存
- [ ] 监控性能指标
- [ ] 定期性能审计
- [ ] 建立性能基准

### 性能优化流程
1. 性能分析：使用工具分析性能瓶颈
2. 问题定位：确定具体优化点
3. 优化实施：针对性优化
4. 效果验证：验证优化效果
5. 持续监控：建立长期监控机制
