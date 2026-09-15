# CSS Variable Theming

CSS 变量主题定制技能。

## CSS 变量基础

### 定义 CSS 变量
```css
:root {
  /* 基础变量 */
  --primary-color: #1890ff;
  --secondary-color: #52c41a;
  --text-color: #333333;
  --bg-color: #ffffff;
  --border-color: #e8e8e8;
  
  /* 间距变量 */
  --spacing-xs: 4px;
  --spacing-sm: 8px;
  --spacing-md: 16px;
  --spacing-lg: 24px;
  --spacing-xl: 32px;
  
  /* 字体变量 */
  --font-family: 'Inter', sans-serif;
  --font-size-base: 16px;
  --font-size-lg: 18px;
  --font-size-xl: 24px;
}
```

### 使用 CSS 变量
```css
.component {
  color: var(--primary-color);
  background: var(--bg-color);
  padding: var(--spacing-md);
  font-family: var(--font-family);
  font-size: var(--font-size-base);
}
```

## 主题切换

### 主题定义
```css
/* 浅色主题 */
:root {
  --primary-color: #1890ff;
  --text-color: #333333;
  --bg-color: #ffffff;
  --border-color: #e8e8e8;
}

/* 深色主题 */
[data-theme="dark"] {
  --primary-color: #40a9ff;
  --text-color: #ffffff;
  --bg-color: #1a1a1a;
  --border-color: #434343;
}
```

### 主题切换实现
```vue
<template>
  <div :data-theme="currentTheme">
    <button @click="toggleTheme">切换主题</button>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const currentTheme = ref('light')

const toggleTheme = () => {
  currentTheme.value = currentTheme.value === 'light' ? 'dark' : 'light'
}
</script>
```

## Vue3 集成

### 响应式主题
```vue
<template>
  <div :data-theme="theme">
    <slot />
  </div>
</template>

<script setup>
import { provide, ref, watch } from 'vue'

const theme = ref(localStorage.getItem('theme') || 'light')

provide('theme', theme)

watch(theme, (newTheme) => {
  localStorage.setItem('theme', newTheme)
  document.documentElement.setAttribute('data-theme', newTheme)
}, { immediate: true })
</script>
```

### Composable 封装
```typescript
// composables/useTheme.ts
import { ref, watch } from 'vue'

export function useTheme() {
  const theme = ref(localStorage.getItem('theme') || 'light')
  
  const setTheme = (newTheme: string) => {
    theme.value = newTheme
    localStorage.setItem('theme', newTheme)
    document.documentElement.setAttribute('data-theme', newTheme)
  }
  
  const toggleTheme = () => {
    const newTheme = theme.value === 'light' ? 'dark' : 'light'
    setTheme(newTheme)
  }
  
  // 初始化主题
  watch(theme, (newTheme) => {
    document.documentElement.setAttribute('data-theme', newTheme)
  }, { immediate: true })
  
  return {
    theme,
    setTheme,
    toggleTheme
  }
}
```

## 动态主题

### 用户自定义主题
```vue
<template>
  <div>
    <div class="theme-controls">
      <label>
        主色调:
        <input type="color" v-model="customTheme.primary" @input="applyCustomTheme" />
      </label>
      <label>
        文字颜色:
        <input type="color" v-model="customTheme.text" @input="applyCustomTheme" />
      </label>
      <label>
        背景颜色:
        <input type="color" v-model="customTheme.background" @input="applyCustomTheme" />
      </label>
    </div>
  </div>
</template>

<script setup>
import { reactive } from 'vue'

const customTheme = reactive({
  primary: '#1890ff',
  text: '#333333',
  background: '#ffffff'
})

const applyCustomTheme = () => {
  document.documentElement.style.setProperty('--primary-color', customTheme.primary)
  document.documentElement.style.setProperty('--text-color', customTheme.text)
  document.documentElement.style.setProperty('--bg-color', customTheme.background)
}
</script>
```

### 预设主题
```typescript
// themes/presets.ts
export const themePresets = {
  default: {
    primary: '#1890ff',
    secondary: '#52c41a',
    text: '#333333',
    background: '#ffffff'
  },
  
  ocean: {
    primary: '#0066cc',
    secondary: '#0099cc',
    text: '#1a1a2e',
    background: '#f0f8ff'
  },
  
  forest: {
    primary: '#2d5016',
    secondary: '#4a7c23',
    text: '#1a1a1a',
    background: '#f5f5dc'
  },
  
  sunset: {
    primary: '#ff6b35',
    secondary: '#f7931e',
    text: '#2d2d2d',
    background: '#fff8e7'
  }
}

export function applyThemePreset(presetName: string) {
  const preset = themePresets[presetName]
  if (preset) {
    Object.entries(preset).forEach(([key, value]) => {
      document.documentElement.style.setProperty(`--${key}-color`, value)
    })
  }
}
```

