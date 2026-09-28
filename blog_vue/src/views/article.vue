<template>
  <div class="article-page">
    <!-- 顶部导航栏 -->
    <daohang :is-transparent="false" />
    
    <!-- 文章封面区域 -->
    <section class="article-cover">
      <div class="cover-container">
        <h1 class="article-title">{{ articleTitle }}</h1>
        <p class="article-meta">{{ articleMeta }}</p>
      </div>
    </section>
    
    <!-- 加载状态 -->
    <div v-if="isLoading" class="loading-state">
      <p>加载中...</p>
    </div>
    
    <!-- 错误状态 -->
    <div v-if="error" class="error-state">
      <p>{{ error }}</p>
    </div>
    <!-- 主内容区域 - 三列布局 -->
    <section class="main-content">
      <!-- 左列：章节索引 -->
      <aside class="chapter-index">
        <div class="index-container">
          <h3 class="index-title">章节索引</h3>
          <ul class="index-list">
            <li v-for="(chapter, index) in chapters" :key="index" class="index-item" :class="{ active: activeChapter === index }">
              <a href="#chapter-{{ index + 1 }}" @click.prevent="scrollToChapter(index)">
                <span class="chapter-number">{{ index + 1 }}</span>
                <span class="chapter-name">{{ chapter }}</span>
              </a>
            </li>
          </ul>
        </div>
      </aside>
      
      <!-- 中列：摘要 + 正文内容 -->
      <main class="article-content">
        <!-- 摘要卡片 -->
        <div class="abstract-card">
          <h3 class="abstract-title">文章摘要</h3>
          <p class="abstract-text">{{ articleAbstract }}</p>
        </div>
        
        <!-- 正文内容 -->
        <div class="content-container">
          <div v-html="htmltext"></div>
        </div>
      </main>
      
      <!-- 右列：空侧边栏 -->
      <aside class="sidebar">
        <div class="sidebar-container">
          <!-- 预留空间，未来可添加相关文章、标签等 -->
        </div>
      </aside>
    </section>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
// @ts-ignore
import daohang from '../components/zhuye/daohang.vue'
import { art } from '../stores/art/art'
import { gethtml } from '../utils/md'

const route = useRoute()
const arts = art()
const articleId = route.params.id as string

// 加载状态
const isLoading = ref(true)
const error = ref('')

// 使用store中的数据
const articleTitle = computed(() => arts.name || '加载中...')
const articleMeta = computed(() => arts.userid ? `作者ID: ${arts.userid}` : '发布时间未知')
const articleAbstract = computed(() => arts.sum || '文章摘要')
const mdtext = computed(() => arts.cont || '# 暂无内容')
const htmltext = ref('')

const getselectart = async () => {
  try {
    isLoading.value = true
    error.value = ''
    await arts.getart(articleId)
    htmltext.value = gethtml(mdtext.value)
  } catch (err) {
    error.value = '加载文章失败，请稍后重试'
    console.error('加载文章失败:', err)
  } finally {
    isLoading.value = false
  }
}

onMounted(async () => {
  await getselectart()
})
</script>

<style scoped>
.article-page {
  min-height: 100vh;
  background: #080b18;
  color: #e2e8f0;
}

