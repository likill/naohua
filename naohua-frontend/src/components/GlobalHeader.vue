<template>
  <a-layout-header class="header">
    <div class="header-container">
      <!-- 左侧：Logo -->
      <RouterLink to="/" class="logo-area">
        <img class="logo" src="@/assets/logo.png" alt="Logo" />
        <span class="logo-text">脑花</span>
      </RouterLink>

      <!-- 中间：导航菜单 -->
      <nav class="nav-menu">
        <div
          v-for="item in visibleMenuItems"
          :key="item.key"
          class="nav-item"
          :class="{ active: isActive(item.key) }"
          @click="handleNavClick(item.key)"
        >
          <component :is="item.icon" class="nav-icon" />
          <span class="nav-label">{{ item.label }}</span>
        </div>
      </nav>

      <!-- 右侧：用户区域 -->
      <div class="user-area">
        <!-- 已登录 -->
        <template v-if="loginUserStore.loginUser.id">
          <a-dropdown placement="bottomRight" :trigger="['click']">
            <div class="user-card">
              <a-avatar
                :src="loginUserStore.loginUser.userAvatar"
                :size="32"
                class="user-avatar"
              >
                {{ loginUserStore.loginUser.userName?.charAt(0) || 'U' }}
              </a-avatar>
              <div class="user-info">
                <span class="user-name">{{ loginUserStore.loginUser.userName || '用户' }}</span>
                <span class="user-role">
                  <span class="role-dot" :class="loginUserStore.loginUser.userRole"></span>
                  {{ getRoleText(loginUserStore.loginUser.userRole) }}
                </span>
              </div>
              <DownOutlined class="dropdown-arrow" />
            </div>
            <template #overlay>
              <a-menu class="user-menu">
                <a-menu-item key="profile" @click="goToProfile">
                  <UserOutlined /> 个人主页
                </a-menu-item>
                <a-menu-divider />
                <a-menu-item key="logout" @click="doLogout" class="logout-item">
                  <LogoutOutlined /> 退出登录
                </a-menu-item>
              </a-menu>
            </template>
          </a-dropdown>
        </template>

        <!-- 未登录 -->
        <template v-else>
          <a-button type="primary" href="/user/login" class="login-btn">
            <template #icon><LoginOutlined /></template>
            登录
          </a-button>
        </template>
      </div>
    </div>
  </a-layout-header>
</template>

<script setup lang="ts">
import { computed, h, ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { message } from 'ant-design-vue'
import { useLoginUserStore } from '@/stores/loginUser.ts'
import { userLogout } from '@/api/userController.ts'
import {
  LogoutOutlined,
  HomeOutlined,
  UserOutlined,
  DownOutlined,
  AppstoreOutlined,
  TeamOutlined,
  LoginOutlined,
} from '@ant-design/icons-vue'

const loginUserStore = useLoginUserStore()
const router = useRouter()
const route = useRoute()

const selectedKey = ref('/')

const menuItems = [
  { key: '/', icon: HomeOutlined, label: '主页' },
  { key: '/admin/userManage', icon: TeamOutlined, label: '用户管理', adminOnly: true },
  { key: '/admin/appManage', icon: AppstoreOutlined, label: '应用管理', adminOnly: true },
]

const visibleMenuItems = computed(() => {
  return menuItems.filter(item => {
    if (item.adminOnly) {
      return loginUserStore.loginUser.userRole === 'admin'
    }
    return true
  })
})

const isActive = (key: string) => {
  return route.path === key
}

const handleNavClick = (key: string) => {
  selectedKey.value = key
  router.push(key)
}

const getRoleText = (role: string | undefined) => {
  const roleMap: Record<string, string> = {
    admin: '管理员',
    user: '普通用户',
    vip: 'VIP',
  }
  return roleMap[role || ''] || '用户'
}

const goToProfile = () => {
  router.push('/user/profile')
}

const doLogout = async () => {
  const res = await userLogout()
  if (res.data.code === 0) {
    loginUserStore.setLoginUser({ userName: '未登录' })
    message.success('退出登录成功')
    await router.push('/user/login')
  } else {
    message.error('退出登录失败，' + res.data.message)
  }
}
</script>

<style scoped>
.header {
  background: rgba(255, 255, 255, 0.1) !important;
  backdrop-filter: blur(20px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.15);
  padding: 0 !important;
  height: 64px;
  line-height: 64px;
}

.header-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 24px;
  display: flex;
  align-items: center;
  height: 100%;
}

