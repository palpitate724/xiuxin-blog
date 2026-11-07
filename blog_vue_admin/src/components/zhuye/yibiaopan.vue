<template>
  <!-- 仪表盘: 统计卡片 + 近期文章概览 -->
  <div class="dashboard">
    <div class="dashboard__header">
      <h1 class="dashboard__title">仪表盘</h1>
      <p class="dashboard__desc">欢迎回来，这里是 Xiuxin Blog 后台管理面板</p>
    </div>

    <!-- 统计卡片行 -->
    <div class="stat-grid">
      <div class="stat-card" v-for="(card, index) in statCards" :key="index">
        <div class="stat-card__icon" :style="{ background: card.colorSoft }">
          <component :is="card.iconComponent" :size="24" />
        </div>
        <div class="stat-card__info">
          <span class="stat-card__value">{{ card.value }}</span>
          <span class="stat-card__label">{{ card.label }}</span>
        </div>
      </div>
    </div>

    <!-- 图表区 + 近期动态 -->
    <div class="dashboard__body">
      <div class="admin-card admin-card--wide">
        <div class="admin-card__header">
          <span class="admin-card__title">文章发布趋势</span>
          <select class="admin-card__select" v-model="trendPeriod">
            <option value="7">近7天</option>
            <option value="30">近30天</option>
            <option value="90">近90天</option>
          </select>
        </div>
        <div class="admin-card__body admin-card__body--chart">
          <div class="chart-placeholder">
            <div class="chart-bars">
              <div
                v-for="(bar, i) in chartData"
                :key="i"
                class="chart-bar"
                :style="{ height: (bar.value / 7) * 130 + 'px', background: bar.color }"
                :title="`${bar.label}: ${bar.value} 篇`"
              >
                <span class="chart-bar__val">{{ bar.value }}</span>
              </div>
            </div>
            <div class="chart-labels">
              <span v-for="(bar, i) in chartData" :key="i" class="chart-label">{{ bar.label }}</span>
            </div>
          </div>
        </div>
      </div>

      <div class="dashboard__side">
        <!-- 近期文章 -->
        <div class="admin-card">
          <div class="admin-card__header">
            <span class="admin-card__title">近期文章</span>
            <router-link to="/zhuye/art" class="admin-card__link">查看全部</router-link>
          </div>
          <div class="admin-card__body">
            <div
              v-for="art in recentArticles"
              :key="art.id"
              class="recent-item"
            >
              <div class="recent-item__dot" :class="art.status"></div>
              <div class="recent-item__info">
                <span class="recent-item__title">{{ art.name }}</span>
                <span class="recent-item__time">{{ art.time }}</span>
              </div>
            </div>
          </div>
        </div>

        <!-- 快捷操作 -->
        <div class="admin-card">
          <div class="admin-card__header">
            <span class="admin-card__title">快捷操作</span>
          </div>
          <div class="admin-card__body admin-card__body--actions">
            <button class="action-btn" @click="goTo('/zhuye/art')">
              <PenTool :size="18" />
              <span>新建文章</span>
            </button>
            <button class="action-btn" @click="goTo('/zhuye/cat')">
              <Folder :size="18" />
              <span>管理分类</span>
            </button>
            <button class="action-btn" @click="goTo('/zhuye/tag')">
              <TagIcon :size="18" />
              <span>管理标签</span>
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { FileText, CheckCircle, File, Tag, PenTool, Folder, Tag as TagIcon } from '@lucide/vue'

// ─── Vue Router 实例 ───────────────────────────────────────────────────────────
/** Vue Router 路由实例，用于页面跳转（快捷操作按钮使用） */
const router = useRouter()

/**
 * 图表时间范围选择器。
 * 控制「文章发布趋势」折线图展示的日期范围。
 * 可选值：'7'（近7天）、'30'（近30天）、'90'（近90天）
 */
const trendPeriod = ref('7')

// ─── 统计卡片数据（模拟）──────────────────────────────────────────────────────
/**
 * 仪表盘顶部的四张统计卡片数据。
 * 每张卡片包含：标签名、数值、图标组件、背景色（soft 色调）。
 * TODO: 后续从 artpage 和 tagpage store 动态获取真实数据。
 */
const statCards = ref([
  { label: '总文章数', value: 128, iconComponent: FileText, colorSoft: 'rgba(59,130,246,0.1)' },
  { label: '已发布', value: 96, iconComponent: CheckCircle, colorSoft: 'rgba(34,197,94,0.1)' },
  { label: '草稿', value: 32, iconComponent: File, colorSoft: 'rgba(245,158,11,0.1)' },
  { label: '总标签数', value: 47, iconComponent: Tag, colorSoft: 'rgba(139,92,246,0.1)' },
])

