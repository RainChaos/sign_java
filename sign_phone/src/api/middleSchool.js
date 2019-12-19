import request from '@/utils/request'


export function getAllMiddleSchoolLists(params) {
  return request({
    url: '/index/middleSchool/listSchoolByRegionNoPage',
    method: 'get',
    params
  })
}
