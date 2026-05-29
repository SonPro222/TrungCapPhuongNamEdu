<template>
  <section class="sinh-vien-layout">


    <nav class="sinh-vien-tabs">


      <RouterLink to="/admin/sinh-vien" exact-active-class="active">
        Quản lý hồ sơ sinh viên và Lớp học phần
      </RouterLink>

      <RouterLink to="/admin/sinh-vien/danh-sach" active-class="active">
        Danh sách sinh viên toàn trường
      </RouterLink>

      <RouterLink to="/admin/sinh-vien/bao-luu" active-class="active">
        Sinh viên bảo lưu
      </RouterLink>

    </nav>

    <RouterView />
  </section>
</template>

<style scoped>
.sinh-vien-layout {
  display: grid;
  gap: 14px;
}

.sinh-vien-head {
  border: 1px solid #bfdbfe;
  border-radius: 16px;
  padding: 18px;
  background: linear-gradient(135deg, #0f172a, #1d4ed8);
  color: #ffffff;
  box-shadow: 0 10px 26px rgba(15, 23, 42, 0.12);
}

.eyebrow {
  margin: 0 0 5px;
  text-transform: uppercase;
  letter-spacing: 0.08em;
  font-size: 11px;
  font-weight: 800;
  opacity: 0.86;
}

.sinh-vien-head h1 {
  margin: 0;
  font-size: 22px;
  line-height: 1.35;
}

.sinh-vien-head p {
  margin: 6px 0 0;
  max-width: 820px;
  color: #dbeafe;
  font-size: 13px;
  line-height: 1.45;
}

.sinh-vien-tabs {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  border: 1px solid #e2e8f0;
  border-radius: 14px;
  padding: 8px;
  background: #ffffff;
  box-shadow: 0 8px 22px rgba(15, 23, 42, 0.04);
}

.sinh-vien-tabs a {
  display: inline-flex;
  align-items: center;
  min-height: 36px;
  border: 1px solid transparent;
  border-radius: 10px;
  padding: 8px 12px;
  background: #f8fafc;
  color: #334155;
  font-size: 14px;
  font-weight: 700;
  letter-spacing: normal;
  text-transform: none;
  text-decoration: none;
  transition: background 0.12s ease, color 0.12s ease, border-color 0.12s ease;
}
.sinh-vien-tabs a.active {
  border-color: #bfdbfe;
  background: #eff6ff;
  color: #1d4ed8;
  font-weight: 700;
}

.sinh-vien-tabs a:hover {
  border-color: #bfdbfe;
  background: #eff6ff;
  color: #1d4ed8;
}

@media (max-width: 700px) {
  .sinh-vien-head {
    padding: 14px;
    border-radius: 14px;
  }

  .sinh-vien-tabs {
    display: grid;
  }

  .sinh-vien-tabs a {
    justify-content: center;
  }
}
</style>