<template>
  <div class="container">
    <!-- 导航栏 -->
    

    <!-- 英雄区域 -->
    <section class="hero">
      <div class="hero-content">
        <h1 class="title">脑花</h1>
        <p class="tagline">让思维落地</p>
        <p class="description">
          脑花是一个创新的思维导图工具，帮助你将脑海中的灵感
          <br />
          转化为清晰的思路，让每一个想法都能落地生根。
        </p>
        <div class="hero-buttons">
          <button class="btn-primary" @click="startUsing">开始使用</button>
          <button class="btn-secondary">了解更多</button>
        </div>
      </div>
      <div class="hero-visual">
        <div class="brain-icon">
          <div class="circle c1"></div>
          <div class="circle c2"></div>
          <div class="circle c3"></div>
          <div class="lines">
            <div class="line l1"></div>
            <div class="line l2"></div>
            <div class="line l3"></div>
            <div class="line l4"></div>
          </div>
        </div>
      </div>
    </section>

    <!-- 特性区域 -->
    <section class="features">
      <div class="feature-card">
        <div class="feature-icon">💡</div>
        <h3>灵感捕捉</h3>
        <p>随时随地记录脑海中的灵感，不让任何一个好想法溜走</p>
      </div>
      <div class="feature-card">
        <div class="feature-icon">🧠</div>
        <h3>思维整理</h3>
        <p>将杂乱的思绪整理成清晰的思维导图</p>
      </div>
      <div class="feature-card">
        <div class="feature-icon">🚀</div>
        <h3>落地执行</h3>
        <p>从想法到行动，让思维真正落地生根</p>
      </div>
    </section>

    <!-- 我的作品 -->
    <section v-if="loginUserStore.loginUser.id" class="my-works">
      <h2 class="section-title">我的作品</h2>
      <div class="app-grid">
        <AppCard
          v-for="app in myApps"
          :key="app.id"
          :app="app"
          @view-chat="viewChat"
          @view-work="viewWork"
        />
      </div>
      <div class="pagination-wrapper">
        <a-pagination
          v-model:current="myAppsPage.current"
          v-model:page-size="myAppsPage.pageSize"
          :total="myAppsPage.total"
          :show-size-changer="false"
          :show-total="(total: number) => `共 ${total} 个应用`"
          @change="loadMyApps"
        />
      </div>
    </section>

    <!-- 精选案例 -->
    <section class="featured-cases">
      <h2 class="section-title featured">精选案例</h2>
      <div class="featured-grid">
        <AppCard
          v-for="app in featuredApps"
          :key="app.id"
          :app="app"
          :featured="true"
          @view-chat="viewChat"
          @view-work="viewWork"
        />
      </div>
      <div class="pagination-wrapper">
        <a-pagination
          v-model:current="featuredAppsPage.current"
          v-model:page-size="featuredAppsPage.pageSize"
          :total="featuredAppsPage.total"
          :show-size-changer="false"
          :show-total="(total: number) => `共 ${total} 个案例`"
          @change="loadFeaturedApps"
        />
      </div>
    </section>

    <!-- 页脚 -->
    <footer class="footer">
      <p>© 2026 脑花 · 让思维落地</p>
    </footer>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { listMyAppVoByPage, listGoodAppVoByPage, addApp } from '@/api/appController'
import { getDeployUrl } from '@/config/env'
import AppCard from '@/components/AppCard.vue'
import { useLoginUserStore } from '@/stores/loginUser'

const router = useRouter()
const loginUserStore = useLoginUserStore()

// 我的应用数据
const myApps = ref<API.AppVO[]>([])
const myAppsPage = reactive({
  current: 1,
  pageSize: 6,
  total: 0,
})

// 精选应用数据
const featuredApps = ref<API.AppVO[]>([])
const featuredAppsPage = reactive({
  current: 1,
  pageSize: 6,
  total: 0,
})

