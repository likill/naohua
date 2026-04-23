# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Vue 3 + TypeScript frontend for an AI code generator called "脑花" (Brain Flower). Users can chat with AI to create web applications, view generated sites, deploy them, and manage their apps.

## Commands

```sh
npm run dev          # Start development server
npm run build        # Type-check, lint, and build for production
npm run build-only   # Build only (no type-check)
npm run type-check   # Run vue-tsc type checking
npm run lint         # Lint with ESLint + auto-fix
npm run format       # Format code with Prettier
npm run preview      # Preview production build
```

## Architecture

### Tech Stack
- **Vue 3** with Composition API (`<script setup>`)
- **TypeScript** with strict mode
- **Pinia** for state management (only `loginUser` store exists)
- **Vue Router 4** for routing
- **Ant Design Vue 4** for UI components
- **Axios** for HTTP with global interceptors
- **Vite 7** for build tooling

### Global Theme
Primary color: `#DC143C` (crimson), Secondary: `#CD5C5C` (indian red), Background: `#FFEFD5` (cream). Theme is configured in `App.vue` via `ConfigProvider` and CSS variables.

### Request Flow
1. `src/request.ts` creates axios instance with `baseURL` from env config
2. Global interceptors handle 401 redirect to login
3. All API calls use this instance with `res.data.code === 0` check pattern

### Pages and Routes
| Route | Component | Purpose |
|-------|-----------|---------|
| `/` | `HomePage.vue` | Landing with my-apps and featured-apps grids |
| `/user/login` | `UserLoginPage.vue` | Login form |
| `/user/register` | `UserRegisterPage.vue` | Registration form |
| `/user/profile` | `UserProfilePage.vue` | User profile with their apps |
| `/app/create` | `AppCreatePage.vue` | Create new app (streams AI response via EventSource) |
| `/app/chat/:id` | `AppChatPage.vue` | Chat + live preview iframe |
| `/app/edit/:id` | `AppEditPage.vue` | Edit app metadata |
| `/admin/userManage` | `UserManagePage.vue` | Admin: user list |
| `/admin/appManage` | `AppManagePage.vue` | Admin: app list |
| `/admin/chatManage` | `ChatManagePage.vue` | Admin: chat history |

### App Creation Flow
1. User submits prompt on `/app/create`
2. `addApp()` creates app, gets `appId`
3. `chatToGenCode` called via **EventSource** (SSE) streaming
4. On `done` event, router navigates to `/app/chat/{appId}`

### Sidebar Navigation Bug Pattern
`AppChatPage` and `AppCreatePage` share a sidebar showing user's apps. Clicking an app changes URL via `router.push()` but Vue doesn't reload the component. **Fix**: Watch `route.params.id` and reset/load data on change:
```typescript
watch(
  () => route.params.id,
  (newId) => {
    if (newId) {
      messages.value = []
      fetchAppInfo()
    }
  }
)
```

### Key Files
- `src/access.ts` - Route guards, admin permission check
- `src/stores/loginUser.ts` - Login user state, `fetchLoginUser()` call here
- `src/config/env.ts` - `API_BASE_URL`, `DEPLOY_DOMAIN`, `getDeployUrl()`
- `src/api/` - Auto-generated API controllers (type-safe wrappers)

### API Controller Pattern
API methods return Axios response. Check `res.data.code === 0` for success, access data via `res.data.data`.

### Pixel Style Components
Global header user area uses a pixel-art aesthetic with:
- 3px solid #333 borders with pixel shadows
- `Courier New` monospace font
- `image-rendering: pixelated`
- Inset box-shadows for 3D button effect