/* Logo 区域 */
.logo-area {
  display: flex;
  align-items: center;
  gap: 12px;
  text-decoration: none;
  transition: opacity 0.3s;
}

.logo-area:hover {
  opacity: 0.85;
}

.logo {
  height: 36px;
  width: 36px;
  border-radius: 8px;
  object-fit: cover;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.logo-text {
  font-size: 20px;
  font-weight: 700;
  color: #fff;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
  letter-spacing: 2px;
}

/* 导航菜单 */
.nav-menu {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-left: 48px;
  flex: 1;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 6px 14px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  color: rgba(255, 255, 255, 0.85);
  font-size: 14px;
  font-weight: 500;
}

.nav-item:hover {
  background: rgba(255, 255, 255, 0.15);
  color: #fff;
}

.nav-item.active {
  background: rgba(255, 255, 255, 0.2);
  color: #fff;
  font-weight: 600;
}

.nav-icon {
  font-size: 16px;
}

.nav-label {
  letter-spacing: 0.5px;
}

/* 用户区域 */
.user-area {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-card {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 4px 12px 4px 4px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid rgba(255, 255, 255, 0.15);
  height: 40px;
}

.user-card:hover {
  background: rgba(255, 255, 255, 0.2);
  border-color: rgba(255, 255, 255, 0.25);
}

.user-avatar {
  background: linear-gradient(135deg, #DC143C 0%, #CD5C5C 100%);
  color: #fff;
  font-size: 14px;
  font-weight: 600;
  box-shadow: 0 2px 8px rgba(220, 20, 60, 0.3);
}

.user-info {
  display: flex;
  flex-direction: column;
  gap: 0px;
  line-height: 1.2;
}

.user-name {
  font-size: 13px;
  font-weight: 600;
  color: #fff;
}

.user-role {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 10px;
  color: rgba(255, 255, 255, 0.7);
  margin-top: 1px;
}

.role-dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: #4ade80;
}

.role-dot.admin {
  background: #DC143C;
}

.role-dot.vip {
  background: #fbbf24;
}

.dropdown-arrow {
  font-size: 10px;
  color: rgba(255, 255, 255, 0.6);
  margin-left: 4px;
}

/* 登录按钮 */
.login-btn {
  background: linear-gradient(135deg, #DC143C 0%, #CD5C5C 100%);
  border: none;
  border-radius: 20px;
  height: 36px;
  padding: 0 20px;
  font-weight: 600;
  box-shadow: 0 4px 15px rgba(220, 20, 60, 0.4);
  display: flex;
  align-items: center;
  gap: 6px;
  transition: all 0.3s ease;
}

.login-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(220, 20, 60, 0.5);
  opacity: 0.95;
}

/* 下拉菜单 */
:deep(.user-menu) {
  background: #fff !important;
  border-radius: 12px !important;
  padding: 8px !important;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.15) !important;
  min-width: 160px !important;
}

:deep(.user-menu .ant-dropdown-menu-item) {
  border-radius: 8px !important;
  padding: 10px 16px !important;
  font-size: 14px !important;
  color: #333 !important;
  display: flex !important;
  align-items: center !important;
  gap: 10px !important;
}

:deep(.user-menu .ant-dropdown-menu-item:hover) {
  background: rgba(220, 20, 60, 0.08) !important;
  color: #DC143C !important;
}

:deep(.user-menu .ant-dropdown-menu-item.logout-item:hover) {
  background: rgba(255, 77, 79, 0.08) !important;
  color: #ff4d4f !important;
}

:deep(.user-menu .ant-dropdown-menu-divider) {
  margin: 8px 0 !important;
}

:deep(.ant-dropdown-menu-item .anticon) {
  font-size: 16px !important;
}

/* 响应式 */
@media (max-width: 768px) {
  .header-container {
    padding: 0 16px;
  }

  .logo-text {
    display: none;
  }

  .nav-menu {
    margin-left: 16px;
    gap: 4px;
  }

  .nav-item {
    padding: 8px 12px;
  }

  .nav-label {
    display: none;
  }

  .user-info {
    display: none;
  }

  .dropdown-arrow {
    display: none;
  }

  .user-card {
    padding: 4px;
    border-radius: 50%;
    background: transparent;
    border: none;
  }
}
</style>
