<template>
  <div class="app-edit-page">
    <a-card title="编辑应用信息" :loading="loading">
      <template #extra>
        <a-button @click="router.back()">返回</a-button>
      </template>

      <a-form
        :model="form"
        :label-col="{ span: 4 }"
        :wrapper-col="{ span: 16 }"
        @finish="handleSubmit"
      >
        <a-form-item label="应用ID">
          <a-input v-model:value="form.id" disabled />
        </a-form-item>

        <a-form-item
          label="应用名称"
          name="appName"
          :rules="[
            { required: true, message: '请输入应用名称' },
            { max: 50, message: '应用名称不能超过50个字符' },
          ]"
        >
          <a-input
            v-model:value="form.appName"
            placeholder="请输入应用名称"
            :maxlength="50"
            show-count
          />
        </a-form-item>

        <a-form-item label="初始提示词">
          <a-textarea
            v-model:value="form.initPrompt"
            placeholder="初始提示词（创建后不可修改）"
            :rows="4"
            disabled
          />
        </a-form-item>

        <a-form-item label="应用封面" v-if="isAdmin">
          <a-input
            v-model:value="form.cover"
            placeholder="请输入封面图片URL"
          />
          <a-upload
            name="file"
            list-type="picture-card"
            class="avatar-uploader"
            :show-upload-list="false"
            :before-upload="beforeUpload"
            @change="handleUploadChange"
            style="margin-top: 8px"
          >
            <img v-if="form.cover" :src="form.cover" alt="封面" style="width: 100%" />
            <div v-else>
              <loading-outlined v-if="uploading"></loading-outlined>
              <plus-outlined v-else></plus-outlined>
              <div class="ant-upload-text">上传封面</div>
            </div>
          </a-upload>
        </a-form-item>

        <a-form-item label="优先级" v-if="isAdmin">
          <a-input-number
            v-model:value="form.priority"
            :min="0"
            :max="999"
            placeholder="数值越大优先级越高"
            style="width: 100%"
          />
          <div style="color: #999; font-size: 12px; margin-top: 4px">
            优先级 ≥ 99 将显示为精选应用
          </div>
        </a-form-item>

        <a-form-item label="创建者">
          <a-input v-model:value="creatorName" disabled />
        </a-form-item>

        <a-form-item label="创建时间">
          <a-input v-model:value="createTimeFormatted" disabled />
        </a-form-item>

        <a-form-item label="更新时间">
          <a-input v-model:value="updateTimeFormatted" disabled />
        </a-form-item>

        <a-form-item :wrapper-col="{ offset: 4, span: 16 }">
          <a-space>
            <a-button type="primary" html-type="submit" :loading="submitting">
              保存
            </a-button>
            <a-button @click="router.back()">取消</a-button>
          </a-space>
        </a-form-item>
      </a-form>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { message } from 'ant-design-vue'
import dayjs from 'dayjs'
import {
  PlusOutlined,
  LoadingOutlined,
} from '@ant-design/icons-vue'
import {
  getAppVoById,
  getAppById,
  updateApp,
  updateAppByAdmin,
} from '@/api/appController'
import { useLoginUserStore } from '@/stores/loginUser'

const router = useRouter()
const route = useRoute()
const loginUserStore = useLoginUserStore()

const appId = ref<string>(route.params.id as string)
const loading = ref(false)
const submitting = ref(false)
const uploading = ref(false)

// 判断是否是管理员
const isAdmin = computed(() => {
  return loginUserStore.loginUser?.userRole === 'admin'
})

// 表单数据
const form = reactive<{
  id?: string
  appName?: string
  initPrompt?: string
  cover?: string
  priority?: number
  userId?: string
  createTime?: string
  updateTime?: string
}>({})

// 创建者名称
const creatorName = ref('')

// 格式化的时间
const createTimeFormatted = computed(() => {
  return form.createTime ? dayjs(form.createTime).format('YYYY-MM-DD HH:mm:ss') : '-'
})

const updateTimeFormatted = computed(() => {
  return form.updateTime ? dayjs(form.updateTime).format('YYYY-MM-DD HH:mm:ss') : '-'
})

/**
 * 加载应用信息
 */
const loadAppInfo = async () => {
  loading.value = true
  try {
    // 如果是管理员，使用 getAppById 获取完整信息
    if (isAdmin.value) {
      const res = await getAppById({ id: appId.value })
      if (res.data.code === 0 && res.data.data) {
        Object.assign(form, res.data.data)
        
        // 获取创建者信息
        const voRes = await getAppVoById({ id: appId.value })
        if (voRes.data.code === 0 && voRes.data.data) {
          creatorName.value = voRes.data.data.user?.userName || '未知'
        }
      } else {
        message.error('加载应用信息失败：' + res.data.message)
        router.back()
      }
    } else {
      // 普通用户使用 getAppVoById
      const res = await getAppVoById({ id: appId.value })
      if (res.data.code === 0 && res.data.data) {
        const appData = res.data.data
        Object.assign(form, appData)
        creatorName.value = appData.user?.userName || '未知'
        
        // 检查是否是自己的应用
        if (appData.userId !== loginUserStore.loginUser?.id) {
          message.error('无权编辑此应用')
          router.back()
          return
        }
      } else {
        message.error('加载应用信息失败：' + res.data.message)
        router.back()
      }
    }
  } catch (error) {
    message.error('加载应用信息失败，请稍后重试')
    router.back()
  } finally {
    loading.value = false
  }
}

/**
 * 提交表单
 */
const handleSubmit = async () => {
  if (!form.id) return
  if (!form.appName?.trim()) {
    message.warning('请输入应用名称')
    return
  }

  submitting.value = true
  try {
    // 根据用户角色调用不同的接口
    if (isAdmin.value) {
      const res = await updateAppByAdmin({
        id: form.id,
        appName: form.appName,
        cover: form.cover,
        priority: form.priority,
      })
      if (res.data.code === 0) {
        message.success('保存成功')
        router.back()
      } else {
        message.error('保存失败：' + res.data.message)
      }
    } else {
      const res = await updateApp({
        id: form.id,
        appName: form.appName,
      })
      if (res.data.code === 0) {
        message.success('保存成功')
        router.back()
      } else {
        message.error('保存失败：' + res.data.message)
      }
    }
  } catch (error) {
    message.error('保存失败，请稍后重试')
  } finally {
    submitting.value = false
  }
}

/**
 * 上传前校验
 */
const beforeUpload = (file: File) => {
  const isImage = file.type.startsWith('image/')
  if (!isImage) {
    message.error('只能上传图片文件！')
  }
  const isLt2M = file.size / 1024 / 1024 < 2
  if (!isLt2M) {
    message.error('图片大小不能超过 2MB！')
  }
  return isImage && isLt2M
}

/**
 * 上传变化处理
 */
const handleUploadChange = (info: any) => {
  if (info.file.status === 'uploading') {
    uploading.value = true
    return
  }
  if (info.file.status === 'done') {
    uploading.value = false
    // 这里应该从响应中获取图片URL
    // form.cover = info.file.response.url
    message.success('上传成功')
  }
  if (info.file.status === 'error') {
    uploading.value = false
    message.error('上传失败')
  }
}

onMounted(() => {
  loadAppInfo()
})
</script>

<style scoped>
.app-edit-page {
  max-width: 900px;
  margin: 0 auto;
}

.avatar-uploader :deep(.ant-upload) {
  width: 128px;
  height: 128px;
}

.avatar-uploader :deep(.ant-upload-select-picture-card) {
  width: 128px;
  height: 128px;
}
</style>

