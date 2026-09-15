# GSAP 动效技能

GSAP (GreenSock Animation Platform) 动效实现技能。

## 基础动画

### 基础 Tween
```typescript
import { gsap } from 'gsap'

// 基础动画
gsap.to('.element', {
  x: 100,
  duration: 1,
  ease: 'power2.out'
})

// 从状态动画
gsap.from('.element', {
  opacity: 0,
  y: 50,
  duration: 1
})

// 从到动画
gsap.fromTo('.element',
  { opacity: 0, y: 50 },
  { opacity: 1, y: 0, duration: 1 }
)
```

### 缓动函数
```typescript
// 常用缓动函数
const easings = {
  // 线性
  'none': 'none',
  'linear': 'linear',
  
  // 缓入
  'power1.in': 'power1.in',
  'power2.in': 'power2.in',
  'power3.in': 'power3.in',
  'power4.in': 'power4.in',
  
  // 缓出
  'power1.out': 'power1.out',
  'power2.out': 'power2.out',
  'power3.out': 'power3.out',
  'power4.out': 'power4.out',
  
  // 缓入缓出
  'power1.inOut': 'power1.inOut',
  'power2.inOut': 'power2.inOut',
  'power3.inOut': 'power3.inOut',
  'power4.inOut': 'power4.inOut',
  
  // 弹性
  'elastic.out': 'elastic.out(1, 0.3)',
  'back.out': 'back.out(1.7)',
  
  // 弹跳
  'bounce.out': 'bounce.out'
}

// 使用自定义缓动
gsap.to('.element', {
  x: 100,
  duration: 1,
  ease: 'elastic.out(1, 0.3)'
})
```

## Timeline 动画

### 基础 Timeline
```typescript
import { gsap } from 'gsap'

const tl = gsap.timeline()

// 顺序执行
tl.to('.element1', { x: 100, duration: 1 })
  .to('.element2', { y: 100, duration: 1 })
  .to('.element3', { opacity: 0, duration: 1 })

// 并行执行
tl.to('.element1', { x: 100, duration: 1 }, 0)
  .to('.element2', { y: 100, duration: 1 }, 0)
  .to('.element3', { opacity: 0, duration: 1 }, 0)

// 延迟执行
tl.to('.element1', { x: 100, duration: 1 })
  .to('.element2', { y: 100, duration: 1 }, '-=0.5') // 提前0.5秒
  .to('.element3', { opacity: 0, duration: 1 }, '+=0.5') // 延迟0.5秒
```

### Timeline 控制
```typescript
const tl = gsap.timeline()

// 播放控制
tl.play()
tl.pause()
tl.reverse()
tl.restart()

// 跳转
tl.seek(2) // 跳转到2秒处
tl.progress(0.5) // 跳转到50%进度
tl.timeScale(2) // 2倍速播放

// 事件监听
tl.eventCallback('onUpdate', () => {
  console.log('动画更新中')
})

tl.eventCallback('onComplete', () => {
  console.log('动画完成')
})
```

## ScrollTrigger

### 基础滚动触发
```typescript
import { gsap } from 'gsap'
import { ScrollTrigger } from 'gsap/ScrollTrigger'

gsap.registerPlugin(ScrollTrigger)

// 滚动触发动画
gsap.to('.element', {
  scrollTrigger: {
    trigger: '.element',
    start: 'top center',
    end: 'bottom center',
    scrub: true
  },
  x: 500,
  rotation: 360
})

// 滚动固定
gsap.to('.element', {
  scrollTrigger: {
    trigger: '.element',
    start: 'top top',
    end: '+=500',
    pin: true,
    scrub: 1
  },
  scale: 1.5
})
```

### 高级 ScrollTrigger
```typescript
// 批量滚动动画
gsap.utils.toArray('.section').forEach(section => {
  gsap.from(section, {
    scrollTrigger: {
      trigger: section,
      start: 'top 80%',
      end: 'top 20%',
      scrub: 1
    },
    opacity: 0,
    y: 50,
    duration: 1
  })
})

// 视差效果
gsap.to('.parallax-bg', {
  scrollTrigger: {
    trigger: '.parallax-section',
    start: 'top bottom',
    end: 'bottom top',
    scrub: true
  },
  y: 100
})

// 滚动进度条
gsap.to('.progress-bar', {
  scrollTrigger: {
    trigger: 'body',
    start: 'top top',
    end: 'bottom bottom',
    scrub: 0.3
  },
  width: '100%'
})
```

## Vue3 集成

### Vue 组件集成
```vue
<template>
  <div ref="elementRef" class="animated-element">
    动画元素
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import { gsap } from 'gsap'

const elementRef = ref<HTMLElement>()
let animation: gsap.core.Tween | null = null

onMounted(() => {
  if (elementRef.value) {
    animation = gsap.to(elementRef.value, {
      x: 100,
      duration: 1,
      ease: 'power2.out'
    })
  }
})

onUnmounted(() => {
  if (animation) {
    animation.kill()
  }
})
</script>
```

### Composable 封装
```typescript
// composables/useGsapAnimation.ts
import { ref, onMounted, onUnmounted } from 'vue'
import { gsap } from 'gsap'

export function useGsapAnimation(
  element: Ref<HTMLElement | undefined>,
  animationConfig: gsap.TweenVars
) {
  let animation: gsap.core.Tween | null = null

  const play = () => {
    animation?.play()
  }

  const pause = () => {
    animation?.pause()
  }

  const reverse = () => {
    animation?.reverse()
  }

  const restart = () => {
    animation?.restart()
  }

  onMounted(() => {
    if (element.value) {
      animation = gsap.to(element.value, animationConfig)
    }
  })

  onUnmounted(() => {
    if (animation) {
      animation.kill()
    }
  })

  return {
    play,
    pause,
    reverse,
    restart
  }
}
```

