export const formatDate = (value, options = {}) => {
  if (!value) return '—';
  const date = value instanceof Date ? value : new Date(value);
  if (Number.isNaN(date.getTime())) return '—';
  return new Intl.DateTimeFormat('vi-VN', options).format(date);
};

export const formatDateTime = (value) => formatDate(value, { hour: '2-digit', minute: '2-digit' });
