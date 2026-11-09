import { http } from './requset'

const tagapi = import.meta.env.VITE_API_TAG

/**
 * 获取tag列表
 */
export const getTag = () => {
  return http.request({
    url: tagapi,
    method: 'GET',
  })
}

/**
 * 添加tag
 * @param name
 * @returns
 */
export const setTag = (name: string) => {
  return http.request({
    url: tagapi,
    method: 'POST',
    data: {
      name: name,
    },
  })
}

/**
 * 删除标签
 * @param id
 * @returns
 */
export const deleteTag = (id: string) => {
  return http.request({
    url: `${tagapi}/${id}`,
    method: 'DELETE',
  })
}
