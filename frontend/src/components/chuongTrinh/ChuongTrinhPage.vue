<template>
  <section class="ctdt-page">
    <header class="hero">
      <div class="hero-copy">
        <p class="eyebrow">Quản lý chương trình đào tạo</p>
        <h1>Chương trình đào tạo</h1>
        <p class="hero-text">
          Giao diện tổng hợp theo đúng cấu trúc backend và dữ liệu DB: chương trình, phiên bản,
          syllabus, nhóm kiến thức, môn học trong chương trình và các điều kiện liên quan.
        </p>
        <div class="path-row">
          <span>Đường dẫn</span>
          <strong>{{ activePath }}</strong>
        </div>
      </div>

      <div class="hero-actions">
        <button class="primary-btn" type="button" @click="openProgramCreate">Thêm chương trình</button>
        <button class="ghost-btn" type="button" @click="openVersionCreate" :disabled="!selectedProgram">
          Thêm phiên bản
        </button>
      </div>
    </header>

    <section class="stats-grid">
      <article class="stat-tile">
        <span class="stat-label">Ngành đang lọc</span>
        <strong>{{ currentNganhLabel }}</strong>
      </article>
      <article class="stat-tile">
        <span class="stat-label">Số chương trình</span>
        <strong>{{ filteredPrograms.length }}</strong>
      </article>
      <article class="stat-tile">
        <span class="stat-label">Phiên bản</span>
        <strong>{{ selectedProgramVersions.length }}</strong>
      </article>
      <article class="stat-tile">
        <span class="stat-label">Môn trong phiên bản</span>
        <strong>{{ versionCourses.length }}</strong>
      </article>
    </section>

    <div v-if="error" class="feedback error">{{ error }}</div>
    <div v-if="success" class="feedback success">{{ success }}</div>

    <section class="workspace">
      <aside class="program-rail">
        <div class="panel-head">
          <div>
            <p class="mini-label">Danh mục</p>
            <h2>Chương trình</h2>
          </div>
          <button class="icon-btn" type="button" @click="reloadAll" :disabled="loading">Tải lại</button>
        </div>

        <div class="filter-box">
          <input
            v-model.trim="keyword"
            class="search-input"
            type="text"
            placeholder="Tìm theo mã, tên, đối tượng tuyển sinh..."
          />
        </div>

        <div class="program-list">
          <button
            v-for="program in filteredPrograms"
            :key="program.id"
            type="button"
            class="program-item"
            :class="{ active: Number(program.id) === selectedProgramId }"
            @click="selectProgram(program.id)"
          >
            <span class="program-code">{{ program.maChuongTrinh || 'Chưa có mã' }}</span>
            <strong>{{ program.tenChuongTrinh || 'Chưa có tên chương trình' }}</strong>
            <small>{{ buildProgramMeta(program) }}</small>
          </button>

          <div v-if="!filteredPrograms.length && !loading" class="empty-state">
            Không có chương trình phù hợp với bộ lọc hiện tại.
          </div>
        </div>
      </aside>

      <div class="workspace-main">
        <section class="summary-panel">
          <div class="summary-head">
            <div>
              <p class="mini-label">Tổng quan chương trình</p>
              <h2>{{ selectedProgram?.tenChuongTrinh || 'Chưa chọn chương trình' }}</h2>
            </div>
            <div class="summary-actions" v-if="selectedProgram">
              <button class="ghost-btn" type="button" @click="openProgramEdit(selectedProgram)">Sửa chương trình</button>
              <button class="danger-btn" type="button" @click="removeProgram(selectedProgram)">Xóa</button>
            </div>
          </div>

          <div v-if="selectedProgram" class="summary-grid">
            <div class="info-block">
              <span>Mã chương trình</span>
              <strong>{{ selectedProgram.maChuongTrinh }}</strong>
            </div>
            <div class="info-block">
              <span>Ngành</span>
              <strong>{{ getNganhName(selectedProgram.nganhId) }}</strong>
            </div>
            <div class="info-block">
              <span>Trình độ</span>
              <strong>{{ getTrinhDoName(selectedProgram.trinhDoId) }}</strong>
            </div>
            <div class="info-block">
              <span>Loại chương trình</span>
              <strong>{{ getLoaiName(selectedProgram.loaiChuongTrinhId) }}</strong>
            </div>
            <div class="info-block">
              <span>Thời gian đào tạo</span>
              <strong>{{ formatText(selectedProgram.thoiGianDaoTao) }}</strong>
            </div>
            <div class="info-block full">
              <span>Đối tượng tuyển sinh</span>
              <strong>{{ formatText(selectedProgram.doiTuongTuyenSinh) }}</strong>
            </div>
          </div>
          <div v-else class="empty-state large">
            Chọn một chương trình bên trái để xem chi tiết và dữ liệu liên quan.
          </div>
        </section>

        <section class="version-panel">
          <div class="panel-head">
            <div>
              <p class="mini-label">Phiên bản ban hành</p>
              <h2>Version chương trình</h2>
            </div>
            <button class="ghost-btn" type="button" @click="openVersionCreate" :disabled="!selectedProgram">
              Thêm version
            </button>
          </div>

          <div class="version-tabs" v-if="selectedProgramVersions.length">
            <button
              v-for="version in selectedProgramVersions"
              :key="version.id"
              type="button"
              class="version-chip"
              :class="{ active: Number(version.id) === selectedVersionId }"
              @click="selectVersion(version.id)"
            >
              <span>{{ version.maVersion || 'Version' }}</span>
              <small>{{ version.soQuyetDinh || 'Chưa có số quyết định' }}</small>
            </button>
          </div>

          <div v-if="selectedVersion" class="version-grid">
            <article class="version-card version-card-hero">
              <div class="version-card-head">
                <div>
                  <p class="mini-label">Phiên bản đang xem</p>
                  <h3>{{ selectedVersion.tenVersion || selectedVersion.maVersion }}</h3>
                </div>
                <div class="inline-actions">
                  <button class="ghost-btn" type="button" @click="openVersionEdit(selectedVersion)">Sửa</button>
                  <button class="danger-btn" type="button" @click="removeVersion(selectedVersion)">Xóa</button>
                </div>
              </div>

              <div class="kv-grid">
                <div><span>Mã version</span><strong>{{ formatText(selectedVersion.maVersion) }}</strong></div>
                <div><span>Số quyết định</span><strong>{{ formatText(selectedVersion.soQuyetDinh) }}</strong></div>
                <div><span>Ngày quyết định</span><strong>{{ formatDate(selectedVersion.ngayQuyetDinh) }}</strong></div>
                <div><span>Ngày áp dụng</span><strong>{{ formatDate(selectedVersion.ngayApDung) }}</strong></div>
                <div><span>Người ký</span><strong>{{ formatText(selectedVersion.nguoiKy) }}</strong></div>
                <div><span>Cơ quan ban hành</span><strong>{{ formatText(selectedVersion.coQuanBanHanh) }}</strong></div>
                <div><span>Tổng tín chỉ</span><strong>{{ formatNumber(selectedVersion.tongTinChi) }}</strong></div>
                <div><span>Tổng số giờ</span><strong>{{ formatNumber(selectedVersion.tongSoGio) }}</strong></div>
                <div><span>Lý thuyết</span><strong>{{ formatNumber(selectedVersion.tongGioLyThuyet) }}</strong></div>
                <div><span>Thực hành</span><strong>{{ formatNumber(selectedVersion.tongGioThucHanh) }}</strong></div>
                <div><span>Kiểm tra</span><strong>{{ formatNumber(selectedVersion.tongGioKiemTra) }}</strong></div>
                <div><span>Hiện hành</span><strong>{{ selectedVersion.laHienHanh ? 'Có' : 'Không' }}</strong></div>
              </div>
            </article>

            <article class="version-card">
              <p class="mini-label">Syllabus chương trình</p>
              <h3>Tổng quan và phương pháp đào tạo</h3>
              <dl class="detail-list">
                <div>
                  <dt>Mô tả tổng quan</dt>
                  <dd>{{ formatText(selectedSyllabus?.moTaTongQuan) }}</dd>
                </div>
                <div>
                  <dt>Mục đích</dt>
                  <dd>{{ formatText(selectedSyllabus?.mucDich) }}</dd>
                </div>
                <div>
                  <dt>Yêu cầu đào tạo</dt>
                  <dd>{{ formatText(selectedSyllabus?.yeuCauDaoTao) }}</dd>
                </div>
                <div>
                  <dt>Phương pháp đào tạo</dt>
                  <dd>{{ formatText(selectedSyllabus?.phuongPhapDaoTao) }}</dd>
                </div>
                <div>
                  <dt>Ghi chú</dt>
                  <dd>{{ formatText(selectedSyllabus?.ghiChu) }}</dd>
                </div>
              </dl>
            </article>
          </div>
          <div v-else class="empty-state">
            Chương trình này chưa có phiên bản để hiển thị.
          </div>
        </section>

        <section class="detail-grid">
          <article class="detail-panel">
            <div class="panel-head compact">
              <div>
                <p class="mini-label">Theo DB</p>
                <h2>Mục tiêu chương trình</h2>
              </div>
              <span class="pill">{{ selectedObjectives.length }}</span>
            </div>
            <ul class="text-list">
              <li v-for="item in selectedObjectives" :key="item.id">
                <strong>{{ mapMucTieuLoai(item.loai) }}</strong>
                <span>{{ item.noiDung }}</span>
              </li>
              <li v-if="!selectedObjectives.length" class="empty-inline">Chưa có mục tiêu.</li>
            </ul>
          </article>

          <article class="detail-panel">
            <div class="panel-head compact">
              <div>
                <p class="mini-label">Theo DB</p>
                <h2>Năng lực đầu ra</h2>
              </div>
              <span class="pill">{{ selectedCompetencies.length }}</span>
            </div>
            <ul class="text-list">
              <li v-for="item in selectedCompetencies" :key="item.id">
                <strong>{{ [item.ma, mapNangLucLoai(item.loai)].filter(Boolean).join(' • ') }}</strong>
                <span>{{ item.noiDung }}</span>
              </li>
              <li v-if="!selectedCompetencies.length" class="empty-inline">Chưa có năng lực đầu ra.</li>
            </ul>
          </article>

          <article class="detail-panel">
            <div class="panel-head compact">
              <div>
                <p class="mini-label">Theo DB</p>
                <h2>Vị trí việc làm</h2>
              </div>
              <span class="pill">{{ selectedJobPositions.length }}</span>
            </div>
            <ul class="text-list">
              <li v-for="item in selectedJobPositions" :key="item.id">
                <strong>{{ item.ten }}</strong>
                <span>{{ formatText(item.moTa) }}</span>
              </li>
              <li v-if="!selectedJobPositions.length" class="empty-inline">Chưa có vị trí việc làm.</li>
            </ul>
          </article>

          <article class="detail-panel">
            <div class="panel-head compact">
              <div>
                <p class="mini-label">Theo DB</p>
                <h2>Điều kiện tốt nghiệp</h2>
              </div>
              <span class="pill">{{ selectedGraduationConditions.length }}</span>
            </div>
            <ul class="text-list">
              <li v-for="item in selectedGraduationConditions" :key="item.id">
                <strong>Điều kiện {{ item.thuTu || '-' }}</strong>
                <span>{{ item.noiDung }}</span>
              </li>
              <li v-if="!selectedGraduationConditions.length" class="empty-inline">Chưa có điều kiện tốt nghiệp.</li>
            </ul>
          </article>
        </section>

        <section class="table-panel">
          <div class="panel-head">
            <div>
              <p class="mini-label">Theo version hiện chọn</p>
              <h2>Nhóm kiến thức</h2>
            </div>
            <span class="path-badge">{{ activePath }} / nhóm-kiến-thức</span>
          </div>

          <div class="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>Mã nhóm</th>
                  <th>Tên nhóm</th>
                  <th>Loại nhóm</th>
                  <th>Thứ tự</th>
                  <th>Tổng tín chỉ</th>
                  <th>Tổng số giờ</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="item in selectedGroups" :key="item.id">
                  <td>{{ formatText(item.ma) }}</td>
                  <td>{{ formatText(item.ten) }}</td>
                  <td>{{ formatText(item.loaiNhom) }}</td>
                  <td>{{ formatNumber(item.thuTu) }}</td>
                  <td>{{ formatNumber(item.tongTinChi) }}</td>
                  <td>{{ formatNumber(item.tongSoGio) }}</td>
                </tr>
                <tr v-if="!selectedGroups.length">
                  <td colspan="6" class="empty-row">Chưa có nhóm kiến thức.</td>
                </tr>
              </tbody>
            </table>
          </div>
        </section>

        <section class="course-layout">
          <article class="table-panel">
            <div class="panel-head">
              <div>
                <p class="mini-label">Theo version hiện chọn</p>
                <h2>Môn trong chương trình</h2>
              </div>
              <span class="path-badge">{{ activePath }} / môn-học</span>
            </div>

            <div class="table-wrap">
              <table>
                <thead>
                  <tr>
                    <th>Mã trong CT</th>
                    <th>Tên môn</th>
                    <th>Kỳ</th>
                    <th>Nhóm kiến thức</th>
                    <th>Tín chỉ</th>
                    <th>Tổng giờ</th>
                    <th>Bắt buộc</th>
                  </tr>
                </thead>
                <tbody>
                  <tr
                    v-for="item in versionCourses"
                    :key="item.id"
                    class="clickable-row"
                    :class="{ selected: Number(item.id) === selectedCourseId }"
                    @click="selectCourse(item.id)"
                  >
                    <td>{{ formatText(item.maMonTrongCt) }}</td>
                    <td>{{ getMonHocName(item.monHocId) }}</td>
                    <td>{{ getKhungKyName(item.khungKyId) }}</td>
                    <td>{{ getNhomKienThucName(item.nhomKienThucId) }}</td>
                    <td>{{ formatNumber(item.soTinChi) }}</td>
                    <td>{{ formatNumber(item.tongGio) }}</td>
                    <td>{{ item.batBuoc ? 'Có' : 'Không' }}</td>
                  </tr>
                  <tr v-if="!versionCourses.length">
                    <td colspan="7" class="empty-row">Chưa có môn trong chương trình.</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </article>

          <article class="inspector">
            <div class="panel-head compact">
              <div>
                <p class="mini-label">Chi tiết môn học</p>
                <h2>{{ selectedCourse ? getMonHocName(selectedCourse.monHocId) : 'Chưa chọn môn' }}</h2>
              </div>
              <span class="pill">{{ selectedCourse ? selectedCourse.maMonTrongCt : '---' }}</span>
            </div>

            <div v-if="selectedCourse" class="inspector-body">
              <div class="info-stack">
                <div><span>Mã môn danh mục</span><strong>{{ getMonHocCode(selectedCourse.monHocId) }}</strong></div>
                <div><span>Loại</span><strong>{{ formatText(selectedCourse.loai) }}</strong></div>
                <div><span>Loại học phần</span><strong>{{ formatText(selectedCourse.loaiHocPhan) }}</strong></div>
                <div><span>Là môn điều kiện</span><strong>{{ selectedCourse.laMonDieuKien ? 'Có' : 'Không' }}</strong></div>
                <div><span>Ghi chú</span><strong>{{ formatText(selectedCourse.ghiChu) }}</strong></div>
              </div>

              <div class="sub-section">
                <h3>Syllabus môn học</h3>
                <dl class="detail-list">
                  <div>
                    <dt>Vị trí</dt>
                    <dd>{{ formatText(selectedSyllabusMon?.viTri) }}</dd>
                  </div>
                  <div>
                    <dt>Tính chất</dt>
                    <dd>{{ formatText(selectedSyllabusMon?.tinhChat) }}</dd>
                  </div>
                  <div>
                    <dt>Mục tiêu</dt>
                    <dd>{{ formatText(selectedSyllabusMon?.mucTieu) }}</dd>
                  </div>
                  <div>
                    <dt>Phương pháp đánh giá</dt>
                    <dd>{{ formatText(selectedSyllabusMon?.phuongPhapDanhGia) }}</dd>
                  </div>
                  <div>
                    <dt>Điều kiện hoàn thành</dt>
                    <dd>{{ formatText(selectedSyllabusMon?.dieuKienHoanThanh) }}</dd>
                  </div>
                  <div>
                    <dt>Hướng dẫn</dt>
                    <dd>{{ formatText(selectedSyllabusMon?.huongDan) }}</dd>
                  </div>
                </dl>
              </div>

              <div class="sub-section">
                <h3>Môn tiên quyết</h3>
                <ul class="text-list compact-list">
                  <li v-for="item in selectedPrerequisites" :key="item.id">
                    <strong>{{ getChuongTrinhMonName(item.monDieuKienId) }}</strong>
                    <span>{{ formatText(item.loai) }}{{ item.ghiChu ? ` • ${item.ghiChu}` : '' }}</span>
                  </li>
                  <li v-if="!selectedPrerequisites.length" class="empty-inline">Chưa có môn tiên quyết.</li>
                </ul>
              </div>

              <div class="sub-section">
                <h3>Điều kiện môn học</h3>
                <ul class="text-list compact-list">
                  <li v-for="item in selectedCourseConditions" :key="item.id">
                    <strong>{{ formatText(item.loai) }}</strong>
                    <span>{{ formatText(item.noiDung) }}</span>
                  </li>
                  <li v-if="!selectedCourseConditions.length" class="empty-inline">Chưa có điều kiện môn học.</li>
                </ul>
              </div>

              <div class="sub-section">
                <h3>Quy đổi điểm</h3>
                <div class="mini-table">
                  <div class="mini-table-head">
                    <span>Từ</span>
                    <span>Đến</span>
                    <span>Điểm quy đổi</span>
                    <span>Kết quả</span>
                  </div>
                  <div v-for="item in selectedScoreConversions" :key="item.id" class="mini-table-row">
                    <span>{{ formatNumber(item.nguongTu) }}</span>
                    <span>{{ formatNumber(item.nguongDen) }}</span>
                    <span>{{ formatNumber(item.diemQuyDoi) }}</span>
                    <span>{{ formatText(item.ketQua) }}</span>
                  </div>
                  <div v-if="!selectedScoreConversions.length" class="empty-inline">Chưa có quy đổi điểm.</div>
                </div>
              </div>
            </div>
            <div v-else class="empty-state large">
              Chọn một môn học để xem syllabus và các điều kiện liên quan.
            </div>
          </article>
        </section>
      </div>
    </section>

    <div v-if="showProgramForm" class="modal-backdrop" @click.self="closeProgramForm">
      <div class="modal">
        <div class="modal-head">
          <div>
            <p class="mini-label">Biểu mẫu</p>
            <h2>{{ editingProgramId ? 'Cập nhật chương trình' : 'Thêm chương trình' }}</h2>
          </div>
          <button class="icon-close" type="button" @click="closeProgramForm">Dong</button>
        </div>

        <form class="form-grid" @submit.prevent="saveProgram">
          <label>
            <span>Ngành</span>
            <select v-model="programForm.nganhId" required>
              <option value="">Chọn ngành</option>
              <option v-for="item in nganhList" :key="item.id" :value="item.id">
                {{ item.maNganh }} - {{ item.tenNganh }}
              </option>
            </select>
          </label>

          <label>
            <span>Trình độ đào tạo</span>
            <select v-model="programForm.trinhDoId" required>
              <option value="">Chọn trình độ</option>
              <option v-for="item in trinhDoList" :key="item.id" :value="item.id">
                {{ item.maTrinhDo }} - {{ item.tenTrinhDo }}
              </option>
            </select>
          </label>

          <label>
            <span>Loại chương trình</span>
            <select v-model="programForm.loaiChuongTrinhId" required>
              <option value="">Chọn loại chương trình</option>
              <option v-for="item in loaiChuongTrinhList" :key="item.id" :value="item.id">
                {{ item.maLoai }} - {{ item.tenLoai }}
              </option>
            </select>
          </label>

          <label>
            <span>Mã chương trình</span>
            <input v-model.trim="programForm.maChuongTrinh" type="text" required />
          </label>

          <label class="wide">
            <span>Tên chương trình</span>
            <input v-model.trim="programForm.tenChuongTrinh" type="text" required />
          </label>

          <label class="wide">
            <span>Đối tượng tuyển sinh</span>
            <textarea v-model.trim="programForm.doiTuongTuyenSinh" rows="3"></textarea>
          </label>

          <label>
            <span>Thời gian đào tạo</span>
            <input v-model.trim="programForm.thoiGianDaoTao" type="text" placeholder="Ví dụ: 2 năm / 4 học kỳ" />
          </label>

          <div class="modal-actions wide">
            <button class="primary-btn" type="submit" :disabled="savingProgram">
              {{ savingProgram ? 'Đang lưu...' : 'Lưu chương trình' }}
            </button>
            <button class="ghost-btn" type="button" @click="closeProgramForm">Hủy</button>
          </div>
        </form>
      </div>
    </div>

    <div v-if="showVersionForm" class="modal-backdrop" @click.self="closeVersionForm">
      <div class="modal">
        <div class="modal-head">
          <div>
            <p class="mini-label">Biểu mẫu</p>
            <h2>{{ editingVersionId ? 'Cập nhật phiên bản' : 'Thêm phiên bản' }}</h2>
          </div>
          <button class="icon-close" type="button" @click="closeVersionForm">Dong</button>
        </div>

        <form class="form-grid" @submit.prevent="saveVersion">
          <label class="wide">
            <span>Chương trình</span>
            <select v-model="versionForm.chuongTrinhId" required>
              <option value="">Chọn chương trình</option>
              <option v-for="item in filteredPrograms" :key="item.id" :value="item.id">
                {{ item.maChuongTrinh }} - {{ item.tenChuongTrinh }}
              </option>
            </select>
          </label>

          <label>
            <span>Mã version</span>
            <input v-model.trim="versionForm.maVersion" type="text" required />
          </label>

          <label>
            <span>Tên version</span>
            <input v-model.trim="versionForm.tenVersion" type="text" />
          </label>

          <label>
            <span>Ngày áp dụng</span>
            <input v-model="versionForm.ngayApDung" type="date" />
          </label>

          <label>
            <span>Ngày hết hiệu lực</span>
            <input v-model="versionForm.ngayHetHieuLuc" type="date" />
          </label>

          <label>
            <span>Số quyết định</span>
            <input v-model.trim="versionForm.soQuyetDinh" type="text" />
          </label>

          <label>
            <span>Ngày quyết định</span>
            <input v-model="versionForm.ngayQuyetDinh" type="date" />
          </label>

          <label>
            <span>Người ký</span>
            <input v-model.trim="versionForm.nguoiKy" type="text" />
          </label>

          <label>
            <span>Cơ quan ban hành</span>
            <input v-model.trim="versionForm.coQuanBanHanh" type="text" />
          </label>

          <label>
            <span>File quyết định</span>
            <input v-model.trim="versionForm.fileQuyetDinh" type="text" />
          </label>

          <label>
            <span>Tổng tín chỉ</span>
            <input v-model="versionForm.tongTinChi" type="number" step="0.1" />
          </label>

          <label>
            <span>Tổng số giờ</span>
            <input v-model="versionForm.tongSoGio" type="number" step="0.1" />
          </label>

          <label>
            <span>Tổng giờ lý thuyết</span>
            <input v-model="versionForm.tongGioLyThuyet" type="number" step="0.1" />
          </label>

          <label>
            <span>Tổng giờ thực hành</span>
            <input v-model="versionForm.tongGioThucHanh" type="number" step="0.1" />
          </label>

          <label>
            <span>Tổng giờ kiểm tra</span>
            <input v-model="versionForm.tongGioKiemTra" type="number" step="0.1" />
          </label>

          <label class="checkbox-field">
            <input v-model="versionForm.laHienHanh" type="checkbox" />
            <span>Đánh dấu là phiên bản hiện hành</span>
          </label>

          <div class="modal-actions wide">
            <button class="primary-btn" type="submit" :disabled="savingVersion">
              {{ savingVersion ? 'Đang lưu...' : 'Lưu phiên bản' }}
            </button>
            <button class="ghost-btn" type="button" @click="closeVersionForm">Hủy</button>
          </div>
        </form>
      </div>
    </div>
  </section>
