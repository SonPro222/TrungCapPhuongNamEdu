import { ENV } from '@/core/config/env.js';
import storage from './storageService.js';

const TOKEN_KEYS = [ENV.tokenKey, 'token', 'access_token'];
const REFRESH_KEYS = [ENV.refreshTokenKey, 'refresh_token'];
const ROLE_KEYS = [ENV.roleKey, 'userRole', 'authorities'];

const firstValue = (keys) => keys.map((key) => storage.get(key)).find(Boolean) || '';

export const tokenService = {
  getAccessToken: () => firstValue(TOKEN_KEYS),
  setAccessToken(token) {
    storage.set(ENV.tokenKey, token);
    storage.set('token', token);
  },
  getRefreshToken: () => firstValue(REFRESH_KEYS),
  setRefreshToken(token) {
    storage.set(ENV.refreshTokenKey, token);
  },
  getRole: () => firstValue(ROLE_KEYS),
  setRole(role) {
    storage.set(ENV.roleKey, role);
  },
  clear() {
    storage.clear([...TOKEN_KEYS, ...REFRESH_KEYS, ...ROLE_KEYS, 'user', 'userInfo']);
  },
};

export default tokenService;