// 加载我的应用
const loadMyApps = async () => {
  try {
    const res = await listMyAppVoByPage({
      pageNum: myAppsPage.current,
      pageSize: myAppsPage.pageSize,
      sortField: 'createTime',
      sortOrder: 'desc',
    })

    if (res.data.code === 0 && res.data.data) {
      myApps.value = res.data.data.records || []
      myAppsPage.total = res.data.data.totalRow || 0
    }
  } catch (error) {
    console.error('加载我的应用失败：', error)
  }
}

// 加载精选应用
const loadFeaturedApps = async () => {
  try {
    const res = await listGoodAppVoByPage({
      pageNum: featuredAppsPage.current,
      pageSize: featuredAppsPage.pageSize,
      sortField: 'createTime',
      sortOrder: 'desc',
    })

    if (res.data.code === 0 && res.data.data) {
      featuredApps.value = res.data.data.records || []
      featuredAppsPage.total = res.data.data.totalRow || 0
    }
  } catch (error) {
    console.error('加载精选应用失败：', error)
  }
}

// 查看对话
const viewChat = (appId: string | number | undefined) => {
  if (appId) {
    router.push(`/app/chat/${appId}?view=1`)
  }
}

// 查看作品
const viewWork = (app: API.AppVO) => {
  if (app.deployKey) {
    const url = getDeployUrl(app.deployKey)
    window.open(url, '_blank')
  }
}

// 开始使用 - 跳转到创建应用页面
const startUsing = () => {
  router.push('/app/create')
}

// 页面加载时获取数据
onMounted(() => {
  if (loginUserStore.loginUser.id) {
    loadMyApps()
  }
  loadFeaturedApps()
  // 设置 body 背景为深色渐变
  document.body.style.background = 'linear-gradient(135deg, #DC143C 0%, #CD5C5C 50%, #DC143C 100%)'
})
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html {
  min-height: 100vh;
  background: linear-gradient(135deg, #DC143C 0%, #CD5C5C 50%, #DC143C 100%) !important;
}

body {
  font-family: 'PingFang SC', 'Microsoft YaHei', sans-serif;
  background: linear-gradient(135deg, #DC143C 0%, #CD5C5C 50%, #DC143C 100%) !important;
  min-height: 100vh;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  min-height: 100vh;
}

/* 首页 GlobalHeader 样式调整 */
.container :deep(.site-title) {
  color: #fff !important;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);
}

.container :deep(.ant-menu-horizontal .ant-menu-item),
.container :deep(.ant-menu-horizontal .ant-menu-submenu) {
  color: rgba(255, 255, 255, 0.9) !important;
}

.container :deep(.ant-menu-horizontal .ant-menu-item:hover),
.container :deep(.ant-menu-horizontal .ant-menu-submenu:hover) {
  background: rgba(255, 255, 255, 0.15) !important;
  color: #fff !important;
}

.container :deep(.ant-menu-horizontal .ant-menu-item-selected) {
  color: #fff !important;
  font-weight: 600;
}

.container :deep(.ant-menu-horizontal .ant-menu-item-selected::after) {
  border-bottom-color: #fff !important;
}

/* 用户区域像素风格在深色背景下调整 */
.container :deep(.pixel-user-area) {
  background: rgba(255, 255, 255, 0.15) !important;
  border-color: rgba(255, 255, 255, 0.3);
  box-shadow:
    4px 4px 0 rgba(0, 0, 0, 0.2),
    inset -2px -2px 0 rgba(255, 255, 255, 0.1),
    inset 2px 2px 0 rgba(0, 0, 0, 0.1);
}

.container :deep(.pixel-decor) {
  background: #fff;
}

.container :deep(.pixel-username) {
  color: #fff;
  text-shadow: 1px 1px 0 rgba(0, 0, 0, 0.3);
}

.container :deep(.pixel-btn) {
  background: rgba(255, 255, 255, 0.2);
  border-color: rgba(255, 255, 255, 0.4);
  box-shadow:
    2px 2px 0 rgba(0, 0, 0, 0.2),
    inset -2px -2px 0 rgba(0, 0, 0, 0.1),
    inset 2px 2px 0 rgba(255, 255, 255, 0.2);
}

.container :deep(.pixel-btn-logout) {
  background: rgba(255, 255, 255, 0.1);
}

/* 导航栏 */
.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 0;
}

.logo {
  font-size: 28px;
  font-weight: bold;
  color: #fff;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);
}

.nav-links {
  display: flex;
  align-items: center;
  gap: 30px;
}

.nav-links a {
  color: #fff;
  text-decoration: none;
  font-size: 16px;
  transition: opacity 0.3s;
}

.nav-links a:hover {
  opacity: 0.8;
}

.btn-login {
  padding: 8px 24px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 20px;
  backdrop-filter: blur(10px);
  transition: background 0.3s;
}

.btn-login:hover {
  background: rgba(255, 255, 255, 0.3);
}

/* 英雄区域 */
.hero {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 80px 0;
  gap: 60px;
}

.hero-content {
  flex: 1;
}

.title {
  font-size: 72px;
  font-weight: bold;
  color: #fff;
  text-shadow: 4px 4px 8px rgba(0, 0, 0, 0.4);
  margin-bottom: 10px;
}

.tagline {
  font-size: 32px;
  color: #fff;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.4);
  margin-bottom: 30px;
  font-weight: 400;
  letter-spacing: 8px;
}

.description {
  font-size: 18px;
  color: rgba(255, 255, 255, 0.95);
  line-height: 1.8;
  margin-bottom: 40px;
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.3);
}