</template>

<script setup>
import { computed, onMounted, reactive, ref, watch } from 'vue';
import { useRoute } from 'vue-router';
import { getAllChuongTrinh } from '@/api/chuongTrinh/ApiRespone/ChuongTrinhController.js';
import { createChuongTrinh, deleteChuongTrinh, updateChuongTrinh } from '@/api/chuongTrinh/ApiRequest/ChuongTrinhController.js';
import { getAllChuongTrinhVersion } from '@/api/chuongTrinh/ApiRespone/ChuongTrinhVersionController.js';
import { createChuongTrinhVersion, deleteChuongTrinhVersion, updateChuongTrinhVersion } from '@/api/chuongTrinh/ApiRequest/ChuongTrinhVersionController.js';
import { getAllSyllabusChuongTrinh } from '@/api/chuongTrinh/ApiRespone/SyllabusChuongTrinhController.js';
import { getAllMucTieuChuongTrinh } from '@/api/chuongTrinh/ApiRespone/MucTieuChuongTrinhController.js';
import { getAllNangLucDauRa } from '@/api/chuongTrinh/ApiRespone/NangLucDauRaController.js';
import { getAllViTriViecLam } from '@/api/chuongTrinh/ApiRespone/ViTriViecLamController.js';
import { getAllNhomKienThuc } from '@/api/chuongTrinh/ApiRespone/NhomKienThucController.js';
import { getAllChuongTrinhMon } from '@/api/chuongTrinh/ApiRespone/ChuongTrinhMonController.js';
import { getAllMonHoc } from '@/api/chuongTrinh/ApiRespone/MonHocController.js';
import { getAllMonTienQuyet } from '@/api/chuongTrinh/ApiRespone/MonTienQuyetController.js';
import { getAllSyllabusMonHoc } from '@/api/chuongTrinh/ApiRespone/SyllabusMonHocController.js';
import { getAllDieuKienMonHoc } from '@/api/chuongTrinh/ApiRespone/DieuKienMonHocController.js';
import { getAllQuyDoiDiem } from '@/api/chuongTrinh/ApiRespone/QuyDoiDiemController.js';
import { getAllDieuKienTotNghiep } from '@/api/chuongTrinh/ApiRespone/DieuKienTotNghiepController.js';
import { getAllNganh } from '@/api/daoTao/ApiRespone/NganhController.js';
import { getAllTrinhDoDaoTao } from '@/api/daoTao/ApiRespone/TrinhDoDaoTaoController.js';
import { getAllLoaiChuongTrinh } from '@/api/daoTao/ApiRespone/LoaiChuongTrinhController.js';
import { getAllKhungKy } from '@/api/daoTao/ApiRespone/KhungKyController.js';
import { getErrorMessage, toNumberOrNull, unwrapApiList } from '@/api/apiResponse.js';

