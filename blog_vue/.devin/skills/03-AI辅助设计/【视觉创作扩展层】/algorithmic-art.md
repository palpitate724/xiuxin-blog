# Algorithmic Art Skill

算法艺术技能，提供生成艺术和创意编程能力。

## 基础模式

### 随机生成
```typescript
// 随机数生成
function random(min: number, max: number): number {
  return Math.random() * (max - min) + min
}

function randomInt(min: number, max: number): number {
  return Math.floor(random(min, max))
}

// 随机颜色
function randomColor(): string {
  const r = randomInt(0, 255)
  const g = randomInt(0, 255)
  const b = randomInt(0, 255)
  return `rgb(${r}, ${g}, ${b})`
}

// 随机选择
function randomChoice<T>(array: T[]): T {
  return array[Math.floor(Math.random() * array.length)]
}
```

### 噪声函数
```typescript
// 简单噪声函数
function noise(x: number, y: number): number {
  const n = Math.sin(x * 12.9898 + y * 78.233) * 43758.5453
  return n - Math.floor(n)
}

// 平滑噪声
function smoothNoise(x: number, y: number): number {
  const corners = (noise(x-1, y-1) + noise(x+1, y-1) + noise(x-1, y+1) + noise(x+1, y+1)) / 16
  const sides = (noise(x-1, y) + noise(x+1, y) + noise(x, y-1) + noise(x, y+1)) / 8
  const center = noise(x, y) / 4
  return corners + sides + center
}
```

## 分形艺术

### 谢尔宾斯基三角形
```typescript
function drawSierpinskiTriangle(
  ctx: CanvasRenderingContext2D,
  x: number,
  y: number,
  size: number,
  depth: number
) {
  if (depth === 0) {
    ctx.beginPath()
    ctx.moveTo(x, y)
    ctx.lineTo(x + size, y)
    ctx.lineTo(x + size / 2, y - size * Math.sqrt(3) / 2)
    ctx.closePath()
    ctx.fillStyle = `hsl(${random(0, 360)}, 70%, 50%)`
    ctx.fill()
  } else {
    const newSize = size / 2
    drawSierpinskiTriangle(ctx, x, y, newSize, depth - 1)
    drawSierpinskiTriangle(ctx, x + newSize, y, newSize, depth - 1)
    drawSierpinskiTriangle(ctx, x + newSize / 2, y - newSize * Math.sqrt(3) / 2, newSize, depth - 1)
  }
}
```

### 科赫雪花
```typescript
function drawKochSnowflake(
  ctx: CanvasRenderingContext2D,
  x1: number,
  y1: number,
  x2: number,
  y2: number,
  depth: number
) {
  if (depth === 0) {
    ctx.beginPath()
    ctx.moveTo(x1, y1)
    ctx.lineTo(x2, y2)
    ctx.strokeStyle = '#1890ff'
    ctx.lineWidth = 1
    ctx.stroke()
  } else {
    const dx = x2 - x1
    const dy = y2 - y1
    
    const x3 = x1 + dx / 3
    const y3 = y1 + dy / 3
    
    const x5 = x1 + 2 * dx / 3
    const y5 = y1 + 2 * dy / 3
    
    const x4 = x3 + (dx / 3) * Math.cos(-Math.PI / 3) - (dy / 3) * Math.sin(-Math.PI / 3)
    const y4 = y3 + (dx / 3) * Math.sin(-Math.PI / 3) + (dy / 3) * Math.cos(-Math.PI / 3)
    
    drawKochSnowflake(ctx, x1, y1, x3, y3, depth - 1)
    drawKochSnowflake(ctx, x3, y3, x4, y4, depth - 1)
    drawKochSnowflake(ctx, x4, y4, x5, y5, depth - 1)
    drawKochSnowflake(ctx, x5, y5, x2, y2, depth - 1)
  }
}
```

## 粒子系统

### 基础粒子
```typescript
class Particle {
  x: number
  y: number
  vx: number
  vy: number
  size: number
  color: string
  life: number
  maxLife: number
  
  constructor(x: number, y: number) {
    this.x = x
    this.y = y
    this.vx = random(-2, 2)
    this.vy = random(-2, 2)
    this.size = random(2, 5)
    this.color = randomColor()
    this.life = 1
    this.maxLife = random(50, 100)
  }
  
  update() {
    this.x += this.vx
    this.y += this.vy
    this.life -= 1 / this.maxLife
    
    // 边界反弹
    if (this.x < 0 || this.x > canvas.width) this.vx *= -1
    if (this.y < 0 || this.y > canvas.height) this.vy *= -1
  }
  
  draw(ctx: CanvasRenderingContext2D) {
    ctx.globalAlpha = this.life
    ctx.beginPath()
    ctx.arc(this.x, this.y, this.size, 0, Math.PI * 2)
    ctx.fillStyle = this.color
    ctx.fill()
    ctx.globalAlpha = 1
  }
  
  isDead(): boolean {
    return this.life <= 0
  }
}
```

