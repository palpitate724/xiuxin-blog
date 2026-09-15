# Web Design Guidelines

Web 设计指南，提供网页设计的最佳实践和规范。

## 响应式设计

### 断点系统
```css
/* Mobile First Approach */
.container {
  width: 100%;
  padding: 0 16px;
}

@media (min-width: 640px) {
  .container {
    max-width: 640px;
    margin: 0 auto;
  }
}

@media (min-width: 768px) {
  .container {
    max-width: 768px;
  }
}

@media (min-width: 1024px) {
  .container {
    max-width: 1024px;
  }
}

@media (min-width: 1280px) {
  .container {
    max-width: 1280px;
  }
}
```

### 移动优先设计
```
原则:
1. 从小屏幕开始设计
2. 逐步增强到大屏幕
3. 优先考虑核心功能
4. 简化交互流程
```

## 可访问性设计

### WCAG 2.1 标准

#### AA 级要求
- **色彩对比**: 文字对比度至少 4.5:1
- **键盘导航**: 所有功能可通过键盘访问
- **焦点指示**: 焦点状态清晰可见
- **文本替代**: 图片提供 alt 文本
- **错误提示**: 错误信息清晰描述

#### 实施检查清单
```html
<!-- 语义化 HTML -->
<nav aria-label="主导航">
  <ul>
    <li><a href="/">首页</a></li>
    <li><a href="/about">关于</a></li>
  </ul>
</nav>

<!-- 表单标签 -->
<label for="email">邮箱</label>
<input id="email" type="email" required aria-required="true">

<!-- 错误提示 -->
<div role="alert" aria-live="polite">
  请输入有效的邮箱地址
</div>

<!-- 按钮状态 -->
<button aria-disabled="true" disabled>提交</button>
```

### 键盘导航
```css
/* 焦点样式 */
:focus-visible {
  outline: 2px solid var(--color-primary);
  outline-offset: 2px;
}

/* 跳过链接 */
.skip-link {
  position: absolute;
  top: -40px;
  left: 0;
  background: var(--color-primary);
  color: white;
  padding: 8px;
  z-index: 100;
}

.skip-link:focus {
  top: 0;
}
```

## 性能优化

### 图片优化
```html
<!-- 响应式图片 -->
<picture>
  <source media="(min-width: 1024px)" srcset="large.webp">
  <source media="(min-width: 768px)" srcset="medium.webp">
  <img src="small.webp" alt="描述" loading="lazy">
</picture>

<!-- 懒加载 -->
<img src="image.jpg" alt="描述" loading="lazy">
```

### CSS 优化
```css
/* 避免深层嵌套 */
/* 不推荐 */
.parent .child .grandchild .target {
  color: red;
}

/* 推荐 */
.target {
  color: red;
}

/* 使用 CSS 变量 */
:root {
  --color-primary: #1890ff;
}

.button {
  background: var(--color-primary);
}
```

### JavaScript 优化
```javascript
// 代码分割
const lazyComponent = () => import('./LazyComponent.vue')

// 防抖和节流
function debounce(func, wait) {
  let timeout
  return function executedFunction(...args) {
    const later = () => {
      clearTimeout(timeout)
      func(...args)
    }
    clearTimeout(timeout)
    timeout = setTimeout(later, wait)
  }
}

// 虚拟滚动
import { useVirtualList } from '@vueuse/core'

const { list, containerProps, wrapperProps } = useVirtualList(
  dataSource,
  { itemHeight: 50 }
)
```

## 浏览器兼容性

### 渐进增强
```css
/* 基础样式 */
.button {
  padding: 8px 16px;
  border: 1px solid #ccc;
  background: white;
}

/* 现代浏览器增强 */
@supports (backdrop-filter: blur(10px)) {
  .button {
    backdrop-filter: blur(10px);
    background: rgba(255, 255, 255, 0.8);
  }
}
```

### 特性检测
```javascript
// 检测特性支持
if ('IntersectionObserver' in window) {
  const observer = new IntersectionObserver(callback)
} else {
  // 降级方案
}
```

## SEO 优化

### 语义化 HTML
```html
<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta name="description" content="页面描述">
  <meta name="keywords" content="关键词">
  <title>页面标题</title>
</head>
<body>
  <header>
    <h1>主标题</h1>
    <nav>导航</nav>
  </header>
  
  <main>
    <article>
      <h2>文章标题</h2>
      <p>文章内容</p>
    </article>
  </main>
  
  <footer>
    <p>页脚信息</p>
  </footer>
</body>
</html>
```

### 结构化数据
```html
<script type="application/ld+json">
{
  "@context": "https://schema.org",
  "@type": "Article",
  "headline": "文章标题",
  "author": {
    "@type": "Person",
    "name": "作者姓名"
  },
  "datePublished": "2024-01-01",
  "description": "文章描述"
}
</script>
```

## 安全设计

### XSS 防护
```javascript
// 输入验证
function sanitizeInput(input) {
  return input
    .replace(/&/g, "&amp;")
    .replace(/</g, "&lt;")
    .replace(/>/g, "&gt;")
    .replace(/"/g, "&quot;")
    .replace(/'/g, "&#039;")
}

// Vue 自动转义
// 默认情况下，Vue 会自动转义 HTML
```

### CSRF 防护
```javascript
// 添加 CSRF Token
axios.defaults.headers.common['X-CSRF-TOKEN'] = 
  document.querySelector('meta[name="csrf-token"]').content
```

## 用户体验设计

### 加载状态
```vue
<template>
  <div v-if="loading" class="loading">
    <div class="spinner"></div>
    <p>加载中...</p>
  </div>
  <div v-else>
    <!-- 内容 -->
  </div>
</template>
```

### 错误处理
```vue
<template>
  <div v-if="error" class="error">
    <h3>出错了</h3>
    <p>{{ error.message }}</p>
    <button @click="retry">重试</button>
  </div>
  <div v-else>
    <!-- 内容 -->
  </div>
</template>
```

### 空状态
```vue
<template>
  <div v-if="isEmpty" class="empty-state">
    <div class="empty-icon">📭</div>
    <h3>暂无数据</h3>
   <p>还没有相关内容</p>
    <button @click="create">创建</button>
  </div>
  <div v-else>
    <!-- 内容 -->
  </div>
</template>
```

## 国际化设计

### 多语言支持
```javascript
// i18n 配置
const messages = {
  zh: {
    hello: '你好',
    welcome: '欢迎'
  },
  en: {
    hello: 'Hello',
    welcome: 'Welcome'
  }
}

// RTL 支持
[dir="rtl"] {
  direction: rtl;
  text-align: right;
}
```

## 设计检查清单

### 布局检查
- [ ] 响应式设计适配所有设备
- [ ] 移动优先设计原则
- [ ] 网格系统对齐
- [ ] 间距系统统一

### 可访问性检查
- [ ] 色彩对比度达标
- [ ] 键盘导航完整
- [ ] 屏幕阅读器支持
- [ ] 语义化 HTML

### 性能检查
- [ ] 图片优化
- [ ] 代码分割
- [ ] 懒加载实现
- [ ] 缓存策略

### 兼容性检查
- [ ] 主流浏览器测试
- [ ] 渐进增强策略
- [ ] 特性检测实现
- [ ] 降级方案准备

### SEO 检查
- [ ] 语义化 HTML
- [ ] Meta 标签完整
- [ ] 结构化数据
- [ ] URL 结构友好

### 安全检查
- [ ] XSS 防护
- [ ] CSRF 防护
- [ ] HTTPS 使用
- [ ] 敏感数据保护
