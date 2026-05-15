import { clearAuthStorage } from '@/utils/permission.js';

const AUTH_EXPIRES_KEY = 'authExpiresAt';
const DEFAULT_SESSION_MS = 8 * 60 * 60 * 1000;

const parseJwtPayload = (token = '') => {
  try {
    const [, payload] = String(token).split('.');
    if (!payload) return null;
    const normalized = payload.replace(/-/g, '+').replace(/_/g, '/');
    const json = decodeURIComponent(
      atob(normalized)
        .split('')
        .map((char) => `%${(`00${char.charCodeAt(0).toString(16)}`).slice(-2)}`)
        .join(''),
    );
    return JSON.parse(json);
  } catch {
    return null;
  }
};

export const getStoredToken = () => localStorage.getItem('accessToken') || localStorage.getItem('token') || '';

export const getTokenExpiresAt = (token = getStoredToken()) => {
  if (!token) return 0;
  if (token === 'demo-token') return Number(localStorage.getItem(AUTH_EXPIRES_KEY) || 0);

  const payload = parseJwtPayload(token);
  if (payload?.exp) return Number(payload.exp) * 1000;

  return Number(localStorage.getItem(AUTH_EXPIRES_KEY) || 0);
};

export const isSessionExpired = () => {
  const token = getStoredToken();
  if (!token) return true;

  const expiresAt = getTokenExpiresAt(token);
  if (!expiresAt) return false;

  return Date.now() >= expiresAt;
};

export const setFallbackSessionExpiry = (token = getStoredToken()) => {
  if (!token) return;
  const jwtExpiresAt = token !== 'demo-token' ? getTokenExpiresAt(token) : 0;
  localStorage.setItem(AUTH_EXPIRES_KEY, String(jwtExpiresAt || Date.now() + DEFAULT_SESSION_MS));
};

export const getSessionTimeLeftLabel = () => {
  const expiresAt = getTokenExpiresAt();
  if (!expiresAt) return 'Đang hoạt động';

  const diff = Math.max(0, expiresAt - Date.now());
  const minutes = Math.ceil(diff / 60000);
  if (minutes <= 0) return 'Hết phiên';
  if (minutes < 60) return `${minutes} phút`;
  return `${Math.floor(minutes / 60)} giờ ${minutes % 60} phút`;
};

export const logoutExpiredSession = () => {
  clearAuthStorage();
  localStorage.removeItem(AUTH_EXPIRES_KEY);
};

export const clearSession = () => {
  clearAuthStorage();
  localStorage.removeItem(AUTH_EXPIRES_KEY);
};

export const getCurrentUserInfo = () => {
  try {
    return JSON.parse(localStorage.getItem('user') || 'null') || {};
  } catch {
    return {};
  }
};

export const getDisplayName = () => {
  const user = getCurrentUserInfo();
  return user.hoTen || user.ho_ten || user.fullName || user.name || user.username || user.email || 'Người dùng';
};
