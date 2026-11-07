import { defineStore } from 'pinia'
import { ref } from 'vue'
import { userlogin, userupfenmian } from '@/api/user'

export const userpage = defineStore(
  'userpage',
  () => {
    const id = ref<string>('')
    const name = ref<string>('')
    const fenmianurl = ref<string>('')
    const token = ref<string>('')

    /**
     * 设置登录状态（由 login.vue 在登录成功后调用）
     * @param id 用户ID
     * @param name 用户名
     * @param fenmianurl 头像URL
     * @param token JWT令牌
     */
    const setLoginState = (userId: string, userName: string, url: string, jwtToken: string) => {
      id.value = userId
      name.value = userName
      fenmianurl.value = url
      token.value = jwtToken
    }

    /**
     * 退出登录
     */
    const logout = () => {
      id.value = ''
      name.value = ''
      fenmianurl.value = ''
      token.value = ''
    }

    /**
     * 获取当前用户角色（预留，后续接入真实接口）
     */
    const getUserRole = (): string => {
      // TODO: 根据 token 解析或请求后端接口获取角色
      return 'admin'
    }

    return {
      id,
      name,
      fenmianurl,
      token,
      setLoginState,
      logout,
      getUserRole,
    }
  },
  {
    persist: true,
  },
)
