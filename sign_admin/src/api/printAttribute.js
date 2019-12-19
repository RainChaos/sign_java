import request from '@/utils/request'

export function getPrintAttributeLists(params) {
  return request({
    url: '/admin/printAttribute/lists',
    method: 'get',
    params
  })
}

export function addPrintAttribute(data) {
  return request({
    url: '/admin/printAttribute/create',
    method: 'post',
    data: data
  })
}

export function updatePrintAttribute(data) {
  return request({
    url: '/admin/printAttribute/update',
    method: 'post',
    data: data
  })
}

export function deletePrintAttribute(ids) {
  return request({
    url: '/admin/printAttribute/delete',
    method: 'post',
    data: ids
  })
}
