<template>
  <!-- 用户登录/注册页 -->
  <Login v-if="currentView === 'login'" @switch-to-signup="currentView = 'signup'" />
  <Signup v-else @switch-to-login="currentView = 'login'" />
</template>

<script setup lang="ts">
import { ref } from 'vue'
import Login from '@/components/user/login.vue'
import Signup from '@/components/user/signin.vue'

const currentView = ref<'login' | 'signup'>('login')
</script>
