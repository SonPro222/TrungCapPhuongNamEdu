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
              <span class="nav-icon">▦</span>
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
              <span class="nav-text">Ngày nghỉ</span>
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

      <button
          type="button"
          class="sidebar-toggle-float"
          :title="sidebarCollapsed ? 'Mở rộng menu' : 'Thu gọn menu'"
          @click="sidebarCollapsed = !sidebarCollapsed"
      >
        {{ sidebarCollapsed ? '›' : '‹' }}
      </button>

      <button type="button" class="collapse-btn" @click="sidebarCollapsed = !sidebarCollapsed">
        <span class="collapse-icon">{{ sidebarCollapsed ? '»' : '«' }}</span>
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
  cauHinhGiangDay: true,
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
@import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700;800;900&display=swap');

.giang-day-shell {
  --primary: #077149;
  --primary-dark: #055d3c;
  --primary-soft: #e8f6f0;
  --primary-soft-2: #f4fbf8;
  --primary-border: #9bd8c1;

  --sidebar-bg: #ffffff;
  --page-bg: #f6faf8;

  --text-main: #102033;
  --text-muted: #64748b;
  --text-soft: #475569;

  --border: #dbe7e1;
  --border-soft: #edf3ef;

  min-height: calc(100vh - 60px);
  display: grid;
  grid-template-columns: 264px minmax(0, 1fr);
  margin: -20px;
  background: var(--page-bg);
  color: var(--text-main);
  font-family: 'Roboto', Arial, sans-serif;
  transition: grid-template-columns 0.25s ease;
}

.module-sidebar {
  position: sticky;
  top: 0;
  align-self: start;
  height: calc(100vh - 60px);
  display: flex;
  flex-direction: column;
  box-sizing: border-box;
  background: var(--sidebar-bg);
  border-right: 1px solid var(--border);
  padding: 14px 10px;
  overflow-x: visible;
  box-shadow: 8px 0 24px rgba(15, 23, 42, 0.025);
}

.sidebar-brand {
  display: flex;
  align-items: center;
  gap: 10px;
  min-height: 52px;
  padding: 6px 8px 14px;
  margin-bottom: 10px;
  border-bottom: 1px solid var(--border-soft);
  white-space: nowrap;
}

.brand-icon {
  width: 38px;
  height: 38px;
  display: grid;
  place-items: center;
  flex: 0 0 auto;
  border-radius: 12px;
  background: var(--primary);
  color: #ffffff;
  font-size: 13px;
  font-weight: 900;
  letter-spacing: 0.3px;
  box-shadow: 0 8px 18px rgba(7, 113, 73, 0.2);
}

.brand-text {
  min-width: 0;
  display: grid;
  gap: 2px;
  opacity: 1;
  transition:
      opacity 0.18s ease,
      width 0.18s ease,
      visibility 0.18s ease;
}

