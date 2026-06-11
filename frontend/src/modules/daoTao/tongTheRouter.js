import PublicLayout from '@/layouts/PublicLayout.vue'

import XemTongTheChuongTrinhPage from './pages/TongThe/XemTongTheChuongTrinhPage.vue'
import XemKyChuongTrinhPage from './pages/TongThe/XemKyChuongTrinhPage.vue'
import XemSyllabusMonHocPage from './pages/TongThe/XemSyllabusMonHocPage.vue'
import XemSyllabusChuongTrinhPage from './pages/TongThe/XemSyllabusChuongTrinhPage.vue'


export const tongTheRoutes = [
    {
        path: '/chuong-trinh',
        component: PublicLayout,
        meta: {
            module: 'xemTongTheChuongTrinh',
            title: 'Xem chương trình đào tạo',
            public: true
        },
        children: [
            {
                path: '',
                redirect: '/chuong-trinh/tong-quan'
            },
            {
                path: 'tong-quan',
                name: 'dao-tao-chuong-trinh-tong-quan',
                component: XemTongTheChuongTrinhPage,
                meta: {
                    title: 'Tổng quan chương trình đào tạo',
                    public: true
                }
            },
            {
                path: ':chuongTrinhId/ky',
                name: 'dao-tao-chuong-trinh-ky',
                component: XemKyChuongTrinhPage,
                meta: {
                    title: 'Xem kỳ chương trình',
                    public: true
                }
            },
            {
                path: ':chuongTrinhId/syllabus-chuong-trinh',
                name: 'dao-tao-syllabus-chuong-trinh-xem',
                component: XemSyllabusChuongTrinhPage,
                meta: {
                    title: 'Xem syllabus chương trình',
                    public: true
                }
            }
        ]
    },
    {
        path: '/syllabus-mon-hoc',
        component: PublicLayout,
        meta: {
            module: 'xemTongTheChuongTrinh',
            title: 'Xem syllabus môn học',
            public: true
        },
        children: [
            {
                path: 'xem',
                name: 'dao-tao-syllabus-mon-hoc-xem',
                component: XemSyllabusMonHocPage,
                meta: {
                    title: 'Xem syllabus môn học',
                    public: true
                }
            }
        ]
    }
]