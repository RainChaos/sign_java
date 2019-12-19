import request from '@/utils/request'

export function getIp() {
  return request({
    url: '/index/charge/getIp',
    method: 'get'
  })
}

export function getOpenId() {
  return request({
    url: '/index/phone/getOpenId',
    method: 'get'
  })
}

export function SubMoneyByItems(data) {
  return request({
    url: '/index/charge/SubMoneyByItems',
    method: 'post',
    data
  })
}

export function getChargeImplStatus(data) {
    return request({
        url: '/index/charge/getChargeImplStatus',
        method: 'get',
        data
    })
}