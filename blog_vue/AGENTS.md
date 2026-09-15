# 项目工作流程和技能配置

## 核心规则
**所有代码生成任务必须严格遵循项目工作流程。**

在开始任何代码生成之前：
1. 阅读 `.devin/workflow.md` 了解完整的九阶段开发工作流程
2. 确定当前任务处于工作流程的哪个阶段
3. 查阅该阶段对应的技能文件（位于 `.devin/skills/` 目录）
4. 按照技能文件的指导进行设计和开发
5. 确保生成的代码符合工作流程中定义的最佳实践

## 工作流程概述
本项目使用 AI UI Design Skills 工作流程，提供完整的 Vue3 项目设计和开发流程。

## 工作流程文件
- `.devin/workflow.md` - 完整的九阶段开发工作流程

## 已加载技能

### 01-开发规范
- `element-plus-best-practices.md` - Element Plus 最佳实践
- `project-structure.md` - 项目结构规范
- `vue3-component-standards.md` - Vue3 组件标准

### 02-组件封装
- `composition-patterns.md` - Composition API 模式
- `custom-form-pattern.md` - 自定义表单模式
- `custom-table-pattern.md` - 自定义表格模式
- `form-validation-regle.md` - Regle 表单验证
- `form-validation-veevalidate.md` - VeeValidate 表单验证
- `props-slots-forwarding.md` - Props 和 Slots 转发

### 03-AI辅助设计
#### 工程化保障层
- `create-adaptable-composable.md` - 创建可适配的 Composable
- `jeecg-codegen.md` - Jeecg 代码生成
- `vtj-pro.md` - VTJ Pro 工具
- `vue-best-practices.md` - Vue 最佳实践
- `vue-debug-guides.md` - Vue 调试指南
- `vue-expert-performance.md` - Vue 专家级性能优化
- `vue-pinia-best-practices.md` - Pinia 最佳实践
- `vue-router-best-practices.md` - Vue Router 最佳实践
- `vue-testing-best-practices.md` - Vue 测试最佳实践

#### 视觉创作扩展层
- `algorithmic-art.md` - 算法艺术
- `apexcharts-vue.md` - ApexCharts Vue 集成
- `canvas-design.md` - Canvas 设计
- `gsap-skills.md` - GSAP 动效技能
- `motion-vue.md` - Vue 动效
- `remotion.md` - Remotion 视频制作
- `vue3-echarts.md` - Vue3 ECharts 集成

#### 设计方向层
- `frontend-design.md` - 前端设计
- `tasteskill.md` - 审美技能
- `ui-ux-pro-max.md` - UI/UX 专业级设计

#### 质量校验层
- `a11y-audit.md` - 可访问性审计
- `audit-component.md` - 组件审计
- `bee-dev-a11y.md` - Bee Dev 可访问性
- `impeccable.md` - 完美设计审计
- `web-design-guidelines.md` - Web 设计指南

### 04-主题定制
- `awesome-design-md.md` - 设计文档生成
- `css-variable-theming.md` - CSS 变量主题
- `dark-mode.md` - 暗黑模式
- `design-tokens.md` - 设计令牌
- `el-icon.md` - Element Plus 图标
- `icon-system.md` - 图标系统

### 05-性能优化
- `auto-import-config.md` - 自动导入配置
- `component-lazy-loading.md` - 组件懒加载
- `vue-scan.md` - Vue 扫描工具

## 开发工作流程

### 阶段一：项目初始化
- 配置 Vue3 + Vite + TypeScript
- 安装 Element Plus 和按需引入插件
- 配置状态管理和路由

### 阶段二：设计方向锚定
- 使用 frontend-design 选定美学锚点
- 使用 Taste Skill 注入审美判断力

### 阶段三：设计系统生成
- 使用 ui-ux-pro-max 生成完整设计系统

### 阶段四：主题落地
- 使用 awesome-design-md 选取品牌设计
- 配置图标系统

### 阶段五：组件封装
- 封装 CustomTable / CustomForm / CustomDialog
- 集成表单验证

### 阶段六：页面开发
- 状态管理
- 路由配置
- 动效实现
- 图表集成

### 阶段七：质量校验
- 设计质量审计
- 可访问性审计
- 代码审计
- 自动化测试

### 阶段八：视觉素材生成（按需）
- Canvas 设计
- 算法艺术
- Remotion 视频

### 阶段九：持续优化
- 性能监控
- 低代码生成

## 项目特定配置

### 技术栈
- Vue 3 + TypeScript + Vite
- Vue Router
- Pinia (状态管理)
- Axios (HTTP 客户端)

### 可用脚本
- `npm run dev` - 开发服务器
- `npm run build` - 构建生产版本
- `npm run preview` - 预览生产构建

### 使用说明
当需要使用特定技能时，可以参考 `.devin/skills/` 目录下的相应文档。每个技能文件都包含详细的指导说明和最佳实践。

详细的完整工作流程请参考 `.devin/workflow.md` 文件。
