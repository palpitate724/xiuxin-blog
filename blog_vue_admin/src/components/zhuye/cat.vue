<template>
  <!-- 分类管理页面 -->
  <div class="page-cat">
    <div class="page-header">
      <h1 class="page-title">分类管理</h1>
      <p class="page-desc">管理博客文章的分类</p>
    </div>

    <!-- 操作栏 -->
    <div class="page-toolbar">
      <button class="btn-primary" @click="openAddModal">
        <Plus :size="16" />
        新建分类
      </button>
      <input
        v-model="searchKeyword"
        class="search-input"
        :placeholder="'搜索分类...'"
        @input="onSearch"
      />
      <span class="page-count">{{ filteredList.length }} 项</span>
    </div>

    <!-- 分类列表 -->
    <div class="cat-list">
      <div v-for="cat in paginatedList" :key="cat.id" class="cat-row">
        <div class="cat-row__main">
          <span class="cat-row__name">{{ cat.name }}</span>
          <span class="cat-row__id">#{{ cat.id }}</span>
        </div>
        <div class="cat-row__meta">
          <span class="cat-row__time">{{ cat.cjiantime }}</span>
          <div class="cat-row__actions">
            <button class="action-btn action-btn--edit" @click="openEditModal(cat)">编辑</button>
            <button class="action-btn action-btn--delete" @click="handleDelete(cat)">删除</button>
          </div>
        </div>
      </div>

      <div v-if="filteredList.length === 0" class="empty-state">
        <FolderOpen :size="40" class="empty-state__icon" />
        <p>{{ searchKeyword ? '暂无匹配分类' : '暂无分类' }}</p>
        <p class="empty-state__hint">
          {{ searchKeyword ? '尝试其他关键词' : '点击「新建分类」开始添加' }}
        </p>
      </div>
    </div>

    <!-- 分页 -->
    <div v-if="totalPages > 1" class="pagination">
      <button class="pagination__btn" :disabled="currentPage <= 1" @click="prevPage">
        <ChevronLeft :size="16" />
      </button>
      <span
        v-for="page in totalPages"
        :key="page"
        class="pagination__number"
        :class="{ active: currentPage === page }"
        @click="goToPage(page)"
        >{{ page }}</span
      >
      <button class="pagination__btn" :disabled="currentPage >= totalPages" @click="nextPage">
        <ChevronRight :size="16" />
      </button>
    </div>

    <!-- 新增/编辑弹窗 -->
    <Teleport to="body">
      <div v-if="showModal" class="modal-overlay" @click.self="closeModal">
        <div class="modal">
          <div class="modal__header">
            <span class="modal__title">{{ isEditing ? '编辑分类' : '新建分类' }}</span>
            <button class="modal__close" @click="closeModal" aria-label="关闭">
              <X :size="16" />
            </button>
          </div>
          <div class="modal__body">
            <div class="form-group">
              <label class="form-label">分类名称</label>
              <input
                v-model="formName"
                class="form-input"
                placeholder="请输入分类名称"
                maxlength="20"
                @keydown.enter="handleSubmit"
              />
            </div>
          </div>
          <div class="modal__footer">
            <button class="btn-ghost" @click="closeModal">取消</button>
            <button class="btn-primary" @click="handleSubmit">
              {{ isEditing ? '保存' : '创建' }}
            </button>
          </div>
        </div>
      </div>
    </Teleport>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch, onMounted } from 'vue'
import { catpage } from '../../stores/cat'
import { Plus, FolderOpen, X, ChevronLeft, ChevronRight } from '@lucide/vue'

/** Pinia Store 实例：分类管理模块，提供 catlist 数据和 getcat() 方法 */
const catpage_v = catpage()

// ─── 状态变量 ──────────────────────────────────────────────────────────────────
/** 搜索关键词，用于过滤分类列表；为空时显示全部 */
const searchKeyword = ref('')
/** 当前页码，从 1 开始计数 */
const currentPage = ref(1)
/** 每页显示条数，固定为 10 */
const PAGE_SIZE = 10

/** 控制新增/编辑弹窗是否可见 */
const showModal = ref(false)
/** 标记当前弹窗处于编辑模式（true）还是新建模式（false） */
const isEditing = ref(false)
/** 正在编辑的分类 ID，用于定位要更新的数据项 */
const editingId = ref('')
/** 弹窗中分类名称的输入绑定 */
const formName = ref('')

/** 所有分类的可响应列表（由 catpage_v.catlist 初始化） */
const allCats = catpage_v.catlist

