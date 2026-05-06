<template>
  <ModuleLayoutShell :config="daoTaoModuleConfig" />
</template>

<script setup>
import ModuleLayoutShell from '@/components/layout/ModuleLayoutShell.vue';
import { daoTaoModuleConfig } from '@/config/daoTao/daoTaoModuleConfig.js';
</script>
