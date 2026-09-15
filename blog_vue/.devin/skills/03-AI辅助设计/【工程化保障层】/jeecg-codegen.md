# Jeecg CodeGen

Jeecg 低代码代码生成技能，提供快速开发能力。

## JeecgBoot 简介

JeecgBoot 是一款基于代码生成器的低代码平台，可以通过在线配置快速生成前后端代码。

### 核心特性
- 代码生成器：在线生成表单、列表、树形代码
- 低代码平台：可视化表单设计
- 流程引擎：工作流集成
- 权限管理：细粒度权限控制
- 移动端支持：多端适配

## 代码生成配置

### 数据库表设计
```sql
-- 创建示例表
CREATE TABLE `demo_user` (
  `id` varchar(36) NOT NULL COMMENT '主键',
  `name` varchar(100) DEFAULT NULL COMMENT '姓名',
  `age` int(11) DEFAULT NULL COMMENT '年龄',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) DEFAULT NULL COMMENT '电话',
  `status` int(1) DEFAULT '1' COMMENT '状态(1正常 0停用)',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户示例表';
```

### 在线代码生成
```
1. 登录 JeecgBoot 系统
2. 进入 "在线开发" -> "代码生成"
3. 选择数据库表
4. 配置生成参数
5. 预览生成代码
6. 下载代码到本地
```

## Vue3 代码生成

### 表单组件生成
```vue
<!-- 生成的表单组件 -->
<template>
  <a-form
    ref="formRef"
    :model="formData"
    :rules="rules"
    :label-col="labelCol"
    :wrapper-col="wrapperCol"
  >
    <a-form-item label="姓名" name="name">
      <a-input v-model:value="formData.name" placeholder="请输入姓名" />
    </a-form-item>
    
    <a-form-item label="年龄" name="age">
      <a-input-number
        v-model:value="formData.age"
        placeholder="请输入年龄"
        :min="0"
        :max="120"
      />
    </a-form-item>
    
    <a-form-item label="邮箱" name="email">
      <a-input v-model:value="formData.email" placeholder="请输入邮箱" />
    </a-form-item>
    
    <a-form-item label="电话" name="phone">
      <a-input v-model:value="formData.phone" placeholder="请输入电话" />
    </a-form-item>
    
    <a-form-item label="状态" name="status">
      <a-radio-group v-model:value="formData.status">
        <a-radio :value="1">正常</a-radio>
        <a-radio :value="0">停用</a-radio>
      </a-radio-group>
    </a-form-item>
  </a-form>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'

const formRef = ref()
const labelCol = { span: 4 }
const wrapperCol = { span: 14 }

const formData = reactive({
  id: '',
  name: '',
  age: undefined,
  email: '',
  phone: '',
  status: 1
})

const rules = {
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ]
}
</script>
```

