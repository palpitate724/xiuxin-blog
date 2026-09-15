# Vue 调试指南

Vue3 应用调试的最佳实践和工具使用。

## Vue DevTools

### 安装和配置
```bash
# Chrome/Edge 扩展
https://devtools.vuejs.org/

# Firefox 扩展
https://devtools.vuejs.org/
```

### 组件检查
```
1. 打开 Vue DevTools
2. 选择 Components 标签
3. 查看组件树结构
4. 检查组件 props、data、computed
5. 查看组件事件
```

### 状态检查
```
1. 选择 Pinia 标签
2. 查看所有 store 状态
3. 检查状态变化历史
4. 时间旅行调试
```

## 浏览器调试

### Console 调试
```typescript
// 组件内调试
console.log('组件挂载', props.data)
console.warn('警告信息', warningData)
console.error('错误信息', errorData)

// 对象调试
console.table(arrayData)
console.dir(objectData)

// 性能调试
console.time('operation')
// 执行操作
console.timeEnd('operation')
```

### Debugger 语句
```typescript
// 在代码中设置断点
function processData(data: any) {
  debugger // 浏览器会在此处暂停
  const result = transformData(data)
  return result
}
```

## Vue 调试 API

### 组件实例调试
```vue
<script setup>
import { getCurrentInstance } from 'vue'

const instance = getCurrentInstance()

// 开发环境下暴露组件实例
if (import.meta.env.DEV) {
  ;(window as any).__VUE_INSTANCE__ = instance
}
</script>
```

### 响应式调试
```typescript
import { watch, watchEffect } from 'vue'

// 监听响应式数据变化
watch(source, (newValue, oldValue) => {
  console.log('数据变化:', { oldValue, newValue })
})

// 副作用调试
watchEffect(() => {
  console.log('副作用执行:', reactiveData.value)
})
```

## 错误处理

### 全局错误处理
```typescript
// main.ts
app.config.errorHandler = (err, instance, info) => {
  console.error('全局错误:', err)
  console.error('错误信息:', info)
  console.error('组件实例:', instance)
  
  // 发送错误到监控服务
  if (import.meta.env.PROD) {
    sendErrorToMonitoring(err, info)
  }
}
```

### 异步错误处理
```typescript
async function fetchData() {
  try {
    const data = await api.getData()
    return data
  } catch (error) {
    console.error('数据获取失败:', error)
    // 用户友好的错误提示
    showErrorToast('数据加载失败，请重试')
    throw error
  }
}
```

## 性能调试

### 性能监控
```typescript
import { onMounted, onUnmounted } from 'vue'

onMounted(() => {
  performance.mark('component-mounted')
})

onUnmounted(() => {
  performance.mark('component-unmounted')
  performance.measure(
    'component-lifecycle',
    'component-mounted',
    'component-unmounted'
  )
  
  const measure = performance.getEntriesByName('component-lifecycle')[0]
  console.log('组件生命周期:', measure.duration)
})
```

### 渲染性能分析
```typescript
// 使用 Vue DevTools 的 Performance 标签
// 1. 开始录制
// 2. 执行操作
// 3. 停止录制
// 4. 分析渲染性能
```

## 网络调试

### API 调试
```typescript
// 拦截器调试
axios.interceptors.request.use(config => {
  console.log('请求配置:', config)
  return config
})

axios.interceptors.response.use(
  response => {
    console.log('响应数据:', response.data)
    return response
  },
  error => {
    console.error('响应错误:', error)
    return Promise.reject(error)
  }
)
```

### 网络请求监控
```typescript
// 使用浏览器 DevTools Network 标签
// 1. 打开 Network 标签
// 2. 过滤 XHR/Fetch 请求
// 3. 查看请求详情
// 4. 分析请求响应时间
```

## 状态调试

