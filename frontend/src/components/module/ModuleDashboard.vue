<template>
  <section class="module-page">
    <div class="page-head">
      <div>
        <p class="eyebrow">Luồng nghiệp vụ</p>
        <h1>{{ title }}</h1>
        <p>{{ description }}</p>
      </div>
      <RouterLink class="back-home" to="/">Trang chủ</RouterLink>
    </div>

    <div class="feature-grid">
      <article v-for="item in features" :key="item" class="feature-card">
        <span>{{ item.charAt(0) }}</span>
        <h3>{{ item }}</h3>
        <p>Trang nghiệp vụ {{ item.toLowerCase() }} đang sẵn sàng để gắn danh sách, form và API.</p>
      </article>
    </div>
  </section>
</template>

<script setup>
import { computed } from 'vue';
import { useRoute } from 'vue-router';

const route = useRoute();

const moduleInfo = {
  chuongTrinh: { title: 'Chương trình', description: 'Quản lý chương trình đào tạo, ngành, môn học, syllabus và chuẩn đầu ra.', features: ['Chương trình đào tạo', 'Ngành', 'Môn học', 'Syllabus'] },
  daoTao: { title: 'Đào tạo', description: 'Quản lý kế hoạch đào tạo, lớp học phần, đăng ký học phần và tiến độ đào tạo.', features: ['Kế hoạch đào tạo', 'Lớp học phần', 'Đăng ký học phần', 'Tiến độ'] },
  diem: { title: 'Điểm', description: 'Quản lý bảng điểm, nhập điểm, tổng kết điểm và lịch sử chỉnh sửa điểm.', features: ['Nhập điểm', 'Bảng điểm', 'Tổng kết', 'Lịch sử điểm'] },
  giangDay: { title: 'Giảng dạy', description: 'Quản lý lớp giảng dạy, lịch dạy, sinh viên lớp học phần và nội dung học tập.', features: ['Lịch dạy', 'Lớp giảng dạy', 'Sinh viên lớp', 'Nội dung học'] },
  hocPhi: { title: 'Học phí', description: 'Quản lý khoản thu, học phí sinh viên, giao dịch và đối soát thanh toán.', features: ['Khoản thu', 'Công nợ', 'Giao dịch', 'Đối soát'] },
  lms: { title: 'LMS', description: 'Quản lý bài tập online, ngân hàng câu hỏi, bài làm và đồng bộ điểm.', features: ['Bài tập', 'Ngân hàng câu hỏi', 'Bài làm', 'Đồng bộ điểm'] },
  sinhVien: { title: 'Sinh viên', description: 'Quản lý hồ sơ sinh viên, lớp hành chính, chương trình theo học và trạng thái học tập.', features: ['Hồ sơ', 'Lớp hành chính', 'Chương trình học', 'Trạng thái'] },
  thi: { title: 'Thi', description: 'Quản lý kỳ thi, lịch thi, phòng thi, đề thi và kết quả thi.', features: ['Kỳ thi', 'Lịch thi', 'Phòng thi', 'Kết quả'] },
  heThong: { title: 'Hệ thống', description: 'Quản trị người dùng, vai trò, quyền truy cập và cấu hình toàn hệ thống.', features: ['Tài khoản', 'Vai trò', 'Phân quyền', 'Cấu hình'] },
};

const current = computed(() => moduleInfo[route.meta.moduleKey] || moduleInfo.chuongTrinh);
const title = computed(() => current.value.title);
const description = computed(() => current.value.description);
const features = computed(() => current.value.features);
</script>

<style scoped>
.module-page { width: 100%; height: 100%; overflow: auto; padding: clamp(18px, 3vw, 36px); }
.page-head { display: flex; justify-content: space-between; gap: 16px; align-items: flex-start; padding: clamp(22px, 4vw, 40px); border-radius: 28px; background: linear-gradient(135deg, #ffffff, #eff6ff); border: 1px solid #dbeafe; box-shadow: 0 24px 70px rgba(15, 23, 42, 0.09); }
.eyebrow { margin: 0 0 8px; color: #2563eb; font-weight: 900; text-transform: uppercase; letter-spacing: 0.08em; }
h1 { margin: 0; color: #0f172a; font-size: clamp(30px, 4vw, 48px); font-weight: 950; }
.page-head p:last-child { max-width: 760px; margin: 12px 0 0; color: #475569; font-size: 16px; line-height: 1.65; }
.back-home { flex: 0 0 auto; padding: 10px 14px; border-radius: 999px; background: #2563eb; color: white; font-weight: 900; text-decoration: none; }
.feature-grid { display: grid; grid-template-columns: repeat(4, minmax(180px, 1fr)); gap: 16px; margin-top: 22px; }
.feature-card { padding: 20px; border-radius: 22px; background: #ffffff; border: 1px solid #e2e8f0; box-shadow: 0 18px 48px rgba(15, 23, 42, 0.06); }
.feature-card span { width: 40px; height: 40px; display: inline-flex; align-items: center; justify-content: center; border-radius: 14px; background: #dbeafe; color: #1d4ed8; font-weight: 950; margin-bottom: 14px; }
.feature-card h3 { margin: 0 0 8px; color: #0f172a; font-size: 17px; }
.feature-card p { margin: 0; color: #64748b; line-height: 1.55; }
@media (max-width: 1000px) { .feature-grid { grid-template-columns: repeat(2, minmax(180px, 1fr)); } }
@media (max-width: 640px) { .page-head { flex-direction: column; } .feature-grid { grid-template-columns: 1fr; } }
</style>
