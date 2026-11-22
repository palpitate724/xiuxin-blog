import axios from 'axios'
import { userpage } from '@/stores/user'
import router from '@/router'

const api = import.meta.env.VITE_API

export const http = axios.create({
  baseURL: api,
  timeout: 5000,
})

http.interceptors.request.use(
  (config) => {
    const userpagr_a = userpage()
    if (userpagr_a.user.token) {
      config.headers.token = userpagr_a.user.token
    }
    return config
  },
  (err) => {
    return Promise.reject(err)
  },
)

http.interceptors.response.use(
  (response) => {
    return response
  },
  (err) => {
    switch (err.response.status) {
      case 401:
        // 处理未授权错误，例如跳转到登录页面
        window.alert('未授权，请登录')
        userpage().logout()
        router.push('/login')
        break
      default:
        break
    }
    return Promise.reject(err)
  },
)
