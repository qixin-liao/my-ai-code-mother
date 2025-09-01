<template>
  <div class="about-page">
    <!-- 关于我们横幅 -->
    <div class="about-hero">
      <a-card class="hero-card" :bordered="false">
        <div class="hero-content">
          <h1 class="hero-title">
            <a-typography-title :level="1">关于我们</a-typography-title>
          </h1>
          <p class="hero-description">致力于打造最优秀的 AI 编程助手平台</p>
        </div>
      </a-card>
    </div>

    <!-- 公司介绍 -->
    <div class="company-section">
      <a-typography-title :level="2" class="section-title">公司介绍</a-typography-title>
      <a-row :gutter="[32, 32]" align="middle">
        <a-col :xs="24" :lg="12">
          <div class="company-content">
            <p class="company-description">
              我们是一家专注于人工智能和编程教育的创新型公司，致力于通过先进的 AI
              技术帮助开发者提高编程效率，降低学习门槛。
            </p>
            <p class="company-description">
              自成立以来，我们始终坚持以用户为中心的产品理念，不断推出优质的编程工具和教育资源，已服务超过
              10 万名开发者。
            </p>
            <div class="company-stats">
              <a-row :gutter="24">
                <a-col :span="8">
                  <a-statistic title="服务用户" value="100000" suffix="+" />
                </a-col>
                <a-col :span="8">
                  <a-statistic title="项目数量" value="50000" suffix="+" />
                </a-col>
                <a-col :span="8">
                  <a-statistic title="满意度" value="98" suffix="%" />
                </a-col>
              </a-row>
            </div>
          </div>
        </a-col>
        <a-col :xs="24" :lg="12">
          <div class="company-image">
            <div class="placeholder-image">
              <svg
                width="500"
                height="400"
                viewBox="0 0 500 400"
                fill="none"
                xmlns="http://www.w3.org/2000/svg"
              >
                <rect width="500" height="400" fill="#f0f0f0" />
                <text x="250" y="200" text-anchor="middle" fill="#999" font-size="16">
                  公司介绍图片
                </text>
              </svg>
            </div>
          </div>
        </a-col>
      </a-row>
    </div>

    <!-- 团队介绍 -->
    <div class="team-section">
      <a-typography-title :level="2" class="section-title">核心团队</a-typography-title>
      <a-row :gutter="[24, 24]">
        <a-col :xs="24" :sm="12" :lg="8" v-for="member in teamMembers" :key="member.id">
          <a-card hoverable class="team-card">
            <template #cover>
              <div class="member-avatar">
                <a-avatar :size="100" :src="member.avatar" />
              </div>
            </template>
            <a-card-meta :title="member.name" :description="member.position" />
            <div class="member-bio">
              <p>{{ member.bio }}</p>
            </div>
            <div class="member-skills">
              <a-tag v-for="skill in member.skills" :key="skill" color="blue">
                {{ skill }}
              </a-tag>
            </div>
          </a-card>
        </a-col>
      </a-row>
    </div>

    <!-- 发展历程 -->
    <div class="timeline-section">
      <a-typography-title :level="2" class="section-title">发展历程</a-typography-title>
      <a-timeline mode="alternate">
        <a-timeline-item v-for="event in timeline" :key="event.id" :color="event.color">
          <template #dot>
            <component :is="event.icon" :style="{ fontSize: '16px' }" />
          </template>
          <div class="timeline-content">
            <h3>{{ event.title }}</h3>
            <p class="timeline-date">{{ event.date }}</p>
            <p>{{ event.description }}</p>
          </div>
        </a-timeline-item>
      </a-timeline>
    </div>

    <!-- 联系我们 -->
    <div class="contact-section">
      <a-typography-title :level="2" class="section-title">联系我们</a-typography-title>
      <a-row :gutter="[32, 32]">
        <a-col :xs="24" :lg="12">
          <a-card title="联系方式" class="contact-card">
            <div class="contact-item">
              <MailOutlined class="contact-icon" />
              <span>邮箱：contact@codefather.cn</span>
            </div>
            <div class="contact-item">
              <PhoneOutlined class="contact-icon" />
              <span>电话：400-123-4567</span>
            </div>
            <div class="contact-item">
              <EnvironmentOutlined class="contact-icon" />
              <span>地址：北京市海淀区中关村软件园</span>
            </div>
            <div class="contact-item">
              <GlobalOutlined class="contact-icon" />
              <span>
                网站：
                <a href="https://www.codefather.cn" target="_blank" rel="noopener noreferrer">
                  www.codefather.cn
                </a>
              </span>
            </div>
          </a-card>
        </a-col>
        <a-col :xs="24" :lg="12">
          <a-card title="关注我们" class="contact-card">
            <div class="social-links">
              <a-button type="primary" size="large" class="social-btn">
                <WechatOutlined />
                微信公众号
              </a-button>
              <a-button size="large" class="social-btn">
                <WeiboOutlined />
                官方微博
              </a-button>
              <a-button size="large" class="social-btn">
                <GithubOutlined />
                GitHub
              </a-button>
            </div>
          </a-card>
        </a-col>
      </a-row>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import {
  RocketOutlined,
  TrophyOutlined,
  StarOutlined,
  CrownOutlined,
  MailOutlined,
  PhoneOutlined,
  EnvironmentOutlined,
  GlobalOutlined,
  WechatOutlined,
  WeiboOutlined,
  GithubOutlined,
} from '@ant-design/icons-vue'

