<template>
  <div class="article-page">
    <!-- 顶部导航栏 -->
    <daohang :is-transparent="isNavTransparent" />
    
    <!-- 文章封面区域 — 有封面图时显示图片，无封面图时显示渐变背景 -->
    <section
      class="article-cover"
      :class="{ 'has-cover': articleCover }"
      :style="articleCover ? coverStyle : { background: 'linear-gradient(135deg, #e8eef6 0%, #d4dce8 100%)' }"
      ref="coverRef"
    >
      <div class="cover-overlay">
      </div>
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
    <!-- 主内容区域 -->
    <section class="main-content">
      <!-- 文章摘要 + 正文内容 -->
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
      
    </section>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, computed, watch } from 'vue'
import { useRoute } from 'vue-router'
// @ts-ignore
import daohang from '../components/zhuye/daohang.vue'
import { art } from '../stores/art/art'
import { renderMarkdown } from '../utils/markdown'
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

// 封面样式 — 响应式主题和封面URL
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

// 使用 CSS position: sticky，无需 JS 控制
// 仅用于移动端检测和初始化
const getselectart = async () => {
  try {
    isLoading.value = true
    error.value = ''
    await arts.getart(articleId)
    htmltext.value = renderMarkdown(mdtext.value)
  } catch (err) {
    error.value = '加载文章失败，请稍后重试'
    console.error('加载文章失败:', err)
  } finally {
    isLoading.value = false
  }
}

onMounted(async () => {
  await getselectart()
  window.addEventListener('scroll', handleNavScroll, { passive: true })
  
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleNavScroll)
  window.removeEventListener('resize', () => {})
})
</script>

<style scoped>
.article-page {
  min-height: 100vh;
  background: var(--color-bg-deepest);
  color: var(--color-text-primary);
  transition: background var(--duration-theme) ease, color var(--duration-theme) ease;
}

/* 文章封面区域 */
.article-cover {
  position: relative;
  width: 100%;
  height: 75vh;
  background: linear-gradient(135deg, var(--color-cover-gradient-start) 0%, var(--color-cover-gradient-end) 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  border-bottom: 1px solid var(--color-border-strong);
  overflow: hidden;
  transition: background var(--duration-theme) ease;
}

/* 浅色主题封面 — 有封面图时显示图片，无封面图时显示淡蓝灰渐变 */
.cover-overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(
    135deg,
    rgba(10, 22, 40, 0.75) 0%,
    rgba(10, 22, 40, 0.5) 40%,
    rgba(10, 22, 40, 0.5) 60%,
    rgba(10, 22, 40, 0.75) 100%
  );
  z-index: 1;
}

