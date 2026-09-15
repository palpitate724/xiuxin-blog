# ApexCharts Vue 技能

ApexCharts 图表组件集成技能。

## 安装配置

### 基础安装
```bash
npm install apexcharts vue3-apexcharts
```

### 插件配置
```typescript
// main.ts
import { createApp } from 'vue'
import VueApexCharts from 'vue3-apexcharts'

const app = createApp(App)
app.use(VueApexCharts)
```

## 基础图表

### 折线图
```vue
<template>
  <div>
    <apexchart
      type="line"
      height="350"
      :options="chartOptions"
      :series="series"
    />
  </div>
</template>

<script setup>
import { ref } from 'vue'

const chartOptions = ref({
  chart: {
    id: 'vuechart-example'
  },
  xaxis: {
    categories: ['1991', '1992', '1993', '1994', '1995', '1996', '1997', 1998]
  }
})

const series = ref([{
  name: 'series-1',
  data: [30, 40, 45, 50, 49, 60, 70, 91]
}])
</script>
```

### 柱状图
```vue
<template>
  <div>
    <apexchart
      type="bar"
      height="350"
      :options="chartOptions"
      :series="series"
    />
  </div>
</template>

<script setup>
import { ref } from 'vue'

const chartOptions = ref({
  chart: {
    id: 'vuechart-example'
  },
  xaxis: {
    categories: ['一月', '二月', '三月', '四月', '五月', '六月']
  },
  plotOptions: {
    bar: {
      borderRadius: 4,
      horizontal: false
    }
  }
})

const series = ref([{
  name: '收入',
  data: [44, 55, 57, 56, 61, 58]
}])
</script>
```

### 饼图
```vue
<template>
  <div>
    <apexchart
      type="pie"
      height="350"
      :options="chartOptions"
      :series="series"
    />
  </div>
</template>

<script setup>
import { ref } from 'vue'

const chartOptions = ref({
  labels: ['产品A', '产品B', '产品C', '产品D'],
  responsive: [{
    breakpoint: 480,
    options: {
      chart: {
        width: 200
      },
      legend: {
        position: 'bottom'
      }
    }
  }]
})

const series = ref([44, 55, 13, 33])
</script>
```

## 高级图表

### 面积图
```vue
<template>
  <div>
    <apexchart
      type="area"
      height="350"
      :options="chartOptions"
      :series="series"
    />
  </div>
</template>

<script setup>
import { ref } from 'vue'

const chartOptions = ref({
  chart: {
    id: 'area-chart'
  },
  dataLabels: {
    enabled: false
  },
  stroke: {
    curve: 'smooth'
  },
  xaxis: {
    type: 'datetime',
    categories: ['2018-09-19T00:00:00.000Z', '2018-09-19T01:30:00.000Z', '2018-09-19T02:30:00.000Z']
  },
  tooltip: {
    x: {
      format: 'dd/MM/yy HH:mm'
    }
  }
})

const series = ref([{
  name: 'Series 1',
  data: [31, 40, 28]
}])
</script>
```

### 混合图表
```vue
<template>
  <div>
    <apexchart
      type="line"
      height="350"
      :options="chartOptions"
      :series="series"
    />
  </div>
</template>

<script setup>
import { ref } from 'vue'

const chartOptions = ref({
  chart: {
    id: 'mixed-chart'
  },
  stroke: {
    width: [0, 2, 5],
    curve: 'smooth'
  },
  plotOptions: {
    bar: {
      columnWidth: '50%'
    }
  },
  xaxis: {
    categories: ['一', '二', '三', '四', '五', '六', '日']
  }
})

const series = ref([
  {
    name: '收入',
    type: 'column',
    data: [23, 11, 22, 27, 13, 22, 37]
  },
  {
    name: '现金',
    type: 'area',
    data: [44, 55, 41, 67, 22, 43, 21]
  },
  {
    name: '支出',
    type: 'line',
    data: [30, 25, 36, 30, 45, 35, 64]
  }
])
</script>
```

