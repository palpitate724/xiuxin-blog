// useTheme Composable
// 固定深色主题，无切换功能

import { computed } from 'vue'

export interface ThemeState {
  isDark: boolean
}

export function useTheme(): ThemeState {
  const isDark = computed(() => true)

  // 始终确保 DOM 为深色
  document.documentElement.setAttribute('data-theme', 'dark')

  return { isDark }
}
