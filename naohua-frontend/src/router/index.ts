import { createRouter, createWebHistory } from 'vue-router'
import HomePage from '@/pages/HomePage.vue'
import UserLoginPage from '@/pages/user/UserLoginPage.vue'
import UserRegisterPage from '@/pages/user/UserRegisterPage.vue'
import UserProfilePage from '@/pages/user/UserProfilePage.vue'
import UserManagePage from '@/pages/admin/UserManagePage.vue'
import AppManagePage from '@/pages/admin/AppManagePage.vue'
import AppChatPage from '@/pages/app/AppChatPage.vue'
import AppEditPage from '@/pages/app/AppEditPage.vue'
import AppCreatePage from '@/pages/app/AppCreatePage.vue'
import ChatManagePage from "@/pages/admin/ChatManagePage.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: '主页',
      component: HomePage,
      meta: { requiresAuth: false },
    },
    {
      path: '/user/login',
      name: '用户登录',
      component: UserLoginPage,
      meta: { requiresAuth: false },
    },
    {
      path: '/user/register',
      name: '用户注册',
      component: UserRegisterPage,
      meta: { requiresAuth: false },
    },
    {
      path: '/user/profile',
      name: '个人主页',
      component: UserProfilePage,
      meta: { requiresAuth: true },
    },
    {
      path: '/admin/userManage',
      name: '用户管理',
      component: UserManagePage,
      meta: { requiresAuth: true, requiresAdmin: true },
    },
    {
      path: '/admin/appManage',
      name: '应用管理',
      component: AppManagePage,
      meta: { requiresAuth: true, requiresAdmin: true },
    },
    {
      path: '/admin/chatManage',
      name: '对话管理',
      component: ChatManagePage,
      meta: { requiresAuth: true, requiresAdmin: true },
    },
    {
      path: '/app/chat/:id',
      name: '应用对话',
      component: AppChatPage,
      meta: { requiresAuth: true },
    },
    {
      path: '/app/create',
      name: '创建应用',
      component: AppCreatePage,
      meta: { requiresAuth: true },
    },
    {
      path: '/app/edit/:id',
      name: '编辑应用',
      component: AppEditPage,
      meta: { requiresAuth: true },
    },
  ],
})

export default router
