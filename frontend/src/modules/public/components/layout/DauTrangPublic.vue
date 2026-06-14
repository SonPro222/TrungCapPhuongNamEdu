<template>
  <header class="public-header">
    <div class="public-container header-inner">
      <RouterLink
          :to="{ name: 'Public.TrangChu' }"
          class="brand-link"
          @click="dongTatCaMenu"
      >
        <div class="brand-logo">
          <span>PN</span>
        </div>

        <div class="brand-text">
          <h1>{{ thongTinTruong.tenDong1 }}</h1>
          <h2>{{ thongTinTruong.tenDong2 }}</h2>
        </div>
      </RouterLink>

      <nav class="desktop-menu">
        <RouterLink :to="{ name: 'Public.TrangChu' }" class="menu-link" @click="dongTatCaMenu">
          Trang chủ
        </RouterLink>

        <RouterLink :to="{ name: 'Public.GioiThieu' }" class="menu-link" @click="dongTatCaMenu">
          Giới thiệu
        </RouterLink>

        <div class="menu-dropdown" @mouseenter="moDropdownDaoTao" @mouseleave="dongDropdownDaoTaoTre">
          <button type="button" class="menu-link menu-dropdown-button" @click="chuyenTrangNganhDaoTao">
            <span>Ngành đào tạo</span>
            <i class="fa-solid fa-chevron-down"></i>
          </button>

          <div v-if="moDropdownNganh" class="menu-dropdown-safe-zone"></div>

          <div
              v-if="moDropdownNganh"
              class="menu-dropdown-panel"
              @mouseenter="moDropdownDaoTao"
              @mouseleave="dongDropdownDaoTaoTre"
          >
            <RouterLink
                :to="{ name: 'Public.NganhDaoTao' }"
                class="menu-dropdown-title"
                @click="dongTatCaMenu"
            >
              Tất cả ngành đào tạo
            </RouterLink>

            <div class="menu-dropdown-divider"></div>

            <RouterLink
                v-for="item in danhSachNganhHienThi"
                :key="item.id"
                :to="{ name: 'Public.ChiTietNganhDaoTao', params: { id: item.id } }"
                class="menu-dropdown-item"
                @click="dongTatCaMenu"
            >
              <span class="menu-dropdown-text">
                <strong>{{ item.title }}</strong>
                <small>{{ item.category }}</small>
              </span>
            </RouterLink>
          </div>
        </div>

        <RouterLink :to="{ name: 'Public.TuyenSinh' }" class="menu-link" @click="dongTatCaMenu">
          Tuyển sinh
        </RouterLink>

        <RouterLink :to="{ name: 'Public.TinTuc' }" class="menu-link" @click="dongTatCaMenu">
          Tin tức
        </RouterLink>

        <RouterLink :to="{ name: 'Public.LienHe' }" class="menu-link" @click="dongTatCaMenu">
          Liên hệ
        </RouterLink>
      </nav>

      <div class="header-actions">
        <button class="icon-button" type="button" @click="dongTatCaMenu">
          <i class="fa-solid fa-magnifying-glass"></i>
        </button>

        <RouterLink :to="{ name: 'Public.DangNhap' }" class="login-button" @click="dongTatCaMenu">
          Đăng nhập
        </RouterLink>
      </div>

      <button class="mobile-menu-button" type="button" @click="moMenu = !moMenu">
        <i class="fa-solid fa-bars"></i>
      </button>
    </div>

    <nav v-if="moMenu" class="mobile-menu public-container">
      <RouterLink :to="{ name: 'Public.TrangChu' }" class="mobile-menu-link" @click="dongTatCaMenu">
        Trang chủ
      </RouterLink>

      <RouterLink :to="{ name: 'Public.GioiThieu' }" class="mobile-menu-link" @click="dongTatCaMenu">
        Giới thiệu
      </RouterLink>

      <button
          type="button"
          class="mobile-menu-link mobile-dropdown-button"
          @click="moMobileNganh = !moMobileNganh"
      >
        Ngành đào tạo
        <i class="fa-solid fa-chevron-down"></i>
      </button>

      <div v-if="moMobileNganh" class="mobile-dropdown-list">
        <RouterLink :to="{ name: 'Public.NganhDaoTao' }" class="mobile-dropdown-item" @click="dongTatCaMenu">
          Tất cả ngành đào tạo
        </RouterLink>

        <RouterLink
            v-for="item in danhSachNganhHienThi"
            :key="item.id"
            :to="{ name: 'Public.ChiTietNganhDaoTao', params: { id: item.id } }"
            class="mobile-dropdown-item"
            @click="dongTatCaMenu"
        >
          {{ item.title }}
        </RouterLink>
      </div>

      <RouterLink :to="{ name: 'Public.TuyenSinh' }" class="mobile-menu-link" @click="dongTatCaMenu">
        Tuyển sinh
      </RouterLink>

      <RouterLink :to="{ name: 'Public.TinTuc' }" class="mobile-menu-link" @click="dongTatCaMenu">
        Tin tức
      </RouterLink>

      <RouterLink :to="{ name: 'Public.LienHe' }" class="mobile-menu-link" @click="dongTatCaMenu">
        Liên hệ
      </RouterLink>

      <RouterLink :to="{ name: 'Public.DangNhap' }" class="mobile-menu-link" @click="dongTatCaMenu">
        Đăng nhập
      </RouterLink>
    </nav>
  </header>
