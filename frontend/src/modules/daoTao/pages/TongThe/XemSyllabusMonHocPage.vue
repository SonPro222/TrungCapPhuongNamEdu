  <template>
    <div class="dao-tao-xem-syllabus-page">
      <section v-if="errorMessage" class="state-card error">
        <div>
          <h3>Không tải được syllabus</h3>
          <p>{{ errorMessage }}</p>
        </div>

        <button type="button" class="btn primary" @click="quayLai">
          Quay lại
        </button>
      </section>

      <section v-else-if="loading" class="state-card loading-card">
        <div>
          <h3>Đang tải chi tiết syllabus...</h3>
          <p>Hệ thống đang lấy chuẩn đầu ra, tài liệu, cấu hình đánh giá, quy đổi điểm và điều kiện.</p>
        </div>
      </section>

      <section v-else-if="!payload" class="state-card empty">
        <div>
          <h3>Không có dữ liệu syllabus</h3>
          <p>Vui lòng quay lại trang kỳ và chọn lại môn học cần xem syllabus.</p>
        </div>

        <button type="button" class="btn primary" @click="quayLai">
          Quay lại
        </button>
      </section>

      <template v-else>
        <section class="compact-hero">
          <div class="hero-left">
            <button type="button" class="back-btn" @click="quayLai">
              ← Quay lại
            </button>

            <div class="hero-title-row">
              <div class="hero-title-main">
                <p class="eyebrow">Syllabus môn học</p>
                <h1>{{ tenMonHoc }}</h1>
              </div>

              <div class="summary-badges">
                <span :class="['status-badge', coSyllabus ? 'success' : 'muted']">
                  {{ coSyllabus ? 'Có syllabus' : 'Chưa có syllabus' }}
                </span>

                <span v-if="payload.maMon" class="status-badge neutral">
                  Mã môn: {{ payload.maMon }}
                </span>
              </div>
            </div>

            <p class="subtitle">
              Thông tin syllabus của môn học. Các mã kỹ thuật nội bộ không được hiển thị trên giao diện.
            </p>
          </div>

          <div class="metric-grid compact">
            <article>
              <span>Tín chỉ</span>
              <strong>{{ hienThi(payload.soTinChi) }}</strong>
            </article>

            <article>
              <span>Lý thuyết</span>
              <strong>{{ hienThi(payload.gioLyThuyet) }}</strong>
            </article>

            <article>
              <span>Thực hành</span>
              <strong>{{ hienThi(payload.gioThucHanh) }}</strong>
            </article>

            <article>
              <span>Kiểm tra</span>
              <strong>{{ hienThi(payload.gioKiemTra) }}</strong>
            </article>

            <article>
              <span>Buổi học</span>
              <strong>{{ hienThi(payload.soBuoiHoc) }}</strong>
            </article>
          </div>
        </section>

        <section v-if="!coSyllabus" class="state-card empty">
          <div>
            <h3>Môn học này chưa có syllabus</h3>
            <p>Hiện chưa có nội dung syllabus áp dụng cho môn học này.</p>
          </div>
        </section>

        <section v-else class="accordion-list">
          <article class="accordion-card">
            <button type="button" class="accordion-head" @click="toggleBang('thong-tin')">
              <div>
                <h3>Thông tin syllabus</h3>
                <p>Thông tin mô tả, vị trí, tính chất, phương pháp đánh giá</p>
              </div>

              <span class="accordion-meta">
                {{ thongTinSyllabus.length }} mục
                <b>{{ dangMoBang('thong-tin') ? '−' : '+' }}</b>
              </span>
            </button>

            <div v-if="dangMoBang('thong-tin')" class="accordion-body">
              <dl v-if="thongTinSyllabus.length" class="info-list">
                <template v-for="item in thongTinSyllabus" :key="item.label">
                  <dt>{{ item.label }}</dt>
                  <dd>{{ item.value }}</dd>
                </template>
              </dl>

              <div v-else class="empty-inline">
                Chưa có thông tin mô tả syllabus.
              </div>
            </div>
          </article>

          <article class="accordion-card">
            <button type="button" class="accordion-head" @click="toggleBang('muc-tieu')">
              <div>
                <h3>Mục tiêu môn học</h3>
                <p>Mục tiêu, định hướng học tập của môn học</p>
              </div>

              <span class="accordion-meta">
                {{ mucTieuMonHoc ? 'Có dữ liệu' : 'Trống' }}
                <b>{{ dangMoBang('muc-tieu') ? '−' : '+' }}</b>
              </span>
            </button>

            <div v-if="dangMoBang('muc-tieu')" class="accordion-body">
              <div v-if="mucTieuMonHoc" class="rich-text">
                {{ mucTieuMonHoc }}
              </div>

              <div v-else class="empty-inline">
                Chưa có mục tiêu môn học.
              </div>
            </div>
          </article>

          <article class="accordion-card">
            <button type="button" class="accordion-head" @click="toggleBang('chuong-bai')">
              <div>
                <h3>Nội dung syllabus / chương bài</h3>
                <p>Danh sách chương, bài, nội dung và số tiết</p>
              </div>

              <span class="accordion-meta">
                {{ danhSachChuongBai.length }} mục
                <b>{{ dangMoBang('chuong-bai') ? '−' : '+' }}</b>
              </span>
            </button>

            <div v-if="dangMoBang('chuong-bai')" class="accordion-body">
              <div v-if="!danhSachChuongBai.length" class="empty-inline">
                Chưa có nội dung chương bài.
              </div>

              <div v-else class="chapter-list">
                <article
                    v-for="(chuong, index) in danhSachChuongBai"
                    :key="index"
                    class="chapter-card"
                >
                  <div class="chapter-index">{{ index + 1 }}</div>

                  <div class="chapter-content">
                    <div class="chapter-head">
                      <strong>{{ layTenChuong(chuong, index) }}</strong>
                      <span v-if="laySoTietChuong(chuong)">
                        Số tiết: {{ laySoTietChuong(chuong) }}
                      </span>
                    </div>

                    <p v-if="layNoiDungChuong(chuong)">
                      {{ layNoiDungChuong(chuong) }}
                    </p>
                  </div>
                </article>
              </div>
            </div>
          </article>

          <article class="accordion-card">
            <button type="button" class="accordion-head" @click="toggleBang('chuan-dau-ra')">
              <div>
                <h3>Chuẩn đầu ra</h3>
                <p>Mã chuẩn đầu ra, nội dung, mức độ và ghi chú</p>
              </div>

              <span class="accordion-meta">
                {{ danhSachChuanDauRa.length }} dòng
                <b>{{ dangMoBang('chuan-dau-ra') ? '−' : '+' }}</b>
              </span>
            </button>

            <div v-if="dangMoBang('chuan-dau-ra')" class="accordion-body">
              <div v-if="!danhSachChuanDauRa.length" class="empty-inline">
                Chưa có chuẩn đầu ra theo syllabus áp dụng.
              </div>

              <div v-else class="table-wrap">
                <table>
                  <thead>
                  <tr>
                    <th class="col-stt">STT</th>
                    <th class="col-code">Mã</th>
                    <th>Chuẩn đầu ra</th>
                    <th class="col-short">Mức độ</th>
                    <th>Ghi chú</th>
                  </tr>
                  </thead>

                  <tbody>
                  <tr v-for="(item, index) in danhSachChuanDauRa" :key="`cdr-${index}`">
                    <td>{{ index + 1 }}</td>
                    <td>{{ hienThi(layTruong(item, ['ma', 'maChuanDauRa', 'code'])) }}</td>
                    <td>{{ hienThiNoiDung(item) }}</td>
                    <td>{{ hienThi(layTruong(item, ['mucDo', 'capDo', 'level'])) }}</td>
                    <td>{{ hienThi(layTruong(item, ['ghiChu', 'moTa'])) }}</td>
                  </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </article>

          <article class="accordion-card">
            <button type="button" class="accordion-head" @click="toggleBang('danh-gia')">
              <div>
                <h3>Cấu hình đánh giá</h3>
                <p>Cột điểm, loại điểm, tỷ lệ, điểm tối đa và bắt buộc</p>
              </div>

              <span :class="['accordion-meta', tongTyLeDanhGia === 100 ? 'success' : 'warning']">
                Tổng {{ tongTyLeDanhGia }}%
                <b>{{ dangMoBang('danh-gia') ? '−' : '+' }}</b>
              </span>
            </button>

            <div v-if="dangMoBang('danh-gia')" class="accordion-body">
              <div v-if="!danhSachCauHinhDanhGia.length" class="empty-inline">
                Chưa có cấu hình đánh giá theo syllabus áp dụng.
              </div>

              <div v-else class="table-wrap">
                <table>
                  <thead>
                  <tr>
                    <th class="col-stt">STT</th>
                    <th>Tên cột điểm</th>
                    <th>Loại điểm</th>
                    <th class="col-short">Tỷ lệ %</th>
                    <th class="col-short">Điểm tối đa</th>
                    <th class="col-short">Bắt buộc</th>
                    <th>Ghi chú</th>
                  </tr>
                  </thead>

                  <tbody>
                  <tr v-for="(item, index) in danhSachCauHinhDanhGia" :key="`dg-${index}`">
                    <td>{{ layThuTu(item, index) }}</td>
                    <td>{{ hienThi(layTruong(item, ['tenCotDiem', 'ten', 'ma'])) }}</td>
                    <td>{{ hienThi(layTruong(item, ['loaiDiem', 'loai', 'loaiMau'])) }}</td>
                    <td>{{ hienThi(layTruong(item, ['tyLe', 'tiLe'])) }}</td>
                    <td>{{ hienThi(layTruong(item, ['diemToiDa', 'diemMax'])) }}</td>
                    <td>{{ hienThiBoolean(layTruong(item, ['batBuoc', 'required'])) }}</td>
                    <td>{{ hienThi(layTruong(item, ['ghiChu', 'moTa'])) }}</td>
                  </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </article>

          <article class="accordion-card">
            <button type="button" class="accordion-head" @click="toggleBang('quy-doi-diem')">
              <div>
                <h3>Quy đổi điểm theo chương trình</h3>
                <p>Ngưỡng điểm, điểm quy đổi, kết quả và công thức</p>
              </div>

              <span class="accordion-meta">
                {{ danhSachQuyDoiDiem.length }} dòng
                <b>{{ dangMoBang('quy-doi-diem') ? '−' : '+' }}</b>
              </span>
            </button>

            <div v-if="dangMoBang('quy-doi-diem')" class="accordion-body">
              <div v-if="!danhSachQuyDoiDiem.length" class="empty-inline">
                Chưa có quy đổi điểm theo chương trình/syllabus áp dụng.
              </div>

              <div v-else class="table-wrap">
                <table>
                  <thead>
                  <tr>
                    <th class="col-stt">STT</th>
                    <th class="col-code">Mã</th>
                    <th>Tên</th>
                    <th class="col-short">Ngưỡng từ</th>
                    <th class="col-short">Ngưỡng đến</th>
                    <th class="col-short">Điểm quy đổi</th>
                    <th class="col-short">Kết quả</th>
                    <th>Công thức</th>
                  </tr>
                  </thead>

                  <tbody>
                  <tr v-for="(item, index) in danhSachQuyDoiDiem" :key="`qdd-${index}`">
                    <td>{{ layThuTu(item, index) }}</td>
                    <td>{{ hienThi(layTruong(item, ['ma', 'code'])) }}</td>
                    <td>{{ hienThi(layTruong(item, ['ten', 'tenMau', 'moTa'])) }}</td>
                    <td>{{ hienThi(layTruong(item, ['nguongTu', 'diemTu', 'tuDiem'])) }}</td>
                    <td>{{ hienThi(layTruong(item, ['nguongDen', 'diemDen', 'denDiem'])) }}</td>
                    <td>{{ hienThi(layTruong(item, ['diemQuyDoi', 'diemHe4', 'diem'])) }}</td>
                    <td>{{ hienThi(layTruong(item, ['ketQua', 'xepLoai', 'trangThai'])) }}</td>
                    <td>{{ hienThi(layTruong(item, ['congThuc', 'ghiChu'])) }}</td>
                  </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </article>

          <article class="accordion-card">
            <button type="button" class="accordion-head" @click="toggleBang('dieu-kien')">
              <div>
                <h3>Điều kiện của syllabus</h3>
                <p>Loại điều kiện, nội dung, số lượng, yêu cầu và ghi chú</p>
              </div>

              <span class="accordion-meta">
                {{ danhSachDieuKien.length }} dòng
                <b>{{ dangMoBang('dieu-kien') ? '−' : '+' }}</b>
              </span>
            </button>

            <div v-if="dangMoBang('dieu-kien')" class="accordion-body">
              <div v-if="!danhSachDieuKien.length" class="empty-inline">
                Chưa có điều kiện theo syllabus áp dụng.
              </div>

              <div v-else class="table-wrap">
                <table>
                  <thead>
                  <tr>
                    <th class="col-stt">STT</th>
                    <th class="col-short">Loại</th>
                    <th>Tên/Nội dung</th>
                    <th class="col-short">Số lượng</th>
                    <th>Yêu cầu</th>
                    <th>Ghi chú</th>
                  </tr>
                  </thead>

                  <tbody>
                  <tr v-for="(item, index) in danhSachDieuKien" :key="`dk-${index}`">
                    <td>{{ layThuTu(item, index) }}</td>
                    <td>{{ hienThi(layTruong(item, ['loai', 'loaiDieuKien', 'type'])) }}</td>
                    <td>{{ hienThi(layTruong(item, ['ten', 'noiDung', 'moTa'])) }}</td>
                    <td>{{ hienThi(layTruong(item, ['soLuong', 'quantity'])) }}</td>
                    <td>{{ hienThi(layTruong(item, ['yeuCau', 'requirement'])) }}</td>
                    <td>{{ hienThi(layTruong(item, ['ghiChu'])) }}</td>
                  </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </article>

          <article class="accordion-card">
            <button type="button" class="accordion-head" @click="toggleBang('file-syllabus')">
              <div>
                <h3>File syllabus</h3>
                <p>File được gán vào syllabus môn học</p>
              </div>

              <span class="accordion-meta">
                {{ danhSachFileMau.length }} file
                <b>{{ dangMoBang('file-syllabus') ? '−' : '+' }}</b>
              </span>
            </button>

            <div v-if="dangMoBang('file-syllabus')" class="accordion-body">
              <div v-if="!danhSachFileMau.length" class="empty-inline">
                Chưa có file nào được gán vào syllabus này.
              </div>

              <div v-else class="document-list">
                <article
                    v-for="file in danhSachFileMau"
                    :key="file.id"
                    class="document-item"
                >
                  <div class="document-info">
                    <strong>{{ file.tenFile || 'File không có tên' }}</strong>
                    <span>
                      {{ [file.loaiFile, formatKichThuoc(file.kichThuoc)].filter(Boolean).join(' · ') || 'Không rõ định dạng' }}
                    </span>
                  </div>

                  <div class="document-actions">
                    <button
                        type="button"
                        class="btn"
                        :disabled="!!loadingFile"
                        @click="xemFileMau(file)"
                    >
                      {{ loadingFile === `xem-${file.id}` ? 'Đang tải...' : 'Xem' }}
                    </button>

                    <button
                        type="button"
                        class="btn primary"
                        :disabled="!!loadingFile"
                        @click="taiFileMau(file)"
                    >
                      {{ loadingFile === `tai-${file.id}` ? 'Đang tải...' : 'Tải' }}
                    </button>
                  </div>
                </article>
              </div>
            </div>
          </article>

          <article class="accordion-card">
            <button type="button" class="accordion-head" @click="toggleBang('tai-lieu')">
              <div>
                <h3>Tài liệu học tập</h3>
                <p>Tài liệu, file hoặc link phục vụ môn học</p>
              </div>

              <span class="accordion-meta">
                {{ danhSachTaiLieu.length }} tài liệu
                <b>{{ dangMoBang('tai-lieu') ? '−' : '+' }}</b>
              </span>
            </button>

            <div v-if="dangMoBang('tai-lieu')" class="accordion-body">
              <div v-if="!danhSachTaiLieu.length" class="empty-inline">
                Chưa có tài liệu học tập theo syllabus áp dụng.
              </div>

              <div v-else class="document-list">
                <article
                    v-for="(taiLieu, index) in danhSachTaiLieu"
                    :key="index"
                    class="document-item"
                >
                  <div class="document-info">
                    <strong>{{ layTenTaiLieu(taiLieu, index) }}</strong>
                    <span v-if="layLinkTaiLieu(taiLieu)">Có file/link đính kèm</span>
                    <span v-else>Chưa có đường dẫn file</span>
                  </div>

                  <div class="document-actions">
                    <button
                        type="button"
                        class="btn"
                        :disabled="!layLinkTaiLieu(taiLieu)"
                        @click="xemTaiLieu(taiLieu)"
                    >
                      Xem
                    </button>

                    <button
                        type="button"
                        class="btn primary"
                        :disabled="!layLinkTaiLieu(taiLieu)"
                        @click="taiTaiLieu(taiLieu)"
                    >
                      Tải
                    </button>
                  </div>
                </article>
              </div>
            </div>
          </article>

          <article class="accordion-card">
            <button type="button" class="accordion-head" @click="toggleBang('link-syllabus')">
              <div>
                <h3>File / link syllabus</h3>
                <p>Đường dẫn syllabus nếu có</p>
              </div>

              <span class="accordion-meta">
                {{ linkSyllabus ? 'Có link' : 'Trống' }}
                <b>{{ dangMoBang('link-syllabus') ? '−' : '+' }}</b>
              </span>
            </button>

            <div v-if="dangMoBang('link-syllabus')" class="accordion-body">
              <div v-if="!linkSyllabus" class="empty-inline">
                Chưa có file hoặc link syllabus.
              </div>

              <button v-else type="button" class="btn primary" @click="moLienKet(linkSyllabus)">
                Xem / tải syllabus
              </button>
            </div>
          </article>
        </section>
      </template>
    </div>
  </template>

  <script setup>
  import { computed, onMounted, ref } from 'vue'
  import { useRoute, useRouter } from 'vue-router'
  import { daoTaoService } from '../../services/daoTaoService.js'
  import { layThongBaoLoi } from '../../utils/layThongBaoLoi.js'

  const route = useRoute()
  const router = useRouter()

  const payload = ref(null)
  const errorMessage = ref('')
  const loading = ref(false)
  const loadingFile = ref(null)
  const bangDangMo = ref('thong-tin')

  const tenMonHoc = computed(() => {
    return payload.value?.tenMon || 'Syllabus môn học'
  })

  const coSyllabus = computed(() => {
    return Boolean(payload.value?.coSyllabus)
  })

  const syllabus = computed(() => {
    return payload.value?.syllabus || {}
  })

  const mucTieuMonHoc = computed(() => {
    return layTruong(syllabus.value, [
      'mucTieu',
      'mucTieuMonHoc',
      'mucTieuHocPhan',
      'objectives'
    ])
  })

  const thongTinSyllabus = computed(() => {
    const item = syllabus.value || {}

    return [
      { label: 'Tên syllabus', value: layTruong(item, ['tenSyllabus', 'ten', 'tieuDe']) },
      { label: 'Mô tả', value: layTruong(item, ['moTa', 'description']) },
      { label: 'Vị trí môn học', value: layTruong(item, ['viTri', 'viTriMonHoc']) },
      { label: 'Tính chất', value: layTruong(item, ['tinhChat']) },
      { label: 'Phương pháp đánh giá', value: layTruong(item, ['phuongPhapDanhGia', 'danhGia']) },
      { label: 'Điều kiện hoàn thành', value: layTruong(item, ['dieuKienHoanThanh']) },
      { label: 'Hướng dẫn thực hiện', value: layTruong(item, ['huongDan', 'huongDanThucHien']) },
      { label: 'Ghi chú', value: layTruong(item, ['ghiChu']) }
    ].filter((row) => coGiaTri(row.value))
  })

  const danhSachChuongBai = computed(() => {
    return sapXepTheoThuTu(layMangDauTien(payload.value, ['chuongBai', 'danhSachChuongBai', 'noiDung', 'noiDungSyllabus']))
  })

  const danhSachChuanDauRa = computed(() => {
    return layMangDauTien(payload.value, ['chuanDauRa', 'nangLucDauRa', 'learningOutcomes', 'syllabus.chuanDauRa', 'syllabus.nangLucDauRa', 'syllabus.learningOutcomes'])
  })

  const danhSachTaiLieu = computed(() => {
    return layMangDauTien(payload.value, ['taiLieu', 'taiLieuHocTap', 'syllabusTaiLieu', 'tepDinhKem', 'files', 'syllabus.taiLieu', 'syllabus.taiLieuHocTap', 'syllabus.syllabusTaiLieu'])
  })

  const danhSachCauHinhDanhGia = computed(() => {
    return sapXepTheoThuTu(layMangDauTien(payload.value, ['cauHinhDanhGia', 'cotDiem', 'danhSachCotDiem', 'syllabus.cauHinhDanhGia', 'syllabus.cotDiem', 'syllabus.danhSachCotDiem']))
  })

  const tongTyLeDanhGia = computed(() => {
    const total = danhSachCauHinhDanhGia.value.reduce((sum, item) => {
      const value = Number(layTruong(item, ['tyLe', 'tiLe']) ?? 0)
      return sum + (Number.isNaN(value) ? 0 : value)
    }, 0)

    return Number(total.toFixed(2))
  })

  const danhSachQuyDoiDiem = computed(() => {
    return sapXepTheoThuTu(layMangDauTien(payload.value, ['quyDoiDiemTheoChuongTrinh', 'quyDoiDiem', 'quyDoiKetQua', 'bangQuyDoiDiem', 'syllabus.quyDoiDiemTheoChuongTrinh', 'syllabus.quyDoiDiem', 'syllabus.quyDoiKetQua']))
  })

  const danhSachDieuKien = computed(() => {
    return sapXepTheoThuTu(layMangDauTien(payload.value, ['dieuKien', 'dieuKienMonHoc', 'syllabusMonHocDieuKien', 'dieuKienSyllabus', 'syllabus.dieuKien', 'syllabus.dieuKienMonHoc', 'syllabus.syllabusMonHocDieuKien']))
  })

  const danhSachFileMau = computed(() => {
    return payload.value?.fileSyllabus || []
  })

  const linkSyllabus = computed(() => {
    return (
        layTruong(payload.value, ['duongDan', 'url', 'link', 'fileUrl']) ||
        layTruong(syllabus.value, ['duongDan', 'url', 'link', 'fileUrl'])
    )
  })

  onMounted(() => {
    docPayloadTuSession()
  })

  async function taiChiTietSyllabusNeuCan(basePayload = {}) {
    const syllabusId = basePayload?.syllabusId || basePayload?.syllabus?.id || route.query.syllabusMonHocId || route.query.syllabusId || null

    if (!syllabusId) {
      return basePayload
    }

    loading.value = true

    try {
      const res = await daoTaoService.syllabusMonHoc.getChiTietXem(syllabusId)
      const chiTiet = layDataTuApiResponse(res)
      const syllabus = chiTiet?.syllabusMonHoc || basePayload.syllabus || {}

      const syllabusMonHocMauId = syllabus?.syllabusMonHocMauId || null

      return {
        ...basePayload,
        coSyllabus: true,
        syllabusId,
        syllabusMonHocMauId,
        syllabus,
        soTinChi: basePayload.soTinChi ?? syllabus.soTinChi,
        gioLyThuyet: basePayload.gioLyThuyet ?? syllabus.gioLyThuyet,
        gioThucHanh: basePayload.gioThucHanh ?? syllabus.gioThucHanh,
        gioKiemTra: basePayload.gioKiemTra ?? syllabus.gioKiemTra,
        soBuoiHoc: basePayload.soBuoiHoc ?? syllabus.soBuoiHoc,
        chuongBai: chiTiet?.chuongBai || [],
        taiLieu: chiTiet?.taiLieu || [],
        cauHinhDanhGia: chiTiet?.cauHinhDanhGia || [],
        quyDoiDiemTheoChuongTrinh: chiTiet?.quyDoiDiemTheoChuongTrinh || [],
        quyDoiDiem: chiTiet?.quyDoiDiem || [],
        dieuKien: chiTiet?.dieuKienMonHoc || [],
        dieuKienMonHoc: chiTiet?.dieuKienMonHoc || [],
        fileSyllabus: Array.isArray(chiTiet?.fileSyllabus) ? chiTiet.fileSyllabus : []
      }
    } catch (error) {
      throw new Error(layThongBaoLoi(error, 'Không tải được chi tiết syllabus.'))
    } finally {
      loading.value = false
    }
  }

  async function docPayloadTuSession() {
    const refKey = route.query.ref

    try {
      let basePayload = {}

      if (refKey) {
        const raw = sessionStorage.getItem(String(refKey))

        if (!raw) {
          errorMessage.value = 'Không tìm thấy dữ liệu syllabus. Vui lòng quay lại và chọn lại môn học.'
          return
        }

        basePayload = JSON.parse(raw)
      } else if (route.query.syllabusMonHocId || route.query.syllabusId) {
        basePayload = {
          coSyllabus: true,
          syllabusId: route.query.syllabusMonHocId || route.query.syllabusId
        }
      } else {
        errorMessage.value = 'Thiếu tham chiếu dữ liệu syllabus.'
        return
      }

      payload.value = await taiChiTietSyllabusNeuCan(basePayload)
    } catch (error) {
      errorMessage.value = error?.message || 'Dữ liệu syllabus không hợp lệ.'
    }
  }

  function layDataTuApiResponse(res) {
    if (res?.success !== undefined && res?.data !== undefined) return res.data
    if (res?.data?.success !== undefined && res?.data?.data !== undefined) return res.data.data
    return res?.data || res
  }

  function quayLai() {
    router.back()
  }

  function toggleBang(key) {
    bangDangMo.value = bangDangMo.value === key ? '' : key
  }

  function dangMoBang(key) {
    return bangDangMo.value === key
  }

  function layTruong(item, keys = []) {
    if (!item) return null

    for (const key of keys) {
      const value = layNestedValue(item, key)
      if (coGiaTri(value)) return value
    }

    return null
  }

  function layNestedValue(item, path) {
    if (!path.includes('.')) return item?.[path]
    return path.split('.').reduce((current, key) => current?.[key], item)
  }

  function layMangDauTien(item, keys = []) {
    if (!item) return []

    for (const key of keys) {
      const value = layNestedValue(item, key)
      if (Array.isArray(value) && value.length > 0) return value
    }

    return []
  }

  function layTenChuong(chuong, index) {
    return (
        chuong?.tenChuong ||
        chuong?.tieuDe ||
        chuong?.ten ||
        chuong?.chuDe ||
        `Chương ${index + 1}`
    )
  }

  function layNoiDungChuong(chuong) {
    return chuong?.noiDung || chuong?.moTa || chuong?.ghiChu || ''
  }

  function laySoTietChuong(chuong) {
    return chuong?.soTiet || chuong?.thoiLuong || chuong?.tongGio || null
  }

  function hienThiNoiDung(item) {
    if (typeof item === 'string') return item
    return item?.noiDung || item?.moTa || item?.ten || item?.ma || '—'
  }

  function layTenTaiLieu(taiLieu, index) {
    return (
        taiLieu?.tenTaiLieu ||
        taiLieu?.ten ||
        taiLieu?.tieuDe ||
        taiLieu?.duongDan ||
        taiLieu?.url ||
        taiLieu?.link ||
        `Tài liệu ${index + 1}`
    )
  }

  function layThuTu(item, index) {
    return item?.thuTu ?? item?.stt ?? index + 1
  }

  function sapXepTheoThuTu(rows = []) {
    return [...rows].sort((a, b) => {
      const thuTuA = Number(a?.thuTu ?? a?.stt ?? 999999)
      const thuTuB = Number(b?.thuTu ?? b?.stt ?? 999999)

      if (thuTuA !== thuTuB) return thuTuA - thuTuB

      const tenA = String(a?.tenCotDiem || a?.ten || a?.ma || '')
      const tenB = String(b?.tenCotDiem || b?.ten || b?.ma || '')
      return tenA.localeCompare(tenB, 'vi')
    })
  }

  function hienThiBoolean(value) {
    if (value === true) return 'Có'
    if (value === false) return 'Không'
    return hienThi(value)
  }

  function layLinkTaiLieu(taiLieu) {
    return (
        taiLieu?.duongDan ||
        taiLieu?.url ||
        taiLieu?.link ||
        taiLieu?.fileUrl ||
        taiLieu?.duongDanTaiVe ||
        taiLieu?.duongDanCongKhai ||
        ''
    )
  }

  function xemTaiLieu(taiLieu) {
    const link = layLinkTaiLieu(taiLieu)
    if (!link) return

    moLienKet(chuyenLinkDownloadThanhPreview(link))
  }

  function taiTaiLieu(taiLieu) {
    const link = layLinkTaiLieu(taiLieu)
    if (!link) return

    moLienKet(chuyenLinkPreviewThanhDownload(link))
  }

  function chuyenLinkDownloadThanhPreview(link) {
    const rawLink = String(link || '').trim()

    if (!rawLink) return ''

    if (rawLink.includes('/download')) {
      return rawLink.replace('/download', '/preview')
    }

    return rawLink
  }

  function chuyenLinkPreviewThanhDownload(link) {
    const rawLink = String(link || '').trim()

    if (!rawLink) return ''

    if (rawLink.includes('/preview')) {
      return rawLink.replace('/preview', '/download')
    }

    return rawLink
  }

  function moLienKet(link) {
    if (!link) return
    window.open(link, '_blank')
  }

  async function xemFileMau(file) {
    loadingFile.value = `xem-${file.id}`

    try {
      const res = await daoTaoService.syllabusMonHocFile.view(file.id)
      const url = URL.createObjectURL(res.data)
      window.open(url, '_blank')
    } catch (_) {
      // ignore
    } finally {
      loadingFile.value = null
    }
  }

  async function taiFileMau(file) {
    loadingFile.value = `tai-${file.id}`

    try {
      const res = await daoTaoService.syllabusMonHocFile.download(file.id)
      const url = URL.createObjectURL(res.data)
      const a = document.createElement('a')
      a.href = url
      a.download = file.tenFile || 'syllabus'
      document.body.appendChild(a)
      a.click()
      document.body.removeChild(a)
      URL.revokeObjectURL(url)
    } catch (_) {
      // ignore
    } finally {
      loadingFile.value = null
    }
  }

  function formatKichThuoc(bytes) {
    if (!bytes) return ''
    if (bytes < 1024) return `${bytes} B`
    if (bytes < 1024 * 1024) return `${(bytes / 1024).toFixed(1)} KB`
    return `${(bytes / (1024 * 1024)).toFixed(1)} MB`
  }

  function coGiaTri(value) {
    return value !== null && value !== undefined && value !== ''
  }

  function hienThi(value) {
    if (!coGiaTri(value)) return '—'
    return value
  }
  </script>

  <style scoped>
  .dao-tao-xem-syllabus-page {
    --primary: #077149;
    --primary-dark: #055c3c;
    --primary-soft: #e7f5ef;
    --primary-soft-2: #f3fbf7;
    --primary-border: #a7d8c3;
    --text-main: #0f172a;
    --text-muted: #516173;
    --line: #d8e7df;
    --surface: #ffffff;
    --surface-soft: #f6faf8;

    min-height: calc(100vh - var(--header-height, 60px));
    padding: 8px 12px 16px;
    background:
        radial-gradient(circle at top left, rgba(7, 113, 73, 0.07), transparent 30%),
        linear-gradient(180deg, #f5fbf8 0%, #edf7f2 100%);
    color: var(--text-main);
    font-family: 'Roboto', Arial, Helvetica, sans-serif;
  }

  button {
    font-family: 'Roboto', Arial, Helvetica, sans-serif;
  }

  .compact-hero,
  .accordion-card,
  .state-card {
    border: 1px solid var(--primary-border);
    border-radius: 13px;
    background: var(--surface);
    box-shadow: 0 6px 18px rgba(7, 113, 73, 0.07);
  }

  /* =========================
     HERO
  ========================= */

  .compact-hero {
    display: grid;
    grid-template-columns: minmax(320px, 1fr) minmax(430px, 0.78fr);
    gap: 10px;
    align-items: center;
    padding: 9px 12px;
    margin-bottom: 8px;
    background:
        linear-gradient(135deg, rgba(255, 255, 255, 0.97) 0%, rgba(231, 245, 239, 0.95) 100%);
  }

  .hero-left {
    display: flex;
    min-width: 0;
    flex-direction: column;
    justify-content: center;
  }

  .back-btn {
    width: fit-content;
    margin: 0 0 5px;
    border: 0;
    background: transparent;
    color: var(--primary);
    font-size: 11.5px;
    font-weight: 800;
    cursor: pointer;
    padding: 0;
  }

  .back-btn:hover {
    color: var(--primary-dark);
    text-decoration: underline;
  }

  .hero-title-row {
    display: flex;
    align-items: flex-end;
    justify-content: space-between;
    gap: 10px;
    min-width: 0;
  }

  .hero-title-main {
    min-width: 0;
  }

  .eyebrow {
    margin: 0 0 2px;
    color: var(--primary);
    font-size: 10.5px;
    font-weight: 900;
    text-transform: uppercase;
    letter-spacing: 0.055em;
  }

  .compact-hero h1 {
    margin: 0;
    color: var(--text-main);
    font-size: 20px;
    line-height: 1.15;
    font-weight: 800;
    word-break: break-word;
  }

  .subtitle {
    margin: 4px 0 0;
    color: var(--text-muted);
    font-size: 12px;
    line-height: 1.35;
  }

  .summary-badges {
    display: flex;
    flex-wrap: wrap;
    justify-content: flex-end;
    gap: 5px;
    flex-shrink: 0;
  }

  .status-badge {
    display: inline-flex;
    align-items: center;
    min-height: 21px;
    border-radius: 999px;
    padding: 3px 8px;
    font-size: 11px;
    font-weight: 900;
    line-height: 1;
    white-space: nowrap;
  }

  .status-badge.success {
    border: 1px solid #86d7ad;
    background: #dff7eb;
    color: var(--primary-dark);
  }

  .status-badge.muted {
    border: 1px solid #d1d5db;
    background: #f8fafc;
    color: #64748b;
  }

  .status-badge.neutral {
    border: 1px solid var(--primary-border);
    background: var(--primary-soft);
    color: var(--primary);
  }

  /* =========================
     METRICS - COMPACT
  ========================= */

  .metric-grid {
    display: grid;
    gap: 6px;
  }

  .metric-grid.compact {
    grid-template-columns: repeat(5, minmax(72px, 1fr));
    align-content: center;
  }

  .metric-grid article {
    display: flex;
    min-width: 0;
    min-height: 42px;
    flex-direction: column;
    justify-content: center;
    border: 1px solid var(--primary-border);
    border-radius: 9px;
    background: rgba(255, 255, 255, 0.78);
    padding: 5px 8px;
  }

  .metric-grid span {
    display: block;
    color: var(--primary-dark);
    font-size: 10px;
    font-weight: 900;
    line-height: 1.15;
  }

  .metric-grid strong {
    display: block;
    margin-top: 2px;
    color: var(--primary);
    font-size: 15.5px;
    font-weight: 900;
    line-height: 1.05;
  }

  /* =========================
     ACCORDION
  ========================= */

  .accordion-list {
    display: grid;
    gap: 8px;
  }

  .accordion-card {
    overflow: hidden;
  }

  .accordion-head {
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 12px;
    width: 100%;
    border: 0;
    background: linear-gradient(135deg, #ffffff 0%, var(--primary-soft-2) 100%);
    padding: 9px 12px;
    text-align: left;
    cursor: pointer;
  }

  .accordion-head:hover {
    background: var(--primary-soft);
  }

  .accordion-head h3 {
    margin: 0;
    color: var(--primary);
    font-size: 14px;
    font-weight: 900;
    line-height: 1.25;
  }

  .accordion-head p {
    margin: 2px 0 0;
    color: var(--text-muted);
    font-size: 11.5px;
    line-height: 1.3;
  }

  .accordion-meta {
    display: inline-flex;
    align-items: center;
    gap: 7px;
    min-height: 24px;
    flex-shrink: 0;
    border: 1px solid var(--primary-border);
    border-radius: 999px;
    background: var(--primary-soft);
    color: var(--primary-dark);
    padding: 2px 5px 2px 9px;
    font-size: 11px;
    font-weight: 900;
    white-space: nowrap;
  }

  .accordion-meta.success {
    border-color: #86d7ad;
    background: #dcfce7;
    color: #166534;
  }

  .accordion-meta.warning {
    border-color: #fcd34d;
    background: #fffbeb;
    color: #92400e;
  }

  .accordion-meta b {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    width: 19px;
    height: 19px;
    border-radius: 999px;
    background: var(--primary);
    color: #ffffff;
    font-size: 14px;
    line-height: 1;
    font-weight: 900;
  }

  .accordion-body {
    padding: 10px;
    border-top: 1px solid var(--line);
    background: #ffffff;
  }

  /* =========================
     INFO
  ========================= */

  .info-list {
    display: grid;
    grid-template-columns: 145px minmax(0, 1fr);
    gap: 5px 10px;
    margin: 0;
  }

  .info-list dt {
    color: var(--primary-dark);
    font-size: 11.5px;
    font-weight: 900;
    line-height: 1.35;
  }

  .info-list dd {
    margin: 0;
    color: var(--text-main);
    font-size: 12px;
    line-height: 1.4;
    white-space: pre-line;
    word-break: break-word;
  }

  .rich-text {
    color: var(--text-main);
    font-size: 12px;
    line-height: 1.5;
    white-space: pre-line;
  }

  .empty-inline {
    padding: 8px 9px;
    border: 1px dashed var(--primary-border);
    border-radius: 10px;
    background: var(--primary-soft-2);
    color: var(--text-muted);
    font-size: 12px;
    line-height: 1.4;
  }

  /* =========================
     CHAPTERS
  ========================= */

  .chapter-list {
    display: grid;
    grid-template-columns: repeat(2, minmax(0, 1fr));
    gap: 7px;
  }

  .chapter-card {
    display: grid;
    grid-template-columns: 28px minmax(0, 1fr);
    gap: 8px;
    min-width: 0;
    border: 1px solid var(--line);
    border-radius: 10px;
    background: var(--surface-soft);
    padding: 8px;
  }

  .chapter-index {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    width: 26px;
    height: 26px;
    border-radius: 8px;
    background: var(--primary);
    color: #ffffff;
    font-size: 11.5px;
    font-weight: 900;
  }

  .chapter-content {
    min-width: 0;
  }

  .chapter-head {
    display: flex;
    align-items: flex-start;
    justify-content: space-between;
    gap: 8px;
    color: var(--text-main);
    font-size: 12px;
  }

  .chapter-head strong {
    color: var(--primary);
    font-weight: 900;
    line-height: 1.3;
    word-break: break-word;
  }

  .chapter-head span {
    flex-shrink: 0;
    color: var(--text-muted);
    font-size: 11px;
    font-weight: 800;
    white-space: nowrap;
  }

  .chapter-card p {
    margin: 4px 0 0;
    color: #334155;
    font-size: 12px;
    line-height: 1.4;
    white-space: pre-line;
  }

  /* =========================
     TABLES
  ========================= */

  .table-wrap {
    width: 100%;
    overflow: auto;
    border: 1px solid var(--line);
    border-radius: 10px;
    background: #ffffff;
  }

  .table-wrap table {
    width: 100%;
    min-width: 760px;
    border-collapse: separate;
    border-spacing: 0;
    font-size: 12px;
  }

  .table-wrap th,
  .table-wrap td {
    border-bottom: 1px solid #e7eee9;
    padding: 6px 7px;
    text-align: left;
    vertical-align: top;
    line-height: 1.35;
  }

  .table-wrap th {
    position: sticky;
    top: 0;
    z-index: 1;
    background: var(--primary-soft);
    color: var(--primary-dark);
    font-size: 11px;
    font-weight: 900;
    white-space: nowrap;
  }

  .table-wrap td {
    color: var(--text-main);
    word-break: break-word;
  }

  .table-wrap tbody tr:nth-child(even) td {
    background: #fbfefc;
  }

  .table-wrap tbody tr:hover td {
    background: #f1faf5;
  }

  .table-wrap tr:last-child td {
    border-bottom: 0;
  }

  .col-stt {
    width: 54px;
  }

  .col-code {
    width: 105px;
  }

  .col-short {
    width: 105px;
  }

  /* =========================
     DOCUMENTS
  ========================= */

  .document-list {
    display: grid;
    gap: 6px;
  }

  .document-item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 8px;
    border: 1px solid var(--line);
    border-radius: 10px;
    background: var(--surface-soft);
    padding: 7px 8px;
  }

  .document-info {
    display: grid;
    gap: 2px;
    min-width: 0;
  }

  .document-info strong {
    color: var(--primary);
    font-size: 12px;
    font-weight: 900;
    line-height: 1.3;
    word-break: break-word;
  }

  .document-info span {
    color: var(--text-muted);
    font-size: 11px;
    font-weight: 700;
  }

  .document-actions {
    display: flex;
    flex-shrink: 0;
    gap: 5px;
  }

  /* =========================
     BUTTONS / STATES
  ========================= */

  .btn {
    min-height: 28px;
    border: 1px solid var(--primary-border);
    border-radius: 9px;
    background: #ffffff;
    color: var(--primary-dark);
    padding: 5px 9px;
    font-size: 11.5px;
    font-weight: 800;
    cursor: pointer;
    white-space: nowrap;
    transition:
        background 0.15s ease,
        border-color 0.15s ease,
        color 0.15s ease,
        box-shadow 0.15s ease,
        transform 0.15s ease;
  }

  .btn:hover {
    border-color: var(--primary);
    background: var(--primary-soft);
    color: var(--primary);
    box-shadow: 0 4px 12px rgba(7, 113, 73, 0.12);
  }

  .btn.primary {
    border-color: var(--primary);
    background: var(--primary);
    color: #ffffff;
  }

  .btn.primary:hover {
    border-color: var(--primary-dark);
    background: var(--primary-dark);
    color: #ffffff;
  }

  .btn:disabled {
    cursor: not-allowed;
    opacity: 0.55;
    box-shadow: none;
    transform: none;
  }

  .state-card {
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 12px;
    padding: 12px;
    margin-bottom: 10px;
  }

  .state-card h3 {
    margin: 0 0 3px;
    color: var(--text-main);
    font-size: 14.5px;
    font-weight: 900;
  }

  .state-card p {
    margin: 0;
    color: var(--text-muted);
    font-size: 12px;
    line-height: 1.4;
  }

  .state-card.error {
    border-color: #fecaca;
    background: #fef2f2;
  }

  .state-card.error p {
    color: #991b1b;
  }

  .state-card.empty,
  .state-card.loading-card {
    border-color: var(--primary-border);
    background: var(--primary-soft-2);
  }

  /* =========================
     RESPONSIVE
  ========================= */

  @media (max-width: 1280px) {
    .compact-hero {
      grid-template-columns: 1fr;
    }

    .metric-grid.compact {
      grid-template-columns: repeat(5, minmax(0, 1fr));
    }
  }

  @media (max-width: 980px) {
    .chapter-list {
      grid-template-columns: 1fr;
    }

    .metric-grid.compact {
      grid-template-columns: repeat(3, minmax(0, 1fr));
    }

    .hero-title-row {
      align-items: flex-start;
      flex-direction: column;
    }

    .summary-badges {
      justify-content: flex-start;
    }
  }

  @media (max-width: 640px) {
    .dao-tao-xem-syllabus-page {
      padding: 8px;
    }

    .compact-hero,
    .accordion-card,
    .state-card {
      border-radius: 11px;
    }

    .compact-hero {
      padding: 9px;
    }

    .compact-hero h1 {
      font-size: 19px;
    }

    .metric-grid.compact {
      grid-template-columns: repeat(2, minmax(0, 1fr));
    }

    .metric-grid article {
      min-height: 40px;
    }

    .metric-grid strong {
      font-size: 15px;
    }

    .accordion-head {
      align-items: flex-start;
      flex-direction: column;
      gap: 7px;
    }

    .accordion-meta {
      align-self: flex-start;
    }

    .info-list {
      grid-template-columns: 1fr;
      gap: 3px;
    }

    .info-list dt {
      margin-top: 4px;
    }

    .chapter-head {
      flex-direction: column;
      gap: 3px;
    }

    .chapter-head span {
      white-space: normal;
    }

    .document-item,
    .state-card {
      align-items: stretch;
      flex-direction: column;
    }

    .document-actions {
      justify-content: flex-start;
      flex-wrap: wrap;
    }
  }
  </style>