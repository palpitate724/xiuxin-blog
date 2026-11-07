<template>
  <!-- 标签管理页面 -->
  <div class="page-tag">
    <div class="page-header">
      <h1 class="page-title">标签管理</h1>
      <p class="page-desc">管理博客文章的标签</p>
    </div>

    <!-- 操作栏 -->
    <div class="page-toolbar">
      <button class="btn-primary" @click="openAddModal">
        <Plus :size="16" />
        新建标签
      </button>
      <input
        v-model="searchKeyword"
        class="search-input"
        :placeholder="'搜索标签...'"
        @input="onSearch"
      />
      <span class="page-count">{{ filteredList.length }} 项</span>
    </div>

    <!-- 标签云 + 列表双视图 -->
    <div class="tag-layout">
      <!-- 标签云 -->
      <div class="tag-cloud-card admin-card">
        <div class="admin-card__header">
          <span class="admin-card__title">标签云</span>
        </div>
        <div class="admin-card__body admin-card__body--cloud">
          <span
            v-for="tag in sortedByCount"
            :key="tag.id"
            class="cloud-tag"
            :class="`size-${tag.articleCount}`"
            :title="`${tag.name} · ${tag.articleCount} 篇文章`"
          >{{ tag.name }}</span>
          <div v-if="sortedByCount.length === 0" class="cloud-empty">暂无标签</div>
        </div>
      </div>

      <!-- 标签列表 -->
      <div class="tag-list-card admin-card">
        <div class="admin-card__header">
          <span class="admin-card__title">标签列表</span>
        </div>
        <div class="tag-list">
          <div v-for="tag in paginatedList" :key="tag.id" class="tag-row">
            <div class="tag-row__info">
              <span class="tag-row__badge">{{ tag.name }}</span>
              <span class="tag-row__count">{{ tag.articleCount }} 篇文章</span>
            </div>
            <div class="tag-row__actions">
              <button class="action-btn action-btn--edit" @click="openEditModal(tag)">编辑</button>
              <button class="action-btn action-btn--delete" @click="handleDelete(tag)">删除</button>
            </div>
          </div>
          <div v-if="filteredList.length === 0" class="empty-state">
            <TagIcon :size="40" class="empty-state__icon" />
            <p>{{ searchKeyword ? '暂无匹配标签' : '暂无标签' }}</p>
            <p class="empty-state__hint">{{ searchKeyword ? '尝试其他关键词' : '点击「新建标签」开始添加' }}</p>
          </div>
        </div>
        <!-- 分页 -->
        <div v-if="totalPages > 1" class="pagination">
          <button class="pagination__btn" :disabled="currentPage <= 1" @click="prevPage">
            <ChevronLeft :size="16" />
          </button>
          <span v-for="page in totalPages" :key="page"
                class="pagination__number" :class="{ active: currentPage === page }"
                @click="goToPage(page)">{{ page }}</span>
          <button class="pagination__btn" :disabled="currentPage >= totalPages" @click="nextPage">
            <ChevronRight :size="16" />
          </button>
        </div>
      </div>
    </div>

    <!-- 新增/编辑弹窗 -->
    <Teleport to="body">
      <div v-if="showModal" class="modal-overlay" @click.self="closeModal">
        <div class="modal">
          <div class="modal__header">
            <span class="modal__title">{{ isEditing ? '编辑标签' : '新建标签' }}</span>
            <button class="modal__close" @click="closeModal" aria-label="关闭">
              <X :size="16" />
            </button>
          </div>
          <div class="modal__body">
            <div class="form-group">
              <label class="form-label">标签名称</label>
              <input
                v-model="formName"
                class="form-input"
                placeholder="请输入标签名称"
                maxlength="20"
                @keydown.enter="handleSubmit"
              />
            </div>
          </div>
          <div class="modal__footer">
            <button class="btn-ghost" @click="closeModal">取消</button>
            <button class="btn-primary" @click="handleSubmit">{{ isEditing ? '保存' : '创建' }}</button>
          </div>
        </div>
      </div>
    </Teleport>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import { tagpage } from '../../stores/tag'
