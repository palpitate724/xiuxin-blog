<template>
  <div class="matrix-bridge-container">
    <canvas 
      ref="canvasRef" 
      class="matrix-canvas"
      :width="canvasWidth"
      :height="canvasHeight"
    ></canvas>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'

// ============ 常量定义 ============
const MATRIX_CONFIG = {
  // 区域配置
  BRIDGE_HEIGHT: 150, // 过渡区域高度（200px * 0.75 = 150px）
  
  // 透视网格配置
  ROWS_DESKTOP: 12, // 桌面端横向线条数
  COLS_DESKTOP: 32, // 桌面端纵向线条数
  ROWS_MOBILE: 8,   // 移动端横向线条数
  COLS_MOBILE: 16,  // 移动端纵向线条数
  
  // 单元格配置
  CELL_W: 44, // 单元格宽度
  CELL_H: 20, // 单元格高度
  LIGHT_DELAY: 220, // 单元格点亮停留时间(ms)
  
  // 数据雨配置
  RAIN_SPEED_MIN: 0.5, // 数据雨最小速度(px/帧)
  RAIN_SPEED_MAX: 1.5, // 数据雨最大速度(px/帧)
  
  // 卡片入场配置
  CARD_ENTER_DELAY: 140, // 矩阵稳定后卡片入场延迟(ms)
  CARD_STAGGER: 120, // 卡片交错延迟(ms)
  
  // 性能配置
  FPS_THRESHOLD: 40, // FPS低于此值降载
  MOBILE_BREAKPOINT: 768, // 移动端断点
  
  // 颜色配置
  BG_COLOR: '#080b18',
  NEBULA_COLOR: '#1a1440',
  GRID_BASE_COLOR: 'rgba(0, 204, 255, 0.25)', // 从0.15提高到0.25
  GRID_HIGHLIGHT_COLOR: 'rgba(0, 255, 200, 0.55)', // 从0.4提高到0.55
  CELL_INACTIVE_COLOR: 'rgba(57, 255, 20, 0.25)', // 从0.15提高到0.25
  CELL_ACTIVE_COLOR: '#39ff14',
  CELL_FLASH_COLOR: '#00ccff',
  RAIN_COLOR: '#39ff14',
  RAIN_HEAD_COLOR: '#00ccff',
}

// 代码字符集
const CODE_CHARS = '01<>/{ }[]'

// ============ 单元格类定义 ============
interface Cell {
  row: number
  col: number
  x: number
  y: number
  char: string
  isActive: boolean
  activeTime: number
  flashOpacity: number
}

// ============ 数据雨类定义 ============
interface RainDrop {
  x: number
  y: number
  speed: number
  char: string
}

// ============ 组件状态 ============
const canvasRef = ref<HTMLCanvasElement>()
const canvasWidth = ref(0)
const canvasHeight = ref(MATRIX_CONFIG.BRIDGE_HEIGHT)
const cells = ref<Cell[]>([])
const rainDrops = ref<RainDrop[]>([])
let animationId: number | null = null
let isAnimating = false
let isMobile = false
let lastTime = 0
let frameCount = 0
let fps = 60
let activeRowIndex = 0 // 当前激活的行索引
let lightTimer: number | null = null

// ============ 初始化 Canvas ============
const initCanvas = () => {
  const canvas = canvasRef.value
  if (!canvas) return
  
  const ctx = canvas.getContext('2d')
  if (!ctx) return
  
  // 设置Canvas尺寸
  canvasWidth.value = window.innerWidth
  canvasHeight.value = MATRIX_CONFIG.BRIDGE_HEIGHT
  
  // 检测是否为移动端
  isMobile = window.innerWidth < MATRIX_CONFIG.MOBILE_BREAKPOINT
  
  // 初始化单元格
  initCells()
  
  // 初始化数据雨
  initRain()
  
  // 开始动画
  startAnimation()
  
  console.log('微型数据矩阵初始化完成', {
    canvasWidth: canvasWidth.value,
    canvasHeight: canvasHeight.value,
    isMobile,
    cellCount: cells.value.length,
    rainCount: rainDrops.value.length
  })
}

