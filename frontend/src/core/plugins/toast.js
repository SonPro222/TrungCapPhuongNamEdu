import { reactive } from 'vue';

export const toastState = reactive({
  items: [],
});

let toastId = 0;

export const toast = {
  show(message, type = 'info', timeout = 2800) {
    const item = { id: ++toastId, message, type };
    toastState.items.push(item);
    if (timeout) setTimeout(() => toast.remove(item.id), timeout);
    return item.id;
  },
  success(message) {
    return this.show(message, 'success');
  },
  error(message) {
    return this.show(message, 'error', 4000);
  },
  warning(message) {
    return this.show(message, 'warning');
  },
  remove(id) {
    const index = toastState.items.findIndex((item) => item.id === id);
    if (index >= 0) toastState.items.splice(index, 1);
  },
};

export default {
  install(app) {
    app.config.globalProperties.$toast = toast;
    app.provide('toast', toast);
  },
};
