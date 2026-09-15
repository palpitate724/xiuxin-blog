# Vue Scan

Vue 运行时性能监控技能。

## 性能监控基础

### Vue DevTools Performance
```
1. 打开 Vue DevTools
2. 选择 Performance 标签
3. 点击开始录制
4. 执行用户操作
5. 停止录制并分析
```

### Performance API
```typescript
// 使用 Performance API 监控组件性能
import { onMounted, onUnmounted } from 'vue'

export function usePerformanceMonitor(componentName: string) {
  let startTime: number
  let renderCount = 0
  
  onMounted(() => {
    startTime = performance.now()
    console.log(`${componentName} 组件挂载开始`)
  })
  
  onUnmounted(() => {
    const endTime = performance.now()
    const duration = endTime - startTime
    
    console.log(`${componentName} 组件卸载`)
    console.log(`总持续时间: ${duration.toFixed(2)}ms`)
    console.log(`渲染次数: ${renderCount}`)
  })
  
  const recordRender = () => {
    renderCount++
  }
  
  return {
    recordRender
  }
}
```

## 组件性能分析

### 渲染性能监控
```typescript
// 监控组件渲染次数
import { onMounted, onUpdated, onUnmounted } from 'vue'

export function useRenderMonitor(componentName: string) {
  let renderCount = 0
  let updateCount = 0
  
  onMounted(() => {
    console.log(`${componentName} 首次渲染`)
    renderCount++
  })
  
  onUpdated(() => {
    updateCount++
    console.log(`${componentName} 更新次数: ${updateCount}`)
  })
  
  onUnmounted(() => {
    console.log(`${componentName} 总渲染次数: ${renderCount}`)
    console.log(`${componentName} 总更新次数: ${updateCount}`)
  })
}
```

### 计算属性性能
```typescript
// 监控计算属性性能
import { computed, watchEffect } from 'vue'

export function useComputedMonitor<T>(
  getter: () => T,
  name: string
) {
  let computeCount = 0
  let lastComputeTime = 0
  
  const monitoredComputed = computed(() => {
    const startTime = performance.now()
    const result = getter()
    const endTime = performance.now()
    
    computeCount++
    lastComputeTime = endTime - startTime
    
    if (lastComputeTime > 16) { // 超过一帧时间
      console.warn(`${name} 计算耗时过长: ${lastComputeTime.toFixed(2)}ms`)
    }
    
    return result
  })
  
  watchEffect(() => {
    console.log(`${name} 计算次数: ${computeCount}`)
  })
  
  return {
    value: monitoredComputed,
    computeCount,
    lastComputeTime
  }
}
```

## 内存监控

### 内存使用监控
```typescript
// 监控内存使用情况
export function useMemoryMonitor() {
  const memoryUsage = ref({
    usedJSHeapSize: 0,
    totalJSHeapSize: 0,
    jsHeapSizeLimit: 0
  })
  
  const updateMemoryUsage = () => {
    if ('memory' in performance) {
      const memory = (performance as any).memory
      memoryUsage.value = {
        usedJSHeapSize: memory.usedJSHeapSize,
        totalJSHeapSize: memory.totalJSHeapSize,
        jsHeapSizeLimit: memory.jsHeapSizeLimit
      }
    }
  }
  
  const startMonitoring = (interval = 5000) => {
    updateMemoryUsage()
    return setInterval(updateMemoryUsage, interval)
  }
  
  return {
    memoryUsage,
    updateMemoryUsage,
    startMonitoring
  }
}
```

### 内存泄漏检测
```typescript
// 检测潜在的内存泄漏
export function useMemoryLeakDetector() {
  const componentInstances = new WeakMap()
  
  const trackComponent = (component: any, name: string) => {
    componentInstances.set(component, {
      name,
      createdAt: Date.now(),
      renderCount: 0
    })
  }
  
  const checkLeaks = () => {
    let leakCount = 0
    
    componentInstances.forEach((metadata, component) => {
      const age = Date.now() - metadata.createdAt
      
      // 如果组件存在时间过长且未被清理
      if (age > 60000) { // 1分钟
        console.warn(`潜在内存泄漏: ${metadata.name} 存在时间过长`)
        leakCount++
      }
    })
    
    return leakCount
  }
  
  return {
    trackComponent,
    checkLeaks
  }
}
```

## 网络性能监控

### API 请求监控
```typescript
// 监控 API 请求性能
export function useApiMonitor() {
  const requestMetrics = ref<Array<{
    url: string
    duration: number
    status: number
    timestamp: number
  }>>([])
  
  const monitorRequest = async (
    url: string,
    requestFn: () => Promise<any>
  ) => {
    const startTime = performance.now()
    
    try {
      const response = await requestFn()
      const endTime = performance.now()
      const duration = endTime - startTime
      
      requestMetrics.value.push({
        url,
        duration,
        status: response.status,
        timestamp: Date.now()
      })
      
      if (duration > 1000) {
        console.warn(`API 请求耗时过长: ${url} - ${duration.toFixed(2)}ms`)
      }
      
      return response
    } catch (error) {
      const endTime = performance.now()
      const duration = endTime - startTime
      
      requestMetrics.value.push({
        url,
        duration,
        status: 0,
        timestamp: Date.now()
      })
      
      throw error
    }
  }
  
  const getSlowRequests = (threshold = 1000) => {
    return requestMetrics.value.filter(m => m.duration > threshold)
  }
  
  return {
    requestMetrics,
    monitorRequest,
    getSlowRequests
  }
}
```

