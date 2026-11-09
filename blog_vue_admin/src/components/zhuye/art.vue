<template>
  <!-- 文章管理页面: 列表视图 ↔ 编辑视图 -->
  <div class="page-article">
    <!-- ── 列表视图 ── -->
    <template v-if="!isEditing">
      <div class="article-header">
        <h1 class="article-title">{{ '文章管理' }}</h1>
        <p class="article-desc">{{ '管理博客文章的增删改查' }}</p>
      </div>

      <!-- 操作栏 -->
      <div class="article-toolbar">
        <button class="btn-primary" @click="switchToEditor(null)">
          <Plus :size="16" />
          {{ '新建文章' }}
        </button>
        <input v-model="searchKeyword" class="search-input" :placeholder="'搜索文章...'" />
      </div>

      <!-- 文章列表 -->
      <div class="article-list">
        <div
          v-for="article in artpage_v.artlist"
          :key="article.id"
          class="article-row"
          @click="switchToEditor(article.id)"
        >
          <div class="article-row__main">
            <span class="article-row__badge" v-if="article.status === 0">
              <Check :size="12" />
            </span>
            <span class="article-row__badge draft" v-else>
              <MoreHorizontal :size="12" />
            </span>
            <span class="article-row__title">{{ article.name }}</span>
          </div>
          <div class="article-row__meta">
            <span class="article-row__category">{{ article.catid }}</span>
            <span class="article-row__tags" v-for="tag in article.tagvolist" :key="tag.id">
              {{ tag.name }}
            </span>
            <span class="article-row__time">{{ formatRelativeTime(article.uptime) }}</span>
          </div>
        </div>

        <div v-if="filteredArticles.length === 0" class="article-empty">
          <Inbox :size="32" class="article-empty__icon" />
          <p>{{ '暂无文章' }}</p>
        </div>
      </div>

      <!-- 分页控件 -->
      <div v-if="filteredArticles.length > 0" class="pagination">
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
    </template>

    <!-- ── 编辑视图 ── -->
    <template v-else>
      <div class="editor-header">
        <button class="btn-text" @click="switchToEditor(null)">
          <ChevronLeft :size="16" />
          {{ '返回' }}
        </button>
        <div class="editor-header__actions">
          <button class="btn-ghost" @click="previewOnly = !previewOnly">
            <EyeOff v-if="previewOnly" :size="14" />
            <Eye v-else :size="14" />
            {{ previewOnly ? '编辑' : '预览' }}
          </button>
          <button class="btn-ghost" @click="saveDraft">{{ '保存草稿' }}</button>
          <button class="btn-primary" @click="publishArticle">
            {{ editingArticle ? '更新' : '发布' }}
          </button>
        </div>
      </div>

      <!-- 文章元信息 -->
      <div class="meta-form">
        <div class="meta-form__row">
          <label class="meta-form__label">{{ '标题' }}</label>
          <input v-model="editForm.name" class="meta-form__input" :placeholder="'请输入文章标题'" />
        </div>
        <div class="meta-form__row meta-form__row--split">
          <div class="meta-form__group">
            <label class="meta-form__label">{{ '分类' }}</label>
            <select v-model="editForm.catid" class="meta-form__select">
              <option v-for="cat in catpage_v.catlist" :key="cat.id" :value="cat.id">
                {{ cat.name }}
              </option>
            </select>
          </div>
          <div class="meta-form__group">
            <label class="meta-form__label">{{ '标签' }}</label>
            <!-- 从后端加载的标签列表（多选） -->
            <div class="tag-checkbox-group" v-if="tagpage_v.taglist.length > 0">
              <label v-for="tag in tagpage_v.taglist" :key="tag.id" class="tag-checkbox-label">
                <input type="checkbox" :value="tag.id" v-model="selectedTagIds" />
                <span>{{ tag.name }}</span>
              </label>
            </div>
            <!-- 后端标签尚未加载时的备选：手动逗号分隔输入 -->
            <input
              v-else
              v-model="tagsText"
              class="meta-form__input"
              :placeholder="'多个标签用逗号分隔'"
            />
          </div>
        </div>

        <!-- 摘要 -->
        <div class="meta-form__row">
          <label class="meta-form__label">{{ '摘要' }}</label>
          <input
            v-model="editForm.sum"
            class="meta-form__input"
            :placeholder="'请输入文章摘要 (最多200字)'"
          />
        </div>

        <!-- 封面图片上传 -->
        <div class="meta-form__row">
          <label class="meta-form__label">{{ '封面图片' }}</label>
          <div
            class="cover-upload"
            :class="{ 'has-image': coverPreview }"
            @dragover.prevent
            @drop.prevent="handleCoverDrop"
          >
            <input
              v-if="!coverPreview"
              type="file"
              accept="image/*"
              class="cover-input"
              @change="handleCoverUpload"
            />
            <template v-if="coverPreview">
              <img :src="coverPreview" class="cover-preview" alt="封面预览" />
              <button class="cover-clear" title="清除封面" @click.stop="clearCover">
                <X :size="14" />
              </button>
            </template>
            <template v-else>
              <Image :size="20" class="cover-icon" />
              <button class="cover-btn" type="button" @click="triggerCoverUpload">
                {{ '选择封面图片' }}
              </button>
            </template>
          </div>
        </div>
      </div>

      <!-- Markdown 编辑器 -->
      <div class="md-editor" :class="{ 'preview-only': previewOnly }">
        <!-- 工具栏 -->
        <div class="md-toolbar">
          <div class="md-toolbar__left">
            <button
              v-for="btn in toolbarButtons"
              :key="btn.command"
              :title="btn.title"
              class="md-toolbar__btn"
              @click="insertMarkdown(btn)"
            >
              <component :is="btn.iconComponent" :size="16" />
            </button>
          </div>
          <div class="md-toolbar__right">
            <button
              class="md-toolbar__btn"
              :class="{ active: showPreview }"
              :title="'切换 Markdown 预览'"
              @click="showPreview = !showPreview"
            >
              {{ showPreview ? '编辑' : '预览' }}
            </button>
            <button
              class="md-toolbar__btn"
              :class="{ active: showCodeHighlight }"
              :title="'代码语法高亮 (实时)'"
              @click="showCodeHighlight = !showCodeHighlight"
            >
              <Palette :size="16" />
            </button>
          </div>
        </div>

        <!-- 编辑区 + 预览/代码高亮区 -->
        <div
          class="md-editor__body"
          :class="{ 'has-preview': (showPreview || showCodeHighlight) && !previewOnly }"
        >
          <!-- 编辑区 -->
          <div class="md-editor__pane" :class="{ hidden: previewOnly }">
            <textarea
              v-model="editForm.cont"
              class="md-editor__textarea"
              :placeholder="'请输入 Markdown 内容...'"
              @keydown="handleTextareaKeydown"
            ></textarea>
          </div>

          <!-- Markdown 预览区 -->
          <div
            v-if="showPreview && !showCodeHighlight"
            class="md-editor__pane md-editor__pane--preview"
          >
            <div class="md-editor__preview" v-html="renderedHtml"></div>
          </div>

          <!-- 代码语法高亮区 -->
          <div
            v-if="showCodeHighlight && !showPreview"
            class="md-editor__pane md-editor__pane--preview"
          >
            <div class="md-editor__code-preview" v-html="highlightedCode"></div>
          </div>

          <!-- 拖动分隔条 -->
          <div
            v-if="(showPreview || showCodeHighlight) && !previewOnly"
            class="md-editor__resize"
            @mousedown="startResize"
          >
            <div class="md-editor__divider">
              <span class="md-editor__grip"></span>
              <span class="md-editor__grip"></span>
              <span class="md-editor__grip"></span>
            </div>
          </div>
        </div>

        <!-- 底部计数 + 快捷键提示 -->
        <div class="md-editor__footer">
          <div class="md-editor__footer-left">
            <span class="md-editor__count">{{ totalWordCount }} 字</span>
            <span v-if="editForm.cont" class="md-editor__count md-editor__count--draft">
              {{ Math.ceil(totalWordCount / 200) }} 页
            </span>
          </div>
          <span class="md-editor__shortcuts"> Tab 缩进 · Ctrl+B/I/K 格式化 · Ctrl+Enter 发布 </span>
        </div>
      </div>
    </template>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, nextTick, onUnmounted, watch, onMounted } from 'vue'
