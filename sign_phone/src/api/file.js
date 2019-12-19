import request from '@/utils/request'


export function deleteFile(data) {
  return request({
    url: '/index/File/delete',
    method: 'post',
    data
  })
}
