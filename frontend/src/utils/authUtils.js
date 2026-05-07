// src/utils/authUtils.js
import { clearAuthStorage, getHomeByRole, normalizeRole } from '@/utils/permission.js';

export { clearAuthStorage, getHomeByRole, normalizeRole };

export const extractLoginPayload = (res) => res?.data || res?.result || res;

export const extractToken = (res) => {
  const payload = extractLoginPayload(res);

  return payload?.accessToken || payload?.token || payload?.jwt || payload?.access_token || res?.accessToken || res?.token || '';
};

export const extractRole = (res) => {
  const payload = extractLoginPayload(res);
  const user = payload?.user || payload?.taiKhoan || payload?.account || payload;

  const role = payload?.role || payload?.vaiTro || payload?.maVaiTro || payload?.loaiTaiKhoan || payload?.loai_tai_khoan || user?.role || user?.vaiTro || user?.maVaiTro || user?.loaiTaiKhoan || user?.loai_tai_khoan || '';

  return normalizeRole(role);
};

export const extractStatus = (res) => {
  const payload = extractLoginPayload(res);
  const user = payload?.user || payload?.taiKhoan || payload?.account || payload;

  return payload?.trangThai || payload?.trang_thai || user?.trangThai || user?.trang_thai || '';
};

export const extractPermissions = (res) => {
  const payload = extractLoginPayload(res);
  const user = payload?.user || payload?.taiKhoan || payload?.account || payload;

  return payload?.permissions || payload?.quyen || payload?.dsQuyen || payload?.urls || user?.permissions || user?.quyen || user?.dsQuyen || user?.urls || [];
};

export const saveAuthSession = ({ token, role, user, permissions }) => {
  localStorage.setItem('accessToken', token);
  localStorage.setItem('token', token);
  localStorage.setItem('role', normalizeRole(role));
  localStorage.setItem('user', JSON.stringify(user || {}));
  localStorage.setItem('permissions', JSON.stringify(permissions || []));
};
