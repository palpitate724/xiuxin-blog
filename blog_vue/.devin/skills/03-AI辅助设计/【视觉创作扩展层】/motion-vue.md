# Motion Vue 动效技能

Motion Vue 组件动效实现技能。

## 安装配置

### 基础安装
```bash
npm install @vueuse/motion
```

### 插件配置
```typescript
// main.ts
import { MotionPlugin } from '@vueuse/motion'
import { createApp } from 'vue'

const app = createApp(App)
app.use(MotionPlugin)
```

## 基础动效

### 淡入淡出
```vue
<template>
  <div
    v-motion
    :initial="{ opacity: 0, y: 100 }"
    :enter="{ opacity: 1, y: 0 }"
    :leave="{ opacity: 0, y: -100 }"
  >
    动画内容
  </div>
</template>
```

### 缩放动画
```vue
<template>
  <div
    v-motion
    :initial="{ scale: 0 }"
    :enter="{ scale: 1 }"
    :leave="{ scale: 0 }"
  >
    缩放内容
  </div>
</template>
```

### 旋转动画
```vue
<template>
  <div
    v-motion
    :initial="{ rotate: -180 }"
    :enter="{ rotate: 0 }"
    :leave="{ rotate: 180 }"
  >
    旋转内容
  </div>
</template>
```

## 交互动效

### 悬停效果
```vue
<template>
  <div
    v-motion
    :initial="{ scale: 1 }"
    :hover="{ scale: 1.1 }"
    :tapped="{ scale: 0.9 }"
  >
    悬停缩放
  </div>
</template>
```

### 点击效果
```vue
<template>
  <div
    v-motion
    :initial="{ scale: 1 }"
    :click="{ scale: 0.95 }"
    @click="handleClick"
  >
    点击反馈
  </div>
</template>

<script setup>
const handleClick = () => {
  console.log('点击事件')
}
</script>
```

### 拖拽效果
```vue
<template>
  <div
    v-motion
    :initial="{ x: 0, y: 0 }"
    :drag="{ 
      initial: { x: 0, y: 0 },
      move: (event, data) => ({
        x: data.deltaX,
        y: data.deltaY
      })
    }"
  >
    拖拽元素
  </div>
</template>
```

## 滚动动效

### 滚动出现
```vue
<template>
  <div
    v-motion
    :initial="{ opacity: 0, y: 50 }"
    :visible-once="{ opacity: 1, y: 0 }"
  >
    滚动出现
  </div>
</template>
```

### 视差滚动
```vue
<template>
  <div
    v-motion
    :initial="{ y: 0 }"
    :visible="{ 
      y: (progress) => progress * 100 
    }"
  >
    视差滚动
  </div>
</template>
```

### 滚动进度
```vue
<template>
  <div
    v-motion
    :initial="{ width: '0%' }"
    :visible="{ 
      width: (progress) => `${progress * 100}%` 
    }"
  >
    滚动进度条
  </div>
</template>
```

## 过渡动画

### 列表过渡
```vue
<template>
  <TransitionGroup
    name="list"
    tag="div"
  >
    <div
      v-for="item in items"
      :key="item.id"
      v-motion
      :initial="{ opacity: 0, x: -20 }"
      :enter="{ opacity: 1, x: 0 }"
      :leave="{ opacity: 0, x: 20 }"
    >
      {{ item.name }}
    </div>
  </TransitionGroup>
</template>

<script setup>
import { ref } from 'vue'

const items = ref([
  { id: 1, name: '项目1' },
  { id: 2, name: '项目2' },
  { id: 3, name: '项目3' }
])
</script>

<style>
.list-enter-active,
.list-leave-active {
  transition: all 0.3s ease;
}

.list-enter-from,
.list-leave-to {
  opacity: 0;
  transform: translateX(-20px);
}
</style>
```

### 路由过渡
```vue
<template>
  <router-view v-slot="{ Component }">
    <Transition
      name="fade"
      mode="out-in"
    >
      <component
        :is="Component"
        v-motion
        :initial="{ opacity: 0 }"
        :enter="{ opacity: 1 }"
        :leave="{ opacity: 0 }"
      />
    </Transition>
  </router-view>
</template>

<style>
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
```

## 高级动效

### 弹簧效果
```vue
<template>
  <div
    v-motion
    :initial="{ scale: 0 }"
    :enter="{ 
      scale: 1,
      transition: {
        type: 'spring',
        stiffness: 260,
        damping: 20
      }
    }"
  >
    弹簧动画
  </div>
</template>
```

### 序列动画
```vue
<template>
  <div
    v-for="(item, index) in items"
    :key="index"
    v-motion
    :initial="{ opacity: 0, y: 20 }"
    :enter="{ 
      opacity: 1, 
      y: 0,
      transition: {
        delay: index * 100,
        duration: 300
      }
    }"
  >
    {{ item }}
  </div>
</template>

<script setup>
import { ref } from 'vue'

const items = ref(['项目1', '项目2', '项目3', '项目4'])
</script>
```

### 路径动画
```vue
<template>
  <div
    v-motion
    :initial="{ pathOffset: 0 }"
    :enter="{ 
      pathOffset: 1,
      transition: {
        duration: 2000,
        ease: 'linear'
      }
    }"
  >
    <svg width="200" height="200">
      <path
        d="M 10 10 Q 100 100 190 10"
        stroke="#1890ff"
        stroke-width="2"
        fill="none"
      />
    </svg>
  </div>
</template>
```

