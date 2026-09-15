# VeeValidate 表单验证

使用 VeeValidate 进行 Vue3 表单验证的最佳实践。

## 安装

```bash
npm install vee-validate @vee-validate/rules
```

## 基础配置

```typescript
// main.ts
import { configure } from 'vee-validate'
import { localize, setLocale } from '@vee-validate/i18n'
import zh_CN from '@vee-validate/i18n/dist/locale/zh_CN.json'

configure({
  generateMessage: localize({ zh_CN }),
  validateOnBlur: true,
  validateOnChange: true,
  validateOnInput: false,
  validateOnModelUpdate: true
})

setLocale('zh_CN')
```

## 基础使用

```vue
<template>
  <Form @submit="onSubmit">
    <Field
      name="username"
      v-model="username"
      :rules="required"
      v-slot="{ field, errorMessage }"
    >
      <el-input
        v-bind="field"
        placeholder="请输入用户名"
      />
      <div v-if="errorMessage" class="error-message">
        {{ errorMessage }}
      </div>
    </Field>
    
    <Field
      name="email"
      v-model="email"
      :rules="emailRule"
      v-slot="{ field, errorMessage }"
    >
      <el-input
        v-bind="field"
        placeholder="请输入邮箱"
      />
      <div v-if="errorMessage" class="error-message">
        {{ errorMessage }}
      </div>
    </Field>
    
    <el-button type="primary" native-type="submit">提交</el-button>
  </Form>
</template>

<script setup lang="ts">
import { Form, Field } from 'vee-validate'
import { required, email } from '@vee-validate/rules'

const username = ref('')
const email = ref('')

const emailRule = (value: string) => {
  if (!value) return '请输入邮箱'
  if (!email(value)) return '请输入正确的邮箱地址'
  return true
}

const onSubmit = (values: any) => {
  console.log('表单提交:', values)
}
</script>
```

## 自定义验证规则

```typescript
import { defineRule } from 'vee-validate'

// 手机号验证
defineRule('phone', (value: string) => {
  if (!value) return true
  if (!/^1[3-9]\d{9}$/.test(value)) {
    return '请输入正确的手机号码'
  }
  return true
})

// 强密码验证
defineRule('strongPassword', (value: string) => {
  if (!value) return true
  if (!/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}$/.test(value)) {
    return '密码必须包含大小写字母和数字，至少8位'
  }
  return true
})

// 确认密码验证
defineRule('confirmed', (value: string, [target]: any, ctx: any) => {
  if (value === ctx.form[target]) {
    return true
  }
  return '两次输入的密码不一致'
})
```

## 组合验证

```vue
<template>
  <Form @submit="onSubmit" v-slot="{ errors }">
    <Field
      name="password"
      v-model="password"
      :rules="passwordRules"
      v-slot="{ field, errorMessage }"
    >
      <el-input
        v-bind="field"
        type="password"
        placeholder="请输入密码"
      />
      <div v-if="errorMessage" class="error-message">
        {{ errorMessage }}
      </div>
    </Field>
    
    <Field
      name="confirmPassword"
      v-model="confirmPassword"
      :rules="confirmPasswordRules"
      v-slot="{ field, errorMessage }"
    >
      <el-input
        v-bind="field"
        type="password"
        placeholder="请确认密码"
      />
      <div v-if="errorMessage" class="error-message">
        {{ errorMessage }}
      </div>
    </Field>
    
    <el-button type="primary" native-type="submit">提交</el-button>
  </Form>
</template>

<script setup lang="ts">
import { Form, Field } from 'vee-validate'
import { required, min } from '@vee-validate/rules'

const password = ref('')
const confirmPassword = ref('')

const passwordRules = {
  required: true,
  min: 8,
  strongPassword: true
}

const confirmPasswordRules = {
  required: true,
  confirmed: ['password']
}

const onSubmit = (values: any) => {
  console.log('表单提交:', values)
}
</script>
```

## 动态表单验证

