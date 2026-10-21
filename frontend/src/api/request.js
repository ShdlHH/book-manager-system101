import axios from 'axios'
import { message } from '../utils/globalMessage'
import { getToken, removeToken } from '../utils/auth'
import router from '../router'

const service = axios.create({
  baseURL: '/api',
  timeout: 10000
})

service.interceptors.request.use(
  (config) => {
    const token = getToken()
    if (token) {
      config.headers['Authorization'] = 'Bearer ' + token
    }
    return config
  },
  (error) => Promise.reject(error)
)

service.interceptors.response.use(
  (response) => {
    const res = response.data
    if (res.code === 200) {
      return res.data
    }
    if (res.code === 401) {
      removeToken()
      message.error(res.msg || '登录已过期，请重新登录')
      router.push('/login')
      return Promise.reject(new Error(res.msg))
    }
    message.error(res.msg || '请求失败')
    return Promise.reject(new Error(res.msg))
  },
  (error) => {
    message.error(error.message || '网络异常')
    return Promise.reject(error)
  }
)

export default service
