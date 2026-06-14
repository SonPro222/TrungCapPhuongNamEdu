<template>
  <section :class="['giang-day-shell', { collapsed: sidebarCollapsed }]">
    <aside class="module-sidebar">
      <div class="sidebar-brand">
        <div class="brand-icon">GD</div>

        <div class="brand-text">
          <strong>Quản lý giảng dạy</strong>
          <span>Phân công & điều phối</span>
        </div>
      </div>

      <nav class="accordion-menu">
        <div class="menu-section">
          <button type="button" class="section-head" @click="toggleGroup('giangDay')">
            <span class="head-left">
              <span class="nav-icon">♙</span>
              <span class="nav-text">Vận hành giảng dạy</span>
            </span>

            <span class="chevron">{{ openGroups.giangDay ? '⌃' : '⌄' }}</span>
          </button>

          <div v-show="openGroups.giangDay" class="section-body">


            <RouterLink
                :to="dieuPhoiTo"
                :class="[
                'menu-link',
                {
                  active: isActive([
                    'GiangDay.ChiTietLopHocPhan',
                    'GiangDay.ChiTietBuoiHoc'
                  ])
                }
              ]"
            >
              <span class="dot"></span>
              <span class="nav-text">Điều phối giảng dạy</span>
            </RouterLink>

            <RouterLink
                :to="{ name: 'GiangDay.LichHoc' }"
                :class="['menu-link', { active: isActive(['GiangDay.LichHoc']) }]"
            >
              <span class="dot"></span>
              <span class="nav-text">Lịch học</span>
            </RouterLink>

            <RouterLink
                :to="{ name: 'GiangDay.RangBuocLichHoc' }"
                :class="['menu-link', { active: isActive(['GiangDay.RangBuocLichHoc']) }]"
            >
              <span class="dot"></span>
              <span class="nav-text">Ràng buộc lịch</span>
            </RouterLink>

          </div>
        </div>

        <div class="menu-section">
          <button type="button" class="section-head" @click="toggleGroup('danhMuc')">
            <span class="head-left">
              <span class="nav-icon">▤</span>
              <span class="nav-text">Danh mục</span>
            </span>

            <span class="chevron">{{ openGroups.danhMuc ? '⌃' : '⌄' }}</span>
          </button>

          <div v-show="openGroups.danhMuc" class="section-body">
            <RouterLink
                :to="{ name: 'GiangDay.GiaoVien' }"
                :class="[
                'menu-link',
                {
                  active: isActive([
                    'GiangDay.GiaoVien',
                    'GiangDay.ChiTietGiaoVien'
                  ])
                }
              ]"
            >
              <span class="dot"></span>
              <span class="nav-text">Giáo viên</span>
            </RouterLink>

            <RouterLink
                :to="{ name: 'GiangDay.PhongHoc' }"
                :class="['menu-link', { active: isActive(['GiangDay.PhongHoc']) }]"
            >
              <span class="dot"></span>
              <span class="nav-text">Phòng học</span>
            </RouterLink>

            <RouterLink
                :to="{ name: 'GiangDay.CaHoc' }"
                :class="['menu-link', { active: isActive(['GiangDay.CaHoc']) }]"
            >
              <span class="dot"></span>
              <span class="nav-text">Ca học</span>
            </RouterLink>
            <RouterLink
                :to="{ name: 'GiangDay.NgayNghi' }"
                :class="['menu-link', { active: isActive(['GiangDay.NgayNghi']) }]"
            >
              <span class="dot"></span>
              <span class="nav-text">Ngày Nghỉ</span>
            </RouterLink>
          </div>
        </div>

        <div class="menu-section">
          <button type="button" class="section-head" @click="toggleGroup('baoCao')">
            <span class="head-left">
              <span class="nav-icon">⌁</span>
              <span class="nav-text">Báo cáo</span>
            </span>

            <span class="chevron">{{ openGroups.baoCao ? '⌃' : '⌄' }}</span>
          </button>

          <div v-show="openGroups.baoCao" class="section-body">
            <button type="button" class="menu-link disabled">
              <span class="dot"></span>
              <span class="nav-text">Báo cáo giảng dạy</span>
              <span class="soon">sau</span>
            </button>
          </div>
        </div>
      </nav>

      <button type="button" class="collapse-btn" @click="sidebarCollapsed = !sidebarCollapsed">
        <span>{{ sidebarCollapsed ? '»' : '«' }}</span>
        <span class="nav-text">{{ sidebarCollapsed ? 'Mở rộng' : 'Thu gọn' }}</span>
      </button>
    </aside>

    <section class="module-main">
      <main class="module-content">
        <RouterView :key="$route.fullPath" />
      </main>
    </section>
  </section>
</template>

<script setup>
import { computed, reactive, ref } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()

const sidebarCollapsed = ref(false)

