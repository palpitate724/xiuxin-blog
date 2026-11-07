import { http } from './requset'

const catapi = import.meta.env.VITE_API_CAT

/**
 * 获取cat列表
 * @returns
 */
export const getCat = () => {
  return http.request({
    url: catapi,
    method: 'GET',
  })
}

/**
 * 添加cat
 * @param name
 * @returns
 */
export const setCat = (name: string) => {
  return http.request({
    url: catapi,
    method: 'POST',
    data: name,
  })
}
