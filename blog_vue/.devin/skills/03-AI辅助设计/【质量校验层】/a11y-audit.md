# 自动化可访问性审计

自动化可访问性审计技能，提供全面的可访问性检查和优化建议。

## 审计工具

### axe-core 集成
```typescript
import { inject } from 'vue'
import type { AxeResults } from 'axe-core'

// 可访问性审计 Composable
export function useA11yAudit() {
  const auditResults = ref<AxeResults | null>(null)
  const auditing = ref(false)

  const runAudit = async (context = document) => {
    auditing.value = true
    
    try {
      const axe = (await import('axe-core')).default
      const results = await axe.run(context)
      auditResults.value = results
      return results
    } catch (error) {
      console.error('可访问性审计失败:', error)
      throw error
    } finally {
      auditing.value = false
    }
  }

  const getViolations = () => {
    return auditResults.value?.violations || []
  }

  const getPasses = () => {
    return auditResults.value?.passes || []
  }

  const getIncomplete = () => {
    return auditResults.value?.incomplete || []
  }

  return {
    auditResults,
    auditing,
    runAudit,
    getViolations,
    getPasses,
    getIncomplete
  }
}
```

### 自动化测试集成
```typescript
// 可访问性测试用例
import { describe, it, expect } from 'vitest'
import { mount } from '@vue/test-utils'
import { axe } from 'jest-axe'

describe('Component Accessibility', () => {
  it('should not have accessibility violations', async () => {
    const wrapper = mount(MyComponent)
    const results = await axe(wrapper.element)
    expect(results).toHaveNoViolations()
  })
})
```

## 常见可访问性问题

### 1. 色彩对比度
```vue
<!-- 问题：对比度不足 -->
<template>
  <button style="color: #999; background: #fff">
    点击我
  </button>
</template>

<!-- 解决方案：提高对比度 -->
<template>
  <button style="color: #333; background: #fff">
    点击我
  </button>
</template>

<!-- 或使用更深的背景色 -->
<template>
  <button style="color: #fff; background: #1890ff">
    点击我
  </button>
</template>
```

### 2. 缺少 alt 文本
```vue
<!-- 问题：图片缺少 alt 属性 -->
<template>
  <img src="logo.png" />
</template>

<!-- 解决方案：添加描述性 alt 文本 -->
<template>
  <img src="logo.png" alt="公司 Logo" />
</template>

<!-- 装饰性图片使用空 alt -->
<template>
  <img src="divider.png" alt="" role="presentation" />
</template>
```

### 3. 表单标签缺失
```vue
<!-- 问题：输入框缺少标签 -->
<template>
  <input v-model="email" placeholder="邮箱" />
</template>

<!-- 解决方案：添加 label -->
<template>
  <label for="email">邮箱</label>
  <input id="email" v-model="email" />
</template>

<!-- 或使用 aria-label -->
<template>
  <input v-model="email" aria-label="邮箱" />
</template>
```

### 4. 焦点管理
```vue
<!-- 问题：焦点状态不明显 -->
<template>
  <button @click="handleClick">点击</button>
</template>

<style>
button:focus {
  /* 缺少明显的焦点样式 */
}
</style>

<!-- 解决方案：添加明显的焦点样式 -->
<template>
  <button @click="handleClick">点击</button>
</template>

<style>
button:focus-visible {
  outline: 2px solid #1890ff;
  outline-offset: 2px;
}
</style>
```

### 5. 键盘导航
```vue
<!-- 问题：自定义组件不支持键盘 -->
<template>
  <div @click="handleClick" class="custom-button">
    点击我
  </div>
</template>

<!-- 解决方案：添加键盘支持 -->
<template>
  <div
    @click="handleClick"
    @keydown.enter="handleClick"
    @keydown.space.prevent="handleClick"
    tabindex="0"
    role="button"
    class="custom-button"
  >
    点击我
  </div>
</template>
```

## ARIA 属性使用

