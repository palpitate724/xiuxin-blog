import { defineStore }  from "pinia";
import { ref } from 'vue'

import { getart } from '../../api/art'

export const artlistpage = defineStore('artlist',
     () => {
        interface Tag {
            id: number,
            name: string
        }
        interface Art {
            id: number,
            name: string,
            userid: number,
            catid: number,
            fenmianurl: string,
            sum: string,
            tagvolist: Tag[]
        }
        const artlist = ref<Art[]>([])

        /**
         * 查询文章列表（暂时全部文章，后续完善分页和搜索）
         */
        const getartlist = async () => {
            
            const { data } = await getart()
            if (data.code !== 200) {
                alert(data.message)
                return
            }
            artlist.value = data.data
        }



        return {
            artlist,
            getartlist
        }

     }
)
