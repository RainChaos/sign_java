import request from '@/utils/request'

export function getAuthRuleLists() {
  return request({
    url: '/admin/authRule/lists',
    method: 'post'
  })
}

export function getAuthRuleParentList() {
  return request({
    url: '/admin/authRule/parent/list',
    method: 'post'
  })
}

export function addAuthRule(data) {
  return request({
    url: '/admin/authRule/create',
    method: 'post',
    data: data
  })
}

export function updateAuthRule(data) {
  return request({
    url: '/admin/authRule/update',
    method: 'post',
    data: data
  })
}

export function deleteAuthRule(data) {
  return request({
    url: '/admin/authRule/delete',
    method: 'post',
    data: data
  })
}

export function getAuthRuleSort(data) {
  return request({
    url: '/admin/authRule/getAuthRuleSort',
    method: 'post',
    data: data
  })
}

export function getAuthRuleInterface(params) {
  return request({
    url: '/admin/authRule/getAuthRuleInterface',
    method: 'get',
    params
  })
}

export function addAuthRuleInterface(data) {
  return request({
    url: '/admin/authRule/addAuthRuleInterface',
    method: 'post',
    data: data
  })
}

export function deleteAuthRuleInterface(data) {
  return request({
    url: '/admin/authRule/deleteAuthRuleInterface',
    method: 'post',
    data: data
  })
}

export function updateAuthRuleInterface(data) {
  return request({
    url: '/admin/authRule/updateAuthRuleInterface',
    method: 'post',
    data: data
  })
}








