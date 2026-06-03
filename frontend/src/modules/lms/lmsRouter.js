import { ROLES } from '@/core/constants/roles'
import { requireAuth } from '@/core/guards/authGuard'

import LmsLayout from './layouts/LmsLayout.vue'
import LmsDashboard from './pages/LmsDashboard.vue'
import QuestionBankPage from './pages/QuestionBankPage.vue'
import QuestionFormPage from './pages/QuestionFormPage.vue'
import QuestionApplyReviewPage from './pages/QuestionApplyReviewPage.vue'
import MatrixBlueprintPage from './pages/MatrixBlueprintPage.vue'
import OnlineAssignmentPage from './pages/OnlineAssignmentPage.vue'
import OnlineAssignmentFormPage from './pages/OnlineAssignmentFormPage.vue'
import AssignToClassPage from './pages/AssignToClassPage.vue'
import ClassAssignmentPage from './pages/ClassAssignmentPage.vue'
import SubmissionListPage from './pages/SubmissionListPage.vue'
import GradeEssayPage from './pages/GradeEssayPage.vue'
import AssignmentResultPage from './pages/AssignmentResultPage.vue'
import PushScorePage from './pages/PushScorePage.vue'
import StudentAssignedWorkPage from './pages/StudentAssignedWorkPage.vue'
import StudentAttemptPage from './pages/StudentAttemptPage.vue'
import StudentResultPage from './pages/StudentResultPage.vue'
import LmsHistoryPage from './pages/LmsHistoryPage.vue'
import LmsDataApiPage from './pages/LmsDataApiPage.vue'

const GV_ROLES = [ROLES.ADMIN, ROLES.GIAO_VIEN, ROLES.DAO_TAO]
const SV_ROLES = [ROLES.ADMIN, ROLES.GIAO_VIEN, ROLES.SINH_VIEN]
const ALL_ROLES = [ROLES.ADMIN, ROLES.GIAO_VIEN, ROLES.SINH_VIEN, ROLES.DAO_TAO]

export const lmsRoutes = [
  {
    path: 'lms', component: LmsLayout, beforeEnter: [requireAuth], meta: { module: 'lms', title: 'LMS', roles: ALL_ROLES },
    children: [
      { path: '', redirect: { name: 'Lms.TongQuan' } },
      { path: 'tong-quan', name: 'Lms.TongQuan', component: LmsDashboard, meta: { title: 'Tổng quan LMS', roles: ALL_ROLES } },

      { path: 'ngan-hang-cau-hoi', name: 'Lms.NganHangCauHoi', component: QuestionBankPage, meta: { title: 'Ngân hàng câu hỏi version', roles: GV_ROLES } },
      { path: 'ngan-hang-cau-hoi/them', name: 'Lms.NganHangCauHoiThem', component: QuestionFormPage, meta: { title: 'Soạn câu hỏi', roles: GV_ROLES } },
      { path: 'ngan-hang-cau-hoi/:id/sua', name: 'Lms.NganHangCauHoiSua', component: QuestionFormPage, meta: { title: 'Cập nhật version câu hỏi', roles: GV_ROLES } },
      { path: 'ra-soat-cau-hoi', name: 'Lms.RaSoatCauHoi', component: QuestionApplyReviewPage, meta: { title: 'Rà soát áp dụng câu hỏi', roles: GV_ROLES } },

      { path: 'ma-tran-de', name: 'Lms.MaTranDe', component: MatrixBlueprintPage, meta: { title: 'Ma trận đề', roles: GV_ROLES } },

      { path: 'bai-tap-online', name: 'Lms.BaiTapOnline', component: OnlineAssignmentPage, meta: { title: 'Bài tập online', roles: GV_ROLES } },
      { path: 'bai-tap-online/them', name: 'Lms.BaiTapOnlineThem', component: OnlineAssignmentFormPage, meta: { title: 'Tạo bài tập online', roles: GV_ROLES } },
      { path: 'bai-tap-online/:id/sua', name: 'Lms.BaiTapOnlineSua', component: OnlineAssignmentFormPage, meta: { title: 'Sửa bài tập online', roles: GV_ROLES } },

      { path: 'giao-bai', name: 'Lms.GiaoBai', component: AssignToClassPage, meta: { title: 'Giao bài cho lớp', roles: GV_ROLES } },
      { path: 'bai-tap-lop', name: 'Lms.BaiTapLop', component: ClassAssignmentPage, meta: { title: 'Quản lý bài tập lớp', roles: GV_ROLES } },
      { path: 'bai-tap-lop/:id/danh-sach-nop', name: 'Lms.DanhSachNop', component: SubmissionListPage, meta: { title: 'Danh sách làm/nộp bài', roles: GV_ROLES } },
      { path: 'lan-lam-bai/:id/cham-bai', name: 'Lms.ChamBai', component: GradeEssayPage, meta: { title: 'Chấm bài', roles: GV_ROLES } },
      { path: 'bai-tap-lop/:id/ket-qua', name: 'Lms.KetQua', component: AssignmentResultPage, meta: { title: 'Kết quả bài tập lớp', roles: GV_ROLES } },
      { path: 'bai-tap-lop/:id/day-diem', name: 'Lms.DayDiem', component: PushScorePage, meta: { title: 'Đẩy điểm sang bảng điểm', roles: GV_ROLES } },

      { path: 'bai-duoc-giao', name: 'Lms.SinhVienBaiDuocGiao', component: StudentAssignedWorkPage, meta: { title: 'Bài được giao', roles: SV_ROLES } },
      { path: 'lam-bai/:id', name: 'Lms.LamBai', component: StudentAttemptPage, meta: { title: 'Làm bài online', roles: SV_ROLES } },
      { path: 'bai-tap-lop/:id/ket-qua-sinh-vien', name: 'Lms.KetQuaSinhVien', component: StudentResultPage, meta: { title: 'Kết quả của tôi', roles: SV_ROLES } },

      { path: 'lich-su', name: 'Lms.LichSu', component: LmsHistoryPage, meta: { title: 'Lịch sử LMS', roles: ALL_ROLES } },
      { path: 'du-lieu-api', name: 'Lms.DuLieuApi', component: LmsDataApiPage, meta: { title: 'Kho dữ liệu API LMS', roles: GV_ROLES } },
    ]
  }
]
