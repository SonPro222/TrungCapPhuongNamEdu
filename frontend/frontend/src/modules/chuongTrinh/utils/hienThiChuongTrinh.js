export const giaTri = (record = {}, keys = [], fallback = '—') => {
  for (const key of keys) {
    const value = record?.[key];
    if (value !== undefined && value !== null && value !== '') return value;
  }
  return fallback;
};

export const idCua = (record = {}) => record?.id ?? record?.ID ?? record?._id;

export const dinhDangNgay = (value) => {
  if (!value) return '—';
  const date = new Date(value);
  if (Number.isNaN(date.getTime())) return value;
  return new Intl.DateTimeFormat('vi-VN').format(date);
};

export const dinhDangSo = (value, suffix = '') => {
  if (value === null || value === undefined || value === '') return '—';
  const number = Number(value);
  if (Number.isNaN(number)) return `${value}${suffix}`;
  return `${new Intl.NumberFormat('vi-VN', { maximumFractionDigits: 1 }).format(number)}${suffix}`;
};

export const nhanChuongTrinh = (item = {}) => {
  const ma = giaTri(item, ['maChuongTrinh', 'ma'], 'CT');
  const ten = giaTri(item, ['tenChuongTrinh', 'ten'], 'Chương trình đào tạo');
  return `${ma} - ${ten}`;
};

export const nhanPhienBan = (item = {}) => {
  const ma = giaTri(item, ['maVersion', 'ma'], 'Version');
  const ten = giaTri(item, ['tenVersion', 'ten'], 'Phiên bản chương trình');
  return `${ma} - ${ten}`;
};

export const nhanMonHoc = (item = {}, danhMucMon = []) => {
  const mon = danhMucMon.find((row) => String(idCua(row)) === String(item.monHocId)) || item;
  const ma = giaTri(item, ['maMonTrongCt'], giaTri(mon, ['maMon'], 'MH'));
  const ten = giaTri(mon, ['tenMon', 'ten'], giaTri(item, ['tenMon'], 'Môn học'));
  return `${ma} - ${ten}`;
};

export const locTheo = (rows = [], key, value) => {
  if (value === undefined || value === null || value === '') return rows;
  return rows.filter((item) => String(item?.[key]) === String(value));
};

export const timTheoId = (rows = [], id) => rows.find((item) => String(idCua(item)) === String(id));

export const tong = (rows = [], key) => rows.reduce((sum, item) => sum + (Number(item?.[key]) || 0), 0);

export const timKiem = (rows = [], keyword = '', fields = []) => {
  const text = keyword.trim().toLowerCase();
  if (!text) return rows;
  return rows.filter((item) => fields.some((field) => String(item?.[field] ?? '').toLowerCase().includes(text)));
};
