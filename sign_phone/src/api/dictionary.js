import request from '@/utils/request'

export function getListsByCode(params) {
  return request({
    url: '/index/dictionary/listDictionaryByType',
    method: 'get',
    params
  })
}
