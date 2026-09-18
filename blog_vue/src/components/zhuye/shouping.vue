<template>
  <section class="hero-section" ref="heroSectionRef">
    <div class="hero-content">
      <video 
        class="hero-video" 
        autoplay 
        muted 
        loop 
        playsinline
        src="../../assets/img/4K7.mp4"
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
import { ref } from 'vue'

const heroSectionRef = ref<HTMLElement | null>(null)

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
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}

.hero-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, rgba(10, 22, 40, 0.3) 0%, rgba(30, 41, 59, 0.2) 100%);
}

.hero-text {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  text-align: center;
  z-index: 10;
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