### Pinia 状态调试
```typescript
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()

// 订阅状态变化
userStore.$subscribe((mutation, state) => {
  console.log('状态变化:', mutation)
  console.log('新状态:', state)
})

// 订阅 action 调用
userStore.$onAction(({ name, args, after, onError }) => {
  console.log('Action 调用:', name, args)
  
  after((result) => {
    console.log('Action 结果:', result)
  })
  
  onError((error) => {
    console.error('Action 错误:', error)
  })
})
```

## 路由调试

### 路由导航调试
```typescript
import { useRouter } from 'vue-router'

const router = useRouter()

// 监听路由变化
router.beforeEach((to, from, next) => {
  console.log('路由导航:', { from: from.path, to: to.path })
  next()
})

router.afterEach((to, from) => {
  console.log('导航完成:', { from: from.path, to: to.path })
})
```

### 路由错误调试
```typescript
router.onError((error) => {
  console.error('路由错误:', error)
  
  if (error.name === 'NavigationDuplicated') {
    console.warn('重复导航')
  }
})
```

## 组件调试

### Props 调试
```vue
<script setup>
import { watch } from 'vue'

const props = defineProps<{
  data: any
}>()

// 监听 props 变化
watch(() => props.data, (newValue, oldValue) => {
  console.log('Props 变化:', { oldValue, newValue })
}, { deep: true })
</script>
```

### 事件调试
```vue
<script setup>
const emit = defineEmits<{
  update: [value: any]
}>()

const handleUpdate = (value: any) => {
  console.log('触发事件:', value)
  emit('update', value)
}
</script>
```

## 模板调试

### 模板表达式调试
```vue
<template>
  <div>
    <!-- 调试表达式 -->
    <div>{{ debugData }}</div>
    
    <!-- 条件渲染调试 -->
    <div v-if="shouldRender">
      {{ conditionResult }}
    </div>
  </div>
</template>

<script setup>
const debugData = computed(() => {
  const result = complexCalculation()
  console.log('计算结果:', result)
  return result
})
</script>
```

## 常见问题调试

### 组件不更新
```typescript
// 检查响应式数据
console.log('原始数据:', originalData)
console.log('响应式数据:', reactiveData)

// 检查计算属性
console.log('计算属性依赖:', computedValueEffect.dependencies)

// 强制更新
function forceUpdate() {
  // 使用 key 强制重新渲染
  componentKey.value++
}
```

### 内存泄漏
```typescript
// 检查事件监听器
console.log('活动的事件监听器:', getEventListeners(element))

// 检查定时器
console.log('活动的定时器:', getActiveTimers())

// 清理函数
onUnmounted(() => {
  // 清理事件监听器
  element.removeEventListener('event', handler)
  
  // 清理定时器
  clearInterval(timer)
  
  // 清理订阅
  subscription.unsubscribe()
})
```

## 调试工具

### Vue DevTools 高级功能
```
1. 组件时间旅行
2. 状态快照比较
3. 性能分析
4. 组件渲染追踪
5. 事件追踪
```

### 浏览器性能分析
```
1. Performance 标签
2. Memory 标签
3. Coverage 标签
4. Lighthouse 工具
```

## 生产环境调试

### 错误监控
```typescript
// 集成错误监控服务
import * as Sentry from '@sentry/vue'

app.use(Sentry.init({
  dsn: 'your-sentry-dsn',
  environment: import.meta.env.MODE,
  tracesSampleRate: 1.0
}))
```

### 日志管理
```typescript
// 生产环境日志配置
const logger = {
  log: (...args: any[]) => {
    if (import.meta.env.DEV) {
      console.log(...args)
    }
  },
  error: (...args: any[]) => {
    // 生产环境发送到错误监控
    if (import.meta.env.PROD) {
      sendToErrorMonitoring(args)
    } else {
      console.error(...args)
    }
  }
}
```

## 调试最佳实践

### 调试原则
1. 使用合适的调试工具
2. 避免生产环境打印敏感信息
3. 及时清理调试代码
4. 使用有意义的日志信息
5. 建立统一的调试规范

### 调试策略
1. 从简单到复杂逐步调试
2. 使用二分法定位问题
3. 充分利用浏览器工具
4. 建立调试文档
5. 团队分享调试经验
