import request from '@/utils/request'

export function getPiciLists(params) {
  return request({
    url: '/index/pici/lists',
    method: 'get',
    params
  })
}
