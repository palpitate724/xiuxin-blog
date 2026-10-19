import { defineStore }  from "pinia";
import { ref } from 'vue'

import { getart } from '../../api/art'

export const artlistpage = defineStore('artlist',
     () => {
        interface Tag {
            id: string,
            name: string
        }
        interface Art {
            id: string,
            name: string,
            userid: string,
            catid: string,
            fenmianurl: string,
            sum: string,
            tagvolist: Tag[]
        }
        const artlist = ref<Art[]>([])
        const current = ref(1)
        const size = ref(10)
        const pages = ref(0)
        const total = ref(0)

        /**
         * 分页查询文章列表
         */
        const getartlist = async (paget: number,sizet: number) => {
            
            const { data } = await getart(paget,sizet)
            if (data.code !== 200) {
                alert(data.message)
                return
            }
            artlist.value = data.data.records
            current.value = data.data.current
            size.value = data.data.size
            pages.value = data.data.pages
            total.value = data.data.total
            console.log("art-getartlist",artlist.value)
        }



        return {
            artlist,
            current,
            size,
            pages,
            total,
            getartlist
        }

     }
)
