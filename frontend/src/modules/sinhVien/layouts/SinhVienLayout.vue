<template>
  <section :class="['sinh-vien-shell', { collapsed: sidebarCollapsed }]">
    <aside class="module-sidebar">
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
              <span class="nav-icon">▦</span>
              <span class="nav-text">Hồ sơ & lớp học phần</span>
            </span>

            <span class="chevron">{{ openGroups.hoSo ? '⌃' : '⌄' }}</span>
          </button>

          <div v-show="openGroups.hoSo" class="section-body">
            <RouterLink
                :to="{ name: 'AdminSinhVienFlow' }"
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
        </div>

        <div class="menu-section">
          <button type="button" class="section-head" @click="toggleGroup('danhSach')">
            <span class="head-left">
              <span class="nav-icon">□</span>
              <span class="nav-text">Danh sách sinh viên</span>
            </span>

            <span class="chevron">{{ openGroups.danhSach ? '⌃' : '⌄' }}</span>
          </button>

          <div v-show="openGroups.danhSach" class="section-body">
            <RouterLink
                :to="{ name: 'AdminSinhVienDanhSach' }"
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
        </div>

        <div class="menu-section">
          <button type="button" class="section-head" @click="toggleGroup('trangThai')">
            <span class="head-left">
              <span class="nav-icon">▤</span>
              <span class="nav-text">Trạng thái sinh viên</span>
            </span>

            <span class="chevron">{{ openGroups.trangThai ? '⌃' : '⌄' }}</span>
          </button>

          <div v-show="openGroups.trangThai" class="section-body">
            <RouterLink
                :to="{ name: 'AdminSinhVienBaoLuu' }"
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
        </div>

        <div class="menu-section">
          <button type="button" class="section-head" @click="toggleGroup('goiY')">
            <span class="head-left">
              <span class="nav-icon">⌁</span>
              <span class="nav-text">Gợi ý nghiệp vụ</span>
            </span>

            <span class="chevron">{{ openGroups.goiY ? '⌃' : '⌄' }}</span>
          </button>

          <div v-show="openGroups.goiY" class="section-body tips-body">
            <div class="tip-item">
              <span class="tip-dot blue">1</span>
              <span>Chọn ngành trước khi chọn chương trình.</span>
            </div>

            <div class="tip-item">
              <span class="tip-dot green">2</span>
              <span>Sau khi tạo lớp có thể tiếp nhận sinh viên.</span>
            </div>

            <div class="tip-item">
              <span class="tip-dot orange">3</span>
              <span>Mở lớp học phần rồi gán sinh viên vào lớp học phần.</span>
            </div>

            <div class="tip-item">
              <span class="tip-dot purple">4</span>
              <span>Sau khi gán sinh viên xong mới chuyển sang module giảng dạy.</span>
            </div>
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
  min-height: 100vh;
  display: grid;
  grid-template-columns: 280px minmax(0, 1fr);
  margin: -20px;
  background: #f8fafc;
  color: #1f2937;
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

.dot {
  width: 7px;
  height: 7px;
  border-radius: 999px;
  background: #cbd5e1;
  flex: 0 0 auto;
}

.menu-desc {
  margin: 0 8px 6px 19px;
  border-left: 2px solid #dbeafe;
  padding: 6px 0 6px 10px;
  color: #667085;
  font-size: 12px;
  line-height: 1.45;
}

.tips-body {
  gap: 10px;
  padding-top: 8px;
  padding-bottom: 8px;
}

.tip-item {
  display: flex;
  align-items: flex-start;
  gap: 10px;
  color: #475467;
  font-size: 13px;
  line-height: 1.4;
}

.tip-dot {
  width: 24px;
  height: 24px;
  border-radius: 999px;
  display: grid;
  place-items: center;
  color: #ffffff;
  font-size: 12px;
  font-weight: 800;
  flex: 0 0 auto;
}

.tip-dot.blue {
  background: #3b82f6;
}

.tip-dot.green {
  background: #10b981;
}

.tip-dot.orange {
  background: #f59e0b;
}

.tip-dot.purple {
  background: #7c3aed;
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

.sinh-vien-shell.collapsed {
  grid-template-columns: 76px minmax(0, 1fr);
}

.sinh-vien-shell.collapsed .module-sidebar {
  padding-inline: 10px;
}

.sinh-vien-shell.collapsed .brand-text,
.sinh-vien-shell.collapsed .nav-text,
.sinh-vien-shell.collapsed .chevron,
.sinh-vien-shell.collapsed .menu-desc,
.sinh-vien-shell.collapsed .tips-body {
  display: none;
}

.sinh-vien-shell.collapsed .sidebar-brand {
  justify-content: center;
  padding-inline: 0;
}

.sinh-vien-shell.collapsed .section-head {
  justify-content: center;
  padding-inline: 0;
}

.sinh-vien-shell.collapsed .head-left {
  justify-content: center;
}

.sinh-vien-shell.collapsed .section-body {
  padding-left: 0;
}

.sinh-vien-shell.collapsed .menu-link {
  justify-content: center;
  padding-inline: 0;
}

.sinh-vien-shell.collapsed .dot {
  display: none;
}

.sinh-vien-shell.collapsed .collapse-btn {
  justify-content: center;
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
    border-bottom: 1px solid #e5e7eb;
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

  .sinh-vien-shell.collapsed .section-body {
    padding-left: 32px;
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
  .module-content {
    padding: 12px;
  }
}
</style>