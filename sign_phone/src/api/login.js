import adminRequest from '@/utils/adminRequest'

export function login(username, password) {
  return adminRequest({
    url: '/admin/login/dologin',
    method: 'post',
    data: {
      username,
      password
    }
  })
}

export function getInfo(token) {
  return adminRequest({
    url: '/admin/operator/getInfo',
    method: 'get',
    params: { token }
  })
}

export function logout() {
  return adminRequest({
    url: '/admin/login/logout',
    method: 'post'
  })
}
