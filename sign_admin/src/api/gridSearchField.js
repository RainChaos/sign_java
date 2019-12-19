import request from '@/utils/request'

export function getGridSearchFieldLists(params) {
  return request({
    url: '/admin/field/grid/getGridSearchFieldList',
    method: 'get',
    params
  })
}

export function getFieldNameByTableName(params) {
  return request({
    url: '/admin/field/grid/getFieldNameByTableName',
    method: 'get',
    params
  })
}

export function addGridSearchField(data) {
  return request({
    url: '/admin/field/grid/insert',
    method: 'post',
    data
  })
}
export function updateGridSearchField(data) {
  return request({
    url: '/admin/field/grid/update',
    method: 'post',
    data
  })
}

export function deleteGridSearchField(data) {
  return request({
    url: '/admin/field/grid/delete',
    method: 'post',
    data
  })
}

export function setOrderSortAll(params) {
  return request({
    url: '/admin/field/grid/setOrderSortAll',
    method: 'post',
    params
  })
}

