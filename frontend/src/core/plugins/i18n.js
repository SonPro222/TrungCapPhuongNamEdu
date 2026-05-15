const messages = {
  vi: {
    common: {
      add: 'Thêm mới',
      edit: 'Chỉnh sửa',
      delete: 'Xóa',
      save: 'Lưu',
      cancel: 'Hủy',
      search: 'Tìm kiếm',
    },
  },
};

export const t = (path, locale = 'vi') => {
  const value = path.split('.').reduce((obj, key) => obj?.[key], messages[locale]);
  return value || path;
};

export default {
  install(app) {
    app.config.globalProperties.$t = t;
    app.provide('t', t);
  },
};
