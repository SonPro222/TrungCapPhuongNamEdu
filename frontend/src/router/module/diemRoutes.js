import DiemLayout from '@/components/diem/DiemLayout.vue';
import BaiKiemTraPage from '@/components/diem/BaiKiemTraPage.vue';
import BaiNopSinhVienPage from '@/components/diem/BaiNopSinhVienPage.vue';
import CauHinhDanhGiaPage from '@/components/diem/CauHinhDanhGiaPage.vue';
import DiemChiTietPage from '@/components/diem/DiemChiTietPage.vue';
import KetQuaLopHocPhanPage from '@/components/diem/KetQuaLopHocPhanPage.vue';
import KetQuaMonHocPage from '@/components/diem/KetQuaMonHocPage.vue';

const diemRoutes = [
    {
        path: '/diem',
        component: DiemLayout,
        redirect: '/diem/bai-kiem-tra',
        children: [
        { path: 'bai-kiem-tra', name: 'diem-bai-kiem-tra', component: BaiKiemTraPage },
        { path: 'bai-nop-sinh-vien', name: 'diem-bai-nop-sinh-vien', component: BaiNopSinhVienPage },
        { path: 'cau-hinh-danh-gia', name: 'diem-cau-hinh-danh-gia', component: CauHinhDanhGiaPage },
        { path: 'diem-chi-tiet', name: 'diem-diem-chi-tiet', component: DiemChiTietPage },
        { path: 'ket-qua-lop-hoc-phan', name: 'diem-ket-qua-lop-hoc-phan', component: KetQuaLopHocPhanPage },
        { path: 'ket-qua-mon-hoc', name: 'diem-ket-qua-mon-hoc', component: KetQuaMonHocPage },
        ],
    },
];

export default diemRoutes;
