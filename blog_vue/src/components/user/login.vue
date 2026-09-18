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
      
      <div class="social-login">
        <p class="social-title">第三方登录</p>
        <div class="social-icons">
          <a href="#" class="social-icon github" title="GitHub登录">
            <svg viewBox="0 0 24 24">
              <path d="M12 0c-6.626 0-12 5.373-12 12 0 5.302 3.438 9.8 8.207 11.387.599.111.793-.261.793-.577v-2.234c-3.338.726-4.033-1.416-4.033-1.416-.546-1.387-1.333-1.756-1.333-1.756-1.089-.745.083-.729.083-.729 1.205.084 1.839 1.237 1.839 1.237 1.07 1.834 2.807 1.304 3.492.997.107-.775.418-1.305.762-1.604-2.665-.305-5.467-1.334-5.467-5.931 0-1.311.469-2.381 1.236-3.221-.124-.303-.535-1.524.117-3.176 0 0 1.008-.322 3.301 1.23.957-.266 1.983-.399 3.003-.404 1.02.005 2.047.138 3.006.404 2.291-1.552 3.297-1.23 3.297-1.23.653 1.653.242 2.874.118 3.176.77.84 1.235 1.911 1.235 3.221 0 4.609-2.807 5.624-5.479 5.921.43.372.823 1.102.823 2.222v3.293c0 .319.192.694.801.576 4.765-1.589 8.199-6.086 8.199-11.386 0-6.627-5.373-12-12-12z" fill="white"/>
            </svg>
          </a>
          <a href="#" class="social-icon google" title="谷歌登录">
            <svg viewBox="0 0 24 24">
              <path d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z" fill="#4285F4"/>
              <path d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z" fill="#34A853"/>
              <path d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l2.85-2.22.81-.62z" fill="#FBBC05"/>
              <path d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z" fill="#EA4335"/>
            </svg>
          </a>
          <a href="#" class="social-icon gitee" title="Gitee登录">
            <svg viewBox="0 0 24 24">
              <path d="M11.984 0A12 12 0 0 0 0 12a12 12 0 0 0 12 12 12 12 0 0 0 12-12A12 12 0 0 0 11.984 0zm6.09 5.333c.328 0 .593.266.592.593v1.482a.594.594 0 0 1-.593.592H9.777c-.982 0-1.778.796-1.778 1.778v5.63c0 .327.266.592.593.592h5.63c.983 0 1.778-.796 1.778-1.778v-.296a.593.593 0 0 0-.592-.593h-4.444a.593.593 0 0 1-.593-.593v-1.482c0-.327.266-.593.593-.593h4.444c.983 0 1.778.796 1.778 1.778v1.852c0 1.637-1.33 2.963-2.963 2.963H8.592a2.963 2.963 0 0 1-2.963-2.963V9.778c0-1.637 1.33-2.963 2.963-2.963h9.482z" fill="#ED4D4B"/>
            </svg>
          </a>
        </div>
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

.social-login {
  margin-top: 24px;
  padding-top: 24px;
  border-top: 1px solid rgba(59, 130, 246, 0.2);
}

.social-title {
  text-align: center;
  font-size: 0.85rem;
  color: rgba(148, 163, 184, 0.8);
  margin: 0 0 16px 0;
}

.social-icons {
  display: flex;
  justify-content: center;
  gap: 20px;
}

.social-icon {
  width: 44px;
  height: 44px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 12px;
  background: rgba(30, 41, 59, 0.4);
  border: 1px solid rgba(59, 130, 246, 0.2);
  transition: all 0.3s ease;
  backdrop-filter: blur(10px);
  text-decoration: none;
}

.social-icon svg {
  width: 24px;
  height: 24px;
  transition: transform 0.3s ease;
}

.social-icon:hover {
  background: rgba(59, 130, 246, 0.2);
  border-color: rgba(59, 130, 246, 0.4);
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(59, 130, 246, 0.3);
}

.social-icon:hover svg {
  transform: scale(1.1);
}

.social-icon.github:hover {
  border-color: rgba(255, 255, 255, 0.3);
  box-shadow: 0 4px 15px rgba(255, 255, 255, 0.2);
}

.social-icon.google:hover {
  border-color: rgba(66, 133, 244, 0.4);
  box-shadow: 0 4px 15px rgba(66, 133, 244, 0.3);
}

.social-icon.gitee:hover {
  border-color: rgba(237, 77, 75, 0.4);
  box-shadow: 0 4px 15px rgba(237, 77, 75, 0.3);
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
  
  .login-header {
    margin-bottom: 20px;
  }
  
  .form-input {
    padding: 12px 14px;
    font-size: 0.95rem;
  }
  
  .submit-button {
    padding: 12px 20px;
    font-size: 0.95rem;
  }
  
  .social-login {
    margin-top: 20px;
    padding-top: 20px;
  }
  
  .social-title {
    font-size: 0.8rem;
    margin-bottom: 12px;
  }
  
  .social-icons {
    gap: 16px;
  }
  
  .social-icon {
    width: 40px;
    height: 40px;
  }
  
  .social-icon svg {
    width: 20px;
    height: 20px;
  }
}

@media (max-width: 768px) {
  .login-container {
    max-width: 100%;
    padding: 16px;
  }
  
  .login-card {
    padding: 32px 24px;
  }
  
  .login-title {
    font-size: 1.6rem;
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
  
  .social-login {
    margin-top: 20px;
    padding-top: 20px;
  }
  
  .social-icons {
    gap: 18px;
  }
  
  .social-icon {
    width: 42px;
    height: 42px;
  }
  
  .social-icon svg {
    width: 22px;
    height: 22px;
  }
}
</style>