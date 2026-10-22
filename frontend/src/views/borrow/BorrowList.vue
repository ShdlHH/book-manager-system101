<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessageBox } from 'element-plus'
import { useUserStore } from '../../stores/user'
import { message } from '../../utils/globalMessage'
import { formatDate } from '../../utils/format'
import { getMyBorrowList, getBorrowPage, getOverdueList, borrowBook, returnBook, renewBook } from '../../api/borrow'
import { getBookPage } from '../../api/book'

const userStore = useUserStore()
const isAdmin = computed(() => userStore.isAdmin)

const searchForm = reactive({
  username: '',
  bookTitle: '',
  status: null
})
const overdueOnly = ref(false)

const tableData = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)
const loading = ref(false)

const borrowDialogVisible = ref(false)
const bookLoading = ref(false)
const bookOptions = ref([])
const selectedBookId = ref(null)

const statusMap = {
  0: { text: '借阅中', type: 'primary' },
  1: { text: '已归还', type: 'success' },
  2: { text: '已逾期', type: 'danger' },
  3: { text: '续借中', type: 'warning' }
}

async function loadData() {
  loading.value = true
  try {
    let res
    if (!isAdmin.value) {
      res = await getMyBorrowList()
    } else if (overdueOnly.value) {
      res = await getOverdueList({ pageNum: pageNum.value, pageSize: pageSize.value })
    } else {
      const params = { pageNum: pageNum.value, pageSize: pageSize.value }
      if (searchForm.username) params.username = searchForm.username
      if (searchForm.bookTitle) params.bookTitle = searchForm.bookTitle
      if (searchForm.status !== null && searchForm.status !== '') params.status = searchForm.status
      res = await getBorrowPage(params)
    }
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
  searchForm.bookTitle = ''
  searchForm.status = null
  overdueOnly.value = false
  handleSearch()
}

function handleOverdueChange() {
  pageNum.value = 1
  loadData()
}

async function openBorrowDialog() {
  borrowDialogVisible.value = true
  selectedBookId.value = null
  bookLoading.value = true
  try {
    const res = await getBookPage({ pageNum: 1, pageSize: 100, status: 1 })
    bookOptions.value = (res.records || []).filter(book => book.availableNum > 0)
  } catch (e) {
  } finally {
    bookLoading.value = false
  }
}

async function submitBorrow() {
  if (!selectedBookId.value) {
    message.warning('请选择要借阅的图书')
    return
  }
  try {
    await borrowBook(selectedBookId.value)
    message.success('借阅成功')
    borrowDialogVisible.value = false
    loadData()
  } catch (e) {}
}

async function handleReturn(row) {
  try {
    await ElMessageBox.confirm(`确定归还《${row.bookTitle || '该图书'}》吗？`, '提示', { type: 'warning' })
    await returnBook(row.id)
    message.success('归还成功')
    loadData()
  } catch (e) {}
}

async function handleRenew(row) {
  try {
    await ElMessageBox.confirm(`确定续借《${row.bookTitle || '该图书'}》14天吗？`, '提示', { type: 'warning' })
    await renewBook(row.id)
    message.success('续借成功')
    loadData()
  } catch (e) {}
}

function canReturn(row) {
  return row.status !== 1
}

function canRenew(row) {
  return (row.status === 0 || row.status === 3) && (row.renewCount ?? 0) < 2
}

onMounted(loadData)
</script>

<template>
  <div>
    <el-card v-if="isAdmin" shadow="never" style="margin-bottom: 16px">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="用户名">
          <el-input v-model="searchForm.username" placeholder="请输入用户名" clearable style="width: 160px" />
        </el-form-item>
        <el-form-item label="书名">
          <el-input v-model="searchForm.bookTitle" placeholder="请输入书名" clearable style="width: 160px" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="全部状态" clearable style="width: 120px">
            <el-option label="借阅中" :value="0" />
            <el-option label="已归还" :value="1" />
            <el-option label="已逾期" :value="2" />
            <el-option label="续借中" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
        <el-form-item>
          <el-checkbox v-model="overdueOnly" @change="handleOverdueChange">只看逾期</el-checkbox>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never">
      <div style="margin-bottom: 16px">
        <el-button type="primary" @click="openBorrowDialog">
          <el-icon><Plus /></el-icon>借书
        </el-button>
      </div>

      <el-table :data="tableData" v-loading="loading" border stripe>
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column v-if="isAdmin" prop="username" label="用户名" width="120" />
        <el-table-column v-if="isAdmin" prop="phone" label="联系电话" width="130">
          <template #default="{ row }">{{ row.phone || '-' }}</template>
        </el-table-column>
        <el-table-column prop="bookTitle" label="书名" min-width="160" />
        <el-table-column prop="author" label="作者" width="130" />
        <el-table-column label="借阅日期" width="120">
          <template #default="{ row }">{{ formatDate(row.borrowDate) }}</template>
        </el-table-column>
        <el-table-column label="应还日期" width="120">
          <template #default="{ row }">{{ formatDate(row.dueDate) }}</template>
        </el-table-column>
        <el-table-column label="归还日期" width="120">
          <template #default="{ row }">{{ row.returnDate ? formatDate(row.returnDate) : '-' }}</template>
        </el-table-column>
        <el-table-column label="状态" width="90">
          <template #default="{ row }">
            <el-tag :type="statusMap[row.status]?.type || 'info'">
              {{ statusMap[row.status]?.text || '未知' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="续借次数" width="90">
          <template #default="{ row }">{{ row.renewCount ?? 0 }}</template>
        </el-table-column>
        <el-table-column label="逾期天数" width="90">
          <template #default="{ row }">
            <span :style="{ color: row.overdueDays ? '#F56C6C' : '#909399' }">
              {{ row.overdueDays ? row.overdueDays + ' 天' : '-' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{ row }">
            <el-button v-if="canReturn(row)" size="small" type="primary" @click="handleReturn(row)">归还</el-button>
            <el-button v-if="canRenew(row)" size="small" type="warning" @click="handleRenew(row)">续借</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-if="isAdmin"
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

    <el-dialog v-model="borrowDialogVisible" title="借书" width="520px">
      <el-form label-width="90px" v-loading="bookLoading">
        <el-form-item label="选择图书">
          <el-select v-model="selectedBookId" placeholder="请选择可借阅图书" filterable style="width: 100%">
            <el-option
              v-for="book in bookOptions"
              :key="book.id"
              :label="`${book.title}（可借 ${book.availableNum} 本）`"
              :value="book.id"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="borrowDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitBorrow">确定借阅</el-button>
      </template>
    </el-dialog>
  </div>
</template>