const route = useRoute();

const loading = ref(false);
const error = ref('');
const success = ref('');
const keyword = ref('');

const programs = ref([]);
const versions = ref([]);
const syllabusPrograms = ref([]);
const objectives = ref([]);
const competencies = ref([]);
const jobPositions = ref([]);
const knowledgeGroups = ref([]);
const curriculumCourses = ref([]);
const subjects = ref([]);
const prerequisites = ref([]);
const syllabusSubjects = ref([]);
const courseConditions = ref([]);
const scoreConversions = ref([]);
const graduationConditions = ref([]);
const nganhList = ref([]);
const trinhDoList = ref([]);
const loaiChuongTrinhList = ref([]);
const khungKyList = ref([]);

const showProgramForm = ref(false);
const showVersionForm = ref(false);
const savingProgram = ref(false);
const savingVersion = ref(false);
const editingProgramId = ref(null);
const editingVersionId = ref(null);

const selectedProgramId = ref(null);
const selectedVersionId = ref(null);
const selectedCourseId = ref(null);

const currentNganhId = computed(() => (route.params.nganhId ? Number(route.params.nganhId) : null));

const programForm = reactive({
  nganhId: '',
  trinhDoId: '',
  loaiChuongTrinhId: '',
  maChuongTrinh: '',
  tenChuongTrinh: '',
  doiTuongTuyenSinh: '',
  thoiGianDaoTao: '',
});

