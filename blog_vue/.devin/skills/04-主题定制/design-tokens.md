# Design Tokens

设计令牌系统，提供统一的设计变量管理。

## 令牌架构

### 三层令牌系统
```
Primitive Tokens (原始令牌)
    ↓
Semantic Tokens (语义令牌)
    ↓
Component Tokens (组件令牌)
```

## Primitive Tokens

### 颜色原始令牌
```css
:root {
  /* 基础颜色 */
  --color-white: #ffffff;
  --color-black: #000000;
  
  /* 灰色色板 */
  --color-gray-50: #fafafa;
  --color-gray-100: #f5f5f5;
  --color-gray-200: #e8e8e8;
  --color-gray-300: #d9d9d9;
  --color-gray-400: #bfbfbf;
  --color-gray-500: #8c8c8c;
  --color-gray-600: #595959;
  --color-gray-700: #434343;
  --color-gray-800: #262626;
  --color-gray-900: #1f1f1f;
  
  /* 品牌色 */
  --color-brand-50: #e6f7ff;
  --color-brand-100: #bae7ff;
  --color-brand-200: #91d5ff;
  --color-brand-300: #69c0ff;
  --color-brand-400: #40a9ff;
  --color-brand-500: #1890ff;
  --color-brand-600: #096dd9;
  --color-brand-700: #0050b3;
  --color-brand-800: #003a8c;
  --color-brand-900: #002766;
}
```

### 间距原始令牌
```css
:root {
  --spacing-0: 0;
  --spacing-1: 4px;
  --spacing-2: 8px;
  --spacing-3: 12px;
  --spacing-4: 16px;
  --spacing-5: 20px;
  --spacing-6: 24px;
  --spacing-8: 32px;
  --spacing-10: 40px;
  --spacing-12: 48px;
  --spacing-16: 64px;
  --spacing-20: 80px;
  --spacing-24: 96px;
}
```

### 字体原始令牌
```css
:root {
  /* 字体族 */
  --font-family-sans: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
  --font-family-mono: 'JetBrains Mono', 'Fira Code', monospace;
  --font-family-serif: 'Georgia', 'Times New Roman', serif;
  
  /* 字号 */
  --font-size-xs: 12px;
  --font-size-sm: 14px;
  --font-size-base: 16px;
  --font-size-lg: 18px;
  --font-size-xl: 20px;
  --font-size-2xl: 24px;
  --font-size-3xl: 30px;
  --font-size-4xl: 36px;
  --font-size-5xl: 48px;
  --font-size-6xl: 60px;
  
  /* 字重 */
  --font-weight-light: 300;
  --font-weight-normal: 400;
  --font-weight-medium: 500;
  --font-weight-semibold: 600;
  --font-weight-bold: 700;
  
  /* 行高 */
  --line-height-tight: 1.25;
  --line-height-normal: 1.5;
  --line-height-relaxed: 1.75;
  --line-height-loose: 2;
}
```

### 圆角原始令牌
```css
:root {
  --radius-none: 0;
  --radius-sm: 2px;
  --radius-md: 4px;
  --radius-lg: 8px;
  --radius-xl: 12px;
  --radius-2xl: 16px;
  --radius-full: 9999px;
}
```

### 阴影原始令牌
```css
:root {
  --shadow-sm: 0 1px 2px 0 rgba(0, 0, 0, 0.05);
  --shadow-md: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
  --shadow-lg: 0 10px 15px -3px rgba(0, 0, 0, 0.1);
  --shadow-xl: 0 20px 25px -5px rgba(0, 0, 0, 0.1);
  --shadow-2xl: 0 25px 50px -12px rgba(0, 0, 0, 0.25);
}
```

## Semantic Tokens

### 语义颜色令牌
```css
:root {
  /* 主色 */
  --color-primary: var(--color-brand-500);
  --color-primary-hover: var(--color-brand-600);
  --color-primary-active: var(--color-brand-700);
  --color-primary-disabled: var(--color-brand-300);
  
  /* 成功色 */
  --color-success: #52c41a;
  --color-success-hover: #73d13d;
  --color-success-active: #389e0d;
  
  /* 警告色 */
  --color-warning: #faad14;
  --color-warning-hover: #ffc53d;
  --color-warning-active: #d48806;
  
  /* 危险色 */
  --color-danger: #ff4d4f;
  --color-danger-hover: #ff7875;
  --color-danger-active: #d9363e;
  
  /* 信息色 */
  --color-info: #1890ff;
  --color-info-hover: #40a9ff;
  --color-info-active: #096dd9;
  
  /* 文本色 */
  --color-text-primary: var(--color-gray-900);
  --color-text-secondary: var(--color-gray-600);
  --color-text-tertiary: var(--color-gray-400);
  --color-text-disabled: var(--color-gray-300);
  
  /* 背景色 */
  --color-bg-primary: var(--color-white);
  --color-bg-secondary: var(--color-gray-50);
  --color-bg-tertiary: var(--color-gray-100);
  --color-bg-disabled: var(--color-gray-200);
  
  /* 边框色 */
  --color-border-primary: var(--color-gray-200);
  --color-border-secondary: var(--color-gray-300);
  --color-border-tertiary: var(--color-gray-400);
}
```

### 语义间距令牌
```css
:root {
  --spacing-xs: var(--spacing-1);
  --spacing-sm: var(--spacing-2);
  --spacing-md: var(--spacing-4);
  --spacing-lg: var(--spacing-6);
  --spacing-xl: var(--spacing-8);
  --spacing-2xl: var(--spacing-12);
}
```

