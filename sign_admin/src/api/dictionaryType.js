import request from '@/utils/request'

export function getDictionaryLists(params) {
  return request({
    url: '/admin/dict/listForPid',
    method: 'get',
    params
  })
}

export function getDictionaryType(params) {
  return request({
    url: '/admin/dict/getDictionaryTypeList',
    method: 'get',
    params
  })
}

export function addDictionaryType(data) {
  return request({
    url: '/admin/dict/insert',
    method: 'post',
    data: data
  })
}

export function updateDictionaryType(data) {
  return request({
    url: '/admin/dict/update',
    method: 'post',
    data: data
  })
}

export function deleteDictionaryType(ids) {
  return request({
    url: '/admin/dict/delete',
    method: 'post',
    data: ids
  })
}
