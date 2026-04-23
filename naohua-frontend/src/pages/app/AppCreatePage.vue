<template>
  <div id="appCreatePage">
    <!-- 顶部栏 -->
    <div class="header-bar">
      <div class="header-left">
        <h1 class="page-title">创建新应用</h1>
      </div>
      <div class="header-right">
        <a-button type="default" @click="goHome">
          <template #icon>
            <HomeOutlined />
          </template>
          返回首页
        </a-button>
      </div>
    </div>

    <!-- 主要内容区域 -->
    <div class="main-content">
      <!-- 左侧侧边栏 -->
      <div class="sidebar">
        <div class="sidebar-header">
          <h3>我的作品</h3>
          <a-button type="primary" size="small" @click="goCreate">
            <template #icon>
              <PlusOutlined />
            </template>
            新建
          </a-button>
        </div>
        <div class="app-list">
          <div
            v-for="app in myAppsList"
            :key="app.id"
            class="app-item"
            @click="app.id && switchApp(app.id)"
          >
            <div class="app-item-icon">
              <AppstoreOutlined />
            </div>
            <div class="app-item-info">
              <div class="app-item-name">{{ app.appName }}</div>
              <div class="app-item-desc">{{ app.initPrompt || '暂无描述' }}</div>
            </div>
          </div>
          <a-empty v-if="myAppsList.length === 0 && !loadingApps" description="暂无作品" />
          <a-spin v-if="loadingApps" />
        </div>
      </div>

      <!-- 左侧创建区域 -->
      <div class="create-section">
        <!-- 消息区域 -->
        <div class="messages-container">
          <div v-if="messages.length === 0" class="empty-state">
            <div class="empty-icon">🚀</div>
            <h3>开始创建你的应用</h3>
            <p>在下方输入框描述你想要的应用功能，AI 将为你生成完整的网站代码</p>
          </div>
          <div v-for="(message, index) in messages" :key="index" class="message-item">
            <div v-if="message.type === 'user'" class="user-message">
              <div class="message-content">{{ message.content }}</div>
              <div class="message-avatar">
                <a-avatar :src="loginUserStore.loginUser.userAvatar" />
              </div>
            </div>
            <div v-else class="ai-message">
              <div class="message-avatar">
                <a-avatar :src="aiAvatar" />
              </div>
              <div class="message-content">
                <MarkdownRenderer v-if="message.content" :content="message.content" />
                <div v-if="message.loading" class="loading-indicator">
                  <a-spin size="small" />
                  <span>AI 正在创建应用...</span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 用户消息输入框 -->
        <div class="input-container">
          <div class="input-wrapper">
            <a-textarea
              v-model:value="userInput"
              :placeholder="getInputPlaceholder()"
              :rows="4"
              :maxlength="1000"
              @keydown.enter.prevent="handleSend"
              :disabled="creating"
            />
            <div class="input-actions">
              <a-button
                type="primary"
                @click="handleSend"
                :loading="creating"
                :disabled="!userInput.trim()"
              >
                <template #icon>
                  <SendOutlined />
                </template>
              </a-button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { useLoginUserStore } from '@/stores/loginUser'
import { addApp } from '@/api/appController'
import { listMyAppVoByPage } from '@/api/appController'
import { API_BASE_URL } from '@/config/env'
import request from '@/request'
import MarkdownRenderer from '@/components/MarkdownRenderer.vue'
import aiAvatar from '@/assets/aiAvatar.png'

import {
  HomeOutlined,
  SendOutlined,
  PlusOutlined,
  AppstoreOutlined,
} from '@ant-design/icons-vue'

const router = useRouter()
const loginUserStore = useLoginUserStore()

// 对话相关
interface Message {
  type: 'user' | 'ai'
  content: string
  loading?: boolean
}

const messages = ref<Message[]>([])
const userInput = ref('')
const creating = ref(false)

// 侧边栏相关
const myAppsList = ref<API.AppVO[]>([])
const loadingApps = ref(false)

// 加载用户的应用列表
const loadMyApps = async () => {
  loadingApps.value = true
  try {
    const res = await listMyAppVoByPage({
      pageNum: 1,
      pageSize: 10,
      sortField: 'createTime',
      sortOrder: 'desc',
    })
    if (res.data.code === 0 && res.data.data) {
      myAppsList.value = res.data.data.records || []
    }
  } catch (error) {
    console.error('加载我的应用失败：', error)
  } finally {
    loadingApps.value = false
  }
}

// 切换应用
const switchApp = (appId: number) => {
  router.push(`/app/chat/${appId}`)
}

// 返回首页
const goHome = () => {
  router.push('/')
}

