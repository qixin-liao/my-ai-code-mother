<template>
  <a-layout-header class="header">
    <div class="header-content">
      <!-- 左侧 Logo 和标题 -->
      <div class="header-left">
        <img src="/logo.svg" alt="Logo" class="logo" />
        <h1 class="site-title">AI 编程助手</h1>
      </div>

      <!-- 中间菜单 -->
      <div class="header-center">
        <a-menu
          v-model:selectedKeys="selectedKeys"
          mode="horizontal"
          :items="menuItems"
          class="header-menu"
          @click="handleMenuClick"
        />
      </div>

      <!-- 右侧用户操作区 -->
      <div class="header-right">
        <a-button type="primary" @click="handleLogin"> 登录 </a-button>
      </div>
    </div>
  </a-layout-header>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import type { MenuProps } from 'ant-design-vue'

const router = useRouter()
const route = useRoute()

// 当前选中的菜单项
const selectedKeys = ref<string[]>(['home'])

// 菜单配置
const menuItems: MenuProps['items'] = [
  {
    key: 'home',
    label: '首页',
  },
  {
    key: 'about',
    label: '关于',
  },
]

// 根据当前路由更新选中的菜单项
const updateSelectedKeys = () => {
  const path = route.path
  if (path === '/') {
    selectedKeys.value = ['home']
  } else if (path === '/about') {
    selectedKeys.value = ['about']
  }
}

// 监听路由变化
watch(route, updateSelectedKeys, { immediate: true })

// 菜单点击处理
const handleMenuClick = ({ key }: { key: string }) => {
  const routeMap: Record<string, string> = {
    home: '/',
    about: '/about',
  }

  const targetRoute = routeMap[key]
  if (targetRoute && targetRoute !== route.path) {
    router.push(targetRoute)
  }
}

// 登录处理
const handleLogin = () => {
  console.log('点击登录')
  // TODO: 实现登录逻辑
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
  max-width: 1200px;
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
  justify-content: center;
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
