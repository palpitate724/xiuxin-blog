import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig({
  base: '/admin/',
  plugins: [vue(), vueDevTools()],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
    },
  },
  // 编译时特性标志: 开发环境启用 devtools, 生产环境禁用
  define: {
    __VUE_PROD_DEVTOOLS__: JSON.stringify(process.env.NODE_ENV !== 'production'),
  },
})
