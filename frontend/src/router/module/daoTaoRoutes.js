// src/router/module/daoTaoRoutes.js
import DaoTaoLayout from '@/components/daoTao/DaoTaoLayout.vue';
import NganhPage from '@/components/daoTao/NganhPage.vue';
import NganhDetailPage from '@/components/daoTao/NganhDetailPage.vue';
import TrinhDoDaoTaoPage from '@/components/daoTao/TrinhDoDaoTaoPage.vue';
import LoaiChuongTrinhPage from '@/components/daoTao/LoaiChuongTrinhPage.vue';
import KhungKyPage from '@/components/daoTao/KhungKyPage.vue';
import KhoaDaoTaoPage from '@/components/daoTao/KhoaDaoTaoPage.vue';
import LopHanhChinhPage from '@/components/daoTao/LopHanhChinhPage.vue';
import DaoTaoDetailPage from '@/components/daoTao/DaoTaoDetailPage.vue';

import ChuongTrinhPage from '@/components/chuongTrinh/ChuongTrinhPage.vue';
import ChuongTrinhVersionPage from '@/components/chuongTrinh/ChuongTrinhVersionPage.vue';
import SyllabusChuongTrinhPage from '@/components/chuongTrinh/SyllabusChuongTrinhPage.vue';
import MucTieuChuongTrinhPage from '@/components/chuongTrinh/MucTieuChuongTrinhPage.vue';
import NangLucDauRaPage from '@/components/chuongTrinh/NangLucDauRaPage.vue';
import ViTriViecLamPage from '@/components/chuongTrinh/ViTriViecLamPage.vue';
import NhomKienThucPage from '@/components/chuongTrinh/NhomKienThucPage.vue';
import ChuongTrinhMonPage from '@/components/chuongTrinh/ChuongTrinhMonPage.vue';
import MonTienQuyetPage from '@/components/chuongTrinh/MonTienQuyetPage.vue';
import DieuKienMonHocPage from '@/components/chuongTrinh/DieuKienMonHocPage.vue';
import QuyDoiDiemPage from '@/components/chuongTrinh/QuyDoiDiemPage.vue';
import SyllabusMonHocPage from '@/components/chuongTrinh/SyllabusMonHocPage.vue';
import SyllabusChuongBaiPage from '@/components/chuongTrinh/SyllabusChuongBaiPage.vue';
import SyllabusTaiLieuPage from '@/components/chuongTrinh/SyllabusTaiLieuPage.vue';

