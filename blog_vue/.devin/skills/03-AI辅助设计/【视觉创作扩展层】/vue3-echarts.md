# Vue3 ECharts 技能

ECharts 图表组件集成技能。

## 安装配置

### 基础安装
```bash
npm install echarts vue-echarts
```

### 插件配置
```typescript
// main.ts
import { createApp } from 'vue'
import ECharts from 'vue-echarts'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { BarChart, LineChart, PieChart } from 'echarts/charts'
import {
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent
} from 'echarts/components'

use([
  CanvasRenderer,
  BarChart,
  LineChart,
  PieChart,
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent
])

const app = createApp(App)
app.component('v-chart', ECharts)
```

## 基础图表

### 折线图
```vue
<template>
  <v-chart
    class="chart"
    :option="lineOption"
    autoresize
  />
</template>

<script setup>
import { ref } from 'vue'

const lineOption = ref({
  title: {
    text: '折线图示例'
  },
  tooltip: {
    trigger: 'axis'
  },
  xAxis: {
    type: 'category',
    data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
  },
  yAxis: {
    type: 'value'
  },
  series: [{
    data: [150, 230, 224, 218, 135, 147, 260],
    type: 'line'
  }]
})
</script>

<style scoped>
.chart {
  height: 400px;
  width: 100%;
}
</style>
```

### 柱状图
```vue
<template>
  <v-chart
    class="chart"
    :option="barOption"
    autoresize
  />
</template>

<script setup>
import { ref } from 'vue'

const barOption = ref({
  title: {
    text: '柱状图示例'
  },
  tooltip: {
    trigger: 'axis'
  },
  xAxis: {
    type: 'category',
    data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
  },
  yAxis: {
    type: 'value'
  },
  series: [{
    data: [120, 200, 150, 80, 70, 110, 130],
    type: 'bar'
  }]
})
</script>
```

### 饼图
```vue
<template>
  <v-chart
    class="chart"
    :option="pieOption"
    autoresize
  />
</template>

<script setup>
import { ref } from 'vue'

const pieOption = ref({
  title: {
    text: '饼图示例',
    left: 'center'
  },
  tooltip: {
    trigger: 'item'
  },
  legend: {
    orient: 'vertical',
    left: 'left'
  },
  series: [{
    name: '访问来源',
    type: 'pie',
    radius: '50%',
    data: [
      { value: 1048, name: '搜索引擎' },
      { value: 735, name: '直接访问' },
      { value: 580, name: '邮件营销' },
      { value: 484, name: '联盟广告' },
      { value: 300, name: '视频广告' }
    ],
    emphasis: {
      itemStyle: {
        shadowBlur: 10,
        shadowOffsetX: 0,
        shadowColor: 'rgba(0, 0, 0, 0.5)'
      }
    }
  }]
})
</script>
```

## 高级图表

### 面积图
```vue
<template>
  <v-chart
    class="chart"
    :option="areaOption"
    autoresize
  />
</template>

<script setup>
import { ref } from 'vue'

const areaOption = ref({
  title: {
    text: '面积图示例'
  },
  tooltip: {
    trigger: 'axis'
  },
  xAxis: {
    type: 'category',
    boundaryGap: false,
    data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
  },
  yAxis: {
    type: 'value'
  },
  series: [{
    data: [820, 932, 901, 934, 1290, 1330, 1320],
    type: 'line',
    areaStyle: {}
  }]
})
</script>
```

### 混合图表
```vue
<template>
  <v-chart
    class="chart"
    :option="mixedOption"
    autoresize
  />
</template>

<script setup>
import { ref } from 'vue'

const mixedOption = ref({
  title: {
    text: '混合图表示例'
  },
  tooltip: {
    trigger: 'axis'
  },
  legend: {
    data: ['销量', '收入']
  },
  xAxis: {
    type: 'category',
    data: ['衬衫', '羊毛衫', '雪纺衫', '裤子', '高跟鞋', '袜子']
  },
  yAxis: [
    {
      type: 'value',
      name: '销量'
    },
    {
      type: 'value',
      name: '收入'
    }
  ],
  series: [
    {
      name: '销量',
      type: 'bar',
      data: [5, 20, 36, 10, 10, 20]
    },
    {
      name: '收入',
      type: 'line',
      yAxisIndex: 1,
      data: [120, 200, 150, 80, 70, 110]
    }
  ]
})
</script>
```

