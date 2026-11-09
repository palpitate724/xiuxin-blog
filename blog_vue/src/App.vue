<script setup lang="ts">
import { ref, onMounted, onUnmounted, watch } from 'vue'
import { useTheme } from './composables/useTheme'

const { isDark } = useTheme()

// 检测是否为移动设备
const isMobile = ref(false)
const checkMobile = () => {
  isMobile.value = window.innerWidth <= 768 || 'ontouchstart' in window
}

// 回到顶部功能
const showBackTop = ref(false)
const scrollProgress = ref(0)

function handleScroll() {
  showBackTop.value = window.scrollY > 300
  const scrollTop = window.scrollY
  const docHeight = document.documentElement.scrollHeight - window.innerHeight
  scrollProgress.value = docHeight > 0 ? (scrollTop / docHeight) * 100 : 0
}

function scrollToTop() {
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

// 鼠标跟随效果
const cursorStyle = ref({ left: '0px', top: '0px', opacity: '0' })
const cursorTrails = ref<Array<{ x: number; y: number; opacity: number; text: string }>>([])
const maxTrails = 6
const cursorText = ref('◈')

const handleMouseMove = (e: MouseEvent) => {
  const target = e.target as HTMLElement
  const shouldHide = target.closest('.form-input') !== null ||
                     target.closest('.form-group') !== null ||
                     target.closest('.pagination') !== null
  if (shouldHide !== isOverInput.value) isOverInput.value = shouldHide

  const shouldShow = !shouldHide && !isMobile.value
  cursorStyle.value = {
    left: `${e.clientX}px`,
    top: `${e.clientY}px`,
    opacity: shouldShow ? '1' : '0'
  }

  cursorTrails.value.unshift({
    x: e.clientX,
    y: e.clientY,
    opacity: 1,
    text: cursorText.value
  })
  if (cursorTrails.value.length > maxTrails) {
    cursorTrails.value.pop()
  }
}

const handleMouseLeave = () => {
  cursorStyle.value.opacity = '0'
}

const isOverInput = ref(false)

const getTrailStyle = (trail: { x: number; y: number; opacity: number; text: string }, index: number) => {
  const fade = 1 - (index / maxTrails)
  const alpha = fade * 0.4
  return {
    left: `${trail.x}px`,
    top: `${trail.y}px`,
    opacity: (trail.opacity * fade * 0.5).toString(),
    fontSize: `${10 + fade * 4}px`,
    color: `rgba(150, 220, 255, ${alpha})`
  }
}

// 背景动画系统 - 优化版
let animationFrameId: number | null = null
let starsCanvas: HTMLCanvasElement | null = null
let starsCtx: CanvasRenderingContext2D | null = null
let auroraCanvas: HTMLCanvasElement | null = null
let auroraCtx: CanvasRenderingContext2D | null = null
let auroraAnimationId: number | null = null

// DPR 适配
const setupCanvas = (canvas: HTMLCanvasElement): CanvasRenderingContext2D | null => {
  const dpr = window.devicePixelRatio || 1
  const rect = canvas.getBoundingClientRect()

  canvas.width = rect.width * dpr
  canvas.height = rect.height * dpr

  const ctx = canvas.getContext('2d')
  if (!ctx) return null

  ctx.scale(dpr, dpr)
  canvas.style.width = `${rect.width}px`
  canvas.style.height = `${rect.height}px`

  return ctx
}

// 鼠标位置
const mousePos = ref({ x: 0, y: 0 })
let mouseX = 0, mouseY = 0

// Star type definition
interface Star {
  x: number; y: number; z: number;
  baseSize: number; brightness: number;
  twinkleSpeed: number; twinklePhase: number;
}

// Aurora Orb type definition
interface AuroraOrb {
  x: number; y: number; radius: number;
  color: string; opacity: number;
  vx: number; vy: number;
  phase: number; speed: number;
}

let stars: Star[] = []
let auroraOrbs: AuroraOrb[] = []
let time = 0

const initStars = () => {
  starsCanvas = document.getElementById('stars-canvas') as HTMLCanvasElement
  if (!starsCanvas) return
  starsCtx = starsCanvas.getContext('2d')
  if (!starsCtx) return

  const resize = () => {
    starsCanvas!.width = window.innerWidth
    starsCanvas!.height = window.innerHeight
  }
  resize()
  window.addEventListener('resize', resize)

  const starCount = Math.floor((window.innerWidth * window.innerHeight) / 4000)
  stars = []
  for (let i = 0; i < starCount; i++) {
    stars.push({
      x: Math.random() * starsCanvas!.width,
      y: Math.random() * starsCanvas!.height,
      z: Math.random() * 1.5 + 0.5,
      baseSize: Math.random() * 1.5 + 0.3,
      brightness: Math.random() * 0.7 + 0.3,
      twinkleSpeed: Math.random() * 0.02 + 0.005,
      twinklePhase: Math.random() * Math.PI * 2
    })
  }
}

const drawStars = () => {
  if (!starsCtx || !starsCanvas) return
  const { width, height } = starsCanvas
  starsCtx.clearRect(0, 0, width, height)

  const starColor = '200, 230, 255'

  stars.forEach(star => {
    const twinkle = Math.sin(time * star.twinkleSpeed + star.twinklePhase) * 0.3 + 0.7
    const alpha = star.brightness * twinkle

    starsCtx!.beginPath()
    starsCtx!.arc(star.x, star.y, star.baseSize * star.z, 0, Math.PI * 2)
    starsCtx!.fillStyle = `rgba(${starColor}, ${alpha})`
    starsCtx!.fill()

    if (star.brightness > 0.7) {
      const gradient = starsCtx!.createRadialGradient(
        star.x, star.y, 0, star.x, star.y, star.baseSize * 3
      )
      gradient.addColorStop(0, `rgba(${starColor}, ${alpha * 0.3})`)
      gradient.addColorStop(1, `rgba(${starColor}, 0)`)
      starsCtx!.beginPath()
      starsCtx!.arc(star.x, star.y, star.baseSize * 3, 0, Math.PI * 2)
      starsCtx!.fillStyle = gradient
      starsCtx!.fill()
    }
  })
}

const initAurora = () => {
  auroraCanvas = document.getElementById('aurora-canvas') as HTMLCanvasElement
  if (!auroraCanvas) return
  auroraCtx = auroraCanvas.getContext('2d')
  if (!auroraCtx) return

  const resize = () => {
    auroraCanvas!.width = window.innerWidth
    auroraCanvas!.height = window.innerHeight
  }
  resize()
  window.addEventListener('resize', resize)

  const lightColors = [
    'rgba(59, 130, 246, 0.04)',
    'rgba(6, 182, 212, 0.03)',
    'rgba(139, 92, 246, 0.025)',
    'rgba(99, 102, 241, 0.02)'
  ]
  const darkColors = [
    'rgba(59, 130, 246, 0.08)',
    'rgba(6, 182, 212, 0.06)',
    'rgba(139, 92, 246, 0.05)',
    'rgba(99, 102, 241, 0.04)'
  ]
  const colors = darkColors

  auroraOrbs = []
  for (let i = 0; i < 6; i++) {
    auroraOrbs.push({
      x: Math.random() * window.innerWidth,
      y: Math.random() * window.innerHeight,
      radius: Math.random() * 300 + 200,
      color: colors[i % colors.length],
      opacity: Math.random() * 0.5 + 0.5,
      vx: (Math.random() - 0.5) * 0.3,
      vy: (Math.random() - 0.5) * 0.2,
      phase: Math.random() * Math.PI * 2,
      speed: Math.random() * 0.0003 + 0.0001
    })
  }
}

const drawAurora = () => {
  if (!auroraCtx || !auroraCanvas) return
  const { width, height } = auroraCanvas
  auroraCtx.clearRect(0, 0, width, height)

  time += 16
  mousePos.value.x = mouseX
  mousePos.value.y = mouseY

  auroraOrbs.forEach((orb, i) => {
    orb.x += orb.vx + Math.sin(time * orb.speed + orb.phase) * 0.5
    orb.y += orb.vy + Math.cos(time * orb.speed * 0.7 + orb.phase) * 0.3

    const dx = (mouseX - width / 2) * 0.01 * (i % 2 === 0 ? 1 : -1)
    const dy = (mouseY - height / 2) * 0.01
    orb.x += dx * 0.02
    orb.y += dy * 0.02

    if (orb.x < -orb.radius) orb.x = width + orb.radius
    if (orb.x > width + orb.radius) orb.x = -orb.radius
    if (orb.y < -orb.radius) orb.y = height + orb.radius
    if (orb.y > height + orb.radius) orb.y = -orb.radius

    const gradient = auroraCtx!.createRadialGradient(
      orb.x, orb.y, 0, orb.x, orb.y, orb.radius
    )
    gradient.addColorStop(0, orb.color.replace(/[\d.]+\)$/, `${orb.opacity * 0.8})`))
    gradient.addColorStop(0.5, orb.color.replace(/[\d.]+\)$/, `${orb.opacity * 0.3})`))
    gradient.addColorStop(1, 'rgba(0, 0, 0, 0)')

    auroraCtx!.beginPath()
    auroraCtx!.arc(orb.x, orb.y, orb.radius, 0, Math.PI * 2)
    auroraCtx!.fillStyle = gradient
    auroraCtx!.fill()
  })
}


