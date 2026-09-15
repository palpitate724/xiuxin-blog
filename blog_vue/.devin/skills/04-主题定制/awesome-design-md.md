# Awesome Design MD

选取品牌 DESIGN.md 并映射到主题变量的技能。

## DESIGN.md 结构

### 品牌设计文档
```markdown
# 项目设计文档

## 品牌标识
- 品牌名称: [品牌名]
- 品牌标语: [标语]
- 品牌故事: [故事]

## 色彩系统
### 主色调
- 主色: #1890ff
- 辅助色: #52c41a
- 强调色: #faad14

### 中性色
- 黑色: #000000
- 白色: #ffffff
- 灰色系列: #f5f5f5, #d9d9d9, #8c8c8c, #595959, #262626

### 语义色
- 成功: #52c41a
- 警告: #faad14
- 错误: #ff4d4f
- 信息: #1890ff

## 字体系统
### 字体族
- 主要字体: Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif
- 代码字体: 'JetBrains Mono', 'Fira Code', monospace
- 衬线字体: Georgia, 'Times New Roman', serif

### 字号规范
- H1: 48px
- H2: 36px
- H3: 30px
- H4: 24px
- H5: 20px
- H6: 16px
- Body: 16px
- Small: 14px
- Caption: 12px

### 字重规范
- Light: 300
- Regular: 400
- Medium: 500
- Semibold: 600
- Bold: 700

## 间距系统
- 基础单位: 4px
- 间距等级: 4px, 8px, 12px, 16px, 24px, 32px, 48px, 64px

## 圆角规范
- 小圆角: 2px
- 中圆角: 4px
- 大圆角: 8px
- 超大圆角: 16px
- 完全圆角: 9999px

## 阴影规范
- 小阴影: 0 1px 2px rgba(0, 0, 0, 0.05)
- 中阴影: 0 4px 6px rgba(0, 0, 0, 0.1)
- 大阴影: 0 10px 15px rgba(0, 0, 0, 0.1)
- 超大阴影: 0 20px 25px rgba(0, 0, 0, 0.1)
```

## 映射到 CSS 变量

### 颜色映射
```css
:root {
  /* 主色调 */
  --color-primary: #1890ff;
  --color-secondary: #52c41a;
  --color-accent: #faad14;
  
  /* 中性色 */
  --color-black: #000000;
  --color-white: #ffffff;
  --color-gray-50: #f5f5f5;
  --color-gray-200: #d9d9d9;
  --color-gray-500: #8c8c8c;
  --color-gray-700: #595959;
  --color-gray-900: #262626;
  
  /* 语义色 */
  --color-success: #52c41a;
  --color-warning: #faad14;
  --color-error: #ff4d4f;
  --color-info: #1890ff;
}
```

### 字体映射
```css
:root {
  /* 字体族 */
  --font-family-primary: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
  --font-family-mono: 'JetBrains Mono', 'Fira Code', monospace;
  --font-family-serif: Georgia, 'Times New Roman', serif;
  
  /* 字号 */
  --font-size-h1: 48px;
  --font-size-h2: 36px;
  --font-size-h3: 30px;
  --font-size-h4: 24px;
  --font-size-h5: 20px;
  --font-size-h6: 16px;
  --font-size-body: 16px;
  --font-size-small: 14px;
  --font-size-caption: 12px;
  
  /* 字重 */
  --font-weight-light: 300;
  --font-weight-regular: 400;
  --font-weight-medium: 500;
  --font-weight-semibold: 600;
  --font-weight-bold: 700;
}
```

### 间距映射
```css
:root {
  --spacing-1: 4px;
  --spacing-2: 8px;
  --spacing-3: 12px;
  --spacing-4: 16px;
  --spacing-6: 24px;
  --spacing-8: 32px;
  --spacing-12: 48px;
  --spacing-16: 64px;
}
```

### 圆角映射
```css
:root {
  --radius-sm: 2px;
  --radius-md: 4px;
  --radius-lg: 8px;
  --radius-xl: 16px;
  --radius-full: 9999px;
}
```

