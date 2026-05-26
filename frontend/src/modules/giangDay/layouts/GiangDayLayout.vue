<template>
  <section class="giang-day-layout">
    <PageHeader
        title="Quản lý giảng dạy"
        description="Đào tạo quản lý lớp học phần, lịch học, phân công và xem tổng hợp sinh viên, điểm, điểm danh theo từng lớp."
    />

    <nav class="giang-day-menu">
      <RouterLink :to="{ name: 'GiangDay.QuanLy' }">
        Quản lý giảng dạy
      </RouterLink>

      <RouterLink :to="{ name: 'GiangDay.LopHocPhan' }">
        Lớp học phần
      </RouterLink>

      <RouterLink :to="{ name: 'GiangDay.PhanCong' }">
        Phân công giảng dạy
      </RouterLink>

      <RouterLink :to="{ name: 'GiangDay.LichHoc' }">
        Lịch học
      </RouterLink>

      <RouterLink :to="{ name: 'GiangDay.GiaoVien' }">
        Giáo viên
      </RouterLink>

      <RouterLink :to="{ name: 'GiangDay.PhongHoc' }">
        Phòng học
      </RouterLink>

      <RouterLink :to="{ name: 'GiangDay.CaHoc' }">
        Ca học
      </RouterLink>
    </nav>

    <div class="giang-day-content">
      <RouterView :key="$route.fullPath" />
    </div>
  </section>
</template>

<script setup>
import PageHeader from '@/shared/components/PageHeader.vue'
</script>

<style scoped>
.giang-day-layout {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.giang-day-menu {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.giang-day-menu a {
  padding: 9px 13px;
  border: 1px solid var(--color-border);
  border-radius: var(--radius);
  background: var(--color-white);
  color: var(--color-text);
  text-decoration: none;
  font-weight: 700;
}

.giang-day-menu a.router-link-active {
  background: var(--color-primary);
  border-color: var(--color-primary);
  color: var(--color-white);
}

.giang-day-content {
  background: var(--color-white);
  border: 1px solid var(--color-border);
  border-radius: var(--radius);
  padding: 20px;
}
</style>