import { ref } from 'vue';
import { defineStore } from 'pinia';
import { login,signup,getuserminio } from '../../api/user';

export const uservo = defineStore('user',
    () => {
        const id = ref('')
        const name = ref('')
        interface url{
            code?: number,
            data: string,
            message?: string
        }
        const touxiangurl: url = {
            code: 0,
            data: '',
            message: ''
        }
        const token = ref('')

        /**
         * 登录api接口方法
         * @param username 
         * @param password 
         * @returns 
         */
        const loginvo = async (username:string, password:string) => {
            const { data } = await login(username, password)
            if (data.code !== 200) {
                alert(data.message)
                return
            }
            id.value = data.id
            name.value = data.name
            touxiangurl.data = data.touxiangurl
            token.value = data.token
            alert('登录成功,将跳转到首页')
        }

        /**
         * 注册api接口方法
         * @param username 
         * @param email 
         * @param password 
         * @param touxiangname 
         * @returns 
         */
        const signupvo = async (username:string, email:string, password:string, objectname:string) => {
            const { data } = await signup(username, password, email, objectname)
            if (data.code !== 200) {
                alert(data.message)
                return
            }
            id.value = data.id
            name.value = data.name
            touxiangurl.data = data.touxiangurl
            token.value = data.token
            alert('注册成功,将跳转到首页')
        }

        /**
         * 上传头像到minio并获取临时url
         * @param file 头像文件
         */
        const getminio = async (objectname: string) => {
                 const gettouxiangurl = await getuserminio(objectname)
                 if (gettouxiangurl.status !== 200) {
                     alert('获取头像失败')
                     return
                 }
                 if (gettouxiangurl.data.code !== 200) {
                     alert(gettouxiangurl.data.message)
                     return
                 }
                 touxiangurl.code = gettouxiangurl.data.code
                 touxiangurl.data = gettouxiangurl.data.data
                 touxiangurl.message = gettouxiangurl.data.message
                 
        }

        return {
            id,
            name,
            touxiangurl,
            token,
            loginvo,
            signupvo,
            getminio
        }
    },
    {
        persist: {
            key: 'user',
            storage: localStorage,
            pick: ['id', 'name','touxiangurl', 'token']
        }
    }
)