### 语义化角色
```vue
<!-- 导航 -->
<nav aria-label="主导航">
  <ul>
    <li><a href="/">首页</a></li>
    <li><a href="/about">关于</a></li>
  </ul>
</nav>

<!-- 主要内容 -->
<main role="main">
  <h1>页面标题</h1>
</main>

<!-- 补充内容 -->
<aside aria-label="侧边栏">
  <h2>相关文章</h2>
</aside>

<!-- 搜索 -->
<div role="search">
  <label for="search">搜索</label>
  <input id="search" type="search" />
  <button type="submit">搜索</button>
</div>
```

### 状态和属性
```vue
<!-- 加载状态 -->
<button aria-busy="true" disabled>
  加载中...
</button>

<!-- 展开状态 -->
<button
  :aria-expanded="isExpanded"
  @click="toggleExpand"
>
  {{ isExpanded ? '收起' : '展开' }}
</button>

<!-- 选中状态 -->
<div
  role="checkbox"
  :aria-checked="isChecked"
  @click="toggleCheck"
  tabindex="0"
>
  选项
</div>

<!-- 错误状态 -->
<div role="alert" aria-live="polite">
  <p>{{ errorMessage }}</p>
</div>
```

### 动态内容
```vue
<!-- 实时区域 -->
<div aria-live="polite" aria-atomic="true">
  {{ statusMessage }}
</div>

<!-- 紧急消息 -->
<div role="alert" aria-live="assertive">
  {{ urgentMessage }}
</div>
```

## 键盘导航优化

### Tab 键顺序
```vue
<template>
  <div>
    <!-- 自然 Tab 顺序 -->
    <input ref="firstInput" />
    <input ref="secondInput" />
    <button @click="handleSubmit">提交</button>
  </div>
</template>

<script setup>
// 自定义 Tab 顺序
const handleKeyDown = (event: KeyboardEvent) => {
  if (event.key === 'Tab') {
    if (event.shiftKey) {
      // Shift + Tab: 反向
      // 自定义逻辑
    } else {
      // Tab: 正向
      // 自定义逻辑
    }
  }
}
</script>
```

### 快捷键支持
```vue
<template>
  <div @keydown="handleGlobalKeydown">
    <!-- 全局快捷键 -->
    <button
      @click="handleSave"
      title="保存 (Ctrl+S)"
    >
      保存
    </button>
  </div>
</template>

<script setup>
const handleGlobalKeydown = (event: KeyboardEvent) => {
  // Ctrl+S: 保存
  if (event.ctrlKey && event.key === 's') {
    event.preventDefault()
    handleSave()
  }
  
  // Ctrl+F: 搜索
  if (event.ctrlKey && event.key === 'f') {
    event.preventDefault()
    focusSearch()
  }
  
  // Escape: 关闭对话框
  if (event.key === 'Escape') {
    closeDialog()
  }
}
</script>
```

### 焦点陷阱
```vue
<template>
  <div ref="modalRef" @keydown="handleModalKeydown">
    <!-- 模态框内容 -->
  </div>
</template>

<script setup>
const modalRef = ref<HTMLElement>()
const focusableElements = ref<HTMLElement[]>([])

const getFocusableElements = () => {
  const elements = modalRef.value?.querySelectorAll(
    'button, [href], input, select, textarea, [tabindex]:not([tabindex="-1"])'
  )
  return Array.from(elements || []) as HTMLElement[]
}

const handleModalKeydown = (event: KeyboardEvent) => {
  if (event.key !== 'Tab') return
  
  const elements = getFocusableElements()
  const firstElement = elements[0]
  const lastElement = elements[elements.length - 1]
  
  if (event.shiftKey) {
    // Shift + Tab
    if (document.activeElement === firstElement) {
      event.preventDefault()
      lastElement.focus()
    }
  } else {
    // Tab
    if (document.activeElement === lastElement) {
      event.preventDefault()
      firstElement.focus()
    }
  }
}
</script>
```

## 屏幕阅读器优化