// 跳转到创建页面
const goCreate = () => {
  // 已经在创建页面，不需要操作
}

// 获取输入框占位符
const getInputPlaceholder = () => {
  return '描述你想要的应用，例如：帮我创建一个个人博客网站，包含文章列表、详情页、分类标签...'
}

// 发送消息
const handleSend = async () => {
  if (!userInput.value.trim() || creating.value) {
    return
  }

  // 检查登录状态
  if (!loginUserStore.loginUser.id) {
    message.warning('请先登录')
    await router.push('/user/login')
    return
  }

  const prompt = userInput.value.trim()
  userInput.value = ''

  // 添加用户消息
  messages.value.push({
    type: 'user',
    content: prompt,
  })

  // 添加AI消息占位符
  const aiMessageIndex = messages.value.length
  messages.value.push({
    type: 'ai',
    content: '',
    loading: true,
  })

  // 开始创建应用
  creating.value = true

  let appId = ''

  try {
    // 第一步：创建应用
    const res = await addApp({
      initPrompt: prompt,
    })

    if (res.data.code === 0 && res.data.data) {
      appId = String(res.data.data)
      messages.value[aiMessageIndex].content = '应用创建成功，正在生成网站...'
    } else {
      message.error('创建失败：' + res.data.message)
      messages.value[aiMessageIndex].content = '创建失败：' + res.data.message
      messages.value[aiMessageIndex].loading = false
      creating.value = false
      return
    }
  } catch (error) {
    console.error('创建应用失败：', error)
    message.error('创建失败，请重试')
    messages.value[aiMessageIndex].content = '创建失败，请重试'
    messages.value[aiMessageIndex].loading = false
    creating.value = false
    return
  }

  // 第二步：调用 chatToGenCode 生成网站
  await generateCode(appId, prompt, aiMessageIndex)

  creating.value = false
}

// 生成代码 - 使用 EventSource 处理流式响应
const generateCode = async (appId: string, userMessage: string, aiMessageIndex: number) => {
  let eventSource: EventSource | null = null
  let streamCompleted = false

  try {
    // 获取 axios 配置的 baseURL
    const baseURL = request.defaults.baseURL || API_BASE_URL

    // 构建URL参数
    const params = new URLSearchParams({
      appId: appId || '',
      message: userMessage,
    })

    const url = `${baseURL}/app/chat/gen/code?${params}`

    // 创建 EventSource 连接
    eventSource = new EventSource(url, {
      withCredentials: true,
    })

    let fullContent = ''

    // 处理接收到的消息
    eventSource.onmessage = function (event) {
      if (streamCompleted) return

      try {
        // 解析JSON包装的数据
        const parsed = JSON.parse(event.data)
        const content = parsed.d

        // 拼接内容
        if (content !== undefined && content !== null) {
          fullContent += content
          messages.value[aiMessageIndex].content = fullContent
          messages.value[aiMessageIndex].loading = false
        }
      } catch (error) {
        console.error('解析消息失败:', error)
      }
    }

    // 处理done事件
    eventSource.addEventListener('done', function () {
      if (streamCompleted) return

      streamCompleted = true
      eventSource?.close()

      messages.value[aiMessageIndex].loading = false
      message.success('应用创建成功')
      // 跳转到聊天页面
      router.push(`/app/chat/${appId}`)
    })

    // 处理business-error事件
    eventSource.addEventListener('business-error', function (event: MessageEvent) {
      if (streamCompleted) return

      try {
        const errorData = JSON.parse(event.data)
        const errorMessage = errorData.message || '生成过程中出现错误'
        messages.value[aiMessageIndex].content = `❌ ${errorMessage}`
        messages.value[aiMessageIndex].loading = false
        message.error(errorMessage)

        streamCompleted = true
        eventSource?.close()
      } catch (parseError) {
        console.error('解析错误事件失败:', parseError)
        messages.value[aiMessageIndex].content = '❌ 生成过程中出现错误'
        messages.value[aiMessageIndex].loading = false
      }
    })

    // 处理错误
    eventSource.onerror = function () {
      if (streamCompleted) return
      streamCompleted = true
      eventSource?.close()
      messages.value[aiMessageIndex].loading = false
    }
  } catch (error) {
    console.error('创建 EventSource 失败：', error)
    messages.value[aiMessageIndex].content = '❌ 生成过程中出现错误，请重试'
    messages.value[aiMessageIndex].loading = false
  }
}

// 页面加载时获取数据
onMounted(() => {
  loadMyApps()
})
</script>

<style scoped>
#appCreatePage {
  height: 100vh;
  display: flex;
  flex-direction: column;
  padding: 16px;
  background: linear-gradient(135deg, rgba(220, 20, 60, 0.03) 0%, rgba(205, 92, 92, 0.05) 100%);
}