### 雷达图
```vue
<template>
  <div>
    <apexchart
      type="radar"
      height="350"
      :options="chartOptions"
      :series="series"
    />
  </div>
</template>

<script setup>
import { ref } from 'vue'

const chartOptions = ref({
  chart: {
    id: 'radar-chart'
  },
  xaxis: {
    categories: ['一月', '二月', '三月', '四月', '五月', '六月']
  }
})

const series = ref([
  {
    name: '系列 1',
    data: [65, 59, 80, 81, 56, 55]
  },
  {
    name: '系列 2',
    data: [28, 48, 40, 19, 86, 27]
  }
])
</script>
```

## 交互功能

### 缩放功能
```vue
<template>
  <div>
    <apexchart
      type="line"
      height="350"
      :options="chartOptions"
      :series="series"
    />
  </div>
</template>

<script setup>
import { ref } from 'vue'

const chartOptions = ref({
  chart: {
    id: 'zoom-chart',
    zoom: {
      enabled: true,
      type: 'x',
      autoScaleYaxis: true
    },
    toolbar: {
      show: true
    }
  },
  xaxis: {
    type: 'datetime',
    categories: ['2018-09-19T00:00:00.000Z', '2018-09-19T01:30:00.000Z']
  }
})

const series = ref([{
  name: '数据',
  data: [31, 40]
}])
</script>
```

### 工具提示
```vue
<template>
  <div>
    <apexchart
      type="line"
      height="350"
      :options="chartOptions"
      :series="series"
    />
  </div>
</template>

<script setup>
import { ref } from 'vue'

const chartOptions = ref({
  chart: {
    id: 'tooltip-chart'
  },
  tooltip: {
    theme: 'dark',
    x: {
      show: true,
      format: 'dd/MM/yy HH:mm'
    },
    marker: {
      show: true
    }
  },
  xaxis: {
    categories: ['一', '二', '三', '四', '五']
  }
})

const series = ref([{
  name: '数据',
  data: [10, 20, 30, 40, 50]
}])
</script>
```

## 响应式设计

### 自适应图表
```vue
<template>
  <div>
    <apexchart
      type="line"
      height="350"
      :options="chartOptions"
      :series="series"
    />
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useMediaQuery } from '@vueuse/core'

const isMobile = useMediaQuery('(max-width: 768px)')

const chartOptions = computed(() => ({
  chart: {
    id: 'responsive-chart',
    toolbar: {
      show: !isMobile.value
    }
  },
  xaxis: {
    categories: ['一', '二', '三', '四', '五', '六', '日'],
    labels: {
      rotate: isMobile.value ? -45 : 0,
      hideOverlappingLabels: true
    }
  },
  legend: {
    position: isMobile.value ? 'bottom' : 'top'
  }
}))

const series = ref([{
  name: '数据',
  data: [10, 20, 30, 40, 50, 60, 70]
}])
</script>
```

## 实时数据

### 动态更新
```vue
<template>
  <div>
    <apexchart
      ref="chartRef"
      type="line"
      height="350"
      :options="chartOptions"
      :series="series"
    />
    <button @click="updateData">更新数据</button>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const chartRef = ref()
const series = ref([{
  name: '实时数据',
  data: [10, 20, 30, 40, 50]
}])

const chartOptions = ref({
  chart: {
    id: 'realtime-chart',
    animations: {
      enabled: true,
      easing: 'linear',
      dynamicAnimation: {
        speed: 1000
      }
    }
  },
  xaxis: {
    categories: ['一', '二', '三', '四', '五']
  }
})

const updateData = () => {
  const newData = series.value[0].data.map(() => Math.floor(Math.random() * 100))
  series.value[0].data = newData
}
</script>
```

## 主题定制

### 深色主题
```vue
<template>
  <div>
    <apexchart
      type="line"
      height="350"
      :options="chartOptions"
      :series="series"
    />
  </div>
</template>

<script setup>
import { ref } from 'vue'

const chartOptions = ref({
  chart: {
    id: 'dark-theme-chart',
    background: '#1a1a1a',
    theme: 'dark'
  },
  theme: {
    mode: 'dark',
    palette: 'palette1'
  },
  xaxis: {
    categories: ['一', '二', '三', '四', '五']
  }
})

const series = ref([{
  name: '数据',
  data: [10, 20, 30, 40, 50]
}])
</script>
```

