export const chuongTrinhModuleConfig = {
    title: 'Chương trình đào tạo',
    description: 'Điều hướng theo nhóm dữ liệu: chương trình gốc, phiên bản ban hành, syllabus và cấu trúc môn học trong chương trình.',
    basePath: 'chuong-trinh',
    navGroups: [
        {
            title: 'Tổng quan',
            items: [
                { label: 'Chương trình đào tạo', to: '/chuong-trinh/chuong-trinh', path: 'chuong-trinh/chuong-trinh' },
                { label: 'Version chương trình', to: '/chuong-trinh/chuong-trinh-version', path: 'chuong-trinh/chuong-trinh-version' },
            ],
        },
        {
            title: 'Nội dung chương trình',
            items: [
                { label: 'Syllabus chương trình', to: '/chuong-trinh/syllabus-chuong-trinh', path: 'chuong-trinh/syllabus-chuong-trinh' },
                { label: 'Mục tiêu chương trình', to: '/chuong-trinh/muc-tieu-chuong-trinh', path: 'chuong-trinh/muc-tieu-chuong-trinh' },
                { label: 'Năng lực đầu ra', to: '/chuong-trinh/nang-luc-dau-ra', path: 'chuong-trinh/nang-luc-dau-ra' },
                { label: 'Vị trí việc làm', to: '/chuong-trinh/vi-tri-viec-lam', path: 'chuong-trinh/vi-tri-viec-lam' },
                { label: 'Điều kiện tốt nghiệp', to: '/chuong-trinh/dieu-kien-tot-nghiep', path: 'chuong-trinh/dieu-kien-tot-nghiep' },
            ],
        },
        {
            title: 'Cấu trúc học phần',
            items: [
                { label: 'Môn học', to: '/chuong-trinh/mon-hoc', path: 'chuong-trinh/mon-hoc' },
                { label: 'Nhóm kiến thức', to: '/chuong-trinh/nhom-kien-thuc', path: 'chuong-trinh/nhom-kien-thuc' },
                { label: 'Môn trong chương trình', to: '/chuong-trinh/chuong-trinh-mon', path: 'chuong-trinh/chuong-trinh-mon' },
                { label: 'Môn tiên quyết', to: '/chuong-trinh/mon-tien-quyet', path: 'chuong-trinh/mon-tien-quyet' },
                { label: 'Nhóm tự chọn', to: '/chuong-trinh/nhom-tu-chon', path: 'chuong-trinh/nhom-tu-chon' },
                { label: 'Môn tự chọn', to: '/chuong-trinh/mon-tu-chon', path: 'chuong-trinh/mon-tu-chon' },
            ],
        },
        {
            title: 'Đánh giá và học liệu',
            items: [
                { label: 'Syllabus môn học', to: '/chuong-trinh/syllabus-mon-hoc', path: 'chuong-trinh/syllabus-mon-hoc' },
                { label: 'Syllabus chương bài', to: '/chuong-trinh/syllabus-chuong-bai', path: 'chuong-trinh/syllabus-chuong-bai' },
                { label: 'Tài liệu syllabus', to: '/chuong-trinh/syllabus-tai-lieu', path: 'chuong-trinh/syllabus-tai-lieu' },
                { label: 'Điều kiện môn học', to: '/chuong-trinh/dieu-kien-mon-hoc', path: 'chuong-trinh/dieu-kien-mon-hoc' },
                { label: 'Quy đổi điểm', to: '/chuong-trinh/quy-doi-diem', path: 'chuong-trinh/quy-doi-diem' },
            ],
        },
    ],
};
