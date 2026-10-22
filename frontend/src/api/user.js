import request from './request'

export function login(username, password) {
  return request.post('/user/login', { username, password })
}

export function register(username, password) {
  return request.post('/user/register', { username, password })
}

export function getUserInfo() {
  return request.get('/user/info')
}

export function updateUserInfo(data) {
  return request.put('/user/info', data)
}

export function logout() {
  return request.post('/user/logout')
}

export function getUserPage(params) {
  return request.get('/user/page', { params })
}

export function addUser(data) {
  return request.post('/user', data)
}

export function updateUserByAdmin(id, data) {
  return request.put(`/user/${id}`, data)
}

export function updateUserStatus(id, status) {
  return request.put(`/user/${id}/status`, { status })
}

export function deleteUser(id) {
  return request.delete(`/user/${id}`)
}
