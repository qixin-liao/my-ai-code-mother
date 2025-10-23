<template>
  <a-layout class="layout">
    <!-- 顶部导航栏 -->
    <GlobalHeader />

    <!-- 主体内容区域 -->
    <a-layout-content class="content">
      <div class="content-wrapper">
        <router-view />
      </div>
    </a-layout-content>

    <!-- 底部版权信息 -->
    <GlobalFooter />
  </a-layout>
</template>

<script setup lang="ts">
import GlobalHeader from '@/components/GlobalHeader.vue'
import GlobalFooter from '@/components/GlobalFooter.vue'
</script>

<style scoped>
.layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.content {
  flex: 1;
  margin-top: 64px;
  /* 为固定的 header 留出空间 */
  background: #fff;
}

.content-wrapper {
  max-width: 90%;
  margin: 0 auto;
  padding: 24px;
  min-height: calc(100vh - 64px - 120px);
  /* 减去 header 和 footer 的高度 */
}

/* 响应式设计 */
@media (max-width: 768px) {
  .content-wrapper {
    padding: 16px;
  }
}

@media (max-width: 480px) {
  .content-wrapper {
    padding: 12px;
  }
}
</style>
