import request from '@/utils/request'

export function getAuthGroupLists(params) {
  return request({
    url: '/admin/authGroup/lists',
    method: 'get',
    params
  })
}

export function updateStatus(id, status) {
  return request({
    url: '/admin/authGroup/updateStatus',
    method: 'post',
    data: {
      id,
      status
    }
  })
}

export function addAuthGroup(data) {
  return request({
    url: '/admin/authGroup/create',
    method: 'post',
    data: data
  })
}

export function updateAuthGroup(data) {
  return request({
    url: '/admin/authGroup/update',
    method: 'post',
    data: data
  })
}

export function deleteAuthGroup(data) {
  return request({
    url: '/admin/authGroup/delete',
    method: 'post',
    data: data
  })
}

export function getAuthRulesTree(data) {
  return request({
    url: '/admin/authGroup/getAuthRulesTree',
    method: 'post',
    data: data
  })
}

export function assignGroupRules(data) {
  return request({
    url: '/admin/authGroup/assignRules',
    method: 'post',
    data: data
  })
}
