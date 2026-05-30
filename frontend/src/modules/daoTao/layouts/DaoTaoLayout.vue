<template>
  <section class="dao-tao-layout">
    <RouterView :key="$route.fullPath" />
  </section>
</template>

<style scoped>
.dao-tao-layout {
  display: grid;
  gap: 14px;
}
</style>