<template>
  <canvas 
    ref="canvasRef" 
    class="meteor-canvas"
  ></canvas>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'

// ============ 流星配置 ============
const METEOR_CONFIG = {
  // 流星生成间隔（毫秒）- 更频繁
  intervalMin: 500,
  intervalMax: 1200,
  
  // 流星速度范围 - 调整为现在的0.5倍（即原来的0.25倍）
  speedMin: 2,     // 4 * 0.5
  speedMax: 3.75,  // 7.5 * 0.5
  
  // 重力加速度（抛物线轨迹）- 也调整为0.5倍
  gravity: 0.0375, // 0.075 * 0.5
  
  // 流星拖尾长度 - 调整为现在的0.6倍
  tailMin: 180,   // 300 * 0.6
  tailMax: 420,   // 700 * 0.6
  
  // 流星大小范围（头部圆点大小）
  sizeMin: 1.5,
  sizeMax: 2.0,
  
  // 闪烁参数
  flickerSpeed: 0.05, // 闪烁速度
  flickerIntensity: 0.3, // 闪烁强度
  
  // 颜色配置 - 青蓝色
  headColor: '#ffffff',
  glowColor: '#96DCFF',
  tailStartColor: '#96DCFF',
  tailEndColor: 'rgba(150, 220, 255, 0)',
  
  // 线条粗细 - 极细
  lineWidthMin: 0.5,
  lineWidthMax: 1.3,
  
  // 最大同时存在的流星数量
  maxMeteors: 6,
  
  // 移动端降级配置
  mobile: {
    maxMeteors: 4,
    intervalMin: 800,
    intervalMax: 1500
  }
}

// ============ 流星类定义 ============
interface Meteor {
  x: number
  y: number
  length: number
  speed: number
  angle: number
  alpha: number
  phase: 'appearing' | 'flying' | 'fading'
  life: number
  maxLife: number
  vx: number
  vy: number
  lineWidth: number
  size: number // 流星大小
  trail: Array<{x: number, y: number}> // 轨迹历史
  flickerOffset: number // 闪烁偏移
}

// ============ 组件状态 ============
const canvasRef = ref<HTMLCanvasElement>()
let ctx: CanvasRenderingContext2D | null = null
let meteors: Meteor[] = []
let animationId: number | null = null
let meteorTimer: number | null = null
let isMobile = false

// ============ 初始化Canvas ============
const initCanvas = () => {
  const canvas = canvasRef.value
  if (!canvas) return
  
  ctx = canvas.getContext('2d')
  if (!ctx) return
  
  resizeCanvas()
  window.addEventListener('resize', resizeCanvas)
  
  // 检测是否为移动端
  isMobile = window.innerWidth <= 768
  
  // 开始动画循环
  loop()
  
  // 开始生成流星
  startMeteorGeneration()
  
  console.log('流星效果初始化完成', { isMobile })
}

// ============ 调整Canvas尺寸 ============
const resizeCanvas = () => {
  const canvas = canvasRef.value
  if (!canvas) return
  
  const dpr = window.devicePixelRatio || 1
  canvas.width = window.innerWidth * dpr
  canvas.height = window.innerHeight * dpr
  canvas.style.width = `${window.innerWidth}px`
  canvas.style.height = `${window.innerHeight}px`
  
  if (ctx) {
    ctx.scale(dpr, dpr)
  }
}