// ─── 图表数据（模拟）───────────────────────────────────────────────────────────
/**
 * 文章发布趋势的柱状图数据，按周一到周日排列。
 * 每项包含：星期标签、发文数量、CSS 渐变背景色。
 * TODO: 后续根据 trendPeriod 值调用后端趋势接口替换此数据。
 */
const chartData = ref([
  { label: '周一', value: 3, color: 'linear-gradient(to top, rgba(59,130,246,0.3), var(--color-accent))' },
  { label: '周二', value: 5, color: 'linear-gradient(to top, rgba(59,130,246,0.3), var(--color-accent))' },
  { label: '周三', value: 2, color: 'linear-gradient(to top, rgba(59,130,246,0.3), var(--color-accent))' },
  { label: '周四', value: 7, color: 'linear-gradient(to top, rgba(59,130,246,0.3), var(--color-accent))' },
  { label: '周五', value: 4, color: 'linear-gradient(to top, rgba(59,130,246,0.3), var(--color-accent))' },
  { label: '周六', value: 1, color: 'linear-gradient(to top, rgba(59,130,246,0.3), var(--color-accent))' },
  { label: '周日', value: 6, color: 'linear-gradient(to top, rgba(59,130,246,0.3), var(--color-accent))' },
])

// ─── 近期文章列表（模拟）───────────────────────────────────────────────────────
/**
 * 仪表盘右侧「近期文章」面板展示的文章摘要列表。
 * 每条记录包含：ID、标题、状态（published/draft）、相对时间。
 * TODO: 后续调用 artpage_v.getartlist() 获取真实数据。
 */
const recentArticles = ref([
  { id: '1', name: 'Vue 3.6 新特性深度解析', status: 'published', time: '2小时前' },
  { id: '2', name: 'Spring Boot 3 实践笔记', status: 'published', time: '1天前' },
  { id: '3', name: 'Markdown 编辑器设计思路', status: 'draft', time: '3天前' },
  { id: '4', name: 'TypeScript 类型体操入门', status: 'draft', time: '5天前' },
])

// ─── 快捷导航函数 ──────────────────────────────────────────────────────────────
/**
 * 跳转到指定路由路径，供快捷操作按钮调用。
 * @param path 目标路由路径
 */
const goTo = (path: string) => router.push(path)

// TODO: 接入真实后端接口
// const loadStats = async () => { const res = await getDashboardStats() }
// const loadChartData = async () => { const res = await getArticleTrend(trendPeriod.value) }

// ─── 生命周期 ──────────────────────────────────────────────────────────────────
/** 组件挂载时初始化仪表盘数据（当前仅预留接口，未实际调用） */
onMounted(() => {
  // loadStats()
  // loadChartData()
})
</script>

<style scoped>
.dashboard {
  width: 100%;
  max-width: var(--layout-max-width);
  margin: 0 auto;
}

.dashboard__header {
  margin-bottom: var(--spacing-xl);
  padding: 0 var(--spacing-xs);
}

.dashboard__title {
  font-size: var(--font-size-xl3);
  font-weight: var(--font-weight-bold);
  color: var(--color-text-primary);
  letter-spacing: -0.02em;
}

.dashboard__desc {
  margin-top: var(--spacing-xs);
  font-size: var(--font-size-sm);
  color: var(--color-text-secondary);
}

/* ── 统计卡片网格 ── */
.stat-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: var(--spacing-md);
  margin-bottom: var(--spacing-lg);
}

.stat-card {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
  padding: var(--spacing-md) var(--spacing-lg);
  background: var(--color-surface);
  border: 1px solid var(--color-border-divider);
  border-radius: var(--radius-xl);
  box-shadow: var(--color-shadow-sm);
  transition: border-color var(--duration-fast) var(--ease-standard),
              box-shadow var(--duration-fast) var(--ease-standard),
              transform var(--duration-fast) var(--ease-out);
  animation: cardSlideIn 0.4s ease both;
}

.stat-card:nth-child(1) { animation-delay: 0.05s; }
.stat-card:nth-child(2) { animation-delay: 0.1s; }
.stat-card:nth-child(3) { animation-delay: 0.15s; }
.stat-card:nth-child(4) { animation-delay: 0.2s; }

@keyframes cardSlideIn {
  from { opacity: 0; transform: translateY(12px); }
  to   { opacity: 1; transform: translateY(0); }
}

