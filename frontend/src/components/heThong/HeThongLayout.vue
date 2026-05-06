<template>
  <ModuleLayoutShell :config="heThongModuleConfig" />
</template>

<script setup>
import ModuleLayoutShell from '@/components/layout/ModuleLayoutShell.vue';
import { heThongModuleConfig } from '@/config/heThong/heThongModuleConfig.js';
</script>