const versionForm = reactive({
  chuongTrinhId: '',
  maVersion: '',
  tenVersion: '',
  ngayApDung: '',
  ngayHetHieuLuc: '',
  soQuyetDinh: '',
  ngayQuyetDinh: '',
  nguoiKy: '',
  coQuanBanHanh: '',
  fileQuyetDinh: '',
  tongTinChi: '',
  tongSoGio: '',
  tongGioLyThuyet: '',
  tongGioThucHanh: '',
  tongGioKiemTra: '',
  laHienHanh: false,
});

const activePath = computed(() => {
  const base = currentNganhId.value ? `dao-tao/nganh/${currentNganhId.value}/chuong-trinh` : 'chuong-trinh/chuong-trinh';
  const versionCode = selectedVersion.value?.maVersion ? `/${selectedVersion.value.maVersion}` : '';
  const courseCode = selectedCourse.value?.maMonTrongCt ? `/${selectedCourse.value.maMonTrongCt}` : '';
  return `${base}${versionCode}${courseCode}`;
});

const currentNganhLabel = computed(() => {
  if (!currentNganhId.value) return 'Tất cả ngành';
  return getNganhName(currentNganhId.value);
});

const nganhMap = computed(() => buildMap(nganhList.value));
const trinhDoMap = computed(() => buildMap(trinhDoList.value));
const loaiMap = computed(() => buildMap(loaiChuongTrinhList.value));
const khungKyMap = computed(() => buildMap(khungKyList.value));
const subjectMap = computed(() => buildMap(subjects.value));
const groupMap = computed(() => buildMap(knowledgeGroups.value));
const programCourseMap = computed(() => buildMap(curriculumCourses.value));

