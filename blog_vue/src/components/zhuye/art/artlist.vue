<template>
  <div class="artlist-container">
    <section class="blog-list" ref="blogListRef">
      <!-- 文章列表 -->
      <div v-for="(art, index) in artlist.artlist" :key="art.id.toString()">
            <article 
              :ref="(el) => setCardRef(el as HTMLElement, index)"
              :class="['blog-item', index % 2 === 0 ? 'layout-left' : 'layout-right', { show: isCardShown[index] }]">
              <div class="blog-cover blog-cover-1">
                <img :src="art.fenmianurl" alt="博客封面图" style="width: 100%; height: 100%; object-fit: cover;">
              </div>
              <div class="blog-content">
                <router-link :to="`/article/${art.id}`" class="blog-title text-row">
                  {{ art.name }}
                </router-link>
                <p class="blog-excerpt text-row">{{ art.sum }}</p>
                <div class="blog-meta text-row">
                  <span v-for="tag in art.tagvolist" :key="tag.id" class="blog-tags">{{ tag.name }}</span>
                </div>
              </div>
            </article>
        </div>
    </section>
    
    <!-- 分页组件 -->
    <section class="pagination">
      <el-pagination
        v-model:current-page="currentPage"
        :page-size="pageSize"
        :total="total"
        :page-count="totalPages"
        layout="prev, pager, next"
        @current-change="handlePageChange"
      />
    </section>
  </div>
</template>

<script lang="ts" setup>
import { onMounted, ref, computed, watch } from 'vue'
import { artlistpage } from "../../../stores/art/artlist"

const artlist = artlistpage()
const isCardShown = ref<boolean[]>([])
const cardRefs = ref<HTMLElement[]>([])
const currentPage = ref(1)
const pageSize = 10
const totalPages = computed(() => artlist.pages)
const total = computed(() => artlist.total)

// 监听store中的current变化，同步到本地currentPage
watch(() => artlist.current, (newVal) => {
  currentPage.value = newVal
})



const setCardRef = (el: HTMLElement | null, index: number) => {
  if (el) {
    cardRefs.value[index] = el
  }
}

const pageselect = (page: number) => {
  currentPage.value = page
  artlist.getartlist(page, pageSize)
  // 重置卡片显示状态
  isCardShown.value = new Array(artlist.artlist.length).fill(false)
  // 触发卡片入场动画
  setTimeout(() => {
    startAnimation()
  }, 100)
}

const handlePageChange = (page: number) => {
  pageselect(page)
}

const modgetartlist = async ()=> {
  await artlist.getartlist(1,pageSize)
  console.log(artlist.artlist)
  // 同步当前页码
  currentPage.value = artlist.current || 1
  // 初始化卡片显示状态和ref数组
  isCardShown.value = new Array(artlist.artlist.length).fill(false)
  cardRefs.value = new Array(artlist.artlist.length).fill(null)
}

const startAnimation = () => {
  // 按照文档要求实现交错延迟入场动画
  artlist.artlist.forEach((_, index) => {
    setTimeout(() => {
      isCardShown.value[index] = true
    }, index * 120) // 每张卡片间隔120ms
  })
}

const resetAnimation = () => {
  // 重置所有卡片显示状态
  isCardShown.value = new Array(artlist.artlist.length).fill(false)
}

onMounted(async() => {
  await modgetartlist()
})

defineExpose({
  startAnimation,
  resetAnimation
})
</script>

<style scoped>
.artlist-container {
  width: 69%;
  margin: 0 auto;
}

/* 博客列表 */
.blog-list {
  display: flex;
  flex-direction: column;
  gap: 32px;
  margin-bottom: 80px;
  padding: 64px 24px 0;
  position: relative;
}

/* 液态玻璃博客卡片 */
.blog-item {
  display: flex;
  height: 260px;
  margin-bottom: 0;
  overflow: hidden;
  border-radius: 24px;
  /* 液态玻璃基础 */
  background: linear-gradient(
    160deg,
    rgba(255, 255, 255, 0.08) 0%,
    rgba(255, 255, 255, 0.03) 40%,
    rgba(255, 255, 255, 0.01) 100%
  );
  backdrop-filter: blur(28px) saturate(1.9) brightness(1.06);
  -webkit-backdrop-filter: blur(28px) saturate(1.9) brightness(1.06);
  border: 1px solid rgba(255, 255, 255, 0.08);
  background-clip: padding-box;
  transition: all 0.5s cubic-bezier(0.34, 1.56, 0.64, 1);
  width: 100%;
  opacity: 0;
  transform: translateY(30px) scale(0.98);
  filter: blur(2px);
  box-shadow:
    0 8px 32px rgba(0, 0, 0, 0.22),
    0 2px 8px rgba(0, 0, 0, 0.12),
    inset 0 1px 0 rgba(255, 255, 255, 0.12),
    inset 0 -1px 0 rgba(0, 0, 0, 0.08);
  position: relative;
}

