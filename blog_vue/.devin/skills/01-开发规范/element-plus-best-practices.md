# Element Plus 最佳实践

Element Plus 在 Vue3 项目中的最佳使用实践。

## 按需引入配置

使用 unplugin-vue-components 实现按需引入：

```typescript
// vite.config.ts
import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'

export default defineConfig({
  plugins: [
    Components({
      resolvers: [ElementPlusResolver()]
    })
  ]
})
```

## 主题定制

```typescript
// styles/element-plus.scss
@forward 'element-plus/theme-chalk/src/common/var.scss' with (
  $colors: (
    'primary': (
      'base': #409eff,
    ),
  ),
);
```

## 组件使用规范

### Form 表单

```vue
<el-form :model="form" :rules="rules" ref="formRef">
  <el-form-item label="用户名" prop="username">
    <el-input v-model="form.username" />
  </el-form-item>
</el-form>
```

### Table 表格

```vue
<el-table :data="tableData" v-loading="loading">
  <el-table-column prop="name" label="姓名" />
  <el-table-column prop="age" label="年龄" />
</el-table>
```

## 图标使用

```vue
<template>
  <el-icon><User /></el-icon>
</template>

<script setup>
import { User } from '@element-plus/icons-vue'
</script>
```

## 响应式处理

使用 Element Plus 的响应式工具：

```typescript
import { useBreakpoints } from '@vueuse/core'

const breakpoints = useBreakpoints({
  tablet: 768,
  laptop: 1024,
  desktop: 1280
})
```
