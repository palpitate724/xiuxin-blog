import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getCat, setCat } from '../api/cat'

export const catpage = defineStore(
  'catpage',
  () => {
    interface Cat {
      id: string
      name: string
    }

    const catlist = ref<Cat[]>([])

    const getcat = async () => {
      const { data } = await getCat()
      console.log('cat-getcat-data', data.data)
      catlist.value = data.data
    }

    return {
      catlist,
      getcat,
    }
  },
  {
    persist: true,
  },
)