.hero-buttons {
  display: flex;
  gap: 20px;
}

.btn-primary {
  padding: 14px 40px;
  background: #fff;
  color: #DC143C;
  border: none;
  border-radius: 30px;
  font-size: 18px;
  font-weight: bold;
  cursor: pointer;
  transition: transform 0.3s, box-shadow 0.3s;
}

.btn-primary:hover {
  transform: translateY(-3px);
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
}

.btn-secondary {
  padding: 14px 40px;
  background: transparent;
  color: #fff;
  border: 2px solid #fff;
  border-radius: 30px;
  font-size: 18px;
  cursor: pointer;
  transition: background 0.3s;
}

.btn-secondary:hover {
  background: rgba(255, 255, 255, 0.1);
}

/* 脑花视觉元素 */
.hero-visual {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
}

.brain-icon {
  position: relative;
  width: 300px;
  height: 300px;
}

.circle {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.15);
  backdrop-filter: blur(5px);
}

.c1 {
  width: 200px;
  height: 200px;
  top: 50px;
  left: 50px;
  background: rgba(255, 255, 255, 0.2);
}

.c2 {
  width: 140px;
  height: 140px;
  top: 30px;
  left: 120px;
  background: rgba(255, 255, 255, 0.25);
}

.c3 {
  width: 80px;
  height: 80px;
  top: 100px;
  left: 180px;
  background: rgba(255, 255, 255, 0.3);
}

.lines {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

.line {
  position: absolute;
  background: rgba(255, 255, 255, 0.6);
  border-radius: 2px;
}

.l1 {
  width: 100px;
  height: 4px;
  top: -2px;
  left: -50px;
}

.l2 {
  width: 4px;
  height: 100px;
  top: -50px;
  left: -2px;
}

.l3 {
  width: 80px;
  height: 4px;
  top: -2px;
  left: 20px;
  transform: rotate(45deg);
  transform-origin: left center;
}

.l4 {
  width: 80px;
  height: 4px;
  top: -2px;
  left: 20px;
  transform: rotate(-45deg);
  transform-origin: left center;
}

/* 特性区域 */
.features {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 30px;
  padding: 60px 0;
}

.feature-card {
  background: rgba(255, 255, 255, 0.95);
  padding: 40px 30px;
  border-radius: 20px;
  text-align: center;
  transition: transform 0.3s, box-shadow 0.3s;
  backdrop-filter: blur(10px);
}

.feature-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.2);
}

