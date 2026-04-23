<template>
  <div id="appManagePage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <div class="header-icon">
          <AppstoreOutlined />
        </div>
        <div class="header-text">
          <h1 class="page-title">应用管理</h1>
          <p class="page-desc">管理所有用户创建的应用</p>
        </div>
      </div>
      <div class="header-right">
        <a-button type="primary" @click="fetchData">
          <template #icon>
            <ReloadOutlined />
          </template>
          刷新
        </a-button>
      </div>
    </div>

    <!-- 搜索区域 -->
    <div class="search-card">
      <a-form layout="inline" :model="searchParams" @finish="doSearch">
        <a-form-item label="应用名称">
          <a-input
            v-model:value="searchParams.appName"
            placeholder="输入应用名称"
            allow-clear
            class="search-input"
          />
        </a-form-item>
        <a-form-item label="创建者">
          <a-input
            v-model:value="searchParams.userId"
            placeholder="输入用户ID"
            allow-clear
            class="search-input-small"
          />
        </a-form-item>
        <a-form-item label="生成类型">
          <a-select
            v-model:value="searchParams.codeGenType"
            placeholder="选择生成类型"
            style="width: 150px"
            allow-clear
          >
            <a-select-option value="">全部</a-select-option>
            <a-select-option
              v-for="option in CODE_GEN_TYPE_OPTIONS"
              :key="option.value"
              :value="option.value"
            >
              {{ option.label }}
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item>
          <a-space>
            <a-button type="primary" html-type="submit">
              <template #icon>
                <SearchOutlined />
              </template>
              搜索
            </a-button>
            <a-button @click="resetSearch">
              <template #icon>
                <ClearOutlined />
              </template>
              重置
            </a-button>
          </a-space>
        </a-form-item>
      </a-form>
    </div>

    <!-- 表格卡片 -->
    <div class="table-card">
      <div class="table-header">
        <span class="table-count">共 {{ total }} 个应用</span>
      </div>
      <a-table
        :columns="columns"
        :data-source="data"
        :pagination="pagination"
        :loading="loading"
        @change="doTableChange"
        :scroll="{ x: 1200 }"
        :row-key="(record: API.AppVO) => record.id"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.dataIndex === 'cover'">
            <a-image
              v-if="record.cover"
              :src="record.cover"
              :width="80"
              :height="60"
              class="cover-image"
              :preview-mask="false"
            />
            <div v-else class="no-cover">
              <PictureOutlined />
              <span>无封面</span>
            </div>
          </template>
          <template v-else-if="column.dataIndex === 'initPrompt'">
            <a-tooltip :title="record.initPrompt" placement="topLeft">
              <div class="prompt-text">{{ record.initPrompt }}</div>
            </a-tooltip>
          </template>
          <template v-else-if="column.dataIndex === 'codeGenType'">
            <a-tag :color="getTypeColor(record.codeGenType)">
              {{ formatCodeGenType(record.codeGenType) }}
            </a-tag>
          </template>
          <template v-else-if="column.dataIndex === 'priority'">
            <a-tag v-if="record.priority === 99" color="gold" class="featured-tag">
              <StarFilled /> 精选
            </a-tag>
            <span v-else class="priority-text">{{ record.priority || 0 }}</span>
          </template>
          <template v-else-if="column.dataIndex === 'deployedTime'">
            <span v-if="record.deployedTime" class="time-text">
              {{ formatTime(record.deployedTime) }}
            </span>
            <span v-else class="text-muted">未部署</span>
          </template>
          <template v-else-if="column.dataIndex === 'createTime'">
            <span class="time-text">{{ formatTime(record.createTime) }}</span>
          </template>
          <template v-else-if="column.dataIndex === 'user'">
            <UserInfo :user="record.user" size="small" />
          </template>
          <template v-else-if="column.key === 'action'">
            <a-space class="action-buttons">
              <a-button type="primary" size="small" @click="editApp(record)">
                <template #icon><EditOutlined /></template>
                编辑
              </a-button>
              <a-button
                size="small"
                @click="toggleFeatured(record)"
                :class="{ 'featured-btn': record.priority === 99 }"
              >
                <template #icon>
                  <StarOutlined v-if="record.priority !== 99" />
                  <StarFilled v-else />
                </template>
                {{ record.priority === 99 ? '取消' : '精选' }}
              </a-button>
              <a-popconfirm
                title="确定要删除这个应用吗？"
                @confirm="deleteApp(record.id)"
                placement="topRight"
              >
                <a-button danger size="small">
                  <template #icon><DeleteOutlined /></template>
                  删除
                </a-button>
              </a-popconfirm>
            </a-space>
          </template>
        </template>
      </a-table>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import {
  listAppVoByPageByAdmin,
  deleteAppByAdmin,
  updateAppByAdmin,
} from '@/api/appController'
import { CODE_GEN_TYPE_OPTIONS, formatCodeGenType } from '@/utils/codeGenTypes'
import { formatTime } from '@/utils/time'
import UserInfo from '@/components/UserInfo.vue'
import {
  AppstoreOutlined,
  SearchOutlined,
  ReloadOutlined,
  ClearOutlined,
  EditOutlined,
  DeleteOutlined,
  StarOutlined,
  StarFilled,
  PictureOutlined,
} from '@ant-design/icons-vue'

