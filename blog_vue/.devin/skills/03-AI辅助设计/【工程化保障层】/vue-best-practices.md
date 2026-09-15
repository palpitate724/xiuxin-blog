# Vue 3 最佳实践

Vue 3 开发的最佳实践和规范。

## 组件设计

### 单一职责原则
```vue
<!-- ❌ 不推荐：一个组件做太多事情 -->
<template>
  <div>
    <UserForm />
    <UserList />
    <UserStats />
    <UserSettings />
  </div>
</template>

<!-- ✅ 推荐：拆分为小组件 -->
<template>
  <div>
    <UserManagement />
  </div>
</template>
```

### 组件命名
```vue
<!-- ❌ 不推荐 -->
<template>
  <userCard />
</template>

<!-- ✅ 推荐：使用 PascalCase -->
<template>
  <UserCard />
</template>
```

### Props 定义
```typescript
// ❌ 不推荐
const props = defineProps({
  user: Object,
  count: Number
})

// ✅ 推荐：使用 TypeScript 接口
interface Props {
  user: User
  count: number
}

const props = withDefaults(defineProps<Props>(), {
  count: 0
})
```

## 响应式数据

### 响应式最佳实践
```typescript
// ❌ 不推荐：不必要的响应式
const staticData = ref('static data')

// ✅ 推荐：静态数据不需要响应式
const staticData = 'static data'

// ❌ 不推荐：过度解构
const { user, count } = props

// ✅ 推荐：保持响应性
const user = toRef(props, 'user')
const count = toRef(props, 'count')
```

### 计算属性 vs 方法
```vue
<script setup>
// ❌ 不推荐：每次渲染都重新计算
const fullName = () => {
  return props.firstName + ' ' + props.lastName
}

// ✅ 推荐：使用计算属性缓存结果
const fullName = computed(() => {
  return props.firstName + ' ' + props.lastName
})
</script>
```

## 生命周期

### 生命周期使用
```vue
<script setup>
// ❌ 不推荐：滥用生命周期
onMounted(() => {
  fetchData()
  setupEventListeners()
  initializeChart()
  // 太多逻辑
})

// ✅ 推荐：分离关注点
onMounted(() => {
  fetchData()
})

const setupListeners = () => {
  // 事件监听逻辑
}

onMounted(setupListeners)
</script>
```

## 状态管理

### 本地状态 vs 全局状态
```typescript
// ❌ 不推荐：全局状态用于局部数据
const store = useStore()
store.setLocalData(data)

// ✅ 推荐：局部数据使用本地状态
const localData = ref(data)

// ✅ 推荐：全局状态用于共享数据
const store = useStore()
store.setSharedData(sharedData)
```

### Pinia 最佳实践
```typescript
// stores/user.ts
export const useUserStore = defineStore('user', () => {
  // State
  const user = ref<User | null>(null)
  const loading = ref(false)
  
  // Getters
  const isLoggedIn = computed(() => !!user.value)
  
  // Actions
  async function login(credentials: LoginCredentials) {
    loading.value = true
    try {
      user.value = await api.login(credentials)
    } finally {
      loading.value = false
    }
  }
  
  function logout() {
    user.value = null
  }
  
  return {
    user,
    loading,
    isLoggedIn,
    login,
    logout
  }
})
```

## 组件通信

### Props vs Events
```vue
<!-- 父组件 -->
<template>
  <ChildComponent
    :data="parentData"
    @update="handleUpdate"
  />
</template>

<!-- 子组件 -->
<script setup>
interface Props {
  data: any
}

const props = defineProps<Props>()
const emit = defineEmits<{
  update: [value: any]
}>()

const handleUpdate = (newValue: any) => {
  emit('update', newValue)
}
</script>
```

### Provide/Inject
```typescript
// 父组件
import { provide, ref } from 'vue'

const theme = ref('light')
provide('theme', theme)

// 子组件
import { inject } from 'vue'

const theme = inject<Ref<string>>('theme')
```

## 样式管理

### Scoped Styles
```vue
<style scoped>
/* ✅ 推荐：使用 scoped 避免样式污染 */
.component {
  color: red;
}
</style>
```

### CSS Modules
```vue
<template>
  <div :class="$style.container">
    <div :class="$style.content">Content</div>
  </div>
</template>

<style module>
.container {
  max-width: 1200px;
  margin: 0 auto;
}

.content {
  padding: 20px;
}
</style>
```