### 跳过链接
```vue
<template>
  <a href="#main-content" class="skip-link">
    跳到主要内容
  </a>
  
  <div id="main-content">
    <!-- 主要内容 -->
  </div>
</template>

<style>
.skip-link {
  position: absolute;
  top: -40px;
  left: 0;
  background: #1890ff;
  color: white;
  padding: 8px 16px;
  z-index: 100;
}

.skip-link:focus {
  top: 0;
}
</style>
```

### 页面标题
```vue
<template>
  <div role="heading" aria-level="1">
    {{ pageTitle }}
  </div>
</template>

<script setup>
const pageTitle = ref('页面标题')

// 动态更新页面标题
watch(pageTitle, (newTitle) => {
  document.title = newTitle
})
</script>
```

### 隐藏内容
```vue
<template>
  <!-- 视觉隐藏但屏幕阅读器可读 -->
  <span class="sr-only">必填项</span>
  
  <!-- 完全隐藏 -->
  <div v-if="false" aria-hidden="true">
    内容
  </div>
</template>

<style>
.sr-only {
  position: absolute;
  width: 1px;
  height: 1px;
  padding: 0;
  margin: -1px;
  overflow: hidden;
  clip: rect(0, 0, 0, 0);
  white-space: nowrap;
  border: 0;
}
</style>
```

## 可访问性测试

### 手动测试清单
- [ ] 键盘导航：所有功能可通过键盘访问
- [ ] 屏幕阅读器：使用 NVDA/JAWS 测试
- [ ] 色彩对比：使用对比度检查工具
- [ ] 字体大小：支持 200% 缩放
- [ ] 焦点管理：焦点状态清晰可见
- [ ] 表单标签：所有表单元素有标签
- [ ] 错误提示：错误信息清晰描述
- [ ] 链接文本：链接文本描述性强

### 自动化测试
```typescript
// Vitest + axe-core
import { describe, it, expect } from 'vitest'
import { mount } from '@vue/test-utils'
import { axe } from 'jest-axe'

describe('Accessibility Tests', () => {
  it('passes axe accessibility tests', async () => {
    const wrapper = mount(MyComponent)
    const results = await axe(wrapper.element)
    expect(results).toHaveNoViolations()
  })
  
  it('has proper ARIA attributes', () => {
    const wrapper = mount(MyComponent)
    const button = wrapper.find('button')
    expect(button.attributes('aria-label')).toBeDefined()
  })
  
  it('supports keyboard navigation', () => {
    const wrapper = mount(MyComponent)
    const button = wrapper.find('button')
    
    button.trigger('keydown', { key: 'Enter' })
    // 验证功能是否正常
  })
})
```

## 可访问性文档

### 组件可访问性说明
```markdown
# 组件可访问性文档

## Button 组件

### 键盘支持
- Enter/Space: 激活按钮
- Tab: 聚焦按钮

### ARIA 属性
- aria-disabled: 禁用状态
- aria-label: 自定义标签

### 屏幕阅读器
- 按钮文本会被朗读
- 支持自定义 aria-label

## Modal 组件

### 键盘支持
- Escape: 关闭模态框
- Tab: 在模态框内导航
- 焦点陷阱: 防止焦点离开模态框

### ARIA 属性
- role="dialog": 对话框角色
- aria-modal="true": 模态状态
- aria-labelledby: 标题引用
- aria-describedby: 描述引用

### 屏幕阅读器
- 模态框打开时宣布
- 焦点自动移入模态框
```

## 持续改进

### CI/CD 集成
```yaml
# .github/workflows/a11y.yml
name: Accessibility Tests

on: [push, pull_request]

jobs:
  a11y:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - uses: actions/setup-node@v2
        with:
          node-version: '16'
      - run: npm install
      - run: npm run test:a11y
```

### 定期审计
- 每次发布前进行可访问性审计
- 每月进行全面的可访问性检查
- 收集用户反馈，持续改进
- 关注 WCAG 标准更新
