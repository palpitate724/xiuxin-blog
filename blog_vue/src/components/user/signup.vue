<template>
  <div class="signup-container">
    <div class="signup-card">
      <div class="signup-header">
        <h2 class="signup-title">创建账户</h2>
        <p class="signup-subtitle">加入蔚蓝极客，开启技术之旅</p>
      </div>
      
      <form class="signup-form" @submit.prevent="handleSubmit">
        <!-- 头像上传 -->
        <div class="avatar-section">
          <div class="avatar-upload-container">
            <div class="avatar-circle" :class="{ 'has-avatar': avatarPreview }">
              <img v-if="avatarPreview" :src="avatarPreview" alt="头像预览" class="avatar-image" />
              <svg v-else viewBox="0 0 24 24" class="avatar-placeholder">
                <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2" stroke="currentColor" stroke-width="1.5" fill="none" stroke-linecap="round" stroke-linejoin="round"/>
                <circle cx="12" cy="7" r="4" stroke="currentColor" stroke-width="1.5" fill="none" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </div>
            <div class="avatar-info">
              <div class="avatar-header">
                <span class="avatar-hint">支持 JPG / PNG，建议尺寸 200×200，大小不超过2MB(可选)</span>
              </div>
              <label for="avatar" class="upload-button">
                <svg viewBox="0 0 24 24" class="upload-icon">
                  <path d="M12 4L12 16M12 4L8 8M12 4L16 8M4 20L20 20" stroke="currentColor" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
                <span>选择文件</span>
              </label>
              <input 
                type="file" 
                id="avatar" 
                name="avatar" 
                class="avatar-input" 
                accept="image/*"
                @change="handleAvatarChange"
              />
            </div>
          </div>
        </div>
        <div v-if="selectedFile" class="file-status">
          <span class="file-name">{{ selectedFile.name }}</span>
          <span class="file-size">{{ formatFileSize(selectedFile.size) }}</span>
        </div>
        <div v-if="uploadError" class="error-message">
          {{ uploadError }}
        </div>
        
        <!-- 表单字段 -->
        <div class="form-group">
          <label for="username" class="form-label">
            <span class="label-icon">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path>
                <circle cx="12" cy="7" r="4"></circle>
              </svg>
            </span>
            用户名
          </label>
          <input 
            type="text" 
            id="username" 
            name="username" 
            class="form-input" 
            placeholder="请输入用户名"
            required
          />
        </div>
        
        <div class="form-group">
          <label for="email" class="form-label">
            <span class="label-icon">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"></path>
                <polyline points="22,6 12,13 2,6"></polyline>
              </svg>
            </span>
            邮箱
          </label>
          <input 
            type="email" 
            id="email" 
            name="email" 
            class="form-input" 
            placeholder="请输入邮箱地址"
            required
          />
        </div>
        
        <div class="form-group">
          <label for="password" class="form-label">
            <span class="label-icon">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <rect x="3" y="11" width="18" height="11" rx="2" ry="2"></rect>
                <path d="M7 11V7a5 5 0 0 1 10 0v4"></path>
              </svg>
            </span>
            密码
          </label>
          <input 
            type="password" 
            id="password" 
            name="password" 
            class="form-input" 
            placeholder="请输入密码（至少6位）"
            required
            minlength="6"
          />
        </div>
        
        <div class="form-group">
          <label for="password2" class="form-label">
            <span class="label-icon">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <rect x="3" y="11" width="18" height="11" rx="2" ry="2"></rect>
                <path d="M7 11V7a5 5 0 0 1 10 0v4"></path>
              </svg>
            </span>
            确认密码
          </label>
          <input 
            type="password" 
            id="password2" 
            name="password2" 
            class="form-input" 
            placeholder="请再次输入密码"
            required
          />
        </div>
        
        <!-- 服务条款 -->
        <div class="terms-section">
          <label class="checkbox-label">
            <input type="checkbox" name="terms" class="checkbox" required />
            <span>我已阅读并同意</span>
            <a href="#" class="terms-link">服务条款</a>
            <span>和</span>
            <a href="#" class="terms-link">隐私政策</a>
          </label>
        </div>
        
        <button type="submit" class="submit-button">
          <span class="button-text">创建账户</span>
          <span class="button-icon">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M5 12h14M12 5l7 7-7 7"></path>
            </svg>
          </span>
        </button>
      </form>
      
      <div class="signup-footer">
        <p class="footer-text">
          已有账户？
          <a href="#" class="login-link" @click.prevent="$emit('switch-to-login')">立即登录</a>
        </p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