// ============ 初始化单元格 ============
const initCells = () => {
  const rows = isMobile ? MATRIX_CONFIG.ROWS_MOBILE : MATRIX_CONFIG.ROWS_DESKTOP
  const cols = isMobile ? MATRIX_CONFIG.COLS_MOBILE : MATRIX_CONFIG.COLS_DESKTOP
  
  cells.value = []
  
  for (let row = 0; row < rows; row++) {
    for (let col = 0; col < cols; col++) {
      cells.value.push({
        row,
        col,
        x: col * MATRIX_CONFIG.CELL_W,
        y: row * MATRIX_CONFIG.CELL_H,
        char: CODE_CHARS[Math.floor(Math.random() * CODE_CHARS.length)],
        isActive: false,
        activeTime: 0,
        flashOpacity: 0
      })
    }
  }
}

// ============ 初始化数据雨 ============
const initRain = () => {
  const cols = isMobile ? MATRIX_CONFIG.COLS_MOBILE : MATRIX_CONFIG.COLS_DESKTOP
  
  rainDrops.value = []
  
  for (let i = 0; i < cols; i++) {
    rainDrops.value.push({
      x: i * (canvasWidth.value / cols) + (canvasWidth.value / cols) / 2,
      y: Math.random() * -MATRIX_CONFIG.BRIDGE_HEIGHT,
      speed: MATRIX_CONFIG.RAIN_SPEED_MIN + Math.random() * (MATRIX_CONFIG.RAIN_SPEED_MAX - MATRIX_CONFIG.RAIN_SPEED_MIN),
      char: CODE_CHARS[Math.floor(Math.random() * CODE_CHARS.length)]
    })
  }
}

// ============ 绘制透视网格 ============
const drawPerspectiveGrid = (ctx: CanvasRenderingContext2D) => {
  const w = canvasWidth.value
  const h = canvasHeight.value
  const vanishX = w / 2
  const vanishY = -h * 0.4
  const rows = isMobile ? MATRIX_CONFIG.ROWS_MOBILE : MATRIX_CONFIG.ROWS_DESKTOP
  const cols = isMobile ? MATRIX_CONFIG.COLS_MOBILE : MATRIX_CONFIG.COLS_DESKTOP
  
  // 绘制横向线（越往上越稀疏）
  ctx.strokeStyle = MATRIX_CONFIG.GRID_BASE_COLOR
  ctx.lineWidth = 1
  for (let i = 0; i <= rows; i++) {
    const t = i / rows
    const y = h - (h - vanishY) * Math.pow(t, 1.6)
    if (y < 0 || y > h) continue
    ctx.beginPath()
    ctx.moveTo(0, y)
    ctx.lineTo(w, y)
    ctx.stroke()
  }
  
  // 绘制纵向线（使用内曲线效果）
  for (let i = 0; i <= cols; i++) {
    const t = i / cols
    const xBottom = t * w
    
    // 控制点：使线条向内弯曲
    const controlX = vanishX + (xBottom - vanishX) * 0.3
    const controlY = h * 0.4
    
    ctx.beginPath()
    ctx.moveTo(xBottom, h)
    // 使用二次贝塞尔曲线创建内曲线效果
    ctx.quadraticCurveTo(controlX, controlY, vanishX, vanishY)
    ctx.stroke()
  }
}

