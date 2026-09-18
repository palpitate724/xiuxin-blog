<template>
  <div class="user-page">
    <div class="user-container">
      <!-- Logo 区域 -->
      <div class="logo-section">
        <Logo />
      </div>
      
      <!-- 功能组件区域 -->
      <div class="auth-section">
        <Login v-if="currentView === 'login'" @switch-to-signup="currentView = 'signup'" />
        <Signup v-if="currentView === 'signup'" @switch-to-login="currentView = 'login'" />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import Logo from '../components/user/logo.vue'
// The login component is provided by the Vue SFC compiler at runtime.
// @ts-ignore The component declaration may not expose its generated default export to TypeScript.
import Login from '../components/user/login.vue'
// The signup component is provided by the Vue SFC compiler at runtime.
// @ts-ignore The component declaration may not expose its generated default export to TypeScript.
import Signup from '../components/user/signup.vue'

// 当前视图状态
const currentView = ref<'login' | 'signup'>('login')
</script>

<style scoped>
.user-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  position: relative;
  z-index: 3;
}

/* 移动端适配 */
@media (max-width: 768px) {
  .user-page {
    padding: 16px;
  }
}

.user-container {
  width: 100%;
  max-width: 1200px;
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 80px;
  align-items: center;
  position: relative;
}

.logo-section {
  display: flex;
  justify-content: center;
  align-items: center;
}

.auth-section {
  display: flex;
  justify-content: center;
  align-items: center;
}

/* 移动端适配 */
@media (max-width: 768px) {
  .user-container {
    grid-template-columns: 1fr;
    gap: 40px;
    max-width: 100%;
  }
  
  .logo-section {
    order: -1;
  }
  
  .auth-section {
    width: 100%;
  }
}

@media (max-width: 480px) {
  .user-page {
    padding: 12px;
  }
  
  .user-container {
    gap: 24px;
  }
}
</style>