</template>

<script setup>
import { computed, ref, watch, onBeforeUnmount } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useNoiDungPublic } from '@/modules/public/composables/useNoiDungPublic.js'

const route = useRoute()
const router = useRouter()

const {
  thongTinTruong: thongTinTruongRef,
  danhSachNganhDaoTao: danhSachNganhDaoTaoRef
} = useNoiDungPublic()

const thongTinTruong = computed(() => thongTinTruongRef.value)

const danhSachNganhHienThi = computed(() => {
  return danhSachNganhDaoTaoRef.value.filter((item) => item.trangThaiHienThi !== false)
})

const moMenu = ref(false)
const moDropdownNganh = ref(false)
const moMobileNganh = ref(false)

let boDemDongDropdown = null

const xoaBoDemDongDropdown = () => {
  if (boDemDongDropdown) {
    clearTimeout(boDemDongDropdown)
    boDemDongDropdown = null
  }
}

const moDropdownDaoTao = () => {
  xoaBoDemDongDropdown()
  moDropdownNganh.value = true
}

const dongDropdownDaoTaoTre = () => {
  xoaBoDemDongDropdown()

  boDemDongDropdown = setTimeout(() => {
    moDropdownNganh.value = false
  }, 180)
}

const dongTatCaMenu = () => {
  xoaBoDemDongDropdown()
  moDropdownNganh.value = false
  moMenu.value = false
  moMobileNganh.value = false
}

const chuyenTrangNganhDaoTao = () => {
  dongTatCaMenu()
  router.push({ name: 'Public.NganhDaoTao' })
}

watch(
    () => route.fullPath,
    () => {
      dongTatCaMenu()
    }
)

onBeforeUnmount(() => {
  xoaBoDemDongDropdown()
})
</script>

<style scoped>
.menu-dropdown {
  position: relative;
  height: 72px;
  display: flex;
  align-items: center;
}

.menu-dropdown-button {
  height: 72px;
  display: inline-flex;
  align-items: center;
  gap: 6px;
  border: none;
  background: transparent;
  cursor: pointer;
  font-family: inherit;
  white-space: nowrap;
}

.menu-dropdown-button i {
  font-size: 10px;
  margin-top: 2px;
}

.menu-dropdown-safe-zone {
  position: absolute;
  top: 68px;
  left: 50%;
  z-index: 9998;
  width: 390px;
  height: 22px;
  transform: translateX(-50%);
  background: transparent;
}

.menu-dropdown-panel {
  position: absolute;
  top: 82px;
  left: 50%;
  z-index: 9999;
  width: 360px;
  padding: 12px;
  border: 1px solid #dbe9f8;
  border-radius: 16px;
  background: #ffffff;
  box-shadow: 0 18px 42px rgba(7, 63, 131, 0.18);
  transform: translateX(-50%);
}

.menu-dropdown-panel::before {
  content: '';
  position: absolute;
  top: -8px;
  left: 50%;
  width: 16px;
  height: 16px;
  border-left: 1px solid #dbe9f8;
  border-top: 1px solid #dbe9f8;
  background: #ffffff;
  transform: translateX(-50%) rotate(45deg);
}

.menu-dropdown-title {
  display: block;
  width: 100%;
  padding: 12px 14px;
  border-radius: 11px;
  background: #eff6ff;
  color: #063b7a;
  font-size: 14px;
  font-weight: 900;
  text-decoration: none;
}

.menu-dropdown-title:hover {
  background: #dbeafe;
  color: #075fc8;
}

.menu-dropdown-divider {
  height: 1px;
  margin: 10px 0;
  background: #edf4fb;
}

.menu-dropdown-item {
  width: 100%;
  min-height: 54px;
  display: block;
  padding: 9px 12px;
  border-radius: 12px;
  color: #123b68;
  text-decoration: none;
}

.menu-dropdown-item:hover {
  background: #f4f9ff;
}

.menu-dropdown-text {
  display: grid;
  gap: 3px;
}

.menu-dropdown-text strong {
  display: block;
  color: #063b7a;
  font-size: 14px;
  line-height: 1.35;
  font-weight: 900;
}

.menu-dropdown-text small {
  display: block;
  margin-top: 3px;
  color: #607894;
  font-size: 12px;
  line-height: 1.3;
  font-weight: 700;
}

.mobile-dropdown-button {
  width: 100%;
  justify-content: space-between;
  border: none;
  background: transparent;
  font-family: inherit;
  cursor: pointer;
}

.mobile-dropdown-list {
  display: grid;
  gap: 4px;
  padding: 8px 0 8px 16px;
}

.mobile-dropdown-item {
  display: block;
  padding: 10px 12px;
  border-radius: 9px;
  color: #063b7a;
  font-size: 14px;
  font-weight: 700;
  text-decoration: none;
}

.mobile-dropdown-item:hover {
  background: #eff6ff;
}

@media (max-width: 1180px) {
  .menu-dropdown-panel,
  .menu-dropdown-safe-zone {
    display: none;
  }
}
</style>