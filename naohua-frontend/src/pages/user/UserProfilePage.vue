<template>
  <div class="profile-page">
    <!-- 顶部栏 -->
    <div class="header-bar">
      <div class="header-left">
        <a-button type="default" @click="goBack">
          <template #icon>
            <LeftOutlined />
          </template>
          返回
        </a-button>
      </div>
      <h1 class="page-title">个人主页</h1>
      <div class="header-right"></div>
    </div>

    <!-- 主要内容 -->
    <div class="main-content">
      <!-- 用户信息卡片 -->
      <div class="profile-card">
        <div class="card-corner tl"></div>
        <div class="card-corner tr"></div>
        <div class="card-corner bl"></div>
        <div class="card-corner br"></div>

        <div class="avatar-section">
          <div class="avatar-wrapper">
            <img
              v-if="loginUserStore.loginUser.userAvatar"
              :src="loginUserStore.loginUser.userAvatar"
              class="pixel-avatar"
              alt="avatar"
            />
            <div v-else class="pixel-avatar-default">
              <span>👤</span>
            </div>
          </div>
          <div class="user-info">
            <h2 class="username">{{ loginUserStore.loginUser.userName || '未登录用户' }}</h2>
            <div class="user-role">
              <span class="role-badge" :class="loginUserStore.loginUser.userRole">
                {{ getRoleText(loginUserStore.loginUser.userRole) }}
              </span>
            </div>
            <div class="join-date" v-if="loginUserStore.loginUser.createTime">
              <span>加入于 {{ formatDate(loginUserStore.loginUser.createTime) }}</span>
            </div>
          </div>
        </div>

        <div class="stats-section">
          <div class="stat-item">
            <div class="stat-value">{{ stats.appCount }}</div>
            <div class="stat-label">应用数量</div>
          </div>
          <div class="stat-divider"></div>
          <div class="stat-item">
            <div class="stat-value">{{ stats.chatCount }}</div>
            <div class="stat-label">对话次数</div>
          </div>
          <div class="stat-divider"></div>
          <div class="stat-item">
            <div class="stat-value">{{ stats.likeCount }}</div>
            <div class="stat-label">获赞次数</div>
          </div>
        </div>
      </div>

      <!-- 我的应用列表 -->
      <div class="apps-section">
        <div class="section-header">
          <h3 class="section-title">我的作品</h3>
          <a-button type="primary" @click="goToCreate">
            <template #icon>
              <PlusOutlined />
            </template>
            创建新应用
          </a-button>
        </div>

        <div class="apps-grid" v-if="myApps.length > 0">
          <AppCard
            v-for="app in myApps"
            :key="app.id"
            :app="app"
            @view-chat="viewChat"
            @view-work="viewWork"
          />
        </div>
        <a-empty v-else description="暂无作品" class="empty-state" />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useLoginUserStore } from '@/stores/loginUser'
import { listMyAppVoByPage } from '@/api/appController'
import { getDeployUrl } from '@/config/env'
import AppCard from '@/components/AppCard.vue'
import { LeftOutlined, PlusOutlined } from '@ant-design/icons-vue'

const router = useRouter()
const loginUserStore = useLoginUserStore()

const myApps = ref<API.AppVO[]>([])

const stats = ref({
  appCount: 0,
  chatCount: 0,
  likeCount: 0,
})

const getRoleText = (role: string | undefined) => {
  const roleMap: Record<string, string> = {
    admin: '管理员',
    user: '普通用户',
    vip: 'VIP会员',
  }
  return roleMap[role || ''] || '普通用户'
}

const formatDate = (dateStr: string | undefined) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return `${date.getFullYear()}年${date.getMonth() + 1}月${date.getDate()}日`
}

const loadMyApps = async () => {
  try {
    const res = await listMyAppVoByPage({
      pageNum: 1,
      pageSize: 10,
      sortField: 'createTime',
      sortOrder: 'desc',
    })
    if (res.data.code === 0 && res.data.data) {
      myApps.value = res.data.data.records || []
      stats.value.appCount = res.data.data.totalRow || 0
    }
  } catch (error) {
    console.error('加载我的应用失败：', error)
  }
}

const viewChat = (appId: string | number | undefined) => {
  if (appId) {
    router.push(`/app/chat/${appId}`)
  }
}

const viewWork = (app: API.AppVO) => {
  if (app.deployKey) {
    const url = getDeployUrl(app.deployKey)
    window.open(url, '_blank')
  }
}

const goBack = () => {
  router.back()
}

const goToCreate = () => {
  router.push('/app/create')
}

onMounted(() => {
  loadMyApps()
})
</script>

<style scoped>
.profile-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #DC143C 0%, #CD5C5C 50%, #DC143C 100%);
  padding: 16px;
}

.header-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 20px;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  margin-bottom: 16px;
}

.header-left,
.header-right {
  width: 100px;
}

.page-title {
  margin: 0;
  font-size: 20px;
  font-weight: 600;
  color: #DC143C;
  font-family: 'Courier New', monospace;
  text-shadow: 2px 2px 0 rgba(0, 0, 0, 0.1);
}

