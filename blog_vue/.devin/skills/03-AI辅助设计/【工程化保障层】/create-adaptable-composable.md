# Adaptable Composable 创建

创建可适应、可复用的 Vue3 Composable。

## Composable 设计原则

### 单一职责
```typescript
// ✅ 推荐：每个 Composable 只做一件事
// composables/useCounter.ts
export function useCounter(initialValue = 0) {
  const count = ref(initialValue)
  const increment = () => count.value++
  const decrement = () => count.value--
  const reset = () => count.value = initialValue
  
  return { count, increment, decrement, reset }
}

// ❌ 不推荐：一个 Composable 做太多事情
export function useEverything() {
  const count = ref(0)
  const user = ref(null)
  const theme = ref('light')
  // ...太多不相关的功能
}
```

### 可组合性
```typescript
// composables/usePagination.ts
export function usePagination<T>(items: Ref<T[]>) {
  const currentPage = ref(1)
  const pageSize = ref(10)
  
  const paginatedItems = computed(() => {
    const start = (currentPage.value - 1) * pageSize.value
    const end = start + pageSize.value
    return items.value.slice(start, end)
  })
  
  const totalPages = computed(() => Math.ceil(items.value.length / pageSize.value))
  
  const nextPage = () => {
    if (currentPage.value < totalPages.value) {
      currentPage.value++
    }
  }
  
  const prevPage = () => {
    if (currentPage.value > 1) {
      currentPage.value--
    }
  }
  
  const goToPage = (page: number) => {
    if (page >= 1 && page <= totalPages.value) {
      currentPage.value = page
    }
  }
  
  return {
    currentPage,
    pageSize,
    paginatedItems,
    totalPages,
    nextPage,
    prevPage,
    goToPage
  }
}

// composables/useSorting.ts
export function useSorting<T>(items: Ref<T[]>) {
  const sortKey = ref<keyof T | null>(null)
  const sortOrder = ref<'asc' | 'desc'>('asc')
  
  const sortedItems = computed(() => {
    if (!sortKey.value) return items.value
    
    return [...items.value].sort((a, b) => {
      const aValue = a[sortKey.value!]
      const bValue = b[sortKey.value!]
      
      if (aValue < bValue) return sortOrder.value === 'asc' ? -1 : 1
      if (aValue > bValue) return sortOrder.value === 'asc' ? 1 : -1
      return 0
    })
  })
  
  const sortBy = (key: keyof T) => {
    if (sortKey.value === key) {
      sortOrder.value = sortOrder.value === 'asc' ? 'desc' : 'asc'
    } else {
      sortKey.value = key
      sortOrder.value = 'asc'
    }
  }
  
  return {
    sortKey,
    sortOrder,
    sortedItems,
    sortBy
  }
}

// 组合使用
export function useTable<T>(items: Ref<T[]>) {
  const pagination = usePagination(items)
  const sorting = useSorting(items)
  
  const displayItems = computed(() => {
    return sorting.sortedItems.slice(
      (pagination.currentPage.value - 1) * pagination.pageSize.value,
      pagination.currentPage.value * pagination.pageSize.value
    )
  })
  
  return {
    ...pagination,
    ...sorting,
    displayItems
  }
}
```

## 响应式设计

### 媒体查询 Composable
```typescript
// composables/useMediaQuery.ts
export function useMediaQuery(query: string) {
  const matches = ref(false)
  let mediaQuery: MediaQueryList | null = null
  
  const updateMatches = () => {
    if (mediaQuery) {
      matches.value = mediaQuery.matches
    }
  }
  
  onMounted(() => {
    mediaQuery = window.matchMedia(query)
    updateMatches()
    mediaQuery.addEventListener('change', updateMatches)
  })
  
  onUnmounted(() => {
    if (mediaQuery) {
      mediaQuery.removeEventListener('change', updateMatches)
    }
  })
  
  return matches
}

// 使用
const isMobile = useMediaQuery('(max-width: 768px)')
const isDarkMode = useMediaQuery('(prefers-color-scheme: dark)')
```

### 响应式配置
```typescript
// composables/useResponsiveConfig.ts
export function useResponsiveConfig<T>(configs: {
  mobile: T
  tablet: T
  desktop: T
}) {
  const isMobile = useMediaQuery('(max-width: 768px)')
  const isTablet = useMediaQuery('(min-width: 769px) and (max-width: 1024px)')
  
  const currentConfig = computed(() => {
    if (isMobile.value) return configs.mobile
    if (isTablet.value) return configs.tablet
    return configs.desktop
  })
  
  return currentConfig
}

// 使用
const buttonConfig = useResponsiveConfig({
  mobile: { size: 'small', fullWidth: true },
  tablet: { size: 'medium', fullWidth: false },
  desktop: { size: 'large', fullWidth: false }
})
```

## 错误处理

