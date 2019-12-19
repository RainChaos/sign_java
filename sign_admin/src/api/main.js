import request from '@/utils/request'

export function getServerInfo() {
  return request({
    url: '/admin/main/getServerInfo',
    method: 'get'
  })
}

export function getCountStudent(params) {
  return request({
    url: '/admin/main/getCountStudent',
    method: 'get',
    params
  })
}
export function getCountZyLists(params) {
  return request({
    url: '/admin/main/getCountZyLists',
    method: 'get',
    params
  })
}
export function getCountClassLists(params) {
  return request({
    url: '/admin/main/getCountClassLists',
    method: 'get',
    params
  })
}
export function getCountXiLists(params) {
    return request({
        url: '/admin/main/getCountXiLists',
        method: 'get',
        params
    })
}

export function getConfigData() {
  return request({
    url: '/admin/main/getConfigData',
    method: 'get'
  })
}
