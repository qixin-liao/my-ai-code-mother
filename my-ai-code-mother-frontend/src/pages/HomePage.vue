<template>
  <div class="home-page">
    <!-- 网站标题和输入框区域 -->
    <div class="hero-section">
      <a-card class="hero-card" :bordered="false">
        <div class="hero-content">
          <h1 class="hero-title">
            <a-typography-title :level="1"> 一句话 🐱 呈所想 </a-typography-title>
          </h1>
          <p class="hero-description">与 AI 对话轻松创建应用和网站</p>

          <!-- 用户提示词输入框 -->
          <div class="prompt-input-wrapper">
            <a-textarea v-model:value="userPrompt" placeholder="使用 NoCode 创建一个高效的小工具，帮我计算......"
              :auto-size="{ minRows: 3, maxRows: 6 }" class="prompt-input" :maxlength="500" show-count />
            <div class="input-actions">
              <a-button type="primary" size="large" :loading="creating" @click="handleCreateApp">
                <template #icon>
                  <RocketOutlined />
                </template>
                开始创建
              </a-button>
            </div>
          </div>

          <!-- 快捷选项 -->
          <div class="quick-options">
            <a-button v-for="option in quickOptions" :key="option" size="small" @click="userPrompt = option">
              {{ option }}
            </a-button>
          </div>
        </div>
      </a-card>
    </div>

    <!-- 我的应用列表 -->
    <div class="apps-section">
      <div class="section-header">
        <a-typography-title :level="2"> 我的作品 </a-typography-title>
        <a-input-search v-model:value="myAppSearchParams.appName" placeholder="搜索应用名称" style="width: 300px"
          @search="loadMyApps" />
      </div>

      <a-spin :spinning="myAppLoading">
        <a-row :gutter="[24, 24]" v-if="myApps.length > 0">
          <a-col :xs="24" :sm="12" :lg="8" :xl="6" v-for="app in myApps" :key="app.id">
            <a-card hoverable class="app-card" @click="goToChatPage(app.id)">
              <template #cover>
                <div class="app-cover">
                  <img :src="app.cover || defaultCoverImage" :alt="app.appName || '应用封面'" />
                </div>
              </template>
              <a-card-meta :title="app.appName || '未命名应用'">
                <template #description>
                  <div class="app-description">
                    <div class="app-info">
                      创建于 {{ dayjs(app.createTime).format('YYYY-MM-DD HH:mm:ss') }}
                    </div>
                    <div class="app-actions" @click.stop>
                      <a-button type="link" size="small" @click="goToEditPage(app.id)">
                        编辑
                      </a-button>
                      <a-popconfirm title="确定要删除这个应用吗？" ok-text="确定" cancel-text="取消"
                        @confirm="handleDeleteApp(app.id)">
                        <a-button type="link" size="small" danger>
                          删除
                        </a-button>
                      </a-popconfirm>
                    </div>
                  </div>
                </template>
              </a-card-meta>
            </a-card>
          </a-col>
        </a-row>
        <a-empty v-else description="暂无应用，快去创建一个吧！" />
      </a-spin>

      <div class="pagination-wrapper" v-if="myAppsTotal > 0">
        <a-pagination v-model:current="myAppSearchParams.pageNum" v-model:page-size="myAppSearchParams.pageSize"
          :total="myAppsTotal" :show-total="(total: number) => `共 ${total} 个应用`" @change="loadMyApps" />
      </div>
    </div>

    <!-- 精选应用列表 -->
    <div class="apps-section featured-section">
      <div class="section-header">
        <a-typography-title :level="2"> 精选案例 </a-typography-title>
        <a-input-search v-model:value="featuredAppSearchParams.appName" placeholder="搜索应用名称" style="width: 300px"
          @search="loadFeaturedApps" />
      </div>

      <a-spin :spinning="featuredAppLoading">
        <a-row :gutter="[24, 24]" v-if="featuredApps.length > 0">
          <a-col :xs="24" :sm="12" :lg="8" :xl="6" v-for="app in featuredApps" :key="app.id">
            <a-card hoverable class="app-card" @click="goToChatPage(app.id)">
              <template #cover>
                <div class="app-cover">
                  <img :src="app.cover || defaultCoverImage" :alt="app.appName || '应用封面'" />
                </div>
              </template>
              <a-card-meta :title="app.appName || '未命名应用'">
                <template #description>
                  <div class="app-description">
                    <div class="app-info">
                      <a-tag color="gold" v-if="app.priority && app.priority >= 99">
                        精选
                      </a-tag>
                      <span>创建者：{{ app.user?.userName || '未知' }}</span>
                    </div>
                  </div>
                </template>
              </a-card-meta>
            </a-card>
          </a-col>
        </a-row>
        <a-empty v-else description="暂无精选应用" />
      </a-spin>

      <div class="pagination-wrapper" v-if="featuredAppsTotal > 0">
        <a-pagination v-model:current="featuredAppSearchParams.pageNum"
          v-model:page-size="featuredAppSearchParams.pageSize" :total="featuredAppsTotal"
          :show-total="(total: number) => `共 ${total} 个应用`" @change="loadFeaturedApps" />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import dayjs from 'dayjs'
import {
  RocketOutlined,
} from '@ant-design/icons-vue'
import {
  addApp,
  listMyAppVoByPage,
  listAppVoByPage,
  deleteApp,
} from '@/api/appController'
import { useLoginUserStore } from '@/stores/loginUser'

const router = useRouter()
const loginUserStore = useLoginUserStore()