const filteredPrograms = computed(() => {
  const normalizedKeyword = normalize(keyword.value);

  return programs.value
    .filter((item) => !currentNganhId.value || Number(item.nganhId) === currentNganhId.value)
    .filter((item) => {
      if (!normalizedKeyword) return true;
      return [
        item.maChuongTrinh,
        item.tenChuongTrinh,
        item.doiTuongTuyenSinh,
        item.thoiGianDaoTao,
        getNganhName(item.nganhId),
        getLoaiName(item.loaiChuongTrinhId),
      ].some((value) => normalize(value).includes(normalizedKeyword));
    })
    .sort((a, b) => String(a.maChuongTrinh || '').localeCompare(String(b.maChuongTrinh || '')));
});

const selectedProgram = computed(() => filteredPrograms.value.find((item) => Number(item.id) === selectedProgramId.value) || null);

const selectedProgramVersions = computed(() => {
  if (!selectedProgram.value) return [];

  return versions.value
    .filter((item) => Number(item.chuongTrinhId) === Number(selectedProgram.value.id))
    .sort((a, b) => {
      if (Boolean(b.laHienHanh) !== Boolean(a.laHienHanh)) return Number(Boolean(b.laHienHanh)) - Number(Boolean(a.laHienHanh));
      return String(b.ngayQuyetDinh || b.ngayApDung || '').localeCompare(String(a.ngayQuyetDinh || a.ngayApDung || ''));
    });
});

const selectedVersion = computed(() => selectedProgramVersions.value.find((item) => Number(item.id) === selectedVersionId.value) || null);
const selectedSyllabus = computed(() => syllabusPrograms.value.find((item) => Number(item.chuongTrinhVersionId) === selectedVersionId.value) || null);

const selectedObjectives = computed(() => {
  return objectives.value
    .filter((item) => Number(item.chuongTrinhVersionId) === selectedVersionId.value)
    .sort((a, b) => Number(a.thuTu || 0) - Number(b.thuTu || 0));
});

const selectedCompetencies = computed(() => {
  return competencies.value
    .filter((item) => Number(item.chuongTrinhVersionId) === selectedVersionId.value)
    .sort((a, b) => Number(a.thuTu || 0) - Number(b.thuTu || 0));
});

const selectedJobPositions = computed(() => {
  return jobPositions.value
    .filter((item) => Number(item.chuongTrinhVersionId) === selectedVersionId.value)
    .sort((a, b) => Number(a.thuTu || 0) - Number(b.thuTu || 0));
});

const selectedGraduationConditions = computed(() => {
  return graduationConditions.value
    .filter((item) => Number(item.chuongTrinhVersionId) === selectedVersionId.value)
    .sort((a, b) => Number(a.thuTu || 0) - Number(b.thuTu || 0));
});

const selectedGroups = computed(() => {
  return knowledgeGroups.value
    .filter((item) => Number(item.chuongTrinhVersionId) === selectedVersionId.value)
    .sort((a, b) => Number(a.thuTu || 0) - Number(b.thuTu || 0));
});

const versionCourses = computed(() => {
  return curriculumCourses.value
    .filter((item) => Number(item.chuongTrinhVersionId) === selectedVersionId.value)
    .sort((a, b) => Number(a.thuTu || 0) - Number(b.thuTu || 0));
});

const selectedCourse = computed(() => versionCourses.value.find((item) => Number(item.id) === selectedCourseId.value) || null);
const selectedSyllabusMon = computed(() => syllabusSubjects.value.find((item) => Number(item.chuongTrinhMonId) === selectedCourseId.value) || null);

const selectedPrerequisites = computed(() => {
  return prerequisites.value.filter((item) => Number(item.monId) === selectedCourseId.value);
});

const selectedCourseConditions = computed(() => {
  if (!selectedSyllabusMon.value) return [];
  return courseConditions.value
    .filter((item) => Number(item.syllabusMonId) === Number(selectedSyllabusMon.value.id))
    .sort((a, b) => Number(a.thuTu || 0) - Number(b.thuTu || 0));
});

const selectedScoreConversions = computed(() => {
  return scoreConversions.value.filter((item) => Number(item.chuongTrinhMonId) === selectedCourseId.value);
});

watch(filteredPrograms, (items) => {
  if (!items.length) {
    selectedProgramId.value = null;
    return;
  }

  const exists = items.some((item) => Number(item.id) === selectedProgramId.value);
  if (!exists) {
    selectedProgramId.value = Number(items[0].id);
  }
}, { immediate: true });

watch(selectedProgramVersions, (items) => {
  if (!items.length) {
    selectedVersionId.value = null;
    return;
  }

  const exists = items.some((item) => Number(item.id) === selectedVersionId.value);
  if (!exists) {
    const current = items.find((item) => item.laHienHanh);
    selectedVersionId.value = Number((current || items[0]).id);
  }
}, { immediate: true });

watch(versionCourses, (items) => {
  if (!items.length) {
    selectedCourseId.value = null;
    return;
  }

  const exists = items.some((item) => Number(item.id) === selectedCourseId.value);
  if (!exists) {
    selectedCourseId.value = Number(items[0].id);
  }
}, { immediate: true });

watch(currentNganhId, () => {
  if (currentNganhId.value) {
    programForm.nganhId = currentNganhId.value;
  }
});

const reloadAll = async () => {
  loading.value = true;
  error.value = '';
  success.value = '';

  try {
    const [
      programData,
      versionData,
      syllabusData,
      objectiveData,
      competencyData,
      jobData,
      groupData,
      courseData,
      subjectData,
      prerequisiteData,
      syllabusSubjectData,
      conditionData,
      conversionData,
      graduationData,
      nganhData,
      trinhDoData,
      loaiData,
      khungKyData,
    ] = await Promise.all([
      getAllChuongTrinh(),
      getAllChuongTrinhVersion(),
      getAllSyllabusChuongTrinh(),
      getAllMucTieuChuongTrinh(),
      getAllNangLucDauRa(),
      getAllViTriViecLam(),
      getAllNhomKienThuc(),
      getAllChuongTrinhMon(),
      getAllMonHoc(),
      getAllMonTienQuyet(),
      getAllSyllabusMonHoc(),
      getAllDieuKienMonHoc(),
      getAllQuyDoiDiem(),
      getAllDieuKienTotNghiep(),
      getAllNganh(),
      getAllTrinhDoDaoTao(),
      getAllLoaiChuongTrinh(),
      getAllKhungKy(),
    ]);

    programs.value = unwrapApiList(programData);
    versions.value = unwrapApiList(versionData);
    syllabusPrograms.value = unwrapApiList(syllabusData);
    objectives.value = unwrapApiList(objectiveData);
    competencies.value = unwrapApiList(competencyData);
    jobPositions.value = unwrapApiList(jobData);
    knowledgeGroups.value = unwrapApiList(groupData);
    curriculumCourses.value = unwrapApiList(courseData);
    subjects.value = unwrapApiList(subjectData);
    prerequisites.value = unwrapApiList(prerequisiteData);
    syllabusSubjects.value = unwrapApiList(syllabusSubjectData);
    courseConditions.value = unwrapApiList(conditionData);
    scoreConversions.value = unwrapApiList(conversionData);
    graduationConditions.value = unwrapApiList(graduationData);
    nganhList.value = unwrapApiList(nganhData);
    trinhDoList.value = unwrapApiList(trinhDoData);
    loaiChuongTrinhList.value = unwrapApiList(loaiData);
    khungKyList.value = unwrapApiList(khungKyData);
  } catch (err) {
    error.value = getErrorMessage(err);
  } finally {
    loading.value = false;
  }
};

