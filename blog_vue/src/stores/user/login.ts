import { ref } from 'vue'
import { defineStore } from 'pinia'

export const loginpage = defineStore('loginpage',
    () => {
        const name = ref('')
        const password = ref('')

        return {
            name,
            password,
        }
    },
    {
        persist: {
            key: 'login',
            storage: localStorage,
            pick: ['name'],
        }
    }
)
