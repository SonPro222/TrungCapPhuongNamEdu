import { ref, watch } from 'vue';

export const useDebounce = (source, delay = 300) => {
  const debounced = ref(source?.value ?? source);
  let timer = null;

  watch(
    () => source?.value ?? source,
    (value) => {
      clearTimeout(timer);
      timer = setTimeout(() => {
        debounced.value = value;
      }, delay);
    },
    { immediate: true }
  );

  return debounced;
};

export const debounce = (fn, delay = 300) => {
  let timer = null;
  return (...args) => {
    clearTimeout(timer);
    timer = setTimeout(() => fn(...args), delay);
  };
};
