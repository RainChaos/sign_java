import axios from 'axios'
import { AlertModule } from 'vux'
import { Confirm } from 'vux'
import store from '../store'
import { getToken } from '@/utils/auth'

// 创建axios实例
const service = axios.create({
  baseURL: process.env.BASE_API, // api 的 base_url
  timeout: 30000 // 请求超时时间
})

// request拦截器
service.interceptors.request.use(
  config => {
    if (store.getters.token) {
      config.headers['Admin-Sign-Token'] = getToken() // 让每个请求携带自定义token 请根据实际情况自行修改
      config.headers['Current-Url'] = 'teacher'
    }
    return config
  },
  error => {
    Promise.reject(error)
  }
)

// response 拦截器
service.interceptors.response.use(
  response => {
    /**
     * code为非20000是抛错 可结合自己业务进行修改
     */
    var res = response.data
    var typeData = typeof res

    if (typeData === 'string') {
      res = eval('(' + res + ')')
    }
    if (res.code !== 20000) {
      // 50008:非法的token; 50012:其他客户端登录了;  50014:Token 过期了;
      if (res.code === 50008 || res.code === 50012 || res.code === 50014) {
        AlertModule.show({
          title:'错误提示',
          content: res.msg
        })
      }
      return Promise.reject('error')
    } else if (!res.success) {
      AlertModule.show({
        title: '错误提示',
        content: res.msg
      })
      return Promise.reject('error')
    } else {
      return res
    }
  },
  error => {
    AlertModule.show({
      title: '错误提示',
      content: error.msg,
    })
    return Promise.reject(error)
  }
)

export default service