onMounted(() => {
  checkMobile()
  window.addEventListener('resize', checkMobile)
  document.addEventListener('mousemove', handleMouseMove)
  document.addEventListener('mouseleave', handleMouseLeave)
  window.addEventListener('scroll', handleScroll, { passive: true })

  document.addEventListener('mousemove', (e) => {
    mouseX = e.clientX
    mouseY = e.clientY
  })

  const updateTrails = () => {
    cursorTrails.value = cursorTrails.value.map(trail => ({
      ...trail,
      opacity: trail.opacity * 0.92
    })).filter(trail => trail.opacity > 0.02)
    animationFrameId = requestAnimationFrame(updateTrails)
  }
  animationFrameId = requestAnimationFrame(updateTrails)

  initStars()
  initAurora()

  const animate = () => {
    drawStars()
    drawAurora()
    auroraAnimationId = requestAnimationFrame(animate)
  }
  animate()
})

onUnmounted(() => {
  window.removeEventListener('resize', checkMobile)
  document.removeEventListener('mousemove', handleMouseMove)
  document.removeEventListener('mouseleave', handleMouseLeave)
  window.removeEventListener('scroll', handleScroll)

  if (animationFrameId) cancelAnimationFrame(animationFrameId)
  if (auroraAnimationId) cancelAnimationFrame(auroraAnimationId)
})

