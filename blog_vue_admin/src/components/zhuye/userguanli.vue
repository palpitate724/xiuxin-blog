<template>
  <!-- 用户管理页面：展示当前登录用户的详细信息卡片 -->
  <div class="page-user">
    <!-- ── 页头 ── -->
    <div class="page-header">
      <h1 class="page-title">用户管理</h1>
      <p class="page-desc">查看和管理您的账户信息</p>
    </div>

    <!-- ── 主信息卡片（头像 + 基本信息） ── -->
    <div class="profile-card">
      <div class="profile-card__banner"></div>
      <div class="profile-card__body">
        <!-- 左侧：头像 -->
        <div class="profile-avatar-wrap">
          <div
            class="profile-avatar"
            :style="{ backgroundImage: avatarUrl ? `url(${avatarUrl})` : 'none' }"
          >
            <span v-if="!avatarUrl" class="profile-avatar__fallback">{{
              userName.charAt(0).toUpperCase()
            }}</span>
          </div>
          <label class="avatar-upload-btn" title="更换头像">
            <Camera :size="14" />
            <input
              type="file"
              accept="image/*"
              class="avatar-upload-input"
              @change="handleAvatarUpload"
            />
          </label>
        </div>

        <!-- 右侧：基本信息 -->
        <div class="profile-info">
          <div class="profile-info__top">
            <h2 class="profile-name">{{ userName || '未登录' }}</h2>
            <span class="profile-role-badge">超级管理员</span>
          </div>
          <div class="profile-meta">
            <div class="meta-item">
              <span class="meta-item__icon"><User :size="14" /></span>
              <span class="meta-item__label">用户名</span>
              <span class="meta-item__value">{{ userName || '—' }}</span>
            </div>
            <div class="meta-item">
              <span class="meta-item__icon"><Hash :size="14" /></span>
              <span class="meta-item__label">用户 ID</span>
              <span class="meta-item__value meta-item__value--mono">{{ userId || '—' }}</span>
            </div>
            <div class="meta-item">
              <span class="meta-item__icon"><Shield :size="14" /></span>
              <span class="meta-item__label">角色</span>
              <span class="meta-item__value">管理员</span>
            </div>
            <div class="meta-item">
              <span class="meta-item__icon"><CalendarDays :size="14" /></span>
              <span class="meta-item__label">注册时间</span>
              <span class="meta-item__value">—</span>
            </div>
          </div>
          <div class="profile-actions">
            <button class="btn-primary" @click="showTokenModal = true">
              <KeyRound :size="16" />
              查看 Token
            </button>
            <button class="btn-ghost" @click="handleLogout">
              <LogOut :size="16" />
              退出登录
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- ── 统计概览 ── -->
    <div class="stat-grid-inline">
      <div class="stat-tile" v-for="(tile, i) in statTiles" :key="i">
        <div class="stat-tile__icon" :style="{ background: tile.colorSoft }">
          <component :is="tile.iconComponent" :size="20" />
        </div>
        <div class="stat-tile__info">
          <span class="stat-tile__value">{{ tile.value }}</span>
          <span class="stat-tile__label">{{ tile.label }}</span>
        </div>
      </div>
    </div>

    <!-- ── 快捷操作区 ── -->
    <div class="admin-card admin-card--wide">
      <div class="admin-card__header">
        <span class="admin-card__title">快捷操作</span>
      </div>
      <div class="admin-card__body admin-card__body--actions">
        <router-link to="/zhuye/yibiaopan" class="action-btn">
          <LayoutDashboard :size="18" />
          <span>返回仪表盘</span>
        </router-link>
        <router-link to="/zhuye/art" class="action-btn">
          <FileText :size="18" />
          <span>文章管理</span>
        </router-link>
        <router-link to="/zhuye/cat" class="action-btn">
          <FolderOpen :size="18" />
          <span>分类管理</span>
        </router-link>
        <router-link to="/zhuye/tag" class="action-btn">
          <TagIcon :size="18" />
          <span>标签管理</span>
        </router-link>
      </div>
    </div>

    <!-- ── Token 弹窗 ── -->
    <Teleport to="body">
      <div v-if="showTokenModal" class="modal-overlay" @click.self="showTokenModal = false">
        <div class="modal">
          <div class="modal__header">
            <span class="modal__title">访问令牌</span>
            <button class="modal__close" @click="showTokenModal = false" aria-label="关闭">
              <X :size="16" />
            </button>
          </div>
          <div class="modal__body">
            <p class="token-hint">以下是您的访问令牌，请妥善保管。</p>
            <div class="token-display">
              <code>{{ token || '暂无 Token' }}</code>
              <button class="copy-btn" @click="copyToken" :disabled="!token">
                <Copy v-if="copied === false" :size="14" />
                <Check v-else :size="14" />
                {{ copied === false ? '复制' : '已复制' }}
              </button>
            </div>
          </div>
          <div class="modal__footer">
            <button class="btn-ghost" @click="showTokenModal = false">关闭</button>
          </div>
        </div>
      </div>
    </Teleport>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { userpage } from '../../stores/user'
