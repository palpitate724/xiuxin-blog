import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getArtMinio } from '../api/art'

export const artminio = defineStore(
  'artminio',
  () => {
    const objectname = ref<string>('')

    /**
     * 上传文章封面图片到minio
     * @param file
     */
    const upartminio = async (file: File) => {
      const { data } = await getArtMinio(file)
      objectname.value = data.data
      console.log('artminio-postartminio-objectname', objectname.value)
    }

    return {
      objectname,
      upartminio,
    }
  },
  {
    persist: true,
  },
)