defineExpose({
  hideCustomCursor: () => { cursorStyle.value.opacity = '0' },
  showCustomCursor: () => {
    if (!isOverInput.value && !isMobile.value) cursorStyle.value.opacity = '1'
  }
})
</script>

<template>
  <div class="app-container" :class="{ 'hide-default-cursor': !isMobile }">
    <!-- 顶部滚动进度条 -->
    <div class="scroll-progress" :style="{ transform: `scaleX(${scrollProgress / 100})` }"></div>

    <!-- 背景层 -->
    <canvas id="stars-canvas" class="stars-canvas"></canvas>
    <canvas id="aurora-canvas" class="aurora-canvas"></canvas>

    <!-- 鼠标跟随环境光 -->
    <div class="ambient-glow" :style="{
      left: `${mousePos.x}px`,
      top: `${mousePos.y}px`,
      opacity: isMobile ? '0' : '0.4',
      background: 'radial-gradient(circle, rgba(59, 130, 246, 0.12) 0%, transparent 70%)'
    }"></div>

    <!-- 噪点纹理 -->
    <div class="noise-overlay"></div>

    <!-- 鼠标跟随效果 -->
    <div class="cursor-follower" :style="cursorStyle">{{ cursorText }}</div>
    <div class="cursor-trail" v-for="(trail, index) in cursorTrails"
         :key="index"
         :style="getTrailStyle(trail, index)">
      {{ trail.text }}
    </div>

    <!-- 回到顶部按钮 -->
    <transition name="fade">
      <button v-show="showBackTop" class="back-top" @click="scrollToTop" aria-label="回到顶部">↑</button>
    </transition>

    <!-- 页面转场动画 -->
    <transition name="page-transition" mode="out-in">
      <router-view></router-view>
    </transition>
  </div>
</template>

<style>
/* ===== 全局主题过渡 ===== */
.app-container {
  min-height: 100vh;
  position: relative;
  margin: 0;
  padding: 0;
  overflow-x: hidden;
  /* 背景色由 tokens 控制，支持主题过渡 */
  background: var(--color-bg-deep);
}

/* 动态背景渐变 - 暗色/亮色 */
.app-container::before {
  content: '';
  position: fixed;
  inset: 0;
  background: linear-gradient(
    160deg,
    var(--color-bg-deepest) 0%,
    var(--color-bg-deep) 40%,
    var(--color-bg-mid) 70%,
    var(--color-bg-deep) 100%
  );
  z-index: -1;
  transition: background var(--duration-theme) ease;
}

.app-container.hide-default-cursor {
  cursor: none;
}

/* 星空 Canvas */
.stars-canvas {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 0;
  pointer-events: none;
}

/* 亮色主题下隐藏星空 */

/* 极光 Canvas */
.aurora-canvas {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1;
  pointer-events: none;
  mix-blend-mode: screen;
}

/* 亮色主题下隐藏极光 */

