<template>
  <section :class="['he-thong-shell', { collapsed: sidebarCollapsed }]">
    <aside class="module-sidebar">
      <div class="sidebar-brand">
        <div class="brand-icon">HT</div>

        <div class="brand-text">
          <strong>Quản lý hệ thống</strong>
          <span>Tài khoản & nhật ký</span>
        </div>
      </div>

      <nav class="accordion-menu">
        <div class="menu-section">
          <button type="button" class="section-head" @click="toggleGroup('taiKhoan')">
            <span class="head-left">
              <span class="nav-icon">▦</span>
              <span class="nav-text">Tài khoản</span>
            </span>

            <span class="chevron">{{ openGroups.taiKhoan ? '⌃' : '⌄' }}</span>
          </button>

          <div v-show="openGroups.taiKhoan" class="section-body">
            <RouterLink
                :to="{ name: 'HeThong.QuanLyTaiKhoan' }"
                :class="['menu-link', { active: isActive(['HeThong.QuanLyTaiKhoan']) }]"
            >
              <span class="dot"></span>
              <span class="nav-text">Quản lý tài khoản</span>
            </RouterLink>

            <RouterLink
                :to="{ name: 'HeThong.CapNhatTaiKhoan' }"
                :class="['menu-link', { active: isActive(['HeThong.CapNhatTaiKhoan']) }]"
            >
              <span class="dot"></span>
              <span class="nav-text">Cập nhật tài khoản</span>
            </RouterLink>

            <RouterLink
                :to="{ name: 'HeThong.DoiMatKhauTaiKhoan' }"
                :class="['menu-link', { active: isActive(['HeThong.DoiMatKhauTaiKhoan']) }]"
            >
              <span class="dot"></span>
              <span class="nav-text">Đổi mật khẩu</span>
            </RouterLink>
          </div>
        </div>

        <div class="menu-section">
          <button type="button" class="section-head" @click="toggleGroup('cauHinh')">
            <span class="head-left">
              <span class="nav-icon">▤</span>
              <span class="nav-text">Cấu hình hệ thống</span>
            </span>

            <span class="chevron">{{ openGroups.cauHinh ? '⌃' : '⌄' }}</span>
          </button>

          <div v-show="openGroups.cauHinh" class="section-body">
            <RouterLink
                :to="{ name: 'HeThong.Crudmau' }"
                :class="['menu-link', { active: isActive(['HeThong.Crudmau']) }]"
            >
              <span class="dot"></span>
              <span class="nav-text">Quản lý CRUD gốc</span>
            </RouterLink>
          </div>
        </div>

        <div class="menu-section">
          <button type="button" class="section-head" @click="toggleGroup('nhatKy')">
            <span class="head-left">
              <span class="nav-icon">⌁</span>
              <span class="nav-text">Nhật ký</span>
            </span>

            <span class="chevron">{{ openGroups.nhatKy ? '⌃' : '⌄' }}</span>
          </button>

          <div v-show="openGroups.nhatKy" class="section-body">
            <RouterLink
                :to="{ name: 'HeThong.NhatKyDangNhap' }"
                :class="['menu-link', { active: isActive(['HeThong.NhatKyDangNhap']) }]"
            >
              <span class="dot"></span>
              <span class="nav-text">Nhật ký đăng nhập</span>
            </RouterLink>

            <RouterLink
                :to="{ name: 'HeThong.NhatKyHeThong' }"
                :class="['menu-link', { active: isActive(['HeThong.NhatKyHeThong']) }]"
            >
              <span class="dot"></span>
              <span class="nav-text">Nhật ký hệ thống</span>
            </RouterLink>
          </div>
        </div>

        <div class="menu-section">
          <button type="button" class="section-head" @click="toggleGroup('tep')">
            <span class="head-left">
              <span class="nav-icon">□</span>
              <span class="nav-text">Dữ liệu & tệp</span>
            </span>

            <span class="chevron">{{ openGroups.tep ? '⌃' : '⌄' }}</span>
          </button>

          <div v-show="openGroups.tep" class="section-body">
            <RouterLink
                :to="{ name: 'HeThong.TepDinhKem' }"
                :class="['menu-link', { active: isActive(['HeThong.TepDinhKem']) }]"
            >
              <span class="dot"></span>
              <span class="nav-text">Quản lý tệp đính kèm</span>
            </RouterLink>
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
import { reactive, ref } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()