// ============ 绘制单元格 ============
const drawCells = (ctx: CanvasRenderingContext2D) => {
  ctx.font = '12px monospace'
  
  cells.value.forEach(cell => {
    if (cell.isActive) {
      // 激活状态
      const timeSinceActive = Date.now() - cell.activeTime
      const progress = timeSinceActive / MATRIX_CONFIG.LIGHT_DELAY
      
      if (progress < 1) {
        // 点亮状态
        ctx.fillStyle = MATRIX_CONFIG.CELL_ACTIVE_COLOR
        ctx.globalAlpha = 0.5 + progress * 0.35
        
        // 闪烁高光
        if (progress < 0.3) {
          ctx.fillStyle = MATRIX_CONFIG.CELL_FLASH_COLOR
          ctx.globalAlpha = 0.3 + (1 - progress / 0.3) * 0.3
        }
      } else {
        // 向下滑动流出
        const slideProgress = (progress - 1) * 2
        if (slideProgress < 1) {
          ctx.fillStyle = MATRIX_CONFIG.CELL_ACTIVE_COLOR
          ctx.globalAlpha = 0.85 * (1 - slideProgress)
          ctx.fillText(cell.char, cell.x, cell.y + slideProgress * 20)
        } else {
          // 重置单元格
          cell.isActive = false
          cell.activeTime = 0
          cell.char = CODE_CHARS[Math.floor(Math.random() * CODE_CHARS.length)]
        }
      }
      
      if (progress < 1) {
        ctx.fillText(cell.char, cell.x, cell.y)
      }
    } else {
      // 未激活状态
      ctx.fillStyle = MATRIX_CONFIG.CELL_INACTIVE_COLOR
      ctx.globalAlpha = 0.25 // 从0.15提高到0.25
      ctx.fillText(cell.char, cell.x, cell.y)
    }
  })
  
  ctx.globalAlpha = 1
}

// ============ 绘制数据雨 ============
const drawRain = (ctx: CanvasRenderingContext2D) => {
  ctx.font = '12px monospace'
  
  rainDrops.value.forEach(drop => {
    // 更新位置
    drop.y += drop.speed
    
    // 循环
    if (drop.y > canvasHeight.value) {
      drop.y = -20
      drop.char = CODE_CHARS[Math.floor(Math.random() * CODE_CHARS.length)]
    }
    
    // 绘制渐变
    const gradient = ctx.createLinearGradient(0, drop.y - 60, 0, drop.y + 20)
    gradient.addColorStop(0, 'rgba(57, 255, 20, 0.05)')
    gradient.addColorStop(0.7, 'rgba(57, 255, 20, 0.45)')
    gradient.addColorStop(1, 'rgba(0, 204, 255, 0.7)')
    
    ctx.fillStyle = gradient
    ctx.fillText(drop.char, drop.x, drop.y)
  })
}

// ============ 绘制星云背景 ============
const drawNebula = (ctx: CanvasRenderingContext2D) => {
  const w = canvasWidth.value
  const h = canvasHeight.value
  
  // 径向渐变星云
  const gradient = ctx.createRadialGradient(w / 2, h / 2, 0, w / 2, h / 2, h)
  gradient.addColorStop(0, 'rgba(26, 20, 64, 0.5)') // 从0.35提高到0.5
  gradient.addColorStop(0.5, 'rgba(26, 20, 64, 0.35)') // 从0.25提高到0.35
  gradient.addColorStop(1, 'rgba(8, 11, 24, 0)')
  
  ctx.fillStyle = gradient
  ctx.fillRect(0, 0, w, h)
}

// ============ 动画循环 ============
const animate = (now: number) => {
  if (!isAnimating) return
  
  const canvas = canvasRef.value
  if (!canvas) return
  
  const ctx = canvas.getContext('2d')
  if (!ctx) return
  
  // FPS检测
  frameCount++
  if (now - lastTime >= 1000) {
    fps = frameCount
    frameCount = 0
    lastTime = now
    
    // FPS低于阈值时降载
    if (fps < MATRIX_CONFIG.FPS_THRESHOLD) {
      // 降低动画速度
      rainDrops.value.forEach(drop => {
        drop.speed *= 0.7
      })
    }
  }
  
  // 清空画布
  ctx.clearRect(0, 0, canvasWidth.value, canvasHeight.value)
  
  // 绘制星云背景
  drawNebula(ctx)
  
  // 绘制透视网格
  drawPerspectiveGrid(ctx)
  
  // 绘制单元格
  drawCells(ctx)
  
  // 绘制数据雨
  drawRain(ctx)
  
  animationId = requestAnimationFrame(animate)
}

