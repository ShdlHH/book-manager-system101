import request from './request'

export function borrowBook(bookId) {
  return request.post('/borrow', { bookId })
}

export function returnBook(id) {
  return request.put(`/borrow/${id}/return`)
}

export function renewBook(id) {
  return request.put(`/borrow/${id}/renew`)
}

export function getMyBorrowList() {
  return request.get('/borrow/my-list')
}

export function getBorrowPage(params) {
  return request.get('/borrow/page', { params })
}

export function getOverdueList(params) {
  return request.get('/borrow/overdue', { params })
}
