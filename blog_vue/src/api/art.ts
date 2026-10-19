import { http } from './requset'

const artapi = import.meta.env.VITE_API_ART

/**
 * 分页查询文章api接口方法(还未完善)
 */
export const getart = (page: number,size: number) => {
    return http.request({
        url: artapi,
        method: 'get',
        params: {
            page,
            size
        }
    })
}

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
export const insart = (name: string, userid: string, catid: string, objectname: string, sum: string, cont: string, tagids: string[]) => {
    return http.request({
        url: artapi,
        method: 'post',
        data: {
            name,
            userid,
            catid,
            objectname,
            sum,
            cont,
            tagids
        }
    })
}

/**
 * 精确查询文章
 * @param id
 * @returns 
 */
export const selectart = (id: string) => {
    return http.request({
        url: `${artapi}/${id}`,
        method: 'get',
    })
}