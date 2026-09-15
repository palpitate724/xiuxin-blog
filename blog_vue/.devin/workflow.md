# AI UI Design Skills 工作流程

本工作流程基于 AI UI Design Skills 技能集合，提供完整的 Vue3 项目设计和开发流程。

## 工作流程概览

```
┌─────────────────────────────────────────────────────┐
│ 阶段一：项目初始化                                    │
│ 配置 Vue3 + Vite + TypeScript + Element Plus         │
│ 安装按需引入插件                                      │
│ 【新增】安装 vuejs-ai/skills 基础套件                  │
└─────────────────────────────────────────────────────┘
                        ↓
┌─────────────────────────────────────────────────────┐
│ 阶段二：设计方向锚定（03-AI辅助设计）                  │
│  frontend-design → 选定美学锚点                       │
│  Taste Skill → 注入审美判断力                         │
└─────────────────────────────────────────────────────┘
                        ↓
┌─────────────────────────────────────────────────────┐
│ 阶段三：设计系统生成（03-AI辅助设计）                  │
│  ui-ux-pro-max → 生成完整设计系统                     │
└─────────────────────────────────────────────────────┘
                        ↓
┌─────────────────────────────────────────────────────┐
│ 阶段四：主题落地（04-主题定制）                        │
│  awesome-design-md → 选取品牌 DESIGN.md               │
│ 映射到 Element Plus 主题变量                          │
│ 【新增】el-icon / icon-system → 图标系统配置           │
└─────────────────────────────────────────────────────┘
                        ↓
┌─────────────────────────────────────────────────────┐
│ 阶段五：组件封装（02-组件封装）                        │
│  封装 CustomTable / CustomForm / CustomDialog         │
│ 【新增】form-validation-regle / veevalidate → 表单验证  │
└─────────────────────────────────────────────────────┘
                        ↓
┌─────────────────────────────────────────────────────┐
│ 阶段六：页面开发                                      │
│  按提示词工程规范与 AI 协作生成页面代码                │
│ 【新增】vue-pinia-best-practices → 状态管理            │
│ 【新增】vue-router-best-practices → 路由配置            │
│ 【新增】gsap-skills / motion-vue → 动效实现            │
│ 【新增】apexcharts-vue / vue3-echarts → 图表集成        │
└─────────────────────────────────────────────────────┘
                        ↓
┌─────────────────────────────────────────────────────┐
│ 阶段七：质量校验（03-AI辅助设计）                      │
│  Impeccable → 设计质量审计                            │
│  web-design-guidelines → 可访问性审计                 │
│ 【新增】audit-component → 组件级代码审计               │
│ 【新增】a11y-audit / bee-dev-a11y → 自动化 a11y 测试   │
│ 【新增】vue-testing-best-practices → 自动化测试        │
└─────────────────────────────────────────────────────┘
                        ↓
┌─────────────────────────────────────────────────────┐
│ 阶段八：视觉素材生成（按需启用）                       │
│  canvas-design / algorithmic-art / remotion           │
└─────────────────────────────────────────────────────┘
                        ↓
┌─────────────────────────────────────────────────────┐
│ 阶段九：持续优化                                      │
│ 【新增】vue-expert-performance / vue-scan → 性能监控   │
│ 【新增】jeecg-codegen / vtj-pro → 低代码快速生成       │
└─────────────────────────────────────────────────────┘
```

## 详细工作流程

### 阶段一：项目初始化

#### 1.1 创建项目
```bash
npm create vue@latest my-project
cd my-project
npm install
```

#### 1.2 安装依赖
```bash
# Element Plus
npm install element-plus @element-plus/icons-vue

# 按需引入插件
npm install -D unplugin-vue-components unplugin-auto-import

# 状态管理
npm install pinia

# 路由
npm install vue-router

# 动效库（按需）
npm install gsap
npm install @vueuse/motion

# 图表库（按需）
npm install vue-echarts echarts
npm install apexcharts vue3-apexcharts

# 表单验证（按需）
npm install @regle/core @regle/validators
npm install vee-validate @vee-validate/rules
```

#### 1.3 配置 Vite
```typescript
// vite.config.ts
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'

export default defineConfig({
  plugins: [
    vue(),
    AutoImport({
      imports: ['vue', 'vue-router', 'pinia', '@vueuse/core'],
      dts: 'src/auto-imports.d.ts'
    }),
    Components({
      resolvers: [ElementPlusResolver()],
      dts: 'src/components.d.ts'
    })
  ]
})
```

### 阶段二：设计方向锚定

#### 2.1 使用 frontend-design
```
调用 frontend-design 技能：
1. 分析项目类型和目标用户
2. 选择合适的设计风格
3. 确定色彩策略和排版风格
4. 生成 DESIGN.md 文档
```

#### 2.2 使用 Taste Skill
```
调用 Taste Skill 技能：
1. 审查设计方向的审美质量
2. 检查色彩和谐度和对比度
3. 评估视觉平衡和层次
4. 提供优化建议
```

### 阶段三：设计系统生成

#### 3.1 使用 ui-ux-pro-max
```
调用 ui-ux-pro-max 技能：
1. 生成完整的设计令牌系统
2. 创建色彩、间距、字体规范
3. 定义组件设计规范
4. 生成 CSS 变量文件
```

