import request from '@/utils/request'

export function getRegionLists(params) {
  return request({
    url: '/admin/region/listByFatherCode',
    method: 'get',
    params
  })
}

export function addRegion(data) {
  return request({
    url: '/admin/region/insert',
    method: 'post',
    data: data
  })
}

export function updateRegion(data) {
  return request({
    url: '/admin/region/update',
    method: 'post',
    data: data
  })
}

export function deleteRegion(data) {
  return request({
    url: '/admin/region/delete',
    method: 'post',
    data: data
  })
}
export function setOrderSortAll(params) {
  return request({
    url: '/admin/region/setOrderSortAll',
    method: 'get',
    params
  })
}

export function getRegion(params) {
  return request({
    url: '/admin/region/get',
    method: 'get',
    params
  })
}
