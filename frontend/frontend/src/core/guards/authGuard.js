import tokenService from '@/core/services/tokenService.js';
import { clearAuthStorage, getHomeByRole, normalizeRole } from '@/utils/permission.js';

export const authGuard = (to, from, next) => {
  if (to.meta?.public) return next();

  const token = tokenService.getAccessToken();
  const role = normalizeRole(tokenService.getRole());

  if (!token || !role) {
    clearAuthStorage();
    return next('/login');
  }

  return next();
};

export const guestGuard = (to, from, next) => {
  const token = tokenService.getAccessToken();
  const role = normalizeRole(tokenService.getRole());
  if (token && role) return next(getHomeByRole(role));
  return next();
};
