import request from '@/utils/request'

export function getStudentFieldLists(params) {
  return request({
    url: '/admin/studentField/lists',
    method: 'get',
    params
  })
}

export function addStudentField(data) {
  return request({
    url: '/admin/studentField/create',
    method: 'post',
    data
  })
}
export function updateStudentField(data) {
  return request({
    url: '/admin/studentField/update',
    method: 'post',
    data
  })
}

export function deleteStudentField(data) {
  return request({
    url: '/admin/studentField/delete',
    method: 'post',
    data
  })
}
