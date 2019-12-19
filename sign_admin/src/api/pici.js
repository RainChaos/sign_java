import request from '@/utils/request'

export function getPiciLists(params) {
  return request({
    url: '/admin/pici/lists',
    method: 'get',
    params
  })
}

export function addPici(data) {
  return request({
    url: '/admin/pici/create',
    method: 'post',
    data: data
  })
}

export function updatePici(data) {
  return request({
    url: '/admin/pici/update',
    method: 'post',
    data: data
  })
}

export function deletePici(ids) {
  return request({
    url: '/admin/pici/delete',
    method: 'post',
    data: ids
  })
}

export function setOrderSortAll(params) {
  return request({
    url: '/admin/pici/setOrderSortAll',
    method: 'post',
    params
  })
}

