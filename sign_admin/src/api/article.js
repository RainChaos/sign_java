import request from '@/utils/request'

export function getArticleLists(params) {
  return request({
    url: '/admin/article/getArticleList',
    method: 'get',
    params
  })
}

export function addArticle(data) {
  return request({
    url: '/admin/article/insert',
    method: 'post',
    data
  })
}

export function updateArticle(data) {
  return request({
    url: '/admin/article/update',
    method: 'post',
    data
  })
}

export function deleteArticle(data) {
  return request({
    url: '/admin/article/delete',
    method: 'post',
    data
  })
}

export function getOneArticle(data) {
  return request({
    url: '/admin/article/getOneArticle',
    method: 'post',
    data
  })
}