// ─── 计算属性：过滤 / 分页 ─────────────────────────────────────────────────────
/**
 * 根据搜索关键词过滤分类列表。
 * 匹配分类名称（不区分大小写），关键词为空时返回全量数据。
 */
const filteredList = computed(() => {
  if (!searchKeyword.value.trim()) return allCats
  const kw = searchKeyword.value.toLowerCase()
  return allCats.filter((c) => c.name.toLowerCase().includes(kw))
})

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
/** 搜索关键词变化时自动将页码重置为 1，避免翻页后搜索时停留在空页面 */
watch(searchKeyword, () => {
  currentPage.value = 1
})

// ─── 弹窗控制 ──────────────────────────────────────────────────────────────────
/** 打开新增弹窗，重置表单状态并标记为非编辑模式 */
const openAddModal = () => {
  isEditing.value = false
  editingId.value = ''
  formName.value = ''
  showModal.value = true
}

/**
 * 打开编辑弹窗，用传入分类对象的数据初始化表单
 * @param cat 要编辑的分类对象
 */
const openEditModal = (cat: { id: string; name: string; cjiantime: string }) => {
  isEditing.value = true
  editingId.value = cat.id
  formName.value = cat.name
  showModal.value = true
}

/** 关闭弹窗，不保存任何更改 */
const closeModal = () => {
  showModal.value = false
}

// ─── 提交（预留接入真实 API）──────────────────────────────────────────────────
/**
 * 处理分类的新建或更新操作。
 * - 编辑模式：在 allCats 中查找对应 ID 并更新名称（模拟）
 * - 新建模式：生成新 ID 并追加到列表头部（模拟）
 * 操作完成后关闭弹窗。
 */
const handleSubmit = async () => {
  const name = formName.value.trim()
  if (!name) {
    alert('请输入分类名称')
    return
  }

  if (isEditing.value) {
    // TODO: await updateCat(editingId.value, { catname: name })
    const idx = allCats.findIndex((c) => c.id === editingId.value)
    if (idx !== -1 && allCats[idx]) {
      allCats[idx].name = name
    }
    alert('分类已更新（模拟）')
  } else {
    // TODO: const res = await addCat({ catname: name })
    const newId = String(Date.now())
    allCats.unshift({ id: newId, name: name, cjiantime: new Date().toISOString() })
    catpage_v.setcat(name)
  }
  closeModal()
}

// ─── 删除（预留接入真实 API）──────────────────────────────────────────────────
/**
 * 删除指定分类。
 * 弹出浏览器原生确认对话框，用户确认后才从列表中移除对应项。
 * @param cat 要删除的分类对象（仅需要 id 字段）
 */
const handleDelete = (cat: { id: string; name?: string }) => {
  if (!confirm(`确定要删除分类「${cat.name}」吗？`)) return
  catpage_v.deletecat(cat.id)
  // ...
}

// ─── 分页导航 ──────────────────────────────────────────────────────────────────
/** 搜索触发时重置到第一页（与 watch 作用相同，供模板直接绑定） */
const onSearch = () => {
  currentPage.value = 1
}
/**
 * 跳转到指定页码，自动限制在合法范围 [1, totalPages] 内
 * @param p 目标页码
 */
const goToPage = (p: number) => {
  currentPage.value = Math.max(1, Math.min(totalPages.value, p))
}
/** 上一页：当不在第一页时页码减 1 */
const prevPage = () => {
  if (currentPage.value > 1) currentPage.value--
}
/** 下一页：当不在最后一页时页码加 1 */
const nextPage = () => {
  if (currentPage.value < totalPages.value) currentPage.value++
}
onMounted(() => {
  catpage_v.getcat()
})
</script>

<style scoped>
.page-cat {
  width: 100%;
  max-width: var(--layout-max-width);
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: var(--spacing-lg);
}

/* ── 页头 ── */
.page-header {
}
.page-title {
  font-size: var(--font-size-xl3);
  font-weight: var(--font-weight-bold);
  color: var(--color-text-primary);
  letter-spacing: -0.02em;
}
.page-desc {
  margin-top: var(--spacing-xs);
  font-size: var(--font-size-sm);
  color: var(--color-text-secondary);
}

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
  transition:
    border-color var(--duration-fast) var(--ease-standard),
    box-shadow var(--duration-fast) var(--ease-standard);
}
.search-input::placeholder {
  color: var(--color-text-muted);
}
.search-input:focus {
  border-color: var(--color-accent);
  box-shadow: 0 0 0 3px var(--color-accent-glow-soft);
}
.page-count {
  font-size: var(--font-size-xs);
  color: var(--color-text-muted);
  white-space: nowrap;
}

