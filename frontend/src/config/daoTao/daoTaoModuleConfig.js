export const daoTaoModuleConfig = {
    title: 'Đào tạo',
    description: 'Quản lý cấu trúc đào tạo từ ngành, trình độ, loại chương trình đến khung kỳ, khóa đào tạo và lớp hành chính.',
    basePath: 'dao-tao',
    navGroups: [
        {
            title: 'Danh mục đào tạo',
            items: [
                { label: 'Ngành đào tạo', to: '/dao-tao/nganh', path: 'dao-tao/nganh' },
                { label: 'Trình độ đào tạo', to: '/dao-tao/trinh-do-dao-tao', path: 'dao-tao/trinh-do-dao-tao' },
                { label: 'Loại chương trình', to: '/dao-tao/loai-chuong-trinh', path: 'dao-tao/loai-chuong-trinh' },
            ],
        },
        {
            title: 'Tổ chức học tập',
            items: [
                { label: 'Khung kỳ', to: '/dao-tao/khung-ky', path: 'dao-tao/khung-ky' },
                { label: 'Khóa đào tạo', to: '/dao-tao/khoa-dao-tao', path: 'dao-tao/khoa-dao-tao' },
                { label: 'Lớp hành chính', to: '/dao-tao/lop-hanh-chinh', path: 'dao-tao/lop-hanh-chinh' },
            ],
        },
    ],
};
