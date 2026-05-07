// src/utils/permission.js
export const normalizeRole = (role) => {
  const value = String(role || '')
    .trim()
    .toLowerCase()
    .replace('role_', '')
    .replaceAll('-', '_');

  if (value.includes('admin')) return 'admin';
  if (value.includes('dao_tao') || value.includes('daotao') || value.includes('phong_dao_tao')) return 'dao_tao';
  if (value.includes('nhan_vien') || value.includes('nhanvien')) return 'nhan_vien';
  if (value.includes('giao_vien') || value.includes('giaovien')) return 'giao_vien';
  if (value.includes('sinh_vien') || value.includes('sinhvien')) return 'sinh_vien';

  return '';
};

export const getHomeByRole = (role) => {
  const value = normalizeRole(role);

  if (value === 'admin') return '/he-thong';
  if (value === 'dao_tao') return '/dao-tao';
  if (value === 'nhan_vien') return '/chuong-trinh';
  if (value === 'giao_vien') return '/giang-day';
  if (value === 'sinh_vien') return '/sinh-vien';

  return '/';
};

export const rolePermissions = {
  admin: ['*'],
  dao_tao: ['/dao-tao', '/chuong-trinh', '/sinh-vien', '/giang-day', '/diem', '/thi', '/lms'],
  nhan_vien: ['/dao-tao', '/chuong-trinh', '/sinh-vien', '/hoc-phi', '/diem'],
  giao_vien: ['/giang-day', '/diem', '/thi', '/lms'],
  sinh_vien: ['/sinh-vien', '/diem', '/thi', '/hoc-phi', '/lms'],
};

export const parsePermissions = (raw) => {
  if (!raw) return [];

  try {
    const parsed = typeof raw === 'string' ? JSON.parse(raw) : raw;
    if (Array.isArray(parsed)) return parsed.map(String).filter(Boolean);
    if (Array.isArray(parsed?.paths)) return parsed.paths.map(String).filter(Boolean);
    if (Array.isArray(parsed?.urls)) return parsed.urls.map(String).filter(Boolean);
    if (Array.isArray(parsed?.permissions)) return parsed.permissions.map(String).filter(Boolean);
  } catch (error) {
    return String(raw)
      .split(',')
      .map((item) => item.trim())
      .filter(Boolean);
  }

  return [];
};

export const getPermissionsByRole = (role) => {
  const value = normalizeRole(role);
  const dbPermissions = parsePermissions(localStorage.getItem('permissions'));

  if (value === 'admin') return ['*'];
  if (dbPermissions.length) return dbPermissions;

  return rolePermissions[value] || [];
};

export const canAccessPath = (path, role) => {
  const value = normalizeRole(role);
  const permissions = getPermissionsByRole(value);

  if (value === 'admin' || permissions.includes('*')) return true;

  return permissions.some((prefix) => path === prefix || path.startsWith(`${prefix}/`));
};

export const clearAuthStorage = () => {
  localStorage.removeItem('accessToken');
  localStorage.removeItem('token');
  localStorage.removeItem('user');
  localStorage.removeItem('role');
  localStorage.removeItem('permissions');
};