/* ── 列表 ── */
.cat-list {
  display: flex;
  flex-direction: column;
  background: var(--color-surface);
  border: 1px solid var(--color-border-divider);
  border-radius: var(--radius-lg);
  overflow: hidden;
}
.cat-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: var(--spacing-md) var(--spacing-lg);
  border-bottom: 1px solid var(--color-border-divider);
  transition:
    background-color var(--duration-fast) var(--ease-standard),
    box-shadow var(--duration-fast) var(--ease-standard),
    transform var(--duration-fast) var(--ease-standard);
}
.cat-row:last-child {
  border-bottom: none;
}
.cat-row:hover {
  background: var(--color-surface-hover);
  box-shadow: inset 3px 0 0 var(--color-accent);
}

.cat-row__main {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
}
.cat-row__name {
  font-size: var(--font-size-md);
  font-weight: var(--font-weight-medium);
  color: var(--color-text-primary);
}
.cat-row__id {
  font-size: var(--font-size-xs);
  color: var(--color-text-muted);
  font-family: monospace;
}
.cat-row__meta {
  display: flex;
  align-items: center;
  gap: var(--spacing-lg);
}
.cat-row__time {
  font-size: var(--font-size-xs);
  color: var(--color-text-muted);
  white-space: nowrap;
}
.cat-row__actions {
  display: flex;
  gap: var(--spacing-sm);
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
.empty-state__hint {
  font-size: var(--font-size-xs);
  color: var(--color-text-muted);
  margin-top: var(--spacing-sm);
}

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
.action-btn--edit:hover {
  background: var(--color-accent-soft);
}
.action-btn--delete {
  background: transparent;
  color: var(--color-error);
  border-color: var(--color-error-border);
}
.action-btn--delete:hover {
  background: var(--color-error-soft);
}

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
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
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
  from {
    opacity: 0;
    transform: scale(0.95) translateY(8px);
  }
  to {
    opacity: 1;
    transform: scale(1) translateY(0);
  }
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
.modal__close:hover {
  background: var(--color-bg-secondary);
}
.modal__body {
  padding: var(--spacing-lg);
}
.modal__footer {
  display: flex;
  justify-content: flex-end;
  gap: var(--spacing-sm);
  padding: var(--spacing-md) var(--spacing-lg);
  border-top: 1px solid var(--color-border-divider);
}

/* ── 表单 ── */
.form-group {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-xs);
}
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
  transition:
    border-color var(--duration-fast) var(--ease-standard),
    box-shadow var(--duration-fast) var(--ease-standard);
}
.form-input::placeholder {
  color: var(--color-text-muted);
}
.form-input:focus {
  border-color: var(--color-accent);
  box-shadow: 0 0 0 3px var(--color-accent-glow-soft);
}

/* ── 通用按钮 ── */
.btn-primary,
.btn-ghost {
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
.btn-primary:hover {
  background: var(--color-accent-hover);
  box-shadow: 0 2px 8px var(--color-accent-glow-soft);
}
.btn-ghost {
  background: transparent;
  color: var(--color-text-secondary);
  border: 1px solid var(--color-border);
}
.btn-ghost:hover {
  color: var(--color-text-primary);
  border-color: var(--color-border-strong);
  background: var(--color-surface-hover);
}

/* ── 分页 ── */
.pagination {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: var(--spacing-xs);
  padding: var(--spacing-md) 0;
}
.pagination__btn,
.pagination__number {
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
.pagination__btn {
  background: var(--color-bg-secondary);
  color: var(--color-text-primary);
}
.pagination__btn:hover:not(:disabled) {
  background: var(--color-surface-hover);
  border-color: var(--color-border-strong);
}
.pagination__btn:disabled {
  cursor: not-allowed;
  opacity: 0.4;
}
.pagination__number {
  background: var(--color-bg-tertiary);
  color: var(--color-text-secondary);
  padding: 0 var(--spacing-xs);
}
.pagination__number:hover {
  background: var(--color-bg-secondary);
  color: var(--color-text-primary);
  border-color: var(--color-border-strong);
}
.pagination__number.active {
  background: var(--color-accent);
  color: var(--color-white);
  border-color: var(--color-accent);
  font-weight: var(--font-weight-medium);
}
</style>