.cover-overlay::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 40%;
  background: linear-gradient(to top, var(--color-bg-deepest) 0%, transparent 100%);
  pointer-events: none;
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
  text-shadow:
    0 0 40px rgba(59, 130, 246, 0.4),
    0 4px 20px var(--color-shadow-text);
  letter-spacing: 2px;
  animation: article-title-glow 3s ease-in-out infinite alternate;
  /* 渐变文字质感 */
  background: linear-gradient(135deg, #ffffff 0%, #bae6fd 60%, #7dd3fc 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

@keyframes article-title-glow {
  from {
    text-shadow:
      0 0 40px rgba(59, 130, 246, 0.3),
      0 4px 20px var(--color-shadow-text);
  }
  to {
    text-shadow:
      0 0 60px rgba(59, 130, 246, 0.6),
      0 0 100px rgba(6, 182, 212, 0.2),
      0 4px 20px var(--color-shadow-text);
  }
}

.article-meta {
  font-size: 1.1rem;
  color: var(--color-text-secondary);
  text-shadow: 0 2px 10px var(--color-shadow-text);
  letter-spacing: 1px;
}

/* 加载状态 */
.loading-state {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 60px;
  color: var(--color-text-muted);
  font-size: 1.1rem;
  position: relative;
}

.loading-state::after {
  content: '';
  display: block;
  width: 24px;
  height: 24px;
  border: 2px solid var(--color-border-dim);
  border-top-color: var(--color-accent);
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
  margin-left: 12px;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

/* 错误状态 */
.error-state {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 60px;
  color: var(--color-error-soft);
  font-size: 1.1rem;
  background: var(--color-error-bg);
  border: 1px solid var(--color-error-border);
  border-radius: 12px;
  margin: 40px auto;
  max-width: 600px;
}

/* 主内容区域 */
.main-content {
  max-width: 900px;
  margin: 0 auto;
  padding: 56px 24px;
}

/* 章节索引 - 使用 sticky 固定 */


/* 移动端模式 */


/* 液态玻璃章节索引容器 */


/* 玻璃表面高光 */


/* 顶部边缘光晕 */


/* 渐变顶条装饰 */


.index-title {
  font-size: 1.15rem;
  font-weight: 600;
  color: var(--color-white);
  margin-bottom: 20px;
  padding-bottom: 12px;
  border-bottom: 1px solid var(--color-border-dim);
  letter-spacing: 0.5px;
  /* 标题前装饰线 */
  position: relative;
  padding-left: 14px;
}

.index-title::before {
  content: '';
  position: absolute;
  left: 0;
  top: 25%;
  bottom: 25%;
  width: 3px;
  background: linear-gradient(180deg, var(--color-accent) 0%, var(--color-accent-secondary) 100%);
  border-radius: 2px;
  box-shadow: 0 0 8px var(--color-accent-glow);
}























/* 副标题（子级章节） */








/* 中列：正文内容 - 完全展示 */
.article-content {
  display: flex;
  flex-direction: column;
  gap: 28px;
  max-width: 900px;
}

/* 液态玻璃摘要卡片 */
.abstract-card {
  /* 液态玻璃基础效果 */
  background: linear-gradient(
    160deg,
    rgba(255, 255, 255, 0.07) 0%,
    rgba(255, 255, 255, 0.03) 40%,
    rgba(255, 255, 255, 0.01) 100%
  );
  backdrop-filter: blur(32px) saturate(1.9) brightness(1.06);
  -webkit-backdrop-filter: blur(32px) saturate(1.9) brightness(1.06);
  border: 1px solid transparent;
  background-clip: padding-box;
  border-radius: 20px;
  padding: 28px 32px;
  box-shadow:
    0 8px 32px rgba(0, 0, 0, 0.2),
    0 2px 8px rgba(0, 0, 0, 0.1),
    inset 0 1px 0 rgba(255, 255, 255, 0.1),
    inset 0 -1px 0 rgba(0, 0, 0, 0.08);
  width: 100%;
  position: relative;
  overflow: hidden;
}

/* 玻璃表面高光 */
.abstract-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 50%;
  background: linear-gradient(
    180deg,
    rgba(255, 255, 255, 0.08) 0%,
    rgba(255, 255, 255, 0.02) 60%,
    transparent 100%
  );
  border-radius: 20px 20px 0 0;
  pointer-events: none;
  z-index: 1;
}

/* 渐变顶条装饰 */
.abstract-card .top-bar {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 2px;
  background: linear-gradient(90deg, var(--color-accent-secondary), var(--color-accent-tertiary), var(--color-accent));
  opacity: 0.7;
  border-radius: 20px 20px 0 0;
}

.abstract-title {
  font-size: 1.2rem;
  font-weight: 600;
  color: var(--color-white);
  margin-bottom: 14px;
  padding-bottom: 10px;
  border-bottom: 1px solid var(--color-border-dim);
  display: flex;
  align-items: center;
  gap: 8px;
}

.abstract-title::before {
  content: '';
  display: inline-block;
  width: 4px;
  height: 16px;
  background: linear-gradient(180deg, var(--color-accent), var(--color-accent-secondary));
  border-radius: 2px;
}

.abstract-text {
  font-size: 1.02rem;
  line-height: 1.8;
  color: var(--color-text-primary-soft);
}

/* 液态玻璃正文容器 */
.content-container {
  /* 液态玻璃基础效果 */
  background: linear-gradient(
    160deg,
    rgba(255, 255, 255, 0.06) 0%,
    rgba(255, 255, 255, 0.025) 40%,
    rgba(255, 255, 255, 0.008) 100%
  );
  backdrop-filter: blur(32px) saturate(1.9) brightness(1.06);
  -webkit-backdrop-filter: blur(32px) saturate(1.9) brightness(1.06);
  border: 1px solid transparent;
  background-clip: padding-box;
  border-radius: 20px;
  padding: 40px;
  width: 100%;
  box-shadow:
    0 8px 32px rgba(0, 0, 0, 0.2),
    0 2px 8px rgba(0, 0, 0, 0.1),
    inset 0 1px 0 rgba(255, 255, 255, 0.08),
    inset 0 -1px 0 rgba(0, 0, 0, 0.06),
    0 0 60px var(--color-accent-glow-xlight);
  position: relative;
  overflow: hidden;
}

/* 玻璃表面高光 */
.content-container::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 50%;
  background: linear-gradient(
    180deg,
    rgba(255, 255, 255, 0.07) 0%,
    rgba(255, 255, 255, 0.015) 60%,
    transparent 100%
  );
  border-radius: 20px 20px 0 0;
  pointer-events: none;
  z-index: 1;
}

