<template>
  <div class="user-page" @mousemove="handleMouseMove" @mouseleave="handleMouseLeave">
    <!-- 代码雨背景 -->
    <canvas ref="matrixCanvas" class="matrix-rain"></canvas>
    
    <!-- 随机曲线背景 -->
    <canvas ref="curvesCanvas" class="curves-background"></canvas>
    
    <!-- 背景装饰 -->
    <div class="background-decoration">
      <div class="glow-effect"></div>
    </div>
    
    <!-- 鼠标跟随效果 -->
    <div class="cursor-follower" :style="cursorStyle">{{ cursorText }}</div>
    <div class="cursor-trail" v-for="(trail, index) in cursorTrails" 
         :key="index" 
         :style="getTrailStyle(trail, index)">
      {{ trail.text }}
    </div>
    
    <!-- 页面扭曲效果 -->
    <div class="distortion-effect" :style="distortionStyle"></div>
    
    <div class="user-container">
      <!-- Logo 区域 -->
      <div class="logo-section">
        <Logo />
      </div>
      
      <!-- 功能组件区域 -->
      <div class="auth-section">
        <Login v-if="currentView === 'login'" @switch-to-signup="currentView = 'signup'" @hide-cursor="hideCustomCursor" @show-cursor="showCustomCursor" />
        <Signup v-if="currentView === 'signup'" @switch-to-login="currentView = 'login'" @hide-cursor="hideCustomCursor" @show-cursor="showCustomCursor" />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import Logo from '../components/user/logo.vue'
// The login component is provided by the Vue SFC compiler at runtime.
// @ts-ignore The component declaration may not expose its generated default export to TypeScript.
import Login from '../components/user/login.vue'
// The signup component is provided by the Vue SFC compiler at runtime.
// @ts-ignore The component declaration may not expose its generated default export to TypeScript.
import Signup from '../components/user/signup.vue'

// 当前视图状态
const currentView = ref<'login' | 'signup'>('login')

// 鼠标显示控制
const isOverInput = ref(false)

// 鼠标跟随效果
const cursorStyle = ref({
  left: '0px',
  top: '0px',
  opacity: '0'
})

const cursorTrails = ref<Array<{ x: number; y: number; opacity: number; text: string }>>([])
const maxTrails = 8
const cursorText = ref('</>')

// 扭曲效果
const distortionStyle = ref({
  left: '0px',
  top: '0px',
  opacity: '0'
})

const handleMouseMove = (e: MouseEvent) => {
  const target = e.target as HTMLElement
  const isInput = target.closest('.form-input') !== null
  const isLabel = target.closest('.form-label') !== null
  const isFormGroup = target.closest('.form-group') !== null
  
  // 检查是否在输入框或表单组上
  const shouldHide = isInput || isFormGroup
  
  if (shouldHide !== isOverInput.value) {
    isOverInput.value = shouldHide
  }
  
  // 根据是否在输入框/表单组上设置透明度
  const shouldShow = !shouldHide
  cursorStyle.value = {
    left: `${e.clientX}px`,
    top: `${e.clientY}px`,
    opacity: shouldShow ? '1' : '0'
  }
  
  // 更新扭曲效果位置
  distortionStyle.value = {
    left: `${e.clientX}px`,
    top: `${e.clientY}px`,
    opacity: shouldShow ? '1' : '0'
  }
  
  // 添加轨迹点，每个轨迹点显示不同的字符
  const chars = ['<', '/', '>']
  const randomChar = chars[Math.floor(Math.random() * chars.length)]
  
  cursorTrails.value.unshift({
    x: e.clientX,
    y: e.clientY,
    opacity: 1,
    text: randomChar
  })
  
  // 限制轨迹点数量
  if (cursorTrails.value.length > maxTrails) {
    cursorTrails.value.pop()
  }
}

const handleMouseLeave = () => {
  cursorStyle.value.opacity = '0'
  distortionStyle.value.opacity = '0'
}

