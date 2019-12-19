import Cookies from 'js-cookie' //用来操作cookie的插件
import { getSchoolStructureLength, getCurrentPici } from '@/api/config'

const app = {
  state: {
    //侧边栏 定义了侧边栏的状态，打开或者关闭，以及是否需要动画。
    sidebar: {
      opened: !+Cookies.get('sidebarStatus'),
      withoutAnimation: false
    },
    device: 'desktop',
    structureLength: 18,  //班级单位长度
    currentPici: 0
  },
  //定义对应的操作mutation
  mutations: {
    TOGGLE_SIDEBAR: state => {
      if (state.sidebar.opened) {
        Cookies.set('sidebarStatus', 1)
      } else {
        Cookies.set('sidebarStatus', 0)
      }
      state.sidebar.opened = !state.sidebar.opened
      state.sidebar.withoutAnimation = false
    },
    CLOSE_SIDEBAR: (state, withoutAnimation) => {
      Cookies.set('sidebarStatus', 1)
      state.sidebar.opened = false
      state.sidebar.withoutAnimation = withoutAnimation
    },
    TOGGLE_DEVICE: (state, device) => {
      state.device = device
    },
    STRUCTURE_LENGTH: (state, length) => {
      state.structureLength = length
    },
    CURRENT_PICI: (state, CurrentPici) => {
      state.currentPici = CurrentPici
    }
  },
  //异步操作action。
  actions: {
    ToggleSideBar: ({ commit }) => {
      commit('TOGGLE_SIDEBAR')
    },
    CloseSideBar({ commit }, { withoutAnimation }) {
      commit('CLOSE_SIDEBAR', withoutAnimation)
    },
    ToggleDevice({ commit }, device) {
      commit('TOGGLE_DEVICE', device)
    },
    // 获取用户信息
    StructureLength({ commit, state }) {
      return new Promise((resolve, reject) => {
        getSchoolStructureLength().then(response => {
          const data = response.data
          commit('STRUCTURE_LENGTH', data)
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },
    // 获取用户信息
    CurrentPici({ commit, state }) {
      return new Promise((resolve, reject) => {
        getCurrentPici().then(response => {
          const data = response.data
          commit('CURRENT_PICI', data)
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    }
  }
}

export default app