/* 鼠标跟随环境光 */
.ambient-glow {
  position: fixed;
  width: 500px;
  height: 500px;
  border-radius: 50%;
  transform: translate(-50%, -50%);
  pointer-events: none;
  z-index: 2;
  transition: left 0.3s ease-out, top 0.3s ease-out;
  mix-blend-mode: screen;
}

/* 亮色主题下隐藏环境光 */

/* 噪点纹理 */
.noise-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 3;
  opacity: 0.03;
  background-image: url("data:image/svg+xml,%3Csvg viewBox='0 0 200 200' xmlns='http://www.w3.org/2000/svg'%3E%3Cfilter id='noiseFilter'%3E%3CfeTurbulence type='fractalNoise' baseFrequency='0.9' numOctaves='4' stitchTiles='stitch'/%3E%3C/filter%3E%3Crect width='100%25' height='100%25' filter='url(%23noiseFilter)'/%3E%3C/svg%3E");
  transition: opacity var(--duration-theme) ease;
}


/* ===== 顶部滚动进度条 ===== */
.scroll-progress {
  position: fixed;
  top: 0;
  left: 0;
  height: 3px;
  width: 100%;
  transform-origin: left;
  transform: scaleX(0);
  background: linear-gradient(90deg, var(--color-progress-start) 0%, var(--color-accent-secondary) 50%, var(--color-progress-end) 100%);
  z-index: 9999;
  transition: transform 0.1s linear;
  box-shadow: 0 0 15px var(--color-progress-start-soft), 0 0 30px var(--color-accent-glow-soft);
}

/* ===== 自定义鼠标 ===== */
.cursor-follower {
  position: fixed;
  width: 32px;
  height: 32px;
  pointer-events: none;
  z-index: 9998;
  transform: translate(-50%, -50%);
  transition: opacity 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  font-family: 'Courier New', monospace;
  font-size: 14px;
  font-weight: bold;
  color: var(--color-accent-laser);
  text-shadow: 0 0 10px var(--color-accent-glow);
  white-space: nowrap;
}

.cursor-trail {
  position: fixed;
  pointer-events: none;
  z-index: 9997;
  transform: translate(-50%, -50%);
  transition: opacity 0.1s ease;
  font-family: 'Courier New', monospace;
  font-weight: bold;
  white-space: nowrap;
}

@media (max-width: 768px) {
  .app-container.hide-default-cursor {
    cursor: auto;
  }
  .cursor-follower,
  .cursor-trail {
    display: none !important;
  }
}

/* ===== 回到顶部按钮 ===== */
.back-top {
  position: fixed;
  right: 24px;
  bottom: 24px;
  width: 44px;
  height: 44px;
  border: none;
  border-radius: 50%;
  background: linear-gradient(135deg,
    rgba(0, 204, 255, 0.2) 0%,
    rgba(6, 182, 212, 0.2) 50%,
    rgba(139, 92, 246, 0.2) 100%
  );
  color: var(--color-accent-laser);
  font-size: 20px;
  font-weight: bold;
  cursor: pointer;
  box-shadow:
    0 4px 20px var(--color-accent-glow-soft),
    0 0 30px var(--color-accent-glow-softer),
    inset 0 1px 0 var(--color-glass-shine);
  transition: all 0.4s cubic-bezier(0.34, 1.56, 0.64, 1);
  z-index: 1080;
  backdrop-filter: blur(10px);
  border: 1px solid var(--color-glass-border-top);
}

.back-top:hover {
  transform: translateY(-4px) scale(1.08);
  box-shadow:
    0 8px 30px var(--color-accent-glow),
    0 0 50px var(--color-accent-glow-soft),
    0 0 80px var(--color-glass-outer-glow),
    inset 0 1px 0 var(--color-glass-hover-highlight);
}

.back-top:active {
  transform: translateY(-2px) scale(0.96);
  transition: all 0.1s ease;
}

/* 亮色主题下 back-top */

/* ===== 淡入淡出过渡 ===== */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

/* ===== 页面转场动画 ===== */
.page-transition-enter-active {
  transition: opacity 0.4s ease, transform 0.4s ease;
}
.page-transition-leave-active {
  transition: opacity 0.3s ease;
}
.page-transition-enter-from {
  opacity: 0;
  transform: translateY(8px);
}
.page-transition-leave-to {
  opacity: 0;
  transform: translateY(-8px);
}

/* ===== 减少动效偏好 ===== */
@media (prefers-reduced-motion: reduce) {
  .ambient-glow,
  .cursor-follower,
  .cursor-trail {
    transition: none;
    animation: none;
  }
  .stars-canvas,
  .aurora-canvas {
    display: none;
  }
}

/* ===== 亮色主题特殊处理 ===== */
</style>