import { Plus, Tag as TagIcon, X, ChevronLeft, ChevronRight } from '@lucide/vue'

/** Pinia Store 实例：标签管理模块，提供 taglist 数据和 gettag() 方法 */
const tagpage_v = tagpage()

// ─── 状态变量 ──────────────────────────────────────────────────────────────────
/** 搜索关键词，用于过滤标签列表；为空时显示全部 */
const searchKeyword = ref('')
/** 当前页码，从 1 开始计数 */
const currentPage = ref(1)
/** 每页显示条数，固定为 10 */
const PAGE_SIZE = 10

/** 控制新增/编辑弹窗是否可见 */
const showModal = ref(false)
/** 标记当前弹窗处于编辑模式（true）还是新建模式（false） */
const isEditing = ref(false)
/** 正在编辑的标签 ID，用于定位要更新的数据项 */
const editingId = ref('')
/** 弹窗中标签名称的输入绑定 */
const formName = ref('')

// ─── 模拟数据（后续替换为 tagpage_v.taglist）────────────────────────────────────
/**
 * 本地标签数据源，开发期用于填充标签云和列表。
 * 每个标签包含：ID、名称、关联文章数量。
 * TODO: 上线后改用 tagpage_v.taglist 动态数据。
 */
const MOCK_TAGS = [
  { id: '1', name: 'Vue',       articleCount: 12 },
  { id: '2', name: 'TypeScript', articleCount: 8 },
  { id: '3', name: 'Spring Boot', articleCount: 15 },
  { id: '4', name: 'Java',      articleCount: 10 },
  { id: '5', name: 'MySQL',     articleCount: 6 },
  { id: '6', name: 'Redis',     articleCount: 4 },
  { id: '7', name: 'Docker',    articleCount: 3 },
  { id: '8', name: 'Git',       articleCount: 7 },
  { id: '9', name: 'Markdown',  articleCount: 5 },
  { id: '10', name: 'CSS',      articleCount: 9 },
  { id: '11', name: 'JavaScript', articleCount: 11 },
  { id: '12', name: 'Node.js',  articleCount: 6 },
]

/** 所有标签的可响应列表（由 MOCK_TAGS 初始化） */
const allTags = ref(MOCK_TAGS)

// ─── 计算属性：过滤 / 排序 / 分页 ──────────────────────────────────────────────
/**
 * 根据搜索关键词过滤标签列表。
 * 匹配标签名称（不区分大小写），关键词为空时返回全量数据。
 */
const filteredList = computed(() => {
  if (!searchKeyword.value.trim()) return allTags.value
  const kw = searchKeyword.value.toLowerCase()
  return allTags.value.filter(t => t.name.toLowerCase().includes(kw))
})

/**
 * 按关联文章数量降序排列，用于标签云的字号映射。
 * 文章数越多的标签在云图中字体越大。
 */
const sortedByCount = computed(() =>
  [...filteredList.value].sort((a, b) => b.articleCount - a.articleCount)
)

/** 总页数，基于过滤后的列表长度计算，最小值为 1 */
const totalPages = computed(() => Math.max(1, Math.ceil(filteredList.value.length / PAGE_SIZE)))

/**
 * 根据当前页码和 PAGE_SIZE 截取对应页的数据子集。
 * 公式：start = (currentPage - 1) * PAGE_SIZE
 */
const paginatedList = computed(() => {
  const start = (currentPage.value - 1) * PAGE_SIZE
  return filteredList.value.slice(start, start + PAGE_SIZE)
})

// ─── 搜索时重置分页 ────────────────────────────────────────────────────────────
/** 搜索关键词变化时自动将页码重置为 1，防止翻页后搜索时停留在空页面 */
watch(searchKeyword, () => { currentPage.value = 1 })

// ─── 弹窗控制 ──────────────────────────────────────────────────────────────────
/** 打开新增弹窗，重置表单状态并标记为非编辑模式 */
const openAddModal = () => {
  isEditing.value = false
  editingId.value = ''
  formName.value = ''
  showModal.value = true
}

