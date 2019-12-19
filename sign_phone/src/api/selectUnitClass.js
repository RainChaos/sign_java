import request from '@/utils/request'

export function getClass(params) {
  return request({
    url: '/index/selectUnitClass/getUnitClass',
    method: 'get',
    params
  })
}