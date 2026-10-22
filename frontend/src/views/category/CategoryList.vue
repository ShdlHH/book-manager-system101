<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessageBox } from 'element-plus'
import { message } from '../../utils/globalMessage'
import { getCategoryPage, addCategory, updateCategory, deleteCategory } from '../../api/category'

const searchName = ref('')
const tableData = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)
const loading = ref(false)

const dialogVisible = ref(false)
const dialogTitle = ref('新增分类')
const formRef = ref(null)
const form = reactive({ id: null, name: '', sort: 0 })

const rules = {
  name: [{ required: true, message: '请输入分类名称', trigger: 'blur' }]
}

async function loadData() {
  loading.value = true
  try {
    const params = { pageNum: pageNum.value, pageSize: pageSize.value }
    if (searchName.value) params.name = searchName.value
    const res = await getCategoryPage(params)
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

function handleAdd() {
  dialogTitle.value = '新增分类'
  Object.assign(form, { id: null, name: '', sort: 0 })
  dialogVisible.value = true
}

function handleEdit(row) {
  dialogTitle.value = '编辑分类'
  Object.assign(form, { id: row.id, name: row.name, sort: row.sort ?? 0 })
  dialogVisible.value = true
}

async function handleSubmit() {
  await formRef.value.validate()
  try {
    if (form.id) {
      await updateCategory(form.id, { name: form.name, sort: form.sort })
      message.success('修改成功')
    } else {
      await addCategory({ name: form.name, sort: form.sort })
      message.success('新增成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (e) {}
}

async function handleDelete(row) {
  try {
    await ElMessageBox.confirm(`确定删除分类「${row.name}」吗？`, '提示', { type: 'warning' })
    await deleteCategory(row.id)
    message.success('删除成功')
    loadData()
  } catch (e) {}
}

onMounted(loadData)
</script>

<template>
  <div>
    <el-card shadow="never" style="margin-bottom: 16px">
      <el-form :inline="true">
        <el-form-item label="分类名称">
          <el-input v-model="searchName" placeholder="请输入分类名称" clearable style="width: 200px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never">
      <div style="margin-bottom: 16px">
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>新增分类
        </el-button>
      </div>

      <el-table :data="tableData" v-loading="loading" border stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="分类名称" min-width="200" />
        <el-table-column prop="sort" label="排序" width="100" />
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="handleEdit(row)">编辑</el-button>
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

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="420px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="分类名称" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="form.sort" :min="0" style="width: 100%" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>
