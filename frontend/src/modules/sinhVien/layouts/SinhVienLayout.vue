<template>
  <section :class="['sinh-vien-shell', { collapsed: sidebarCollapsed }]">
    <aside class="module-sidebar">
      <button
          type="button"
          class="sidebar-toggle-handle"
          :title="sidebarCollapsed ? 'Mở rộng menu' : 'Thu gọn menu'"
          @click="sidebarCollapsed = !sidebarCollapsed"
      >
        <svg viewBox="0 0 24 24" aria-hidden="true">
          <path
              v-if="sidebarCollapsed"
              d="M9.29 6.71a1 1 0 0 1 1.42 0L16 12l-5.29 5.29a1 1 0 0 1-1.42-1.42L13.17 12 9.29 8.12a1 1 0 0 1 0-1.41Z"
          />
          <path
              v-else
              d="M14.71 6.71a1 1 0 0 0-1.42 0L8 12l5.29 5.29a1 1 0 0 0 1.42-1.42L10.83 12l3.88-3.88a1 1 0 0 0 0-1.41Z"
          />
        </svg>
      </button>

      <div class="sidebar-brand">
        <div class="brand-icon">SV</div>

        <div class="brand-text">
          <strong>Quản lý sinh viên</strong>
          <span>Hồ sơ & lớp học phần</span>
        </div>
      </div>

      <nav class="accordion-menu">
        <div class="menu-section">
          <button type="button" class="section-head" @click="toggleGroup('hoSo')">
            <span class="head-left">
              <span class="nav-icon">
                <svg viewBox="0 0 24 24" aria-hidden="true">
                  <path d="M4 5.75A1.75 1.75 0 0 1 5.75 4h12.5A1.75 1.75 0 0 1 20 5.75v12.5A1.75 1.75 0 0 1 18.25 20H5.75A1.75 1.75 0 0 1 4 18.25V5.75Zm3 1.5v3.5h3.5v-3.5H7Zm5.5 0v3.5H17v-3.5h-4.5ZM7 12.75v4h3.5v-4H7Zm5.5 0v4H17v-4h-4.5Z" />
                </svg>
              </span>

              <span class="nav-text">Hồ sơ & lớp học phần</span>
            </span>

            <span class="chevron" :class="{ open: openGroups.hoSo }">
              <svg viewBox="0 0 24 24" aria-hidden="true">
                <path d="M7.41 8.59 12 13.17l4.59-4.58L18 10l-6 6-6-6 1.41-1.41Z" />
              </svg>
            </span>
          </button>

          <Transition name="menu-slide">
            <div v-show="openGroups.hoSo" class="section-body">
              <RouterLink
                  :to="{ name: 'AdminSinhVienFlow' }"
                  active-class=""
                  exact-active-class=""
                  :class="[
                  'menu-link',
                  {
                    active: isActive([
                      'AdminSinhVienFlow'
                    ])
                  }
                ]"
              >
                <span class="dot"></span>
                <span class="nav-text">Quản lý hồ sơ SV & LHP</span>
              </RouterLink>

              <div v-show="tabActive === 'hoSo'" class="menu-desc">
                Tạo lớp hành chính, tiếp nhận sinh viên, mở lớp học phần và gán sinh viên vào lớp học phần.
              </div>
            </div>
          </Transition>
        </div>

        <div class="menu-section">
          <button type="button" class="section-head" @click="toggleGroup('danhSach')">
            <span class="head-left">
              <span class="nav-icon">
                <svg viewBox="0 0 24 24" aria-hidden="true">
                  <path d="M7 7.5A2.5 2.5 0 1 1 12 7.5 2.5 2.5 0 0 1 7 7.5Zm8-1.75h5v1.5h-5v-1.5Zm0 4h5v1.5h-5v-1.5ZM4 16.5c0-2.2 2.7-4 5.5-4s5.5 1.8 5.5 4V18H4v-1.5Zm12-.5h4v1.5h-4V16Z" />
                </svg>
              </span>

              <span class="nav-text">Danh sách sinh viên</span>
            </span>

            <span class="chevron" :class="{ open: openGroups.danhSach }">
              <svg viewBox="0 0 24 24" aria-hidden="true">
                <path d="M7.41 8.59 12 13.17l4.59-4.58L18 10l-6 6-6-6 1.41-1.41Z" />
              </svg>
            </span>
          </button>

          <Transition name="menu-slide">
            <div v-show="openGroups.danhSach" class="section-body">
              <RouterLink
                  :to="{ name: 'AdminSinhVienDanhSach' }"
                  active-class=""
                  exact-active-class=""
                  :class="[
                  'menu-link',
                  {
                    active: isActive([
                      'AdminSinhVienDanhSach',
                      'AdminSinhVienChiTiet',
                      'AdminSinhVienChiTietDiem'
                    ])
                  }
                ]"
              >
                <span class="dot"></span>
                <span class="nav-text">Sinh viên toàn trường</span>
              </RouterLink>

              <div v-show="tabActive === 'toanTruong'" class="menu-desc">
                Xem toàn bộ sinh viên, thông tin chi tiết và điểm học tập của sinh viên.
              </div>
            </div>
          </Transition>
        </div>

        <div class="menu-section">
          <button type="button" class="section-head" @click="toggleGroup('trangThai')">
            <span class="head-left">
              <span class="nav-icon">
                <svg viewBox="0 0 24 24" aria-hidden="true">
                  <path d="M5 4h14a1 1 0 0 1 1 1v14a1 1 0 0 1-1 1H5a1 1 0 0 1-1-1V5a1 1 0 0 1 1-1Zm2 3v2h10V7H7Zm0 4v2h10v-2H7Zm0 4v2h7v-2H7Z" />
                </svg>
              </span>

              <span class="nav-text">Trạng thái sinh viên</span>
            </span>

            <span class="chevron" :class="{ open: openGroups.trangThai }">
              <svg viewBox="0 0 24 24" aria-hidden="true">
                <path d="M7.41 8.59 12 13.17l4.59-4.58L18 10l-6 6-6-6 1.41-1.41Z" />
              </svg>
            </span>
          </button>

          <Transition name="menu-slide">
            <div v-show="openGroups.trangThai" class="section-body">
              <RouterLink
                  :to="{ name: 'AdminSinhVienBaoLuu' }"
                  active-class=""
                  exact-active-class=""
                  :class="[
                  'menu-link',
                  {
                    active: isActive([
                      'AdminSinhVienBaoLuu'
                    ])
                  }
                ]"
              >
                <span class="dot"></span>
                <span class="nav-text">Sinh viên bảo lưu</span>
              </RouterLink>

              <div v-show="tabActive === 'baoLuu'" class="menu-desc">
                Quản lý sinh viên đang tạm dừng hoặc bảo lưu kết quả học tập.
              </div>
            </div>
          </Transition>
        </div>

        <div class="menu-section">
          <button type="button" class="section-head" @click="toggleGroup('goiY')">
            <span class="head-left">
              <span class="nav-icon">
                <svg viewBox="0 0 24 24" aria-hidden="true">
                  <path d="M12 2a7 7 0 0 0-4 12.74V17a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1v-2.26A7 7 0 0 0 12 2Zm3 18H9v2h6v-2Z" />
                </svg>
              </span>

              <span class="nav-text">Gợi ý nghiệp vụ</span>
            </span>

            <span class="chevron" :class="{ open: openGroups.goiY }">
              <svg viewBox="0 0 24 24" aria-hidden="true">
                <path d="M7.41 8.59 12 13.17l4.59-4.58L18 10l-6 6-6-6 1.41-1.41Z" />
              </svg>
            </span>
          </button>

          <Transition name="menu-slide">
            <div v-show="openGroups.goiY" class="section-body tips-body">
              <div class="tip-item">
                <span class="tip-dot">1</span>
                <span>Chọn ngành trước khi chọn chương trình.</span>
              </div>

              <div class="tip-item">
                <span class="tip-dot">2</span>
                <span>Sau khi tạo lớp có thể tiếp nhận sinh viên.</span>
              </div>

              <div class="tip-item">
                <span class="tip-dot">3</span>
                <span>Mở lớp học phần rồi gán sinh viên vào lớp học phần.</span>
              </div>

              <div class="tip-item">
                <span class="tip-dot">4</span>
                <span>Sau khi gán sinh viên xong mới chuyển sang module giảng dạy.</span>
              </div>
            </div>
          </Transition>
        </div>
      </nav>
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
  hoSo: true,
  danhSach: true,
  trangThai: true,
  goiY: true
})

