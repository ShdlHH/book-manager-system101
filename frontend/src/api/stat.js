import request from './request'

export function getDashboard() {
  return request.get('/stat/dashboard')
}

export function getBorrowTrend(days = 7) {
  return request.get('/stat/borrow-trend', { params: { days } })
}

export function getCategoryRatio() {
  return request.get('/stat/category-ratio')
}

export function getHotBooks() {
  return request.get('/stat/hot-books')
}
