---
name: ui-design
description: Use when designing UI components, implementing UI/UX patterns, or working with Ant Design Vue components. Covers form design, modal dialogs, cards, and layout patterns.
---

# UI Design Patterns

## Card Design

```vue
<div class="card">
  <div class="card-header">标题</div>
  <div class="card-body">内容区域</div>
  <div class="card-footer">操作区域</div>
</div>

<style scoped>
.card {
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  overflow: hidden;
  transition: transform 0.3s, box-shadow 0.3s;
}

.card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
}
</style>
```

## Button Styles

```vue
<!-- Primary Button -->
<a-button type="primary">主要操作</a-button>

<!-- Default Button -->
<a-button>默认按钮</a-button>

<!-- Danger Button -->
<a-button danger>危险操作</a-button>

<!-- Custom Styled Button -->
<style>
.pixel-btn {
  font-family: 'Courier New', monospace;
  background: #DC143C;
  border: 3px solid #333;
  box-shadow: 2px 2px 0 #333;
}
</style>
```

## Form Layout

```vue
<a-form :model="form" layout="vertical">
  <a-form-item label="用户名" name="username">
    <a-input v-model:value="form.username" />
  </a-form-item>
  
  <a-form-item label="描述" name="desc">
    <a-textarea v-model:value="form.desc" :rows="4" />
  </a-form-item>
</a-form>
```

## Modal Dialog

```vue
<a-modal
  v-model:open="visible"
  title="弹窗标题"
  @ok="handleOk"
  @cancel="handleCancel"
>
  <p>弹窗内容</p>
</a-modal>
```

## Page Layout with Header + Content

```vue
<div class="page-layout">
  <div class="header-bar">
    <div class="header-left">
      <h1 class="page-title">页面标题</h1>
    </div>
    <div class="header-right">
      <a-button>操作</a-button>
    </div>
  </div>
  
  <div class="main-content">
    <!-- 内容区域 -->
  </div>
</div>

<style scoped>
.page-layout {
  height: 100vh;
  display: flex;
  flex-direction: column;
  padding: 16px;
}

.header-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 20px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  margin-bottom: 16px;
}

.main-content {
  flex: 1;
  overflow: auto;
}
</style>
```

## Sidebar + Main Layout

```vue
<div class="layout">
  <div class="sidebar">
    <!-- 侧边栏 -->
  </div>
  <div class="main">
    <!-- 主内容 -->
  </div>
</div>

<style scoped>
.layout {
  display: flex;
  gap: 16px;
  height: 100%;
}

.sidebar {
  width: 260px;
  flex-shrink: 0;
  background: white;
  border-radius: 12px;
}

.main {
  flex: 1;
  overflow: auto;
}
</style>
```

## Empty State

```vue
<a-empty description="暂无数据">
  <a-button type="primary">创建第一个</a-button>
</a-empty>
```

## Loading States

```vue
<!-- Spin -->
<a-spin size="large" />

<!-- Skeleton -->
<a-skeleton :active="loading" :paragraph="{ rows: 4 }" />

<!-- Button loading -->
<a-button :loading="submitting">提交</a-button>
```

## Avatar with Status

```vue
<div class="avatar-wrapper">
  <a-avatar :src="userAvatar" :size="64" />
  <span class="status-dot online"></span>
</div>

<style scoped>
.avatar-wrapper {
  position: relative;
  display: inline-block;
}

.status-dot {
  position: absolute;
  bottom: 4px;
  right: 4px;
  width: 12px;
  height: 12px;
  border-radius: 50%;
  border: 2px solid white;
}

.status-dot.online {
  background: #4ade80;
}
</style>
```

## Dropdown Menu

```vue
<a-dropdown>
  <a-space>
    用户名 <DownOutlined />
  </a-space>
  <template #overlay>
    <a-menu @click="handleMenuClick">
      <a-menu-item key="profile">个人主页</a-menu-item>
      <a-menu-item key="settings">设置</a-menu-item>
      <a-menu-divider />
      <a-menu-item key="logout">退出登录</a-menu-item>
    </a-menu>
  </template>
</a-dropdown>
```
