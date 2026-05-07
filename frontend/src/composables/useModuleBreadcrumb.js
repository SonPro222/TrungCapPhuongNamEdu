import { computed } from 'vue';
import { useRoute } from 'vue-router';
import { CHUONG_TRINH_TABLE_CONFIG } from '../config/chuongTrinhTable.config';
import { buildBreadcrumbPath, parseModuleRoute } from '../utils/moduleRoute.utils';

export const useModuleBreadcrumb = () => {
  const route = useRoute();

  const parsedRoute = computed(() => parseModuleRoute(route));

  const breadcrumbItems = computed(() => parsedRoute.value.trail.map((item, index) => ({
    resourceKey: item.resourceKey,
    title: CHUONG_TRINH_TABLE_CONFIG[item.resourceKey]?.title || item.resourceKey,
    id: item.id,
    to: buildBreadcrumbPath(parsedRoute.value.trail, index),
  })));

  return { parsedRoute, breadcrumbItems };
};
