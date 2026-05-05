import HeThongLayout from '@/components/heThong/HeThongLayout.vue';
import HeThongTaiKhoanQuanLyPage from '@/components/heThong/HeThongTaiKhoanQuanLyPage.vue';

const heThongRoutes = [
    {
        path: '/he-thong',
        component: HeThongLayout,
        redirect: '/he-thong/quan-ly-tai-khoan',
        children: [
            {
                path: 'quan-ly-tai-khoan',
                name: 'he-thong-quan-ly-tai-khoan',
                component: HeThongTaiKhoanQuanLyPage,
            },
        ],
    },
];

export default heThongRoutes;