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
    data: {
      name: name,
    },
  })
}

/**
 * 删除cat
 * @param id
 * @returns
 */
export const deleteCat = (id: string) => {
  return http.request({
    url: `${catapi}/${id}`,
    method: 'DELETE',
  })
}