const resetProgramForm = () => {
  editingProgramId.value = null;
  Object.assign(programForm, {
    nganhId: currentNganhId.value || '',
    trinhDoId: '',
    loaiChuongTrinhId: '',
    maChuongTrinh: '',
    tenChuongTrinh: '',
    doiTuongTuyenSinh: '',
    thoiGianDaoTao: '',
  });
};

const resetVersionForm = () => {
  editingVersionId.value = null;
  Object.assign(versionForm, {
    chuongTrinhId: selectedProgram.value?.id || '',
    maVersion: '',
    tenVersion: '',
    ngayApDung: '',
    ngayHetHieuLuc: '',
    soQuyetDinh: '',
    ngayQuyetDinh: '',
    nguoiKy: '',
    coQuanBanHanh: '',
    fileQuyetDinh: '',
    tongTinChi: '',
    tongSoGio: '',
    tongGioLyThuyet: '',
    tongGioThucHanh: '',
    tongGioKiemTra: '',
    laHienHanh: false,
  });
};

const openProgramCreate = () => {
  resetProgramForm();
  showProgramForm.value = true;
};

const openProgramEdit = (item) => {
  editingProgramId.value = item.id;
  Object.assign(programForm, {
    nganhId: item.nganhId ?? '',
    trinhDoId: item.trinhDoId ?? '',
    loaiChuongTrinhId: item.loaiChuongTrinhId ?? '',
    maChuongTrinh: item.maChuongTrinh ?? '',
    tenChuongTrinh: item.tenChuongTrinh ?? '',
    doiTuongTuyenSinh: item.doiTuongTuyenSinh ?? '',
    thoiGianDaoTao: item.thoiGianDaoTao ?? '',
  });
  showProgramForm.value = true;
};

const closeProgramForm = () => {
  showProgramForm.value = false;
  resetProgramForm();
};

const openVersionCreate = () => {
  if (!selectedProgram.value) return;
  resetVersionForm();
  versionForm.chuongTrinhId = selectedProgram.value.id;
  showVersionForm.value = true;
};

const openVersionEdit = (item) => {
  editingVersionId.value = item.id;
  Object.assign(versionForm, {
    chuongTrinhId: item.chuongTrinhId ?? '',
    maVersion: item.maVersion ?? '',
    tenVersion: item.tenVersion ?? '',
    ngayApDung: item.ngayApDung ?? '',
    ngayHetHieuLuc: item.ngayHetHieuLuc ?? '',
    soQuyetDinh: item.soQuyetDinh ?? '',
    ngayQuyetDinh: item.ngayQuyetDinh ?? '',
    nguoiKy: item.nguoiKy ?? '',
    coQuanBanHanh: item.coQuanBanHanh ?? '',
    fileQuyetDinh: item.fileQuyetDinh ?? '',
    tongTinChi: item.tongTinChi ?? '',
    tongSoGio: item.tongSoGio ?? '',
    tongGioLyThuyet: item.tongGioLyThuyet ?? '',
    tongGioThucHanh: item.tongGioThucHanh ?? '',
    tongGioKiemTra: item.tongGioKiemTra ?? '',
    laHienHanh: Boolean(item.laHienHanh),
  });
  showVersionForm.value = true;
};

const closeVersionForm = () => {
  showVersionForm.value = false;
  resetVersionForm();
};

const saveProgram = async () => {
  savingProgram.value = true;
  error.value = '';
  success.value = '';

  try {
    const payload = cleanPayload({
      nganhId: toNumberOrNull(programForm.nganhId),
      trinhDoId: toNumberOrNull(programForm.trinhDoId),
      loaiChuongTrinhId: toNumberOrNull(programForm.loaiChuongTrinhId),
      maChuongTrinh: programForm.maChuongTrinh,
      tenChuongTrinh: programForm.tenChuongTrinh,
      doiTuongTuyenSinh: programForm.doiTuongTuyenSinh,
      thoiGianDaoTao: programForm.thoiGianDaoTao,
    });

    if (editingProgramId.value) {
      await updateChuongTrinh(editingProgramId.value, payload);
      success.value = 'Đã cập nhật chương trình đào tạo.';
    } else {
      await createChuongTrinh(payload);
      success.value = 'Đã thêm chương trình đào tạo mới.';
    }

    closeProgramForm();
    await reloadAll();
  } catch (err) {
    error.value = getErrorMessage(err);
  } finally {
    savingProgram.value = false;
  }
};

const saveVersion = async () => {
  savingVersion.value = true;
  error.value = '';
  success.value = '';

  try {
    const payload = cleanPayload({
      chuongTrinhId: toNumberOrNull(versionForm.chuongTrinhId),
      maVersion: versionForm.maVersion,
      tenVersion: versionForm.tenVersion,
      ngayApDung: versionForm.ngayApDung,
      ngayHetHieuLuc: versionForm.ngayHetHieuLuc,
      soQuyetDinh: versionForm.soQuyetDinh,
      ngayQuyetDinh: versionForm.ngayQuyetDinh,
      nguoiKy: versionForm.nguoiKy,
      coQuanBanHanh: versionForm.coQuanBanHanh,
      fileQuyetDinh: versionForm.fileQuyetDinh,
      tongTinChi: toNumberOrNull(versionForm.tongTinChi),
      tongSoGio: toNumberOrNull(versionForm.tongSoGio),
      tongGioLyThuyet: toNumberOrNull(versionForm.tongGioLyThuyet),
      tongGioThucHanh: toNumberOrNull(versionForm.tongGioThucHanh),
      tongGioKiemTra: toNumberOrNull(versionForm.tongGioKiemTra),
      laHienHanh: Boolean(versionForm.laHienHanh),
    });

    if (editingVersionId.value) {
      await updateChuongTrinhVersion(editingVersionId.value, payload);
      success.value = 'Đã cập nhật version chương trình.';
    } else {
      await createChuongTrinhVersion(payload);
      success.value = 'Đã thêm version chương trình.';
    }

    closeVersionForm();
    await reloadAll();
  } catch (err) {
    error.value = getErrorMessage(err);
  } finally {
    savingVersion.value = false;
  }
};

const removeProgram = async (item) => {
  if (!item?.id) return;
  if (!window.confirm(`Xóa chương trình ${item.tenChuongTrinh || item.maChuongTrinh}?`)) return;

  try {
    await deleteChuongTrinh(item.id);
    success.value = 'Đã xóa chương trình đào tạo.';
    await reloadAll();
  } catch (err) {
    error.value = getErrorMessage(err);
  }
};

const removeVersion = async (item) => {
  if (!item?.id) return;
  if (!window.confirm(`Xóa version ${item.maVersion || item.tenVersion}?`)) return;

  try {
    await deleteChuongTrinhVersion(item.id);
    success.value = 'Đã xóa version chương trình.';
    await reloadAll();
  } catch (err) {
    error.value = getErrorMessage(err);
  }
};

const selectProgram = (id) => {
  selectedProgramId.value = Number(id);
};

const selectVersion = (id) => {
  selectedVersionId.value = Number(id);
};

const selectCourse = (id) => {
  selectedCourseId.value = Number(id);
};

function buildMap(items) {
  return Object.fromEntries((items || []).map((item) => [Number(item.id), item]));
}

