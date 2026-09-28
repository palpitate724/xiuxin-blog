<template>
  <section class="hero-section" ref="heroSectionRef">
    <div class="hero-content">
      <!-- 淡入淡出切换：预创建所有video，用opacity过渡 -->
      <video
        v-for="(video, index) in videos"
        :key="video"
        ref="videoRefs"
        class="hero-video"
        :class="{ active: index === currentVideoIndex }"
        autoplay
        muted
        playsinline
        :src="video"
        @ended="handleVideoEnded(index)"
        @error="handleVideoError"
        @loadeddata="handleVideoLoaded"
      ></video>
      
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
  name => new URL(`../../assets/img/${name}`, import.meta.url).href
)

/* ---------- 状态 ---------- */
const heroSectionRef = ref<HTMLElement | null>(null)
const videoRefs = ref<HTMLVideoElement[]>([])
const currentVideoIndex = ref(0)
let videoInterval: number | null = null

/* ---------- 切换逻辑 ---------- */
async function nextVideo() {
  console.log('切换到下一个视频')
  // 1. 索引循环
  const nextIndex = (currentVideoIndex.value + 1) % videos.length
  currentVideoIndex.value = nextIndex

  // 2. 等 DOM 更新完成
  await nextTick()

  // 3. 手动 load + play，否则画面不刷新
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
  
  // 初始播放第一个视频
  if (videoRefs.value[0]) {
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
  border-bottom: 1px solid rgba(59, 130, 246, 0.2);
}

.hero-video {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  z-index: -1;
  opacity: 0;
  transition: opacity 1.5s ease-in-out;
  /* 扭曲动画效果 */
  animation: 
    pulse 8s ease-in-out infinite,
    subtle-rotate 20s linear infinite,
    hue-shift 15s linear infinite;
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
  background: linear-gradient(135deg, rgba(10, 22, 40, 0.3) 0%, rgba(30, 41, 59, 0.2) 100%);
  /* 叠加动态扭曲效果 */
  animation: overlay-pulse 10s ease-in-out infinite;
}

/* 叠加层脉冲效果 */
@keyframes overlay-pulse {
  0%, 100% {
    opacity: 0.3;
  }
  50% {
    opacity: 0.35;
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
  animation: text-float 6s ease-in-out infinite;
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
  font-size: 3rem;
  font-weight: 700;
  color: white;
  margin-bottom: 16px;
  text-shadow: 0 4px 20px rgba(0, 0, 0, 0.5);
  letter-spacing: 2px;
}

.hero-subtitle {
  font-size: 1.2rem;
  color: rgba(147, 197, 253, 0.9);
  text-shadow: 0 2px 10px rgba(0, 0, 0, 0.3);
}

/* 移动端适配 */
@media (max-width: 768px) {
  .hero-content {
    height: calc(100vh - 56px);
  }
  
  .hero-title {
    font-size: 2rem;
  }
  
  .hero-subtitle {
    font-size: 1rem;
  }
  
  .hero-video {
    /* 移动端减弱动画效果 */
    animation: 
      pulse 10s ease-in-out infinite,
      subtle-rotate 30s linear infinite,
      hue-shift 20s linear infinite;
  }
}

@media (max-width: 480px) {
  .hero-content {
    height: calc(100vh - 56px);
  }
  
  .hero-title {
    font-size: 1.5rem;
  }
  
  .hero-subtitle {
    font-size: 0.9rem;
  }
}
</style>