### 异步操作 Composable
```typescript
// composables/useAsync.ts
export function useAsync<T>(
  asyncFunction: () => Promise<T>,
  immediate = true
) {
  const data = ref<T | null>(null)
  const error = ref<Error | null>(null)
  const loading = ref(false)
  
  const execute = async () => {
    loading.value = true
    error.value = null
    
    try {
      data.value = await asyncFunction()
    } catch (err) {
      error.value = err as Error
      throw err
    } finally {
      loading.value = false
    }
  }
  
  if (immediate) {
    execute()
  }
  
  return {
    data,
    error,
    loading,
    execute
  }
}

// 使用
const { data, error, loading, execute } = useAsync(
  () => api.fetchUser(),
  true
)
```

### 重试机制
```typescript
// composables/useRetry.ts
export function useRetry<T>(
  asyncFunction: () => Promise<T>,
  options: {
    maxAttempts?: number
    delay?: number
    onRetry?: (attempt: number, error: Error) => void
  } = {}
) {
  const {
    maxAttempts = 3,
    delay = 1000,
    onRetry
  } = options
  
  const { data, error, loading, execute } = useAsync(async () => {
    let lastError: Error | null = null
    
    for (let attempt = 1; attempt <= maxAttempts; attempt++) {
      try {
        return await asyncFunction()
      } catch (err) {
        lastError = err as Error
        if (attempt < maxAttempts) {
          onRetry?.(attempt, lastError)
          await new Promise(resolve => setTimeout(resolve, delay * attempt))
        }
      }
    }
    
    throw lastError
  }, false)
  
  return {
    data,
    error,
    loading,
    execute
  }
}
```

## 本地存储

### LocalStorage Composable
```typescript
// composables/useLocalStorage.ts
export function useLocalStorage<T>(
  key: string,
  defaultValue: T,
  options: {
    serializer?: (value: T) => string
    deserializer?: (value: string) => T
  } = {}
) {
  const {
    serializer = JSON.stringify,
    deserializer = JSON.parse
  } = options
  
  const storedValue = ref<T>(defaultValue)
  
  // 读取初始值
  try {
    const item = localStorage.getItem(key)
    if (item !== null) {
      storedValue.value = deserializer(item)
    }
  } catch (error) {
    console.error(`Error reading localStorage key "${key}":`, error)
  }
  
  // 监听变化并保存
  watch(
    storedValue,
    (newValue) => {
      try {
        localStorage.setItem(key, serializer(newValue))
      } catch (error) {
        console.error(`Error setting localStorage key "${key}":`, error)
      }
    },
    { deep: true }
  )
  
  // 移除存储
  const remove = () => {
    localStorage.removeItem(key)
    storedValue.value = defaultValue
  }
  
  return {
    value: storedValue,
    remove
  }
}

// 使用
const { value: theme, remove: removeTheme } = useLocalStorage('theme', 'light')
```

### SessionStorage Composable
```typescript
// composables/useSessionStorage.ts
export function useSessionStorage<T>(
  key: string,
  defaultValue: T
) {
  const storedValue = ref<T>(defaultValue)
  
  try {
    const item = sessionStorage.getItem(key)
    if (item !== null) {
      storedValue.value = JSON.parse(item)
    }
  } catch (error) {
    console.error(`Error reading sessionStorage key "${key}":`, error)
  }
  
  watch(
    storedValue,
    (newValue) => {
      try {
        sessionStorage.setItem(key, JSON.stringify(newValue))
      } catch (error) {
        console.error(`Error setting sessionStorage key "${key}":`, error)
      }
    },
    { deep: true }
  )
  
  const remove = () => {
    sessionStorage.removeItem(key)
    storedValue.value = defaultValue
  }
  
  return {
    value: storedValue,
    remove
  }
}
```

## 事件处理

### 鼠标事件 Composable
```typescript
// composables/useMouse.ts
export function useMouse() {
  const x = ref(0)
  const y = ref(0)
  
  const update = (event: MouseEvent) => {
    x.value = event.clientX
    y.value = event.clientY
  }
  
  onMounted(() => {
    window.addEventListener('mousemove', update)
  })
  
  onUnmounted(() => {
    window.removeEventListener('mousemove', update)
  })
  
  return { x, y }
}

// 使用
const { x, y } = useMouse()
```

### 键盘事件 Composable
```typescript
// composables/useKeyboard.ts
export function useKeyboard() {
  const pressedKeys = ref<Set<string>>(new Set())
  
  const onKeyDown = (event: KeyboardEvent) => {
    pressedKeys.value.add(event.key)
  }
  
  const onKeyUp = (event: KeyboardEvent) => {
    pressedKeys.value.delete(event.key)
  }
  
  onMounted(() => {
    window.addEventListener('keydown', onKeyDown)
    window.addEventListener('keyup', onKeyUp)
  })
  
  onUnmounted(() => {
    window.removeEventListener('keydown', onKeyDown)
    window.removeEventListener('keyup', onKeyUp)
  })
  
  const isPressed = (key: string) => pressedKeys.value.has(key)
  
  return {
    pressedKeys,
    isPressed
  }
}

// 使用
const { isPressed } = useKeyboard()
const isCtrlPressed = computed(() => isPressed('Control'))
```