// 表单组件在输入时隐藏/恢复自定义鼠标
const hideCustomCursor = () => {
  cursorStyle.value.opacity = '0'
  distortionStyle.value.opacity = '0'
}

const showCustomCursor = () => {
  if (!isOverInput.value) {
    cursorStyle.value.opacity = '1'
    distortionStyle.value.opacity = '1'
  }
}

const getTrailStyle = (trail: { x: number; y: number; opacity: number; text: string }, index: number) => {
  const opacity = (1 - index / maxTrails) * 0.4
  const fontSize = 12.6 - index * 1.35
  return {
    left: `${trail.x}px`,
    top: `${trail.y}px`,
    opacity: opacity.toString(),
    fontSize: `${fontSize}px`
  }
}

// 代码雨效果
const matrixCanvas = ref<HTMLCanvasElement | null>(null)
let animationFrameId: number | null = null
let matrixInterval: number | null = null

// 随机曲线效果
const curvesCanvas = ref<HTMLCanvasElement | null>(null)
let curvesAnimationId: number | null = null

// 维护两组曲线用于平滑过渡
const curveGroups = [
  [] as Array<{
    startX: number
    startY: number
    endX: number
    endY: number
    controlX1: number
    controlY1: number
    controlX2: number
    controlY2: number
    color: string
    width: number
    speed: number
    phase: number
  }>,
  [] as Array<{
    startX: number
    startY: number
    endX: number
    endY: number
    controlX1: number
    controlY1: number
    controlX2: number
    controlY2: number
    color: string
    width: number
    speed: number
    phase: number
  }>
]
let currentGroupIndex = 0
let transitionProgress = 0 // 0-1，用于过渡动画
let isTransitioning = false

const initMatrixRain = () => {
  const canvas = matrixCanvas.value
  if (!canvas) return
  
  const ctx = canvas.getContext('2d')
  if (!ctx) return
  
  // 设置画布大小
  const resizeCanvas = () => {
    canvas.width = window.innerWidth
    canvas.height = window.innerHeight
  }
  
  resizeCanvas()
  window.addEventListener('resize', resizeCanvas)
  
  // 代码雨配置
  const fontSize = 14
  const columns = canvas.width / fontSize
  const drops: number[] = []
  
  // 初始化每列的y坐标
  for (let i = 0; i < columns; i++) {
    drops[i] = Math.random() * canvas.height // 随机起始位置
  }
  
  // 代码字符集（包含编程相关的字符）
  const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789@#$%^&*()_+-=[]{}|;:,.<>?'
  
  const drawMatrix = () => {
    // 半透明黑色背景，产生拖尾效果
    ctx.fillStyle = 'rgba(10, 22, 40, 0.05)'
    ctx.fillRect(0, 0, canvas.width, canvas.height)
    
    // 设置文字样式
    ctx.fillStyle = '#0f0' // 绿色文字
    ctx.font = `${fontSize}px monospace`
    
    // 绘制字符
    for (let i = 0; i < drops.length; i++) {
      const text = chars[Math.floor(Math.random() * chars.length)]
      const x = i * fontSize
      const y = drops[i] * fontSize
      
      // 随机亮度，模拟闪烁效果
      const brightness = Math.random()
      if (brightness > 0.95) {
        ctx.fillStyle = '#fff' // 偶尔亮白色
      } else if (brightness > 0.8) {
        ctx.fillStyle = '#0f0' // 亮绿色
      } else {
        ctx.fillStyle = '#0a0' // 暗绿色
      }
      
      ctx.fillText(text, x, y)
      
      // 超过底部或随机重置
      if (y > canvas.height && Math.random() > 0.975) {
        drops[i] = 0
      }
      
      drops[i]++
    }
  }
  
  // 启动动画
  matrixInterval = window.setInterval(drawMatrix, 50)
}

