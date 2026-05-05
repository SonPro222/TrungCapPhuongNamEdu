import SinhVienLayout from '@/components/sinhVien/SinhVienLayout.vue';
import SinhVienPage from '@/components/sinhVien/SinhVienPage.vue';
import SinhVienChuongTrinhPage from '@/components/sinhVien/SinhVienChuongTrinhPage.vue';
import MienTruBaoLuuPage from '@/components/sinhVien/MienTruBaoLuuPage.vue';

const sinhVienRoutes = [
    {
        path: '/sinh-vien',
        component: SinhVienLayout,
        redirect: '/sinh-vien/sinh-vien',
        children: [
        { path: 'sinh-vien', name: 'sinhVien-sinh-vien', component: SinhVienPage },
        { path: 'sinh-vien-chuong-trinh', name: 'sinhVien-sinh-vien-chuong-trinh', component: SinhVienChuongTrinhPage },
        { path: 'mien-tru-bao-luu', name: 'sinhVien-mien-tru-bao-luu', component: MienTruBaoLuuPage },
        ],
    },
];

export default sinhVienRoutes;
