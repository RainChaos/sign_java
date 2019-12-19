import request from '@/utils/request'

export function getStudentSearchFieldLists(params) {
  return request({
    url: '/admin/field/grid/getStudentSearchFieldList',
    method: 'get',
    params
  })
}

export function addStudentSearchField(data) {
  return request({
    url: '/admin/field/grid/searchInsert',
    method: 'post',
    data: data
  })
}

export function updateStudentSearchField(data) {
  return request({
    url: '/admin/field/grid/searchUpdate',
    method: 'post',
    data: data
  })
}

export function deleteStudentSearchField(ids) {
  return request({
    url: '/admin/field/grid/searchDelete',
    method: 'post',
    data: ids
  })
}
