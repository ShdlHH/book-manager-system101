import { defineStore } from 'pinia'
import { login as loginApi, getUserInfo, logout as logoutApi } from '../api/user'
import { getToken, setToken, removeToken } from '../utils/auth'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: getToken() || '',
    userInfo: null
  }),
  getters: {
    isAdmin: (state) => state.userInfo?.role === 1,
    username: (state) => state.userInfo?.nickname || state.userInfo?.username || ''
  },
  actions: {
    async login(username, password) {
      const data = await loginApi(username, password)
      this.token = data.token
      this.userInfo = data.user
      setToken(data.token)
      return data
    },
    async fetchUserInfo() {
      const data = await getUserInfo()
      this.userInfo = data
      return data
    },
    async logout() {
      try {
        await logoutApi()
      } catch (e) {
        // 忽略登出接口错误
      }
      this.token = ''
      this.userInfo = null
      removeToken()
    }
  }
})