### 散点图
```vue
<template>
  <v-chart
    class="chart"
    :option="scatterOption"
    autoresize
  />
</template>

<script setup>
import { ref } from 'vue'

const scatterOption = ref({
  title: {
    text: '散点图示例'
  },
  xAxis: {},
  yAxis: {},
  series: [{
    symbolSize: 20,
    data: [
      [10.0, 8.04],
      [8.0, 6.95],
      [13.0, 7.58],
      [9.0, 8.81],
      [11.0, 8.33],
      [14.0, 9.96],
      [6.0, 7.24],
      [4.0, 4.26],
      [12.0, 10.84],
      [7.0, 4.82],
      [5.0, 5.68]
    ],
    type: 'scatter'
  }]
})
</script>
```

## 交互功能

### 缩放功能
```vue
<template>
  <v-chart
    class="chart"
    :option="zoomOption"
    autoresize
  />
</template>

<script setup>
import { ref } from 'vue'
import { DataZoomComponent } from 'echarts/components'

// 注册组件
use([DataZoomComponent])

const zoomOption = ref({
  title: {
    text: '缩放图表示例'
  },
  tooltip: {
    trigger: 'axis'
  },
  dataZoom: [
    {
      type: 'slider',
      show: true,
      start: 0,
      end: 100
    },
    {
      type: 'inside',
      start: 0,
      end: 100
    }
  ],
  xAxis: {
    type: 'category',
    data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
  },
  yAxis: {
    type: 'value'
  },
  series: [{
    data: [820, 932, 901, 934, 1290, 1330, 1320],
    type: 'line'
  }]
})
</script>
```

### 工具提示
```vue
<template>
  <v-chart
    class="chart"
    :option="tooltipOption"
    autoresize
  />
</template>

<script setup>
import { ref } from 'vue'

const tooltipOption = ref({
  title: {
    text: '工具提示示例'
  },
  tooltip: {
    trigger: 'axis',
    formatter: function (params) {
      let result = params[0].name + '<br/>'
      params.forEach(function (item) {
        result += item.marker + item.seriesName + ': ' + item.value + '<br/>'
      })
      return result
    }
  },
  xAxis: {
    type: 'category',
    data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
  },
  yAxis: {
    type: 'value'
  },
  series: [{
    name: '销量',
    data: [820, 932, 901, 934, 1290, 1330, 1320],
    type: 'line'
  }]
})
</script>
```

## 响应式设计

### 自适应图表
```vue
<template>
  <v-chart
    class="chart"
    :option="responsiveOption"
    autoresize
  />
</template>

<script setup>
import { ref, computed } from 'vue'
import { useMediaQuery } from '@vueuse/core'

const isMobile = useMediaQuery('(max-width: 768px)')

const responsiveOption = computed(() => ({
  title: {
    text: '响应式图表示例',
    textStyle: {
      fontSize: isMobile.value ? 14 : 18
    }
  },
  tooltip: {
    trigger: 'axis'
  },
  legend: {
    orient: isMobile.value ? 'horizontal' : 'vertical',
    right: isMobile.value ? 'center' : 'right',
    top: isMobile.value ? 'bottom' : 'center'
  },
  xAxis: {
    type: 'category',
    data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
    axisLabel: {
      rotate: isMobile.value ? 45 : 0
    }
  },
  yAxis: {
    type: 'value'
  },
  series: [{
    data: [820, 932, 901, 934, 1290, 1330, 1320],
    type: 'line'
  }]
}))
</script>
```

## 实时数据

### 动态更新
```vue
<template>
  <div>
    <v-chart
      ref="chartRef"
      class="chart"
      :option="realtimeOption"
      autoresize
    />
    <button @click="updateData">更新数据</button>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const chartRef = ref()
const data = ref([820, 932, 901, 934, 1290, 1330, 1320])

const realtimeOption = ref({
  title: {
    text: '实时数据示例'
  },
  tooltip: {
    trigger: 'axis'
  },
  xAxis: {
    type: 'category',
    data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
  },
  yAxis: {
    type: 'value'
  },
  series: [{
    data: data.value,
    type: 'line'
  }]
})

const updateData = () => {
  const newData = data.value.map(() => Math.floor(Math.random() * 2000))
  data.value = newData
  realtimeOption.value.series[0].data = newData
}
</script>
```

