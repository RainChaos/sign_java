import request from '@/utils/request'

export function getDictionaryLists(params) {
  return request({
    url: '/admin/dictionary/listDictionaryByType',
    method: 'get',
    params
  })
}

export function getUseDictionaryLists(params) {
  return request({
    url: '/admin/dictionary/listDictionaryByType',
    method: 'get',
    params
  })
}

export function addDictionary(data) {
  return request({
    url: '/admin/dictionary/insert',
    method: 'post',
    data: data
  })
}

export function updateDictionary(data) {
  return request({
    url: '/admin/dictionary/update',
    method: 'post',
    data: data
  })
}

export function deleteDictionary(data) {
  return request({
    url: '/admin/dictionary/delete',
    method: 'post',
    data: data
  })
}


export function setDefault(data) {
  return request({
    url: '/admin/dictionary/setDefault',
    method: 'post',
    data: data
  })
}

export function updateStatus(data) {
  return request({
    url: '/admin/dictionary/updateStatus',
    method: 'post',
    data
  })
}

export function setOrderSortAll(params) {
  return request({
    url: '/admin/dictionary/setOrderSortAll',
    method: 'get',
    params
  })
}
