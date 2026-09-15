# Remotion Skill

Remotion 视频创作技能，提供程序化视频生成能力。

## Remotion 基础

### 项目初始化
```bash
# 创建 Remotion 项目
npm init remotion

# 或使用 TypeScript
npm init remotion --template hello-world
```

### 基础配置
```typescript
// remotion.config.ts
import { Config } from '@remotion/cli/config'

export default {
  webpack: (config) => {
    return config
  },
} as Config
```

## 组件创建

### 基础视频组件
```tsx
import { AbsoluteFill, interpolate, useCurrentFrame, useVideoConfig } from 'remotion'

export const MyVideo: React.FC = () => {
  const frame = useCurrentFrame()
  const { fps, durationInFrames } = useVideoConfig()
  
  const opacity = interpolate(
    frame,
    [0, 30],
    [0, 1],
    { extrapolateRight: 'clamp' }
  )
  
  return (
    <AbsoluteFill style={{ backgroundColor: 'white' }}>
      <div
        style={{
          opacity,
          display: 'flex',
          justifyContent: 'center',
          alignItems: 'center',
          fontSize: 80,
          fontWeight: 'bold',
        }}
      >
        Hello Remotion!
      </div>
    </AbsoluteFill>
  )
}
```

### 动画效果
```tsx
import { AbsoluteFill, interpolate, useCurrentFrame, spring } from 'remotion'

export const AnimatedText: React.FC<{ text: string }> = ({ text }) => {
  const frame = useCurrentFrame()
  
  const scale = spring({
    frame,
    fps: 30,
    config: {
      damping: 10,
      stiffness: 100,
      mass: 1,
    },
  })
  
  const rotation = interpolate(frame, [0, 60], [0, 360])
  
  return (
    <AbsoluteFill
      style={{
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: '#1a1a1a',
      }}
    >
      <div
        style={{
          transform: `scale(${scale}) rotate(${rotation}deg)`,
          fontSize: 120,
          fontWeight: 'bold',
          color: '#1890ff',
        }}
      >
        {text}
      </div>
    </AbsoluteFill>
  )
}
```

## 序列管理

### 视频序列
```tsx
import { Composition, Sequence } from 'remotion'
import { MyVideo } from './MyVideo'
import { AnimatedText } from './AnimatedText'

export const RemotionVideo: React.FC = () => {
  return (
    <>
      <Composition
        id="my-video"
        component={MyVideo}
        durationInFrames={180}
        fps={30}
        width={1920}
        height={1080}
      />
      
      <Composition
        id="animated-text"
        component={AnimatedText}
        durationInFrames={120}
        fps={30}
        width={1920}
        height={1080}
        props={{ text: 'Hello!' }}
      />
    </>
  )
}
```

### 场景切换
```tsx
import { AbsoluteFill, Sequence, useCurrentFrame } from 'remotion'

export const SceneTransition: React.FC = () => {
  const frame = useCurrentFrame()
  
  return (
    <AbsoluteFill>
      <Sequence from={0} durationInFrames={60}>
        <Scene1 />
      </Sequence>
      
      <Sequence from={60} durationInFrames={60}>
        <Scene2 />
      </Sequence>
      
      <Sequence from={120} durationInFrames={60}>
        <Scene3 />
      </Sequence>
    </AbsoluteFill>
  )
}
```

## 音频处理

### 音频播放
```tsx
import { AbsoluteFill, Audio, useCurrentFrame } from 'remotion'

export const VideoWithAudio: React.FC = () => {
  return (
    <AbsoluteFill>
      <Audio src={require('./music.mp3')} />
      <div style={{ backgroundColor: '#1a1a1a' }}>
        <h1>Video with Audio</h1>
      </div>
    </AbsoluteFill>
  )
}
```

### 音频可视化
```tsx
import { AbsoluteFill, useAudioData, useCurrentFrame, useVideoConfig } from 'remotion'
import { visualizeAudio } from '@remotion/audio-utils'

export const AudioVisualizer: React.FC = () => {
  const { audioData } = useAudioData(require('./music.mp3'))
  const frame = useCurrentFrame()
  const { fps } = useVideoConfig()
  
  if (!audioData) {
    return null
  }
  
  const visualization = visualizeAudio({
    fps,
    frame,
    audioData,
    numberOfSamples: 100,
  })
  
  return (
    <AbsoluteFill style={{ backgroundColor: '#1a1a1a' }}>
      {visualization.map((v, i) => (
        <div
          key={i}
          style={{
            position: 'absolute',
            left: i * 10,
            bottom: 0,
            height: v * 100,
            width: 8,
            backgroundColor: '#1890ff',
          }}
        />
      ))}
    </AbsoluteFill>
  )
}
```

