import request from '@/utils/request'

export function getOperatorList(params) {
  return request({
    url: '/admin/operator/lists',
    method: 'get',
    params
  })
}

export function updateStatus(id, status) {
  return request({
    url: '/admin/operator/updateStatus',
    method: 'post',
    data: {
      id,
      status
    }
  })
}

export function addOperator(data) {
  return request({
    url: '/admin/operator/create',
    method: 'post',
    data
  })
}

export function updateOperator(data) {
  return request({
    url: '/admin/operator/update',
    method: 'post',
    data
  })
}

export function deleteOperator(params) {
  return request({
    url: '/admin/operator/delete',
    method: 'post',
    params
  })
}

export function assignAuthGroup(data) {
  return request({
    url: '/admin/operator/assignGroup',
    method: 'post',
    data
  })
}

export function getAuthRulesTree(data) {
  return request({
    url: '/admin/operator/getAuthRulesTree',
    method: 'post',
    data
  })
}

export function assignOperatorRules(data) {
  return request({
    url: '/admin/operator/assignRules',
    method: 'post',
    data
  })
}
export function updatePassword(data) {
  return request({
    url: '/admin/operator/updatePassword',
    method: 'post',
    data
  })
}
export function getOperatorInfo(params) {
  return request({
    url: '/admin/operator/getOperatorInfo',
    method: 'get',
    params
  })
}