:deep(.ant-btn-default) {
  border-radius: 8px;
  border: 2px solid #333;
  box-shadow: 2px 2px 0 #333;
  font-family: 'Courier New', monospace;
}

:deep(.ant-btn-default:hover) {
  border-color: #DC143C;
  color: #DC143C;
}

.main-content {
  max-width: 1200px;
  margin: 0 auto;
}

/* 个人信息卡片 - 像素风格 */
.profile-card {
  position: relative;
  background: rgba(255, 255, 255, 0.95);
  border: 3px solid #333;
  box-shadow:
    6px 6px 0 #333,
    inset -3px -3px 0 #999,
    inset 3px 3px 0 #fff;
  padding: 24px;
  margin-bottom: 24px;
  image-rendering: pixelated;
}

.card-corner {
  position: absolute;
  width: 12px;
  height: 12px;
  background: #DC143C;
}

.card-corner.tl {
  top: -3px;
  left: -3px;
  box-shadow:
    12px 0 0 #DC143C,
    0 12px 0 #DC143C;
}

.card-corner.tr {
  top: -3px;
  right: -3px;
  box-shadow:
    -12px 0 0 #DC143C,
    0 12px 0 #DC143C;
}

.card-corner.bl {
  bottom: -3px;
  left: -3px;
  box-shadow:
    12px 0 0 #DC143C,
    0 -12px 0 #DC143C;
}

.card-corner.br {
  bottom: -3px;
  right: -3px;
  box-shadow:
    -12px 0 0 #DC143C,
    0 -12px 0 #DC143C;
}

.avatar-section {
  display: flex;
  align-items: center;
  gap: 20px;
  margin-bottom: 24px;
}

.avatar-wrapper {
  position: relative;
  width: 80px;
  height: 80px;
}

.pixel-avatar {
  width: 72px;
  height: 72px;
  border: 3px solid #333;
  box-shadow: 3px 3px 0 #333;
  object-fit: cover;
  image-rendering: pixelated;
}

.pixel-avatar-default {
  width: 72px;
  height: 72px;
  border: 3px solid #333;
  box-shadow: 3px 3px 0 #333;
  background: #FFEFD5;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 36px;
  image-rendering: pixelated;
}

.user-info {
  flex: 1;
}

.username {
  font-family: 'Courier New', monospace;
  font-size: 24px;
  font-weight: bold;
  color: #333;
  text-shadow: 2px 2px 0 #999;
  margin: 0 0 8px 0;
}

.user-role {
  margin-bottom: 8px;
}

.role-badge {
  display: inline-block;
  font-family: 'Courier New', monospace;
  font-size: 12px;
  font-weight: bold;
  padding: 4px 12px;
  border: 2px solid #333;
  box-shadow: 2px 2px 0 #333;
  text-transform: uppercase;
}

.role-badge.admin {
  background: #DC143C;
  color: #fff;
}

.role-badge.user {
  background: #4ade80;
  color: #333;
}

.role-badge.vip {
  background: #fbbf24;
  color: #333;
}

.join-date {
  font-family: 'Courier New', monospace;
  font-size: 12px;
  color: #666;
}

.stats-section {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 32px;
  padding-top: 16px;
  border-top: 2px dashed #ccc;
}

.stat-item {
  text-align: center;
}

.stat-value {
  font-family: 'Courier New', monospace;
  font-size: 32px;
  font-weight: bold;
  color: #DC143C;
  text-shadow: 2px 2px 0 #999;
}

.stat-label {
  font-family: 'Courier New', monospace;
  font-size: 12px;
  color: #666;
  text-transform: uppercase;
  margin-top: 4px;
}

.stat-divider {
  width: 2px;
  height: 40px;
  background: #ccc;
}

/* 应用列表 */
.apps-section {
  background: rgba(255, 255, 255, 0.95);
  border: 3px solid #333;
  box-shadow:
    6px 6px 0 #333,
    inset -3px -3px 0 #999,
    inset 3px 3px 0 #fff;
  padding: 20px;
  image-rendering: pixelated;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 12px;
  border-bottom: 2px solid #333;
}

.section-title {
  font-family: 'Courier New', monospace;
  font-size: 18px;
  font-weight: bold;
  color: #333;
  text-shadow: 1px 1px 0 #999;
  margin: 0;
}

:deep(.ant-btn-primary) {
  background: #DC143C;
  border: 2px solid #333;
  box-shadow:
    2px 2px 0 #333,
    inset -1px -1px 0 #8B0000,
    inset 1px 1px 0 #FF6B6B;
  font-family: 'Courier New', monospace;
  height: auto;
  padding: 6px 16px;
}

:deep(.ant-btn-primary:hover) {
  background: #CD5C5C;
  transform: translate(-1px, -1px);
  box-shadow:
    3px 3px 0 #333,
    inset -1px -1px 0 #8B0000,
    inset 1px 1px 0 #FF6B6B;
}

.apps-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 16px;
}

.empty-state {
  padding: 40px;
}
</style>
