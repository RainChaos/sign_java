import request from '@/utils/request'

export function getPrintTemplateLists(params) {
  return request({
    url: '/admin/printTemplate/lists',
    method: 'get',
    params
  })
}

export function addPrintTemplate(data) {
  return request({
    url: '/admin/printTemplate/create',
    method: 'post',
    data: data
  })
}

export function updatePrintTemplate(data) {
  return request({
    url: '/admin/printTemplate/update',
    method: 'post',
    data: data
  })
}

export function deletePrintTemplate(ids) {
  return request({
    url: '/admin/printTemplate/delete',
    method: 'post',
    data: ids
  })
}
