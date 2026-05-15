import { USER_ROLES, ROLE_LABELS } from '@/shared/constants/roles.js';

export { USER_ROLES, ROLE_LABELS };

export const normalizeUserRole = (role = '') => String(role).trim().toUpperCase();
