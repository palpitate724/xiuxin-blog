import { http } from './requset'

const apiart = import.meta.env.VITE_API_ART
const apiartminio = import.meta.env.VITE_API_ART_MINIO

/**
 * 添加文章api接口方法
 * @param name 文章标题
 * @param userid 用户id
 * @param catid 分类id
 * @param objectname 头像name
 * @param sum 摘要
 * @param cont 内容
 * @param tagids 标签
 * @returns Promise<any>
 */
export const addArt = (
  name: string,
  userid: string,
  catid: string,
  objiectname: string,
  sum: string,
  cont: string,
  tagids: string[],
) => {
  return http.request({
    url: apiart,
    method: 'POST',
    data: {
      name,
      userid,
      catid,
      objiectname,
      sum,
      cont,
      tagids,
    },
  })
}

/**
 * 分页查询
 * @param paget
 * @param size
 * @returns
 */
export const getArtList = (paget: number, size: number) => {
  return http.request({
    url: apiart,
    method: 'GET',
    data: {
      paget,
      size,
    },
  })
}

/**
 * 根据id获取文章详情
 * @param id
 * @returns
 */
export const getArtDetail = (id: string) => {
  return http.request({
    url: `${apiart}/${id}`,
    method: 'GET',
  })
}

/**
 * 上传文章封面到minio
 * @param file
 */
export const getArtMinio = (file: File) => {
  const filedata = new FormData()
  filedata.append('file', file)
  return http.request({
    url: apiartminio,
    method: 'POST',
    data: filedata,
  })
}