function normalize(value) {
  return String(value ?? '').trim().toLowerCase();
}

function cleanPayload(payload) {
  return Object.fromEntries(
    Object.entries(payload).map(([key, value]) => {
      if (value === '' || value === undefined) return [key, null];
      return [key, value];
    }),
  );
}

function formatText(value) {
  return value === null || value === undefined || value === '' ? 'Chưa cập nhật' : value;
}

function formatNumber(value) {
  if (value === null || value === undefined || value === '') return '0';
  return String(value);
}

function formatDate(value) {
  if (!value) return 'Chưa cập nhật';
  return value;
}

function getNganhName(id) {
  const item = nganhMap.value[Number(id)];
  return item ? `${item.maNganh} - ${item.tenNganh}` : 'Chưa gán ngành';
}

function getTrinhDoName(id) {
  const item = trinhDoMap.value[Number(id)];
  return item ? `${item.maTrinhDo} - ${item.tenTrinhDo}` : 'Chưa gán trình độ';
}

function getLoaiName(id) {
  const item = loaiMap.value[Number(id)];
  return item ? `${item.maLoai} - ${item.tenLoai}` : 'Chưa gán loại chương trình';
}

function getKhungKyName(id) {
  const item = khungKyMap.value[Number(id)];
  return item ? `${item.maKy} - ${item.tenKy}` : 'Chưa gán kỳ';
}

function getMonHocName(id) {
  const item = subjectMap.value[Number(id)];
  return item ? item.tenMon : 'Chưa gán môn học';
}

function getMonHocCode(id) {
  const item = subjectMap.value[Number(id)];
  return item?.maMon || 'Chưa có mã';
}

function getNhomKienThucName(id) {
  const item = groupMap.value[Number(id)];
  return item ? `${item.ma || 'NKT'} - ${item.ten}` : 'Chưa gán nhóm';
}

function getChuongTrinhMonName(id) {
  const item = programCourseMap.value[Number(id)];
  if (!item) return 'Chưa xác định';
  return `${item.maMonTrongCt || 'Môn'} - ${getMonHocName(item.monHocId)}`;
}

function mapMucTieuLoai(value) {
  const map = {
    chung: 'Mục tiêu chung',
    kien_thuc: 'Kiến thức',
    ky_nang: 'Kỹ năng',
    nang_luc_tu_chu_trach_nhiem: 'Năng lực tự chủ và trách nhiệm',
  };
  return map[value] || formatText(value);
}

function mapNangLucLoai(value) {
  const map = {
    co_ban: 'Cơ bản',
    cot_loi: 'Cốt lõi',
    nang_cao: 'Nâng cao',
  };
  return map[value] || formatText(value);
}

function buildProgramMeta(program) {
  return [
    getNganhName(program.nganhId),
    getLoaiName(program.loaiChuongTrinhId),
    formatText(program.thoiGianDaoTao),
  ].join(' • ');
}

onMounted(reloadAll);
</script>

