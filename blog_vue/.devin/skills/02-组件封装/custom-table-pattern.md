# Custom Table 组件封装模式

基于 Element Plus 的表格组件封装模式。

## 基础封装

```vue
<template>
  <el-table
    :data="data"
    :loading="loading"
    v-bind="$attrs"
    @selection-change="handleSelectionChange"
  >
    <el-table-column
      v-if="selectable"
      type="selection"
      width="55"
    />
    
    <template
      v-for="column in columns"
      :key="column.prop"
    >
      <el-table-column
        :prop="column.prop"
        :label="column.label"
        :width="column.width"
        :min-width="column.minWidth"
        :fixed="column.fixed"
        :sortable="column.sortable"
      >
        <template #default="{ row }">
          <slot
            v-if="column.slot"
            :name="column.slot"
            :row="row"
            :column="column"
          >
            {{ row[column.prop] }}
          </slot>
          <span v-else>{{ row[column.prop] }}</span>
        </template>
      </el-table-column>
    </template>
    
    <template #empty>
      <slot name="empty">
        <el-empty description="暂无数据" />
      </slot>
    </template>
  </el-table>
</template>

<script setup lang="ts">
import { ref } from 'vue'

interface Column {
  prop: string
  label: string
  width?: number
  minWidth?: number
  fixed?: boolean | 'left' | 'right'
  sortable?: boolean
  slot?: string
}

interface Props {
  data: any[]
  columns: Column[]
  loading?: boolean
  selectable?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  loading: false,
  selectable: false
})

const emit = defineEmits<{
  selectionChange: [selection: any[]]
}>()

const handleSelectionChange = (selection: any[]) => {
  emit('selectionChange', selection)
}
</script>
```

## 使用示例

```vue
<template>
  <CustomTable
    :data="tableData"
    :columns="columns"
    :loading="loading"
    selectable
    @selection-change="handleSelectionChange"
  >
    <template #status="{ row }">
      <el-tag :type="row.status === 'active' ? 'success' : 'danger'">
        {{ row.status }}
      </el-tag>
    </template>
  </CustomTable>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import CustomTable from '@/components/common/CustomTable.vue'

const tableData = ref([
  { id: 1, name: '张三', status: 'active' },
  { id: 2, name: '李四', status: 'inactive' }
])

const columns = ref([
  { prop: 'id', label: 'ID', width: 80 },
  { prop: 'name', label: '姓名', width: 120 },
  { prop: 'status', label: '状态', slot: 'status' }
])

const loading = ref(false)

const handleSelectionChange = (selection: any[]) => {
  console.log('选中项:', selection)
}
</script>
```

## 高级功能扩展

### 分页表格

```vue
<template>
  <div class="custom-table-pagination">
    <CustomTable
      :data="paginatedData"
      :columns="columns"
      :loading="loading"
    />
    
    <el-pagination
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      :total="total"
      :page-sizes="[10, 20, 50, 100]"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'

const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const paginatedData = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return props.data.slice(start, end)
})

const handleSizeChange = (size: number) => {
  pageSize.value = size
  currentPage.value = 1
}

const handleCurrentChange = (page: number) => {
  currentPage.value = page
}
</script>
```

### 远程搜索表格

```typescript
export function useRemoteTable<T>(
  fetcher: (params: any) => Promise<{ data: T[], total: number }>
) {
  const data = ref<T[]>([])
  const loading = ref(false)
  const total = ref(0)
  const currentPage = ref(1)
  const pageSize = ref(10)
  const searchParams = ref<Record<string, any>>({})
  
  const fetchData = async () => {
    loading.value = true
    try {
      const result = await fetcher({
        page: currentPage.value,
        pageSize: pageSize.value,
        ...searchParams.value
      })
      data.value = result.data
      total.value = result.total
    } finally {
      loading.value = false
    }
  }
  
  const search = (params: Record<string, any>) => {
    searchParams.value = params
    currentPage.value = 1
    fetchData()
  }
  
  const reset = () => {
    searchParams.value = {}
    currentPage.value = 1
    fetchData()
  }
  
  return {
    data,
    loading,
    total,
    currentPage,
    pageSize,
    search,
    reset,
    refresh: fetchData
  }
}
```