// 定义事件
const emit = defineEmits<{
  'switch-to-login': []
  'hide-cursor': []
  'show-cursor': []
}>()

const avatarPreview = ref<string>('')
const selectedFile = ref<File | null>(null)
const uploadError = ref<string>('')

const handleAvatarChange = (e: Event) => {
  const target = e.target as HTMLInputElement
  const file = target.files?.[0]
  
  // 重置错误状态
  uploadError.value = ''
  
  if (file) {
    // 验证文件类型
    const validTypes = ['image/jpeg', 'image/png', 'image/jpg']
    if (!validTypes.includes(file.type)) {
      uploadError.value = '仅支持 JPG / PNG 格式'
      return
    }
    
    // 验证文件大小 (2MB = 2 * 1024 * 1024 bytes)
    const maxSize = 2 * 1024 * 1024
    if (file.size > maxSize) {
      uploadError.value = '文件大小不能超过 2MB'
      return
    }
    
    selectedFile.value = file
    
    const reader = new FileReader()
    reader.onload = (e) => {
      avatarPreview.value = e.target?.result as string
    }
    reader.readAsDataURL(file)
  } else {
    selectedFile.value = null
    avatarPreview.value = ''
  }
}

const formatFileSize = (bytes: number): string => {
  if (bytes === 0) return '0 B'
  const k = 1024
  const sizes = ['B', 'KB', 'MB', 'GB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return Math.round((bytes / Math.pow(k, i)) * 100) / 100 + ' ' + sizes[i]
}

const handleSubmit = (e: Event) => {
  const form = e.target as HTMLFormElement
  const formData = new FormData(form)
  const signupData: Record<string, FormDataEntryValue> = {}
  formData.forEach((value, key) => {
    signupData[key] = value
  })
  console.log('Signup data:', signupData)
  // TODO: 实现注册逻辑
}
</script>

<style scoped>
.signup-container {
  width: 100%;
  max-width: 480px;
  padding: 20px;
}

.signup-card {
  /* 液态玻璃基础效果 */
  background: linear-gradient(
    160deg,
    rgba(255, 255, 255, 0.08) 0%,
    rgba(255, 255, 255, 0.03) 40%,
    rgba(255, 255, 255, 0.01) 100%
  );
  backdrop-filter: blur(32px) saturate(1.9) brightness(1.06);
  -webkit-backdrop-filter: blur(32px) saturate(1.9) brightness(1.06);
  border: 1px solid transparent;
  background-clip: padding-box;
  border-radius: 24px;
  padding: 36px 40px;
  box-shadow:
    0 8px 32px rgba(0, 0, 0, 0.22),
    0 2px 8px rgba(0, 0, 0, 0.12),
    inset 0 1px 0 rgba(255, 255, 255, 0.12),
    inset 0 -1px 0 rgba(0, 0, 0, 0.08);
  animation: slideIn 0.8s cubic-bezier(0.34, 1.56, 0.64, 1) forwards;
  position: relative;
  overflow: hidden;
}

/* 玻璃表面高光 */
.signup-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 55%;
  background: linear-gradient(
    180deg,
    rgba(255, 255, 255, 0.1) 0%,
    rgba(255, 255, 255, 0.025) 55%,
    transparent 100%
  );
  border-radius: 24px 24px 0 0;
  pointer-events: none;
  z-index: 1;
  transition: background 0.4s ease;
}

/* 顶部边缘光晕 */
.signup-card::after {
  content: '';
  position: absolute;
  top: 0;
  left: 20px;
  right: 20px;
  height: 1px;
  background: linear-gradient(
    90deg,
    transparent 0%,
    rgba(255, 255, 255, 0.3) 20%,
    rgba(255, 255, 255, 0.45) 50%,
    rgba(255, 255, 255, 0.3) 80%,
    transparent 100%
  );
  pointer-events: none;
  z-index: 2;
}

/* 卡片装饰光晕 */
.signup-card .deco-glow {
  position: absolute;
  top: -50%;
  right: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(circle at 70% 30%, rgba(6, 182, 212, 0.06) 0%, transparent 50%),
              radial-gradient(circle at 20% 80%, rgba(139, 92, 246, 0.04) 0%, transparent 50%);
  pointer-events: none;
  z-index: 0;
}

.signup-header {
  text-align: center;
  margin-bottom: 20px;
  position: relative;
  z-index: 1;
}