/* 顶部边缘光晕 */
.content-container::after {
  content: '';
  position: absolute;
  top: 0;
  left: 20px;
  right: 20px;
  height: 1px;
  background: linear-gradient(
    90deg,
    transparent 0%,
    rgba(255, 255, 255, 0.2) 20%,
    rgba(255, 255, 255, 0.35) 50%,
    rgba(255, 255, 255, 0.2) 80%,
    transparent 100%
  );
  pointer-events: none;
  z-index: 2;
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
  /* 平滑滚动锚点 */
  scroll-padding-top: 80px;
}

:deep(h1) {
  font-size: 2.2rem;
  color: var(--color-white);
  border-bottom: 2px solid var(--color-border-dim);
  padding-bottom: 0.5em;
  position: relative;
}

:deep(h1)::after {
  content: '';
  position: absolute;
  bottom: -2px;
  left: 0;
  width: 60px;
  height: 2px;
  background: linear-gradient(90deg, var(--color-accent), transparent);
}

:deep(h2) {
  font-size: 1.75rem;
  color: var(--color-accent-light);
  border-bottom: 1px solid var(--color-border-dim);
  padding-bottom: 0.4em;
  position: relative;
}

:deep(h2)::after {
  content: '';
  position: absolute;
  bottom: -1px;
  left: 0;
  width: 40px;
  height: 1px;
  background: linear-gradient(90deg, var(--color-accent-secondary), transparent);
}

:deep(h3) {
  font-size: 1.35rem;
  color: var(--color-white);
  margin-top: 1.5em;
}

:deep(h4) {
  font-size: 1.15rem;
  color: var(--color-white);
}

:deep(h5) {
  font-size: 1.05rem;
  color: var(--color-accent-light);
}

:deep(h6) {
  font-size: 0.95rem;
  color: var(--color-text-secondary);
}

/* 段落 */
:deep(p) {
  font-size: 1.05rem;
  line-height: 1.9;
  color: var(--color-text-primary-soft);
  margin-bottom: 1.2em;
}

/* 链接 */
:deep(a) {
  color: var(--color-accent);
  text-decoration: none;
  border-bottom: 1px solid transparent;
  transition: all 0.2s ease;
}

:deep(a:hover) {
  border-color: var(--color-accent);
  color: var(--color-accent-light);
  text-shadow: 0 0 8px var(--color-accent-glow);
}

/* 行内代码 */
:deep(code) {
  font-family: 'Courier New', monospace;
  font-size: 0.85em;
  padding: 0.15em 0.4em;
  border-radius: 4px;
  background: rgba(6, 182, 212, 0.1);
  color: var(--color-accent-secondary);
  line-height: 1.5;
  border: 1px solid rgba(6, 182, 212, 0.15);
}

