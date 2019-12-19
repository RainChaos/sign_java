import request from '@/utils/request'

export function getConfigLists(params) {
  return request({
    url: '/admin/config/lists',
    method: 'get',
    params
  })
}

export function addConfig(data) {
  return request({
    url: '/admin/config/create',
    method: 'post',
    data: data
  })
}

export function updateConfig(data) {
  return request({
    url: '/admin/Config/update',
    method: 'post',
    data: data
  })
}

export function deleteConfig(data) {
  return request({
    url: '/admin/config/delete',
    method: 'post',
    data: data
  })
}

export function getConfigGroup(data) {
  return request({
    url: '/admin/config/getConfigGroup',
    method: 'post',
    data: data
  })
}

export function getConfigData(data) {
  return request({
    url: '/admin/config/getData',
    method: 'post',
    data: data
  })
}

// 获取配置类型
export function getConfigType(data) {
  return request({
    url: '/admin/config/getConfigType',
    method: 'post',
    data: data
  })
}

// 获取配置分组
export function getGroupLists(data) {
  return request({
    url: '/admin/config/getGroupLists',
    method: 'post',
    data: data
  })
}

// 保存配置
export function saveConfig(data) {
  return request({
    url: '/admin/config/saveConfig',
    method: 'post',
    data: data
  })
}

// 获取班级单位长度
export function getSchoolStructureLength() {
  return request({
    url: '/admin/config/getSchoolStructureLength',
    method: 'get'
  })
}

// 获取当前批次
export function getCurrentPici() {
  return request({
    url: '/admin/config/getCurrentPici',
    method: 'get'
  })
}

export function getLimitClassNumber() {
  return request({
    url: '/admin/config/getLimitClassNumber',
    method: 'get'
  })
}

export function getExpandedUnitCode() {
  return request({
    url: '/admin/config/getExpandedUnitCode',
    method: 'get'
  })
}

