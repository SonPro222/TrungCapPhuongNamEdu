import { canAccessPath, getHomeByRole, normalizeRole } from '@/utils/permission.js';
import tokenService from '@/core/services/tokenService.js';

export const adminGuard = (to, from, next) => {
  const role = normalizeRole(tokenService.getRole());
  if (role && canAccessPath(to.path, role)) return next();
  return next(getHomeByRole(role));
};
