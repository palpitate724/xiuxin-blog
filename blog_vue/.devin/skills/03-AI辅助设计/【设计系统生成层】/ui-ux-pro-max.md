# UI/UX Pro Max Skill

综合设计系统生成技能，提供完整的设计规范和组件库。

## 设计系统架构

### 三层 Token 架构

#### 1. Primitive Tokens (原始令牌)
```
基础设计令牌，不受语义影响
- 颜色: 纯色值 (HEX/RGB)
- 间距: 像素值
- 字号: 像素值
- 圆角: 像素值
- 阴影: CSS box-shadow
- 动效: 时间、缓动函数
```

#### 2. Semantic Tokens (语义令牌)
```
语义化设计令牌，描述用途
- 颜色: primary, secondary, success, warning, danger
- 间距: xs, sm, md, lg, xl
- 字号: h1, h2, h3, body, caption
- 圆角: sm, md, lg, full
- 阴影: sm, md, lg, xl
- 动效: fast, normal, slow
```

#### 3. Component Tokens (组件令牌)
```
组件特定的设计令牌
- 按钮: 按钮高度、内边距、圆角
- 输入框: 输入框高度、边框、圆角
- 卡片: 卡片内边距、圆角、阴影
- 模态框: 模态框宽度、圆角、阴影
```

## 色彩系统

### 主色板生成
```css
:root {
  /* Primary Colors */
  --color-primary-50: #e6f7ff;
  --color-primary-100: #bae7ff;
  --color-primary-200: #91d5ff;
  --color-primary-300: #69c0ff;
  --color-primary-400: #40a9ff;
  --color-primary-500: #1890ff;
  --color-primary-600: #096dd9;
  --color-primary-700: #0050b3;
  --color-primary-800: #003a8c;
  --color-primary-900: #002766;
}
```

### 语义色映射
```css
:root {
  /* Semantic Colors */
  --color-primary: var(--color-primary-500);
  --color-primary-hover: var(--color-primary-600);
  --color-primary-active: var(--color-primary-700);
  
  --color-success: #52c41a;
  --color-warning: #faad14;
  --color-danger: #ff4d4f;
  --color-info: #1890ff;
}
```

### 中性色系统
```css
:root {
  /* Neutral Colors */
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
}
```

## 间距系统

### 8px 基准网格
```css
:root {
  /* Spacing Scale */
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

### 语义间距
```css
:root {
  /* Semantic Spacing */
  --spacing-xs: var(--spacing-1);
  --spacing-sm: var(--spacing-2);
  --spacing-md: var(--spacing-4);
  --spacing-lg: var(--spacing-6);
  --spacing-xl: var(--spacing-8);
  --spacing-2xl: var(--spacing-12);
}
```

## 字体系统

### 字体族
```css
:root {
  /* Font Families */
  --font-sans: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
  --font-mono: 'JetBrains Mono', 'Fira Code', monospace;
  --font-serif: 'Georgia', 'Times New Roman', serif;
}
```

### 字号规范
```css
:root {
  /* Font Sizes */
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
}
```

### 字重规范
```css
:root {
  /* Font Weights */
  --font-weight-light: 300;
  --font-weight-normal: 400;
  --font-weight-medium: 500;
  --font-weight-semibold: 600;
  --font-weight-bold: 700;
}
```

### 行高规范
```css
:root {
  /* Line Heights */
  --line-height-tight: 1.25;
  --line-height-normal: 1.5;
  --line-height-relaxed: 1.75;
  --line-height-loose: 2;
}
```

## 圆角系统

```css
:root {
  /* Border Radius */
  --radius-none: 0;
  --radius-sm: 2px;
  --radius-md: 4px;
  --radius-lg: 8px;
  --radius-xl: 12px;
  --radius-2xl: 16px;
  --radius-full: 9999px;
}
```

## 阴影系统

```css
:root {
  /* Shadows */
  --shadow-sm: 0 1px 2px 0 rgba(0, 0, 0, 0.05);
  --shadow-md: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
  --shadow-lg: 0 10px 15px -3px rgba(0, 0, 0, 0.1);
  --shadow-xl: 0 20px 25px -5px rgba(0, 0, 0, 0.1);
  --shadow-2xl: 0 25px 50px -12px rgba(0, 0, 0, 0.25);
}
```

## 动效系统

### 缓动函数
```css
:root {
  /* Easing Functions */
  --ease-linear: linear;
  --ease-in: cubic-bezier(0.4, 0, 1, 1);
  --ease-out: cubic-bezier(0, 0, 0.2, 1);
  --ease-in-out: cubic-bezier(0.4, 0, 0.2, 1);
  --ease-bounce: cubic-bezier(0.68, -0.55, 0.265, 1.55);
}
```

### 持续时间
```css
:root {
  /* Durations */
  --duration-fast: 150ms;
  --duration-normal: 300ms;
  --duration-slow: 500ms;
  --duration-slower: 1000ms;
}
```

## 组件规范

### 按钮组件
```css
.btn {
  height: 40px;
  padding: 0 var(--spacing-md);
  border-radius: var(--radius-md);
  font-size: var(--font-size-base);
  font-weight: var(--font-weight-medium);
  transition: all var(--duration-normal) var(--ease-out);
}

.btn-sm {
  height: 32px;
  padding: 0 var(--spacing-sm);
  font-size: var(--font-size-sm);
}

.btn-lg {
  height: 48px;
  padding: 0 var(--spacing-lg);
  font-size: var(--font-size-lg);
}
```

### 输入框组件
```css
.input {
  height: 40px;
  padding: 0 var(--spacing-md);
  border: 1px solid var(--color-gray-300);
  border-radius: var(--radius-md);
  font-size: var(--font-size-base);
  transition: border-color var(--duration-fast) var(--ease-out);
}

.input:focus {
  border-color: var(--color-primary);
  outline: none;
}
```

### 卡片组件
```css
.card {
  padding: var(--spacing-lg);
  border-radius: var(--radius-lg);
  background: white;
  box-shadow: var(--shadow-md);
  border: 1px solid var(--color-gray-200);
}
```

## 响应式断点

```css
:root {
  /* Breakpoints */
  --breakpoint-sm: 640px;
  --breakpoint-md: 768px;
  --breakpoint-lg: 1024px;
  --breakpoint-xl: 1280px;
  --breakpoint-2xl: 1536px;
}
```

## 深色模式

```css
:root[data-theme="dark"] {
  /* Dark Mode Overrides */
  --color-primary: var(--color-primary-400);
  --color-gray-50: #1a1a1a;
  --color-gray-100: #262626;
  --color-gray-200: #404040;
  --color-gray-300: #595959;
  --color-gray-400: #8c8c8c;
  --color-gray-500: #bfbfbf;
  --color-gray-600: #d9d9d9;
  --color-gray-700: #e8e8e8;
  --color-gray-800: #f5f5f5;
  --color-gray-900: #fafafa;
}
```

## 设计系统文档输出

生成完整的设计系统文档，包含：

```markdown
# 设计系统

## 色彩系统
- 主色板
- 辅助色
- 中性色
- 语义色

## 间距系统
- 基准网格
- 语义间距
- 组件间距

## 字体系统
- 字体族
- 字号规范
- 字重规范
- 行高规范

## 组件规范
- 按钮
- 输入框
- 卡片
- 模态框
- 其他组件

## 动效系统
- 缓动函数
- 持续时间
- 动效原则

## 响应式设计
- 断点系统
- 布局适配
- 组件适配
```
