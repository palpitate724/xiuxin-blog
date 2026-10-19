<template>
  <div class="article-page">
    <!-- 顶部导航栏 -->
    <daohang :is-transparent="isNavTransparent" />
    
    <!-- 文章封面区域 -->
    <section class="article-cover" :style="coverStyle" ref="coverRef">
      <div class="cover-overlay"></div>
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
            <li v-for="(chapter, index) in chapters" :key="index" class="index-item" :class="{ active: activeChapter === index, subtitle: chapter.level > 0 }">
              <a :href="`#${chapter.id}`" @click.prevent="scrollToChapter(index)">
                <span v-if="chapter.level > 0" class="chapter-number subtitle-num">{{ index + 1 }}</span>
                <span v-else class="chapter-number">{{ index + 1 }}</span>
                <span class="chapter-name">{{ chapter.text }}</span>
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
import { ref, onMounted, onUnmounted, computed } from 'vue'
import { useRoute } from 'vue-router'
// @ts-ignore
import daohang from '../components/zhuye/daohang.vue'
import { art } from '../stores/art/art'
import { renderMarkdown, parseArticleIndexFromHtml, flattenIndices } from '../utils/markdown'

const route = useRoute()
const arts = art()
const articleId = route.params.id as string

// 导航栏透明度控制
const isNavTransparent = ref(true)
const coverRef = ref<HTMLElement | null>(null)

const handleNavScroll = () => {
  const cover = coverRef.value
  if (!cover) return
  const coverHeight = cover.offsetHeight
  const threshold = coverHeight * (2 / 3)
  isNavTransparent.value = window.scrollY < threshold
}

// 加载状态
const isLoading = ref(true)
const error = ref('')

// 使用store中的数据
const articleTitle = computed(() => arts.name || '加载中...')
const articleMeta = computed(() => arts.userid ? `作者ID: ${arts.userid}` : '发布时间未知')
const articleAbstract = computed(() => arts.sum || '文章摘要')
const articleCover = computed(() => arts.fenmianurl || '')
const mdtext = computed(() => arts.cont || '# 暂无内容')
const htmltext = ref('')

// 封面样式
const coverStyle = computed(() => {
  if (articleCover.value) {
    return {
      backgroundImage: `url(${articleCover.value})`,
      backgroundSize: 'cover',
      backgroundPosition: 'center',
      backgroundRepeat: 'no-repeat'
    }
  }
  return {}
})

// 章节索引 (主标题-副标题层级结构)
const chapters = ref<Array<{ text: string; id: string; level: number }>>([])
const activeChapter = ref(0)

const scrollToChapter = (index: number) => {
  activeChapter.value = index
  const element = document.getElementById(chapters.value[index]?.id)
  if (element) {
    element.scrollIntoView({ behavior: 'smooth', block: 'start' })
  }
}

const getselectart = async () => {
  try {
    isLoading.value = true
    error.value = ''
    await arts.getart(articleId)
    htmltext.value = renderMarkdown(mdtext.value)
    // 从渲染后的 HTML 解析章节索引（树形结构）
    const indexTree = parseArticleIndexFromHtml(htmltext.value)
    // 展平为带层级信息的列表：level 0 为主标题，level >0 为副标题
    chapters.value = flattenIndices(indexTree)
  } catch (err) {
    error.value = '加载文章失败，请稍后重试'
    console.error('加载文章失败:', err)
  } finally {
    isLoading.value = false
  }
}

onMounted(async () => {
  await getselectart()
  window.addEventListener('scroll', handleNavScroll)
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleNavScroll)
})
</script>

<style scoped>
.article-page {
  min-height: 100vh;
  background: var(--color-bg-deepest);
  color: var(--color-text-primary);
}

/* 文章封面区域 */
.article-cover {
  position: relative;
  width: 100%;
  height: 80vh;
  background: linear-gradient(135deg, var(--color-cover-gradient-start) 0%, var(--color-cover-gradient-end) 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  border-bottom: 1px solid var(--color-border-dim);
  overflow: hidden;
}

.cover-overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(135deg,
      rgba(10, 22, 40, 0.7) 0%,
      rgba(10, 22, 40, 0.4) 50%,
      rgba(10, 22, 40, 0.7) 100%
    );
  z-index: 1;
}