// ============ 创建流星 ============
const createMeteor = () => {
  if (meteors.length >= (isMobile ? METEOR_CONFIG.mobile.maxMeteors : METEOR_CONFIG.maxMeteors)) {
    return
  }
  
  const canvas = canvasRef.value
  if (!canvas) return
  
  // 随机起始位置（屏幕上方，拓宽起始X）
  const startX = Math.random() * window.innerWidth * 1.5
  const startY = -100 // 从屏幕上方100px开始
  
  // 水平速度（恒定，现在的0.5倍）
  const speedX = -(Math.random() * 1.75 + 2) // -2 到 -3.75（0.5倍）
  // 初始垂直速度（向上，然后会受重力影响向下，现在的0.5倍）
  const speedY = -(Math.random() * 0.75 + 0.5) // -0.5 到 -1.25（0.5倍）
  
  // 随机拖尾长度（极长）
  const length = METEOR_CONFIG.tailMin + Math.random() * (METEOR_CONFIG.tailMax - METEOR_CONFIG.tailMin)
  
  // 固定线条粗细为1.5
  const lineWidth = 1.5
  
  // 随机流星大小
  const size = METEOR_CONFIG.sizeMin + Math.random() * (METEOR_CONFIG.sizeMax - METEOR_CONFIG.sizeMin)
  
  const meteor: Meteor = {
    x: startX,
    y: startY,
    length: length,
    speed: Math.sqrt(speedX * speedX + speedY * speedY),
    angle: Math.atan2(speedY, speedX) * 180 / Math.PI,
    alpha: 0,
    phase: 'appearing',
    life: 1, // 生命值 0-1
    maxLife: 100,
    vx: speedX,
    vy: speedY,
    lineWidth: lineWidth,
    size: size,
    trail: [], // 轨迹历史
    flickerOffset: Math.random() * Math.PI * 2 // 随机闪烁相位
  }
  
  meteors.push(meteor)
  console.log('创建抛物线流星', meteors.length)
}

// ============ 更新流星位置 ============
const updateMeteor = (meteor: Meteor) => {
  // 记录当前位置到轨迹历史
  meteor.trail.push({ x: meteor.x, y: meteor.y })
  
  // 限制轨迹历史长度（适配更慢的速度，轨迹需要更长）
  const maxTrailLength = Math.floor(meteor.length / 3) // 除数从5减小到3，轨迹更长
  if (meteor.trail.length > maxTrailLength) {
    meteor.trail.shift()
  }
  
  // 水平速度保持不变
  meteor.x += meteor.vx
  
  // 垂直速度受重力影响（抛物线轨迹）
  meteor.vy += METEOR_CONFIG.gravity
  meteor.y += meteor.vy
  
  // 更新闪烁偏移
  meteor.flickerOffset += METEOR_CONFIG.flickerSpeed
  
  // 更新生命周期 - 延缓消散
  meteor.life -= 0.002 // 更慢的生命衰减
  
  // 阶段管理
  if (meteor.phase === 'appearing') {
    // 快速显现
    meteor.alpha = Math.min(1, meteor.alpha + 0.2)
    if (meteor.alpha >= 1) {
      meteor.phase = 'flying'
    }
  } else if (meteor.phase === 'flying') {
    // 保持高亮
    meteor.alpha = 1
    // 飞行到一定阶段后开始消逝
    if (meteor.life < 0.4) {
      meteor.phase = 'fading'
    }
  } else if (meteor.phase === 'fading') {
    // 逐渐消逝
    meteor.alpha = Math.max(0, meteor.life)
  }
}

