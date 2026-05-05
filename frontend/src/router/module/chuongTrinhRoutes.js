import ChuongTrinhLayout from '@/components/chuongTrinh/ChuongTrinhLayout.vue';
import ChuongTrinhPage from '@/components/chuongTrinh/ChuongTrinhPage.vue';
import ChuongTrinhVersionPage from '@/components/chuongTrinh/ChuongTrinhVersionPage.vue';
import SyllabusChuongTrinhPage from '@/components/chuongTrinh/SyllabusChuongTrinhPage.vue';
import MucTieuChuongTrinhPage from '@/components/chuongTrinh/MucTieuChuongTrinhPage.vue';
import NangLucDauRaPage from '@/components/chuongTrinh/NangLucDauRaPage.vue';
import ViTriViecLamPage from '@/components/chuongTrinh/ViTriViecLamPage.vue';
import MonHocPage from '@/components/chuongTrinh/MonHocPage.vue';
import NhomKienThucPage from '@/components/chuongTrinh/NhomKienThucPage.vue';
import ChuongTrinhMonPage from '@/components/chuongTrinh/ChuongTrinhMonPage.vue';
import MonTienQuyetPage from '@/components/chuongTrinh/MonTienQuyetPage.vue';
import NhomTuChonPage from '@/components/chuongTrinh/NhomTuChonPage.vue';
import MonTuChonPage from '@/components/chuongTrinh/MonTuChonPage.vue';
import SyllabusMonHocPage from '@/components/chuongTrinh/SyllabusMonHocPage.vue';
import SyllabusChuongBaiPage from '@/components/chuongTrinh/SyllabusChuongBaiPage.vue';
import SyllabusTaiLieuPage from '@/components/chuongTrinh/SyllabusTaiLieuPage.vue';
import DieuKienMonHocPage from '@/components/chuongTrinh/DieuKienMonHocPage.vue';
import QuyDoiDiemPage from '@/components/chuongTrinh/QuyDoiDiemPage.vue';
import DieuKienTotNghiepPage from '@/components/chuongTrinh/DieuKienTotNghiepPage.vue';

const chuongTrinhRoutes = [
    {
        path: '/chuong-trinh',
        component: ChuongTrinhLayout,
        children: [
        { path: '', redirect: '/chuong-trinh/chuong-trinh' },
        { path: 'chuong-trinh', name: 'chuong-trinh-chuong-trinh', component: ChuongTrinhPage },
        { path: 'chuong-trinh-version', name: 'chuong-trinh-chuong-trinh-version', component: ChuongTrinhVersionPage },
        { path: 'syllabus-chuong-trinh', name: 'chuong-trinh-syllabus-chuong-trinh', component: SyllabusChuongTrinhPage },
        { path: 'muc-tieu-chuong-trinh', name: 'chuong-trinh-muc-tieu-chuong-trinh', component: MucTieuChuongTrinhPage },
        { path: 'nang-luc-dau-ra', name: 'chuong-trinh-nang-luc-dau-ra', component: NangLucDauRaPage },
        { path: 'vi-tri-viec-lam', name: 'chuong-trinh-vi-tri-viec-lam', component: ViTriViecLamPage },
        { path: 'mon-hoc', name: 'chuong-trinh-mon-hoc', component: MonHocPage },
        { path: 'nhom-kien-thuc', name: 'chuong-trinh-nhom-kien-thuc', component: NhomKienThucPage },
        { path: 'chuong-trinh-mon', name: 'chuong-trinh-chuong-trinh-mon', component: ChuongTrinhMonPage },
        { path: 'mon-tien-quyet', name: 'chuong-trinh-mon-tien-quyet', component: MonTienQuyetPage },
        { path: 'nhom-tu-chon', name: 'chuong-trinh-nhom-tu-chon', component: NhomTuChonPage },
        { path: 'mon-tu-chon', name: 'chuong-trinh-mon-tu-chon', component: MonTuChonPage },
        { path: 'syllabus-mon-hoc', name: 'chuong-trinh-syllabus-mon-hoc', component: SyllabusMonHocPage },
        { path: 'syllabus-chuong-bai', name: 'chuong-trinh-syllabus-chuong-bai', component: SyllabusChuongBaiPage },
        { path: 'syllabus-tai-lieu', name: 'chuong-trinh-syllabus-tai-lieu', component: SyllabusTaiLieuPage },
        { path: 'dieu-kien-mon-hoc', name: 'chuong-trinh-dieu-kien-mon-hoc', component: DieuKienMonHocPage },
        { path: 'quy-doi-diem', name: 'chuong-trinh-quy-doi-diem', component: QuyDoiDiemPage },
        { path: 'dieu-kien-tot-nghiep', name: 'chuong-trinh-dieu-kien-tot-nghiep', component: DieuKienTotNghiepPage },
        ]
    }
];

export default chuongTrinhRoutes;
