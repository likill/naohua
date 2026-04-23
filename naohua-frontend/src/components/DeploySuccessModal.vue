<template>
  <a-modal v-model:open="visible" title="部署成功" :footer="null" width="600px">
    <div class="deploy-success">
      <div class="success-icon">
        <CheckCircleOutlined style="color: #DC143C; font-size: 48px" />
      </div>
      <h3>网站部署成功！</h3>
      <p>你的网站已经成功部署，可以通过以下链接访问：</p>
      <div class="deploy-url">
        <a-input :value="deployUrl" readonly>
          <template #suffix>
            <a-button type="text" @click="handleCopyUrl">
              <CopyOutlined />
            </a-button>
          </template>
        </a-input>
      </div>
      <div class="deploy-actions">
        <a-button type="primary" @click="handleOpenSite">访问网站</a-button>
        <a-button @click="handleClose">关闭</a-button>
      </div>
    </div>
  </a-modal>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { message } from 'ant-design-vue'
import { CheckCircleOutlined, CopyOutlined } from '@ant-design/icons-vue'

interface Props {
  open: boolean
  deployUrl: string
}

interface Emits {
  (e: 'update:open', value: boolean): void
  (e: 'open-site'): void
}

const props = defineProps<Props>()
const emit = defineEmits<Emits>()

const visible = computed({
  get: () => props.open,
  set: (value) => emit('update:open', value),
})

const handleCopyUrl = async () => {
  try {
    await navigator.clipboard.writeText(props.deployUrl)
    message.success('链接已复制到剪贴板')
  } catch (error) {
    console.error('复制失败：', error)
    message.error('复制失败')
  }
}

const handleOpenSite = () => {
  emit('open-site')
}

const handleClose = () => {
  visible.value = false
}
</script>

<style scoped>
.deploy-success {
  text-align: center;
  padding: 24px;
}

.success-icon {
  margin-bottom: 16px;
}

.success-icon .anticon {
  color: #DC143C;
  font-size: 56px;
}

.deploy-success h3 {
  margin: 0 0 16px;
  font-size: 24px;
  font-weight: 600;
  color: #DC143C;
}

.deploy-success p {
  margin: 0 0 24px;
  color: #666;
  font-size: 15px;
}

.deploy-url {
  margin-bottom: 24px;
}

:deep(.ant-input) {
  border-radius: 8px;
  background: rgba(255, 239, 213, 0.5);
}

.deploy-actions {
  display: flex;
  gap: 12px;
  justify-content: center;
}

:deep(.ant-btn-primary) {
  background: linear-gradient(135deg, #DC143C 0%, #CD5C5C 100%);
  border: none;
  border-radius: 8px;
  height: 36px;
  padding: 0 24px;
}

:deep(.ant-btn-primary:hover) {
  background: linear-gradient(135deg, #CD5C5C 0%, #DC143C 100%);
}

:deep(.ant-btn-default) {
  border-radius: 8px;
  height: 36px;
  padding: 0 24px;
}

:deep(.ant-btn-default:hover) {
  border-color: #DC143C;
  color: #DC143C;
}
</style>
