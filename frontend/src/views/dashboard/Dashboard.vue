<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'
import * as echarts from 'echarts'
import { getDashboard, getBorrowTrend, getCategoryRatio, getHotBooks } from '../../api/stat'

const stats = ref({
  totalBooks: 0,
  totalUsers: 0,
  borrowingCount: 0,
  overdueCount: 0
})
const hotBooks = ref([])
const trendDays = ref(7)
const trendRef = ref(null)
const pieRef = ref(null)
let trendChart = null
let pieChart = null

const statCards = [
  { key: 'totalBooks', label: '图书总数', icon: 'Reading', color: '#409EFF' },
  { key: 'totalUsers', label: '读者总数', icon: 'User', color: '#67C23A' },
  { key: 'borrowingCount', label: '借阅中', icon: 'Tickets', color: '#E6A23C' },
  { key: 'overdueCount', label: '逾期未还', icon: 'Warning', color: '#F56C6C' }
]

async function loadDashboard() {
  const data = await getDashboard()
  stats.value = data
}

async function loadTrend() {
  const data = await getBorrowTrend(trendDays.value)
  const dates = data.map(item => item.date)
  const counts = data.map(item => item.borrowCount)
  if (!trendChart) {
    trendChart = echarts.init(trendRef.value)
  }
  trendChart.setOption({
    tooltip: { trigger: 'axis' },
    grid: { left: 40, right: 20, top: 30, bottom: 30 },
    xAxis: { type: 'category', data: dates, boundaryGap: false },
    yAxis: { type: 'value', minInterval: 1 },
    series: [{
      name: '借阅数量',
      type: 'line',
      smooth: true,
      data: counts,
      itemStyle: { color: '#409EFF' },
      areaStyle: { opacity: 0.15 }
    }]
  })
}

async function loadCategoryRatio() {
  const data = await getCategoryRatio()
  if (!pieChart) {
    pieChart = echarts.init(pieRef.value)
  }
  pieChart.setOption({
    tooltip: { trigger: 'item', formatter: '{b}：{c} 本（{d}%）' },
    legend: { orient: 'vertical', left: 'left', top: 'center' },
    series: [{
      name: '分类占比',
      type: 'pie',
      radius: ['40%', '70%'],
      center: ['60%', '50%'],
      avoidLabelOverlap: false,
      itemStyle: { borderRadius: 6, borderColor: '#fff', borderWidth: 2 },
      label: { show: false },
      emphasis: { label: { show: true, fontSize: 16, fontWeight: 'bold' } },
      data: data.map(item => ({ name: item.categoryName, value: item.bookCount }))
    }]
  })
}

async function loadHotBooks() {
  hotBooks.value = await getHotBooks()
}

function handleTrendDaysChange() {
  loadTrend()
}

function handleResize() {
  trendChart?.resize()
  pieChart?.resize()
}

onMounted(() => {
  loadDashboard()
  loadTrend()
  loadCategoryRatio()
  loadHotBooks()
  window.addEventListener('resize', handleResize)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize)
  trendChart?.dispose()
  pieChart?.dispose()
})
</script>

<template>
  <div>
    <el-row :gutter="16">
      <el-col v-for="card in statCards" :key="card.key" :span="6">
        <el-card shadow="never" style="margin-bottom: 16px">
          <div style="display: flex; align-items: center">
            <div
              :style="{
                width: '52px', height: '52px', borderRadius: '10px',
                background: card.color + '1a', display: 'flex',
                alignItems: 'center', justifyContent: 'center', marginRight: '14px'
              }"
            >
              <el-icon :size="26" :color="card.color"><component :is="card.icon" /></el-icon>
            </div>
            <div>
              <div style="font-size: 13px; color: #909399">{{ card.label }}</div>
              <div style="font-size: 26px; font-weight: bold; color: #303133">
                {{ stats[card.key] ?? 0 }}
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16">
      <el-col :span="16">
        <el-card shadow="never" style="margin-bottom: 16px">
          <template #header>
            <div style="display: flex; align-items: center; justify-content: space-between">
              <span style="font-weight: bold">借阅趋势</span>
              <el-radio-group v-model="trendDays" size="small" @change="handleTrendDaysChange">
                <el-radio-button :label="7">近7天</el-radio-button>
                <el-radio-button :label="15">近15天</el-radio-button>
                <el-radio-button :label="30">近30天</el-radio-button>
              </el-radio-group>
            </div>
          </template>
          <div ref="trendRef" style="height: 320px"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="never" style="margin-bottom: 16px">
          <template #header>
            <span style="font-weight: bold">图书分类占比</span>
          </template>
          <div ref="pieRef" style="height: 320px"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-card shadow="never">
      <template #header>
        <span style="font-weight: bold">热门图书 TOP10</span>
      </template>
      <el-table :data="hotBooks" border stripe>
        <el-table-column type="index" label="排名" width="70" />
        <el-table-column prop="title" label="书名" min-width="200" />
        <el-table-column prop="author" label="作者" width="180" />
        <el-table-column prop="borrowCount" label="借阅次数" width="120">
          <template #default="{ row }">
            <el-tag type="warning">{{ row.borrowCount }} 次</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>