/* 玻璃表面高光（顶部光线折射） */
.blog-item::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 55%;
  background: linear-gradient(
    180deg,
    rgba(255, 255, 255, 0.1) 0%,
    rgba(255, 255, 255, 0.025) 55%,
    transparent 100%
  );
  border-radius: 24px 24px 0 0;
  pointer-events: none;
  z-index: 1;
  transition: background 0.4s ease;
}

/* 顶部边缘光晕 */
.blog-item::after {
  content: '';
  position: absolute;
  top: 0;
  left: 16px;
  right: 16px;
  height: 1px;
  background: linear-gradient(
    90deg,
    transparent 0%,
    rgba(255, 255, 255, 0.3) 20%,
    rgba(255, 255, 255, 0.5) 50%,
    rgba(255, 255, 255, 0.3) 80%,
    transparent 100%
  );
  pointer-events: none;
  z-index: 2;
}

.blog-item.show {
  opacity: 1;
  transform: translateY(0) scale(1);
  filter: blur(0);
}

/* 左右交替 */
.layout-left {
  flex-direction: row;
}

.layout-right {
  flex-direction: row-reverse;
}

/* 悬浮液态玻璃增强 */
.blog-item:hover {
  transform: translateY(-6px) scale(1.01);
  border-color: rgba(255, 255, 255, 0.14);
  box-shadow:
    0 16px 56px rgba(0, 0, 0, 0.3),
    0 4px 14px rgba(0, 0, 0, 0.16),
    inset 0 1px 0 rgba(255, 255, 255, 0.18),
    inset 0 -1px 0 rgba(0, 0, 0, 0.08),
    0 0 0 1px rgba(255, 255, 255, 0.06),
    0 0 40px var(--color-accent-glow-soft);
}

.blog-item:hover::before {
  background: linear-gradient(
    180deg,
    rgba(255, 255, 255, 0.16) 0%,
    rgba(255, 255, 255, 0.05) 55%,
    transparent 100%
  );
}

/* 左右交替 */
.layout-left {
  flex-direction: row;
}

.layout-right {
  flex-direction: row-reverse;
}

/* 封面图区域 */
.blog-cover {
  flex: 0 0 50%;
  height: 100%;
  overflow: hidden;
  background: linear-gradient(135deg, var(--color-bg-nav) 0%, var(--color-navy-mid) 100%);
  transition: transform 0.5s ease;
  position: relative;
}

.blog-cover::after {
  content: '';
  position: absolute;
  inset: 0;
  background: linear-gradient(
    to right,
    transparent 60%,
    rgba(10, 22, 40, 0.4) 100%
  );
  pointer-events: none;
}

.layout-left .blog-cover::after {
  background: linear-gradient(
    to right,
    transparent 50%,
    rgba(10, 22, 40, 0.5) 100%
  );
}

.layout-right .blog-cover::after {
  background: linear-gradient(
    to left,
    transparent 50%,
    rgba(10, 22, 40, 0.5) 100%
  );
}

/* 左布局的封面：右边缘切斜线 */
.layout-left .blog-cover {
  clip-path: polygon(0 0, 100% 0, 88% 100%, 0 100%);
  margin-right: -3%;
}

/* 右布局的封面：左边缘切斜线 */
.layout-right .blog-cover {
  clip-path: polygon(12% 0, 100% 0, 100% 100%, 0 100%);
  margin-left: -3%;
}

.blog-item:hover .blog-cover {
  transform: scale(1.08);
}

/* 内容区域 */
.blog-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 28px 36px;
  z-index: 1;
  position: relative;
}

/* 文本行依次出现 */
.text-row {
  opacity: 0;
  transform: translateY(12px);
  transition: opacity 0.3s ease, transform 0.3s ease;
}

.blog-item.show .text-row:nth-child(1) {
  transition-delay: 0.05s;
  opacity: 1;
  transform: translateY(0);
}
.blog-item.show .text-row:nth-child(2) {
  transition-delay: 0.1s;
  opacity: 1;
  transform: translateY(0);
}
.blog-item.show .text-row:nth-child(3) {
  transition-delay: 0.15s;
  opacity: 1;
  transform: translateY(0);
}