/**
 * 打开编辑弹窗，用传入标签对象的数据初始化表单
 * @param tag 要编辑的标签对象
 */
const openEditModal = (tag: { id: string; name: string }) => {
  isEditing.value = true
  editingId.value = tag.id
  formName.value = tag.name
  showModal.value = true
}

/** 关闭弹窗，不保存任何更改 */
const closeModal = () => { showModal.value = false }

// ─── 提交（预留接入真实 API）──────────────────────────────────────────────────
/**
 * 处理标签的新建或更新操作。
 * - 编辑模式：在 allTags 中查找对应 ID 并更新名称（模拟）
 * - 新建模式：生成新 ID，articleCount 初始为 0，追加到列表末尾（模拟）
 * 操作完成后关闭弹窗。
 */
const handleSubmit = async () => {
  const name = formName.value.trim()
  if (!name) { alert('请输入标签名称'); return }

  if (isEditing.value) {
    // TODO: await updateTag(editingId.value, { tagname: name })
    const idx = allTags.value.findIndex(t => t.id === editingId.value)
    if (idx !== -1) allTags.value[idx].name = name
    alert('标签已更新（模拟）')
  } else {
    // TODO: const res = await addTag({ tagname: name })
    allTags.value.push({ id: String(Date.now()), name, articleCount: 0 })
    alert('标签已创建（模拟）')
  }
  closeModal()
}

// ─── 删除（预留接入真实 API）──────────────────────────────────────────────────
/**
 * 删除指定标签。
 * 弹出浏览器原生确认对话框，用户确认后才从列表中移除对应项。
 * @param tag 要删除的标签对象（需要 id 和 name 用于确认提示）
 */
const handleDelete = async (tag: { id: string; name: string }) => {
  if (!confirm(`确定要删除标签「${tag.name}」吗？`)) return
  // TODO: await deleteTag(tag.id)
  allTags.value = allTags.value.filter(t => t.id !== tag.id)
}

// ─── 分页导航 ──────────────────────────────────────────────────────────────────
/** 搜索触发时重置到第一页（与 watch 作用相同，供模板直接绑定） */
const onSearch   = () => { currentPage.value = 1 }
/**
 * 跳转到指定页码，自动限制在合法范围 [1, totalPages] 内
 * @param p 目标页码
 */
const goToPage   = (p: number) => { currentPage.value = Math.max(1, Math.min(totalPages.value, p)) }
/** 上一页：当不在第一页时页码减 1 */
const prevPage   = () => { if (currentPage.value > 1) currentPage.value-- }
/** 下一页：当不在最后一页时页码加 1 */
const nextPage   = () => { if (currentPage.value < totalPages.value) currentPage.value++ }
</script>

<style scoped>
.page-tag {
  width: 100%;
  max-width: var(--layout-max-width);
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: var(--spacing-lg);
}

/* ── 页头 ── */
.page-title {
  font-size: var(--font-size-xl3);
  font-weight: var(--font-weight-bold);
  color: var(--color-text-primary);
  letter-spacing: -0.02em;
}
.page-desc { margin-top: var(--spacing-xs); font-size: var(--font-size-sm); color: var(--color-text-secondary); }

/* ── 工具栏 ── */
.page-toolbar {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
}
.search-input {
  flex: 1;
  max-width: 220px;
  padding: 8px 12px;
  background: var(--color-bg-tertiary);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
  font-size: var(--font-size-sm);
  color: var(--color-text-primary);
  outline: none;
  transition: border-color var(--duration-fast) var(--ease-standard),
              box-shadow var(--duration-fast) var(--ease-standard);
}
.search-input::placeholder { color: var(--color-text-muted); }
.search-input:focus {
  border-color: var(--color-accent);
  box-shadow: 0 0 0 3px var(--color-accent-glow-soft);
}
.page-count { font-size: var(--font-size-xs); color: var(--color-text-muted); white-space: nowrap; }

/* ── 双栏布局 ── */
.tag-layout {
  display: grid;
  grid-template-columns: 1fr 1.4fr;
  gap: var(--spacing-lg);
  align-items: start;
}

