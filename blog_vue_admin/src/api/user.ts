import { http } from './requset'

const userloginapi = import.meta.env.VITE_API_USER_LOGIN
const userupfenmianapi = import.meta.env.VITE_API_USER_MINIO

/**
 * Logs in a user
 * @param name
 * @param password
 * @returns
 */
export const userlogin = (name: string, password: string) => {
  return http.request({
    url: userloginapi,
    method: 'POST',
    data: {
      name,
      password,
    },
  })
}

/**
 * 上传user头像
 * @param file
 * @returns
 */
export const userupfenmian = (file: File) => {
  const formData = new FormData()
  formData.append('file', file)
  return http.request({
    url: userupfenmianapi,
    method: 'POST',
    data: formData,
  })
}