// ============ 点亮单元格 ============
const lightUpCells = () => {
  const rows = isMobile ? MATRIX_CONFIG.ROWS_MOBILE : MATRIX_CONFIG.ROWS_DESKTOP
  const cols = isMobile ? MATRIX_CONFIG.COLS_MOBILE : MATRIX_CONFIG.COLS_DESKTOP
  
  // 逐行点亮
  const lightRow = () => {
    if (activeRowIndex >= rows) {
      activeRowIndex = 0
      return
    }
    
    // 点亮当前行的所有单元格
    for (let col = 0; col < cols; col++) {
      const cell = cells.value[activeRowIndex * cols + col]
      if (cell) {
        cell.isActive = true
        cell.activeTime = Date.now()
      }
    }
    
    activeRowIndex++
    
    // 继续下一行
    lightTimer = window.setTimeout(lightRow, 50)
  }
  
  lightRow()
}

// ============ 停止点亮 ============
const stopLightUp = () => {
  if (lightTimer) {
    clearTimeout(lightTimer)
    lightTimer = null
  }
  activeRowIndex = 0
}

// ============ 动画控制 ============
const startAnimation = () => {
  if (!isAnimating) {
    isAnimating = true
    lastTime = performance.now()
    animate(lastTime)
    
    // 开始点亮单元格
    lightUpCells()
  }
}

const pauseAnimation = () => {
  isAnimating = false
  stopLightUp()
  if (animationId) {
    cancelAnimationFrame(animationId)
    animationId = null
  }
}

const resetAnimation = () => {
  pauseAnimation()
  initCells()
  initRain()
  activeRowIndex = 0
}

// ============ 滚动监听 ============
const handleScroll = () => {
  const bridgeContainer = document.querySelector('.matrix-bridge-container')
  if (!bridgeContainer) return
  
  const rect = bridgeContainer.getBoundingClientRect()
  const windowHeight = window.innerHeight
  
  // 计算过渡区域在视口中的可见比例
  const visibleRatio = Math.max(0, Math.min(1, (windowHeight - rect.top) / rect.height))
  
  if (visibleRatio > 0.1) {
    // 状态B：滚动进入过渡区域
    if (!isAnimating) {
      startAnimation()
      
      // 延迟触发文章卡片入场动画
      setTimeout(() => {
        // 触发卡片入场
        const event = new CustomEvent('matrixCardEnter')
        window.dispatchEvent(event)
      }, MATRIX_CONFIG.CARD_ENTER_DELAY)
    }
  } else if (rect.bottom < 0 || rect.top > windowHeight) {
    // 状态C：滚动完全离开过渡区域
    pauseAnimation()
  }
}

// ============ 生命周期 ============
onMounted(() => {
  initCanvas()
  window.addEventListener('scroll', handleScroll)
  window.addEventListener('resize', () => {
    canvasWidth.value = window.innerWidth
    isMobile = window.innerWidth < MATRIX_CONFIG.MOBILE_BREAKPOINT
    initCells()
    initRain()
  })
  
  // 检测prefers-reduced-motion
  if (window.matchMedia('(prefers-reduced-motion: reduce)').matches) {
    pauseAnimation()
  }
})

onUnmounted(() => {
  pauseAnimation()
  stopLightUp()
  window.removeEventListener('scroll', handleScroll)
})

// 暴露方法给父组件
defineExpose({
  startAnimation,
  pauseAnimation,
  resetAnimation
})
</script>

<style scoped>
.matrix-bridge-container {
  position: relative;
  width: 100%;
  height: 150px;
  background: transparent;
  overflow: hidden;
  pointer-events: none;
  z-index: 2;
}

.matrix-canvas {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  display: block;
}
</style>
