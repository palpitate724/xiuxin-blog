# Bee Dev A11y

axe-core 自动化扫描技能，提供专业的可访问性自动化测试。

## axe-core 集成

### 基础配置
```typescript
// vite.config.ts
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  test: {
    globals: true,
    environment: 'jsdom'
  }
})
```

### 安装依赖
```bash
npm install axe-core @axe-core/vue jest-axe vitest
```

## 自动化扫描

### 页面级扫描
```typescript
import { runAxe } from '@/utils/a11y'

export async function scanPage() {
  const results = await runAxe(document)
  
  console.log('可访问性扫描结果:')
  console.log(`违规: ${results.violations.length}`)
  console.log(`通过: ${results.passes.length}`)
  console.log(`不完整: ${results.incomplete.length}`)
  
  return results
}
```

### 组件级扫描
```typescript
import { mount } from '@vue/test-utils'
import { axe } from 'jest-axe'

export async function scanComponent(component: any, props = {}) {
  const wrapper = mount(component, { props })
  const results = await axe(wrapper.element)
  
  return {
    hasViolations: results.violations.length > 0,
    violations: results.violations,
    passes: results.passes
  }
}
```

## 扫描配置

### 自定义规则
```typescript
import axe from 'axe-core'

const customConfig = {
  rules: {
    // 启用特定规则
    'color-contrast': { enabled: true },
    'image-alt': { enabled: true },
    
    // 禁用特定规则
    'landmark-one-main': { enabled: false },
    
    // 自定义规则配置
    'label': {
      enabled: true,
      options: {
        // 自定义选项
      }
    }
  },
  
  // 排除特定元素
  exclude: [
    ['#ad-banner'],
    ['.analytics']
  ]
}

export async function runCustomAxe(context = document) {
  return await axe.run(context, customConfig)
}
```

### WCAG 级别配置
```typescript
const wcagLevels = {
  A: ['wcag2a', 'wcag21a'],
  AA: ['wcag2a', 'wcag2aa', 'wcag21a', 'wcag21aa'],
  AAA: ['wcag2a', 'wcag2aa', 'wcag2aaa', 'wcag21a', 'wcag21aa', 'wcag21aaa']
}

export async function runAxeWithLevel(
  level: 'A' | 'AA' | 'AAA' = 'AA',
  context = document
) {
  const config = {
    runOnly: {
      type: 'tag',
      values: wcagLevels[level]
    }
  }
  
  return await axe.run(context, config)
}
```

## 集成测试

### Vitest 集成
```typescript
// tests/a11y/basic.test.ts
import { describe, it, expect } from 'vitest'
import { mount } from '@vue/test-utils'
import { axe } from 'jest-axe'
import MyComponent from '@/components/MyComponent.vue'

describe('Accessibility Tests', () => {
  it('should not have accessibility violations', async () => {
    const wrapper = mount(MyComponent)
    const results = await axe(wrapper.element)
    expect(results).toHaveNoViolations()
  })
  
  it('should have proper color contrast', async () => {
    const wrapper = mount(MyComponent)
    const results = await axe(wrapper.element, {
      rules: {
        'color-contrast': { enabled: true }
      }
    })
    
    const contrastViolations = results.violations.filter(
      v => v.id === 'color-contrast'
    )
    expect(contrastViolations).toHaveLength(0)
  })
})
```

### 组件测试套件
```typescript
// tests/a11y/components.test.ts
import { describe, it, expect } from 'vitest'
import { mount } from '@vue/test-utils'
import { axe } from 'jest-axe'

describe('Component Accessibility Suite', () => {
  const components = [
    'Button',
    'Input',
    'Modal',
    'Table',
    'Form'
  ]
  
  components.forEach(componentName => {
    it(`${componentName} should be accessible`, async () => {
      const Component = await import(`@/components/${componentName}.vue`)
      const wrapper = mount(Component.default)
      const results = await axe(wrapper.element)
      expect(results).toHaveNoViolations()
    })
  })
})
```

## 报告生成

