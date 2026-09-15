<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-header">
        <h2 class="login-title">系统登录</h2>
        <p class="login-subtitle">欢迎回到蔚蓝极客</p>
      </div>
      
      <form class="login-form" @submit.prevent="handleSubmit">
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
            placeholder="请输入密码"
            required
          />
        </div>
        
        <div class="form-options">
          <label class="checkbox-label">
            <input type="checkbox" name="remember" class="checkbox" />
            <span>记住登录状态</span>
          </label>
          <a href="#" class="forgot-link">忘记密码？</a>
        </div>
        
        <button type="submit" class="submit-button">
          <span class="button-text">登录系统</span>
          <span class="button-icon">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M5 12h14M12 5l7 7-7 7"></path>
            </svg>
          </span>
        </button>
      </form>
      
      <div class="login-footer">
        <p class="footer-text">
          还没有账户？
          <a href="#" class="register-link" @click.prevent="$emit('switch-to-signup')">立即注册</a>
        </p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
// 定义事件
const emit = defineEmits<{
  'switch-to-signup': []
  'hide-cursor': []
  'show-cursor': []
}>()

const handleSubmit = (e: Event) => {
  const form = e.target as HTMLFormElement
  const formData = new FormData(form)
  const loginData: Record<string, FormDataEntryValue> = {}
  formData.forEach((value, key) => {
    loginData[key] = value
  })
  console.log('Login data:', loginData)
  // TODO: 实现登录逻辑
}
</script>

<style scoped>
.login-container {
  width: 100%;
  max-width: 440px;
  padding: 20px;
}

.login-card {
  background: transparent;
  backdrop-filter: none;
  border: none;
  border-radius: 16px;
  padding: 40px;
  box-shadow: none;
  animation: slideIn 0.8s ease-out;
}

.login-header {
  text-align: center;
  margin-bottom: 24px;
}

.login-title {
  font-size: 1.8rem;
  font-weight: 700;
  color: white;
  margin: 0 0 8px 0;
  letter-spacing: 1px;
  text-shadow: 0 0 20px rgba(59, 130, 246, 0.5);
}

.login-subtitle {
  font-size: 0.95rem;
  color: rgba(147, 197, 253, 0.8);
  margin: 0;
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 18px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
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
  padding: 14px 16px;
  background: rgba(30, 41, 59, 0.4);
  border: 1px solid rgba(59, 130, 246, 0.3);
  border-radius: 8px;
  font-size: 1rem;
  color: white;
  transition: all 0.3s ease;
  outline: none;
  backdrop-filter: blur(10px);
  caret-color: #38BDF8;
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

.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 0.9rem;
}

.checkbox-label {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  color: rgba(148, 163, 184, 0.8);
  transition: color 0.3s ease;
}

.checkbox-label:hover {
  color: rgba(147, 197, 253, 0.9);
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

.forgot-link {
  color: #3b82f6;
  text-decoration: none;
  font-weight: 500;
  transition: all 0.3s ease;
}

.forgot-link:hover {
  color: #60a5fa;
  text-shadow: 0 0 10px rgba(59, 130, 246, 0.5);
}

.submit-button {
  padding: 14px 24px;
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

.login-footer {
  text-align: center;
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px solid rgba(59, 130, 246, 0.2);
}

.footer-text {
  font-size: 0.9rem;
  color: rgba(148, 163, 184, 0.8);
  margin: 0;
}

.register-link {
  color: #3b82f6;
  text-decoration: none;
  font-weight: 600;
  margin-left: 4px;
  transition: all 0.3s ease;
}

.register-link:hover {
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
  .login-card {
    padding: 30px 20px;
  }
  
  .login-title {
    font-size: 1.5rem;
  }
}
</style>