const daoTaoRoutes = [
  {
    path: '/dao-tao',
    component: DaoTaoLayout,
    redirect: '/dao-tao/nganh',
    children: [
      { path: 'nganh', name: 'dao-tao-nganh', component: NganhPage },
      { path: 'nganh/:nganhId', name: 'dao-tao-nganh-detail', component: NganhDetailPage, props: true },

      { path: 'nganh/:nganhId/chuong-trinh', name: 'dao-tao-nganh-chuong-trinh', component: ChuongTrinhPage, props: true },
      { path: 'chuong-trinh/:chuongTrinhId/version', name: 'dao-tao-chuong-trinh-version', component: ChuongTrinhVersionPage, props: true },

      { path: 'chuong-trinh-version/:chuongTrinhVersionId/syllabus-chuong-trinh', name: 'dao-tao-version-syllabus-chuong-trinh', component: SyllabusChuongTrinhPage, props: true },

      { path: 'chuong-trinh-version/:chuongTrinhVersionId/muc-tieu', name: 'dao-tao-version-muc-tieu', component: MucTieuChuongTrinhPage, props: true },
      { path: 'chuong-trinh-version/:chuongTrinhVersionId/nang-luc-dau-ra', name: 'dao-tao-version-nang-luc', component: NangLucDauRaPage, props: true },
      { path: 'chuong-trinh-version/:chuongTrinhVersionId/vi-tri-viec-lam', name: 'dao-tao-version-vi-tri-viec-lam', component: ViTriViecLamPage, props: true },
      { path: 'chuong-trinh-version/:chuongTrinhVersionId/nhom-kien-thuc', name: 'dao-tao-version-nhom-kien-thuc', component: NhomKienThucPage, props: true },
      { path: 'chuong-trinh-version/:chuongTrinhVersionId/chuong-trinh-mon', name: 'dao-tao-version-chuong-trinh-mon', component: ChuongTrinhMonPage, props: true },

      { path: 'chuong-trinh-mon/:chuongTrinhMonId/mon-tien-quyet', name: 'dao-tao-chuong-trinh-mon-tien-quyet', component: MonTienQuyetPage, props: true },
      { path: 'chuong-trinh-mon/:chuongTrinhMonId/dieu-kien-mon-hoc', name: 'dao-tao-chuong-trinh-mon-dieu-kien', component: DieuKienMonHocPage, props: true },
      { path: 'chuong-trinh-mon/:chuongTrinhMonId/quy-doi-diem', name: 'dao-tao-chuong-trinh-mon-quy-doi-diem', component: QuyDoiDiemPage, props: true },
      { path: 'chuong-trinh-mon/:chuongTrinhMonId/syllabus-mon-hoc', name: 'dao-tao-chuong-trinh-mon-syllabus', component: SyllabusMonHocPage, props: true },
      { path: 'syllabus-mon-hoc/:syllabusMonId/chuong-bai', name: 'dao-tao-syllabus-mon-chuong-bai', component: SyllabusChuongBaiPage, props: true },
      { path: 'syllabus-mon-hoc/:syllabusMonId/tai-lieu', name: 'dao-tao-syllabus-mon-tai-lieu', component: SyllabusTaiLieuPage, props: true },

      { path: 'trinh-do-dao-tao', name: 'dao-tao-trinh-do', component: TrinhDoDaoTaoPage },
      { path: 'loai-chuong-trinh', name: 'dao-tao-loai-chuong-trinh', component: LoaiChuongTrinhPage },
      { path: 'loai-chuong-trinh/:loaiChuongTrinhId/khung-ky', name: 'dao-tao-loai-chuong-trinh-khung-ky', component: KhungKyPage, props: true },
      { path: 'khung-ky', name: 'dao-tao-khung-ky', component: KhungKyPage },
      { path: 'khoa-dao-tao', name: 'dao-tao-khoa-dao-tao', component: KhoaDaoTaoPage },
      { path: 'khoa-dao-tao/:khoaDaoTaoId/lop-hanh-chinh', name: 'dao-tao-khoa-lop-hanh-chinh', component: LopHanhChinhPage, props: true },
      { path: 'chuong-trinh-version/:chuongTrinhVersionId/lop-hanh-chinh', name: 'dao-tao-version-lop-hanh-chinh', component: LopHanhChinhPage, props: true },
      { path: 'lop-hanh-chinh', name: 'dao-tao-lop-hanh-chinh', component: LopHanhChinhPage },

      { path: 'trinh-do-dao-tao/:id', name: 'dao-tao-trinh-do-detail', component: DaoTaoDetailPage, meta: { title: 'Chi tiết trình độ đào tạo', backTo: { name: 'dao-tao-trinh-do' } } },
      { path: 'loai-chuong-trinh/:id', name: 'dao-tao-loai-chuong-trinh-detail', component: DaoTaoDetailPage, meta: { title: 'Chi tiết loại chương trình', backTo: { name: 'dao-tao-loai-chuong-trinh' } } },
      { path: 'khung-ky/:id', name: 'dao-tao-khung-ky-detail', component: DaoTaoDetailPage, meta: { title: 'Chi tiết khung kỳ', backTo: { name: 'dao-tao-khung-ky' } } },
      { path: 'khoa-dao-tao/:id', name: 'dao-tao-khoa-dao-tao-detail', component: DaoTaoDetailPage, meta: { title: 'Chi tiết khóa đào tạo', backTo: { name: 'dao-tao-khoa-dao-tao' } } },
      { path: 'lop-hanh-chinh/:id', name: 'dao-tao-lop-hanh-chinh-detail', component: DaoTaoDetailPage, meta: { title: 'Chi tiết lớp hành chính', backTo: { name: 'dao-tao-lop-hanh-chinh' } } },
    ],
  },
];

export default daoTaoRoutes;
