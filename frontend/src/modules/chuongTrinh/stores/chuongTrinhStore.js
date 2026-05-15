import { computed, reactive } from 'vue';
import { CHUONG_TRINH_RESOURCES, DEFAULT_CHUONG_TRINH_RESOURCE } from '@/modules/chuongTrinh/constants/trangThaiChuongTrinh.js';

const state = reactive({
  activeKey: DEFAULT_CHUONG_TRINH_RESOURCE.key,
  cache: {},
  filters: {},
});

export const useChuongTrinhStore = () => {
  const setActiveKey = (key) => {
    if (CHUONG_TRINH_RESOURCES.some((item) => item.key === key)) state.activeKey = key;
  };

  const setRows = (key, rows = []) => {
    state.cache[key] = rows;
  };

  const setFilters = (key, filters = {}) => {
    state.filters[key] = { ...filters };
  };

  const clearResource = (key) => {
    delete state.cache[key];
    delete state.filters[key];
  };

  return {
    state,
    activeResource: computed(() => CHUONG_TRINH_RESOURCES.find((item) => item.key === state.activeKey) || DEFAULT_CHUONG_TRINH_RESOURCE),
    setActiveKey,
    setRows,
    setFilters,
    clearResource,
  };
};

export default useChuongTrinhStore;
