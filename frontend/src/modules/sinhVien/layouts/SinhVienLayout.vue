<template>
  <section class="sinh-vien-layout">


    <nav class="sinh-vien-tabs">
      <RouterLink to="/admin/sinh-vien/lop-hanh-chinh" exact-active-class="active">
        Tạo lớp hành chính
      </RouterLink>

      <RouterLink to="/admin/sinh-vien/tao-lop-hoc-phan" exact-active-class="active">
        Tạo lớp học phần
      </RouterLink>

      <RouterLink to="/admin/sinh-vien" exact-active-class="active">
        Tạo Hồ sơ sinh viên
      </RouterLink>

      <RouterLink to="/admin/sinh-vien/lop-hoc-phan" exact-active-class="active">
        Phân bổ lớp học phần cho sinh viên.
      </RouterLink>
      <RouterLink to="/admin/sinh-vien/bao-luu" exact-active-class="active">
        Danh sách sinh viên bảo lưu
      </RouterLink>
    </nav>

    <RouterView />
  </section>
</template>

<style scoped>
.sinh-vien-layout {
  display: grid;
  gap: 18px;
}

.sinh-vien-head {
  background: linear-gradient(135deg, #0f172a, #1d4ed8);
  color: #ffffff;
  border-radius: 20px;
  padding: 24px;
}

.eyebrow {
  margin: 0 0 6px;
  text-transform: uppercase;
  letter-spacing: 0.08em;
  font-size: 12px;
  opacity: 0.82;
}

.sinh-vien-head h1 {
  margin: 0;
  font-size: 26px;
}

.sinh-vien-head p {
  margin: 8px 0 0;
  max-width: 820px;
  color: #dbeafe;
}

.sinh-vien-tabs {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  background: #ffffff;
  border: 1px solid #e2e8f0;
  border-radius: 16px;
  padding: 10px;
}

.sinh-vien-tabs a {
  text-decoration: none;
  border-radius: 12px;
  padding: 10px 14px;
  font-weight: 700;
  color: #334155;
  background: #f8fafc;
  border: 1px solid transparent;
}

.sinh-vien-tabs a.active {
  color: #1d4ed8;
  background: #eff6ff;
  border-color: #bfdbfe;
}

.sinh-vien-tabs a:hover {
  color: #1d4ed8;
}
</style>