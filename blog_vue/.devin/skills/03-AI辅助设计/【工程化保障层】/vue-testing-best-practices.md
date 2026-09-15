# Vue 测试最佳实践

Vue3 组件测试的最佳实践和规范。

## 测试框架配置

### Vitest 配置
```typescript
// vitest.config.ts
import { defineConfig } from 'vitest/config'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  test: {
    globals: true,
    environment: 'jsdom',
    setupFiles: './tests/setup.ts',
    coverage: {
      provider: 'v8',
      reporter: ['text', 'json', 'html'],
      exclude: [
        'node_modules/',
        'tests/',
        '**/*.d.ts',
        '**/*.config.*',
        '**/mockData',
        '**/dist'
      ]
    }
  }
})
```

### 测试工具配置
```typescript
// tests/setup.ts
import { config } from '@vue/test-utils'
import { vi } from 'vitest'

// 全局组件注册
config.global.stubs = {
  'router-link': true,
  'router-view': true
}

// Mock 全局对象
global.fetch = vi.fn()
```

## 组件测试

### 基础组件测试
```typescript
// Button.spec.ts
import { describe, it, expect } from 'vitest'
import { mount } from '@vue/test-utils'
import Button from '@/components/common/Button.vue'

describe('Button Component', () => {
  it('renders properly', () => {
    const wrapper = mount(Button, {
      props: {
        text: 'Click me'
      }
    })
    expect(wrapper.text()).toContain('Click me')
  })
  
  it('emits click event', async () => {
    const wrapper = mount(Button)
    await wrapper.trigger('click')
    expect(wrapper.emitted()).toHaveProperty('click')
  })
  
  it('disables when disabled prop is true', () => {
    const wrapper = mount(Button, {
      props: {
        disabled: true
      }
    })
    expect(wrapper.find('button').attributes('disabled')).toBeDefined()
  })
})
```

### 表单组件测试
```typescript
// Input.spec.ts
import { describe, it, expect } from 'vitest'
import { mount } from '@vue/test-utils'
import Input from '@/components/common/Input.vue'

describe('Input Component', () => {
  it('binds v-model correctly', async () => {
    const wrapper = mount(Input, {
      props: {
        modelValue: 'initial value'
      }
    })
    
    const input = wrapper.find('input')
    await input.setValue('new value')
    
    expect(wrapper.emitted('update:modelValue')).toBeTruthy()
    expect(wrapper.emitted('update:modelValue')![0]).toEqual(['new value'])
  })
  
  it('shows error message', () => {
    const wrapper = mount(Input, {
      props: {
        error: 'This field is required'
      }
    })
    
    expect(wrapper.text()).toContain('This field is required')
  })
})
```

## Composable 测试

### Composable 单元测试
```typescript
// composables/useCounter.spec.ts
import { describe, it, expect } from 'vitest'
import { useCounter } from '@/composables/useCounter'

describe('useCounter', () => {
  it('initializes with default value', () => {
    const { count } = useCounter()
    expect(count.value).toBe(0)
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
})
```

### 异步 Composable 测试
```typescript
// composables/useAsyncData.spec.ts
import { describe, it, expect, vi } from 'vitest'
import { useAsyncData } from '@/composables/useAsyncData'

describe('useAsyncData', () => {
  it('fetches data successfully', async () => {
    const mockData = { id: 1, name: 'Test' }
    const mockFetcher = vi.fn().mockResolvedValue(mockData)
    
    const { data, loading, error, fetchData } = useAsyncData(mockFetcher)
    
    await fetchData()
    
    expect(data.value).toEqual(mockData)
    expect(loading.value).toBe(false)
    expect(error.value).toBeNull()
  })
  
  it('handles errors', async () => {
    const mockError = new Error('Network error')
    const mockFetcher = vi.fn().mockRejectedValue(mockError)
    
    const { error, fetchData } = useAsyncData(mockFetcher)
    
    await expect(fetchData()).rejects.toThrow('Network error')
    expect(error.value).toBe(mockError)
  })
})
```

## Store 测试

### Pinia Store 测试
```typescript
// stores/user.spec.ts
import { describe, it, expect, beforeEach } from 'vitest'
import { setActivePinia, createPinia } from 'pinia'
import { useUserStore } from '@/stores/user'

describe('User Store', () => {
  beforeEach(() => {
    setActivePinia(createPinia())
  })
  
  it('initializes with empty state', () => {
    const store = useUserStore()
    expect(store.user).toBeNull()
    expect(store.loading).toBe(false)
  })
  
  it('logs in successfully', async () => {
    const store = useUserStore()
    const mockCredentials = { username: 'test', password: '123' }
    
    await store.login(mockCredentials)
    
    expect(store.user).toBeTruthy()
    expect(store.loading).toBe(false)
  })
})
```

## 路由测试

### 路由组件测试
```typescript
// views/Home.spec.ts
import { describe, it, expect } from 'vitest'
import { mount } from '@vue/test-utils'
import { createRouter, createMemoryHistory } from 'vue-router'
import Home from '@/views/Home.vue'

describe('Home View', () => {
  it('renders correctly', async () => {
    const router = createRouter({
      history: createMemoryHistory(),
      routes: [
        { path: '/', component: Home }
      ]
    })
    
    router.push('/')
    await router.isReady()
    
    const wrapper = mount(Home, {
      global: {
        plugins: [router]
      }
    })
    
    expect(wrapper.find('h1').text()).toBe('Welcome')
  })
})
```

