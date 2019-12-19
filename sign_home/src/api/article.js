import request from '@/utils/request'

export function getArticleByTitle(params) {
  return request({
    url: '/index/article/getArticleByTitle',
    method: 'get',
    params
  })
}

export function getArticlesByCate(params) {
  return request({
    url: '/index/article/getArticlesByCate',
    method: 'get',
    params
  })
}
export function getArticleById(params) {
  return request({
    url: '/index/article/getArticleById',
    method: 'get',
    params
  })
}
