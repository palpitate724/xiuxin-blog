import { createApp } from 'vue'
import { createPinia } from 'pinia'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'

import App from './App.vue'
import router from './router'

// 全局样式 — 设计令牌 + 重置
import './styles/tokens.css'
import './styles/global.css'

const app = createApp(App)
const pinia = createPinia()
pinia.use(piniaPluginPersistedstate)
// 注册插件
app.use(pinia)
app.use(router)

// 生产环境禁用 devtools
// Vue 3.6+: 使用编译时特性标志 __VUE_PROD_DEVTOOLS__
// (在 vite.config.ts 中通过 define 设置, 开发环境自动为 true)

// 全局错误处理
app.config.errorHandler = (err, instance, info) => {
  console.error('[Vue Error]', err, info)
}

app.mount('#app')
