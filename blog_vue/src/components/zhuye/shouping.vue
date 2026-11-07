<template>
  <section class="hero-section" ref="heroSectionRef">
    <div class="hero-content">
      <!-- 懒加载：所有video使用 preload="none" + poster，仅活跃的video加载source -->
      <video
        v-for="(video, index) in videos"
        :key="video"
        ref="videoRefs"
        class="hero-video"
        :class="{ active: index === currentVideoIndex }"
        :poster="posters[index]"
        preload="none"
        muted
        playsinline
        @ended="handleVideoEnded(index)"
        @error="handleVideoError"
        @loadeddata="handleVideoLoaded"
      >
        <!-- 仅活跃的视频才加载 source，实现懒加载 -->
        <source v-if="index === currentVideoIndex" :src="video" type="video/mp4">
      </video>
      
      <div class="hero-overlay"></div>
      <div class="hero-text">
        <h1 class="hero-title">蔚蓝极客</h1>
        <p class="hero-subtitle">探索技术边界，记录成长轨迹</p>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount, nextTick } from 'vue'

/* ---------- 配置 ---------- */
const fileNames = [
  '1 (1).mp4',
  '1 (2).mp4',
  '1 (3).mp4',
  '1 (4).mp4',
  '1 (5).mp4',
  '1 (6).mp4',
  '1 (7).mp4',
]
const SWITCH_INTERVAL = 15000 // 兜底切换时间（毫秒）

/* ---------- 路径生成 ---------- */
// 用 new URL 处理带空格/括号的路径，避免打包后 404
const videos = fileNames.map(
  name => new URL(`../../../public/video/${name}`, import.meta.url).href
)

/* ---------- 封面海报 ---------- */
const posters = fileNames.map(
  (_, index) => new URL(`../../../public/video/poster-${index + 1}.jpg`, import.meta.url).href
)

/* ---------- 状态 ---------- */
const heroSectionRef = ref<HTMLElement | null>(null)
const videoRefs = ref<HTMLVideoElement[]>([])
const currentVideoIndex = ref(0)
let videoInterval: number | null = null

/* ---------- 切换逻辑 ---------- */
async function nextVideo() {
  console.log('切换到下一个视频')
  // 1. 暂停当前视频
  const prevIndex = currentVideoIndex.value
  if (videoRefs.value[prevIndex]) {
    videoRefs.value[prevIndex].pause()
  }

  // 2. 索引循环
  const nextIndex = (prevIndex + 1) % videos.length
  currentVideoIndex.value = nextIndex

  // 3. 等 DOM 更新完成 (source 元素被渲染)
  await nextTick()

  // 4. 手动 load + play，否则画面不刷新
  const v = videoRefs.value[nextIndex]
  if (v) {
    v.load()
    v.play().catch(err => {
      console.error('视频播放失败:', err)
      // 自动播放可能被浏览器拦截，忽略即可
    })
  }
}

/* ---------- 事件处理 ---------- */
const handleVideoEnded = (index: number) => {
  console.log(`视频 ${index} 播放完成`)
  // 只有当前播放的视频结束才触发切换
  if (index === currentVideoIndex.value) {
    nextVideo()
  }
}

const handleVideoError = (e: Event) => {
  console.error('视频加载错误:', e)
}

const handleVideoLoaded = () => {
  console.log('视频加载成功')
}

/* ---------- 生命周期 ---------- */
onMounted(() => {
  console.log('组件已挂载，启动视频轮播')
  
  // 兜底：即使 @ended 未触发，也能定时切换
  videoInterval = window.setInterval(nextVideo, SWITCH_INTERVAL)
  
  // 初始播放第一个视频（已经设置首个 source）
  if (videoRefs.value[0]) {
    videoRefs.value[0].load()
    videoRefs.value[0].play().catch(err => {
      console.error('初始视频播放失败:', err)
    })
  }
})

onBeforeUnmount(() => {
  console.log('组件卸载，清理定时器')
  if (videoInterval !== null) {
    clearInterval(videoInterval)
    videoInterval = null
  }
  // 暂停所有视频，释放内存
  videoRefs.value.forEach(v => v.pause())
})

// 暴露内部ref给父组件，用于滚动监听
defineExpose({
  heroSectionRef
})
</script>

<style scoped>
/* Hero区域 */
.hero-section {
  width: 100%;
  margin-bottom: 0;
}

.hero-content {
  position: relative;
  width: 100%;
  height: 100vh;
  border-radius: 0;
  overflow: hidden;
  border-bottom: 1px solid var(--color-border-strong);
}

.hero-video {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  z-index: -1;
  opacity: 0;
  transition: opacity var(--duration-hero-fade) var(--ease-standard);
  /* 扭曲动画效果 */
  animation:
    pulse var(--duration-hero-pulse) var(--ease-standard) infinite,
    subtle-rotate var(--duration-hero-rotate) var(--ease-linear) infinite,
    hue-shift var(--duration-hero-hue) var(--ease-linear) infinite;
  filter: blur(0px) saturate(1.1);
}

.hero-video.active {
  opacity: 1;
}

.hero-video:hover {
  filter: blur(0.5px) saturate(1.2);
}

/* 脉冲缩放效果 */
@keyframes pulse {
  0%, 100% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.03);
  }
}