### 列表组件生成
```vue
<!-- 生成的列表组件 -->
<template>
  <div class="user-list">
    <div class="table-operations">
      <a-button type="primary" @click="handleAdd">新增</a-button>
      <a-button @click="handleBatchDelete">批量删除</a-button>
    </div>
    
    <a-table
      :columns="columns"
      :data-source="dataSource"
      :loading="loading"
      :row-selection="rowSelection"
      :pagination="pagination"
      @change="handleTableChange"
    >
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'status'">
          <a-tag :color="record.status === 1 ? 'green' : 'red'">
            {{ record.status === 1 ? '正常' : '停用' }}
          </a-tag>
        </template>
        
        <template v-if="column.key === 'action'">
          <a @click="handleEdit(record)">编辑</a>
          <a-divider type="vertical" />
          <a @click="handleDelete(record)">删除</a>
        </template>
      </template>
    </a-table>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { message } from 'ant-design-vue'

const columns = [
  { title: '姓名', dataIndex: 'name', key: 'name' },
  { title: '年龄', dataIndex: 'age', key: 'age' },
  { title: '邮箱', dataIndex: 'email', key: 'email' },
  { title: '电话', dataIndex: 'phone', key: 'phone' },
  { title: '状态', dataIndex: 'status', key: 'status' },
  { title: '操作', key: 'action', fixed: 'right', width: 150 }
]

const dataSource = ref([])
const loading = ref(false)
const selectedRowKeys = ref([])

const pagination = reactive({
  current: 1,
  pageSize: 10,
  total: 0,
  showSizeChanger: true,
  showTotal: (total: number) => `共 ${total} 条`
})

const rowSelection = {
  selectedRowKeys: selectedRowKeys,
  onChange: (keys: string[]) => {
    selectedRowKeys.value = keys
  }
}

const loadData = async () => {
  loading.value = true
  try {
    const response = await api.list({
      pageNo: pagination.current,
      pageSize: pagination.pageSize
    })
    dataSource.value = response.records
    pagination.total = response.total
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  // 新增逻辑
}

const handleEdit = (record: any) => {
  // 编辑逻辑
}

const handleDelete = (record: any) => {
  // 删除逻辑
}

const handleBatchDelete = () => {
  // 批量删除逻辑
}

const handleTableChange = (pag: any) => {
  pagination.current = pag.current
  pagination.pageSize = pag.pageSize
  loadData()
}

onMounted(() => {
  loadData()
})
</script>
```

## API 接口生成

### API 服务生成
```typescript
// api/user.ts
import { defHttp } from '@/utils/http/axios'

enum Api {
  list = '/demo/user/list',
  save = '/demo/user/add',
  edit = '/demo/user/edit',
  delete = '/demo/user/delete',
  deleteBatch = '/demo/user/deleteBatch',
  exportXls = '/demo/user/exportXls',
  importExcel = '/demo/user/importExcel'
}

export const list = (params: any) =>
  defHttp.get<any>({ url: Api.list, params })

export const save = (params: any) =>
  defHttp.post<any>({ url: Api.save, params })

export const edit = (params: any) =>
  defHttp.put<any>({ url: Api.edit, params })

export const deleteOne = (params: any) =>
  defHttp.delete<any>({ url: Api.delete, params })

export const deleteBatch = (params: any) =>
  defHttp.delete<any>({ url: Api.deleteBatch, params })

export const exportXls = (params: any) =>
  defHttp.get<any>({ url: Api.exportXls, params, responseType: 'blob' })

export const importExcel = (params: any) =>
  defHttp.upload<any>({ url: Api.importExcel, params })
```

## 自定义模板

### Vue3 模板定制
```velocity
## 自定义 Vue3 列表模板
<template>
  <div class="${entityName}-list">
    <div class="table-operations">
      <a-button type="primary" @click="handleAdd">新增</a-button>
    </div>
    
    <a-table
      :columns="columns"
      :data-source="dataSource"
      :loading="loading"
      :pagination="pagination"
      @change="handleTableChange"
    >
#foreach($field in $fields)
#if($field.showInList)
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === '${field.fieldName}'">
          {{ record.${field.fieldName} }}
        </template>
      </template>
#end
#end
    </a-table>
  </div>
</template>
```

### TypeScript 类型生成
```typescript
// types/user.ts
export interface User {
  id: string
  name: string
  age: number
  email: string
  phone: string
  status: number
  createBy: string
  createTime: string
  updateBy: string
  updateTime: string
}

export interface UserQuery {
  pageNo: number
  pageSize: number
  name?: string
  email?: string
  status?: number
}
```

## 最佳实践

### 代码生成规范
1. 数据库表设计遵循命名规范
2. 字段注释完整清晰
3. 合理设置字段类型和长度
4. 添加必要的索引
5. 遵循数据库设计范式

### 生成后优化
1. 添加必要的业务逻辑
2. 优化表单验证规则
3. 完善错误处理
4. 添加单元测试
5. 优化性能和用户体验

### 集成到现有项目
1. 调整代码风格匹配项目规范
2. 集成到现有的路由系统
3. 统一状态管理
4. 调整 API 接口调用
5. 适配主题和样式
