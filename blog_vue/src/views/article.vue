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
          <div v-for="(section, index) in contentSections" :key="index" :id="'chapter-' + (index + 1)" class="content-section">
            <h2 class="section-title">{{ section.title }}</h2>
            <div class="section-content" v-html="section.content"></div>
          </div>
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
import { ref } from 'vue'
// @ts-ignore
import daohang from '../components/zhuye/daohang.vue'

// 文章数据
const articleTitle = ref('深入理解 Vue 3 Composition API')
const articleMeta = ref('发布于 2024年1月15日 · 阅读时间 15分钟')
const articleAbstract = ref('本文将深入探讨 Vue 3 Composition API 的核心概念、使用场景以及最佳实践，帮助开发者更好地理解和应用这一强大的功能。')

// 章节索引
const chapters = ref([
  '什么是 Composition API',
  '为什么要使用 Composition API',
  '核心概念详解',
  '实际应用场景',
  '最佳实践指南',
  '与 Options API 的对比',
  '常见问题解答',
  '总结与展望'
])

const activeChapter = ref(0)

// 正文内容
const contentSections = ref([
  {
    title: '什么是 Composition API',
    content: '<p>Composition API 是 Vue 3 引入的一套新的 API，它提供了一种更灵活的方式来组织组件逻辑。与 Options API 相比，Composition API 允许我们将相关功能的代码组织在一起，而不是按照 data、methods、computed 等选项分散。</p><p>这种组织方式使得代码更加模块化、可复用，并且更容易进行类型推断。</p>'
  },
  {
    title: '为什么要使用 Composition API',
    content: '<p>Composition API 的出现解决了 Options API 在大型项目中的一些痛点：</p><ul><li>逻辑复用更加方便</li><li>更好的 TypeScript 支持</li><li>更灵活的代码组织方式</li><li>更容易进行代码分割和优化</li></ul>'
  },
  {
    title: '核心概念详解',
    content: '<p>Composition API 的核心概念包括：</p><ul><li><strong>setup() 函数</strong>：组件的入口点</li><li><strong>ref 和 reactive</strong>：响应式数据的创建</li><li><strong>computed 和 watch</strong>：计算属性和侦听器</li><li><strong>生命周期钩子</strong>：组件生命周期管理</li></ul>'
  }
])

const scrollToChapter = (index: number) => {
  activeChapter.value = index
  const element = document.getElementById(`chapter-${index + 1}`)
  if (element) {
    element.scrollIntoView({ behavior: 'smooth' })
  }
}
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