## 性能优化

### v-memo
```vue
<template>
  <!-- ✅ 推荐：对于静态列表使用 v-memo -->
  <div v-for="item in list" :key="item.id" v-memo="[item.id]">
    {{ item.name }}
  </div>
</template>
```

### 虚拟滚动
```vue
<script setup>
import { useVirtualList } from '@vueuse/core'

const { list, containerProps, wrapperProps } = useVirtualList(
  largeData,
  { itemHeight: 50 }
)
</script>

<template>
  <div v-bind="containerProps" style="height: 300px; overflow: auto;">
    <div v-bind="wrapperProps">
      <div v-for="{ data, index } in list" :key="index" :style="{ height: '50px' }">
        {{ data }}
      </div>
    </div>
  </div>
</template>
```

## 错误处理

### 错误边界
```vue
<template>
  <ErrorBoundary>
    <MyComponent />
  </ErrorBoundary>
</template>

<script setup>
import ErrorBoundary from '@/components/ErrorBoundary.vue'
</script>
```

### 异步错误处理
```typescript
// ❌ 不推荐：没有错误处理
const data = await fetchData()

// ✅ 推荐：完整的错误处理
try {
  const data = await fetchData()
  return data
} catch (error) {
  console.error('获取数据失败:', error)
  throw error
}
```

## TypeScript 集成

### 类型定义
```typescript
// types/user.ts
export interface User {
  id: number
  name: string
  email: string
  role: 'admin' | 'user'
}

export interface UserState {
  user: User | null
  loading: boolean
  error: Error | null
}
```

### 组件类型
```vue
<script setup lang="ts">
interface Props {
  user: User
  onUpdate: (user: User) => void
}

const props = defineProps<Props>()
const emit = defineEmits<{
  update: [user: User]
}>()
</script>
```

## 测试

### 单元测试
```typescript
// MyComponent.spec.ts
import { describe, it, expect } from 'vitest'
import { mount } from '@vue/test-utils'
import MyComponent from './MyComponent.vue'

describe('MyComponent', () => {
  it('renders properly', () => {
    const wrapper = mount(MyComponent, {
      props: {
        message: 'Hello World'
      }
    })
    expect(wrapper.text()).toContain('Hello World')
  })
})
```

## 目录结构

### 推荐结构
```
src/
├── assets/           # 静态资源
├── components/       # 组件
│   ├── common/      # 通用组件
│   └── business/    # 业务组件
├── composables/     # 组合式函数
├── directives/      # 自定义指令
├── layouts/         # 布局组件
├── pages/           # 页面组件
├── router/          # 路由配置
├── stores/          # 状态管理
├── styles/          # 全局样式
├── types/           # 类型定义
├── utils/           # 工具函数
└── main.ts          # 入口文件
```

## 代码规范

### ESLint 配置
```javascript
// .eslintrc.js
module.exports = {
  extends: [
    'plugin:vue/vue3-essential',
    'plugin:vue/vue3-strongly-recommended',
    'plugin:vue/vue3-recommended'
  ],
  rules: {
    'vue/multi-word-component-names': 'off',
    'vue/no-v-html': 'warn'
  }
}
```

### Prettier 配置
```javascript
// .prettierrc
{
  "semi": false,
  "singleQuote": true,
  "trailingComma": "none",
  "printWidth": 100
}
```

## 最佳实践清单

### 组件设计
- [ ] 组件职责单一
- [ ] Props 使用 TypeScript 类型
- [ ] 组件命名使用 PascalCase
- [ ] 避免过度嵌套

### 状态管理
- [ ] 合理使用本地状态和全局状态
- [ ] 状态更新使用 actions
- [ ] 避免直接修改状态

### 性能优化
- [ ] 合理使用计算属性
- [ ] 大列表使用虚拟滚动
- [ ] 避免不必要的响应式
- [ ] 合理使用 v-memo

### 类型安全
- [ ] 所有组件使用 TypeScript
- [ ] 避免使用 any 类型
- [ ] 定义明确的接口类型

### 可维护性
- [ ] 组件拆分合理
- [ ] 代码结构清晰
- [ ] 注释充分但不冗余
- [ ] 遵循项目规范
