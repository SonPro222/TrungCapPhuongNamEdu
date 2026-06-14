<template>
  <main class="tuyen-sinh-page">
    <section class="tuyen-sinh-hero">
      <div class="public-container hero-grid">
        <div class="hero-content">
          <span class="hero-label">Tuyển sinh</span>

          <h1>Thông tin tuyển sinh Trường Trung cấp Phương Nam</h1>

          <p>
            Cập nhật điều kiện xét tuyển, hồ sơ cần chuẩn bị, lịch tuyển sinh,
            học phí, chính sách hỗ trợ và các câu hỏi thường gặp dành cho học viên.
          </p>

          <div class="hero-actions">
            <RouterLink :to="{ name: 'Public.LienHe' }" class="primary-button">
              Đăng ký tư vấn
              <i class="fa-solid fa-arrow-right"></i>
            </RouterLink>

            <RouterLink
                v-if="danhSachHienThi.length"
                :to="{ name: 'Public.ChiTietTuyenSinh', params: { slug: danhSachHienThi[0].slug } }"
                class="secondary-button"
            >
              Xem chi tiết tuyển sinh
              <i class="fa-solid fa-circle-info"></i>
            </RouterLink>
          </div>
        </div>

        <div class="hero-process-card">
          <h2>Quy trình xét tuyển</h2>

          <div class="process-list">
            <div
                v-for="(item, index) in quyTrinhXetTuyen"
                :key="item.tieuDe"
                class="process-item"
            >
              <div class="process-number">
                {{ String(index + 1).padStart(2, '0') }}
              </div>

              <div>
                <h3>{{ item.tieuDe }}</h3>
                <p>{{ item.moTa }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <section class="quick-info-section">
      <div class="public-container quick-info-grid">
        <article>
          <i class="fa-solid fa-file-lines"></i>
          <h3>Xét học bạ THCS/THPT</h3>
          <p>Xét tuyển dựa trên kết quả học tập và hồ sơ đăng ký của học viên.</p>
        </article>

        <article>
          <i class="fa-solid fa-headset"></i>
          <h3>Tư vấn chọn ngành</h3>
          <p>Hỗ trợ học viên chọn ngành học phù hợp với năng lực và định hướng nghề nghiệp.</p>
        </article>

        <article>
          <i class="fa-solid fa-folder-open"></i>
          <h3>Nộp hồ sơ linh hoạt</h3>
          <p>Học viên có thể chuẩn bị hồ sơ và liên hệ nhà trường để được hướng dẫn.</p>
        </article>

        <article>
          <i class="fa-solid fa-user-check"></i>
          <h3>Nhập học nhanh</h3>
          <p>Quy trình đơn giản, hỗ trợ hoàn tất hồ sơ và thủ tục nhập học kịp thời.</p>
        </article>
      </div>
    </section>

    <section class="public-section">
      <div class="public-container admission-layout">
        <aside class="admission-sidebar">
          <h2>Danh mục tuyển sinh</h2>

          <label>
            <span>Tìm kiếm</span>
            <input
                v-model.trim="tuKhoa"
                type="text"
                placeholder="Tìm nội dung tuyển sinh"
            />
          </label>

          <nav>
            <RouterLink
                v-for="item in danhSachDaLoc"
                :key="item.slug"
                :to="{ name: 'Public.ChiTietTuyenSinh', params: { slug: item.slug } }"
                class="sidebar-link"
            >
              <i :class="item.icon || 'fa-solid fa-circle-info'"></i>
              <span>{{ item.tieuDe }}</span>
            </RouterLink>
          </nav>
        </aside>

        <div class="admission-main">
          <div class="section-heading-left">
            <span>Nội dung tuyển sinh</span>
            <h2>Thông tin cần biết trước khi đăng ký</h2>
            <p>
              Các mục bên dưới được quản lý từ trang admin public và đang hiển thị
              trực tiếp trên website.
            </p>
          </div>

          <div class="admission-list">
            <RouterLink
                v-for="item in danhSachDaLoc"
                :key="item.slug"
                :to="{ name: 'Public.ChiTietTuyenSinh', params: { slug: item.slug } }"
                class="admission-item"
            >
              <div class="admission-icon">
                <i :class="item.icon || 'fa-solid fa-circle-info'"></i>
              </div>

              <div class="admission-body">
                <h3>{{ item.tieuDe }}</h3>
                <p>{{ item.moTa }}</p>

                <div
                    v-if="item.thongTinNhanh && item.thongTinNhanh.length"
                    class="quick-tags"
                >
                  <span
                      v-for="tag in item.thongTinNhanh.slice(0, 3)"
                      :key="tag"
                  >
                    {{ tag }}
                  </span>
                </div>
              </div>

              <div class="admission-arrow">
                <i class="fa-solid fa-arrow-right"></i>
              </div>
            </RouterLink>
          </div>

          <div v-if="!danhSachDaLoc.length" class="empty-state">
            <i class="fa-solid fa-circle-info"></i>
            <h2>Không có nội dung tuyển sinh phù hợp</h2>
            <p>Vui lòng thử lại với từ khóa khác.</p>
          </div>
        </div>
      </div>
    </section>

    <section class="public-section gray-section">
      <div class="public-container bottom-grid">
        <div class="document-card">
          <h2>Hồ sơ cần chuẩn bị</h2>

          <ul>
            <li>
              <i class="fa-solid fa-circle-check"></i>
              <span>Phiếu đăng ký xét tuyển theo mẫu của trường</span>
            </li>

            <li>
              <i class="fa-solid fa-circle-check"></i>
              <span>Bản sao bằng tốt nghiệp hoặc giấy chứng nhận tốt nghiệp tạm thời</span>
            </li>

            <li>
              <i class="fa-solid fa-circle-check"></i>
              <span>Bản sao học bạ THCS/THPT</span>
            </li>

            <li>
              <i class="fa-solid fa-circle-check"></i>
              <span>Bản sao CCCD/CMND và ảnh hồ sơ theo yêu cầu</span>
            </li>
          </ul>
        </div>

        <div class="timeline-card">
          <h2>Mốc thời gian tuyển sinh</h2>

          <div class="timeline-grid">
            <article>
              <i class="fa-solid fa-calendar-days"></i>
              <strong>Đợt 1</strong>
              <span>01/03 - 30/04</span>
              <p>Nhận hồ sơ và xét tuyển.</p>
            </article>

            <article>
              <i class="fa-solid fa-calendar-days"></i>
              <strong>Đợt 2</strong>
              <span>01/05 - 30/06</span>
              <p>Nhận hồ sơ và xét tuyển.</p>
            </article>

            <article>
              <i class="fa-solid fa-calendar-check"></i>
              <strong>Đợt 3</strong>
              <span>01/07 - 31/08</span>
              <p>Nhận hồ sơ và xét tuyển.</p>
            </article>

            <article>
              <i class="fa-solid fa-calendar-plus"></i>
              <strong>Đợt bổ sung</strong>
              <span>01/09 - 30/09</span>
              <p>Xét tuyển nếu còn chỉ tiêu.</p>
            </article>
          </div>
        </div>
      </div>
    </section>

    <section class="public-section">
      <div class="public-container tu-van-box">
        <div>
          <span>Cần hỗ trợ?</span>
          <h2>Đăng ký tư vấn tuyển sinh ngay hôm nay</h2>
          <p>
            Để lại thông tin liên hệ, nhà trường sẽ hỗ trợ bạn chọn ngành học,
            chuẩn bị hồ sơ và hoàn tất thủ tục xét tuyển.
          </p>
        </div>

        <RouterLink :to="{ name: 'Public.LienHe' }">
          Gửi thông tin tư vấn
          <i class="fa-solid fa-paper-plane"></i>
        </RouterLink>
      </div>
    </section>
  </main>
</template>

<script setup>
import { computed, ref } from 'vue'
import { useTuyenSinhPublic } from '@/modules/public/composables/useTuyenSinhPublic.js'

const { danhSachTuyenSinh } = useTuyenSinhPublic()

const tuKhoa = ref('')

const quyTrinhXetTuyen = [
  {
    tieuDe: 'Đăng ký tư vấn',
    moTa: 'Học viên để lại thông tin để được nhà trường hỗ trợ.'
  },
  {
    tieuDe: 'Chuẩn bị hồ sơ',
    moTa: 'Hoàn tất các giấy tờ cần thiết theo hướng dẫn.'
  },
  {
    tieuDe: 'Xét tuyển',
    moTa: 'Nhà trường tiếp nhận hồ sơ và xét tuyển.'
  },
  {
    tieuDe: 'Nhập học',
    moTa: 'Nhận thông báo và hoàn tất thủ tục nhập học.'
  }
]

const danhSachHienThi = computed(() => {
  return danhSachTuyenSinh.value.filter((item) => item.trangThaiHienThi !== false)
})

const danhSachDaLoc = computed(() => {
  const keyword = tuKhoa.value.toLowerCase()

  return danhSachHienThi.value.filter((item) => {
    return (
        !keyword ||
        String(item.tieuDe || '').toLowerCase().includes(keyword) ||
        String(item.moTa || '').toLowerCase().includes(keyword)
    )
  })
})
</script>

<style scoped>
.tuyen-sinh-page {
  background: #ffffff;
}

.tuyen-sinh-hero {
  padding: 66px 0;
  background:
      radial-gradient(circle at top right, rgba(7, 95, 200, 0.16), transparent 34%),
      linear-gradient(135deg, #eff6ff 0%, #ffffff 58%, #f8fbff 100%);
}

.hero-grid {
  display: grid;
  grid-template-columns: minmax(0, 1fr) 430px;
  gap: 38px;
  align-items: center;
}

.hero-label {
  width: fit-content;
  display: inline-flex;
  align-items: center;
  min-height: 34px;
  padding: 0 14px;
  border-radius: 999px;
  background: #dbeafe;
  color: #075fc8;
  font-size: 13px;
  font-weight: 900;
}

.hero-content h1 {
  max-width: 820px;
  margin: 16px 0 14px;
  color: #063b7a;
  font-size: 46px;
  line-height: 1.12;
  font-weight: 900;
}

.hero-content p {
  max-width: 740px;
  margin: 0;
  color: #486581;
  font-size: 16px;
  line-height: 1.75;
}

.hero-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  margin-top: 26px;
}

.primary-button,
.secondary-button {
  min-height: 46px;
  display: inline-flex;
  align-items: center;
  gap: 9px;
  padding: 0 20px;
  border-radius: 999px;
  font-size: 14px;
  font-weight: 900;
  text-decoration: none;
}

.primary-button {
  background: #075fc8;
  color: #ffffff;
  box-shadow: 0 12px 24px rgba(7, 95, 200, 0.22);
}

.secondary-button {
  border: 1px solid #cfe1f6;
  background: #ffffff;
  color: #063b7a;
}

.hero-process-card {
  padding: 28px;
  border: 1px solid #dbeafe;
  border-radius: 24px;
  background: #ffffff;
  box-shadow: 0 18px 42px rgba(7, 63, 131, 0.1);
}

.hero-process-card h2 {
  margin: 0 0 18px;
  color: #063b7a;
  font-size: 22px;
  font-weight: 900;
}

.process-list {
  display: grid;
  gap: 14px;
}

.process-item {
  display: grid;
  grid-template-columns: 44px 1fr;
  gap: 12px;
}

.process-number {
  width: 44px;
  height: 44px;
  display: grid;
  place-items: center;
  border-radius: 14px;
  background: #075fc8;
  color: #ffffff;
  font-size: 13px;
  font-weight: 900;
}

.process-item h3 {
  margin: 0 0 4px;
  color: #063b7a;
  font-size: 15px;
  font-weight: 900;
}

.process-item p {
  margin: 0;
  color: #607894;
  font-size: 13px;
  line-height: 1.55;
}

.quick-info-section {
  padding: 0 0 36px;
  margin-top: -28px;
}

.quick-info-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 18px;
}

