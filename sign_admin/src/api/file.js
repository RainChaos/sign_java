import request from '@/utils/request'


export function deleteFile(data) {
  return request({
    url: '/admin/File/delete',
    method: 'post',
    data
  })
}
