const DEFAULT_HOME = '/he-thong';

const ROLE_ALIASES = {
    ADMIN: 'ADMIN',
    ROLE_ADMIN: 'ADMIN',
    SUPER_ADMIN: 'ADMIN',
    ROLE_QUAN_TRI: 'ADMIN',
    QUAN_TRI: 'ADMIN',
    GIAO_VU: 'ADMIN',
    DAO_TAO: 'ADMIN',
    GIANG_VIEN: 'GIANG_VIEN',
    ROLE_GIANG_VIEN: 'GIANG_VIEN',
    GIAO_VIEN: 'GIANG_VIEN',
    SINH_VIEN: 'SINH_VIEN',
    ROLE_SINH_VIEN: 'SINH_VIEN',
    STUDENT: 'SINH_VIEN',
    USER: 'USER',
};

const ROLE_HOME_MAP = {
    ADMIN: DEFAULT_HOME,
    GIANG_VIEN: '/giang-day',
    SINH_VIEN: '/sinh-vien',
    USER: DEFAULT_HOME,
};

const ROLE_PATH_MAP = {
    ADMIN: ['/'],
    GIANG_VIEN: ['/he-thong', '/quy-trinh', '/du-lieu', '/giang-day', '/lms'],
    SINH_VIEN: ['/he-thong', '/quy-trinh', '/du-lieu', '/sinh-vien', '/lms', '/hoc-phi', '/diem'],
    USER: ['/', '/login', '/he-thong', '/quy-trinh', '/du-lieu'],
};

export const normalizeRole = (role) => {
    if (!role) return 'USER';
    const value = String(role).trim().toUpperCase();
    return ROLE_ALIASES[value] || value || 'USER';
};

export const getCurrentUserRole = () => normalizeRole(
    localStorage.getItem('role') || localStorage.getItem('userRole') || 'USER',
);

export const getHomeByRole = (role = getCurrentUserRole()) => ROLE_HOME_MAP[normalizeRole(role)] || DEFAULT_HOME;

export const hasRole = (roles = [], role = getCurrentUserRole()) => {
    const normalizedRole = normalizeRole(role);
    if (!Array.isArray(roles)) return normalizedRole === normalizeRole(roles);
    return roles.map((item) => normalizeRole(item)).includes(normalizedRole);
};

export const canAccessPath = (path = '', role = getCurrentUserRole()) => {
    const normalizedRole = normalizeRole(role);
    const normalizedPath = path || '/';

    if (normalizedRole === 'ADMIN') return true;

    const allowedPaths = ROLE_PATH_MAP[normalizedRole] || ROLE_PATH_MAP.USER;
    return allowedPaths.some((allowedPath) => normalizedPath === allowedPath || normalizedPath.startsWith(`${allowedPath}/`));
};

export const filterMenuByRole = (menus = [], role = getCurrentUserRole()) => menus
    .filter((menu) => canAccessPath(menu.path || menu.to || '', role))
    .map((menu) => ({
        ...menu,
        children: Array.isArray(menu.children)
            ? menu.children.filter((child) => canAccessPath(child.path || child.to || '', role))
            : [],
    }));

export const clearAuthStorage = () => {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('token');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('role');
    localStorage.removeItem('userRole');
    localStorage.removeItem('user');
};

export default {
    normalizeRole,
    getCurrentUserRole,
    getHomeByRole,
    hasRole,
    canAccessPath,
    filterMenuByRole,
    clearAuthStorage,
};


