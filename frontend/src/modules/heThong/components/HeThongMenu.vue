<template>
  <nav class="he-thong-menu">
    <RouterLink :to="{ name: 'HeThong.QuanLyTaiKhoan' }">
      Quản lý tài khoản
    </RouterLink>

    <RouterLink :to="{ name: 'HeThong.CapNhatTaiKhoan' }">
      Cập nhật tài khoản
    </RouterLink>

    <RouterLink :to="{ name: 'HeThong.DoiMatKhauTaiKhoan' }">
      Đổi mật khẩu tài khoản
    </RouterLink>

    <RouterLink :to="{ name: 'HeThong.Crudmau' }">
      Quản lý CRUD gốc
    </RouterLink>

    <RouterLink :to="{ name: 'HeThong.NhatKyDangNhap' }">
      Nhật ký đăng nhập
    </RouterLink>

    <RouterLink :to="{ name: 'HeThong.NhatKyHeThong' }">
      Nhật ký hệ thống
    </RouterLink>
    <RouterLink :to="{ name: 'HeThong.TepDinhKem' }">
      Quản lý tệp đính kèm
    </RouterLink>
  </nav>
</template>

<style scoped>
.he-thong-menu {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.he-thong-menu a {
  padding: 9px 12px;
  border: 1px solid var(--color-border);
  border-radius: var(--radius);
  background: var(--color-white);
  color: var(--color-text);
  text-decoration: none;
  font-weight: 600;
}

.he-thong-menu a.router-link-active {
  background: var(--color-primary);
  border-color: var(--color-primary);
  color: #fff;
}
</style>