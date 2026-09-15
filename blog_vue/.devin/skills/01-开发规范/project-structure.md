# 项目结构规范

Vue3 项目的标准目录结构和组织方式。

## 标准目录结构

```
src/
├── assets/           # 静态资源
│   ├── images/
│   ├── fonts/
│   └── styles/
├── components/       # 公共组件
│   ├── common/      # 通用组件
│   ├── business/    # 业务组件
│   └── layout/      # 布局组件
├── composables/      # 组合式函数
├── directives/       # 自定义指令
├── hooks/           # 自定义 Hooks
├── router/          # 路由配置
├── stores/          # Pinia 状态管理
├── utils/           # 工具函数
├── views/           # 页面组件
├── App.vue          # 根组件
└── main.ts          # 入口文件
```

## 文件命名规范

- 组件文件：PascalCase.vue
- 工具文件：kebab-case.ts
- 样式文件：kebab-case.css/.scss
- 类型文件：*.d.ts

## 导入路径规范

使用 @ 别名简化导入：

```typescript
import { formatDate } from '@/utils/date'
import Button from '@/components/common/Button.vue'
```

## 环境配置

```env
# .env.development
VITE_API_BASE_URL=http://localhost:3000

# .env.production
VITE_API_BASE_URL=https://api.example.com
```
