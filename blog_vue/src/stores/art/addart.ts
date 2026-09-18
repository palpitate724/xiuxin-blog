import { defineStore } from "pinia"

import { insart } from '../../api/art'

export const addartpage = defineStore('addart',
    () => {

        interface Addart {
            name: string,
            userid: string,
            catid: string,
            objectname: string,
            sum: string,
            cont: string,
            tagids: string[]
        }
        const addart: Addart = {
            name: '',
            userid: '',
            catid: '',
            objectname: '',
            sum: '',
            cont: '',
            tagids: []
        }

        /**
         * 添加文章
         * @param name 文章标题
         * @param userid 用户id
         * @param catid 分类id
         * @param objectname 头像name
         * @param sum 摘要
         * @param cont 内容
         * @param tagids 标签
         * @returns Promise<any>
         */
        const insaddart = async ()=> {
            const { data } = await insart("测试", "2097228643959463937", "2097182354127405057", "art/7bb247cb8838400497ebcf229cbbe7c6.png", "测试", "测试", ["2097218740687720449","2097218804558581761","2097218835973918722","2097218839425830913","2097218842252791810"])
            if (data.code !== 200) {
                alert(data.message)
                return
            }
        }


        return {
            addart,
            insaddart

        }

    },
    {
        persist: {
            key: 'addart',
            storage: localStorage,
            pick: ["addart"]
        }
    }

)