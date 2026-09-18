<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'

// 检测是否为移动设备
const isMobile = ref(false)
const checkMobile = () => {
  isMobile.value = window.innerWidth <= 768 || 'ontouchstart' in window
}

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
  const isFormGroup = target.closest('.form-group') !== null
  
  // 检查是否在输入框或表单组上
  const shouldHide = isInput || isFormGroup
  
  if (shouldHide !== isOverInput.value) {
    isOverInput.value = shouldHide
  }
  
  // 根据是否在输入框/表单组上设置透明度
  const shouldShow = !shouldHide && !isMobile.value
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
  if (!isOverInput.value && !isMobile.value) {
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

let animationFrameId: number | null = null

// 背景动画代码
const matrixCanvas = ref<HTMLCanvasElement | null>(null)
let matrixInterval: number | null = null

const curvesCanvas = ref<HTMLCanvasElement | null>(null)
let curvesAnimationId: number | null = null

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
let transitionProgress = 0
let isTransitioning = false

const initMatrixRain = () => {
  const canvas = matrixCanvas.value
  if (!canvas) return
  
  const ctx = canvas.getContext('2d')
  if (!ctx) return
  
  const resizeCanvas = () => {
    canvas.width = window.innerWidth
    canvas.height = window.innerHeight
  }
  
  resizeCanvas()
  window.addEventListener('resize', resizeCanvas)
  
  const fontSize = 14
  const columns = canvas.width / fontSize
  const drops: number[] = []
  
  for (let i = 0; i < columns; i++) {
    drops[i] = Math.random() * canvas.height
  }
  
  const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789@#$%^&*()_+-=[]{}|;:,.<>?'
  
  const drawMatrix = () => {
    ctx.fillStyle = 'rgba(10, 22, 40, 0.05)'
    ctx.fillRect(0, 0, canvas.width, canvas.height)
    
    ctx.fillStyle = '#0f0'
    ctx.font = `${fontSize}px monospace`
    
    for (let i = 0; i < drops.length; i++) {
      const text = chars[Math.floor(Math.random() * chars.length)]
      const x = i * fontSize
      const y = drops[i] * fontSize
      
      const brightness = Math.random()
      if (brightness > 0.95) {
        ctx.fillStyle = '#fff'
      } else if (brightness > 0.8) {
        ctx.fillStyle = '#0f0'
      } else {
        ctx.fillStyle = '#0a0'
      }
      
      ctx.fillText(text, x, y)
      
      if (y > canvas.height && Math.random() > 0.975) {
        drops[i] = 0
      }
      
      drops[i]++
    }
  }
  
  matrixInterval = window.setInterval(drawMatrix, 50)
}

const initCurves = () => {
  const canvas = curvesCanvas.value
  if (!canvas) return
  
  const ctx = canvas.getContext('2d')
  if (!ctx) return
  
  const resizeCanvas = () => {
    canvas.width = window.innerWidth
    canvas.height = window.innerHeight
  }
  
  resizeCanvas()
  window.addEventListener('resize', resizeCanvas)
  
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
      
      const controlX1 = startX + (Math.random() - 0.5) * canvas.width * 0.5
      const controlY1 = startY + (Math.random() - 0.5) * canvas.height * 0.5
      const controlX2 = endX + (Math.random() - 0.5) * canvas.width * 0.5
      const controlY2 = endY + (Math.random() - 0.5) * canvas.height * 0.5
      
      const colors = [
        'rgba(59, 130, 246, 0.3)',
        'rgba(6, 182, 212, 0.25)',
        'rgba(139, 92, 246, 0.2)',
        'rgba(99, 102, 241, 0.25)',
        'rgba(14, 165, 233, 0.2)'
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
  
  curveGroups[0] = generateCurveGroup()
  curveGroups[1] = generateCurveGroup()
  
  const lerp = (start: number, end: number, t: number) => {
    return start + (end - start) * t
  }
  
  const easeInOutCubic = (t: number) => {
    return t < 0.5 ? 4 * t * t * t : 1 - Math.pow(-2 * t + 2, 3) / 2
  }
  
  const drawTransitionCurve = (oldCurve: any, newCurve: any, progress: number, time: number) => {
    const easedProgress = easeInOutCubic(progress)
    
    const startX = lerp(oldCurve.startX, newCurve.startX, easedProgress)
    const startY = lerp(oldCurve.startY, newCurve.startY, easedProgress)
    const endX = lerp(oldCurve.endX, newCurve.endX, easedProgress)
    const endY = lerp(oldCurve.endY, newCurve.endY, easedProgress)
    const controlX1 = lerp(oldCurve.controlX1, newCurve.controlX1, easedProgress)
    const controlY1 = lerp(oldCurve.controlY1, newCurve.controlY1, easedProgress)
    const controlX2 = lerp(oldCurve.controlX2, newCurve.controlX2, easedProgress)
    const controlY2 = lerp(oldCurve.controlY2, newCurve.controlY2, easedProgress)
    const width = lerp(oldCurve.width, newCurve.width, easedProgress)
    
    const oldAlpha = parseFloat(oldCurve.color.match(/[\d.]+(?=\))/)?.[0] || '0.3')
    const newAlpha = parseFloat(newCurve.color.match(/[\d.]+(?=\))/)?.[0] || '0.3')
    const blendedAlpha = lerp(oldAlpha, newAlpha, easedProgress)
    const color = oldCurve.color.replace(/[\d.]+(?=\))/, blendedAlpha.toFixed(3))
    
    ctx.beginPath()
    ctx.moveTo(startX, startY)
    
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
  
  const drawCurve = (curve: any, time: number) => {
    ctx.beginPath()
    ctx.moveTo(curve.startX, curve.startY)
    
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
  
  const drawCurves = (time: number) => {
    ctx.clearRect(0, 0, canvas.width, canvas.height)
    
    const currentGroup = curveGroups[currentGroupIndex]
    const nextGroupIndex = (currentGroupIndex + 1) % 2
    const nextGroup = curveGroups[nextGroupIndex]
    
    if (isTransitioning) {
      currentGroup.forEach((oldCurve, index) => {
        const newCurve = nextGroup[index] || nextGroup[0]
        drawTransitionCurve(oldCurve, newCurve, transitionProgress, time)
      })
      
      transitionProgress += 0.02
      if (transitionProgress >= 1) {
        transitionProgress = 0
        isTransitioning = false
        currentGroupIndex = nextGroupIndex
      }
    } else {
      currentGroup.forEach((curve) => {
        drawCurve(curve, time)
      })
    }
    
    curvesAnimationId = requestAnimationFrame(drawCurves)
  }
  
  curvesAnimationId = requestAnimationFrame((time) => drawCurves(time))
  
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
  checkMobile()
  window.addEventListener('resize', checkMobile)
  document.addEventListener('mousemove', handleMouseMove)
  document.addEventListener('mouseleave', handleMouseLeave)
  
  // 更新轨迹透明度
  const updateTrails = () => {
    cursorTrails.value = cursorTrails.value.map(trail => ({
      ...trail,
      opacity: trail.opacity * 0.95
    })).filter(trail => trail.opacity > 0.01)
    
    animationFrameId = requestAnimationFrame(updateTrails)
  }
  
  animationFrameId = requestAnimationFrame(updateTrails)
  
  // 初始化背景动画
  initMatrixRain()
  initCurves()
})

