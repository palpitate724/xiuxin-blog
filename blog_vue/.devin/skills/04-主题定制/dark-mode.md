# Dark Mode

深色模式实现技能。

## 深色模式基础

### 基础实现
```css
/* 浅色模式（默认） */
:root {
  --bg-primary: #ffffff;
  --bg-secondary: #f5f5f5;
  --text-primary: #333333;
  --text-secondary: #666666;
  --border-color: #e8e8e8;
}

/* 深色模式 */
[data-theme="dark"] {
  --bg-primary: #1a1a1a;
  --bg-secondary: #2d2d2d;
  --text-primary: #ffffff;
  --text-secondary: #b3b3b3;
  --border-color: #434343;
}
```

### CSS 变量使用
```css
.component {
  background: var(--bg-primary);
  color: var(--text-primary);
  border: 1px solid var(--border-color);
}
```

## Vue3 集成

### 主题切换组件
```vue
<template>
  <div :data-theme="currentTheme">
    <button @click="toggleTheme" class="theme-toggle">
      {{ currentTheme === 'light' ? '🌙' : '☀️' }}
    </button>
    <slot />
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'

const currentTheme = ref('light')

const toggleTheme = () => {
  currentTheme.value = currentTheme.value === 'light' ? 'dark' : 'light'
}

onMounted(() => {
  // 从 localStorage 读取主题
  const savedTheme = localStorage.getItem('theme')
  if (savedTheme) {
    currentTheme.value = savedTheme
  }
  
  // 检测系统主题偏好
  if (!savedTheme && window.matchMedia('(prefers-color-scheme: dark)').matches) {
    currentTheme.value = 'dark'
  }
})

watch(currentTheme, (newTheme) => {
  // 保存到 localStorage
  localStorage.setItem('theme', newTheme)
  // 设置 HTML 属性
  document.documentElement.setAttribute('data-theme', newTheme)
}, { immediate: true })
</script>
```

### Composable 封装
```typescript
// composables/useDarkMode.ts
import { ref, onMounted, watch } from 'vue'

export function useDarkMode() {
  const isDark = ref(false)
  
  const toggleDarkMode = () => {
    isDark.value = !isDark.value
  }
  
  const setDarkMode = (value: boolean) => {
    isDark.value = value
  }
  
  onMounted(() => {
    // 从 localStorage 读取
    const saved = localStorage.getItem('darkMode')
    if (saved !== null) {
      isDark.value = JSON.parse(saved)
    }
    
    // 检测系统偏好
    if (saved === null && window.matchMedia('(prefers-color-scheme: dark)').matches) {
      isDark.value = true
    }
  })
  
  watch(isDark, (value) => {
    // 保存到 localStorage
    localStorage.setItem('darkMode', JSON.stringify(value))
    // 设置 HTML 属性
    document.documentElement.setAttribute('data-theme', value ? 'dark' : 'light')
  }, { immediate: true })
  
  return {
    isDark,
    toggleDarkMode,
    setDarkMode
  }
}
```

## Element Plus 深色模式

### Element Plus 深色主题
```scss
// styles/element-plus/dark.scss
@use "element-plus/theme-chalk/src/dark/css-vars.scss" as *;

// 自定义深色变量
:root {
  --el-color-primary: #40a9ff;
  --el-bg-color: #1a1a1a;
  --el-text-color-primary: #ffffff;
  --el-text-color-regular: #b3b3b3;
  --el-border-color: #434343;
}
```

### 组件级深色模式
```vue
<template>
  <el-config-provider :locale="locale" :theme="isDark ? darkTheme : null">
    <app />
  </el-config-provider>
</template>

<script setup>
import { ref } from 'vue'
import { dark } from 'element-plus/es/themes'

const isDark = ref(false)
const darkTheme = dark
</script>
```

## 系统主题检测

### 自动切换
```typescript
// 检测系统主题偏好
const prefersDarkMode = window.matchMedia('(prefers-color-scheme: dark)')

// 监听系统主题变化
prefersDarkMode.addEventListener('change', (e) => {
  const newTheme = e.matches ? 'dark' : 'light'
  document.documentElement.setAttribute('data-theme', newTheme)
})

// 获取当前系统主题
const systemPrefersDark = prefersDarkMode.matches
```

