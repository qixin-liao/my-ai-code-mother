<template>
  <div class="app-chat-page">
    <!-- 顶部栏 -->
    <div class="top-bar">
      <div class="left-section">
        <a-button type="text" @click="router.back()">
          <template #icon>
            <ArrowLeftOutlined />
          </template>
        </a-button>
        <a-typography-title :level="4" class="app-name">
          {{ appInfo?.appName || '未命名应用' }}
        </a-typography-title>
      </div>
      <div class="right-section">
        <a-button type="primary" :loading="deploying" @click="handleDeploy" v-if="appInfo">
          <template #icon>
            <CloudUploadOutlined />
          </template>
          部署
        </a-button>
      </div>
    </div>

    <!-- 主内容区域 -->
    <div class="main-content">
      <!-- 左侧对话区 -->
      <div class="chat-section">
        <div class="messages-container" ref="messagesContainer">
          <div v-for="(msg, index) in messages" :key="index"
            :class="['message-item', msg.role === 'user' ? 'user-message' : 'ai-message']">
            <div class="message-avatar">
              <a-avatar v-if="msg.role === 'user'" :src="loginUser?.userAvatar">
                <template #icon>
                  <UserOutlined />
                </template>
              </a-avatar>
              <a-avatar v-else style="background-color: #87d068">
                <template #icon>
                  <RobotOutlined />
                </template>
              </a-avatar>
            </div>
            <div class="message-content">
              <div class="message-text" v-html="renderMarkdown(msg.content)"></div>
              <div class="message-time">
                {{ dayjs(msg.timestamp).format('HH:mm:ss') }}
              </div>
            </div>
          </div>

          <!-- AI 正在输入 -->
          <div v-if="aiTyping" class="message-item ai-message">
            <div class="message-avatar">
              <a-avatar style="background-color: #87d068">
                <template #icon>
                  <RobotOutlined />
                </template>
              </a-avatar>
            </div>
            <div class="message-content">
              <div class="typing-indicator">
                <span></span>
                <span></span>
                <span></span>
              </div>
            </div>
          </div>
        </div>

        <!-- 输入框 -->
        <div class="input-area">
          <a-textarea v-model:value="userInput" placeholder="请输入消息，按 Enter 发送，Shift + Enter 换行"
            :auto-size="{ minRows: 2, maxRows: 4 }" @pressEnter="handleSendMessage" :disabled="sending" />
          <div class="input-actions">
            <a-button type="primary" :loading="sending" @click="handleSendMessage">
              <template #icon>
                <SendOutlined />
              </template>
              发送
            </a-button>
          </div>
        </div>
      </div>

      <!-- 右侧预览区 -->
      <div class="preview-section">
        <div class="preview-header">
          <a-typography-title :level="5"> 生成后的网页展示 </a-typography-title>
          <a-button v-if="previewUrl" type="link" :href="previewUrl" target="_blank">
            <template #icon>
              <LinkOutlined />
            </template>
            新窗口打开
          </a-button>
        </div>
        <div class="preview-content">
          <iframe v-if="previewUrl" :src="previewUrl" frameborder="0" class="preview-iframe"></iframe>
          <a-empty v-else description="等待 AI 生成网站..." :image="Empty.PRESENTED_IMAGE_SIMPLE" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, nextTick, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { message, Empty } from 'ant-design-vue'
import dayjs from 'dayjs'
import {
  ArrowLeftOutlined,
  CloudUploadOutlined,
  UserOutlined,
  RobotOutlined,
  SendOutlined,
  LinkOutlined,
} from '@ant-design/icons-vue'
import { getAppVoById, deployApp } from '@/api/appController'
import { useLoginUserStore } from '@/stores/loginUser'
import { marked } from 'marked'
import hljs from 'highlight.js'
import 'highlight.js/styles/github-dark.css'

const router = useRouter()
const route = useRoute()
const loginUserStore = useLoginUserStore()
const loginUser = computed(() => loginUserStore.loginUser)

const appId = ref<string>(route.params.id as string)
const appInfo = ref<API.AppVO>()

interface Message {
  role: 'user' | 'ai'
  content: string
  timestamp: Date
}

const messages = ref<Message[]>([])
const messagesContainer = ref<HTMLDivElement>()
const userInput = ref('')
const sending = ref(false)
const aiTyping = ref(false)
const deploying = ref(false)
const previewUrl = ref('') // 预览 URL

/**
 * 加载应用信息
 */
const loadAppInfo = async () => {
  try {
    const res = await getAppVoById({ id: appId.value })
    if (res.data.code === 0 && res.data.data) {
      appInfo.value = res.data.data

      // 只有在创建模式下（URL参数带有 create=true），才自动发送初始提示词
      const isCreateMode = route.query.create === 'true'
      if (isCreateMode && appInfo.value.initPrompt && messages.value.length === 0) {
        await sendMessageToAI(appInfo.value.initPrompt, false)
      }
      // 如果不是创建模式,直接预览页面
      else {
        updatePreview()
      }
    } else {
      message.error('加载应用信息失败：' + res.data.message)
      router.back()
    }
  } catch (error) {
    message.error('加载应用信息失败，请稍后重试')
    router.back()
  }
}