onUnmounted(() => {
  window.removeEventListener('resize', checkMobile)
  document.removeEventListener('mousemove', handleMouseMove)
  document.removeEventListener('mouseleave', handleMouseLeave)
  
  if (animationFrameId) {
    cancelAnimationFrame(animationFrameId)
  }
  
  if (matrixInterval) {
    clearInterval(matrixInterval)
  }
  if (curvesAnimationId) {
    cancelAnimationFrame(curvesAnimationId)
  }
})

// 暴露方法给子组件调用
defineExpose({
  hideCustomCursor,
  showCustomCursor
})
</script>

<template>
  <div class="app-container" :class="{ 'hide-default-cursor': !isMobile }">
    <!-- 背景动画 -->
    <canvas ref="matrixCanvas" class="matrix-rain"></canvas>
    <canvas ref="curvesCanvas" class="curves-background"></canvas>
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
    
    <router-view></router-view>
  </div>
</template>

<style>
.app-container {
  min-height: 100vh;
  position: relative;
  background: linear-gradient(135deg, #0a1628 0%, #1a2a4a 50%, #0d1b2a 100%);
  margin: 0;
  padding: 0;
}

.app-container.hide-default-cursor {
  cursor: none;
}

@media (max-width: 768px) {
  .app-container.hide-default-cursor {
    cursor: auto;
  }
  
  .cursor-follower,
  .cursor-trail,
  .distortion-effect {
    display: none !important;
  }
}

/* 背景动画样式 */
.matrix-rain {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 0;
  pointer-events: none;
}

.curves-background {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1;
  pointer-events: none;
}

.background-decoration {
  position: fixed;
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

@media (max-width: 768px) {
  .glow-effect {
    width: 400px;
    height: 400px;
  }
}
</style>