### 语义字体令牌
```css
:root {
  /* 标题字号 */
  --font-size-h1: var(--font-size-5xl);
  --font-size-h2: var(--font-size-4xl);
  --font-size-h3: var(--font-size-3xl);
  --font-size-h4: var(--font-size-2xl);
  --font-size-h5: var(--font-size-xl);
  --font-size-h6: var(--font-size-lg);
  
  /* 正文字号 */
  --font-size-body: var(--font-size-base);
  --font-size-caption: var(--font-size-sm);
  --font-size-small: var(--font-size-xs);
}
```

## Component Tokens

### 按钮组件令牌
```css
:root {
  /* 按钮尺寸 */
  --button-height-sm: 32px;
  --button-height-md: 40px;
  --button-height-lg: 48px;
  
  /* 按钮内边距 */
  --button-padding-sm: var(--spacing-sm) var(--spacing-md);
  --button-padding-md: var(--spacing-md) var(--spacing-lg);
  --button-padding-lg: var(--spacing-lg) var(--spacing-xl);
  
  /* 按钮圆角 */
  --button-radius: var(--radius-md);
  
  /* 按钮字体 */
  --button-font-size: var(--font-size-base);
  --button-font-weight: var(--font-weight-medium);
}
```

### 输入框组件令牌
```css
:root {
  /* 输入框尺寸 */
  --input-height-sm: 32px;
  --input-height-md: 40px;
  --input-height-lg: 48px;
  
  /* 输入框内边距 */
  --input-padding-sm: var(--spacing-sm) var(--spacing-md);
  --input-padding-md: var(--spacing-md) var(--spacing-lg);
  --input-padding-lg: var(--spacing-lg) var(--spacing-xl);
  
  /* 输入框圆角 */
  --input-radius: var(--radius-md);
  
  /* 输入框边框 */
  --input-border-width: 1px;
  --input-border-color: var(--color-border-primary);
  --input-border-color-hover: var(--color-primary);
  --input-border-color-focus: var(--color-primary);
}
```

### 卡片组件令牌
```css
:root {
  /* 卡片内边距 */
  --card-padding: var(--spacing-lg);
  
  /* 卡片圆角 */
  --card-radius: var(--radius-lg);
  
  /* 卡片阴影 */
  --card-shadow: var(--shadow-md);
  --card-shadow-hover: var(--shadow-lg);
  
  /* 卡片背景 */
  --card-bg: var(--color-bg-primary);
  --card-bg-hover: var(--color-bg-secondary);
}
```

## 令牌使用

### CSS 变量使用
```css
.component {
  /* 使用原始令牌 */
  padding: var(--spacing-4);
  background: var(--color-brand-500);
  
  /* 使用语义令牌 */
  padding: var(--spacing-md);
  background: var(--color-primary);
  
  /* 使用组件令牌 */
  padding: var(--button-padding-md);
  background: var(--color-primary);
  border-radius: var(--button-radius);
}
```

### Vue3 使用
```vue
<template>
  <div :style="componentStyle">
    内容
  </div>
</template>

<script setup>
import { computed } from 'vue'

const componentStyle = computed(() => ({
  padding: 'var(--spacing-md)',
  background: 'var(--color-primary)',
  borderRadius: 'var(--button-radius)'
}))
</script>
```

## 令牌主题

### 浅色主题
```css
:root {
  --color-primary: var(--color-brand-500);
  --color-text-primary: var(--color-gray-900);
  --color-bg-primary: var(--color-white);
}
```

### 深色主题
```css
[data-theme="dark"] {
  --color-primary: var(--color-brand-400);
  --color-text-primary: var(--color-gray-100);
  --color-bg-primary: var(--color-gray-900);
}
```

## 令牌管理

### TypeScript 类型定义
```typescript
// types/tokens.ts
export interface DesignTokens {
  colors: {
    primary: string
    secondary: string
    success: string
    warning: string
    danger: string
  }
  spacing: {
    xs: string
    sm: string
    md: string
    lg: string
    xl: string
  }
  typography: {
    fontSize: {
      base: string
      h1: string
      h2: string
      h3: string
    }
    fontWeight: {
      normal: string
      medium: string
      bold: string
    }
  }
}
```

### 令牌验证
```typescript
// utils/tokenValidator.ts
export function validateTokens(tokens: DesignTokens): boolean {
  // 验证颜色格式
  const colorRegex = /^#[0-9A-Fa-f]{6}$/
  
  for (const color of Object.values(tokens.colors)) {
    if (!colorRegex.test(color)) {
      console.error(`Invalid color format: ${color}`)
      return false
    }
  }
  
  // 验证间距格式
  const spacingRegex = /^\d+(px|rem|em)?$/
  
  for (const spacing of Object.values(tokens.spacing)) {
    if (!spacingRegex.test(spacing)) {
      console.error(`Invalid spacing format: ${spacing}`)
      return false
    }
  }
  
  return true
}
```

## 最佳实践

### 令牌命名规范
1. 使用语义化命名
2. 保持命名一致性
3. 避免过于具体的命名
4. 使用 kebab-case 命名
5. 分层管理令牌

### 令牌使用原则
1. 优先使用语义令牌
2. 组件特定使用组件令牌
3. 避免直接使用原始令牌
4. 保持令牌的一致性
5. 定期审查和更新令牌

### 令牌版本管理
1. 使用语义化版本
2. 记录变更历史
3. 提供迁移指南
4. 保持向后兼容
5. 定期发布更新