<style scoped>
.ctdt-page {
  padding: 24px;
  color: #102033;
  background:
    linear-gradient(180deg, #f6fbff 0%, #eef3f8 38%, #f7f8fa 100%);
  min-height: 100%;
}

.hero {
  display: flex;
  justify-content: space-between;
  gap: 24px;
  padding: 28px;
  border-radius: 24px;
  background:
    linear-gradient(140deg, rgba(8, 47, 73, 0.96), rgba(21, 94, 117, 0.9)),
    linear-gradient(120deg, #0b3b5a, #164e63);
  color: #f8fbff;
  box-shadow: 0 24px 60px rgba(8, 47, 73, 0.22);
}

.hero-copy {
  max-width: 760px;
}

.eyebrow,
.mini-label {
  margin: 0 0 8px;
  font-size: 12px;
  font-weight: 700;
  letter-spacing: 0.08em;
  text-transform: uppercase;
}

.eyebrow {
  color: #9bd5ff;
}

.mini-label {
  color: #57718d;
}

.hero h1,
.summary-head h2,
.panel-head h2,
.version-card h3,
.inspector h2 {
  margin: 0;
}

.hero h1 {
  font-size: 34px;
  line-height: 1.08;
}

.hero-text {
  margin: 14px 0 0;
  max-width: 700px;
  color: rgba(248, 251, 255, 0.84);
  line-height: 1.7;
}

.path-row {
  display: inline-flex;
  align-items: center;
  gap: 10px;
  margin-top: 18px;
  padding: 10px 14px;
  border: 1px solid rgba(155, 213, 255, 0.22);
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.08);
  font-size: 13px;
}

.path-row span {
  color: rgba(248, 251, 255, 0.72);
}

.hero-actions,
.summary-actions,
.inline-actions,
.modal-actions {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}

.hero-actions {
  align-items: flex-start;
}

.primary-btn,
.ghost-btn,
.danger-btn,
.icon-btn,
.icon-close {
  border: 0;
  border-radius: 12px;
  cursor: pointer;
  font-weight: 700;
  transition: transform 0.18s ease, opacity 0.18s ease, background 0.18s ease;
}

.primary-btn,
.ghost-btn,
.danger-btn,
.icon-btn {
  min-height: 42px;
  padding: 0 16px;
}

.primary-btn {
  background: #f59e0b;
  color: #172554;
}

.ghost-btn {
  background: rgba(18, 52, 77, 0.08);
  color: #0f3a56;
}

.hero .ghost-btn {
  background: rgba(255, 255, 255, 0.14);
  color: #f8fbff;
}

.danger-btn {
  background: #fee2e2;
  color: #b91c1c;
}

.icon-btn {
  background: #e2edf7;
  color: #17324d;
}

.icon-close {
  width: 40px;
  height: 40px;
  background: #edf3f9;
  color: #17324d;
}

.primary-btn:hover,
.ghost-btn:hover,
.danger-btn:hover,
.icon-btn:hover,
.icon-close:hover {
  transform: translateY(-1px);
  opacity: 0.92;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 14px;
  margin-top: 18px;
}

.stat-tile {
  padding: 18px;
  border-radius: 18px;
  background: rgba(255, 255, 255, 0.82);
  border: 1px solid rgba(188, 208, 224, 0.7);
  backdrop-filter: blur(10px);
}

.stat-label {
  display: block;
  margin-bottom: 8px;
  color: #5b728b;
  font-size: 13px;
}

.stat-tile strong {
  font-size: 24px;
}

.feedback {
  margin-top: 16px;
  padding: 14px 16px;
  border-radius: 14px;
  font-weight: 600;
}

.feedback.error {
  color: #991b1b;
  background: #fee2e2;
}

.feedback.success {
  color: #166534;
  background: #dcfce7;
}

.workspace {
  display: grid;
  grid-template-columns: 320px minmax(0, 1fr);
  gap: 18px;
  margin-top: 18px;
}

.program-rail,
.summary-panel,
.version-panel,
.detail-panel,
.table-panel,
.inspector {
  background: rgba(255, 255, 255, 0.9);
  border: 1px solid rgba(191, 209, 223, 0.82);
  border-radius: 22px;
  box-shadow: 0 18px 40px rgba(15, 35, 58, 0.08);
}

.program-rail {
  padding: 18px;
  display: flex;
  flex-direction: column;
  min-height: 700px;
}

.workspace-main {
  display: grid;
  gap: 18px;
}

.panel-head,
.summary-head,
.modal-head {
  display: flex;
  justify-content: space-between;
  gap: 16px;
  align-items: flex-start;
}

.panel-head.compact {
  align-items: center;
}

.search-input,
.form-grid input,
.form-grid select,
.form-grid textarea {
  width: 100%;
  border: 1px solid #cad8e4;
  border-radius: 14px;
  background: #fff;
  padding: 12px 14px;
  font: inherit;
  color: #102033;
}

.search-input:focus,
.form-grid input:focus,
.form-grid select:focus,
.form-grid textarea:focus {
  outline: none;
  border-color: #2f7ea8;
  box-shadow: 0 0 0 3px rgba(47, 126, 168, 0.12);
}

.filter-box {
  margin-top: 16px;
}

.program-list {
  display: grid;
  gap: 10px;
  margin-top: 16px;
  overflow: auto;
  padding-right: 4px;
}

.program-item {
  padding: 16px;
  border: 1px solid #d8e3ec;
  border-radius: 16px;
  background: linear-gradient(180deg, #ffffff, #f8fbfd);
  text-align: left;
  cursor: pointer;
  transition: border-color 0.18s ease, transform 0.18s ease, box-shadow 0.18s ease;
}

.program-item:hover,
.program-item.active {
  border-color: #2f7ea8;
  transform: translateY(-1px);
  box-shadow: 0 16px 32px rgba(47, 126, 168, 0.12);
}

.program-item strong,
.program-item small,
.program-code {
  display: block;
}

.program-code {
  margin-bottom: 8px;
  color: #0f5d84;
  font-size: 12px;
  font-weight: 800;
  letter-spacing: 0.06em;
  text-transform: uppercase;
}

.program-item strong {
  font-size: 16px;
  line-height: 1.4;
}

.program-item small {
  margin-top: 8px;
  color: #597289;
  line-height: 1.5;
}

.summary-panel,
.version-panel,
.detail-panel,
.table-panel,
.inspector {
  padding: 20px;
}

.summary-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 14px;
  margin-top: 18px;
}

.info-block {
  padding: 16px;
  border-radius: 18px;
  background: linear-gradient(180deg, #f7fafc, #f0f6fa);
}

.info-block span,
.kv-grid span,
.info-stack span {
  display: block;
  margin-bottom: 6px;
  color: #597289;
  font-size: 13px;
}

.info-block strong,
.kv-grid strong,
.info-stack strong {
  display: block;
  line-height: 1.5;
}

.info-block.full {
  grid-column: 1 / -1;
}

.version-tabs {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
  margin-top: 18px;
}

.version-chip {
  padding: 12px 14px;
  border: 1px solid #d7e2ea;
  border-radius: 16px;
  background: #f9fbfc;
  cursor: pointer;
  text-align: left;
  transition: all 0.18s ease;
}

.version-chip span,
.version-chip small {
  display: block;
}

.version-chip span {
  font-weight: 800;
}

.version-chip small {
  margin-top: 4px;
  color: #6d8194;
}

.version-chip.active,
.version-chip:hover {
  border-color: #2f7ea8;
  background: #eef7fb;
}

.version-grid {
  display: grid;
  grid-template-columns: 1.15fr 0.85fr;
  gap: 16px;
  margin-top: 18px;
}

.version-card {
  padding: 18px;
  border-radius: 20px;
  background: linear-gradient(180deg, #fbfdff, #f4f8fb);
  border: 1px solid #dce7ee;
}

.version-card-hero {
  background:
    linear-gradient(180deg, rgba(255, 255, 255, 0.92), rgba(239, 248, 251, 0.96)),
    linear-gradient(135deg, #f9fcff, #edf8ff);
}

.version-card-head {
  display: flex;
  justify-content: space-between;
  gap: 12px;
  align-items: flex-start;
}

.kv-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 14px;
  margin-top: 18px;
}

.detail-list {
  display: grid;
  gap: 12px;
  margin: 16px 0 0;
}

.detail-list dt {
  color: #4f6478;
  font-size: 13px;
  font-weight: 700;
}

.detail-list dd {
  margin: 6px 0 0;
  line-height: 1.6;
}

.detail-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 18px;
}

.text-list {
  display: grid;
  gap: 12px;
  margin: 18px 0 0;
  padding: 0;
  list-style: none;
}

.text-list li {
  padding: 14px 16px;
  border-radius: 16px;
  background: #f8fbfd;
}

.text-list strong,
.text-list span {
  display: block;
}

.text-list span {
  margin-top: 6px;
  color: #496277;
  line-height: 1.6;
}

.pill,
.path-badge {
  display: inline-flex;
  align-items: center;
  border-radius: 999px;
  font-size: 12px;
  font-weight: 800;
}

.pill {
  min-height: 32px;
  padding: 0 12px;
  background: #e7f4fb;
  color: #0f5d84;
}

.path-badge {
  padding: 8px 12px;
  background: #eef4f8;
  color: #446176;
}

.table-wrap {
  margin-top: 16px;
  overflow: auto;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th,
td {
  padding: 14px 12px;
  border-bottom: 1px solid #dbe6ee;
  text-align: left;
  vertical-align: top;
  line-height: 1.5;
}

th {
  color: #4a6277;
  font-size: 13px;
  white-space: nowrap;
}

.empty-row,
.empty-inline,
.empty-state {
  color: #6a7f92;
}

.empty-state {
  padding: 28px 0;
  text-align: center;
}

.empty-state.large {
  padding: 42px 18px;
}

.course-layout {
  display: grid;
  grid-template-columns: 1.15fr 0.85fr;
  gap: 18px;
}

.clickable-row {
  cursor: pointer;
  transition: background 0.18s ease;
}

.clickable-row:hover,
.clickable-row.selected {
  background: #eef7fb;
}

.inspector-body {
  margin-top: 18px;
  display: grid;
  gap: 18px;
}

.info-stack {
  display: grid;
  gap: 12px;
}

.sub-section {
  padding-top: 4px;
}

.sub-section h3 {
  margin: 0 0 12px;
  font-size: 17px;
}

.compact-list li {
  padding: 12px 14px;
}

.mini-table {
  display: grid;
  gap: 8px;
}

.mini-table-head,
.mini-table-row {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 10px;
  padding: 12px 14px;
  border-radius: 14px;
  background: #f8fbfd;
}

.mini-table-head {
  color: #4a6277;
  font-size: 13px;
  font-weight: 700;
}

.modal-backdrop {
  position: fixed;
  inset: 0;
  z-index: 50;
  background: rgba(9, 27, 40, 0.45);
  backdrop-filter: blur(6px);
  display: grid;
  place-items: center;
  padding: 18px;
}

.modal {
  width: min(920px, 100%);
  max-height: calc(100vh - 36px);
  overflow: auto;
  background: #fbfdff;
  border-radius: 26px;
  padding: 22px;
  box-shadow: 0 32px 80px rgba(9, 27, 40, 0.28);
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 14px;
  margin-top: 18px;
}

.form-grid label {
  display: grid;
  gap: 8px;
}

.form-grid label span {
  font-size: 13px;
  font-weight: 700;
  color: #496277;
}

.form-grid .wide,
.modal-actions {
  grid-column: 1 / -1;
}

.checkbox-field {
  display: flex !important;
  align-items: center;
  gap: 10px;
}

.checkbox-field input {
  width: 18px;
  height: 18px;
  margin: 0;
}

@media (max-width: 1280px) {
  .detail-grid,
  .course-layout,
  .version-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 980px) {
  .workspace,
  .stats-grid,
  .summary-grid,
  .form-grid {
    grid-template-columns: 1fr;
  }

  .hero,
  .panel-head,
  .summary-head,
  .modal-head,
  .version-card-head {
    flex-direction: column;
  }

  .program-rail {
    min-height: auto;
  }
}
</style>
