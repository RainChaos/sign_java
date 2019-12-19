import request from '@/utils/request'

export function importList(data) {
  return request({
    url: '/admin/restore/importList',
    method: 'get',
    data
  })
}

export function deleteBackData(data) {
  return request({
    url: '/admin/restore/delete',
    method: 'post',
    data
  })
}

export function importBackData(data) {
  return request({
    url: '/admin/restore/import',
    method: 'post',
    data
  })
}