import MarkdownIt from 'markdown-it'
import hljs from 'highlight.js'
import 'highlight.js/styles/atom-one-light.css'
import {
  Plus,
  ChevronLeft,
  ChevronRight,
  Bold,
  Italic,
  Strikethrough,
  Code,
  CodeSquare,
  Link,
  Image,
  Quote,
  ListOrdered,
  List,
  Heading,
  Table,
  Eye,
  EyeOff,
  Palette,
  Check,
  MoreHorizontal,
  Inbox,
  X,
} from '@lucide/vue'

import { artpage } from '../../stores/art'
import { catpage } from '../../stores/cat'
import { tagpage } from '../../stores/tag'
import { artminio } from '../../stores/artminio'

/**
 * 从分类列表构建分类 ID → 名称 的映射表。
 * 当 catpage_v.catlist 为空时回退到默认的 CATEGORY_FALLBACK。
 * 用途：在文章列表中直接展示分类中文名而非原始 ID。
 */
const CATEGORY_FALLBACK: Record<string, string> = {
  '1': '技术',
  '2': '生活',
  '3': '随笔',
}

const categoryMap = computed<Record<string, string>>(() => {
  const map: Record<string, string> = { '': '默认' }
  catpage_v.catlist.forEach((cat) => {
    if (cat.id && cat.name) map[cat.id] = cat.name
  })
  return { ...CATEGORY_FALLBACK, ...map }
})

// ============================================================================
// 常量 文章状态（供模板和 CRUD 使用）
// ============================================================================
/**
 * ArticleStatus 枚举 — 统一使用字符串表示文章状态，便于模板直接展示。
 * PUBLISHED: 已发布（对应后端 status: 0）
 * DRAFT: 草稿（对应后端 status: 1）
 */
const ArticleStatus = {
  PUBLISHED: 'published' as const, //已发布
  DRAFT: 'draft' as const, //草稿
}

// ============================================================================
// 常量 分类
// ============================================================================
/** Pinia Store 实例：分类管理模块，提供 catlist 数据和 getcat() 方法 */
const catpage_v = catpage()

// ============================================================================
// 常量
// ============================================================================
/** 每页显示的文章数量 */
const PAGE_SIZE = 10
/** 模拟当前登录用户 ID（后续从 userpage store 获取） */
const MOCK_USERID = '2104505781171433473'
/** Tab 键缩进空格数 */
const TAB_SIZE = 4

/** 格式化相对时间（如："3天前"、"昨天"、"今天"） */
const formatRelativeTime = (dateStr: string): string => {
  if (!dateStr) return '未知'
  const date = new Date(dateStr)
  if (Number.isNaN(date.getTime())) return dateStr
  const now = new Date()
  const diffMs = now.getTime() - date.getTime()
  const diffDays = Math.floor(diffMs / 86400000)
  if (diffDays < 0) return dateStr
  if (diffDays === 0) return '今天'
  if (diffDays === 1) return '昨天'
  if (diffDays < 7) return `${diffDays}天前`
  if (diffDays < 30) return `${Math.floor(diffDays / 7)}周前`
  return date.toLocaleDateString('zh-CN', { year: 'numeric', month: 'short', day: 'numeric' })
}

// ============================================================================
// Markdown 编辑器工具栏配置（图标组件已在顶部 @lucide/vue 导入）
// ============================================================================
/**
 * toolbarButtons 数组 — Markdown 编辑器工具栏按钮配置。
 * 每项包含：
 *   command: 触发 insertMarkdown 时的命令标识（对应 switch-case 分支）
 *   title: 按钮 hover 提示文本
 *   iconComponent: @lucide/vue 图标组件（用于 <component :is="..."> 动态渲染）
 */
const toolbarButtons = [
  { command: 'bold', title: '粗体 (Ctrl+B)', iconComponent: Bold },
  { command: 'italic', title: '斜体 (Ctrl+I)', iconComponent: Italic },
  { command: 'strike', title: '删除线', iconComponent: Strikethrough },
  { command: 'inlineCode', title: '行内代码', iconComponent: Code },
  { command: 'codeBlock', title: '代码块', iconComponent: CodeSquare },
  { command: 'link', title: '插入链接', iconComponent: Link },
  { command: 'image', title: '插入图片', iconComponent: Image },
  { command: 'quote', title: '引用', iconComponent: Quote },
  { command: 'listOl', title: '有序列表', iconComponent: ListOrdered },
  { command: 'listUl', title: '无序列表', iconComponent: List },
  { command: 'heading', title: '标题', iconComponent: Heading },
  { command: 'table', title: '插入表格', iconComponent: Table },
]

