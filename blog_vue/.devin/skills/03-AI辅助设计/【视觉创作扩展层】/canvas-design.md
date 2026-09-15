# Canvas Design Skill

Canvas 视觉设计技能，提供 Canvas 绘图和图形设计能力。

## 基础 Canvas 操作

### 画布初始化
```typescript
// Canvas 初始化
const canvas = document.getElementById('myCanvas') as HTMLCanvasElement
const ctx = canvas.getContext('2d')!

// 设置画布尺寸
canvas.width = 800
canvas.height = 600

// 高清屏适配
function setupCanvas(canvas: HTMLCanvasElement) {
  const dpr = window.devicePixelRatio || 1
  const rect = canvas.getBoundingClientRect()
  
  canvas.width = rect.width * dpr
  canvas.height = rect.height * dpr
  
  const ctx = canvas.getContext('2d')!
  ctx.scale(dpr, dpr)
  
  canvas.style.width = `${rect.width}px`
  canvas.style.height = `${rect.height}px`
  
  return ctx
}
```

### 基础绘图
```typescript
// 绘制矩形
ctx.fillStyle = '#1890ff'
ctx.fillRect(10, 10, 100, 50)

// 绘制圆形
ctx.beginPath()
ctx.arc(150, 50, 25, 0, Math.PI * 2)
ctx.fillStyle = '#52c41a'
ctx.fill()

// 绘制线条
ctx.beginPath()
ctx.moveTo(200, 10)
ctx.lineTo(300, 50)
ctx.strokeStyle = '#ff4d4f'
ctx.lineWidth = 2
ctx.stroke()

// 绘制文本
ctx.font = '16px Arial'
ctx.fillStyle = '#333'
ctx.fillText('Hello Canvas', 10, 100)
```

## 图形变换

### 基础变换
```typescript
// 平移
ctx.translate(100, 100)

// 旋转
ctx.rotate(Math.PI / 4) // 45度

// 缩放
ctx.scale(1.5, 1.5)

// 重置变换
ctx.setTransform(1, 0, 0, 1, 0, 0)
```

### 组合变换
```typescript
function drawRotatedRect(x: number, y: number, width: number, height: number, angle: number) {
  ctx.save()
  ctx.translate(x + width / 2, y + height / 2)
  ctx.rotate(angle)
  ctx.fillStyle = '#1890ff'
  ctx.fillRect(-width / 2, -height / 2, width, height)
  ctx.restore()
}

drawRotatedRect(100, 100, 50, 30, Math.PI / 6)
```

## 渐变和图案

### 线性渐变
```typescript
const gradient = ctx.createLinearGradient(0, 0, 200, 0)
gradient.addColorStop(0, '#1890ff')
gradient.addColorStop(1, '#52c41a')

ctx.fillStyle = gradient
ctx.fillRect(10, 10, 200, 100)
```

### 径向渐变
```typescript
const radialGradient = ctx.createRadialGradient(100, 100, 0, 100, 100, 50)
radialGradient.addColorStop(0, '#1890ff')
radialGradient.addColorStop(1, 'transparent')

ctx.fillStyle = radialGradient
ctx.fillRect(50, 50, 100, 100)
```

### 图案填充
```typescript
// 创建图案
const patternCanvas = document.createElement('canvas')
patternCanvas.width = 20
patternCanvas.height = 20
const patternCtx = patternCanvas.getContext('2d')!

patternCtx.fillStyle = '#f0f0f0'
patternCtx.fillRect(0, 0, 20, 20)
patternCtx.fillStyle = '#e0e0e0'
patternCtx.fillRect(0, 0, 10, 10)
patternCtx.fillRect(10, 10, 10, 10)

const pattern = ctx.createPattern(patternCanvas, 'repeat')
ctx.fillStyle = pattern
ctx.fillRect(0, 0, 400, 300)
```

## 动画效果

### 基础动画
```typescript
let x = 0
let y = 100
let dx = 2
let dy = 1

function animate() {
  ctx.clearRect(0, 0, canvas.width, canvas.height)
  
  // 绘制移动的圆
  ctx.beginPath()
  ctx.arc(x, y, 20, 0, Math.PI * 2)
  ctx.fillStyle = '#1890ff'
  ctx.fill()
  
  // 更新位置
  x += dx
  y += dy
  
  // 边界检测
  if (x + 20 > canvas.width || x - 20 < 0) dx = -dx
  if (y + 20 > canvas.height || y - 20 < 0) dy = -dy
  
  requestAnimationFrame(animate)
}

animate()
```

### 缓动动画
```typescript
// 缓动函数
function easeInOutQuad(t: number): number {
  return t < 0.5 ? 2 * t * t : -1 + (4 - 2 * t) * t
}

function animateValue(start: number, end: number, duration: number) {
  const startTime = performance.now()
  
  function update(currentTime: number) {
    const elapsed = currentTime - startTime
    const progress = Math.min(elapsed / duration, 1)
    const eased = easeInOutQuad(progress)
    const current = start + (end - start) * eased
    
    ctx.clearRect(0, 0, canvas.width, canvas.height)
    ctx.font = '24px Arial'
    ctx.fillText(Math.round(current).toString(), 100, 100)
    
    if (progress < 1) {
      requestAnimationFrame(update)
    }
  }
  
  requestAnimationFrame(update)
}

animateValue(0, 100, 2000)
```