// 随机曲线效果
const initCurves = () => {
  const canvas = curvesCanvas.value
  if (!canvas) return
  
  const ctx = canvas.getContext('2d')
  if (!ctx) return
  
  // 设置画布大小
  const resizeCanvas = () => {
    canvas.width = window.innerWidth
    canvas.height = window.innerHeight
  }
  
  resizeCanvas()
  window.addEventListener('resize', resizeCanvas)
  
  // 生成单组随机曲线
  const generateCurveGroup = () => {
    const curves: Array<{
      startX: number
      startY: number
      endX: number
      endY: number
      controlX1: number
      controlY1: number
      controlX2: number
      controlY2: number
      color: string
      width: number
      speed: number
      phase: number
    }> = []
    
    const curveCount = 15
    
    for (let i = 0; i < curveCount; i++) {
      const startX = Math.random() * canvas.width
      const startY = Math.random() * canvas.height
      const endX = Math.random() * canvas.width
      const endY = Math.random() * canvas.height
      
      // 控制点产生贝塞尔曲线
      const controlX1 = startX + (Math.random() - 0.5) * canvas.width * 0.5
      const controlY1 = startY + (Math.random() - 0.5) * canvas.height * 0.5
      const controlX2 = endX + (Math.random() - 0.5) * canvas.width * 0.5
      const controlY2 = endY + (Math.random() - 0.5) * canvas.height * 0.5
      
      // 蓝色系随机颜色
      const colors = [
        'rgba(59, 130, 246, 0.3)',   // 蓝色
        'rgba(6, 182, 212, 0.25)',   // 青色
        'rgba(139, 92, 246, 0.2)',  // 紫色
        'rgba(99, 102, 241, 0.25)',  // 靛蓝
        'rgba(14, 165, 233, 0.2)'    // 天蓝
      ]
      const color = colors[Math.floor(Math.random() * colors.length)]
      
      curves.push({
        startX,
        startY,
        endX,
        endY,
        controlX1,
        controlY1,
        controlX2,
        controlY2,
        color,
        width: Math.random() * 2 + 0.5,
        speed: Math.random() * 0.002 + 0.001,
        phase: Math.random() * Math.PI * 2
      })
    }
    
    return curves
  }
  
  // 初始化第一组曲线
  curveGroups[0] = generateCurveGroup()
  curveGroups[1] = generateCurveGroup()
  
  // 平滑过渡插值函数
  const lerp = (start: number, end: number, t: number) => {
    return start + (end - start) * t
  }
  
  // 缓动函数
  const easeInOutCubic = (t: number) => {
    return t < 0.5 ? 4 * t * t * t : 1 - Math.pow(-2 * t + 2, 3) / 2
  }
  
  // 绘制过渡曲线
  const drawTransitionCurve = (oldCurve: any, newCurve: any, progress: number, time: number) => {
    const easedProgress = easeInOutCubic(progress)
    
    // 插值计算中间状态
    const startX = lerp(oldCurve.startX, newCurve.startX, easedProgress)
    const startY = lerp(oldCurve.startY, newCurve.startY, easedProgress)
    const endX = lerp(oldCurve.endX, newCurve.endX, easedProgress)
    const endY = lerp(oldCurve.endY, newCurve.endY, easedProgress)
    const controlX1 = lerp(oldCurve.controlX1, newCurve.controlX1, easedProgress)
    const controlY1 = lerp(oldCurve.controlY1, newCurve.controlY1, easedProgress)
    const controlX2 = lerp(oldCurve.controlX2, newCurve.controlX2, easedProgress)
    const controlY2 = lerp(oldCurve.controlY2, newCurve.controlY2, easedProgress)
    const width = lerp(oldCurve.width, newCurve.width, easedProgress)
    
    // 混合颜色
    const oldAlpha = parseFloat(oldCurve.color.match(/[\d.]+(?=\))/)?.[0] || '0.3')
    const newAlpha = parseFloat(newCurve.color.match(/[\d.]+(?=\))/)?.[0] || '0.3')
    const blendedAlpha = lerp(oldAlpha, newAlpha, easedProgress)
    const color = oldCurve.color.replace(/[\d.]+(?=\))/, blendedAlpha.toFixed(3))
    
    ctx.beginPath()
    ctx.moveTo(startX, startY)
    
    // 动态控制点，产生流动效果
    const offsetX = Math.sin(time * newCurve.speed + newCurve.phase) * 50
    const offsetY = Math.cos(time * newCurve.speed + newCurve.phase) * 50
    
    ctx.bezierCurveTo(
      controlX1 + offsetX,
      controlY1 + offsetY,
      controlX2 - offsetX,
      controlY2 - offsetY,
      endX,
      endY
    )
    
    ctx.strokeStyle = color
    ctx.lineWidth = width
    ctx.lineCap = 'round'
    ctx.stroke()
  }
  
  // 绘制普通曲线
  const drawCurve = (curve: any, time: number) => {
    ctx.beginPath()
    ctx.moveTo(curve.startX, curve.startY)
    
    // 动态控制点，产生流动效果
    const offsetX = Math.sin(time * curve.speed + curve.phase) * 50
    const offsetY = Math.cos(time * curve.speed + curve.phase) * 50
    
    ctx.bezierCurveTo(
      curve.controlX1 + offsetX,
      curve.controlY1 + offsetY,
      curve.controlX2 - offsetX,
      curve.controlY2 - offsetY,
      curve.endX,
      curve.endY
    )
    
    ctx.strokeStyle = curve.color
    ctx.lineWidth = curve.width
    ctx.lineCap = 'round'
    ctx.stroke()
  }
  
  // 绘制曲线
  const drawCurves = (time: number) => {
    ctx.clearRect(0, 0, canvas.width, canvas.height)
    
    const currentGroup = curveGroups[currentGroupIndex]
    const nextGroupIndex = (currentGroupIndex + 1) % 2
    const nextGroup = curveGroups[nextGroupIndex]
    
    if (isTransitioning) {
      // 过渡动画
      currentGroup.forEach((oldCurve, index) => {
        const newCurve = nextGroup[index] || nextGroup[0]
        drawTransitionCurve(oldCurve, newCurve, transitionProgress, time)
      })
      
      // 更新过渡进度
      transitionProgress += 0.02
      if (transitionProgress >= 1) {
        transitionProgress = 0
        isTransitioning = false
        currentGroupIndex = nextGroupIndex
      }
    } else {
      // 正常绘制
      currentGroup.forEach((curve) => {
        drawCurve(curve, time)
      })
    }
    
    curvesAnimationId = requestAnimationFrame(drawCurves)
  }
  
  // 启动动画
  curvesAnimationId = requestAnimationFrame((time) => drawCurves(time))
  
  // 定期触发过渡
  setInterval(() => {
    if (!isTransitioning) {
      const nextGroupIndex = (currentGroupIndex + 1) % 2
      curveGroups[nextGroupIndex] = generateCurveGroup()
      isTransitioning = true
      transitionProgress = 0
    }
  }, 10000)
}

