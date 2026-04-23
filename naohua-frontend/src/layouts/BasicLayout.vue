<template>
  <a-layout class="basic-layout">
    <!-- 顶部导航栏 -->
    <GlobalHeader />
    <!-- 主要内容区域 -->
    <a-layout-content class="main-content">
      <router-view />
    </a-layout-content>
    <!-- 底部版权信息 -->
    <!-- <GlobalFooter /> -->
  </a-layout>
</template>

<script setup lang="ts">
import GlobalHeader from '@/components/GlobalHeader.vue'
import GlobalFooter from '@/components/GlobalFooter.vue'
</script>

<style scoped>
.basic-layout {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background: transparent;
}

.main-content {
  flex: 1;
  width: 100%;
  padding: 0;
  margin: 0;
  background: transparent;
}

/* 确保 ant-layout-content 也是透明的 */
:deep(.ant-layout-content) {
  background: transparent !important;
}
</style>
