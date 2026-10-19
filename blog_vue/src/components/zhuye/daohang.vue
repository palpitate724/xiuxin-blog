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
  backdrop-filter: blur(20px);
  border-bottom: 1px solid var(--color-border-dim);
  transition: all 0.3s ease;
}

.daohang.transparent {
  background: transparent;
  backdrop-filter: none;
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
}

.daohang-logo {
  display: flex;
  align-items: center;
  gap: 12px;
  text-decoration: none;
}

.logo-icon {
  width: 28px;
  height: 28px;
  transition: transform 0.3s ease;
}

.daohang-logo:hover .logo-icon {
  transform: rotate(10deg);
}

.logo-text {
  font-size: 1.1rem;
  font-weight: 600;
  color: var(--color-white);
  letter-spacing: 1px;
  transition: color 0.3s ease;
}

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
}

.nav-link {
  padding: 8px 16px;
  color: var(--color-text-secondary);
  text-decoration: none;
  font-size: 0.9rem;
  font-weight: 500;
  border-radius: 8px;
  transition: all 0.3s ease;
  position: relative;
}

.nav-link:hover {
  color: var(--color-white);
  background: var(--color-accent-glow);
}

.nav-link.active {
  color: var(--color-white);
  background: var(--color-accent-glow-soft);
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

.main-divider {
  width: 1px;
  height: 24px;
  background: var(--color-border-divider);
}

.daohang-user {
  display: flex;
  align-items: center;
}

.daohang-right {
  display: flex;
  align-items: center;
}

.login-link {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  background: var(--color-accent-glow-softer);
  border: 1px solid var(--color-border-dim);
  border-radius: 8px;
  color: var(--color-accent-lighter);
  text-decoration: none;
  font-size: 0.9rem;
  font-weight: 500;
  transition: all 0.3s ease;
  backdrop-filter: blur(10px);
}

.login-link:hover {
  background: var(--color-accent-glow-soft);
  border-color: var(--color-border-stronger);
  color: var(--color-white);
  transform: translateY(-1px);
}

.login-icon {
  width: 18px;
  height: 18px;
  transition: color 0.3s ease;
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
  
  .logo-icon {
    width: 24px;
    height: 24px;
  }
  
  .logo-text {
    font-size: 1rem;
  }
  
  .daohang-links {
    display: none; /* 移动端暂时隐藏导航链接 */
  }
  
  .login-link {
    padding: 6px 12px;
    font-size: 0.85rem;
  }
  
  .login-icon {
    width: 16px;
    height: 16px;
  }
}

@media (max-width: 480px) {
  .daohang-container {
    padding: 0 12px;
  }
  
  .logo-text {
    font-size: 0.9rem;
  }
  
  .login-link {
    padding: 6px 10px;
    font-size: 0.8rem;
  }
  
  .login-icon {
    width: 14px;
    height: 14px;
  }
}
</style>
