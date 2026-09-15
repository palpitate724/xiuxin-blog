<template>
  <div class="logo-container">
    <div class="logo-content">
      <div class="logo-icon">
        <svg viewBox="0 0 100 100" class="logo-svg">
          <defs>
            <linearGradient id="logoGradient" x1="0%" y1="0%" x2="100%" y2="100%">
              <stop offset="0%" style="stop-color:#3b82f6;stop-opacity:1" />
              <stop offset="50%" style="stop-color:#06b6d4;stop-opacity:1" />
              <stop offset="100%" style="stop-color:#8b5cf6;stop-opacity:1" />
            </linearGradient>
            <filter id="glow">
              <feGaussianBlur stdDeviation="2" result="coloredBlur"/>
              <feMerge>
                <feMergeNode in="coloredBlur"/>
                <feMergeNode in="SourceGraphic"/>
              </feMerge>
            </filter>
          </defs>
          <!-- 背景圆形 -->
          <circle cx="50" cy="50" r="45" fill="rgba(59, 130, 246, 0.1)" stroke="url(#logoGradient)" stroke-width="2" />
          
          <!-- 代码符号 </> -->
          <text x="50" y="58" text-anchor="middle" font-family="monospace" font-size="28" font-weight="bold" fill="url(#logoGradient)" filter="url(#glow)">
            &lt;/&gt;
          </text>
          
          <!-- 装饰点 -->
          <circle cx="25" cy="25" r="3" fill="#3b82f6" opacity="0.6">
            <animate attributeName="opacity" values="0.6;1;0.6" dur="2s" repeatCount="indefinite"/>
          </circle>
          <circle cx="75" cy="25" r="2" fill="#06b6d4" opacity="0.4">
            <animate attributeName="opacity" values="0.4;0.8;0.4" dur="3s" repeatCount="indefinite"/>
          </circle>
          <circle cx="75" cy="75" r="3" fill="#8b5cf6" opacity="0.5">
            <animate attributeName="opacity" values="0.5;1;0.5" dur="2.5s" repeatCount="indefinite"/>
          </circle>
        </svg>
      </div>
      <h1 class="logo-title">蔚蓝极客</h1>
      <p class="logo-subtitle">个人博客系统</p>
      <div class="tech-stack">
        <span class="tech-tag">Vue 3</span>
        <span class="tech-tag">TypeScript</span>
        <span class="tech-tag">Vite</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
</script>

<style scoped>
.logo-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px;
}

.logo-content {
  text-align: center;
  animation: fadeInUp 1s ease-out;
}

.logo-icon {
  margin-bottom: 32px;
}

.logo-svg {
  width: 140px;
  height: 140px;
  filter: drop-shadow(0 0 20px rgba(59, 130, 246, 0.3));
  animation: float 4s ease-in-out infinite;
}

.logo-title {
  font-size: 2.8rem;
  font-weight: 700;
  color: white;
  margin: 0 0 8px 0;
  letter-spacing: 4px;
  text-shadow: 0 0 30px rgba(59, 130, 246, 0.5);
  font-family: 'Segoe UI', system-ui, sans-serif;
}

.logo-subtitle {
  font-size: 1.2rem;
  color: rgba(147, 197, 253, 0.9);
  margin: 0 0 24px 0;
  font-weight: 300;
  letter-spacing: 2px;
  text-transform: uppercase;
}

.tech-stack {
  display: flex;
  gap: 12px;
  justify-content: center;
  flex-wrap: wrap;
}

.tech-tag {
  padding: 6px 16px;
  background: rgba(59, 130, 246, 0.15);
  border: 1px solid rgba(59, 130, 246, 0.3);
  border-radius: 20px;
  color: #93c5fd;
  font-size: 0.85rem;
  font-weight: 500;
  letter-spacing: 1px;
  backdrop-filter: blur(10px);
  transition: all 0.3s ease;
}

.tech-tag:hover {
  background: rgba(59, 130, 246, 0.25);
  border-color: rgba(59, 130, 246, 0.5);
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.2);
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes float {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-15px);
  }
}

@media (max-width: 768px) {
  .logo-svg {
    width: 100px;
    height: 100px;
  }
  
  .logo-title {
    font-size: 2rem;
    letter-spacing: 2px;
  }
  
  .logo-subtitle {
    font-size: 1rem;
  }
  
  .tech-stack {
    gap: 8px;
  }
  
  .tech-tag {
    padding: 4px 12px;
    font-size: 0.75rem;
  }
}
</style>