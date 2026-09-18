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
      
      <!-- 右侧区域：用户登录 -->
      <div class="daohang-right">
        <!-- 用户登录 -->
        <a href="/user" class="login-link">
          <svg viewBox="0 0 24 24" class="login-icon">
            <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2" stroke="currentColor" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round"/>
            <circle cx="12" cy="7" r="4" stroke="currentColor" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
          <span>登录</span>
        </a>
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
  background: rgba(10, 22, 40, 0.8);
  backdrop-filter: blur(20px);
  border-bottom: 1px solid rgba(59, 130, 246, 0.1);
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
  background: rgba(147, 197, 253, 0.3);
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
  color: white;
  letter-spacing: 1px;
  transition: color 0.3s ease;
}

.daohang-logo:hover .logo-text {
  color: #93c5fd;
}

.daohang-links {
  display: flex;
  gap: 8px;
  align-items: center;
}

.nav-divider {
  width: 1px;
  height: 16px;
  background: rgba(147, 197, 253, 0.3);
}

.nav-link {
  padding: 8px 16px;
  color: rgba(147, 197, 253, 0.8);
  text-decoration: none;
  font-size: 0.9rem;
  font-weight: 500;
  border-radius: 8px;
  transition: all 0.3s ease;
  position: relative;
}

.nav-link:hover {
  color: white;
  background: rgba(59, 130, 246, 0.1);
}

.nav-link.active {
  color: white;
  background: rgba(59, 130, 246, 0.2);
}

.nav-link.active::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 20px;
  height: 2px;
  background: #3b82f6;
  border-radius: 1px;
}

.main-divider {
  width: 1px;
  height: 24px;
  background: rgba(147, 197, 253, 0.3);
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
  background: rgba(59, 130, 246, 0.1);
  border: 1px solid rgba(59, 130, 246, 0.2);
  border-radius: 8px;
  color: rgba(147, 197, 253, 0.8);
  text-decoration: none;
  font-size: 0.9rem;
  font-weight: 500;
  transition: all 0.3s ease;
  backdrop-filter: blur(10px);
}

.login-link:hover {
  background: rgba(59, 130, 246, 0.2);
  border-color: rgba(59, 130, 246, 0.4);
  color: white;
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
