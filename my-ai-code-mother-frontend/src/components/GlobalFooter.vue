<template>
  <a-layout-footer class="footer">
    <div class="footer-content">
      <p class="copyright">
        编程导航原创项目 by
        <a
          href="https://www.codefather.cn"
          target="_blank"
          rel="noopener noreferrer"
          class="author-link"
        >
          程序员鱼皮
        </a>
      </p>
    </div>
  </a-layout-footer>
</template>

<script setup lang="ts">
// 无需额外逻辑
</script>

<style scoped>
.footer {
  background: #f0f2f5;
  border-top: 1px solid #e8e8e8;
  padding: 24px 0;
  text-align: center;
  position: relative;
  bottom: 0;
  width: 100%;
  margin-top: auto;
}

.footer-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 24px;
}

.copyright {
  margin: 0;
  color: #666;
  font-size: 14px;
  line-height: 1.5;
}

.author-link {
  color: #1890ff;
  text-decoration: none;
  transition: color 0.3s;
}

.author-link:hover {
  color: #40a9ff;
  text-decoration: underline;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .footer-content {
    padding: 0 16px;
  }

  .copyright {
    font-size: 12px;
  }
}
</style>