.signup-title {
  font-size: 1.85rem;
  font-weight: 700;
  color: var(--color-white);
  margin: 0 0 8px 0;
  letter-spacing: 1px;
  text-shadow:
    0 0 30px var(--color-accent-glow),
    0 2px 10px var(--color-shadow-text);
  position: relative;
  /* 渐变文字效果 */
  background: linear-gradient(135deg, #ffffff 0%, #bae6fd 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

/* 浅色主题注册标题 */

.signup-title::after {
  content: '';
  display: block;
  width: 50px;
  height: 3px;
  background: linear-gradient(90deg, var(--color-accent-secondary), var(--color-accent-tertiary));
  border-radius: 2px;
  margin: 12px auto 0;
  box-shadow: 0 0 10px var(--color-accent-secondary-soft);
}

.signup-subtitle {
  font-size: 0.9rem;
  color: var(--color-text-muted);
  margin: 0;
  letter-spacing: 0.5px;
}

.signup-form {
  display: flex;
  flex-direction: column;
  gap: 14px;
  position: relative;
  z-index: 1;
}

.avatar-section {
  margin-bottom: 4px;
  display: flex;
  flex-direction: column;
  gap: 6px;
  width: 100%;
}

.file-status,
.error-message {
  width: 100%;
}

.avatar-upload-container {
  display: flex;
  align-items: center;
  gap: 20px;
  width: 100%;
}

.avatar-circle {
  width: 58px;
  height: 58px;
  border-radius: 50%;
  overflow: hidden;
  border: 2px solid var(--color-accent-cyan-soft);
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--color-bg-avatar);
  transition: all 0.35s cubic-bezier(0.34, 1.56, 0.64, 1);
  flex-shrink: 0;
  box-shadow:
    0 0 12px var(--color-accent-cyan-glow),
    inset 0 0 10px rgba(6, 182, 212, 0.1);
  position: relative;
}

.avatar-circle::after {
  content: '';
  position: absolute;
  inset: -3px;
  border-radius: 50%;
  background: conic-gradient(from 0deg, var(--color-accent-cyan), var(--color-accent), var(--color-accent-tertiary), var(--color-accent-cyan));
  opacity: 0;
  z-index: -1;
  transition: opacity 0.3s ease;
  animation: avatar-ring-rotate 3s linear infinite;
}

@keyframes avatar-ring-rotate {
  to { transform: rotate(360deg); }
}

.avatar-circle:hover::after {
  opacity: 0.5;
}

.avatar-circle:hover {
  border-color: var(--color-accent-cyan);
  box-shadow:
    0 0 20px var(--color-accent-cyan-glow),
    inset 0 0 10px rgba(6, 182, 212, 0.15);
  transform: scale(1.05);
}

.avatar-circle.has-avatar {
  border-color: var(--color-accent-cyan-soft);
  box-shadow:
    0 0 14px var(--color-accent-cyan-glow),
    inset 0 0 10px rgba(6, 182, 212, 0.1);
}

.avatar-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.avatar-placeholder {
  width: 26px;
  height: 26px;
  color: var(--color-white-soft);
  transition: color 0.3s ease;
}

.avatar-circle:hover .avatar-placeholder {
  color: var(--color-accent-cyan);
}

.avatar-info {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 8px 14px;
  background: rgba(15, 26, 44, 0.7);
  border: 1px solid var(--color-accent-cyan-softer);
  border-radius: 12px;
  backdrop-filter: blur(10px);
  height: 58px;
  gap: 12px;
  transition: all 0.3s ease;
}

.avatar-info:hover {
  border-color: var(--color-accent-cyan-soft);
  background: rgba(15, 26, 44, 0.85);
}

.avatar-header {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.avatar-hint {
  color: var(--color-text-muted);
  font-size: 0.75rem;
  font-weight: 400;
}

.upload-button {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  background: linear-gradient(135deg, rgba(6, 182, 212, 0.15) 0%, rgba(59, 130, 246, 0.15) 100%);
  border: 1px solid var(--color-accent-cyan-soft);
  border-radius: 8px;
  color: var(--color-accent-cyan);
  font-size: 0.85rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
  white-space: nowrap;
  align-self: flex-start;
  position: relative;
  overflow: hidden;
}

.upload-button::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.1), transparent);
  transition: left 0.5s ease;
}

.upload-button:hover::before {
  left: 100%;
}

.upload-button:hover {
  background: linear-gradient(135deg, rgba(6, 182, 212, 0.25) 0%, rgba(59, 130, 246, 0.25) 100%);
  border-color: var(--color-accent-cyan);
  box-shadow: 0 0 16px var(--color-accent-cyan-glow);
  transform: translateY(-1px);
}

