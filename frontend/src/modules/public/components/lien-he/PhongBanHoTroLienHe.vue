<template>
  <section class="public-container department-section">
    <div class="section-title-contact">
      <div>
        <i class="fa-solid fa-users-gear"></i>
      </div>

      <div>
        <h3>Phòng ban hỗ trợ</h3>
        <p>Các bộ phận chuyên trách sẵn sàng hỗ trợ bạn nhanh chóng và hiệu quả.</p>
      </div>
    </div>

    <div class="department-grid">
      <article v-for="item in phongBanHoTro" :key="item.id" class="department-card">
        <div class="department-card-head">
          <div>
            <i :class="item.icon"></i>
          </div>
          <h4>{{ item.name }}</h4>
        </div>

        <p>{{ item.desc }}</p>

        <ul>
          <li>
            <span>Hotline:</span>
            <strong>{{ item.phone }}</strong>
          </li>
          <li>
            <span>Email:</span>
            <strong>{{ item.email }}</strong>
          </li>
        </ul>

        <button type="button">Liên hệ ngay</button>
      </article>
    </div>
  </section>
</template>

<script setup>
const phongBanHoTro = [
  {
    id: 1,
    name: 'Phòng Tuyển sinh',
    icon: 'fa-solid fa-graduation-cap',
    desc: 'Tư vấn tuyển sinh, ngành nghề, chính sách học phí và học bổng.',
    phone: '1900 633 088',
    email: 'tuyensinh@phuongnam.edu.vn'
  },
  {
    id: 2,
    name: 'Phòng Đào tạo',
    icon: 'fa-solid fa-book-open',
    desc: 'Thông tin chương trình đào tạo, thời khóa biểu và học vụ.',
    phone: '(028) 38 680 886 (102)',
    email: 'daotao@phuongnam.edu.vn'
  },
  {
    id: 3,
    name: 'Công tác Sinh viên',
    icon: 'fa-solid fa-users',
    desc: 'Hỗ trợ đời sống sinh viên, hoạt động ngoại khóa, học bổng.',
    phone: '(028) 38 680 886 (103)',
    email: 'sinhvien@phuongnam.edu.vn'
  },
  {
    id: 4,
    name: 'Hành chính - Văn phòng',
    icon: 'fa-solid fa-file-signature',
    desc: 'Tiếp nhận hồ sơ, công văn, và các thủ tục hành chính.',
    phone: '(028) 38 680 886 (100)',
    email: 'vanphong@phuongnam.edu.vn'
  }
]
</script>

<style scoped>
.department-section {
  margin-bottom: 46px;
}

.section-title-contact {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 24px;
}

.section-title-contact > div:first-child {
  width: 34px;
  height: 34px;
  display: grid;
  place-items: center;
  border-radius: 8px;
  background: #eff6ff;
  color: #075fc8;
}

.section-title-contact h3 {
  margin: 0;
  color: #1e293b;
  font-size: 22px;
  font-weight: 900;
}

.section-title-contact p {
  margin: 3px 0 0;
  color: #64748b;
  font-size: 13px;
}

.department-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 22px;
}

.department-card {
  overflow: hidden;
  padding: 24px;
  border: 1px solid #e2e8f0;
  border-radius: 18px;
  background: #ffffff;
  box-shadow: 0 8px 22px rgba(7, 63, 131, 0.05);
  transition: 0.25s;
}

.department-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 14px 30px rgba(7, 63, 131, 0.1);
}

.department-card-head {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 14px;
}

.department-card-head div {
  width: 48px;
  height: 48px;
  display: grid;
  place-items: center;
  border-radius: 999px;
  background: #eff6ff;
  color: #075fc8;
  font-size: 20px;
}

.department-card h4 {
  margin: 0;
  color: #1e293b;
  font-size: 17px;
  font-weight: 900;
}

.department-card p {
  min-height: 56px;
  margin: 0 0 16px;
  color: #64748b;
  font-size: 13px;
  line-height: 1.55;
}

.department-card ul {
  display: grid;
  gap: 7px;
  padding: 0;
  margin: 0 0 20px;
  list-style: none;
}

.department-card li {
  color: #64748b;
  font-size: 13px;
}

.department-card li strong {
  color: #1e293b;
}

.department-card button {
  width: 100%;
  min-height: 40px;
  border: 1px solid #075fc8;
  border-radius: 10px;
  background: #ffffff;
  color: #075fc8;
  font-weight: 900;
  cursor: pointer;
}

.department-card button:hover {
  background: #075fc8;
  color: #ffffff;
}

@media (max-width: 1180px) {
  .department-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 760px) {
  .department-grid {
    grid-template-columns: 1fr;
  }
}
</style>