// 默认封面图
const defaultCoverImage = 'https://mix2.oss-cn-shanghai.aliyuncs.com/wx/tuiwen/user/twgenerated/2025/10/31/7ercmqak0wf4e9795dd319048bebfd8a02ccc57796.jpg'

// 用户输入的提示词
const userPrompt = ref('')
const creating = ref(false)

// 快捷选项
const quickOptions = [
  '波普风电商页面',
  '企业网站',
  '电商运营后台',
  '暗黑话题社区',
]

// 我的应用
const myApps = ref<API.AppVO[]>([])
const myAppsTotal = ref(0)
const myAppLoading = ref(false)
const myAppSearchParams = reactive<API.AppQueryRequest>({
  pageNum: 1,
  pageSize: 8,
  appName: '',
})

// 精选应用
const featuredApps = ref<API.AppVO[]>([])
const featuredAppsTotal = ref(0)
const featuredAppLoading = ref(false)
const featuredAppSearchParams = reactive<API.AppQueryRequest>({
  pageNum: 1,
  pageSize: 12,
  appName: '',
  sortField: 'priority',
  sortOrder: 'descend',
})

/**
 * 创建应用
 */
const handleCreateApp = async () => {
  if (!userPrompt.value.trim()) {
    message.warning('请输入应用描述')
    return
  }

  creating.value = true
  try {
    const res = await addApp({ initPrompt: userPrompt.value })
    if (res.data.code === 0 && res.data.data) {
      message.success('应用创建成功，正在跳转...')
      // 跳转到对话页面，添加 create 参数标识是创建模式
      router.push(`/app/chat/${res.data.data}?create=true`)
    } else {
      message.error('创建失败：' + res.data.message)
    }
  } catch (error) {
    message.error('创建失败，请稍后重试')
  } finally {
    creating.value = false
  }
}

/**
 * 加载我的应用
 */
const loadMyApps = async () => {
  myAppLoading.value = true
  try {
    const res = await listMyAppVoByPage(myAppSearchParams)
    if (res.data.code === 0 && res.data.data) {
      myApps.value = res.data.data.records || []
      myAppsTotal.value = res.data.data.totalRow || 0
    } else {
      message.error('加载失败：' + res.data.message)
    }
  } catch (error) {
    message.error('加载失败，请稍后重试')
  } finally {
    myAppLoading.value = false
  }
}

/**
 * 加载精选应用
 */
const loadFeaturedApps = async () => {
  featuredAppLoading.value = true
  try {
    const res = await listAppVoByPage(featuredAppSearchParams)
    if (res.data.code === 0 && res.data.data) {
      featuredApps.value = res.data.data.records || []
      featuredAppsTotal.value = res.data.data.totalRow || 0
    } else {
      message.error('加载失败：' + res.data.message)
    }
  } catch (error) {
    message.error('加载失败，请稍后重试')
  } finally {
    featuredAppLoading.value = false
  }
}

/**
 * 删除应用
 */
const handleDeleteApp = async (id: string | undefined) => {
  if (!id) return

  try {
    const res = await deleteApp({ id })
    if (res.data.code === 0) {
      message.success('删除成功')
      loadMyApps()
    } else {
      message.error('删除失败：' + res.data.message)
    }
  } catch (error) {
    message.error('删除失败，请稍后重试')
  }
}

/**
 * 跳转到对话页面
 */
const goToChatPage = (id: string | undefined) => {
  if (!id) return
  router.push(`/app/chat/${id}`)
}

/**
 * 跳转到编辑页面
 */
const goToEditPage = (id: string | undefined) => {
  if (!id) return
  router.push(`/app/edit/${id}`)
}

onMounted(() => {
  // 加载我的应用和精选应用
  loadMyApps()
  loadFeaturedApps()
})
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

.prompt-input-wrapper {
  max-width: 800px;
  margin: 0 auto 24px;
}

.prompt-input {
  font-size: 16px;
  border-radius: 8px;
  margin-bottom: 16px;
}

.input-actions {
  display: flex;
  justify-content: center;
}

.quick-options {
  display: flex;
  gap: 12px;
  justify-content: center;
  flex-wrap: wrap;
}

.quick-options .ant-btn {
  border-radius: 16px;
  background: rgba(255, 255, 255, 0.2);
  color: white;
  border: 1px solid rgba(255, 255, 255, 0.3);
}

.quick-options .ant-btn:hover {
  background: rgba(255, 255, 255, 0.3);
  border-color: rgba(255, 255, 255, 0.5);
}

/* 应用列表区域 */
.apps-section {
  margin-bottom: 48px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.section-header .ant-typography {
  margin-bottom: 0 !important;
}

.app-card {
  height: 100%;
  transition: transform 0.3s, box-shadow 0.3s;
  cursor: pointer;
}

.app-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

.app-cover {
  height: 180px;
  overflow: hidden;
  background: #f5f5f5;
  display: flex;
  align-items: center;
  justify-content: center;
}

.app-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}


.app-description {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.app-info {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 12px;
  color: #999;
}

.app-actions {
  display: flex;
  gap: 8px;
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 24px;
}

.featured-section {
  background: #fafafa;
  padding: 24px;
  border-radius: 8px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .hero-content {
    padding: 32px 16px;
  }

  .section-header {
    flex-direction: column;
    gap: 16px;
    align-items: flex-start;
  }

  .section-header .ant-input-search {
    width: 100% !important;
  }

  .quick-options {
    gap: 8px;
  }
}

@media (max-width: 480px) {
  .hero-description {
    font-size: 16px;
  }

  .prompt-input {
    font-size: 14px;
  }
}
</style>
