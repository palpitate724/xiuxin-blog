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
  filter: blur(0px);
}

.hero-video.active {
  opacity: 1;
}

.hero-video:hover {
  filter: blur(0.5px);
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
  background: linear-gradient(135deg, var(--color-cover-gradient-start) 0%, var(--color-cover-gradient-end) 100%);
  opacity: 0; /* 罩层透明 - 视频完全展示 */
  /* 叠加动态扭曲效果 */
  animation: overlay-pulse var(--duration-hero-overlay) var(--ease-standard) infinite;
}

/* 叠加层脉冲效果 */
@keyframes overlay-pulse {
  0%, 100% {
    opacity: var(--color-overlay-opacity-soft);
  }
  50% {
    opacity: var(--color-overlay-opacity-mid);
  }
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
  text-shadow: 0 4px 20px var(--color-shadow-text);
  letter-spacing: var(--hero-letter-spacing);
}

.hero-subtitle {
  font-size: var(--font-size-hero-subtitle);
  color: var(--color-accent-light);
  text-shadow: 0 2px 10px var(--color-shadow-text);
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
    opacity: 0;
  }
  
  .hero-text {
    animation: none;
  }
  
  .hero-video:hover {
    filter: none;
  }
}
</style>
