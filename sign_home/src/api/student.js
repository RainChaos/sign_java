import request from '@/utils/request'

export function getFormConfigLists(params) {
  return request({
    url: '/index/student/getAddFormConfigLists',
    method: 'get',
    params
  })
}export function getEditFormConfigLists(params) {
  return request({
    url: '/index/student/getEditFormConfigLists',
    method: 'get',
    params
  })
}

export function getStudentInfo(params) {
  return request({
    url: '/index/student/getStudentInfo',
    method: 'get',
    params
  })
}
export function searchStudentInfo(params) {
  return request({
    url: '/index/student/searchStudentInfo',
    method: 'get',
    params
  })
}
export function getShowChargeItems(params) {
  return request({
    url: '/index/student/getShowChargeItems',
    method: 'get',
    params
  })
}
export function getStudentChargeItems(params) {
  return request({
    url: '/index/student/getStudentChargeItems',
    method: 'get',
    params
  })
}

export function addStudentInfo(data) {
  return request({
    url: '/index/student/addStudentInfo',
    method: 'post',
    data
  })
}

export function editStudentInfo(data) {
  return request({
    url: '/index/student/editStudentInfo',
    method: 'post',
    data
  })
}
export function editStudentInfoForLDYBZ(data) {
  return request({
    url: '/index/student/editStudentInfoForLDYBZ',
    method: 'post',
    data
  })
}
