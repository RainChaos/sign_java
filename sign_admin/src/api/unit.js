import request from '@/utils/request'

export function getStructureLists(params) {
  return request({
    url: '/admin/schoolStructure/getlists',
    method: 'get',
    params
  })
}export function getClassList(params) {
  return request({
    url: '/admin/schoolStructure/getClassList',
    method: 'get',
    params
  })
}

export function getListsLimitByOperatorClass(params) {
  return request({
    url: '/admin/schoolStructure/getListsLimitByOperatorClass',
    method: 'get',
    params
  })
}
export function getStructureOnlyYxZyLists(params) {
  return request({
    url: '/admin/schoolStructure/getStructureOnlyYxZyLists',
    method: 'get',
    params
  })
}

export function getClassLists(params) {
  return request({
    url: '/admin/schoolStructure/getClassLists',
    method: 'get',
    params
  })
}

export function SyncCharges(params) {
  return request({
    url: '/admin/schoolStructure/SyncCharges',
    method: 'get',
    params
  })
}

export function getNextUnitCode(params) {
  return request({
    url: '/admin/schoolStructure/getNextUnitCode',
    method: 'get',
    params
  })
}

export function saveUnit(data) {
  return request({
    url: '/admin/schoolStructure/saveUnit',
    method: 'post',
    data
  })
}

export function deleteUnit(data) {
  return request({
    url: '/admin/schoolStructure/delete',
    method: 'post',
    data
  })
}
export function getAllLists(data) {
  return request({
    url: '/admin/schoolStructure/getAllLists',
    method: 'post',
    data
  })
}
export function getChildsCount(data) {
  return request({
    url: '/admin/schoolStructure/getChildsCount',
    method: 'post',
    data
  })
}
