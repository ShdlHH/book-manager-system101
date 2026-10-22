<script setup>
import { computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '../stores/user'
import { ElMessageBox } from 'element-plus'
import { message } from '../utils/globalMessage'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const activeMenu = computed(() => route.path)

const menuItems = computed(() => {
  const items = [
    { index: '/dashboard', title: '数据看板', icon: 'DataLine' },
    { index: '/book', title: '图书管理', icon: 'Reading' },
    { index: '/borrow', title: '借阅管理', icon: 'Tickets' },
    { index: '/category', title: '分类管理', icon: 'Files' },
    { index: '/todo', title: '待办事项', icon: 'List' }
  ]
  if (userStore.isAdmin) {
    items.push({ index: '/user', title: '用户管理', icon: 'UserFilled' })
  }
  items.push({ index: '/profile', title: '个人信息', icon: 'User' })
  return items
})

onMounted(async () => {
  if (!userStore.userInfo) {
    try {
      await userStore.fetchUserInfo()
    } catch (e) {
      router.push('/login')
    }
  }
})

function handleSelect(index) {
  router.push(index)
}

async function handleCommand(command) {
  if (command === 'profile') {
    router.push('/profile')
    return
  }
  if (command === 'logout') {
    try {
      await ElMessageBox.confirm('确定要退出登录吗？', '提示', { type: 'warning' })
      await userStore.logout()
      message.success('已退出登录')
      router.push('/login')
    } catch (e) {
      // 取消
    }
  }
}
</script>

<template>
  <el-container style="height: 100vh">
    <el-aside width="220px" style="background: #304156">
      <div style="height: 60px; display: flex; align-items: center; justify-content: center; color: #fff; font-size: 18px; font-weight: bold; border-bottom: 1px solid #1f2d3d">
        图书管理系统
      </div>
      <el-menu
        :default-active="activeMenu"
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
        @select="handleSelect"
      >
        <el-menu-item v-for="item in menuItems" :key="item.index" :index="item.index">
          <el-icon><component :is="item.icon" /></el-icon>
          <span>{{ item.title }}</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header style="background: #fff; border-bottom: 1px solid #e6e6e6; display: flex; align-items: center; justify-content: space-between">
        <span style="font-size: 16px; color: #303133">{{ route.meta.title }}</span>
        <el-dropdown @command="handleCommand">
          <span style="cursor: pointer">
            <el-icon><User /></el-icon>
            {{ userStore.username || '用户' }}
            <el-icon><ArrowDown /></el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="profile">个人信息</el-dropdown-item>
              <el-dropdown-item command="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </el-header>

      <el-main style="background: #f0f2f5; padding: 20px">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>