.stat-card:hover {
  border-color: var(--color-border-strong);
  box-shadow: var(--color-shadow-md);
  transform: translateY(-2px);
}

.stat-card__icon {
  width: 48px;
  height: 48px;
  border-radius: var(--radius-lg);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  font-size: 1.3rem;
}

.stat-card__info {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.stat-card__value {
  font-size: 1.625rem;
  font-weight: var(--font-weight-bold);
  color: var(--color-text-primary);
  line-height: 1.1;
  letter-spacing: -0.02em;
}

.stat-card__label {
  font-size: var(--font-size-xs);
  color: var(--color-text-muted);
  text-transform: uppercase;
  letter-spacing: 0.05em;
  font-weight: var(--font-weight-medium);
}

/* ── 主体布局 ── */
.dashboard__body {
  display: grid;
  grid-template-columns: 1fr 300px;
  gap: var(--spacing-lg);
  align-items: start;
}

.admin-card__select {
  padding: 4px 10px;
  background: var(--color-bg-tertiary);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-sm);
  font-size: var(--font-size-xs);
  color: var(--color-text-secondary);
  cursor: pointer;
  outline: none;
  transition: border-color var(--duration-fast);
}

.admin-card__select:focus {
  border-color: var(--color-accent);
}

.admin-card__link {
  font-size: var(--font-size-xs);
  color: var(--color-accent);
  text-decoration: none;
  font-weight: var(--font-weight-medium);
  transition: color var(--duration-fast);
}

.admin-card__link:hover {
  color: var(--color-accent-hover);
  text-decoration: underline;
}

/* ── 图表区 ── */
.chart-bars {
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
  gap: var(--spacing-xs);
  height: 160px;
}

.chart-bar {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-end;
  border-radius: var(--radius-sm) var(--radius-sm) 0 0;
  min-height: 4px;
  transition: opacity var(--duration-fast), transform var(--duration-fast);
  position: relative;
  animation: barGrow 0.5s ease both;
}

.chart-bar:hover {
  opacity: 0.85;
  transform: scaleY(1.03);
  transform-origin: bottom;
}

@keyframes barGrow {
  from { height: 0 !important; opacity: 0; }
  to   { opacity: 1; }
}

.chart-bar__val {
  font-size: 0.65rem;
  color: var(--color-text-muted);
  margin-bottom: 4px;
  font-variant-numeric: tabular-nums;
}

.chart-labels { display: flex; justify-content: space-between; }

.chart-label {
  flex: 1;
  text-align: center;
  font-size: var(--font-size-xs);
  color: var(--color-text-muted);
}

/* ── 右侧边栏 ── */
.dashboard__side {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-lg);
}

/* ── 近期文章列表 ── */
.recent-item {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  padding: var(--spacing-sm) 0;
  border-bottom: 1px solid var(--color-border-divider);
}

.recent-item:last-child { border-bottom: none; }

.recent-item__dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  flex-shrink: 0;
}

.recent-item__dot.published { background: var(--color-success); }
.recent-item__dot.draft    { background: var(--color-text-muted); }

.recent-item__info {
  display: flex;
  flex-direction: column;
  gap: 2px;
  min-width: 0;
  flex: 1;
}

.recent-item__title {
  font-size: var(--font-size-sm);
  color: var(--color-text-primary);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  font-weight: var(--font-weight-medium);
}

.recent-item__time {
  font-size: var(--font-size-xs);
  color: var(--color-text-muted);
}

/* ── 快捷操作按钮 ── */
.admin-card__body--actions {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-sm);
}

.action-btn {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  padding: var(--spacing-sm) var(--spacing-md);
  background: var(--color-bg-secondary);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
  font-size: var(--font-size-sm);
  color: var(--color-text-primary);
  cursor: pointer;
  transition: background-color var(--duration-fast),
              border-color var(--duration-fast),
              box-shadow var(--duration-fast),
              transform var(--duration-fast);
}

.action-btn:hover {
  background: var(--color-surface-hover);
  border-color: var(--color-border-strong);
  box-shadow: 0 2px 8px var(--color-shadow-sm);
  transform: translateY(-1px);
}

.action-btn__icon { font-size: 1.1rem; }

/* ── 响应式 ── */
@media (max-width: 1024px) {
  .stat-grid { grid-template-columns: repeat(2, 1fr); }
  .dashboard__body { grid-template-columns: 1fr; }
}

@media (max-width: 640px) {
  .stat-grid { grid-template-columns: 1fr; }
  .chart-bars { height: 120px; }
}
</style>
