import vue from '@vitejs/plugin-vue'
import { defineConfig } from 'vite'
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    AutoImport({
      // 自动导入 Vue、Vue Router、Pinia 相关函数
      imports: [
        'vue',
        'vue-router',
        'pinia'
      ],
      // 生成类型声明文件
      dts: 'src/auto-imports.d.ts',
      // 自动导入的目录
      dirs: [
        'src/composables',
        'src/utils',
        'src/stores'
      ],
      // 排除某些文件
      exclude: [
        'dist',
        'node_modules',
        '**/*.d.ts'
      ]
    }),
    Components({
      // 组件解析器 - 自动按需导入 Element Plus 组件
      resolvers: [
        ElementPlusResolver(),
      ],
      // 组件文件类型
      extensions: ['vue', 'tsx'],
      // 生成类型声明文件
      dts: 'src/components.d.ts',
      // 搜索组件的目录
      dirs: ['src/components'],
      // 包含的组件
      include: [/\.vue$/, /\.vue\?vue/, /\.tsx$/],
      // 排除的组件
      exclude: [/[\\/]node_modules[\\/]/, /[\\/]\.git[\\/]/, /[\\/]\.nuxt[\\/]/]
    })
  ],
  resolve: {
    alias: {
      '@': '/src'
    }
  }
})