## 集成测试

### 完整流程测试
```typescript
// integration/user-flow.spec.ts
import { describe, it, expect } from 'vitest'
import { mount } from '@vue/test-utils'
import { createRouter, createMemoryHistory } from 'vue-router'
import { createPinia } from 'pinia'
import UserForm from '@/components/UserForm.vue'
import { useUserStore } from '@/stores/user'

describe('User Flow Integration', () => {
  it('completes user registration flow', async () => {
    const pinia = createPinia()
    const router = createRouter({
      history: createMemoryHistory(),
      routes: [
        { path: '/register', component: UserForm }
      ]
    })
    
    const wrapper = mount(UserForm, {
      global: {
        plugins: [pinia, router]
      }
    })
    
    const store = useUserStore()
    
    // 填写表单
    await wrapper.find('#username').setValue('testuser')
    await wrapper.find('#email').setValue('test@example.com')
    await wrapper.find('#password').setValue('password123')
    
    // 提交表单
    await wrapper.find('form').trigger('submit')
    
    // 验证结果
    expect(store.user).toBeTruthy()
    expect(store.user?.username).toBe('testuser')
  })
})
```

## E2E 测试

### Playwright 配置
```typescript
// playwright.config.ts
import { defineConfig } from '@playwright/test'

export default defineConfig({
  testDir: './e2e',
  fullyParallel: true,
  forbidOnly: !!process.env.CI,
  retries: process.env.CI ? 2 : 0,
  workers: process.env.CI ? 1 : undefined,
  reporter: 'html',
  use: {
    baseURL: 'http://localhost:3000',
    trace: 'on-first-retry',
  },
  projects: [
    {
      name: 'chromium',
      use: { browserName: 'chromium' },
    },
    {
      name: 'firefox',
      use: { browserName: 'firefox' },
    },
    {
      name: 'webkit',
      use: { browserName: 'webkit' },
    },
  ],
})
```

### E2E 测试示例
```typescript
// e2e/login.spec.ts
import { test, expect } from '@playwright/test'

test('user can login', async ({ page }) => {
  await page.goto('/login')
  
  await page.fill('#username', 'testuser')
  await page.fill('#password', 'password123')
  await page.click('button[type="submit"]')
  
  await expect(page).toHaveURL('/dashboard')
  await expect(page.locator('h1')).toContainText('Welcome')
})
```

## Mock 策略

### API Mock
```typescript
// tests/mocks/api.ts
import { vi } from 'vitest'

export const mockApi = {
  getUser: vi.fn(() => Promise.resolve({
    id: 1,
    name: 'Test User',
    email: 'test@example.com'
  })),
  
  login: vi.fn((credentials) => {
    if (credentials.username === 'test' && credentials.password === '123') {
      return Promise.resolve({
        token: 'mock-token',
        user: { id: 1, name: 'Test User' }
      })
    }
    return Promise.reject(new Error('Invalid credentials'))
  })
}
```

### 组件 Mock
```typescript
// tests/mocks/components.ts
import { defineComponent } from 'vue'

export const MockChart = defineComponent({
  name: 'MockChart',
  render() {
    return h('div', { class: 'mock-chart' }, 'Chart Component')
  }
})
```

## 测试覆盖率

### 覆盖率目标
```
总体覆盖率: > 80%
核心业务逻辑: > 90%
工具函数: > 95%
组件覆盖率: > 70%
```

### 覆盖率报告
```bash
# 生成覆盖率报告
npm run test:coverage

# 查看覆盖率报告
open coverage/index.html
```

## 最佳实践

### 测试原则
1. 测试应该独立运行
2. 测试名称应该描述测试的行为
3. 一个测试只验证一个行为
4. 测试应该快速执行
5. 测试应该易于维护

### 组织结构
```
tests/
├── unit/              # 单元测试
│   ├── components/   # 组件测试
│   ├── composables/   # Composable 测试
│   └── stores/       # Store 测试
├── integration/      # 集成测试
├── e2e/              # E2E 测试
├── mocks/            # Mock 数据
└── setup.ts          # 测试配置
```

### 命名规范
```typescript
// ✅ 推荐：描述性命名
describe('Button Component', () => {
  it('emits click event when clicked', () => {})
  it('disables when disabled prop is true', () => {})
})

// ❌ 不推荐：模糊命名
describe('Button', () => {
  it('works', () => {})
  it('test2', () => {})
})
```

### 测试数据管理
```typescript
// tests/fixtures/user.ts
export const mockUser = {
  id: 1,
  name: 'Test User',
  email: 'test@example.com',
  role: 'user'
}

export const mockUsers = [
  mockUser,
  { id: 2, name: 'Admin User', email: 'admin@example.com', role: 'admin' }
]
```

## 持续集成

### CI 配置
```yaml
# .github/workflows/test.yml
name: Tests

on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    
    steps:
      - uses: actions/checkout@v3
      - uses: actions/setup-node@v3
        with:
          node-version: '18'
          cache: 'npm'
      
      - run: npm ci
      - run: npm run test
      - run: npm run test:coverage
      
      - name: Upload coverage
        uses: codecov/codecov-action@v3
```