```vue
<template>
  <Form @submit="onSubmit">
    <div v-for="(field, index) in dynamicFields" :key="index">
      <Field
        :name="`fields.${index}.value`"
        v-model="field.value"
        :rules="field.required ? 'required' : ''"
        v-slot="{ field: fieldProps, errorMessage }"
      >
        <el-input
          v-bind="fieldProps"
          :placeholder="field.placeholder"
        />
        <div v-if="errorMessage" class="error-message">
          {{ errorMessage }}
        </div>
      </Field>
    </div>
    
    <el-button @click="addField">添加字段</el-button>
    <el-button type="primary" native-type="submit">提交</el-button>
  </Form>
</template>

<script setup lang="ts">
import { Form, Field } from 'vee-validate'
import { required } from '@vee-validate/rules'

interface DynamicField {
  value: string
  placeholder: string
  required: boolean
}

const dynamicFields = ref<DynamicField[]>([
  { value: '', placeholder: '字段1', required: true }
])

const addField = () => {
  dynamicFields.value.push({
    value: '',
    placeholder: `字段${dynamicFields.value.length + 1}`,
    required: false
  })
}

const onSubmit = (values: any) => {
  console.log('表单提交:', values)
}
</script>
```

## 异步验证

```vue
<template>
  <Form @submit="onSubmit">
    <Field
      name="username"
      v-model="username"
      :rules="validateUsername"
      v-slot="{ field, errorMessage, meta }"
    >
      <el-input
        v-bind="field"
        placeholder="请输入用户名"
      />
      <div v-if="errorMessage" class="error-message">
        {{ errorMessage }}
      </div>
      <div v-if="meta.validating" class="validating">
        验证中...
      </div>
    </Field>
    
    <el-button type="primary" native-type="submit">提交</el-button>
  </Form>
</template>

<script setup lang="ts">
import { Form, Field } from 'vee-validate'
import { required } from '@vee-validate/rules'

const username = ref('')

const validateUsername = async (value: string) => {
  if (!value) return '请输入用户名'
  
  try {
    const response = await fetch(`/api/check-username?username=${value}`)
    const data = await response.json()
    
    if (!data.available) {
      return '用户名已存在'
    }
    return true
  } catch (error) {
    return '验证失败，请重试'
  }
}

const onSubmit = (values: any) => {
  console.log('表单提交:', values)
}
</script>
```

## 表单状态管理

```vue
<template>
  <Form
    @submit="onSubmit"
    v-slot="{ meta, errors, values, resetForm }"
  >
    <Field
      name="username"
      v-model="username"
      :rules="required"
      v-slot="{ field, errorMessage }"
    >
      <el-input v-bind="field" placeholder="请输入用户名" />
      <div v-if="errorMessage" class="error-message">
        {{ errorMessage }}
      </div>
    </Field>
    
    <div class="form-status">
      <p>表单状态: {{ meta.valid ? '有效' : '无效' }}</p>
      <p>是否修改: {{ meta.dirty ? '是' : '否' }}</p>
      <p>是否触摸: {{ meta.touched ? '是' : '否' }}</p>
    </div>
    
    <el-button type="primary" native-type="submit">提交</el-button>
    <el-button @click="resetForm">重置</el-button>
  </Form>
</template>

<script setup lang="ts">
import { Form, Field } from 'vee-validate'
import { required } from '@vee-validate/rules'

const username = ref('')

const onSubmit = (values: any) => {
  console.log('表单提交:', values)
}
</script>
```

## 与 Element Plus 集成

```vue
<template>
  <el-form
    ref="formRef"
    :model="formData"
    :rules="veeRules"
    label-width="120px"
    @submit.prevent="onSubmit"
  >
    <el-form-item label="用户名" prop="username">
      <el-input v-model="formData.username" />
    </el-form-item>
    
    <el-form-item label="邮箱" prop="email">
      <el-input v-model="formData.email" />
    </el-form-item>
    
    <el-form-item>
      <el-button type="primary" @click="onSubmit">提交</el-button>
    </el-form-item>
  </el-form>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useForm } from 'vee-validate'
import { required, email } from '@vee-validate/rules'

const formRef = ref()

const { handleSubmit, errors } = useForm({
  initialValues: {
    username: '',
    email: ''
  },
  validationSchema: {
    username: required,
    email: email
  }
})

const formData = reactive({
  username: '',
  email: ''
})

const veeRules = {
  username: (value: string) => {
    if (!value) return '请输入用户名'
    return true
  },
  email: (value: string) => {
    if (!value) return '请输入邮箱'
    if (!email(value)) return '请输入正确的邮箱地址'
    return true
  }
}

const onSubmit = handleSubmit(async (values) => {
  console.log('表单提交:', values)
})
</script>
```