// ============================================================================
// 工具函数
// ============================================================================
/** 格式化 Date 对象为 YYYY-MM-DD 字符串，用于生成文章 updateTime */
const formatDate = (date: Date) => {
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`
}

// ============================================================================
// markdown-it + highlight.js 实例 (模块作用域创建一次)
// ============================================================================
/**
 * markdown-it 实例：将 Markdown 文本编译为 HTML。
 * 配置项说明：
 *   html: true       — 允许在 Markdown 中使用原始 HTML 标签
 *   linkify: true    — 自动将 URL 文本转为可点击的超链接
 *   typographer: true — 启用排版优化（如 "..." → "…"，"(c)" → "©"）
 *   breaks: false    — 单换行不转为 <br>，遵循标准 Markdown 规则
 *   highlight: ...   — 为代码块提供语法高亮（委托给 highlight.js）
 */
const md = new MarkdownIt({
  html: true,
  linkify: true,
  typographer: true,
  breaks: false,
  highlight: (str: string, lang: string): string => {
    if (lang && hljs.getLanguage(lang)) {
      try {
        return hljs.highlight(str, { language: lang, ignoreIllegals: true }).value
      } catch {
        return ''
      }
    }
    return ''
  },
})

// ============================================================================
// Pinia Store 实例
// ============================================================================
/** 文章 Store 实例：提供 artlist、getartlist、addart 等方法 */
const artpage_v = artpage()
/** 标签 Store 实例：提供 taglist、gettag 方法 */
const tagpage_v = tagpage()

// ============================================================================
// 视图状态
// ============================================================================
/** 当前是否处于编辑视图：false = 文章列表，true = Markdown 编辑器 */
const isEditing = ref(false)
/** 仅预览模式：隐藏编辑器，只显示渲染后的 Markdown 内容 */
const previewOnly = ref(false)
/** 是否显示 Markdown 实时预览面板（与 showCodeHighlight 互斥） */
const showPreview = ref(true)
/** 是否显示代码语法高亮面板（与 showPreview 互斥） */
const showCodeHighlight = ref(false)
/** 当前正在编辑的文章对象；null 表示新建文章 */
const editingArticle = ref()

// ============================================================================
// 文章列表 & 搜索 & 分页
// 数据来源：artpage_v.artlist（后端接口），模板加载时同步刷新
// ============================================================================
/**
 * 将 artpage_v.artlist（后端原始数据）转换为前端 Article 数组。
 * 搜索和分页均基于此计算属性，数据始终来源于 store，不依赖本地数组。
 */
const articlesList = artpage_v.artlist
/** 用户输入的搜索关键词，用于过滤文章列表 */
const searchKeyword = ref('')
/** 当前页码，从 1 开始计数 */
const currentPage = ref(1)

/**
 * 根据搜索关键词过滤文章列表。
 * 匹配范围：标题、标签（逗号拼接后）、分类名称（通过 categoryMap 映射）。
 * 关键词为空时直接返回全量数据。
 */
const filteredArticles = computed(() => {
  if (!searchKeyword.value.trim()) return articlesList
  const kw = searchKeyword.value.toLowerCase()
  return articlesList.filter(
    (a) =>
      a.name.toLowerCase().includes(kw) ||
      (categoryMap.value[a.catid] ?? '').toLowerCase().includes(kw),
  )
})

/** 总页数，基于过滤后的文章数量计算，最小值为 1 */
const totalPages = computed(() => Math.max(1, Math.ceil(filteredArticles.value.length / PAGE_SIZE)))

/**
 * 根据当前页码截取对应页面的文章子集。
 * 公式：start = (currentPage - 1) * PAGE_SIZE
 */
const paginatedArticles = computed(() => {
  const start = (currentPage.value - 1) * PAGE_SIZE
  return filteredArticles.value.slice(start, start + PAGE_SIZE)
})

/** 搜索关键词变化时自动重置到第一页，防止翻页后搜索时停留在空页面 */
watch(searchKeyword, () => {
  currentPage.value = 1
})

/** 编辑器中文本域的字数统计（用于底部显示） */
const totalWordCount = computed(() => editForm.value.cont.length)

// ============================================================================
// 编辑表单
// ============================================================================
/**
 * editForm — 编辑器中当前文章的编辑表单数据。
 * 新建时以默认空值初始化，编辑已有文章时从 article 对象填充。
 * 字段含义：
 *   name       : 文章标题
 *   catid      : 分类 ID（字符串）
 *   objectname : 封面图片 URL
 *   sum        : 文章摘要
 *   cont       : 文章正文（Markdown 原始内容）
 *   tags       : 已选标签 ID 数组（用于提交后端）
 */
const editForm = ref({
  name: '',
  catid: '',
  objectname: '',
  sum: '',
  cont: '',
  tagids: [] as string[],
})

/**
 * tagsText — 标签文本输入的双向绑定计算属性（兼容旧逻辑，保留用于调试输出）。
 * - get: 将 editForm.tags（ID 数组）以逗号+空格连接为字符串
 * - set: 将逗号分隔的字符串拆分为字符串数组，并过滤空项，写回 editForm.tags
 * 注：正式多选模式下由 selectedTagIds 替代此 computed，此处仅作降级兜底。
 */
const tagsText = computed({
  get: () => editForm.value.tagids.join(', '),
  set: (val: string) => {
    editForm.value.tagids = val
      .split(',')
      .map((t) => t.trim())
      .filter(Boolean)
  },
})

// ============================================================================
// 标签多选（从后端获取标签列表后启用）
// ============================================================================
/**
 * selectedTagIds — 已选中的标签 ID 列表，直接绑定到多选框组（v-model）。
 * 初始化为空数组；切换到编辑视图时从 article.tags 同步填充。
 * 与 editForm.tags 双向同步：
 *   - selectedTagIds → watch → editForm.tags
 *   - editForm.tags  ← 由 selectedTagIds 变更驱动
 */
const selectedTagIds = ref<string[]>([])

/**
 * selectedTags — 根据 selectedTagIds 从 tagpage_v.taglist 中查找对应标签名称。
 * 用于在编辑器中显示已选标签的中文名称（如 "Vue" 而非 "1"）。
 * TODO: 后端返回标签对象后直接使用该字段，无需二次查找
 */
const selectedTags = computed<string[]>(() => {
  return selectedTagIds.value
    .map((id) => tagpage_v.taglist.find((t) => t.id === id)?.name ?? id)
    .filter(Boolean)
})

/** 监听 selectedTagIds 变化，同步写回 editForm.tags（保持数据一致性） */
watch(selectedTagIds, (ids) => {
  editForm.value.tagids = ids
})

// ============================================================================
// Markdown 预览 & 高亮
// ============================================================================
const coverPreview = ref('')

const renderedHtml = computed(() => md.render(editForm.value.cont))
const highlightedCode = computed(() => {
  if (!editForm.value.cont) return ''
  try {
    return hljs.highlightAuto(editForm.value.cont).value
  } catch {
    return editForm.value.cont
  }
})

// ============================================================================
// 文章 CRUD
// ============================================================================
/** 发布文章（新建或更新，成功后重新拉取列表） */
const publishArticle = async () => {
  if (!editForm.value.name.trim()) {
    alert('请输入文章标题')
    return
  }
  if (!editForm.value.cont.trim()) {
    alert('请输入文章内容')
    return
  }
  try {
    // TODO: 调用 artpage_v.addart(...) 提交后端
    const aart = {
      name: editForm.value.name.trim(),
      userid: MOCK_USERID,
      catid: String(editForm.value.catid) || '0',
      objectname: editForm.value.objectname,
      sum: editForm.value.sum,
      cont: editForm.value.cont,
      tagids: editForm.value.tagids,
      status: editingArticle.value ? 0 : 0, // TODO: 草稿状态 1
      cjiantime: formatDate(new Date()),
    }
    console.log('[发布] 提交数据:', aart)
    artpage_v.addart(
      aart.name,
      aart.userid,
      aart.catid,
      aart.objectname,
      aart.sum,
      aart.cont,
      aart.tagids,
    )
    alert('文章已发布')
    isEditing.value = false
    editingArticle.value = null
    // 发布成功后重新从后端获取列表
    await artpage_v.getartlist(1, PAGE_SIZE)
  } catch (e: any) {
    alert('发布失败：' + (e?.message || '未知错误'))
  }
}

/** 保存草稿（模拟，预留后端接口接入） */
const saveDraft = async () => {
  if (!editForm.value.name.trim()) {
    alert('请输入文章标题')
    return
  }
  try {
    // TODO: await artpage_v.addart(...)
    console.log('[草稿] 保存成功:', editForm.value)
    alert('草稿已保存')
  } catch (e: any) {
    alert('保存失败：' + (e?.message || '未知错误'))
  }
}

// ============================================================================
// 视图切换 & 导航
// ============================================================================
/**
 * 切换到编辑视图。
 * @param article 要编辑的文章对象；null 表示新建文章
 */
const switchToEditor = async (id: string | null) => {
  if (id) {
    await artpage_v.getartdetail(id)
    if (artpage_v.artdetail === null) {
      alert('未找到指定文章')
      return
    }
    editingArticle.value = artpage_v.artdetail
    coverPreview.value = editingArticle.value.fenmianurl
    console.log('[编辑] 填充表单数据:', artpage_v.artdetail)
    editForm.value = {
      name: editingArticle.value.name,
      catid: editingArticle.value.catid,
      objectname: editingArticle.value.objectname ?? '',
      sum: editingArticle.value.sum ?? '',
      cont: editingArticle.value.cont,
      tagids: [...editingArticle.value.tagvolist],
    }
    // 同步已选标签 ID（用于多选框显示选中状态）
    if (!Array.isArray(editingArticle.value.tags)) {
      editingArticle.value.tags = []
    }
    selectedTagIds.value = [...editingArticle.value.tags]
  } else {
    editingArticle.value = null
    coverPreview.value = ''
    editForm.value = { name: '', catid: '', objectname: '', sum: '', cont: '', tagids: [] }
    selectedTagIds.value = []
  }
  isEditing.value = true
  previewOnly.value = false
  showPreview.value = true
}

// 搜索关键词变化时自动重置到第一页（防止翻页后搜索）
watch(searchKeyword, () => {
  currentPage.value = 1
})

/** 跳转到指定页 */
const goToPage = (page: number) => {
  currentPage.value = Math.max(1, Math.min(totalPages.value, page))
}
/** 上一页 */
const prevPage = () => {
  if (currentPage.value > 1) currentPage.value--
}
/** 下一页 */
const nextPage = () => {
  if (currentPage.value < totalPages.value) currentPage.value++
}

// ============================================================================
// 封面图片上传
// ============================================================================
const artminio_v = artminio()
/** 处理封面图片上传文件选择 */
const handleCoverUpload = async (e: Event) => {
  const input = e.target as HTMLInputElement
  if (!input.files || input.files.length === 0) return
  const file = input.files[0]!
  if (!file.type.startsWith('image/')) {
    alert('请选择图片文件')
    return
  }
  const reader = new FileReader()
  reader.onload = () => {
    const dataUrl = reader.result as string
    coverPreview.value = dataUrl
  }
  reader.readAsDataURL(file)

  await artminio_v.upartminio(file)
  editForm.value.objectname = artminio_v.objectname
}

/** 触发封面上传文件选择对话框 */
const triggerCoverUpload = () => {
  const input = document.querySelector('.cover-input') as HTMLInputElement
  if (input) input.click()
}

/** 处理拖拽上传封面图片 */
const handleCoverDrop = (e: DragEvent) => {
  if (!e.dataTransfer || !e.dataTransfer.files || e.dataTransfer.files.length === 0) return
  const file = e.dataTransfer.files[0]!
  if (!file.type.startsWith('image/')) {
    alert('请选择图片文件')
    return
  }
  const reader = new FileReader()

  reader.onload = () => {
    const dataUrl = reader.result as string
    coverPreview.value = dataUrl
    editForm.value.objectname = dataUrl
  }
  reader.readAsDataURL(file)
}

/** 清除封面图片 */
const clearCover = () => {
  coverPreview.value = ''
  editForm.value.objectname = ''
  const input = document.querySelector('.cover-input') as HTMLInputElement
  if (input) input.value = ''
}

// ============================================================================
// Markdown 编辑器工具栏
// ============================================================================
/** 插入 Markdown 格式化文本到文本域 */
const insertMarkdown = (btn: { command: string; title: string; iconComponent: any }) => {
  const textarea = document.querySelector('.md-editor__textarea') as HTMLTextAreaElement
  if (!textarea) return
  const start = textarea.selectionStart
  const end = textarea.selectionEnd
  const value = editForm.value.cont
  const before = value.slice(0, start)
  const after = value.slice(end)
  const selected = value.slice(start, end)
  let newText = ''
  let cursorOffset = 0

  switch (btn.command) {
    case 'bold':
      newText = `**${selected || '粗体文本'}**`
      cursorOffset = selected ? selected.length + 4 : 4
      break
    case 'italic':
      newText = `*${selected || '斜体文本'}*`
      cursorOffset = selected ? selected.length + 2 : 2
      break
    case 'strike':
      newText = `~~${selected || '删除文本'}~~`
      cursorOffset = selected ? selected.length + 4 : 4
      break
    case 'inlineCode':
      newText = `\`${selected || '行内代码'}\``
      cursorOffset = selected ? selected.length + 2 : 2
      break
    case 'codeBlock': {
      const lang = 'javascript'
      newText = `\`\`\`${lang}\n${selected || '代码'}\n\`\`\``
      cursorOffset = 0
      break
    }
    case 'link':
      newText = `[${selected || '链接文本'}](https://)`
      cursorOffset = selected ? selected.length + 3 : 0
      break
    case 'image':
      newText = `![${selected || '图片描述'}](https://)`
      cursorOffset = selected ? selected.length + 4 : 0
      break
    case 'quote':
      newText = `> ${selected || '请输入引用内容'}`
      cursorOffset = 0
      break
    case 'listOl':
      newText = '1. 列表项\n2. 列表项\n3. 列表项'
      cursorOffset = 0
      break
    case 'listUl':
      newText = '- 列表项\n- 列表项\n- 列表项'
      cursorOffset = 0
      break
    case 'heading':
      newText = `## ${selected || '标题文本'}`
      cursorOffset = 0
      break
    case 'table':
      newText = '| 列名 | 列名 |\n|------|------|\n| 内容 | 内容 |\n| 内容 | 内容 |'
      cursorOffset = 0
      break
    default:
      return
  }

  editForm.value.cont = before + newText + after

  // 设置光标位置
  nextTick(() => {
    const newPos = start + newText.length - cursorOffset
    textarea.setSelectionRange(newPos, newPos)
    textarea.focus()
  })
}

