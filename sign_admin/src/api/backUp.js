import request from '@/utils/request'

export function exportList(data) {
  return request({
    url: '/admin/backup/exportList',
    method: 'get',
    data
  })
}

export function exportBackPost(data) {
  return request({
    url: '/admin/backup/export',
    method: 'post',
    data
  })
}

export function exportBackGet(params) {
  return request({
    url: '/admin/backup/export',
    method: 'get',
    params
  })
}
export function Optimize(data) {
  return request({
    url: '/admin/backup/optimize',
    method: 'post',
    data
  })
}

export function Repair(data) {
  return request({
    url: '/admin/backup/repair',
    method: 'post',
    data
  })
}

export function BackupXml(data) {
  return request({
    url: '/admin/backup/backupxml',
    method: 'post',
    data
  })
}

export function RestoreXml(data) {
  return request({
    url: '/admin/backup/RestoreXml',
    method: 'post',
    data
  })
}

export function signBackUp() {
  return request({
    url: '/admin/sign/backUp',
    method: 'get',
  })
}

export function getBackList(params) {
  return request({
    url: '/admin/backUp/get',
    method: 'get',
    params
  })
}

export function downloadBackup(data) {
  return request({
    url: '/admin/backup',
    method: 'post',
    data,
    responseType: 'blob'
  })
}


