# Icon System

图标系统设计和使用技能。

## 图标系统架构

### 图标分类体系
```
图标系统
├── 基础图标
│   ├── 用户图标
│   ├── 操作图标
│   ├── 状态图标
│   └── 导航图标
├── 业务图标
│   ├── 功能图标
│   ├── 品牌图标
│   └── 行业图标
└── 装饰图标
    ├── 背景图标
    ├── 分隔图标
    └── 氛围图标
```

## 图标设计规范

### 设计原则
1. **一致性**: 保持风格统一
2. **简洁性**: 避免过度复杂
3. **可识别性**: 清晰表达含义
4. **可扩展性**: 适应不同尺寸
5. **适应性**: 支持多种主题

### 尺寸规范
```css
/* 图标尺寸标准 */
.icon-xs { width: 12px; height: 12px; }
.icon-sm { width: 16px; height: 16px; }
.icon-md { width: 20px; height: 20px; }
.icon-lg { width: 24px; height: 24px; }
.icon-xl { width: 32px; height: 32px; }
.icon-2xl { width: 48px; height: 48px; }
.icon-3xl { width: 64px; height: 64px; }
```

### 网格系统
```css
/* 图标网格系统 */
.icon-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(64px, 1fr));
  gap: 16px;
  padding: 16px;
}

.icon-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 12px;
  border: 1px solid var(--border-color);
  border-radius: var(--radius-md);
  cursor: pointer;
  transition: all 0.3s;
}

.icon-item:hover {
  border-color: var(--primary-color);
  background: var(--bg-secondary);
}
```

## SVG 图标

### 基础 SVG 图标
```vue
<template>
  <svg
    :width="size"
    :height="size"
    :viewBox="viewBox"
    :fill="fill"
    :color="color"
    class="custom-icon"
  >
    <path :d="pathData" />
  </svg>
</template>

<script setup>
interface Props {
  size?: number | string
  viewBox?: string
  fill?: string
  color?: string
  pathData: string
}

const props = withDefaults(defineProps<Props>(), {
  size: 24,
  viewBox: '0 0 24 24',
  fill: 'currentColor',
  color: 'currentColor'
})
</script>
```

### SVG 图标库
```typescript
// icons/svgIcons.ts
export const svgIcons = {
  user: {
    pathData: 'M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z',
    viewBox: '0 0 24 24'
  },
  settings: {
    pathData: 'M19.14 12.94c.04-.3.06-.61.06-.94 0-.32-.02-.64-.07-.94l2.03-1.58c.18-.14.23-.41.12-.61l-1.92-3.32c-.12-.22-.37-.29-.59-.22l-2.39.96c-.5-.38-1.03-.7-1.62-.94l-.36-2.54c-.04-.24-.24-.41-.48-.41h-3.84c-.24 0-.43.17-.47.41l-.36 2.54c-.59.24-1.13.57-1.62.94l-2.39-.96c-.22-.08-.47 0-.59.22L2.74 8.87c-.12.21-.08.47.12.61l2.03 1.58c-.05.3-.09.63-.09.94s.02.64.07.94l-2.03 1.58c-.18.14-.23.41-.12.61l1.92 3.32c.12.22.37.29.59.22l2.39-.96c.5.38 1.03.7 1.62.94l.36 2.54c.05.24.24.41.48.41h3.84c.24 0 .44-.17.47-.41l.36-2.54c.59-.24 1.13-.56 1.62-.94l2.39.96c.22.08.47 0 .59-.22l1.92-3.32c.12-.22.07-.47-.12-.61l-2.01-1.58zM12 15.6c-1.98 0-3.6-1.62-3.6-3.6s1.62-3.6 3.6-3.6 3.6 1.62 3.6 3.6-1.62 3.6-3.6 3.6z',
    viewBox: '0 0 24 24'
  }
}
```

## 图标组件封装