## 文本动画

### 打字机效果
```tsx
import { AbsoluteFill, useCurrentFrame, interpolate } from 'remotion'

export const Typewriter: React.FC<{ text: string }> = ({ text }) => {
  const frame = useCurrentFrame()
  
  const charCount = Math.floor(frame / 2)
  const visibleText = text.slice(0, charCount)
  
  return (
    <AbsoluteFill
      style={{
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: '#1a1a1a',
      }}
    >
      <div
        style={{
          fontSize: 60,
          fontWeight: 'bold',
          color: '#1890ff',
          fontFamily: 'monospace',
        }}
      >
        {visibleText}
        {charCount < text.length && (
          <span style={{ animation: 'blink 1s infinite' }}>|</span>
        )}
      </div>
    </AbsoluteFill>
  )
}
```

### 文本路径动画
```tsx
import { AbsoluteFill, useCurrentFrame, interpolate } from 'remotion'

export const TextPath: React.FC<{ text: string }> = ({ text }) => {
  const frame = useCurrentFrame()
  
  const progress = interpolate(frame, [0, 180], [0, 1])
  
  return (
    <AbsoluteFill
      style={{
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: '#1a1a1a',
      }}
    >
      <svg width="800" height="200">
        <defs>
          <path
            id="textPath"
            d="M 100 100 Q 400 0 700 100"
            fill="none"
            stroke="#1890ff"
            strokeWidth="2"
          />
        </defs>
        <text
          fontSize="40"
          fontWeight="bold"
          fill="#1890ff"
        >
          <textPath href="#textPath" startOffset={`${progress * 100}%`}>
            {text}
          </textPath>
        </text>
      </svg>
    </AbsoluteFill>
  )
}
```

## 图像处理

### 图像序列
```tsx
import { AbsoluteFill, Sequence, useCurrentFrame } from 'remotion'

export const ImageSequence: React.FC = () => {
  const images = [
    require('./image1.jpg'),
    require('./image2.jpg'),
    require('./image3.jpg'),
  ]
  
  return (
    <AbsoluteFill>
      {images.map((image, index) => (
        <Sequence
          key={index}
          from={index * 60}
          durationInFrames={60}
        >
          <img
            src={image}
            style={{
              width: '100%',
              height: '100%',
              objectFit: 'cover',
            }}
          />
        </Sequence>
      ))}
    </AbsoluteFill>
  )
}
```

### 图像滤镜
```tsx
import { AbsoluteFill, useCurrentFrame, interpolate } from 'remotion'

export const ImageFilter: React.FC = () => {
  const frame = useCurrentFrame()
  
  const blur = interpolate(frame, [0, 60], [0, 10])
  const brightness = interpolate(frame, [0, 60], [100, 150])
  const contrast = interpolate(frame, [0, 60], [100, 150])
  
  return (
    <AbsoluteFill>
      <img
        src={require('./image.jpg')}
        style={{
          width: '100%',
          height: '100%',
          objectFit: 'cover',
          filter: `blur(${blur}px) brightness(${brightness}%) contrast(${contrast}%)`,
        }}
      />
    </AbsoluteFill>
  )
}
```

## 特效制作

### 粒子效果
```tsx
import { AbsoluteFill, useCurrentFrame, useVideoConfig } from 'remotion'

export const ParticleEffect: React.FC = () => {
  const frame = useCurrentFrame()
  const { width, height } = useVideoConfig()
  
  const particles = Array.from({ length: 100 }, (_, i) => {
    const angle = (i / 100) * Math.PI * 2
    const radius = 100 + Math.sin(frame * 0.05 + i) * 50
    const x = width / 2 + Math.cos(angle) * radius
    const y = height / 2 + Math.sin(angle) * radius
    
    return (
      <div
        key={i}
        style={{
          position: 'absolute',
          left: x,
          top: y,
          width: 10,
          height: 10,
          borderRadius: '50%',
          backgroundColor: `hsl(${(i / 100) * 360}, 70%, 50%)`,
        }}
      />
    )
  })
  
  return (
    <AbsoluteFill style={{ backgroundColor: '#1a1a1a' }}>
      {particles}
    </AbsoluteFill>
  )
}
```

