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
  padding: 44px 40px;
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
.login-card::before {
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
.login-card::after {
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
.login-card .deco-glow {
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(circle at 30% 20%, var(--color-accent-glow-xlight) 0%, transparent 50%),
              radial-gradient(circle at 70% 80%, rgba(6, 182, 212, 0.05) 0%, transparent 50%);
  pointer-events: none;
  z-index: 0;
}

.login-header {
  text-align: center;
  margin-bottom: 28px;
  position: relative;
  z-index: 1;
}

.login-title {
  font-size: 1.9rem;
  font-weight: 700;
  color: var(--color-white);
  margin: 0 0 8px 0;
  letter-spacing: 1px;
  text-shadow:
    0 0 30px var(--color-progress-start-soft),
    0 2px 10px var(--color-shadow-text);
  position: relative;
  /* 渐变文字效果 */
  background: linear-gradient(135deg, #ffffff 0%, #bae6fd 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

/* 浅色主题登录标题 */

.login-title::after {
  content: '';
  display: block;
  width: 50px;
  height: 3px;
  background: linear-gradient(90deg, var(--color-accent), var(--color-accent-secondary));
  border-radius: 2px;
  margin: 12px auto 0;
  box-shadow: 0 0 10px var(--color-accent-glow);
}

.login-subtitle {
  font-size: 0.9rem;
  color: var(--color-text-muted);
  margin: 0;
  letter-spacing: 0.5px;
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 18px;
  position: relative;
  z-index: 1;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 6px;
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

.label-icon {
  width: 16px;
  height: 16px;
  color: var(--color-accent);
  opacity: 0.8;
}

.form-input {
  padding: 14px 16px;
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

.form-input::before {
  content: '';
  position: absolute;
  inset: -1px;
  border-radius: 11px;
  padding: 1px;
  background: linear-gradient(135deg, transparent 0%, transparent 100%);
  -webkit-mask: linear-gradient(#fff 0 0) content-box, linear-gradient(#fff 0 0);
  mask: linear-gradient(#fff 0 0) content-box, linear-gradient(#fff 0 0);
  -webkit-mask-composite: xor;
  mask-composite: exclude;
  transition: background 0.35s ease;
  pointer-events: none;
}

.form-input:hover {
  cursor: text;
  border-color: var(--color-border-stronger);
  background: rgba(30, 41, 59, 0.6);
}

.form-input:hover::before {
  background: linear-gradient(135deg, var(--color-accent-glow-soft) 0%, transparent 100%);
}

.form-input:focus {
  border-color: var(--color-accent);
  background: rgba(30, 41, 59, 0.7);
  box-shadow:
    0 0 0 3px var(--color-accent-glow-soft),
    0 0 20px var(--color-accent-glow-softer),
    inset 0 1px 0 rgba(255, 255, 255, 0.05);
}

.form-input:focus::before {
  background: linear-gradient(135deg, var(--color-accent-glow) 0%, var(--color-accent-secondary-soft) 100%);
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
  -webkit-box-shadow: 0 0 0 30px rgba(30, 41, 59, 0.9) inset, 0 0 0 3px var(--color-accent-glow-soft), 0 0 20px var(--color-accent-glow-softer);
}

.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 0.88rem;
  color: var(--color-text-muted-softer);
}

.checkbox-label {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  color: var(--color-text-muted-softer);
  transition: color 0.3s ease;
}

.checkbox-label:hover {
  color: var(--color-text-secondary);
}

.checkbox {
  cursor: pointer;
  accent-color: var(--color-accent);
  width: 15px;
  height: 15px;
  background: rgba(30, 41, 59, 0.6);
  border: 1px solid var(--color-border-stronger);
  border-radius: 4px;
  backdrop-filter: blur(10px);
  transition: all 0.2s ease;
}

.checkbox:hover {
  border-color: var(--color-accent);
  box-shadow: 0 0 6px var(--color-accent-glow-soft);
}

.forgot-link {
  color: var(--color-accent);
  text-decoration: none;
  font-weight: 500;
  transition: all 0.3s ease;
  position: relative;
}

.forgot-link::after {
  content: '';
  position: absolute;
  bottom: -2px;
  left: 0;
  width: 0;
  height: 1px;
  background: var(--color-accent-secondary);
  transition: width 0.3s ease;
}

.forgot-link:hover {
  color: var(--color-accent-light);
  text-shadow: 0 0 10px var(--color-accent-glow);
}

.forgot-link:hover::after {
  width: 100%;
}

.submit-button {
  padding: 14px 24px;
  background: linear-gradient(135deg, var(--color-accent) 0%, var(--color-accent-secondary) 50%, var(--color-accent-tertiary) 100%);
  color: var(--color-white);
  border: none;
  border-radius: 10px;
  font-size: 0.95rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.4s cubic-bezier(0.34, 1.56, 0.64, 1);
  box-shadow:
    0 4px 20px var(--color-accent-glow),
    0 0 40px var(--color-accent-glow-xlight);
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
    0 8px 30px var(--color-accent-glow),
    0 0 50px var(--color-accent-glow-soft),
    0 0 80px var(--color-accent-glow-xlight);
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

.login-footer {
  text-align: center;
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid var(--color-border-dim);
  position: relative;
  z-index: 1;
}

.footer-text {
  font-size: 0.88rem;
  color: var(--color-text-muted);
  margin: 0;
}

.register-link {
  color: var(--color-accent-secondary);
  text-decoration: none;
  font-weight: 600;
  margin-left: 4px;
  transition: all 0.3s ease;
  position: relative;
}

.register-link::after {
  content: '';
  position: absolute;
  bottom: -2px;
  left: 0;
  width: 0;
  height: 1px;
  background: var(--color-accent-secondary-light);
  transition: width 0.3s ease;
}

.register-link:hover {
  color: var(--color-accent-secondary-light);
  text-shadow: 0 0 10px var(--color-accent-secondary-soft);
}

.register-link:hover::after {
  width: 100%;
}

.social-login {
  margin-top: 28px;
  padding-top: 24px;
  border-top: 1px solid var(--color-border-dim);
  position: relative;
  z-index: 1;
}

.social-title {
  text-align: center;
  font-size: 0.82rem;
  color: var(--color-text-muted);
  margin: 0 0 16px 0;
  letter-spacing: 1px;
  text-transform: uppercase;
}

.social-icons {
  display: flex;
  justify-content: center;
  gap: 16px;
}

.social-icon {
  width: 44px;
  height: 44px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 12px;
  background: rgba(30, 41, 59, 0.5);
  border: 1px solid var(--color-border);
  transition: all 0.35s cubic-bezier(0.34, 1.56, 0.64, 1);
  backdrop-filter: blur(10px);
  text-decoration: none;
  position: relative;
  overflow: hidden;
}

.social-icon::before {
  content: '';
  position: absolute;
  inset: 0;
  border-radius: 12px;
  padding: 1px;
  background: linear-gradient(135deg, transparent 0%, var(--color-accent-glow-soft) 100%);
  -webkit-mask: linear-gradient(#fff 0 0) content-box, linear-gradient(#fff 0 0);
  mask: linear-gradient(#fff 0 0) content-box, linear-gradient(#fff 0 0);
  -webkit-mask-composite: xor;
  mask-composite: exclude;
  opacity: 0;
  transition: opacity 0.3s ease;
  pointer-events: none;
}

.social-icon svg {
  width: 22px;
  height: 22px;
  transition: transform 0.3s ease;
  position: relative;
  z-index: 1;
}

.social-icon:hover {
  background: var(--color-accent-glow);
  border-color: var(--color-border-stronger);
  transform: translateY(-3px) scale(1.05);
  box-shadow: 0 6px 20px var(--color-accent-glow);
}

.social-icon:hover::before {
  opacity: 1;
}

.social-icon:hover svg {
  transform: scale(1.15);
}

.social-icon.github:hover {
  border-color: var(--color-white-softer);
  box-shadow: 0 6px 20px var(--color-white-soft);
  background: rgba(255, 255, 255, 0.1);
}

.social-icon.google:hover {
  border-color: var(--color-google-blue-soft);
  box-shadow: 0 6px 20px var(--color-google-blue-soft);
  background: rgba(66, 133, 244, 0.1);
}

.social-icon.gitee:hover {
  border-color: var(--color-gitee-red-soft);
  box-shadow: 0 6px 20px var(--color-gitee-red-soft);
  background: rgba(237, 77, 75, 0.1);
}

/* 悬浮状态增强 */
.login-card:hover {
  box-shadow:
    0 12px 48px rgba(0, 0, 0, 0.28),
    0 4px 12px rgba(0, 0, 0, 0.16),
    inset 0 1px 0 rgba(255, 255, 255, 0.16),
    inset 0 -1px 0 rgba(0, 0, 0, 0.08);
  border-color: rgba(255, 255, 255, 0.12);
}

.login-card:hover::before {
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
  .login-card {
    padding: 32px 20px;
    border-radius: 16px;
  }

  .login-title {
    font-size: 1.5rem;
  }

  .login-header {
    margin-bottom: 20px;
  }

  .form-input {
    padding: 12px 14px;
    font-size: 0.9rem;
  }

  .submit-button {
    padding: 12px 20px;
    font-size: 0.9rem;
  }

  .social-login {
    margin-top: 20px;
    padding-top: 20px;
  }

  .social-title {
    font-size: 0.75rem;
    margin-bottom: 12px;
  }

  .social-icons {
    gap: 12px;
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
    padding: 36px 24px;
  }

  .login-title {
    font-size: 1.6rem;
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

  .social-login {
    margin-top: 20px;
    padding-top: 20px;
  }

  .social-icons {
    gap: 14px;
  }

  .social-icon {
    width: 42px;
    height: 42px;
  }

  .social-icon svg {
    width: 21px;
    height: 21px;
  }
}

/* ===== 浅色主题样式 ===== */
/* 登录卡片浅色适配 — 冷调玻璃质感 */


/* 表单输入框浅色适配 */



/* 标签浅色适配 */


/* 复选框浅色适配 */

/* 忘记密码链接 */


/* 注册链接 */


/* 页脚文字 */


/* 第三方登录区域 */




/* 提交按钮浅色适配 */

</style>