// ============================================================================
// 文本域键盘事件 (Tab / Shift+Tab / Enter / Ctrl+Enter)
// ============================================================================
/**
 * 处理文本域的功能键:
 * - Tab:           缩进 (插入 4 个空格)
 * - Shift+Tab:     反缩进 (移除行首缩进)
 * - Enter:         在列表项中自动续行
 * - Ctrl+Enter:    快捷发布
 */
const handleTextareaKeydown = (e: KeyboardEvent) => {
  const textarea = e.currentTarget as HTMLTextAreaElement
  if (!textarea) return

  const start = textarea.selectionStart
  const end = textarea.selectionEnd
  const value = editForm.value.cont
  const lines = value.split('\n')
  const cursorLine = value.slice(0, start).split('\n').length - 1
  const lineStart =
    value.split('\n').slice(0, cursorLine).join('\n').length + (cursorLine > 0 ? 1 : 0)
  const lineText = lines[cursorLine] || ''

  // —— Tab / Shift+Tab (缩进 / 反缩进) ——
  if (e.key === 'Tab') {
    e.preventDefault()
    if (e.shiftKey) {
      const indentMatch = lineText.match(/^ +/)
      if (indentMatch) {
        const currentIndent = indentMatch[0].length
        const removeSpaces = Math.min(TAB_SIZE, currentIndent)
        const beforeCursor = value.slice(0, start)
        const afterCursor = value.slice(end)
        const newLine = lineText.slice(removeSpaces)
        const newValue = beforeCursor + newLine + afterCursor
        const newCursor = start - removeSpaces
        editForm.value.cont = newValue
        nextTick(() => {
          textarea.setSelectionRange(newCursor, newCursor)
          textarea.focus()
        })
      }
      return
    }
    const beforeCursor = value.slice(0, start)
    const afterCursor = value.slice(end)
    const newValue = beforeCursor + ' '.repeat(TAB_SIZE) + afterCursor
    const newCursor = start + TAB_SIZE
    editForm.value.cont = newValue
    nextTick(() => {
      textarea.setSelectionRange(newCursor, newCursor)
      textarea.focus()
    })
    return
  }

  // —— Enter 在列表项中自动续行 ——
  if (e.key === 'Enter') {
    const listMatch = lineText.match(/^(\s*[-*+]|\s*\d+\.)\s+/)
    if (listMatch) {
      e.preventDefault()
      const indent = listMatch[1]!
      const restOfLine = lineText.slice(listMatch[0].length)
      if (restOfLine.trim() === '') {
        const before = value.slice(0, lineStart)
        const after = value.slice(lineStart + lineText.length + 1)
        editForm.value.cont = before + after
        nextTick(() => {
          textarea.setSelectionRange(lineStart, lineStart)
          textarea.focus()
        })
        return
      }
      const newlineIndent = indent.replace(/\d+/, '1')
      const newValue = value.slice(0, start) + '\n' + newlineIndent + value.slice(end)
      editForm.value.cont = newValue
      nextTick(() => {
        const cursorPos = start + 1 + newlineIndent.length
        textarea.setSelectionRange(cursorPos, cursorPos)
        textarea.focus()
      })
      return
    }
  }

  // —— Ctrl+Enter 快捷发布 ——
  if (e.ctrlKey && e.key === 'Enter') {
    e.preventDefault()
    publishArticle()
    return
  }
}