### 阴影映射
```css
:root {
  --shadow-sm: 0 1px 2px rgba(0, 0, 0, 0.05);
  --shadow-md: 0 4px 6px rgba(0, 0, 0, 0.1);
  --shadow-lg: 0 10px 15px rgba(0, 0, 0, 0.1);
  --shadow-xl: 0 20px 25px rgba(0, 0, 0, 0.1);
}
```

## Element Plus 主题映射

### Element Plus 变量映射
```scss
// styles/element-plus/theme.scss
@forward 'element-plus/theme-chalk/src/common/var.scss' with (
  $colors: (
    'primary': (
      'base': #1890ff,
    ),
    'success': (
      'base': #52c41a,
    ),
    'warning': (
      'base': #faad14,
    ),
    'danger': (
      'base': #ff4d4f,
    ),
    'info': (
      'base': #1890ff,
    ),
  ),
  $font-family: (
    '': 'Inter',
  ),
  $border-radius: (
    'base': 4px,
    'small': 2px,
    'large': 8px,
    'extra-large': 16px,
  ),
);
```

## 自动化映射工具

### 解析 DESIGN.md
```typescript
// utils/parseDesignMd.ts
import { readFileSync } from 'fs'
import { parse } from 'yaml'

interface DesignDoc {
  brand: {
    name: string
    slogan: string
    story: string
  }
  colors: {
    primary: string
    secondary: string
    accent: string
    neutrals: Record<string, string>
    semantic: Record<string, string>
  }
  typography: {
    fontFamily: Record<string, string>
    fontSize: Record<string, string>
    fontWeight: Record<string, string>
  }
  spacing: {
    baseUnit: string
    scale: string[]
  }
  borderRadius: Record<string, string>
  boxShadow: Record<string, string>
}

export function parseDesignMd(filePath: string): DesignDoc {
  const content = readFileSync(filePath, 'utf-8')
  return parse(content)
}
```

### 生成 CSS 变量
```typescript
// utils/generateCssVariables.ts
export function generateCssVariables(designDoc: DesignDoc): string {
  let css = ':root {\n'
  
  // 颜色变量
  css += `  --color-primary: ${designDoc.colors.primary};\n`
  css += `  --color-secondary: ${designDoc.colors.secondary};\n`
  css += `  --color-accent: ${designDoc.colors.accent};\n`
  
  // 中性色
  Object.entries(designDoc.colors.neutrals).forEach(([name, value]) => {
    css += `  --color-${name}: ${value};\n`
  })
  
  // 语义色
  Object.entries(designDoc.colors.semantic).forEach(([name, value]) => {
    css += `  --color-${name}: ${value};\n`
  })
  
  // 字体变量
  Object.entries(designDoc.typography.fontFamily).forEach(([name, value]) => {
    css += `  --font-family-${name}: ${value};\n`
  })
  
  // 字号变量
  Object.entries(designDoc.typography.fontSize).forEach(([name, value]) => {
    css += `  --font-size-${name}: ${value};\n`
  })
  
  // 字重变量
  Object.entries(designDoc.typography.fontWeight).forEach(([name, value]) => {
    css += `  --font-weight-${name}: ${value};\n`
  })
  
  // 间距变量
  designDoc.spacing.scale.forEach((value, index) => {
    css += `  --spacing-${index + 1}: ${value};\n`
  })
  
  // 圆角变量
  Object.entries(designDoc.borderRadius).forEach(([name, value]) => {
    css += `  --radius-${name}: ${value};\n`
  })
  
  // 阴影变量
  Object.entries(designDoc.boxShadow).forEach(([name, value]) => {
    css += `  --shadow-${name}: ${value};\n`
  })
  
  css += '}\n'
  return css
}
```

