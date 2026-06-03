<template>
  <section :class="['lms-shell', { collapsed: sidebarCollapsed }]">
    <aside class="module-sidebar">
      <div class="sidebar-brand">
        <div class="brand-icon">LMS</div>

        <div class="brand-text">
          <strong>Quản lý LMS</strong>
          <span>Câu hỏi, bài tập & giao bài</span>
        </div>
      </div>

      <nav class="accordion-menu">
        <div class="menu-section">
          <button type="button" class="section-head" @click="toggleGroup('tongQuan')">
            <span class="head-left">
              <span class="nav-icon">▦</span>
              <span class="nav-text">Tổng quan</span>
            </span>

            <span class="chevron">{{ openGroups.tongQuan ? '⌃' : '⌄' }}</span>
          </button>

          <div v-show="openGroups.tongQuan" class="section-body">
            <RouterLink
                :to="{ name: 'Lms.TongQuan' }"
                :class="['menu-link', { active: isActive(['Lms.TongQuan']) }]"
            >
              <span class="dot"></span>
              <span class="nav-text">Dashboard LMS</span>
            </RouterLink>
          </div>
        </div>

        <div class="menu-section">
          <button type="button" class="section-head" @click="toggleGroup('cauHoi')">
            <span class="head-left">
              <span class="nav-icon">?</span>
              <span class="nav-text">Ngân hàng câu hỏi</span>
            </span>

            <span class="chevron">{{ openGroups.cauHoi ? '⌃' : '⌄' }}</span>
          </button>

          <div v-show="openGroups.cauHoi" class="section-body">
            <RouterLink
                :to="{ name: 'Lms.NganHangCauHoi' }"
                :class="['menu-link', { active: isActive(['Lms.NganHangCauHoi']) }]"
            >
              <span class="dot"></span>
              <span class="nav-text">Câu hỏi version</span>
            </RouterLink>

            <RouterLink
                :to="{ name: 'Lms.RaSoatCauHoi' }"
                :class="['menu-link', { active: isActive(['Lms.RaSoatCauHoi']) }]"
            >
              <span class="dot"></span>
              <span class="nav-text">Rà soát câu hỏi</span>
            </RouterLink>

            <RouterLink
                :to="{ name: 'Lms.MaTranDe' }"
                :class="['menu-link', { active: isActive(['Lms.MaTranDe']) }]"
            >
              <span class="dot"></span>
              <span class="nav-text">Ma trận đề</span>
            </RouterLink>
          </div>
        </div>

        <div class="menu-section">
          <button type="button" class="section-head" @click="toggleGroup('baiTap')">
            <span class="head-left">
              <span class="nav-icon">▤</span>
              <span class="nav-text">Bài tập online</span>
            </span>

            <span class="chevron">{{ openGroups.baiTap ? '⌃' : '⌄' }}</span>
          </button>

          <div v-show="openGroups.baiTap" class="section-body">
            <RouterLink
                :to="{ name: 'Lms.BaiTapOnline' }"
                :class="['menu-link', { active: isActive(['Lms.BaiTapOnline']) }]"
            >
              <span class="dot"></span>
              <span class="nav-text">Bài tập online</span>
            </RouterLink>

            <RouterLink
                :to="{ name: 'Lms.GiaoBai' }"
                :class="['menu-link', { active: isActive(['Lms.GiaoBai']) }]"
            >
              <span class="dot"></span>
              <span class="nav-text">Giao bài</span>
            </RouterLink>

            <RouterLink
                :to="{ name: 'Lms.BaiTapLop' }"
                :class="['menu-link', { active: isActive(['Lms.BaiTapLop']) }]"
            >
              <span class="dot"></span>
              <span class="nav-text">Bài tập lớp</span>
            </RouterLink>
          </div>
        </div>

        <div class="menu-section">
          <button type="button" class="section-head" @click="toggleGroup('sinhVien')">
            <span class="head-left">
              <span class="nav-icon">♙</span>
              <span class="nav-text">Sinh viên</span>
            </span>

            <span class="chevron">{{ openGroups.sinhVien ? '⌃' : '⌄' }}</span>
          </button>

          <div v-show="openGroups.sinhVien" class="section-body">
            <RouterLink
                :to="{ name: 'Lms.SinhVienBaiDuocGiao' }"
                :class="['menu-link', { active: isActive(['Lms.SinhVienBaiDuocGiao']) }]"
            >
              <span class="dot"></span>
              <span class="nav-text">Bài được giao</span>
            </RouterLink>

            <RouterLink
                :to="{ name: 'Lms.LichSu' }"
                :class="['menu-link', { active: isActive(['Lms.LichSu']) }]"
            >
              <span class="dot"></span>
              <span class="nav-text">Lịch sử làm bài</span>
            </RouterLink>
          </div>
        </div>

        <div class="menu-section">
          <button type="button" class="section-head" @click="toggleGroup('duLieu')">
            <span class="head-left">
              <span class="nav-icon">□</span>
              <span class="nav-text">Dữ liệu hệ thống</span>
            </span>

            <span class="chevron">{{ openGroups.duLieu ? '⌃' : '⌄' }}</span>
          </button>

          <div v-show="openGroups.duLieu" class="section-body">
            <RouterLink
                :to="{ name: 'Lms.DuLieuApi' }"
                :class="['menu-link', { active: isActive(['Lms.DuLieuApi']) }]"
            >
              <span class="dot"></span>
              <span class="nav-text">API dữ liệu</span>
            </RouterLink>
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
import { reactive, ref } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()