### 阶段四：主题落地

#### 4.1 使用 awesome-design-md
```
调用 awesome-design-md 技能：
1. 解析 DESIGN.md 文档
2. 映射设计变量到 CSS 变量
3. 生成 Element Plus 主题配置
4. 创建主题切换功能
```

#### 4.2 配置图标系统
```
使用 el-icon 和 icon-system 技能：
1. 选择合适的图标库
2. 配置图标系统
3. 创建图标组件封装
4. 实现图标主题适配
```

### 阶段五：组件封装

#### 5.1 封装基础组件
```
使用组件封装技能：
1. 封装 CustomTable 组件
2. 封装 CustomForm 组件
3. 封装 CustomDialog 组件
4. 实现统一的组件规范
```

#### 5.2 集成表单验证
```
使用表单验证技能：
1. 选择 Regle 或 VeeValidate
2. 配置验证规则
3. 集成到 CustomForm
4. 实现错误处理
```

### 阶段六：页面开发

#### 6.1 状态管理
```
使用 vue-pinia-best-practices：
1. 设计 Store 结构
2. 实现状态管理逻辑
3. 配置持久化
4. 集成到组件中
```

#### 6.2 路由配置
```
使用 vue-router-best-practices：
1. 设计路由结构
2. 配置路由守卫
3. 实现懒加载
4. 处理权限控制
```

#### 6.3 动效实现
```
使用 gsap-skills 或 motion-vue：
1. 设计交互动效
2. 实现页面过渡
3. 添加微交互
4. 优化性能
```

#### 6.4 图表集成
```
使用 apexcharts-vue 或 vue3-echarts：
1. 选择合适的图表库
2. 配置图表组件
3. 实现数据可视化
4. 添加交互功能
```

### 阶段七：质量校验

#### 7.1 设计质量审计
```
使用 Impeccable 技能：
1. 审查视觉层次
2. 检查可用性
3. 验证一致性
4. 评估可访问性
```

#### 7.2 可访问性审计
```
使用 web-design-guidelines、a11y-audit、bee-dev-a11y：
1. 检查 WCAG 合规性
2. 验证键盘导航
3. 测试屏幕阅读器
4. 运行自动化扫描
```

#### 7.3 代码审计
```
使用 audit-component 技能：
1. 审查组件代码质量
2. 检查性能问题
3. 验证类型安全
4. 评估可维护性
```

#### 7.4 自动化测试
```
使用 vue-testing-best-practices：
1. 编写单元测试
2. 实现集成测试
3. 配置 E2E 测试
4. 设置 CI/CD
```

### 阶段八：视觉素材生成（按需启用）

#### 8.1 Canvas 设计
```
使用 canvas-design 技能：
1. 创建 Canvas 组件
2. 实现图形绘制
3. 添加动画效果
4. 优化性能
```

#### 8.2 算法艺术
```
使用 algorithmic-art 技能：
1. 实现生成艺术算法
2. 创建粒子系统
3. 添加交互效果
4. 优化渲染性能
```

#### 8.3 Remotion 视频
```
使用 remotion 技能：
1. 设计视频结构
2. 创建动画序列
3. 添加音频和特效
4. 导出最终视频
```

### 阶段九：持续优化

#### 9.1 性能监控
```
使用 vue-expert-performance 和 vue-scan：
1. 监控运行时性能
2. 分析内存使用
3. 检查 FPS
4. 优化加载性能
```

#### 9.2 低代码生成
```
使用 jeecg-codegen 或 vtj-pro：
1. 使用代码生成器
2. 快速生成 CRUD 页面
3. 可视化表单设计
4. 加速开发流程
```

## 工具集成

### VS Code 技能集成
```
1. 安装 Devin CLI 技能插件
2. 配置技能自动调用
3. 设置快捷键
4. 集成到开发流程
```

### AI 辅助开发
```
1. 使用技能进行代码生成
2. AI 辅助设计决策
3. 自动化代码审查
4. 智能错误修复
```

## 质量保证

### 代码质量
- 遵循 Vue3 最佳实践
- 使用 TypeScript 类型检查
- 配置 ESLint 和 Prettier
- 实施代码审查流程

### 性能质量
- 使用性能监控工具
- 优化加载性能
- 减少内存占用
- 提升用户体验

### 可访问性
- 遵循 WCAG 标准
- 支持键盘导航
- 兼容屏幕阅读器
- 提供文本替代

## 持续改进

### 定期审查
- 每月设计质量审查
- 每周性能监控
- 每次发布前完整测试
- 持续收集用户反馈

### 技能更新
- 关注技能库更新
- 学习新的设计趋势
- 优化工作流程
- 分享最佳实践

## 项目交付

### 交付清单
- [ ] 完整的设计系统
- [ ] 响应式界面实现
- [ ] 性能优化完成
- [ ] 可访问性达标
- [ ] 测试覆盖完整
- [ ] 文档齐全
- [ ] 代码质量高
- [ ] 用户体验优秀

### 交付文档
- 设计系统文档
- 组件使用文档
- API 接口文档
- 部署运维文档
- 用户使用手册
