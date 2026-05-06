export const heThongModuleConfig = {
    title: 'Hệ thống',
    description: 'Quản lý tài khoản, vai trò, phân quyền và vận hành người dùng trong toàn bộ hệ thống.',
    basePath: 'he-thong',
    navGroups: [
        {
            title: 'Tài khoản',
            items: [
                { label: 'Tài khoản sinh viên', to: '/he-thong/tai-khoan-sinh-vien', path: 'he-thong/tai-khoan-sinh-vien' },
                { label: 'Tài khoản giảng viên', to: '/he-thong/tai-khoan-giang-vien', path: 'he-thong/tai-khoan-giang-vien' },
                { label: 'Nhân viên', to: '/he-thong/nhan-vien', path: 'he-thong/nhan-vien' },
                { label: 'Đào tạo / Admin', to: '/he-thong/dao-tao-admin', path: 'he-thong/dao-tao-admin' },
            ],
        },
        {
            title: 'Phân quyền',
            items: [
                { label: 'Vai trò / Quyền', to: '/he-thong/vai-tro-quyen', path: 'he-thong/vai-tro-quyen' },
                { label: 'Liên kết phân quyền', to: '/he-thong/lien-ket-phan-quyen', path: 'he-thong/lien-ket-phan-quyen' },
            ],
        },
    ],
};
