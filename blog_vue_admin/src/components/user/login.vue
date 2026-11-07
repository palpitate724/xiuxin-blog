<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-header">
        <div class="login-logo">
          <span class="login-logo__dot"></span>
          <span class="login-logo__text">Xiuxin</span>
          <span class="login-logo__sub">BLOG</span>
        </div>
        <h1 class="login-title">登录</h1>
        <p class="login-subtitle">欢迎回来，请登录您的账户</p>
      </div>

      <form class="login-form" @submit.prevent="handleLogin">
        <div class="form-group">
          <label for="username" class="form-label">
            <span class="label-icon"><User :size="16" /></span> 账号
          </label>
          <input
            id="username"
            v-model="form.username"
            type="text"
            class="form-input"
            placeholder="请输入用户名"
            required
            autocomplete="username"
          />
        </div>

        <div class="form-group">
          <label for="password" class="form-label">
            <span class="label-icon"><Lock :size="16" /></span> 密码
          </label>
          <div class="password-wrap">
            <input
              id="password"
              v-model="form.password"
              :type="showPassword ? 'text' : 'password'"
              class="form-input password-input"
              placeholder="请输入密码"
              required
              autocomplete="current-password"
            />
            <button type="button" class="toggle-password" @click="showPassword = !showPassword" aria-label="切换密码可见性">
              <EyeOff v-if="showPassword" :size="18" />
              <Eye v-else :size="18" />
            </button>
          </div>
        </div>

        <div class="form-options">
          <label class="checkbox-label">
            <input v-model="form.remember" type="checkbox" class="checkbox" />
            <span>记住登录状态</span>
          </label>
          <a href="#" class="forgot-link">忘记密码？</a>
        </div>

        <button type="submit" class="submit-button" :disabled="loading">
          <span class="button-text">
            <span v-if="loading" class="loading-spinner"></span>
            {{ loading ? '登录中...' : '登录' }}
          </span>
        </button>
      </form>

      <div v-if="globalError" class="global-error">{{ globalError }}</div>

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
            <svg viewBox="0 0 24 24" width="20" height="20"><path d="M12 0c-6.627 0-12 5.373-12 12 0 5.302 3.438 9.8 8.207 11.387.599.111.793-.261.793-.577v-2.234c-3.338.726-4.033-1.416-4.033-1.416-.546-1.387-1.333-1.756-1.333-1.756-1.089-.745.083-.729.083-.729 1.205.084 1.839 1.237 1.839 1.237 1.07 1.834 2.807 1.304 3.492.997.107-.775.418-1.305.762-1.604-2.665-.305-5.467-1.334-5.467-5.931 0-1.311.469-2.381 1.236-3.221-.124-.303-.535-1.524.117-3.176 0 0 1.008-.322 3.301 1.23.957-.266 1.983-.399 3.003-.404 1.02.005 2.047.138 3.006.404 2.291-1.552 3.297-1.23 3.297-1.23.653 1.653.242 2.874.118 3.176.77.84 1.235 1.911 1.235 3.221 0 4.609-2.807 5.624-5.479 5.921.43.372.823 1.102.823 2.222v3.293c0 .319.192.694.801.576 4.765-1.589 8.199-6.086 8.199-11.386 0-6.627-5.373-12-12-12z" fill="currentColor"/></svg>
          </a>
          <a href="#" class="social-icon google" title="谷歌登录">
            <svg viewBox="0 0 24 24" width="20" height="20"><path fill="#4285F4" d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z"/><path fill="#34A853" d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z"/><path fill="#FBBC05" d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l2.85-2.22.81-.62z"/><path fill="#EA4335" d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z"/></svg>
          </a>
          <a href="#" class="social-icon gitee" title="Gitee登录">
            <svg viewBox="0 0 24 24" width="20" height="20"><path fill="#ED4D4B" d="M11.984 0A12 12 0 0 0 0 12a12 12 0 0 0 12 12 12 12 0 0 0 12-12A12 12 0 0 0 11.984 0zm6.09 5.333c.328 0 .593.266.592.593v1.482a.594.594 0 0 1-.593.592H9.777c-.982 0-1.778.796-1.778 1.778v5.63c0 .327.266.592.593.592h5.63c.983 0 1.778-.796 1.778-1.778v-.296a.593.593 0 0 0-.592-.593h-4.444a.593.593 0 0 1-.593-.593v-1.482c0-.327.266-.593.593-.593h4.444c.983 0 1.778.796 1.778 1.778v1.852c0 1.637-1.33 2.963-2.963 2.963H8.592a2.963 2.963 0 0 1-2.963-2.963V9.778c0-1.637 1.33-2.963 2.963-2.963h9.482z"/></svg>
          </a>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { userpage } from '../../stores/user'