### 生成 Element Plus 主题
```typescript
// utils/generateElementPlusTheme.ts
export function generateElementPlusTheme(designDoc: DesignDoc): string {
  let scss = '@forward \'element-plus/theme-chalk/src/common/var.scss\' with (\n'
  
  // 颜色映射
  scss += '  $colors: (\n'
  scss += `    'primary': (\n`
  scss += `      'base': ${designDoc.colors.primary},\n`
  scss += `    ),\n`
  scss += `    'success': (\n`
  scss += `      'base': ${designDoc.colors.semantic.success},\n`
  scss += `    ),\n`
  scss += `    'warning': (\n`
  scss += `      'base': ${designDoc.colors.semantic.warning},\n`
  scss += `    ),\n`
  scss += `    'danger': (\n`
  scss += `      'base': ${designDoc.colors.semantic.error},\n`
  scss += `    ),\n`
  scss += `  ),\n`
  
  // 字体映射
  scss += '  $font-family: (\n'
  scss += `    '': '${designDoc.typography.fontFamily.primary}',\n`
  scss += `  ),\n`
  
  // 圆角映射
  scss += '  $border-radius: (\n'
  Object.entries(designDoc.borderRadius).forEach(([name, value]) => {
    scss += `    '${name}': ${value},\n`
  })
  scss += `  ),\n`
  
  scss += ');\n'
  return scss
}
```

## 使用示例

### 完整流程
```typescript
// scripts/setupTheme.ts
import { parseDesignMd } from './utils/parseDesignMd'
import { generateCssVariables } from './utils/generateCssVariables'
import { generateElementPlusTheme } from './utils/generateElementPlusTheme'
import { writeFileSync } from 'fs'

const designDoc = parseDesignMd('./DESIGN.md')

// 生成 CSS 变量
const cssVariables = generateCssVariables(designDoc)
writeFileSync('./styles/variables.css', cssVariables)

// 生成 Element Plus 主题
const elementPlusTheme = generateElementPlusTheme(designDoc)
writeFileSync('./styles/element-plus/theme.scss', elementPlusTheme)

console.log('主题文件生成完成')
```

## 设计验证

### 对比度检查
```typescript
// utils/validateContrast.ts
export function validateContrast(designDoc: DesignDoc): ValidationResult {
  const results: ValidationResult = {
    valid: true,
    errors: []
  }
  
  // 检查主色与背景对比度
  const primaryContrast = getContrastRatio(
    designDoc.colors.primary,
    designDoc.colors.neutrals.white
  )
  
  if (primaryContrast < 4.5) {
    results.valid = false
    results.errors.push(
      `主色与白色背景对比度不足: ${primaryContrast.toFixed(2)} (需要 >= 4.5)`
    )
  }
  
  // 检查文字对比度
  const textContrast = getContrastRatio(
    designDoc.colors.neutrals.black,
    designDoc.colors.neutrals.white
  )
  
  if (textContrast < 7) {
    results.valid = false
    results.errors.push(
      `黑色文字与白色背景对比度不足: ${textContrast.toFixed(2)} (需要 >= 7)`
    )
  }
  
  return results
}
```

### 颜色和谐度检查
```typescript
// utils/validateColorHarmony.ts
export function validateColorHarmony(designDoc: DesignDoc): ValidationResult {
  const results: ValidationResult = {
    valid: true,
    errors: []
  }
  
  const colors = [
    designDoc.colors.primary,
    designDoc.colors.secondary,
    designDoc.colors.accent
  ]
  
  // 检查颜色是否过于相似
  for (let i = 0; i < colors.length; i++) {
    for (let j = i + 1; j < colors.length; j++) {
      const distance = getColorDistance(colors[i], colors[j])
      if (distance < 15) {
        results.valid = false
        results.errors.push(
          `颜色 ${colors[i]} 和 ${colors[j]} 过于相似 (距离: ${distance.toFixed(2)})`
        )
      }
    }
  }
  
  return results
}
```

## 最佳实践

### DESIGN.md 编写规范
1. 使用 Markdown 格式
2. 包含完整的设计规范
3. 提供具体的颜色值
4. 包含使用示例
5. 保持文档更新

### 映射规范
1. 保持命名一致性
2. 提供语义化命名
3. 包含所有必要变量
4. 考虑深色模式
5. 提供默认值

### 自动化工具
1. 自动解析 DESIGN.md
2. 生成 CSS 变量
3. 生成主题文件
4. 验证设计规范
5. 提供错误提示