### 粒子系统
```typescript
class ParticleSystem {
  particles: Particle[] = []
  maxParticles: number
  
  constructor(maxParticles: number = 100) {
    this.maxParticles = maxParticles
  }
  
  emit(x: number, y: number, count: number = 1) {
    for (let i = 0; i < count; i++) {
      if (this.particles.length < this.maxParticles) {
        this.particles.push(new Particle(x, y))
      }
    }
  }
  
  update() {
    this.particles.forEach(p => p.update())
    this.particles = this.particles.filter(p => !p.isDead())
  }
  
  draw(ctx: CanvasRenderingContext2D) {
    this.particles.forEach(p => p.draw(ctx))
  }
}
```

## 生命游戏

### 细胞自动机
```typescript
class GameOfLife {
  grid: number[][]
  cols: number
  rows: number
  cellSize: number
  
  constructor(cols: number, rows: number, cellSize: number) {
    this.cols = cols
    this.rows = rows
    this.cellSize = cellSize
    this.grid = this.createGrid()
  }
  
  createGrid(): number[][] {
    const grid: number[][] = []
    for (let i = 0; i < this.cols; i++) {
      grid[i] = []
      for (let j = 0; j < this.rows; j++) {
        grid[i][j] = Math.random() > 0.5 ? 1 : 0
      }
    }
    return grid
  }
  
  countNeighbors(grid: number[][], x: number, y: number): number {
    let sum = 0
    for (let i = -1; i < 2; i++) {
      for (let j = -1; j < 2; j++) {
        const col = (x + i + this.cols) % this.cols
        const row = (y + j + this.rows) % this.rows
        sum += grid[col][row]
      }
    }
    sum -= grid[x][y]
    return sum
  }
  
  update() {
    const nextGrid = this.grid.map(arr => [...arr])
    
    for (let i = 0; i < this.cols; i++) {
      for (let j = 0; j < this.rows; j++) {
        const state = this.grid[i][j]
        const neighbors = this.countNeighbors(this.grid, i, j)
        
        if (state === 0 && neighbors === 3) {
          nextGrid[i][j] = 1
        } else if (state === 1 && (neighbors < 2 || neighbors > 3)) {
          nextGrid[i][j] = 0
        }
      }
    }
    
    this.grid = nextGrid
  }
  
  draw(ctx: CanvasRenderingContext2D) {
    for (let i = 0; i < this.cols; i++) {
      for (let j = 0; j < this.rows; j++) {
        if (this.grid[i][j] === 1) {
          ctx.fillStyle = '#1890ff'
          ctx.fillRect(
            i * this.cellSize,
            j * this.cellSize,
            this.cellSize - 1,
            this.cellSize - 1
          )
        }
      }
    }
  }
}
```

## 流场

### 向量场
```typescript
class FlowField {
  vectors: { x: number; y: number }[][]
  cols: number
  rows: number
  scale: number
  particles: Particle[]
  
  constructor(width: number, height: number, scale: number) {
    this.cols = Math.floor(width / scale)
    this.rows = Math.floor(height / scale)
    this.scale = scale
    this.vectors = this.createVectors()
    this.particles = []
    
    for (let i = 0; i < 500; i++) {
      this.particles.push(new FlowParticle(width, height))
    }
  }
  
  createVectors(): { x: number; y: number }[][] {
    const vectors: { x: number; y: number }[][] = []
    
    for (let i = 0; i < this.cols; i++) {
      vectors[i] = []
      for (let j = 0; j < this.rows; j++) {
        const angle = noise(i * 0.1, j * 0.1) * Math.PI * 2
        vectors[i][j] = {
          x: Math.cos(angle),
          y: Math.sin(angle)
        }
      }
    }
    
    return vectors
  }
  
  update() {
    this.particles.forEach(p => {
      const col = Math.floor(p.x / this.scale)
      const row = Math.floor(p.y / this.scale)
      
      if (col >= 0 && col < this.cols && row >= 0 && row < this.rows) {
        const vector = this.vectors[col][row]
        p.vx += vector.x * 0.1
        p.vy += vector.y * 0.1
      }
      
      p.update()
    })
  }
  
  draw(ctx: CanvasRenderingContext2D) {
    this.particles.forEach(p => p.draw(ctx))
  }
}

class FlowParticle {
  x: number
  y: number
  vx: number
  vy: number
  history: { x: number; y: number }[]
  maxHistory: number
  
  constructor(width: number, height: number) {
    this.x = random(0, width)
    this.y = random(0, height)
    this.vx = 0
    this.vy = 0
    this.history = []
    this.maxHistory = 20
  }
  
  update() {
    this.x += this.vx
    this.y += this.vy
    
    this.history.push({ x: this.x, y: this.y })
    if (this.history.length > this.maxHistory) {
      this.history.shift()
    }
    
    // 速度衰减
    this.vx *= 0.95
    this.vy *= 0.95
  }
  
  draw(ctx: CanvasRenderingContext2D) {
    if (this.history.length < 2) return
    
    ctx.beginPath()
    ctx.moveTo(this.history[0].x, this.history[0].y)
    
    for (let i = 1; i < this.history.length; i++) {
      ctx.lineTo(this.history[i].x, this.history[i].y)
    }
    
    ctx.strokeStyle = `rgba(24, 144, 255, ${this.history.length / this.maxHistory})`
    ctx.lineWidth = 1
    ctx.stroke()
  }
}
```