onMounted(() => {
  initMatrixRain()
  initCurves()
  
  // 更新轨迹透明度
  const updateTrails = () => {
    cursorTrails.value = cursorTrails.value.map(trail => ({
      ...trail,
      opacity: trail.opacity * 0.95
    })).filter(trail => trail.opacity > 0.01)
    
    animationFrameId = requestAnimationFrame(updateTrails)
  }
  
  animationFrameId = requestAnimationFrame(updateTrails)
})

onUnmounted(() => {
  if (matrixInterval) {
    clearInterval(matrixInterval)
  }
  if (curvesAnimationId) {
    cancelAnimationFrame(curvesAnimationId)
  }
  if (animationFrameId) {
    cancelAnimationFrame(animationFrameId)
  }
  window.removeEventListener('resize', () => {})
})
</script>

<style scoped>
.user-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #0a1628 0%, #1a2a4a 50%, #0d1b2a 100%);
  padding: 20px;
  position: relative;
  overflow: hidden;
  cursor: none; /* 隐藏默认鼠标 */
}

.matrix-rain {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 0;
  pointer-events: none;
}

.curves-background {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1;
  pointer-events: none;
}

.background-decoration {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  pointer-events: none;
  z-index: 2;
}

.glow-effect {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 600px;
  height: 600px;
  background: radial-gradient(circle, rgba(59, 130, 246, 0.1) 0%, transparent 70%);
  animation: pulse 4s ease-in-out infinite;
}