## 自定义指令

### 滚动动画指令
```typescript
// directives/scrollAnimate.ts
import { Directive } from 'vue'
import { useMotion } from '@vueuse/motion'

export const scrollAnimate: Directive = {
  mounted(el, binding) {
    const { apply } = useMotion(el, {
      initial: binding.value.initial || { opacity: 0, y: 50 },
      visibleOnce: binding.value.visible || { opacity: 1, y: 0 }
    })
    
    apply('initial')
  }
}
```

### 使用自定义指令
```vue
<template>
  <div v-scroll-animate="{
    initial: { opacity: 0, y: 50 },
    visible: { opacity: 1, y: 0 }
  }">
    滚动动画
  </div>
</template>

<script setup>
import { scrollAnimate } from '@/directives/scrollAnimate'
</script>
```

## Composable 封装

### 动画 Composable
```typescript
// composables/useAnimation.ts
import { useMotion } from '@vueuse/motion'

export function useAnimation(
  element: Ref<HTMLElement | undefined>,
  config: {
    initial?: any
    enter?: any
    leave?: any
  }
) {
  const { apply, variant } = useMotion(element, config)
  
  const playEnter = () => {
    apply('enter')
  }
  
  const playLeave = () => {
    apply('leave')
  }
  
  const reset = () => {
    apply('initial')
  }
  
  return {
    apply,
    variant,
    playEnter,
    playLeave,
    reset
  }
}
```

### 使用动画 Composable
```vue
<template>
  <div ref="elementRef">
    动画元素
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useAnimation } from '@/composables/useAnimation'

const elementRef = ref<HTMLElement>()
const { playEnter, playLeave } = useAnimation(elementRef, {
  initial: { opacity: 0, y: 50 },
  enter: { opacity: 1, y: 0 },
  leave: { opacity: 0, y: -50 }
})

onMounted(() => {
  playEnter()
})
</script>
```

## 性能优化

### 延迟加载
```vue
<template>
  <div
    v-motion
    :initial="{ opacity: 0 }"
    :visible="{ 
      opacity: 1,
      transition: {
        delay: 300
      }
    }"
  >
    延迟加载内容
  </div>
</template>
```

### 节流动画
```vue
<template>
  <div
    v-motion
    :initial="{ x: 0 }"
    :hover="{ 
      x: 50,
      transition: {
        duration: 300
      }
    }"
  >
    节流动画
  </div>
</template>
```

### 硬件加速
```vue
<template>
  <div
    v-motion
    :initial="{ opacity: 0, transform: 'translateY(50px)' }"
    :enter="{ 
      opacity: 1, 
      transform: 'translateY(0)',
      transition: {
        duration: 300
      }
    }"
    style="will-change: transform, opacity"
  >
    硬件加速动画
  </div>
</template>
```

## 响应式动效

### 媒体查询
```vue
<template>
  <div
    v-motion
    :initial="{ 
      opacity: 0,
      y: isMobile ? 30 : 50
    }"
    :enter="{ 
      opacity: 1,
      y: 0,
      transition: {
        duration: isMobile ? 300 : 500
      }
    }"
  >
    响应式动画
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useMediaQuery } from '@vueuse/core'

const isMobile = useMediaQuery('(max-width: 768px)')
</script>
```

## 可访问性

### 减少动画
```vue
<template>
  <div
    v-motion
    :initial="{ opacity: 0 }"
    :enter="{ 
      opacity: 1,
      transition: prefersReducedMotion ? { duration: 0 } : { duration: 300 }
    }"
  >
    可访问动画
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useMediaQuery } from '@vueuse/core'

const prefersReducedMotion = useMediaQuery('(prefers-reduced-motion: reduce)')
</script>
```

## 最佳实践

### 动画原则
1. 目的明确：每个动画都应该有明确的目的
2. 保持自然：动画应该感觉自然流畅
3. 性能优先：使用 transform 和 opacity 进行动画
4. 用户控制：尊重用户的动画偏好设置
5. 适度使用：不要过度使用动画

### 配置规范
```typescript
// 动画配置规范
export const animationPresets = {
  fadeIn: {
    initial: { opacity: 0 },
    enter: { opacity: 1 },
    leave: { opacity: 0 }
  },
  
  slideUp: {
    initial: { opacity: 0, y: 50 },
    enter: { opacity: 1, y: 0 },
    leave: { opacity: 0, y: -50 }
  },
  
  scaleIn: {
    initial: { opacity: 0, scale: 0.8 },
    enter: { opacity: 1, scale: 1 },
    leave: { opacity: 0, scale: 0.8 }
  },
  
  rotateIn: {
    initial: { opacity: 0, rotate: -10 },
    enter: { opacity: 1, rotate: 0 },
    leave: { opacity: 0, rotate: 10 }
  }
}
```

### 组件封装
```vue
<!-- components/AnimatedWrapper.vue -->
<template>
  <div
    v-motion
    :initial="preset.initial"
    :enter="preset.enter"
    :leave="preset.leave"
  >
    <slot />
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { animationPresets } from '@/utils/animations'

interface Props {
  preset?: keyof typeof animationPresets
  custom?: any
}

const props = withDefaults(defineProps<Props>(), {
  preset: 'fadeIn'
})

const preset = computed(() => {
  return props.custom || animationPresets[props.preset]
})
</script>
```
