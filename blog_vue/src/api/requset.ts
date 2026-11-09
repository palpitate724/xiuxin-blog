import axios from 'axios'

const api = import.meta.env.VITE_API
console.log('api', import.meta.env.VITE_API)

export const http = axios.create({
    baseURL: api,
    timeout: 5000
})