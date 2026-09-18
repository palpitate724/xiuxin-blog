import { defineStore } from 'pinia'

export const art = defineStore('art',
    () => {


        


    },
    {
        persist: {
            key: 'art',
            storage: localStorage,
            pick: ["art"]
        }
    }
)