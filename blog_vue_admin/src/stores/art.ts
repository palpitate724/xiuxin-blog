import { defineStore } from 'pinia'
import { ref } from 'vue'
import { addArt, getArtList, getArtDetail } from '../api/art'

export const artpage = defineStore(
  'artpage',
  () => {
    interface Tag {
      id: string
      name: string
    }
    interface Cat {
      id: string
      catname: string
    }
    interface User {
      id: string
      name: string
    }
    interface Art {
      id: string
      name: string
      uservo: User
      cat: Cat
      fenmianurl: string
      sum: string
      cont: string
      tagvolist: Tag[]
      status: number
      cjiantime: string
      uptime: string
    }
    interface Artl {
      id: string
      name: string
      userid: string
      catid: string
      fenmianurl: string
      sum: string
      cont: string
      tagvolist: Tag[]
      status: number
      cjiantime: string
      uptime: string
    }
    // 文章详细数据
    const artdetail = ref<Art>()
    // 分页查询返回列表
    const artlist = ref<Artl[]>([])
    // 当前页码
    const current = ref(1)
    // 每页显示条数
    const size = ref(10)
    // 总页数
    const pages = ref(0)
    // 总条数
    const total = ref(0)

    /**
     * 添加文章
     * @param name
     * @param userid
     * @param catid
     * @param objiectname
     * @param sum
     * @param cont
     * @param tagids
     */
    const addart = async (
      name: string,
      userid: string,
      catid: string,
      objiectname: string,
      sum: string,
      cont: string,
      tagids: string[],
    ) => {
      const { data } = await addArt(name, userid, catid, objiectname, sum, cont, tagids)
      console.log(data)
    }

    /**
     * 分页从查询文章列表
     * @param paget
     * @param sizet
     */
    const getartlist = async (paget: number, sizet: number) => {
      const { data } = await getArtList(paget, sizet)
      console.log('art-getartlist-data', data.data)
      artlist.value = data.data.records
      current.value = data.data.current
      size.value = data.data.size
      pages.value = data.data.pages
      total.value = data.data.total
    }

    /**
     * 根据id获取文章详情
     * @param id
     */
    const getartdetail = async (id: string) => {
      const { data } = await getArtDetail(id)
      console.log('art-getartdetail-data', data.data)
      artdetail.value = data.data
      console.log('art-getartdetail-artdetail', artdetail.value)
    }

    return {
      artlist,
      current,
      size,
      pages,
      total,
      artdetail,
      addart,
      getartlist,
      getartdetail,
    }
  },
  {
    persist: true,
  },
)
