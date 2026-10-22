<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessageBox } from 'element-plus'
import { message } from '../../utils/globalMessage'
import { formatDateTime } from '../../utils/format'
import { getTodoList, addTodo, updateTodo, deleteTodo, batchDeleteTodo, changeTodoStatus } from '../../api/todo'

const searchForm = reactive({
  title: '',
  status: null,
  priority: null
})

const tableData = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)
const loading = ref(false)
const selectedRows = ref([])

const dialogVisible = ref(false)
const dialogTitle = ref('新增待办')
const formRef = ref(null)
const form = reactive({
  id: null,
  title: '',
  content: '',
  priority: 2,
  status: 0
})

const rules = {
  title: [{ required: true, message: '请输入待办标题', trigger: 'blur' }]
}

const priorityMap = {
  1: { text: '高', type: 'danger' },
  2: { text: '中', type: 'warning' },
  3: { text: '低', type: 'info' }
}

async function loadData() {
  loading.value = true
  try {
    const params = { pageNum: pageNum.value, pageSize: pageSize.value }
    if (searchForm.title) params.title = searchForm.title
    if (searchForm.status !== null && searchForm.status !== '') params.status = searchForm.status
    if (searchForm.priority !== null && searchForm.priority !== '') params.priority = searchForm.priority
    const res = await getTodoList(params)
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
  searchForm.title = ''
  searchForm.status = null
  searchForm.priority = null
  handleSearch()
}

function handleSelectionChange(rows) {
  selectedRows.value = rows
}

function handleAdd() {
  dialogTitle.value = '新增待办'
  Object.assign(form, { id: null, title: '', content: '', priority: 2, status: 0 })
  dialogVisible.value = true
}

function handleEdit(row) {
  dialogTitle.value = '编辑待办'
  Object.assign(form, {
    id: row.id,
    title: row.title,
    content: row.content || '',
    priority: row.priority ?? 2,
    status: row.status ?? 0
  })
  dialogVisible.value = true
}

async function handleSubmit() {
  await formRef.value.validate()
  try {
    if (form.id) {
      await updateTodo({ ...form })
      message.success('修改成功')
    } else {
      await addTodo({ title: form.title, content: form.content, priority: form.priority, status: form.status })
      message.success('新增成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (e) {}
}

async function handleToggleStatus(row) {
  try {
    await changeTodoStatus(row.id)
    message.success('状态修改成功')
    loadData()
  } catch (e) {}
}

async function handleDelete(row) {
  try {
    await ElMessageBox.confirm(`确定删除待办「${row.title}」吗？`, '提示', { type: 'warning' })
    await deleteTodo(row.id)
    message.success('删除成功')
    loadData()
  } catch (e) {}
}

async function handleBatchDelete() {
  if (selectedRows.value.length === 0) {
    message.warning('请先选择要删除的待办')
    return
  }
  try {
    await ElMessageBox.confirm(`确定删除选中的 ${selectedRows.value.length} 条待办吗？`, '提示', { type: 'warning' })
    await batchDeleteTodo(selectedRows.value.map(row => row.id))
    message.success('批量删除成功')
    loadData()
  } catch (e) {}
}

onMounted(loadData)
</script>

<template>
  <div>
    <el-card shadow="never" style="margin-bottom: 16px">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="标题">
          <el-input v-model="searchForm.title" placeholder="请输入标题" clearable style="width: 180px" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="全部状态" clearable style="width: 120px">
            <el-option label="未完成" :value="0" />
            <el-option label="已完成" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="优先级">
          <el-select v-model="searchForm.priority" placeholder="全部优先级" clearable style="width: 130px">
            <el-option label="高" :value="1" />
            <el-option label="中" :value="2" />
            <el-option label="低" :value="3" />
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
          <el-icon><Plus /></el-icon>新增待办
        </el-button>
        <el-button type="danger" @click="handleBatchDelete">批量删除</el-button>
      </div>

      <el-table :data="tableData" v-loading="loading" border stripe @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="50" />
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="title" label="标题" min-width="160" />
        <el-table-column prop="content" label="内容" min-width="200">
          <template #default="{ row }">{{ row.content || '-' }}</template>
        </el-table-column>
        <el-table-column label="优先级" width="90">
          <template #default="{ row }">
            <el-tag :type="priorityMap[row.priority]?.type || 'info'">
              {{ priorityMap[row.priority]?.text || '-' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="90">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">
              {{ row.status === 1 ? '已完成' : '未完成' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" width="170">
          <template #default="{ row }">{{ formatDateTime(row.createTime) }}</template>
        </el-table-column>
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button
              size="small"
              :type="row.status === 1 ? 'warning' : 'success'"
              @click="handleToggleStatus(row)"
            >
              {{ row.status === 1 ? '撤销' : '完成' }}
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

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="520px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" />
        </el-form-item>
        <el-form-item label="内容">
          <el-input v-model="form.content" type="textarea" :rows="4" />
        </el-form-item>
        <el-form-item label="优先级">
          <el-radio-group v-model="form.priority">
            <el-radio :label="1">高</el-radio>
            <el-radio :label="2">中</el-radio>
            <el-radio :label="3">低</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio :label="0">未完成</el-radio>
            <el-radio :label="1">已完成</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>
