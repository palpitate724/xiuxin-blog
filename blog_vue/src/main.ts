import { createApp } from 'vue'
import { createPinia } from 'pinia'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import App from './App.vue'
import router from './router/index.ts'
import './styles/tokens.css'
import './styles/global.css'
// 基础 Element Plus 变量（深/亮色共用字体/字号/圆角）
import './styles/element-plus/variables.scss'
import './styles/element-plus/dark-theme.scss'

const pinia = createPinia()
pinia.use(piniaPluginPersistedstate)

const app = createApp(App)
app.use(pinia).use(router).use(ElementPlus).mount('#app')