## Element Plus 主题定制

### Element Plus 变量覆盖
```css
/* styles/element-plus/variables.scss */
@forward 'element-plus/theme-chalk/src/common/var.scss' with (
  $colors: (
    'primary': (
      'base': #1890ff,
    ),
  ),
);
```

### SCSS 变量定制
```scss
// styles/element-plus/custom.scss
@use "element-plus/theme-chalk/src/index" as *;

// 覆盖变量
$el-color-primary: #1890ff;
$el-color-success: #52c41a;
$el-color-warning: #faad14;
$el-color-danger: #ff4d4f;
$el-color-info: #1890ff;

// 覆盖组件变量
$el-button-border-radius: 4px;
$el-input-border-radius: 4px;
$el-card-border-radius: 8px;
```

## 响应式主题

### 媒体查询主题
```css
/* 默认主题 */
:root {
  --font-size-base: 16px;
  --spacing-unit: 16px;
}

/* 移动端主题 */
@media (max-width: 768px) {
  :root {
    --font-size-base: 14px;
    --spacing-unit: 12px;
  }
}

/* 大屏主题 */
@media (min-width: 1920px) {
  :root {
    --font-size-base: 18px;
    --spacing-unit: 20px;
  }
}
```

### 容器查询主题
```css
.component {
  --primary-color: #1890ff;
  --padding: 16px;
}

@container (min-width: 500px) {
  .component {
    --primary-color: #52c41a;
    --padding: 24px;
  }
}
```

## 性能优化

### CSS 变量性能
```css
/* ✅ 推荐：在根元素定义变量 */
:root {
  --primary-color: #1890ff;
}

/* ❌ 不推荐：在频繁变化的元素上定义变量 */
.dynamic-element {
  --dynamic-color: #1890ff;
}
```

### 变量缓存
```typescript
// 缓存计算后的变量值
const cachedStyles = new Map<string, string>()

function getComputedStyleValue(variable: string): string {
  if (cachedStyles.has(variable)) {
    return cachedStyles.get(variable)!
  }
  
  const value = getComputedStyle(document.documentElement)
    .getPropertyValue(variable)
    .trim()
  
  cachedStyles.set(variable, value)
  return value
}
```

## 可访问性

### 对比度检查
```typescript
function getContrastRatio(color1: string, color2: string): number {
  const lum1 = getLuminance(color1)
  const lum2 = getLuminance(color2)
  const brightest = Math.max(lum1, lum2)
  const darkest = Math.min(lum1, lum2)
  return (brightest + 0.05) / (darkest + 0.05)
}

function getLuminance(color: string): number {
  const rgb = hexToRgb(color)
  const [r, g, b] = rgb.map(channel => {
    channel = channel / 255
    return channel <= 0.03928
      ? channel / 12.92
      : Math.pow((channel + 0.055) / 1.055, 2.4)
  })
  return 0.2126 * r + 0.7152 * g + 0.0722 * b
}

// 检查主题对比度
function validateThemeContrast(theme: ThemeColors): boolean {
  const contrastRatio = getContrastRatio(theme.text, theme.background)
  return contrastRatio >= 4.5 // WCAG AA 标准
}
```

### 减少动画偏好
```css
@media (prefers-reduced-motion: reduce) {
  * {
    animation-duration: 0.01ms !important;
    animation-iteration-count: 1 !important;
    transition-duration: 0.01ms !important;
  }
}
```

## 最佳实践

### 变量命名规范
1. 使用语义化命名
2. 使用 kebab-case 命名
3. 分层组织变量
4. 避免过于具体的命名
5. 保持命名一致性

### 变量组织原则
1. 基础变量在 :root 中定义
2. 主题变量在主题选择器中定义
3. 组件变量在组件作用域中定义
4. 使用 CSS 变量替代硬编码值
5. 提供变量默认值

### 性能考虑
1. 避免在频繁变化的元素上使用变量
2. 合理使用 CSS 变量继承
3. 减少变量计算复杂度
4. 缓存计算后的变量值
5. 避免过度嵌套变量