### 故障效果
```tsx
import { AbsoluteFill, useCurrentFrame, interpolate } from 'remotion'

export const GlitchEffect: React.FC = () => {
  const frame = useCurrentFrame()
  
  const glitchOffset = interpolate(
    frame,
    [0, 10, 20, 30],
    [0, 5, 0, 5],
    { extrapolateRight: 'clamp' }
  )
  
  return (
    <AbsoluteFill
      style={{
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: '#1a1a1a',
      }}
    >
      <div
        style={{
          fontSize: 120,
          fontWeight: 'bold',
          color: '#1890ff',
          position: 'relative',
        }}
      >
        <div
          style={{
            position: 'absolute',
            left: glitchOffset,
            color: '#ff0000',
            mixBlendMode: 'difference',
          }}
        >
          GLITCH
        </div>
        <div
          style={{
            position: 'absolute',
            left: -glitchOffset,
            color: '#00ff00',
            mixBlendMode: 'difference',
          }}
        >
          GLITCH
        </div>
        GLITCH
      </div>
    </AbsoluteFill>
  )
}
```

## 数据可视化

### 图表动画
```tsx
import { AbsoluteFill, useCurrentFrame, interpolate, spring } from 'remotion'

export const ChartAnimation: React.FC = () => {
  const frame = useCurrentFrame()
  const data = [30, 50, 80, 45, 90, 60, 75]
  
  const bars = data.map((value, index) => {
    const height = spring({
      frame: frame - index * 10,
      fps: 30,
      config: { damping: 10, stiffness: 100 },
    })
    
    return (
      <div
        key={index}
        style={{
          position: 'absolute',
          left: 100 + index * 80,
          bottom: 100,
          width: 50,
          height: height * value,
          backgroundColor: '#1890ff',
          borderRadius: '4px',
        }}
      />
    )
  })
  
  return (
    <AbsoluteFill style={{ backgroundColor: '#1a1a1a' }}>
      {bars}
    </AbsoluteFill>
  )
}
```

### 数据流动画
```tsx
import { AbsoluteFill, useCurrentFrame, interpolate } from 'remotion'

export const DataFlow: React.FC = () => {
  const frame = useCurrentFrame()
  
  const particles = Array.from({ length: 50 }, (_, i) => {
    const progress = (frame * 0.02 + i * 0.1) % 1
    const x = interpolate(progress, [0, 1], [0, 1920])
    const y = 200 + Math.sin(i * 0.5) * 100
    
    return (
      <div
        key={i}
        style={{
          position: 'absolute',
          left: x,
          top: y,
          width: 8,
          height: 8,
          borderRadius: '50%',
          backgroundColor: '#1890ff',
          opacity: interpolate(progress, [0, 0.8, 1], [0, 1, 0]),
        }}
      />
    )
  })
  
  return (
    <AbsoluteFill style={{ backgroundColor: '#1a1a1a' }}>
      {particles}
    </AbsoluteFill>
  )
}
```

## Vue3 集成

### Remotion + Vue
```tsx
import { AbsoluteFill, useCurrentFrame } from 'remotion'
import { createApp } from 'vue'
import MyVueComponent from './MyVueComponent.vue'

export const VueInRemotion: React.FC = () => {
  const frame = useCurrentFrame()
  const containerRef = React.useRef<HTMLDivElement>(null)
  
  React.useEffect(() => {
    if (containerRef.current) {
      const app = createApp(MyVueComponent, { frame })
      app.mount(containerRef.current)
      
      return () => {
        app.unmount()
      }
    }
  }, [frame])
  
  return (
    <AbsoluteFill>
      <div ref={containerRef} />
    </AbsoluteFill>
  )
}
```

## 渲染配置

### 输出设置
```bash
# 渲染视频
npx remotion render out/video.mp4

# 渲染 GIF
npx remotion render out/video.gif

# 渲染为序列帧
npx remotion render out/frame-%05d.png

# 自定义渲染参数
npx remotion render out/video.mp4 --codec=libx264 --crf=23 --preset=slow
```

### 预览控制
```bash
# 启动开发服务器
npx remotion preview

# 指定端口
npx remotion preview --port=3001

# 指定合成
npx remotion preview --composition=my-video
```

## 性能优化

### 帧缓存
```tsx
import { useFrame } from 'remotion'

export const CachedComponent: React.FC = () => {
  const frame = useFrame()
  
  // 使用帧缓存减少重复计算
  const memoizedValue = React.useMemo(() => {
    return expensiveCalculation(frame)
  }, [frame])
  
  return <div>{memoizedValue}</div>
}
```

### 懒加载
```tsx
import { lazy, Suspense } from 'react'

const HeavyComponent = lazy(() => import('./HeavyComponent'))

export const LazyLoaded: React.FC = () => {
  return (
    <Suspense fallback={<div>Loading...</div>}>
      <HeavyComponent />
    </Suspense>
  )
}
```