const tabActive = computed(() => {
  const n = route.name

  if (n === 'AdminSinhVienBaoLuu') {
    return 'baoLuu'
  }

  if ([
    'AdminSinhVienDanhSach',
    'AdminSinhVienChiTiet',
    'AdminSinhVienChiTietDiem'
  ].includes(n)) {
    return 'toanTruong'
  }

  return 'hoSo'
})

function toggleGroup(name) {
  openGroups[name] = !openGroups[name]
}

function isActive(names) {
  return names.includes(route.name)
}
</script>

<style scoped>
.sinh-vien-shell {
  --primary: #077149;
  --primary-dark: #055f3d;
  --primary-light: #0b8d5d;
  --primary-soft: #e7f6ef;
  --primary-soft-2: #f3fbf7;

  --text-900: #101828;
  --text-800: #1d2939;
  --text-700: #344054;
  --text-600: #475467;
  --text-500: #667085;
  --text-400: #98a2b3;

  --line: #e4e7ec;
  --line-soft: #edf1f5;
  --page-bg: #f6f8fa;

  min-height: 100vh;
  display: grid;
  grid-template-columns: 268px minmax(0, 1fr);
  margin: -20px;
  background: var(--page-bg);
  color: var(--text-800);
  font-family: "Roboto", system-ui, -apple-system, BlinkMacSystemFont, "Segoe UI", sans-serif;
}

