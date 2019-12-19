import request from '@/utils/request'

export function getStudentFieldLists(params) {
  return request({
    url: '/admin/field/student/getStudentFieldListByKey',
    method: 'get',
    params
  })
}
export function getStudentFieldListBySearch(params) {
  return request({
    url: '/admin/field/student/getStudentFieldListBySearch',
    method: 'get',
    params
  })
}
export function addStudentField(data) {
  return request({
    url: '/admin/field/student/insert',
    method: 'post',
    data
  })
}
export function updateStudentField(data) {
  return request({
    url: '/admin/field/student/update',
    method: 'post',
    data
  })
}

export function deleteStudentField(data) {
  return request({
    url: '/admin/field/student/delete',
    method: 'post',
    data
  })
}
