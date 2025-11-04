<template>
  <a-layout-header class="header">
    <div class="header-content">
      <!-- 左侧 Logo 和标题 -->
      <div class="header-left">
        <img src="/logo.svg" alt="Logo" class="logo" />
        <h1 class="site-title">GenCodeAI</h1>
      </div>

      <!-- 中间菜单 -->
      <div class="header-center">
        <a-menu v-model:selectedKeys="selectedKeys" mode="horizontal" :items="menuItems" class="header-menu"
          @click="handleMenuClick" />
      </div>

      <!-- 右侧用户操作区 -->
      <div class="user-login-status">
        <div v-if="loginUserStore.loginUser.id">
          <a-dropdown>
            <a-space style="cursor: pointer">
              <a-avatar
                :src="loginUserStore.loginUser.userAvatar || 'https://mix2.oss-cn-shanghai.aliyuncs.com/wx/tuiwen/user/twgenerated/2025/10/17/dy5d29kx5q2b4b3e831eacd6f1193a50ca136c6a5b.jpg'" />
              {{ loginUserStore.loginUser.userName ?? '无名' }}
            </a-space>
            <template #overlay>
              <a-menu>
                <a-menu-item @click="doLogout">
                  <LogoutOutlined />
                  退出登录
                </a-menu-item>
              </a-menu>
            </template>
          </a-dropdown>
        </div>
        <div v-else>
          <a-button type="primary" href="/user/login">登录</a-button>
        </div>
      </div>

    </div>
  </a-layout-header>
</template>

<script setup lang="ts">
import { computed, ref, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import type { MenuProps } from 'ant-design-vue'
import { message } from 'ant-design-vue'
import { LogoutOutlined } from '@ant-design/icons-vue'
import { useLoginUserStore } from '@/stores/loginUser'
import { userLogout } from '@/api/userController'

const loginUserStore = useLoginUserStore()
const router = useRouter()
const route = useRoute()

// 当前选中的菜单项
const selectedKeys = ref<string[]>(['home'])

// 菜单配置
const originMenuItems = ref([
  {
    key: '/',
    label: '首页',
  },
  {
    key: '/admin/userManage',
    label: '用户管理',
  },
  {
    key: '/admin/appManage',
    label: '应用管理',
  },
  {
    key: '/about',
    label: '关于',
  },
])
/**
 * 根据用户角色动态生成菜单
 */
const menuItems = computed(() => {
  return originMenuItems.value.filter((item) => {
    // 只有管理员可以看到管理菜单
    const adminPaths = ['/admin/userManage', '/admin/appManage']
    if (adminPaths.includes(item.key)) {
      return loginUserStore.loginUser.userRole === 'admin'
    }
    return true
  })
})

// 根据当前路由更新选中的菜单项
const updateSelectedKeys = () => {
  const path = route.path
  selectedKeys.value = [path]
}

// 监听路由变化
watch(route, updateSelectedKeys, { immediate: true })

// 菜单点击处理
const handleMenuClick = ({ key }: { key: string }) => {
  router.push(key)
}
// 用户注销
const doLogout = async () => {
  const res = await userLogout()
  if (res.data.code === 0) {
    loginUserStore.updateLoginUser({
      userName: '未登录',
    })
    message.success('退出登录成功')
    await router.push('/user/login')
  } else {
    message.error('退出登录失败，' + res.data.message)
  }
}

</script>

<style scoped>
.header {
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 0;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
}

.header-content {
  max-width: 90%;
  margin: 0 auto;
  padding: 0 24px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 64px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo {
  width: 40px;
  height: 40px;
  object-fit: contain;
}

.site-title {
  margin: 0;
  font-size: 20px;
  font-weight: 600;
  color: #1890ff;
}

.header-center {
  flex: 1;
  display: flex;
  justify-content: flex-start;
}

.header-menu {
  border-bottom: none;
  background: transparent;
  min-width: 400px;
}

.header-right {
  display: flex;
  align-items: center;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .header-content {
    padding: 0 16px;
  }

  .site-title {
    font-size: 16px;
  }

  .header-menu {
    min-width: auto;
  }

  .header-center {
    display: none;
  }
}

@media (max-width: 480px) {
  .header-left {
    gap: 8px;
  }

  .logo {
    width: 32px;
    height: 32px;
  }

  .site-title {
    font-size: 14px;
  }
}
</style>
