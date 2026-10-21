<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessageBox } from 'element-plus'
import { useUserStore } from '../../stores/user'
import { getBookPage, addBook, updateBook, deleteBook, updateBookStatus } from '../../api/book'
import { getCategoryList } from '../../api/category'
import { message } from '../../utils/globalMessage'

const userStore = useUserStore()

const searchForm = reactive({
  title: '',
  author: '',
  categoryId: null,
  status: null
})

const tableData = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)
const loading = ref(false)

const categoryOptions = ref([])

const dialogVisible = ref(false)
const dialogTitle = ref('新增图书')
const formRef = ref(null)
const bookForm = reactive({
  id: null,
  isbn: '',
  title: '',
  author: '',
  publisher: '',
  categoryId: null,
  cover: '',
  description: '',
  totalNum: 0,
  price: null
})

const rules = {
  isbn: [{ required: true, message: '请输入ISBN', trigger: 'blur' }],
  title: [{ required: true, message: '请输入书名', trigger: 'blur' }],
  author: [{ required: true, message: '请输入作者', trigger: 'blur' }],
  totalNum: [{ required: true, message: '请输入库存', trigger: 'blur' }]
}

async function loadCategories() {
  try {
    categoryOptions.value = await getCategoryList()
  } catch (e) {}
}

async function loadData() {
  loading.value = true
  try {
    const params = { pageNum: pageNum.value, pageSize: pageSize.value }
    if (searchForm.title) params.title = searchForm.title
    if (searchForm.author) params.author = searchForm.author
    if (searchForm.categoryId) params.categoryId = searchForm.categoryId
    if (searchForm.status !== null && searchForm.status !== '') params.status = searchForm.status
    const res = await getBookPage(params)
    tableData.value = res.records
    total.value = res.total
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
  searchForm.title = ''
  searchForm.author = ''
  searchForm.categoryId = null
  searchForm.status = null
  handleSearch()
}

function handleAdd() {
  dialogTitle.value = '新增图书'
  Object.assign(bookForm, {
    id: null, isbn: '', title: '', author: '', publisher: '',
    categoryId: null, cover: '', description: '', totalNum: 0, price: null
  })
  dialogVisible.value = true
}

function handleEdit(row) {
  dialogTitle.value = '编辑图书'
  Object.assign(bookForm, {
    id: row.id, isbn: row.isbn, title: row.title, author: row.author,
    publisher: row.publisher, categoryId: row.categoryId, cover: row.cover,
    description: row.description, totalNum: row.totalNum, price: row.price
  })
  dialogVisible.value = true
}

async function handleSubmit() {
  await formRef.value.validate()
  try {
    if (bookForm.id) {
      await updateBook(bookForm.id, bookForm)
      message.success('修改成功')
    } else {
      await addBook(bookForm)
      message.success('新增成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (e) {}
}

async function handleDelete(row) {
  try {
    await ElMessageBox.confirm(`确定删除图书「${row.title}」吗？`, '提示', { type: 'warning' })
    await deleteBook(row.id)
    message.success('删除成功')
    loadData()
  } catch (e) {}
}

async function handleToggleStatus(row) {
  const newStatus = row.status === 1 ? 0 : 1
  const text = newStatus === 1 ? '上架' : '下架'
  try {
    await ElMessageBox.confirm(`确定${text}图书「${row.title}」吗？`, '提示', { type: 'warning' })
    await updateBookStatus(row.id, newStatus)
    message.success(`${text}成功`)
    loadData()
  } catch (e) {}
}

function statusTag(status) {
  return status === 1
    ? { type: 'success', text: '上架' }
    : { type: 'info', text: '下架' }
}

const categoryMap = computed(() => {
  const map = {}
  categoryOptions.value.forEach(c => { map[c.id] = c.name })
  return map
})

onMounted(() => {
  loadCategories()
  loadData()
})
</script>

<template>
  <div>
    <el-card style="margin-bottom: 16px">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="书名">
          <el-input v-model="searchForm.title" placeholder="请输入书名" clearable style="width: 180px" />
        </el-form-item>
        <el-form-item label="作者">
          <el-input v-model="searchForm.author" placeholder="请输入作者" clearable style="width: 180px" />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="searchForm.categoryId" placeholder="全部分类" clearable style="width: 180px">
            <el-option v-for="c in categoryOptions" :key="c.id" :label="c.name" :value="c.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="全部状态" clearable style="width: 120px">
            <el-option label="上架" :value="1" />
            <el-option label="下架" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card>
      <div style="margin-bottom: 16px">
        <el-button v-if="userStore.isAdmin" type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>新增图书
        </el-button>
      </div>

      <el-table :data="tableData" v-loading="loading" border stripe>
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="isbn" label="ISBN" width="160" />
        <el-table-column prop="title" label="书名" min-width="160" />
        <el-table-column prop="author" label="作者" width="120" />
        <el-table-column prop="publisher" label="出版社" width="140" />
        <el-table-column label="分类" width="100">
          <template #default="{ row }">{{ categoryMap[row.categoryId] || '-' }}</template>
        </el-table-column>
        <el-table-column label="库存" width="100">
          <template #default="{ row }">{{ row.availableNum }} / {{ row.totalNum }}</template>
        </el-table-column>
        <el-table-column label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="statusTag(row.status).type">{{ statusTag(row.status).text }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column v-if="userStore.isAdmin" label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button size="small" :type="row.status === 1 ? 'warning' : 'success'" @click="handleToggleStatus(row)">
              {{ row.status === 1 ? '下架' : '上架' }}
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

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="560px">
      <el-form ref="formRef" :model="bookForm" :rules="rules" label-width="90px">
        <el-form-item label="ISBN" prop="isbn">
          <el-input v-model="bookForm.isbn" />
        </el-form-item>
        <el-form-item label="书名" prop="title">
          <el-input v-model="bookForm.title" />
        </el-form-item>
        <el-form-item label="作者" prop="author">
          <el-input v-model="bookForm.author" />
        </el-form-item>
        <el-form-item label="出版社">
          <el-input v-model="bookForm.publisher" />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="bookForm.categoryId" placeholder="请选择分类" style="width: 100%">
            <el-option v-for="c in categoryOptions" :key="c.id" :label="c.name" :value="c.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="封面URL">
          <el-input v-model="bookForm.cover" placeholder="可留空" />
        </el-form-item>
        <el-form-item label="简介">
          <el-input v-model="bookForm.description" type="textarea" :rows="2" />
        </el-form-item>
        <el-form-item label="总库存" prop="totalNum">
          <el-input-number v-model="bookForm.totalNum" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="价格">
          <el-input-number v-model="bookForm.price" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>