### HTML 报告
```typescript
import { runAxe } from '@/utils/a11y'

export async function generateHtmlReport() {
  const results = await runAxe()
  
  const html = `
    <!DOCTYPE html>
    <html>
    <head>
      <title>可访问性报告</title>
      <style>
        body { font-family: Arial, sans-serif; padding: 20px; }
        .violation { background: #ffebee; padding: 10px; margin: 10px 0; }
        .pass { background: #e8f5e9; padding: 10px; margin: 10px 0; }
        h1 { color: #333; }
        h2 { color: #666; }
      </style>
    </head>
    <body>
      <h1>可访问性扫描报告</h1>
      <p>扫描时间: ${new Date().toLocaleString()}</p>
      
      <h2>违规问题 (${results.violations.length})</h2>
      ${results.violations.map(v => `
        <div class="violation">
          <h3>${v.id}</h3>
          <p>${v.description}</p>
          <p>影响: ${v.impact}</p>
          <pre>${v.helpUrl}</pre>
        </div>
      `).join('')}
      
      <h2>通过检查 (${results.passes.length})</h2>
      ${results.passes.map(p => `
        <div class="pass">
          <h3>${p.id}</h3>
          <p>${p.description}</p>
        </div>
      `).join('')}
    </body>
    </html>
  `
  
  return html
}
```

### JSON 报告
```typescript
export async function generateJsonReport() {
  const results = await runAxe()
  
  const report = {
    timestamp: new Date().toISOString(),
    summary: {
      violations: results.violations.length,
      passes: results.passes.length,
      incomplete: results.incomplete.length
    },
    violations: results.violations.map(v => ({
      id: v.id,
      description: v.description,
      impact: v.impact,
      tags: v.tags,
      nodes: v.nodes.map(n => ({
        html: n.html,
        target: n.target,
        failureSummary: n.failureSummary
      }))
    })),
    passes: results.passes.map(p => ({
      id: p.id,
      description: p.description
    }))
  }
  
  return JSON.stringify(report, null, 2)
}
```

## CI/CD 集成

### GitHub Actions
```yaml
# .github/workflows/a11y.yml
name: Accessibility Tests

on:
  push:
    branches: [main, develop]
  pull_request:
    branches: [main, develop]

jobs:
  a11y:
    runs-on: ubuntu-latest
    
    steps:
      - uses: actions/checkout@v3
      
      - name: Setup Node.js
        uses: actions/setup-node@v3
        with:
          node-version: '18'
          cache: 'npm'
      
      - name: Install dependencies
        run: npm ci
      
      - name: Run accessibility tests
        run: npm run test:a11y
      
      - name: Upload accessibility report
        if: always()
        uses: actions/upload-artifact@v3
        with:
          name: a11y-report
          path: a11y-report.html
```

### GitLab CI
```yaml
# .gitlab-ci.yml
a11y:
  stage: test
  image: node:18
  script:
    - npm ci
    - npm run test:a11y
  artifacts:
    paths:
      - a11y-report.html
    when: always
  only:
    - main
    - develop
    - merge_requests
```

## 监控和告警

### 实时监控
```typescript
// 开发环境实时监控
if (import.meta.env.DEV) {
  setInterval(async () => {
    const results = await runAxe()
    
    if (results.violations.length > 0) {
      console.warn('发现可访问性问题:', results.violations)
    }
  }, 30000) // 每30秒检查一次
}
```

### 告警配置
```typescript
export async function checkAndAlert() {
  const results = await runAxe()
  
  if (results.violations.length > 0) {
    // 发送告警
    const criticalViolations = results.violations.filter(
      v => v.impact === 'critical' || v.impact === 'serious'
    )
    
    if (criticalViolations.length > 0) {
      // 发送邮件/Slack 通知
      await sendAlert({
        type: 'a11y',
        severity: 'high',
        message: `发现 ${criticalViolations.length} 个严重的可访问性问题`,
        details: criticalViolations
      })
    }
  }
}
```

## 性能优化

### 懒加载扫描
```typescript
export async function lazyAxeScan() {
  if ('requestIdleCallback' in window) {
    requestIdleCallback(async () => {
      const axe = await import('axe-core')
      const results = await axe.default.run(document)
      processResults(results)
    })
  } else {
    // 降级方案
    setTimeout(async () => {
      const axe = await import('axe-core')
      const results = await axe.default.run(document)
      processResults(results)
    }, 1000)
  }
}
```

### 增量扫描
```typescript
export async function incrementalAxeScan(element: HTMLElement) {
  const axe = await import('axe-core')
  const results = await axe.default.run(element)
  
  return results
}

// 只扫描新添加的元素
const observer = new MutationObserver((mutations) => {
  mutations.forEach(async (mutation) => {
    mutation.addedNodes.forEach(async (node) => {
      if (node.nodeType === Node.ELEMENT_NODE) {
        const results = await incrementalAxeScan(node as HTMLElement)
        if (results.violations.length > 0) {
          console.warn('新元素存在可访问性问题:', results.violations)
        }
      }
    })
  })
})

observer.observe(document.body, {
  childList: true,
  subtree: true
})
```

## 最佳实践

### 扫描时机
```typescript
// 1. 开发环境实时扫描
if (import.meta.env.DEV) {
  setInterval(lazyAxeScan, 60000)
}

// 2. 组件挂载后扫描
onMounted(async () => {
  const results = await runAxe()
  if (results.violations.length > 0) {
    console.warn('组件存在可访问性问题:', results.violations)
  }
})

// 3. 路由变化后扫描
router.afterEach(async () => {
  const results = await runAxe()
  processResults(results)
})
```

### 结果处理
```typescript
export function processResults(results: axe.AxeResults) {
  const {
    violations,
    passes,
    incomplete
  } = results
  
  // 分类处理
  const critical = violations.filter(v => v.impact === 'critical')
  const serious = violations.filter(v => v.impact === 'serious')
  const moderate = violations.filter(v => v.impact === 'moderate')
  const minor = violations.filter(v => v.impact === 'minor')
  
  // 优先处理严重问题
  if (critical.length > 0) {
    console.error('严重可访问性问题:', critical)
  }
  
  if (serious.length > 0) {
    console.warn('重要可访问性问题:', serious)
  }
  
  // 生成报告
  generateReport(results)
}
```

## 工具集成

### VS Code 扩展
```json
// .vscode/settings.json
{
  "axe-core.enabled": true,
  "axe-core.rules": {
    "color-contrast": "warn",
    "image-alt": "error",
    "label": "error"
  }
}
```

### Browser DevTools
```typescript
// 开发工具集成
if (import.meta.env.DEV) {
  window.__AXE_CORE__ = true
  
  // 添加自定义命令
  (window as any).runA11yScan = async () => {
    const results = await runAxe()
    console.table(results.violations)
    return results
  }
}
```

## 持续改进

### 定期扫描
```typescript
// 每周自动扫描
export function scheduleWeeklyScan() {
  const now = new Date()
  const nextSunday = new Date(now)
  nextSunday.setDate(now.getDate() + (7 - now.getDay()))
  nextSunday.setHours(0, 0, 0, 0)
  
  const delay = nextSunday.getTime() - now.getTime()
  
  setTimeout(async () => {
    const results = await runAxe()
    await generateAndSendReport(results)
    
    // 递归调用，实现每周扫描
    scheduleWeeklyScan()
  }, delay)
}
```

### 趋势分析
```typescript
export async function trackA11yTrends() {
  const history = await loadA11yHistory()
  const current = await runAxe()
  
  const trend = {
    date: new Date().toISOString(),
    violations: current.violations.length,
    trend: history.length > 0 
      ? current.violations.length - history[history.length - 1].violations
      : 0
  }
  
  await saveA11yHistory([...history, trend])
  
  if (trend.trend > 0) {
    console.warn('可访问性问题数量增加:', trend.trend)
  } else if (trend.trend < 0) {
    console.log('可访问性问题数量减少:', trend.trend)
  }
}
```