/* 文章封面区域 */
.article-cover {
  position: relative;
  width: 100%;
  height: 80vh; /* 1.2倍 = 66.67vh * 1.2 = 80vh */
  background: linear-gradient(135deg, rgba(10, 22, 40, 0.9) 0%, rgba(30, 41, 59, 0.8) 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  border-bottom: 1px solid rgba(59, 130, 246, 0.2);
}

.cover-container {
  text-align: center;
  max-width: 800px;
  padding: 0 24px;
}

.article-title {
  font-size: 3rem;
  font-weight: 700;
  color: white;
  margin-bottom: 1rem;
  text-shadow: 0 4px 20px rgba(0, 0, 0, 0.5);
  letter-spacing: 2px;
}

.article-meta {
  font-size: 1.1rem;
  color: rgba(147, 197, 253, 0.8);
  text-shadow: 0 2px 10px rgba(0, 0, 0, 0.3);
}

/* 加载状态 */
.loading-state {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 40px;
  color: rgba(147, 197, 253, 0.8);
  font-size: 1.1rem;
}

/* 错误状态 */
.error-state {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 40px;
  color: rgba(239, 68, 68, 0.8);
  font-size: 1.1rem;
}

/* 主内容区域 - 三列布局 */
.main-content {
  display: grid;
  grid-template-columns: 250px minmax(0, 900px) 250px;
  gap: 32px;
  max-width: 1400px;
  margin: 0 auto;
  padding: 64px 24px;
  align-items: start;
}

/* 左列：章节索引 */
.chapter-index {
  position: sticky;
  top: 80px;
  height: fit-content;
}

.index-container {
  background: rgba(30, 41, 59, 0.4);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(59, 130, 246, 0.15);
  border-radius: 12px;
  padding: 24px;
}

.index-title {
  font-size: 1.2rem;
  font-weight: 600;
  color: white;
  margin-bottom: 20px;
  padding-bottom: 12px;
  border-bottom: 1px solid rgba(59, 130, 246, 0.2);
}

.index-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.index-item {
  margin-bottom: 8px;
}

.index-item a {
  display: flex;
  align-items: center;
  padding: 10px 12px;
  color: rgba(226, 232, 240, 0.7);
  text-decoration: none;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.index-item a:hover {
  background: rgba(59, 130, 246, 0.1);
  color: white;
}

.index-item.active a {
  background: rgba(59, 130, 246, 0.2);
  color: #93c5fd;
  border-left: 3px solid #3b82f6;
}

.chapter-number {
  font-size: 0.9rem;
  color: rgba(147, 197, 253, 0.6);
  margin-right: 12px;
  font-weight: 600;
}

.chapter-name {
  font-size: 0.95rem;
}

/* 中列：正文内容 */
.article-content {
  min-height: 600px;
  display: flex;
  flex-direction: column;
  gap: 32px;
  max-width: 900px;
}

/* 摘要卡片 */
.abstract-card {
  background: rgba(30, 41, 59, 0.4);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(59, 130, 246, 0.15);
  border-radius: 12px;
  padding: 32px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
  width: 100%;
}

.abstract-title {
  font-size: 1.3rem;
  font-weight: 600;
  color: white;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 1px solid rgba(59, 130, 246, 0.2);
}

.abstract-text {
  font-size: 1.05rem;
  line-height: 1.8;
  color: rgba(226, 232, 240, 0.85);
}

/* 正文内容容器 */
.content-container {
  background: rgba(30, 41, 59, 0.4);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(59, 130, 246, 0.15);
  border-radius: 12px;
  padding: 40px;
  width: 100%;
}

.content-section {
  margin-bottom: 48px;
}

.content-section:last-child {
  margin-bottom: 0;
}

.section-title {
  font-size: 1.8rem;
  font-weight: 600;
  color: white;
  margin-bottom: 24px;
  padding-bottom: 12px;
  border-bottom: 1px solid rgba(59, 130, 246, 0.2);
}

.section-content {
  font-size: 1.05rem;
  line-height: 1.8;
  color: rgba(226, 232, 240, 0.85);
}

.section-content p {
  margin-bottom: 16px;
}

.section-content ul {
  margin: 16px 0;
  padding-left: 24px;
}

.section-content li {
  margin-bottom: 8px;
}

/* 右列：空侧边栏 */
.sidebar {
  position: sticky;
  top: 80px;
  height: fit-content;
}

.sidebar-container {
  background: rgba(30, 41, 59, 0.2);
  border: 1px dashed rgba(59, 130, 246, 0.2);
  border-radius: 12px;
  padding: 24px;
  min-height: 200px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: rgba(147, 197, 253, 0.4);
  font-size: 0.9rem;
}

/* 移动端适配 */
@media (max-width: 1024px) {
  .main-content {
    grid-template-columns: 200px 1fr;
    gap: 24px;
  }
  
  .sidebar {
    display: none;
  }
}

@media (max-width: 768px) {
  .article-title {
    font-size: 2rem;
  }
  
  .article-meta {
    font-size: 1rem;
  }
  
  .article-cover {
    height: 50vh;
  }
  
  .main-content {
    grid-template-columns: 1fr;
    padding: 32px 16px;
  }
  
  .chapter-index {
    position: static;
    order: 2;
  }
  
  .article-content {
    order: 1;
  }
  
  .content-container {
    padding: 24px;
  }
  
  .section-title {
    font-size: 1.5rem;
  }
}
</style>
