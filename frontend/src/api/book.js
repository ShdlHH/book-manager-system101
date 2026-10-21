import request from './request'

export function getBookPage(params) {
  return request.get('/book/page', { params })
}

export function getBookDetail(id) {
  return request.get(`/book/${id}`)
}

export function addBook(data) {
  return request.post('/book', data)
}

export function updateBook(id, data) {
  return request.put(`/book/${id}`, data)
}

export function deleteBook(id) {
  return request.delete(`/book/${id}`)
}

export function updateBookStatus(id, status) {
  return request.put(`/book/${id}/status`, { status })
}
