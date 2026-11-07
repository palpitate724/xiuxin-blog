<template>
  <div class="admin-shell">
    <!-- 顶部导航栏 -->
    <header class="admin-header">
      <div class="admin-header__left">
        <div class="admin-logo">
          <span class="admin-logo__dot"></span>
          <span class="admin-logo__text">Xiuxin</span>
          <span class="admin-logo__sub">BLOG</span>
        </div>
        <div class="admin-breadcrumb">
          <span class="breadcrumb-item" v-for="item in breadcrumbs" :key="item.path">
            <router-link v-if="item.path" :to="item.path">{{ item.label }}</router-link>
            <span v-else>{{ item.label }}</span>
          </span>
        </div>
      </div>
      <div class="admin-header__right">
        <div class="admin-user" :title="'当前用户：' + userName">
          <div class="admin-user__avatar">
            <img src="/favicon.ico" alt="avatar" class="admin-user__img" />
          </div>
          <div class="admin-user__info">
            <span class="admin-user__name">{{ userName }}</span>
            <span class="admin-user__role">超级管理员</span>
          </div>
        </div>
      </div>
    </header>

    <div class="admin-body">
      <!-- 左侧导航 -->
      <nav class="admin-sidebar">
        <ul class="admin-menu">
          <li
            v-for="item in menuItems"
            :key="item.to"
            :class="['admin-menu__item', { active: currentPath === item.to }]"
          >
            <router-link :to="item.to" class="admin-menu__link">
              <component :is="item.iconComponent" :size="18" class="admin-menu__icon" />
              <span class="admin-menu__label">{{ item.label }}</span>
            </router-link>
          </li>
        </ul>
      </nav>

      <!-- 主内容区 -->
      <main class="admin-main">
        <router-view v-slot="{ Component }">
          <transition name="fade-slide" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </main>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { LayoutDashboard, FileText, FolderOpen, Tag, User } from '@lucide/vue'

/** Vue Router 路由实例，用于获取当前路径（未直接使用但保留以便后续扩展） */
const route = useRoute()
/** Vue Router 导航实例（未直接使用但保留以便后续扩展） */
const router = useRouter()

/**
 * 当前路由路径（响应式）。
 * 用于高亮对应侧边栏菜单项、生成面包屑导航。
 */
const currentPath = computed(() => route.path)

/** 当前登录用户名（硬编码为 admin，后续从 userpage store 读取） */
const userName = computed(() => 'admin')

// ─── 面包屑导航 ────────────────────────────────────────────────────────────────
/**
 * 根据当前路径自动生成面包屑导航数组。
 * 逻辑：
 *   - 匹配预设的路由 → 标签映射表
 *   - 若匹配到，返回 [首页, 当前页] 两级面包屑
 *   - 未匹配则仅显示首页
 * TODO: 后续可扩展为动态路由解析
 */
const breadcrumbs = computed(() => {
  const map: Record<string, { label: string; path: string }> = {
    '/zhuye/yibiaopan': { label: '仪表盘', path: '/zhuye/yibiaopan' },
    '/zhuye/art':       { label: '文章管理', path: '/zhuye/art' },
    '/zhuye/cat':       { label: '分类管理', path: '/zhuye/cat' },
    '/zhuye/tag':       { label: '标签管理', path: '/zhuye/tag' },
    '/user':            { label: '用户登录', path: '' },
  }
  const entry = map[currentPath.value]
  if (entry) return [{ label: '首页', path: '/zhuye/yibiaopan' }, entry]
  return [{ label: '首页', path: '/zhuye/yibiaopan' }]
})

// ─── 侧边栏菜单项 ──────────────────────────────────────────────────────────────
/**
 * 侧边栏导航菜单配置数组。
 * 每项包含：
 *   - label: 菜单显示文本
 *   - iconComponent: @lucide/vue 图标组件（用于 <component :is="..."> 动态渲染）
 *   - to: 路由路径
 */
const menuItems = [
  { label: '仪表盘',   iconComponent: LayoutDashboard, to: '/zhuye/yibiaopan' },
  { label: '文章管理', iconComponent: FileText, to: '/zhuye/art' },
  { label: '分类管理', iconComponent: FolderOpen, to: '/zhuye/cat' },
  { label: '标签管理', iconComponent: Tag, to: '/zhuye/tag' },
  { label: '用户登录', iconComponent: User, to: '/user' },
]
</script>

<style scoped>
.admin-shell {
  height: 100vh;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  background: var(--color-bg-primary);
}

/* ── Header ── */
.admin-header {
  position: sticky;
  top: 0;
  z-index: var(--z-sticky);
  height: var(--layout-header-height);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 var(--spacing-lg);
  background: var(--color-surface);
  border-bottom: 1px solid var(--color-border-divider);
  box-shadow: var(--color-shadow-sm);
  flex-shrink: 0;
}

.admin-header__left {
  display: flex;
  align-items: center;
  gap: var(--spacing-lg);
}

