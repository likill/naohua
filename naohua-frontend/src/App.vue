<script setup lang="ts">
import BasicLayout from '@/layouts/BasicLayout.vue'
import { ConfigProvider } from 'ant-design-vue'
</script>

<template>
  <ConfigProvider
    :theme="{
      token: {
        colorPrimary: '#DC143C',
      },
    }"
  >
    <BasicLayout />
  </ConfigProvider>
</template>

<style>
/* 全局样式优化 */
* {
  box-sizing: border-box;
}

:root {
  --primary-color: #DC143C;
  --secondary-color: #CD5C5C;
  --bg-cream: #fffdfa;
  --gradient-bg: linear-gradient(135deg, #DC143C 0%, #CD5C5C 50%, #DC143C 100%);
}

body {
  margin: 0;
  padding: 0;
  font-family:
    -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Roboto', 'Oxygen', 'Ubuntu', 'Cantarell',
    'Fira Sans', 'Droid Sans', 'Helvetica Neue', sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  overflow-x: hidden;
  background: var(--bg-cream);
}

html {
  overflow-x: hidden;
}

/* Ant Design Vue 组件背景色覆盖 - 不使用 !important，让页面自己管理背景 */
.ant-layout {
  background: var(--bg-cream);
}

.ant-card {
  background: var(--bg-cream) !important;
}

.ant-modal-content {
  background: #fff !important;
}

.ant-input,
.ant-input-affix-wrapper,
.ant-select-selector,
.ant-picker {
  background-color: #fff !important;
}

.ant-btn-default {
  background: #fff !important;
}

.ant-tabs {
  background: var(--bg-cream) !important;
}

.ant-form {
  background: var(--bg-cream) !important;
}

.ant-collapse {
  background: var(--bg-cream) !important;
}

.ant-dropdown-menu {
  background: #fff !important;
}

.ant-select-dropdown {
  background: #fff !important;
}

.ant-picker-dropdown {
  background: #fff !important;
}

.ant-message-notice-content {
  background: #fff !important;
}

.ant-notification-notice {
  background: #fff !important;
}

.ant-popover-inner {
  background: #fff !important;
}

.ant-drawer-content {
  background: var(--bg-cream) !important;
}

.ant-modal-mask {
  background: rgba(220, 20, 60, 0.3) !important;
}

.ant-spin-dot-item {
  background-color: var(--primary-color) !important;
}

/* 主题色按钮优化 */
.ant-btn-primary {
  background: var(--gradient-bg) !important;
  border: none !important;
}

.ant-btn-primary:hover {
  opacity: 0.9;
}

/* 输入框主题色聚焦 */
.ant-input:focus,
.ant-input-focused,
.ant-input-affix-wrapper:focus,
.ant-input-affix-wrapper-focused {
  border-color: var(--primary-color) !important;
  box-shadow: 0 0 0 2px rgba(220, 20, 60, 0.1) !important;
}

/* 选择器主题色 */
.ant-select:not(.ant-select-disabled):hover .ant-select-selector {
  border-color: var(--primary-color) !important;
}

.ant-select-focused:not(.ant-select-disabled) .ant-select-selector {
  border-color: var(--primary-color) !important;
  box-shadow: 0 0 0 2px rgba(220, 20, 60, 0.1) !important;
}

/* ========== 统一表格样式 ========== */

/* 表格容器 */
.ant-table-wrapper {
  background: transparent !important;
  border-radius: 16px;
  overflow: hidden;
}

.ant-table {
  background: transparent !important;
  font-size: 14px;
}

/* 表头 */
.ant-table-thead > tr > th {
  background: linear-gradient(135deg, #DC143C 0%, #CD5C5C 100%) !important;
  color: #fff !important;
  font-weight: 600 !important;
  border-bottom: 2px solid rgba(255, 255, 255, 0.3) !important;
  padding: 14px 16px !important;
  font-size: 14px;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.ant-table-thead > tr > th::before {
  display: none !important;
}

/* 表体 */
.ant-table-tbody > tr > td {
  background: #fff !important;
  border-bottom: 1px solid rgba(220, 20, 60, 0.08) !important;
  padding: 14px 16px !important;
  transition: all 0.2s ease;
}

/* 表体 hover */
.ant-table-tbody > tr:hover > td {
  background: rgba(220, 20, 60, 0.03) !important;
}

/* 斑马纹 - 可选 */
/*
.ant-table-tbody > tr:nth-child(odd) > td {
  background: #fafafa !important;
}
*/

/* 固定列背景 */
.ant-table-cell-fix-left,
.ant-table-cell-fix-right {
  background: #fff !important;
}

.ant-table-cell-fix-left-first,
.ant-table-cell-fix-right-last {
  background: #fff !important;
}

/* 圆角容器 */
.ant-table-container {
  border-radius: 16px;
  overflow: hidden;
  border: 1px solid rgba(220, 20, 60, 0.1);
}

/* 表格外层容器 */
.ant-table-wrapper {
  box-shadow: 0 4px 20px rgba(220, 20, 60, 0.08);
}

/* 分页 */
.ant-pagination {
  margin-top: 20px !important;
}

.ant-pagination-item {
  border-radius: 8px !important;
  border-color: rgba(220, 20, 60, 0.2) !important;
}

.ant-pagination-item a {
  color: #DC143C !important;
}

.ant-pagination-item-active {
  background: linear-gradient(135deg, #DC143C 0%, #CD5C5C 100%) !important;
  border-color: #DC143C !important;
}

.ant-pagination-item-active a {
  color: #fff !important;
}

.ant-pagination-prev button,
.ant-pagination-next button {
  border-radius: 8px !important;
  color: #DC143C !important;
}

.ant-pagination-options .ant-select-selector {
  border-radius: 8px !important;
}

/* 空状态 */
.ant-empty-description {
  color: #999;
}

/* 加载状态 */
.ant-spin-dot-item {
  background-color: #DC143C !important;
}

/* 选中行 */
.ant-table-tbody > tr.ant-table-row-selected > td {
  background: rgba(220, 20, 60, 0.08) !important;
}
</style>
