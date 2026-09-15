# Regle 表单验证

使用 Regle 进行 Vue3 表单验证的最佳实践。

## 安装

```bash
npm install @regle/core @regle/validators
```

## 基础使用

```typescript
import { useForm, useField } from '@regle/core'
import { required, email, minLength } from '@regle/validators'

export function useLoginForm() {
  const { form, handleSubmit, isValid } = useForm({
    username: useField('', {
      validators: [required(), minLength(3)]
    }),
    email: useField('', {
      validators: [required(), email()]
    }),
    password: useField('', {
      validators: [required(), minLength(6)]
    })
  })
  
  const onSubmit = handleSubmit(async (values) => {
    console.log('表单提交:', values)
    // 提交逻辑
  })
  
  return {
    form,
    isValid,
    onSubmit
  }
}
```

## 自定义验证器

```typescript
import { createValidator } from '@regle/validators'

export const phone = createValidator({
  validator: (value: string) => {
    return /^1[3-9]\d{9}$/.test(value)
  },
  message: '请输入正确的手机号码'
})

export const strongPassword = createValidator({
  validator: (value: string) => {
    return /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}$/.test(value)
  },
  message: '密码必须包含大小写字母和数字，至少8位'
})

export const confirmPassword = (passwordField: string) => {
  return createValidator({
    validator: (value: string, context: any) => {
      return value === context.form[passwordField]
    },
    message: '两次输入的密码不一致'
  })
}
```

## 异步验证

```typescript
import { createValidator } from '@regle/validators'

export const uniqueUsername = createValidator({
  validator: async (value: string) => {
    const response = await fetch(`/api/check-username?username=${value}`)
    const data = await response.json()
    return data.available
  },
  message: '用户名已存在',
  async: true
})

export function useRegistrationForm() {
  const { form, handleSubmit, isSubmitting } = useForm({
    username: useField('', {
      validators: [
        required(),
        minLength(3),
        uniqueUsername()
      ]
    })
  })
  
  return {
    form,
    handleSubmit,
    isSubmitting
  }
}
```

## 条件验证

```typescript
import { useForm, useField } from '@regle/core'
import { required, email } from '@regle/validators'

export function useDynamicForm() {
  const { form, watch } = useForm({
    hasEmail: useField(false),
    email: useField('', {
      validators: [
        required(),
        email()
      ],
      when: (context) => context.form.hasEmail.value
    })
  })
  
  return {
    form,
    watch
  }
}
```

## 集成 Element Plus

```vue
<template>
  <el-form
    ref="formRef"
    :model="form"
    :rules="validationRules"
    label-width="120px"
  >
    <el-form-item label="用户名" prop="username">
      <el-input v-model="form.username" />
      <div v-if="errors.username" class="error-message">
        {{ errors.username }}
      </div>
    </el-form-item>
    
    <el-form-item label="邮箱" prop="email">
      <el-input v-model="form.email" />
      <div v-if="errors.email" class="error-message">
        {{ errors.email }}
      </div>
    </el-form-item>
    
    <el-form-item>
      <el-button
        type="primary"
        :disabled="!isValid"
        @click="onSubmit"
      >
        提交
      </el-button>
    </el-form-item>
  </el-form>
</template>

<script setup lang="ts">
import { useForm, useField } from '@regle/core'
import { required, email, minLength } from '@regle/validators'

const { form, errors, isValid, handleSubmit } = useForm({
  username: useField('', {
    validators: [required(), minLength(3)]
  }),
  email: useField('', {
    validators: [required(), email()]
  })
})

const onSubmit = handleSubmit(async (values) => {
  console.log('提交表单:', values)
})
</script>

<style scoped>
.error-message {
  color: #f56c6c;
  font-size: 12px;
  margin-top: 4px;
}
</style>
```

## 组合验证器

```typescript
import { createValidator } from '@regle/validators'

export const passwordPolicy = createValidator({
  validator: (value: string) => {
    const checks = {
      length: value.length >= 8,
      uppercase: /[A-Z]/.test(value),
      lowercase: /[a-z]/.test(value),
      number: /\d/.test(value),
      special: /[!@#$%^&*]/.test(value)
    }
    
    return Object.values(checks).every(Boolean)
  },
  message: '密码必须满足复杂性要求'
})

export const detailedPassword = createValidator({
  validator: (value: string) => {
    const errors: string[] = []
    
    if (value.length < 8) errors.push('至少8位')
    if (!/[A-Z]/.test(value)) errors.push('包含大写字母')
    if (!/[a-z]/.test(value)) errors.push('包含小写字母')
    if (!/\d/.test(value)) errors.push('包含数字')
    if (!/[!@#$%^&*]/.test(value)) errors.push('包含特殊字符')
    
    return {
      valid: errors.length === 0,
      message: errors.join('、')
    }
  },
  message: '密码不满足要求'
})
```

## 表单状态管理

```typescript
import { useForm, useField } from '@regle/core'
import { required } from '@regle/validators'

export function useFormState() {
  const { form, errors, touched, dirty, isValid, isDirty } = useForm({
    username: useField('', {
      validators: [required()]
    }),
    email: useField('', {
      validators: [required()]
    })
  })
  
  const resetForm = () => {
    Object.keys(form).forEach(key => {
      form[key].value = ''
    })
  }
  
  const getFieldState = (fieldName: string) => {
    return {
      value: form[fieldName].value,
      error: errors[fieldName],
      touched: touched[fieldName],
      dirty: dirty[fieldName]
    }
  }
  
  return {
    form,
    errors,
    touched,
    dirty,
    isValid,
    isDirty,
    resetForm,
    getFieldState
  }
}
```
