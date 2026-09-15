# Auto Import Config

自动导入配置技能，优化开发体验和构建性能。

## unplugin-auto-import

### 基础配置
```typescript
// vite.config.ts
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import AutoImport from 'unplugin-auto-import/vite'

export default defineConfig({
  plugins: [
    vue(),
    AutoImport({
      // 自动导入 Vue 相关函数
      imports: [
        'vue',
        'vue-router',
        'pinia',
        '@vueuse/core'
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
    })
  ]
})
```

### 自定义导入
```typescript
AutoImport({
  imports: [
    {
      'lodash-es': [
        // 命名导入
        'debounce',
        'throttle',
        'cloneDeep',
        // 别名导入
        ['camelCase', 'toCamelCase']
      ],
      'axios': [
        ['default', 'axios'] // 默认导入
      ]
    }
  ]
})
```

## unplugin-vue-components

### 基础配置
```typescript
// vite.config.ts
import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'

export default defineConfig({
  plugins: [
    Components({
      // 组件解析器
      resolvers: [
        ElementPlusResolver(),
        // 自定义组件解析器
        (componentName) => {
          if (componentName.startsWith('My')) {
            return {
              name: componentName,
              from: `@/components/${componentName}.vue`
            }
          }
        }
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
  ]
})
```

### 自定义组件解析
```typescript
Components({
  resolvers: [
    // 自定义组件前缀
    (componentName) => {
      if (componentName.startsWith('Base')) {
        return {
          name: componentName,
          from: '@/components/base'
        }
      }
    },
    
    // 业务组件
    (componentName) => {
      if (componentName.startsWith('Business')) {
        return {
          name: componentName,
          from: '@/components/business'
        }
      }
    }
  ]
})
```

## TypeScript 配置

### 类型声明配置
```json
// tsconfig.json
{
  "compilerOptions": {
    "types": [
      "element-plus/global",
      "unplugin-vue-components/resolvers"
    ],
    "paths": {
      "@/*": ["./src/*"]
    }
  }
}
```

### 自动生成类型
```typescript
// vite.config.ts
AutoImport({
  dts: 'src/auto-imports.d.ts',
  // 类型生成选项
  vueTemplate: true
})

Components({
  dts: 'src/components.d.ts'
})
```

## 性能优化

### 按需导入优化
```typescript
// ✅ 推荐：使用自动导入按需加载
import { ref, computed } from 'vue' // 自动导入

// ❌ 不推荐：手动全量导入
import Vue from 'vue'
```

### 组件懒加载
```typescript
// ✅ 推荐：自动导入 + 懒加载
const HeavyComponent = defineAsyncComponent(() =>
  import('@/components/HeavyComponent.vue')
)

// ❌ 不推荐：直接导入
import HeavyComponent from '@/components/HeavyComponent.vue'
```

## 最佳实践

### 配置规范
1. 合理设置自动导入范围
2. 避免过度自动导入
3. 定期清理未使用的导入
4. 保持类型声明文件更新
5. 监控构建性能影响

### 性能监控
```typescript
// 监控自动导入性能
import { performance } from 'perf_hooks'

const startTime = performance.now()

// 执行自动导入配置

const endTime = performance.now()
console.log(`自动导入配置耗时: ${endTime - startTime}ms`)
```

### 团队协作
1. 统一自动导入配置
2. 提供配置文档
3. 建立代码规范
4. 定期审查配置
5. 收集团队反馈