// ============================================================================
// Markdown 编辑器快捷键 (Ctrl+B / Ctrl+I / Ctrl+K)
// ============================================================================
const handleKeydown = (e: KeyboardEvent) => {
  if (!isEditing.value) return
  const textarea = document.querySelector('.md-editor__textarea') as HTMLTextAreaElement
  if (!textarea || document.activeElement !== textarea) return

  const isMac = navigator.platform.includes('Mac')
  const ctrlOrCmd = isMac ? e.metaKey : e.ctrlKey
  if (!ctrlOrCmd) return

  const map: Record<string, string> = {
    b: 'bold',
    i: 'italic',
    k: 'link',
  }

  if (e.key in map) {
    e.preventDefault()
    insertMarkdown({ command: map[e.key]!, title: '', iconComponent: Bold })
  }
}

// ============================================================================
// Markdown 编辑器分栏拖拽
// ============================================================================
let isResizing = false

const startResize = () => {
  isResizing = true
  document.addEventListener('mousemove', onResize)
  document.addEventListener('mouseup', stopResize)
}

const onResize = (e: MouseEvent) => {
  if (!isResizing) return
  const container = document.querySelector('.md-editor__body') as HTMLElement | null
  if (!container) return
  const rect = container.getBoundingClientRect()
  const percent = ((e.clientX - rect.left) / rect.width) * 100
  const clamped = Math.max(20, Math.min(80, percent))
  container.style.setProperty('--md-split-percent', `${clamped}%`)
}

const stopResize = () => {
  isResizing = false
  document.removeEventListener('mousemove', onResize)
  document.removeEventListener('mouseup', stopResize)
}

// ============================================================================
// 生命周期
// ============================================================================
/** 挂载时加载文章列表、分类列表和标签列表 */
onMounted(() => {
  artpage_v.getartlist(1, PAGE_SIZE)
  catpage_v.getcat()
  tagpage_v.gettag()
})
document.addEventListener('keydown', handleKeydown)

onUnmounted(() => {
  document.removeEventListener('keydown', handleKeydown)
  // 清理拖拽事件
  if (isResizing) {
    document.removeEventListener('mousemove', onResize)
    document.removeEventListener('mouseup', stopResize)
    isResizing = false
  }
})
</script>

<style scoped>
/* =============================================================================
 * art.vue — 文章管理页面 (Markdown 编辑器 + 文章列表)
 * 依赖: markdown-it + highlight.js
 * 设计令牌: src/styles/tokens.css
 * =========================================================================== */

/* ── 页面容器 ── */
.page-article {
  width: 100%;
  max-width: var(--layout-max-width);
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: var(--spacing-lg);
}

/* ── 列表视图 ── */
.article-header {
  margin-bottom: var(--spacing-sm);
}

.article-title {
  font-size: var(--font-size-xl3);
  font-weight: var(--font-weight-bold);
  color: var(--color-text-primary);
  letter-spacing: -0.02em;
  line-height: var(--line-height-tight);
}

.article-desc {
  margin-top: var(--spacing-xs);
  font-size: var(--font-size-sm);
  color: var(--color-text-secondary);
}

/* 操作栏 */
.article-toolbar {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
  margin-bottom: var(--spacing-md);
}