/**
 * 发送消息
 */
const handleSendMessage = async (e?: KeyboardEvent) => {
  // 如果按下了 Shift + Enter，不发送消息
  if (e && e.shiftKey) {
    return
  }
  e?.preventDefault()

  if (!userInput.value.trim() || sending.value) {
    return
  }

  const message = userInput.value
  userInput.value = ''
  await sendMessageToAI(message, false)
}

/**
 * 发送消息给 AI
 */
const sendMessageToAI = async (content: string, isInitial: boolean = false) => {
  // 添加用户消息
  if (!isInitial) {
    messages.value.push({
      role: 'user',
      content,
      timestamp: new Date(),
    })
  }

  scrollToBottom()
  sending.value = true
  aiTyping.value = true

  try {
    // 使用 fetch 调用 SSE 接口
    const response = await fetch(
      `http://localhost:8123/api/app/chat/gen/code?appId=${appId.value}&message=${encodeURIComponent(content)}`,
      {
        method: 'GET',
        headers: {
          'Accept': 'text/event-stream',
        },
        credentials: 'include', // 携带 Cookie
      }
    )

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`)
    }

    const reader = response.body?.getReader()
    const decoder = new TextDecoder()

    let aiMessage = ''
    let aiMessageIndex = -1

    // 读取流式数据
    while (reader) {
      const { done, value } = await reader.read()

      if (done) {
        break
      }

      // 解码数据
      const chunk = decoder.decode(value, { stream: true })
      const lines = chunk.split('\n')

      for (const line of lines) {
        if (line.startsWith('data:')) {
          const data = line.substring(5).trim()

          if (!data) continue

          try {
            // 解析 JSON 格式 {"V": "内容"}
            const json = JSON.parse(data)
            if (json.V) {
              aiMessage += json.V

              // 如果还没有创建 AI 消息，创建一个
              if (aiMessageIndex === -1) {
                messages.value.push({
                  role: 'ai',
                  content: aiMessage,
                  timestamp: new Date(),
                })
                aiMessageIndex = messages.value.length - 1
                aiTyping.value = false
              } else {
                // 更新现有的 AI 消息
                messages.value[aiMessageIndex].content = aiMessage
              }

              scrollToBottom()
            }
          } catch (e) {
            // 忽略 JSON 解析错误
            console.debug('Parse JSON error:', e)
          }
        } else if (line.startsWith('event:')) {
          const event = line.substring(6).trim()
          if (event === 'done') {
            // 收到结束事件，重新加载应用信息并更新预览
            await reloadAppInfoAndUpdatePreview()
            aiTyping.value = false
            sending.value = false
            return // 结束处理
          }
        }
      }
    }

    // 流结束时也尝试更新预览
    await reloadAppInfoAndUpdatePreview()
    aiTyping.value = false
    sending.value = false
  } catch (error) {
    console.error('Send message error:', error)
    aiTyping.value = false
    sending.value = false
    message.error('AI 回复出错，请稍后重试')
  }
}

/**
 * 重新加载应用信息并更新预览
 */
const reloadAppInfoAndUpdatePreview = async () => {
  try {
    // 重新获取应用信息，获取最新的 codeGenType
    const res = await getAppVoById({ id: appId.value })
    if (res.data.code === 0 && res.data.data) {
      appInfo.value = res.data.data
      // 更新预览
      updatePreview()
    }
  } catch (error) {
    console.error('重新加载应用信息失败:', error)
    // 即使加载失败也尝试更新预览
    updatePreview()
  }
}

/**
 * 更新预览
 */
const updatePreview = () => {
  if (appInfo.value?.codeGenType && appId.value) {
    previewUrl.value = `http://localhost:8123/api/static/${appInfo.value.codeGenType}_${appId.value}/`
  }
  // 刷新 iframe
  if (previewUrl.value) {
    previewUrl.value = previewUrl.value + '?t=' + new Date().getTime()
  }
}

/**
 * 部署应用
 */
const handleDeploy = async () => {
  if (!appId.value) return

  deploying.value = true
  try {
    const res = await deployApp({ appId: appId.value })
    if (res.data.code === 0 && res.data.data) {
      message.success('部署成功！')
      message.info({
        content: `部署地址：${res.data.data}`,
        duration: 10,
      })
    } else {
      message.error('部署失败：' + res.data.message)
    }
  } catch (error) {
    message.error('部署失败，请稍后重试')
  } finally {
    deploying.value = false
  }
}

/**
 * 滚动到底部
 */
