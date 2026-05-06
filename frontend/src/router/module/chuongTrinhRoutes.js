import ChuongTrinhLayout from '@/components/chuongTrinh/ChuongTrinhLayout.vue';
import ChuongTrinhCrudPage from '@/components/chuongTrinh/ChuongTrinhCrudPage.vue';

const chuongTrinhChildPages = [
    'chuong-trinh',
    'chuong-trinh-version',
    'syllabus-chuong-trinh',
    'muc-tieu-chuong-trinh',
    'nang-luc-dau-ra',
    'vi-tri-viec-lam',
    'dieu-kien-tot-nghiep',
    'mon-hoc',
    'nhom-kien-thuc',
    'chuong-trinh-mon',
    'mon-tien-quyet',
    'nhom-tu-chon',
    'mon-tu-chon',
    'syllabus-mon-hoc',
    'syllabus-chuong-bai',
    'syllabus-tai-lieu',
    'dieu-kien-mon-hoc',
    'quy-doi-diem',
];

const chuongTrinhChildren = [
    { path: '', redirect: '/chuong-trinh/chuong-trinh' },
    ...chuongTrinhChildPages.map((pageKey) => ({
        path: pageKey,
        name: `chuong-trinh-${pageKey}`,
        component: ChuongTrinhCrudPage,
        meta: { pageKey },
    })),
];

const chuongTrinhRoutes = [
    {
        path: '/chuong-trinh',
        component: ChuongTrinhLayout,
        children: chuongTrinhChildren,
    },
];

export default chuongTrinhRoutes;
