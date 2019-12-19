import request from '@/utils/request'

export function getSelectUnitLists(params) {
  return request({
    url: '/index/schoolStructure/getSelectUnitList',
    method: 'get',
    params
  })
}

export function getSelectUnitCount(params) {
  return request({
    url: '/index/selectUnit/getSelectUnitCount',
    method: 'get',
    params
  })
}