.upload-icon {
  width: 16px;
  height: 16px;
  stroke-width: 2.5;
}

.file-status {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 6px 10px;
  background: rgba(6, 182, 212, 0.1);
  border-radius: var(--radius-sm);
  border: 1px solid var(--color-accent-cyan-softer);
}

.file-name {
  color: var(--color-white);
  font-size: 0.8rem;
  font-weight: 500;
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.file-size {
  color: var(--color-text-muted);
  font-size: 0.75rem;
  font-weight: 400;
}

.error-message {
  color: var(--color-error-soft);
  font-size: 0.82rem;
  font-weight: 500;
  padding: 6px 10px;
  background: var(--color-error-bg);
  border-radius: var(--radius-sm);
  border: 1px solid var(--color-error-border);
}

.avatar-input {
  display: none;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.form-label {
  font-size: 0.88rem;
  font-weight: 500;
  color: var(--color-text-secondary);
  display: flex;
  align-items: center;
  gap: 8px;
  letter-spacing: 0.3px;
}

.form-input {
  padding: 13px 16px;
  background: rgba(30, 41, 59, 0.5);
  border: 1px solid var(--color-border-strong);
  border-radius: 10px;
  font-size: 0.95rem;
  color: var(--color-white);
  transition: all 0.35s cubic-bezier(0.4, 0, 0.2, 1);
  outline: none;
  backdrop-filter: blur(10px);
  caret-color: var(--color-accent-cyan);
  -webkit-tap-highlight-color: transparent;
  position: relative;
}

.form-input:hover {
  cursor: text;
  border-color: var(--color-border-stronger);
  background: rgba(30, 41, 59, 0.6);
}

.form-input:focus {
  border-color: var(--color-accent-cyan);
  background: rgba(30, 41, 59, 0.7);
  box-shadow:
    0 0 0 3px var(--color-accent-cyan-glow-soft),
    0 0 20px var(--color-accent-cyan-glow);
}

.form-input::placeholder {
  color: var(--color-text-placeholder);
}

/* 修复浏览器自动填充样式 */
.form-input:-webkit-autofill,
.form-input:-webkit-autofill:hover,
.form-input:-webkit-autofill:focus {
  -webkit-text-fill-color: var(--color-white);
  -webkit-box-shadow: 0 0 0 30px rgba(30, 41, 59, 0.9) inset;
  transition: background-color 5000s ease-in-out 0s;
  caret-color: var(--color-accent-cyan);
}

.form-input:-webkit-autofill:focus {
  -webkit-box-shadow: 0 0 0 30px rgba(30, 41, 59, 0.9) inset, 0 0 0 3px var(--color-accent-cyan-glow-soft), 0 0 20px var(--color-accent-cyan-glow);
}

.terms-section {
  margin-top: 2px;
}

.checkbox-label {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 6px;
  font-size: 0.85rem;
  color: var(--color-text-muted-softer);
  cursor: pointer;
}

.checkbox {
  cursor: pointer;
  accent-color: var(--color-accent-secondary);
  width: 15px;
  height: 15px;
  background: rgba(30, 41, 59, 0.6);
  border: 1px solid var(--color-border-stronger);
  border-radius: 4px;
  backdrop-filter: blur(10px);
  transition: all 0.2s ease;
}

.checkbox:hover {
  border-color: var(--color-accent-secondary);
  box-shadow: 0 0 6px var(--color-accent-secondary-soft);
}

.terms-link {
  color: var(--color-accent-secondary);
  text-decoration: none;
  font-weight: 500;
  transition: all 0.3s ease;
  position: relative;
}

.terms-link::after {
  content: '';
  position: absolute;
  bottom: -2px;
  left: 0;
  width: 0;
  height: 1px;
  background: var(--color-accent-secondary-light);
  transition: width 0.3s ease;
}

.terms-link:hover {
  color: var(--color-accent-secondary-light);
  text-shadow: 0 0 10px var(--color-accent-secondary-soft);
}

.terms-link:hover::after {
  width: 100%;
}

.submit-button {
  padding: 13px 24px;
  background: linear-gradient(135deg, var(--color-accent-secondary) 0%, var(--color-accent) 50%, var(--color-accent-tertiary) 100%);
  color: var(--color-white);
  border: none;
  border-radius: 10px;
  font-size: 0.95rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.4s cubic-bezier(0.34, 1.56, 0.64, 1);
  box-shadow:
    0 4px 20px var(--color-accent-secondary-soft),
    0 0 40px var(--color-accent-cyan-glow-xlight);
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  position: relative;
  overflow: hidden;
  margin-top: 4px;
  letter-spacing: 0.5px;
}

.submit-button::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.15), transparent);
  transition: left 0.6s ease;
}

