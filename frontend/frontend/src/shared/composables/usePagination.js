import { computed, ref } from 'vue';

export const usePagination = (itemsRef, pageSize = 10) => {
  const page = ref(1);
  const size = ref(pageSize);

  const total = computed(() => itemsRef.value?.length || 0);
  const totalPages = computed(() => Math.max(1, Math.ceil(total.value / size.value)));
  const pagedItems = computed(() => {
    const start = (page.value - 1) * size.value;
    return (itemsRef.value || []).slice(start, start + size.value);
  });

  const setPage = (nextPage) => {
    page.value = Math.min(Math.max(1, Number(nextPage) || 1), totalPages.value);
  };

  const resetPage = () => setPage(1);

  return { page, size, total, totalPages, pagedItems, setPage, resetPage };
};
