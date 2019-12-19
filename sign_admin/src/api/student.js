import request from '@/utils/request'

export function getStudentLists(params) {
  return request({
    url: '/admin/student/lists',
    method: 'get',
    params
  })
}

export function getSearchField(params) {
  return request({
    url: '/admin/student/getSearchField',
    method: 'get',
    params
  })
}

export function addStudentInfo(data) {
  return request({
    url: '/admin/student/addStudentInfo',
    method: 'post',
    data
  })
}

export function editStudentInfo(data) {
  return request({
    url: '/admin/student/editStudentInfo',
    method: 'post',
    data
  })
}
export function editStudentInfoForLDYBZ(data) {
  return request({
    url: '/admin/student/editStudentInfoForLDYBZ',
    method: 'post',
    data
  })
}
export function changeUnitCode(data) {
  return request({
    url: '/admin/student/changeUnitCode',
    method: 'post',
    data
  })
}

export function changeStudentStatus(data) {
  return request({
    url: '/admin/student/changeStudentStatus',
    method: 'post',
    data
  })
}
export function deleteStudentInfo(data) {
  return request({
    url: '/admin/student/deleteStudentInfo',
    method: 'post',
    data
  })
}

export function getAddFormConfigLists(params) {
  return request({
    url: '/admin/student/getAddFormConfigLists',
    method: 'get',
    params
  })
}

export function getEditFormConfigLists(params) {
  return request({
    url: '/admin/student/getEditFormConfigLists',
    method: 'get',
    params
  })
}

export function exportStudentLists(params) {
  return request({
    url: '/admin/student/exportStudentLists',
    method: 'get',
    params
  })
}
export function printTzs(params) {
  return request({
    url: '/admin/student/printTzs',
    method: 'get',
    params
  })
}

export function setIsPrintTzs(params) {
  return request({
    url: '/admin/student/setIsPrintTzs',
    method: 'post',
    params
  })
}

export function getStudentInfo(params) {
  return request({
    url: '/admin/student/getStudentInfo',
    method: 'get',
    params
  })
}

export function getStudentChargeItems(params) {
  return request({
    url: '/admin/student/getStudentChargeItems',
    method: 'get',
    params
  })
}

export function setRegister(data) {
  return request({
    url: '/admin/student/setRegister',
    method: 'post',
    data
  })
}

export function autoFenBan(data) {
  return request({
    url: '/admin/student/autoFenBan',
    method: 'post',
    data
  })
}

export function exportTzsStudentLists(params) {
  return request({
    url: '/admin/student/exportStudentLists',
    method: 'get',
    params
  })
}
