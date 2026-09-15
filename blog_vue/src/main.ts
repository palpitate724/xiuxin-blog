import { createApp } from 'vue'
import { createPinia } from 'pinia'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'
import App from './App.vue'
import router from './router/index.ts'
import './styles/global.css'

const pinia = createPinia()

pinia.use(piniaPluginPersistedstate)

const app = createApp(App)

app.use(pinia).use(router).mount('#app')

