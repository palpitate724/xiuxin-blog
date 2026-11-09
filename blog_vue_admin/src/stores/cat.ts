import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getCat, setCat, deleteCat } from '../api/cat'

export const catpage = defineStore(
  'catpage',
  () => {
    interface Cat {
      id: string
      name: string
      cjiantime: string
    }

    const catlist = ref<Cat[]>([])

    /**
     * 获取分类列表
     */
    const getcat = async () => {
      const { data } = await getCat()
      console.log('cat-getcat-data', data.data)
      catlist.value = data.data
      console.log('cat-getcat-catlist', catlist.value)
    }

    /**
     * 添加分类
     * @param name 分类名称
     * @returns
     */
    const setcat = async (name: string) => {
      const { data } = await setCat(name)
      console.log('cat-setcat-data', data)
    }

    /**
     * 删除分类
     * @param id
     */
    const deletecat = async (id: string) => {
      const { data } = await deleteCat(id)
      console.log('cat-deletecat-data', data)
      // 删除成功后从catlist中移除对应的分类
      catlist.value = catlist.value.filter((cat) => cat.id !== id)
    }

    return {
      catlist,
      getcat,
      setcat,
      deletecat,
    }
  },
  {
    persist: true,
  },
)
