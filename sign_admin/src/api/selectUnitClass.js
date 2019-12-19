import request from '@/utils/request'

export function getSelectUnitClassLists(params) {
  return request({
    url: '/admin/selectUnitClass/lists',
    method: 'get',
    params
  })
}

export function addSelectUnitClass(data) {
  return request({
    url: '/admin/selectUnitClass/create',
    method: 'post',
    data
  })
}

export function updateSelectUnitClass(data) {
  return request({
    url: '/admin/selectUnitClass/update',
    method: 'post',
    data
  })
}

export function deleteSelectUnitClass(data) {
  return request({
    url: '/admin/selectUnitClass/delete',
    method: 'post',
    data
  })
}