## 图形组合

### 剪裁
```typescript
// 创建剪裁区域
ctx.save()
ctx.beginPath()
ctx.arc(150, 150, 100, 0, Math.PI * 2)
ctx.clip()

// 绘制被剪裁的内容
ctx.fillStyle = '#1890ff'
ctx.fillRect(50, 50, 200, 200)

ctx.restore()
```

### 路径操作
```typescript
// 合并路径
ctx.beginPath()
ctx.arc(100, 100, 50, 0, Math.PI * 2)
ctx.arc(150, 100, 50, 0, Math.PI * 2)
ctx.fillStyle = '#1890ff'
ctx.fill('evenodd') // 奇偶规则
```

## 图像处理

### 图像绘制
```typescript
const image = new Image()
image.src = 'path/to/image.jpg'

image.onload = () => {
  // 绘制原图
  ctx.drawImage(image, 10, 10)
  
  // 缩放绘制
  ctx.drawImage(image, 10, 10, 200, 150)
  
  // 裁剪绘制
  ctx.drawImage(image, 0, 0, 100, 100, 10, 10, 200, 200)
}
```

### 图像滤镜
```typescript
function applyFilter(ctx: CanvasRenderingContext2D, filter: string) {
  ctx.filter = filter
}

// 应用滤镜
ctx.filter = 'blur(5px)'
ctx.drawImage(image, 0, 0)

ctx.filter = 'grayscale(100%)'
ctx.drawImage(image, 100, 0)

ctx.filter = 'brightness(150%)'
ctx.drawImage(image, 200, 0)
```

## 交互处理

### 鼠标交互
```typescript
interface MousePosition {
  x: number
  y: number
}

const mousePos: MousePosition = { x: 0, y: 0 }

canvas.addEventListener('mousemove', (e) => {
  const rect = canvas.getBoundingClientRect()
  mousePos.x = e.clientX - rect.left
  mousePos.y = e.clientY - rect.top
})

function drawInteractive() {
  ctx.clearRect(0, 0, canvas.width, canvas.height)
  
  // 绘制跟随鼠标的圆
  ctx.beginPath()
  ctx.arc(mousePos.x, mousePos.y, 20, 0, Math.PI * 2)
  ctx.fillStyle = '#1890ff'
  ctx.fill()
  
  requestAnimationFrame(drawInteractive)
}

drawInteractive()
```

### 点击检测
```typescript
interface ClickableShape {
  x: number
  y: number
  width: number
  height: number
  color: string
}

const shapes: ClickableShape[] = [
  { x: 50, y: 50, width: 100, height: 50, color: '#1890ff' },
  { x: 200, y: 50, width: 100, height: 50, color: '#52c41a' }
]

canvas.addEventListener('click', (e) => {
  const rect = canvas.getBoundingClientRect()
  const x = e.clientX - rect.left
  const y = e.clientY - rect.top
  
  shapes.forEach(shape => {
    if (x >= shape.x && x <= shape.x + shape.width &&
        y >= shape.y && y <= shape.y + shape.height) {
      console.log('点击了形状:', shape.color)
    }
  })
})
```

## 性能优化

### 离屏渲染
```typescript
// 创建离屏画布
const offscreenCanvas = document.createElement('canvas')
offscreenCanvas.width = 200
offscreenCanvas.height = 200
const offscreenCtx = offscreenCanvas.getContext('2d')!

// 在离屏画布上绘制复杂图形
function drawComplexShape(ctx: CanvasRenderingContext2D) {
  ctx.fillStyle = '#1890ff'
  ctx.fillRect(0, 0, 200, 200)
  ctx.beginPath()
  ctx.arc(100, 100, 50, 0, Math.PI * 2)
  ctx.fillStyle = '#52c41a'
  ctx.fill()
}

drawComplexShape(offscreenCtx)

// 将离屏画布绘制到主画布
function drawFrame() {
  ctx.clearRect(0, 0, canvas.width, canvas.height)
  ctx.drawImage(offscreenCanvas, 0, 0)
  requestAnimationFrame(drawFrame)
}

drawFrame()
```

### 对象池
```typescript
class Particle {
  x: number
  y: number
  vx: number
  vy: number
  life: number
  
  constructor() {
    this.reset()
  }
  
  reset() {
    this.x = Math.random() * canvas.width
    this.y = Math.random() * canvas.height
    this.vx = (Math.random() - 0.5) * 2
    this.vy = (Math.random() - 0.5) * 2
    this.life = 1
  }
  
  update() {
    this.x += this.vx
    this.y += this.vy
    this.life -= 0.01
    
    if (this.life <= 0) {
      this.reset()
    }
  }
  
  draw(ctx: CanvasRenderingContext2D) {
    ctx.globalAlpha = this.life
    ctx.beginPath()
    ctx.arc(this.x, this.y, 5, 0, Math.PI * 2)
    ctx.fillStyle = '#1890ff'
    ctx.fill()
    ctx.globalAlpha = 1
  }
}

// 对象池
const particles: Particle[] = []
const maxParticles = 100

for (let i = 0; i < maxParticles; i++) {
  particles.push(new Particle())
}

function animateParticles() {
  ctx.clearRect(0, 0, canvas.width, canvas.height)
  
  particles.forEach(particle => {
    particle.update()
    particle.draw(ctx)
  })
  
  requestAnimationFrame(animateParticles)
}

animateParticles()
```

