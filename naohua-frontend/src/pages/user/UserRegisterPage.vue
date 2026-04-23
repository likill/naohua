<template>
  <div id="userRegisterPage">
    <div class="register-wrapper">
      <!-- 左侧品牌区域 -->
      <div class="brand-side">
        <div class="brand-content">
          <img src="@/assets/logo.png" alt="Logo" class="brand-logo" />
          <h1 class="brand-title">脑花</h1>
          <p class="brand-slogan">让创意落地</p>
          <div class="feature-list">
            <div class="feature-item">
              <svg class="feature-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M12 2L2 7l10 5 10-5-10-5zM2 17l10 5 10-5M2 12l10 5 10-5"/>
              </svg>
              <span>AI 智能生成应用</span>
            </div>
            <div class="feature-item">
              <svg class="feature-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M12 2L2 7l10 5 10-5-10-5zM2 17l10 5 10-5M2 12l10 5 10-5"/>
              </svg>
              <span>实时预览效果</span>
            </div>
            <div class="feature-item">
              <svg class="feature-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M12 2L2 7l10 5 10-5-10-5zM2 17l10 5 10-5M2 12l10 5 10-5"/>
              </svg>
              <span>一键部署上线</span>
            </div>
          </div>
        </div>
        <!-- 装饰元素 -->
        <div class="deco-circle deco-1"></div>
        <div class="deco-circle deco-2"></div>
        <div class="deco-circle deco-3"></div>
        <div class="deco-dots"></div>
      </div>

      <!-- 右侧表单区域 -->
      <div class="form-side">
        <div class="form-card">
          <div class="form-header">
            <h2 class="form-title">创建账户</h2>
            <p class="form-subtitle">开启您的创意之旅</p>
          </div>

          <a-form :model="formState" name="basic" autocomplete="off" @finish="handleSubmit">
            <a-form-item name="userAccount" :rules="[{ required: true, message: '请输入账号' }]">
              <a-input v-model:value="formState.userAccount" placeholder="请输入账号" size="large">
                <template #prefix>
                  <svg class="input-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/>
                    <circle cx="12" cy="7" r="4"/>
                  </svg>
                </template>
              </a-input>
            </a-form-item>
            <a-form-item
              name="userPassword"
              :rules="[
                { required: true, message: '请输入密码' },
                { min: 8, message: '密码不能小于 8 位' },
              ]"
            >
              <a-input-password v-model:value="formState.userPassword" placeholder="请输入密码" size="large">
                <template #prefix>
                  <svg class="input-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <rect x="3" y="11" width="18" height="11" rx="2" ry="2"/>
                    <path d="M7 11V7a5 5 0 0 1 10 0v4"/>
                  </svg>
                </template>
              </a-input-password>
            </a-form-item>
            <a-form-item
              name="checkPassword"
              :rules="[
                { required: true, message: '请确认密码' },
                { min: 8, message: '密码不能小于 8 位' },
                { validator: validateCheckPassword },
              ]"
            >
              <a-input-password v-model:value="formState.checkPassword" placeholder="请确认密码" size="large">
                <template #prefix>
                  <svg class="input-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <rect x="3" y="11" width="18" height="11" rx="2" ry="2"/>
                    <path d="M7 11V7a5 5 0 0 1 10 0v4"/>
                  </svg>
                </template>
              </a-input-password>
            </a-form-item>

            <div class="tips">
              已有账号？
              <RouterLink to="/user/login" class="link">立即登录</RouterLink>
            </div>

            <a-form-item>
              <a-button type="primary" html-type="submit" class="submit-btn">注 册</a-button>
            </a-form-item>
          </a-form>
        </div>
      </div>
    </div>
  </div>
</template>


<script setup lang="ts">
import { useRouter } from 'vue-router'
import { userRegister } from '@/api/userController.ts'
import { message } from 'ant-design-vue'
import { reactive } from 'vue'

const router = useRouter()

const formState = reactive<API.UserRegisterRequest>({
  userAccount: '',
  userPassword: '',
  checkPassword: '',
})

const validateCheckPassword = (rule: unknown, value: string, callback: (error?: Error) => void) => {
  if (value && value !== formState.userPassword) {
    callback(new Error('两次输入密码不一致'))
  } else {
    callback()
  }
}

const handleSubmit = async (values: API.UserRegisterRequest) => {
  const res = await userRegister(values)
  if (res.data.code === 0) {
    message.success('注册成功')
    router.push({
      path: '/user/login',
      replace: true,
    })
  } else {
    message.error('注册失败，' + res.data.message)
  }
}
</script>

<style scoped>
#userRegisterPage {
  height: 100vh;
  width: 100vw;
  background: #FFEFD5;
  overflow: hidden;
}

.register-wrapper {
  display: flex;
  height: 100%;
  overflow: hidden;
}

/* ========== 左侧品牌区域 ========== */
.brand-side {
  flex: 1;
  background: linear-gradient(135deg, #DC143C 0%, #B83232 50%, #CD5C5C 100%);
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  height: 100%;
  min-height: 100%;
}

.brand-content {
  position: relative;
  z-index: 2;
  text-align: center;
  color: #fff;
  padding: 40px;
}

.brand-logo {
  width: 80px;
  height: 80px;
  border-radius: 20px;
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.3);
  margin-bottom: 20px;
  animation: float 3s ease-in-out infinite;
}

@keyframes float {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-10px); }
}

.brand-title {
  font-size: 48px;
  font-weight: 800;
  margin: 0 0 8px;
  letter-spacing: 8px;
  text-shadow: 2px 2px 8px rgba(0, 0, 0, 0.2);
}

.brand-slogan {
  font-size: 18px;
  opacity: 0.9;
  margin: 0 0 40px;
  letter-spacing: 4px;
}