/* ── 标签云 ── */
.admin-card__body--cloud {
  display: flex;
  flex-wrap: wrap;
  gap: var(--spacing-sm);
  padding: var(--spacing-lg);
  min-height: 120px;
  align-content: flex-start;
}
.cloud-tag {
  padding: 4px 12px;
  border-radius: var(--radius-full);
  background: var(--color-bg-secondary);
  border: 1px solid var(--color-border);
  color: var(--color-text-secondary);
  font-size: 0.8rem;
  cursor: default;
  transition: all var(--duration-fast) var(--ease-standard);
  white-space: nowrap;
}
.cloud-tag:hover {
  border-color: var(--color-border-strong);
  background: var(--color-surface-hover);
  color: var(--color-text-primary);
}
/* 字号随文章数变化 */
.cloud-tag.size-5 { font-size: 0.7rem; }
.cloud-tag.size-4 { font-size: 0.78rem; }
.cloud-tag.size-3 { font-size: 0.87rem; }
.cloud-tag.size-2 { font-size: 0.95rem; }
.cloud-tag.size-1 { font-size: 1.05rem; font-weight: var(--font-weight-semibold); }
.cloud-empty { color: var(--color-text-muted); font-size: var(--font-size-sm); }

/* ── 标签列表 ── */
.tag-list {
  display: flex;
  flex-direction: column;
}
.tag-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: var(--spacing-sm) var(--spacing-lg);
  border-bottom: 1px solid var(--color-border-divider);
  transition: background-color var(--duration-fast) var(--ease-standard),
              box-shadow var(--duration-fast) var(--ease-standard);
}
.tag-row:last-child { border-bottom: none; }
.tag-row:hover {
  background: var(--color-surface-hover);
  box-shadow: inset 3px 0 0 var(--color-accent);
}

.tag-row__info {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  min-width: 0;
}
.tag-row__badge {
  padding: 2px 10px;
  background: var(--color-bg-secondary);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-full);
  font-size: 0.8rem;
  color: var(--color-text-primary);
  white-space: nowrap;
}
.tag-row__count {
  font-size: var(--font-size-xs);
  color: var(--color-text-muted);
}

/* ── 空状态 ── */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: var(--spacing-xl) 0;
  text-align: center;
  color: var(--color-text-secondary);
}
.empty-state__icon {
  margin-bottom: var(--spacing-md);
  opacity: 0.5;
}
.empty-state__hint { font-size: var(--font-size-xs); color: var(--color-text-muted); margin-top: var(--spacing-sm); }

/* ── 按钮 ── */
.action-btn {
  padding: 4px 10px;
  border-radius: var(--radius-sm);
  font-size: var(--font-size-xs);
  font-weight: var(--font-weight-medium);
  cursor: pointer;
  border: 1px solid transparent;
  transition: all var(--duration-fast) var(--ease-standard);
}
.action-btn--edit {
  background: transparent;
  color: var(--color-accent);
  border-color: var(--color-accent);
}
.action-btn--edit:hover { background: var(--color-accent-soft); }
.action-btn--delete {
  background: transparent;
  color: var(--color-error);
  border-color: var(--color-error-border);
}
.action-btn--delete:hover { background: var(--color-error-soft); }

/* ── 弹窗 ── */
.modal-overlay {
  position: fixed;
  inset: 0;
  z-index: var(--z-modal);
  background: rgba(0, 0, 0, 0.35);
  backdrop-filter: blur(4px);
  display: flex;
  align-items: center;
  justify-content: center;
  animation: modalFadeIn 0.2s ease;
}

@keyframes modalFadeIn {
  from { opacity: 0; }
  to   { opacity: 1; }
}

.modal {
  width: 90%;
  max-width: 440px;
  background: var(--color-surface);
  border: 1px solid var(--color-border-divider);
  border-radius: var(--radius-xl);
  box-shadow: var(--color-shadow-lg);
  overflow: hidden;
  animation: modalSlideIn 0.25s cubic-bezier(0.34, 1.56, 0.64, 1);
}

