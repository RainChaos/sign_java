import request from '@/utils/request'

export function getExcelField(params) {
  return request({
    url: '/admin/importStudent/getExcelField',
    method: 'get',
    params
  })
}
export function setExcelMajorMap(params) {
  return request({
    url: '/admin/importStudent/setExcelMajorMap',
    method: 'get',
    params
  })
}

export function getStudentMapMajorLists(params) {
  return request({
    url: '/admin/studentMapMajor/lists',
    method: 'get',
    params
  })
}
export function updateStudentMapMajor(data) {
  return request({
    url: '/admin/studentMapMajor/update',
    method: 'post',
    data
  })
}
export function deleteStudentMapMajor(data) {
  return request({
    url: '/admin/studentMapMajor/delete',
    method: 'post',
    data
  })
}
export function getStudentMapFieldLists(params) {
  return request({
    url: '/admin/studentMapField/lists',
    method: 'get',
    params
  })
}
export function updateStudentMapField(data) {
  return request({
    url: '/admin/studentMapField/update',
    method: 'post',
    data
  })
}

export function deleteStudentMapField(data) {
  return request({
    url: '/admin/studentMapField/delete',
    method: 'post',
    data
  })
}
export function importStudentData(data) {
  return request({
    url: '/admin/importStudent/importStudentData',
    method: 'post',
    data
  })
}

