# Composition API 模式

Vue3 Composition API 的常见模式和最佳实践。

## 基本 Composition 模式

```typescript
import { ref, computed, onMounted } from 'vue'

export function useCounter(initialValue = 0) {
  const count = ref(initialValue)
  const double = computed(() => count.value * 2)
  
  const increment = () => {
    count.value++
  }
  
  const decrement = () => {
    count.value--
  }
  
  return {
    count,
    double,
    increment,
    decrement
  }
}
```

## 异步数据获取模式

```typescript
import { ref, onMounted } from 'vue'

export function useAsyncData<T>(
  fetcher: () => Promise<T>,
  initialData?: T
) {
  const data = ref<T | undefined>(initialData)
  const loading = ref(false)
  const error = ref<Error | null>(null)
  
  const fetchData = async () => {
    loading.value = true
    error.value = null
    
    try {
      data.value = await fetcher()
    } catch (e) {
      error.value = e as Error
    } finally {
      loading.value = false
    }
  }
  
  onMounted(() => {
    fetchData()
  })
  
  return {
    data,
    loading,
    error,
    refresh: fetchData
  }
}
```

## 表单处理模式

```typescript
import { ref, reactive } from 'vue'

export function useForm<T extends Record<string, any>>(
  initialValues: T,
  validate?: (values: T) => Record<string, string>
) {
  const form = reactive({ ...initialValues })
  const errors = reactive<Record<string, string>>({})
  const touched = reactive<Record<string, boolean>>({})
  
  const setValue = (field: keyof T, value: any) => {
    form[field] = value
    touched[field] = true
    
    if (validate) {
      const validationErrors = validate(form)
      errors[field] = validationErrors[field] || ''
    }
  }
  
  const reset = () => {
    Object.assign(form, initialValues)
    Object.keys(errors).forEach(key => delete errors[key])
    Object.keys(touched).forEach(key => delete touched[key])
  }
  
  const isValid = computed(() => {
    return Object.keys(errors).every(key => !errors[key])
  })
  
  return {
    form,
    errors,
    touched,
    setValue,
    reset,
    isValid
  }
}
```

## 事件监听模式

```typescript
import { onMounted, onUnmounted } from 'vue'

export function useEventListener<K extends keyof WindowEventMap>(
  target: EventTarget,
  event: K,
  handler: (event: WindowEventMap[K]) => void
) {
  onMounted(() => {
    target.addEventListener(event, handler)
  })
  
  onUnmounted(() => {
    target.removeEventListener(event, handler)
  })
}
```

## 本地存储模式

```typescript
import { ref, watch } from 'vue'

export function useLocalStorage<T>(key: string, defaultValue: T) {
  const stored = localStorage.getItem(key)
  const value = ref<T>(stored ? JSON.parse(stored) : defaultValue)
  
  watch(value, (newValue) => {
    localStorage.setItem(key, JSON.stringify(newValue))
  }, { deep: true })
  
  return value
}
```
