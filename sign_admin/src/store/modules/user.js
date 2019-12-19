import { login, logout, getInfo, sso } from '@/api/login'
import { getToken, setToken, removeToken } from '@/utils/auth'
// import VueRouter from 'vue-router'
// import router from './router/router'

const user = {
  state: {
    token: getToken(),
    username: '',
    rules: '',
    web_site_name: '',
    logo_image_url: ''
  },

  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_USERNAME: (state, username) => {
      state.username = username
    },
    SET_RULES: (state, rules) => {
      state.rules = rules
    },
    SET_WEB_SITE_NAME: (state, web_site_name) => {
      state.web_site_name = web_site_name
    },
    SET_LOGO_URL: (state, logo_url) => {
      state.logo_image_url = logo_url
    }
  },
  actions: {
    // 登录
    Login({ commit }, userInfo) {
      const username = userInfo.username.trim()
      return new Promise((resolve, reject) => {
        login(username, userInfo.password).then(response => {
          const data = response.data
          setToken(data.token) // 登录成功后将token 放入cookie中
          commit('SET_TOKEN', data.token) //对全局token做出修改
         //返回一个解析过带着给定值的Promise对象
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },
    sso({ commit }, userInfo) {
      console.log(userInfo);
      return new Promise((resolve, reject) => {
        sso(userInfo).then(response => {
          const data = response.data
          setToken(data.token)
          commit('SET_TOKEN', data.token)
          resolve(response)
        }).catch(response => {
          // console.log(123)
          // reject(error)
          // this.$router.push({path:'/login'})
          // console.log("跳转")
          resolve(response)
        })
      })
    },
    // 获取用户信息
    GetInfo({ commit, state }) {
      return new Promise((resolve, reject) => {
        getInfo(state.token).then(response => {
          const data = response.data
          console.log(data)
          commit('SET_USERNAME', data.username)
          commit('SET_RULES', data.rules)
          commit('SET_WEB_SITE_NAME', data.webSiteName)
          commit('SET_LOGO_URL', data.logoUrl)
          document.title = data.webSiteName + '-后台管理'
          resolve(response)
        }).catch(error => {
          // reject(error)
        })
      })
    },
    // 登出
    LogOut({ commit, state }) {
      return new Promise((resolve, reject) => {
        // logout(state.token).then(() => {
          commit('SET_TOKEN', '')
          removeToken()
          resolve()
        // }).catch(error => {
        //   reject(error)
        // })
      })
    },

    // 前端 登出
    FedLogOut({ commit }) {
      return new Promise(resolve => {
        commit('SET_TOKEN', '')
        removeToken()
        resolve()
      })
    }
  }
}

export default user