.search-input {
  flex: 1;
  max-width: 260px;
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

/* 文章列表 */
.article-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.article-row {
  padding: var(--spacing-md) var(--spacing-lg);
  background: var(--color-surface);
  border: 1px solid var(--color-border-divider);
  border-radius: var(--radius-lg);
  cursor: pointer;
  transition:
    background-color var(--duration-fast) var(--ease-standard),
    border-color var(--duration-fast) var(--ease-standard),
    box-shadow var(--duration-fast) var(--ease-standard),
    transform var(--duration-fast) var(--ease-out);
}

.article-row:hover {
  background: var(--color-surface-hover);
  border-color: var(--color-border-strong);
  box-shadow: var(--color-shadow-md);
  transform: translateY(-1px);
}

.article-row:active {
  transform: translateY(0);
}

.article-row__main {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
  margin-bottom: 6px;
}

/* 状态徽章 */
.article-row__badge {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 22px;
  height: 22px;
  font-size: 0.75rem;
  color: var(--color-accent);
  border-radius: var(--radius-full);
  background: var(--color-accent-soft);
  flex-shrink: 0;
  font-weight: var(--font-weight-semibold);
  transition: all var(--duration-fast) var(--ease-standard);
}

.article-row__badge:hover {
  transform: scale(1.1);
}

.article-row__badge.draft {
  color: var(--color-text-muted);
  background: var(--color-bg-secondary);
}

.article-row__title {
  font-size: var(--font-size-md);
  font-weight: var(--font-weight-semibold);
  color: var(--color-text-primary);
  flex: 1;
  min-width: 0;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  transition: color var(--duration-fast);
}

.article-row:hover .article-row__title {
  color: var(--color-accent);
}

.article-row__meta {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
  font-size: var(--font-size-xs);
  color: var(--color-text-secondary);
  flex-wrap: wrap;
}

.article-row__category {
  padding: 2px 8px;
  background: var(--color-bg-secondary);
  border-radius: var(--radius-full);
  font-size: 0.75rem;
  color: var(--color-text-secondary);
  white-space: nowrap;
  transition: all var(--duration-fast);
}

.article-row:hover .article-row__category {
  background: var(--color-accent-soft);
  color: var(--color-accent);
}

.article-row__tags {
  flex: 1;
  min-width: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  color: var(--color-text-muted);
  font-size: 0.75rem;
}

.article-row__time {
  white-space: nowrap;
  color: var(--color-text-muted);
  font-size: 0.75rem;
  font-variant-numeric: tabular-nums;
}

/* 空状态 */
.article-empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: var(--spacing-xl) 0;
  text-align: center;
  color: var(--color-text-secondary);
}

.article-empty__icon {
  margin-bottom: var(--spacing-md);
  opacity: 0.5;
}

/* ── 分页控件 ── */
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
  border-radius: var(--radius-md);
  font-size: var(--font-size-sm);
  cursor: pointer;
  transition:
    background-color var(--duration-fast) var(--ease-standard),
    border-color var(--duration-fast) var(--ease-standard),
    color var(--duration-fast) var(--ease-standard),
    box-shadow var(--duration-fast) var(--ease-standard),
    transform var(--duration-fast) var(--ease-out);
}

.pagination__btn {
  background: var(--color-bg-secondary);
  color: var(--color-text-primary);
}

.pagination__btn:hover:not(:disabled) {
  background: var(--color-surface-hover);
  border-color: var(--color-border-strong);
  transform: translateY(-1px);
  box-shadow: var(--color-shadow-sm);
}

.pagination__btn:active:not(:disabled) {
  transform: translateY(0);
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
  transform: translateY(-1px);
}

.pagination__number:active {
  transform: translateY(0);
}

.pagination__number.active {
  background: var(--color-accent);
  color: var(--color-white);
  border-color: var(--color-accent);
  font-weight: var(--font-weight-semibold);
  box-shadow: 0 2px 8px var(--color-accent-glow-soft);
}

/* ── 编辑视图 ── */
.editor-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding-bottom: var(--spacing-sm);
  border-bottom: 1px solid var(--color-border-divider);
  min-height: 48px;
}

.editor-header__actions {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
}

/* 通用按钮 */
.btn-primary,
.btn-ghost,
.btn-text {
  display: inline-flex;
  align-items: center;
  gap: var(--spacing-xs);
  padding: 8px 16px;
  border-radius: var(--radius-md);
  font-size: var(--font-size-sm);
  font-weight: var(--font-weight-medium);
  cursor: pointer;
  transition:
    background-color var(--duration-fast) var(--ease-standard),
    color var(--duration-fast) var(--ease-standard),
    border-color var(--duration-fast) var(--ease-standard),
    box-shadow var(--duration-fast) var(--ease-standard);
}

.btn-primary {
  background: var(--color-accent);
  color: var(--color-white);
  border: 1px solid var(--color-accent);
  box-shadow: var(--color-shadow-sm);
}

.btn-primary:hover {
  background: var(--color-accent-hover);
  border-color: var(--color-accent-hover);
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

.btn-text {
  background: transparent;
  color: var(--color-text-secondary);
  border: none;
  padding: 6px 12px;
}

.btn-text:hover {
  color: var(--color-text-primary);
  background: var(--color-surface-hover);
  border-radius: var(--radius-sm);
}

/* ── 文章元信息表单 ── */
.meta-form {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-md);
  margin-bottom: var(--spacing-md);
}

.meta-form__row {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-xs);
}

.meta-form__row--split {
  flex-direction: row;
  gap: var(--spacing-md);
}

.meta-form__group {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: var(--spacing-xs);
}

.meta-form__label {
  font-size: var(--font-size-xs);
  font-weight: var(--font-weight-medium);
  color: var(--color-text-secondary);
  letter-spacing: 0.01em;
}

.meta-form__input,
.meta-form__select {
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
  font-family: inherit;
}

.meta-form__input::placeholder {
  color: var(--color-text-muted);
}

.meta-form__input:focus,
.meta-form__select:focus {
  border-color: var(--color-accent);
  box-shadow: 0 0 0 3px var(--color-accent-glow-soft);
}

.meta-form__select {
  cursor: pointer;
  background: var(--color-bg-tertiary);
  appearance: auto;
}

/* ── 标签多选框组 ── */
.tag-checkbox-group {
  display: flex;
  flex-wrap: wrap;
  gap: var(--spacing-sm);
  padding: var(--spacing-sm);
  background: var(--color-bg-tertiary);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
  min-height: 42px;
  align-content: flex-start;
  transition:
    border-color var(--duration-fast) var(--ease-standard),
    box-shadow var(--duration-fast) var(--ease-standard);
}

.tag-checkbox-group:focus-within {
  border-color: var(--color-accent);
  box-shadow: 0 0 0 3px var(--color-accent-glow-soft);
}

.tag-checkbox-label {
  display: inline-flex;
  align-items: center;
  gap: var(--spacing-xs);
  padding: 4px 10px;
  background: var(--color-bg-secondary);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-full);
  font-size: var(--font-size-sm);
  color: var(--color-text-secondary);
  cursor: pointer;
  transition:
    background-color var(--duration-fast) var(--ease-standard),
    border-color var(--duration-fast) var(--ease-standard),
    color var(--duration-fast) var(--ease-standard);
  user-select: none;
}

.tag-checkbox-label:hover {
  border-color: var(--color-border-strong);
  background: var(--color-surface-hover);
  color: var(--color-text-primary);
}

