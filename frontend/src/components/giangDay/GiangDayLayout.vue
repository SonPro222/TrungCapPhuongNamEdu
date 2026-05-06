<template>
  <div class="module-layout">
    <aside class="module-sidebar">
      <div class="brand-block">
        <p class="eyebrow">Module</p>
        <h2>Giảng dạy</h2>
        <p>
          Điều phối lớp học, lịch học, phân công giảng viên, phòng học và các nghiệp vụ vận hành
          giảng dạy.
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
const routePathLabel = computed(() => route.path.replace(/^\//, '') || 'giang-day');
const navGroups = [
  {
    title: 'Danh mục vận hành',
    items: [
      { label: 'Ca học', to: '/giang-day/ca-hoc', path: 'giang-day/ca-hoc' },
      { label: 'Phòng học', to: '/giang-day/phong-hoc', path: 'giang-day/phong-hoc' },
      { label: 'Giáo viên', to: '/giang-day/giao-vien', path: 'giang-day/giao-vien' },
      { label: 'Lớp học phần', to: '/giang-day/lop-hoc-phan', path: 'giang-day/lop-hoc-phan' },
    ],
  },
  {
    title: 'Tổ chức giảng dạy',
    items: [
      { label: 'Phân công giảng dạy', to: '/giang-day/phan-cong-giang-day', path: 'giang-day/phan-cong-giang-day' },
      { label: 'Sinh viên lớp học phần', to: '/giang-day/sinh-vien-lop-hoc-phan', path: 'giang-day/sinh-vien-lop-hoc-phan' },
      { label: 'Lịch học', to: '/giang-day/lich-hoc', path: 'giang-day/lich-hoc' },
      { label: 'Yêu cầu đổi lịch', to: '/giang-day/yeu-cau-doi-lich', path: 'giang-day/yeu-cau-doi-lich' },
      { label: 'Điểm danh', to: '/giang-day/diem-danh', path: 'giang-day/diem-danh' },
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
