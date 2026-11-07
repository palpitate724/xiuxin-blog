import { defineStore } from 'pinia'
import { computed } from 'vue'

export const useThemeStore = defineStore('theme', () => {
  const isDark = computed(() => true)

  return {
    isDark,
  }
})
