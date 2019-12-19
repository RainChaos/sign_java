import adminRequest from '@/utils/adminRequest'

// 按专业统计
export function countZyLists(params) {
  return adminRequest({
    url: '/admin/main/getCountZyLists',
    method: 'get',
    params
  })
}

// 按专业统计
export function countBanjiLists(params) {
  return adminRequest({
    url: '/admin/main/getCountClassLists',
    method: 'get',
    params
  })
}

// 获取拥有权限的班级
export function getBanjiLists(params) {
    return adminRequest({
        url: '/admin/studentCount/getBanjiLists',
        method: 'get',
        params
    })
}

// 按城市
export function countChengshiLists(params) {
  return adminRequest({
    url: '/admin/studentCount/countChengshiLists',
    method: 'get',
    params
  })
}

// 按招生人
export function countZsrLists(params) {
  return adminRequest({
    url: '/admin/studentCount/getCountZsrLists',
    method: 'get',
    params
  })
}


// 按系统计
export function countXiLists(params) {
    return adminRequest({
        url: '/admin/main/getCountXiLists',
        method: 'get',
        params
    })
}
