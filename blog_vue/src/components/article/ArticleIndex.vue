<template>
  <div class="article-index">
    <h3 class="index-title">文章目录</h3>
    <ul class="index-list">
      <li
        v-for="item in flattenedIndices"
        :key="item.id"
        :class="['index-item', `level-${item.level}`]"
        :style="{ paddingLeft: `${item.level * 16 + 8}px` }"
      >
        <a
          :href="`#${item.id}`"
          @click.prevent="scrollToHeading(item.id)"
          class="index-link"
        >
          {{ item.text }}
        </a>
      </li>
    </ul>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { parseArticleIndex, parseArticleIndexFromHtml, flattenIndices } from '@/utils/markdown'

const props = defineProps<{
  content: string // markdown文本或HTML文本
  isHtml?: boolean // 是否为HTML文本，默认为false
}>()

// 解析文章索引
const indices = computed(() => {
  if (props.isHtml) {
    return parseArticleIndexFromHtml(props.content)
  } else {
    return parseArticleIndex(props.content)
  }
})

// 扁平化索引用于渲染
const flattenedIndices = computed(() => {
  return flattenIndices(indices.value)
})

// 滚动到指定标题
const scrollToHeading = (id: string) => {
  const element = document.getElementById(id)
  if (element) {
    element.scrollIntoView({ behavior: 'smooth', block: 'start' })
  }
}
</script>

<style scoped>
.article-index {
  background: var(--color-surface);
  border: 1px solid var(--color-border-dim);
  border-radius: 8px;
  padding: 16px;
  backdrop-filter: blur(10px);
}

.index-title {
  font-size: 1.1rem;
  font-weight: 600;
  color: var(--color-progress-start);
  margin: 0 0 12px 0;
  padding-bottom: 8px;
  border-bottom: 1px solid var(--color-border-dim);
}

.index-list {
  list-style: none;
  margin: 0;
  padding: 0;
}

.index-item {
  margin: 4px 0;
}

.index-link {
  display: block;
  color: var(--color-accent-lighter);
  text-decoration: none;
  font-size: 0.9rem;
  padding: 4px 8px;
  border-radius: 4px;
  transition: all 0.2s ease;
  line-height: 1.6;
}

.index-link:hover {
  color: var(--color-progress-start);
  background: var(--color-accent-glow);
  transform: translateX(4px);
}

/* 不同级别的标题样式 */
.level-1 .index-link {
  font-weight: 600;
  font-size: 1rem;
}

.level-2 .index-link {
  font-weight: 500;
  font-size: 0.95rem;
}

.level-3 .index-link {
  font-weight: 400;
  font-size: 0.9rem;
}

.level-4 .index-link,
.level-5 .index-link,
.level-6 .index-link {
  font-weight: 400;
  font-size: 0.85rem;
  color: var(--color-text-muted);
}
</style>