.feature-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
  text-align: left;
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 16px;
  opacity: 0.95;
}

.feature-icon {
  width: 24px;
  height: 24px;
  opacity: 0.9;
}

/* 装饰圆形 */
.deco-circle {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.08);
  pointer-events: none;
}

.deco-1 {
  width: 300px;
  height: 300px;
  top: -80px;
  left: -80px;
  animation: pulse 4s ease-in-out infinite;
}

.deco-2 {
  width: 200px;
  height: 200px;
  bottom: -60px;
  right: -60px;
  animation: pulse 4s ease-in-out infinite 1s;
}

.deco-3 {
  width: 120px;
  height: 120px;
  top: 50%;
  left: 20%;
  transform: translateY(-50%);
  animation: pulse 4s ease-in-out infinite 2s;
}

@keyframes pulse {
  0%, 100% { opacity: 0.5; transform: scale(1); }
  50% { opacity: 0.8; transform: scale(1.05); }
}

/* 点状装饰 */
.deco-dots {
  position: absolute;
  width: 100%;
  height: 100%;
  background-image: radial-gradient(rgba(255,255,255,0.15) 1px, transparent 1px);
  background-size: 30px 30px;
  pointer-events: none;
}

/* ========== 右侧表单区域 ========== */
.form-side {
  width: 520px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px;
  background: linear-gradient(180deg, #FFF5F5 0%, #FFFFFF 50%, #FFEFEF 100%);
  height: 100%;
  overflow: hidden;
}

.form-card {
  width: 100%;
  max-width: 400px;
  background: #fff;
  border-radius: 20px;
  padding: 40px;
  box-shadow: 0 8px 40px rgba(220, 20, 60, 0.12);
  animation: slideUp 0.6s ease-out;
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 覆盖全局 ant-form 背景色 */
:deep(.ant-form) {
  background: transparent !important;
}

.form-header {
  margin-bottom: 32px;
}

.form-title {
  font-size: 28px;
  font-weight: 700;
  color: #DC143C;
  margin: 0 0 8px;
}

.form-subtitle {
  font-size: 15px;
  color: #666;
  margin: 0;
}

/* 输入框样式 */
:deep(.ant-input) {
  border-radius: 12px;
  border: 2px solid #eee;
  padding: 12px 16px 12px 44px;
  font-size: 15px;
  height: 48px;
  transition: all 0.3s ease;
}

:deep(.ant-input-affix-wrapper) {
  border-radius: 12px;
  border: 2px solid #eee;
  padding: 4px 16px 4px 44px;
  height: 48px;
  transition: all 0.3s ease;
}

:deep(.ant-input:hover),
:deep(.ant-input-affix-wrapper:hover) {
  border-color: #CD5C5C;
}

:deep(.ant-input:focus),
:deep(.ant-input-affix-wrapper:focus),
:deep(.ant-input-affix-wrapper-focused) {
  border-color: #DC143C;
  box-shadow: 0 0 0 3px rgba(220, 20, 60, 0.1);
}

:deep(.ant-input-affix-wrapper .ant-input) {
  border: none;
  padding: 0;
  height: auto;
}

:deep(.ant-input-affix-wrapper .ant-input:focus) {
  box-shadow: none;
}

:deep(.ant-input-prefix) {
  margin-right: 8px;
}

.input-icon {
  width: 20px;
  height: 20px;
  color: #999;
  transition: color 0.3s;
}

:deep(.ant-input:hover .input-icon),
:deep(.ant-input:focus ~ .input-icon),
:deep(.ant-input-affix-wrapper:hover .input-icon) {
  color: #DC143C;
}

/* 密码眼睛图标 */
:deep(.ant-input-password-icon) {
  color: #999;
  transition: color 0.3s;
}

:deep(.ant-input-password-icon:hover) {
  color: #DC143C;
}

/* 提示链接 */
.tips {
  margin-bottom: 24px;
  color: #666;
  font-size: 14px;
  text-align: center;
}

.link {
  color: #DC143C;
  text-decoration: none;
  font-weight: 600;
  transition: color 0.3s;
}

.link:hover {
  color: #CD5C5C;
  text-decoration: underline;
}

/* 提交按钮 */
.submit-btn {
  width: 100%;
  height: 52px;
  background: linear-gradient(135deg, #DC143C 0%, #CD5C5C 100%);
  border: none;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
  letter-spacing: 2px;
  box-shadow: 0 4px 20px rgba(220, 20, 60, 0.35);
  transition: all 0.3s ease;
}

.submit-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 30px rgba(220, 20, 60, 0.45);
  background: linear-gradient(135deg, #CD5C5C 0%, #DC143C 100%);
}

.submit-btn:active {
  transform: translateY(0);
}

/* ========== 响应式 ========== */
@media (max-width: 900px) {
  .register-wrapper {
    flex-direction: column;
    height: 100%;
  }

  .brand-side {
    min-height: auto;
    padding: 40px 20px;
    height: auto;
    flex: none;
  }

  .brand-content {
    padding: 20px;
  }

  .brand-title {
    font-size: 36px;
  }

  .feature-list {
    display: none;
  }

  .form-side {
    width: 100%;
    flex: 1;
    padding: 30px 20px;
    overflow-y: auto;
    background: linear-gradient(180deg, #FFF5F5 0%, #FFFFFF 100%);
  }

  .form-card {
    max-width: 100%;
  }
}

@media (max-width: 480px) {
  .brand-side {
    padding: 40px 20px;
  }

  .brand-logo {
    width: 64px;
    height: 64px;
  }

  .brand-title {
    font-size: 28px;
    letter-spacing: 4px;
  }

  .form-card {
    animation: none;
  }
}
</style>