import {
  User,
  Hash,
  Shield,
  CalendarDays,
  Camera,
  KeyRound,
  LogOut,
  LayoutDashboard,
  FileText,
  FolderOpen,
  Tag as TagIcon,
  X,
  Copy,
  Check,
} from '@lucide/vue'

// ─── Pinia Store ──────────────────────────────────────────────────────────────
const userpage_v = userpage()
const router = useRouter()

/** 当前用户 ID（来自 store） */
const userId = computed(() => userpage_v.user.id)
/** 当前用户名（来自 store） */
const userName = computed(() => userpage_v.user.name)
/** 当前用户头像 URL（来自 store） */
const avatarUrl = computed(() => userpage_v.user.touxiangurl ?? '')
/** 当前用户 Token（来自 store） */
const token = computed(() => userpage_v.user.token)

// ─── 状态变量 ─────────────────────────────────────────────────────────────────
/** 是否显示 Token 弹窗 */
const showTokenModal = ref(false)
/** 复制按钮反馈状态：false = 未复制，true = 已复制 */
const copied = ref(false)

// ─── 统计面板数据（模拟占位，与 yibiaopan 保持一致风格）────────────────────────
/** 顶部统计磁贴数组，每项包含：标签、数值、图标组件、背景色 */
const statTiles = ref([
  { label: '发布文章', value: '—', iconComponent: FileText, colorSoft: 'rgba(59,130,246,0.1)' },
  { label: '草稿数量', value: '—', iconComponent: FileText, colorSoft: 'rgba(245,158,11,0.1)' },
  { label: '收藏文章', value: '—', iconComponent: FileText, colorSoft: 'rgba(16,185,129,0.1)' },
  { label: '评论数', value: '—', iconComponent: FileText, colorSoft: 'rgba(139,92,246,0.1)' },
])

// ─── 生命周期 ──────────────────────────────────────────────────────────────────
onMounted(() => {
  // TODO: 根据实际路由参数或 store 加载对应用户的完整信息
})

// ─── 方法 ──────────────────────────────────────────────────────────────────────
/** 处理头像上传 */
const handleAvatarUpload = async (e: Event) => {
  const input = e.target as HTMLInputElement
  if (!input.files || input.files.length === 0) return
  const file = input.files[0]!
  if (!file.type.startsWith('image/')) return

  try {
    await userpage_v.userupfenmian(file)
    // 更新本地预览
    const reader = new FileReader()
    reader.onload = () => {
      userpage_v.user.touxiangurl = reader.result as string
    }
    reader.readAsDataURL(file)
  } catch {
    alert('头像上传失败')
  }
}

/** 复制 Token 到剪贴板 */
const copyToken = () => {
  navigator.clipboard.writeText(token.value).then(() => {
    copied.value = true
    setTimeout(() => {
      copied.value = false
    }, 2000)
  })
}

