import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getTag, setTag, deleteTag } from '../api/tag'

export const tagpage = defineStore(
  'tagpage',
  () => {
    /** 单个标签的结构：id 唯一标识，name 显示名称 */
    interface TagItem {
      id: string
      name: string
      cjiantime: string
    }

    /** 标签列表，由后端接口加载；开发期使用模拟数据占位 */
    const taglist = ref<TagItem[]>([])

    /**
     * 获取标签列表。
     * TODO: 接入真实后端接口后替换模拟数据
     */
    const gettag = async () => {
      const { data } = await getTag()
      console.log('tag-gettag-data', data.data)
      taglist.value = data.data
      console.log('tag-gettag-taglist', taglist.value)
    }

    /**
     * 添加标签。
     * @param name
     */
    const settag = async (name: string) => {
      const { data } = await setTag(name)
      console.log('tag-settag-data', data)
    }

    /**
     * 删除标签。
     */
    const deletetag = async (id: string) => {
      const { data } = await deleteTag(id)
      console.log('tag-deletetag-data', data)
      // 删除成功后从taglist中移除对应的标签
      taglist.value = taglist.value.filter((tag) => tag.id !== id)
    }

    return {
      taglist,
      gettag,
      settag,
      deletetag,
    }
  },
  {
    persist: true,
  },
)