.cover-container {
  position: relative;
  z-index: 2;
  text-align: center;
  max-width: 800px;
  padding: 0 24px;
}

.article-title {
  font-size: 3rem;
  font-weight: 700;
  color: var(--color-white);
  margin-bottom: 1rem;
  text-shadow: 0 4px 20px var(--color-shadow-text);
  letter-spacing: 2px;
}

.article-meta {
  font-size: 1.1rem;
  color: var(--color-text-secondary);
  text-shadow: 0 2px 10px var(--color-shadow-text);
}

/* 加载状态 */
.loading-state {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 40px;
  color: var(--color-text-secondary);
  font-size: 1.1rem;
}

/* 错误状态 */
.error-state {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 40px;
  color: var(--color-error-soft);
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

/* 左列：章节索引 - 独立滚动 */
.chapter-index {
  position: sticky;
  top: 80px;
  height: calc(100vh - 100px);
  overflow-y: auto;
  scrollbar-width: thin;
  scrollbar-color: var(--color-accent-glow) var(--color-border-dim);
}

.index-container {
  background: var(--color-surface);
  backdrop-filter: blur(20px);
  border: 1px solid var(--color-border);
  border-radius: 12px;
  padding: 24px;
}

.index-title {
  font-size: 1.2rem;
  font-weight: 600;
  color: var(--color-white);
  margin-bottom: 20px;
  padding-bottom: 12px;
  border-bottom: 1px solid var(--color-border-dim);
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
  color: var(--color-text-muted);
  text-decoration: none;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.index-item a:hover {
  background: var(--color-accent-glow);
  color: var(--color-white);
}

.index-item.active a {
  background: var(--color-accent-glow-soft);
  color: var(--color-accent-light);
}

.index-item.active .chapter-number {
  color: var(--color-accent);
}

.chapter-number {
  font-size: 0.9rem;
  color: var(--color-text-secondary);
  margin-right: 12px;
  font-weight: 600;
}

.chapter-name {
  font-size: 0.95rem;
}

/* 副标题（子级章节） */
.index-item.subtitle {
  margin-left: 16px;
  margin-bottom: 6px;
}

.index-item.subtitle .chapter-number {
  font-size: 0.75rem;
  color: var(--color-text-muted);
  font-weight: 400;
  margin-right: 8px;
}

.index-item.subtitle .chapter-name {
  font-size: 0.9rem;
  color: var(--color-text-secondary);
}

.index-item.subtitle.active .chapter-name,
.index-item.subtitle.active .chapter-number {
  color: var(--color-accent-secondary);
}

/* 中列：正文内容 - 完全展示 */
.article-content {
  display: flex;
  flex-direction: column;
  gap: 32px;
  max-width: 900px;
}

/* 摘要卡片 */
.abstract-card {
  background: var(--color-surface);
  backdrop-filter: blur(20px);
  border: 1px solid var(--color-border);
  border-radius: 12px;
  padding: 32px;
  box-shadow: 0 4px 20px var(--color-shadow-card);
  width: 100%;
}

.abstract-title {
  font-size: 1.3rem;
  font-weight: 600;
  color: var(--color-white);
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 1px solid var(--color-border-dim);
}

.abstract-text {
  font-size: 1.05rem;
  line-height: 1.8;
  color: var(--color-text-primary-soft);
}

/* 正文内容 - Markdown 渲染样式 */
.content-container {
  background: var(--color-surface);
  backdrop-filter: blur(20px);
  border: 1px solid var(--color-border);
  border-radius: 12px;
  padding: 40px;
  width: 100%;
}

/* 深度选择器：样式作用于 v-html 渲染的 Markdown 内容 */
:deep(h1),
:deep(h2),
:deep(h3),
:deep(h4),
:deep(h5),
:deep(h6) {
  font-weight: 600;
  line-height: 1.3;
  letter-spacing: -0.01em;
  margin-top: 2em;
  margin-bottom: 1em;
  scroll-margin-top: 80px;
}

:deep(h1) {
  font-size: 2.25rem;
  color: var(--color-white);
  border-bottom: 2px solid var(--color-border-dim);
  padding-bottom: 0.5em;
}

:deep(h2) {
  font-size: 1.8rem;
  color: var(--color-accent);
  border-bottom: 1px solid var(--color-border-dim);
  padding-bottom: 0.4em;
}

:deep(h3) {
  font-size: 1.4rem;
  color: var(--color-white);
  margin-top: 1.5em;
}

:deep(h4) {
  font-size: 1.2rem;
  color: var(--color-white);
}

:deep(h5) {
  font-size: 1.1rem;
  color: var(--color-accent-light);
}

:deep(h6) {
  font-size: 1rem;
  color: var(--color-text-secondary);
}

/* 段落 */
:deep(p) {
  font-size: 1.05rem;
  line-height: 1.8;
  color: var(--color-text-primary-soft);
  margin-bottom: 1.2em;
}

/* 链接 */
:deep(a) {
  color: var(--color-accent);
  text-decoration: none;
  border-bottom: 1px solid transparent;
  transition: border-color 0.2s ease;
}

:deep(a:hover) {
  border-color: var(--color-accent-glow);
  color: var(--color-white);
}

/* 行内代码 */
:deep(code) {
  font-family: 'Courier New', monospace;
  font-size: 0.85em;
  padding: 0.15em 0.4em;
  border-radius: 4px;
  background: var(--color-surface-dark);
  color: var(--color-accent-secondary);
  line-height: 1.5;
}

/* 避免行内代码被 pre 规则覆盖 */
:deep(p code),
:deep(li code) {
  background: var(--color-surface-dark);
}

/* 代码块 */
:deep(pre) {
  position: relative;
  background: #0d1117;
  border-radius: 8px;
  padding: 20px;
  margin: 1.5em 0;
  overflow-x: auto;
  font-size: 0.9rem;
  line-height: 1.6;
  border: 1px solid var(--color-border-dim);
}

:deep(pre code) {
  background: transparent;
  padding: 0;
  font-size: 0.9rem;
  color: #e6edf3;
  border-radius: 0;
}

/* 引用块 */
:deep(blockquote) {
  margin: 1.5em 0;
  padding: 0.7em 1.6em;
  background: var(--color-surface-dark);
  border-radius: 8px;
  border: 1px solid var(--color-border-dim);
  color: var(--color-text-primary-soft);
  font-style: italic;
}

:deep(blockquote p) {
  margin-bottom: 0.5em;
  line-height: 1.6;
}

/* 有序列表 / 无序列表 */
:deep(ul),
:deep(ol) {
  margin: 1em 0;
  padding-left: 2.5em;
  color: var(--color-text-primary-soft);
  line-height: 1.8;
}

:deep(li) {
  margin-bottom: 0.5em;
}

:deep(li > p) {
  display: inline;
  margin: 0;
}

/* 表格 */
:deep(table) {
  width: 100%;
  border-collapse: collapse;
  margin: 1.5em 0;
  font-size: 0.95rem;
  color: var(--color-text-primary-soft);
  border-radius: 8px;
  overflow: hidden;
}

:deep(th),
:deep(td) {
  padding: 0.7em 1em;
  text-align: left;
  border-bottom: 1px solid var(--color-border-dim);
}

:deep(th) {
  background: var(--color-surface);
  color: var(--color-white);
  font-weight: 600;
}

:deep(tr:hover td) {
  background: var(--color-accent-glow-soft);
}

/* 横分割线 */
:deep(hr) {
  border: none;
  height: 1px;
  background: var(--color-border-dim);
  margin: 2em 0;
}

/* 图片 */
:deep(img) {
  max-width: 100%;
  height: auto;
  display: block;
  margin: 1.5em auto;
  border-radius: 8px;
  border: 1px solid var(--color-border-dim);
}

/* 图片标题 */
:deep(figcaption) {
  text-align: center;
  font-size: 0.9rem;
  color: var(--color-text-secondary);
  margin-top: 0.5em;
}

/* 右列：空侧边栏 */
.sidebar {
  position: sticky;
  top: 80px;
  height: fit-content;
}

.sidebar-container {
  background: var(--color-surface-light);
  border: 1px dashed var(--color-border-dim);
  border-radius: 12px;
  padding: 24px;
  min-height: 200px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--color-text-dim);
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
    height: auto;
    max-height: none;
    overflow: visible;
  }
  
  .article-content {
    order: 1;
    max-height: none;
    overflow: visible;
  }
  
  .content-container {
    padding: 24px;
  }

  :deep(h2) {
    font-size: 1.5rem;
  }
}
</style>
