# Custom Form 组件封装模式

基于 Element Plus 的表单组件封装模式。

## 基础封装

```vue
<template>
  <el-form
    ref="formRef"
    :model="formData"
    :rules="formRules"
    :label-width="labelWidth"
    v-bind="$attrs"
  >
    <el-form-item
      v-for="field in fields"
      :key="field.prop"
      :label="field.label"
      :prop="field.prop"
      :required="field.required"
    >
      <!-- 输入框 -->
      <el-input
        v-if="field.type === 'input'"
        v-model="formData[field.prop]"
        :placeholder="field.placeholder"
        :disabled="field.disabled"
        :type="field.inputType || 'text'"
      />
      
      <!-- 选择器 -->
      <el-select
        v-else-if="field.type === 'select'"
        v-model="formData[field.prop]"
        :placeholder="field.placeholder"
        :disabled="field.disabled"
        :multiple="field.multiple"
      >
        <el-option
          v-for="option in field.options"
          :key="option.value"
          :label="option.label"
          :value="option.value"
        />
      </el-select>
      
      <!-- 日期选择器 -->
      <el-date-picker
        v-else-if="field.type === 'date'"
        v-model="formData[field.prop]"
        :type="field.dateType || 'date'"
        :placeholder="field.placeholder"
        :disabled="field.disabled"
      />
      
      <!-- 数字输入 -->
      <el-input-number
        v-else-if="field.type === 'number'"
        v-model="formData[field.prop]"
        :min="field.min"
        :max="field.max"
        :step="field.step"
        :disabled="field.disabled"
      />
      
      <!-- 开关 -->
      <el-switch
        v-else-if="field.type === 'switch'"
        v-model="formData[field.prop]"
        :disabled="field.disabled"
      />
      
      <!-- 自定义插槽 -->
      <slot
        v-else-if="field.type === 'slot'"
        :name="field.slot"
        :field="field"
        :value="formData[field.prop]"
      />
    </el-form-item>
    
    <el-form-item v-if="showActions">
      <el-button type="primary" @click="handleSubmit">提交</el-button>
      <el-button @click="handleReset">重置</el-button>
    </el-form-item>
  </el-form>
</template>

<script setup lang="ts">
import { ref, reactive, watch } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'

interface FieldOption {
  label: string
  value: any
}

interface FormField {
  prop: string
  label: string
  type: 'input' | 'select' | 'date' | 'number' | 'switch' | 'slot'
  placeholder?: string
  required?: boolean
  disabled?: boolean
  inputType?: 'text' | 'password' | 'textarea'
  options?: FieldOption[]
  multiple?: boolean
  dateType?: 'date' | 'datetime' | 'daterange'
  min?: number
  max?: number
  step?: number
  slot?: string
}

interface Props {
  modelValue: Record<string, any>
  fields: FormField[]
  rules?: FormRules
  labelWidth?: string
  showActions?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  rules: () => ({}),
  labelWidth: '120px',
  showActions: true
})

const emit = defineEmits<{
  'update:modelValue': [value: Record<string, any>]
  submit: [data: Record<string, any>]
  reset: []
}>()

const formRef = ref<FormInstance>()
const formData = reactive({ ...props.modelValue })

watch(() => props.modelValue, (newValue) => {
  Object.assign(formData, newValue)
}, { deep: true })

watch(formData, (newValue) => {
  emit('update:modelValue', { ...newValue })
}, { deep: true })

const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate((valid) => {
    if (valid) {
      emit('submit', { ...formData })
    }
  })
}

const handleReset = () => {
  formRef.value?.resetFields()
  emit('reset')
}

const validate = () => {
  return formRef.value?.validate()
}

const resetFields = () => {
  formRef.value?.resetFields()
}

defineExpose({
  validate,
  resetFields
})
</script>
```

## 使用示例

```vue
<template>
  <CustomForm
    v-model="formData"
    :fields="formFields"
    :rules="formRules"
    @submit="handleSubmit"
  />
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import CustomForm from '@/components/common/CustomForm.vue'

const formData = reactive({
  username: '',
  email: '',
  role: '',
  birthDate: '',
  age: 18,
  active: true
})

const formFields = ref([
  {
    prop: 'username',
    label: '用户名',
    type: 'input',
    placeholder: '请输入用户名',
    required: true
  },
  {
    prop: 'email',
    label: '邮箱',
    type: 'input',
    inputType: 'email',
    placeholder: '请输入邮箱',
    required: true
  },
  {
    prop: 'role',
    label: '角色',
    type: 'select',
    placeholder: '请选择角色',
    options: [
      { label: '管理员', value: 'admin' },
      { label: '用户', value: 'user' }
    ]
  },
  {
    prop: 'birthDate',
    label: '出生日期',
    type: 'date',
    dateType: 'date',
    placeholder: '选择日期'
  },
  {
    prop: 'age',
    label: '年龄',
    type: 'number',
    min: 0,
    max: 120
  },
  {
    prop: 'active',
    label: '状态',
    type: 'switch'
  }
])

const formRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ]
}

const handleSubmit = (data: any) => {
  console.log('表单提交:', data)
}
</script>
```

## 动态表单

```typescript
export function useDynamicForm<T extends Record<string, any>>(
  initialValues: T
) {
  const formData = reactive<T>({ ...initialValues })
  const formFields = ref<FormField[]>([])
  
  const addField = (field: FormField) => {
    formFields.value.push(field)
    formData[field.prop] = initialValues[field.prop] || ''
  }
  
  const removeField = (prop: string) => {
    const index = formFields.value.findIndex(f => f.prop === prop)
    if (index > -1) {
      formFields.value.splice(index, 1)
      delete formData[prop]
    }
  }
  
  const updateField = (prop: string, updates: Partial<FormField>) => {
    const field = formFields.value.find(f => f.prop === prop)
    if (field) {
      Object.assign(field, updates)
    }
  }
  
  return {
    formData,
    formFields,
    addField,
    removeField,
    updateField
  }
}
```

## 表单验证增强

```typescript
import type { FormRules } from 'element-plus'

export function createFormRules(schema: Record<string, any>): FormRules {
  const rules: FormRules = {}
  
  for (const [field, config] of Object.entries(schema)) {
    rules[field] = []
    
    if (config.required) {
      rules[field].push({
        required: true,
        message: config.message || `请输入${config.label}`,
        trigger: config.trigger || 'blur'
      })
    }
    
    if (config.pattern) {
      rules[field].push({
        pattern: config.pattern,
        message: config.patternMessage || '格式不正确',
        trigger: config.trigger || 'blur'
      })
    }
    
    if (config.min !== undefined || config.max !== undefined) {
      rules[field].push({
        min: config.min,
        max: config.max,
        message: config.lengthMessage || `长度在 ${config.min} 到 ${config.max} 个字符`,
        trigger: config.trigger || 'blur'
      })
    }
    
    if (config.validator) {
      rules[field].push({
        validator: config.validator,
        trigger: config.trigger || 'blur'
      })
    }
  }
  
  return rules
}
```
