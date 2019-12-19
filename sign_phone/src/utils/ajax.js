import $ from 'jquery'
import { getToken } from '@/utils/auth'
export function ajax(config) {
  $.ajax({
    url: process.env.BASE_API + config.url,
    type: config.type === undefined ? 'POST' : config.type, // GET
    async: config.async === undefined ? true : config.async, // 或false,是否异步
    xhrFields: {
      withCredentials: true
    },
    crossDomain: true,
    headers: {
      'Home-Sign-Token': getToken()
    },
    data: config.data === undefined ? {} : config.data,
    timeout: config.timeout === undefined ? 20000 : config.timeout, // 超时时间
    dataType: 'json', // 返回的数据格式：
    success: config.success
  })
}