const sidebarCollapsed = ref(false)

const openGroups = reactive({
  taiKhoan: true,
  cauHinh: true,
  nhatKy: true,
  tep: true
})

function toggleGroup(name) {
  openGroups[name] = !openGroups[name]
}

function isActive(names) {
  return names.includes(route.name)
}
</script>

<style scoped>
.he-thong-shell {
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
  border-radius: 10px;
  background: transparent;
  color: var(--text-soft);
  padding: 7px 10px;
  text-decoration: none;
  cursor: pointer;
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

.dot {
  width: 6px;
  height: 6px;
  flex: 0 0 auto;
  border-radius: 999px;
  background: #cbd5e1;
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

/* ================= TRẠNG THÁI COLLAPSED ================= */

.he-thong-shell.collapsed {
  grid-template-columns: 72px minmax(0, 1fr);
}

.he-thong-shell.collapsed .module-sidebar {
  padding-inline: 9px;
}

.he-thong-shell.collapsed .sidebar-brand {
  justify-content: center;
  padding-inline: 0;
}

.he-thong-shell.collapsed .brand-text,
.he-thong-shell.collapsed .nav-text,
.he-thong-shell.collapsed .chevron {
  width: 0;
  opacity: 0;
  visibility: hidden;
}

.he-thong-shell.collapsed .section-head {
  justify-content: center;
  padding-inline: 0;
}

.he-thong-shell.collapsed .head-left {
  justify-content: center;
  gap: 0;
}

.he-thong-shell.collapsed .section-body {
  display: none !important;
}

.he-thong-shell.collapsed .menu-link {
  justify-content: center;
  padding-inline: 0;
}

.he-thong-shell.collapsed .dot {
  display: none;
}

.he-thong-shell.collapsed .collapse-btn {
  justify-content: center;
  padding-inline: 0;
}

.he-thong-shell.collapsed .collapse-icon {
  background: var(--primary);
  color: #ffffff;
}

.he-thong-shell.collapsed .sidebar-toggle-float {
  right: -11px;
  background: #ffffff;
  color: var(--primary);
  border-color: var(--border);
}

.he-thong-shell.collapsed .sidebar-toggle-float:hover {
  background: var(--primary);
  color: #ffffff;
  border-color: var(--primary);
}

/* ================= RESPONSIVE ================= */

@media (max-width: 860px) {
  .he-thong-shell,
  .he-thong-shell.collapsed {
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

  .he-thong-shell.collapsed .brand-text,
  .he-thong-shell.collapsed .nav-text,
  .he-thong-shell.collapsed .chevron {
    width: auto;
    opacity: 1;
    visibility: visible;
  }

  .he-thong-shell.collapsed .section-head {
    justify-content: space-between;
    padding-inline: 10px;
  }

  .he-thong-shell.collapsed .head-left {
    justify-content: flex-start;
    gap: 9px;
  }

  .he-thong-shell.collapsed .section-body {
    display: grid !important;
    padding-left: 31px;
  }

  .he-thong-shell.collapsed .menu-link {
    justify-content: flex-start;
    padding-inline: 10px;
  }

  .he-thong-shell.collapsed .dot {
    display: block;
  }

  .he-thong-shell.collapsed .collapse-btn {
    justify-content: flex-start;
    padding-inline: 10px;
  }

  .he-thong-shell.collapsed .collapse-icon {
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