.quick-info-grid article {
  padding: 22px;
  border: 1px solid #dbe7f5;
  border-radius: 20px;
  background: #ffffff;
  box-shadow: 0 12px 26px rgba(7, 63, 131, 0.08);
}

.quick-info-grid i {
  width: 48px;
  height: 48px;
  display: grid;
  place-items: center;
  margin-bottom: 14px;
  border-radius: 16px;
  background: #eff6ff;
  color: #075fc8;
  font-size: 21px;
}

.quick-info-grid h3 {
  margin: 0 0 8px;
  color: #063b7a;
  font-size: 17px;
  font-weight: 900;
}

.quick-info-grid p {
  margin: 0;
  color: #607894;
  font-size: 14px;
  line-height: 1.6;
}

.admission-layout {
  display: grid;
  grid-template-columns: 320px minmax(0, 1fr);
  gap: 26px;
  align-items: flex-start;
}

.admission-sidebar {
  position: sticky;
  top: 92px;
  padding: 22px;
  border: 1px solid #dbe7f5;
  border-radius: 22px;
  background: #ffffff;
  box-shadow: 0 8px 22px rgba(7, 63, 131, 0.05);
}

.admission-sidebar h2 {
  margin: 0 0 18px;
  color: #063b7a;
  font-size: 20px;
  font-weight: 900;
}