## 复杂动画

### 路径动画
```typescript
import { gsap } from 'gsap'
import { MotionPathPlugin } from 'gsap/MotionPathPlugin'

gsap.registerPlugin(MotionPathPlugin)

// 沿路径动画
gsap.to('.element', {
  motionPath: {
    path: '#path',
    align: '#path',
    autoRotate: true
  },
  duration: 2
})

// SVG 路径动画
gsap.to('#path', {
  strokeDashoffset: 0,
  duration: 2,
  ease: 'power2.inOut'
})
```

### 文本动画
```typescript
// 字符动画
const text = 'Hello World'
const chars = text.split('')

gsap.fromTo('.char',
  { opacity: 0, y: 20 },
  {
    opacity: 1,
    y: 0,
    duration: 0.5,
    stagger: 0.1
  }
)

// 文本描边动画
gsap.fromTo('.text',
  { strokeDashoffset: 1000 },
  {
    strokeDashoffset: 0,
    duration: 2,
    ease: 'power2.inOut'
  }
)
```

### 形状变换
```typescript
// SVG 形状变换
gsap.to('#shape1', {
  morphSVG: '#shape2',
  duration: 2,
  ease: 'power2.inOut'
})

// 颜色变换
gsap.to('.element', {
  backgroundColor: '#ff0000',
  duration: 1
})
```

## 性能优化

### 批量动画
```typescript
// 使用 yoyo 重复动画
gsap.to('.element', {
  x: 100,
  duration: 1,
  yoyo: true,
  repeat: -1
})

// 使用 stagger 批量动画
gsap.to('.item', {
  x: 100,
  duration: 1,
  stagger: 0.1
})

// 使用 fromTo 优化性能
gsap.fromTo('.element',
  { opacity: 0 },
  { opacity: 1, duration: 1 }
)
```

### 硬件加速
```typescript
// 使用 transform 和 opacity
gsap.to('.element', {
  x: 100,
  y: 100,
  rotation: 90,
  scale: 1.5,
  opacity: 0.5,
  duration: 1
})

// 避免动画 top/left
// 不推荐
gsap.to('.element', {
  left: 100,
  top: 100,
  duration: 1
})

// 推荐
gsap.to('.element', {
  x: 100,
  y: 100,
  duration: 1
})
```

## 交互动画

### 鼠标跟随
```typescript
import { gsap } from 'gsap'

const cursor = document.querySelector('.cursor')

document.addEventListener('mousemove', (e) => {
  gsap.to(cursor, {
    x: e.clientX,
    y: e.clientY,
    duration: 0.5,
    ease: 'power2.out'
  })
})
```

### 悬停效果
```typescript
const buttons = document.querySelectorAll('.button')

buttons.forEach(button => {
  button.addEventListener('mouseenter', () => {
    gsap.to(button, {
      scale: 1.1,
      duration: 0.3,
      ease: 'back.out(1.7)'
    })
  })
  
  button.addEventListener('mouseleave', () => {
    gsap.to(button, {
      scale: 1,
      duration: 0.3,
      ease: 'power2.out'
    })
  })
})
```

### 点击反馈
```typescript
const elements = document.querySelectorAll('.clickable')

elements.forEach(element => {
  element.addEventListener('click', (e) => {
    const ripple = document.createElement('div')
    ripple.className = 'ripple'
    ripple.style.left = `${e.clientX}px`
    ripple.style.top = `${e.clientY}px`
    
    document.body.appendChild(ripple)
    
    gsap.to(ripple, {
      scale: 10,
      opacity: 0,
      duration: 0.6,
      ease: 'power2.out',
      onComplete: () => ripple.remove()
    })
  })
})
```

## 响应式动画

### 媒体查询动画
```typescript
// 响应式动画配置
const getAnimationConfig = () => {
  const isMobile = window.innerWidth < 768
  
  return {
    x: isMobile ? 50 : 100,
    duration: isMobile ? 0.5 : 1,
    ease: isMobile ? 'power1.out' : 'power2.out'
  }
}

gsap.to('.element', getAnimationConfig())

// ResizeObserver
const resizeObserver = new ResizeObserver(() => {
  gsap.to('.element', getAnimationConfig())
})

resizeObserver.observe(document.body)
```

## 工具函数

### 动画工具
```typescript
// 顺序动画
export function sequenceAnimations(elements: Element[], config: gsap.TweenVars) {
  const tl = gsap.timeline()
  
  elements.forEach((element, index) => {
    tl.to(element, config, index * 0.1)
  })
  
  return tl
}

// 交错动画
export function staggerAnimations(elements: Element[], config: gsap.TweenVars, stagger: number = 0.1) {
  return gsap.to(elements, {
    ...config,
    stagger
  })
}

// 循环动画
export function loopAnimation(element: Element, config: gsap.TweenVars) {
  return gsap.to(element, {
    ...config,
    repeat: -1,
    yoyo: true
  })
}
```

## 最佳实践

### 性能优化
1. 使用 transform 和 opacity 进行动画
2. 避免动画过多元素
3. 使用 will-change 提示浏览器
4. 合理使用 stagger 减少性能开销
5. 及时清理动画避免内存泄漏

### 可访问性
1. 尊重用户的减少动画偏好
2. 提供动画开关选项
3. 确保动画不影响功能使用
4. 为动画元素提供适当的 ARIA 标签

### 维护性
1. 将动画配置提取为常量
2. 使用命名动画便于调试
3. 编写动画文档说明
4. 建立动画组件库
