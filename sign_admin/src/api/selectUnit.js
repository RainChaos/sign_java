import request from '@/utils/request'

export function getSelectUnitLists(params) {
  return request({
    url: '/admin/selectUnit/lists',
    method: 'get',
    params
  })
}

export function addSelectUnitFromStruture(data) {
  return request({
    url: '/admin/selectUnit/addSelectUnitFromStructure',
    method: 'post',
    data
  })
}

export function deleteSelectUnitFromStruture(data) {
  return request({
    url: '/admin/selectUnit/deleteSelectUnitFromStructure',
    method: 'post',
    data
  })
}

export function updateSelectUnit(data) {
  return request({
    url: '/admin/selectUnit/update',
    method: 'post',
    data
  })
}

export function deleteSelectUnit(data) {
  return request({
    url: '/admin/selectUnit/delete',
    method: 'post',
    data
  })
}

export function getCheckedKeys(params) {
  return request({
    url: '/admin/selectUnit/getCheckedKeys',
    method: 'get',
    params
  })
}

export function getStudentEducation() {
  return request({
    url: '/admin/student/education/get',
    method: 'get'
  })
}
