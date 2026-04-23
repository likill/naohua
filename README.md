# Naohua - AI 零代码应用生成平台

基于 Spring Boot3 + LangChain4j + LangGraph4j 的 AI 零代码应用生成平台。用户输入自然语言描述，由 AI Agent 自动执行并发素材搜集、代码生成、质量检查、项目构建的完整工作流，最终一键部署为可访问的 Web 应用。

## 功能特性

### 1. 多模式代码生成
- **HTML 单页面生成** - 快速生成单页面应用
- **多文件项目生成** - 生成完整的多文件项目结构
- **Vue 工程模式** - 生成完整的 Vue3 项目，支持预览和部署

### 2. 智能素材收集
- **并发图片收集** - 自动从 Pexels 搜索相关图片
- **Undraw 插画** - 收集 SVG 插画素材
- **Mermaid 架构图** - 自动生成架构图和流程图
- **Logo 生成** - 使用 AI 生成品牌 Logo

### 3. AI 工作流编排
使用 LangGraph4j 实现复杂的工作流编排：
- 图片收集计划 → 并发素材收集 → 图片聚合
- Prompt 增强 → 智能路由 → 代码生成 → 质量检查 → 项目构建

### 4. 质量保障
- **输入护栏** - Prompt 安全检查，过滤敏感内容
- **输出护栏** - 代码质量检查，确保生成代码可用
- **重试机制** - 自动重试失败的生成任务

### 5. 一键部署
- 自动构建 Vue 项目
- 生成可访问的部署链接
- 自动生成应用封面截图

## 技术栈

| 分类 | 技术 |
|------|------|
| 后端框架 | Spring Boot 3.5 |
| AI 框架 | LangChain4j 1.1.0 + LangGraph4j 1.6.0 |
| 数据库 | MySQL + MyBatis-Flex |
| 缓存 | Redis + Redisson |
| 监控 | Micrometer + Prometheus + Grafana |
| 前端 | Vue3 + Vite + TypeScript |

## 项目结构

```
naohua-backend/
├── src/main/java/com/jiaxin/aiweb/
│   ├── ai/                    # AI 相关
│   │   ├── guardrail/         # 输入输出护栏
│   │   └── tools/             # AI 工具类
│   ├── config/                 # 配置类
│   ├── controller/            # 控制器
│   ├── core/                   # 核心逻辑
│   │   ├── parser/             # 代码解析器
│   │   └── saver/             # 代码存储器
│   ├── langgraph4j/            # LangGraph4j 工作流
│   │   ├── ai/                 # AI 服务
│   │   ├── model/             # 数据模型
│   │   ├── node/               # 工作流节点
│   │   ├── node/concurrent/    # 并发节点
│   │   ├── state/              # 状态管理
│   │   └── tools/              # 工作流工具
│   ├── monitor/                # 监控
│   ├── ratelimter/             # 限流
│   └── service/                # 服务层
└── src/main/resources/
    └── prompt/                 # AI 提示词模板
```

## 快速开始

### 环境要求
- JDK 21+
- MySQL 8.0+
- Redis 6.0+
- Node.js 18+

### 配置

1. 修改 `src/main/resources/application.yml` 中的数据库和 Redis 配置
2. 配置 AI API Key（支持 DeepSeek、阿里云 DashScope 等）

```yaml
langchain4j:
  open-ai:
    chat-model:
      api-key: your-api-key
      model-name: deepseek-chat
```


## API 示例

### 创建应用并生成代码

```bash
curl -X POST http://localhost:8123/api/app/add \
  -H "Content-Type: application/json" \
  -d '{"initPrompt": "创建一个todo列表应用"}'
```

### 对话生成代码（流式）

```bash
curl -N http://localhost:8123/api/app/chat/gen/code?appId=1&message=添加一个登录功能
```

### 部署应用

```bash
curl -X POST http://localhost:8123/api/app/deploy \
  -H "Content-Type: application/json" \
  -d '{"appId": 1}'
```

## 效果展示

<img width="1276" height="673" alt="屏幕截图 2026-04-23 112205" src="https://github.com/user-attachments/assets/9ee4e7bd-e4e5-491c-bb26-31cd05bdf708" />
<img width="1269" height="672" alt="屏幕截图 2026-04-23 112329" src="https://github.com/user-attachments/assets/af5931b2-c1bb-4562-8ce1-952ab8dea17c" />
<img width="1271" height="669" alt="屏幕截图 2026-04-23 112410" src="https://github.com/user-attachments/assets/526b91eb-5e96-43b5-ae77-fff824b11618" />

## License

MIT