const openGroups = reactive({
  lopHocPhan: true,
  giangDay: true,
  danhMuc: true,
  baoCao: false
})

const lopHocPhanDangChonId = computed(() => {
  if (route.name === 'GiangDay.ChiTietLopHocPhan') {
    return route.params.id || null
  }

  if (route.name === 'GiangDay.ChiTietBuoiHoc') {
    return route.params.lopHocPhanId || null
  }

  return route.query.lopHocPhanId || null
})

const phanCongTo = computed(() => {
  const query = {
    ...route.query
  }

  if (lopHocPhanDangChonId.value) {
    query.lopHocPhanId = lopHocPhanDangChonId.value
  }

  return {
    name: 'GiangDay.PhanCong',
    query
  }
})

const dieuPhoiTo = computed(() => {
  if (lopHocPhanDangChonId.value) {
    return {
      name: 'GiangDay.ChiTietLopHocPhan',
      params: {
        id: lopHocPhanDangChonId.value
      },
      query: route.query
    }
  }

  return {
    name: 'GiangDay.LopHocPhan'
  }
})

function toggleGroup(name) {
  openGroups[name] = !openGroups[name]
}

function isActive(names) {
  return names.includes(route.name)
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700;900&display=swap');

.giang-day-shell {
  min-height: 100vh;
  display: grid;
  grid-template-columns: 280px minmax(0, 1fr);
  margin: -20px;
  background: #f8fafc;
  color: #1f2937;
}

.module-sidebar {
  position: sticky;
  top: 0;
  align-self: start;
  height: 100vh;
  display: flex;
  flex-direction: column;
  border-right: 1px solid #e5e7eb;
  background: #ffffff;
  padding: 16px 12px;
  overflow: hidden;
}

.sidebar-brand {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 4px 8px 18px;
  border-bottom: 1px solid #eef2f7;
  margin-bottom: 12px;
}

.brand-icon {
  width: 38px;
  height: 38px;
  border-radius: 12px;
  display: grid;
  place-items: center;
  background: #eff6ff;
  color: #2563eb;
  font-size: 13px;
  font-weight: 850;
  letter-spacing: 0.3px;
}

.brand-text {
  display: grid;
  gap: 2px;
  min-width: 0;
}

.brand-text strong {
  color: #111827;
  font-size: 15px;
  font-weight: 750;
  white-space: nowrap;
}

.brand-text span {
  color: #667085;
  font-size: 12px;
  white-space: nowrap;
}

.accordion-menu {
  display: grid;
  gap: 8px;
  overflow: auto;
  padding-right: 2px;
}

.menu-section {
  border-radius: 12px;
}

.section-head {
  width: 100%;
  min-height: 42px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 10px;
  border: 0;
  border-radius: 12px;
  background: transparent;
  color: #475467;
  padding: 10px;
  cursor: pointer;
  font-weight: 750;
  text-align: left;
}

.section-head:hover {
  background: #f8fafc;
  color: #175cd3;
}

.head-left {
  display: inline-flex;
  align-items: center;
  gap: 10px;
  min-width: 0;
}

.nav-icon {
  width: 22px;
  display: inline-grid;
  place-items: center;
  color: #667085;
  font-size: 16px;
  flex: 0 0 auto;
}

.chevron {
  color: #98a2b3;
  font-size: 14px;
  flex: 0 0 auto;
}

.section-body {
  display: grid;
  gap: 4px;
  padding: 2px 0 4px 32px;
}

.menu-link {
  min-height: 38px;
  display: flex;
  align-items: center;
  gap: 9px;
  border: 0;
  border-radius: 10px;
  background: transparent;
  color: #475467;
  padding: 8px 10px;
  text-decoration: none;
  cursor: pointer;
  font-size: 14px;
  font-weight: 650;
  text-align: left;
}

.menu-link:hover {
  background: #f2f4f7;
  color: #175cd3;
}

.menu-link.active,
.menu-link.router-link-active {
  background: #eaf2ff;
  color: #175cd3;
}

.menu-link.active .dot,
.menu-link.router-link-active .dot {
  background: #2563eb;
}

.menu-link.disabled {
  cursor: not-allowed;
  opacity: 0.6;
}

.dot {
  width: 7px;
  height: 7px;
  border-radius: 999px;
  background: #cbd5e1;
  flex: 0 0 auto;
}

.soon {
  margin-left: auto;
  border-radius: 999px;
  background: #f2f4f7;
  color: #667085;
  padding: 2px 7px;
  font-size: 11px;
  font-weight: 750;
}

.collapse-btn {
  margin-top: auto;
  width: 100%;
  min-height: 42px;
  display: flex;
  align-items: center;
  gap: 10px;
  border: 1px solid #e5e7eb;
  border-radius: 12px;
  background: #ffffff;
  color: #475467;
  padding: 9px 10px;
  cursor: pointer;
  font-weight: 700;
}

.collapse-btn:hover {
  background: #f8fafc;
}

.module-main {
  min-width: 0;
  display: flex;
  flex-direction: column;
}

.module-content {
  min-width: 0;
  padding: 18px;
  background: #f8fafc;
}

.giang-day-shell.collapsed {
  grid-template-columns: 76px minmax(0, 1fr);
}

.giang-day-shell.collapsed .module-sidebar {
  padding-inline: 10px;
}

.giang-day-shell.collapsed .brand-text,
.giang-day-shell.collapsed .nav-text,
.giang-day-shell.collapsed .chevron,
.giang-day-shell.collapsed .soon {
  display: none;
}

.giang-day-shell.collapsed .sidebar-brand {
  justify-content: center;
  padding-inline: 0;
}

.giang-day-shell.collapsed .section-head {
  justify-content: center;
  padding-inline: 0;
}

.giang-day-shell.collapsed .head-left {
  justify-content: center;
}

.giang-day-shell.collapsed .section-body {
  padding-left: 0;
}

.giang-day-shell.collapsed .menu-link {
  justify-content: center;
  padding-inline: 0;
}

.giang-day-shell.collapsed .dot {
  display: none;
}

.giang-day-shell.collapsed .collapse-btn {
  justify-content: center;
}


.giang-day-shell {
  grid-template-columns: 260px minmax(0, 1fr);
  font-family: 'Roboto', Arial, sans-serif;
  font-size: 13px;
}

.module-sidebar {
  padding: 12px 10px;
}

.sidebar-brand {
  gap: 10px;
  padding: 2px 6px 12px;
  margin-bottom: 8px;
}

.brand-icon {
  width: 34px;
  height: 34px;
  border-radius: 10px;
  font-size: 12px;
}

.brand-text strong {
  font-size: 14px;
}

.brand-text span {
  font-size: 11.5px;
}

.accordion-menu {
  gap: 5px;
}

.section-head {
  min-height: 36px;
  border-radius: 10px;
  padding: 7px 9px;
  font-size: 13px;
}

.section-body {
  gap: 3px;
  padding: 2px 0 4px 28px;
}

.menu-link {
  min-height: 32px;
  border-radius: 9px;
  padding: 6px 9px;
  font-size: 12.5px;
}

.collapse-btn {
  min-height: 36px;
  border-radius: 10px;
  padding: 7px 9px;
  font-size: 12.5px;
}

.module-content {
  padding: 12px;
}

:deep(.page) {
  gap: 10px;
  font-family: 'Roboto', Arial, sans-serif;
}

:deep(.page-head) {
  align-items: center;
  gap: 10px;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  background: #ffffff;
  padding: 10px 12px;
}

:deep(.page-head h1) {
  margin: 0;
  color: #0f172a;
  font-size: 18px;
  line-height: 1.2;
  font-weight: 800;
  letter-spacing: -0.02em;
}

:deep(.page-head p) {
  margin: 3px 0 0;
  color: #64748b;
  font-size: 12px;
  line-height: 1.35;
}

:deep(.btn) {
  min-height: 30px;
  border-radius: 8px;
  padding: 5px 10px;
  font-family: inherit;
  font-size: 12px;
  font-weight: 700;
  line-height: 1.1;
}

:deep(.btn.small) {
  min-height: 24px;
  padding: 4px 8px;
  font-size: 11px;
}

:deep(input),
:deep(select),
:deep(textarea) {
  min-height: 30px;
  border-radius: 8px;
  padding: 5px 8px;
  font-family: inherit;
  font-size: 13px;
}

:deep(.table-card),
:deep(.filter-card),
:deep(.info-card),
:deep(.search-card) {
  padding: 10px;
  border-radius: 12px;
}

:deep(th),
:deep(td) {
  padding: 7px 8px;
  font-size: 12.5px;
}

@media (max-width: 860px) {
  .giang-day-shell,
  .giang-day-shell.collapsed {
    grid-template-columns: 1fr;
    margin: 0;
  }

  .module-sidebar {
    position: relative;
    height: auto;
    border-right: 0;
    border-bottom: 1px solid #e5e7eb;
  }

  .giang-day-shell.collapsed .brand-text,
  .giang-day-shell.collapsed .nav-text,
  .giang-day-shell.collapsed .chevron {
    display: inline;
  }

  .giang-day-shell.collapsed .section-body {
    padding-left: 28px;
  }

  .giang-day-shell.collapsed .menu-link {
    justify-content: flex-start;
    padding-inline: 9px;
  }

  .giang-day-shell.collapsed .dot {
    display: block;
  }
}

@media (max-width: 640px) {
  .module-content {
    padding: 10px;
  }
}

</style>
