import request from '@/utils/request'

export function getMiddleSchoolLists(params) {
  return request({
    url: '/admin/middleSchool/listSchoolByRegion',
    method: 'get',
    params
  })
}

export function getAllMiddleSchoolLists(params) {
  return request({
    url: '/admin/middleSchool/getAllLists',
    method: 'get',
    params
  })
}

export function addMiddleSchool(data) {
  return request({
    url: '/admin/middleSchool/insert',
    method: 'post',
    data: data
  })
}

export function updateMiddleSchool(data) {
  return request({
    url: '/admin/middleSchool/update',
    method: 'post',
    data: data
  })
}

export function deleteMiddleSchool(data) {
  return request({
    url: '/admin/middleSchool/delete',
    method: 'post',
    data: data
  })
}

export function setOrderSortAll(params) {
  return request({
    url: '/admin/middleSchool/setOrderSortAll',
    method: 'get',
    params
  })
}