### 资源加载监控
```typescript
// 监控资源加载性能
export function useResourceMonitor() {
  const resourceMetrics = ref<Array<{
    name: string
    duration: number
    size: number
    type: string
  }>>([])
  
  const monitorResources = () => {
    const resources = performance.getEntriesByType('resource') as PerformanceResourceTiming[]
    
    resources.forEach(resource => {
      const duration = resource.responseEnd - resource.startTime
      const size = resource.transferSize
      
      resourceMetrics.value.push({
        name: resource.name,
        duration,
        size,
        type: resource.initiatorType
      })
    })
  }
  
  const getSlowResources = (threshold = 1000) => {
    return resourceMetrics.value.filter(m => m.duration > threshold)
  }
  
  return {
    resourceMetrics,
    monitorResources,
    getSlowResources
  }
}
```

## FPS 监控

### 帧率监控
```typescript
// 监控页面 FPS
export function useFPSMonitor() {
  const fps = ref(60)
  const frames = ref(0)
  let lastTime = performance.now()
  let animationFrameId: number
  
  const measureFPS = () => {
    frames.value++
    const currentTime = performance.now()
    
    if (currentTime >= lastTime + 1000) {
      fps.value = Math.round((frames.value * 1000) / (currentTime - lastTime))
      frames.value = 0
      lastTime = currentTime
      
      if (fps.value < 30) {
        console.warn(`FPS 过低: ${fps.value}`)
      }
    }
    
    animationFrameId = requestAnimationFrame(measureFPS)
  }
  
  const startMonitoring = () => {
    measureFPS()
  }
  
  const stopMonitoring = () => {
    cancelAnimationFrame(animationFrameId)
  }
  
  return {
    fps,
    startMonitoring,
    stopMonitoring
  }
}
```

## 性能报告

### 生成性能报告
```typescript
// 生成性能报告
export function generatePerformanceReport() {
  const report = {
    timestamp: new Date().toISOString(),
    
    // 页面加载性能
    pageLoad: {
      domContentLoaded: performance.timing.domContentLoadedEventEnd - performance.timing.navigationStart,
      loadComplete: performance.timing.loadEventEnd - performance.timing.navigationStart,
      firstPaint: 0,
      firstContentfulPaint: 0
    },
    
    // 内存使用
    memory: (() => {
      if ('memory' in performance) {
        const memory = (performance as any).memory
        return {
          used: memory.usedJSHeapSize,
          total: memory.totalJSHeapSize,
          limit: memory.jsHeapSizeLimit,
          usage: (memory.usedJSHeapSize / memory.jsHeapSizeLimit * 100).toFixed(2) + '%'
        }
      }
      return null
    })(),
    
    // FPS
    fps: 0,
    
    // 网络请求
    requests: {
      total: 0,
      slow: 0,
      failed: 0
    }
  }
  
  // 获取 Web Vitals
  if ('PerformanceObserver' in window) {
    const observer = new PerformanceObserver((list) => {
      for (const entry of list.getEntries()) {
        if (entry.name === 'FCP') {
          report.pageLoad.firstContentfulPaint = entry.startTime
        }
        if (entry.name === 'FP') {
          report.pageLoad.firstPaint = entry.startTime
        }
      }
    })
    
    observer.observe({ entryTypes: ['paint'] })
  }
  
  return report
}
```

### 性能阈值检查
```typescript
// 检查性能是否达标
export function checkPerformanceThresholds(report: any) {
  const thresholds = {
    pageLoadTime: 2000, // 2秒
    fcp: 1000, // 1秒
    fps: 30, // 30 FPS
    memoryUsage: 80 // 80%
  }
  
  const issues = []
  
  if (report.pageLoad.loadComplete > thresholds.pageLoadTime) {
    issues.push({
      type: 'page_load',
      message: `页面加载时间过长: ${report.pageLoad.loadComplete}ms`,
      threshold: thresholds.pageLoadTime
    })
  }
  
  if (report.pageLoad.firstContentfulPaint > thresholds.fcp) {
    issues.push({
      type: 'fcp',
      message: `首次内容绘制时间过长: ${report.pageLoad.firstContentfulPaint}ms`,
      threshold: thresholds.fcp
    })
  }
  
  if (report.fps < thresholds.fps) {
    issues.push({
      type: 'fps',
      message: `FPS 过低: ${report.fps}`,
      threshold: thresholds.fps
    })
  }
  
  if (report.memory && parseFloat(report.memory.usage) > thresholds.memoryUsage) {
    issues.push({
      type: 'memory',
      message: `内存使用率过高: ${report.memory.usage}`,
      threshold: thresholds.memoryUsage + '%'
    })
  }
  
  return {
    passed: issues.length === 0,
    issues
  }
}
```

## 最佳实践

### 监控策略
1. 生产环境选择性监控
2. 设置合理的采样率
3. 保护用户隐私
4. 避免监控本身影响性能
5. 定期清理监控数据

### 性能基准
```
页面加载时间: < 2s
首次内容绘制: < 1s
可交互时间: < 3s
FPS: > 30
内存使用: < 100MB
CPU 使用: < 50%
```

### 持续监控
1. 建立性能监控仪表板
2. 设置性能告警阈值
3. 定期分析性能趋势
4. 建立性能优化流程
5. 持续改进用户体验
