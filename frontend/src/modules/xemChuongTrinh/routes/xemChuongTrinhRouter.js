export const xemChuongTrinhRoutes = [
  {
    path: 'xem-chuong-trinh',
    children: [
      {
        path: 'nganh',
        name: 'XemChuongTrinh.Tang1Nganh',
        component: () => import('../pages/Tang1NganhPage.vue'),
        meta: { title: 'Tầng 1 - Ngành' }
      },
      {
        path: 'nganh/:nganhId/chuong-trinh',
        name: 'XemChuongTrinh.Tang2ChuongTrinh',
        component: () => import('../pages/Tang2ChuongTrinhPage.vue'),
        meta: { title: 'Tầng 2 - Chương trình' }
      },
      {
        path: 'nganh/:nganhId/chuong-trinh/:chuongTrinhId/version',
        name: 'XemChuongTrinh.Tang3VersionChuongTrinh',
        component: () => import('../pages/Tang3VersionChuongTrinhPage.vue'),
        meta: { title: 'Tầng 3 - Version chương trình' }
      },
      {
        path: 'nganh/:nganhId/chuong-trinh/:chuongTrinhId/version/:versionId/ky',
        name: 'XemChuongTrinh.Tang4Ky',
        component: () => import('../pages/Tang4KyPage.vue'),
        meta: { title: 'Tầng 4 - Kỳ' }
      },
      {
        path: 'nganh/:nganhId/chuong-trinh/:chuongTrinhId/version/:versionId/ky/:khungKyId/nhom-kien-thuc',
        name: 'XemChuongTrinh.Tang5NhomKienThuc',
        component: () => import('../pages/Tang5NhomKienThucPage.vue'),
        meta: { title: 'Tầng 5 - Nhóm kiến thức' }
      },
      {
        path: 'nganh/:nganhId/chuong-trinh/:chuongTrinhId/version/:versionId/ky/:khungKyId/nhom-kien-thuc/:nhomKienThucId/mon',
        name: 'XemChuongTrinh.Tang6MonTrongChuongTrinh',
        component: () => import('../pages/Tang6MonTrongChuongTrinhPage.vue'),
        meta: { title: 'Tầng 6 - Môn trong chương trình' }
      },
      {
        path: 'nganh/:nganhId/chuong-trinh/:chuongTrinhId/version/:versionId/ky/:khungKyId/nhom-kien-thuc/:nhomKienThucId/mon/:chuongTrinhMonId/mon-hoc',
        name: 'XemChuongTrinh.Tang7MonHoc',
        component: () => import('../pages/Tang7MonHocPage.vue'),
        meta: { title: 'Tầng 7 - Môn học' }
      },
      {
        path: 'nganh/:nganhId/chuong-trinh/:chuongTrinhId/version/:versionId/ky/:khungKyId/nhom-kien-thuc/:nhomKienThucId/mon/:chuongTrinhMonId/syllabus',
        name: 'XemChuongTrinh.Tang8SyllabusMon',
        component: () => import('../pages/Tang8SyllabusMonPage.vue'),
        meta: { title: 'Tầng 8 - Syllabus môn' }
      },
      {
        path: 'nganh/:nganhId/chuong-trinh/:chuongTrinhId/version/:versionId/ky/:khungKyId/nhom-kien-thuc/:nhomKienThucId/mon/:chuongTrinhMonId/syllabus/:syllabusMonId/chi-tiet-mon-diem',
        name: 'XemChuongTrinh.Tang9ChiTietMonDiem',
        component: () => import('../pages/Tang9ChiTietMonDiemPage.vue'),
        meta: { title: 'Tầng 9 - Chi tiết môn và điểm' }
      },
      {
        path: 'nganh/:nganhId/chuong-trinh/:chuongTrinhId/version/:versionId/ky/:khungKyId/nhom-kien-thuc/:nhomKienThucId/mon/:chuongTrinhMonId/syllabus/:syllabusMonId/tai-lieu',
        name: 'XemChuongTrinh.Tang10TaiLieuSyllabusMon',
        component: () => import('../pages/Tang10TaiLieuSyllabusMonPage.vue'),
        meta: { title: 'Tầng 10 - Tài liệu syllabus môn' }
      }
    ]
  }
]