.admission-sidebar label {
  display: grid;
  gap: 8px;
  margin-bottom: 18px;
}

.admission-sidebar label span {
  color: #063b7a;
  font-size: 14px;
  font-weight: 900;
}

.admission-sidebar input {
  width: 100%;
  height: 42px;
  padding: 0 13px;
  border: 1px solid #cfe1f6;
  border-radius: 12px;
  outline: none;
  color: #123b68;
  font: inherit;
}

.admission-sidebar input:focus {
  border-color: #075fc8;
  box-shadow: 0 0 0 3px rgba(7, 95, 200, 0.12);
}

.admission-sidebar nav {
  display: grid;
  gap: 8px;
}

.sidebar-link {
  display: grid;
  grid-template-columns: 24px 1fr;
  align-items: center;
  gap: 10px;
  padding: 12px;
  border-radius: 14px;
  color: #486581;
  font-size: 14px;
  font-weight: 800;
  text-decoration: none;
}

.sidebar-link:hover {
  background: #eff6ff;
  color: #075fc8;
}

.sidebar-link i {
  color: #075fc8;
}

.section-heading-left {
  margin-bottom: 22px;
}

.section-heading-left > span {
  color: #075fc8;
  font-size: 13px;
  font-weight: 900;
}

.section-heading-left h2 {
  margin: 8px 0 10px;
  color: #063b7a;
  font-size: 32px;
  font-weight: 900;
}

