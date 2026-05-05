import LmsLayout from '@/components/lms/LmsLayout.vue';
import NganHangCauHoiPage from '@/components/lms/NganHangCauHoiPage.vue';
import DapAnCauHoiPage from '@/components/lms/DapAnCauHoiPage.vue';
import BaiTapOnlinePage from '@/components/lms/BaiTapOnlinePage.vue';
import BaiTapOnlineCauHoiPage from '@/components/lms/BaiTapOnlineCauHoiPage.vue';
import DotGiaoBaiOnlinePage from '@/components/lms/DotGiaoBaiOnlinePage.vue';
import BaiTapLopPage from '@/components/lms/BaiTapLopPage.vue';
import LichSuBaiTapLopPage from '@/components/lms/LichSuBaiTapLopPage.vue';
import LanLamBaiOnlinePage from '@/components/lms/LanLamBaiOnlinePage.vue';
import CauTraLoiSinhVienPage from '@/components/lms/CauTraLoiSinhVienPage.vue';
import CauTraLoiChonDapAnPage from '@/components/lms/CauTraLoiChonDapAnPage.vue';
import KetQuaBaiTapLopPage from '@/components/lms/KetQuaBaiTapLopPage.vue';
import LichSuChinhDiemBaiTapPage from '@/components/lms/LichSuChinhDiemBaiTapPage.vue';
import LichSuDayDiemLmsPage from '@/components/lms/LichSuDayDiemLmsPage.vue';

const lmsRoutes = [
    {
        path: '/lms',
        component: LmsLayout,
        redirect: '/lms/ngan-hang-cau-hoi',
        children: [
        { path: 'ngan-hang-cau-hoi', name: 'lms-ngan-hang-cau-hoi', component: NganHangCauHoiPage },
        { path: 'dap-an-cau-hoi', name: 'lms-dap-an-cau-hoi', component: DapAnCauHoiPage },
        { path: 'bai-tap-online', name: 'lms-bai-tap-online', component: BaiTapOnlinePage },
        { path: 'bai-tap-online-cau-hoi', name: 'lms-bai-tap-online-cau-hoi', component: BaiTapOnlineCauHoiPage },
        { path: 'dot-giao-bai-online', name: 'lms-dot-giao-bai-online', component: DotGiaoBaiOnlinePage },
        { path: 'bai-tap-lop', name: 'lms-bai-tap-lop', component: BaiTapLopPage },
        { path: 'lich-su-bai-tap-lop', name: 'lms-lich-su-bai-tap-lop', component: LichSuBaiTapLopPage },
        { path: 'lan-lam-bai-online', name: 'lms-lan-lam-bai-online', component: LanLamBaiOnlinePage },
        { path: 'cau-tra-loi-sinh-vien', name: 'lms-cau-tra-loi-sinh-vien', component: CauTraLoiSinhVienPage },
        { path: 'cau-tra-loi-chon-dap-an', name: 'lms-cau-tra-loi-chon-dap-an', component: CauTraLoiChonDapAnPage },
        { path: 'ket-qua-bai-tap-lop', name: 'lms-ket-qua-bai-tap-lop', component: KetQuaBaiTapLopPage },
        { path: 'lich-su-chinh-diem-bai-tap', name: 'lms-lich-su-chinh-diem-bai-tap', component: LichSuChinhDiemBaiTapPage },
        { path: 'lich-su-day-diem-lms', name: 'lms-lich-su-day-diem-lms', component: LichSuDayDiemLmsPage },
        ],
    },
];

export default lmsRoutes;
