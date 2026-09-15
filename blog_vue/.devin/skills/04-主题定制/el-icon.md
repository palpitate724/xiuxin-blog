# Element Plus 图标

Element Plus 图标系统使用技能。

## 图标基础

### 安装和引入
```bash
npm install @element-plus/icons-vue
```

### 全局注册
```typescript
// main.ts
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

const app = createApp(App)

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}
```

### 按需引入
```vue
<template>
  <el-icon><User /></el-icon>
  <el-icon><Setting /></el-icon>
  <el-icon><Delete /></el-icon>
</template>

<script setup>
import { User, Setting, Delete } from '@element-plus/icons-vue'
</script>
```

## 图标分类

### 基础图标
```vue
<template>
  <div class="icon-demo">
    <!-- 用户相关 -->
    <el-icon><User /></el-icon>
    <el-icon><UserFilled /></el-icon>
    <el-icon><Avatar /></el-icon>
    
    <!-- 设置相关 -->
    <el-icon><Setting /></el-icon>
    <el-icon><Tools /></el-icon>
    <el-icon><Operation /></el-icon>
    
    <!-- 操作相关 -->
    <el-icon><Edit /></el-icon>
    <el-icon><Delete /></el-icon>
    <el-icon><Plus /></el-icon>
    <el-icon><Minus /></el-icon>
  </div>
</template>
```

### 方向图标
```vue
<template>
  <div class="icon-demo">
    <el-icon><ArrowUp /></el-icon>
    <el-icon><ArrowDown /></el-icon>
    <el-icon><ArrowLeft /></el-icon>
    <el-icon><ArrowRight /></el-icon>
    
    <el-icon><CaretUp /></el-icon>
    <el-icon><CaretDown /></el-icon>
    <el-icon><CaretLeft /></el-icon>
    <el-icon><CaretRight /></el-icon>
    
    <el-icon><Download /></el-icon>
    <el-icon><Upload /></el-icon>
    <el-icon><RefreshLeft /></el-icon>
    <el-icon><RefreshRight /></el-icon>
  </div>
</template>
```

### 文件图标
```vue
<template>
  <div class="icon-demo">
    <el-icon><Folder /></el-icon>
    <el-icon><FolderOpened /></el-icon>
    <el-icon><Document /></el-icon>
    <el-icon><Files /></el-icon>
    <el-icon><Picture /></el-icon>
    <el-icon><VideoPlay /></el-icon>
    <el-icon><Headset /></el-icon>
  </div>
</template>
```

### 媒体图标
```vue
<template>
  <div class="icon-demo">
    <el-icon><Microphone /></el-icon>
    <el-icon><MuteNotification /></el-icon>
    <el-icon><Camera /></el-icon>
    <el-icon><PictureFilled /></el-icon>
    <el-icon><VideoCamera /></el-icon>
    <el-icon><Switch /></el-icon>
  </div>
</template>
```

## 图标样式

### 尺寸控制
```vue
<template>
  <div class="icon-sizes">
    <el-icon :size="12"><User /></el-icon>
    <el-icon :size="16"><User /></el-icon>
    <el-icon :size="20"><User /></el-icon>
    <el-icon :size="24"><User /></el-icon>
    <el-icon :size="32"><User /></el-icon>
    <el-icon :size="48"><User /></el-icon>
  </div>
</template>
```

### 颜色控制
```vue
<template>
  <div class="icon-colors">
    <el-icon color="#409eff"><User /></el-icon>
    <el-icon color="#67c23a"><SuccessFilled /></el-icon>
    <el-icon color="#e6a23c"><WarningFilled /></el-icon>
    <el-icon color="#f56c6c"><CircleCloseFilled /></el-icon>
    <el-icon color="#909399"><InfoFilled /></el-icon>
  </div>
</template>
```

### 自定义样式
```vue
<template>
  <div class="icon-custom">
    <el-icon class="custom-icon"><User /></el-icon>
    <el-icon class="animated-icon"><Loading /></el-icon>
  </div>
</template>

<style scoped>
.custom-icon {
  font-size: 32px;
  color: #409eff;
  transition: all 0.3s;
}

.custom-icon:hover {
  transform: scale(1.2);
  color: #66b1ff;
}

.animated-icon {
  animation: spin 1s linear infinite;
}

@keyframes spin {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}
</style>
```

## 图标组件封装

### 图标按钮组件
```vue
<template>
  <el-button
    :type="type"
    :size="size"
    :disabled="disabled"
    @click="handleClick"
  >
    <el-icon v-if="icon">
      <component :is="icon" />
    </el-icon>
    <span v-if="$slots.default"><slot /></span>
  </el-button>
</template>

<script setup lang="ts">
import { ButtonType, ComponentSize } from 'element-plus'

interface Props {
  icon?: any
  type?: ButtonType
  size?: ComponentSize
  disabled?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  type: 'default',
  size: 'default',
  disabled: false
})

const emit = defineEmits<{
  click: [event: MouseEvent]
}>()

const handleClick = (event: MouseEvent) => {
  emit('click', event)
}
</script>
```

### 图标菜单项组件
```vue
<template>
  <el-menu-item @click="handleClick">
    <el-icon v-if="icon">
      <component :is="icon" />
    </el-icon>
    <span>{{ label }}</span>
  </el-menu-item>
</template>

<script setup lang="ts">
interface Props {
  icon?: any
  label: string
}

defineProps<Props>()

const emit = defineEmits<{
  click: []
}>()

const handleClick = () => {
  emit('click')
}
</script>
```

