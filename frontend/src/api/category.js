import request from './request'

export function getCategoryList() {
  return request.get('/category/list')
}

export function getCategoryPage(params) {
  return request.get('/category/page', { params })
}

export function addCategory(data) {
  return request.post('/category', data)
}

export function updateCategory(id, data) {
  return request.put(`/category/${id}`, data)
}

export function deleteCategory(id) {
  return request.delete(`/category/${id}`)
}
