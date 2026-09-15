import { ref } from 'vue'
import { defineStore } from 'pinia';
import { userupminio } from '../../api/user';

export const signup = defineStore('signup',
    () => {
    
        const name = ref('')
        const password = ref('')
        const email = ref('')
        interface objectname {
            code?: number,
            data: string,
            message?: string
        }
        const objectname: objectname = {
            code: undefined,
            data: '',
            message: undefined
        }

        /**
         * 上传头像获取objectname
         * @param file 
         * @returns 
         */
        const upuserminio = async (file: File) => {
            const { data } = await userupminio(file)
            if (data.code !== 200) {
                alert(data.message)
                return
            }
            objectname.code = data.code
            objectname.data = data.data
            objectname.message = data.message
        }

        return {
            name,
            password,
            email,
            objectname,
            upuserminio
        }
    },
    {
        persist: {
            key: 'signup',
            storage: localStorage,
            pick: ['name', 'email', 'objectname']
        }
    },
)