## L-System

### 林德迈尔系统
```typescript
class LSystem {
  axiom: string
  rules: Record<string, string>
  angle: number
  length: number
  iterations: number
  
  constructor(axiom: string, rules: Record<string, string>, angle: number) {
    this.axiom = axiom
    this.rules = rules
    this.angle = angle
    this.length = 10
    this.iterations = 5
  }
  
  generate(): string {
    let result = this.axiom
    
    for (let i = 0; i < this.iterations; i++) {
      let nextResult = ''
      
      for (const char of result) {
        nextResult += this.rules[char] || char
      }
      
      result = nextResult
    }
    
    return result
  }
  
  draw(ctx: CanvasRenderingContext2D, instructions: string) {
    ctx.save()
    ctx.translate(canvas.width / 2, canvas.height)
    
    const stack: { x: number; y: number; angle: number }[] = []
    let x = 0
    let y = 0
    let currentAngle = -Math.PI / 2
    
    for (const char of instructions) {
      switch (char) {
        case 'F':
          x += this.length * Math.cos(currentAngle)
          y += this.length * Math.sin(currentAngle)
          ctx.beginPath()
          ctx.moveTo(x - this.length * Math.cos(currentAngle), y - this.length * Math.sin(currentAngle))
          ctx.lineTo(x, y)
          ctx.strokeStyle = '#1890ff'
          ctx.lineWidth = 1
          ctx.stroke()
          break
        case '+':
          currentAngle += this.angle
          break
        case '-':
          currentAngle -= this.angle
          break
        case '[':
          stack.push({ x, y, angle: currentAngle })
          break
        case ']':
          const state = stack.pop()!
          x = state.x
          y = state.y
          currentAngle = state.angle
          break
      }
    }
    
    ctx.restore()
  }
}

// 使用示例
const lSystem = new LSystem('F', {
  'F': 'FF+[+F-F-F]-[-F+F+F]',
  '+': '+',
  '-': '-',
  '[': '[',
  ']': ']'
}, Math.PI / 6)

const instructions = lSystem.generate()
lSystem.draw(ctx, instructions)
```

## 迷宫生成

