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
  /* Trừ đi khoảng 60px chiều cao của thanh Header màu xanh */
  /* (Nếu header của bạn cao hơn hoặc thấp hơn, hãy sửa số 60px này nhé) */
  min-height: calc(100vh - 60px);
  display: grid;
  grid-template-columns: 280px minmax(0, 1fr);
  margin: -20px;
  background: #f8fafc;
  color: #1f2937;
  /* Thêm transition để trượt mượt mà */
  transition: grid-template-columns 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.module-sidebar {
  position: sticky;
  /* Nếu thanh Header của bạn đang dùng position: fixed, hãy đổi top thành top: 60px (bằng chiều cao header) */
  top: 0;
  align-self: start;
  /* Chiều cao của sidebar cũng phải trừ hao Header để phần đáy không bị đẩy ra ngoài màn hình */
  height: calc(100vh - 60px);
  display: flex;
  flex-direction: column;
  border-right: 1px solid #e5e7eb;
  background: #ffffff;
  padding: 16px 12px;
  /* Ngăn chặn thanh cuộn ngang khi khung đang thu hẹp */
  overflow-x: hidden;
  box-sizing: border-box; /* Thêm dòng này để 100vh không bị cộng dồn với padding làm lố màn hình */
  /* Đã bỏ overflow-y: auto ở đây để nút luôn cố định */
}

.sidebar-brand {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 4px 8px 18px;
  border-bottom: 1px solid #eef2f7;
  margin-bottom: 12px;
  white-space: nowrap; /* Chống rớt dòng text */
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
  flex-shrink: 0; /* Đảm bảo icon không bị bóp méo khi thu gọn */
}

.brand-text {
  display: grid;
  gap: 2px;
  min-width: 0;
  transition: opacity 0.2s ease;
  opacity: 1;
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
  overflow-x: hidden;
  overflow-y: auto;
  padding-right: 2px;
  flex: 1; /* Thêm dòng này để vùng menu tự động chiếm hết khoảng trống ở giữa, ép nút Thu gọn xuống đáy */
  align-content: start; /* THÊM DÒNG NÀY: Ép các menu dồn lên trên cùng, không bị giãn xa nhau nữa */
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
  white-space: nowrap;
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

.nav-text {
  transition: opacity 0.2s ease;
  opacity: 1;
}

.chevron {
  color: #98a2b3;
  font-size: 14px;
  flex: 0 0 auto;
  transition: opacity 0.2s ease;
  opacity: 1;
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
  white-space: nowrap;
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
  white-space: nowrap;
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

/* ================= TRẠNG THÁI COLLAPSED ================= */
.he-thong-shell.collapsed {
  grid-template-columns: 76px minmax(0, 1fr);
}

.he-thong-shell.collapsed .module-sidebar {
  padding-inline: 10px;
}

/* Thay display: none bằng opacity/visibility để tránh vỡ layout và mượt hơn */
.he-thong-shell.collapsed .brand-text,
.he-thong-shell.collapsed .nav-text,
.he-thong-shell.collapsed .chevron {
  opacity: 0;
  width: 0;
  visibility: hidden;
}

.he-thong-shell.collapsed .sidebar-brand {
  justify-content: center;
  padding-inline: 0;
}

.he-thong-shell.collapsed .section-head {
  justify-content: center;
  padding-inline: 0;
}

.he-thong-shell.collapsed .head-left {
  justify-content: center;
}

/* ĐIỂM QUAN TRỌNG: Ép ẩn toàn bộ menu con bằng CSS, không cần đụng tới biến JS */
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
    height: auto;
    border-right: 0;
    border-bottom: 1px solid #e5e7eb;
  }

  .he-thong-shell.collapsed .brand-text,
  .he-thong-shell.collapsed .nav-text,
  .he-thong-shell.collapsed .chevron {
    opacity: 1;
    width: auto;
    visibility: visible;
  }

  .he-thong-shell.collapsed .section-body {
    display: grid !important; /* Trả lại menu con trên mobile */
    padding-left: 32px;
  }

  .he-thong-shell.collapsed .menu-link {
    justify-content: flex-start;
    padding-inline: 10px;
  }

  .he-thong-shell.collapsed .dot {
    display: block;
  }
}

@media (max-width: 640px) {
  .module-content {
    padding: 12px;
  }
}
</style>