## 动态图标

### 条件渲染图标
```vue
<template>
  <div>
    <el-icon>
      <component :is="isPlaying ? VideoPause : VideoPlay" />
    </el-icon>
    
    <el-icon>
      <component :is="isLiked ? StarFilled : Star" />
    </el-icon>
    
    <el-icon>
      <component :is="isMuted ? MuteNotification : Microphone" />
    </el-icon>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { VideoPlay, VideoPause, Star, StarFilled, Microphone, MuteNotification } from '@element-plus/icons-vue'

const isPlaying = ref(false)
const isLiked = ref(false)
const isMuted = ref(false)
</script>
```

### 状态图标
```vue
<template>
  <div>
    <el-icon :color="statusColor">
      <component :is="statusIcon" />
    </el-icon>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { SuccessFilled, WarningFilled, CircleCloseFilled, InfoFilled } from '@element-plus/icons-vue'

const props = defineProps({
  status: {
    type: String,
    default: 'success'
  }
})

const statusIcon = computed(() => {
  const iconMap = {
    success: SuccessFilled,
    warning: WarningFilled,
    error: CircleCloseFilled,
    info: InfoFilled
  }
  return iconMap[props.status] || InfoFilled
})

const statusColor = computed(() => {
  const colorMap = {
    success: '#67c23a',
    warning: '#e6a23c',
    error: '#f56c6c',
    info: '#909399'
  }
  return colorMap[props.status] || '#909399'
})
</script>
```

## 图标库管理

### 图标映射表
```typescript
// utils/iconMap.ts
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

export const iconMap = {
  // 用户相关
  user: ElementPlusIconsVue.User,
  'user-filled': ElementPlusIconsVue.UserFilled,
  avatar: ElementPlusIconsVue.Avatar,
  
  // 操作相关
  edit: ElementPlusIconsVue.Edit,
  delete: ElementPlusIconsVue.Delete,
  add: ElementPlusIconsVue.Plus,
  remove: ElementPlusIconsVue.Minus,
  
  // 状态相关
  success: ElementPlusIconsVue.SuccessFilled,
  warning: ElementPlusIconsVue.WarningFilled,
  error: ElementPlusIconsVue.CircleCloseFilled,
  info: ElementPlusIconsVue.InfoFilled,
  
  // 方向相关
  'arrow-up': ElementPlusIconsVue.ArrowUp,
  'arrow-down': ElementPlusIconsVue.ArrowDown,
  'arrow-left': ElementPlusIconsVue.ArrowLeft,
  'arrow-right': ElementPlusIconsVue.ArrowRight
}

export function getIcon(iconName: string) {
  return iconMap[iconName] || ElementPlusIconsVue.QuestionFilled
}
```

### 动态图标组件
```vue
<template>
  <el-icon :size="size" :color="color">
    <component :is="iconComponent" />
  </el-icon>
</template>

<script setup>
import { computed } from 'vue'
import { getIcon } from '@/utils/iconMap'

const props = defineProps({
  name: {
    type: String,
    required: true
  },
  size: {
    type: [String, Number],
    default: 16
  },
  color: {
    type: String,
    default: 'inherit'
  }
})

const iconComponent = computed(() => getIcon(props.name))
</script>
```

## 图标优化

### 图标懒加载
```typescript
// 动态导入图标
const loadIcon = async (iconName: string) => {
  const iconModule = await import(`@element-plus/icons-vue`)
  return iconModule[iconName]
}

// 使用
const IconComponent = defineAsyncComponent(() => loadIcon('User'))
```

### 图标预加载
```typescript
// 预加载常用图标
const preloadIcons = () => {
  const commonIcons = ['User', 'Setting', 'Edit', 'Delete', 'Plus']
  
  commonIcons.forEach(iconName => {
    import(`@element-plus/icons-vue/es/${iconName}.mjs`)
  })
}

// 在应用初始化时调用
preloadIcons()
```

## 图标主题适配

### 深色模式图标
```vue
<template>
  <el-icon :color="isDark ? '#ffffff' : '#333333'>
    <User />
  </el-icon>
</template>

<script setup>
import { computed } from 'vue'
import { useDarkMode } from '@/composables/useDarkMode'

const { isDark } = useDarkMode()
</script>
```

### 自定义主题图标
```vue
<template>
  <el-icon :style="{ color: primaryColor }">
    <User />
  </el-icon>
</template>

<script setup>
import { computed } from 'vue'

const primaryColor = computed(() => {
  return getComputedStyle(document.documentElement)
    .getPropertyValue('--color-primary')
    .trim()
})
</script>
```

## 最佳实践

### 图标使用原则
1. 选择语义明确的图标
2. 保持图标风格一致
3. 合理控制图标尺寸
4. 考虑无障碍访问
5. 提供图标描述

### 性能优化
1. 按需引入图标
2. 预加载常用图标
3. 避免过度使用图标
4. 使用 SVG 图标优化
5. 合理缓存图标组件

### 可访问性
1. 为图标提供 aria-label
2. 确保图标有足够的对比度
3. 支持键盘导航
4. 提供文本替代
5. 考虑屏幕阅读器