.feature-icon {
  font-size: 50px;
  margin-bottom: 20px;
}

.feature-card h3 {
  color: #DC143C;
  font-size: 22px;
  margin-bottom: 15px;
}

.feature-card p {
  color: #666;
  font-size: 15px;
  line-height: 1.6;
}

/* 我的作品区域 */
.my-works {
  padding: 60px 0;
}

.section-title {
  font-size: 28px;
  font-weight: 600;
  margin-bottom: 24px;
  color: #fff;
  text-shadow: 1px 1px 3px rgba(0, 0, 0, 0.3);
  display: flex;
  align-items: center;
  gap: 12px;
}

.section-title::before {
  content: '';
  width: 4px;
  height: 28px;
  background: linear-gradient(180deg, #fff 0%, rgba(255,255,255,0.6) 100%);
  border-radius: 2px;
}

.app-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 24px;
  margin-bottom: 32px;
}

/* 精选案例区域 */
.featured-cases {
  padding: 60px 0;
}

.section-title.featured::before {
  background: linear-gradient(180deg, #fbbf24 0%, #f59e0b 100%);
}

.featured-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 24px;
  margin-bottom: 32px;
}

/* 分页 */
.pagination-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 32px;
}

/* 深色背景上的分页器 */
:deep(.ant-pagination-item) {
  background: rgba(255, 255, 255, 0.9);
  border-color: rgba(255, 255, 255, 0.3);
  border-radius: 8px;
}

:deep(.ant-pagination-item a) {
  color: #DC143C;
}

:deep(.ant-pagination-item-active) {
  background: #fff !important;
  border-color: #fff;
}

:deep(.ant-pagination-prev button),
:deep(.ant-pagination-next button) {
  background: rgba(255, 255, 255, 0.9);
  border-color: rgba(255, 255, 255, 0.3);
  border-radius: 8px;
  color: #DC143C;
}

/* 页脚 */
.footer {
  text-align: center;
  padding: 40px 0;
  color: rgba(255, 255, 255, 0.8);
  font-size: 14px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .hero {
    flex-direction: column;
    text-align: center;
    padding: 40px 0;
  }

  .title {
    font-size: 48px;
  }

  .tagline {
    font-size: 24px;
    letter-spacing: 4px;
  }

  .hero-buttons {
    justify-content: center;
  }

  .hero-visual {
    order: -1;
  }

  .brain-icon {
    width: 200px;
    height: 200px;
  }

  .c1 {
    width: 130px;
    height: 130px;
    top: 35px;
    left: 35px;
  }

  .c2 {
    width: 90px;
    height: 90px;
    top: 20px;
    left: 80px;
  }

  .c3 {
    width: 50px;
    height: 50px;
    top: 70px;
    left: 120px;
  }

  .features {
    grid-template-columns: 1fr;
  }

  .app-grid,
  .featured-grid {
    grid-template-columns: 1fr;
  }
}

/* 分页器在深色背景上需要调整 */
:deep(.ant-pagination) {
  background: transparent;
}

:deep(.ant-pagination-item) {
  background: rgba(255, 255, 255, 0.9);
  border-color: rgba(255, 255, 255, 0.3);
}

:deep(.ant-pagination-item a) {
  color: #DC143C;
}

:deep(.ant-pagination-item-active) {
  background: #fff !important;
  border-color: #fff;
}

:deep(.ant-pagination-prev button),
:deep(.ant-pagination-next button) {
  background: rgba(255, 255, 255, 0.9);
  border-color: rgba(255, 255, 255, 0.3);
  color: #DC143C;
}

:deep(.ant-pagination-options .ant-select-selector) {
  background: rgba(255, 255, 255, 0.9) !important;
}
</style>