@keyframes modalSlideIn {
  from { opacity: 0; transform: scale(0.95) translateY(8px); }
  to   { opacity: 1; transform: scale(1) translateY(0); }
}
.modal__header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: var(--spacing-md) var(--spacing-lg);
  border-bottom: 1px solid var(--color-border-divider);
}
.modal__title {
  font-size: var(--font-size-md);
  font-weight: var(--font-weight-semibold);
  color: var(--color-text-primary);
}
.modal__close {
  width: 28px;
  height: 28px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: var(--radius-sm);
  color: var(--color-text-muted);
  cursor: pointer;
  transition: background-color var(--duration-fast) var(--ease-standard);
}
.modal__close:hover { background: var(--color-bg-secondary); }
.modal__body { padding: var(--spacing-lg); }
.modal__footer {
  display: flex;
  justify-content: flex-end;
  gap: var(--spacing-sm);
  padding: var(--spacing-md) var(--spacing-lg);
  border-top: 1px solid var(--color-border-divider);
}

/* ── 表单 ── */
.form-group { display: flex; flex-direction: column; gap: var(--spacing-xs); }
.form-label {
  font-size: var(--font-size-xs);
  font-weight: var(--font-weight-medium);
  color: var(--color-text-secondary);
}
.form-input {
  padding: 10px 14px;
  background: var(--color-bg-tertiary);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
  font-size: var(--font-size-base);
  color: var(--color-text-primary);
  outline: none;
  transition: border-color var(--duration-fast) var(--ease-standard),
              box-shadow var(--duration-fast) var(--ease-standard);
}
.form-input::placeholder { color: var(--color-text-muted); }
.form-input:focus {
  border-color: var(--color-accent);
  box-shadow: 0 0 0 3px var(--color-accent-glow-soft);
}

/* ── 通用按钮 ── */
.btn-primary, .btn-ghost {
  display: inline-flex;
  align-items: center;
  gap: var(--spacing-xs);
  padding: 8px 16px;
  border-radius: var(--radius-md);
  font-size: var(--font-size-sm);
  font-weight: var(--font-weight-medium);
  cursor: pointer;
  transition: all var(--duration-fast) var(--ease-standard);
}
.btn-primary {
  background: var(--color-accent);
  color: var(--color-white);
  border: 1px solid var(--color-accent);
  box-shadow: var(--color-shadow-sm);
}
.btn-primary:hover { background: var(--color-accent-hover); box-shadow: 0 2px 8px var(--color-accent-glow-soft); }
.btn-ghost {
  background: transparent;
  color: var(--color-text-secondary);
  border: 1px solid var(--color-border);
}
.btn-ghost:hover { color: var(--color-text-primary); border-color: var(--color-border-strong); background: var(--color-surface-hover); }

/* ── 分页 ── */
.pagination {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: var(--spacing-xs);
  padding: var(--spacing-md) 0;
}
.pagination__btn, .pagination__number {
  display: flex;
  align-items: center;
  justify-content: center;
  min-width: 36px;
  height: 36px;
  border: 1px solid var(--color-border);
  border-radius: var(--radius-sm);
  font-size: var(--font-size-sm);
  cursor: pointer;
  transition: all var(--duration-fast) var(--ease-standard);
}
.pagination__btn { background: var(--color-bg-secondary); color: var(--color-text-primary); }
.pagination__btn:hover:not(:disabled) { background: var(--color-surface-hover); border-color: var(--color-border-strong); }
.pagination__btn:disabled { cursor: not-allowed; opacity: 0.4; }
.pagination__number { background: var(--color-bg-tertiary); color: var(--color-text-secondary); padding: 0 var(--spacing-xs); }
.pagination__number:hover { background: var(--color-bg-secondary); color: var(--color-text-primary); border-color: var(--color-border-strong); }
.pagination__number.active { background: var(--color-accent); color: var(--color-white); border-color: var(--color-accent); font-weight: var(--font-weight-medium); }

/* ── 响应式 ── */
@media (max-width: 900px) {
  .tag-layout { grid-template-columns: 1fr; }
}
</style>
