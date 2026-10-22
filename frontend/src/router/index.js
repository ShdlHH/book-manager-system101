import { createRouter, createWebHistory } from 'vue-router'
import { getToken } from '../utils/auth'
import { message } from '../utils/globalMessage'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/login/Login.vue'),
    meta: { public: true }
  },
  {
    path: '/',
    component: () => import('../layout/AdminLayout.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('../views/dashboard/Dashboard.vue'),
        meta: { title: '数据看板' }
      },
      {
        path: 'book',
        name: 'Book',
        component: () => import('../views/book/BookList.vue'),
        meta: { title: '图书管理' }
      },
      {
        path: 'borrow',
        name: 'Borrow',
        component: () => import('../views/borrow/BorrowList.vue'),
        meta: { title: '借阅管理' }
      },
      {
        path: 'category',
        name: 'Category',
        component: () => import('../views/category/CategoryList.vue'),
        meta: { title: '分类管理' }
      },
      {
        path: 'todo',
        name: 'Todo',
        component: () => import('../views/todo/TodoList.vue'),
        meta: { title: '待办事项' }
      },
      {
        path: 'user',
        name: 'User',
        component: () => import('../views/user/UserList.vue'),
        meta: { title: '用户管理', requiresAdmin: true }
      },
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('../views/profile/Profile.vue'),
        meta: { title: '个人信息' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach(async (to, from, next) => {
  const token = getToken()
  if (to.meta.public) {
    next()
    return
  }
  if (!token) {
    next('/login')
    return
  }
  if (to.meta.requiresAdmin) {
    const { useUserStore } = await import('../stores/user')
    const userStore = useUserStore()
    if (!userStore.userInfo) {
      try {
        await userStore.fetchUserInfo()
      } catch (e) {
        next('/login')
        return
      }
    }
    if (!userStore.isAdmin) {
      message.warning('无权限访问该页面')
      next('/dashboard')
      return
    }
  }
  next()
})

export default router