const router = useRouter()

const loading = ref(false)

const columns = [
  {
    title: 'ID',
    dataIndex: 'id',
    width: 80,
    fixed: 'left',
  },
  {
    title: '应用名称',
    dataIndex: 'appName',
    width: 150,
    ellipsis: true,
  },
  {
    title: '封面',
    dataIndex: 'cover',
    width: 100,
  },
  {
    title: '初始提示词',
    dataIndex: 'initPrompt',
    width: 200,
    ellipsis: true,
  },
  {
    title: '生成类型',
    dataIndex: 'codeGenType',
    width: 100,
  },
  {
    title: '优先级',
    dataIndex: 'priority',
    width: 80,
  },
  {
    title: '部署时间',
    dataIndex: 'deployedTime',
    width: 160,
  },
  {
    title: '创建者',
    dataIndex: 'user',
    width: 120,
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 160,
  },
  {
    title: '操作',
    key: 'action',
    width: 220,
    fixed: 'right',
  },
]

const data = ref<API.AppVO[]>([])
const total = ref(0)

const searchParams = reactive<API.AppQueryRequest>({
  pageNum: 1,
  pageSize: 10,
})

const fetchData = async () => {
  loading.value = true
  try {
    const res = await listAppVoByPageByAdmin({
      ...searchParams,
    })
    if (res.data.data) {
      data.value = res.data.data.records ?? []
      total.value = res.data.data.totalRow ?? 0
    } else {
      message.error('获取数据失败，' + res.data.message)
    }
  } catch (error) {
    console.error('获取数据失败：', error)
    message.error('获取数据失败')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchData()
})

const pagination = computed(() => ({
  current: searchParams.pageNum ?? 1,
  pageSize: searchParams.pageSize ?? 10,
  total: total.value,
  showSizeChanger: true,
  showTotal: (total: number) => `共 ${total} 条`,
}))

const doTableChange = (page: { current: number; pageSize: number }) => {
  searchParams.pageNum = page.current
  searchParams.pageSize = page.pageSize
  fetchData()
}

const doSearch = () => {
  searchParams.pageNum = 1
  fetchData()
}

const resetSearch = () => {
  searchParams.appName = undefined
  searchParams.userId = undefined
  searchParams.codeGenType = undefined
  searchParams.pageNum = 1
  fetchData()
}

const editApp = (app: API.AppVO) => {
  router.push(`/app/edit/${app.id}`)
}

const toggleFeatured = async (app: API.AppVO) => {
  if (!app.id) return
  const newPriority = app.priority === 99 ? 0 : 99
  try {
    const res = await updateAppByAdmin({
      id: app.id,
      priority: newPriority,
    })
    if (res.data.code === 0) {
      message.success(newPriority === 99 ? '已设为精选' : '已取消精选')
      fetchData()
    } else {
      message.error('操作失败：' + res.data.message)
    }
  } catch (error) {
    console.error('操作失败：', error)
    message.error('操作失败')
  }
}

const deleteApp = async (id: number | undefined) => {
  if (!id) return
  try {
    const res = await deleteAppByAdmin({ id })
    if (res.data.code === 0) {
      message.success('删除成功')
      fetchData()
    } else {
      message.error('删除失败：' + res.data.message)
    }
  } catch (error) {
    console.error('删除失败：', error)
    message.error('删除失败')
  }
}

