import { useLoginUserStore } from '@/stores/loginUser'
import { message } from 'ant-design-vue'
import router from '@/router'

// 是否为首次获取登录用户
let firstFetchLoginUser = true

/**
 * 全局权限校验
 */
router.beforeEach(async (to, _from, next) => {
  const loginUserStore = useLoginUserStore()
  let loginUser = loginUserStore.loginUser

  // 确保页面刷新，首次加载时，能够等后端返回用户信息后再校验权限
  if (firstFetchLoginUser) {
    await loginUserStore.fetchLoginUser()
    loginUser = loginUserStore.loginUser
    firstFetchLoginUser = false
  }

  const routeMeta = to.meta

  // Admin 路由权限校验
  if (routeMeta?.requiresAdmin === true) {
    if (!loginUser || loginUser.userRole !== 'admin') {
      message.error('没有权限')
      next(`/user/login?redirect=${to.fullPath}`)
      return
    }
  }

  // 登录才能访问的路由校验
  if (routeMeta?.requiresAuth === true) {
    if (!loginUser || !loginUser.id) {
      message.error('请先登录')
      next(`/user/login?redirect=${to.fullPath}`)
      return
    }
  }

  next()
})
