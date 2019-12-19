import request from '@/utils/request'

export function getActionLogLists(params) {
  return request({
    url: '/admin/actionLog/lists',
    method: 'get',
    params
  })
}