const getTypeColor = (type: string) => {
  const colors: Record<string, string> = {
    REACT: 'cyan',
    VUE: 'green',
    HTML: 'orange',
    STATIC: 'purple',
  }
  return colors[type] || 'blue'
}
</script>

<style scoped>
#appManagePage {
  padding: 24px;
  background: #FFEFD5;
  min-height: calc(100vh - 100px);
}

/* 页面头部 */
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.header-icon {
  width: 56px;
  height: 56px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #DC143C 0%, #CD5C5C 100%);
  border-radius: 16px;
  font-size: 28px;
  color: #fff;
  box-shadow: 0 4px 12px rgba(220, 20, 60, 0.3);
}

.header-text h1 {
  margin: 0;
  font-size: 24px;
  font-weight: 600;
  color: #333;
}

.header-text p {
  margin: 4px 0 0;
  font-size: 14px;
  color: #999;
}

/* 搜索卡片 */
.search-card {
  background: #fff;
  border-radius: 16px;
  padding: 20px 24px;
  margin-bottom: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
}

:deep(.ant-form-inline) {
  gap: 16px;
}

:deep(.ant-form-item-label > label) {
  color: #666;
  font-weight: 500;
}

.search-input {
  width: 200px;
}

.search-input-small {
  width: 120px;
}

:deep(.ant-input),
:deep(.ant-select-selector) {
  border-radius: 8px !important;
  border-color: #e8e8e8;
  transition: all 0.3s;
}

:deep(.ant-input:hover),
:deep(.ant-select:hover .ant-select-selector) {
  border-color: #DC143C;
}

:deep(.ant-input:focus),
:deep(.ant-select-focused .ant-select-selector) {
  border-color: #DC143C;
  box-shadow: 0 0 0 2px rgba(220, 20, 60, 0.1);
}

/* 表格卡片 */
.table-card {
  background: #fff;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
}

.table-header {
  padding: 16px 24px;
  border-bottom: 1px solid #f0f0f0;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.table-count {
  font-size: 14px;
  color: #666;
  font-weight: 500;
}

/* 封面图片 */
.cover-image {
  border-radius: 8px;
  object-fit: cover;
}

.no-cover {
  width: 80px;
  height: 60px;
  background: linear-gradient(135deg, rgba(220, 20, 60, 0.08) 0%, rgba(205, 92, 92, 0.1) 100%);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 4px;
  color: #CD5C5C;
  font-size: 12px;
  border-radius: 8px;
}

/* 提示文字 */
.prompt-text {
  max-width: 200px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  color: #666;
}

.time-text {
  color: #666;
  font-size: 13px;
}

.text-muted {
  color: #999;
}

.priority-text {
  color: #666;
}

.featured-tag {
  display: inline-flex;
  align-items: center;
  gap: 4px;
}

/* 操作按钮 */
.action-buttons {
  display: flex;
  gap: 8px;
}

:deep(.ant-btn-primary) {
  background: linear-gradient(135deg, #DC143C 0%, #CD5C5C 100%);
  border: none;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(220, 20, 60, 0.3);
}

:deep(.ant-btn-primary:hover) {
  background: linear-gradient(135deg, #CD5C5C 0%, #DC143C 100%);
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(220, 20, 60, 0.4);
}

:deep(.ant-btn) {
  border-radius: 8px;
  display: inline-flex;
  align-items: center;
  gap: 4px;
}

.featured-btn {
  background: linear-gradient(135deg, #fbbf24 0%, #f59e0b 100%);
  border-color: #fbbf24;
  color: #7b4646;
}

.featured-btn:hover {
  background: linear-gradient(135deg, #f59e0b 0%, #fbbf24 100%);
  border-color: #f59e0b;
  color: #943333;
}

:deep(.ant-btn-danger) {
  background: #fff;
  border-color: #ff4d4f;
  color: #ff4d4f;
  border-radius: 8px;
}

:deep(.ant-btn-danger:hover) {
  background: #ff4d4f;
  border-color: #ff4d4f;
  color: #fff;
}

:deep(.ant-tag) {
  border-radius: 6px;
}

:deep(.ant-table-wrapper) {
  border-radius: 0;
}

:deep(.ant-empty-description) {
  color: #999;
}
</style>