.sinh-vien-shell,
.sinh-vien-shell * {
  box-sizing: border-box;
}

.module-sidebar {
  position: sticky;
  top: 0;
  align-self: start;
  height: 100vh;
  display: flex;
  flex-direction: column;
  border-right: 1px solid rgba(7, 113, 73, 0.14);
  background: #fbfdfc;
  padding: 14px 10px;
  overflow: visible;
  box-shadow: 8px 0 24px rgba(16, 24, 40, 0.035);
  z-index: 20;
}

.sidebar-toggle-handle {
  position: absolute;
  top: 50%;
  right: -14px;
  width: 28px;
  height: 44px;
  transform: translateY(-50%);
  border: 1px solid rgba(7, 113, 73, 0.18);
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.96);
  color: var(--primary);
  display: grid;
  place-items: center;
  cursor: pointer;
  box-shadow:
      0 12px 26px rgba(16, 24, 40, 0.09),
      0 4px 12px rgba(7, 113, 73, 0.1);
  backdrop-filter: blur(10px);
  transition:
      background-color 0.18s ease,
      color 0.18s ease,
      border-color 0.18s ease,
      box-shadow 0.18s ease,
      transform 0.18s ease;
  z-index: 50;
}

.sidebar-toggle-handle svg {
  width: 16px;
  height: 16px;
  fill: currentColor;
  transition: transform 0.18s ease;
}

.sidebar-toggle-handle:hover {
  background: var(--primary);
  color: #ffffff;
  border-color: var(--primary);
  box-shadow:
      0 16px 32px rgba(7, 113, 73, 0.24),
      0 4px 12px rgba(16, 24, 40, 0.1);
  transform: translateY(-50%) translateX(1px);
}

.sidebar-toggle-handle:hover svg {
  transform: scale(1.06);
}

.sidebar-toggle-handle:active {
  transform: translateY(-50%) scale(0.94);
}

.sidebar-brand {
  min-width: 0;
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 0 8px 14px;
  margin-bottom: 10px;
  border-bottom: 1px solid var(--line-soft);
}

.brand-icon {
  width: 38px;
  height: 38px;
  border-radius: 13px;
  display: grid;
  place-items: center;
  background: var(--primary);
  color: #ffffff;
  font-size: 13px;
  font-weight: 900;
  letter-spacing: 0.3px;
  box-shadow: 0 10px 22px rgba(7, 113, 73, 0.22);
  flex: 0 0 auto;
}

.brand-text {
  display: grid;
  gap: 2px;
  min-width: 0;
}

.brand-text strong {
  color: var(--text-900);
  font-size: 14px;
  font-weight: 800;
  line-height: 1.2;
  white-space: nowrap;
}

.brand-text span {
  color: var(--text-500);
  font-size: 12px;
  font-weight: 500;
  line-height: 1.25;
  white-space: nowrap;
}

.accordion-menu {
  min-height: 0;
  display: grid;
  align-content: start;
  gap: 6px;
  overflow: auto;
  overflow-x: hidden;
  padding: 0 2px 10px 0;
  scrollbar-width: thin;
  scrollbar-color: rgba(7, 113, 73, 0.24) transparent;
}

.accordion-menu::-webkit-scrollbar {
  width: 5px;
}

.accordion-menu::-webkit-scrollbar-thumb {
  background: rgba(7, 113, 73, 0.22);
  border-radius: 999px;
}

