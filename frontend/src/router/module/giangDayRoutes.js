import GiangDayLayout from '@/components/giangDay/GiangDayLayout.vue';
import CaHocPage from '@/components/giangDay/CaHocPage.vue';
import PhongHocPage from '@/components/giangDay/PhongHocPage.vue';
import GiaoVienPage from '@/components/giangDay/GiaoVienPage.vue';
import LopHocPhanPage from '@/components/giangDay/LopHocPhanPage.vue';
import PhanCongGiangDayPage from '@/components/giangDay/PhanCongGiangDayPage.vue';
import SinhVienLopHocPhanPage from '@/components/giangDay/SinhVienLopHocPhanPage.vue';
import LichHocPage from '@/components/giangDay/LichHocPage.vue';
import YeuCauDoiLichPage from '@/components/giangDay/YeuCauDoiLichPage.vue';
import DiemDanhPage from '@/components/giangDay/DiemDanhPage.vue';

const giangDayRoutes = [
    {
        path: '/giang-day',
        component: GiangDayLayout,
        redirect: '/giang-day/ca-hoc',
        children: [
        { path: 'ca-hoc', name: 'giangDay-ca-hoc', component: CaHocPage },
        { path: 'phong-hoc', name: 'giangDay-phong-hoc', component: PhongHocPage },
        { path: 'giao-vien', name: 'giangDay-giao-vien', component: GiaoVienPage },
        { path: 'lop-hoc-phan', name: 'giangDay-lop-hoc-phan', component: LopHocPhanPage },
        { path: 'phan-cong-giang-day', name: 'giangDay-phan-cong-giang-day', component: PhanCongGiangDayPage },
        { path: 'sinh-vien-lop-hoc-phan', name: 'giangDay-sinh-vien-lop-hoc-phan', component: SinhVienLopHocPhanPage },
        { path: 'lich-hoc', name: 'giangDay-lich-hoc', component: LichHocPage },
        { path: 'yeu-cau-doi-lich', name: 'giangDay-yeu-cau-doi-lich', component: YeuCauDoiLichPage },
        { path: 'diem-danh', name: 'giangDay-diem-danh', component: DiemDanhPage },
        ],
    },
];

export default giangDayRoutes;