import { userlogin } from '../../api/user'
import { User, Lock, Eye, EyeOff } from '@lucide/vue'

/**
 * 声明组件向外抛出事件：切换到注册视图。
 * 父组件（uesrls.vue）监听此事件以切换当前显示的子组件。
 */
defineEmits<{
  'switch-to-signup': []
}>()

/** Pinia Store 实例：用户登录模块，提供 setLoginState / logout 方法 */
const userpage_v = userpage()

// ─── 表单状态 ──────────────────────────────────────────────────────────────────
/**
 * 登录表单数据（使用 reactive 保持响应式）。
 * username: 用户输入的用户名
 * password: 用户输入的密码（明文存储，提交时加密传输）
 * remember: 是否勾选「记住登录状态」复选框
 */
const form = reactive({
  username: '',
  password: '',
  remember: false,
})

/** 全局错误提示文案，登录失败时显示 */
const globalError = ref('')
/** 登录按钮加载状态，防止重复提交 */
const loading = ref(false)
/** 密码可见性控制：true 显示明文，false 显示掩码 */
const showPassword = ref(false)

// ─── 登录逻辑（预留接入真实 API）──────────────────────────────────────────────
/**
 * 处理登录表单提交。
 * 流程：
 *   1. 校验用户名非空
 *   2. 设置 loading 状态并清空上次错误
 *   3. 调用后端接口（当前为模拟）验证凭据
 *   4. 成功后通过 userpage_v.setLoginState() 持久化登录信息到 localStorage
 *   5. 跳转到文章管理首页 /zhuye
 *   6. 失败时显示错误信息
 *
 * TODO: 接入真实后端接口后取消注释对应代码段
 */
const handleLogin = async () => {
  if (!form.username.trim()) { alert('请输入用户名'); return }
  loading.value = true
  globalError.value = ''

  try {
    // TODO: 对接真实后端接口
    // const res = await userlogin(form.username, form.password)
    // if (res.data?.token) {
    //   userpage_v.setLoginState(res.data.id, res.data.name, res.data.touxiangurl, res.data.token)
    //   window.location.href = '/zhuye'
    // }

    // 模拟登录成功（延迟 600ms 模拟网络请求）
    await new Promise(r => setTimeout(r, 600))
    // TODO: 将硬编码的 mock 数据替换为后端返回的真实用户信息
    userpage_v.setLoginState('1', form.username, '', 'mock-jwt-token')
    alert(`登录成功！欢迎 ${form.username}`)
    window.location.href = '/zhuye'
  } catch (e: any) {
    globalError.value = e?.message || '登录失败，请重试'
  } finally {
    loading.value = false
  }
}
/**
 * 渲染登录页面。
 * 功能：
 *   - 用户名/密码输入
 *   - 密码可见性切换（Eye/EyeOff 图标）
 *   - 「记住登录状态」复选框（前端状态，不持久化）
 *   - 第三方登录入口（GitHub/Google/Gitee，当前无对接）
 *   - 切换到注册页（emit switch-to-signup 事件）
 */
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: var(--spacing-lg);
  background: linear-gradient(180deg, var(--color-bg-primary) 0%, var(--color-bg-secondary) 100%);
}
@keyframes cardAppear {
  from { opacity: 0; transform: translateY(16px) scale(0.97); }
  to   { opacity: 1; transform: translateY(0) scale(1); }
}

