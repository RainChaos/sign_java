import request from '@/utils/request'

export function getShowChargeItemLists(params) {
  return request({
    url: '/admin/chargeitem/getChargeItemList',
    method: 'get',
    params
  })
}export function syncChargeItem() {
  return request({
    url: '/admin/chargeitem/syncChargeItem',
    method: 'get',
  })
}

export function addShowChargeItem(data) {
  return request({
    url: '/admin/chargeitem/insert',
    method: 'post',
    data: data
  })
}

export function updateShowChargeItem(data) {
  return request({
    url: '/admin/chargeitem/update',
    method: 'post',
    data: data
  })
}

export function deleteShowChargeItem(ids) {
  return request({
    url: '/admin/chargeitem/delete',
    method: 'post',
    data: ids
  })
}