.brand-text strong {
  overflow: hidden;
  color: var(--text-main);
  font-size: 15px;
  line-height: 1.25;
  font-weight: 850;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.brand-text span {
  overflow: hidden;
  color: var(--text-muted);
  font-size: 12px;
  font-weight: 650;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.accordion-menu {
  flex: 1;
  display: grid;
  align-content: start;
  gap: 6px;
  overflow-x: hidden;
  overflow-y: auto;
  padding-right: 2px;
}

.accordion-menu::-webkit-scrollbar {
  width: 6px;
}

.accordion-menu::-webkit-scrollbar-thumb {
  border-radius: 999px;
  background: #c9d8d0;
}

.accordion-menu::-webkit-scrollbar-track {
  background: transparent;
}

.menu-section {
  border-radius: 12px;
}

.section-head {
  width: 100%;
  min-height: 40px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 8px;
  border: 0;
  border-radius: 12px;
  background: transparent;
  color: var(--text-soft);
  padding: 8px 10px;
  cursor: pointer;
  font-family: inherit;
  font-size: 14px;
  font-weight: 800;
  text-align: left;
  white-space: nowrap;
  transition:
      background 0.16s ease,
      color 0.16s ease;
}

.section-head:hover {
  background: var(--primary-soft-2);
  color: var(--primary-dark);
}

.head-left {
  min-width: 0;
  display: inline-flex;
  align-items: center;
  gap: 9px;
}

.nav-icon {
  width: 22px;
  height: 22px;
  display: inline-grid;
  place-items: center;
  flex: 0 0 auto;
  border-radius: 8px;
  background: var(--primary-soft);
  color: var(--primary);
  font-size: 14px;
  font-weight: 900;
}

.nav-text {
  overflow: hidden;
  opacity: 1;
  text-overflow: ellipsis;
  white-space: nowrap;
  transition:
      opacity 0.18s ease,
      width 0.18s ease,
      visibility 0.18s ease;
}

.chevron {
  flex: 0 0 auto;
  color: #7a8b84;
  font-size: 13px;
  font-weight: 900;
  opacity: 1;
  transition:
      opacity 0.18s ease,
      width 0.18s ease,
      visibility 0.18s ease;
}

.section-body {
  display: grid;
  gap: 3px;
  padding: 2px 0 6px 31px;
}

.menu-link {
  min-height: 34px;
  display: flex;
  align-items: center;
  gap: 8px;
  border: 0;
  border-radius: 10px;
  background: transparent;
  color: var(--text-soft);
  padding: 7px 10px;
  text-decoration: none;
  cursor: pointer;
  font-family: inherit;
  font-size: 13.5px;
  line-height: 1.25;
  font-weight: 700;
  text-align: left;
  white-space: nowrap;
  transition:
      background 0.16s ease,
      color 0.16s ease,
      box-shadow 0.16s ease;
}

.menu-link:hover {
  background: #f2f8f5;
  color: var(--primary-dark);
}

.menu-link.active,
.menu-link.router-link-active {
  background: var(--primary-soft);
  color: var(--primary-dark);
  box-shadow: inset 3px 0 0 var(--primary);
}

.menu-link.active .dot,
.menu-link.router-link-active .dot {
  background: var(--primary);
}

.menu-link.disabled {
  cursor: not-allowed;
  opacity: 0.62;
}

.dot {
  width: 6px;
  height: 6px;
  flex: 0 0 auto;
  border-radius: 999px;
  background: #cbd5e1;
}

.soon {
  margin-left: auto;
  flex: 0 0 auto;
  border-radius: 999px;
  background: #edf3ef;
  color: var(--text-muted);
  padding: 2px 7px;
  font-size: 11px;
  font-weight: 800;
}

.sidebar-toggle-float {
  position: absolute;
  top: 50%;
  right: -11px;
  z-index: 20;
  width: 22px;
  height: 42px;
  display: grid;
  place-items: center;
  border: 1px solid var(--border);
  border-left: 0;
  border-radius: 0 12px 12px 0;
  background: #ffffff;
  color: var(--primary);
  cursor: pointer;
  font-size: 17px;
  line-height: 1;
  font-weight: 900;
  box-shadow: 4px 0 12px rgba(15, 23, 42, 0.06);
  transform: translateY(-50%);
  transition:
      background 0.16s ease,
      color 0.16s ease,
      border-color 0.16s ease,
      box-shadow 0.16s ease,
      transform 0.16s ease;
}

.sidebar-toggle-float:hover {
  background: var(--primary);
  color: #ffffff;
  border-color: var(--primary);
  box-shadow: 5px 0 14px rgba(7, 113, 73, 0.16);
  transform: translateY(-50%) translateX(1px);
}

.sidebar-toggle-float:active {
  transform: translateY(-50%) translateX(0);
}

.collapse-btn {
  width: 100%;
  min-height: 40px;
  display: flex;
  align-items: center;
  justify-content: flex-start;
  gap: 9px;
  margin-top: 10px;
  border: 1px solid var(--primary-border);
  border-radius: 12px;
  background: var(--primary-soft-2);
  color: var(--primary-dark);
  padding: 8px 10px;
  cursor: pointer;
  font-family: inherit;
  font-size: 14px;
  font-weight: 800;
  white-space: nowrap;
  transition:
      background 0.16s ease,
      box-shadow 0.16s ease;
}

.collapse-btn:hover {
  background: var(--primary-soft);
  box-shadow: 0 8px 18px rgba(7, 113, 73, 0.08);
}

.collapse-icon {
  width: 22px;
  height: 22px;
  display: inline-grid;
  place-items: center;
  flex: 0 0 auto;
  border-radius: 8px;
  background: #ffffff;
  color: var(--primary);
  font-size: 16px;
  font-weight: 900;
}

.module-main {
  min-width: 0;
  display: flex;
  flex-direction: column;
}

.module-content {
  min-width: 0;
  min-height: calc(100vh - 60px);
  background: var(--page-bg);
  padding: 16px;
}

/* ================= TINH CHỈNH GIAO DIỆN CON BÊN TRONG MODULE ================= */

:deep(.page) {
  gap: 12px;
  font-family: 'Roboto', Arial, sans-serif;
}

:deep(.page-head) {
  align-items: center;
  gap: 10px;
  border: 1px solid var(--border);
  border-radius: 14px;
  background: #ffffff;
  padding: 12px 14px;
  box-shadow: 0 8px 20px rgba(15, 23, 42, 0.025);
}

:deep(.page-head h1) {
  margin: 0;
  color: var(--text-main);
  font-size: 18px;
  line-height: 1.2;
  font-weight: 850;
  letter-spacing: -0.02em;
}

:deep(.page-head p) {
  margin: 3px 0 0;
  color: var(--text-muted);
  font-size: 12px;
  line-height: 1.35;
}

:deep(.btn) {
  min-height: 32px;
  border-radius: 9px;
  padding: 6px 11px;
  font-family: inherit;
  font-size: 12.5px;
  font-weight: 800;
  line-height: 1.1;
}

:deep(.btn.small) {
  min-height: 26px;
  padding: 4px 8px;
  font-size: 11.5px;
}

:deep(input),
:deep(select),
:deep(textarea) {
  min-height: 32px;
  border-radius: 9px;
  padding: 6px 9px;
  font-family: inherit;
  font-size: 13px;
}

:deep(.table-card),
:deep(.filter-card),
:deep(.info-card),
:deep(.search-card) {
  padding: 12px;
  border-radius: 14px;
  border-color: var(--border);
  background: #ffffff;
}

:deep(th),
:deep(td) {
  padding: 8px 9px;
  font-size: 12.5px;
}

/* ================= TRẠNG THÁI COLLAPSED ================= */

.giang-day-shell.collapsed {
  grid-template-columns: 72px minmax(0, 1fr);
}

.giang-day-shell.collapsed .module-sidebar {
  padding-inline: 9px;
}

.giang-day-shell.collapsed .sidebar-brand {
  justify-content: center;
  padding-inline: 0;
}

.giang-day-shell.collapsed .brand-text,
.giang-day-shell.collapsed .nav-text,
.giang-day-shell.collapsed .chevron,
.giang-day-shell.collapsed .soon {
  width: 0;
  opacity: 0;
  visibility: hidden;
}

.giang-day-shell.collapsed .section-head {
  justify-content: center;
  padding-inline: 0;
}

.giang-day-shell.collapsed .head-left {
  justify-content: center;
  gap: 0;
}

.giang-day-shell.collapsed .section-body {
  display: none !important;
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
  padding-inline: 0;
}

.giang-day-shell.collapsed .collapse-icon {
  background: var(--primary);
  color: #ffffff;
}

.giang-day-shell.collapsed .sidebar-toggle-float {
  right: -11px;
  background: #ffffff;
  color: var(--primary);
  border-color: var(--border);
}

.giang-day-shell.collapsed .sidebar-toggle-float:hover {
  background: var(--primary);
  color: #ffffff;
  border-color: var(--primary);
}

/* ================= RESPONSIVE ================= */

@media (max-width: 860px) {
  .giang-day-shell,
  .giang-day-shell.collapsed {
    grid-template-columns: 1fr;
    margin: 0;
  }

  .module-sidebar {
    position: relative;
    top: auto;
    height: auto;
    overflow-x: hidden;
    border-right: 0;
    border-bottom: 1px solid var(--border);
    padding: 12px;
  }

  .sidebar-brand {
    min-height: auto;
    padding-bottom: 10px;
  }

  .accordion-menu {
    max-height: none;
    overflow: visible;
  }

  .sidebar-toggle-float {
    display: none;
  }

  .giang-day-shell.collapsed .brand-text,
  .giang-day-shell.collapsed .nav-text,
  .giang-day-shell.collapsed .chevron,
  .giang-day-shell.collapsed .soon {
    width: auto;
    opacity: 1;
    visibility: visible;
  }

  .giang-day-shell.collapsed .section-head {
    justify-content: space-between;
    padding-inline: 10px;
  }

  .giang-day-shell.collapsed .head-left {
    justify-content: flex-start;
    gap: 9px;
  }

  .giang-day-shell.collapsed .section-body {
    display: grid !important;
    padding-left: 31px;
  }

  .giang-day-shell.collapsed .menu-link {
    justify-content: flex-start;
    padding-inline: 10px;
  }

  .giang-day-shell.collapsed .dot {
    display: block;
  }

  .giang-day-shell.collapsed .collapse-btn {
    justify-content: flex-start;
    padding-inline: 10px;
  }

  .giang-day-shell.collapsed .collapse-icon {
    background: #ffffff;
    color: var(--primary);
  }

  .module-content {
    min-height: auto;
    padding: 14px;
  }
}

@media (max-width: 640px) {
  .module-sidebar {
    padding: 10px;
  }

  .module-content {
    padding: 12px;
  }

  .section-head {
    min-height: 38px;
  }

  .menu-link {
    min-height: 32px;
  }
}
</style>
