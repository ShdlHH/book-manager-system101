import request from './request'

export function getTodoList(params) {
  return request.get('/todo/list', { params })
}

export function addTodo(data) {
  return request.post('/todo/add', data)
}

export function updateTodo(data) {
  return request.put('/todo/update', data)
}

export function deleteTodo(id) {
  return request.delete(`/todo/delete/${id}`)
}

export function batchDeleteTodo(ids) {
  return request.delete('/todo/delete-batch', { data: ids })
}

export function changeTodoStatus(id) {
  return request.put(`/todo/change-status/${id}`)
}
