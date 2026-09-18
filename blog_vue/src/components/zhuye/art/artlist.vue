<template>
  <div class="artlist-container">
    <section class="blog-list" ref="blogListRef">
      <!-- 文章列表 -->
      <div v-for="(art, index) in artlist.artlist" :key="art.id">
            <article 
              :ref="(el) => setCardRef(el as HTMLElement, index)"
              :class="['blog-item', index % 2 === 0 ? 'layout-left' : 'layout-right', { show: isCardShown[index] }]">
              <div class="blog-cover blog-cover-1">
                <img :src="art.fenmianurl" alt="博客封面图" style="width: 100%; height: 100%; object-fit: cover;">
              </div>
              <div class="blog-content">
                <h2 class="blog-title text-row">{{ art.name }}</h2>
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
      <button class="pagination-btn pagination-prev" disabled>上一页</button>
      <div class="pagination-pages">
        <button class="pagination-page active">1</button>
        <button class="pagination-page">2</button>
        <button class="pagination-page">3</button>
        <span class="pagination-ellipsis">...</span>
        <button class="pagination-page">10</button>
      </div>
      <button class="pagination-btn pagination-next">下一页</button>
    </section>
  </div>
</template>

<script lang="ts" setup>
import { onMounted, ref } from 'vue'
import { artlistpage } from "../../../stores/art/artlist"

const artlist = artlistpage()
const isCardShown = ref<boolean[]>([])
const cardRefs = ref<HTMLElement[]>([])

const setCardRef = (el: HTMLElement | null, index: number) => {
  if (el) {
    cardRefs.value[index] = el
  }
}

const modgetartlist = async ()=> {
  await artlist.getartlist()
  console.log(artlist.artlist)
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
  gap: 16px;
  padding: 32px 0 64px;
}

.pagination-btn {
  padding: 10px 20px;
  background: rgba(59, 130, 246, 0.1);
  border: 1px solid rgba(59, 130, 246, 0.2);
  border-radius: 8px;
  color: rgba(147, 197, 253, 0.8);
  font-size: 0.9rem;
  cursor: pointer;
  transition: all 0.3s ease;
}

.pagination-btn:hover:not(:disabled) {
  background: rgba(59, 130, 246, 0.2);
  border-color: rgba(59, 130, 246, 0.4);
  color: white;
  transform: translateY(-1px);
}

.pagination-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.pagination-pages {
  display: flex;
  gap: 8px;
}

.pagination-page {
  width: 40px;
  height: 40px;
  background: rgba(59, 130, 246, 0.1);
  border: 1px solid rgba(59, 130, 246, 0.2);
  border-radius: 8px;
  color: rgba(147, 197, 253, 0.8);
  font-size: 0.9rem;
  cursor: pointer;
  transition: all 0.3s ease;
}

.pagination-page:hover {
  background: rgba(59, 130, 246, 0.2);
  border-color: rgba(59, 130, 246, 0.4);
  color: white;
}

.pagination-page.active {
  background: rgba(59, 130, 246, 0.3);
  border-color: rgba(59, 130, 246, 0.5);
  color: white;
  box-shadow: 0 2px 8px rgba(59, 130, 246, 0.3);
}

.pagination-ellipsis {
  color: rgba(147, 197, 253, 0.5);
  padding: 0 8px;
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
