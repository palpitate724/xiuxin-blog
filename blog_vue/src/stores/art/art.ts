import { selectart } from './../../api/art';
import { defineStore } from 'pinia'
import { ref } from 'vue'

export const art = defineStore('art',
    () => {

        interface Tag {
            id: number
            name: string
        }
        interface Art {
            id: string,
            name: string,
            userid: string,
            catid: string,
            fenmianurl: string,
            sum: string,
            cont: string,
            tags: Tag[],
            cjiantime: string,
            uptime: string,
            status: number
        }

        const id = ref('')
        const name = ref('')
        const userid = ref('')
        const catid = ref('')
        const fenmianurl = ref('')
        const sum = ref('')
        const cont = ref('')
        const tags = ref<Tag[]>([])
        const cjiantime = ref('')
        const uptime = ref('')
        const status = ref(0)

        const getart = async (artid: string) => {
            const { data } = await selectart(artid)
            if (data.code !== 200) {
                alert(data.message)
                return
            }
            id.value = data.data.id
            name.value = data.data.name
            userid.value = data.data.userid
            catid.value = data.data.catid
            fenmianurl.value = data.data.fenmianurl
            sum.value = data.data.sum
            cont.value = data.data.cont
            tags.value = data.data.tags
            cjiantime.value = data.data.cjiantime
            uptime.value = data.data.uptime
            status.value = data.data.status
        }

        return {
            id,
            name,
            userid,
            catid,
            fenmianurl,
            sum,
            cont,
            tags,
            cjiantime,
            uptime,
            status,
            getart
        }

    },
    {
        persist: {
            key: 'art',
            storage: localStorage,
            pick: ['catid', 'id', 'name', 'userid', 'fenmianurl', 'sum', 'cont', 'tags']
        }
    }
)