---
name: responsive-design
description: Use when implementing responsive layouts, mobile-first design, or adapting components for different screen sizes. Covers breakpoints, container queries, and adaptive patterns.
---

# Responsive Design Guide

## Breakpoints

```css
/* Mobile: < 768px */
/* Tablet: 768px - 1024px */
/* Desktop: > 1024px */

@media (min-width: 768px) {
  /* Tablet and larger */
}

@media (min-width: 1024px) {
  /* Desktop only */
}

@media (max-width: 768px) {
  /* Hide on mobile */
  .desktop-only { display: none; }
}
```

## Mobile-First Container

```vue
<div class="container">
  <!-- Content that adapts from mobile to desktop -->
</div>

<style scoped>
.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}
</style>
```

## Flexbox Responsive Layout

```css
/* Stack on mobile, row on desktop */
.responsive-layout {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

@media (min-width: 768px) {
  .responsive-layout {
    flex-direction: row;
  }
}
```

## Grid Responsive Layout

```css
/* Auto-fit grid */
.app-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 24px;
}

/* Auto-fit with max columns */
.featured-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 24px;
}
```

## Sidebar Hide/Show

```css
/* Hide sidebar on mobile */
.sidebar {
  display: none;
}

@media (min-width: 1024px) {
  .sidebar {
    display: flex;
  }
}
```

## Typography Scaling

```css
.title {
  font-size: 32px;
  font-weight: 600;
}

@media (min-width: 768px) {
  .title {
    font-size: 48px;
  }
}

@media (min-width: 1024px) {
  .title {
    font-size: 72px;
  }
}
```

## Component Responsive Patterns

```vue
<!-- Card grid that collapses on mobile -->
<div class="card-grid">
  <AppCard
    v-for="app in apps"
    :key="app.id"
    :app="app"
  />
</div>

<style scoped>
.card-grid {
  display: grid;
  grid-template-columns: 1fr;  /* Mobile: 1 column */
  gap: 16px;
}

@media (min-width: 768px) {
  .card-grid {
    grid-template-columns: repeat(2, 1fr);  /* Tablet: 2 columns */
    gap: 20px;
  }
}

@media (min-width: 1024px) {
  .card-grid {
    grid-template-columns: repeat(3, 1fr);  /* Desktop: 3 columns */
    gap: 24px;
  }
}
</style>
```

## Page Layout Responsive

```vue
<div class="page">
  <!-- Header always visible -->
  <Header />
  
  <!-- Stack on mobile, side-by-side on desktop -->
  <div class="content">
    <aside class="sidebar">...</aside>
    <main class="main">...</main>
  </div>
</div>

<style scoped>
.content {
  display: flex;
  flex-direction: column;
}

.sidebar {
  width: 100%;
  margin-bottom: 16px;
}

.main {
  flex: 1;
}

@media (min-width: 1024px) {
  .content {
    flex-direction: row;
  }
  
  .sidebar {
    width: 260px;
    flex-shrink: 0;
    margin-bottom: 0;
  }
}
</style>
```

## Input & Form Responsive

```vue
<template>
  <a-form layout="vertical">
    <!-- Stack on mobile -->
    <a-row :gutter="[0, 16]">
      <a-col :xs="24" :md="12">
        <a-form-item label="名称">
          <a-input />
        </a-form-item>
      </a-col>
      <a-col :xs="24" :md="12">
        <a-form-item label="描述">
          <a-input />
        </a-form-item>
      </a-col>
    </a-row>
  </a-form>
</template>
```

## Hide/Show Based on Screen

```css
/* Show only on mobile */
.show-mobile {
  display: block;
}

.hide-mobile {
  display: none;
}

@media (min-width: 768px) {
  .show-mobile {
    display: none;
  }
  
  .hide-mobile {
    display: block;
  }
}
```

## Touch-Friendly Targets

```css
/* Minimum touch target size */
.touch-target {
  min-height: 44px;
  min-width: 44px;
  padding: 12px 16px;
}
```

## Image Responsive

```css
.responsive-img {
  max-width: 100%;
  height: auto;
  object-fit: cover;
}
```
