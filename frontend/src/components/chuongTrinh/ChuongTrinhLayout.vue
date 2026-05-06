<template>
  <ModuleLayoutShell :config="chuongTrinhModuleConfig" />
</template>

<script setup>
import ModuleLayoutShell from '@/components/layout/ModuleLayoutShell.vue';
import { chuongTrinhModuleConfig } from '@/config/chuongTrinh/chuongTrinhModuleConfig.js';
</script>