.blog-title {
  font-size: 1.45rem;
  font-weight: 600;
  color: var(--color-white);
  margin-bottom: 12px;
  line-height: 1.4;
  text-decoration: none;
  display: block;
  transition: all 0.3s ease;
  position: relative;
  padding-left: 12px;
  /* 渐变文字 */
  background: linear-gradient(135deg, #ffffff 0%, #bae6fd 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

/* 浅色主题博客标题 — 深色确保可读性 */


.blog-title::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 3px;
  height: 0;
  background: linear-gradient(180deg, var(--color-accent) 0%, var(--color-accent-secondary) 100%);
  border-radius: 2px;
  transition: height 0.3s ease;
}

.blog-title:hover {
  color: var(--color-progress-start);
  text-shadow: 0 0 20px var(--color-accent-glow);
}

.blog-title:hover::before {
  height: 80%;
}

.blog-excerpt {
  font-size: 0.92rem;
  color: var(--color-text-muted);
  line-height: 1.7;
  margin-bottom: 20px;
  flex: 1;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.blog-meta {
  display: flex;
  gap: 10px;
  font-size: 0.82rem;
  color: var(--color-text-muted);
  align-items: center;
  flex-wrap: wrap;
}

.blog-tags {
  display: inline-flex;
  align-items: center;
  padding: 3px 10px;
  background: var(--color-surface);
  border: 1px solid var(--color-border-dim);
  border-radius: 20px;
  color: var(--color-accent-lighter);
  font-size: 0.78rem;
  transition: all 0.3s ease;
}

/* 浅色主题标签样式 */


/* 浅色主题博客封面区域 */


/* 浅色主题博客卡片整体适配 */



/* 博客摘要文字浅色适配 */

.blog-tags:hover {
  background: var(--color-accent-glow-soft);
  border-color: var(--color-border-stronger);
  color: var(--color-accent-light);
  transform: translateY(-1px);
}

/* 分页组件 */
.pagination {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px 0 80px;
}

/* Element Plus 分页组件样式覆盖 */
.pagination :deep(.el-pagination) {
  --el-pagination-text-color: var(--color-text-secondary);
  --el-pagination-bg-color: transparent;
  --el-pagination-border-radius: 8px;
  --el-pagination-font-size: 0.9rem;
}

/* 普通页码按钮 */
.pagination :deep(.el-pager li) {
  background-color: rgba(255, 255, 255, 0.85) !important;
  color: var(--color-gray-600) !important;
  border: 1px solid rgba(0, 0, 0, 0.08) !important;
}

/* 浅色主题分页 */



/* 普通页码悬停 */
.pagination :deep(.el-pager li:hover) {
  background-color: var(--color-accent-glow-soft);
  color: var(--color-white);
  border-color: var(--color-border-stronger);
  transform: translateY(-2px);
  box-shadow: 0 4px 12px var(--color-accent-glow);
}

/* 选中页码 */
.pagination :deep(.el-pager li.is-active) {
  background: linear-gradient(135deg, var(--color-accent) 0%, var(--color-accent-secondary) 100%) !important;
  color: var(--color-white) !important;
  border-color: transparent !important;
  box-shadow:
    0 4px 15px var(--color-accent-glow),
    0 0 20px var(--color-accent-glow-soft) !important;
  transform: scale(1.05);
}

/* 上一页/下一页按钮 */
.pagination :deep(.el-pagination button) {
  background-color: rgba(30, 41, 59, 0.5) !important;
  color: var(--color-text-secondary) !important;
  border: 1px solid var(--color-border) !important;
  border-radius: 8px;
  min-width: 80px;
  height: 40px;
  margin: 0 4px;
  transition: all 0.3s ease;
}

.pagination :deep(.el-pagination button:hover:not(:disabled)) {
  background-color: var(--color-accent-glow-soft) !important;
  color: var(--color-white) !important;
  border-color: var(--color-border-stronger) !important;
  transform: translateY(-2px);
}

/* 禁用按钮 */
.pagination :deep(.el-pagination button:disabled) {
  background-color: rgba(59, 130, 246, 0.05) !important;
  color: var(--color-accent-lighter) !important;
  border-color: var(--color-border-dim) !important;
}

/* 省略号 */
.pagination :deep(.el-pager li.more) {
  background-color: transparent !important;
  color: var(--color-accent-light) !important;
  border: none !important;
  cursor: default !important;
}

/* 移动端适配 */
@media (max-width: 768px) {
  .blog-list {
    padding: 48px 16px 0;
  }

  .blog-item {
    flex-direction: column !important;
    height: auto;
    gap: 0;
  }

  .blog-cover {
    clip-path: none;
    flex: 0 0 200px;
    margin: 0;
    height: 200px;
  }

  .blog-cover::after {
    background: linear-gradient(to bottom, transparent 50%, rgba(10, 22, 40, 0.6) 100%);
  }

  .blog-content {
    padding: 20px 24px;
  }

  .blog-title {
    font-size: 1.2rem;
  }

  .blog-excerpt {
    font-size: 0.88rem;
  }

  .pagination {
    flex-wrap: wrap;
    gap: 8px;
    padding: 32px 0 64px;
  }
}

@media (max-width: 480px) {
  .blog-list {
    gap: 24px;
    padding: 32px 12px 0;
  }

  .blog-item {
    flex-direction: column !important;
    height: auto;
  }

  .blog-cover {
    clip-path: none;
    margin: 0;
    height: 180px;
  }

  .blog-content {
    padding: 16px 20px;
  }

  .blog-title {
    font-size: 1.1rem;
  }

  .blog-excerpt {
    font-size: 0.85rem;
  }

  .pagination {
    padding: 24px 0 48px;
  }
}
</style>
