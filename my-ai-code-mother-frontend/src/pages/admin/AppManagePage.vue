<template>
    <div id="appManagePage">
        <!-- 搜索表单 -->
        <a-form layout="inline" :model="searchParams" @finish="doSearch">
            <a-form-item label="应用ID">
                <a-input v-model:value="searchParams.id" placeholder="输入应用ID" style="width: 200px" />
            </a-form-item>
            <a-form-item label="应用名称">
                <a-input v-model:value="searchParams.appName" placeholder="输入应用名称" />
            </a-form-item>
            <a-form-item label="用户ID">
                <a-input v-model:value="searchParams.userId" placeholder="输入用户ID" style="width: 200px" />
            </a-form-item>
            <a-form-item label="优先级">
                <a-input-number v-model:value="searchParams.priority" placeholder="优先级" style="width: 120px" />
            </a-form-item>
            <a-form-item>
                <a-button type="primary" html-type="submit">搜索</a-button>
            </a-form-item>
            <a-form-item>
                <a-button @click="handleReset">重置</a-button>
            </a-form-item>
        </a-form>
        <a-divider />

        <!-- 表格 -->
        <a-table :columns="columns" :data-source="data" :pagination="pagination" :loading="loading"
            @change="doTableChange" :scroll="{ x: 1300 }">
            <template #bodyCell="{ column, record }">
                <template v-if="column.dataIndex === 'cover'">
                    <a-image v-if="record.cover" :src="record.cover" :width="80" />
                    <span v-else>-</span>
                </template>
                <template v-else-if="column.dataIndex === 'initPrompt'">
                    <a-tooltip :title="record.initPrompt">
                        <div class="text-ellipsis">
                            {{ record.initPrompt || '-' }}
                        </div>
                    </a-tooltip>
                </template>
                <template v-else-if="column.dataIndex === 'priority'">
                    <a-tag v-if="record.priority && record.priority >= 99" color="gold">
                        精选 ({{ record.priority }})
                    </a-tag>
                    <span v-else>{{ record.priority || 0 }}</span>
                </template>
                <template v-else-if="column.dataIndex === 'deployKey'">
                    <a-tag v-if="record.deployKey" color="green">
                        已部署
                    </a-tag>
                    <a-tag v-else color="default">
                        未部署
                    </a-tag>
                </template>
                <template v-else-if="column.dataIndex === 'user'">
                    <span>{{ record.user?.userName || '-' }}</span>
                </template>
                <template v-else-if="column.dataIndex === 'createTime'">
                    {{ dayjs(record.createTime).format('YYYY-MM-DD HH:mm:ss') }}
                </template>
                <template v-else-if="column.dataIndex === 'updateTime'">
                    {{ dayjs(record.updateTime).format('YYYY-MM-DD HH:mm:ss') }}
                </template>
                <template v-else-if="column.key === 'action'">
                    <a-space>
                        <a-button type="link" size="small" @click="handleEdit(record)">
                            编辑
                        </a-button>
                        <a-button type="link" size="small" @click="handleSetFeatured(record)"
                            v-if="!record.priority || record.priority < 99">
                            设为精选
                        </a-button>
                        <a-button type="link" size="small" @click="handleCancelFeatured(record)" v-else>
                            取消精选
                        </a-button>
                        <a-popconfirm title="确定要删除这个应用吗？" ok-text="确定" cancel-text="取消" @confirm="doDelete(record.id)">
                            <a-button type="link" size="small" danger>删除</a-button>
                        </a-popconfirm>
                    </a-space>
                </template>
            </template>
        </a-table>

        <!-- 编辑弹窗 -->
        <a-modal v-model:open="editModalVisible" title="编辑应用" @ok="handleEditSubmit" @cancel="handleEditCancel"
            :confirm-loading="editSubmitting">
            <a-form :model="editForm" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
                <a-form-item label="应用ID">
                    <a-input v-model:value="editForm.id" disabled />
                </a-form-item>
                <a-form-item label="应用名称" required>
                    <a-input v-model:value="editForm.appName" placeholder="请输入应用名称" :maxlength="50" />
                </a-form-item>
                <a-form-item label="应用封面">
                    <a-input v-model:value="editForm.cover" placeholder="请输入封面图片URL" />
                    <a-image v-if="editForm.cover" :src="editForm.cover" :width="100" style="margin-top: 8px" />
                </a-form-item>
                <a-form-item label="优先级">
                    <a-input-number v-model:value="editForm.priority" :min="0" :max="999" placeholder="数值越大优先级越高"
                        style="width: 100%" />
                    <div style="color: #999; font-size: 12px; margin-top: 4px">
                        优先级 ≥ 99 将显示为精选应用
                    </div>
                </a-form-item>
            </a-form>
        </a-modal>
    </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { message } from 'ant-design-vue'
import dayjs from 'dayjs'
import {
    listAppVoByPageAdmin,
    deleteAppByAdmin,
    updateAppByAdmin,
} from '@/api/appController'

