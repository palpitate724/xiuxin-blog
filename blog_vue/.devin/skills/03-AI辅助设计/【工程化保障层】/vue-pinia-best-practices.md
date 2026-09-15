# Vue Pinia 最佳实践

Pinia 状态管理的最佳实践和规范。

## Store 定义

### 基础 Store
```typescript
// stores/user.ts
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useUserStore = defineStore('user', () => {
  // State
  const user = ref<User | null>(null)
  const loading = ref(false)
  const error = ref<Error | null>(null)
  
  // Getters
  const isLoggedIn = computed(() => !!user.value)
  const userName = computed(() => user.value?.name || 'Guest')
  
  // Actions
  async function login(credentials: LoginCredentials) {
    loading.value = true
    error.value = null
    
    try {
      const response = await api.login(credentials)
      user.value = response.data
    } catch (err) {
      error.value = err as Error
      throw err
    } finally {
      loading.value = false
    }
  }
  
  function logout() {
    user.value = null
    error.value = null
  }
  
  return {
    user,
    loading,
    error,
    isLoggedIn,
    userName,
    login,
    logout
  }
})
```

### Store 模块化
```typescript
// stores/index.ts
export { useUserStore } from './user'
export { useProductStore } from './product'
export { useCartStore } from './cart'
```

## 状态组织

### Store 分离原则
```typescript
// ✅ 推荐：按功能域分离 Store
// stores/user.ts - 用户相关状态
// stores/product.ts - 产品相关状态
// stores/cart.ts - 购物车相关状态

// ❌ 不推荐：混合多个功能域
// stores/app.ts - 包含所有状态
```

### 状态分层
```typescript
// stores/app.ts - 全局应用状态
export const useAppStore = defineStore('app', () => {
  const theme = ref<'light' | 'dark'>('light')
  const language = ref('zh-CN')
  const sidebarCollapsed = ref(false)
  
  return {
    theme,
    language,
    sidebarCollapsed
  }
})

// stores/user.ts - 用户特定状态
export const useUserStore = defineStore('user', () => {
  const user = ref<User | null>(null)
  const preferences = ref<UserPreferences>({})
  
  return {
    user,
    preferences
  }
})
```

## Actions 设计

### 异步 Actions
```typescript
// ✅ 推荐：完整的错误处理
async function fetchUserData() {
  loading.value = true
  error.value = null
  
  try {
    const data = await api.getUser()
    user.value = data
    return data
  } catch (err) {
    error.value = err as Error
    throw err
  } finally {
    loading.value = false
  }
}

// ❌ 不推荐：缺少错误处理
async function fetchUserData() {
  loading.value = true
  const data = await api.getUser()
  user.value = data
  loading.value = false
}
```

### Action 组合
```typescript
// 组合多个 Actions
async function initializeUser() {
  try {
    await fetchUserData()
    await fetchUserPreferences()
    await fetchUserPermissions()
  } catch (error) {
    console.error('用户初始化失败:', error)
    throw error
  }
}
```

## Getters 使用

### 计算属性
```typescript
// ✅ 推荐：使用 computed 进行派生状态
const fullName = computed(() => {
  return `${user.value?.firstName} ${user.value?.lastName}`
})

const filteredProducts = computed(() => {
  return products.value.filter(product => 
    product.category === selectedCategory.value
  )
})

// ❌ 不推荐：在组件中重复计算
// 在多个组件中重复计算相同逻辑
```

### Getter 参数化
```typescript
// 带参数的 Getter
const getProductById = computed(() => {
  return (id: number) => {
    return products.value.find(product => product.id === id)
  }
})

// 使用
const product = getProductById.value(123)
```

## 持久化

### 本地存储集成
```typescript
// stores/user.ts
import { defineStore } from 'pinia'
import { useStorage } from '@vueuse/core'

export const useUserStore = defineStore('user', () => {
  const user = useStorage('user', null as User | null, localStorage)
  const preferences = useStorage('user-preferences', {} as UserPreferences, localStorage)
  
  return {
    user,
    preferences
  }
})
```

### 选择性持久化
```typescript
// 只持久化必要的状态
export const useCartStore = defineStore('cart', () => {
  const items = ref<CartItem[]>([])
  const total = computed(() => calculateTotal(items.value))
  
  // 持久化购物车项目
  const savedItems = useStorage('cart-items', items, localStorage)
  
  // 不持久化临时状态
  const isProcessing = ref(false)
  
  return {
    items: savedItems,
    total,
    isProcessing
  }
})
```

## 类型安全

### TypeScript 集成
```typescript
// types/store.ts
export interface UserState {
  user: User | null
  loading: boolean
  error: Error | null
}

export interface UserActions {
  login: (credentials: LoginCredentials) => Promise<void>
  logout: () => void
  fetchUser: () => Promise<User>
}

export type UserStore = UserState & UserActions

// stores/user.ts
export const useUserStore = defineStore('user', (): UserStore => {
  // 实现
})
```

## Store 测试

### 单元测试
```typescript
// stores/user.spec.ts
import { setActivePinia, createPinia } from 'pinia'
import { describe, it, expect, beforeEach } from 'vitest'
import { useUserStore } from './user'

describe('User Store', () => {
  beforeEach(() => {
    setActivePinia(createPinia())
  })
  
  it('initializes with empty state', () => {
    const store = useUserStore()
    expect(store.user).toBeNull()
    expect(store.loading).toBe(false)
  })
  
  it('sets user on login', async () => {
    const store = useUserStore()
    await store.login({ username: 'test', password: '123' })
    expect(store.user).toBeTruthy()
  })
})
```

## 最佳实践

### Store 设计原则
1. Store 职责单一，按功能域分离
2. State 保持最小化，避免冗余
3. Actions 处理业务逻辑和异步操作
4. Getters 用于派生状态和计算
5. 提供完整的错误处理

### 性能优化
1. 避免在 Store 中存储大量数据
2. 合理使用 computed 缓存计算结果
3. 懒加载大型 Store
4. 选择性持久化状态

### 可维护性
1. 清晰的命名约定
2. 完整的 TypeScript 类型
3. 充分的错误处理
4. 合理的代码组织

### 安全考虑
1. 敏感数据不持久化到本地存储
2. 验证外部数据
3. 正确处理错误状态
4. 提供状态重置方法