### 通用图标组件
```vue
<template>
  <component
    :is="iconComponent"
    :size="computedSize"
    :color="computedColor"
    class="base-icon"
    :class="iconClass"
  />
</template>

<script setup>
import { computed, defineAsyncComponent } from 'vue'
import { svgIcons } from '@/icons/svgIcons'

const props = defineProps({
  name: {
    type: String,
    required: true
  },
  size: {
    type: [String, Number],
    default: 24
  },
  color: {
    type: String,
    default: 'currentColor'
  },
  type: {
    type: String,
    default: 'svg' // 'svg' | 'element'
  }
})

const computedSize = computed(() => {
  return typeof props.size === 'number' ? `${props.size}px` : props.size
})

const computedColor = computed(() => {
  return props.color === 'currentColor' 
    ? 'currentColor' 
    : props.color
})

const iconClass = computed(() => {
  return `icon-${props.name}`
})

const iconComponent = computed(() => {
  if (props.type === 'svg') {
    return defineAsyncComponent(() => import('./SvgIcon.vue'))
  } else {
    // Element Plus 图标或其他图标库
    return defineAsyncComponent(() => import(`@element-plus/icons-vue/es/${props.name}.mjs`))
  }
})
</script>
```

### 图标按钮组件
```vue
<template>
  <button
    :class="['icon-button', buttonClass]"
    :disabled="disabled"
    @click="handleClick"
  >
    <BaseIcon
      :name="icon"
      :size="iconSize"
      :color="iconColor"
    />
    <span v-if="label" class="icon-button-label">{{ label }}</span>
  </button>
</template>

<script setup>
import BaseIcon from './BaseIcon.vue'

const props = defineProps({
  icon: {
    type: String,
    required: true
  },
  label: String,
  iconSize: {
    type: [String, Number],
    default: 20
  },
  iconColor: {
    type: String,
    default: 'currentColor'
  },
  disabled: Boolean,
  variant: {
    type: String,
    default: 'default' // 'default' | 'primary' | 'ghost'
  }
})

const buttonClass = computed(() => {
  return `icon-button--${props.variant}`
})

const emit = defineEmits(['click'])

const handleClick = (event) => {
  if (!props.disabled) {
    emit('click', event)
  }
}
</script>

<style scoped>
.icon-button {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 8px 12px;
  border: 1px solid var(--border-color);
  border-radius: var(--radius-md);
  background: var(--bg-primary);
  cursor: pointer;
  transition: all 0.3s;
}

.icon-button:hover:not(:disabled) {
  border-color: var(--primary-color);
  color: var(--primary-color);
}

.icon-button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.icon-button--primary {
  background: var(--primary-color);
  color: white;
  border-color: var(--primary-color);
}

.icon-button--ghost {
  background: transparent;
  border: none;
}
</style>
```

## 图标管理系统

### 图标注册表
```typescript
// utils/iconRegistry.ts
class IconRegistry {
  private icons: Map<string, any> = new Map()
  
  register(name: string, component: any) {
    this.icons.set(name, component)
  }
  
  get(name: string): any {
    return this.icons.get(name)
  }
  
  has(name: string): boolean {
    return this.icons.has(name)
  }
  
  list(): string[] {
    return Array.from(this.icons.keys())
  }
}

export const iconRegistry = new IconRegistry()

// 注册默认图标
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

Object.entries(ElementPlusIconsVue).forEach(([name, component]) => {
  iconRegistry.register(name, component)
})
```

### 图标预览组件
```vue
<template>
  <div class="icon-preview">
    <div class="icon-search">
      <el-input
        v-model="searchQuery"
        placeholder="搜索图标..."
        clearable
      />
    </div>
    
    <div class="icon-grid">
      <div
        v-for="iconName in filteredIcons"
        :key="iconName"
        class="icon-item"
        @click="selectIcon(iconName)"
      >
        <BaseIcon :name="iconName" :size="32" />
        <span class="icon-name">{{ iconName }}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { iconRegistry } from '@/utils/iconRegistry'
import BaseIcon from './BaseIcon.vue'

const searchQuery = ref('')
const selectedIcon = ref('')

const allIcons = computed(() => iconRegistry.list())

const filteredIcons = computed(() => {
  if (!searchQuery.value) {
    return allIcons.value
  }
  return allIcons.value.filter(iconName =>
    iconName.toLowerCase().includes(searchQuery.value.toLowerCase())
  )
})

const selectIcon = (iconName: string) => {
  selectedIcon.value = iconName
  // 复制图标名称到剪贴板
  navigator.clipboard.writeText(iconName)
}
</script>

<style scoped>
.icon-preview {
  padding: 20px;
}

.icon-search {
  margin-bottom: 20px;
}

.icon-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(80px, 1fr));
  gap: 16px;
}

.icon-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 12px;
  border: 1px solid var(--border-color);
  border-radius: var(--radius-md);
  cursor: pointer;
  transition: all 0.3s;
}

.icon-item:hover {
  border-color: var(--primary-color);
  background: var(--bg-secondary);
}

.icon-name {
  margin-top: 8px;
  font-size: 12px;
  text-align: center;
  word-break: break-all;
}
</style>
```

