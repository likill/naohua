<template>
  <div class="app-card" :class="{ 'app-card--featured': featured }">
    <!-- 封面区域 -->
    <div class="app-preview">
      <img v-if="app.cover" :src="app.cover" :alt="app.appName" class="preview-image" />
      <div v-else class="preview-placeholder">
        <div class="placeholder-icon">🚀</div>
        <span class="placeholder-text">AI 生成</span>
      </div>

      <!-- 悬浮操作层 -->
      <div class="app-overlay">
        <div class="overlay-content">
          <a-button type="primary" size="large" @click.stop="handleViewChat">
            <template #icon><CommentOutlined /></template>
            查看对话
          </a-button>
          <a-button v-if="app.deployKey" size="large" @click.stop="handleViewWork">
            <template #icon><EyeOutlined /></template>
            查看作品
          </a-button>
        </div>
      </div>

      <!-- 精选标签 -->
      <div v-if="featured" class="featured-badge">
        <StarFilled /> 精选
      </div>

      <!-- 类型标签 -->
      <div class="type-badge">
        {{ getTypeName(app.codeGenType) }}
      </div>
    </div>

    <!-- 信息区域 -->
    <div class="app-info">
      <div class="info-header">
        <h3 class="app-title">{{ app.appName || '未命名应用' }}</h3>
        <div v-if="app.priority === 99" class="featured-indicator">
          <StarFilled />
        </div>
      </div>

      <p class="app-desc">{{  app.initPrompt || '暂无描述' }}</p>

      <div class="info-footer">
        <div class="author-info">
          <a-avatar :src="app.user?.userAvatar" :size="28" class="author-avatar">
            {{ app.user?.userName?.charAt(0) || 'U' }}
          </a-avatar>
          <span class="author-name">{{ app.user?.userName || (featured ? '官方' : '未知用户') }}</span>
        </div>
        <div class="app-meta">
          <span v-if="app.deployedTime" class="deployed-tag">
            <CheckCircleOutlined /> 已部署
          </span>
          <span v-else class="not-deployed-tag">
            <ClockCircleOutlined /> 待部署
          </span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { CommentOutlined, EyeOutlined, StarFilled, CheckCircleOutlined, ClockCircleOutlined } from '@ant-design/icons-vue'

interface Props {
  app: API.AppVO
  featured?: boolean
}

interface Emits {
  (e: 'view-chat', appId: string | number | undefined): void
  (e: 'view-work', app: API.AppVO): void
}

const props = withDefaults(defineProps<Props>(), {
  featured: false,
})

const emit = defineEmits<Emits>()

const handleViewChat = () => {
  emit('view-chat', props.app.id)
}

const handleViewWork = () => {
  emit('view-work', props.app)
}

const getTypeName = (type: string | undefined) => {
  const typeMap: Record<string, string> = {
    REACT: 'React',
    VUE: 'Vue',
    HTML: 'HTML',
    STATIC: '静态',
  }
  return typeMap[type || ''] || '默认'
}
</script>

<style scoped>
.app-card {
  background: #fff;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.06);
  border: 1px solid rgba(0, 0, 0, 0.04);
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  cursor: pointer;
}

.app-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 20px 50px rgba(220, 20, 60, 0.18);
  border-color: rgba(220, 20, 60, 0.2);
}

/* 封面区域 */
.app-preview {
  height: 200px;
  position: relative;
  overflow: hidden;
  background: linear-gradient(135deg, #1a1a2e 0%, #16213e 100%);
}

.preview-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s cubic-bezier(0.4, 0, 0.2, 1);
}

.app-card:hover .preview-image {
  transform: scale(1.1);
}

.preview-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 12px;
  background: linear-gradient(135deg, #DC143C 0%, #CD5C5C 50%, #DC143C 100%);
}

.placeholder-icon {
  font-size: 56px;
  filter: drop-shadow(0 4px 8px rgba(0, 0, 0, 0.3));
}

.placeholder-text {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.9);
  font-weight: 500;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

/* 悬浮操作层 */
.app-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.7);
  backdrop-filter: blur(4px);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.app-card:hover .app-overlay {
  opacity: 1;
}

.overlay-content {
  display: flex;
  flex-direction: column;
  gap: 12px;
  transform: translateY(20px);
  transition: transform 0.3s ease;
}

.app-card:hover .overlay-content {
  transform: translateY(0);
}

/* 精选标签 */
.featured-badge {
  position: absolute;
  top: 16px;
  left: 16px;
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 6px 14px;
  background: linear-gradient(135deg, #fbbf24 0%, #f59e0b 100%);
  color: #fff;
  font-size: 12px;
  font-weight: 600;
  border-radius: 20px;
  box-shadow: 0 4px 12px rgba(251, 191, 36, 0.4);
  z-index: 2;
}

/* 类型标签 */
.type-badge {
  position: absolute;
  top: 16px;
  right: 16px;
  padding: 4px 10px;
  background: rgba(255, 255, 255, 0.9);
  color: #333;
  font-size: 11px;
  font-weight: 500;
  border-radius: 6px;
  z-index: 2;
}

/* 信息区域 */
.app-info {
  padding: 20px;
}

.info-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 10px;
}

.app-title {
  font-size: 18px;
  font-weight: 600;
  margin: 0;
  color: #1a1a1a;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  flex: 1;
}

.featured-indicator {
  color: #fbbf24;
  font-size: 16px;
  margin-left: 8px;
}

.app-desc {
  font-size: 14px;
  color: #666;
  margin: 0 0 16px;
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  min-height: 42px;
}

.info-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding-top: 12px;
  border-top: 1px solid #f0f0f0;
}

.author-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.author-avatar {
  background: linear-gradient(135deg, #DC143C 0%, #CD5C5C 100%);
  color: #fff;
  font-size: 12px;
}

.author-name {
  font-size: 13px;
  color: #666;
}

.app-meta {
  display: flex;
  align-items: center;
  gap: 8px;
}

.deployed-tag {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
  color: #52c41a;
  background: rgba(82, 196, 26, 0.1);
  padding: 4px 10px;
  border-radius: 12px;
}

.not-deployed-tag {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
  color: #999;
  background: #f5f5f5;
  padding: 4px 10px;
  border-radius: 12px;
}

/* 精选卡片特殊样式 */
.app-card--featured {
  border: 2px solid rgba(251, 191, 36, 0.3);
}

.app-card--featured:hover {
  border-color: rgba(251, 191, 36, 0.5);
}

/* 按钮样式 */
:deep(.ant-btn-primary) {
  background: linear-gradient(135deg, #DC143C 0%, #CD5C5C 100%);
  border: none;
  border-radius: 10px;
  height: 44px;
  padding: 0 24px;
  font-weight: 500;
  box-shadow: 0 4px 15px rgba(220, 20, 60, 0.4);
}

:deep(.ant-btn-primary:hover) {
  background: linear-gradient(135deg, #CD5C5C 0%, #DC143C 100%);
  transform: scale(1.05);
  box-shadow: 0 6px 20px rgba(220, 20, 60, 0.5);
}

:deep(.ant-btn) {
  background: rgba(255, 255, 255, 0.95);
  border: none;
  border-radius: 10px;
  height: 44px;
  padding: 0 24px;
  font-weight: 500;
  color: #333;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
}

:deep(.ant-btn:hover) {
  background: #fff;
  transform: scale(1.05);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.25);
}
</style>