/* Logo */
.admin-logo {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  font-family: -apple-system, BlinkMacSystemFont, 'Inter', sans-serif;
  font-weight: var(--font-weight-bold);
  font-size: var(--font-size-lg);
  letter-spacing: -0.02em;
  white-space: nowrap;
}
.admin-logo__dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: var(--color-accent);
  box-shadow: 0 0 6px var(--color-accent-glow-soft);
  flex-shrink: 0;
}
.admin-logo__text { color: var(--color-text-primary); }
.admin-logo__sub {
  color: var(--color-text-muted);
  font-size: var(--font-size-xs);
  letter-spacing: 0.1em;
  text-transform: uppercase;
  font-weight: var(--font-weight-medium);
}

/* 面包屑 */
.admin-breadcrumb {
  display: flex;
  align-items: center;
  gap: var(--spacing-xs);
  font-size: var(--font-size-sm);
  color: var(--color-text-muted);
}
.breadcrumb-item + .breadcrumb-item::before {
  content: '/';
  margin-right: var(--spacing-xs);
  color: var(--color-text-dim);
}
.breadcrumb-item a {
  color: var(--color-text-secondary);
  transition: color var(--duration-fast);
}
.breadcrumb-item a:hover { color: var(--color-accent); }

/* Header 右侧用户 */
.admin-header__right {
  display: flex;
  align-items: center;
}
.admin-user {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  padding: 4px 10px 4px 4px;
  border-radius: var(--radius-full);
  background: var(--color-bg-secondary);
  border: 1px solid var(--color-border);
  cursor: pointer;
  transition: background var(--duration-fast), border-color var(--duration-fast);
}
.admin-user:hover {
  background: var(--color-surface-hover);
  border-color: var(--color-border-strong);
}
.admin-user__avatar {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  overflow: hidden;
  background: var(--color-bg-tertiary);
  border: 1px solid var(--color-border);
  display: flex;
  align-items: center;
  justify-content: center;
}
.admin-user__img { width: 100%; height: 100%; object-fit: cover; }
.admin-user__info { display: flex; flex-direction: column; line-height: 1.2; }
.admin-user__name {
  font-size: var(--font-size-sm);
  font-weight: var(--font-weight-semibold);
  color: var(--color-text-primary);
}
.admin-user__role {
  font-size: var(--font-size-xs);
  color: var(--color-text-muted);
}

/* ── Body ── */
.admin-body {
  display: flex;
  flex: 1;
  min-height: 0;
  overflow: hidden;
}

/* ── Sidebar ── */
.admin-sidebar {
  width: var(--layout-sidebar-width);
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
  background: var(--color-surface);
  border-right: 1px solid var(--color-border-divider);
  overflow-y: auto;
  overflow-x: hidden;
  padding: var(--spacing-md) 0;
}
.admin-sidebar::-webkit-scrollbar { width: 4px; }
.admin-sidebar::-webkit-scrollbar-track { background: transparent; }
.admin-sidebar::-webkit-scrollbar-thumb { background: var(--color-border); border-radius: 2px; }

.admin-menu { display: flex; flex-direction: column; gap: 2px; padding: 0 var(--spacing-sm); }

.admin-menu__item { border-radius: var(--radius-md); }

.admin-menu__link {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  padding: var(--spacing-sm) var(--spacing-md);
  border-radius: var(--radius-md);
  color: var(--color-text-secondary);
  font-size: var(--font-size-sm);
  font-weight: var(--font-weight-medium);
  text-decoration: none;
  transition: color var(--duration-fast), background var(--duration-fast);
  position: relative;
}
.admin-menu__link:hover {
  color: var(--color-text-primary);
  background: var(--color-surface-hover);
}
.admin-menu__item.active .admin-menu__link {
  color: var(--color-accent);
  background: var(--color-accent-soft);
  font-weight: var(--font-weight-semibold);
}
.admin-menu__item.active::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 3px;
  height: 60%;
  background: var(--color-accent);
  border-radius: 0 2px 2px 0;
}
.admin-menu__icon {
  flex-shrink: 0;
}
.admin-menu__label { white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }

/* ── Main ── */
.admin-main {
  flex: 1;
  min-width: 0;
  overflow-y: auto;
  overflow-x: hidden;
  padding: var(--spacing-lg);
  background: var(--color-bg-primary);
}
.admin-main::-webkit-scrollbar { width: 6px; }
.admin-main::-webkit-scrollbar-track { background: transparent; }
.admin-main::-webkit-scrollbar-thumb { background: var(--color-border); border-radius: 3px; }
.admin-main::-webkit-scrollbar-thumb:hover { background: var(--color-border-strong); }

/* ── 页面过渡动画 ── */
.fade-slide-enter-active { transition: opacity var(--duration-normal) var(--ease-out), transform var(--duration-normal) var(--ease-out); }
.fade-slide-leave-active  { transition: opacity var(--duration-fast) var(--ease-standard); }
.fade-slide-enter-from     { opacity: 0; transform: translateY(6px); }
.fade-slide-leave-to      { opacity: 0; }

/* ── 响应式 ── */
@media (max-width: 768px) {
  .admin-sidebar { width: 56px; }
  .admin-menu__label { display: none; }
  .admin-menu__link { justify-content: center; padding: var(--spacing-sm); }
  .admin-logo__sub, .admin-breadcrumb { display: none; }
  .admin-user__info { display: none; }
}
</style>
