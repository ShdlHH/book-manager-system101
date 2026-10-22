<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessageBox } from 'element-plus'
import { message } from '../../utils/globalMessage'
import { getUserPage, addUser, updateUserByAdmin, updateUserStatus, deleteUser } from '../../api/user'

const searchForm = reactive({
  username: '',
  nickname: '',
  role: null
})

const tableData = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)
const loading = ref(false)

const dialogVisible = ref(false)
const dialogTitle = ref('新增用户')
const formRef = ref(null)
const form = reactive({
  id: null,
  username: '',
  password: '',
  nickname: '',
  role: 0,
  phone: '',
  maxBorrowNum: 5
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

async function loadData() {
  loading.value = true
  try {
    const params = { pageNum: pageNum.value, pageSize: pageSize.value }
    if (searchForm.username) params.username = searchForm.username
    if (searchForm.nickname) params.nickname = searchForm.nickname
    if (searchForm.role !== null && searchForm.role !== '') params.role = searchForm.role
    const res = await getUserPage(params)
    tableData.value = res.records || []
    total.value = res.total || 0
  } catch (e) {
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  pageNum.value = 1
  loadData()
}

function handleReset() {
  searchForm.username = ''
  searchForm.nickname = ''
  searchForm.role = null
  handleSearch()
}

function handleAdd() {
  dialogTitle.value = '新增用户'
  Object.assign(form, {
    id: null, username: '', password: '', nickname: '', role: 0, phone: '', maxBorrowNum: 5
  })
  dialogVisible.value = true
}

function handleEdit(row) {
  dialogTitle.value = '编辑用户'
  Object.assign(form, {
    id: row.id,
    username: row.username,
    password: '',
    nickname: row.nickname || '',
    role: row.role,
    phone: row.phone || '',
    maxBorrowNum: row.maxBorrowNum ?? 5
  })
  dialogVisible.value = true
}

async function handleSubmit() {
  await formRef.value.validate()
  try {
    if (form.id) {
      await updateUserByAdmin(form.id, {
        nickname: form.nickname,
        role: form.role,
        phone: form.phone,
        maxBorrowNum: form.maxBorrowNum
      })
      message.success('修改成功')
    } else {
      await addUser({
        username: form.username,
        password: form.password,
        nickname: form.nickname,
        role: form.role,
        phone: form.phone,
        maxBorrowNum: form.maxBorrowNum
      })
      message.success('新增成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (e) {}
}

async function handleToggleStatus(row) {
  const newStatus = row.status === 1 ? 0 : 1
  const text = newStatus === 1 ? '启用' : '禁用'
  try {
    await ElMessageBox.confirm(`确定${text}用户「${row.username}」吗？`, '提示', { type: 'warning' })
    await updateUserStatus(row.id, newStatus)
    message.success(`${text}成功`)
    loadData()
  } catch (e) {}
}

async function handleDelete(row) {
  try {
    await ElMessageBox.confirm(`确定删除用户「${row.username}」吗？`, '提示', { type: 'warning' })
    await deleteUser(row.id)
    message.success('删除成功')
    loadData()
  } catch (e) {}
}

onMounted(loadData)
</script>

<template>
  <div>
    <el-card shadow="never" style="margin-bottom: 16px">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="用户名">
          <el-input v-model="searchForm.username" placeholder="请输入用户名" clearable style="width: 160px" />
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="searchForm.nickname" placeholder="请输入昵称" clearable style="width: 160px" />
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="searchForm.role" placeholder="全部角色" clearable style="width: 130px">
            <el-option label="读者" :value="0" />
            <el-option label="管理员" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never">
      <div style="margin-bottom: 16px">
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>新增用户
        </el-button>
      </div>

      <el-table :data="tableData" v-loading="loading" border stripe>
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="username" label="用户名" width="130" />
        <el-table-column prop="nickname" label="昵称" width="130">
          <template #default="{ row }">{{ row.nickname || '-' }}</template>
        </el-table-column>
        <el-table-column label="角色" width="100">
          <template #default="{ row }">
            <el-tag :type="row.role === 1 ? 'danger' : 'success'">
              {{ row.role === 1 ? '管理员' : '读者' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="联系电话" width="140">
          <template #default="{ row }">{{ row.phone || '-' }}</template>
        </el-table-column>
        <el-table-column prop="maxBorrowNum" label="可借数量" width="100" />
        <el-table-column label="状态" width="90">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">
              {{ row.status === 1 ? '正常' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" min-width="160" />
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button
              size="small"
              :type="row.status === 1 ? 'warning' : 'success'"
              @click="handleToggleStatus(row)"
            >
              {{ row.status === 1 ? '禁用' : '启用' }}
            </el-button>
            <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        style="margin-top: 16px; justify-content: flex-end"
        v-model:current-page="pageNum"
        v-model:page-size="pageSize"
        :total="total"
        :page-sizes="[10, 20, 50]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="loadData"
        @current-change="loadData"
      />
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="480px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" :disabled="!!form.id" />
        </el-form-item>
        <el-form-item v-if="!form.id" label="密码" prop="password">
          <el-input v-model="form.password" type="password" show-password />
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="form.nickname" />
        </el-form-item>
        <el-form-item label="角色">
          <el-radio-group v-model="form.role">
            <el-radio :label="0">读者</el-radio>
            <el-radio :label="1">管理员</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="联系电话">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item label="可借数量">
          <el-input-number v-model="form.maxBorrowNum" :min="0" style="width: 100%" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>