### 自定义颜色
```vue
<template>
  <div>
    <apexchart
      type="bar"
      height="350"
      :options="chartOptions"
      :series="series"
    />
  </div>
</template>

<script setup>
import { ref } from 'vue'

const chartOptions = ref({
  chart: {
    id: 'custom-color-chart'
  },
  plotOptions: {
    bar: {
      colors: {
        ranges: [{
          from: 0,
          to: 30,
          color: '#ff4d4f'
        }, {
          from: 31,
          to: 60,
          color: '#faad14'
        }, {
          from: 61,
          to: 100,
          color: '#52c41a'
        }]
      }
    }
  },
  xaxis: {
    categories: ['一', '二', '三', '四', '五']
  }
})

const series = ref([{
  name: '数据',
  data: [25, 50, 75, 30, 80]
}])
</script>
```

## 导出功能

### 图表导出
```vue
<template>
  <div>
    <apexchart
      type="line"
      height="350"
      :options="chartOptions"
      :series="series"
    />
  </div>
</template>

<script setup>
import { ref } from 'vue'

const chartOptions = ref({
  chart: {
    id: 'export-chart',
    toolbar: {
      show: true,
      tools: {
        download: true,
        selection: false,
        zoom: false,
        zoomin: false,
        zoomout: false,
        pan: false,
        reset: false
      },
      export: {
        csv: {
          filename: 'data',
          columnDelimiter: ',',
          headerCategory: 'category',
          headerValue: 'value',
          dateFormatter: function(timestamp) {
            return new Date(timestamp).toDateString()
          }
        },
        svg: {
          filename: 'chart'
        },
        png: {
          filename: 'chart'
        }
      }
    }
  },
  xaxis: {
    categories: ['一', '二', '三', '四', '五']
  }
})

const series = ref([{
  name: '数据',
  data: [10, 20, 30, 40, 50]
}])
</script>
```

## Vue3 Composable

### 图表 Composable
```typescript
// composables/useApexChart.ts
import { ref, computed } from 'vue'
import VueApexCharts from 'vue3-apexcharts'

export function useApexChart(
  type: string,
  initialSeries: any[],
  initialOptions: any
) {
  const series = ref(initialSeries)
  const options = ref(initialOptions)
  
  const updateSeries = (newSeries: any[]) => {
    series.value = newSeries
  }
  
  const updateOptions = (newOptions: any) => {
    options.value = { ...options.value, ...newOptions }
  }
  
  const chartComponent = VueApexCharts
  
  return {
    series,
    options,
    updateSeries,
    updateOptions,
    chartComponent
  }
}
```

### 使用 Composable
```vue
<template>
  <div>
    <component
      :is="chartComponent"
      :type="type"
      :height="350"
      :options="options"
      :series="series"
    />
  </div>
</template>

<script setup>
import { useApexChart } from '@/composables/useApexChart'

const type = 'line'
const initialSeries = [{
  name: '数据',
  data: [10, 20, 30, 40, 50]
}]

const initialOptions = {
  chart: {
    id: 'composable-chart'
  },
  xaxis: {
    categories: ['一', '二', '三', '四', '五']
  }
}

const {
  series,
  options,
  updateSeries,
  chartComponent
} = useApexChart(type, initialSeries, initialOptions)
</script>
```

## 最佳实践

### 性能优化
1. 合理设置数据点数量，避免过多数据
2. 使用虚拟滚动处理大数据集
3. 禁用不必要的动画效果
4. 懒加载图表组件
5. 合理使用缓存

### 可访问性
1. 为图表提供替代文本
2. 确保键盘导航支持
3. 提供数据表格作为替代
4. 使用合适的颜色对比度
5. 支持屏幕阅读器

### 数据处理
1. 数据验证和清洗
2. 处理缺失数据
3. 合理的数据格式化
4. 异常数据处理
5. 数据加载状态处理