.submit-button:hover::before {
  left: 100%;
}

.submit-button:hover {
  transform: translateY(-3px);
  box-shadow:
    0 8px 30px var(--color-accent-secondary-soft),
    0 0 50px var(--color-accent-cyan-glow),
    0 0 80px var(--color-accent-cyan-glow-xlight);
}

.submit-button:active {
  transform: translateY(0) scale(0.98);
  transition: all 0.1s ease;
}

.button-icon {
  width: 18px;
  height: 18px;
  transition: transform 0.3s ease;
}

.submit-button:hover .button-icon {
  transform: translateX(5px);
}

.signup-footer {
  text-align: center;
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px solid var(--color-border-strong);
  position: relative;
  z-index: 1;
}

.footer-text {
  font-size: 0.88rem;
  color: var(--color-text-muted);
  margin: 0;
}

.login-link {
  color: var(--color-accent-secondary);
  text-decoration: none;
  font-weight: 600;
  margin-left: 4px;
  transition: all 0.3s ease;
  position: relative;
}

.login-link::after {
  content: '';
  position: absolute;
  bottom: -2px;
  left: 0;
  width: 0;
  height: 1px;
  background: var(--color-accent-secondary-light);
  transition: width 0.3s ease;
}

.login-link:hover {
  color: var(--color-accent-secondary-light);
  text-shadow: 0 0 10px var(--color-accent-secondary-soft);
}

.login-link:hover::after {
  width: 100%;
}

/* 悬浮状态增强 */
.signup-card:hover {
  box-shadow:
    0 12px 48px rgba(0, 0, 0, 0.28),
    0 4px 12px rgba(0, 0, 0, 0.16),
    inset 0 1px 0 rgba(255, 255, 255, 0.16),
    inset 0 -1px 0 rgba(0, 0, 0, 0.08);
  border-color: rgba(255, 255, 255, 0.12);
}

.signup-card:hover::before {
  background: linear-gradient(
    180deg,
    rgba(255, 255, 255, 0.14) 0%,
    rgba(255, 255, 255, 0.04) 55%,
    transparent 100%
  );
}

@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateX(40px) scale(0.95);
  }
  to {
    opacity: 1;
    transform: translateX(0) scale(1);
  }
}

@media (max-width: 480px) {
  .signup-card {
    padding: 28px 20px;
    border-radius: 16px;
  }

  .signup-title {
    font-size: 1.5rem;
  }

  .signup-header {
    margin-bottom: 16px;
  }

  .avatar-upload-container {
    gap: 16px;
  }

  .avatar-circle {
    width: 52px;
    height: 52px;
  }

  .avatar-placeholder {
    width: 24px;
    height: 24px;
  }

  .avatar-info {
    height: 52px;
    padding: 6px 12px;
  }

  .avatar-hint {
    display: none;
  }

  .upload-button {
    padding: 6px 12px;
    font-size: 0.8rem;
  }

  .file-status {
    display: none;
  }

  .error-message {
    display: none;
  }

  .form-input {
    padding: 12px 14px;
    font-size: 0.9rem;
  }

  .submit-button {
    padding: 12px 20px;
    font-size: 0.9rem;
  }
}

@media (max-width: 768px) {
  .signup-card {
    padding: 32px 24px;
  }

  .signup-title {
    font-size: 1.6rem;
  }

  .avatar-upload-container {
    gap: 18px;
  }

  .avatar-circle {
    width: 56px;
    height: 56px;
  }

  .avatar-info {
    height: 56px;
  }

  .avatar-hint {
    display: none;
  }

  .file-status {
    display: none;
  }

  .error-message {
    display: none;
  }

  .form-group {
    gap: 14px;
  }

  .form-input {
    padding: 12px 16px;
  }

  .submit-button {
    padding: 12px 24px;
  }
}

/* ===== 浅色主题样式 ===== */
/* 注册卡片浅色适配 — 冷调玻璃质感 */


/* 表单输入框浅色适配 */



/* 标签浅色适配 */


/* 头像上传区域浅色适配 */



/* 复选框浅色适配 */


/* 服务条款链接 */


/* 登录链接 */


/* 页脚文字 */


/* 提交按钮浅色适配 */


/* 文件状态和错误提示 */

</style>