.tag-checkbox-label input[type='checkbox'] {
  accent-color: var(--color-accent);
  width: 14px;
  height: 14px;
  cursor: pointer;
  margin: 0;
}

/* 选中状态 */
.tag-checkbox-label:has(input:checked) {
  background: var(--color-accent-soft);
  border-color: var(--color-accent);
  color: var(--color-accent);
  font-weight: var(--font-weight-medium);
}

/* ── 封面图片上传 ── */
.cover-upload {
  position: relative;
  border: 2px dashed var(--color-border);
  border-radius: var(--radius-lg);
  background: var(--color-bg-tertiary);
  transition:
    border-color var(--duration-fast) var(--ease-standard),
    background-color var(--duration-fast) var(--ease-standard),
    box-shadow var(--duration-fast) var(--ease-standard);
  min-height: 160px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: var(--spacing-sm);
  cursor: pointer;
  overflow: hidden;
}

.cover-upload:hover {
  border-color: var(--color-accent);
  background: var(--color-accent-soft);
  box-shadow: 0 0 0 3px var(--color-accent-glow-soft);
}

.cover-upload.has-image {
  border: none;
  background: var(--color-bg-primary);
  cursor: default;
  min-height: 140px;
  padding: 0;
}

.cover-upload.has-image:hover {
  background: var(--color-bg-primary);
  box-shadow: none;
}

/* 隐藏的文件输入 */
.cover-input {
  display: none;
}

/* 上传提示文字 */
.cover-hint {
  font-size: var(--font-size-sm);
  color: var(--color-text-secondary);
}

/* 选择按钮 */
.cover-btn {
  margin-top: var(--spacing-xs);
  padding: 6px 16px;
  background: var(--color-accent);
  color: var(--color-white);
  border: none;
  border-radius: var(--radius-sm);
  font-size: var(--font-size-sm);
  cursor: pointer;
  transition: background-color var(--duration-fast) var(--ease-standard);
}

.cover-btn:hover {
  background: var(--color-accent-hover);
}

/* 封面预览图片 */
.cover-preview {
  width: 100%;
  height: 140px;
  object-fit: cover;
  border-radius: var(--radius-md);
}

/* 清除按钮 */
.cover-clear {
  position: absolute;
  top: 6px;
  right: 6px;
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(0, 0, 0, 0.5);
  color: var(--color-white);
  border: none;
  border-radius: var(--radius-full);
  font-size: 0.75rem;
  cursor: pointer;
  transition: background-color var(--duration-fast) var(--ease-standard);
}

.cover-clear:hover {
  background: var(--color-error);
}

/* ── Markdown 编辑器 ── */
.md-editor {
  display: flex;
  flex-direction: column;
  height: 640px;
  background: var(--color-surface);
  border: 1px solid var(--color-border-divider);
  border-radius: var(--radius-xl);
  box-shadow: var(--color-shadow-md);
  overflow: hidden;
}

.md-editor.preview-only .md-editor__pane:not(.md-editor__pane--preview) {
  display: none;
}

/* 工具栏 */
.md-toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: var(--spacing-sm) var(--spacing-md);
  background: var(--color-surface);
  border-bottom: 1px solid var(--color-border-divider);
  gap: var(--spacing-md);
  min-height: 44px;
}

.md-toolbar__left,
.md-toolbar__right {
  display: flex;
  align-items: center;
  gap: 2px;
}

.md-toolbar__btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 34px;
  height: 34px;
  padding: 0;
  border: none;
  border-radius: var(--radius-sm);
  background: transparent;
  color: var(--color-text-secondary);
  font-size: 0.9rem;
  cursor: pointer;
  transition:
    background-color var(--duration-fast) var(--ease-standard),
    color var(--duration-fast) var(--ease-standard);
}

.md-toolbar__btn:hover {
  color: var(--color-text-primary);
  background: var(--color-bg-secondary);
}

.md-toolbar__btn.active {
  color: var(--color-accent);
  background: var(--color-accent-soft);
}

/* 编辑器主体 */
.md-editor__body {
  display: flex;
  flex: 1;
  overflow: hidden;
  min-height: 0;
}

/* 预览模式: 使用 CSS 变量控制分栏比例 */
.md-editor__body.has-preview .md-editor__pane:first-of-type {
  width: var(--md-split-percent, 50%);
}

.md-editor__body.has-preview .md-editor__pane.md-editor__pane--preview {
  width: calc(100% - var(--md-split-percent, 50%));
}

.md-editor__pane {
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.md-editor__pane.hidden {
  display: none;
}

/* 编辑区 textarea */
.md-editor__textarea {
  flex: 1;
  width: 100%;
  min-height: 0;
  padding: var(--spacing-lg);
  border: none;
  border-radius: 0;
  background: var(--color-surface);
  color: var(--color-text-primary);
  font-family: var(--font-family-mono);
  font-size: var(--font-size-sm);
  line-height: var(--line-height-loose);
  resize: none;
  outline: none;
  tab-size: 4;
}

.md-editor__textarea::placeholder {
  color: var(--color-text-muted);
  opacity: 0.6;
}

.md-editor__textarea:focus {
  background: var(--color-surface);
}

/* 预览区 */
.md-editor__preview {
  flex: 1;
  min-height: 0;
  overflow: auto;
  padding: var(--spacing-lg);
  background: var(--color-surface);
  color: var(--color-text-primary);
  font-size: var(--font-size-base);
  line-height: var(--line-height-loose);
  overflow-wrap: break-word;
  word-wrap: break-word;
}

.md-editor__preview::-webkit-scrollbar {
  width: 6px;
}

.md-editor__preview::-webkit-scrollbar-thumb {
  background: var(--color-border);
  border-radius: var(--radius-full);
}

.md-editor__preview::-webkit-scrollbar-thumb:hover {
  background: var(--color-border-strong);
}

.md-editor__preview :global(h1) {
  font-size: 1.75rem;
  font-weight: var(--font-weight-bold);
  margin: var(--spacing-lg) 0 var(--spacing-sm);
  padding-bottom: var(--spacing-xs);
  border-bottom: 1px solid var(--color-border-divider);
  letter-spacing: -0.02em;
  color: var(--color-text-primary);
}

.md-editor__preview :global(h2) {
  font-size: 1.4rem;
  font-weight: var(--font-weight-semibold);
  margin: var(--spacing-lg) 0 var(--spacing-sm);
  padding-bottom: var(--spacing-xs);
  border-bottom: 1px solid var(--color-border-divider);
  color: var(--color-text-primary);
}

.md-editor__preview :global(h3) {
  font-size: 1.15rem;
  font-weight: var(--font-weight-semibold);
  margin: var(--spacing-md) 0 var(--spacing-sm);
  color: var(--color-text-primary);
}

.md-editor__preview :global(h4) {
  font-size: 1rem;
  font-weight: var(--font-weight-semibold);
  margin: var(--spacing-sm) 0 var(--spacing-xs);
  color: var(--color-text-primary);
}

.md-editor__preview :global(p) {
  margin: var(--spacing-sm) 0;
  color: var(--color-text-primary);
}

.md-editor__preview :global(a) {
  color: var(--color-accent);
  text-decoration: none;
  text-underline-offset: 3px;
  transition: color var(--duration-fast) var(--ease-standard);
}

.md-editor__preview :global(a:hover) {
  color: var(--color-accent-hover);
  text-decoration: underline;
}

.md-editor__preview :global(strong) {
  color: var(--color-text-primary);
  font-weight: var(--font-weight-semibold);
}

.md-editor__preview :global(em) {
  font-style: italic;
  color: var(--color-text-secondary);
}

.md-editor__preview :global(code) {
  padding: 2px 6px;
  background: var(--color-bg-primary);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-xs);
  font-family: var(--font-family-mono);
  font-size: 0.85rem;
  color: var(--color-error);
}