const sidebarCollapsed = ref(false)

const openGroups = reactive({
  tongQuan: true,
  cauHoi: true,
  baiTap: true,
  sinhVien: true,
  duLieu: true
})

function toggleGroup(name) {
  openGroups[name] = !openGroups[name]
}

function isActive(names) {
  return names.includes(route.name)
}
</script>

<style scoped>
.lms-shell,
.lms-shell * {
  box-sizing: border-box;
  font-family: Roboto, Arial, sans-serif;
}

.lms-shell {
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
  font-size: 12px;
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
  font-weight: 800;
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

.dot {
  width: 7px;
  height: 7px;
  border-radius: 999px;
  background: #cbd5e1;
  flex: 0 0 auto;
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
  overflow-x: visible;
}

.module-content :deep(.lms-page) {
  width: 100%;
  max-width: none;
}

.module-content :deep(.table-card),
.module-content :deep(.card),
.module-content :deep(.filter-card) {
  max-width: none;
}

.module-content :deep(.table-card) {
  width: 100%;
  overflow-x: auto;
}

.module-content :deep(table) {
  width: 100%;
}

.lms-shell.collapsed {
  grid-template-columns: 76px minmax(0, 1fr);
}

.lms-shell.collapsed .module-sidebar {
  padding-inline: 10px;
}

.lms-shell.collapsed .brand-text,
.lms-shell.collapsed .nav-text,
.lms-shell.collapsed .chevron {
  display: none;
}

.lms-shell.collapsed .sidebar-brand {
  justify-content: center;
  padding-inline: 0;
}

.lms-shell.collapsed .section-head {
  justify-content: center;
  padding-inline: 0;
}

.lms-shell.collapsed .head-left {
  justify-content: center;
}

.lms-shell.collapsed .section-body {
  padding-left: 0;
}

.lms-shell.collapsed .menu-link {
  justify-content: center;
  padding-inline: 0;
}

.lms-shell.collapsed .dot {
  display: none;
}

.lms-shell.collapsed .collapse-btn {
  justify-content: center;
}

@media (max-width: 860px) {
  .lms-shell,
  .lms-shell.collapsed {
    grid-template-columns: 1fr;
    margin: 0;
  }

  .module-sidebar {
    position: relative;
    height: auto;
    border-right: 0;
    border-bottom: 1px solid #e5e7eb;
  }

  .lms-shell.collapsed .brand-text,
  .lms-shell.collapsed .nav-text,
  .lms-shell.collapsed .chevron {
    display: inline;
  }

  .lms-shell.collapsed .section-body {
    padding-left: 32px;
  }

  .lms-shell.collapsed .menu-link {
    justify-content: flex-start;
    padding-inline: 10px;
  }

  .lms-shell.collapsed .dot {
    display: block;
  }
}

@media (max-width: 640px) {
  .module-content {
    padding: 12px;
  }
}
</style>