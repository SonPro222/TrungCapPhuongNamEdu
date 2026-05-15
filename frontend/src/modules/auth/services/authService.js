import authApi from '@/modules/auth/api/authApi.js';
import tokenService from '@/core/services/tokenService.js';
import { normalizeRole } from '@/utils/permission.js';

const pick = (source, keys) => keys.map((key) => source?.[key]).find(Boolean);

export const normalizeAuthPayload = (payload = {}) => {
  const data = payload?.data ?? payload?.result ?? payload;
  const accessToken = pick(data, ['accessToken', 'token', 'jwt', 'access_token']);
  const refreshToken = pick(data, ['refreshToken', 'refresh_token']);
  const user = data?.user ?? data?.account ?? data?.taiKhoan ?? data;
  const roleValue = pick(user, ['role', 'vaiTro', 'roleName', 'authority']) || pick(data, ['role', 'vaiTro']);

  return {
    raw: data,
    user,
    accessToken,
    refreshToken,
    role: normalizeRole(Array.isArray(roleValue) ? roleValue[0] : roleValue),
  };
};

export const authService = {
  async login(credentials) {
    const payload = await authApi.login(credentials);
    const auth = normalizeAuthPayload(payload);
    if (auth.accessToken) tokenService.setAccessToken(auth.accessToken);
    if (auth.refreshToken) tokenService.setRefreshToken(auth.refreshToken);
    if (auth.role) tokenService.setRole(auth.role);
    if (auth.user) localStorage.setItem('user', JSON.stringify(auth.user));
    return auth;
  },
  async register(payload) {
    return authApi.register(payload);
  },
  async logout() {
    await authApi.logout();
    tokenService.clear();
  },
  getCurrentUser() {
    try {
      return JSON.parse(localStorage.getItem('user') || 'null');
    } catch {
      return null;
    }
  },
};

export default authService;