/* 顶部栏 */
.header-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 20px;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  backdrop-filter: blur(10px);
  margin-bottom: 16px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.page-title {
  margin: 0;
  font-size: 20px;
  font-weight: 600;
  color: #DC143C;
}

.header-right {
  display: flex;
  gap: 12px;
}

:deep(.ant-btn-default) {
  border-radius: 8px;
  border-color: #e8e8e8;
}

:deep(.ant-btn-default:hover) {
  border-color: #DC143C;
  color: #DC143C;
}

:deep(.ant-btn-primary) {
  background: linear-gradient(135deg, #DC143C 0%, #CD5C5C 100%);
  border: none;
  border-radius: 8px;
}

:deep(.ant-btn-primary:hover) {
  background: linear-gradient(135deg, #CD5C5C 0%, #DC143C 100%);
}

/* 主要内容区域 */
.main-content {
  flex: 1;
  display: flex;
  gap: 16px;
  overflow: hidden;
}

/* 侧边栏 */
.sidebar {
  width: 260px;
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  overflow: hidden;
}

.sidebar-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  border-bottom: 1px solid rgba(220, 20, 60, 0.1);
}

.sidebar-header h3 {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
  color: #DC143C;
}

.app-list {
  flex: 1;
  overflow-y: auto;
  padding: 8px;
}

.app-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
  margin-bottom: 4px;
}

.app-item:hover {
  background: rgba(220, 20, 60, 0.08);
}

.app-item-icon {
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, rgba(220, 20, 60, 0.1) 0%, rgba(205, 92, 92, 0.1) 100%);
  border-radius: 8px;
  color: #DC143C;
  font-size: 18px;
}

.app-item-info {
  flex: 1;
  min-width: 0;
}

.app-item-name {
  font-size: 14px;
  font-weight: 500;
  color: #333;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.app-item-desc {
  font-size: 12px;
  color: #999;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  margin-top: 2px;
}

/* 创建区域 */
.create-section {
  flex: 1;
  display: flex;
  flex-direction: column;
  background: white;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  overflow: hidden;
}

.messages-container {
  flex: 1;
  padding: 24px;
  overflow-y: auto;
  scroll-behavior: smooth;
  background: linear-gradient(180deg, rgba(255, 255, 255, 1) 0%, rgba(255, 239, 213, 0.3) 100%);
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  text-align: center;
  color: #666;
}

.empty-icon {
  font-size: 64px;
  margin-bottom: 24px;
}

.empty-state h3 {
  margin: 0 0 12px;
  font-size: 24px;
  color: #DC143C;
}

.empty-state p {
  margin: 0;
  font-size: 16px;
  max-width: 400px;
  line-height: 1.6;
}

.message-item {
  margin-bottom: 16px;
}

.user-message {
  display: flex;
  justify-content: flex-end;
  align-items: flex-start;
  gap: 8px;
}

.ai-message {
  display: flex;
  justify-content: flex-start;
  align-items: flex-start;
  gap: 8px;
}

.message-content {
  max-width: 70%;
  padding: 12px 16px;
  border-radius: 16px;
  line-height: 1.5;
  word-wrap: break-word;
}

.user-message .message-content {
  background: linear-gradient(135deg, #DC143C 0%, #CD5C5C 100%);
  color: white;
  border-bottom-right-radius: 4px;
}

.ai-message .message-content {
  background: linear-gradient(135deg, rgba(220, 20, 60, 0.08) 0%, rgba(205, 92, 92, 0.1) 100%);
  color: #1a1a1a;
  padding: 8px 12px;
  border-bottom-left-radius: 4px;
}

.message-avatar {
  flex-shrink: 0;
}

.loading-indicator {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #DC143C;
}

/* 输入区域 */
.input-container {
  padding: 16px;
  background: rgba(255, 239, 213, 0.5);
  border-top: 1px solid rgba(220, 20, 60, 0.1);
}

.input-wrapper {
  position: relative;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.input-wrapper :deep(.ant-input) {
  padding-right: 50px;
  border: none;
  border-radius: 12px;
}

.input-wrapper :deep(.ant-input:focus) {
  box-shadow: none;
}

.input-actions {
  position: absolute;
  bottom: 8px;
  right: 8px;
}

:deep(.input-actions .ant-btn-primary) {
  width: 36px;
  height: 36px;
  padding: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .sidebar {
    display: none;
  }
}

@media (max-width: 768px) {
  .header-bar {
    padding: 12px 16px;
  }

  .page-title {
    font-size: 16px;
  }

  .message-content {
    max-width: 85%;
  }
}
</style>