const scrollToBottom = () => {
  nextTick(() => {
    if (messagesContainer.value) {
      messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
    }
  })
}

/**
 * 配置 marked 使用 highlight.js
 */
marked.setOptions({
  highlight: function (code: string, lang: string) {
    if (lang && hljs.getLanguage(lang)) {
      try {
        return hljs.highlight(code, { language: lang }).value
      } catch (err) {
        console.error('Highlight error:', err)
      }
    }
    return hljs.highlightAuto(code).value
  },
  breaks: true, // 支持 GitHub 风格的换行
} as any)

/**
 * 渲染 Markdown
 */
const renderMarkdown = (content: string) => {
  return marked.parse(content)
}

onMounted(() => {
  // 加载应用信息，如果是创建点击生成按钮，则发送初始提示词
  loadAppInfo()
})
</script>

<style scoped>
.app-chat-page {
  display: flex;
  flex-direction: column;
  height: calc(100vh - 64px - 120px);
  min-height: 600px;
}

.top-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 24px;
  background: white;
  border-bottom: 1px solid #f0f0f0;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.left-section {
  display: flex;
  align-items: center;
  gap: 12px;
}

.app-name {
  margin: 0 !important;
}

.right-section {
  display: flex;
  gap: 12px;
}

.main-content {
  display: flex;
  flex: 1;
  overflow: hidden;
  gap: 16px;
  padding: 16px;
  background: #f5f5f5;
}

.chat-section {
  flex: 1;
  display: flex;
  flex-direction: column;
  background: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.messages-container {
  flex: 1;
  overflow-y: auto;
  padding: 24px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.message-item {
  display: flex;
  gap: 12px;
  animation: fadeIn 0.3s ease-in;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }

  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.user-message {
  flex-direction: row-reverse;
}

.message-avatar {
  flex-shrink: 0;
}

.message-content {
  max-width: 70%;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.user-message .message-content {
  align-items: flex-end;
}

.message-text {
  padding: 12px 16px;
  border-radius: 8px;
  word-wrap: break-word;
  line-height: 1.6;
}

.user-message .message-text {
  background: #1890ff;
  color: white;
  border-bottom-right-radius: 2px;
}

.ai-message .message-text {
  background: #f5f5f5;
  color: #333;
  border-bottom-left-radius: 2px;
}

.message-text :deep(pre) {
  background: #0d1117;
  padding: 16px;
  border-radius: 6px;
  overflow-x: auto;
  margin: 8px 0;
}

.message-text :deep(pre code) {
  font-family: 'Consolas', 'Monaco', 'Courier New', monospace;
  font-size: 14px;
  line-height: 1.6;
  color: #c9d1d9;
  background: transparent;
  padding: 0;
}

.message-text :deep(code) {
  font-family: 'Consolas', 'Monaco', 'Courier New', monospace;
  font-size: 14px;
  background: rgba(175, 184, 193, 0.2);
  padding: 2px 6px;
  border-radius: 3px;
}

.message-text :deep(p) {
  margin: 8px 0;
}

.message-text :deep(ul),
.message-text :deep(ol) {
  margin: 8px 0;
  padding-left: 24px;
}

.message-time {
  font-size: 12px;
  color: #999;
}

.typing-indicator {
  display: flex;
  gap: 4px;
  padding: 12px 16px;
  background: #f5f5f5;
  border-radius: 8px;
}

.typing-indicator span {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #999;
  animation: typing 1.4s infinite;
}

.typing-indicator span:nth-child(2) {
  animation-delay: 0.2s;
}

.typing-indicator span:nth-child(3) {
  animation-delay: 0.4s;
}

@keyframes typing {

  0%,
  60%,
  100% {
    transform: translateY(0);
  }

  30% {
    transform: translateY(-10px);
  }
}

.input-area {
  padding: 16px 24px;
  border-top: 1px solid #f0f0f0;
  background: white;
}

.input-actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 12px;
}

.preview-section {
  flex: 1;
  display: flex;
  flex-direction: column;
  background: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.preview-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 24px;
  border-bottom: 1px solid #f0f0f0;
}

.preview-header .ant-typography {
  margin: 0 !important;
}

.preview-content {
  flex: 1;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
}

.preview-iframe {
  width: 100%;
  height: 100%;
  border: none;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .main-content {
    flex-direction: column;
  }

  .chat-section,
  .preview-section {
    flex: none;
    height: 50%;
  }
}

@media (max-width: 768px) {
  .top-bar {
    padding: 12px 16px;
  }

  .left-section {
    gap: 8px;
  }

  .app-name {
    font-size: 16px !important;
  }

  .main-content {
    padding: 8px;
    gap: 8px;
  }

  .messages-container {
    padding: 16px;
  }

  .message-content {
    max-width: 85%;
  }

  .input-area {
    padding: 12px 16px;
  }
}
</style>