### 递归回溯算法
```typescript
class MazeGenerator {
  cols: number
  rows: number
  cellSize: number
  grid: Cell[][]
  current: Cell
  stack: Cell[]
  
  constructor(cols: number, rows: number, cellSize: number) {
    this.cols = cols
    this.rows = rows
    this.cellSize = cellSize
    this.grid = this.createGrid()
    this.current = this.grid[0][0]
    this.stack = []
  }
  
  createGrid(): Cell[][] {
    const grid: Cell[][] = []
    
    for (let i = 0; i < this.cols; i++) {
      grid[i] = []
      for (let j = 0; j < this.rows; j++) {
        grid[i][j] = new Cell(i, j, this.cellSize)
      }
    }
    
    return grid
  }
  
  checkNeighbors(cell: Cell): Cell | null {
    const neighbors: Cell[] = []
    
    const top = this.grid[cell.i][cell.j - 1] || null
    const right = this.grid[cell.i + 1]?.[cell.j] || null
    const bottom = this.grid[cell.i][cell.j + 1] || null
    const left = this.grid[cell.i - 1]?.[cell.j] || null
    
    if (top && !top.visited) neighbors.push(top)
    if (right && !right.visited) neighbors.push(right)
    if (bottom && !bottom.visited) neighbors.push(bottom)
    if (left && !left.visited) neighbors.push(left)
    
    if (neighbors.length > 0) {
      const index = Math.floor(Math.random() * neighbors.length)
      return neighbors[index]
    }
    
    return null
  }
  
  step() {
    this.current.visited = true
    const next = this.checkNeighbors(this.current)
    
    if (next) {
      next.visited = true
      this.stack.push(this.current)
      this.removeWalls(this.current, next)
      this.current = next
    } else if (this.stack.length > 0) {
      this.current = this.stack.pop()!
    }
  }
  
  removeWalls(a: Cell, b: Cell) {
    const x = a.i - b.i
    if (x === 1) {
      a.walls.left = false
      b.walls.right = false
    } else if (x === -1) {
      a.walls.right = false
      b.walls.left = false
    }
    
    const y = a.j - b.j
    if (y === 1) {
      a.walls.top = false
      b.walls.bottom = false
    } else if (y === -1) {
      a.walls.bottom = false
      b.walls.top = false
    }
  }
  
  draw(ctx: CanvasRenderingContext2D) {
    for (let i = 0; i < this.cols; i++) {
      for (let j = 0; j < this.rows; j++) {
        this.grid[i][j].draw(ctx)
      }
    }
    
    // 高亮当前单元格
    ctx.fillStyle = '#52c41a'
    ctx.fillRect(
      this.current.i * this.cellSize,
      this.current.j * this.cellSize,
      this.cellSize,
      this.cellSize
    )
  }
}

class Cell {
  i: number
  j: number
  size: number
  walls: { top: boolean; right: boolean; bottom: boolean; left: boolean }
  visited: boolean
  
  constructor(i: number, j: number, size: number) {
    this.i = i
    this.j = j
    this.size = size
    this.walls = { top: true, right: true, bottom: true, left: true }
    this.visited = false
  }
  
  draw(ctx: CanvasRenderingContext2D) {
    const x = this.i * this.size
    const y = this.j * this.size
    
    ctx.strokeStyle = '#333'
    ctx.lineWidth = 2
    
    if (this.walls.top) {
      ctx.beginPath()
      ctx.moveTo(x, y)
      ctx.lineTo(x + this.size, y)
      ctx.stroke()
    }
    
    if (this.walls.right) {
      ctx.beginPath()
      ctx.moveTo(x + this.size, y)
      ctx.lineTo(x + this.size, y + this.size)
      ctx.stroke()
    }
    
    if (this.walls.bottom) {
      ctx.beginPath()
      ctx.moveTo(x + this.size, y + this.size)
      ctx.lineTo(x, y + this.size)
      ctx.stroke()
    }
    
    if (this.walls.left) {
      ctx.beginPath()
      ctx.moveTo(x, y + this.size)
      ctx.lineTo(x, y)
      ctx.stroke()
    }
  }
}
```

## 动画控制

### 动画循环
```typescript
class AnimationController {
  canvas: HTMLCanvasElement
  ctx: CanvasRenderingContext2D
  animationId: number | null = null
  fps: number = 60
  lastTime: number = 0
  interval: number = 1000 / this.fps
  
  constructor(canvas: HTMLCanvasElement) {
    this.canvas = canvas
    this.ctx = canvas.getContext('2d')!
  }
  
  start(callback: (deltaTime: number) => void) {
    const animate = (currentTime: number) => {
      const deltaTime = currentTime - this.lastTime
      
      if (deltaTime >= this.interval) {
        this.lastTime = currentTime - (deltaTime % this.interval)
        callback(deltaTime)
      }
      
      this.animationId = requestAnimationFrame(animate)
    }
    
    this.animationId = requestAnimationFrame(animate)
  }
  
  stop() {
    if (this.animationId) {
      cancelAnimationFrame(this.animationId)
      this.animationId = null
    }
  }
}
```

## Vue3 集成

### 算法艺术组件
```vue
<template>
  <canvas ref="canvasRef"></canvas>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import { ParticleSystem } from '@/utils/particleSystem'

const canvasRef = ref<HTMLCanvasElement>()
let particleSystem: ParticleSystem | null = null
let animationId: number | null = null

onMounted(() => {
  const canvas = canvasRef.value
  if (!canvas) return
  
  const ctx = canvas.getContext('2d')
  if (!ctx) return
  
  canvas.width = 800
  canvas.height = 600
  
  particleSystem = new ParticleSystem(200)
  
  const animate = () => {
    ctx.clearRect(0, 0, canvas.width, canvas.height)
    particleSystem!.update()
    particleSystem!.draw(ctx)
    animationId = requestAnimationFrame(animate)
  }
  
  animate()
})

onUnmounted(() => {
  if (animationId) {
    cancelAnimationFrame(animationId)
  }
})
</script>
```
