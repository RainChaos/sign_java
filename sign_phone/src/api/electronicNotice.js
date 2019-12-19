import request from '@/utils/request'

export function getElectronicNotice(params) {
    console.log(params)
    return request({
        url: '/index/student/getNotice',
        method: 'get',
        params
    })
}
