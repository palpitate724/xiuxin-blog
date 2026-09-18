<template>
  <div class="zhuye-page">
    <!-- 顶部导航栏 -->
    <daohang :is-transparent="isNavTransparent" />
    
    <!-- 主内容区域 -->
    <div class="zhuye-content" ref="contentRef">
      <!-- 首屏Hero区域 -->
      <shouping ref="heroSectionRef" />
      
      <!-- 博客内容列表 -->
      <div class="blog-container">
        <artlist ref="artlistRef" />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import daohang from '../components/zhuye/daohang.vue'
import shouping from '../components/zhuye/shouping.vue'
import artlist from '../components/zhuye/art/artlist.vue'

// 导航栏透明度控制
const isNavTransparent = ref(true)
const heroSectionRef = ref<InstanceType<typeof shouping> | null>(null)
const artlistRef = ref<InstanceType<typeof artlist> | null>(null)
let animationTriggered = false

const handleScroll = () => {
  const heroSection = heroSectionRef.value?.heroSectionRef
  if (!heroSection) return
  
  const heroHeight = heroSection.offsetHeight
  const scrollY = window.scrollY
  
  // 当滚动超过2/3的Hero高度时，导航栏变为不透明
  const threshold = heroHeight * (2 / 3)
  isNavTransparent.value = scrollY < threshold
  
  // 当滚动回到顶部时，重置动画状态
  if (scrollY < 50) {
    resetAnimation()
  }
  
  // 触发过渡动画：当首屏MP4无法完全展示时（即开始滚动时）
  if (!animationTriggered && scrollY > 0) {
    triggerTransitionAnimation()
    animationTriggered = true
  }
}

const resetAnimation = () => {
  // 重置动画触发标志
  animationTriggered = false
  
  // 重置文章列表的动画状态
  if (artlistRef.value) {
    artlistRef.value.resetAnimation()
  }
}

const triggerTransitionAnimation = () => {
  // 直接触发文章列表的卡片动画
  artlistRef.value?.startAnimation()
}

onMounted(() => {
  // 添加滚动监听
  window.addEventListener('scroll', handleScroll)
})

onUnmounted(() => {
  // 移除滚动监听
  window.removeEventListener('scroll', handleScroll)
})
</script>

<style scoped>
.zhuye-page {
  min-height: 100vh;
  background: transparent;
  position: relative;
  overflow: hidden;
  padding: 0;
  margin: 0;
}

.zhuye-content {
  position: relative;
  z-index: 3;
  width: 100%;
  padding: 0;
  margin: 0;
}

/* 博客容器 */
.blog-container {
  max-width: 1400px;
  margin: 0 auto;
}

/* 移动端适配 */
@media (max-width: 768px) {
  .blog-container {
    padding: 0 16px;
  }
}

@media (max-width: 480px) {
  .blog-container {
    padding: 0 12px;
  }
}
</style>
