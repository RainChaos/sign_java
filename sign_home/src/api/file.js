import request from '@/utils/request'


export function deleteFile(params) {
  return request({
    url: '/index/File/delete',
    method: 'get',
    params
  })
}
