// src/utils/permission.js
export const normalizeRole = (role) => {
    const value = String(role || '')
        .trim()
        .toLowerCase()
        .replace('role_', '')
        .replace('-', '_');

    if (value.includes('admin')) return 'admin';
    if (value.includes('dao_tao') || value.includes('phong_dao_tao')) return 'dao_tao';
    if (value.includes('nhan_vien')) return 'nhan_vien';
    if (value.includes('giao_vien')) return 'giao_vien';
    if (value.includes('sinh_vien')) return 'sinh_vien';

    return '';
};

export const getHomeByRole = (role) => {
    const value = normalizeRole(role);

    if (value === 'admin') return '/he-thong';
    if (value === 'dao_tao') return '/dao-tao';
    if (value === 'nhan_vien') return '/dao-tao';
    if (value === 'giao_vien') return '/giang-day';
    if (value === 'sinh_vien') return '/sinh-vien';

    return '/';
};

export const rolePermissions = {
    admin: [
        '/he-thong',
        '/dao-tao',
        '/chuong-trinh',
        '/giang-day',
        '/diem',
        '/sinh-vien',
        '/thi',
        '/hoc-phi',
        '/lms',
    ],
    dao_tao: [
        '/dao-tao',
        '/chuong-trinh',
        '/sinh-vien',
        '/giang-day',
        '/diem',
        '/thi',
        '/lms',
    ],
    nhan_vien: [
        '/dao-tao',
        '/chuong-trinh',
        '/sinh-vien',
        '/hoc-phi',
        '/diem',
    ],
    giao_vien: [
        '/giang-day',
        '/diem',
        '/thi',
        '/lms',
    ],
    sinh_vien: [
        '/sinh-vien',
        '/diem',
        '/thi',
        '/hoc-phi',
        '/lms',
    ],
};

export const canAccessPath = (path, role) => {
    const value = normalizeRole(role);
    const permissions = rolePermissions[value];

    if (!permissions) return false;

    return permissions.some((prefix) => path.startsWith(prefix));
};

export const clearAuthStorage = () => {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    localStorage.removeItem('role');
    localStorage.removeItem('permissions');
};