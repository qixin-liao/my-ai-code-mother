<script setup lang="ts">
import BasicLayout from '@/layouts/BasicLayout.vue'
import { healthCheck } from '@/api/jiankangjiancha'
import { useLoginUserStore } from '@/stores/loginUser'

/**
 * 健康检查
 */
healthCheck()
  .then((res) => {
    console.log("返回：", res)
  })
  .catch((err) => {
    console.log(err)
  })


// /**
//  * 获取登录用户信息
//  */
// const loginUserStore = useLoginUserStore()
// loginUserStore.fetchLoginUser().catch((err) => {
//   console.log(err)
// })

</script>

<template>
  <BasicLayout />
</template>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html,
body {
  height: 100%;
}

#app {
  height: 100%;
}
</style>
