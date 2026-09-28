import MarkdownIt from 'markdown-it'
import anchor from 'markdown-it-anchor'

// 初始化markdown-it实例
const md = new MarkdownIt({
  html: true,
  linkify: true,
  typographer: true
}).use(anchor, {
  level: [1, 2, 3, 4, 5, 6], // 支持所有级别的标题
  slugify: (s: string) => s
    .toLowerCase()
    .trim()
    .replace(/[^\w\s-]/g, '') // 移除非字母数字字符
    .replace(/[\s_-]+/g, '-') // 将空格和下划线替换为连字符
    .replace(/^-+|-+$/g, '') // 移除首尾连字符
})

// 文章索引项接口
export interface ArticleIndex {
  level: number // 标题级别 (1-6)
  text: string // 标题文本
  id: string // 锚点ID
  children?: ArticleIndex[] // 子标题
}

/**
 * 从markdown文本中解析文章索引
 * @param markdownText markdown文本
 * @returns 文章索引树
 */
export function parseArticleIndex(markdownText: string): ArticleIndex[] {
  const tokens = md.parse(markdownText, {})
  const indices: ArticleIndex[] = []
  const stack: ArticleIndex[] = []

  tokens.forEach((token) => {
    if (token.type === 'heading_open') {
      const level = parseInt(token.tag.substring(1), 10)
      const headingText = tokens[tokens.indexOf(token) + 1].content || ''
      const id = generateSlug(headingText)

      const index: ArticleIndex = {
        level,
        text: headingText,
        id
      }

      // 如果栈为空，直接添加到根级别
      if (stack.length === 0) {
        indices.push(index)
        stack.push(index)
      } else {
        // 找到合适的父级
        while (stack.length > 0 && stack[stack.length - 1].level >= level) {
          stack.pop()
        }

        if (stack.length === 0) {
          indices.push(index)
        } else {
          const parent = stack[stack.length - 1]
          if (!parent.children) {
            parent.children = []
          }
          parent.children.push(index)
        }

        stack.push(index)
      }
    }
  })

  return indices
}

/**
 * 从HTML文本中解析文章索引
 * @param htmlText HTML文本
 * @returns 文章索引树
 */
export function parseArticleIndexFromHtml(htmlText: string): ArticleIndex[] {
  const indices: ArticleIndex[] = []
  const stack: ArticleIndex[] = []

  // 使用正则表达式匹配标题
  const headingRegex = /<h([1-6])[^>]*id="([^"]*)"[^>]*>(.*?)<\/h\1>/gi
  let match

  while ((match = headingRegex.exec(htmlText)) !== null) {
    const level = parseInt(match[1], 10)
    const id = match[2]
    const text = match[3].replace(/<[^>]*>/g, '') // 移除HTML标签

    const index: ArticleIndex = {
      level,
      text,
      id
    }

    // 如果栈为空，直接添加到根级别
    if (stack.length === 0) {
      indices.push(index)
      stack.push(index)
    } else {
      // 找到合适的父级
      while (stack.length > 0 && stack[stack.length - 1].level >= level) {
        stack.pop()
      }

      if (stack.length === 0) {
        indices.push(index)
      } else {
        const parent = stack[stack.length - 1]
        if (!parent.children) {
          parent.children = []
        }
        parent.children.push(index)
      }

      stack.push(index)
    }
  }

  return indices
}

/**
 * 渲染markdown为HTML
 * @param markdownText markdown文本
 * @returns HTML文本
 */
export function renderMarkdown(markdownText: string): string {
  return md.render(markdownText)
}

/**
 * 生成URL友好的slug
 * @param text 文本
 * @returns slug
 */
function generateSlug(text: string): string {
  return text
    .toLowerCase()
    .trim()
    .replace(/[^\w\s-]/g, '')
    .replace(/[\s_-]+/g, '-')
    .replace(/^-+|-+$/g, '')
}

/**
 * 扁平化索引树（用于渲染）
 * @param indices 索引树
 * @param level 缩进级别
 * @returns 扁平化的索引数组
 */
export function flattenIndices(
  indices: ArticleIndex[],
  level: number = 0
): Array<ArticleIndex & { level: number }> {
  const result: Array<ArticleIndex & { level: number }> = []

  indices.forEach((index) => {
    result.push({ ...index, level })
    if (index.children && index.children.length > 0) {
      result.push(...flattenIndices(index.children, level + 1))
    }
  })

  return result
}