// 团队成员数据
const teamMembers = ref([
  {
    id: 1,
    name: '程序员鱼皮',
    position: '创始人 & CEO',
    avatar:
      'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMTAwIiBoZWlnaHQ9IjEwMCIgdmlld0JveD0iMCAwIDEwMCAxMDAiIGZpbGw9Im5vbmUiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+CjxyZWN0IHdpZHRoPSIxMDAiIGhlaWdodD0iMTAwIiBmaWxsPSIjZjBmMGYwIi8+Cjx0ZXh0IHg9IjUwIiB5PSI1NSIgdGV4dC1hbmNob3I9Im1pZGRsZSIgZmlsbD0iIzk5OSIgZm9udC1zaXplPSIxMiI+5aS05YOPPC90ZXh0Pgo8L3N2Zz4K',
    bio: '全栈工程师，编程导航创始人，致力于编程教育事业',
    skills: ['全栈开发', 'AI 技术', '产品设计'],
  },
  {
    id: 2,
    name: '技术总监',
    position: 'CTO',
    avatar:
      'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMTAwIiBoZWlnaHQ9IjEwMCIgdmlld0JveD0iMCAwIDEwMCAxMDAiIGZpbGw9Im5vbmUiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+CjxyZWN0IHdpZHRoPSIxMDAiIGhlaWdodD0iMTAwIiBmaWxsPSIjZjBmMGYwIi8+Cjx0ZXh0IHg9IjUwIiB5PSI1NSIgdGV4dC1hbmNob3I9Im1pZGRsZSIgZmlsbD0iIzk5OSIgZm9udC1zaXplPSIxMiI+5aS05YOPPC90ZXh0Pgo8L3N2Zz4K',
    bio: '10年+技术经验，专注于 AI 和大数据技术',
    skills: ['人工智能', '大数据', '架构设计'],
  },
  {
    id: 3,
    name: '产品经理',
    position: 'CPO',
    avatar:
      'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMTAwIiBoZWlnaHQ9IjEwMCIgdmlld0JveD0iMCAwIDEwMCAxMDAiIGZpbGw9Im5vbmUiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+CjxyZWN0IHdpZHRoPSIxMDAiIGhlaWdodD0iMTAwIiBmaWxsPSIjZjBmMGYwIi8+Cjx0ZXh0IHg9IjUwIiB5PSI1NSIgdGV4dC1hbmNob3I9Im1pZGRsZSIgZmlsbD0iIzk5OSIgZm9udC1zaXplPSIxMiI+5aS05YOPPC90ZXh0Pgo8L3N2Zz4K',
    bio: '资深产品经理，专注于用户体验和产品创新',
    skills: ['产品设计', '用户研究', '数据分析'],
  },
])

// 发展历程数据
const timeline = ref([
  {
    id: 1,
    title: '公司成立',
    date: '2020年',
    description: '编程导航正式成立，开始专注于编程教育领域',
    color: 'green',
    icon: RocketOutlined,
  },
  {
    id: 2,
    title: '产品上线',
    date: '2021年',
    description: '首个编程学习平台上线，获得用户好评',
    color: 'blue',
    icon: StarOutlined,
  },
  {
    id: 3,
    title: '用户突破',
    date: '2022年',
    description: '注册用户突破10万，成为行业知名品牌',
    color: 'orange',
    icon: TrophyOutlined,
  },
  {
    id: 4,
    title: 'AI 助手发布',
    date: '2023年',
    description: '推出 AI 编程助手，引领行业技术革新',
    color: 'purple',
    icon: CrownOutlined,
  },
])
</script>

<style scoped>
.about-page {
  padding: 0;
}

/* 关于我们横幅 */
.about-hero {
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
  opacity: 0.9;
}

/* 公共样式 */
.section-title {
  text-align: center;
  margin-bottom: 32px !important;
}

/* 公司介绍 */
.company-section {
  margin-bottom: 64px;
}

.company-content {
  padding: 0 16px;
}

.company-description {
  font-size: 16px;
  line-height: 1.8;
  color: #666;
  margin-bottom: 20px;
}

.company-stats {
  margin-top: 32px;
}

.company-image {
  text-align: center;
}

.company-image img,
.placeholder-image svg {
  max-width: 100%;
  height: auto;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

/* 团队介绍 */
.team-section {
  margin-bottom: 64px;
}

.team-card {
  height: 100%;
  text-align: center;
}

.member-avatar {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 160px;
  background: #fafafa;
}

.member-bio {
  margin: 16px 0;
  color: #666;
  line-height: 1.6;
}

.member-skills {
  margin-top: 12px;
}

/* 发展历程 */
.timeline-section {
  margin-bottom: 64px;
}

.timeline-content h3 {
  margin: 0 0 8px 0;
  color: #1890ff;
}

.timeline-date {
  color: #999;
  font-size: 14px;
  margin-bottom: 8px;
}

/* 联系我们 */
.contact-section {
  margin-bottom: 32px;
}

.contact-card {
  height: 100%;
}

.contact-item {
  display: flex;
  align-items: center;
  margin-bottom: 16px;
  font-size: 16px;
}

.contact-icon {
  margin-right: 12px;
  color: #1890ff;
  font-size: 18px;
}

.social-links {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.social-btn {
  display: flex;
  align-items: center;
  gap: 8px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .hero-content {
    padding: 32px 16px;
  }

  .company-content {
    padding: 0;
  }

  .company-stats {
    margin-top: 24px;
  }

  .social-links {
    align-items: center;
  }

  .social-btn {
    width: 200px;
    justify-content: center;
  }
}

@media (max-width: 480px) {
  .hero-description {
    font-size: 16px;
  }

  .company-description {
    font-size: 14px;
  }

  .contact-item {
    font-size: 14px;
  }
}
</style>
