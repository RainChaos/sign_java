import request from '@/utils/request'

export function getRegionLists(params) {
  return request({
    url: '/index/region/listByFatherCode',
    method: 'get',
    params
  })
}
