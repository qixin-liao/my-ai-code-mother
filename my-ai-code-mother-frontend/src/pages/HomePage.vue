<template>
  <div class="home-page">
    <!-- 欢迎横幅 -->
    <div class="hero-section">
      <a-card class="hero-card" :bordered="false">
        <div class="hero-content">
          <h1 class="hero-title">
            <a-typography-title :level="1"> 欢迎使用 AI 编程助手 </a-typography-title>
          </h1>
          <p class="hero-description">智能编程工具，让代码开发更高效、更简单</p>
          <div class="hero-actions">
            <a-button type="primary" size="large" @click="handleGetStarted"> 开始使用 </a-button>
            <a-button size="large" @click="handleLearnMore"> 了解更多 </a-button>
          </div>
        </div>
      </a-card>
    </div>

    <!-- 功能特性 -->
    <div class="features-section">
      <a-typography-title :level="2" class="section-title"> 核心功能 </a-typography-title>

      <a-row :gutter="[24, 24]">
        <a-col :xs="24" :sm="12" :lg="8" v-for="feature in features" :key="feature.id">
          <a-card hoverable class="feature-card">
            <template #cover>
              <div class="feature-icon">
                <component :is="feature.icon" :style="{ fontSize: '48px', color: '#1890ff' }" />
              </div>
            </template>
            <a-card-meta :title="feature.title" :description="feature.description" />
          </a-card>
        </a-col>
      </a-row>
    </div>

    <!-- 快速开始 -->
    <div class="quick-start-section">
      <a-typography-title :level="2" class="section-title"> 快速开始 </a-typography-title>

      <a-card>
        <a-steps :current="0" direction="vertical">
          <a-step
            v-for="step in quickStartSteps"
            :key="step.id"
            :title="step.title"
            :description="step.description"
          />
        </a-steps>
      </a-card>
    </div>

    <!-- 统计数据 -->
    <div class="stats-section">
      <a-row :gutter="24">
        <a-col :xs="12" :sm="6" v-for="stat in stats" :key="stat.id">
          <a-statistic
            :title="stat.title"
            :value="stat.value"
            :suffix="stat.suffix"
            :prefix="stat.prefix"
            class="stat-item"
          />
        </a-col>
      </a-row>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import {
  CodeOutlined,
  RocketOutlined,
  BulbOutlined,
  ToolOutlined,
  FileTextOutlined,
  ApiOutlined,
} from '@ant-design/icons-vue'
import { message } from 'ant-design-vue'

// 功能特性数据
const features = ref([
  {
    id: 1,
    icon: CodeOutlined,
    title: '智能代码生成',
    description: '基于 AI 技术，快速生成高质量代码片段',
  },
  {
    id: 2,
    icon: ToolOutlined,
    title: '开发工具集',
    description: '提供丰富的开发工具，提升编程效率',
  },
  {
    id: 3,
    icon: FileTextOutlined,
    title: '代码模板',
    description: '预置常用代码模板，快速搭建项目框架',
  },
  {
    id: 4,
    icon: BulbOutlined,
    title: '智能提示',
    description: '实时代码分析，提供智能优化建议',
  },
  {
    id: 5,
    icon: ApiOutlined,
    title: 'API 集成',
    description: '无缝集成各种第三方 API 服务',
  },
  {
    id: 6,
    icon: RocketOutlined,
    title: '快速部署',
    description: '一键部署到云端，支持多种部署方式',
  },
])

// 快速开始步骤
const quickStartSteps = ref([
  {
    id: 1,
    title: '注册账号',
    description: '创建您的专属账号，开启 AI 编程之旅',
  },
  {
    id: 2,
    title: '选择工具',
    description: '从丰富的工具库中选择适合您的开发工具',
  },
  {
    id: 3,
    title: '开始编码',
    description: '使用 AI 助手，快速生成和优化您的代码',
  },
  {
    id: 4,
    title: '部署上线',
    description: '将您的项目部署到生产环境，与世界分享',
  },
])

// 统计数据
const stats = ref([
  {
    id: 1,
    title: '用户数量',
    value: 10000,
    suffix: '+',
    prefix: '',
  },
  {
    id: 2,
    title: '代码生成',
    value: 50000,
    suffix: '+',
    prefix: '',
  },
  {
    id: 3,
    title: '项目模板',
    value: 200,
    suffix: '+',
    prefix: '',
  },
  {
    id: 4,
    title: '满意度',
    value: 98,
    suffix: '%',
    prefix: '',
  },
])

// 事件处理
const handleGetStarted = () => {
  message.success('即将跳转到工具页面')
  // TODO: 路由跳转到工具页面
}

const handleLearnMore = () => {
  message.info('即将跳转到文档页面')
  // TODO: 路由跳转到文档页面
}
</script>

<style scoped>
.home-page {
  padding: 0;
}

/* 英雄区域 */
.hero-section {
  margin-bottom: 48px;
}

.hero-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 12px;
  overflow: hidden;
}

.hero-content {
  text-align: center;
  padding: 48px 24px;
  color: white;
}

.hero-title :deep(.ant-typography) {
  color: white !important;
  margin-bottom: 16px;
}

.hero-description {
  font-size: 18px;
  margin-bottom: 32px;
  opacity: 0.9;
}

.hero-actions {
  display: flex;
  gap: 16px;
  justify-content: center;
}

/* 功能特性区域 */
.features-section {
  margin-bottom: 48px;
}

.section-title {
  text-align: center;
  margin-bottom: 32px !important;
}

.feature-card {
  height: 100%;
  transition: transform 0.3s;
}

.feature-card:hover {
  transform: translateY(-4px);
}

.feature-icon {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 120px;
  background: #f8f9fa;
}

/* 快速开始区域 */
.quick-start-section {
  margin-bottom: 48px;
}

/* 统计数据区域 */
.stats-section {
  background: #f8f9fa;
  padding: 32px;
  border-radius: 8px;
  margin-bottom: 24px;
}

.stat-item {
  text-align: center;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .hero-content {
    padding: 32px 16px;
  }

  .hero-actions {
    flex-direction: column;
    align-items: center;
  }

  .hero-actions .ant-btn {
    width: 200px;
  }

  .stats-section {
    padding: 24px 16px;
  }
}

@media (max-width: 480px) {
  .hero-description {
    font-size: 16px;
  }

  .hero-actions .ant-btn {
    width: 160px;
  }
}
</style>
