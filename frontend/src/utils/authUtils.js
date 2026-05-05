// src/utils/authUtil.js
export const normalizeRole = (role) => {
    const value = String(role || '')
        .trim()
        .toLowerCase()
        .replace('role_', '')
        .replace('-', '_');

    if (value.includes('admin')) return 'admin';
    if (value.includes('sinh_vien') || value.includes('sinhvien')) return 'sinh_vien';
    if (value.includes('giao_vien') || value.includes('giaovien')) return 'giao_vien';
    if (value.includes('nhan_vien') || value.includes('nhanvien')) return 'nhan_vien';
    if (value.includes('dao_tao') || value.includes('daotao') || value.includes('phong_dao_tao')) return 'nhan_vien';

    return '';
};

export const extractLoginPayload = (res) => {
    return res?.data || res?.result || res;
};

export const extractToken = (res) => {
    const payload = extractLoginPayload(res);

    return (
        payload?.accessToken ||
        payload?.token ||
        payload?.jwt ||
        payload?.access_token ||
        res?.accessToken ||
        res?.token ||
        ''
    );
};

export const extractRole = (res) => {
    const payload = extractLoginPayload(res);
    const user = payload?.user || payload?.taiKhoan || payload?.account || payload;

    const role =
        payload?.role ||
        payload?.vaiTro ||
        payload?.maVaiTro ||
        payload?.loaiTaiKhoan ||
        payload?.loai_tai_khoan ||
        user?.role ||
        user?.vaiTro ||
        user?.maVaiTro ||
        user?.loaiTaiKhoan ||
        user?.loai_tai_khoan ||
        '';

    return normalizeRole(role);
};

export const extractStatus = (res) => {
    const payload = extractLoginPayload(res);
    const user = payload?.user || payload?.taiKhoan || payload?.account || payload;

    return (
        payload?.trangThai ||
        payload?.trang_thai ||
        user?.trangThai ||
        user?.trang_thai ||
        ''
    );
};

export const getHomeByRole = (role) => {
    const value = normalizeRole(role);

    if (value === 'admin') return '/he-thong';
    if (value === 'nhan_vien') return '/chuong-trinh';
    if (value === 'giao_vien') return '/giang-day';
    if (value === 'sinh_vien') return '/sinh-vien';

    return '/login';
};

export const clearAuthStorage = () => {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    localStorage.removeItem('role');
};