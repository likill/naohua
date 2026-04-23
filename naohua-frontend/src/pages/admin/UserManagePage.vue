<template>
  <div id="userManagePage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <div class="header-icon">
          <UserOutlined />
        </div>
        <div class="header-text">
          <h1 class="page-title">用户管理</h1>
          <p class="page-desc">管理系统用户信息</p>
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
        <a-form-item label="账号">
          <a-input
            v-model:value="searchParams.userAccount"
            placeholder="输入账号"
            allow-clear
            class="search-input"
          />
        </a-form-item>
        <a-form-item label="用户名">
          <a-input
            v-model:value="searchParams.userName"
            placeholder="输入用户名"
            allow-clear
            class="search-input"
          />
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
        <span class="table-count">共 {{ total }} 个用户</span>
      </div>
      <a-table
        :columns="columns"
        :data-source="data"
        :pagination="pagination"
        :loading="loading"
        @change="doTableChange"
        :row-key="(record: API.UserVO) => record.id"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.dataIndex === 'userAvatar'">
            <a-avatar
              v-if="record.userAvatar"
              :src="record.userAvatar"
              :size="48"
              class="user-avatar"
            />
            <a-avatar v-else :size="48" class="user-avatar-default">
              <template #icon><UserOutlined /></template>
            </a-avatar>
          </template>
          <template v-else-if="column.dataIndex === 'userRole'">
            <a-tag :color="record.userRole === 'admin' ? 'green' : 'blue'" class="role-tag">
              <template #icon>
                <SafetyCertificateOutlined v-if="record.userRole === 'admin'" />
              </template>
              {{ record.userRole === 'admin' ? '管理员' : '普通用户' }}
            </a-tag>
          </template>
          <template v-else-if="column.dataIndex === 'userProfile'">
            <a-tooltip :title="record.userProfile || '暂无简介'">
              <div class="profile-text">
                {{ record.userProfile || '暂无简介' }}
              </div>
            </a-tooltip>
          </template>
          <template v-else-if="column.dataIndex === 'createTime'">
            <span class="time-text">
              {{ dayjs(record.createTime).format('YYYY-MM-DD HH:mm') }}
            </span>
          </template>
          <template v-else-if="column.key === 'action'">
            <a-popconfirm
              title="确定要删除这个用户吗？"
              @confirm="doDelete(record.id)"
              placement="topRight"
            >
              <a-button danger size="small" :disabled="record.userRole === 'admin'">
                <template #icon><DeleteOutlined /></template>
                删除
              </a-button>
            </a-popconfirm>
          </template>
        </template>
      </a-table>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { deleteUser, listUserVoByPage } from '@/api/userController.ts'
import { message } from 'ant-design-vue'
import dayjs from 'dayjs'
import {
  UserOutlined,
  SearchOutlined,
  ReloadOutlined,
  ClearOutlined,
  DeleteOutlined,
  SafetyCertificateOutlined,
} from '@ant-design/icons-vue'

const loading = ref(false)

const columns = [
  {
    title: 'ID',
    dataIndex: 'id',
    width: 80,
  },
  {
    title: '账号',
    dataIndex: 'userAccount',
    width: 120,
  },
  {
    title: '用户名',
    dataIndex: 'userName',
    width: 120,
  },
  {
    title: '头像',
    dataIndex: 'userAvatar',
    width: 80,
  },
  {
    title: '简介',
    dataIndex: 'userProfile',
    width: 200,
    ellipsis: true,
  },
  {
    title: '用户角色',
    dataIndex: 'userRole',
    width: 100,
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 160,
  },
  {
    title: '操作',
    key: 'action',
    width: 100,
    fixed: 'right',
  },
]

const data = ref<API.UserVO[]>([])
const total = ref(0)

const searchParams = reactive<API.UserQueryRequest>({
  pageNum: 1,
  pageSize: 10,
})

const fetchData = async () => {
  loading.value = true
  try {
    const res = await listUserVoByPage({
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
  searchParams.userAccount = undefined
  searchParams.userName = undefined
  searchParams.pageNum = 1
  fetchData()
}

const doDelete = async (id: string | undefined) => {
  if (!id) return
  const res = await deleteUser({ id: Number(id) })
  if (res.data.code === 0) {
    message.success('删除成功')
    fetchData()
  } else {
    message.error('删除失败：' + res.data.message)
  }
}
</script>

<style scoped>
#userManagePage {
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
  width: 160px;
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

/* 用户头像 */
.user-avatar {
  border: 2px solid #f0f0f0;
}

.user-avatar-default {
  background: linear-gradient(135deg, #DC143C 0%, #CD5C5C 100%);
  color: #fff;
}

/* 角色标签 */
.role-tag {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  border-radius: 6px;
}

/* 简介文字 */
.profile-text {
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

/* 按钮样式 */
:deep(.ant-btn-primary) {
  background: linear-gradient(135deg, #DC143C 0%, #CD5C5C 100%);
  border: none;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(220, 20, 60, 0.3);
  display: inline-flex;
  align-items: center;
  gap: 4px;
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

:deep(.ant-btn-danger:disabled) {
  background: #f5f5f5;
  border-color: #d9d9d9;
  color: #bfbfbf;
}

:deep(.ant-empty-description) {
  color: #999;
}
</style>