/* 微妙旋转效果 */
@keyframes subtle-rotate {
  0% {
    transform: scale(1) rotate(0deg);
  }
  50% {
    transform: scale(1.02) rotate(0.5deg);
  }
  100% {
    transform: scale(1) rotate(0deg);
  }
}

/* 色相偏移效果 */
@keyframes hue-shift {
  0% {
    filter: hue-rotate(0deg) brightness(1);
  }
  50% {
    filter: hue-rotate(5deg) brightness(1.05);
  }
  100% {
    filter: hue-rotate(0deg) brightness(1);
  }
}

.hero-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(
    135deg,
    var(--color-cover-gradient-start) 0%,
    rgba(10, 22, 40, 0.5) 40%,
    rgba(10, 22, 40, 0.3) 60%,
    var(--color-cover-gradient-end) 100%
  );
  opacity: 0.75;
  backdrop-filter: blur(1px);
  animation: overlay-pulse var(--duration-hero-overlay) var(--ease-standard) infinite;
}

/* 叠加层脉冲效果 */
@keyframes overlay-pulse {
  0%, 100% {
    opacity: 0.72;
  }
  50% {
    opacity: 0.82;
  }
}

/* 底部渐变遮罩，让内容区更清晰 */
.hero-overlay::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 30%;
  background: linear-gradient(to top, var(--color-bg-deep) 0%, transparent 100%);
  pointer-events: none;
}

.hero-text {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  text-align: center;
  z-index: 10;
  /* 文字也添加轻微抖动效果 */
  animation: text-float var(--duration-hero-text-float) var(--ease-standard) infinite;
}

/* 文字入场动画 */
.hero-text::before {
  content: '';
  display: inline-block;
  width: 60px;
  height: 3px;
  background: linear-gradient(90deg, transparent, var(--color-accent), transparent);
  margin: 0 auto var(--spacing-md);
  animation: line-grow 1s ease-out forwards;
  opacity: 0;
}

@keyframes line-grow {
  from {
    width: 0;
    opacity: 0;
  }
  to {
    width: 60px;
    opacity: 1;
  }
}

/* 文字浮动效果 */
@keyframes text-float {
  0%, 100% {
    transform: translate(-50%, -50%);
  }
  50% {
    transform: translate(-50%, -52%);
  }
}

.hero-title {
  font-size: var(--font-size-display);
  font-weight: var(--font-weight-bold);
  color: var(--color-white);
  margin-bottom: var(--spacing-md);
  text-shadow:
    0 0 40px rgba(59, 130, 246, 0.5),
    0 4px 20px var(--color-shadow-text);
  letter-spacing: var(--hero-letter-spacing);
  animation: title-glow 3s ease-in-out infinite alternate;
  /* 提升文字质感：添加微妙的渐变填充 */
  background: linear-gradient(135deg, #ffffff 0%, #e0f2fe 50%, #bae6fd 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

/* 浅色主题Hero — 完全替换为静态背景，隐藏视频+遮罩+动画 */

@keyframes title-glow {
  from {
    text-shadow:
      0 0 40px rgba(59, 130, 246, 0.4),
      0 4px 20px var(--color-shadow-text);
  }
  to {
    text-shadow:
      0 0 60px rgba(59, 130, 246, 0.7),
      0 0 100px rgba(6, 182, 212, 0.3),
      0 4px 20px var(--color-shadow-text);
  }
}

.hero-subtitle {
  font-size: var(--font-size-hero-subtitle);
  color: var(--color-accent-light);
  text-shadow: 0 2px 10px var(--color-shadow-text);
  letter-spacing: 3px;
  font-weight: 300;
  /* 渐变色文字 */
  background: linear-gradient(90deg, var(--color-accent-lighter) 0%, var(--color-accent-secondary) 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

/* 移动端适配 */
@media (max-width: 768px) {
  .hero-content {
    height: calc(100vh - 56px);
  }

  .hero-title {
    font-size: var(--font-size-hero-title-mobile);
  }

  .hero-subtitle {
    font-size: var(--font-size-hero-subtitle-mobile);
  }

  .hero-video {
    /* 移动端减弱动画效果 */
    animation:
      pulse var(--duration-hero-pulse-mobile) var(--ease-standard) infinite,
      subtle-rotate var(--duration-hero-rotate-mobile) var(--ease-linear) infinite,
      hue-shift var(--duration-hero-hue-mobile) var(--ease-linear) infinite;
  }

  .hero-text::before {
    display: none;
  }
}

@media (max-width: 480px) {
  .hero-content {
    height: calc(100vh - 56px);
  }

  .hero-title {
    font-size: var(--font-size-hero-title-sm);
  }

  .hero-subtitle {
    font-size: var(--font-size-hero-subtitle-sm);
    letter-spacing: 1px;
  }
}

/* 减少 motion 偏好 - 禁用 Hero 区域动画 */
@media (prefers-reduced-motion: reduce) {
  .hero-video {
    animation: none;
    transition: none;
  }

  .hero-overlay {
    animation: none;
    opacity: 0.75;
  }

  .hero-overlay::after {
    display: none;
  }

  .hero-text {
    animation: none;
  }

  .hero-title {
    animation: none;
  }

  .hero-text::before {
    animation: none;
    opacity: 1;
  }

  .hero-video:hover {
    filter: none;
  }
}
</style>