### 响应式系统主题
```typescript
// composables/useSystemTheme.ts
import { ref, onMounted, onUnmounted } from 'vue'

export function useSystemTheme() {
  const systemTheme = ref<'light' | 'dark'>('light')
  let mediaQuery: MediaQueryList | null = null
  
  const updateTheme = (e: MediaQueryListEvent) => {
    systemTheme.value = e.matches ? 'dark' : 'light'
  }
  
  onMounted(() => {
    mediaQuery = window.matchMedia('(prefers-color-scheme: dark)')
    systemTheme.value = mediaQuery.matches ? 'dark' : 'light'
    mediaQuery.addEventListener('change', updateTheme)
  })
  
  onUnmounted(() => {
    if (mediaQuery) {
      mediaQuery.removeEventListener('change', updateTheme)
    }
  })
  
  return systemTheme
}
```

## 深色模式优化

### 图片适配
```vue
<template>
  <img :src="isDark ? darkImage : lightImage" alt="Theme specific image" />
</template>

<script setup>
import { computed } from 'vue'
import { useDarkMode } from '@/composables/useDarkMode'

const { isDark } = useDarkMode()

const lightImage = computed(() => '/images/logo-light.png')
const darkImage = computed(() => '/images/logo-dark.png')
</script>
```

### 图标适配
```vue
<template>
  <component :is="isDark ? MoonIcon : SunIcon" @click="toggleTheme" />
</template>

<script setup>
import { MoonIcon, SunIcon } from '@element-plus/icons-vue'
import { useDarkMode } from '@/composables/useDarkMode'

const { isDark, toggleDarkMode } = useDarkMode()
</script>
```

### 动画适配
```css
/* 深色模式动画优化 */
[data-theme="dark"] {
  /* 减少动画在深色模式下的视觉冲击 */
  .component {
    animation-duration: 0.8s;
    transition-duration: 0.3s;
  }
}

/* 减少动画偏好 */
@media (prefers-reduced-motion: reduce) {
  * {
    animation-duration: 0.01ms !important;
    transition-duration: 0.01ms !important;
  }
}
```

## 深色模式设计

### 色彩调整
```css
/* 深色模式色彩调整原则 */
[data-theme="dark"] {
  /* 降低亮度，提高对比度 */
  --primary-color: #40a9ff; /* 比浅色模式更亮 */
  
  /* 减少纯色使用，增加灰度 */
  --bg-primary: #1a1a1a;
  --bg-secondary: #2d2d2d;
  
  /* 文字颜色需要更高对比度 */
  --text-primary: #ffffff;
  --text-secondary: #b3b3b3;
  
  /* 边框颜色需要更明显 */
  --border-color: #434343;
}
```

### 阴影调整
```css
/* 深色模式阴影调整 */
[data-theme="dark"] {
  /* 深色模式下阴影需要更明显 */
  --shadow-sm: 0 2px 4px rgba(0, 0, 0, 0.3);
  --shadow-md: 0 4px 8px rgba(0, 0, 0, 0.4);
  --shadow-lg: 0 8px 16px rgba(0, 0, 0, 0.5);
  
  /* 使用内阴影增强层次感 */
  --inset-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.3);
}
```

## 最佳实践

### 切换时机
1. 提供明显的切换按钮
2. 支持键盘快捷键切换
3. 记住用户偏好
4. 尊重系统偏好
5. 提供自动切换选项

### 过渡效果
```css
/* 平滑的主题切换过渡 */
* {
  transition: background-color 0.3s ease, color 0.3s ease, border-color 0.3s ease;
}

/* 避免某些属性过渡 */
[data-theme="dark"] {
  .no-transition {
    transition: none !important;
  }
}
```

### 可访问性
1. 确保足够的对比度
2. 提供明显的切换指示
3. 支持键盘操作
4. 遵循 WCAG 标准
5. 考虑色盲用户

### 性能优化
1. 使用 CSS 变量避免重复定义
2. 避免强制重绘
3. 懒加载深色模式资源
4. 使用 GPU 加速动画
5. 优化图片加载

### 测试检查
- [ ] 所有组件在深色模式下正常显示
- [ ] 文字对比度符合 WCAG 标准
- [ ] 图片和图标适配深色模式
- [ ] 动画效果流畅自然
- [ ] 切换过程平滑无闪烁
- [ ] 性能无明显下降
- [ ] 支持系统主题偏好
- [ ] 用户偏好正确保存
