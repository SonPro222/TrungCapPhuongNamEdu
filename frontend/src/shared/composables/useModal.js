import { ref } from 'vue';

export const useModal = (initial = false) => {
  const visible = ref(initial);
  const payload = ref(null);

  const open = (data = null) => {
    payload.value = data;
    visible.value = true;
  };

  const close = () => {
    visible.value = false;
    payload.value = null;
  };

  const toggle = () => {
    visible.value = !visible.value;
  };

  return { visible, payload, open, close, toggle };
};