/* 避免行内代码被 pre 规则覆盖 */
:deep(p code),
:deep(li code) {
  background: rgba(6, 182, 212, 0.1);
}

/* 代码块 */
:deep(pre) {
  position: relative;
  background: rgba(13, 17, 23, 0.8);
  border-radius: 12px;
  padding: 24px;
  margin: 1.5em 0;
  overflow-x: auto;
  font-size: 0.88rem;
  line-height: 1.6;
  border: 1px solid var(--color-border-dim);
  box-shadow: inset 0 2px 8px rgba(0, 0, 0, 0.3);
}

:deep(pre)::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 1px;
  background: linear-gradient(90deg, transparent, var(--color-accent-glow-soft), transparent);
}

:deep(pre code) {
  background: transparent;
  padding: 0;
  font-size: 0.88rem;
  color: #e6edf3;
  border-radius: 0;
  border: none;
}

/* 引用块 */
:deep(blockquote) {
  margin: 1.5em 0;
  padding: 0.8em 1.6em;
  background: linear-gradient(90deg, var(--color-accent-glow-soft) 0%, transparent 100%);
  border-radius: 8px;
  border-left: 3px solid var(--color-accent);
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
  padding-left: 2.2em;
  color: var(--color-text-primary-soft);
  line-height: 1.8;
}

:deep(li) {
  margin-bottom: 0.5em;
  position: relative;
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
  border-radius: 10px;
  overflow: hidden;
  border: 1px solid var(--color-border-dim);
}

:deep(th),
:deep(td) {
  padding: 0.8em 1em;
  text-align: left;
  border-bottom: 1px solid var(--color-border-dim);
}

:deep(th) {
  background: rgba(30, 41, 59, 0.6);
  color: var(--color-white);
  font-weight: 600;
  letter-spacing: 0.3px;
}

:deep(tr:hover td) {
  background: var(--color-accent-glow-xlight);
}

/* 横分割线 */
:deep(hr) {
  border: none;
  height: 1px;
  background: linear-gradient(90deg, transparent, var(--color-border-dim), transparent);
  margin: 2em 0;
}

/* 图片 */
:deep(img) {
  max-width: 100%;
  height: auto;
  display: block;
  margin: 1.5em auto;
  border-radius: 10px;
  border: 1px solid var(--color-border-dim);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.3);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

:deep(img:hover) {
  transform: scale(1.01);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.4);
}

/* 图片标题 */
:deep(figcaption) {
  text-align: center;
  font-size: 0.88rem;
  color: var(--color-text-muted);
  margin-top: 0.5em;
  font-style: italic;
}

/* 右列：空侧边栏 */

/* 液态玻璃侧边栏 */

/* 玻璃表面高光 */

/* 顶部边缘光晕 */

/* 悬浮状态增强 */
.abstract-card:hover,
.content-container:hover,

/* 移动端适配 */


  .abstract-card {
    padding: 20px 24px;
  }

  :deep(h2) {
    font-size: 1.4rem;
  }


/* ===== 亮色主题适配 — 冷调科技质感 ===== */

/* 浅色主题封面 — 有封面图时显示图片，无封面图时显示淡蓝灰渐变 */

/* 浅色主题封面底部渐变 — 仅用于过渡，不遮挡封面图 */

/* 浅色主题封面 — 有封面图时显示图片，无封面图时显示淡蓝灰渐变 */


/* 浅色主题封面区标题 — 深色文字 + 白色外发光确保可读 */


/* 文章详情页卡片 — 玻璃质感 */

/* 浅色主题卡片高光 — 极淡，不产生过曝效果 */


/* 浅色主题隐藏所有渐变装饰条 */

/* 浅色主题摘要标题前装饰条 */














/* 浅色主题章节索引样式 */




/* 浅色主题摘要卡片样式 */



/* 浅色主题加载和错误状态 */


/* 浅色主题侧边栏 */
</style>
