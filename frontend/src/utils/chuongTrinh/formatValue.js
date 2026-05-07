export function findById(list = [], id) {
  return list.find((item) => Number(item?.id) === Number(id));
}

export function rowLabel(row = {}, fallback = 'Bản ghi') {
  return row.tenChuongTrinh || row.tenMon || row.tenNganh || row.tenTrinhDo || row.tenLoai || row.tenKy || row.ten || row.maChuongTrinh || row.maMonTrongCt || row.maMon || row.maVersion || row.ma || `${fallback} #${row.id ?? ''}`;
}

export function formatValue(row = {}, column = {}, dataStore = {}) {
  const value = row?.[column.key];
  if (value === null || value === undefined || value === '') return '—';
  if (column.format === 'boolean') return value ? 'Có' : 'Không';
  if (column.format === 'number') return Number(value).toLocaleString('vi-VN');
  if (column.format === 'date') return new Date(value).toLocaleDateString('vi-VN');

  const maps = {
    nganh: ['nganhList', ['maNganh', 'tenNganh']],
    trinhDo: ['trinhDoList', ['maTrinhDo', 'tenTrinhDo']],
    loaiChuongTrinh: ['loaiChuongTrinhList', ['maLoai', 'tenLoai']],
    program: ['programs', ['maChuongTrinh', 'tenChuongTrinh']],
    version: ['versions', ['maVersion', 'tenVersion']],
    subject: ['subjects', ['maMon', 'tenMon']],
    khungKy: ['khungKyList', ['maKy', 'tenKy']],
    group: ['groups', ['ma', 'ten']],
    course: ['courses', ['maMonTrongCt', 'ghiChu']],
    syllabusMon: ['syllabusSubjects', ['viTri', 'tinhChat']],
  };

  const map = maps[column.format];
  if (!map) return String(value);
  const [storeKey, keys] = map;
  const target = findById(dataStore[storeKey] || [], value);
  if (!target) return `#${value}`;
  return keys.map((key) => target[key]).filter(Boolean).join(' - ') || `#${value}`;
}
