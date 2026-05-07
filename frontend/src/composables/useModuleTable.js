import { computed, ref } from 'vue';
import { CHUONG_TRINH_TABLE_CONFIG } from '../config/chuongTrinhTable.config';
import { CHUONG_TRINH_API } from '../services/chuongTrinh.service';
import { extractArray, filterRowsByContext } from '../utils/moduleTable.utils';

export const useModuleTable = (resourceKeyRef, contextRef) => {
  const loading = ref(false);
  const rows = ref([]);
  const error = ref(null);
  const searchText = ref('');
  const page = ref(1);
  const pageSize = ref(10);

  const config = computed(() => CHUONG_TRINH_TABLE_CONFIG[resourceKeyRef.value]);

  const loadData = async () => {
    const api = CHUONG_TRINH_API[config.value?.apiKey];
    if (!api?.list) return;
    loading.value = true;
    error.value = null;
    try {
      const response = await api.list({ ...contextRef.value });
      rows.value = filterRowsByContext(resourceKeyRef.value, extractArray(response), contextRef.value);
    } catch (err) {
      error.value = err;
      rows.value = [];
    } finally {
      loading.value = false;
    }
  };

  const filteredRows = computed(() => {
    const keyword = searchText.value.trim().toLowerCase();
    if (!keyword) return rows.value;
    const fields = config.value?.searchFields || config.value?.columns?.map((item) => item.key) || [];
    return rows.value.filter((row) => fields.some((key) => String(row[key] ?? '').toLowerCase().includes(keyword)));
  });

  const pagedRows = computed(() => {
    const start = (page.value - 1) * pageSize.value;
    return filteredRows.value.slice(start, start + pageSize.value);
  });

  return { loading, rows, error, searchText, page, pageSize, config, loadData, filteredRows, pagedRows };
};
