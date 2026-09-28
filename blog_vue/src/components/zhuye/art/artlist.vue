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
  gap: 30px;
  margin-bottom: 80px;
  padding: 64px 24px 0;
  position: relative;
}

.blog-item {
  display: flex;
  height: 245px;
  margin-bottom: 0;
  overflow: hidden;
  border-radius: 16px;
  background: rgba(30, 41, 59, 0.4);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(59, 130, 246, 0.15);
  transition: all 0.3s ease;
  width: 100%;
  opacity: 0;
  transform: translateY(26px);
  filter: blur(3px);
  transition: all 0.6s ease-out;
}

.blog-item.show {
  opacity: 1;
  transform: translateY(0);
  filter: blur(0);
}

/* 左右交替 */
.layout-left {
  flex-direction: row;
}

.layout-right {
  flex-direction: row-reverse;
}



.blog-item:hover {
  transform: translateY(-4px);
  border-color: rgba(59, 130, 246, 0.3);
  box-shadow: 0 8px 32px rgba(59, 130, 246, 0.2);
}

.blog-cover {
  flex: 0 0 50%;
  height: 100%;
  overflow: hidden;
  background: linear-gradient(135deg, #1e3a8a 0%, #3b82f6 100%);
  transition: transform 0.3s ease;
  position: relative;
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
  transform: scale(1.05);
}

.blog-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 32px 40px;
  z-index: 1;
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
  font-size: 1.5rem;
  font-weight: 600;
  color: white;
  margin-bottom: 16px;
  line-height: 1.4;
  text-decoration: none;
  display: block;
  transition: color 0.3s ease;
}

.blog-title:hover {
  color: #00ccff;
}

.blog-excerpt {
  font-size: 1rem;
  color: rgba(148, 163, 184, 0.8);
  line-height: 1.6;
  margin-bottom: 24px;
  flex: 1;
}

.blog-meta {
  display: flex;
  gap: 16px;
  font-size: 0.875rem;
  color: rgba(147, 197, 253, 0.7);
}

.blog-date {
  display: flex;
  align-items: center;
  gap: 4px;
}

.blog-tags {
  display: flex;
  align-items: center;
  gap: 4px;
}

/* 左右交替布局 */
.blog-item-left {
  flex-direction: row;
}

.blog-item-right {
  flex-direction: row-reverse;
}

/* 分页组件 */
.pagination {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 32px 0 64px;
}

/* Element Plus 分页组件样式覆盖（非background模式） */
.pagination :deep(.el-pagination) {
  --el-pagination-text-color: rgba(147, 197, 253, 0.8);
  --el-pagination-bg-color: transparent;
  --el-pagination-border-radius: 8px;
  --el-pagination-font-size: 0.9rem;
}

/* 普通页码按钮 */
.pagination :deep(.el-pager li) {
  background-color: rgba(59, 130, 246, 0.1);
  color: rgba(147, 197, 253, 0.8);
  border: 1px solid rgba(59, 130, 246, 0.2);
  border-radius: 8px;
  min-width: 40px;
  height: 40px;
  line-height: 40px;
  margin: 0 4px;
}

/* 普通页码悬停 */
.pagination :deep(.el-pager li:hover) {
  background-color: rgba(59, 130, 246, 0.2);
  color: white;
  border-color: rgba(59, 130, 246, 0.4);
}

/* 选中页码 - 强制高亮 */
.pagination :deep(.el-pager li.is-active) {
  background-color: rgba(59, 130, 246, 0.4) !important;
  color: white !important;
  border-color: rgba(59, 130, 246, 0.6) !important;
  box-shadow: 0 2px 8px rgba(59, 130, 246, 0.4) !important;
}

/* 上一页/下一页按钮 - 默认不高亮 */
.pagination :deep(.el-pagination button) {
  background-color: rgba(59, 130, 246, 0.1) !important;
  color: rgba(147, 197, 253, 0.8) !important;
  border: 1px solid rgba(59, 130, 246, 0.2) !important;
  border-radius: 8px;
  min-width: 80px;
  height: 40px;
  margin: 0 4px;
}

/* 上一页/下一页按钮悬停 */
.pagination :deep(.el-pagination button:hover:not(:disabled)) {
  background-color: rgba(59, 130, 246, 0.2) !important;
  color: white !important;
  border-color: rgba(59, 130, 246, 0.4) !important;
}

/* 禁用按钮 */
.pagination :deep(.el-pagination button:disabled) {
  background-color: rgba(59, 130, 246, 0.05) !important;
  color: rgba(147, 197, 253, 0.4) !important;
  border-color: rgba(59, 130, 246, 0.1) !important;
}

/* 省略号 - 不高亮 */
.pagination :deep(.el-pager li.more) {
  background-color: transparent !important;
  color: rgba(147, 197, 253, 0.5) !important;
  border: none !important;
  cursor: default !important;
}

/* 移动端适配 */
@media (max-width: 768px) {
  .blog-list {
    padding: 48px 0 0;
  }
  
  .blog-item {
    flex-direction: column !important;
    height: auto;
    gap: 16px;
  }
  
  .blog-cover {
    clip-path: none;
    flex: 0 0 200px;
    margin: 0;
    height: 200px;
  }
  
  .blog-content {
    padding: 20px;
  }
  
  .blog-title {
    font-size: 1.2rem;
  }
  
  .blog-excerpt {
    font-size: 0.9rem;
  }
  
  .pagination {
    flex-wrap: wrap;
    gap: 8px;
    padding: 32px 0 64px;
  }
  
  .pagination-btn {
    padding: 8px 16px;
    font-size: 0.85rem;
  }
  
  .pagination-page {
    width: 36px;
    height: 36px;
    font-size: 0.85rem;
  }
}

@media (max-width: 480px) {
  .blog-list {
    gap: 32px;
    padding: 32px 0 0;
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
    padding: 16px;
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
  
  .pagination-btn {
    padding: 6px 12px;
    font-size: 0.8rem;
  }
  
  .pagination-page {
    width: 32px;
    height: 32px;
    font-size: 0.8rem;
  }
}
</style>