.login-card {
  width: 100%;
  max-width: 420px;
  background: var(--color-surface);
  border: 1px solid var(--color-border-divider);
  border-radius: var(--radius-xl);
  box-shadow: var(--color-shadow-md), 0 0 0 1px rgba(59,130,246,0.06);
  padding: calc(var(--spacing-xl) * 2) var(--spacing-xl);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  animation: cardAppear 0.4s cubic-bezier(0.34, 1.56, 0.64, 1);
}

/* ── 头部 ── */
.login-header { text-align: center; margin-bottom: calc(var(--spacing-xl) + var(--spacing-sm)); }
.login-logo {
  display: flex; align-items: center; justify-content: center;
  gap: var(--spacing-xs);
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
  font-weight: var(--font-weight-bold);
  margin-bottom: var(--spacing-lg);
}
.login-logo__dot {
  width: 8px; height: 8px; border-radius: var(--radius-full);
  background: var(--color-accent); box-shadow: 0 0 8px var(--color-accent-soft);
}
.login-logo__text { color: var(--color-text-primary); font-size: 1.1rem; }
.login-logo__sub {
  color: var(--color-text-muted); font-size: 0.7rem;
  letter-spacing: 0.12em; text-transform: uppercase;
  font-weight: var(--font-weight-medium);
}
.login-title {
  font-size: var(--font-size-display); font-weight: var(--font-weight-bold);
  color: var(--color-text-primary); letter-spacing: -0.03em; margin-bottom: var(--spacing-xs);
}
.login-subtitle { font-size: var(--font-size-sm); color: var(--color-text-secondary); }

/* ── 表单 ── */
.login-form { display: flex; flex-direction: column; gap: var(--spacing-md); }
.form-group { display: flex; flex-direction: column; gap: var(--spacing-xs); }
.form-label {
  display: flex; align-items: center; gap: var(--spacing-xs);
  font-size: var(--font-size-sm); font-weight: var(--font-weight-medium);
  color: var(--color-text-secondary);
}
.label-icon { font-size: 1rem; width: 18px; text-align: center; flex-shrink: 0; color: var(--color-accent); }
.form-input {
  padding: 12px 16px;
  background: var(--color-bg-tertiary);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
  font-size: var(--font-size-base);
  color: var(--color-text-primary);
  transition: border-color var(--duration-fast) var(--ease-standard),
              box-shadow var(--duration-fast) var(--ease-standard);
  outline: none; caret-color: var(--color-accent);
}
.form-input::placeholder { color: var(--color-text-muted); }
.form-input:focus {
  border-color: var(--color-accent);
  box-shadow: 0 0 0 3px var(--color-accent-glow-soft), 0 0 8px rgba(59,130,246,0.1);
}
.form-input:-webkit-autofill {
  -webkit-text-fill-color: var(--color-text-primary);
  -webkit-box-shadow: 0 0 0 30px var(--color-bg-tertiary) inset;
  transition: background-color 5000s ease-in-out 0s; caret-color: var(--color-accent);
}

/* 密码框 + 眼睛按钮 */
.password-wrap { position: relative; }
.password-input { padding-right: 48px; }
.toggle-password {
  position: absolute; right: 12px; top: 50%; transform: translateY(-50%);
  background: none; border: none; cursor: pointer; font-size: 1rem;
  padding: 4px; opacity: 0.6; transition: opacity var(--duration-fast);
}
.toggle-password:hover { opacity: 1; }

/* ── 选项行 ── */
.form-options {
  display: flex; justify-content: space-between; align-items: center;
  font-size: var(--font-size-sm); color: var(--color-text-secondary);
}
.checkbox-label { display: flex; align-items: center; gap: var(--spacing-xs); cursor: pointer; transition: color var(--duration-fast); }
.checkbox-label:hover { color: var(--color-text-primary); }
.checkbox { cursor: pointer; accent-color: var(--color-accent); width: 16px; height: 16px; background: var(--color-bg-tertiary); border: 1px solid var(--color-border); border-radius: var(--radius-xs); }
.forgot-link { color: var(--color-accent); font-weight: var(--font-weight-medium); text-decoration: none; transition: color var(--duration-fast); }
.forgot-link:hover { color: var(--color-accent-hover); }