## 图标性能优化

### 图标按需加载
```typescript
// 动态导入图标
const loadIcon = async (iconName: string) => {
  try {
    const iconModule = await import(`@element-plus/icons-vue/es/${iconName}.mjs`)
    return iconModule.default
  } catch (error) {
    console.error(`Failed to load icon: ${iconName}`, error)
    return null
  }
}

// 使用懒加载
const LazyIcon = defineAsyncComponent({
  loader: () => loadIcon('User'),
  loadingComponent: LoadingSpinner,
  errorComponent: ErrorIcon
})
```

### 图标缓存
```typescript
// 图标缓存管理
class IconCache {
  private cache: Map<string, any> = new Map()
  private maxSize: number = 50
  
  get(key: string): any {
    return this.cache.get(key)
  }
  
  set(key: string, value: any): void {
    if (this.cache.size >= this.maxSize) {
      const firstKey = this.cache.keys().next().value
      this.cache.delete(firstKey)
    }
    this.cache.set(key, value)
  }
  
  clear(): void {
    this.cache.clear()
  }
}

export const iconCache = new IconCache()
```

## 图标主题适配

### 主题图标颜色
```css
/* 浅色主题 */
:root {
  --icon-color-primary: #333333;
  --icon-color-secondary: #666666;
  --icon-color-disabled: #cccccc;
}

/* 深色主题 */
[data-theme="dark"] {
  --icon-color-primary: #ffffff;
  --icon-color-secondary: #b3b3b3;
  --icon-color-disabled: #666666;
}
```

### 响应式图标颜色
```vue
<template>
  <BaseIcon
    :name="iconName"
    :color="iconColor"
  />
</template>

<script setup>
import { computed } from 'vue'
import { useDarkMode } from '@/composables/useDarkMode'

const props = defineProps({
  iconName: String
})

const { isDark } = useDarkMode()

const iconColor = computed(() => {
  return isDark.value 
    ? 'var(--icon-color-primary)' 
    : 'var(--icon-color-primary)'
})
</script>
```

## 可访问性

### ARIA 标签
```vue
<template>
  <button
    :aria-label="ariaLabel"
    :title="title"
    class="icon-button"
  >
    <BaseIcon :name="iconName" aria-hidden="true" />
  </button>
</template>

<script setup>
const props = defineProps({
  iconName: {
    type: String,
    required: true
  },
  ariaLabel: {
    type: String,
    required: true
  },
  title: String
})
</script>
```

### 键盘导航
```vue
<template>
  <div
    class="icon-wrapper"
    tabindex="0"
    :aria-label="ariaLabel"
    @keydown="handleKeydown"
  >
    <BaseIcon :name="iconName" />
  </div>
</template>

<script setup>
const handleKeydown = (event: KeyboardEvent) => {
  if (event.key === 'Enter' || event.key === ' ') {
    event.preventDefault()
    // 触发点击事件
  }
}
</script>
```

## 最佳实践

### 图标选择原则
1. 优先使用标准图标
2. 保持图标语义清晰
3. 避免过度使用图标
4. 考虑文化差异
5. 提供图标描述

### 性能优化
1. 按需加载图标
2. 使用 SVG 格式
3. 合理缓存图标
4. 避免重复渲染
5. 优化图标文件大小

### 维护管理
1. 建立图标命名规范
2. 提供图标文档
3. 定期审查图标使用
4. 更新图标库版本
5. 收集用户反馈
