import request from '@/utils/request'

export function getStudentFormFieldLists(params) {
  return request({
    url: '/admin/field/form/getFormFieldBySearch',
    method: 'get',
    params
  })
}

export function addStudentFormField(data) {
  return request({
    url: '/admin/field/form/insert',
    method: 'post',
    data
  })
}
export function updateStudentFormField(data) {
  return request({
    url: '/admin/field/form/update',
    method: 'post',
    data
  })
}

export function updateStudentFormFieldWidth(data) {
  return request({
    url: '/admin/field/form/updateStudentFormFieldWidth',
    method: 'post',
    data
  })
}
export function updateStudentFormFieldWidthV2(data) {
  return request({
    url: '/admin/field/form/updateStudentFormFieldWidthV2',
    method: 'post',
    data
  })
}
export function deleteStudentFormField(data) {
  return request({
    url: '/admin/field/form/delete',
    method: 'post',
    data
  })
}
export function getFieldNameByFormName(params) {
  return request({
    url: '/admin/field/form/getFieldNameByFormName',
    method: 'get',
    params
  })
}
/* export function getFieldNameByFormNameForGrid(params) {
  return request({
    url: '/admin/field/form/getFieldNameByFormNameForGrid',
    method: 'get',
    params
  })
}*/
export function getFormFieldBySearch(params) {
  return request({
    url: '/admin/field/form/getFormFieldBySearch',
    method: 'get',
    params
  })
}

export function getFormConfigLists(params) {
  return request({
    url: '/admin/studentFormField/getFormConfigLists',
    method: 'get',
    params
  })
}

export function setOrderSortAll(params) {
  return request({
    url: '/admin/field/form/setOrderSortAll',
    method: 'post',
    params
  })
}

