# Props 和 Slots 转发模式

Vue3 组件中 props 和 slots 的转发最佳实践。

## Props 转发

### 基础转发

```vue
<template>
  <el-button v-bind="$attrs">自定义按钮</el-button>
</template>

<script setup lang="ts">
// 不需要定义 props，直接通过 $attrs 转发
</script>
```

### 选择性转发

```vue
<template>
  <el-button
    :type="type"
    :size="size"
    :disabled="disabled"
    v-bind="filteredAttrs"
  >
    <slot />
  </el-button>
</template>

<script setup lang="ts">
import { computed, useAttrs } from 'vue'

interface Props {
  type?: 'primary' | 'success' | 'warning' | 'danger'
  size?: 'large' | 'default' | 'small'
  disabled?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  type: 'default',
  size: 'default',
  disabled: false
})

const attrs = useAttrs()

const filteredAttrs = computed(() => {
  const { type, size, disabled, ...rest } = attrs
  return rest
})
</script>
```

### 透传转发

```vue
<template>
  <div class="wrapper">
    <el-input v-bind="$attrs" />
  </div>
</template>

<script setup lang="ts">
// inheritAttrs: false 让我们可以手动控制转发
defineOptions({
  inheritAttrs: false
})
</script>
```

## Slots 转发

### 基础转发

```vue
<template>
  <el-card>
    <template #header>
      <slot name="header">
        <span>默认标题</span>
      </slot>
    </template>
    
    <slot />
    
    <template #footer>
      <slot name="footer" />
    </template>
  </el-card>
</template>
```

### 动态插槽转发

```vue
<template>
  <el-dialog v-bind="$attrs">
    <template
      v-for="(_, name) in $slots"
      #[name]="slotData"
    >
      <slot :name="name" v-bind="slotData || {}" />
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
defineOptions({
  inheritAttrs: false
})
</script>
```

### 条件插槽转发

```vue
<template>
  <el-table :data="data">
    <el-table-column
      v-for="column in columns"
      :key="column.prop"
      :prop="column.prop"
      :label="column.label"
    >
      <template #default="scope">
        <slot
          v-if="$slots[column.prop]"
          :name="column.prop"
          v-bind="scope"
        />
        <span v-else>{{ scope.row[column.prop] }}</span>
      </template>
    </el-table-column>
  </el-table>
</template>
```

## 组合转发模式

```vue
<template>
  <el-form
    ref="formRef"
    :model="modelValue"
    :rules="rules"
    v-bind="formAttrs"
  >
    <el-form-item
      v-for="field in fields"
      :key="field.prop"
      :label="field.label"
      :prop="field.prop"
    >
      <component
        :is="getComponent(field.type)"
        v-model="modelValue[field.prop]"
        v-bind="field.attrs"
      >
        <template
          v-for="(_, name) in getFieldSlots(field)"
          #[name]="slotData"
        >
          <slot :name="name" v-bind="slotData || {}" />
        </template>
      </component>
    </el-form-item>
    
    <slot name="actions" />
  </el-form>
</template>

<script setup lang="ts">
import { computed, useAttrs } from 'vue'

interface Props {
  modelValue: Record<string, any>
  fields: any[]
  rules?: any
}

const props = defineProps<Props>()
const attrs = useAttrs()

const formAttrs = computed(() => {
  const { modelValue, fields, rules, ...rest } = attrs
  return rest
})

const getComponent = (type: string) => {
  const components: Record<string, string> = {
    input: 'el-input',
    select: 'el-select',
    date: 'el-date-picker'
  }
  return components[type] || 'el-input'
}

const getFieldSlots = (field: any) => {
  // 返回字段特定的插槽
  return {}
}
</script>
```

## 事件转发

```vue
<template>
  <el-input
    v-model="inputValue"
    @input="handleInput"
    @change="handleChange"
    @focus="handleFocus"
    @blur="handleBlur"
  />
</template>

<script setup lang="ts">
import { ref } from 'vue'

interface Props {
  modelValue: string
}

const props = defineProps<Props>()
const emit = defineEmits<{
  'update:modelValue': [value: string]
  input: [value: string]
  change: [value: string]
  focus: [event: FocusEvent]
  blur: [event: FocusEvent]
}>()

const inputValue = ref(props.modelValue)

const handleInput = (value: string) => {
  emit('update:modelValue', value)
  emit('input', value)
}

const handleChange = (value: string) => {
  emit('change', value)
}

const handleFocus = (event: FocusEvent) => {
  emit('focus', event)
}

const handleBlur = (event: FocusEvent) => {
  emit('blur', event)
}
</script>
```

## 完整封装示例

```vue
<template>
  <div class="custom-component">
    <el-dialog
      v-model="visible"
      :title="title"
      :width="width"
      :before-close="handleBeforeClose"
      v-bind="dialogAttrs"
    >
      <slot name="default">
        <p>默认内容</p>
      </slot>
      
      <template #footer>
        <slot name="footer">
          <el-button @click="handleCancel">取消</el-button>
          <el-button type="primary" @click="handleConfirm">确认</el-button>
        </slot>
      </template>
      
      <!-- 转发所有其他插槽 -->
      <template
        v-for="(_, name) in dialogSlots"
        #[name]="slotData"
      >
        <slot :name="name" v-bind="slotData || {}" />
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { computed, useAttrs, useSlots } from 'vue'

interface Props {
  modelValue: boolean
  title?: string
  width?: string
}

const props = withDefaults(defineProps<Props>(), {
  title: '对话框',
  width: '50%'
})

const emit = defineEmits<{
  'update:modelValue': [value: boolean]
  confirm: []
  cancel: []
  'before-close': [done: () => void]
}>()

const attrs = useAttrs()
const slots = useSlots()

const visible = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
})

// 过滤掉已处理的 props
const dialogAttrs = computed(() => {
  const { modelValue, title, width, ...rest } = attrs
  return rest
})

// 过滤掉默认插槽和 footer 插槽
const dialogSlots = computed(() => {
  const { default: _, footer: __, ...rest } = slots
  return rest
})

const handleBeforeClose = (done: () => void) => {
  emit('before-close', done)
}

const handleConfirm = () => {
  emit('confirm')
  visible.value = false
}

const handleCancel = () => {
  emit('cancel')
  visible.value = false
}
</script>
```