/** 退出登录并跳转回登录页 */
const handleLogout = () => {
  userpage_v.logout()
  router.push('/user')
}
</script>

<style scoped>
/* =============================================================================
 * userguanli.vue — 用户管理页面
 * 设计令牌: src/styles/tokens.css
 * ============================================================================= */

/* ── 页面容器 ── */
.page-user {
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

/* ── 个人信息主卡片 ── */
.profile-card {
  background: var(--color-surface);
  border: 1px solid var(--color-border-divider);
  border-radius: var(--radius-xl);
  box-shadow: var(--color-shadow-md);
  overflow: hidden;
}

/* 顶部渐变横幅 */
.profile-card__banner {
  height: 100px;
  background: linear-gradient(135deg, var(--color-accent) 0%, #6366f1 100%);
}

.profile-card__body {
  padding: 0 var(--spacing-xl) var(--spacing-xl);
  display: flex;
  align-items: flex-start;
  gap: var(--spacing-lg);
}

/* ── 头像区域 ── */
.profile-avatar-wrap {
  position: relative;
  margin-top: -44px;
  flex-shrink: 0;
}
.profile-avatar {
  width: 88px;
  height: 88px;
  border-radius: var(--radius-full);
  border: 4px solid var(--color-surface);
  background: var(--color-bg-secondary);
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  box-shadow: var(--color-shadow);
  background-size: cover;
  background-position: center;
  flex-shrink: 0;
}
.profile-avatar__fallback {
  font-size: 2rem;
  font-weight: var(--font-weight-bold);
  color: var(--color-accent);
  line-height: 1;
}
.avatar-upload-btn {
  position: absolute;
  bottom: 2px;
  right: 2px;
  width: 26px;
  height: 26px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: var(--radius-full);
  background: var(--color-accent);
  color: var(--color-white);
  cursor: pointer;
  transition: background-color var(--duration-fast);
  box-shadow: var(--color-shadow-sm);
}
.avatar-upload-btn:hover {
  background: var(--color-accent-hover);
}
.avatar-upload-input {
  display: none;
}

/* ── 用户信息区 ── */
.profile-info {
  flex: 1;
  min-width: 0;
  padding-top: 12px;
}
.profile-info__top {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  margin-bottom: var(--spacing-md);
}
.profile-name {
  font-size: var(--font-size-xl);
  font-weight: var(--font-weight-bold);
  color: var(--color-text-primary);
  margin: 0;
}
.profile-role-badge {
  padding: 3px 10px;
  background: var(--color-accent-soft);
  color: var(--color-accent);
  border-radius: var(--radius-full);
  font-size: var(--font-size-xs);
  font-weight: var(--font-weight-semibold);
  white-space: nowrap;
}
.profile-meta {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: var(--spacing-sm) var(--spacing-lg);
  margin-bottom: var(--spacing-lg);
}
.meta-item {
  display: flex;
  align-items: center;
  gap: var(--spacing-xs);
  font-size: var(--font-size-sm);
}
.meta-item__icon {
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--color-text-muted);
  width: 28px;
  flex-shrink: 0;
}
.meta-item__label {
  color: var(--color-text-muted);
  white-space: nowrap;
  min-width: 48px;
}
.meta-item__value {
  color: var(--color-text-primary);
  word-break: break-all;
}
.meta-item__value--mono {
  font-family: var(--font-family-mono);
  font-size: var(--font-size-xs);
}
.profile-actions {
  display: flex;
  gap: var(--spacing-sm);
}

/* ── 统计磁贴行 ── */
.stat-grid-inline {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: var(--spacing-md);
}
.stat-tile {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
  padding: var(--spacing-md) var(--spacing-lg);
  background: var(--color-surface);
  border: 1px solid var(--color-border-divider);
  border-radius: var(--radius-xl);
  box-shadow: var(--color-shadow-sm);
  transition:
    border-color var(--duration-fast) var(--ease-standard),
    box-shadow var(--duration-fast) var(--ease-standard),
    transform var(--duration-fast) var(--ease-out);
}
.stat-tile:hover {
  border-color: var(--color-border-strong);
  box-shadow: var(--color-shadow-md);
  transform: translateY(-2px);
}
.stat-tile__icon {
  width: 44px;
  height: 44px;
  border-radius: var(--radius-lg);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}
.stat-tile__info {
  display: flex;
  flex-direction: column;
  gap: 2px;
}
.stat-tile__value {
  font-size: 1.5rem;
  font-weight: var(--font-weight-bold);
  color: var(--color-text-primary);
  line-height: 1.1;
  letter-spacing: -0.02em;
}
.stat-tile__label {
  font-size: var(--font-size-xs);
  color: var(--color-text-muted);
  text-transform: uppercase;
  letter-spacing: 0.05em;
  font-weight: var(--font-weight-medium);
}

/* ── 快捷操作卡片复用 admin-card 样式 ── */
.admin-card {
  background: var(--color-surface);
  border: 1px solid var(--color-border-divider);
  border-radius: var(--radius-xl);
  box-shadow: var(--color-shadow-sm);
  overflow: hidden;
}
.admin-card--wide {
  width: 100%;
}
.admin-card__header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: var(--spacing-md) var(--spacing-lg);
  border-bottom: 1px solid var(--color-border-divider);
}
.admin-card__title {
  font-size: var(--font-size-md);
  font-weight: var(--font-weight-semibold);
  color: var(--color-text-primary);
}
.admin-card__body {
  padding: var(--spacing-lg);
}
.admin-card__body--actions {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-sm);
}
.action-btn {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  padding: var(--spacing-sm) var(--spacing-md);
  background: var(--color-bg-secondary);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
  font-size: var(--font-size-sm);
  color: var(--color-text-primary);
  text-decoration: none;
  cursor: pointer;
  transition:
    background-color var(--duration-fast),
    border-color var(--duration-fast),
    box-shadow var(--duration-fast),
    transform var(--duration-fast);
}
.action-btn:hover {
  background: var(--color-surface-hover);
  border-color: var(--color-border-strong);
  box-shadow: var(--color-shadow-sm);
  transform: translateY(-1px);
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
  border: none;
}
.btn-primary {
  background: var(--color-accent);
  color: var(--color-white);
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
  max-width: 540px;
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
  transition: background-color var(--duration-fast);
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

/* Token 展示区 */
.token-hint {
  font-size: var(--font-size-sm);
  color: var(--color-text-secondary);
  margin: 0 0 var(--spacing-sm);
}
.token-display {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  padding: var(--spacing-sm) var(--spacing-md);
  background: var(--color-bg-primary);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
  overflow: hidden;
}
.token-display code {
  flex: 1;
  font-family: var(--font-family-mono);
  font-size: var(--font-size-xs);
  color: var(--color-text-primary);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.copy-btn {
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 4px 10px;
  background: var(--color-bg-secondary);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-sm);
  font-size: var(--font-size-xs);
  color: var(--color-text-secondary);
  cursor: pointer;
  flex-shrink: 0;
  transition: all var(--duration-fast);
}
.copy-btn:hover:not(:disabled) {
  background: var(--color-surface-hover);
  border-color: var(--color-border-strong);
  color: var(--color-text-primary);
}
.copy-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}
.copy-btn .check-icon {
  color: var(--color-success);
}

/* ── 响应式 ── */
@media (max-width: 900px) {
  .stat-grid-inline {
    grid-template-columns: repeat(2, 1fr);
  }
}
@media (max-width: 640px) {
  .profile-card__body {
    flex-direction: column;
    align-items: center;
    text-align: center;
    padding: 0 var(--spacing-md) var(--spacing-lg);
  }
  .profile-avatar-wrap {
    margin-top: -44px;
  }
  .profile-meta {
    grid-template-columns: 1fr;
  }
  .profile-actions {
    justify-content: center;
  }
  .stat-grid-inline {
    grid-template-columns: 1fr;
  }
}
</style>
