import HeThongLayout from '@/components/heThong/HeThongLayout.vue';
import HeThongTaiKhoanQuanLyPage from '@/components/heThong/HeThongTaiKhoanQuanLyPage.vue';

const heThongChildren = [
    {
        path: '',
        redirect: '/he-thong/tai-khoan-sinh-vien',
    },
    {
        path: 'tai-khoan-sinh-vien',
        name: 'he-thong-tai-khoan-sinh-vien',
        component: HeThongTaiKhoanQuanLyPage,
        props: { view: 'sinhVien' },
    },
    {
        path: 'tai-khoan-giang-vien',
        name: 'he-thong-tai-khoan-giang-vien',
        component: HeThongTaiKhoanQuanLyPage,
        props: { view: 'giangVien' },
    },
    {
        path: 'nhan-vien',
        name: 'he-thong-nhan-vien',
        component: HeThongTaiKhoanQuanLyPage,
        props: { view: 'nhanVien' },
    },
    {
        path: 'dao-tao-admin',
        name: 'he-thong-dao-tao-admin',
        component: HeThongTaiKhoanQuanLyPage,
        props: { view: 'daoTaoAdmin' },
    },
    {
        path: 'vai-tro-quyen',
        name: 'he-thong-vai-tro-quyen',
        component: HeThongTaiKhoanQuanLyPage,
        props: { view: 'vaiTroQuyen' },
    },
    {
        path: 'lien-ket-phan-quyen',
        name: 'he-thong-lien-ket-phan-quyen',
        component: HeThongTaiKhoanQuanLyPage,
        props: { view: 'lienKet' },
    },
];

const heThongRoutes = [
    {
        path: '/he-thong',
        component: HeThongLayout,
        children: heThongChildren,
    },
];

export default heThongRoutes;