## 工具函数

### 绘制工具
```typescript
// 绘制圆角矩形
function drawRoundedRect(
  ctx: CanvasRenderingContext2D,
  x: number,
  y: number,
  width: number,
  height: number,
  radius: number
) {
  ctx.beginPath()
  ctx.moveTo(x + radius, y)
  ctx.lineTo(x + width - radius, y)
  ctx.quadraticCurveTo(x + width, y, x + width, y + radius)
  ctx.lineTo(x + width, y + height - radius)
  ctx.quadraticCurveTo(x + width, y + height, x + width - radius, y + height)
  ctx.lineTo(x + radius, y + height)
  ctx.quadraticCurveTo(x, y + height, x, y + height - radius)
  ctx.lineTo(x, y + radius)
  ctx.quadraticCurveTo(x, y, x + radius, y)
  ctx.closePath()
}

// 绘制箭头
function drawArrow(
  ctx: CanvasRenderingContext2D,
  fromX: number,
  fromY: number,
  toX: number,
  toY: number
) {
  const headLength = 10
  const angle = Math.atan2(toY - fromY, toX - fromX)
  
  ctx.beginPath()
  ctx.moveTo(fromX, fromY)
  ctx.lineTo(toX, toY)
  ctx.lineTo(toX - headLength * Math.cos(angle - Math.PI / 6), toY - headLength * Math.sin(angle - Math.PI / 6))
  ctx.moveTo(toX, toY)
  ctx.lineTo(toX - headLength * Math.cos(angle + Math.PI / 6), toY - headLength * Math.sin(angle + Math.PI / 6))
  ctx.strokeStyle = '#333'
  ctx.lineWidth = 2
  ctx.stroke()
}
```

### 数学工具
```typescript
// 角度转换
function degreesToRadians(degrees: number): number {
  return degrees * (Math.PI / 180)
}

function radiansToDegrees(radians: number): number {
  return radians * (180 / Math.PI)
}

// 距离计算
function distance(x1: number, y1: number, x2: number, y2: number): number {
  return Math.sqrt((x2 - x1) ** 2 + (y2 - y1) ** 2)
}

// 角度计算
function angle(x1: number, y1: number, x2: number, y2: number): number {
  return Math.atan2(y2 - y1, x2 - x1)
}
```

## Vue3 集成

### Canvas 组件
```vue
<template>
  <canvas ref="canvasRef" :width="width" :height="height"></canvas>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'

interface Props {
  width?: number
  height?: number
}

const props = withDefaults(defineProps<Props>(), {
  width: 800,
  height: 600
})

const canvasRef = ref<HTMLCanvasElement>()
let ctx: CanvasRenderingContext2D | null = null
let animationId: number | null = null

onMounted(() => {
  const canvas = canvasRef.value
  if (!canvas) return
  
  ctx = canvas.getContext('2d')
  if (!ctx) return
  
  // 开始动画
  startAnimation()
})

onUnmounted(() => {
  if (animationId) {
    cancelAnimationFrame(animationId)
  }
})

function startAnimation() {
  if (!ctx) return
  
  function animate() {
    // 清除画布
    ctx.clearRect(0, 0, props.width, props.height)
    
    // 绘制内容
    drawContent(ctx)
    
    // 继续动画
    animationId = requestAnimationFrame(animate)
  }
  
  animate()
}

function drawContent(ctx: CanvasRenderingContext2D) {
  // 绘制逻辑
  ctx.fillStyle = '#1890ff'
  ctx.fillRect(10, 10, 100, 50)
}
</script>
```

### 响应式 Canvas
```vue
<template>
  <canvas ref="canvasRef"></canvas>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, onResize } from 'vue'

const canvasRef = ref<HTMLCanvasElement>()
let ctx: CanvasRenderingContext2D | null = null

const resizeCanvas = () => {
  const canvas = canvasRef.value
  if (!canvas) return
  
  const parent = canvas.parentElement
  if (!parent) return
  
  canvas.width = parent.clientWidth
  canvas.height = parent.clientHeight
  
  if (ctx) {
    redraw()
  }
}

onMounted(() => {
  const canvas = canvasRef.value
  if (!canvas) return
  
  ctx = canvas.getContext('2d')
  if (!ctx) return
  
  resizeCanvas()
  window.addEventListener('resize', resizeCanvas)
})

onUnmounted(() => {
  window.removeEventListener('resize', resizeCanvas)
})

function redraw() {
  if (!ctx) return
  
  ctx.clearRect(0, 0, canvasRef.value!.width, canvasRef.value!.height)
  // 重绘内容
}
</script>
```
