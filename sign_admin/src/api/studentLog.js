import request from '@/utils/request'

export function getStudentLogLists(params) {
  return request({
    url: '/admin/studentLog/lists',
    method: 'get',
    params
  })
}