## 主题定制

### 深色主题
```vue
<template>
  <v-chart
    class="chart"
    :option="darkThemeOption"
    :theme="'dark'"
    autoresize
  />
</template>

<script setup>
import { ref } from 'vue'

const darkThemeOption = ref({
  backgroundColor: '#1a1a1a',
  title: {
    text: '深色主题示例',
    textStyle: {
      color: '#ffffff'
    }
  },
  tooltip: {
    trigger: 'axis'
  },
  xAxis: {
    type: 'category',
    data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
    axisLine: {
      lineStyle: {
        color: '#ffffff'
      }
    },
    axisLabel: {
      color: '#ffffff'
    }
  },
  yAxis: {
    type: 'value',
    axisLine: {
      lineStyle: {
        color: '#ffffff'
      }
    },
    axisLabel: {
      color: '#ffffff'
    }
  },
  series: [{
    data: [820, 932, 901, 934, 1290, 1330, 1320],
    type: 'line',
    lineStyle: {
      color: '#1890ff'
    }
  }]
})
</script>
```

### 自定义主题
```vue
<template>
  <v-chart
    class="chart"
    :option="customThemeOption"
    autoresize
  />
</template>

<script setup>
import { ref } from 'vue'

const customThemeOption = ref({
  color: ['#1890ff', '#52c41a', '#faad14', '#ff4d4f', '#722ed1'],
  title: {
    text: '自定义主题示例'
  },
  tooltip: {
    trigger: 'axis'
  },
  xAxis: {
    type: 'category',
    data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
  },
  yAxis: {
    type: 'value'
  },
  series: [
    {
      name: '系列1',
      data: [820, 932, 901, 934, 1290, 1330, 1320],
      type: 'line'
    },
    {
      name: '系列2',
      data: [620, 732, 701, 734, 1090, 1130, 1120],
      type: 'line'
    }
  ]
})
</script>
```

## Vue3 Composable

### 图表 Composable
```typescript
// composables/useECharts.ts
import { ref, computed } from 'vue'
import ECharts from 'vue-echarts'

export function useECharts(
  initialOption: any,
  theme?: string
) {
  const option = ref(initialOption)
  const chartRef = ref()
  
  const updateOption = (newOption: any) => {
    option.value = { ...option.value, ...newOption }
  }
  
  const updateSeries = (seriesData: any[]) => {
    option.value.series = seriesData
  }
  
  const resize = () => {
    chartRef.value?.resize()
  }
  
  const chartComponent = ECharts
  
  return {
    option,
    chartRef,
    updateOption,
    updateSeries,
    resize,
    chartComponent
  }
}
```

### 使用 Composable
```vue
<template>
  <div>
    <component
      ref="chartRef"
      :is="chartComponent"
      class="chart"
      :option="option"
      :theme="theme"
      autoresize
    />
  </div>
</template>

<script setup>
import { useECharts } from '@/composables/useECharts'

const initialOption = {
  title: {
    text: 'Composable 示例'
  },
  tooltip: {
    trigger: 'axis'
  },
  xAxis: {
    type: 'category',
    data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
  },
  yAxis: {
    type: 'value'
  },
  series: [{
    data: [820, 932, 901, 934, 1290, 1330, 1320],
    type: 'line'
  }]
}

const {
  option,
  chartRef,
  updateOption,
  chartComponent
} = useECharts(initialOption, 'dark')
</script>
```

## 最佳实践

### 性能优化
1. 合理设置数据点数量，避免过多数据
2. 使用按需引入减少包体积
3. 启用渲染优化选项
4. 合理使用动画效果
5. 懒加载图表组件

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

### 响应式设计
1. 使用 autoresize 属性
2. 根据屏幕尺寸调整配置
3. 移动端优化显示
4. 触摸交互优化
5. 横竖屏适配
