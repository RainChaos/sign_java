import request from '@/utils/request'

export function login(username, password) {
  return request({
    url: '/admin/login/dologin',
    method: 'post',
    data: {
      username,
      password
    }
  })
}

export function getInfo(token) {
  return request({
    url: '/admin/operator/getInfo',
    method: 'get',
    params: { token }
  })
}

export function logout() {
  return request({
    url: '/admin/login/logout',
    method: 'post'
  })
}

export function sso(data) {
  return request({
    url: '/sso',
    method: 'post',
    data
  })
}
