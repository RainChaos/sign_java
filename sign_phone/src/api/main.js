import request from '@/utils/request'

export function getWebSiteName() {
  return request({
    url: '/index/config/getWebSiteName',
    method: 'get'
  })
}

export function getAdmissionsProfile() {
  return request({
    url: '/index/config/getAdmissionsProfile',
    method: 'get'
  })
}
export function getBGIMG() {
  return request({
    url: '/index/config/getBGIMG',
    method: 'get'
  })
}
export function getWebSiteAddress() {
  return request({
    url: '/index/config/getWebSiteAddress',
    method: 'get'
  })
}
