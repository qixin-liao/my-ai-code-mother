<script setup lang="ts">
import BasicLayout from '@/layouts/BasicLayout.vue'
import { healthCheck } from '@/api/jiankangjiancha'

healthCheck()
  .then((res) => {
    console.log("返回：",res)
  })
  .catch((err) => {
    console.log(err)
  })
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