// 表格列定义
const columns = [
    {
        title: 'ID',
        dataIndex: 'id',
        width: 100,
    },
    {
        title: '应用名称',
        dataIndex: 'appName',
        width: 150,
    },
    {
        title: '封面',
        dataIndex: 'cover',
        width: 100,
    },
    {
        title: '初始提示词',
        dataIndex: 'initPrompt',
        width: 200,
    },
    {
        title: '优先级',
        dataIndex: 'priority',
        width: 120,
    },
    {
        title: '部署状态',
        dataIndex: 'deployKey',
        width: 100,
    },
    {
        title: '创建者',
        dataIndex: 'user',
        width: 100,
    },
    {
        title: '创建时间',
        dataIndex: 'createTime',
        width: 180,
    },
    {
        title: '更新时间',
        dataIndex: 'updateTime',
        width: 180,
    },
    {
        title: '操作',
        key: 'action',
        width: 240,
        fixed: 'right',
    },
]

// 数据
const data = ref<API.AppVO[]>([])
const total = ref(0)
const loading = ref(false)

// 搜索条件
const searchParams = reactive<API.AppQueryRequest>({
    pageNum: 1,
    pageSize: 10,
})

// 分页参数
const pagination = computed(() => {
    return {
        current: searchParams.pageNum ?? 1,
        pageSize: searchParams.pageSize ?? 10,
        total: total.value,
        showSizeChanger: true,
        showTotal: (total: number) => `共 ${total} 条`,
    }
})

// 编辑相关
const editModalVisible = ref(false)
const editSubmitting = ref(false)
const editForm = reactive<{
    id?: string
    appName?: string
    cover?: string
    priority?: number
}>({})

/**
 * 获取数据
 */
const fetchData = async () => {
    loading.value = true
    try {
        const res = await listAppVoByPageAdmin(searchParams)
        if (res.data.code === 0 && res.data.data) {
            data.value = res.data.data.records || []
            total.value = res.data.data.totalRow || 0
        } else {
            message.error('获取数据失败：' + res.data.message)
        }
    } catch (error) {
        message.error('获取数据失败，请稍后重试')
    } finally {
        loading.value = false
    }
}

/**
 * 搜索处理
 */
const doSearch = () => {
    searchParams.pageNum = 1
    fetchData()
}

/**
 * 重置搜索
 */
const handleReset = () => {
    Object.assign(searchParams, {
        pageNum: 1,
        pageSize: 10,
        id: undefined,
        appName: undefined,
        userId: undefined,
        priority: undefined,
    })
    fetchData()
}

/**
 * 表格变化处理
 */
const doTableChange = (page: any) => {
    searchParams.pageNum = page.current
    searchParams.pageSize = page.pageSize
    fetchData()
}

/**
 * 删除数据
 */
const doDelete = async (id: string | undefined) => {
    if (!id) return

    try {
        const res = await deleteAppByAdmin({ id })
        if (res.data.code === 0) {
            message.success('删除成功')
            fetchData()
        } else {
            message.error('删除失败：' + res.data.message)
        }
    } catch (error) {
        message.error('删除失败，请稍后重试')
    }
}

/**
 * 编辑
 */
const handleEdit = (record: API.AppVO) => {
    Object.assign(editForm, {
        id: record.id,
        appName: record.appName,
        cover: record.cover,
        priority: record.priority,
    })
    editModalVisible.value = true
}

/**
 * 设为精选
 */
const handleSetFeatured = async (record: API.AppVO) => {
    if (!record.id) return

    try {
        const res = await updateAppByAdmin({
            id: record.id,
            appName: record.appName,
            priority: 99,
        })
        if (res.data.code === 0) {
            message.success('设为精选成功')
            fetchData()
        } else {
            message.error('操作失败：' + res.data.message)
        }
    } catch (error) {
        message.error('操作失败，请稍后重试')
    }
}

/**
 * 取消精选
 */
const handleCancelFeatured = async (record: API.AppVO) => {
    if (!record.id) return

    try {
        const res = await updateAppByAdmin({
            id: record.id,
            appName: record.appName,
            priority: 0,
        })
        if (res.data.code === 0) {
            message.success('取消精选成功')
            fetchData()
        } else {
            message.error('操作失败：' + res.data.message)
        }
    } catch (error) {
        message.error('操作失败，请稍后重试')
    }
}

/**
 * 提交编辑
 */
const handleEditSubmit = async () => {
    if (!editForm.id) return
    if (!editForm.appName?.trim()) {
        message.warning('请输入应用名称')
        return
    }

    editSubmitting.value = true
    try {
        const res = await updateAppByAdmin({
            id: editForm.id,
            appName: editForm.appName,
            cover: editForm.cover,
            priority: editForm.priority,
        })
        if (res.data.code === 0) {
            message.success('编辑成功')
            editModalVisible.value = false
            fetchData()
        } else {
            message.error('编辑失败：' + res.data.message)
        }
    } catch (error) {
        message.error('编辑失败，请稍后重试')
    } finally {
        editSubmitting.value = false
    }
}

/**
 * 取消编辑
 */
const handleEditCancel = () => {
    editModalVisible.value = false
    Object.assign(editForm, {})
}

// 页面加载时请求一次
onMounted(() => {
    fetchData()
})
</script>

<style scoped>
.text-ellipsis {
    max-width: 200px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}
</style>