/* ── 提交按钮 ── */
.submit-button {
  padding: 12px 24px;
  background: linear-gradient(135deg, var(--color-accent) 0%, var(--color-accent-secondary) 100%);
  color: var(--color-white);
  border: none; border-radius: var(--radius-md);
  font-size: var(--font-size-md); font-weight: var(--font-weight-semibold);
  cursor: pointer;
  transition: all var(--duration-fast) var(--ease-standard);
  box-shadow: var(--color-shadow);
  display: flex; align-items: center; justify-content: center; gap: var(--spacing-xs);
  position: relative; overflow: hidden;
}
.submit-button:hover:not(:disabled) {
  transform: translateY(-1px);
  box-shadow: 0 4px 20px var(--color-accent-glow);
}
.submit-button:active:not(:disabled) { transform: translateY(0); }
.submit-button:disabled { opacity: 0.7; cursor: not-allowed; transform: none; }
.submit-button::before {
  content: ''; position: absolute; top: 0; left: -100%;
  width: 100%; height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255,255,255,0.15), transparent);
  transition: left var(--duration-slow) var(--ease-linear);
}
.submit-button:hover:not(:disabled)::before { left: 100%; }
.button-text { position: relative; z-index: 1; display: flex; align-items: center; gap: var(--spacing-sm); }

/* ── 加载旋转图标 ── */
.loading-spinner {
  width: 14px;
  height: 14px;
  border: 2px solid rgba(255,255,255,0.4);
  border-top-color: #fff;
  border-radius: 50%;
  animation: spin 0.7s linear infinite;
}
@keyframes spin { to { transform: rotate(360deg); } }

/* ── 全局错误提示 ── */
.global-error {
  margin-top: var(--spacing-sm);
  padding: 10px 14px;
  background: var(--color-error-soft);
  border: 1px solid var(--color-error-border);
  border-radius: var(--radius-md);
  font-size: var(--font-size-sm);
  color: var(--color-error);
  text-align: center;
}

/* ── 页脚 ── */
.login-footer {
  text-align: center; margin-top: var(--spacing-lg);
  padding-top: var(--spacing-lg); border-top: 1px solid var(--color-border-divider);
}
.footer-text { font-size: var(--font-size-sm); color: var(--color-text-secondary); }
.register-link {
  color: var(--color-accent); font-weight: var(--font-weight-semibold);
  margin-left: var(--spacing-xs); text-decoration: none;
  transition: color var(--duration-fast);
}
.register-link:hover { color: var(--color-accent-hover); }

/* ── 第三方登录 ── */
.social-login { margin-top: var(--spacing-lg); padding-top: var(--spacing-lg); border-top: 1px solid var(--color-border-divider); }
.social-title { text-align: center; font-size: var(--font-size-sm); color: var(--color-text-secondary); margin-bottom: var(--spacing-md); }
.social-icons { display: flex; justify-content: center; gap: var(--spacing-md); }
.social-icon {
  width: 44px; height: 44px; display: flex; align-items: center; justify-content: center;
  border-radius: var(--radius-md); background: var(--color-bg-tertiary);
  border: 1px solid var(--color-border); color: var(--color-text-secondary);
  transition: all var(--duration-fast) var(--ease-standard);
  text-decoration: none; flex-shrink: 0;
}
.social-icon:hover { transform: translateY(-2px); border-color: var(--color-border-strong); box-shadow: var(--color-shadow); }
.social-icon.github:hover { color: #181717; background: #fff; border-color: var(--color-border-strong); }
.social-icon.google { color: #4285f4; }
.social-icon.google:hover { background: var(--color-surface); border-color: var(--color-border-strong); }
.social-icon.gitee:hover { color: #ed4d4b; background: var(--color-surface); border-color: var(--color-border-strong); }

/* ── 响应式 ── */
@media (max-width: 480px) {
  .login-card { padding: calc(var(--spacing-xl) * 1.5) var(--spacing-md); }
  .login-title { font-size: 1.75rem; }
}
</style>