.section-heading-left p {
  max-width: 760px;
  margin: 0;
  color: #607894;
  line-height: 1.7;
}

.admission-list {
  display: grid;
  gap: 16px;
}

.admission-item {
  display: grid;
  grid-template-columns: 64px minmax(0, 1fr) 42px;
  gap: 16px;
  align-items: center;
  padding: 22px;
  border: 1px solid #dbe7f5;
  border-radius: 20px;
  background: #ffffff;
  color: inherit;
  text-decoration: none;
  box-shadow: 0 8px 22px rgba(7, 63, 131, 0.05);
  transition: 0.22s ease;
}

.admission-item:hover {
  transform: translateY(-3px);
  box-shadow: 0 16px 34px rgba(7, 63, 131, 0.12);
}

.admission-icon {
  width: 64px;
  height: 64px;
  display: grid;
  place-items: center;
  border-radius: 18px;
  background: #eff6ff;
  color: #075fc8;
  font-size: 26px;
}

.admission-body h3 {
  margin: 0 0 8px;
  color: #063b7a;
  font-size: 21px;
  font-weight: 900;
}

.admission-body p {
  margin: 0;
  color: #607894;
  font-size: 14px;
  line-height: 1.65;
}

.quick-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: 14px;
}

.quick-tags span {
  padding: 7px 10px;
  border-radius: 999px;
  background: #eff6ff;
  color: #075fc8;
  font-size: 12px;
  font-weight: 800;
}

