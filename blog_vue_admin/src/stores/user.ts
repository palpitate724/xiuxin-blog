import { defineStore } from 'pinia'
import { ref } from 'vue'
import { userlogin, userUpfenmian } from '@/api/user'

export const userpage = defineStore(
  'userpage',
  () => {
    interface UserState {
      id: string
      name: string
      touxiangurl?: string
      token: string
      roleid: number
    }
    const user = ref<UserState>({
      id: '',
      name: '',
      token: '',
      roleid: -1,
    })

    /**
     * 设置登录状态（由 login.vue 在登录成功后调用）
     * @param id 用户ID
     * @param name 用户名
     * @param touxiangurl 头像URL
     * @param token JWT令牌
     */
    const login = async (name: string, password: string) => {
      const { data } = await userlogin(name, password)
      user.value.id = data.data.id
      user.value.name = data.data.name
      user.value.touxiangurl = data.data.touxiangurl
      user.value.token = data.data.token
      user.value.roleid = data.data.roleid
    }

    /**
     * 退出登录
     */
    const logout = () => {
      user.value.id = ''
      user.value.name = ''
      user.value.touxiangurl = ''
      user.value.token = ''
    }

    const userupfenmian = async (file: File) => {
      await userUpfenmian(file)
    }

    /**
     * 获取当前用户角色（预留，后续接入真实接口）
     */
    const getUserRole = (): string => {
      // TODO: 根据 token 解析或请求后端接口获取角色
      return 'admin'
    }

    return {
      user,
      login,
      logout,
      getUserRole,
      userupfenmian,
    }
  },
  {
    persist: true,
  },
)