// ============ 绘制流星 ============
const drawMeteor = (meteor: Meteor) => {
  if (!ctx) return
  
  // 计算闪烁值（缓慢闪烁）
  const flickerValue = Math.sin(meteor.flickerOffset) * METEOR_CONFIG.flickerIntensity
  const flickerAlpha = meteor.alpha * (1 + flickerValue) // 闪烁透明度
  
  // 如果有轨迹历史，绘制抛物线尾迹
  if (meteor.trail.length > 1) {
    ctx.beginPath()
    ctx.moveTo(meteor.trail[0].x, meteor.trail[0].y)
    
    // 使用轨迹历史点绘制平滑曲线
    for (let i = 1; i < meteor.trail.length; i++) {
      ctx.lineTo(meteor.trail[i].x, meteor.trail[i].y)
    }
    
    // 连接到当前位置
    ctx.lineTo(meteor.x, meteor.y)
    
    // 创建线性渐变（头部到尾部）- 青蓝色
    const gradient = ctx.createLinearGradient(meteor.x, meteor.y, meteor.trail[0].x, meteor.trail[0].y)
    gradient.addColorStop(0, `rgba(255, 255, 255, ${flickerAlpha})`) // 头部亮白（带闪烁）
    gradient.addColorStop(0.1, `rgba(150, 220, 255, ${flickerAlpha * 0.8})`) // 青蓝色
    gradient.addColorStop(1, 'rgba(150, 220, 255, 0)') // 尾部透明
    
    ctx.strokeStyle = gradient
    // 极细的线条 - 激光感
    ctx.lineWidth = meteor.lineWidth
    ctx.lineCap = 'round'
    ctx.lineJoin = 'round'
    ctx.stroke()
  } else {
    // 如果没有足够轨迹历史，使用原来的绘制方式
    const tailX = meteor.x - meteor.vx * (meteor.length / 20)
    const tailY = meteor.y - meteor.vy * (meteor.length / 20)
    
    const gradient = ctx.createLinearGradient(meteor.x, meteor.y, tailX, tailY)
    gradient.addColorStop(0, `rgba(255, 255, 255, ${flickerAlpha})`)
    gradient.addColorStop(0.1, `rgba(150, 220, 255, ${flickerAlpha * 0.8})`)
    gradient.addColorStop(1, 'rgba(150, 220, 255, 0)')
    
    ctx.beginPath()
    ctx.moveTo(meteor.x, meteor.y)
    ctx.lineTo(tailX, tailY)
    ctx.strokeStyle = gradient
    ctx.lineWidth = meteor.lineWidth
    ctx.lineCap = 'round'
    ctx.stroke()
  }
  
  // 绘制流星头部（高亮核心，大小随机）
  ctx.beginPath()
  ctx.arc(meteor.x, meteor.y, meteor.size, 0, Math.PI * 2)
  ctx.fillStyle = `rgba(255, 255, 255, ${flickerAlpha})`
  ctx.fill()
}

// ============ 清理流星 ============
const cleanupMeteors = () => {
  // 移除飞出屏幕或完全消逝的流星
  meteors = meteors.filter(meteor => {
    const isOffScreen = meteor.x < -200 || meteor.y > window.innerHeight + 200
    const isDead = meteor.alpha <= 0
    return !isOffScreen && !isDead
  })
}

// ============ 动画循环 ============
const loop = () => {
  if (!ctx) return
  
  // 清空画布
  ctx.clearRect(0, 0, window.innerWidth, window.innerHeight)
  
  // 更新和绘制所有流星
  meteors.forEach(meteor => {
    updateMeteor(meteor)
    drawMeteor(meteor)
  })
  
  // 清理无效流星
  cleanupMeteors()
  
  animationId = requestAnimationFrame(loop)
}

// ============ 流星生成定时器 ============
const startMeteorGeneration = () => {
  const scheduleNextMeteor = () => {
    const intervalMin = isMobile ? METEOR_CONFIG.mobile.intervalMin : METEOR_CONFIG.intervalMin
    const intervalMax = isMobile ? METEOR_CONFIG.mobile.intervalMax : METEOR_CONFIG.intervalMax
    const interval = intervalMin + Math.random() * (intervalMax - intervalMin)
    
    meteorTimer = window.setTimeout(() => {
      createMeteor()
      scheduleNextMeteor()
    }, interval)
  }
  
  scheduleNextMeteor()
}

// ============ 停止流星生成 ============
const stopMeteorGeneration = () => {
  if (meteorTimer) {
    clearTimeout(meteorTimer)
    meteorTimer = null
  }
}

// ============ 生命周期 ============
onMounted(() => {
  initCanvas()
})

onUnmounted(() => {
  if (animationId) {
    cancelAnimationFrame(animationId)
  }
  stopMeteorGeneration()
  window.removeEventListener('resize', resizeCanvas)
})

// 暴露方法给父组件
defineExpose({
  createMeteor,
  stopMeteorGeneration
})
</script>

<style scoped>
.meteor-canvas {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 10;
  pointer-events: none;
}
</style>