.admission-arrow {
  width: 42px;
  height: 42px;
  display: grid;
  place-items: center;
  border-radius: 999px;
  background: #075fc8;
  color: #ffffff;
}

.bottom-grid {
  display: grid;
  grid-template-columns: 0.85fr 1.15fr;
  gap: 24px;
}

.document-card,
.timeline-card {
  padding: 28px;
  border: 1px solid #dbe7f5;
  border-radius: 22px;
  background: #ffffff;
  box-shadow: 0 8px 22px rgba(7, 63, 131, 0.05);
}

.document-card h2,
.timeline-card h2 {
  margin: 0 0 20px;
  color: #063b7a;
  font-size: 24px;
  font-weight: 900;
}

.document-card ul {
  display: grid;
  gap: 14px;
  padding: 0;
  margin: 0;
  list-style: none;
}

.document-card li {
  display: grid;
  grid-template-columns: 22px 1fr;
  gap: 10px;
  color: #486581;
  line-height: 1.65;
}

.document-card li i {
  margin-top: 4px;
  color: #075fc8;
}

.timeline-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 14px;
}

.timeline-grid article {
  padding: 18px;
  border: 1px solid #dbe7f5;
  border-radius: 16px;
  background: #f8fbff;
  text-align: center;
}

.timeline-grid i {
  color: #075fc8;
  font-size: 24px;
}

.timeline-grid strong {
  display: block;
  margin-top: 10px;
  color: #063b7a;
  font-size: 15px;
  font-weight: 900;
}

.timeline-grid span {
  display: block;
  margin-top: 5px;
  color: #075fc8;
  font-size: 13px;
  font-weight: 900;
}

.timeline-grid p {
  margin: 8px 0 0;
  color: #607894;
  font-size: 13px;
  line-height: 1.5;
}

.tu-van-box {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 24px;
  padding: 34px;
  border-radius: 24px;
  background: #063b7a;
  color: #ffffff;
}

.tu-van-box span {
  display: inline-flex;
  margin-bottom: 8px;
  color: #bfdbfe;
  font-size: 13px;
  font-weight: 900;
}

.tu-van-box h2 {
  margin: 0 0 10px;
  color: #ffffff;
  font-size: 30px;
  font-weight: 900;
}

.tu-van-box p {
  max-width: 680px;
  margin: 0;
  color: #dbeafe;
  line-height: 1.7;
}

.tu-van-box a {
  min-height: 46px;
  display: inline-flex;
  align-items: center;
  gap: 9px;
  flex: 0 0 auto;
  padding: 0 20px;
  border-radius: 999px;
  background: #ffffff;
  color: #063b7a;
  font-size: 14px;
  font-weight: 900;
  text-decoration: none;
}

.empty-state {
  display: grid;
  place-items: center;
  gap: 10px;
  padding: 56px 0;
  text-align: center;
}

.empty-state i {
  color: #075fc8;
  font-size: 42px;
}

.empty-state h2 {
  margin: 0;
  color: #063b7a;
}

.empty-state p {
  margin: 0;
  color: #607894;
}

@media (max-width: 1180px) {
  .hero-grid,
  .admission-layout,
  .bottom-grid {
    grid-template-columns: 1fr;
  }

  .admission-sidebar {
    position: static;
  }

  .quick-info-grid,
  .timeline-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}

@media (max-width: 760px) {
  .hero-content h1 {
    font-size: 34px;
  }

  .hero-actions,
  .tu-van-box {
    display: grid;
  }

  .primary-button,
  .secondary-button,
  .tu-van-box a {
    justify-content: center;
  }

  .quick-info-grid,
  .timeline-grid {
    grid-template-columns: 1fr;
  }

  .admission-item {
    grid-template-columns: 1fr;
  }

  .admission-arrow {
    display: none;
  }
}
</style>
