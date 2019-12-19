import request from '@/utils/request'

export function inBedAuto(data) {
  return request({
    url: '/admin/dor/inBedAuto',
    method: 'post',
    data
  })
}

export function outBed(data) {
  return request({
    url: '/admin/dor/outBed',
    method: 'post',
    data
  })
}

export function addStuOut(data) {
  return request({
    url: '/admin/dor/addStuOut',
    method: 'post',
    data
  })
}