import request from '@/utils/request'

export function getArticleCategoryTree(params) {
  return request({
    url: '/admin/articleCategory/getArticleCategoryTree',
    method: 'get',
    params
  })
}

export function getArticleCategoryLists(params) {
  return request({
    url: '/admin/articleCategory/listArticleCategoryByFid',
    method: 'get',
    params
  })
}

export function addArticleCategory(data) {
  return request({
    url: '/admin/articleCategory/insert',
    method: 'post',
    data: data
  })
}

export function updateArticleCategory(data) {
  return request({
    url: '/admin/articleCategory/update',
    method: 'post',
    data: data
  })
}

export function deleteArticleCategory(params) {
  return request({
    url: '/admin/articleCategory/delete',
    method: 'post',
    data: params
  })
}