## 网络请求

### Fetch Composable
```typescript
// composables/useFetch.ts
export function useFetch<T>(
  url: string,
  options: RequestInit = {}
) {
  const data = ref<T | null>(null)
  const error = ref<Error | null>(null)
  const loading = ref(false)
  
  const execute = async () => {
    loading.value = true
    error.value = null
    
    try {
      const response = await fetch(url, options)
      
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`)
      }
      
      data.value = await response.json()
    } catch (err) {
      error.value = err as Error
      throw err
    } finally {
      loading.value = false
    }
  }
  
  return {
    data,
    error,
    loading,
    execute
  }
}

// 使用
const { data, error, loading, execute } = useFetch<User>('/api/user')
```

### 防抖和节流
```typescript
// composables/useDebounce.ts
export function useDebounce<T extends (...args: any[]) => any>(
  fn: T,
  delay: number
) {
  let timeoutId: number | null = null
  
  const debouncedFn = (...args: Parameters<T>) => {
    if (timeoutId) {
      clearTimeout(timeoutId)
    }
    
    timeoutId = window.setTimeout(() => {
      fn(...args)
    }, delay)
  }
  
  onUnmounted(() => {
    if (timeoutId) {
      clearTimeout(timeoutId)
    }
  })
  
  return debouncedFn
}

// composables/useThrottle.ts
export function useThrottle<T extends (...args: any[]) => any>(
  fn: T,
  delay: number
) {
  let lastCall = 0
  let timeoutId: number | null = null
  
  const throttledFn = (...args: Parameters<T>) => {
    const now = Date.now()
    
    if (now - lastCall >= delay) {
      lastCall = now
      fn(...args)
    } else {
      if (timeoutId) {
        clearTimeout(timeoutId)
      }
      
      timeoutId = window.setTimeout(() => {
        lastCall = Date.now()
        fn(...args)
      }, delay - (now - lastCall))
    }
  }
  
  onUnmounted(() => {
    if (timeoutId) {
      clearTimeout(timeoutId)
    }
  })
  
  return throttledFn
}
```

## 最佳实践

### Composable 命名
```typescript
// ✅ 推荐：描述性命名
useCounter
useFetch
useLocalStorage
useDebounce

// ❌ 不推荐：模糊命名
useData
useHelper
useUtil
```

### 参数设计
```typescript
// ✅ 推荐：清晰的参数接口
interface UsePaginationOptions {
  initialPage?: number
  pageSize?: number
  onPageChange?: (page: number) => void
}

export function usePagination<T>(
  items: Ref<T[]>,
  options: UsePaginationOptions = {}
) {
  const {
    initialPage = 1,
    pageSize = 10,
    onPageChange
  } = options
  
  // 实现
}

// ❌ 不推荐：参数过多且无结构
export function usePagination<T>(
  items: Ref<T[]>,
  initialPage?: number,
  pageSize?: number,
  onPageChange?: (page: number) => void
) {
  // 实现
}
```

### 返回值设计
```typescript
// ✅ 推荐：明确的返回类型
interface UseCounterReturn {
  count: Ref<number>
  increment: () => void
  decrement: () => void
  reset: () => void
}

export function useCounter(initialValue = 0): UseCounterReturn {
  // 实现
}

// ❌ 不推荐：不明确的返回类型
export function useCounter(initialValue = 0) {
  // 实现
}
```

### 文档化
```typescript
/**
 * 计数器 Composable
 * @param initialValue - 初始值，默认为 0
 * @returns 计数器相关的状态和方法
 * @example
 * ```ts
 * const { count, increment, decrement, reset } = useCounter(10)
 * ```
 */
export function useCounter(initialValue = 0) {
  // 实现
}
```

## 测试 Composable

### 单元测试
```typescript
// composables/useCounter.spec.ts
import { describe, it, expect } from 'vitest'
import { useCounter } from './useCounter'

describe('useCounter', () => {
  it('initializes with default value', () => {
    const { count } = useCounter()
    expect(count.value).toBe(0)
  })
  
  it('initializes with custom value', () => {
    const { count } = useCounter(10)
    expect(count.value).toBe(10)
  })
  
  it('increments correctly', () => {
    const { count, increment } = useCounter()
    increment()
    expect(count.value).toBe(1)
  })
  
  it('decrements correctly', () => {
    const { count, decrement } = useCounter()
    decrement()
    expect(count.value).toBe(-1)
  })
  
  it('resets to initial value', () => {
    const { count, increment, reset } = useCounter(5)
    increment()
    increment()
    reset()
    expect(count.value).toBe(5)
  })
})
```
