# Vue3 组件标准

Vue3 组件开发的标准规范和最佳实践。

## 组件命名规范

- 组件名使用 PascalCase
- 组件文件名使用 kebab-case
- 组件名应该是多词的，避免与 HTML 标签冲突

## 组件结构规范

```vue
<template>
  <!-- 模板 -->
</template>

<script setup lang="ts">
// 脚本逻辑
</script>

<style scoped>
/* 样式 */
</style>
```

## Props 定义规范

使用 TypeScript 接口定义 props：

```typescript
interface Props {
  title: string
  count?: number
  disabled?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  count: 0,
  disabled: false
})
```

## Emits 定义规范

```typescript
interface Emits {
  (e: 'update:modelValue', value: string): void
  (e: 'submit', data: FormData): void
}

const emit = defineEmits<Emits>()
```

## 生命周期规范

在 `<script setup>` 中，直接使用生命周期钩子：

```typescript
import { onMounted, onUnmounted } from 'vue'

onMounted(() => {
  // 组件挂载后
})

onUnmounted(() => {
  // 组件卸载前
})
```

## 样式规范

- 使用 scoped 样式避免污染
- 优先使用 CSS Modules 或 Tailwind CSS
- 避免深层选择器