.md-editor__preview :global(pre) {
  overflow: auto;
  padding: var(--spacing-md);
  margin: var(--spacing-md) 0;
  border-radius: var(--radius-md);
  background: var(--color-bg-primary);
  border: 1px solid var(--color-border);
}

.md-editor__preview :global(pre code) {
  display: block;
  padding: 0;
  background: transparent;
  border-radius: 0;
  color: inherit;
  font-size: 0.85rem;
  line-height: 1.7;
  white-space: pre;
  overflow-x: auto;
}

.md-editor__preview :global(blockquote) {
  padding: var(--spacing-sm) var(--spacing-md);
  margin: var(--spacing-md) 0;
  border-left: 3px solid var(--color-accent);
  background: var(--color-accent-soft);
  border-radius: 0 var(--radius-md) var(--radius-md) 0;
  color: var(--color-text-secondary);
  font-style: italic;
}

.md-editor__preview :global(blockquote p) {
  margin: 0;
}

.md-editor__preview :global(ul),
.md-editor__preview :global(ol) {
  margin: var(--spacing-sm) 0;
  padding-left: 1.5rem;
}

.md-editor__preview :global(li) {
  margin: 4px 0;
  padding-left: 4px;
}

.md-editor__preview :global(li)::marker {
  color: var(--color-accent);
}

.md-editor__preview :global(table) {
  width: 100%;
  border-collapse: collapse;
  margin: var(--spacing-md) 0;
  font-size: 0.875rem;
  overflow: auto;
  display: block;
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
}

.md-editor__preview :global(th),
.md-editor__preview :global(td) {
  padding: 10px 14px;
  border: 1px solid var(--color-border);
  text-align: left;
}

.md-editor__preview :global(th) {
  background: var(--color-bg-secondary);
  font-weight: var(--font-weight-semibold);
  color: var(--color-text-primary);
}

.md-editor__preview :global(tr:nth-child(even)) {
  background: var(--color-bg-primary);
}

.md-editor__preview :global(img) {
  max-width: 100%;
  border-radius: var(--radius-md);
  box-shadow: var(--color-shadow-sm);
  margin: var(--spacing-sm) 0;
}

.md-editor__preview :global(hr) {
  border: none;
  border-top: 1px solid var(--color-border-divider);
  margin: var(--spacing-lg) 0;
}

.md-editor__preview :global(dl) {
  margin: var(--spacing-sm) 0;
}

.md-editor__preview :global(dt) {
  font-weight: var(--font-weight-semibold);
  color: var(--color-text-primary);
  margin-top: var(--spacing-sm);
}

.md-editor__preview :global(dd) {
  margin-left: 0;
  margin-bottom: var(--spacing-sm);
  color: var(--color-text-secondary);
}

/* 拖动分隔条 + 分割线 */
.md-editor__resize {
  width: 14px;
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: col-resize;
  background: var(--color-border-divider);
  transition: background-color var(--duration-fast) var(--ease-standard);
  z-index: 2;
}

.md-editor__resize:hover {
  background: var(--color-accent);
}

/* 垂直分割线 */
.md-editor__divider {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
  height: 70%;
  padding: 4px 0;
  border-left: 1px dashed var(--color-border);
}

.md-editor__resize:hover .md-editor__divider {
  border-left-color: var(--color-white-soft);
}

/* 拖动把手点 */
.md-editor__grip {
  width: 4px;
  height: 4px;
  border-radius: var(--radius-xs);
  background: var(--color-border);
  transition: background-color var(--duration-fast) var(--ease-standard);
}

.md-editor__resize:hover .md-editor__grip {
  background: var(--color-white-soft);
}

/* 代码语法高亮面板 */
.md-editor__code-preview {
  flex: 1;
  min-height: 0;
  overflow: auto;
  padding: var(--spacing-md);
  background: var(--color-bg-primary);
  color: var(--color-text-primary);
  font-family: var(--font-family-mono);
  font-size: 0.85rem;
  line-height: 1.7;
  overflow-wrap: break-word;
  word-wrap: break-word;
  white-space: pre-wrap;
}

.md-editor__code-preview::-webkit-scrollbar {
  width: 6px;
}

.md-editor__code-preview::-webkit-scrollbar-thumb {
  background: var(--color-border);
  border-radius: var(--radius-full);
}

.md-editor__code-preview::-webkit-scrollbar-thumb:hover {
  background: var(--color-border-strong);
}

/* 代码高亮: 自动检测语言后渲染 */
.md-editor__code-preview :global(pre) {
  display: block;
  overflow: auto;
  padding: var(--spacing-sm);
  margin: var(--spacing-sm) 0;
  border-radius: var(--radius-md);
  background: var(--color-bg-tertiary);
  border: 1px solid var(--color-border);
  font-size: 0.85rem;
  line-height: 1.6;
  white-space: pre;
}

.md-editor__code-preview :global(code) {
  display: block;
  padding: 0;
  background: transparent;
  color: inherit;
  font-size: 0.85rem;
  line-height: 1.6;
  white-space: pre;
  overflow-x: auto;
}

/* 底部计数 + 快捷键提示 */
.md-editor__footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: var(--spacing-sm) var(--spacing-lg);
  background: var(--color-bg-secondary);
  border-top: 1px solid var(--color-border-divider);
  font-size: var(--font-size-xs);
  color: var(--color-text-muted);
  min-height: 34px;
  gap: var(--spacing-md);
  flex-shrink: 0;
}

.md-editor__footer-left {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
}

.md-editor__count {
  color: var(--color-text-secondary);
  font-variant-numeric: tabular-nums;
}

.md-editor__count--draft {
  color: var(--color-warning);
}

.md-editor__shortcuts {
  font-family: var(--font-family-mono);
  opacity: 0.5;
  white-space: nowrap;
}

/* 移动端适配 */
@media (max-width: 768px) {
  .md-editor {
    max-height: none;
    min-height: 350px;
  }

  .md-editor__body {
    flex-direction: column;
  }

  .md-editor__body.has-preview .md-editor__pane {
    width: 100% !important;
    height: 50%;
  }

  .md-editor__resize {
    display: none;
  }

  .md-editor__preview {
    max-height: none;
  }
}
</style>