.accordion-menu::-webkit-scrollbar-track {
  background: transparent;
}

.menu-section {
  position: relative;
  border-radius: 12px;
}

.section-head {
  width: 100%;
  min-height: 38px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 10px;
  border: 0;
  border-radius: 11px;
  background: transparent;
  color: var(--text-700);
  padding: 8px 10px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 800;
  text-align: left;
  transition:
      background-color 0.16s ease,
      color 0.16s ease;
}

.section-head:hover {
  background: var(--primary-soft-2);
  color: var(--primary);
}

.head-left {
  display: inline-flex;
  align-items: center;
  gap: 9px;
  min-width: 0;
}

.nav-icon {
  width: 22px;
  height: 22px;
  display: inline-grid;
  place-items: center;
  color: var(--text-500);
  flex: 0 0 auto;
  transition:
      color 0.16s ease,
      background-color 0.16s ease;
}

.nav-icon svg {
  width: 16px;
  height: 16px;
  fill: currentColor;
}

.section-head:hover .nav-icon {
  color: var(--primary);
}

.nav-text {
  min-width: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.chevron {
  width: 22px;
  height: 22px;
  display: inline-grid;
  place-items: center;
  color: #7b8a9b;
  border-radius: 999px;
  flex: 0 0 auto;
  transition:
      transform 0.18s ease,
      color 0.16s ease,
      background-color 0.16s ease;
}

.chevron svg {
  width: 15px;
  height: 15px;
  fill: currentColor;
}

.chevron.open {
  transform: rotate(180deg);
  color: var(--primary);
  background: var(--primary-soft);
}

.section-body {
  position: relative;
  display: grid;
  gap: 3px;
  padding: 2px 0 7px 34px;
}

.section-body::before {
  content: "";
  position: absolute;
  left: 21px;
  top: 7px;
  bottom: 10px;
  width: 1px;
  background: rgba(7, 113, 73, 0.18);
}

.menu-link {
  position: relative;
  min-height: 34px;
  display: flex;
  align-items: center;
  gap: 8px;
  border: 0;
  border-radius: 10px;
  background: transparent;
  color: var(--text-600);
  padding: 7px 10px;
  text-decoration: none;
  cursor: pointer;
  font-size: 13px;
  font-weight: 700;
  text-align: left;
  transition:
      background-color 0.16s ease,
      color 0.16s ease,
      box-shadow 0.16s ease;
}

.menu-link:hover {
  background: #ffffff;
  color: var(--primary);
  box-shadow: 0 6px 16px rgba(16, 24, 40, 0.045);
}

.menu-link.active {
  background: var(--primary-soft);
  color: var(--primary-dark);
  box-shadow: inset 3px 0 0 var(--primary);
}

.menu-link.active .dot {
  background: var(--primary);
}

.dot {
  width: 6px;
  height: 6px;
  border-radius: 999px;
  background: #cbd5e1;
  flex: 0 0 auto;
  transition: background-color 0.16s ease;
}

.menu-desc {
  margin: 3px 8px 6px 19px;
  border-left: 2px solid rgba(7, 113, 73, 0.2);
  padding: 5px 0 5px 9px;
  color: var(--text-500);
  font-size: 12px;
  font-weight: 500;
  line-height: 1.45;
}

.tips-body {
  gap: 6px;
  padding-top: 5px;
  padding-bottom: 8px;
}

.tip-item {
  display: flex;
  align-items: flex-start;
  gap: 8px;
  color: var(--text-600);
  font-size: 12px;
  font-weight: 500;
  line-height: 1.42;
  padding: 6px 8px;
  border-radius: 10px;
  background: #ffffff;
  border: 1px solid rgba(7, 113, 73, 0.08);
}

.tip-dot {
  width: 20px;
  height: 20px;
  border-radius: 999px;
  display: grid;
  place-items: center;
  color: #ffffff;
  background: var(--primary);
  font-size: 11px;
  font-weight: 850;
  flex: 0 0 auto;
}

.module-main {
  min-width: 0;
  display: flex;
  flex-direction: column;
}

.module-content {
  min-width: 0;
  min-height: 100vh;
  padding: 18px;
  background: transparent;
}

.menu-slide-enter-active,
.menu-slide-leave-active {
  transition:
      opacity 0.16s ease,
      transform 0.16s ease,
      max-height 0.18s ease;
  overflow: hidden;
}

.menu-slide-enter-from,
.menu-slide-leave-to {
  opacity: 0;
  transform: translateY(-3px);
  max-height: 0;
}

.menu-slide-enter-to,
.menu-slide-leave-from {
  opacity: 1;
  transform: translateY(0);
  max-height: 240px;
}

.sinh-vien-shell.collapsed {
  grid-template-columns: 74px minmax(0, 1fr);
}

.sinh-vien-shell.collapsed .module-sidebar {
  padding-inline: 10px;
}

.sinh-vien-shell.collapsed .sidebar-toggle-handle {
  right: -14px;
}

.sinh-vien-shell.collapsed .sidebar-brand {
  justify-content: center;
  padding-inline: 0;
}

.sinh-vien-shell.collapsed .brand-icon {
  width: 38px;
  height: 38px;
  border-radius: 13px;
}

.sinh-vien-shell.collapsed .brand-text,
.sinh-vien-shell.collapsed .nav-text,
.sinh-vien-shell.collapsed .chevron,
.sinh-vien-shell.collapsed .menu-desc,
.sinh-vien-shell.collapsed .tips-body {
  display: none;
}

.sinh-vien-shell.collapsed .section-head {
  min-height: 42px;
  justify-content: center;
  padding-inline: 0;
}

.sinh-vien-shell.collapsed .head-left {
  justify-content: center;
}

.sinh-vien-shell.collapsed .nav-icon {
  width: 34px;
  height: 34px;
  border-radius: 10px;
}

.sinh-vien-shell.collapsed .section-head:hover .nav-icon {
  background: var(--primary-soft);
  color: var(--primary);
}

.sinh-vien-shell.collapsed .section-body {
  padding-left: 0;
}

.sinh-vien-shell.collapsed .section-body::before {
  display: none;
}

.sinh-vien-shell.collapsed .menu-link {
  justify-content: center;
  min-height: 34px;
  padding-inline: 0;
  box-shadow: none;
  background: transparent;
}

.sinh-vien-shell.collapsed .menu-link.active {
  box-shadow: none;
  background: transparent;
}

.sinh-vien-shell.collapsed .dot {
  width: 7px;
  height: 7px;
}

@media (max-width: 860px) {
  .sinh-vien-shell,
  .sinh-vien-shell.collapsed {
    grid-template-columns: 1fr;
    margin: 0;
  }

  .module-sidebar {
    position: relative;
    height: auto;
    border-right: 0;
    border-bottom: 1px solid rgba(7, 113, 73, 0.14);
    padding-bottom: 58px;
  }

  .sidebar-toggle-handle,
  .sinh-vien-shell.collapsed .sidebar-toggle-handle {
    top: auto;
    right: 14px;
    bottom: 12px;
    width: 38px;
    height: 38px;
    transform: none;
    border-radius: 12px;
  }

  .sidebar-toggle-handle:hover,
  .sinh-vien-shell.collapsed .sidebar-toggle-handle:hover {
    transform: translateY(-1px);
  }

  .sidebar-toggle-handle:active,
  .sinh-vien-shell.collapsed .sidebar-toggle-handle:active {
    transform: scale(0.96);
  }

  .sinh-vien-shell.collapsed .brand-text,
  .sinh-vien-shell.collapsed .nav-text,
  .sinh-vien-shell.collapsed .chevron {
    display: inline;
  }

  .sinh-vien-shell.collapsed .menu-desc,
  .sinh-vien-shell.collapsed .tips-body {
    display: grid;
  }

  .sinh-vien-shell.collapsed .sidebar-brand {
    justify-content: flex-start;
  }

  .sinh-vien-shell.collapsed .section-head {
    justify-content: space-between;
    padding-inline: 10px;
  }

  .sinh-vien-shell.collapsed .head-left {
    justify-content: flex-start;
  }

  .sinh-vien-shell.collapsed .nav-icon {
    width: 22px;
    height: 22px;
  }

  .sinh-vien-shell.collapsed .section-body {
    padding-left: 34px;
  }

  .sinh-vien-shell.collapsed .section-body::before {
    display: block;
  }

  .sinh-vien-shell.collapsed .menu-link {
    justify-content: flex-start;
    padding-inline: 10px;
  }

  .sinh-vien-shell.collapsed .dot {
    display: block;
  }
}

@media (max-width: 640px) {
  .module-sidebar {
    padding: 12px 10px 58px;
  }

  .module-content {
    padding: 12px;
  }

  .brand-text strong {
    font-size: 14px;
  }

  .section-head {
    min-height: 38px;
  }

  .menu-link {
    font-size: 13px;
  }
}
</style>