import { http } from './requset'

const loginurll = import.meta.env.VITE_API_USER_LOGIN
const signupurll = import.meta.env.VITE_API_USER_SIGNUP
const userminio = import.meta.env.VITE_API_USER_MINIO

/**
 * 登录api接口方法
 * @param name 用户名
 * @param password 密码
 * @returns Promise<any>
 */
export const login = (name: string, password: string) => {
    return http.request({
        url: loginurll,
        method: 'post',
        data: {
            name,
            password
        }
    })
}

/**
 * 注册api接口方法
 * @param name 用户名
 * @param password 密码
 * @param email 邮箱
 * @param Objectnaem 头像name
 * @returns Promise<any>
 */
export const signup = (name: string, password: string, email: string, objectname: string) => {
    console.log("user-signup",name,password,email,objectname)
    return http.request({
        url: signupurll,
        method: 'post',
        data: {
            name,
            password,
            email,
            objectname
        }
    })
}

/**
 * 上传头像到minio
 * @param file 头像文件
 * @returns Promise<any>
 */
export const userupminio = (file: File) => {
    const formData = new FormData()
    formData.append('file', file)
     return http.request({
         url: userminio,
         method: 'post',
         data: formData,
     })
}

/**
 * 获取头像临时url
 * @param objectname 
 * @returns 
 */
export const getuserminio = (objectname: string) => {
    console.log("user-getuserminio",objectname)
    return http.request({
        url: userminio,
        method: 'get',
        params: {
            objectname
        }
    })
}