@keyframes pulse {
  0%, 100% {
    opacity: 0.5;
    transform: translate(-50%, -50%) scale(1);
  }
  50% {
    opacity: 0.8;
    transform: translate(-50%, -50%) scale(1.1);
  }
}

.cursor-follower {
  position: fixed;
  width: 36px;
  height: 18px;
  pointer-events: none;
  z-index: 9998;
  transform: translate(-50%, -50%);
  transition: opacity 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  font-family: 'Courier New', monospace;
  font-size: 14.4px;
  font-weight: bold;
  color: #3b82f6;
  text-shadow: 0 0 9px rgba(59, 130, 246, 0.8), 0 0 18px rgba(59, 130, 246, 0.5);
  white-space: nowrap;
}

.distortion-effect {
  position: fixed;
  width: 200px;
  height: 200px;
  pointer-events: none;
  z-index: 9996;
  transform: translate(-50%, -50%);
  transition: opacity 0.3s ease;
  border-radius: 50%;
  background: radial-gradient(circle, 
    transparent 0%, 
    rgba(59, 130, 246, 0.05) 20%, 
    rgba(6, 182, 212, 0.1) 40%, 
    rgba(139, 92, 246, 0.05) 60%, 
    transparent 80%
  );
  filter: blur(4px) contrast(1.2) brightness(1.1);
  animation: distortionPulse 3s ease-in-out infinite;
  mix-blend-mode: overlay;
}

@keyframes distortionPulse {
  0%, 100% {
    transform: translate(-50%, -50%) scale(1) rotate(0deg);
    opacity: 0.7;
  }
  25% {
    transform: translate(-50%, -50%) scale(1.05) rotate(5deg);
    opacity: 0.8;
  }
  50% {
    transform: translate(-50%, -50%) scale(1.1) rotate(0deg);
    opacity: 0.9;
  }
  75% {
    transform: translate(-50%, -50%) scale(1.05) rotate(-5deg);
    opacity: 0.8;
  }
}

.cursor-trail {
  position: fixed;
  pointer-events: none;
  z-index: 9997;
  transform: translate(-50%, -50%);
  transition: opacity 0.1s ease;
  font-family: 'Courier New', monospace;
  font-size: 10.8px;
  font-weight: bold;
  color: rgba(59, 130, 246, 0.3);
  white-space: nowrap;
}

.user-container {
  width: 100%;
  max-width: 1200px;
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 80px;
  align-items: center;
  position: relative;
  z-index: 3;
}

.logo-section {
  display: flex;
  justify-content: center;
  align-items: center;
}

.auth-section {
  display: flex;
  justify-content: center;
  align-items: center;
}

@media (max-width: 768px) {
  .user-container {
    grid-template-columns: 1fr;
    gap: 40px;
  }
  
  .logo-section {
    order: -1;
  }
  
  .glow-effect {
    width: 400px;
    height: 400px;
  }
  
  .user-page {
    cursor: auto; /* 移动端恢复默认鼠标 */
  }
  
  .cursor-follower,
  .cursor-trail,
  .distortion-effect {
    display: none; /* 移动端隐藏自定义鼠标 */
  }
}
</style>