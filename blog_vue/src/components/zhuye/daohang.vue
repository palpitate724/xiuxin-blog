<template>
  <nav class="daohang" :class="{ 'transparent': isTransparent }">
    <div class="daohang-container">
      <!-- 左侧区域：Logo + 导航链接 -->
      <div class="daohang-left">
        <!-- Logo区域 -->
        <div class="daohang-logo">
          <span class="logo-text">wl-blog</span>
        </div>

        <span class="main-divider"></span>

        <!-- 导航链接 -->
        <div class="daohang-links">
          <a href="/" class="nav-link">首页</a>
          <span class="nav-divider"></span>
          <a href="#" class="nav-link">文章</a>
          <span class="nav-divider"></span>
          <a href="#" class="nav-link">归档</a>
          <span class="nav-divider"></span>
          <a href="#" class="nav-link">关于</a>
        </div>
      </div>


    </div>
  </nav>
</template>

<script setup lang="ts">
// 导航栏透明度
const props = defineProps<{
  // 无主题切换逻辑，固定深色
  isTransparent?: boolean
}>()
</script>

<style scoped>
.daohang {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  background: var(--color-bg-nav);
  backdrop-filter: blur(24px) saturate(1.8);
  -webkit-backdrop-filter: blur(24px) saturate(1.8);
  border-bottom: 1px solid var(--color-border-dim);
  transition: all var(--duration-theme) ease;
}

.daohang::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 1px;
  background: linear-gradient(90deg, transparent 0%, var(--color-accent-glow-soft) 20%, var(--color-accent-secondary-soft) 50%, var(--color-accent-glow-soft) 80%, transparent 100%);
  opacity: 0.5;
  pointer-events: none;
}

.daohang.transparent {
  background: transparent;
  backdrop-filter: none;
  -webkit-backdrop-filter: none;
  border-bottom: none;
}

.daohang-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 24px;
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.daohang-left {
  display: flex;
  align-items: center;
  gap: 32px;
}

.main-divider {
  width: 1px;
  height: 24px;
  background: var(--color-border-divider);
  transition: background var(--duration-theme) ease;
}

.daohang-logo {
  display: flex;
  align-items: center;
  gap: 12px;
  text-decoration: none;
}

.logo-text {
  font-size: 1.1rem;
  font-weight: 600;
  color: var(--color-text-primary);
  letter-spacing: 1px;
  transition: color var(--duration-theme) ease;
}

/* 浅色主题导航栏文字 */

.daohang-logo:hover .logo-text {
  color: var(--color-accent-light);
}

.daohang-links {
  display: flex;
  gap: 8px;
  align-items: center;
}

.nav-divider {
  width: 1px;
  height: 16px;
  background: var(--color-border-divider);
  transition: background var(--duration-theme) ease;
}

.nav-link {
  padding: 8px 16px;
  color: var(--color-text-secondary);
  text-decoration: none;
  font-size: 0.9rem;
  font-weight: 500;
  border-radius: 10px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  letter-spacing: 0.02em;
}

.nav-link::after {
  content: '';
  position: absolute;
  bottom: 4px;
  left: 50%;
  width: 0;
  height: 2px;
  background: linear-gradient(90deg, var(--color-accent), var(--color-accent-secondary));
  border-radius: 1px;
  transform: translateX(-50%);
  transition: width 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
}

.nav-link:hover {
  color: var(--color-text-primary);
  background: var(--color-accent-glow-soft);
  transform: translateY(-1px);
}

.nav-link:hover::after {
  width: 20px;
}

/* 浅色主题导航链接 */



/* 浅色主题分隔线 */

/* 浅色主题主题切换按钮 */


/* 浅色主题 Logo 文字 */


.nav-link.active {
  color: var(--color-white);
  background: var(--color-accent-glow);
}

.nav-link.active::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 20px;
  height: 2px;
  background: var(--color-accent);
  border-radius: var(--radius-xs);
}

.daohang-right {
  display: flex;
  align-items: center;
  gap: 12px;
}

/* 导航栏主题切换按钮 */



.theme-icon-nav {
  width: 18px;
  height: 18px;
  transition: transform 0.4s cubic-bezier(0.34, 1.56, 0.64, 1), opacity 0.3s ease;
}


/* 移动端适配 */
@media (max-width: 768px) {
  .daohang-container {
    padding: 0 16px;
    height: 56px;
  }

  .daohang-left {
    gap: 12px;
  }

  .logo-text {
    font-size: 1rem;
  }

  .daohang-links {
    display: none;
  }

  .theme-toggle-nav {
    width: 32px;
    height: 32px;
  }
}

@media (max-width: 480px) {
  .daohang-container {
    padding: 0 12px;
  }

  .logo-text {
    font-size: 0.9rem;
  }
}
</style>
