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
  background: transparent;
  backdrop-filter: none;
  border: none;
  border-radius: 16px;
  padding: 32px 40px;
  box-shadow: none;
  animation: slideIn 0.8s ease-out;
}

.signup-header {
  text-align: center;
  margin-bottom: 16px;
}

.signup-title {
  font-size: 1.8rem;
  font-weight: 700;
  color: white;
  margin: 0 0 8px 0;
  letter-spacing: 1px;
  text-shadow: 0 0 20px rgba(59, 130, 246, 0.5);
}

.signup-subtitle {
  font-size: 0.95rem;
  color: rgba(147, 197, 253, 0.8);
  margin: 0;
}

.signup-form {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.avatar-section {
  margin-bottom: 8px;
  display: flex;
  flex-direction: column;
  gap: 4px;
  width: 100%;
}

.file-status,
.error-message {
  width: 100%;
}

.avatar-upload-container {
  display: flex;
  align-items: center;
  gap: 24px;
  width: 100%;
}

.avatar-circle {
  width: 58px;
  height: 58px;
  border-radius: 50%;
  overflow: hidden;
  border: 1px solid rgba(56, 189, 248, 0.3);
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(15, 26, 44, 0.6);
  transition: all 0.3s ease;
  flex-shrink: 0;
  box-shadow: 0 0 8px rgba(56, 189, 248, 0.15);
}

.avatar-circle:hover {
  border-color: rgba(56, 189, 248, 0.5);
  box-shadow: 0 0 12px rgba(56, 189, 248, 0.25);
}

.avatar-circle.has-avatar {
  border-color: rgba(56, 189, 248, 0.4);
  box-shadow: 0 0 10px rgba(56, 189, 248, 0.2);
}

.avatar-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.avatar-placeholder {
  width: 28px;
  height: 28px;
  color: rgba(255, 255, 255, 0.7);
}

.avatar-info {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 8px 16px;
  background: rgba(15, 26, 44, 0.7);
  border: 1px solid rgba(56, 189, 248, 0.2);
  border-radius: 12px;
  backdrop-filter: blur(10px);
  height: 58px;
  gap: 12px;
}

.avatar-header {
  display: flex;
  flex-direction: column;
  gap: 1px;
}

.avatar-title {
  color: #CBD5E1;
  font-size: 0.85rem;
  font-weight: 500;
}

.avatar-hint {
  color: #94A3B8;
  font-size: 0.75rem;
  font-weight: 400;
}

.file-status {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 4px 12px;
  background: rgba(56, 189, 248, 0.1);
  border-radius: 6px;
  border: 1px solid rgba(56, 189, 248, 0.2);
  margin-top: 4px;
}

.file-name {
  color: #FFFFFF;
  font-size: 0.8rem;
  font-weight: 500;
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.file-size {
  color: #94A3B8;
  font-size: 0.75rem;
  font-weight: 400;
}

.error-message {
  color: #EF4444;
  font-size: 0.8rem;
  font-weight: 500;
  padding: 6px 12px;
  background: rgba(239, 68, 68, 0.1);
  border-radius: 6px;
  border: 1px solid rgba(239, 68, 68, 0.3);
  margin-top: 4px;
}

.avatar-header {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.avatar-title {
  color: #CBD5E1;
  font-size: 0.85rem;
  font-weight: 500;
}

.avatar-hint {
  color: #94A3B8;
  font-size: 0.75rem;
  font-weight: 400;
}

.upload-button {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  background: rgba(56, 189, 248, 0.15);
  border: 1px solid rgba(56, 189, 248, 0.3);
  border-radius: 8px;
  color: #38BDF8;
  font-size: 0.85rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  white-space: nowrap;
  align-self: flex-start;
}

.upload-button:hover {
  background: rgba(56, 189, 248, 0.25);
  border-color: rgba(56, 189, 248, 0.5);
  box-shadow: 0 0 12px rgba(56, 189, 248, 0.3);
  transform: translateY(-1px);
}

.upload-icon {
  width: 18px;
  height: 18px;
  stroke-width: 2.5;
}

.file-status {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 6px 10px;
  background: rgba(56, 189, 248, 0.1);
  border-radius: 6px;
  border: 1px solid rgba(56, 189, 248, 0.2);
}

.file-name {
  color: #FFFFFF;
  font-size: 0.8rem;
  font-weight: 500;
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.file-size {
  color: #94A3B8;
  font-size: 0.75rem;
  font-weight: 400;
}

.error-message {
  color: #EF4444;
  font-size: 0.8rem;
  font-weight: 500;
  padding: 6px 10px;
  background: rgba(239, 68, 68, 0.1);
  border-radius: 6px;
  border: 1px solid rgba(239, 68, 68, 0.3);
}

.avatar-input {
  display: none;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.form-label {
  font-size: 0.9rem;
  font-weight: 500;
  color: rgba(147, 197, 253, 0.9);
  display: flex;
  align-items: center;
  gap: 8px;
}

.label-icon {
  width: 18px;
  height: 18px;
  color: #3b82f6;
}

.form-input {
  padding: 10px 16px;
  background: rgba(30, 41, 59, 0.4);
  border: 1px solid rgba(59, 130, 246, 0.3);
  border-radius: 8px;
  font-size: 1rem;
  color: white;
  transition: all 0.3s ease;
  outline: none;
  backdrop-filter: blur(10px);
  caret-color: #38BDF8;
  -webkit-tap-highlight-color: transparent;
}

.form-input:hover {
  cursor: text;
}

.form-input:focus {
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.2), 0 0 20px rgba(59, 130, 246, 0.1);
}

.form-input::placeholder {
  color: rgba(148, 163, 184, 0.5);
}

/* 修复浏览器自动填充样式 */
.form-input:-webkit-autofill,
.form-input:-webkit-autofill:hover,
.form-input:-webkit-autofill:focus {
  -webkit-text-fill-color: white;
  -webkit-box-shadow: 0 0 0 30px rgba(30, 41, 59, 0.4) inset;
  transition: background-color 5000s ease-in-out 0s;
  caret-color: #38BDF8;
}

.form-input:-webkit-autofill:focus {
  -webkit-box-shadow: 0 0 0 30px rgba(30, 41, 59, 0.4) inset, 0 0 0 3px rgba(59, 130, 246, 0.2), 0 0 20px rgba(59, 130, 246, 0.1);
}

.terms-section {
  margin-top: 2px;
}

.checkbox-label {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 4px;
  font-size: 0.85rem;
  color: rgba(148, 163, 184, 0.8);
  cursor: pointer;
}

.checkbox {
  cursor: pointer;
  accent-color: #3b82f6;
  width: 16px;
  height: 16px;
  background: rgba(30, 41, 59, 0.4);
  border: 1px solid rgba(59, 130, 246, 0.3);
  border-radius: 4px;
  backdrop-filter: blur(10px);
}

.terms-link {
  color: #3b82f6;
  text-decoration: none;
  font-weight: 500;
  transition: color 0.3s ease;
}

.terms-link:hover {
  color: #60a5fa;
  text-shadow: 0 0 10px rgba(59, 130, 246, 0.5);
}

.submit-button {
  padding: 10px 24px;
  background: linear-gradient(135deg, #3b82f6 0%, #06b6d4 50%, #8b5cf6 100%);
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(59, 130, 246, 0.3);
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  position: relative;
  overflow: hidden;
  margin-top: 2px;
}

.submit-button::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
  transition: left 0.5s ease;
}

.submit-button:hover::before {
  left: 100%;
}

.submit-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 25px rgba(59, 130, 246, 0.4);
}

.submit-button:active {
  transform: translateY(0);
}

.button-icon {
  width: 20px;
  height: 20px;
  transition: transform 0.3s ease;
}

.submit-button:hover .button-icon {
  transform: translateX(4px);
}

.signup-footer {
  text-align: center;
  margin-top: 12px;
  padding-top: 12px;
  border-top: 1px solid rgba(59, 130, 246, 0.2);
}

.footer-text {
  font-size: 0.9rem;
  color: rgba(148, 163, 184, 0.8);
  margin: 0;
}

.login-link {
  color: #3b82f6;
  text-decoration: none;
  font-weight: 600;
  margin-left: 4px;
  transition: all 0.3s ease;
}

.login-link:hover {
  color: #60a5fa;
  text-shadow: 0 0 10px rgba(59, 130, 246, 0.5);
}

@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateX(30px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

@media (max-width: 480px) {
  .signup-card {
    padding: 30px 20px;
  }
  
  .signup-title {
    font-size: 1.5rem;
  }
  
  .signup-header {
    margin-bottom: 20px;
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
  
  .avatar-title {
    font-size: 0.8rem;
  }
  
  .avatar-hint {
    display: none; /* 移动端隐藏格式提示 */
  }
  
  .upload-button {
    padding: 6px 12px;
    font-size: 0.8rem;
  }
  
  .file-status {
    display: none; /* 移动端隐藏文件状态 */
  }
  
  .error-message {
    display: none; /* 移动端隐藏错误消息 */
  }
  
  .form-input {
    padding: 12px 14px;
    font-size: 0.95rem;
  }
  
  .submit-button {
    padding: 12px 20px;
    font-size: 0.95rem;
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
    gap: 20px;
  }
  
  .avatar-circle {
    width: 56px;
    height: 56px;
  }
  
  .avatar-info {
    height: 56px;
  }
  
  .avatar-hint {
    display: none; /* 移动端隐藏格式提示 */
  }
  
  .file-status {
    display: none; /* 移动端隐藏文件状态 */
  }
  
  .error-message {
    display: none; /* 移动端隐藏错误消息 */
  }
  
  .form-group {
    gap: 16px;
  }
  
  .form-input {
    padding: 12px 16px;
  }
  
  .submit-button {
    padding: 12px 24px;
  }
}
</style>