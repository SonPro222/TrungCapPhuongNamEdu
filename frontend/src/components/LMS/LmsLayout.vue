<template>
  <div class="module-layout">
    <aside class="module-sidebar">
      <div class="brand-block">
        <p class="eyebrow">Module</p>
        <h2>LMS</h2>
        <p>
          Vận hành bài tập, ngân hàng câu hỏi, làm bài online và lịch sử chấm điểm trong LMS.
        </p>
      </div>
      <div class="path-block">
        <span>Đường dẫn hiện tại</span>
        <strong>{{ routePathLabel }}</strong>
      </div>
      <div class="nav-groups">
        <section v-for="group in navGroups" :key="group.title" class="nav-group">
          <h3>{{ group.title }}</h3>
          <RouterLink v-for="item in group.items" :key="item.to" :to="item.to" class="nav-link">
            <span>{{ item.label }}</span>
            <small>{{ item.path }}</small>
          </RouterLink>
        </section>
      </div>
    </aside>
    <main class="module-content">
      <RouterView />
    </main>
  </div>
</template>
<script setup>
import { computed } from 'vue';
import { RouterLink, RouterView, useRoute } from 'vue-router';
const route = useRoute();
const routePathLabel = computed(() => route.path.replace(/^\//, '') || 'lms');
const navGroups = [
  {
    title: 'Ngân hàng và nội dung',
    items: [
      { label: 'Ngân hàng câu hỏi', to: '/lms/ngan-hang-cau-hoi', path: 'lms/ngan-hang-cau-hoi' },
      { label: 'Đáp án câu hỏi', to: '/lms/dap-an-cau-hoi', path: 'lms/dap-an-cau-hoi' },
      { label: 'Bài tập online', to: '/lms/bai-tap-online', path: 'lms/bai-tap-online' },
      { label: 'Bài tập online câu hỏi', to: '/lms/bai-tap-online-cau-hoi', path: 'lms/bai-tap-online-cau-hoi' },
      { label: 'Đợt giao bài online', to: '/lms/dot-giao-bai-online', path: 'lms/dot-giao-bai-online' },
      { label: 'Bài tập lớp', to: '/lms/bai-tap-lop', path: 'lms/bai-tap-lop' },
    ],
  },
  {
    title: 'Theo dõi kết quả',
    items: [
      { label: 'Lịch sử bài tập lớp', to: '/lms/lich-su-bai-tap-lop', path: 'lms/lich-su-bai-tap-lop' },
      { label: 'Lần làm bài online', to: '/lms/lan-lam-bai-online', path: 'lms/lan-lam-bai-online' },
      { label: 'Câu trả lời sinh viên', to: '/lms/cau-tra-loi-sinh-vien', path: 'lms/cau-tra-loi-sinh-vien' },
      { label: 'Câu trả lời chọn đáp án', to: '/lms/cau-tra-loi-chon-dap-an', path: 'lms/cau-tra-loi-chon-dap-an' },
      { label: 'Kết quả bài tập lớp', to: '/lms/ket-qua-bai-tap-lop', path: 'lms/ket-qua-bai-tap-lop' },
      { label: 'Lịch sử chỉnh điểm bài tập', to: '/lms/lich-su-chinh-diem-bai-tap', path: 'lms/lich-su-chinh-diem-bai-tap' },
      { label: 'Lịch sử đẩy điểm LMS', to: '/lms/lich-su-day-diem-lms', path: 'lms/lich-su-day-diem-lms' },
    ],
  },
];
</script>
<style scoped>
.module-layout { width: 100%; height: 100%; min-height: 0; display: grid; grid-template-columns: 320px minmax(0, 1fr); background: linear-gradient(180deg, #eef4f8 0%, #f7f9fb 100%); overflow: hidden; }
.module-sidebar { display: flex; flex-direction: column; gap: 18px; padding: 18px; color: #eff7ff; background: linear-gradient(180deg, rgba(6, 26, 39, 0.98), rgba(18, 64, 86, 0.98)); overflow-y: auto; }
.brand-block,.path-block,.nav-group { border: 1px solid rgba(155, 213, 255, 0.14); border-radius: 20px; background: rgba(255, 255, 255, 0.05); }
.brand-block,.path-block { padding: 18px; }
.eyebrow { margin: 0 0 8px; color: #92d8ff; font-size: 12px; font-weight: 800; letter-spacing: 0.08em; text-transform: uppercase; }
.brand-block h2 { margin: 0; font-size: 25px; line-height: 1.15; }
.brand-block p:last-child,.path-block span,.nav-link small { color: rgba(231, 243, 252, 0.72); }
.brand-block p:last-child { margin: 12px 0 0; line-height: 1.6; }
.path-block span,.path-block strong { display: block; }
.path-block strong { margin-top: 8px; color: #ffffff; line-height: 1.5; word-break: break-word; }
.nav-groups { display: grid; gap: 14px; }
.nav-group { padding: 14px; }
.nav-group h3 { margin: 0 0 12px; font-size: 13px; text-transform: uppercase; letter-spacing: 0.08em; color: #92d8ff; }
.nav-link { display: block; padding: 12px 14px; border-radius: 14px; color: #eff7ff; text-decoration: none; transition: background 0.18s ease, transform 0.18s ease; }
.nav-link + .nav-link { margin-top: 6px; }
.nav-link span,.nav-link small { display: block; }
.nav-link span { font-weight: 700; }
.nav-link small { margin-top: 4px; font-size: 12px; }
.nav-link:hover,.nav-link.router-link-active { background: rgba(146, 216, 255, 0.14); transform: translateX(2px); }
.module-content { min-width: 0; height: 100%; min-height: 0; overflow: auto; }
@media (max-width: 1100px) { .module-layout { grid-template-columns: 1fr; grid-template-rows: auto minmax(0, 1fr); } .module-sidebar { max-height: 360px; } }
</style>
