import request from '@/utils/request'

// 按专业统计
export function countZyLists(params) {
  return request({
    url: '/admin/studentCount/countZyLists',
    method: 'get',
    params
  })
}

// 按城市
export function countChengshiLists(params) {
  return request({
    url: '/admin/studentCount/countChengshiLists',
    method: 'get',
    params
  })
}

// 按招生人
export function countZsrLists(params) {
  return request({
    url: '/admin/studentCount/countZsrLists',
    method: 'get',
    params
  })
}

// 按专业导出统计数据
export function exportZyLists(params) {
  return request({
    url: '/admin/studentCount/exportZyLists',
    method: 'get',
    params
  })
}

// 按城市导出统计数据
export function exportChengShiLists(params) {
  return request({
    url: '/admin/studentCount/exportChengShiLists',
    method: 'get',
    params
  })
}

// 按招生人导出统计数据
export function exportZsrLists(params) {
  return request({
    url: '/admin/studentCount/exportZsrLists',
    method: 'get',
    params
  })
}

// 按组导出统计数据
export function countGroup(params) {
  return request({
    url: '/admin/studentCount/countGroup',
    method: 'get',
    params
  })
}

//获取省市区的字段
export function getDataTypeIsSSQ() {
  return request({
    url: '/admin/getDataTypeIsSSQ',
    method: 'get'
  })
}

export function getFieldNameStatisticsConfig() {
  return request({
    url: '/admin/statistics/fieldName/config/get',
    method: 'get'
  })
}

export function countFieldNameList(params) {
  return request({
    url: '/admin/studentCount/countFieldNameList',
    method: 'get',
    params
  })
}

export function exportFieldNameStatistics(params) {
  return request({
    url: '/admin/studentCount/exportFieldNameStatistics',
    method: 'get',
    params
  })
}
