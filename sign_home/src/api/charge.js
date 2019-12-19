import request from '@/utils/request'

export function getIp() {
  return request({
    url: '/index/charge/getIp',
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