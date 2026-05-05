<!-- src/components/layout/MainLayout.vue -->
<template>
  <div class="main-layout">
    <AppNav />

    <div class="main-body">
      <router-view />
    </div>
  </div>
</template>

<script setup>
import AppNav from '@/components/nav/AppNav.vue';
</script>

<style scoped>
.main-layout {
  width: 100vw;
  height: 100vh;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  background: #f4f6f9;
}

.main-body {
  width: 100%;
  flex: 1;
  min-height: 0;
  overflow: hidden;
  background: #f4f6f9;
}
</style>
