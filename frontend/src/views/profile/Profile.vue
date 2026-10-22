<script setup>
import { ref, reactive, onMounted } from 'vue'
import { message } from '../../utils/globalMessage'
import { useUserStore } from '../../stores/user'
import { getUserInfo, updateUserInfo } from '../../api/user'

const userStore = useUserStore()

const info = ref({})
const formRef = ref(null)
const form = reactive({
  nickname: '',
  phone: '',
  password: ''
})

const rules = {
  phone: [{ pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }]
}

async function loadInfo() {
  const data = await getUserInfo()
  info.value = data
  form.nickname = data.nickname || ''
  form.phone = data.phone || ''
  form.password = ''
}

async function handleSubmit() {
  await formRef.value.validate()
  try {
    const data = { nickname: form.nickname, phone: form.phone }
    if (form.password) data.password = form.password
    await updateUserInfo(data)
    message.success('保存成功')
    form.password = ''
    await userStore.fetchUserInfo()
    await loadInfo()
  } catch (e) {}
}

onMounted(loadInfo)
</script>

<template>
  <el-row :gutter="16">
    <el-col :span="10">
      <el-card shadow="never">
        <template #header>
          <span style="font-weight: bold">账号信息</span>
        </template>
        <el-descriptions :column="1" border>
          <el-descriptions-item label="用户名">{{ info.username || '-' }}</el-descriptions-item>
          <el-descriptions-item label="角色">
            <el-tag :type="info.role === 1 ? 'danger' : 'success'">
              {{ info.role === 1 ? '管理员' : '读者' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="info.status === 1 ? 'success' : 'info'">
              {{ info.status === 1 ? '正常' : '禁用' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="可借数量">{{ info.maxBorrowNum ?? '-' }}</el-descriptions-item>
        </el-descriptions>
      </el-card>
    </el-col>

    <el-col :span="14">
      <el-card shadow="never">
        <template #header>
          <span style="font-weight: bold">修改信息</span>
        </template>
        <el-form ref="formRef" :model="form" :rules="rules" label-width="90px" style="max-width: 420px">
          <el-form-item label="昵称">
            <el-input v-model="form.nickname" />
          </el-form-item>
          <el-form-item label="联系电话" prop="phone">
            <el-input v-model="form.phone" />
          </el-form-item>
          <el-form-item label="新密码">
            <el-input v-model="form.password" type="password" show-password placeholder="不修改请留空" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSubmit">保存</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </el-col>
  </el-row>
</template>
