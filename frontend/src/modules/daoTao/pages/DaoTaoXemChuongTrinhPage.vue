<template>
  <div class="xay-dung-page dao-tao-xem-page">
    <div v-if="thongBao" :class="['notice', loaiThongBao]">{{ thongBao }}</div>

    <nav class="tang-nav mini-scroll">
      <button
          v-for="tang in cacTangDaoTao"
          :key="tang.so"
          type="button"
          :class="['tang-btn', { active: tang.so === tangHienTai }]"
          @click="veTang(tang.so)"
      >
        <b>Tầng {{ tang.so }}</b>
        <span>{{ tang.ten }}</span>
      </button>
    </nav>

    <div class="dao-tao-sticky-flow-header">
      <div class="flow-summary breadcrumb-flow mini-scroll">
        <button
            v-for="muc in tomTatDieuHuong"
            :key="muc.key"
            type="button"
            :class="['breadcrumb-pill', { active: muc.tang === tangHienTai, disabled: !muc.enabled }]"
            :disabled="!muc.enabled"
            :title="muc.enabled ? `Bấm để quay về ${muc.tenTang}` : 'Chưa có dữ liệu để đi tới mục này'"
            @click="diChuyenTheoTomTat(muc)"
        >
          <span>{{ muc.label }}:</span>
          <b>{{ muc.value || '-' }}</b>
        </button>
      </div>
    </div>


    <section v-for="group in groups" :key="group.key" :class="['flow-group', group.mau]">
      <div class="group-title">
        <h3>{{ group.title }}</h3>
        <p>{{ group.description }}</p>
      </div>

      <div v-if="tangHienTai === 9" class="syllabus-ap-dung-toolbar">
      </div>

      <template v-for="bang in group.tables" :key="bang.key">

        <!-- Panel gợi ý khung kỳ (tầng 6) -->
        <div
            v-if="tangHienTai === 6 && bang.key === 'khungKy' && selected.chuongTrinhVersion"
            class="khung-ky-goi-y-panel"
        >
          <div v-if="dangLayGoiY" class="goi-y-loading">Đang tải gợi ý kỳ học...</div>
          <template v-else-if="khungKyGoiY">
            <div :class="['goi-y-status', khungKyGoiY.daTaoDuKy ? 'goi-y-ok' : 'goi-y-warn']">
              <span class="goi-y-msg">{{ khungKyGoiY.message }}</span>
              <span class="goi-y-count">
                {{ khungKyGoiY.soKyDaTao }}/{{ khungKyGoiY.soKy }} kỳ
                ({{ khungKyGoiY.soThang }} tháng)
              </span>
            </div>
            <div v-if="!khungKyGoiY.daTaoDuKy && khungKyGoiY.kyTiepTheoGoiY" class="goi-y-next">
              <span>Kỳ tiếp theo cần tạo:</span>
              <strong>{{ khungKyGoiY.kyTiepTheoGoiY.tenKy }} ({{ khungKyGoiY.kyTiepTheoGoiY.maKy }})</strong>
              — từ <strong>{{ khungKyGoiY.kyTiepTheoGoiY.ngayBatDauGoiY }}</strong>
              đến <strong>{{ khungKyGoiY.kyTiepTheoGoiY.ngayKetThucGoiY }}</strong>
              <button
                  type="button"
                  class="btn small primary"
                  :disabled="dangTaoDuKy"
                  @click="taoDuKyConThieu"
              >{{ dangTaoDuKy ? 'Đang tạo...' : `Tạo đủ ${khungKyGoiY.soKyConThieu} kỳ còn thiếu` }}</button>
            </div>
            <div class="goi-y-list">
              <span
                  v-for="item in khungKyGoiY.danhSachKy"
                  :key="item.thuTu"
                  :class="['goi-y-ky-badge', item.daTonTai ? 'da-tao' : 'chua-tao']"
                  :title="item.daTonTai ? `${item.ngayBatDauHienTai} → ${item.ngayKetThucHienTai}` : `Gợi ý: ${item.ngayBatDauGoiY} → ${item.ngayKetThucGoiY}`"
              >{{ item.maKy }}</span>
            </div>
          </template>
        </div>
        <!-- End panel gợi ý -->

        <!-- Panel cảnh báo tải học theo kỳ (tầng 6) -->
        <div
            v-if="tangHienTai === 6 && bang.key === 'khungKy' && selected.chuongTrinhVersion"
            class="tai-hoc-panel"
        >
          <div class="tai-hoc-header" @click="taiHocMoRong = !taiHocMoRong">
            <span class="tai-hoc-title">Cảnh báo tải học theo kỳ</span>
            <span v-if="canhBaoTaiHoc" class="tai-hoc-summary">
              <span v-for="item in canhBaoTaiHoc.danhSachKy.filter(k => !k.chuaXepKy)" :key="item.maKy"
                    :class="['tai-badge', `tai-${item.mucDoTai?.toLowerCase()}`]"
                    :title="item.canhBao?.join('\n')">
                {{ item.maKy }}
              </span>
            </span>
            <span class="tai-hoc-toggle">{{ taiHocMoRong ? '▲' : '▼' }}</span>
          </div>
          <template v-if="taiHocMoRong">
            <div v-if="dangLayCanhBaoTaiHoc" class="goi-y-loading">Đang tính tải học...</div>
            <template v-else-if="canhBaoTaiHoc">
              <!-- Cảnh báo chung -->
              <div v-if="canhBaoTaiHoc.canhBaoChung?.length" class="tai-canh-bao-chung">
                <span v-for="(cb, i) in canhBaoTaiHoc.canhBaoChung" :key="i" class="tai-cb-item">⚠ {{ cb }}</span>
              </div>
              <!-- Tóm tắt tổng -->
              <div class="tai-tong-hop">
                <span>TB/kỳ:</span>
                <strong>{{ canhBaoTaiHoc.soMonTrungBinhMoiKy }} môn</strong>
                <strong>{{ canhBaoTaiHoc.gioTrungBinhMoiKy }} giờ</strong>
                <strong>{{ canhBaoTaiHoc.tinChiTrungBinhMoiKy }} TC</strong>
                <span class="tai-nguong">Ngưỡng QT: {{ canhBaoTaiHoc.nguongQuaTaiTheoGio }}h / {{ canhBaoTaiHoc.nguongQuaTaiTheoTinChi }}TC</span>
              </div>
              <!-- Bảng từng kỳ -->
              <table class="tai-hoc-table">
                <thead>
                  <tr>
                    <th>Kỳ</th>
                    <th>Môn</th>
                    <th>Giờ</th>
                    <th>TC</th>
                    <th>% tải</th>
                    <th>Mức</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="item in canhBaoTaiHoc.danhSachKy" :key="item.maKy"
                      :class="['tai-row', `tai-row-${item.mucDoTai?.toLowerCase()}`]"
                      :title="item.canhBao?.join('\n')">
                    <td>{{ item.tenKy }}</td>
                    <td>{{ item.soMon }}</td>
                    <td>{{ item.tongGio }}</td>
                    <td>{{ item.tongTinChi }}</td>
                    <td>{{ item.chuaXepKy || !item.daCoKhungKy ? '—' : item.tyLeTaiTheoGio + '%' }}</td>
                    <td><span :class="['tai-badge', `tai-${item.mucDoTai?.toLowerCase()}`]">{{ mucDoTaiLabel(item.mucDoTai) }}</span></td>
                  </tr>
                </tbody>
              </table>
            </template>
          </template>
        </div>
        <!-- End panel cảnh báo tải học -->

        <div
            v-if="tangHienTai === 7 && bang.key === 'monHoc'"
            class="mon-hoc-tang-7-filter"
        >
          <label>
            <span>Lọc môn học trong version</span>
            <input
                v-model="tuKhoaLocMonHocTang7"
                type="text"
                placeholder="Nhập mã môn hoặc tên môn..."
            >
          </label>

          <small>Hiển thị toàn bộ kết quả, nếu nhiều dữ liệu thì cuộn phần hàng bên dưới.</small>
        </div>


        <div
            v-if="tangHienTai === 8 && bang.key === 'syllabusMonHocmau'"
            class="mon-hoc-tang-7-filter"
        >
          <label>
            <span>Lọc syllabus môn học mẫu</span>
            <input
                v-model="tuKhoaLocSyllabusmauTang8"
                type="text"
                placeholder="Nhập mã, tên syllabus hoặc tên môn học..."
            >
          </label>
          <small>Lọc theo mã syllabus, tên syllabus hoặc tên môn học mẫu.</small>
        </div>
        <div
            :class="[
              'bang-tang-wrapper',
              {
                'bang-mon-hoc-tang-7-scroll': tangHienTai === 7 && bang.key === 'monHoc',
                'bang-chuong-trinh-mon-tang-7': tangHienTai === 7 && bang.key === 'chuongTrinhMon'
              }
            ]"
        >
          <BangThemNghiepVu
              v-bind="bang.config"
              :items="bang.rows"
              :all-items="bang.allRows"
              :lookups="lookups"
              :service="bang.service"
              :selected-id="bang.selectedId"
              :selected-ids="bang.selectedIds"
              :saved-ids="bang.savedIds"
              :table-message="bang.tableMessage"
              :can-toggle-save="bang.canToggleSave"
              :can-select="bang.canSelect"
              :can-show-saved-status="bang.canShowSavedStatus"
              :multi-select="bang.multiSelect"
              :multi-select-ids="bang.multiSelectIds"
              :multi-select-label="bang.multiSelectLabel"
              :multi-selected-label="bang.multiSelectedLabel"
              :toggle-save-label="bang.toggleSaveLabel"
              :toggle-saved-label="bang.toggleSavedLabel"
              :save-status-text="bang.saveStatusText"
              :status-saved-label="bang.statusSavedLabel"
              :status-unsaved-label="bang.statusUnsavedLabel"
              :can-view="bang.canView"
              :view-label="bang.viewLabel"
              :viewed-id="bang.viewedId"
              :parent-values="bang.parentValues"
              :parent-text="bang.parentText"
              :readonly-info-fields="bang.readonlyInfoFields"
              :disabled="bang.disabled"
              :disabled-text="bang.disabledText"
              :loai-bang="bang.loaiBang"
              :read-only="bang.readOnly"
              :table-title="bang.tableTitle"
              :empty-text="bang.emptyText"
              :delete-handler="bang.deleteHandler"
              :delete-label="bang.deleteLabel"
              @select="chonDongBang(bang, $event)"
              @multi-select="tichChonDongBang(bang, $event)"
              @view="xemDongBang(bang, $event)"
              @file-view="xemTepTrongCotFileDaLuu(bang, $event)"
              @toggle-save="luuDongBang(bang, $event)"
              @saved="sauKhiLuuVaCapNhatGoiY(bang.key, $event)"
              @deleted="sauKhiXoaVaCapNhatGoiY(bang.key, $event)"
              @notify="xuLyThongBaoBang(bang, $event)"
          />
        </div>

        <!-- Panel file syllabus môn học (tầng 9) — hiện SAU bảng Tài liệu đã lưu -->
        <div
            v-if="tangHienTai === 9 && bang.key === 'syllabusTaiLieu' && selected.syllabusMonHoc"
            class="bang-tang-wrapper"
        >
          <div class="bang-header-row">
            <span class="bang-header-title">File syllabus môn học</span>
            <label class="btn tiny primary" style="cursor:pointer;">
              {{ fileSyllabusUploading ? 'Đang tải...' : '+ Upload file' }}
              <input
                  type="file"
                  accept=".pdf,.doc,.docx"
                  style="display:none"
                  :disabled="fileSyllabusLoading || fileSyllabusUploading"
                  @change="uploadFileSyllabusApDung($event)"
              >
            </label>
            <span v-if="fileSyllabusLoading || fileSyllabusUploading" class="file-loading-text">Đang xử lý...</span>
          </div>

          <div v-if="fileSyllabusThongBao" :class="['table-message', fileSyllabusLoaiThongBao === 'error' ? 'error' : 'success']">
            {{ fileSyllabusThongBao }}
          </div>

          <div v-if="!danhSachFileDaGanSyllabus.length" class="bang-rong-text">
            Chưa có file nào. Khi gán syllabus mẫu, file sẽ được copy tự động. Hoặc upload file mới bằng nút bên trên.
          </div>
          <table v-else class="bang-table">
            <thead>
              <tr>
                <th>#</th>
                <th>Tên file</th>
                <th>Loại file</th>
                <th>Kích thước</th>
                <th>Ngày tạo</th>
                <th>Thao tác</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(f, idx) in danhSachFileDaGanSyllabus" :key="f.id">
                <td>{{ idx + 1 }}</td>
                <td class="file-ten-col">{{ f.tenFile }}</td>
                <td>{{ f.loaiFile }}</td>
                <td>{{ formatKichThuocFile(f.kichThuoc) }}</td>
                <td>{{ f.createdAt ? new Date(f.createdAt).toLocaleDateString('vi-VN') : '' }}</td>
                <td class="file-action-cell">
                  <button
                      type="button"
                      class="btn tiny"
                      :disabled="fileSyllabusLoading"
                      @click="xemFileSyllabus(f.id)"
                  >Xem</button>
                  <label class="btn tiny" style="cursor:pointer;">
                    {{ fileSyllabusThayId === f.id ? 'Đang tải...' : 'Cập nhật' }}
                    <input
                        type="file"
                        accept=".pdf,.doc,.docx"
                        style="display:none"
                        :disabled="fileSyllabusLoading || !!fileSyllabusThayId"
                        @change="thayFileSyllabus(f.id, $event)"
                    >
                  </label>
                  <button
                      type="button"
                      class="btn tiny danger"
                      :disabled="fileSyllabusLoading"
                      @click="xoaFileSyllabus(f)"
                  >Xóa</button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
        <!-- End file syllabus panel -->

      </template>
    </section>

    <!-- Bảng 3 tầng 2: Ngành hệ đào tạo theo ngành -->
    <section v-if="tangHienTai === 2 && selected.nganh" class="flow-group mau-tim">
      <div class="group-title">
        <h3>Bảng 3: Ngành hệ đào tạo theo ngành — {{ selected.nganh.tenNganh }}</h3>
        <p>Mỗi dòng là một hệ đào tạo (Ngành + Trình độ + Loại chương trình). Bấm "Xem chương trình" để vào tầng 3.</p>
      </div>

      <div class="bang-da-gan-tang-2">
        <div class="bang-da-gan-head">
          <h4>Thêm ngành hệ đào tạo</h4>
          <button type="button" class="btn tiny" @click="taiDuLieuCoSanTatCaBang">Tải lại</button>
        </div>

        <div v-if="thongBaoNganhHe" :class="['table-message', loaiThongBaoNganhHe === 'error' ? 'error' : 'success']">
          {{ thongBaoNganhHe }}
        </div>

        <form class="form-nganh-he" @submit.prevent="luuNganhHe">
          <label>
            <span>Trình độ *</span>
            <select v-model="formNganhHe.trinhDoId" required>
              <option :value="null">— Chọn —</option>
              <option v-for="td in duLieu.trinhDoDaoTao" :key="td.id" :value="td.id">
                {{ td.maTrinhDo }} – {{ td.tenTrinhDo }}
              </option>
            </select>
          </label>
          <label>
            <span>Loại chương trình *</span>
            <select v-model="formNganhHe.loaiChuongTrinhId" required>
              <option :value="null">— Chọn —</option>
              <option v-for="lct in duLieu.loaiChuongTrinh" :key="lct.id" :value="lct.id">
                {{ lct.maLoai }} – {{ lct.tenLoai }}
              </option>
            </select>
          </label>
          <label>
            <span>Số tháng</span>
            <input v-model.number="formNganhHe.soThang" type="number" min="1" placeholder="24">
          </label>
          <label>
            <span>Số kỳ</span>
            <input v-model.number="formNganhHe.soKy" type="number" min="1" placeholder="4">
          </label>
          <label>
            <span>Mã hệ <small>(tự sinh nếu để trống)</small></span>
            <input v-model.trim="formNganhHe.maHe" placeholder="VD: TC_CQ">
          </label>
          <label>
            <span>Tên hệ <small>(tự sinh nếu để trống)</small></span>
            <input v-model.trim="formNganhHe.tenHe" placeholder="VD: Trung cấp chính quy">
          </label>
          <div class="hang-nut-nganh-he">
            <button type="submit" class="btn primary" :disabled="dangLuuNganhHe">
              {{ formNganhHe.id ? 'Cập nhật' : 'Thêm' }}
            </button>
            <button type="button" class="btn" @click="resetFormNganhHe">
              {{ formNganhHe.id ? 'Hủy sửa' : 'Làm mới' }}
            </button>
          </div>
        </form>

        <div class="table-wrap mini-scroll" style="margin-top:12px">
          <table>
            <thead>
              <tr>
                <th>Trình độ</th>
                <th>Loại chương trình</th>
                <th>Tháng / Kỳ</th>
                <th>Mã hệ</th>
                <th>Tên hệ</th>
                <th>Trạng thái</th>
                <th class="col-action">Thao tác</th>
              </tr>
            </thead>
            <tbody>
              <tr v-if="!nganhHeDaoTaoTheoNganh.length">
                <td colspan="7" class="empty-cell">Chưa có ngành hệ đào tạo nào cho ngành này.</td>
              </tr>
              <tr v-for="item in nganhHeDaoTaoTheoNganh" :key="`nhe-${item.id}`">
                <td>{{ item.maTrinhDo || item.trinhDoId }} – {{ item.tenTrinhDo || '' }}</td>
                <td>{{ item.maLoai || item.loaiChuongTrinhId }} – {{ item.tenLoai || '' }}</td>
                <td>{{ item.soThang ? item.soThang + ' tháng' : '-' }} / {{ item.soKy ? item.soKy + ' kỳ' : '-' }}</td>
                <td>{{ item.maHe || '-' }}</td>
                <td>{{ item.tenHe || '-' }}</td>
                <td>
                  <span :class="item.trangThai === 'dang_su_dung' ? 'trang-thai-on' : 'trang-thai-off'">
                    {{ item.trangThai === 'dang_su_dung' ? 'Đang dùng' : 'Ngừng' }}
                  </span>
                </td>
                <td class="actions-cell">
                  <button type="button" class="save-status" @click="xemChuongTrinhTuNganhHe(item)">Xem chương trình</button>
                  <button type="button" class="btn tiny" @click="suaNganhHe(item)">Sửa</button>
                  <button type="button" class="btn tiny danger" @click="xoaNganhHe(item.id)">Xóa</button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </section>

    <div v-if="cacNutDieuHuongTang.length && tangHienTai !== 1 && tangHienTai !== 2 && tangHienTai !== 3 && tangHienTai !== 4 && tangHienTai !== 6 && tangHienTai !== 7 && tangHienTai !== 8 && tangHienTai !== 9"
         class="continue-panel continue-panel-bottom">
      <div>
        <b>Điều hướng tầng {{ tangHienTai }}</b>
        <span>{{ moTaDieuHuongTang }}</span>
      </div>
      <div class="continue-actions">
        <button
            v-for="nut in cacNutDieuHuongTang"
            :key="`bottom-${nut.key}`"
            type="button"
            :class="['btn', nut.primary ? 'primary deep-action' : 'secondary-action']"
            :disabled="!nut.enabled"
            @click="bamNutDieuHuongTang(nut)"
        >
          {{ nut.label }}
        </button>
      </div>
    </div>
  </div>
</template>
<script setup>
import {computed, nextTick, ref, watch} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import BangThemNghiepVu from '../components/BangThemNghiepVu.vue'
import {useDaoTaoXemChuongTrinh} from '../composables/useDaoTaoXemChuongTrinh'
import {layThongBaoLoi} from '../utils/layThongBaoLoi'
import {tepDinhKemUploadService} from '../services/tepDinhKemUploadService'

const route = useRoute()
const router = useRouter()

const {
  thongBao,
  loaiThongBao,
  tableMessages,
  selected,
  viewed,
  duLieu,
  quyDoiDiemMaumauRows,
  lookups,
  services,
  baoTin,
  selectEntity,
  viewEntity,
  chonBangPhuVaoLuong,
  toggleLuuBangPhu,
  taiDuLieuCoSanTatCaBang,
  taiDuLieuTheoSyllabusMonHocApDung,
  sauKhiLuu,
  sauKhiXoa,
  lamMoiLuon,
  luuChuongTrinhTong
} = useDaoTaoXemChuongTrinh({
  autoLoad: true
})

// ===== KHUNG KỲ GỢI Ý =====
const khungKyGoiY = ref(null)
const dangLayGoiY = ref(false)
const dangTaoDuKy = ref(false)

async function layGoiYKhungKy(versionId) {
  if (!versionId) { khungKyGoiY.value = null; return }
  dangLayGoiY.value = true
  try {
    const res = await services.khungKy.goiYTheoVersion(versionId)
    khungKyGoiY.value = res?.data ?? res
  } catch {
    khungKyGoiY.value = null
  } finally {
    dangLayGoiY.value = false
  }
}

async function taoDuKyConThieu() {
  const versionId = selected.chuongTrinhVersion?.id
  if (!versionId) return
  dangTaoDuKy.value = true
  try {
    const res = await services.khungKy.taoDuKyConThieu(versionId)
    const list = res?.data ?? res
    if (Array.isArray(list)) {
      // Cập nhật rawData thông qua sauKhiLuu cho từng kỳ mới tạo
      for (const row of list) {
        sauKhiLuu('khungKy', row)
      }
    }
    await layGoiYKhungKy(versionId)
    baoTin('Đã tạo đủ kỳ còn thiếu.')
  } catch (e) {
    baoTin(e?.message || 'Tạo kỳ thất bại.', 'error')
  } finally {
    dangTaoDuKy.value = false
  }
}

watch(
    () => selected.chuongTrinhVersion?.id,
    (versionId) => layGoiYKhungKy(versionId),
    {immediate: true}
)
function sauKhiLuuVaCapNhatGoiY(key, saved) {
  sauKhiLuu(key, saved)
  if (key === 'khungKy') {
    layGoiYKhungKy(selected.chuongTrinhVersion?.id)
    layCanhBaoTaiHoc(selected.chuongTrinhVersion?.id)
  }
}

function sauKhiXoaVaCapNhatGoiY(key, id) {
  sauKhiXoa(key, id)
  if (key === 'khungKy') {
    layGoiYKhungKy(selected.chuongTrinhVersion?.id)
    layCanhBaoTaiHoc(selected.chuongTrinhVersion?.id)
  }
}
// ===== END KHUNG KỲ GỢI Ý =====

// ===== CẢNH BÁO TẢI HỌC THEO KỲ =====
const canhBaoTaiHoc = ref(null)
const dangLayCanhBaoTaiHoc = ref(false)
const taiHocMoRong = ref(false)

async function layCanhBaoTaiHoc(versionId) {
  if (!versionId) { canhBaoTaiHoc.value = null; return }
  dangLayCanhBaoTaiHoc.value = true
  try {
    const res = await services.khungKy.canhBaoTaiHoc(versionId)
    canhBaoTaiHoc.value = res?.data ?? res
  } catch {
    canhBaoTaiHoc.value = null
  } finally {
    dangLayCanhBaoTaiHoc.value = false
  }
}

function mucDoTaiLabel(mucDo) {
  const map = { BINH_THUONG: 'Bình thường', QUA_TAI: 'Quá tải', NHE_TAI: 'Nhẹ tải', CHUA_TAO_KY: 'Chưa tạo kỳ', CHUA_XEP_KY: 'Chưa xếp kỳ' }
  return map[mucDo] ?? mucDo
}

watch(
    () => selected.chuongTrinhVersion?.id,
    (versionId) => layCanhBaoTaiHoc(versionId),
    {immediate: true}
)
// ===== END CẢNH BÁO TẢI HỌC =====

// ===== GỢI Ý NGÀY HẾT HIỆU LỰC THEO THÁNG NGÀNH HỆ ĐÀO TẠO =====
const chuongTrinhVersionFields = computed(() => {
  const baseFields = configs.chuongTrinhVersion?.fields || []
  return baseFields.map((field) => {
    if (field.key !== 'ngayApDung') return field
    return {
      ...field,
      onChange(value, form) {
        if (!value) return
        const soThang = selected.nganhHeDaoTao?.soThang
        if (!soThang || soThang <= 0) return
        if (form.ngayHetHieuLuc) return  // không ghi đè nếu đã nhập tay
        const ngayApDung = new Date(value)
        if (isNaN(ngayApDung.getTime())) return
        const ngayHetHieuLuc = new Date(ngayApDung)
        ngayHetHieuLuc.setMonth(ngayHetHieuLuc.getMonth() + soThang)
        ngayHetHieuLuc.setDate(ngayHetHieuLuc.getDate() - 1)
        form.ngayHetHieuLuc = ngayHetHieuLuc.toISOString().slice(0, 10)
      }
    }
  })
})
// ===== END GỢI Ý NGÀY HẾT HIỆU LỰC =====

const cotDiemMauDaTichChonIds = ref([])
const dangGanCotDiemMau = ref(false)

function layIdDong(item) {
  const id = item?.id
  return id === null || id === undefined || id === '' ? null : id
}

function tichChonDongBang(bang, payload) {
  if (bang.key !== 'cauHinhDanhGiaMau') return

  const id = layIdDong(payload?.item)
  if (!id) return

  const idText = String(id)
  const current = cotDiemMauDaTichChonIds.value.map((value) => String(value))

  if (payload?.checked) {
    if (!current.includes(idText)) cotDiemMauDaTichChonIds.value = [...cotDiemMauDaTichChonIds.value, id]
    return
  }

  cotDiemMauDaTichChonIds.value = cotDiemMauDaTichChonIds.value.filter((value) => String(value) !== idText)
}

function boTichChonCotDiemMau() {
  cotDiemMauDaTichChonIds.value = []
}

const chuongTrinhNganhLoaiDangChon = computed(() => selected.nganhLoaiChuongTrinh || null)

const chuongTrinhTrinhDoDangChon = computed(() => {
  if (selected.trinhDoDaoTao?.id) return selected.trinhDoDaoTao

  if (cauHinhTrinhDoTheoNganh.value.length === 1) {
    return timTrinhDoTheoId(cauHinhTrinhDoTheoNganh.value[0].trinhDoId)
  }

  return null
})

const nganhHeDaoTaoIdTheoChon = computed(() => {
  // Tìm nganhHeDaoTao phù hợp từ dữ liệu đã load
  const nganhLoai = chuongTrinhNganhLoaiDangChon.value
  const trinhDo = chuongTrinhTrinhDoDangChon.value
  if (!nganhLoai?.nganhId || !trinhDo?.id || !nganhLoai?.loaiChuongTrinhId) return null
  const match = (duLieu.value.nganhHeDaoTao || []).find(item =>
    String(item.nganhId) === String(nganhLoai.nganhId) &&
    String(item.trinhDoId) === String(trinhDo.id) &&
    String(item.loaiChuongTrinhId) === String(nganhLoai.loaiChuongTrinhId)
  )
  return match?.id || null
})

const chuongTrinhParent = computed(() => ({
  nganhHeDaoTaoId: selected.nganhHeDaoTao?.id || null,
  nganhId: selected.nganhHeDaoTao?.nganhId || selected.nganh?.id || route.params.nganhId || null,
}))

const chuongTrinhParentText = computed(() => {
  const he = selected.nganhHeDaoTao
  if (!he) return 'Chưa chọn ngành hệ đào tạo'
  return `Ngành hệ: ${he.tenHe || he.maHe || he.id} (${he.tenTrinhDo || '-'} / ${he.tenLoai || '-'})`
})

const cauHinhTrinhDoTheoNganh = computed(() => {
  const nganhId = selected.nganh?.id || route.params.nganhId || null
  if (!nganhId) return []

  return (duLieu.value.nganhTrinhDoDaoTao || [])
      .filter((item) => String(item.nganhId || '') === String(nganhId || ''))
      .filter((item) => item.trangThai === 'dang_su_dung')
})

const cauHinhLoaiChuongTrinhTheoNganh = computed(() => {
  const nganhId = selected.nganh?.id || route.params.nganhId || null
  if (!nganhId) return []

  return (duLieu.value.nganhLoaiChuongTrinh || [])
      .filter((item) => String(item.nganhId || '') === String(nganhId || ''))
      .filter((item) => item.trangThai === 'dang_su_dung')
})

function timTrinhDoTheoId(id) {
  return (duLieu.value.trinhDoDaoTao || []).find((row) => String(row.id || '') === String(id || '')) || null
}

function timLoaiChuongTrinhTheoId(id) {
  return (duLieu.value.loaiChuongTrinh || []).find((row) => String(row.id || '') === String(id || '')) || null
}

function tenTrinhDoTheoId(id) {
  const item = timTrinhDoTheoId(id)
  return item?.tenTrinhDo || item?.ten || id || '-'
}

function maTrinhDoTheoId(id) {
  const item = timTrinhDoTheoId(id)
  return item?.maTrinhDo || item?.ma || id || '-'
}

function tenLoaiChuongTrinhTheoId(id) {
  const item = timLoaiChuongTrinhTheoId(id)
  return item?.tenLoai || item?.ten || id || '-'
}

function maLoaiChuongTrinhTheoId(id) {
  const item = timLoaiChuongTrinhTheoId(id)
  return item?.maLoai || item?.ma || id || '-'
}

function idsTrinhDoDaGanTheoNganh() {
  return cauHinhTrinhDoTheoNganh.value
      .map((item) => item.trinhDoId)
      .filter((id) => id !== null && id !== undefined && id !== '')
}

function idsLoaiChuongTrinhDaGanTheoNganh() {
  return cauHinhLoaiChuongTrinhTheoNganh.value
      .map((item) => item.loaiChuongTrinhId)
      .filter((id) => id !== null && id !== undefined && id !== '')
}

const chuongTrinhVersionParent = computed(() => ({
  chuongTrinhId: selected.chuongTrinh?.id || null
}))

const chuongTrinhVersionParentText = computed(() => {
  return `Đang chọn: Chương trình ${selected.chuongTrinh?.maChuongTrinh || '-'} - ${selected.chuongTrinh?.tenChuongTrinh || '-'}`
})

const versionParent = computed(() => ({
  chuongTrinhVersionId: selected.chuongTrinhVersion?.id || null
}))

const versionParentText = computed(() => {
  return `Đang chọn: Version ${selected.chuongTrinhVersion?.maVersion || '-'} - ${selected.chuongTrinhVersion?.tenVersion || '-'}`
})

const syllabusChuongTrinhParent = computed(() => ({
  chuongTrinhVersionId: selected.chuongTrinhVersion?.id || null,
  syllabusChuongTrinhmauId: selected.syllabusChuongTrinhmau?.id || null
}))

const syllabusChuongTrinhParentText = computed(() => {
  return `${versionParentText.value} | Syllabus chương trình mẫu ${selected.syllabusChuongTrinhmau?.ma || '-'} - ${selected.syllabusChuongTrinhmau?.ten || '-'}`
})


const syllabusChuongTrinhApDungTheoVersion = computed(() => {
  const versionId = selected.chuongTrinhVersion?.id || route.params.versionId || null
  if (!versionId) return null

  return (duLieu.value.syllabusChuongTrinh || [])
      .find((row) => String(row.chuongTrinhVersionId || '') === String(versionId || '')) || null
})

const syllabusChuongTrinhIdDangGan = computed(() => syllabusChuongTrinhApDungTheoVersion.value?.id || null)

const syllabusChuongTrinhMauIdDangGan = computed(() => {
  return syllabusChuongTrinhApDungTheoVersion.value?.syllabusChuongTrinhmauId
      || syllabusChuongTrinhApDungTheoVersion.value?.syllabusChuongTrinhMauId
      || null
})

const syllabusChuongTrinhMauDangGanTheoVersion = computed(() => {
  const mauId = syllabusChuongTrinhMauIdDangGan.value
  if (!mauId) return null

  return (duLieu.value.syllabusChuongTrinhmau || [])
      .find((row) => String(row.id || '') === String(mauId || '')) || null
})

const rowsSyllabusChuongTrinhMauTheoVersion = computed(() => {
  const chuongTrinhId = selected.chuongTrinh?.id || route.params.chuongTrinhId || null
  if (!chuongTrinhId) return []

  return (duLieu.value.syllabusChuongTrinhmau || [])
      .filter((row) => String(row.chuongTrinhId || '') === String(chuongTrinhId || ''))
})

const rowsSyllabusChuongTrinhApDungTheoVersion = computed(() => {
  const row = syllabusChuongTrinhApDungTheoVersion.value
  return row ? [row] : []
})

function locRowsTheoSyllabusChuongTrinhMau(rows = []) {
  const mauId = syllabusChuongTrinhMauIdDangGan.value
  if (!mauId) return []

  return (rows || []).filter((row) => {
    const rowMauId = row.syllabusChuongTrinhMauId || row.syllabusChuongTrinhmauId
    return String(rowMauId || '') === String(mauId || '')
  })
}

function locRowsTheoSyllabusChuongTrinhApDung(rows = []) {
  const syllabusId = syllabusChuongTrinhIdDangGan.value
  if (!syllabusId) return []

  return (rows || []).filter((row) => {
    const rowSyllabusId = row.syllabusChuongTrinhId || row.syllabusChuongTrinhID
    return String(rowSyllabusId || '') === String(syllabusId || '')
  })
}

function taoThuTuKeTiep(rows = []) {
  const max = (rows || []).reduce((value, row) => Math.max(value, Number(row?.thuTu || 0)), 0)
  return max + 1
}

function layMaBangMau(item = {}) {
  return String(item.ma || '').trim()
}

function timDongDaCopyTheoMa(key, item = {}) {
  const syllabusId = syllabusChuongTrinhIdDangGan.value
  const ma = layMaBangMau(item)
  if (!syllabusId || !ma) return null

  return (duLieu.value[key] || []).find((row) => {
    const rowSyllabusId = row.syllabusChuongTrinhId || row.syllabusChuongTrinhID
    return String(rowSyllabusId || '') === String(syllabusId || '')
        && String(row.ma || '').trim() === ma
  }) || null
}

function buildPayloadApDungTuMau(key, item = {}) {
  const syllabusChuongTrinhId = syllabusChuongTrinhIdDangGan.value
  const rowsHienCo = locRowsTheoSyllabusChuongTrinhApDung(duLieu.value[key] || [])
  const base = {
    syllabusChuongTrinhId,
    ma: item.ma || null,
    thuTu: item.thuTu ?? taoThuTuKeTiep(rowsHienCo),
    ghiChu: item.ghiChu || null
  }

  if (key === 'mucTieuChuongTrinh') {
    return {
      ...base,
      loai: item.loai || 'chung',
      noiDung: item.noiDung || ''
    }
  }

  if (key === 'nangLucDauRa') {
    return {
      ...base,
      loai: item.loai || 'co_ban',
      noiDung: item.noiDung || ''
    }
  }

  if (key === 'viTriViecLam') {
    return {
      ...base,
      ten: item.ten || '',
      moTa: item.moTa || null
    }
  }

  if (key === 'dieuKienTotNghiep') {
    return {
      ...base,
      noiDung: item.noiDung || ''
    }
  }

  return base
}

const nhomKienThucParent = computed(() => ({
  chuongTrinhVersionId: selected.chuongTrinhVersion?.id || null,
  nhomKienThucmauId: selected.nhomKienThucmau?.id || null
}))

const nhomKienThucParentText = computed(() => {
  return `${versionParentText.value} | Nhóm kiến thức mẫu ${selected.nhomKienThucmau?.ma || '-'} - ${selected.nhomKienThucmau?.ten || '-'}`
})

const nhomTuChonParent = computed(() => ({
  chuongTrinhVersionId: selected.chuongTrinhVersion?.id || null,
  nhomTuChonmauId: selected.nhomTuChonmau?.id || null
}))

const nhomTuChonParentText = computed(() => {
  return `${versionParentText.value} | Nhóm tự chọn mẫu ${selected.nhomTuChonmau?.ma || '-'} - ${selected.nhomTuChonmau?.ten || '-'}`
})

const khungKyParent = computed(() => ({
  chuongTrinhVersionId: selected.chuongTrinhVersion?.id || null
}))

const khungKyParentText = computed(() => {
  return `${versionParentText.value} | Loại CT ${selected.nganhHeDaoTao?.tenLoai || selected.loaiChuongTrinh?.tenLoai || '-'}`
})


const khungKyDangChonText = computed(() => {
  return selected.khungKy?.id
      ? `${selected.khungKy.maKy || '-'} - ${selected.khungKy.tenKy || '-'}`
      : 'Chưa chọn kỳ học'
})

// defaultForm cho khungKy lấy từ gợi ý BE (chỉ áp dụng khi tạo mới)
const khungKyDefaultFormGoiY = computed(() => {
  const base = {
    chuongTrinhVersionId: null,
    maKy: '', tenKy: '', thuTu: null, ngayBatDau: null, ngayKetThuc: null
  }
  const next = khungKyGoiY.value?.kyTiepTheoGoiY
  if (!next || khungKyGoiY.value?.daTaoDuKy) return base
  return {
    ...base,
    maKy: next.maKy || '',
    tenKy: next.tenKy || '',
    thuTu: next.thuTu || null,
    ngayBatDau: next.ngayBatDauGoiY || null,
    ngayKetThuc: next.ngayKetThucGoiY || null
  }
})


function layGiaTriHienThi(...values) {
  const value = values.find((item) => item !== null && item !== undefined && item !== '')
  return value || '-'
}

function taoOThongTinNhomTheoKy(loaiNhom) {
  const nhommauLabel = loaiNhom === 'tuChon' ? 'Nhóm tự chọn' : 'Nhóm KT mẫu'
  const nhommauValue = loaiNhom === 'tuChon'
      ? layGiaTriHienThi(selected.nhomTuChonmau?.ten, selected.nhomTuChonmau?.ma)
      : layGiaTriHienThi(selected.nhomKienThucmau?.ten, selected.nhomKienThucmau?.ma)

  return [
    {
      key: 'infoNganh',
      label: 'Ngành',
      value: layGiaTriHienThi(selected.nganh?.tenNganh, selected.chuongTrinh?.tenNganh)
    },
    {
      key: 'infoNganhHeDaoTao',
      label: 'Hệ đào tạo',
      value: layGiaTriHienThi(selected.nganhHeDaoTao?.tenHe, selected.nganhHeDaoTao?.maHe)
    },
    {
      key: 'infoChuongTrinh',
      label: 'CTĐT',
      value: layGiaTriHienThi(selected.chuongTrinh?.maChuongTrinh, selected.chuongTrinh?.tenChuongTrinh)
    },
    {
      key: 'infoVersion',
      label: 'Version',
      value: layGiaTriHienThi(selected.chuongTrinhVersion?.maVersion, selected.chuongTrinhVersion?.tenVersion)
    },
    {
      key: 'infoKy',
      label: 'Kỳ',
      value: layGiaTriHienThi(selected.khungKy?.tenKy, selected.khungKy?.maKy)
    },
    {
      key: 'infoNhommau',
      label: nhommauLabel,
      value: nhommauValue
    }
  ]
}

function chuanHoaEnum(value) {
  return String(value ?? '')
      .trim()
      .toLowerCase()
}

const NHAN_TRANG_THAI_CHUONG_TRINH_VERSION = {
  dang_soan: 'Đang soạn',
  cho_ap_dung: 'Chờ áp dụng',
  dang_ap_dung: 'Đang áp dụng',
  het_hieu_luc: 'Hết hiệu lực',
  tam_dung: 'Tạm dừng',
  da_huy: 'Đã hủy'
}

function layNhanTrangThaiChuongTrinhVersion(value) {
  const key = chuanHoaEnum(value)
  if (!key) return '-'
  return NHAN_TRANG_THAI_CHUONG_TRINH_VERSION[key] || value || '-'
}

function boSungNhanHienThiBang(key, row = {}) {
  if (!row || typeof row !== 'object') return row

  const result = {...row}

  if (key === 'chuongTrinhVersion') {
    result.trangThaiHienThi = layNhanTrangThaiChuongTrinhVersion(row.trangThai)
  }

  if ([
    'mucTieuChuongTrinhmau',
    'mucTieuChuongTrinh',
    'nangLucDauRamau',
    'nangLucDauRa'
  ].includes(key)) {
    result.loaiHienThi = layNhanLoaiMucTieuNangLuc(row.loai)
  }

  if (key === 'chuongTrinhMon') {
    result.loaiHienThi = layNhanLoaiMonTang7(row.loai)
    result.loaiHocPhanHienThi = layNhanLoaiHocPhanTang7(row.loaiHocPhan)
    result.loaiPhamViHienThi = layNhanPhamViMonTang7(row.loaiPhamVi)
  }

  if (['cauHinhDanhGiaMau', 'cauHinhDanhGia'].includes(key)) {
    result.loaiDiemHienThi = layNhanLoaiDiemTang9(row.loaiDiem)
  }

  if (['quyDoiDiemMau', 'chuongTrinhMonQuyDoiDiemMau'].includes(key)) {
    result.loaiMauHienThi = layNhanLoaiQuyDoiTang9(row.loaiMau)
    result.ketQuaHienThi = layNhanKetQuaTang9(row.ketQua)
  }

  if ([
    'dieuKienMonHocmau',
    'dieuKienMonHoc',
    'taiLieumau',
    'syllabusMonHocmauTaiLieu',
    'syllabusTaiLieu'
  ].includes(key)) {
    result.loaiHienThi = layNhanLoaiDieuKienTaiLieuTang9(row.loai)
  }

  return result
}
const NHAN_LOAI_MUC_TIEU_NANG_LUC = {
  // Mục tiêu chương trình / mục tiêu chương trình mẫu
  chung: 'Chung',
  kien_thuc: 'Kiến thức',
  ky_nang: 'Kỹ năng',
  thai_do: 'Thái độ',
  nang_luc_tu_chu_trach_nhiem: 'Năng lực tự chủ trách nhiệm',

  // Năng lực đầu ra / năng lực đầu ra mẫu
  cot_loi: 'Cốt lõi',
  nang_cao: 'Nâng cao',

  // Các giá trị dự phòng nếu DB/API đang có
  nang_luc: 'Năng lực',
  co_ban: 'Cơ bản',
  chuyen_mon: 'Chuyên môn',
  tu_chu_trach_nhiem: 'Tự chủ trách nhiệm',
  khac: 'Khác'
}

const NHAN_LOAI_MON_TANG_7 = {
  bat_buoc: 'Bắt buộc',
  tu_chon: 'Tự chọn'
}

const NHAN_LOAI_HOC_PHAN_TANG_7 = {
  mon_hoc: 'Môn học',
  mo_dun: 'Mô đun',
  module: 'Mô đun'
}

const NHAN_PHAM_VI_MON_TANG_7 = {
  mon_chung: 'Môn chung',
  mon_co_so: 'Môn cơ sở',
  mon_chuyen_nganh: 'Môn chuyên ngành',
  mon_tu_chon_nang_cao: 'Môn tự chọn nâng cao'
}

const NHAN_LOAI_DIEM_TANG_9 = {
  bai_tap: 'Bài tập',
  kiem_tra: 'Kiểm tra',
  giua_ky: 'Giữa kỳ',
  cuoi_ky: 'Cuối kỳ',
  thuc_hanh: 'Thực hành',
  chuyen_can: 'Chuyên cần',
  khac: 'Khác'
}

const NHAN_LOAI_QUY_DOI_TANG_9 = {
  quy_doi_ket_qua: 'Quy đổi kết quả',
  QUY_DOI_KET_QUA: 'Quy đổi kết quả'
}

const NHAN_KET_QUA_TANG_9 = {
  dat: 'Đạt',
  khong_dat: 'Không đạt'
}

const NHAN_LOAI_DIEU_KIEN_TAI_LIEU_TANG_9 = {
  hoc_lieu: 'Học liệu',
  giao_trinh: 'Giáo trình',
  tai_lieu_tham_khao: 'Tài liệu tham khảo',
  tai_lieu_bat_buoc: 'Tài liệu bắt buộc',
  tai_lieu_khac: 'Tài liệu khác',
  dieu_kien_tien_quyet: 'Điều kiện tiên quyết',
  dieu_kien_song_hanh: 'Điều kiện song hành',
  dieu_kien_khac: 'Điều kiện khác',
  khac: 'Khác'
}

function layNhanLoaiDiemTang9(value) {
  const key = chuanHoaEnum(value)
  if (!key) return '-'
  return NHAN_LOAI_DIEM_TANG_9[key] || value || '-'
}

function layNhanLoaiQuyDoiTang9(value) {
  const key = chuanHoaEnum(value)
  if (!key) return '-'
  return NHAN_LOAI_QUY_DOI_TANG_9[key] || NHAN_LOAI_QUY_DOI_TANG_9[value] || value || '-'
}

function layNhanKetQuaTang9(value) {
  const key = chuanHoaEnum(value)
  if (!key) return '-'
  return NHAN_KET_QUA_TANG_9[key] || value || '-'
}

function layNhanLoaiDieuKienTaiLieuTang9(value) {
  const key = chuanHoaEnum(value)
  if (!key) return '-'
  return NHAN_LOAI_DIEU_KIEN_TAI_LIEU_TANG_9[key] || value || '-'
}

function layNhanLoaiMonTang7(value) {
  const key = chuanHoaEnum(value)
  if (!key) return '-'
  return NHAN_LOAI_MON_TANG_7[key] || value || '-'
}

function layNhanLoaiHocPhanTang7(value) {
  const key = chuanHoaEnum(value)
  if (!key) return '-'
  return NHAN_LOAI_HOC_PHAN_TANG_7[key] || value || '-'
}

function layNhanPhamViMonTang7(value) {
  const key = chuanHoaEnum(value)
  if (!key) return '-'
  return NHAN_PHAM_VI_MON_TANG_7[key] || value || '-'
}

function layNhanLoaiMucTieuNangLuc(value) {
  const key = chuanHoaEnum(value)
  if (!key) return '-'
  return NHAN_LOAI_MUC_TIEU_NANG_LUC[key] || value || '-'
}

const viTriHienTaiText = computed(() => {
  if (tangHienTai.value === 6) {
    return `${versionParentText.value} | Màn hình Kỳ học`
  }

  if (tangHienTai.value === 7) {
    return `${versionParentText.value} | Kỳ ${khungKyDangChonText.value} | Màn hình Môn trong kỳ`
  }

  return versionParentText.value
})

const monHocParentText = computed(() => {
  return `Đang chọn: Version ${selected.chuongTrinhVersion?.tenVersion || '-'} | Tạo môn học mẫu trước khi tạo Chương trình môn`
})

const syllabusMonHocmauParent = computed(() => ({
  monHocId: selected.chuongTrinhMon?.monHocId || selected.monHoc?.id || null
}))

const syllabusMonHocmauFilter = computed(() => ({
  monHocId: dongDangDung('monHoc')?.id || null
}))

const syllabusMonHocmauParentText = computed(() => {
  const monHocDangChon = selected.chuongTrinhMon || selected.monHoc || {}
  return `Đang chọn: Môn học mẫu ${monHocDangChon.maMon || monHocDangChon.maMonTrongCt || '-'} - ${monHocDangChon.tenMon || '-'}`
})

const syllabusMonHocmauChiTietParent = computed(() => ({
  syllabusMonHocMauId: selected.syllabusMonHocmau?.id || null
}))

const syllabusMonHocmauChiTietFilter = computed(() => ({
  syllabusMonHocMauId: dongDangDung('syllabusMonHocmau')?.id || null
}))

const syllabusMonHocmauChiTietText = computed(() => {
  return `Đang chọn: Syllabus môn học mẫu ${selected.syllabusMonHocmau?.ma || '-'} - ${selected.syllabusMonHocmau?.ten || '-'}`
})

const chuongTrinhMonParent = computed(() => ({
  chuongTrinhVersionId: selected.chuongTrinhVersion?.id || null,
  monHocId: selected.monHoc?.id || null,
  khungKyId: selected.khungKy?.id || null,
  nhomKienThucId: null
}))

const chuongTrinhMonTheoKyFilter = computed(() => ({
  chuongTrinhVersionId: selected.chuongTrinhVersion?.id || null,
  khungKyId: selected.khungKy?.id || null
}))
const tuKhoaLocMonHocTang7 = ref('')
const tuKhoaLocSyllabusmauTang8 = ref('')

const danhSachSyllabusmauTang8DaLoc = computed(() => {
  const tuKhoa = chuanHoaTuKhoaTimKiem(tuKhoaLocSyllabusmauTang8.value)
  const monHocId = selected.chuongTrinhMon?.monHocId || selected.monHoc?.id || null

  let danhSach = duLieu.value.syllabusMonHocmau || []

  if (monHocId) {
    danhSach = danhSach.filter((row) => String(row.monHocId || '') === String(monHocId || ''))
  }

  if (!tuKhoa) return danhSach

  return danhSach.filter((row) => {
    const ma = chuanHoaTuKhoaTimKiem(row.ma)
    const ten = chuanHoaTuKhoaTimKiem(row.ten)
    const tenMonHoc = chuanHoaTuKhoaTimKiem(row.tenMonHoc)
    return ma.includes(tuKhoa) || ten.includes(tuKhoa) || tenMonHoc.includes(tuKhoa)
  })
})


// Form Sửa môn trong kỳ ở Tầng 7: bỏ "Phạm vi môn" (loaiPhamVi) khỏi ô nhập,
// Loại phạm vi & Nhóm kiến thức được gán ở trang "Nhóm kiến thức và môn".
const chuongTrinhMonFieldsTang7 = [
  {key: 'maMonTrongCt', label: 'Mã môn CT', required: true},
  {key: 'loai', label: 'Loại môn', type: 'select', lookup: 'loaiMon'},
  {key: 'loaiHocPhan', label: 'Loại học phần', type: 'select', lookup: 'loaiHocPhan'},
  {key: 'batBuoc', label: 'Bắt buộc', type: 'boolean'},
  {key: 'laMonDieuKien', label: 'Môn điều kiện', type: 'boolean'},
  {key: 'thuTu', label: 'Thứ tự', type: 'number', step: 1},
  {key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true}
]

function chuanHoaTuKhoaTimKiem(value) {
  return String(value || '')
      .trim()
      .toLowerCase()
      .normalize('NFD')
      .replace(/[\u0300-\u036f]/g, '')
}

const danhSachMonHocTang7DaLoc = computed(() => {
  const tuKhoa = chuanHoaTuKhoaTimKiem(tuKhoaLocMonHocTang7.value)
  const chuongTrinhVersionId = selected.chuongTrinhVersion?.id

  // Tầng 7 chỉ dùng MÔN ĐÃ NẰM TRONG VERSION (đã copy vào version), không dùng toàn bộ môn mẫu.
  const monTrongVersion = (duLieu.value.chuongTrinhMon || [])
      .filter((row) => String(row.chuongTrinhVersionId || '') === String(chuongTrinhVersionId || ''))

  // Map monHocId → tenKyDaGan (backend trả sẵn trong chuongTrinhMon response)
  const monHocKyMap = {}
  monTrongVersion.forEach((row) => {
    const monHocId = String(row.monHocId || row.monId || '')
    if (monHocId) monHocKyMap[monHocId] = row.tenKyDaGan || 'Chưa gán kỳ'
  })

  const monHocIdTrongVersion = new Set(Object.keys(monHocKyMap))

  const danhSach = (duLieu.value.monHoc || [])
      .filter((mon) => monHocIdTrongVersion.has(String(mon.id || '')))
      .map((mon) => ({...mon, tenKyDaGan: monHocKyMap[String(mon.id)] || 'Chưa gán kỳ'}))

  if (!tuKhoa) {
    return danhSach
  }

  return danhSach.filter((mon) => {
    const maMon = chuanHoaTuKhoaTimKiem(mon.maMon)
    const tenMon = chuanHoaTuKhoaTimKiem(mon.tenMon)

    return maMon.includes(tuKhoa) || tenMon.includes(tuKhoa)
  })
})
const chuongTrinhMonParentText = computed(() => {
  return `Đang chọn: Version ${selected.chuongTrinhVersion?.tenVersion || '-'} | Môn học ${selected.monHoc?.tenMon || '-'} | Kỳ ${selected.khungKy?.tenKy || '-'}`
})
const monTuChonParent = computed(() => ({
  nhomId: selected.nhomTuChon?.id || null,
  chuongTrinhMonId: selected.chuongTrinhMon?.id || null
}))

const monTuChonParentText = computed(() => {
  return `Đang chọn: Nhóm tự chọn ${selected.nhomTuChon?.ten || '-'} | Môn CT ${selected.chuongTrinhMon?.maMonTrongCt || '-'}`
})

const chuongTrinhMonOnlyParent = computed(() => ({
  chuongTrinhMonId:
      selected.chuongTrinhMon?.id ||
      route.query.chuongTrinhMonId ||
      route.params.chuongTrinhMonId ||
      null
}))

const syllabusMonHocApDungParent = computed(() => ({
  chuongTrinhMonId:
      selected.chuongTrinhMon?.id ||
      route.query.chuongTrinhMonId ||
      route.params.chuongTrinhMonId ||
      null,
  syllabusMonHocMauId: selected.syllabusMonHocmau?.id || null
}))

const chuongTrinhMonOnlyParentText = computed(() => {
  return `Đang chọn: Môn trong chương trình ${selected.chuongTrinhMon?.maMonTrongCt || '-'} - ${selected.chuongTrinhMon?.tenMon || '-'}`
})

function taoOThongTinMonTrongChuongTrinh() {
  return [
    {
      key: 'monTrongChuongTrinhDangChon',
      label: 'Môn trong chương trình',
      value: `${selected.chuongTrinhMon?.maMonTrongCt || '-'} - ${selected.chuongTrinhMon?.tenMon || '-'}`
    }
  ]
}

function taoOThongTinSyllabusMonHoc() {
  return [
    {
      key: 'syllabusDangChon',
      label: 'Syllabus',
      value: layGiaTriHienThi(
          selected.syllabusMonHoc?.ten,
          selected.syllabusMonHoc?.tenSyllabusMonHocmau,
          selected.syllabusMonHoc?.ma,
          selected.syllabusMonHoc?.maSyllabusMonHocmau
      )
    },
    {
      key: 'monTrongChuongTrinhDangChon',
      label: 'Môn trong chương trình',
      value: `${selected.chuongTrinhMon?.maMonTrongCt || '-'} - ${selected.chuongTrinhMon?.tenMon || '-'}`
    }
  ]
}

function tachDanhSachDuongDanTaiLieuCu(duongDan) {
  if (!duongDan) return []

  const text = String(duongDan || '').trim()

  if (!text) return []

  if (text.startsWith('[')) {
    try {
      const parsed = JSON.parse(text)

      if (Array.isArray(parsed)) {
        return parsed
            .map((item) => {
              if (typeof item === 'string') {
                return {
                  id: null,
                  tenmau: '',
                  tenHienThi: '',
                  duongDan: item,
                  contentType: '',
                  dungLuong: 0
                }
              }

              return {
                id: item?.id || item?.tepDinhKemId || null,
                tenmau: item?.tenmau || item?.fileName || '',
                tenHienThi: item?.tenHienThi || item?.tenmau || item?.fileName || '',
                duongDan: item?.duongDan || '',
                contentType: item?.contentType || '',
                dungLuong: item?.dungLuong || 0
              }
            })
            .filter((item) => item.duongDan)
      }
    } catch (error) {
      return [{
        id: null,
        tenmau: '',
        tenHienThi: '',
        duongDan: text,
        contentType: '',
        dungLuong: 0
      }]
    }
  }

  return [{
    id: null,
    tenmau: '',
    tenHienThi: '',
    duongDan: text,
    contentType: '',
    dungLuong: 0
  }]
}

function taoGiaTriDuongDanSauKhiThemFile(danhSachDuongDan) {
  const danhSachHopLe = Array.isArray(danhSachDuongDan)
      ? danhSachDuongDan.filter((item) => item?.duongDan)
      : []

  if (!danhSachHopLe.length) return null

  if (danhSachHopLe.length === 1) {
    return danhSachHopLe[0].duongDan
  }

  return JSON.stringify(danhSachHopLe)
}

async function uploadFileTaiLieu({file, files, payload, field}) {
  const syllabusMonId =
      payload?.syllabusMonId ||
      selected.syllabusMonHoc?.id ||
      route.query.syllabusMonId ||
      0

  const chuongTrinhMonId =
      selected.chuongTrinhMon?.id ||
      route.query.chuongTrinhMonId ||
      route.params.chuongTrinhMonId ||
      null

  const monHocId =
      selected.chuongTrinhMon?.monHocId ||
      selected.monHoc?.id ||
      route.query.monHocId ||
      route.params.monHocId ||
      null

  const nghiepVu = field?.nghiepVu || 'tai_lieu_syllabus'
  const danhSachFile = Array.isArray(files) && files.length ? files : [file].filter(Boolean)

  if (!danhSachFile.length) {
    return {}
  }

  const danhSachFileInfo = await tepDinhKemUploadService.uploadMany({
    files: danhSachFile,
    module: 'dao-tao',
    nghiepVu,
    doiTuongId: syllabusMonId,
    nguoiGuiLoai: 'DAO_TAO',
    nguoiGuiId: 1,
    nguoiGuiTen: 'Đào tạo',
    moTa: payload?.ten || '',

    nganhId:
        selected.nganh?.id ||
        route.params.nganhId ||
        route.query.nganhId ||
        null,

    chuongTrinhId:
        selected.chuongTrinh?.id ||
        route.params.chuongTrinhId ||
        route.query.chuongTrinhId ||
        null,

    chuongTrinhVersionId:
        selected.chuongTrinhVersion?.id ||
        route.params.versionId ||
        route.query.versionId ||
        null,

    monHocId,
    chuongTrinhMonId,
    syllabusMonHocId: syllabusMonId,
    taiLieumauId: payload?.taiLieumauId || null,
    tangNghiepVu: field?.tangNghiepVu || 'tang-10-chi-tiet-syllabus-ap-dung'
  })

  const danhSachDuongDanCu = tachDanhSachDuongDanTaiLieuCu(payload?.duongDan)

  const danhSachDuongDanMoi = danhSachFileInfo.map((fileInfo, index) => ({
    id: fileInfo?.id || fileInfo?.tepDinhKemId || null,
    tenmau: fileInfo?.tenmau || fileInfo?.fileName || danhSachFile[index]?.name || '',
    tenHienThi: fileInfo?.tenHienThi || danhSachFile[index]?.webkitRelativePath || danhSachFile[index]?.name || '',
    duongDan: tepDinhKemUploadService.taoDuongDanTaiFile(fileInfo),
    contentType: fileInfo?.contentType || danhSachFile[index]?.type || '',
    dungLuong: fileInfo?.dungLuong || danhSachFile[index]?.size || 0
  }))

  const danhSachDuongDanSauKhiThem = [
    ...danhSachDuongDanCu,
    ...danhSachDuongDanMoi
  ]

  return {
    duongDan: taoGiaTriDuongDanSauKhiThemFile(danhSachDuongDanSauKhiThem)
  }
}

async function uploadFileSyllabusChuongTrinh({file, files, payload, field, editingId}) {
  const danhSachFile = Array.isArray(files) && files.length ? files : [file].filter(Boolean)

  if (!danhSachFile.length) {
    return {}
  }

  const versionId =
      payload?.chuongTrinhVersionId ||
      selected.chuongTrinhVersion?.id ||
      route.params.versionId ||
      route.query.versionId ||
      null

  const syllabusChuongTrinhId =
      payload?.id ||
      payload?.syllabusChuongTrinhId ||
      selected.syllabusChuongTrinh?.id ||
      null

  const syllabusChuongTrinhmauId =
      payload?.syllabusChuongTrinhmauId ||
      selected.syllabusChuongTrinhmau?.id ||
      (field?.tepNguon === 'syllabusChuongTrinhmau' ? editingId : null) ||
      null

  const danhSachFileInfo = await tepDinhKemUploadService.uploadMany({
    files: danhSachFile,
    module: 'dao-tao',
    nghiepVu: field?.nghiepVu || 'syllabus_chuong_trinh',
    doiTuongId: syllabusChuongTrinhId || syllabusChuongTrinhmauId || versionId || 0,
    nguoiGuiLoai: 'DAO_TAO',
    nguoiGuiId: 1,
    nguoiGuiTen: 'Đào tạo',
    moTa: payload?.ten || '',

    nganhId:
        selected.nganh?.id ||
        route.params.nganhId ||
        route.query.nganhId ||
        null,

    chuongTrinhId:
        selected.chuongTrinh?.id ||
        route.params.chuongTrinhId ||
        route.query.chuongTrinhId ||
        null,

    chuongTrinhVersionId: versionId,

    syllabusChuongTrinhId:
        field?.tepNguon === 'syllabusChuongTrinh'
            ? syllabusChuongTrinhId
            : null,

    taiLieumauId:
        field?.tepNguon === 'syllabusChuongTrinhmau'
            ? syllabusChuongTrinhmauId
            : null,

    tangNghiepVu: field?.tangNghiepVu || 'tang-5-syllabus-chuong-trinh'
  })

  const danhSachDuongDanCu = tachDanhSachDuongDanTaiLieuCu(payload?.duongDan)

  const danhSachDuongDanMoi = danhSachFileInfo.map((fileInfo, index) => ({
    id: fileInfo?.id || fileInfo?.tepDinhKemId || null,
    tenmau: fileInfo?.tenmau || fileInfo?.fileName || danhSachFile[index]?.name || '',
    tenHienThi: fileInfo?.tenHienThi || danhSachFile[index]?.webkitRelativePath || danhSachFile[index]?.name || '',
    duongDan: tepDinhKemUploadService.taoDuongDanTaiFile(fileInfo),
    contentType: fileInfo?.contentType || danhSachFile[index]?.type || '',
    dungLuong: fileInfo?.dungLuong || danhSachFile[index]?.size || 0
  }))

  return {
    duongDan: taoGiaTriDuongDanSauKhiThemFile([
      ...danhSachDuongDanCu,
      ...danhSachDuongDanMoi
    ])
  }
}

const monTienQuyetParent = computed(() => ({
  monId: selected.chuongTrinhMon?.id || null
}))

const syllabusMonParent = computed(() => ({
  syllabusMonId: selected.syllabusMonHoc?.id || null
}))

const syllabusMonParentText = computed(() => {
  const tenSyllabus =
      selected.syllabusMonHoc?.ten ||
      selected.syllabusMonHoc?.tenSyllabusMonHocmau ||
      selected.syllabusMonHoc?.ma ||
      selected.syllabusMonHoc?.maSyllabusMonHocmau ||
      selected.syllabusMonHoc?.mucTieu ||
      '-'

  return `Đang chọn: Syllabus môn học ${tenSyllabus} | Môn CT ${selected.chuongTrinhMon?.maMonTrongCt || selected.chuongTrinhMon?.tenMon || '-'}`
})

function dongDangDung(key) {
  return viewed[key] || selected[key] || null
}

const chuongTrinhFilter = computed(() => {
  const nganhHeDaoTaoId =
    selected.nganhHeDaoTao?.id ||
    viewed.nganhHeDaoTao?.id ||
    route.query.nganhHeDaoTaoId ||
    null
  return { nganhHeDaoTaoId }
})

const chuongTrinhVersionFilter = computed(() => ({
  chuongTrinhId: dongDangDung('chuongTrinh')?.id || null
}))

const versionFilter = computed(() => ({
  chuongTrinhVersionId: dongDangDung('chuongTrinhVersion')?.id || null
}))

const khungKyFilter = computed(() => ({
  chuongTrinhVersionId: dongDangDung('chuongTrinhVersion')?.id || null,
  loaiChuongTrinhId: dongDangDung('loaiChuongTrinh')?.id || selected.chuongTrinh?.loaiChuongTrinhId || null
}))

const chuongTrinhMonViewFilter = computed(() => ({
  chuongTrinhVersionId: dongDangDung('chuongTrinhVersion')?.id || null,
  khungKyId: dongDangDung('khungKy')?.id || null
}))

const chuongTrinhMonOnlyFilter = computed(() => ({
  chuongTrinhMonId:
      dongDangDung('chuongTrinhMon')?.id ||
      route.query.chuongTrinhMonId ||
      route.params.chuongTrinhMonId ||
      null
}))
const syllabusMonFilter = computed(() => ({
  syllabusMonId: dongDangDung('syllabusMonHoc')?.id || null
}))

const syllabusMonHocScopeParent = computed(() => ({
  syllabusMonHocId: dongDangDung('syllabusMonHoc')?.id || null
}))

const syllabusMonHocScopeFilter = computed(() => ({
  syllabusMonHocId: dongDangDung('syllabusMonHoc')?.id || null
}))

function laMauQuyDoiKetQua(row) {
  return String(row?.loaiMau || row?.loai_mau || '').trim().toUpperCase() === 'QUY_DOI_KET_QUA'
}

function laySyllabusNguonCotDiemId() {
  return selected.syllabusMonHoc?.syllabusMonHocMauId
      || selected.syllabusMonHoc?.syllabusMonHocNguonId
      || selected.syllabusMonHocmau?.id
      || null
}
function laySyllabusmauDangChon(row = {}) {
  const syllabusMonHocMauId = row?.syllabusMonHocMauId
      || row?.syllabusMonHocmau?.id
      || laySyllabusNguonCotDiemId()

  if (!syllabusMonHocMauId) return null

  const trongDanhSach = (duLieu.value.syllabusMonHocmau || []).find((item) => {
    return String(item.id || '') === String(syllabusMonHocMauId || '')
  })

  if (trongDanhSach) return trongDanhSach

  if (String(selected.syllabusMonHocmau?.id || '') === String(syllabusMonHocMauId || '')) {
    return selected.syllabusMonHocmau
  }

  return null
}

function layTenSyllabusmauHienThi(row = {}) {
  const syllabusmau = laySyllabusmauDangChon(row)
  const syllabusMonHocMauId = row?.syllabusMonHocMauId
      || row?.syllabusMonHocmau?.id
      || laySyllabusNguonCotDiemId()

  return row?.tenSyllabusMonHocmau
      || row?.tenSyllabusmau
      || syllabusmau?.ten
      || selected.syllabusMonHoc?.tenSyllabusMonHocmau
      || row?.tenSyllabusMonHoc
      || row?.tenSyllabus
      || syllabusmau?.ma
      || selected.syllabusMonHoc?.maSyllabusMonHocmau
      || (syllabusMonHocMauId ? `Syllabus mẫu #${syllabusMonHocMauId}` : '-')
}
const cotDiemSyllabusmauRows = computed(() => {
  const sourceSyllabusId = laySyllabusNguonCotDiemId()

  if (!sourceSyllabusId) return []

  return (duLieu.value.cauHinhDanhGiaMau || [])
      .filter((row) => String(row.syllabusMonHocMauId || '') === String(sourceSyllabusId || ''))
      .map((row) => ({
        ...row,

        tenSyllabusmauHienThi: layTenSyllabusmauHienThi(row)
      }))
})
const cotDiemSyllabusApDungRows = computed(() => {
  const syllabusMonHocId = syllabusMonHocScopeFilter.value.syllabusMonHocId
      || syllabusMonHocScopeParent.value.syllabusMonHocId
      || selected.syllabusMonHoc?.id
      || null

  if (!syllabusMonHocId) return []

  return (duLieu.value.cauHinhDanhGia || [])
      .filter((row) => String(row.syllabusMonHocId || '') === String(syllabusMonHocId || ''))
      .map((row) => ({
        ...row,

        tenSyllabusmauHienThi: layTenSyllabusmauHienThi(row)
      }))
})
watch(
    () => [syllabusMonHocScopeParent.value.syllabusMonHocId, laySyllabusNguonCotDiemId()].join('|'),
    () => {
      cotDiemMauDaTichChonIds.value = []
    }
)


const quyDoiKetQuaDungChungRows = computed(() => {
  const syllabusMonHocMauId = selected.syllabusMonHoc?.syllabusMonHocMauId || selected.syllabusMonHocmau?.id || null
  if (!syllabusMonHocMauId) return []

  return (quyDoiDiemMaumauRows.value || [])
      .filter(laMauQuyDoiKetQua)
      .filter((row) => String(row.syllabusMonHocMauId || '') === String(syllabusMonHocMauId || ''))
})

const mauQuyDoiKetQuaDaGanSyllabusRows = computed(() => {
  const syllabusMonHocId = syllabusMonHocScopeFilter.value.syllabusMonHocId
  if (!syllabusMonHocId) return []

  return (duLieu.value.chuongTrinhMonQuyDoiDiemMau || [])
      .filter((row) => String(row.syllabusMonHocId || '') === String(syllabusMonHocId || ''))
      .filter(laMauQuyDoiKetQua)
})


const dieuKienMonHocmauTheoSyllabus = computed(() => {
  const syllabusMonHocMauId = selected.syllabusMonHoc?.syllabusMonHocMauId || selected.syllabusMonHocmau?.id || null
  if (!syllabusMonHocMauId) return []

  const dieuKienmauMap = new Map((duLieu.value.dieuKienMonHocmau || []).map((row) => [String(row.id || ''), row]))

  return (duLieu.value.syllabusMonHocmauDieuKien || [])
      .filter((row) => String(row.syllabusMonHocMauId || '') === String(syllabusMonHocMauId || ''))
      .map((row) => {
        const mau = dieuKienmauMap.get(String(row.dieuKienmauId || '')) || {}
        return {
          ...mau,
          ...row,
          id: row.dieuKienmauId || mau.id || row.id,
          dieuKienmauId: row.dieuKienmauId || mau.id || null,
          syllabusMonHocmauDieuKienId: row.id,
          ma: row.ma || mau.ma || '',
          loai: row.loai || mau.loai || '',
          noiDung: row.noiDung || mau.noiDung || '',
          ghiChu: row.ghiChu || mau.ghiChu || '',
          createdAt: row.createdAt || mau.createdAt || null,
          updatedAt: row.updatedAt || mau.updatedAt || null
        }
      })
      .sort((a, b) => Number(a.thuTu || 0) - Number(b.thuTu || 0))
})

function coGiaTriCha(value) {
  return value !== null && value !== undefined && value !== ''
}

function locDongTheoCha(rows = [], parentValues = {}) {
  const dieuKien = Object.entries(parentValues).filter(([, value]) => coGiaTriCha(value))
  if (!dieuKien.length) return rows

  return rows.filter((row) => {
    return dieuKien.every(([key, value]) => String(row?.[key] ?? '') === String(value))
  })
}

function layIdDongTheoCha(rows = [], parentValues = {}) {
  const dieuKien = Object.entries(parentValues).filter(([, value]) => coGiaTriCha(value))
  if (!dieuKien.length) return []

  return rows
      .filter((row) => {
        return dieuKien.every(([key, value]) => String(row?.[key] ?? '') === String(value))
      })
      .map((row) => row?.id)
      .filter((id) => id !== null && id !== undefined && id !== '')
}

function laySavedIdsTheoBang(key, allRows = [], rows = [], parentValues = {}, filterValues = {}) {
  if (key === 'trinhDoDaoTao') return idsTrinhDoDaGanTheoNganh()
  if (key === 'loaiChuongTrinh') return idsLoaiChuongTrinhDaGanTheoNganh()

  return laySelectedIdsTheoBang(key, allRows, rows, parentValues, filterValues)
}

function laySelectedIdsTheoBang(key, allRows = [], rows = [], parentValues = {}, filterValues = {}) {
  if (key === 'trinhDoDaoTao' || key === 'loaiChuongTrinh') {
    return []
  }

  const bangMauConSyllabus = {
    mucTieuChuongTrinhmau: {apDungKey: 'mucTieuChuongTrinh'},
    nangLucDauRamau: {apDungKey: 'nangLucDauRa'},
    viTriViecLammau: {apDungKey: 'viTriViecLam'},
    dieuKienTotNghiepmau: {apDungKey: 'dieuKienTotNghiep'}
  }

  if (bangMauConSyllabus[key]) {
    const syllabusId = syllabusChuongTrinhIdDangGan.value
    const mauId = syllabusChuongTrinhMauIdDangGan.value
    if (!syllabusId || !mauId) return []

    const maDaLuu = new Set(
        (duLieu.value[bangMauConSyllabus[key].apDungKey] || [])
            .filter((row) => String(row.syllabusChuongTrinhId || '') === String(syllabusId || ''))
            .map((row) => String(row.ma || '').trim())
            .filter(Boolean)
    )

    return (duLieu.value[key] || [])
        .filter((row) => String(row.syllabusChuongTrinhMauId || row.syllabusChuongTrinhmauId || '') === String(mauId || ''))
        .filter((row) => maDaLuu.has(String(row.ma || '').trim()))
        .map((row) => row.id)
        .filter((id) => id !== null && id !== undefined && id !== '')
  }
  if (key === 'syllabusChuongTrinhmau') {
    const chuongTrinhVersionId = filterValues.chuongTrinhVersionId || parentValues.chuongTrinhVersionId || selected.chuongTrinhVersion?.id
    if (!chuongTrinhVersionId) return []
    return (duLieu.value.syllabusChuongTrinh || [])
        .filter((row) => String(row.chuongTrinhVersionId || '') === String(chuongTrinhVersionId || ''))
        .map((row) => row.syllabusChuongTrinhmauId)
        .filter((id) => id !== null && id !== undefined && id !== '')
  }

  if (key === 'khungKymau') {
    const chuongTrinhVersionId = filterValues.chuongTrinhVersionId || selected.chuongTrinhVersion?.id
    if (!chuongTrinhVersionId) return []
    return (duLieu.value.khungKy || [])
        .filter((row) => String(row.chuongTrinhVersionId || '') === String(chuongTrinhVersionId || ''))
        .map((row) => row.khungKymauId)
        .filter((id) => id !== null && id !== undefined && id !== '')
  }

  if (key === 'nhomKienThucmau') {
    const chuongTrinhVersionId = filterValues.chuongTrinhVersionId || selected.chuongTrinhVersion?.id
    if (!chuongTrinhVersionId) return []
    return (duLieu.value.nhomKienThuc || [])
        .filter((row) => String(row.chuongTrinhVersionId || '') === String(chuongTrinhVersionId || ''))
        .map((row) => row.nhomKienThucmauId)
        .filter((id) => id !== null && id !== undefined && id !== '')
  }

  if (key === 'nhomTuChonmau') {
    const chuongTrinhVersionId = filterValues.chuongTrinhVersionId || selected.chuongTrinhVersion?.id
    if (!chuongTrinhVersionId) return []
    return (duLieu.value.nhomTuChon || [])
        .filter((row) => String(row.chuongTrinhVersionId || '') === String(chuongTrinhVersionId || ''))
        .map((row) => row.nhomTuChonmauId)
        .filter((id) => id !== null && id !== undefined && id !== '')
  }

  if (key === 'dieuKienMonHocmau') {
    const syllabusMonId = filterValues.syllabusMonId || parentValues.syllabusMonId || selected.syllabusMonHoc?.id
    if (!syllabusMonId) return []

    const maDaLuu = new Set(
        (duLieu.value.dieuKienMonHoc || [])
            .filter((row) => String(row.syllabusMonId || '') === String(syllabusMonId || ''))
            .map((row) => String(row.ma || ''))
            .filter(Boolean)
    )

    return (duLieu.value.dieuKienMonHocmau || [])
        .filter((row) => maDaLuu.has(String(row.ma || '')))
        .map((row) => row.id)
        .filter((id) => id !== null && id !== undefined && id !== '')
  }

  if (key === 'taiLieumau') {
    const syllabusMonId = filterValues.syllabusMonId || parentValues.syllabusMonId || selected.syllabusMonHoc?.id
    if (!syllabusMonId) return []

    const maDaLuu = new Set(
        (duLieu.value.syllabusTaiLieu || [])
            .filter((row) => String(row.syllabusMonId || '') === String(syllabusMonId || ''))
            .map((row) => String(row.ma || ''))
            .filter(Boolean)
    )

    return (duLieu.value.taiLieumau || [])
        .filter((row) => maDaLuu.has(String(row.ma || '')))
        .map((row) => row.id)
        .filter((id) => id !== null && id !== undefined && id !== '')
  }

  if (key === 'syllabusMonHocmauChuongBai') {
    const syllabusMonId = filterValues.syllabusMonId || parentValues.syllabusMonId || selected.syllabusMonHoc?.id
    if (!syllabusMonId) return []

    const chuongBaiDaLuu = duLieu.value.syllabusChuongBai || []
    return rows
        .filter((row) => chuongBaiDaLuu.some((item) => {
          if (String(item.syllabusMonId || '') !== String(syllabusMonId || '')) return false
          const cungMaChuong = row.maChuong && item.maChuong && String(item.maChuong || '') === String(row.maChuong || '')
          const cungTen = row.ten && item.ten && String(item.ten || '') === String(row.ten || '')
          const cungThuTu = row.thuTu !== null && row.thuTu !== undefined && item.thuTu !== null && item.thuTu !== undefined
              && String(item.thuTu || '') === String(row.thuTu || '')
          return cungMaChuong
              || cungThuTu
              || (cungTen && !row.maChuong && !item.maChuong && row.thuTu == null && item.thuTu == null)
        }))
        .map((row) => row.id)
        .filter((id) => id !== null && id !== undefined && id !== '')
  }

  if (key === 'syllabusMonHocmauTaiLieu') {
    const syllabusMonId = filterValues.syllabusMonId || parentValues.syllabusMonId || selected.syllabusMonHoc?.id
    if (!syllabusMonId) return []

    const taiLieuDaLuu = duLieu.value.syllabusTaiLieu || []
    return rows
        .filter((row) => taiLieuDaLuu.some((item) => {
          if (String(item.syllabusMonId || '') !== String(syllabusMonId || '')) return false
          const cungMa = row.ma && item.ma && String(item.ma || '') === String(row.ma || '')
          const cungTen = row.ten && item.ten && String(item.ten || '') === String(row.ten || '')
          const cungTaiLieumau = row.taiLieumauId && item.taiLieumauId && String(item.taiLieumauId || '') === String(row.taiLieumauId || '')
          return cungMa || cungTen || cungTaiLieumau
        }))
        .map((row) => row.id)
        .filter((id) => id !== null && id !== undefined && id !== '')
  }

  if (key === 'syllabusMonHocmau') {
    const chuongTrinhMonId = filterValues.chuongTrinhMonId || parentValues.chuongTrinhMonId || selected.chuongTrinhMon?.id
    if (!chuongTrinhMonId) return []

    return (duLieu.value.syllabusMonHoc || [])
        .filter((row) => String(row.chuongTrinhMonId || '') === String(chuongTrinhMonId || ''))
        .map((row) => row.syllabusMonHocMauId)
        .filter((id) => id !== null && id !== undefined && id !== '')
  }


  if (key === 'quyDoiDiemMau') {
    const syllabusMonHocId = filterValues.syllabusMonHocId || parentValues.syllabusMonHocId || selected.syllabusMonHoc?.id
    if (!syllabusMonHocId) return []

    return (duLieu.value.chuongTrinhMonQuyDoiDiemMau || [])
        .filter((row) => String(row.syllabusMonHocId || '') === String(syllabusMonHocId || ''))
        .filter(laMauQuyDoiKetQua)
        .map((row) => row.quyDoiDiemMauId)
        .filter((id) => id !== null && id !== undefined && id !== '')
  }
  if (key === 'cauHinhDanhGiaMau') {
    const chuongTrinhMonId = filterValues.chuongTrinhMonId || parentValues.chuongTrinhMonId || selected.chuongTrinhMon?.id

    if (!chuongTrinhMonId) return []

    return (duLieu.value.cauHinhDanhGiaMau || [])
        .filter((mau) => {
          const tenCotDiemMau = String(mau.tenCotDiem || '').trim()
          const ghiChuMau = String(mau.ghiChu || '').trim()

          return (duLieu.value.quyDoiDiem || []).some((row) => {
            const tenDaLuu = String(row.tenCotDiemMau || row.ghiChu || '').trim()

            return String(row.chuongTrinhMonId || '') === String(chuongTrinhMonId || '')
                && String(row.loaiMau || 'COT_DIEM') === String(mau.loaiDiem || 'COT_DIEM')
                && String(row.tyLe ?? '') === String(mau.tyLe ?? '')
                && String(row.diemToiDa ?? '') === String(mau.diemToiDa ?? '')
                && String(row.thuTu ?? '') === String(mau.thuTu ?? '')
                && String(row.batBuoc !== false) === String(mau.batBuoc !== false)
                && (
                    tenDaLuu === tenCotDiemMau
                    || tenDaLuu === ghiChuMau
                    || String(row.ghiChu || '').trim() === tenCotDiemMau
                    || String(row.tenCotDiemMau || '').trim() === tenCotDiemMau
                )
          })
        })
        .map((mau) => mau.id)
        .filter((id) => id !== null && id !== undefined && id !== '')
  }
  if (key === 'nganh') {
    return []
  }

  if (key === 'monHoc') {
    const chuongTrinhVersionId = filterValues.chuongTrinhVersionId || parentValues.chuongTrinhVersionId || selected.chuongTrinhVersion?.id
    const khungKyId = filterValues.khungKyId || parentValues.khungKyId || selected.khungKy?.id

    return (duLieu.value.chuongTrinhMon || [])
        .filter((row) => String(row.chuongTrinhVersionId || '') === String(chuongTrinhVersionId || ''))
        .filter((row) => !khungKyId || String(row.khungKyId || '') === String(khungKyId || ''))
        .map((row) => row.monHocId || row.monId)
        .filter((id) => id !== null && id !== undefined && id !== '')
  }

  if (key === 'chuongTrinhMon') {
    return layIdDongTheoCha(allRows, filterValues)
  }

  return layIdDongTheoCha(allRows.length ? allRows : rows, filterValues)
}

const bangXuongSongKeys = new Set([
  'nganh',
  'trinhDoDaoTao',
  'loaiChuongTrinh',
  'chuongTrinh',
  'chuongTrinhVersion',
  'khungKy',
  'nhomKienThuc',
  'nhomTuChon',
  'monHoc',
  'chuongTrinhMon',
  'syllabusMonHoc'
])


const bangDocLapKhongLocTheoCha = new Set([
  // Các bảng mẫu/kho mẫu dùng chung phải luôn hiện toàn bộ dữ liệu từ API mẫu.
  // Việc chọn Chương trình môn chỉ dùng cho nút Lưu để gắn qua bảng nối, không được làm mất dữ liệu mẫu.
  'quyDoiDiemMau'
])

const bangmauMauKeys = new Set([
  'mucTieuChuongTrinhmau',
  'nangLucDauRamau',
  'viTriViecLammau',
  'dieuKienTotNghiepmau',
  'syllabusChuongTrinhmau',
  'khungKymau',
  'nhomKienThucmau',
  'nhomTuChonmau',
  'monHoc',
  'syllabusMonHocmau',
  'dieuKienMonHocmau',
  'taiLieumau',
  'quyDoiDiemMau'
])

const nhanNutXemTheoBang = {
  nganh: 'Xem chương trình',
  chuongTrinh: 'Xem version',
  chuongTrinhVersion: 'Xem chi tiết version',
  monHoc: 'Xem syllabus mẫu',
  chuongTrinhMon: 'Xem syllabus áp dụng',
  syllabusMonHoc: 'Xem chi tiết syllabus',
  nhomKienThuc: 'Xem môn trong nhóm',
  nhomTuChon: 'Xem môn tự chọn'
}

const bangKhongCanChon = new Set([
  'chuongTrinhVersionMucTieu',
  'chuongTrinhVersionNangLuc',
  'chuongTrinhVersionViTriViecLam',
  'chuongTrinhVersionDieuKienTotNghiep',
  'monTuChon',
  'monTienQuyet',
  'chuongTrinhMonQuyDoiDiemMau',
  'cauHinhDanhGiaMau',
  'cauHinhDanhGia',
  'quyDoiDiem',
  'syllabusMonHocmauChuongBai',
  'syllabusMonHocmauDieuKien',
  'syllabusMonHocmauTaiLieu',
  'syllabusMonHocDieuKien',
  'syllabusMonHocTaiLieu',
])

const bangChiLuuGanKhongChon = new Set([
  'syllabusChuongTrinhmau',
  'mucTieuChuongTrinhmau',
  'nangLucDauRamau',
  'viTriViecLammau',
  'dieuKienTotNghiepmau',
  'khungKymau',
  'nhomKienThucmau',
  'nhomTuChonmau',
  'monHoc',
  'syllabusMonHocmau',
  'quyDoiDiemMau',
  'dieuKienMonHocmau',
  'taiLieumau'
])

const bangCoNutLuuBoLuuMacDinh = new Set([
  'syllabusChuongTrinhmau',
  'mucTieuChuongTrinhmau',
  'nangLucDauRamau',
  'viTriViecLammau',
  'dieuKienTotNghiepmau',
  'khungKymau',
  'nhomKienThucmau',
  'nhomTuChonmau',
  'syllabusMonHocmau',
  'quyDoiDiemMau',
  'dieuKienMonHocmau',
  'taiLieumau'
])

const bangChiHienTrangThaiDaLuuMacDinh = new Set([
  'trinhDoDaoTao',
  'loaiChuongTrinh',
  'chuongTrinh',
  'chuongTrinhVersion',
  'khungKy',
  'nhomKienThuc',
  'nhomTuChon',
  'monHoc',
  'chuongTrinhMon',
  'syllabusMonHoc',
  'chuongTrinhVersionMucTieu',
  'chuongTrinhVersionNangLuc',
  'chuongTrinhVersionViTriViecLam',
  'chuongTrinhVersionDieuKienTotNghiep',
  'monTuChon',
  'monTienQuyet',
  'chuongTrinhMonQuyDoiDiemMau',
  'cauHinhDanhGiaMau',
  'cauHinhDanhGia',
  'quyDoiDiem',
  'syllabusMonHocmauChuongBai',
  'syllabusMonHocmauDieuKien',
  'syllabusMonHocmauTaiLieu',
  'syllabusChuongBai',
  'syllabusMonHocDieuKien',
  'dieuKienMonHoc',
  'syllabusMonHocTaiLieu',
  'syllabusTaiLieu'
])


function chonDongBang(bang, item) {
  if (bang.key === 'chuongTrinhMon') {
    selectEntity(bang.key, item)
    chonMonHocTheoChuongTrinhMon(item)
    return
  }

  if (bangCoNutLuuLienKet.has(bang.key)) {
    selectEntity(bang.key, item)
    return
  }

  if (bang.loaiBang === 'phu') {
    chonBangPhuVaoLuong(bang.key, item, bang.parentValues, bang.service)
    return
  }

  selectEntity(bang.key, item)
}

function xemDongBang(bang, item) {
  viewEntity(bang.key, item)

  if (!item) return

  selectEntity(bang.key, item)

  if (bang.key === 'nganh') {
    router.push({name: 'DaoTao.XemChuongTrinh.CauHinh', params: {nganhId: item.id}})
    return
  }

  if (bang.key === 'trinhDoDaoTao' || bang.key === 'loaiChuongTrinh') {
    const trinhDoId = bang.key === 'trinhDoDaoTao' ? item.id : selected.trinhDoDaoTao?.id
    const loaiChuongTrinhId = bang.key === 'loaiChuongTrinh' ? item.id : selected.loaiChuongTrinh?.id
    if (selected.nganh?.id && trinhDoId && loaiChuongTrinhId) {
      router.push({
        name: 'DaoTao.XemChuongTrinh.ChuongTrinh',
        params: {nganhId: selected.nganh.id},
        query: {trinhDoId, loaiChuongTrinhId}
      })
    }
    return
  }

  if (bang.key === 'chuongTrinh') {
    router.push({
      name: 'DaoTao.XemChuongTrinh.Version',
      params: {nganhId: item.nganhId || selected.nganh?.id, chuongTrinhId: item.id},
      query: {
        nganhHeDaoTaoId: item.nganhHeDaoTaoId || selected.nganhHeDaoTao?.id || undefined
      }
    })
    return
  }

  if (bang.key === 'chuongTrinhVersion') {
    router.push({
      name: 'DaoTao.XemChuongTrinh.TongQuan',
      params: {
        nganhId: selected.nganh?.id,
        chuongTrinhId: item.chuongTrinhId || selected.chuongTrinh?.id,
        versionId: item.id
      }
    })
    return
  }

  if (bang.key === 'khungKy') {
    router.push({
      name: 'DaoTao.XemChuongTrinh.Mon',
      params: {
        nganhId: selected.nganh?.id,
        chuongTrinhId: selected.chuongTrinh?.id,
        versionId: selected.chuongTrinhVersion?.id
      },
      query: {
        khungKyId: item.id
      }
    })
    return
  }


  if (bang.key === 'syllabusMonHoc') {
    const chuongTrinhMonId = item.chuongTrinhMonId || selected.chuongTrinhMon?.id
    const chuongTrinhMon = timDongTheoId('chuongTrinhMon', chuongTrinhMonId) || selected.chuongTrinhMon

    router.push({
      name: 'DaoTao.XemChuongTrinh.SyllabusApDung',
      params: taoParamsTangSau({
        nganhId: selected.nganh?.id,
        chuongTrinhId: selected.chuongTrinh?.id,
        versionId: selected.chuongTrinhVersion?.id,
        chuongTrinhMonId
      }),
      query: taoParamsTangSau({
        syllabusMonId: item.id,
        khungKyId: selected.khungKy?.id || chuongTrinhMon?.khungKyId
      })
    })
    return
  }

  if (bang.key === 'monHoc') {
    router.push({
      name: 'DaoTao.XemChuongTrinh.Syllabusmau',
      params: {
        nganhId: selected.nganh?.id,
        chuongTrinhId: selected.chuongTrinh?.id,
        versionId: selected.chuongTrinhVersion?.id,
        monHocId: item.id
      }
    })
    return
  }

  if (bang.key === 'chuongTrinhMon') {
    chonMonHocTheoChuongTrinhMon(item)
    router.push({
      name: 'DaoTao.XemChuongTrinh.Syllabusmau',
      params: {
        nganhId: selected.nganh?.id,
        chuongTrinhId: selected.chuongTrinh?.id,
        versionId: selected.chuongTrinhVersion?.id,
        monHocId: item.monHocId || item.monId || item.monHoc?.id
      },
      query: {
        chuongTrinhMonId: item.id
      }
    })
  }
}

function xemTepTrongCotFileDaLuu(bang, payload) {
  if (!['taiLieumau', 'syllabusTaiLieu', 'syllabusChuongTrinhmau', 'syllabusChuongTrinh'].includes(bang?.key)) return

  const item = payload?.item || payload || {}

  if (!item?.id) {
    baoTin('Không xác định được dòng dữ liệu cần xem tệp.', 'error')
    return
  }

  if (!['syllabusChuongTrinhmau', 'syllabusChuongTrinh'].includes(bang.key)) {
    selectEntity(bang.key, item)
  }

  if (['syllabusChuongTrinhmau', 'syllabusChuongTrinh'].includes(bang.key)) {
    router.push({
      name: 'DaoTao.XemChuongTrinh.SyllabusTep',
      params: taoParamsTangSau({
        nganhId: selected.nganh?.id || route.params.nganhId,
        chuongTrinhId: selected.chuongTrinh?.id || route.params.chuongTrinhId,
        versionId: selected.chuongTrinhVersion?.id || route.params.versionId,
        chuongTrinhMonId: route.params.chuongTrinhMonId || 0
      }),
      query: taoParamsTangSau({
        tepNguon: bang.key,
        syllabusChuongTrinhmauId: bang.key === 'syllabusChuongTrinhmau' ? item.id : null,
        syllabusChuongTrinhId: bang.key === 'syllabusChuongTrinh' ? item.id : null
      })
    })
    return
  }

  const syllabusMonId =
      selected.syllabusMonHoc?.id ||
      item.syllabusMonId ||
      route.query.syllabusMonId ||
      null

  const chuongTrinhMonId =
      selected.chuongTrinhMon?.id ||
      route.params.chuongTrinhMonId ||
      route.query.chuongTrinhMonId ||
      null

  if (!syllabusMonId || !chuongTrinhMonId) {
    baoTin('Cần chọn Syllabus môn học áp dụng và Môn trong chương trình trước khi xem tệp.', 'error')
    return
  }

  const query = {
    syllabusMonId,
    tepNguon: bang.key,
    khungKyId: selected.khungKy?.id || selected.chuongTrinhMon?.khungKyId || route.query.khungKyId
  }

  if (bang.key === 'taiLieumau') {
    query.taiLieumauId = item.id
  }

  if (bang.key === 'syllabusTaiLieu') {
    query.syllabusTaiLieuId = item.id
  }

  router.push({
    name: 'DaoTao.XemChuongTrinh.SyllabusTep',
    params: taoParamsTangSau({
      nganhId: selected.nganh?.id || route.params.nganhId,
      chuongTrinhId: selected.chuongTrinh?.id || route.params.chuongTrinhId,
      versionId: selected.chuongTrinhVersion?.id || route.params.versionId,
      chuongTrinhMonId
    }),
    query: taoParamsTangSau(query)
  })
}

const bangCoNutLuuLienKet = new Set([
  'syllabusChuongTrinhmau',
  'khungKymau',
  'nhomKienThucmau',
  'nhomTuChonmau',
  'syllabusMonHocmau',
  'mucTieuChuongTrinhmau',
  'nangLucDauRamau',
  'viTriViecLammau',
  'dieuKienTotNghiepmau',
  'monHoc',
  'chuongTrinhMon',
  'monTienQuyet',
  'quyDoiDiemMau',
  'cauHinhDanhGiaMau',
  'dieuKienMonHocmau',
  'taiLieumau',
  'dieuKienMonHoc',
  'syllabusChuongBai',
  'syllabusTaiLieu'
])

function datThongBaoBang(key, message, type = 'success') {
  if (!key) return

  tableMessages[key] = {message, type, at: Date.now()}

  setTimeout(() => {
    if (tableMessages[key]?.message === message) {
      delete tableMessages[key]
    }
  }, 5000)
}

function xuLyThongBaoBang(bang, payload) {
  const message = typeof payload === 'string'
      ? payload
      : payload?.message || payload?.text || 'Có lỗi khi thao tác dữ liệu.'

  const type = typeof payload === 'object'
      ? payload?.type || 'success'
      : 'success'

  datThongBaoBang(bang.key, message, type === 'error' ? 'error' : 'success')
}

async function ganTrinhDoVaoNganh(item) {
  if (!item?.id) {
    datThongBaoBang('trinhDoDaoTao', 'Không tìm thấy trình độ đào tạo để gán vào ngành.', 'error')
    return
  }

  const nganhId = selected.nganh?.id || route.params.nganhId || null
  if (!nganhId) {
    datThongBaoBang('trinhDoDaoTao', 'Cần chọn ngành trước khi gán trình độ đào tạo.', 'error')
    return
  }

  try {
    await services.nganhTrinhDoDaoTao.luu({
      nganhId,
      trinhDoId: item.id,
      trangThai: 'dang_su_dung',
      ghiChu: 'Gán trình độ đào tạo vào ngành'
    })

    selectEntity('trinhDoDaoTao', item)
    await taiDuLieuCoSanTatCaBang()
    // datThongBaoBang('trinhDoDaoTao', 'Đã gán trình độ đào tạo vào ngành.', 'success')
    // datThongBaoBang('nganhTrinhDoDaoTao', 'Danh sách trình độ đào tạo theo ngành đã được cập nhật.', 'success')
  } catch (error) {
    const message = layThongBaoLoi(error, 'Không gán được trình độ đào tạo vào ngành.')
    datThongBaoBang('trinhDoDaoTao', message, 'error')
  }
}

async function ganLoaiChuongTrinhVaoNganh(item) {
  if (!item?.id) {
    datThongBaoBang('loaiChuongTrinh', 'Không tìm thấy loại chương trình để gán vào ngành.', 'error')
    return
  }

  const nganhId = selected.nganh?.id || route.params.nganhId || null
  if (!nganhId) {
    datThongBaoBang('loaiChuongTrinh', 'Cần chọn ngành trước khi gán loại chương trình.', 'error')
    return
  }

  try {
    await services.nganhLoaiChuongTrinh.luu({
      nganhId,
      loaiChuongTrinhId: item.id,
      trangThai: 'dang_su_dung',
      ghiChu: 'Gán loại chương trình vào ngành'
    })

    selectEntity('loaiChuongTrinh', item)
    await taiDuLieuCoSanTatCaBang()
    // datThongBaoBang('loaiChuongTrinh', 'Đã gán loại chương trình vào ngành.', 'success')
    // datThongBaoBang('nganhLoaiChuongTrinh', 'Danh sách loại chương trình theo ngành đã được cập nhật.', 'success')
  } catch (error) {
    const message = layThongBaoLoi(error, 'Không gán được loại chương trình vào ngành.')
    datThongBaoBang('loaiChuongTrinh', message, 'error')
  }
}


function layDuLieuPhanHoiLuu(result) {
  return result?.data?.data || result?.data || result || null
}

function taoPayloadSyllabusMonHocTumau(item, chuongTrinhMonId) {
  return {
    chuongTrinhMonId,
    syllabusMonHocMauId: item.id,
    monHocId: item.monHocId || selected.monHoc?.id || selected.chuongTrinhMon?.monHocId || null,
    ma: item.ma || '',
    ten: item.ten || '',
    viTri: item.viTri || '',
    tinhChat: item.tinhChat || '',
    soTinChi: item.soTinChi ?? null,
    soBuoiHoc: item.soBuoiHoc ?? null,
    soTietMoiBuoi: item.soTietMoiBuoi ?? null,
    soPhutMotTiet: item.soPhutMotTiet ?? null,
    mucTieu: item.mucTieu || '',
    phuongPhapDanhGia: item.phuongPhapDanhGia || '',
    dieuKienHoanThanh: item.dieuKienHoanThanh || '',
    huongDan: item.huongDan || '',
    diemDatToiThieu: item.diemDatToiThieu ?? null,
    donViDiem: item.donViDiem || 'thang_10',
    tyLeChuyenCanToiThieu: item.tyLeChuyenCanToiThieu ?? null,
    batBuocDuThi: item.batBuocDuThi ?? false,
    congThucQuyDoi: item.congThucQuyDoi || '',
    ghiChu: item.ghiChu || ''
  }
}


function taoMaChuongTuDong(row, index) {
  const thuTu = row?.thuTu ?? index + 1
  const soThuTu = String(thuTu || index + 1).replace(/[^0-9A-Za-z_-]/g, '') || String(index + 1)
  return `CH-${soThuTu}`
}

function layTenDieuKienTumau(dieuKienmau) {
  const ma = String(dieuKienmau?.ma || '').trim()
  const noiDung = String(dieuKienmau?.noiDung || '').trim()
  if (ma && noiDung) return `${ma} - ${noiDung}`.slice(0, 250)
  return (noiDung || ma || 'Điều kiện mẫu').slice(0, 250)
}

function layMangResponse(res) {
  if (Array.isArray(res)) return res

  const payload = res?.data

  if (Array.isArray(payload)) return payload
  if (Array.isArray(payload?.data)) return payload.data
  if (Array.isArray(payload?.content)) return payload.content
  if (Array.isArray(payload?.items)) return payload.items
  if (Array.isArray(payload?.result)) return payload.result
  if (Array.isArray(payload?.data?.content)) return payload.data.content
  if (Array.isArray(payload?.data?.items)) return payload.data.items

  return []
}

async function copyChiTietSyllabusmauSangSyllabusMon({syllabusMonId, syllabusMonHocMauId, force = false}) {
  if (!syllabusMonId || !syllabusMonHocMauId) return

  const serviceChuongBai = services.syllabusChuongBai
  const serviceDieuKien = services.dieuKienMonHoc
  const serviceTaiLieu = services.syllabusTaiLieu
  const serviceCauHinhDanhGia = services.cauHinhDanhGia
  const serviceQuyDoiDiem = services.quyDoiDiem

  const chuongBaimau = (duLieu.value.syllabusMonHocmauChuongBai || [])
      .filter((row) => String(row.syllabusMonHocMauId || '') === String(syllabusMonHocMauId || ''))
      .sort((a, b) => Number(a.thuTu ?? 999999) - Number(b.thuTu ?? 999999))

  const ganDieuKienmau = (duLieu.value.syllabusMonHocmauDieuKien || [])
      .filter((row) => String(row.syllabusMonHocMauId || '') === String(syllabusMonHocMauId || ''))
      .sort((a, b) => Number(a.thuTu ?? 999999) - Number(b.thuTu ?? 999999))

  const ganTaiLieumau = (duLieu.value.syllabusMonHocmauTaiLieu || [])
      .filter((row) => String(row.syllabusMonHocMauId || '') === String(syllabusMonHocMauId || ''))
      .sort((a, b) => Number(a.thuTu ?? 999999) - Number(b.thuTu ?? 999999))

  const cauHinhDanhGiaMauRows = (duLieu.value.cauHinhDanhGiaMau || [])
      .filter((row) => String(row.syllabusMonHocMauId || '') === String(syllabusMonHocMauId || ''))
      .sort((a, b) => Number(a.thuTu ?? 999999) - Number(b.thuTu ?? 999999))

  const quyDoiDiemMauRows = (duLieu.value.quyDoiDiemMau || [])
      .filter((row) => String(row.syllabusMonHocMauId || '') === String(syllabusMonHocMauId || ''))
      .sort((a, b) => Number(a.thuTu ?? 999999) - Number(b.thuTu ?? 999999))

  const chuongBaiDaCo = (duLieu.value.syllabusChuongBai || [])
      .filter((row) => String(row.syllabusMonId || '') === String(syllabusMonId || ''))

  const dieuKienDaCo = (duLieu.value.dieuKienMonHoc || [])
      .filter((row) => String(row.syllabusMonId || '') === String(syllabusMonId || ''))

  const taiLieuDaCo = (duLieu.value.syllabusTaiLieu || [])
      .filter((row) => String(row.syllabusMonId || '') === String(syllabusMonId || ''))

  const cauHinhDanhGiaDaCo = (duLieu.value.cauHinhDanhGia || [])
      .filter((row) => String(row.syllabusMonHocId || '') === String(syllabusMonId || ''))

  const quyDoiDiemDaCo = (duLieu.value.quyDoiDiem || [])
      .filter((row) => String(row.syllabusMonHocId || '') === String(syllabusMonId || ''))

  let soChuongBai = 0
  let soDieuKien = 0
  let soTaiLieu = 0
  let soCauHinhDanhGia = 0
  let soQuyDoiDiem = 0

  if (serviceChuongBai?.create && chuongBaimau.length && (force || !chuongBaiDaCo.length)) {
    for (const [index, row] of chuongBaimau.entries()) {
      await serviceChuongBai.create({
        syllabusMonId,
        maChuong: row.maChuong || taoMaChuongTuDong(row, index),
        ten: row.ten || `Chương/bài ${index + 1}`,
        mucTieu: row.mucTieu || null,
        noiDung: row.noiDung || null,
        tongGio: row.tongGio ?? null,
        gioLyThuyet: row.gioLyThuyet ?? null,
        gioThucHanh: row.gioThucHanh ?? null,
        gioKiemTra: row.gioKiemTra ?? null,
        thuTu: row.thuTu ?? index + 1,
        ghiChu: row.ghiChu || null
      })
      soChuongBai += 1
    }
  }

  if (serviceDieuKien?.create && ganDieuKienmau.length && (force || !dieuKienDaCo.length)) {
    for (const [index, gan] of ganDieuKienmau.entries()) {
      const dieuKienmau = (duLieu.value.dieuKienMonHocmau || [])
          .find((row) => String(row.id || '') === String(gan.dieuKienmauId || ''))

      if (!dieuKienmau) continue

      await serviceDieuKien.create({
        syllabusMonId,
        loai: dieuKienmau.loai || 'khac',
        ten: layTenDieuKienTumau(dieuKienmau),
        noiDung: dieuKienmau.noiDung || layTenDieuKienTumau(dieuKienmau),
        soLuong: dieuKienmau.soLuong ?? null,
        yeuCau: dieuKienmau.yeuCau || null,
        thuTu: gan.thuTu ?? index + 1,
        ghiChu: [gan.ghiChu, dieuKienmau.ghiChu].filter(Boolean).join(' | ') || null
      })
      soDieuKien += 1
    }
  }

  if (serviceTaiLieu?.create && ganTaiLieumau.length && (force || !taiLieuDaCo.length)) {
    for (const [index, gan] of ganTaiLieumau.entries()) {
      const taiLieumau = (duLieu.value.taiLieumau || [])
          .find((row) => String(row.id || '') === String(gan.taiLieumauId || ''))

      if (!taiLieumau) continue

      await serviceTaiLieu.create({
        syllabusMonId,
        ma: taiLieumau.ma || `TL-${syllabusMonId}-${index + 1}`,
        ten: taiLieumau.ten || `Tài liệu ${index + 1}`,
        loai: taiLieumau.loai || null,
        tacGia: taiLieumau.tacGia || null,
        nhaXuatBan: taiLieumau.nhaXuatBan || null,
        namXuatBan: taiLieumau.namXuatBan ?? null,
        duongDan: taiLieumau.duongDan || null,
        batBuoc: Boolean(gan.batBuoc),
        thuTu: gan.thuTu ?? index + 1,
        ghiChu: [gan.ghiChu, taiLieumau.ghiChu].filter(Boolean).join(' | ') || null
      })
      soTaiLieu += 1
    }
  }

  if (serviceCauHinhDanhGia?.create && cauHinhDanhGiaMauRows.length && (force || !cauHinhDanhGiaDaCo.length)) {
    for (const row of cauHinhDanhGiaMauRows) {
      await serviceCauHinhDanhGia.create(taoPayloadCotDiemSyllabusTuMau(row, syllabusMonId))
      soCauHinhDanhGia += 1
    }
  }

  if (serviceQuyDoiDiem?.create && quyDoiDiemMauRows.length && (force || !quyDoiDiemDaCo.length)) {
    for (const [index, row] of quyDoiDiemMauRows.entries()) {
      await serviceQuyDoiDiem.create({
        syllabusMonHocId: syllabusMonId,
        ma: row.ma || null,
        ten: row.ten || null,
        nguongTu: row.nguongTu ?? null,
        nguongDen: row.nguongDen ?? null,
        diemQuyDoi: row.diemQuyDoi ?? null,
        ketQua: row.ketQua || null,
        congThuc: row.congThuc || null,
        loaiMau: row.loaiMau || null,
        tyLe: row.tyLe ?? null,
        diemToiDa: row.diemToiDa ?? null,
        thuTu: row.thuTu ?? index + 1,
        batBuoc: row.batBuoc ?? false,
        ghiChu: row.ghiChu || null
      })
      soQuyDoiDiem += 1
    }
  }

  // Copy file từ syllabusMonHocMauFile → syllabusMonHocFile
  let soFile = 0
  const loiCopyFile = []

  try {
    const resMauFiles = await services.syllabusMonHocMauFile.list(syllabusMonHocMauId)
    const mauFiles = layMangResponse(resMauFiles)

    console.log('[COPY FILE SYLLABUS MẪU] syllabusMonHocMauId =', syllabusMonHocMauId)
    console.log('[COPY FILE SYLLABUS MẪU] raw response =', resMauFiles?.data)
    console.log('[COPY FILE SYLLABUS MẪU] parsed mauFiles =', mauFiles)

    if (!mauFiles.length) {
      datThongBaoBang(
        'syllabusMonHoc',
        `Syllabus mẫu ${syllabusMonHocMauId} không có file mẫu hoặc FE parse response file mẫu bị rỗng.`,
        'warning'
      )
    }

    for (const f of mauFiles) {
      if (!f?.id) {
        console.warn('[COPY FILE SYLLABUS MẪU] Bỏ qua file không có id:', f)
        continue
      }
      try {
        console.log('[COPY FILE SYLLABUS MẪU] Copy file:', f.id, f.tenFile)
        await services.syllabusMonHocFile.copyFromMau(syllabusMonId, f.id)
        soFile++
      } catch (error) {
        console.error('[COPY FILE SYLLABUS MẪU LỖI]', f, error)
        loiCopyFile.push(`${f.tenFile || f.id}: ${layThongBaoLoi(error)}`)
      }
    }
  } catch (error) {
    console.error('[COPY FILE SYLLABUS MẪU] Lỗi load danh sách file mẫu:', error)
    loiCopyFile.push(layThongBaoLoi(error))
  }

  if (loiCopyFile.length) {
    datThongBaoBang('syllabusMonHoc', `Có ${loiCopyFile.length} file syllabus mẫu copy lỗi: ${loiCopyFile.join('; ')}`, 'error')
  }

  if (soChuongBai || soDieuKien || soTaiLieu || soCauHinhDanhGia || soQuyDoiDiem || soFile) {
    datThongBaoBang(
        'syllabusMonHoc',
        `Đã copy từ syllabus mẫu: ${soChuongBai} chương/bài, ${soTaiLieu} tài liệu, ${soDieuKien} điều kiện, ${soCauHinhDanhGia} cấu hình đánh giá, ${soQuyDoiDiem} quy đổi điểm, ${soFile} file syllabus.`,
        'success'
    )
  } else {
    datThongBaoBang(
        'syllabusMonHoc',
        'Đã gán syllabus. Chi tiết không copy thêm vì bảng áp dụng đã có dữ liệu hoặc syllabus mẫu chưa có chi tiết.',
        'success'
    )
  }
}


function layTenCotDiemTuMau(item = {}) {
  return item.tenCotDiem || item.ten || item.tenMau || item.ma || item.ghiChu || ''
}

function layLoaiDiemTuMau(item = {}) {
  const raw = item.loaiDiem || item.loaiMau || item.loai_mau || 'khac'
  const loai = String(raw || '').trim()
  if (!loai || loai.toUpperCase() === 'COT_DIEM') return 'khac'
  return loai
}

function taoPayloadCotDiemSyllabusTuMau(item = {}, syllabusMonHocId) {
  return {
    syllabusMonHocId,
    tenCotDiem: layTenCotDiemTuMau(item),
    loaiDiem: layLoaiDiemTuMau(item),
    tyLe: item.tyLe ?? null,
    diemToiDa: item.diemToiDa ?? 10,
    thuTu: item.thuTu ?? null,

    // Copy từ cau_hinh_danh_gia_mau sang cau_hinh_danh_gia
    batBuoc: item.batBuoc ?? true,
    ghiChu: item.ghiChu ?? item.tenCotDiem ?? item.ten ?? item.ma ?? null
  }
}

function khopCotDiemSyllabusVoiMau(row = {}, item = {}, syllabusMonHocId) {
  const payload = taoPayloadCotDiemSyllabusTuMau(item, syllabusMonHocId)
  const tenRow = String(row.tenCotDiem || '').trim()
  const tenPayload = String(payload.tenCotDiem || '').trim()
  const ghiChuRow = String(row.ghiChu || '').trim()
  const ghiChuPayload = String(payload.ghiChu || '').trim()

  return String(row.syllabusMonHocId || '') === String(syllabusMonHocId || '')
      && (
        tenRow === tenPayload
        || (ghiChuPayload && ghiChuRow === ghiChuPayload)
        || (item.ma && ghiChuRow === String(item.ma).trim())
      )
      && String(row.loaiDiem || 'khac') === String(payload.loaiDiem || 'khac')
      && String(row.tyLe ?? '') === String(payload.tyLe ?? '')
      && String(row.diemToiDa ?? '') === String(payload.diemToiDa ?? '')
}

function timCotDiemSyllabusTuMau(item = {}, syllabusMonHocId) {
  return (duLieu.value.cauHinhDanhGia || []).find((row) => khopCotDiemSyllabusVoiMau(row, item, syllabusMonHocId)) || null
}

function layIdsCotDiemMauDaGanSyllabus(rows = [], syllabusMonHocId) {
  if (!syllabusMonHocId) return []
  return (rows || [])
      .filter((row) => timCotDiemSyllabusTuMau(row, syllabusMonHocId))
      .map((row) => row.id)
      .filter((id) => id !== null && id !== undefined && id !== '')
}

const soCotDiemMauChuaGan = computed(() => {
  const syllabusMonHocId = syllabusMonHocScopeParent.value.syllabusMonHocId
  if (!syllabusMonHocId) return 0

  return (cotDiemSyllabusmauRows.value || [])
      .filter((row) => !timCotDiemSyllabusTuMau(row, syllabusMonHocId))
      .length
})

const coTheGanTatCaCotDiemMau = computed(() => {
  return Boolean(syllabusMonHocScopeParent.value.syllabusMonHocId) && soCotDiemMauChuaGan.value > 0
})

const cotDiemMauDaTichChonRows = computed(() => {
  const selectedIds = new Set(cotDiemMauDaTichChonIds.value.map((id) => String(id)))
  if (!selectedIds.size) return []

  return (cotDiemSyllabusmauRows.value || []).filter((row) => selectedIds.has(String(row.id)))
})

const coTheGanCotDiemMauDaTichChon = computed(() => {
  const syllabusMonHocId = syllabusMonHocScopeParent.value.syllabusMonHocId
  if (!syllabusMonHocId) return false

  return cotDiemMauDaTichChonRows.value.some((row) => !timCotDiemSyllabusTuMau(row, syllabusMonHocId))
})

async function taiLaiDuLieuTangSyllabusDangChon() {
  if (selected.syllabusMonHoc?.id) {
    await taiDuLieuTheoSyllabusMonHocApDung(selected.syllabusMonHoc)
    return
  }

  await taiDuLieuCoSanTatCaBang()
}

async function ganDanhSachCotDiemMauVaoSyllabus(rows = [], successMessage = 'Đã gán cột điểm mẫu vào syllabus.') {
  if (dangGanCotDiemMau.value) return

  const syllabusMonHocId = syllabusMonHocScopeParent.value.syllabusMonHocId

  if (!syllabusMonHocId) {
    baoTin('Cần chọn Syllabus môn học trước khi gán Cột điểm mẫu.', 'error')
    return
  }

  const rowsCanCopy = (rows || []).filter((row) => !timCotDiemSyllabusTuMau(row, syllabusMonHocId))

  if (!rowsCanCopy.length) {
    datThongBaoBang('cauHinhDanhGia', 'Các cột điểm mẫu đã chọn đều đã được gán hoặc không còn dữ liệu cần gán.', 'success')
    return
  }

  const service = services.cauHinhDanhGia
  if (!service?.create) {
    baoTin('Chưa khai báo API tạo Cột điểm của syllabus.', 'error')
    return
  }

  dangGanCotDiemMau.value = true
  try {
    for (const item of rowsCanCopy) {
      await service.create(taoPayloadCotDiemSyllabusTuMau(item, syllabusMonHocId))
    }

    await taiLaiDuLieuTangSyllabusDangChon()
    cotDiemMauDaTichChonIds.value = cotDiemMauDaTichChonIds.value.filter((id) => {
      return !(rowsCanCopy || []).some((row) => String(row.id) === String(id))
    })
    datThongBaoBang('cauHinhDanhGia', successMessage.replace('{count}', rowsCanCopy.length), 'success')
  } catch (error) {
    const message = layThongBaoLoi(error, 'Không gán được Cột điểm mẫu vào syllabus.')
    datThongBaoBang('cauHinhDanhGia', message, 'error')
  } finally {
    dangGanCotDiemMau.value = false
  }
}

async function ganCotDiemMauDaTichChonVaoSyllabus() {
  await ganDanhSachCotDiemMauVaoSyllabus(
      cotDiemMauDaTichChonRows.value,
      'Đã gán {count} cột điểm mẫu đã tích chọn vào syllabus.'
  )
}

async function ganTatCaCotDiemMauVaoSyllabus() {
  const syllabusMonHocId = syllabusMonHocScopeParent.value.syllabusMonHocId

  if (!syllabusMonHocId) {
    baoTin('Cần chọn Syllabus môn học trước khi gán toàn bộ Cột điểm mẫu.', 'error')
    return
  }

  const rowsCanCopy = (cotDiemSyllabusmauRows.value || [])
      .filter((row) => !timCotDiemSyllabusTuMau(row, syllabusMonHocId))

  if (!rowsCanCopy.length) {
    datThongBaoBang('cauHinhDanhGia', 'Không còn cột điểm mẫu nào cần gán.', 'success')
    return
  }

  await ganDanhSachCotDiemMauVaoSyllabus(
      rowsCanCopy,
      'Đã gán {count} cột điểm mẫu vào syllabus.'
  )
}

async function ganCotDiemMauVaoSyllabus(item) {
  if (dangGanCotDiemMau.value) return

  const syllabusMonHocId = syllabusMonHocScopeParent.value.syllabusMonHocId

  if (!syllabusMonHocId) {
    baoTin('Cần chọn Syllabus môn học trước khi gán Cột điểm mẫu.', 'error')
    return
  }

  const service = services.cauHinhDanhGia
  if (!service?.create) {
    baoTin('Chưa khai báo API tạo Cột điểm của syllabus.', 'error')
    return
  }

  dangGanCotDiemMau.value = true
  try {
    const existing = timCotDiemSyllabusTuMau(item, syllabusMonHocId)
    if (existing?.id) {
      selectEntity('cauHinhDanhGiaMau', null)
      selectEntity('cauHinhDanhGia', existing)
      datThongBaoBang('cauHinhDanhGia', 'Đã bỏ chọn dòng mẫu. Cột điểm đã gán vào syllabus vẫn được giữ nguyên.', 'success')
      return
    }

    const payload = taoPayloadCotDiemSyllabusTuMau(item, syllabusMonHocId)
    await service.create(payload)
    await taiLaiDuLieuTangSyllabusDangChon()
    datThongBaoBang('cauHinhDanhGia', 'Đã gán cột điểm mẫu vào Cột điểm của syllabus.', 'success')
  } catch (error) {
    const message = layThongBaoLoi(error, 'Không gán được Cột điểm mẫu vào syllabus.')
    datThongBaoBang('cauHinhDanhGia', message, 'error')
  } finally {
    dangGanCotDiemMau.value = false
  }
}

async function ganSyllabusmauVaoSyllabusMonHocApDung(item, {boGanNeuDaGan = false} = {}) {
  if (!item?.id) {
    datThongBaoBang('syllabusMonHocmau', 'Không tìm thấy syllabus mẫu để gán.', 'error')
    return
  }

  const chuongTrinhMonId = selected.chuongTrinhMon?.id || route.query.chuongTrinhMonId || route.params.chuongTrinhMonId || null
  if (!chuongTrinhMonId) {
    datThongBaoBang('syllabusMonHocmau', 'Cần chọn Môn trong chương trình ở tầng 7 trước khi gán syllabus mẫu.', 'error')
    return
  }

  const service = services.syllabusMonHoc
  if (!service?.create) {
    datThongBaoBang('syllabusMonHocmau', 'Chưa khai báo API tạo syllabus_mon_hoc.', 'error')
    return
  }

  const existingCungmau = (duLieu.value.syllabusMonHoc || []).find((row) => {
    return String(row.chuongTrinhMonId || '') === String(chuongTrinhMonId || '')
        && String(row.syllabusMonHocMauId || '') === String(item.id || '')
  })

  if (existingCungmau?.id) {
    if (boGanNeuDaGan) {
      selectEntity('syllabusMonHocmau', null)
      selectEntity('syllabusMonHoc', existingCungmau)

      // Copy file mẫu sang áp dụng nếu chưa có (force=false để không ghi đè)
      try {
        await copyChiTietSyllabusmauSangSyllabusMon({
          syllabusMonId: existingCungmau.id,
          syllabusMonHocMauId: item.id,
          force: false
        })
        await taiDuLieuTheoSyllabusMonHocApDung(existingCungmau)
      } catch (error) {
        console.warn('[boGanNeuDaGan copy file]', error)
      }

      datThongBaoBang(
        'syllabusMonHocmau',
        'Đã bỏ chọn dòng mẫu. Syllabus đã gán vào môn trong chương trình vẫn được giữ nguyên.',
        'success'
      )

      datThongBaoBang(
        'syllabusMonHoc',
        'Dữ liệu syllabus đã gán không bị xóa.',
        'success'
      )

      return
    }

    selectEntity('syllabusMonHocmau', item)
    selectEntity('syllabusMonHoc', existingCungmau)
    try {
      await copyChiTietSyllabusmauSangSyllabusMon({
        syllabusMonId: existingCungmau.id,
        syllabusMonHocMauId: item.id,
        force: false
      })
    } catch (error) {
      datThongBaoBang('syllabusMonHoc', layThongBaoLoi(error, 'Syllabus đã gán, nhưng không copy bổ sung được chi tiết từ syllabus mẫu.'), 'error')
    }
    datThongBaoBang('syllabusMonHocmau', 'Syllabus mẫu này đã được gán vào môn trong chương trình. Đã chọn dòng áp dụng bên dưới.', 'success')
    return
  }

  const existingKhacmau = (duLieu.value.syllabusMonHoc || []).find((row) => {
    return String(row.chuongTrinhMonId || '') === String(chuongTrinhMonId || '')
  })

  const payload = taoPayloadSyllabusMonHocTumau(item, chuongTrinhMonId)

  try {
    let savedId = null

    if (existingKhacmau?.id && service.update) {
      const saved = await service.update(existingKhacmau.id, payload)
      const row = layDuLieuPhanHoiLuu(saved)
      savedId = row?.id || existingKhacmau.id
      datThongBaoBang('syllabusMonHocmau', 'Đã thay syllabus mẫu cũ bằng syllabus mẫu đang chọn cho môn trong chương trình.', 'success')
    } else if (existingKhacmau?.id && service.delete) {
      await service.delete(existingKhacmau.id)
      const saved = await service.create(payload)
      const row = layDuLieuPhanHoiLuu(saved)
      savedId = row?.id || null
      datThongBaoBang('syllabusMonHocmau', 'Đã thay syllabus mẫu cũ bằng syllabus mẫu đang chọn cho môn trong chương trình.', 'success')
    } else {
      const saved = await service.create(payload)
      const row = layDuLieuPhanHoiLuu(saved)
      savedId = row?.id || null
      datThongBaoBang('syllabusMonHocmau', 'Đã gán syllabus mẫu vào bảng Syllabus đã gán vào môn trong chương trình.', 'success')
    }

    selectEntity('syllabusMonHocmau', item)

    if (savedId) {
      await copyChiTietSyllabusmauSangSyllabusMon({
        syllabusMonId: savedId,
        syllabusMonHocMauId: item.id,
        force: !existingKhacmau?.id
      })
    }

    await taiDuLieuCoSanTatCaBang()

    const rowDaGan = (duLieu.value.syllabusMonHoc || []).find((row) => {
      if (savedId && String(row.id || '') === String(savedId || '')) return true
      return String(row.chuongTrinhMonId || '') === String(chuongTrinhMonId || '')
          && String(row.syllabusMonHocMauId || '') === String(item.id || '')
    })

    if (rowDaGan) {
      selectEntity('syllabusMonHoc', rowDaGan)
      datThongBaoBang('syllabusMonHoc', 'Bảng Syllabus đã gán vào môn trong chương trình đã được cập nhật. Bấm Xem chi tiết syllabus để sang tầng chi tiết.', 'success')
    }
  } catch (error) {
    const message = layThongBaoLoi(error, 'Không gán được syllabus mẫu vào môn trong chương trình.')
    datThongBaoBang('syllabusMonHocmau', message, 'error')
  }
}


async function ganSyllabusChuongTrinhMauVaoVersion(item) {
  const chuongTrinhVersionId = selected.chuongTrinhVersion?.id || route.params.versionId || null
  if (!chuongTrinhVersionId) {
    datThongBaoBang('syllabusChuongTrinhmau', 'Cần chọn Version trước khi gán syllabus chương trình mẫu.', 'error')
    return
  }

  if (!item?.id) {
    datThongBaoBang('syllabusChuongTrinhmau', 'Không tìm thấy syllabus chương trình mẫu để gán.', 'error')
    return
  }

  try {
    const existing = syllabusChuongTrinhApDungTheoVersion.value
    const existingMauId = existing?.syllabusChuongTrinhmauId || existing?.syllabusChuongTrinhMauId || null

    if (existing?.id && String(existingMauId || '') === String(item.id || '')) {
      selectEntity('syllabusChuongTrinhmau', null)
      selectEntity('syllabusChuongTrinh', existing)

      datThongBaoBang(
        'syllabusChuongTrinhmau',
        'Đã bỏ chọn dòng mẫu. Syllabus chương trình đã lưu vào Version vẫn được giữ nguyên.',
        'success'
      )

      datThongBaoBang(
        'syllabusChuongTrinh',
        'Dữ liệu Syllabus chương trình đã lưu vào Version không bị xóa.',
        'success'
      )

      return
    }

    await services.syllabusChuongTrinh.dongBoTuMau({
      chuongTrinhVersionId,
      syllabusChuongTrinhMauId: item.id
    })

    selectEntity('syllabusChuongTrinhmau', item)
    await taiDuLieuCoSanTatCaBang()
    datThongBaoBang('syllabusChuongTrinhmau', 'Đã copy syllabus chương trình mẫu và các bảng con vào Version.', 'success')
  } catch (error) {
    datThongBaoBang('syllabusChuongTrinhmau', layThongBaoLoi(error, 'Không gán được syllabus chương trình mẫu vào Version.'), 'error')
  }
}

async function ganBangConSyllabusMauVaoVersion(mauKey, apDungKey, item) {
  const syllabusChuongTrinhId = syllabusChuongTrinhIdDangGan.value
  if (!syllabusChuongTrinhId) {
    datThongBaoBang(mauKey, 'Version này chưa có Syllabus chương trình đã lưu. Cần gán Syllabus chương trình mẫu trước.', 'error')
    return
  }

  if (!item?.id) {
    datThongBaoBang(mauKey, 'Không tìm thấy dòng mẫu để gán.', 'error')
    return
  }

  try {
    const existing = timDongDaCopyTheoMa(apDungKey, item)
    if (existing?.id) {
      selectEntity(mauKey, null)
      selectEntity(apDungKey, existing)

      datThongBaoBang(
        mauKey,
        'Đã bỏ chọn dòng mẫu. Dữ liệu đã lưu vào Version vẫn được giữ nguyên.',
        'success'
      )

      datThongBaoBang(
        apDungKey,
        'Dữ liệu đã lưu vào Version không bị xóa.',
        'success'
      )

      return
    }

    await services[apDungKey].create(buildPayloadApDungTuMau(apDungKey, item))
    await taiDuLieuCoSanTatCaBang()
    datThongBaoBang(mauKey, 'Đã copy dòng mẫu vào Syllabus chương trình của Version.', 'success')
  } catch (error) {
    datThongBaoBang(mauKey, layThongBaoLoi(error, 'Không copy được dòng mẫu vào Version.'), 'error')
  }
}

function luuDongBang(bang, item) {
  const parentValues = bang.linkParentValues || bang.parentValues || {}

  if (bang.key === 'syllabusMonHocmau') {
    ganSyllabusmauVaoSyllabusMonHocApDung(item, {boGanNeuDaGan: true})
    return
  }

  if (bang.key === 'syllabusChuongTrinhmau') {
    ganSyllabusChuongTrinhMauVaoVersion(item)
    return
  }

  if (bang.key === 'mucTieuChuongTrinhmau') {
    ganBangConSyllabusMauVaoVersion('mucTieuChuongTrinhmau', 'mucTieuChuongTrinh', item)
    return
  }

  if (bang.key === 'nangLucDauRamau') {
    ganBangConSyllabusMauVaoVersion('nangLucDauRamau', 'nangLucDauRa', item)
    return
  }

  if (bang.key === 'viTriViecLammau') {
    ganBangConSyllabusMauVaoVersion('viTriViecLammau', 'viTriViecLam', item)
    return
  }

  if (bang.key === 'dieuKienTotNghiepmau') {
    ganBangConSyllabusMauVaoVersion('dieuKienTotNghiepmau', 'dieuKienTotNghiep', item)
    return
  }

  if (bang.key === 'trinhDoDaoTao') {
    ganTrinhDoVaoNganh(item)
    return
  }

  if (bang.key === 'loaiChuongTrinh') {
    ganLoaiChuongTrinhVaoNganh(item)
    return
  }

  if (typeof bang.toggleHandler === 'function') {
    bang.toggleHandler(item)
    return
  }

  if (bang.key === 'quyDoiDiemMau') {
    if (!parentValues.syllabusMonHocId) {
      baoTin('Cần chọn Syllabus môn học trước khi gán dữ liệu mẫu.', 'error')
      return
    }

    if (!laMauQuyDoiKetQua(item)) {
      baoTin('Dữ liệu mẫu không hợp lệ.', 'error')
      return
    }
  }

  toggleLuuBangPhu(bang.key, item, parentValues, bang.service)
}

const bangPhuHienDuLieuDayDu = new Set([
  'mucTieuChuongTrinhmau',
  'nangLucDauRamau',
  'viTriViecLammau',
  'dieuKienTotNghiepmau',
  'syllabusChuongTrinhmau',
  'khungKymau',
  'nhomKienThucmau',
  'nhomTuChonmau',
  'syllabusChuongTrinh',
  'monTienQuyet',
  'quyDoiDiemMau',
  'dieuKienMonHocmau',
  'taiLieumau',
  'dieuKienMonHoc',
  'syllabusChuongBai',
  'syllabusTaiLieu'
])

function sapXepBangTheoThuTu(key, rows = []) {
  if (!['cauHinhDanhGiaMau', 'cauHinhDanhGia', 'quyDoiDiem'].includes(key)) return rows

  return [...rows].sort((a, b) => {
    const thuTuA = Number(a?.thuTu ?? 999999)
    const thuTuB = Number(b?.thuTu ?? 999999)

    if (thuTuA !== thuTuB) return thuTuA - thuTuB

    const tenA = String(a?.tenCotDiem || a?.tenCotDiemMau || a?.ghiChu || '')
    const tenB = String(b?.tenCotDiem || b?.tenCotDiemMau || b?.ghiChu || '')

    return tenA.localeCompare(tenB, 'vi')
  })
}

function layThongBaoTyLeCotDiemSyllabus(rows = []) {
  // Validator riêng cho tầng 9 - bảng 2. Cột điểm của syllabus.
  // Chỉ hiện cảnh báo khi đã chọn syllabus và tổng tỷ lệ chưa đúng 100%.
  if (!selected.syllabusMonHoc?.id) return null

  const tenDaCo = new Set()
  const tenTrung = new Set()

  rows.forEach((row) => {
    const ten = String(row.tenCotDiem || row.tenCotDiemMau || row.ten || row.ghiChu || '').trim()
    const tenKey = ten.toLowerCase()
    if (!tenKey) return

    if (tenDaCo.has(tenKey)) {
      tenTrung.add(ten)
    }

    tenDaCo.add(tenKey)
  })

  if (tenTrung.size) {
    return {
      type: 'error',
      message: `Bảng 2. Cột điểm của syllabus đang bị trùng tên cột điểm: ${Array.from(tenTrung).join(', ')}.`
    }
  }

  const tongTyLe = rows.reduce((total, row) => {
    const value = Number(row.tyLe ?? 0)
    return total + (Number.isNaN(value) ? 0 : value)
  }, 0)

  const tongLamTron = Number(tongTyLe.toFixed(2))

  if (!rows.length) {
    return {
      type: 'error',
      message: 'Bảng 2. Cột điểm của syllabus chưa có cột điểm nào. Tổng Tỷ lệ % hiện là 0%, phải đủ 100%.'
    }
  }

  if (tongLamTron < 100) {
    return {
      type: 'error',
      message: `Bảng 2. Cột điểm của syllabus: Tổng Tỷ lệ % hiện là ${tongLamTron}%. Phải đúng 100%, còn thiếu ${Number((100 - tongLamTron).toFixed(2))}%.`
    }
  }

  if (tongLamTron > 100) {
    return {
      type: 'error',
      message: `Bảng 2. Cột điểm của syllabus: Tổng Tỷ lệ % hiện là ${tongLamTron}%. Phải đúng 100%, đang vượt ${Number((tongLamTron - 100).toFixed(2))}%.`
    }
  }

  return null
}

async function boMonKhoiKy(item) {
  if (!item?.id) {
    baoTin('Không tìm thấy môn trong chương trình để bỏ khỏi kỳ.', 'error')
    return
  }

  const dongY = confirm('Chỉ bỏ môn khỏi kỳ hiện tại, môn vẫn còn trong Version. Tiếp tục?')
  if (!dongY) return

  const payload = {
    chuongTrinhVersionId: item.chuongTrinhVersionId,
    monHocId: item.monHocId || item.monId,
    maMonTrongCt: item.maMonTrongCt,
    khungKyId: null,
    nhomKienThucId: item.nhomKienThucId ?? null,
    loai: item.loai || 'bat_buoc',
    loaiHocPhan: item.loaiHocPhan || 'mon_hoc',
    loaiPhamVi: item.loaiPhamVi || 'mon_chuyen_nganh',
    batBuoc: item.batBuoc !== false,
    laMonDieuKien: item.laMonDieuKien === true,
    thuTu: item.thuTu ?? null,
    ghiChu: item.ghiChu || ''
  }

  try {
    const saved = await services.chuongTrinhMon.update(item.id, payload)
    sauKhiLuu('chuongTrinhMon', saved)
    selectEntity('chuongTrinhMon', null)
    baoTin('Đã bỏ môn khỏi kỳ hiện tại. Môn vẫn còn trong Version/chương trình.', 'success')
  } catch (error) {
    baoTin(layThongBaoLoi(error, 'Không bỏ môn khỏi kỳ được.'), 'error')
  }
}

function taoBang(key, options = {}) {
  const parentValues = options.parentValues || {}
  const linkParentValues = options.linkParentValues || parentValues
  const filterValues = options.filterValues || parentValues
  const allRows = key === 'quyDoiDiemMau'
      ? (quyDoiDiemMaumauRows.value || [])
      : (duLieu.value[key] || [])

  const loaiBang = bangmauMauKeys.has(key) ? 'mau-mau' : (bangXuongSongKeys.has(key) ? 'xuong-song' : 'phu')
  const hienDayDuBangPhu = loaiBang === 'phu' && bangPhuHienDuLieuDayDu.has(key) && !options.forceFilter
  const hienDayDuBangmauMau = loaiBang === 'mau-mau'
  const khongLocTheoCha = bangDocLapKhongLocTheoCha.has(key)

  const rowsChuaSapXep = khongLocTheoCha || hienDayDuBangmauMau || hienDayDuBangPhu ? allRows : locDongTheoCha(allRows, filterValues)
  const rowsDaSapXep = sapXepBangTheoThuTu(key, rowsChuaSapXep)
  const rowsNguon = Array.isArray(options.displayRows) ? options.displayRows : rowsDaSapXep
  const rows = rowsNguon.map((row) => boSungNhanHienThiBang(key, row))
  const coNutLuuBoLuu = options.canToggleSave === undefined ? bangCoNutLuuBoLuuMacDinh.has(key) : Boolean(options.canToggleSave)
  const coNutChon = options.canSelect === undefined ? !bangKhongCanChon.has(key) && !bangChiLuuGanKhongChon.has(key) : Boolean(options.canSelect)
  const coHienTrangThaiDaLuu = options.canShowSavedStatus === undefined
      ? (coNutLuuBoLuu || bangChiHienTrangThaiDaLuuMacDinh.has(key))
      : Boolean(options.canShowSavedStatus)
  const idsDaLuuTheoNguCanh = Array.isArray(options.savedIds)
      ? options.savedIds
      : (coHienTrangThaiDaLuu ? laySavedIdsTheoBang(key, allRows, rows, linkParentValues, linkParentValues) : [])
  const idsHienThiDaLuu = coHienTrangThaiDaLuu && !coNutLuuBoLuu && idsDaLuuTheoNguCanh.length === 0 && !['trinhDoDaoTao', 'loaiChuongTrinh'].includes(key)
      ? rows.map((row) => row?.id).filter((id) => id !== null && id !== undefined && id !== '')
      : idsDaLuuTheoNguCanh
  const idsLienQuanTheoNguCanh = coNutChon ? laySelectedIdsTheoBang(key, allRows, rows, linkParentValues, linkParentValues) : []
  const tableMessageTuKiemTraTyLe = key === 'cauHinhDanhGia'
      ? layThongBaoTyLeCotDiemSyllabus(rows)
      : null
  return {
    key,
    config: {
      ...configs[key],
      ...(options.fields ? {fields: options.fields} : {}),
      ...(options.columns ? {columns: options.columns} : {}),
      ...(options.defaultForm ? {defaultForm: options.defaultForm} : {}),
      title: options.title || configs[key]?.title,
      description: options.description || configs[key]?.description,
      canView: Boolean(options.canView),
      viewLabel: options.viewLabel || nhanNutXemTheoBang[key] || 'Xem thêm',
      resetAfterSave: options.resetAfterSave !== undefined ? options.resetAfterSave : true,
      uploadFile: options.uploadFile || null
    },
    rows,
    allRows,
    service: services[key],
    selectedId: selected[key]?.id || null,
    viewedId: viewed[key]?.id || null,
    selectedIds: idsLienQuanTheoNguCanh,
    savedIds: idsHienThiDaLuu,
    canToggleSave: coNutLuuBoLuu,
    canSelect: coNutChon,
    canShowSavedStatus: coHienTrangThaiDaLuu,
    multiSelect: Boolean(options.multiSelect),
    multiSelectIds: options.multiSelectIds || [],
    multiSelectLabel: options.multiSelectLabel || 'Tích chọn',
    multiSelectedLabel: options.multiSelectedLabel || 'Bỏ tích',
    toggleHandler: options.toggleHandler || null,
    canView: Boolean(options.canView),
    viewLabel: options.viewLabel || nhanNutXemTheoBang[key] || 'Xem thêm',
    tableMessage: tableMessageTuKiemTraTyLe || tableMessages?.[key] || null,
    parentValues,
    linkParentValues,
    parentText: options.parentText || '',
    readonlyInfoFields: options.readonlyInfoFields || [],
    loaiBang,
    readOnly: Boolean(options.readOnly),
    tableTitle: options.tableTitle || '',
    emptyText: options.emptyText || '',
    toggleSaveLabel: options.toggleSaveLabel || 'Gán vào',
    toggleSavedLabel: options.toggleSavedLabel || 'Đã gán',
    saveStatusText: options.saveStatusText || '',
    statusSavedLabel: options.statusSavedLabel || '✓ Đã gán',
    statusUnsavedLabel: options.statusUnsavedLabel || '+ Chưa gán',
    disabled: Boolean(options.disabled),
    disabledText: options.disabledText || '',
    deleteHandler: options.deleteHandler || null,
    deleteLabel: options.deleteLabel || 'Xóa'
  }
}


const cacTangDaoTao = [
  {so: 1, ten: 'Ngành', route: 'DaoTao.XemChuongTrinh', moTa: 'Chọn ngành để bắt đầu xem/xây dựng chương trình.'},
  {
    so: 2,
    ten: 'Cấu hình chương trình',
    route: 'DaoTao.XemChuongTrinh.CauHinh',
    moTa: 'Chọn trình độ đào tạo và loại chương trình.'
  },
  {
    so: 3,
    ten: 'Chương trình',
    route: 'DaoTao.XemChuongTrinh.ChuongTrinh',
    moTa: 'Lọc chương trình theo ngành, trình độ và loại chương trình.'
  },
  {
    so: 4,
    ten: 'Version chương trình',
    route: 'DaoTao.XemChuongTrinh.Version',
    moTa: 'Chọn phiên bản chương trình để đi vào chi tiết.'
  },
  {
    so: 5,
    ten: 'Thông tin tổng quan version',
    route: 'DaoTao.XemChuongTrinh.TongQuan',
    moTa: 'Syllabus chương trình, mục tiêu, năng lực, vị trí việc làm, điều kiện tốt nghiệp.'
  },
  {so: 6, ten: 'Kỳ học', route: 'DaoTao.XemChuongTrinh.KhungCauTruc', moTa: 'Xem kỳ học của Version trước.'},
  {
    so: 7,
    ten: 'Môn trong kỳ',
    route: 'DaoTao.XemChuongTrinh.Mon',
    moTa: 'Gán môn học trực tiếp vào kỳ qua bảng chương trình môn.'
  },
  {
    so: 8,
    ten: 'Gán syllabus mẫu vào môn',
    route: 'DaoTao.XemChuongTrinh.Syllabusmau',
    moTa: 'Chọn syllabus mẫu/mẫu và gán vào môn trong chương trình.'
  },
  {
    so: 9,
    ten: 'Chi tiết syllabus áp dụng',
    route: 'DaoTao.XemChuongTrinh.SyllabusApDung',
    moTa: 'Quản lý chương bài, điều kiện và tài liệu của syllabus đã áp dụng.'
  }
]

const tangHienTai = computed(() => {
  if (route.name === 'DaoTao.XemChuongTrinh.KhungCauTruc') return 6
  if (route.name === 'DaoTao.XemChuongTrinh.Mon') return 7
  if (route.name === 'DaoTao.XemChuongTrinh.Syllabusmau') return 8
  if (route.name === 'DaoTao.XemChuongTrinh.SyllabusApDung') return 9

  return cacTangDaoTao.find((tang) => tang.route === route.name)?.so || 1
})
const thongTinTangHienTai = computed(() => cacTangDaoTao.find((tang) => tang.so === tangHienTai.value) || cacTangDaoTao[0])

const coTheVeTangTongQuan = computed(() => Boolean(selected.nganh?.id && selected.chuongTrinh?.id && selected.chuongTrinhVersion?.id))
const coTheTiepTucTang2 = computed(() => Boolean(selected.nganh?.id && selected.nganhHeDaoTao?.id))
const coTheTiepTucTang6 = computed(() => coTheVeTangTongQuan.value)
const hienNutTiepTucChung = computed(() => tangHienTai.value === 2 || tangHienTai.value === 6)
const tieuDeNutTiepTuc = computed(() => tangHienTai.value === 2 ? 'Tiếp tục xem Chương trình' : 'Tiếp tục xem Môn trong chương trình')
const coTheBamNutTiepTuc = computed(() => tangHienTai.value === 2 ? coTheTiepTucTang2.value : coTheTiepTucTang6.value)
const moTaNutTiepTuc = computed(() => {
  if (tangHienTai.value === 2) {
    return coTheTiepTucTang2.value
        ? 'Đã chọn ngành hệ đào tạo. Có thể bấm Tiếp tục để chuyển sang tầng Chương trình.'
        : 'Cần bấm Xem chương trình ở một hệ đào tạo trong bảng bên dưới.'
  }

  return coTheTiepTucTang6.value
      ? 'Đã có Version. Bấm để xem các môn trong chương trình của Version đang chọn.'
      : 'Cần chọn Version trước khi đi tiếp.'
})

const tomTatDieuHuong = computed(() => [
  {
    key: 'nganh',
    label: 'Ngành',
    value: selected.nganh?.tenNganh || '',
    tang: 1,
    tenTang: 'Tầng 1 - Ngành',
    enabled: Boolean(selected.nganh?.id) || tangHienTai.value === 1
  },
  {
    key: 'nganhHeDaoTao',
    label: 'Hệ đào tạo',
    value: selected.nganhHeDaoTao?.tenHe || selected.nganhHeDaoTao?.maHe || '',
    tang: 2,
    tenTang: 'Tầng 2 - Cấu hình chương trình',
    enabled: Boolean(selected.nganh?.id)
  },
  {
    key: 'chuongTrinh',
    label: 'CTĐT',
    value: selected.chuongTrinh?.tenChuongTrinh || '',
    tang: 3,
    tenTang: 'Tầng 3 - Chương trình',
    enabled: Boolean(selected.nganh?.id)
  },
  {
    key: 'chuongTrinhVersion',
    label: 'Version',
    value: selected.chuongTrinhVersion?.tenVersion || '',
    tang: 4,
    tenTang: 'Tầng 4 - Version chương trình',
    enabled: Boolean(selected.nganh?.id && selected.chuongTrinh?.id)
  },
  {
    key: 'khungKy',
    label: 'Kỳ',
    value: selected.khungKy?.tenKy || selected.khungKy?.maKy || '',
    tang: 6,
    tenTang: 'Tầng 6 - Kỳ học',
    enabled: coTheVeTangTongQuan.value
  },
  {
    key: 'chuongTrinhMon',
    label: 'Môn trong CT',
    value: selected.chuongTrinhMon?.maMonTrongCt || selected.chuongTrinhMon?.tenMon || '',
    tang: 7,
    tenTang: 'Tầng 7 - Môn trong kỳ',
    enabled: Boolean(coTheVeTangTongQuan.value && selected.khungKy?.id)
  },
  {
    key: 'monHoc',
    label: 'Môn học',
    value: selected.monHoc?.tenMon || selected.monHoc?.maMon || '',
    tang: 7,
    tenTang: 'Tầng 7 - Môn trong kỳ',
    enabled: Boolean(coTheVeTangTongQuan.value && selected.chuongTrinhMon?.id)
  },
  {
    key: 'syllabusMonHoc',
    label: 'Syllabus môn',
    value:
        selected.syllabusMonHoc?.ten ||
        selected.syllabusMonHoc?.tenSyllabusMonHocmau ||
        selected.syllabusMonHoc?.ma ||
        selected.syllabusMonHoc?.maSyllabusMonHocmau ||
        '',
    tang: 8,
    tenTang: 'Tầng 8 - Gán syllabus mẫu vào môn',
    enabled: Boolean(coTheVeTangTongQuan.value && selected.chuongTrinhMon?.id)
  },
  {
    key: 'syllabusTaiLieu',
    label: 'Tài liệu syllabus',
    value: selected.syllabusTaiLieu?.ten || selected.syllabusTaiLieu?.ma || '',
    tang: 9,
    tenTang: 'Tầng 9 - Chi tiết syllabus áp dụng',
    enabled: Boolean(coTheVeTangTongQuan.value && selected.syllabusMonHoc?.id)
  }
])

function diChuyenTheoTomTat(muc) {
  if (!muc?.enabled) return
  veTang(muc.tang)
}

const coTheVeTang7 = computed(() => Boolean(
    coTheVeTangTongQuan.value &&
    selected.khungKy?.id
))
const coTheVeTang8 = computed(() => Boolean(
    coTheVeTangTongQuan.value &&
    selected.chuongTrinhMon?.id
))
const coTheVeTang9 = computed(() => Boolean(coTheVeTangTongQuan.value && selected.chuongTrinhMon?.id))

const cacNutDieuHuongTang = computed(() => {
  const so = tangHienTai.value
  const taoNut = (key, label, tang, enabled = true, primary = false, action = 'tang') => ({
    key,
    label,
    tang,
    enabled: Boolean(enabled),
    primary,
    action
  })

  if (so === 1) return [taoNut('tiep-cau-hinh', 'Tiếp tục sang Cấu hình chương trình →', 2, Boolean(selected.nganh?.id), true)]
  if (so === 2) return []
  if (so === 3) return [
    taoNut('ve-cau-hinh', '← Về Cấu hình', 2, Boolean(selected.nganh?.id)),
    taoNut('tiep-version', 'Tiếp tục xem Version →', 4, Boolean(selected.chuongTrinh?.id), true)
  ]
  if (so === 4) return [
    taoNut('ve-chuong-trinh', '← Về Chương trình', 3, Boolean(selected.nganh?.id)),
    taoNut('tiep-tong-quan', 'Sang Tổng quan Version →', 5, coTheVeTangTongQuan.value, true)
  ]
  if (so === 5) return [
    taoNut('ve-version', '← Về Version', 4, Boolean(selected.nganh?.id && selected.chuongTrinh?.id)),
    taoNut('xem-ky-hoc', 'Xem kỳ học →', 6, coTheVeTangTongQuan.value, true)
  ]
  if (so === 6) return [
    taoNut('ve-tong-quan', '← Về Tổng quan Version', 5, coTheVeTangTongQuan.value),
    taoNut('tiep-mon', 'Xem môn trong kỳ →', 7, coTheVeTang7.value, true)
  ]
  if (so === 7) return [
    taoNut('ve-ky-hoc', '← Về Kỳ học', 6, coTheVeTangTongQuan.value),
    taoNut('gan-syllabus-mau', 'Gán syllabus mẫu cho môn →', 8, coTheVeTang8.value, true)
  ]
  if (so === 8) return [
    taoNut('ve-mon', '← Về Môn trong kỳ', 7, coTheVeTangTongQuan.value),
    taoNut('xem-chi-tiet-syllabus', 'Xem chi tiết syllabus →', 9, coTheVeTang9.value, true)
  ]
  return [
    taoNut('ve-syllabus-mau', '← Về Syllabus mẫu', 8, coTheVeTangTongQuan.value),
    taoNut('ve-mon', '← Về Môn trong kỳ', 7, coTheVeTangTongQuan.value)
  ]
})

const moTaDieuHuongTang = computed(() => {
  if (tangHienTai.value === 1) return selected.nganh?.id ? 'Đã chọn ngành. Có thể đi tiếp sang cấu hình chương trình.' : 'Chọn một ngành trước khi đi tiếp.'
  if (tangHienTai.value === 2) return moTaNutTiepTuc.value
  if (tangHienTai.value === 5) return 'Tầng tổng quan dài, nút chuyển tầng được đặt cả trên và dưới để không cần kéo lên đầu trang.'
  if (tangHienTai.value === 6) return 'Chọn một kỳ rồi bấm Xem môn trong kỳ để đi đúng flow Version → Kỳ → Môn.'
  if (tangHienTai.value === 7) return 'Đang xem môn theo Version/Kỳ đã chọn. Bấm Chi tiết ở môn trong chương trình để xem chi tiết môn.'
  if (tangHienTai.value === 8) return coTheVeTang9.value ? 'Có thể sang syllabus áp dụng của Môn trong CT đang chọn.' : 'Muốn sang tầng 9 cần chọn Môn trong chương trình ở tầng 7.'
  if (tangHienTai.value === 9) return 'Đây là tầng cuối. Có thể quay lại tầng 8 hoặc tầng 7 để kiểm tra dữ liệu.'
  return 'Dùng các nút này để đi theo flow mà không cần kéo lên thanh tầng phía trên.'
})

function cuonLenDauTrangSauKhiDoiTang() {
  nextTick(() => {
    window.setTimeout(() => {
      const diemDau = document.querySelector('.dao-tao-xem-page')
      if (diemDau) {
        diemDau.scrollIntoView({behavior: 'smooth', block: 'start'})
        return
      }

      window.scrollTo({top: 0, behavior: 'smooth'})
    }, 80)
  })
}

function bamNutDieuHuongTang(nut) {
  if (!nut?.enabled) return
  if (nut.action === 'luu') {
    luuChuongTrinhTong()
    cuonLenDauTrangSauKhiDoiTang()
    return
  }

  veTang(nut.tang)
  cuonLenDauTrangSauKhiDoiTang()
}

function bamNutTiepTucChung() {
  if (!coTheBamNutTiepTuc.value) return
  if (tangHienTai.value === 2) {
    veTang(3)
    cuonLenDauTrangSauKhiDoiTang()
    return
  }
  if (tangHienTai.value === 6) {
    veTang(7)
    cuonLenDauTrangSauKhiDoiTang()
  }
}

function timDongTheoId(key, id) {
  if (!id) return null
  return (duLieu.value[key] || []).find((item) => String(item.id) === String(id)) || null
}

function timMonHocCuaChuongTrinhMon(chuongTrinhMon) {
  const monHocId = chuongTrinhMon?.monHocId || chuongTrinhMon?.monId || chuongTrinhMon?.monHoc?.id || null
  return timDongTheoId('monHoc', monHocId)
}

function chonMonHocTheoChuongTrinhMon(chuongTrinhMon) {
  const monHoc = timMonHocCuaChuongTrinhMon(chuongTrinhMon)
  if (monHoc && String(selected.monHoc?.id || '') !== String(monHoc.id)) {
    selectEntity('monHoc', monHoc)
  }
}

function taoParamsTangSau(params = {}) {
  return Object.fromEntries(
      Object.entries(params).filter(([, value]) => value !== null && value !== undefined && value !== '')
  )
}

function napLuaChonTheoRoute() {
  const nganh = timDongTheoId('nganh', route.params.nganhId)
  if (nganh && String(selected.nganh?.id || '') !== String(nganh.id)) selectEntity('nganh', nganh)

  const nganhHeDaoTao = timDongTheoId('nganhHeDaoTao', route.query.nganhHeDaoTaoId)
  if (nganhHeDaoTao && String(selected.nganhHeDaoTao?.id || '') !== String(nganhHeDaoTao.id)) {
    selectEntity('nganhHeDaoTao', nganhHeDaoTao)
  }

  const chuongTrinh = timDongTheoId('chuongTrinh', route.params.chuongTrinhId)
  if (chuongTrinh && String(selected.chuongTrinh?.id || '') !== String(chuongTrinh.id)) {
    selectEntity('chuongTrinh', chuongTrinh)

    const nganhLoai = timDongTheoId('nganhLoaiChuongTrinh', chuongTrinh.nganhLoaiChuongTrinhId)
    if (nganhLoai) selectEntity('nganhLoaiChuongTrinh', nganhLoai)
  }
  // Khôi phục nganhHeDaoTao từ chuongTrinh nếu chưa có (F5 trên Tầng 4+)
  if (!selected.nganhHeDaoTao?.id && chuongTrinh?.nganhHeDaoTaoId) {
    const nhdt = timDongTheoId('nganhHeDaoTao', chuongTrinh.nganhHeDaoTaoId)
    if (nhdt) selectEntity('nganhHeDaoTao', nhdt)
  }
  const khungKyTheoQuery = timDongTheoId('khungKy', route.query.khungKyId)
  if (khungKyTheoQuery && String(selected.khungKy?.id || '') !== String(khungKyTheoQuery.id)) {
    selectEntity('khungKy', khungKyTheoQuery)
  }


  const nhomTuChonTheoQuery = timDongTheoId('nhomTuChon', route.query.nhomTuChonId)
  if (nhomTuChonTheoQuery && String(selected.nhomTuChon?.id || '') !== String(nhomTuChonTheoQuery.id)) {
    selectEntity('nhomTuChon', nhomTuChonTheoQuery)
  }

  const version = timDongTheoId('chuongTrinhVersion', route.params.versionId)
  if (version && String(selected.chuongTrinhVersion?.id || '') !== String(version.id)) selectEntity('chuongTrinhVersion', version)

  const monHoc = timDongTheoId('monHoc', route.params.monHocId)
  if (monHoc && String(selected.monHoc?.id || '') !== String(monHoc.id)) selectEntity('monHoc', monHoc)

  const chuongTrinhMon = timDongTheoId(
      'chuongTrinhMon',
      route.params.chuongTrinhMonId || route.query.chuongTrinhMonId
  )

  if (chuongTrinhMon && String(selected.chuongTrinhMon?.id || '') !== String(chuongTrinhMon.id)) {
    const khungKyCuaMon = timDongTheoId('khungKy', chuongTrinhMon.khungKyId)
    if (khungKyCuaMon && String(selected.khungKy?.id || '') !== String(khungKyCuaMon.id)) {
      selectEntity('khungKy', khungKyCuaMon)
    }

    selectEntity('chuongTrinhMon', chuongTrinhMon)
    chonMonHocTheoChuongTrinhMon(chuongTrinhMon)
  }
  const syllabusMonTheoQuery = timDongTheoId('syllabusMonHoc', route.query.syllabusMonId)
  if (syllabusMonTheoQuery && String(selected.syllabusMonHoc?.id || '') !== String(syllabusMonTheoQuery.id)) {
    selectEntity('syllabusMonHoc', syllabusMonTheoQuery)
  }
  if (!route.params.monHocId && selected.chuongTrinhMon?.id && !selected.monHoc?.id) {
    chonMonHocTheoChuongTrinhMon(selected.chuongTrinhMon)
  }

  const trinhDoTheoQuery = timDongTheoId('trinhDoDaoTao', route.query.trinhDoId)
  if (trinhDoTheoQuery) selectEntity('trinhDoDaoTao', trinhDoTheoQuery)

  const loaiTheoQuery = timDongTheoId('loaiChuongTrinh', route.query.loaiChuongTrinhId)
  if (loaiTheoQuery) selectEntity('loaiChuongTrinh', loaiTheoQuery)

  const nganhLoaiTheoQuery = timDongTheoId('nganhLoaiChuongTrinh', route.query.nganhLoaiChuongTrinhId)
  if (nganhLoaiTheoQuery) {
    selectEntity('nganhLoaiChuongTrinh', nganhLoaiTheoQuery)

    const loaiTheoNganh = timDongTheoId('loaiChuongTrinh', nganhLoaiTheoQuery.loaiChuongTrinhId)
    if (loaiTheoNganh) selectEntity('loaiChuongTrinh', loaiTheoNganh)
  }
}

watch(
    () => [
      route.fullPath,
      duLieu.value.nganh.length,
      duLieu.value.chuongTrinh.length,
      duLieu.value.chuongTrinhVersion.length,
      duLieu.value.khungKy.length,
      duLieu.value.nhomKienThuc.length,
      duLieu.value.monHoc.length,
      duLieu.value.chuongTrinhMon.length,
      duLieu.value.syllabusMonHoc.length
    ],
    () => napLuaChonTheoRoute(),
    {immediate: true}
)

function veTang(soTang) {
  if (soTang <= 1) {
    router.push({name: 'DaoTao.XemChuongTrinh'})
    return
  }

  if (soTang === 2 && selected.nganh?.id) {
    router.push({name: 'DaoTao.XemChuongTrinh.CauHinh', params: {nganhId: selected.nganh.id}})
    return
  }

  if (soTang === 3 && selected.nganh?.id) {
    router.push({
      name: 'DaoTao.XemChuongTrinh.ChuongTrinh',
      params: {nganhId: selected.nganh.id},
      query: {
        nganhHeDaoTaoId: selected.nganhHeDaoTao?.id || selected.chuongTrinh?.nganhHeDaoTaoId || route.query.nganhHeDaoTaoId || undefined,
        trinhDoId: selected.trinhDoDaoTao?.id || undefined,
        loaiChuongTrinhId: selected.loaiChuongTrinh?.id || undefined
      }
    })
    return
  }

  if (soTang === 4 && selected.nganh?.id && selected.chuongTrinh?.id) {
    router.push({
      name: 'DaoTao.XemChuongTrinh.Version',
      params: {nganhId: selected.nganh.id, chuongTrinhId: selected.chuongTrinh.id},
      query: {
        nganhHeDaoTaoId: selected.nganhHeDaoTao?.id || selected.chuongTrinh?.nganhHeDaoTaoId || undefined
      }
    })
    return
  }

  if (soTang === 5 && coTheVeTangTongQuan.value) {
    router.push({
      name: 'DaoTao.XemChuongTrinh.TongQuan',
      params: {
        nganhId: selected.nganh.id,
        chuongTrinhId: selected.chuongTrinh.id,
        versionId: selected.chuongTrinhVersion.id
      }
    })
    return
  }

  if (soTang === 6 && coTheVeTangTongQuan.value) {
    router.push({
      name: 'DaoTao.XemChuongTrinh.KhungCauTruc',
      params: {
        nganhId: selected.nganh.id,
        chuongTrinhId: selected.chuongTrinh.id,
        versionId: selected.chuongTrinhVersion.id
      },
      query: {}
    })
    return
  }

  if (soTang === 7 && coTheVeTangTongQuan.value) {
    router.push({
      name: 'DaoTao.XemChuongTrinh.Mon',
      params: {
        nganhId: selected.nganh.id,
        chuongTrinhId: selected.chuongTrinh.id,
        versionId: selected.chuongTrinhVersion.id
      },
      query: {
        khungKyId: selected.khungKy?.id || undefined
      }
    })
    return
  }

  if (soTang === 8 && coTheVeTangTongQuan.value) {
    const monHocId = selected.monHoc?.id || selected.chuongTrinhMon?.monHocId || selected.chuongTrinhMon?.monId || null
    router.push({
      name: 'DaoTao.XemChuongTrinh.Syllabusmau',
      params: taoParamsTangSau({
        nganhId: selected.nganh.id,
        chuongTrinhId: selected.chuongTrinh.id,
        versionId: selected.chuongTrinhVersion.id,
        monHocId
      }),
      query: taoParamsTangSau({
        chuongTrinhMonId: selected.chuongTrinhMon?.id || null
      })
    })
    return
  }

  if (soTang === 9 && coTheVeTangTongQuan.value) {
    router.push({
      name: 'DaoTao.XemChuongTrinh.SyllabusApDung',
      params: taoParamsTangSau({
        nganhId: selected.nganh.id,
        chuongTrinhId: selected.chuongTrinh.id,
        versionId: selected.chuongTrinhVersion.id,
        chuongTrinhMonId: selected.chuongTrinhMon?.id || null
      }),
      query: taoParamsTangSau({
        syllabusMonId: selected.syllabusMonHoc?.id || null,
        khungKyId: selected.khungKy?.id || selected.chuongTrinhMon?.khungKyId || null,
      })
    })
  }
}

function denKhungCauTruc() {
  if (!coTheVeTangTongQuan.value) return
  router.push({
    name: 'DaoTao.XemChuongTrinh.KhungCauTruc',
    params: {
      nganhId: selected.nganh.id,
      chuongTrinhId: selected.chuongTrinh.id,
      versionId: selected.chuongTrinhVersion.id
    },
    query: {}
  })
}

function denMonTrongChuongTrinh() {
  if (!coTheVeTangTongQuan.value) return
  router.push({
    name: 'DaoTao.XemChuongTrinh.Mon',
    params: {
      nganhId: selected.nganh.id,
      chuongTrinhId: selected.chuongTrinh.id,
      versionId: selected.chuongTrinhVersion.id
    }
  })
}

function xemTrinhDoTheoNganh(item) {
  const trinhDo = timTrinhDoTheoId(item.trinhDoId)
  if (!trinhDo) {
    datThongBaoBang('nganhTrinhDoDaoTao', 'Không tìm thấy trình độ đào tạo của dòng này.', 'error')
    return
  }

  selectEntity('trinhDoDaoTao', trinhDo)
  datThongBaoBang('nganhTrinhDoDaoTao', `Đang xem trình độ đào tạo: ${trinhDo.tenTrinhDo || trinhDo.ten || trinhDo.id}.`, 'success')
}

async function xoaTrinhDoTheoNganh(item) {
  if (!item?.id) {
    datThongBaoBang('nganhTrinhDoDaoTao', 'Không tìm thấy dòng trình độ đào tạo để xóa.', 'error')
    return
  }

  if (!confirm(`Xóa trình độ đào tạo "${tenTrinhDoTheoId(item.trinhDoId)}" khỏi ngành này?`)) return

  try {
    await services.nganhTrinhDoDaoTao.delete(item.id)
    if (String(selected.trinhDoDaoTao?.id || '') === String(item.trinhDoId || '')) {
      selectEntity('trinhDoDaoTao', null)
    }
    await taiDuLieuCoSanTatCaBang()
    // datThongBaoBang('nganhTrinhDoDaoTao', 'Đã xóa trình độ đào tạo khỏi ngành.', 'success')
  } catch (error) {
    const message = layThongBaoLoi(error, 'Không xóa được trình độ đào tạo khỏi ngành.')
    datThongBaoBang('nganhTrinhDoDaoTao', message, 'error')
  }
}

function xemLoaiChuongTrinhTheoNganh(item) {
  const loaiChuongTrinh = timLoaiChuongTrinhTheoId(item.loaiChuongTrinhId)
  if (!loaiChuongTrinh) {
    datThongBaoBang('nganhLoaiChuongTrinh', 'Không tìm thấy loại chương trình của dòng này.', 'error')
    return
  }

  selectEntity('loaiChuongTrinh', loaiChuongTrinh)
  selectEntity('nganhLoaiChuongTrinh', item)

  const trinhDo = chuongTrinhTrinhDoDangChon.value

  router.push({
    name: 'DaoTao.XemChuongTrinh.ChuongTrinh',
    params: {nganhId: selected.nganh?.id || item.nganhId},
    query: {
      nganhLoaiChuongTrinhId: item.id,
      loaiChuongTrinhId: item.loaiChuongTrinhId,
      trinhDoId: trinhDo?.id || undefined
    }
  })
}

async function xoaLoaiChuongTrinhTheoNganh(item) {
  if (!item?.id) {
    datThongBaoBang('nganhLoaiChuongTrinh', 'Không tìm thấy dòng loại chương trình để xóa.', 'error')
    return
  }

  if (!confirm(`Xóa loại chương trình "${tenLoaiChuongTrinhTheoId(item.loaiChuongTrinhId)}" khỏi ngành này?`)) return

  try {
    await services.nganhLoaiChuongTrinh.delete(item.id)
    if (String(selected.loaiChuongTrinh?.id || '') === String(item.loaiChuongTrinhId || '')) {
      selectEntity('loaiChuongTrinh', null)
    }
    await taiDuLieuCoSanTatCaBang()
    // datThongBaoBang('nganhLoaiChuongTrinh', 'Đã xóa loại chương trình khỏi ngành.', 'success')
  } catch (error) {
    const message = layThongBaoLoi(error, 'Không xóa được loại chương trình khỏi ngành.')
    datThongBaoBang('nganhLoaiChuongTrinh', message, 'error')
  }
}

// ===== Bảng 3 tầng 2: Ngành hệ đào tạo theo ngành =====

const nganhHeDaoTaoTheoNganh = computed(() => {
  const nganhId = selected.nganh?.id || route.params.nganhId || null
  if (!nganhId) return []
  return (duLieu.value.nganhHeDaoTao || [])
      .filter(item => String(item.nganhId || '') === String(nganhId || ''))
})

const thongBaoNganhHe = ref('')
const loaiThongBaoNganhHe = ref('success')
const dangLuuNganhHe = ref(false)

const defaultFormNganhHe = () => ({
  id: null,
  trinhDoId: null,
  loaiChuongTrinhId: null,
  soThang: null,
  soKy: null,
  maHe: '',
  tenHe: ''
})
const formNganhHe = ref(defaultFormNganhHe())

function resetFormNganhHe() {
  formNganhHe.value = defaultFormNganhHe()
}

function hienThongBaoNganhHe(msg, loai = 'success') {
  thongBaoNganhHe.value = msg
  loaiThongBaoNganhHe.value = loai
  setTimeout(() => { if (thongBaoNganhHe.value === msg) thongBaoNganhHe.value = '' }, 4000)
}

async function luuNganhHe() {
  const nganhId = selected.nganh?.id || null
  if (!nganhId) { hienThongBaoNganhHe('Cần chọn ngành trước.', 'error'); return }
  if (!formNganhHe.value.trinhDoId) { hienThongBaoNganhHe('Cần chọn trình độ.', 'error'); return }
  if (!formNganhHe.value.loaiChuongTrinhId) { hienThongBaoNganhHe('Cần chọn loại chương trình.', 'error'); return }

  const payload = {
    nganhId,
    trinhDoId: formNganhHe.value.trinhDoId,
    loaiChuongTrinhId: formNganhHe.value.loaiChuongTrinhId,
    soThang: formNganhHe.value.soThang || null,
    soKy: formNganhHe.value.soKy || null,
    maHe: formNganhHe.value.maHe || null,
    tenHe: formNganhHe.value.tenHe || null,
    trangThai: 'dang_su_dung'
  }

  dangLuuNganhHe.value = true
  try {
    if (formNganhHe.value.id) {
      await services.nganhHeDaoTao.update(formNganhHe.value.id, payload)
      hienThongBaoNganhHe('Đã cập nhật ngành hệ đào tạo.')
    } else {
      await services.nganhHeDaoTao.create(payload)
      hienThongBaoNganhHe('Đã thêm ngành hệ đào tạo.')
    }
    resetFormNganhHe()
    await taiDuLieuCoSanTatCaBang()
  } catch (error) {
    hienThongBaoNganhHe(layThongBaoLoi(error, 'Không lưu được ngành hệ đào tạo.'), 'error')
  } finally {
    dangLuuNganhHe.value = false
  }
}

function suaNganhHe(item) {
  formNganhHe.value = {
    id: item.id,
    trinhDoId: item.trinhDoId,
    loaiChuongTrinhId: item.loaiChuongTrinhId,
    soThang: item.soThang || null,
    soKy: item.soKy || null,
    maHe: item.maHe || '',
    tenHe: item.tenHe || ''
  }
}

async function xoaNganhHe(id) {
  if (!confirm('Xóa ngành hệ đào tạo này?')) return
  try {
    await services.nganhHeDaoTao.delete(id)
    hienThongBaoNganhHe('Đã xóa.')
    await taiDuLieuCoSanTatCaBang()
  } catch (error) {
    hienThongBaoNganhHe(layThongBaoLoi(error, 'Không xóa được ngành hệ đào tạo.'), 'error')
  }
}

function xemChuongTrinhTuNganhHe(item) {
  selectEntity('nganhHeDaoTao', item)
  veTang(3)
}

const groups = computed(() => {
  if (tangHienTai.value === 1) {
    return [{
      key: 'tang-1-nganh',
      title: 'Tầng 1: Ngành',
      description: 'Bấm Xem thêm tại một ngành để chuyển sang tầng cấu hình chương trình.',
      mau: 'mau-xanh',
      tables: [taoBang('nganh', {canView: true})]
    }]
  }

  if (tangHienTai.value === 2) {
    return []
  }

  if (tangHienTai.value === 3) {
    return [{
      key: 'tang-3-chuong-trinh',
      title: 'Tầng 3: Chương trình đào tạo',
      description: 'Chương trình đào tạo là con của Loại chương trình theo ngành. Vào tầng này bằng nút Xem ở bảng 2.2.1.',
      mau: 'mau-luc',
      tables: [
        taoBang('chuongTrinh', {
          parentValues: chuongTrinhParent.value,
          filterValues: chuongTrinhFilter.value,
          parentText: chuongTrinhParentText.value,
          canView: true,
          disabled: !selected.nganhHeDaoTao,
          disabledText: 'Cần bấm Xem chương trình ở bảng Ngành hệ đào tạo trước.'
        })
      ]
    }]
  }

  if (tangHienTai.value === 4) {
    return [{
      key: 'tang-4-version',
      title: 'Tầng 4: Version chương trình',
      description: 'Bấm Xem thêm tại version để vào tổng quan version.',
      mau: 'mau-cam',
      tables: [
        taoBang('chuongTrinhVersion', {
          parentValues: chuongTrinhVersionParent.value,
          filterValues: chuongTrinhVersionFilter.value,
          parentText: chuongTrinhVersionParentText.value,
          canView: true,
          fields: chuongTrinhVersionFields.value,
          disabled: !selected.chuongTrinh,
          disabledText: 'Cần chọn Chương trình trước.'
        })
      ]
    }]
  }

  if (tangHienTai.value === 5) {
    return [{
      key: 'tang-5-tong-quan',
      title: 'Tầng 5: Thông tin tổng quan version',
      description: 'Mẫu thuộc chương trình để tái sử dụng; Version chỉ lưu bản copy độc lập.',
      mau: 'mau-hong',
      tables: [
        taoBang('syllabusChuongTrinhmau', {
          displayRows: rowsSyllabusChuongTrinhMauTheoVersion.value,
          linkParentValues: versionParent.value,
          parentText: `${chuongTrinhVersionParentText.value} | Chỉ hiển thị syllabus chương trình mẫu thuộc Chương trình đang chọn. Bảng mẫu tại tầng 5 chỉ gán/hủy gán, không CRUD.`,
          readOnly: true,
          canSelect: false,
          canToggleSave: true,
          canShowSavedStatus: true,
          toggleSaveLabel: 'Gán vào',
          toggleSavedLabel: 'Đã gán',
          statusSavedLabel: '✓ Đã gán',
          statusUnsavedLabel: '+ Chưa gán',
          saveStatusText: 'Bảng mẫu chỉ dùng để copy/gán vào Version hiện tại. Bấm Đã gán lần nữa chỉ bỏ chọn dòng mẫu, không xóa dữ liệu đã lưu.',
          disabled: !selected.chuongTrinhVersion,
          disabledText: 'Cần chọn Version trước.',
          emptyText: 'Chương trình này chưa có syllabus chương trình mẫu.',
          uploadFile: uploadFileSyllabusChuongTrinh
        }),
        taoBang('syllabusChuongTrinh', {
          displayRows: rowsSyllabusChuongTrinhApDungTheoVersion.value,
          parentValues: versionParent.value,
          filterValues: versionFilter.value,
          forceFilter: true,
          readOnly: false,
          canSelect: false,
          canToggleSave: false,
          canShowSavedStatus: false,
          title: 'Syllabus chương trình đã lưu vào Version',
          description: 'Chỉ hiển thị syllabus chương trình đã được copy/gán từ bảng mẫu vào Version hiện tại.',
          tableTitle: 'Syllabus chương trình đã lưu/gắn vào Version',
          parentText: versionParentText.value,
          disabled: !selected.chuongTrinhVersion,
          uploadFile: uploadFileSyllabusChuongTrinh,
          disabledText: 'Cần chọn Version trước.',
          emptyText: 'Version này chưa có syllabus chương trình đã lưu.'
        }),
        taoBang('mucTieuChuongTrinhmau', {
          displayRows: locRowsTheoSyllabusChuongTrinhMau(duLieu.value.mucTieuChuongTrinhmau || []),
          linkParentValues: {syllabusChuongTrinhMauId: syllabusChuongTrinhMauIdDangGan.value},
          parentText: `${syllabusChuongTrinhParentText.value} | Chỉ hiển thị mục tiêu mẫu thuộc syllabus chương trình mẫu đang gán. Bảng mẫu tại tầng 5 chỉ gán/hủy gán, không CRUD.`,
          readOnly: true,
          canSelect: false,
          canToggleSave: true,
          canShowSavedStatus: true,
          toggleSaveLabel: 'Gán vào',
          toggleSavedLabel: 'Đã gán',
          statusSavedLabel: '✓ Đã gán',
          statusUnsavedLabel: '+ Chưa gán',
          saveStatusText: 'Bấm Gán vào để copy dữ liệu mẫu vào Version; bấm Đã gán lần nữa chỉ bỏ chọn dòng mẫu, không xóa dữ liệu đã lưu.',
          disabled: !selected.chuongTrinhVersion || !syllabusChuongTrinhMauIdDangGan.value,
          disabledText: 'Cần gán Syllabus chương trình mẫu cho Version trước.'
        }),
        taoBang('mucTieuChuongTrinh', {
          displayRows: locRowsTheoSyllabusChuongTrinhApDung(duLieu.value.mucTieuChuongTrinh || []),
          parentValues: {syllabusChuongTrinhId: syllabusChuongTrinhIdDangGan.value},
          filterValues: {syllabusChuongTrinhId: syllabusChuongTrinhIdDangGan.value},
          forceFilter: true,
          readOnly: false,
          canSelect: false,
          canToggleSave: false,
          canShowSavedStatus: false,
          title: '10. Mục tiêu chương trình đã lưu vào Version',
          description: 'Chỉ hiển thị mục tiêu đã copy từ mục tiêu mẫu thuộc syllabus chương trình mẫu đang gán.',
          tableTitle: 'Mục tiêu chương trình đã lưu vào Version',
          parentText: syllabusChuongTrinhParentText.value,
          disabled: !selected.chuongTrinhVersion || !syllabusChuongTrinhIdDangGan.value,
          disabledText: 'Cần gán Syllabus chương trình mẫu cho Version trước.'
        }),
        taoBang('nangLucDauRamau', {
          displayRows: locRowsTheoSyllabusChuongTrinhMau(duLieu.value.nangLucDauRamau || []),
          linkParentValues: {syllabusChuongTrinhMauId: syllabusChuongTrinhMauIdDangGan.value},
          parentText: `${syllabusChuongTrinhParentText.value} | Chỉ hiển thị năng lực đầu ra mẫu thuộc syllabus chương trình mẫu đang gán. Bảng mẫu tại tầng 5 chỉ gán/hủy gán, không CRUD.`,
          readOnly: true,
          canSelect: false,
          canToggleSave: true,
          canShowSavedStatus: true,
          toggleSaveLabel: 'Gán vào',
          toggleSavedLabel: 'Đã gán',
          statusSavedLabel: '✓ Đã gán',
          statusUnsavedLabel: '+ Chưa gán',
          saveStatusText: 'Bấm Gán vào để copy dữ liệu mẫu vào Version; bấm Đã gán lần nữa chỉ bỏ chọn dòng mẫu, không xóa dữ liệu đã lưu.',
          disabled: !selected.chuongTrinhVersion || !syllabusChuongTrinhMauIdDangGan.value,
          disabledText: 'Cần gán Syllabus chương trình mẫu cho Version trước.'
        }),
        taoBang('nangLucDauRa', {
          displayRows: locRowsTheoSyllabusChuongTrinhApDung(duLieu.value.nangLucDauRa || []),
          parentValues: {syllabusChuongTrinhId: syllabusChuongTrinhIdDangGan.value},
          filterValues: {syllabusChuongTrinhId: syllabusChuongTrinhIdDangGan.value},
          forceFilter: true,
          readOnly: false,
          canSelect: false,
          canToggleSave: false,
          canShowSavedStatus: false,
          title: '13. Năng lực đầu ra đã lưu vào Version',
          description: 'Chỉ hiển thị năng lực đầu ra đã copy từ năng lực mẫu thuộc syllabus chương trình mẫu đang gán.',
          tableTitle: 'Năng lực đầu ra đã lưu/gắn vào Version',
          parentText: syllabusChuongTrinhParentText.value,
          disabled: !selected.chuongTrinhVersion || !syllabusChuongTrinhIdDangGan.value,
          disabledText: 'Cần gán Syllabus chương trình mẫu cho Version trước.'
        }),
        taoBang('viTriViecLammau', {
          displayRows: locRowsTheoSyllabusChuongTrinhMau(duLieu.value.viTriViecLammau || []),
          linkParentValues: {syllabusChuongTrinhMauId: syllabusChuongTrinhMauIdDangGan.value},
          parentText: `${syllabusChuongTrinhParentText.value} | Chỉ hiển thị vị trí việc làm mẫu thuộc syllabus chương trình mẫu đang gán. Bảng mẫu tại tầng 5 chỉ gán/hủy gán, không CRUD.`,
          readOnly: true,
          canSelect: false,
          canToggleSave: true,
          canShowSavedStatus: true,
          toggleSaveLabel: 'Gán vào',
          toggleSavedLabel: 'Đã gán',
          statusSavedLabel: '✓ Đã gán',
          statusUnsavedLabel: '+ Chưa gán',
          saveStatusText: 'Bấm Gán vào để copy dữ liệu mẫu vào Version; bấm Đã gán lần nữa chỉ bỏ chọn dòng mẫu, không xóa dữ liệu đã lưu.',
          disabled: !selected.chuongTrinhVersion || !syllabusChuongTrinhMauIdDangGan.value,
          disabledText: 'Cần gán Syllabus chương trình mẫu cho Version trước.'
        }),
        taoBang('viTriViecLam', {
          displayRows: locRowsTheoSyllabusChuongTrinhApDung(duLieu.value.viTriViecLam || []),
          parentValues: {syllabusChuongTrinhId: syllabusChuongTrinhIdDangGan.value},
          filterValues: {syllabusChuongTrinhId: syllabusChuongTrinhIdDangGan.value},
          forceFilter: true,
          readOnly: false,
          canSelect: false,
          canToggleSave: false,
          canShowSavedStatus: false,
          title: '16. Vị trí việc làm đã lưu vào Version',
          description: 'Chỉ hiển thị vị trí việc làm đã copy từ vị trí mẫu thuộc syllabus chương trình mẫu đang gán.',
          tableTitle: 'Vị trí việc làm đã lưu/gắn vào Version',
          parentText: syllabusChuongTrinhParentText.value,
          disabled: !selected.chuongTrinhVersion || !syllabusChuongTrinhIdDangGan.value,
          disabledText: 'Cần gán Syllabus chương trình mẫu cho Version trước.'
        }),
        taoBang('dieuKienTotNghiepmau', {
          displayRows: locRowsTheoSyllabusChuongTrinhMau(duLieu.value.dieuKienTotNghiepmau || []),
          linkParentValues: {syllabusChuongTrinhMauId: syllabusChuongTrinhMauIdDangGan.value},
          parentText: `${syllabusChuongTrinhParentText.value} | Chỉ hiển thị điều kiện tốt nghiệp mẫu thuộc syllabus chương trình mẫu đang gán. Bảng mẫu tại tầng 5 chỉ gán/hủy gán, không CRUD.`,
          readOnly: true,
          canSelect: false,
          canToggleSave: true,
          canShowSavedStatus: true,
          toggleSaveLabel: 'Gán vào',
          toggleSavedLabel: 'Đã gán',
          statusSavedLabel: '✓ Đã gán',
          statusUnsavedLabel: '+ Chưa gán',
          saveStatusText: 'Bấm Gán vào để copy dữ liệu mẫu vào Version; bấm Đã gán lần nữa chỉ bỏ chọn dòng mẫu, không xóa dữ liệu đã lưu.',
          disabled: !selected.chuongTrinhVersion || !syllabusChuongTrinhMauIdDangGan.value,
          disabledText: 'Cần gán Syllabus chương trình mẫu cho Version trước.'
        }),
        taoBang('dieuKienTotNghiep', {
          displayRows: locRowsTheoSyllabusChuongTrinhApDung(duLieu.value.dieuKienTotNghiep || []),
          parentValues: {syllabusChuongTrinhId: syllabusChuongTrinhIdDangGan.value},
          filterValues: {syllabusChuongTrinhId: syllabusChuongTrinhIdDangGan.value},
          forceFilter: true,
          readOnly: false,
          canSelect: false,
          canToggleSave: false,
          canShowSavedStatus: false,
          title: '19. Điều kiện tốt nghiệp đã lưu vào Version',
          description: 'Chỉ hiển thị điều kiện tốt nghiệp đã copy từ điều kiện mẫu thuộc syllabus chương trình mẫu đang gán.',
          tableTitle: 'Điều kiện tốt nghiệp đã lưu vào Version',
          parentText: syllabusChuongTrinhParentText.value,
          disabled: !selected.chuongTrinhVersion || !syllabusChuongTrinhIdDangGan.value,
          disabledText: 'Cần gán Syllabus chương trình mẫu cho Version trước.'
        })      ]
    }]
  }

  if (tangHienTai.value === 6) {
    return [{
      key: 'tang-6-ky-hoc',
      title: 'Tầng 6: Kỳ học',
      description: 'Tạo/chọn kỳ học của Version trước. Bấm Xem môn trong kỳ để gán môn trực tiếp vào kỳ.',
      mau: 'mau-vang',
      tables: [
        taoBang('khungKymau', {
          linkParentValues: khungKyParent.value,
          parentText: `${versionParentText.value} | Kho khung kỳ mẫu dùng chung.`,
          disabled: !selected.chuongTrinhVersion,
          disabledText: 'Cần chọn Version trước.'
        }),
        taoBang('khungKy', {
          parentValues: khungKyParent.value,
          filterValues: khungKyFilter.value,
          parentText: khungKyParentText.value,
          defaultForm: khungKyDefaultFormGoiY.value,
          canView: true,
          viewLabel: 'Xem môn trong kỳ',
          disabled: !selected.chuongTrinhVersion,
          disabledText: 'Cần chọn Version trước.'
        })
      ]
    }]
  }


  if (tangHienTai.value === 7) {
    return [{
      key: 'tang-7-mon-trong-ky',
      title: 'Tầng 7: Môn trong kỳ',
      description: 'Gán môn học trực tiếp vào kỳ qua bảng chương trình môn.',
      mau: 'mau-nmau',
      tables: [
        taoBang('monHoc', {
          parentValues: {},
          filterValues: {},
          displayRows: danhSachMonHocTang7DaLoc.value,
          linkParentValues: chuongTrinhMonParent.value,
          parentText: `${viTriHienTaiText.value} | Môn học đã nằm trong version. Bấm Gán vào kỳ để đưa môn vào Kỳ đang chọn.`,
          canToggleSave: true,
          canSelect: false,
          canShowSavedStatus: true,
          toggleSaveLabel: 'Gán vào kỳ',
          toggleSavedLabel: 'Bỏ khỏi kỳ',
          statusSavedLabel: '✓ Đã ở kỳ này',
          statusUnsavedLabel: '+ Chưa gán kỳ',
          tableTitle: 'Môn học trong version',
          emptyText: 'Version này chưa có môn nào. Hãy copy môn vào version ở trang "Nhóm kiến thức và môn" trước.',
          disabled: !selected.chuongTrinhVersion || !selected.khungKy,
          disabledText: 'Cần chọn Version và Kỳ học trước.',
          columns: [
            {key: 'maMon', label: 'Mã môn'},
            {key: 'tenMon', label: 'Tên môn'},
            {key: 'tenKyDaGan', label: 'Thuộc kỳ'}
          ]
        }),
        taoBang('chuongTrinhMon', {
          parentValues: chuongTrinhMonTheoKyFilter.value,
          filterValues: chuongTrinhMonTheoKyFilter.value,
          parentText: viTriHienTaiText.value,
          canView: true,
          viewLabel: 'Chi tiết',
          readOnly: false,
          canSelect: true,
          canToggleSave: false,
          fields: chuongTrinhMonFieldsTang7,
          title: 'Môn đã lưu vào kỳ',
          description: 'Các môn đã gắn vào kỳ hiện tại. Bấm "Bỏ khỏi kỳ" chỉ gỡ khungKyId khỏi môn trong Version, không xóa môn khỏi Version.',
          tableTitle: 'Môn học đã gắn vào kỳ',
          emptyText: 'Chưa có môn học nào được gắn vào kỳ hiện tại.',
          disabled: !selected.chuongTrinhVersion || !selected.khungKy,
          disabledText: 'Cần chọn Version và Kỳ học trước.',
          deleteHandler: boMonKhoiKy,
          deleteLabel: 'Bỏ khỏi kỳ'
        })
      ]
    }]
  }


  if (tangHienTai.value === 8) {
    return [{
      key: 'tang-8-gan-syllabus-mon',
      title: 'Tầng 8: Gán syllabus áp dụng cho môn trong chương trình',
      description: 'Tầng này chỉ chọn/copy syllabus mẫu vào môn trong chương trình. Điểm và quy đổi kết quả không CRUD trực tiếp tại môn nữa.',
      mau: 'mau-troi',
      tables: [
        taoBang('syllabusMonHocmau', {
          parentValues: syllabusMonHocmauParent.value,
          filterValues: syllabusMonHocmauFilter.value,
          displayRows: danhSachSyllabusmauTang8DaLoc.value,
          linkParentValues: syllabusMonHocApDungParent.value,
          parentText: `${chuongTrinhMonOnlyParentText.value} | Chỉ hiện syllabus mẫu đúng môn. Bảng này không CRUD tại tầng 8; bấm Chọn để xem dòng mẫu, bấm Chọn + copy để tạo/cập nhật bảng Syllabus đã gán vào môn trong chương trình bên dưới.`,
          readOnly: true,
          canSelect: true,
          canToggleSave: true,
          canShowSavedStatus: true,
          toggleSaveLabel: 'Chọn + copy',
          toggleSavedLabel: 'Bỏ chọn',
          statusSavedLabel: '✓ Đã chọn/gán vào môn',
          statusUnsavedLabel: '+ Chưa chọn',
          title: 'Syllabus môn học mẫu - TÁI SỬ DỤNG',
          description: 'Kho syllabus mẫu theo môn chỉ để xem và tái sử dụng tại tầng 8. CRUD syllabus mẫu thực hiện ở tab Syllabus môn học mẫu ngoài ngành/chương trình; tại đây chỉ chọn/copy xuống syllabus áp dụng.',
          tableTitle: 'Chọn syllabus mẫu đúng môn để gán/copy',
          emptyText: 'Môn này chưa có syllabus mẫu. Hãy tạo ở tab Syllabus môn học mẫu trước.',
          disabled: !selected.chuongTrinhMon,
          disabledText: 'Cần chọn Môn trong chương trình ở tầng 7 trước.'
        }),
        taoBang('syllabusMonHoc', {
          parentValues: chuongTrinhMonOnlyParent.value,
          filterValues: chuongTrinhMonOnlyFilter.value,
          forceFilter: true,
          parentText: `${chuongTrinhMonOnlyParentText.value} | Đây là dữ liệu syllabus_mon_hoc đã gán/copy từ syllabus mẫu. Bấm Xem chi tiết syllabus để sang tầng 9 quản lý cột điểm và quy đổi kết quả.`,
          readOnly: false,
          canSelect: true,
          canToggleSave: false,
          canView: true,
          viewLabel: 'Xem chi tiết syllabus',
          title: 'Syllabus đã gán vào môn trong chương trình',
          description: 'Môn trong chương trình chỉ giữ quan hệ với syllabus. Cột điểm và mẫu quy đổi kết quả nằm ở tầng 9 theo syllabus.',
          tableTitle: 'Syllabus đã gán vào môn trong chương trình',
          emptyText: 'Chưa có syllabus nào được gán cho môn trong chương trình này. Hãy bấm Gán vào syllabus môn ở bảng trên.',
          disabled: !selected.chuongTrinhMon,
          disabledText: 'Cần chọn Môn trong chương trình ở tầng 7 trước.'
        })
      ]
    }]
  }

  return [{
    key: 'tang-9-chi-tiet-syllabus-ap-dung',
    title: 'Tầng 9: Chi tiết syllabus áp dụng',
    description: 'Quản lý chương/bài, điều kiện thực hiện môn học và tài liệu của syllabus môn học đã áp dụng.',
    mau: 'mau-xam',
    tables: [
      taoBang('cauHinhDanhGiaMau', {
        displayRows: cotDiemSyllabusmauRows.value,
        linkParentValues: syllabusMonHocScopeParent.value,
        savedIds: layIdsCotDiemMauDaGanSyllabus(cotDiemSyllabusmauRows.value, syllabusMonHocScopeParent.value.syllabusMonHocId),
        parentText: `${syllabusMonParentText.value} | Đây là cột điểm lấy từ syllabus mẫu. Tích chọn nhiều dòng rồi bấm Gán cột điểm đã tích chọn, hoặc bấm Gán vào syllabus trên từng dòng để copy thành Cột điểm của syllabus áp dụng.`,
        readOnly: true,
        canSelect: false,
        multiSelect: false,
        canToggleSave: true,
        canShowSavedStatus: true,
        toggleHandler: ganCotDiemMauVaoSyllabus,
        toggleSaveLabel: 'Gán vào syllabus',
        toggleSavedLabel: 'Bỏ gán',
        statusSavedLabel: '✓ Đã gán',
        statusUnsavedLabel: '+ Chưa gán',
        title: '1. Cột điểm của syllabus mẫu',
        description: 'Nguồn cột điểm lấy từ cau_hinh_danh_gia_mau của syllabus mẫu, không lấy từ bảng quy_doi_diem_mau.',
        tableTitle: 'Cột điểm của syllabus mẫu',
        emptyText: 'Syllabus mẫu chưa có cột điểm để copy.',
        disabled: !selected.syllabusMonHoc,
        disabledText: 'Cần chọn Syllabus môn học áp dụng ở tầng 9 trước.',
        columns: [
          {key: 'tenSyllabusmauHienThi', label: 'Syllabus mẫu'},
          {key: 'tenCotDiem', label: 'Tên cột điểm'},
          {key: 'loaiDiemHienThi', label: 'Loại điểm'},
          {key: 'tyLe', label: 'Tỷ lệ %'},
          {key: 'diemToiDa', label: 'Điểm tối đa'},
          {key: 'thuTu', label: 'Thứ tự'},
          {key: 'batBuoc', label: 'Bắt buộc'},
          {key: 'ghiChu', label: 'Ghi chú'}
        ]
      }),
      taoBang('cauHinhDanhGia', {
        parentValues: syllabusMonHocScopeParent.value,
        filterValues: syllabusMonHocScopeFilter.value,
        displayRows: cotDiemSyllabusApDungRows.value,
        forceFilter: true,
        resetAfterSave: false,
        parentText: `${syllabusMonParentText.value} | Đây là cột điểm thật sự của syllabus, chỉ gồm thông tin cột điểm: tên, loại điểm, tỷ lệ, điểm tối đa, thứ tự, bắt buộc.`,
        readOnly: false,
        canSelect: false,
        canToggleSave: false,
        readonlyInfoFields: taoOThongTinSyllabusMonHoc(),
        title: '2. Cột điểm của syllabus',
        description: 'Dùng để tính điểm tổng kết môn theo syllabus. Không chứa ngưỡng quy đổi/kết quả A-B-C/Đạt-Không đạt.',
        tableTitle: 'Cột điểm đang áp dụng cho syllabus',
        emptyText: 'Chưa có cột điểm nào cho syllabus này.',
        disabled: !selected.syllabusMonHoc,
        disabledText: 'Cần chọn Syllabus môn học áp dụng ở tầng 9 trước.',
        columns: [
          {key: 'tenSyllabusmauHienThi', label: 'Syllabus mẫu'},
          {key: 'tenCotDiem', label: 'Tên cột điểm'},
          {key: 'loaiDiemHienThi', label: 'Loại điểm'},
          {key: 'tyLe', label: 'Tỷ lệ %'},
          {key: 'diemToiDa', label: 'Điểm tối đa'},
          {key: 'thuTu', label: 'Thứ tự'},
          {key: 'batBuoc', label: 'Bắt buộc'},
          {key: 'ghiChu', label: 'Ghi chú'}
        ]
      }),
      taoBang('quyDoiDiemMau', {
        displayRows: quyDoiKetQuaDungChungRows.value,
        linkParentValues: syllabusMonHocScopeParent.value,
        parentText: `${syllabusMonParentText.value} | Đây là mẫu quy đổi kết quả thuộc syllabus mẫu của syllabus đang chọn. BE chỉ trả dữ liệu theo syllabusMonHocMauId, không trả toàn ngành.`,
        readOnly: false,
        canSelect: false,
        canToggleSave: true,
        canShowSavedStatus: true,
        toggleSaveLabel: 'Gán vào syllabus',
        toggleSavedLabel: 'Bỏ gán',
        statusSavedLabel: '✓ Đã gán',
        statusUnsavedLabel: '+ Chưa gán',
        title: '3. Mẫu quy đổi kết quả theo syllabus mẫu',
        description: 'Mẫu quy đổi kết quả thuộc syllabus mẫu đang chọn. Mẫu này dùng để quy đổi điểm tổng kết sau khi đã tính từ cột điểm.',
        tableTitle: 'Mẫu quy đổi kết quả theo syllabus mẫu',
        emptyText: 'Chưa có mẫu quy đổi kết quả cho syllabus mẫu của syllabus đang chọn.',
        disabled: !selected.syllabusMonHoc,
        disabledText: 'Cần chọn Syllabus môn học áp dụng ở tầng 9 trước.'
      }),
      taoBang('chuongTrinhMonQuyDoiDiemMau', {
        parentValues: syllabusMonHocScopeParent.value,
        filterValues: syllabusMonHocScopeFilter.value,
        displayRows: mauQuyDoiKetQuaDaGanSyllabusRows.value,
        forceFilter: true,
        readOnly: true,
        canSelect: false,
        canToggleSave: false,
        readonlyInfoFields: taoOThongTinSyllabusMonHoc(),
        parentText: syllabusMonParentText.value,
        title: '4. Mẫu quy đổi kết quả đã gán vào syllabus',
        description: 'Danh sách mẫu quy đổi kết quả đã gán cho syllabus đang chọn. Bảng này không sinh/copy dữ liệu sang cột điểm.',
        tableTitle: 'Mẫu quy đổi kết quả đã gán cho syllabus',
        emptyText: 'Chưa có mẫu quy đổi kết quả nào được gán cho syllabus này.',
        disabled: !selected.syllabusMonHoc,
        disabledText: 'Cần chọn Syllabus môn học áp dụng ở tầng 9 trước.'
      }),
      taoBang('syllabusMonHocmauChuongBai', {
        linkParentValues: syllabusMonParent.value,
        parentValues: {syllabusMonHocMauId: selected.syllabusMonHoc?.syllabusMonHocMauId || selected.syllabusMonHocmau?.id || null},
        filterValues: {syllabusMonHocMauId: selected.syllabusMonHoc?.syllabusMonHocMauId || selected.syllabusMonHocmau?.id || null},
        parentText: `${syllabusMonParentText.value} | Chỉ hiển thị chương/bài mẫu thuộc đúng syllabus mẫu của môn đang chọn.`,
        title: 'Chương/bài mẫu của syllabus - TÁI SỬ DỤNG',
        description: 'Dữ liệu mẫu chỉ để chọn/copy xuống chương/bài áp dụng. CRUD chương/bài mẫu thực hiện ở tab Syllabus môn học mẫu.',
        tableTitle: 'Chương/bài mẫu của syllabus - TÁI SỬ DỤNG',
        toggleSaveLabel: 'Gán vào',
        toggleSavedLabel: 'Bỏ gán',
        statusSavedLabel: '✓ Đã gán',
        statusUnsavedLabel: '+ Chưa gán',
        canToggleSave: true,
        canSelect: false,
        forceFilter: true,
        readOnly: true,
        disabled: !selected.syllabusMonHoc,
        disabledText: 'Cần chọn Syllabus môn học áp dụng ở tầng 9 trước.'
      }),
      taoBang('syllabusChuongBai', {
        parentValues: syllabusMonParent.value,
        filterValues: syllabusMonFilter.value,
        forceFilter: true,
        parentText: syllabusMonParentText.value,
        title: 'Chương/bài đã lưu trong syllabus áp dụng',
        description: 'Bảng hứng chương/bài đã copy từ chương/bài mẫu, được lưu riêng cho syllabus môn học áp dụng và có thể chỉnh nhẹ theo chương trình.',
        tableTitle: 'Chương/bài đã lưu trong syllabus áp dụng',
        emptyText: 'Chưa có chương/bài nào trong syllabus này. Hãy bấm Gán vào ở bảng chương/bài mẫu phía trên.',
        disabled: !selected.syllabusMonHoc,
        disabledText: 'Cần chọn Syllabus môn học áp dụng ở tầng 9 trước.'
      }),
      taoBang('dieuKienMonHocmau', {
        linkParentValues: syllabusMonParent.value,
        parentValues: {syllabusMonHocMauId: selected.syllabusMonHoc?.syllabusMonHocMauId || selected.syllabusMonHocmau?.id || null},
        filterValues: {syllabusMonHocMauId: selected.syllabusMonHoc?.syllabusMonHocMauId || selected.syllabusMonHocmau?.id || null},
        displayRows: dieuKienMonHocmauTheoSyllabus.value,
        parentText: `${syllabusMonParentText.value} | Chỉ hiển thị điều kiện mẫu thuộc đúng syllabus mẫu của môn đang chọn.`,
        title: 'Điều kiện môn học mẫu - TÁI SỬ DỤNG',
        description: 'Dữ liệu mẫu chỉ để chọn/copy xuống điều kiện áp dụng. CRUD điều kiện mẫu thực hiện ở tab Syllabus môn học mẫu.',
        tableTitle: 'Điều kiện môn học mẫu - TÁI SỬ DỤNG',
        toggleSaveLabel: 'Gán vào',
        toggleSavedLabel: 'Bỏ gán',
        statusSavedLabel: '✓ Đã gán',
        statusUnsavedLabel: '+ Chưa gán',
        canToggleSave: true,
        canSelect: false,
        forceFilter: true,
        readOnly: true,
        columns: [
          {key: 'ma', label: 'Mã điều kiện'},
          {key: 'loaiHienThi', label: 'Loại điều kiện'},
          {key: 'noiDung', label: 'Nội dung'},
              {key: 'ghiChu', label: 'Ghi chú'},
          {key: 'createdAt', label: 'Ngày tạo'},
          {key: 'updatedAt', label: 'Ngày cập nhật'}
        ],
        disabled: !selected.syllabusMonHoc,
        disabledText: 'Cần chọn Syllabus môn học áp dụng ở tầng 9 trước.'
      }),
      taoBang('dieuKienMonHoc', {
        parentValues: syllabusMonParent.value,
        filterValues: syllabusMonFilter.value,
        parentText: syllabusMonParentText.value,
        title: 'Điều kiện môn học đã lưu vào syllabus',
        description: 'Các điều kiện thực hiện môn học đã được lưu riêng cho syllabus môn học áp dụng.',
        tableTitle: 'Điều kiện môn học đã lưu vào syllabus',
        emptyText: 'Chưa có điều kiện môn học nào được lưu vào syllabus này.',
        disabled: !selected.syllabusMonHoc,
        disabledText: 'Cần chọn Syllabus môn học áp dụng ở tầng 9 trước.'
      }),
      taoBang('syllabusMonHocmauTaiLieu', {
        linkParentValues: syllabusMonParent.value,
        parentValues: {syllabusMonHocMauId: selected.syllabusMonHoc?.syllabusMonHocMauId || selected.syllabusMonHocmau?.id || null},
        filterValues: {syllabusMonHocMauId: selected.syllabusMonHoc?.syllabusMonHocMauId || selected.syllabusMonHocmau?.id || null},
        parentText: `${syllabusMonParentText.value} | Chỉ hiển thị tài liệu mẫu thuộc đúng syllabus mẫu của môn đang chọn.`,
        title: 'Tài liệu mẫu của syllabus - TÁI SỬ DỤNG',
        description: 'Dữ liệu mẫu chỉ để chọn/copy xuống tài liệu áp dụng. CRUD tài liệu mẫu thực hiện ở tab Syllabus môn học mẫu.',
        tableTitle: 'Tài liệu mẫu của syllabus - TÁI SỬ DỤNG',
        toggleSaveLabel: 'Gán vào',
        toggleSavedLabel: 'Bỏ gán',
        statusSavedLabel: '✓ Đã gán',
        statusUnsavedLabel: '+ Chưa gán',
        canToggleSave: true,
        canSelect: false,
        forceFilter: true,
        readOnly: true,
        disabled: !selected.syllabusMonHoc,
        disabledText: 'Cần chọn Syllabus môn học áp dụng ở tầng 9 trước.'
      }),
      taoBang('syllabusTaiLieu', {
        parentValues: syllabusMonParent.value,
        filterValues: syllabusMonFilter.value,
        parentText: syllabusMonParentText.value,
        title: 'Tài liệu đã lưu vào syllabus',
        description: 'Tài liệu đã được lưu riêng cho syllabus môn học áp dụng. File được upload vào app, không nhập đường dẫn thủ công.',
        tableTitle: 'Tài liệu đã lưu vào syllabus',
        emptyText: 'Chưa có tài liệu nào được lưu vào syllabus này.',
        uploadFile: uploadFileTaiLieu,
        disabled: !selected.syllabusMonHoc,
        disabledText: 'Cần chọn Syllabus môn học áp dụng ở tầng 9 trước.'
      })
    ]
  }]
})

const configs = {
  nganh: {
    title: 'Ngành',
    description: 'Tạo hoặc chọn ngành làm cha đầu tiên của chương trình.',
    defaultForm: {maNganh: '', tenNganh: '', moTa: ''},
    fields: [
      {key: 'maNganh', label: 'Mã ngành', required: true},
      {key: 'tenNganh', label: 'Tên ngành', required: true},
      {key: 'moTa', label: 'Mô tả', type: 'textarea', wide: true}
    ],
    uniqueRules: [
      {field: 'maNganh', message: 'Mã ngành đã tồn tại.'}
    ],
    columns: [
      {key: 'maNganh', label: 'Mã ngành'},
      {key: 'tenNganh', label: 'Tên ngành'},
      {key: 'moTa', label: 'Mô tả'},
      {key: 'createdAt', label: 'Ngày tạo'},
      {key: 'updatedAt', label: 'Ngày cập nhật'}
    ]
  },
  trinhDoDaoTao: {
    title: 'Trình độ đào tạo',
    defaultForm: {maTrinhDo: '', tenTrinhDo: '', moTa: ''},
    fields: [
      {key: 'maTrinhDo', label: 'Mã trình độ', required: true},
      {key: 'tenTrinhDo', label: 'Tên trình độ', required: true},
      {key: 'moTa', label: 'Mô tả', type: 'textarea', wide: true}
    ],
    uniqueRules: [
      {field: 'maTrinhDo', message: 'Mã trình độ đã tồn tại.'}
    ],
    columns: [
      {key: 'maTrinhDo', label: 'Mã'},
      {key: 'tenTrinhDo', label: 'Tên trình độ'},
      {key: 'moTa', label: 'Mô tả'}
    ]
  },
  loaiChuongTrinh: {
    title: 'Loại chương trình',
    defaultForm: {maLoai: '', tenLoai: '', soThang: null, soKy: null, moTa: ''},
    fields: [
      {key: 'maLoai', label: 'Mã loại', required: true},
      {key: 'tenLoai', label: 'Tên loại', required: true},
      {key: 'soThang', label: 'Số tháng', type: 'number'},
      {key: 'soKy', label: 'Số kỳ', type: 'number'},
      {key: 'moTa', label: 'Mô tả', type: 'textarea', wide: true}
    ],
    uniqueRules: [
      {field: 'maLoai', message: 'Mã loại chương trình đã tồn tại.'}
    ],
    columns: [
      {key: 'maLoai', label: 'Mã'},
      {key: 'tenLoai', label: 'Tên loại'},
      {key: 'soThang', label: 'Số tháng'},
      {key: 'soKy', label: 'Số kỳ'},
      {key: 'moTa', label: 'Mô tả'}
    ]
  },
  chuongTrinh: {
    title: 'Chương trình đào tạo',
    defaultForm: {
      nganhHeDaoTaoId: null,
      nganhLoaiChuongTrinhId: null,
      nganhId: null,
      maChuongTrinh: '',
      tenChuongTrinh: '',
      doiTuongTuyenSinh: '',
      thoiGianDaoTao: ''
    },
    fields: [
      {
        key: 'nganhHeDaoTaoId',
        label: 'Ngành hệ đào tạo',
        required: true,
        type: 'select',
        lookup: 'nganhHeDaoTao',
        labelKey: ['tenHe', 'label'],
        locked: true
      },
      {key: 'nganhLoaiChuongTrinhId', required: false, hidden: true},
      {key: 'nganhId', required: false, hidden: true},
      {key: 'maChuongTrinh', label: 'Mã CTĐT', required: true},
      {key: 'tenChuongTrinh', label: 'Tên CTĐT', required: true},
      {key: 'doiTuongTuyenSinh', label: 'Đối tượng tuyển sinh'},
      {key: 'thoiGianDaoTao', label: 'Thời gian đào tạo'}
    ],
    uniqueRules: [
      {field: 'maChuongTrinh', message: 'Mã chương trình đào tạo đã tồn tại.'}
    ],
    columns: [
      {key: 'maChuongTrinh', label: 'Mã CTĐT'},
      {key: 'tenChuongTrinh', label: 'Tên chương trình'},
      {key: 'tenNganh', label: 'Ngành'},
      {key: 'tenHe', label: 'Ngành hệ đào tạo'}
    ]
  },
  chuongTrinhVersion: {
    title: ' Phiên bản chương trình',
    defaultForm: {
      chuongTrinhId: null,
      maVersion: '',
      tenVersion: '',
      ngayApDung: '',
      ngayHetHieuLuc: '',
      soQuyetDinh: '',
      ngayQuyetDinh: '',
      nguoiKy: '',
      coQuanBanHanh: '',
      fileQuyetDinh: '',
      tongTinChi: null,
      tongSoGio: null,
      tongGioLyThuyet: null,
      tongGioThucHanh: null,
      tongGioKiemTra: null,
      trangThai: 'DANG_SOAN',
      laHienHanh: false
    },
    fields: [
      {
        key: 'chuongTrinhId',
        required: true,
        label: 'Chương trình',
        type: 'select',
        lookup: 'chuongTrinh',
        labelKey: ['maChuongTrinh', 'tenChuongTrinh'],
        locked: true
      },
      {key: 'maVersion', label: 'Mã version', required: true},
      {key: 'tenVersion', label: 'Tên version', required: true},
      {
        key: 'ngayApDung',
        label: 'Ngày áp dụng',
        type: 'date',
        validate: (value, form) => {
          if (value && form.ngayHetHieuLuc && value > form.ngayHetHieuLuc)
            return 'Ngày áp dụng không được sau ngày hết hiệu lực.'
          return null
        }
      },
      {
        key: 'ngayHetHieuLuc',
        label: 'Ngày hết hiệu lực',
        type: 'date',
        validate: (value, form) => {
          if (value && form.ngayApDung && value < form.ngayApDung)
            return 'Ngày hết hiệu lực không được trước ngày áp dụng.'
          return null
        }
      },
      {key: 'soQuyetDinh', label: 'Số quyết định'},
      {key: 'ngayQuyetDinh', label: 'Ngày quyết định', type: 'date'},
      {key: 'nguoiKy', label: 'Người ký'},
      {key: 'coQuanBanHanh', label: 'Cơ quan ban hành'},

      {
        key: 'trangThai',
        label: 'Trạng thái',
        type: 'select',
        lookup: 'trangThaiChuongTrinhVersion',
        labelKey: ['ten']
      },

      {key: 'tongTinChi', label: 'Tổng tín chỉ', type: 'number', max: 9999.9, step: 0.1, locked: true},
      {key: 'tongSoGio', label: 'Tổng giờ', type: 'number', max: 999999.9, step: 0.1, locked: true},
      {key: 'tongGioLyThuyet', label: 'Giờ LT', type: 'number', max: 999999.9, step: 0.1, locked: true},
      {key: 'tongGioThucHanh', label: 'Giờ TH', type: 'number', max: 999999.9, step: 0.1, locked: true},
      {key: 'tongGioKiemTra', label: 'Giờ KT', type: 'number', max: 999999.9, step: 0.1, locked: true},
      {key: 'laHienHanh', label: 'Hiện hành', type: 'boolean', locked: true}
    ],
    columns: [
      {key: 'maVersion', label: 'Mã version'},
      {key: 'tenVersion', label: 'Tên version'},
      {key: 'tenChuongTrinh', label: 'Chương trình'},
      {key: 'ngayApDung', label: 'Ngày áp dụng'},
      {key: 'ngayHetHieuLuc', label: 'Ngày hết hiệu lực'},
      {key: 'trangThaiHienThi', label: 'Trạng thái', width: '120px'},
      {key: 'laHienHanh', label: 'Hiện hành', width: '90px'},
      {key: 'conHieuLucTheoNgay', label: 'Còn hiệu lực', width: '100px'},
      {key: 'duocPhepChinhSua', label: 'Được sửa', width: '90px'},
      {key: 'duocPhepVanHanh', label: 'Được vận hành', width: '110px'},
      {key: 'tongTinChi', label: 'Tín chỉ'},
      {key: 'tongSoGio', label: 'Tổng giờ'},
      {key: 'tongGioLyThuyet', label: 'Giờ LT'},
      {key: 'tongGioThucHanh', label: 'Giờ TH'},
      {key: 'tongGioKiemTra', label: 'Giờ KT'},
      {key: 'soMonChuaCoSyllabus', label: 'Môn thiếu SB', width: '100px'},
      {key: 'canhBaoTongHop', label: 'Cảnh báo'},
      {key: 'lyDoTrangThai', label: 'Lý do trạng thái'}
    ]
  },
  mucTieuChuongTrinhmau: {
    title: 'Mục tiêu chương trình mẫu',
    description: 'Kho mục tiêu mẫu dùng chung. Khi bấm ✓, hệ thống gán mục tiêu mẫu vào Version qua bảng chuong_trinh_version_muc_tieu.',
    defaultForm: {ma: '', loai: 'chung', noiDung: '', ghiChu: ''},
    fields: [
      {key: 'ma', label: 'Mã mục tiêu', required: true},
      {key: 'loai', label: 'Loại', type: 'select', lookup: 'loaiMucTieu', required: true},
      {key: 'noiDung', label: 'Nội dung', type: 'textarea', required: true, wide: true},
      {key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true}
    ],
    uniqueRules: [
      {field: 'ma', message: 'Mã mục tiêu mẫu đã tồn tại.'}
    ],
    columns: [
      {key: 'ma', label: 'Mã'},
      {key: 'loaiHienThi', label: 'Loại'},
      {key: 'noiDung', label: 'Nội dung'},
      {key: 'ghiChu', label: 'Ghi chú'},
      {key: 'createdAt', label: 'Ngày tạo'},
      {key: 'updatedAt', label: 'Ngày cập nhật'}
    ]
  },
  mucTieuChuongTrinh: {
    title: ' Mục tiêu chương trình đã lưu vào Version',
    description: 'Bảng mục tiêu chương trình đã lưu theo Version.',
    defaultForm: {
      chuongTrinhVersionId: null,
      ma: '',
      loai: 'chung',
      noiDung: '',
      thuTu: null,
      ghiChu: ''
    },
    fields: [
      {
        key: 'chuongTrinhVersionId',
        label: 'Version',
        type: 'select',
        lookup: 'chuongTrinhVersion',
        labelKey: ['maVersion', 'tenVersion'],
        locked: true
      },
      {key: 'ma', label: 'Mã mục tiêu mẫu'},
      {key: 'loai', label: 'Loại', type: 'select', lookup: 'loaiMucTieu', required: true},
      {key: 'noiDung', label: 'Nội dung', type: 'textarea', required: true, wide: true},
      {key: 'thuTu', label: 'Thứ tự', type: 'number', step: 1},
      {key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true}
    ],
    columns: [
      {key: 'tenVersion', label: 'Version'},
      {key: 'ma', label: 'Mã mục tiêu mẫu'},
      {key: 'loaiHienThi', label: 'Loại'},
      {key: 'noiDung', label: 'Nội dung'},
      {key: 'ghiChu', label: 'Ghi chú'},
      {key: 'createdAt', label: 'Ngày tạo'},
      {key: 'updatedAt', label: 'Ngày cập nhật'}
    ]
  },
  nangLucDauRamau: {
    title: ' Năng lực đầu ra mẫu',
    description: 'Kho năng lực mẫu dùng chung. Khi bấm ✓, hệ thống gán năng lực mẫu vào Version qua bảng chuong_trinh_version_nang_luc.',
    defaultForm: {ma: '', loai: 'co_ban', noiDung: '', ghiChu: ''},
    fields: [
      {key: 'ma', label: 'Mã năng lực', required: true},
      {key: 'loai', label: 'Loại', type: 'select', lookup: 'loaiNangLuc', required: true},
      {key: 'noiDung', label: 'Nội dung', type: 'textarea', required: true, wide: true},
      {key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true}
    ],
    uniqueRules: [
      {field: 'ma', scopeKeys: ['loai'], message: 'Mã năng lực mẫu đã tồn tại với loại năng lực này.'},
      {field: 'loai', scopeKeys: ['ma'], message: 'Loại năng lực đã tồn tại với mã năng lực này.'}
    ],
    columns: [
      {key: 'ma', label: 'Mã'},
      {key: 'loaiHienThi', label: 'Loại'},
      {key: 'noiDung', label: 'Nội dung'},

      {key: 'ghiChu', label: 'Ghi chú'},
      {key: 'createdAt', label: 'Ngày tạo'},
      {key: 'updatedAt', label: 'Ngày cập nhật'}
    ]
  },
  nangLucDauRa: {
    title: ' Năng lực đầu ra nhập riêng - KHÔNG XÀI mẫu',
    description: 'Bảng nhập riêng trực tiếp theo Version/chương trình, không lấy từ bảng mẫu và không dùng cơ chế tái sử dụng.',
    defaultForm: {chuongTrinhVersionId: null, ma: '', loai: 'co_ban', noiDung: '', thuTu: null, ghiChu: ''},
    fields: [
      {
        key: 'chuongTrinhVersionId',
        label: 'Version',
        type: 'select',
        lookup: 'chuongTrinhVersion',
        labelKey: ['maVersion', 'tenVersion'],
        locked: true
      },
      {key: 'ma', label: 'Mã năng lực', required: true},
      {key: 'loai', label: 'Loại', type: 'select', lookup: 'loaiNangLuc', required: true},
      {key: 'noiDung', label: 'Nội dung', type: 'textarea', required: true, wide: true},
      {key: 'thuTu', label: 'Thứ tự', type: 'number', step: 1},
      {key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true}
    ],
    columns: [
      {key: 'tenVersion', label: 'Version'},
      {key: 'ma', label: 'Mã'},
      {key: 'loaiHienThi', label: 'Loại'},
      {key: 'noiDung', label: 'Nội dung'},
      {key: 'ghiChu', label: 'Ghi chú'}
    ]
  },
  viTriViecLammau: {
    title: 'Vị trí việc làm mẫu',
    description: 'Kho vị trí việc làm mẫu dùng chung. Khi bấm ✓, hệ thống gán vị trí vào Version qua bảng chuong_trinh_version_vi_tri_viec_lam.',
    defaultForm: {ma: '', ten: '', moTa: '', ghiChu: ''},
    fields: [
      {key: 'ma', label: 'Mã vị trí', required: true},
      {key: 'ten', label: 'Tên vị trí', required: true},
      {key: 'moTa', label: 'Mô tả', type: 'textarea', wide: true},
      {key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true}
    ],
    uniqueRules: [
      {field: 'ma', message: 'Mã vị trí việc làm mẫu đã tồn tại.'}
    ],
    columns: [
      {key: 'ma', label: 'Mã'},
      {key: 'ten', label: 'Vị trí'},
      {key: 'moTa', label: 'Mô tả'},
      {key: 'ghiChu', label: 'Ghi chú'},
      {key: 'createdAt', label: 'Ngày tạo'},
      {key: 'updatedAt', label: 'Ngày cập nhật'}
    ]
  },

  viTriViecLam: {
    title: ' Vị trí việc làm đã lưu vào Version',
    description: 'Bảng vị trí việc làm đã lưu theo Version.',
    defaultForm: {
      chuongTrinhVersionId: null,
      ma: '',
      ten: '',
      moTa: '',
      thuTu: null,
      ghiChu: ''
    },
    fields: [
      {
        key: 'chuongTrinhVersionId',
        label: 'Version',
        type: 'select',
        lookup: 'chuongTrinhVersion',
        labelKey: ['maVersion', 'tenVersion'],
        locked: true
      },
      {key: 'ma', label: 'Mã vị trí'},
      {key: 'ten', label: 'Tên vị trí', required: true},
      {key: 'moTa', label: 'Mô tả', type: 'textarea', wide: true},
      {key: 'thuTu', label: 'Thứ tự', type: 'number', step: 1},
      {key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true}
    ],
    columns: [
      {key: 'tenVersion', label: 'Version'},
      {key: 'ma', label: 'Mã'},
      {key: 'ten', label: 'Vị trí'},
      {key: 'moTa', label: 'Mô tả'},
      {key: 'ghiChu', label: 'Ghi chú'},
      {key: 'createdAt', label: 'Ngày tạo'},
      {key: 'updatedAt', label: 'Ngày cập nhật'}
    ]
  }, dieuKienTotNghiepmau: {
    title: 'Điều kiện tốt nghiệp mẫu',
    description: 'Kho điều kiện tốt nghiệp mẫu dùng chung. Bấm Gán vào để copy điều kiện mẫu sang bảng Điều kiện tốt nghiệp đã lưu vào Version.',
    defaultForm: {
      ma: '',
      noiDung: '',
      ghiChu: ''
    },
    fields: [
      {key: 'ma', label: 'Mã điều kiện', required: true},
      {key: 'noiDung', label: 'Nội dung', type: 'textarea', required: true, wide: true},
      {key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true}
    ],
    uniqueRules: [
      {field: 'ma', message: 'Mã điều kiện tốt nghiệp mẫu đã tồn tại.'}
    ],
    columns: [
      {key: 'ma', label: 'Mã'},
      {key: 'noiDung', label: 'Nội dung'},
      {key: 'ghiChu', label: 'Ghi chú'},
      {key: 'createdAt', label: 'Ngày tạo'},
      {key: 'updatedAt', label: 'Ngày cập nhật'}
    ]
  },

  dieuKienTotNghiep: {
    title: '19. Điều kiện tốt nghiệp đã lưu vào Version',
    description: 'Bảng điều kiện tốt nghiệp đã được copy từ điều kiện tốt nghiệp mẫu/mẫu vào Version.',
    defaultForm: {
      chuongTrinhVersionId: null,
      ma: '',
      noiDung: '',
      thuTu: null,
      ghiChu: ''
    },
    fields: [
      {
        key: 'chuongTrinhVersionId',
        label: 'Version',
        type: 'select',
        lookup: 'chuongTrinhVersion',
        labelKey: ['maVersion', 'tenVersion'],
        locked: true
      },
      {key: 'ma', label: 'Mã điều kiện mẫu'},
      {key: 'noiDung', label: 'Nội dung', type: 'textarea', required: true, wide: true},
      {key: 'thuTu', label: 'Thứ tự', type: 'number', step: 1},
      {key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true}
    ],
    columns: [
      {key: 'tenVersion', label: 'Version'},
      {key: 'ma', label: 'Mã điều kiện mẫu'},
      {key: 'noiDung', label: 'Nội dung'},
      {key: 'ghiChu', label: 'Ghi chú'},
      {key: 'createdAt', label: 'Ngày tạo'},
      {key: 'updatedAt', label: 'Ngày cập nhật'}
    ]
  },
  chuongTrinhVersionMucTieu: {
    title: 'Mục tiêu gán vào Version - TÁI SỬ DỤNG',
    description: 'Bảng nối dùng để chọn mục tiêu mẫu và gán/tái sử dụng vào Version, không phải bảng nhập riêng.',
    defaultForm: {chuongTrinhVersionId: null, mucTieumauId: null, thuTu: null, ghiChu: ''},
    fields: [{
      key: 'chuongTrinhVersionId',
      label: 'Version',
      type: 'select',
      lookup: 'chuongTrinhVersion',
      labelKey: ['maVersion', 'tenVersion'],
      locked: true
    }, {
      key: 'mucTieumauId',
      label: 'Mục tiêu mẫu',
      type: 'select',
      lookup: 'mucTieuChuongTrinhmau',
      labelKey: ['ma', 'noiDung'],
      required: true
    }, {key: 'thuTu', label: 'Thứ tự', type: 'number', step: 1}, {
      key: 'ghiChu',
      label: 'Ghi chú',
      type: 'textarea',
      wide: true
    }],
    columns: [{key: 'tenVersion', label: 'Version'}, {key: 'tenMucTieumau', label: 'Mục tiêu mẫu'}, {
      key: 'thuTu',
      label: 'Thứ tự'
    }, {key: 'ghiChu', label: 'Ghi chú'}]
  },
  chuongTrinhVersionNangLuc: {
    title: 'Năng lực gán vào Version - TÁI SỬ DỤNG',
    description: 'Bảng nối dùng để chọn năng lực mẫu và gán/tái sử dụng vào Version, không phải bảng nhập riêng.',
    defaultForm: {chuongTrinhVersionId: null, nangLucmauId: null, thuTu: null, ghiChu: ''},
    fields: [{
      key: 'chuongTrinhVersionId',
      label: 'Version',
      type: 'select',
      lookup: 'chuongTrinhVersion',
      labelKey: ['maVersion', 'tenVersion'],
      locked: true
    }, {
      key: 'nangLucmauId',
      label: 'Năng lực mẫu',
      type: 'select',
      lookup: 'nangLucDauRamau',
      labelKey: ['ma', 'noiDung'],
      required: true
    }, {key: 'thuTu', label: 'Thứ tự', type: 'number', step: 1}, {
      key: 'ghiChu',
      label: 'Ghi chú',
      type: 'textarea',
      wide: true
    }],
    columns: [{key: 'tenVersion', label: 'Version'}, {key: 'tenNangLucmau', label: 'Năng lực mẫu'}, {
      key: 'thuTu',
      label: 'Thứ tự'
    }, {key: 'ghiChu', label: 'Ghi chú'}]
  },
  chuongTrinhVersionViTriViecLam: {
    title: 'Vị trí việc làm gán vào Version - TÁI SỬ DỤNG',
    description: 'Bảng nối dùng để chọn vị trí việc làm mẫu và gán/tái sử dụng vào Version, không phải bảng nhập riêng.',
    defaultForm: {chuongTrinhVersionId: null, viTrimauId: null, thuTu: null, ghiChu: ''},
    fields: [{
      key: 'chuongTrinhVersionId',
      label: 'Version',
      type: 'select',
      lookup: 'chuongTrinhVersion',
      labelKey: ['maVersion', 'tenVersion'],
      locked: true
    }, {
      key: 'viTrimauId',
      label: 'Vị trí mẫu',
      type: 'select',
      lookup: 'viTriViecLammau',
      labelKey: ['ma', 'ten'],
      required: true
    }, {key: 'thuTu', label: 'Thứ tự', type: 'number', step: 1}, {
      key: 'ghiChu',
      label: 'Ghi chú',
      type: 'textarea',
      wide: true
    }],
    columns: [{key: 'tenVersion', label: 'Version'}, {key: 'tenViTrimau', label: 'Vị trí mẫu'}, {
      key: 'thuTu',
      label: 'Thứ tự'
    }, {key: 'ghiChu', label: 'Ghi chú'}]
  },
  chuongTrinhVersionDieuKienTotNghiep: {
    title: 'Điều kiện tốt nghiệp gán vào Version - TÁI SỬ DỤNG',
    description: 'Bảng nối dùng để chọn điều kiện tốt nghiệp mẫu và gán/tái sử dụng vào Version, không phải bảng nhập riêng.',
    defaultForm: {chuongTrinhVersionId: null, dieuKienmauId: null, thuTu: null, ghiChu: ''},
    fields: [{
      key: 'chuongTrinhVersionId',
      label: 'Version',
      type: 'select',
      lookup: 'chuongTrinhVersion',
      labelKey: ['maVersion', 'tenVersion'],
      locked: true
    }, {
      key: 'dieuKienmauId',
      label: 'Điều kiện mẫu',
      type: 'select',
      lookup: 'dieuKienTotNghiepmau',
      labelKey: ['ma', 'noiDung'],
      required: true
    }, {key: 'thuTu', label: 'Thứ tự', type: 'number', step: 1}, {
      key: 'ghiChu',
      label: 'Ghi chú',
      type: 'textarea',
      wide: true
    }],
    columns: [{key: 'tenVersion', label: 'Version'}, {key: 'tenDieuKienmau', label: 'Điều kiện mẫu'}, {
      key: 'thuTu',
      label: 'Thứ tự'
    }, {key: 'ghiChu', label: 'Ghi chú'}]
  },
  syllabusChuongTrinhmau: {
    title: 'Syllabus chương trình mẫu',
    description: 'Bảng mẫu/mẫu dùng chung để chọn, tái sử dụng.',
    defaultForm: {
      ma: '',
      ten: '',
      mucTieu: '',
      doiTuongTuyenSinh: '',
      thoiGianDaoTao: '',
      khoiLuongKienThuc: '',
      dieuKienTotNghiep: '',
      phuongPhapDaoTao: '',
      phuongPhapDanhGia: '',
      huongDanThucHien: '',
      duongDan: '',
      ghiChu: ''
    },
    fields: [{key: 'ma', label: 'Mã', required: true}, {key: 'ten', label: 'Tên', required: true}, {
      key: 'mucTieu',
      label: 'Mục tiêu',
      type: 'textarea',
      wide: true
    }, {key: 'doiTuongTuyenSinh', label: 'Đối tượng tuyển sinh'}, {
      key: 'thoiGianDaoTao',
      label: 'Thời gian đào tạo'
    }, {key: 'khoiLuongKienThuc', label: 'Khối lượng kiến thức'}, {
      key: 'dieuKienTotNghiep',
      label: 'Điều kiện tốt nghiệp',
      type: 'textarea',
      wide: true
    }, {key: 'phuongPhapDaoTao', label: 'Phương pháp đào tạo', type: 'textarea', wide: true}, {
      key: 'phuongPhapDanhGia',
      label: 'Phương pháp đánh giá',
      type: 'textarea',
      wide: true
    },
      {
        key: '__fileTaiLieu',
        label: 'File syllabus chương trình mẫu',
        type: 'file',
        themNhieuO: true,
        accept: '.pdf,.doc,.docx,.xls,.xlsx,.ppt,.pptx,.txt,.csv,.jpg,.jpeg,.png,.webp,.mp4,.mov,.avi,.mkv,.webm,.mp3,.wav,.m4a,.ogg,.zip,.rar,.7z',
        nghiepVu: 'syllabus_chuong_trinh_mau',
        tangNghiepVu: 'tang-5-syllabus-chuong-trinh-mau',
        tepNguon: 'syllabusChuongTrinhmau'
      },
      {key: 'duongDan', label: 'Đường dẫn đã lưu', locked: true, openFile: true},
      {key: 'huongDanThucHien', label: 'Hướng dẫn thực hiện', type: 'textarea', wide: true}, {
        key: 'ghiChu',
        label: 'Ghi chú',
        type: 'textarea',
        wide: true
      }],
    uniqueRules: [{field: 'ma', message: 'Mã syllabus chương trình mẫu đã tồn tại.'}],

    columns: [
      {key: 'ma', label: 'Mã'},
      {key: 'ten', label: 'Tên'},
      {key: 'mucTieu', label: 'Mục tiêu'},
      {key: 'doiTuongTuyenSinh', label: 'Đối tượng tuyển sinh'},
      {key: 'thoiGianDaoTao', label: 'Thời gian đào tạo'},
      {key: 'khoiLuongKienThuc', label: 'Khối lượng kiến thức'},
      {key: 'dieuKienTotNghiep', label: 'Điều kiện tốt nghiệp'},
      {key: 'phuongPhapDaoTao', label: 'Phương pháp đào tạo'},
      {key: 'phuongPhapDanhGia', label: 'Phương pháp đánh giá'},
      {key: 'huongDanThucHien', label: 'Hướng dẫn thực hiện'},
      {key: 'duongDan', label: 'File đã lưu', openFile: true, openFilePage: true},
      {key: 'ghiChu', label: 'Ghi chú'},
      {key: 'createdAt', label: 'Ngày tạo'},
      {key: 'updatedAt', label: 'Ngày cập nhật'}
    ]
  },
  syllabusChuongTrinh: {
    title: '4.3.2. Syllabus chương trình đã lưu vào Version',
    description: 'Bảng lưu/áp dụng đã gắn vào Version thật.',
    defaultForm: {
      chuongTrinhVersionId: null,
      syllabusChuongTrinhmauId: null,
      ma: '',
      ten: '',
      mucTieu: '',
      doiTuongTuyenSinh: '',
      thoiGianDaoTao: '',
      khoiLuongKienThuc: '',
      dieuKienTotNghiep: '',
      moTaTongQuan: '',
      mucDich: '',
      yeuCauDaoTao: '',
      phuongPhapDaoTao: '',
      phuongPhapDanhGia: '',
      huongDanThucHien: '',
      duongDan: '',
      ghiChu: ''
    },
    fields: [
      {
        key: 'chuongTrinhVersionId',
        label: 'Version',
        type: 'select',
        lookup: 'chuongTrinhVersion',
        labelKey: ['maVersion', 'tenVersion'],
        locked: true
      },
      {
        key: 'syllabusChuongTrinhmauId',
        label: 'Syllabus mẫu',
        type: 'select',
        lookup: 'syllabusChuongTrinhmau',
        labelKey: ['ma', 'ten'],
        locked: true
      },
      {key: 'ma', label: 'Mã'},
      {key: 'ten', label: 'Tên'},
      {key: 'mucTieu', label: 'Mục tiêu', type: 'textarea', wide: true},
      {key: 'doiTuongTuyenSinh', label: 'Đối tượng tuyển sinh'},
      {key: 'thoiGianDaoTao', label: 'Thời gian đào tạo'},
      {key: 'khoiLuongKienThuc', label: 'Khối lượng kiến thức'},
      {key: 'dieuKienTotNghiep', label: 'Điều kiện tốt nghiệp', type: 'textarea', wide: true},
      {key: 'moTaTongQuan', label: 'Mô tả tổng quan', type: 'textarea', wide: true},
      {key: 'mucDich', label: 'Mục đích', type: 'textarea', wide: true},
      {key: 'yeuCauDaoTao', label: 'Yêu cầu đào tạo', type: 'textarea', wide: true},
      {key: 'phuongPhapDaoTao', label: 'Phương pháp đào tạo', type: 'textarea', wide: true},
      {key: 'phuongPhapDanhGia', label: 'Phương pháp đánh giá', type: 'textarea', wide: true},
      {key: 'huongDanThucHien', label: 'Hướng dẫn thực hiện', type: 'textarea', wide: true},
      {
        key: '__fileTaiLieu',
        label: 'File syllabus chương trình',
        type: 'file',
        themNhieuO: true,
        accept: '.pdf,.doc,.docx,.xls,.xlsx,.ppt,.pptx,.txt,.csv,.jpg,.jpeg,.png,.webp,.mp4,.mov,.avi,.mkv,.webm,.mp3,.wav,.m4a,.ogg,.zip,.rar,.7z',
        nghiepVu: 'syllabus_chuong_trinh',
        tangNghiepVu: 'tang-5-syllabus-chuong-trinh-da-luu-vao-version',
        tepNguon: 'syllabusChuongTrinh'
      },
      {key: 'duongDan', label: 'Đường dẫn đã lưu', locked: true, openFile: true},
      {key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true}
    ],
    columns: [
      {key: 'tenVersion', label: 'Version'},
      {key: 'tenSyllabusChuongTrinhmau', label: 'Syllabus mẫu'},
      {key: 'ma', label: 'Mã'},
      {key: 'ten', label: 'Tên'},
      {key: 'mucTieu', label: 'Mục tiêu'},
      {key: 'doiTuongTuyenSinh', label: 'Đối tượng tuyển sinh'},
      {key: 'thoiGianDaoTao', label: 'Thời gian đào tạo'},
      {key: 'khoiLuongKienThuc', label: 'Khối lượng kiến thức'},
      {key: 'dieuKienTotNghiep', label: 'Điều kiện tốt nghiệp'},
      {key: 'moTaTongQuan', label: 'Mô tả tổng quan'},
      {key: 'mucDich', label: 'Mục đích'},
      {key: 'yeuCauDaoTao', label: 'Yêu cầu đào tạo'},
      {key: 'phuongPhapDaoTao', label: 'Phương pháp đào tạo'},
      {key: 'phuongPhapDanhGia', label: 'Phương pháp đánh giá'},
      {key: 'huongDanThucHien', label: 'Hướng dẫn thực hiện'},
      {key: 'duongDan', label: 'File đã lưu', openFile: true, openFilePage: true},
      {key: 'ghiChu', label: 'Ghi chú'},
      {key: 'createdAt', label: 'Ngày tạo'},
      {key: 'updatedAt', label: 'Ngày cập nhật'}
    ]
  },
  nhomKienThucmau: {
    title: 'Nhóm kiến thức mẫu',
    description: 'Bảng mẫu/mẫu dùng chung để chọn, tái sử dụng.',
    defaultForm: {ma: '', ten: '', loaiNhom: 'chung', moTa: ''},
    fields: [{key: 'ma', label: 'Mã nhóm mẫu', required: true}, {
      key: 'ten',
      label: 'Tên nhóm mẫu',
      required: true
    }, {key: 'loaiNhom', label: 'Loại nhóm', type: 'select', lookup: 'loaiNhom'}, {
      key: 'moTa',
      label: 'Mô tả',
      type: 'textarea',
      wide: true
    }],
    uniqueRules: [{field: 'ma', message: 'Mã nhóm kiến thức mẫu đã tồn tại.'}],
    columns: [{key: 'ma', label: 'Mã'}, {key: 'ten', label: 'Tên nhóm mẫu'}, {
      key: 'loaiNhom',
      label: 'Loại'
    }, {key: 'moTa', label: 'Mô tả'}, {key: 'createdAt', label: 'Ngày tạo'}, {key: 'updatedAt', label: 'Ngày cập nhật'}]
  },
  nhomKienThuc: {
    title: 'Nhóm kiến thức gán vào Version',
    description: 'Bảng nhóm kiến thức đã được copy từ nhóm kiến thức mẫu/mẫu vào Version.',
    defaultForm: {
      chuongTrinhVersionId: null,
      nhomKienThucmauId: null,
      ma: '',
      ten: '',
      thuTu: null,
      loaiNhom: 'chung',
      tongTinChi: null,
      tongSoGio: null,
      tongGioLyThuyet: null,
      tongGioThucHanh: null,
      tongGioKiemTra: null,
      moTa: ''
    },
    fields: [
      {
        key: 'chuongTrinhVersionId',
        label: 'Version',
        type: 'select',
        lookup: 'chuongTrinhVersion',
        labelKey: ['maVersion', 'tenVersion'],
        locked: true
      },
      {
        key: 'nhomKienThucmauId',
        label: 'Nhóm kiến thức mẫu',
        type: 'select',
        lookup: 'nhomKienThucmau',
        labelKey: ['ma', 'ten'],
        locked: true
      },
      {key: 'ma', label: 'Mã nhóm', required: true},
      {key: 'ten', label: 'Tên nhóm', required: true},
      {key: 'loaiNhom', label: 'Loại nhóm', type: 'select', lookup: 'loaiNhom'},
      {key: 'thuTu', label: 'Thứ tự', type: 'number', step: 1},
      {key: 'tongTinChi', label: 'Tổng tín chỉ', type: 'number', max: 9999.9, step: 0.1},
      {key: 'tongSoGio', label: 'Tổng giờ', type: 'number', max: 999999.9, step: 0.1},
      {key: 'tongGioLyThuyet', label: 'Giờ lý thuyết', type: 'number', max: 999999.9, step: 0.1},
      {key: 'tongGioThucHanh', label: 'Giờ thực hành', type: 'number', max: 999999.9, step: 0.1},
      {key: 'tongGioKiemTra', label: 'Giờ kiểm tra', type: 'number', max: 999999.9, step: 0.1},
      {key: 'moTa', label: 'Mô tả', type: 'textarea', wide: true}
    ],
    uniqueRules: [
      {
        field: 'ma',
        scopeKeys: ['chuongTrinhVersionId'],
        message: 'Mã nhóm kiến thức đã tồn tại trong Version đang chọn.'
      },
      {
        field: 'thuTu',
        scopeKeys: ['chuongTrinhVersionId'],
        message: 'Thứ tự nhóm kiến thức đã tồn tại trong Version đang chọn.'
      },
      {
        field: 'nhomKienThucmauId',
        scopeKeys: ['chuongTrinhVersionId'],
        message: 'Nhóm kiến thức mẫu này đã được gán vào Version đang chọn.'
      }
    ],
    columns: [
      {key: 'tenVersion', label: 'Version'},
      {key: 'tenNhomKienThucmau', label: 'Nhóm kiến thức mẫu'},
      {key: 'ma', label: 'Mã'},
      {key: 'ten', label: 'Tên nhóm'},
      {key: 'loaiNhom', label: 'Loại nhóm'},
      {key: 'tongTinChi', label: 'Tổng tín chỉ'},
      {key: 'tongSoGio', label: 'Tổng giờ'},
      {key: 'tongGioLyThuyet', label: 'Giờ lý thuyết'},
      {key: 'tongGioThucHanh', label: 'Giờ thực hành'},
      {key: 'tongGioKiemTra', label: 'Giờ kiểm tra'},
      {key: 'moTa', label: 'Mô tả'},
      {key: 'createdAt', label: 'Ngày tạo'},
      {key: 'updatedAt', label: 'Ngày cập nhật'}
    ]
  },
  nhomTuChonmau: {
    title: 'Nhóm tự chọn mẫu',
    description: 'Bảng mẫu/mẫu dùng chung để chọn, tái sử dụng.',
    defaultForm: {ma: '', ten: '', moTa: ''},
    fields: [{key: 'ma', label: 'Mã nhóm tự chọn mẫu', required: true}, {
      key: 'ten',
      label: 'Tên nhóm tự chọn mẫu',
      required: true
    }, {key: 'moTa', label: 'Mô tả', type: 'textarea', wide: true}],
    uniqueRules: [{field: 'ma', message: 'Mã nhóm tự chọn mẫu đã tồn tại.'}],
    columns: [{key: 'ma', label: 'Mã'}, {key: 'ten', label: 'Tên nhóm tự chọn mẫu'}, {
      key: 'moTa',
      label: 'Mô tả'
    }, {key: 'createdAt', label: 'Ngày tạo'}, {key: 'updatedAt', label: 'Ngày cập nhật'}]
  },
  nhomTuChon: {
    title: 'Nhóm tự chọn gán vào Version - TÁI SỬ DỤNG',
    description: 'Bảng nhóm tự chọn đã được copy từ nhóm tự chọn mẫu/mẫu vào Version.',
    defaultForm: {
      chuongTrinhVersionId: null,
      nhomTuChonmauId: null,
      ma: '',
      ten: '',
      soMonChon: null,
      soTinChiCanDat: null,
      ghiChu: '',
      moTa: ''
    },
    fields: [
      {
        key: 'chuongTrinhVersionId',
        label: 'Version',
        type: 'select',
        lookup: 'chuongTrinhVersion',
        labelKey: ['maVersion', 'tenVersion'],
        locked: true
      },
      {
        key: 'nhomTuChonmauId',
        label: 'Nhóm tự chọn mẫu',
        type: 'select',
        lookup: 'nhomTuChonmau',
        labelKey: ['ma', 'ten'],
        locked: true
      },
      {key: 'ma', label: 'Mã nhóm tự chọn'},
      {key: 'ten', label: 'Tên nhóm', required: true},
      {key: 'soMonChon', label: 'Số môn chọn', type: 'number'},
      {key: 'soTinChiCanDat', label: 'Số tín chỉ cần đạt', type: 'number', max: 9999.9, step: 0.1},
      {key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true},
      {key: 'moTa', label: 'Mô tả', type: 'textarea', wide: true}
    ],
    uniqueRules: [
      {
        field: 'ten',
        scopeKeys: ['chuongTrinhVersionId'],
        message: 'Tên nhóm tự chọn đã tồn tại trong Version đang chọn.'
      },
      {
        field: 'nhomTuChonmauId',
        scopeKeys: ['chuongTrinhVersionId'],
        message: 'Nhóm tự chọn mẫu này đã được gán vào Version đang chọn.'
      }
    ],
    columns: [
      {key: 'tenVersion', label: 'Version'},
      {key: 'tenNhomTuChonmau', label: 'Nhóm tự chọn mẫu'},
      {key: 'ma', label: 'Mã'},
      {key: 'ten', label: 'Tên nhóm'},
      {key: 'soMonChon', label: 'Số môn chọn'},
      {key: 'soTinChiCanDat', label: 'Số tín chỉ cần đạt'},
      {key: 'ghiChu', label: 'Ghi chú'},
      {key: 'moTa', label: 'Mô tả'},
      {key: 'createdAt', label: 'Ngày tạo'},
      {key: 'updatedAt', label: 'Ngày cập nhật'}
    ]
  },
  khungKymau: {
    title: 'Khung kỳ mẫu',
    description: 'Bảng mẫu/mẫu dùng chung để chọn, tái sử dụng.',
    defaultForm: {maKy: '', tenKy: '', thuTu: null, moTa: ''},
    fields: [{key: 'maKy', label: 'Mã kỳ', required: true}, {
      key: 'tenKy',
      label: 'Tên kỳ',
      required: true
    }, {key: 'thuTu', label: 'Thứ tự', type: 'number', step: 1, required: true}, {
      key: 'moTa',
      label: 'Mô tả',
      type: 'textarea',
      wide: true
    }],
    uniqueRules: [{field: 'maKy', message: 'Mã kỳ mẫu đã tồn tại.'}, {
      field: 'thuTu',
      message: 'Thứ tự kỳ mẫu đã tồn tại.'
    }],
    columns: [{key: 'maKy', label: 'Mã kỳ'}, {key: 'tenKy', label: 'Tên kỳ'}, {
      key: 'thuTu',
      label: 'Thứ tự'
    }, {key: 'moTa', label: 'Mô tả'}, {key: 'createdAt', label: 'Ngày tạo'}, {key: 'updatedAt', label: 'Ngày cập nhật'}]
  },
  khungKy: {
    title: 'Khung kỳ gán vào Version - TÁI SỬ DỤNG',
    description: 'Bảng lưu/áp dụng đã gắn vào Version thật.',
    defaultForm: {
      chuongTrinhVersionId: null,
      maKy: '',
      tenKy: '',
      thuTu: null,
      ngayBatDau: null,
      ngayKetThuc: null
    },
    fields: [{
      key: 'chuongTrinhVersionId',
      label: 'Version',
      type: 'select',
      lookup: 'chuongTrinhVersion',
      labelKey: ['maVersion', 'tenVersion'],
      locked: true
    }, {
      key: 'maKy', label: 'Mã kỳ', required: true
    }, {
      key: 'tenKy', label: 'Tên kỳ', required: true
    }, {
      key: 'thuTu',
      label: 'Thứ tự',
      type: 'number',
      step: 1,
      required: true
    }, {
      key: 'ngayBatDau',
      label: 'Ngày bắt đầu',
      type: 'date',
      validate: (value, form) => {
        if (value && form.ngayKetThuc && value > form.ngayKetThuc)
          return 'Ngày bắt đầu kỳ không được sau ngày kết thúc kỳ.'
        return null
      }
    }, {
      key: 'ngayKetThuc',
      label: 'Ngày kết thúc',
      type: 'date',
      validate: (value, form) => {
        if (value && form.ngayBatDau && value < form.ngayBatDau)
          return 'Ngày kết thúc kỳ không được trước ngày bắt đầu kỳ.'
        return null
      }
    }],
    uniqueRules: [{
      field: 'maKy',
      scopeKeys: ['chuongTrinhVersionId'],
      message: 'Mã kỳ đã tồn tại trong Version đang chọn.'
    }, {
      field: 'thuTu',
      scopeKeys: ['chuongTrinhVersionId'],
      message: 'Thứ tự khung kỳ đã tồn tại trong Version đang chọn.'
    }, {
      field: 'khungKymauId',
      scopeKeys: ['chuongTrinhVersionId'],
      message: 'Khung kỳ mẫu này đã được gán vào Version đang chọn.'
    }],
    columns: [
      {key: 'tenVersion', label: 'Version'},
      {key: 'maKy', label: 'Mã kỳ'},
      {key: 'tenKy', label: 'Tên kỳ'},
      {key: 'thuTu', label: 'Thứ tự'},
      {key: 'ngayBatDau', label: 'Ngày bắt đầu'},
      {key: 'ngayKetThuc', label: 'Ngày kết thúc'}
    ]
  },
  monHoc: {
    title: 'Môn học mẫu',
    allowToggleSelect: true,
    defaultForm: {maMon: '', tenMon: '', moTa: ''},
    fields: [
      {key: 'maMon', label: 'Mã môn', required: true},
      {key: 'tenMon', label: 'Tên môn', required: true},
      {key: 'moTa', label: 'Mô tả', type: 'textarea', wide: true}
    ],
    uniqueRules: [
      {field: 'maMon', message: 'Mã môn học mẫu đã tồn tại.'}
    ],
    columns: [
      {key: 'maMon', label: 'Mã môn'},
      {key: 'tenMon', label: 'Tên môn'},
      {key: 'moTa', label: 'Mô tả'}
    ]
  },
  chuongTrinhMon: {
    title: 'Chương trình môn',
    defaultForm: {
      chuongTrinhVersionId: null,
      monHocId: null,
      maMonTrongCt: '',
      khungKyId: null,
      nhomKienThucId: null,
      loai: 'bat_buoc',
      loaiHocPhan: 'mon_hoc',
      loaiPhamVi: 'mon_chuyen_nganh',
      batBuoc: true,
      laMonDieuKien: false,
      thuTu: null,
      ghiChu: ''
    },
    fields: [
      {key: 'maMonTrongCt', label: 'Mã môn CT', required: true},
      {key: 'loai', label: 'Loại môn', type: 'select', lookup: 'loaiMon'},
      {key: 'loaiHocPhan', label: 'Loại học phần', type: 'select', lookup: 'loaiHocPhan'},
      {key: 'loaiPhamVi', label: 'Phạm vi môn', type: 'select', lookup: 'loaiPhamViMon'},
      {key: 'batBuoc', label: 'Bắt buộc', type: 'boolean'},
      {key: 'laMonDieuKien', label: 'Môn điều kiện', type: 'boolean'},
      {key: 'thuTu', label: 'Thứ tự', type: 'number', step: 1},
      {key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true}
    ],
    requiredParentKeys: [
      {key: 'chuongTrinhVersionId', label: 'Version chương trình'},
      {key: 'monHocId', label: 'Môn học mẫu'},
      {key: 'khungKyId', label: 'Khung kỳ'}
    ],
    uniqueRules: [
      {
        field: 'maMonTrongCt',
        scopeKeys: ['chuongTrinhVersionId'],
        message: 'Mã môn CT đã tồn tại trong Version chương trình này.'
      },
      {
        field: 'thuTu',
        scopeKeys: ['chuongTrinhVersionId', 'khungKyId'],
        message: 'Thứ tự môn trong Chương trình môn đã tồn tại trong Version/Kỳ đang chọn.'
      }
    ],
    columns: [
      {key: 'maMonTrongCt', label: 'Mã môn CT'},
      {key: 'tenMon', label: 'Môn học'},
      {key: 'tenVersion', label: 'Version'},
      {key: 'tenKy', label: 'Kỳ'},
      {key: 'tenNhomKienThuc', label: 'Nhóm KT'},
      {key: 'loaiHienThi', label: 'Loại'},
      {key: 'loaiHocPhanHienThi', label: 'Loại học phần'},
      {key: 'loaiPhamViHienThi', label: 'Phạm vi'},
      {key: 'laMonDieuKien', label: 'Môn điều kiện'},
      {key: 'ghiChu', label: 'Ghi chú'}
    ]
  },
  monTuChon: {
    title: 'Môn tự chọn',
    defaultForm: {nhomId: null, chuongTrinhMonId: null},
    fields: [
      {
        key: 'nhomId',
        required: true,
        label: 'Nhóm tự chọn',
        type: 'select',
        lookup: 'nhomTuChon',
        labelKey: ['ten'],
        locked: true
      },
      {
        key: 'chuongTrinhMonId',
        required: true,
        label: 'Môn CT',
        type: 'select',
        lookup: 'chuongTrinhMon',
        labelKey: ['maMonTrongCt', 'tenMon'],
        locked: true
      }
    ],
    uniqueRules: [
      {
        field: 'chuongTrinhMonId',
        scopeKeys: ['nhomId'],
        message: 'Môn tự chọn này đã tồn tại trong Nhóm tự chọn đang chọn.'
      }
    ],
    columns: [
      {key: 'tenNhomTuChon', label: 'Nhóm tự chọn'},
      {key: 'tenChuongTrinhMon', label: 'Môn CT'}
    ]
  },
  monTienQuyet: {
    title: 'Môn tiên quyết',
    defaultForm: {monId: null, monDieuKienId: null, loai: 'tien_quyet', ghiChu: ''},
    fields: [
      {
        key: 'monDieuKienId',
        label: 'Môn điều kiện',
        type: 'select',
        lookup: 'chuongTrinhMon',
        labelKey: ['maMonTrongCt', 'tenMon'],
        required: true
      },
      {key: 'loai', label: 'Loại điều kiện', type: 'select', lookup: 'loaiDieuKien'},
      {key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true}
    ],
    uniqueRules: [
      {
        field: 'monDieuKienId',
        scopeKeys: ['monId'],
        message: 'Môn điều kiện đã tồn tại cho Môn trong chương trình đang chọn.'
      }
    ],
    columns: [
      {key: 'tenMon', label: 'Môn chính'},
      {key: 'tenMonDieuKien', label: 'Môn điều kiện'},
      {key: 'loaiHienThi', label: 'Loại'},
      {key: 'ghiChu', label: 'Ghi chú'}
    ]
  },
  quyDoiDiem: {
    title: 'Quy đổi điểm đã lưu cho môn trong chương trình',
    defaultForm: {
      chuongTrinhMonId: null,
      loaiMau: 'QUY_DOI_KET_QUA',
      nguongTu: null,
      nguongDen: null,
      diemQuyDoi: null,
      ketQua: 'dat',
      tyLe: null,
      diemToiDa: 10,
      thuTu: null,
      batBuoc: true,
      congThuc: '',
      ghiChu: ''
    },
    fields: [
      {key: 'chuongTrinhMonId', label: 'Môn trong chương trình', hidden: true, required: true},
      {key: 'loaiMau', label: 'Loại mẫu'},
      {key: 'nguongTu', label: 'Ngưỡng từ', type: 'number', max: 999.99, step: 0.01},
      {key: 'nguongDen', label: 'Ngưỡng đến', type: 'number', max: 999.99, step: 0.01},
      {key: 'diemQuyDoi', label: 'Điểm quy đổi', type: 'number', max: 999.99, step: 0.01},
      {key: 'ketQua', label: 'Kết quả', type: 'select', lookup: 'ketQua'},
      {key: 'tyLe', label: 'Tỷ lệ %', type: 'number', max: 100, step: 0.01},
      {key: 'diemToiDa', label: 'Điểm tối đa', type: 'number', max: 999.99, step: 0.01},
      {key: 'thuTu', label: 'Thứ tự', type: 'number', step: 1},
      {key: 'batBuoc', label: 'Bắt buộc', type: 'checkbox'},
      {key: 'congThuc', label: 'Công thức'},
      {key: 'ghiChu', label: 'Tên cột điểm / Ghi chú', type: 'textarea', wide: true}
    ],
    columns: [
      {key: 'tenSyllabusMonHocmau', label: 'Syllabus mẫu'},
      {key: 'tenCotDiemMau', label: 'Tên cột điểm mẫu'},
      {key: 'loaiMau', label: 'Loại mẫu'},
      {key: 'nguongTu', label: 'Ngưỡng từ'},
      {key: 'nguongDen', label: 'Ngưỡng đến'},
      {key: 'diemQuyDoi', label: 'Điểm quy đổi'},
      {key: 'ketQua', label: 'Kết quả'},
      {key: 'congThuc', label: 'Công thức'},
      {key: 'ghiChu', label: 'Tên cột điểm / Ghi chú'}
    ]
  },
  cauHinhDanhGia: {
    title: 'Cột điểm của syllabus áp dụng',
    defaultForm: {
      syllabusMonHocId: null,
      tenCotDiem: '',
      loaiDiem: 'khac',
      tyLe: null,
      diemToiDa: 10,
      thuTu: null
    },
    fields: [
      {key: 'syllabusMonHocId', label: 'Syllabus áp dụng', hidden: true, required: true},
      {key: 'tenCotDiem', label: 'Tên cột điểm', required: true},
      {key: 'loaiDiem', label: 'Loại điểm', type: 'select', lookup: 'loaiDiemDanhGia'},
      {key: 'tyLe', label: 'Tỷ lệ %', type: 'number', max: 100, step: 0.01, required: true},
      {key: 'diemToiDa', label: 'Điểm tối đa', type: 'number', max: 999.99, step: 0.01},
      {key: 'thuTu', label: 'Thứ tự', type: 'number', step: 1}
    ],
    uniqueRules: [
      {field: 'tenCotDiem', message: 'Tên cột điểm đã tồn tại trong syllabus.'}
    ],
    columns: [
      {key: 'tenSyllabusMonHocmau', label: 'Syllabus mẫu'},
      {key: 'tenCotDiem', label: 'Tên cột điểm'},
      {key: 'loaiDiem', label: 'Loại điểm'},
      {key: 'tyLe', label: 'Tỷ lệ %'},
      {key: 'diemToiDa', label: 'Điểm tối đa'},
      {key: 'thuTu', label: 'Thứ tự'}
    ]
  },
  cauHinhDanhGiaMau: {
    title: 'Cột điểm mẫu của syllabus mẫu',
    defaultForm: {
      syllabusMonHocMauId: null,
      tenCotDiem: '',
      loaiDiem: 'khac',
      tyLe: null,
      diemToiDa: 10,
      thuTu: null,
      batBuoc: true,
      ghiChu: ''
    },
    fields: [
      {key: 'syllabusMonHocMauId', label: 'Syllabus mẫu', hidden: true, required: true},
      {key: 'tenCotDiem', label: 'Tên cột điểm', required: true},
      {key: 'loaiDiem', label: 'Loại điểm', type: 'select', lookup: 'loaiDiemDanhGia'},
      {key: 'tyLe', label: 'Tỷ lệ %', type: 'number', max: 100, step: 0.01, required: true},
      {key: 'diemToiDa', label: 'Điểm tối đa', type: 'number', max: 999.99, step: 0.01},
      {key: 'thuTu', label: 'Thứ tự', type: 'number', step: 1},
      {key: 'batBuoc', label: 'Bắt buộc', type: 'checkbox'},
      {key: 'ghiChu', label: 'Ghi chú mẫu', type: 'textarea', wide: true}
    ],
    uniqueRules: [
      {field: 'tenCotDiem', message: 'Tên cột điểm mẫu đã tồn tại.'}
    ],
    columns: [
      {key: 'tenSyllabusMonHocmau', label: 'Syllabus mẫu'},
      {key: 'tenCotDiem', label: 'Tên cột điểm'},
      {key: 'loaiDiem', label: 'Loại điểm'},
      {key: 'tyLe', label: 'Tỷ lệ %'},
      {key: 'diemToiDa', label: 'Điểm tối đa'},
      {key: 'thuTu', label: 'Thứ tự'},
      {key: 'batBuoc', label: 'Bắt buộc'},
      {key: 'ghiChu', label: 'Ghi chú mẫu'}
    ]
  },
  quyDoiDiemMau: {
    title: 'Mẫu quy đổi kết quả theo syllabus mẫu',
    defaultForm: {
      ma: '',
      ten: '',
      loaiMau: 'QUY_DOI_KET_QUA',
      nguongTu: null,
      nguongDen: null,
      diemQuyDoi: null,
      ketQua: 'dat',
      tyLe: null,
      diemToiDa: 10,
      thuTu: null,
      batBuoc: true,
      congThuc: '',
      ghiChu: ''
    },
    fields: [
      {key: 'ma', label: 'Mã mẫu quy đổi', required: true},
      {key: 'ten', label: 'Tên mẫu quy đổi', required: true},
      {key: 'loaiMau', label: 'Loại quy đổi', hidden: true},
      {key: 'nguongTu', label: 'Ngưỡng từ', type: 'number', max: 999.99, step: 0.01},
      {key: 'nguongDen', label: 'Ngưỡng đến', type: 'number', max: 999.99, step: 0.01},
      {key: 'diemQuyDoi', label: 'Điểm quy đổi', type: 'number', max: 999.99, step: 0.01},
      {key: 'ketQua', label: 'Kết quả', type: 'select', lookup: 'ketQua'},
      {key: 'congThuc', label: 'Công thức'},
      {key: 'ghiChu', label: 'Ghi chú mẫu', type: 'textarea', wide: true}
    ],
    uniqueRules: [
      {field: 'ma', message: 'Mã mẫu quy đổi điểm đã tồn tại.'}
    ],
    columns: [
      {key: 'ma', label: 'Mã mẫu quy đổi'},
      {key: 'ten', label: 'Tên mẫu quy đổi'},
      {key: 'loaiMauHienThi', label: 'Loại quy đổi'},
      {key: 'nguongTu', label: 'Ngưỡng từ'},
      {key: 'nguongDen', label: 'Ngưỡng đến'},
      {key: 'diemQuyDoi', label: 'Điểm quy đổi'},
      {key: 'ketQuaHienThi', label: 'Kết quả'},
      {key: 'congThuc', label: 'Công thức'},
      {key: 'ghiChu', label: 'Ghi chú mẫu'}
    ]
  },

  chuongTrinhMonQuyDoiDiemMau: {
    title: 'Mẫu quy đổi kết quả đã gán vào syllabus',
    defaultForm: {
      syllabusMonHocId: null,
      quyDoiDiemMauId: null,
      ghiChu: ''
    },
    fields: [],
    columns: [
      {key: 'tenChuongTrinhMon', label: 'Môn trong chương trình'},
      {key: 'maQuyDoiDiemMau', label: 'Mã mẫu quy đổi'},
      {key: 'tenQuyDoiDiemMau', label: 'Tên mẫu quy đổi'},
      {key: 'loaiMauHienThi', label: 'Loại quy đổi'},
      {key: 'nguongTu', label: 'Ngưỡng từ'},
      {key: 'nguongDen', label: 'Ngưỡng đến'},
      {key: 'diemQuyDoi', label: 'Điểm quy đổi'},
      {key: 'ketQuaHienThi', label: 'Kết quả'},
      {key: 'congThuc', label: 'Công thức'},
      {key: 'ghiChuMau', label: 'Ghi chú mẫu'},
      {key: 'ghiChu', label: 'Ghi chú gán'}
    ]
  },

  syllabusMonHocmau: {
    title: 'Syllabus môn học mẫu - TÁI SỬ DỤNG',
    defaultForm: {
      monHocId: null,
      ma: '',
      ten: '',
      viTri: '',
      tinhChat: '',
      soTinChi: null,
      soBuoiHoc: null,
      soTietMoiBuoi: null,
      soPhutMotTiet: null,
      mucTieu: '',
      phuongPhapDanhGia: '',
      dieuKienHoanThanh: '',
      huongDan: '',
      diemDatToiThieu: null,
      donViDiem: 'thang_10',
      tyLeChuyenCanToiThieu: null,
      batBuocDuThi: true,
      congThucQuyDoi: '',
      ghiChu: ''
    },
    fields: [
      {
        key: 'monHocId',
        label: 'Môn học mẫu',
        type: 'select',
        lookup: 'monHoc',
        labelKey: ['maMon', 'tenMon'],
        locked: true
      },
      {key: 'ma', label: 'Mã syllabus mẫu', required: true},
      {key: 'ten', label: 'Tên syllabus mẫu', required: true},
      {key: 'viTri', label: 'Vị trí', type: 'textarea', wide: true},
      {key: 'tinhChat', label: 'Tính chất', type: 'textarea', wide: true},
      {key: 'soTinChi', label: 'Số tín chỉ', type: 'number', min: 0, step: 0.1},
      {key: 'soBuoiHoc', label: 'Số buổi học', type: 'number', min: 1, step: 1, required: true},
      {key: 'soTietMoiBuoi', label: 'Số tiết mỗi buổi', type: 'number', min: 0.1, step: 0.1, required: true},
      {key: 'soPhutMotTiet', label: 'Số phút một tiết', type: 'number', min: 1, step: 1, required: true},
      {key: 'mucTieu', label: 'Mục tiêu', type: 'textarea', wide: true},
      {key: 'phuongPhapDanhGia', label: 'Phương pháp đánh giá', type: 'textarea', wide: true},
      {key: 'dieuKienHoanThanh', label: 'Điều kiện hoàn thành', type: 'textarea', wide: true},
      {
        key: 'diemDatToiThieu',
        label: 'Điểm đạt tối thiểu',
        type: 'number',
        max: 10,
        step: 0.01,
        maxBy: {donViDiem: {thang_10: 10, phan_tram: 100}}
      },
      {key: 'donViDiem', label: 'Đơn vị điểm', type: 'select', lookup: 'donViDiem'},
      {key: 'tyLeChuyenCanToiThieu', label: 'Tỷ lệ chuyên cần tối thiểu', type: 'number', max: 100, step: 0.01},
      {key: 'batBuocDuThi', label: 'Bắt buộc dự thi', type: 'boolean'},
      {key: 'congThucQuyDoi', label: 'Công thức quy đổi'},
      {key: 'huongDan', label: 'Hướng dẫn', type: 'textarea', wide: true},
      {key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true}
    ],
    uniqueRules: [
      {field: 'ma', message: 'Mã syllabus môn học mẫu đã tồn tại.'}
    ],
    columns: [
      {key: 'ma', label: 'Mã'},
      {key: 'ten', label: 'Tên syllabus mẫu'},
      {key: 'tenMonHoc', label: 'Môn học'},
      {key: 'soTinChi', label: 'Số tín chỉ'},
      {key: 'tongGio', label: 'Tổng giờ'},
      {key: 'gioLyThuyet', label: 'Giờ lý thuyết'},
      {key: 'gioThucHanh', label: 'Giờ thực hành'},
      {key: 'gioKiemTra', label: 'Giờ kiểm tra'},
      {key: 'viTri', label: 'Vị trí'},
      {key: 'tinhChat', label: 'Tính chất'},
      {key: 'soBuoiHoc', label: 'Số buổi'},
      {key: 'soTietMoiBuoi', label: 'Tiết/buổi'},
      {key: 'soPhutMotTiet', label: 'Phút/tiết'},
      {key: 'mucTieu', label: 'Mục tiêu'},
      {key: 'phuongPhapDanhGia', label: 'Phương pháp đánh giá'},
      {key: 'dieuKienHoanThanh', label: 'Điều kiện hoàn thành'},
      {key: 'huongDan', label: 'Hướng dẫn'},
      {key: 'diemDatToiThieu', label: 'Điểm đạt tối thiểu'},
      {key: 'donViDiem', label: 'Đơn vị điểm'},
      {key: 'tyLeChuyenCanToiThieu', label: 'Tỷ lệ chuyên cần tối thiểu'},
      {key: 'batBuocDuThi', label: 'Bắt buộc dự thi'},
      {key: 'congThucQuyDoi', label: 'Công thức quy đổi'},
      {key: 'ghiChu', label: 'Ghi chú'},
      {key: 'createdAt', label: 'Ngày tạo'},
      {key: 'updatedAt', label: 'Ngày cập nhật'}
    ]
  },
  syllabusMonHocmauChuongBai: {
    title: 'Chương/bài của syllabus mẫu ',
    defaultForm: {
      syllabusMonHocMauId: null,
      maChuong: '',
      ten: '',
      mucTieu: '',
      noiDung: '',
      thuTu: null,
      tongGio: null,
      gioLyThuyet: null,
      gioThucHanh: null,
      gioKiemTra: null,
      ghiChu: ''
    },
    fields: [
      {
        key: 'syllabusMonHocMauId',
        label: 'Syllabus mẫu',
        type: 'select',
        lookup: 'syllabusMonHocmau',
        labelKey: ['ma', 'ten'],
        locked: true
      },
      {key: 'maChuong', label: 'Mã chương'},
      {key: 'ten', label: 'Tên chương/bài', required: true},
      {key: 'mucTieu', label: 'Mục tiêu', type: 'textarea', wide: true},
      {key: 'noiDung', label: 'Nội dung', type: 'textarea', wide: true},
      {key: 'thuTu', label: 'Thứ tự', type: 'number', step: 1},
      {key: 'tongGio', label: 'Tổng giờ', type: 'number', max: 9999.99, step: 0.01},
      {key: 'gioLyThuyet', label: 'Giờ LT', type: 'number', max: 9999.99, step: 0.01},
      {key: 'gioThucHanh', label: 'Giờ TH', type: 'number', max: 9999.99, step: 0.01},
      {key: 'gioKiemTra', label: 'Giờ KT', type: 'number', max: 9999.99, step: 0.01},
      {key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true}
    ],
    columns: [
      {key: 'tenSyllabusMonHocmau', label: 'Syllabus mẫu'},
      {key: 'thuTu', label: 'Thứ tự'},
      {key: 'maChuong', label: 'Mã chương'},
      {key: 'ten', label: 'Tên chương/bài'},
      {key: 'mucTieu', label: 'Mục tiêu'},
      {key: 'noiDung', label: 'Nội dung'},
      {key: 'tongGio', label: 'Tổng giờ'},
      {key: 'gioLyThuyet', label: 'Giờ LT'},
      {key: 'gioThucHanh', label: 'Giờ TH'},
      {key: 'gioKiemTra', label: 'Giờ KT'},
      {key: 'ghiChu', label: 'Ghi chú'}
    ]
  },
  syllabusMonHocmauDieuKien: {
    title: 'Điều kiện của syllabus mẫu ',
    defaultForm: {syllabusMonHocMauId: null, dieuKienmauId: null, thuTu: null, ghiChu: ''},
    fields: [
      {
        key: 'syllabusMonHocMauId',
        label: 'Syllabus mẫu',
        type: 'select',
        lookup: 'syllabusMonHocmau',
        labelKey: ['ma', 'ten'],
        locked: true
      },
      {
        key: 'dieuKienmauId',
        label: 'Điều kiện mẫu',
        type: 'select',
        lookup: 'dieuKienMonHocmau',
        labelKey: ['ma', 'noiDung'],
        required: true
      },
      {key: 'thuTu', label: 'Thứ tự', type: 'number', step: 1},
      {key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true}
    ],
    columns: [
      {key: 'tenSyllabusMonHocmau', label: 'Syllabus mẫu'},
      {key: 'tenDieuKienmau', label: 'Điều kiện mẫu'},
      {key: 'ghiChu', label: 'Ghi chú'}
    ]
  },
  syllabusMonHocmauTaiLieu: {
    title: 'Tài liệu mẫu của syllabus',
    description: 'Bản tài liệu mẫu thuộc đúng syllabus môn học mẫu. Ở tầng chương trình chỉ chọn/copy, không CRUD.',
    defaultForm: {
      syllabusMonHocMauId: null,
      taiLieumauId: null,
      ma: '',
      ten: '',
      loai: 'GIAO_TRINH',
      tacGia: '',
      nhaXuatBan: '',
      namXuatBan: null,
      duongDan: '',
      thuTu: null,
      batBuoc: false,
      ghiChu: ''
    },
    fields: [
      {
        key: 'syllabusMonHocMauId',
        label: 'Syllabus mẫu',
        type: 'select',
        lookup: 'syllabusMonHocmau',
        labelKey: ['ma', 'ten'],
        locked: true
      },
      {key: 'ma', label: 'Mã tài liệu', required: true},
      {key: 'ten', label: 'Tên tài liệu', required: true},
      {key: 'loai', label: 'Loại tài liệu', type: 'select', lookup: 'loaiTaiLieu'},
      {key: 'tacGia', label: 'Tác giả'},
      {key: 'nhaXuatBan', label: 'Nhà xuất bản'},
      {key: 'namXuatBan', label: 'Năm xuất bản', type: 'number'},
      {key: 'duongDan', label: 'Đường dẫn đã lưu', locked: true, openFile: true},
      {key: 'thuTu', label: 'Thứ tự', type: 'number', step: 1},
      {key: 'batBuoc', label: 'Bắt buộc', type: 'boolean'},
      {key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true}
    ],
    columns: [
      {key: 'tenSyllabusMonHocmau', label: 'Syllabus mẫu'},
      {key: 'ma', label: 'Mã tài liệu'},
      {key: 'ten', label: 'Tên tài liệu'},
      {key: 'loaiHienThi', label: 'Loại điều kiện'},
      {key: 'tacGia', label: 'Tác giả'},
      {key: 'nhaXuatBan', label: 'Nhà xuất bản'},
      {key: 'namXuatBan', label: 'Năm xuất bản'},
      {key: 'duongDan', label: 'File đã lưu', openFile: true, openFilePage: true},
      {key: 'ghiChu', label: 'Ghi chú'},
      {key: 'createdAt', label: 'Ngày tạo'},
      {key: 'updatedAt', label: 'Ngày cập nhật'}
    ]
  },
  syllabusMonHoc: {
    title: 'Syllabus môn học đã gán vào môn trong chương trình',
    description: 'Bảng syllabus môn học áp dụng cho môn trong chương trình.',
    defaultForm: {
      chuongTrinhMonId: null,
      syllabusMonHocMauId: null,
      monHocId: null,
      ma: '',
      ten: '',
      viTri: '',
      tinhChat: '',
      soTinChi: null,
      soBuoiHoc: null,
      soTietMoiBuoi: null,
      soPhutMotTiet: null,
      mucTieu: '',
      phuongPhapDanhGia: '',
      dieuKienHoanThanh: '',
      huongDan: '',
      diemDatToiThieu: null,
      donViDiem: 'thang_10',
      tyLeChuyenCanToiThieu: null,
      batBuocDuThi: false,
      congThucQuyDoi: '',
      ghiChu: ''
    },
    fields: [
      {
        key: 'chuongTrinhMonId',
        label: 'Môn trong chương trình',
        type: 'select',
        lookup: 'chuongTrinhMon',
        labelKey: ['maMonTrongCt', 'tenMon'],
        locked: true
      },
      {
        key: 'syllabusMonHocMauId',
        label: 'Syllabus môn học mẫu',
        type: 'select',
        lookup: 'syllabusMonHocmau',
        labelKey: ['ma', 'ten'],
        locked: true
      },
      {
        key: 'monHocId',
        label: 'Môn học mẫu',
        type: 'select',
        lookup: 'monHoc',
        labelKey: ['maMon', 'tenMon'],
        locked: true
      },
      {key: 'ma', label: 'Mã syllabus áp dụng'},
      {key: 'ten', label: 'Tên syllabus áp dụng'},
      {key: 'viTri', label: 'Vị trí', type: 'textarea', wide: true},
      {key: 'tinhChat', label: 'Tính chất', type: 'textarea', wide: true},
      {key: 'soTinChi', label: 'Số tín chỉ', type: 'number', min: 0, step: 0.1},
      {key: 'soBuoiHoc', label: 'Số buổi học', type: 'number', min: 1, step: 1, required: true},
      {key: 'soTietMoiBuoi', label: 'Số tiết mỗi buổi', type: 'number', min: 0.1, step: 0.1, required: true},
      {key: 'soPhutMotTiet', label: 'Số phút một tiết', type: 'number', min: 1, step: 1, required: true},
      {key: 'mucTieu', label: 'Mục tiêu', type: 'textarea', wide: true},
      {key: 'phuongPhapDanhGia', label: 'Phương pháp đánh giá', type: 'textarea', wide: true},
      {key: 'dieuKienHoanThanh', label: 'Điều kiện hoàn thành', type: 'textarea', wide: true},
      {key: 'huongDan', label: 'Hướng dẫn', type: 'textarea', wide: true},
      {key: 'diemDatToiThieu', label: 'Điểm đạt tối thiểu', type: 'number', step: 0.01},
      {key: 'donViDiem', label: 'Đơn vị điểm', type: 'select', lookup: 'donViDiem'},
      {key: 'tyLeChuyenCanToiThieu', label: 'Tỷ lệ chuyên cần tối thiểu', type: 'number', step: 0.01},
      {key: 'batBuocDuThi', label: 'Bắt buộc dự thi', type: 'boolean'},
      {key: 'congThucQuyDoi', label: 'Công thức quy đổi', type: 'textarea', wide: true},
      {key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true}
    ],
    columns: [
      {key: 'tenChuongTrinhMon', label: 'Môn trong CT'},
      {key: 'tenSyllabusMonHocmau', label: 'Syllabus mẫu'},
      {key: 'ma', label: 'Mã'},
      {key: 'ten', label: 'Tên syllabus áp dụng'},
      {key: 'tenMonHoc', label: 'Môn học mẫu'},
      {key: 'soTinChi', label: 'Số tín chỉ'},
      {key: 'tongGio', label: 'Tổng giờ'},
      {key: 'gioLyThuyet', label: 'Giờ lý thuyết'},
      {key: 'gioThucHanh', label: 'Giờ thực hành'},
      {key: 'gioKiemTra', label: 'Giờ kiểm tra'},
      {key: 'viTri', label: 'Vị trí'},
      {key: 'tinhChat', label: 'Tính chất'},
      {key: 'soBuoiHoc', label: 'Số buổi'},
      {key: 'soTietMoiBuoi', label: 'Tiết/buổi'},
      {key: 'soPhutMotTiet', label: 'Phút/tiết'},
      {key: 'mucTieu', label: 'Mục tiêu'},
      {key: 'phuongPhapDanhGia', label: 'Phương pháp đánh giá'},
      {key: 'dieuKienHoanThanh', label: 'Điều kiện hoàn thành'},
      {key: 'huongDan', label: 'Hướng dẫn'},
      {key: 'diemDatToiThieu', label: 'Điểm đạt tối thiểu'},
      {key: 'donViDiem', label: 'Đơn vị điểm'},
      {key: 'tyLeChuyenCanToiThieu', label: 'Tỷ lệ chuyên cần tối thiểu'},
      {key: 'batBuocDuThi', label: 'Bắt buộc dự thi'},
      {key: 'congThucQuyDoi', label: 'Công thức quy đổi'},
      {key: 'ghiChu', label: 'Ghi chú'},
      {key: 'createdAt', label: 'Ngày tạo'},
      {key: 'updatedAt', label: 'Ngày cập nhật'}
    ]
  },
  dieuKienMonHoc: {
    title: 'Điều kiện môn học đã lưu vào syllabus',
    description: 'Điều kiện thực hiện môn học đã lưu riêng cho syllabus áp dụng.',
    defaultForm: {
      syllabusMonId: null,
      loai: 'phong_hoc',
      ten: '',
      noiDung: '',
      soLuong: null,
      yeuCau: '',
      thuTu: null,
      ghiChu: ''
    },
    fields: [
      {
        key: 'syllabusMonId',
        label: 'Syllabus môn',
        type: 'select',
        lookup: 'syllabusMonHoc',
        labelKey: ['ten', 'tenSyllabusMonHocmau', 'ma'],
        locked: true
      },
      {key: 'loai', label: 'Loại điều kiện', type: 'select', lookup: 'loaiDieuKienMonHoc', required: true},
      {key: 'ten', label: 'Tên điều kiện', required: true},
      {key: 'noiDung', label: 'Nội dung', type: 'textarea', required: true, wide: true},
      {key: 'soLuong', label: 'Số lượng', type: 'number', step: 1},
      {key: 'yeuCau', label: 'Yêu cầu', type: 'textarea', wide: true},
      {key: 'thuTu', label: 'Thứ tự', type: 'number', step: 1},
      {key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true}
    ],
    columns: [
      {key: 'tenSyllabusMon', label: 'Syllabus môn'},
      {key: 'loaiHienThi', label: 'Loại điều kiện'},
      {key: 'ten', label: 'Tên điều kiện'},
      {key: 'noiDung', label: 'Nội dung'},
      {key: 'soLuong', label: 'Số lượng'},
      {key: 'yeuCau', label: 'Yêu cầu'},
      {key: 'ghiChu', label: 'Ghi chú'},
      {key: 'createdAt', label: 'Ngày tạo'},
      {key: 'updatedAt', label: 'Ngày cập nhật'}
    ]
  },
  dieuKienMonHocmau: {
    title: 'Điều kiện môn học mẫu',
    description: 'Kho điều kiện thực hiện môn học dùng chung.',
    defaultForm: {
      ma: '',
      loai: 'phong_hoc',
      noiDung: '',
      ghiChu: ''
    },
    fields: [
      {key: 'ma', label: 'Mã điều kiện', required: true},
      {key: 'loai', label: 'Loại điều kiện', type: 'select', lookup: 'loaiDieuKienMonHoc', required: true},
      {key: 'noiDung', label: 'Nội dung', type: 'textarea', required: true, wide: true},
      {key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true}
    ],
    columns: [
      {key: 'ma', label: 'Mã'},
      {key: 'loaiHienThi', label: 'Loại điều kiện'},
      {key: 'noiDung', label: 'Nội dung'},
      {key: 'ghiChu', label: 'Ghi chú'},
      {key: 'createdAt', label: 'Ngày tạo'},
      {key: 'updatedAt', label: 'Ngày cập nhật'}
    ]
  },
  syllabusMonHocDieuKien: {
    title: 'Điều kiện gán vào syllabus môn - TÁI SỬ DỤNG',
    defaultForm: {syllabusMonId: null, dieuKienmauId: null, thuTu: null, ghiChu: ''},
    fields: [
      {
        key: 'syllabusMonId',
        label: 'Syllabus môn',
        type: 'select',
        lookup: 'syllabusMonHoc',
        labelKey: ['viTri', 'mucTieu'],
        locked: true
      },
      {
        key: 'dieuKienmauId',
        label: 'Điều kiện mẫu',
        type: 'select',
        lookup: 'dieuKienMonHocmau',
        labelKey: ['ma', 'noiDung'],
        required: true
      },
      {key: 'thuTu', label: 'Thứ tự', type: 'number', step: 1},
      {key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true}
    ],
    uniqueRules: [
      {
        field: 'dieuKienmauId',
        scopeKeys: ['syllabusMonId'],
        message: 'Điều kiện này đã được gán vào Syllabus môn đang chọn.'
      },
      {
        field: 'thuTu',
        scopeKeys: ['syllabusMonId'],
        message: 'Thứ tự điều kiện đã tồn tại trong Syllabus môn đang chọn.'
      }
    ],
    columns: [
      {key: 'tenDieuKienmau', label: 'Điều kiện mẫu'},
      {key: 'ghiChu', label: 'Ghi chú'}
    ]
  },
  syllabusChuongBai: {
    title: 'Chương/bài của syllabus',
    description: 'Nội dung chi tiết từng chương/bài trong syllabus môn học áp dụng.',
    defaultForm: {
      syllabusMonId: null,
      maChuong: '',
      ten: '',
      mucTieu: '',
      noiDung: '',
      thuTu: null,
      tongGio: null,
      gioLyThuyet: null,
      gioThucHanh: null,
      gioKiemTra: null,
      ghiChu: ''
    },
    fields: [
      {
        key: 'syllabusMonId',
        label: 'Syllabus môn',
        type: 'select',
        lookup: 'syllabusMonHoc',
        labelKey: ['ten', 'tenSyllabusMonHocmau', 'ma'],
        locked: true
      },
      {key: 'maChuong', label: 'Mã chương', required: true},
      {key: 'ten', label: 'Tên chương/bài', required: true},
      {key: 'mucTieu', label: 'Mục tiêu', type: 'textarea', wide: true},
      {key: 'noiDung', label: 'Nội dung', type: 'textarea', wide: true},
      {key: 'tongGio', label: 'Tổng giờ', type: 'number', step: 0.1},
      {key: 'gioLyThuyet', label: 'Giờ lý thuyết', type: 'number', step: 0.1},
      {key: 'gioThucHanh', label: 'Giờ thực hành', type: 'number', step: 0.1},
      {key: 'gioKiemTra', label: 'Giờ kiểm tra', type: 'number', step: 0.1},
      {key: 'thuTu', label: 'Thứ tự', type: 'number', step: 1},
      {key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true}
    ],
    columns: [
      {key: 'tenSyllabusMon', label: 'Syllabus môn'},
      {key: 'thuTu', label: 'Thứ tự'},
      {key: 'maChuong', label: 'Mã chương'},
      {key: 'ten', label: 'Tên chương/bài'},
      {key: 'mucTieu', label: 'Mục tiêu'},
      {key: 'noiDung', label: 'Nội dung'},
      {key: 'tongGio', label: 'Tổng giờ'},
      {key: 'gioLyThuyet', label: 'Giờ lý thuyết'},
      {key: 'gioThucHanh', label: 'Giờ thực hành'},
      {key: 'gioKiemTra', label: 'Giờ kiểm tra'},
      {key: 'ghiChu', label: 'Ghi chú'},
      {key: 'createdAt', label: 'Ngày tạo'},
      {key: 'updatedAt', label: 'Ngày cập nhật'}
    ]
  },
  taiLieumau: {
    title: 'Tài liệu mẫu',
    description: 'Kho tài liệu mẫu dùng chung cho syllabus môn học. Không nhập đường dẫn thủ công, chọn file để hệ thống tự upload và lưu đường dẫn.',
    defaultForm: {
      ma: '',
      ten: '',
      loai: 'GIAO_TRINH',
      tacGia: '',
      nhaXuatBan: '',
      namXuatBan: null,
      duongDan: '',
      ghiChu: ''
    },
    fields: [
      {key: 'ma', label: 'Mã tài liệu', required: true},
      {key: 'ten', label: 'Tên tài liệu', required: true},
      {key: 'loai', label: 'Loại tài liệu', type: 'select', lookup: 'loaiTaiLieu'},
      {key: 'tacGia', label: 'Tác giả'},
      {key: 'nhaXuatBan', label: 'Nhà xuất bản'},
      {key: 'namXuatBan', label: 'Năm xuất bản', type: 'number'},
      {
        key: '__fileTaiLieu',
        label: 'File tài liệu',
        type: 'file',
        themNhieuO: true,
        accept: '.pdf,.doc,.docx,.xls,.xlsx,.ppt,.pptx,.txt,.csv,.jpg,.jpeg,.png,.webp,.mp4,.mov,.avi,.mkv,.webm,.mp3,.wav,.m4a,.ogg,.zip,.rar,.7z',
        nghiepVu: 'syllabus_tai_lieu_mau',
        tangNghiepVu: 'tang-10-tai-lieu-mau-mau'
      },
      {key: 'duongDan', label: 'Đường dẫn đã lưu', locked: true, openFile: true},
      {key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true}
    ],
    columns: [
      {key: 'ma', label: 'Mã'},
      {key: 'ten', label: 'Tên tài liệu'},
      {key: 'loaiHienThi', label: 'Loại điều kiện'},
      {key: 'tacGia', label: 'Tác giả'},
      {key: 'nhaXuatBan', label: 'Nhà xuất bản'},
      {key: 'namXuatBan', label: 'Năm xuất bản'},
      {key: 'duongDan', label: 'File đã lưu', openFile: true, openFilePage: true},
      {key: 'ghiChu', label: 'Ghi chú'},
      {key: 'createdAt', label: 'Ngày tạo'},
      {key: 'updatedAt', label: 'Ngày cập nhật'}
    ]
  },
  syllabusMonHocTaiLieu: {
    title: 'Tài liệu gán vào syllabus môn - TÁI SỬ DỤNG',
    defaultForm: {syllabusMonId: null, taiLieumauId: null, thuTu: null, batBuoc: false, ghiChu: ''},
    fields: [
      {
        key: 'syllabusMonId',
        label: 'Syllabus môn',
        type: 'select',
        lookup: 'syllabusMonHoc',
        labelKey: ['viTri', 'mucTieu'],
        locked: true
      },
      {
        key: 'taiLieumauId',
        label: 'Tài liệu mẫu',
        type: 'select',
        lookup: 'taiLieumau',
        labelKey: ['ma', 'ten'],
        required: true
      },
      {key: 'thuTu', label: 'Thứ tự', type: 'number', step: 1},
      {key: 'batBuoc', label: 'Bắt buộc', type: 'boolean'},
      {key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true}
    ],
    uniqueRules: [
      {
        field: 'taiLieumauId',
        scopeKeys: ['syllabusMonId'],
        message: 'Tài liệu này đã được gán vào Syllabus môn đang chọn.'
      },
      {
        field: 'thuTu',
        scopeKeys: ['syllabusMonId'],
        message: 'Thứ tự tài liệu đã tồn tại trong Syllabus môn đang chọn.'
      }
    ],
    columns: [
      {key: 'tenTaiLieumau', label: 'Tài liệu mẫu'},
      {key: 'ghiChu', label: 'Ghi chú'}
    ]
  },
  syllabusTaiLieu: {
    title: 'Tài liệu đã lưu vào syllabus',
    description: 'Tài liệu đã lưu riêng cho syllabus môn học áp dụng.',
    defaultForm: {
      syllabusMonId: null,
      ma: '',
      ten: '',
      loai: 'GIAO_TRINH',
      tacGia: '',
      nhaXuatBan: '',
      namXuatBan: null,
      duongDan: '',
      batBuoc: false,
      thuTu: null,
      ghiChu: ''
    },
    fields: [
      {
        key: 'syllabusMonId',
        label: 'Syllabus môn',
        type: 'select',
        lookup: 'syllabusMonHoc',
        labelKey: ['ten', 'tenSyllabusMonHocmau', 'ma'],
        locked: true
      },
      {key: 'ma', label: 'Mã tài liệu', required: true},
      {key: 'ten', label: 'Tên tài liệu', required: true},
      {key: 'loai', label: 'Loại tài liệu', type: 'select', lookup: 'loaiTaiLieu'},
      {key: 'tacGia', label: 'Tác giả'},
      {key: 'nhaXuatBan', label: 'Nhà xuất bản'},
      {key: 'namXuatBan', label: 'Năm xuất bản', type: 'number'},
      {
        key: '__fileTaiLieu',
        label: 'File tài liệu',
        type: 'file',
        themNhieuO: true,
        accept: '.pdf,.doc,.docx,.xls,.xlsx,.ppt,.pptx,.txt,.csv,.jpg,.jpeg,.png,.webp,.mp4,.mov,.avi,.mkv,.webm,.mp3,.wav,.m4a,.ogg,.zip,.rar,.7z',
        nghiepVu: 'syllabus_tai_lieu',
        tangNghiepVu: 'tang-10-tai-lieu-da-luu-vao-syllabus'
      },
      {key: 'duongDan', label: 'Đường dẫn đã lưu', locked: true, openFile: true},
      {key: 'batBuoc', label: 'Bắt buộc', type: 'boolean'},
      {key: 'thuTu', label: 'Thứ tự', type: 'number', step: 1},
      {key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true}
    ],
    columns: [
      {key: 'tenSyllabusMon', label: 'Syllabus môn'},
      {key: 'ma', label: 'Mã tài liệu'},
      {key: 'ten', label: 'Tên tài liệu'},
      {key: 'loaiHienThi', label: 'Loại điều kiện'},
      {key: 'tacGia', label: 'Tác giả'},
      {key: 'nhaXuatBan', label: 'Nhà xuất bản'},
      {key: 'namXuatBan', label: 'Năm xuất bản'},
      {key: 'duongDan', label: 'File đã lưu', openFile: true, openFilePage: true},
      {key: 'ghiChu', label: 'Ghi chú'},
      {key: 'createdAt', label: 'Ngày tạo'},
      {key: 'updatedAt', label: 'Ngày cập nhật'}
    ]
  },
}

// ===== FILE SYLLABUS MÔN HỌC (tang 9) =====

const fileSyllabusLoading = ref(false)
const fileSyllabusUploading = ref(false)
const fileSyllabusThayId = ref(null)  // id file đang được thay thế
const fileSyllabusThongBao = ref('')
const fileSyllabusLoaiThongBao = ref('') // 'success' | 'error'

const danhSachFileDaGanSyllabus = computed(() => duLieu.value.syllabusMonHocFile || [])

function formatKichThuocFile(bytes) {
  if (!bytes && bytes !== 0) return ''
  if (bytes < 1024) return `${bytes} B`
  if (bytes < 1024 * 1024) return `${(bytes / 1024).toFixed(1)} KB`
  return `${(bytes / (1024 * 1024)).toFixed(1)} MB`
}

async function thayFileSyllabus(fileId, event) {
  const file = event.target.files?.[0]
  if (!file) return
  event.target.value = ''
  fileSyllabusThayId.value = fileId
  fileSyllabusThongBao.value = ''
  try {
    await services.syllabusMonHocFile.replace(fileId, file)
    fileSyllabusThongBao.value = `Đã cập nhật file thành công.`
    fileSyllabusLoaiThongBao.value = 'success'
    await taiDuLieuTheoSyllabusMonHocApDung(selected.syllabusMonHoc)
  } catch (e) {
    fileSyllabusThongBao.value = `Lỗi: ${layThongBaoLoi(e)}`
    fileSyllabusLoaiThongBao.value = 'error'
  } finally {
    fileSyllabusThayId.value = null
  }
}

async function xoaFileSyllabus(file) {
  if (!confirm(`Xóa file "${file.tenFile}"?`)) return
  fileSyllabusLoading.value = true
  fileSyllabusThongBao.value = ''
  try {
    await services.syllabusMonHocFile.delete(file.id)
    fileSyllabusThongBao.value = `Đã xóa file "${file.tenFile}".`
    fileSyllabusLoaiThongBao.value = 'success'
    await taiDuLieuTheoSyllabusMonHocApDung(selected.syllabusMonHoc)
  } catch (e) {
    fileSyllabusThongBao.value = `Lỗi: ${layThongBaoLoi(e)}`
    fileSyllabusLoaiThongBao.value = 'error'
  } finally {
    fileSyllabusLoading.value = false
  }
}

async function uploadFileSyllabusApDung(event) {
  const file = event.target.files?.[0]
  if (!file) return
  event.target.value = ''
  const syllabusMonHocId = selected.syllabusMonHoc?.id
  if (!syllabusMonHocId) return
  fileSyllabusUploading.value = true
  fileSyllabusThongBao.value = ''
  try {
    await services.syllabusMonHocFile.upload(syllabusMonHocId, file)
    fileSyllabusThongBao.value = `Đã tải lên file "${file.name}".`
    fileSyllabusLoaiThongBao.value = 'success'
    await taiDuLieuTheoSyllabusMonHocApDung(selected.syllabusMonHoc)
  } catch (e) {
    fileSyllabusThongBao.value = `Lỗi tải file: ${layThongBaoLoi(e)}`
    fileSyllabusLoaiThongBao.value = 'error'
  } finally {
    fileSyllabusUploading.value = false
  }
}

function xemFileSyllabus(fileId) {
  router.push({
    name: 'DaoTao.XemChuongTrinh.SyllabusTep',
    params: route.params,
    query: {
      ...route.query,
      tepNguon: 'syllabusMonHocFile',
      syllabusMonHocFileId: fileId,
      fileIndex: 0
    }
  })
}

// ===== END FILE SYLLABUS MÔN HỌC =====

</script>
<style scoped>
/* ===== KHUNG KỲ GỢI Ý PANEL ===== */
.khung-ky-goi-y-panel {
  margin: 8px 0 12px;
  padding: 10px 14px;
  border-radius: 6px;
  background: #fef9ec;
  border: 1px solid #f0c040;
  font-size: 13px;
  display: flex;
  flex-direction: column;
  gap: 6px;
}
.goi-y-loading { color: #888; font-style: italic; }
.goi-y-status {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
}
.goi-y-status.goi-y-ok { color: #2a7a2a; }
.goi-y-status.goi-y-warn { color: #b05e00; }
.goi-y-msg { font-weight: 500; }
.goi-y-count {
  font-size: 12px;
  background: rgba(0,0,0,.07);
  border-radius: 10px;
  padding: 2px 8px;
}
.goi-y-next {
  display: flex;
  align-items: center;
  gap: 6px;
  flex-wrap: wrap;
  font-size: 13px;
}
.goi-y-next .btn.small { padding: 3px 10px; font-size: 12px; margin-left: 6px; }
.goi-y-list {
  display: flex;
  gap: 6px;
  flex-wrap: wrap;
}
.goi-y-ky-badge {
  display: inline-block;
  padding: 2px 10px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 600;
  cursor: default;
}
.goi-y-ky-badge.da-tao  { background: #d4f5d4; color: #1a6b1a; border: 1px solid #7ec87e; }
.goi-y-ky-badge.chua-tao { background: #f0f0f0; color: #888; border: 1px dashed #bbb; }
/* ===== END KHUNG KỲ GỢI Ý PANEL ===== */

/* ===== CẢNH BÁO TẢI HỌC PANEL ===== */
.tai-hoc-panel {
  margin: 4px 0 12px;
  border-radius: 6px;
  border: 1px solid #d0d8e8;
  background: #f8fafc;
  font-size: 13px;
  overflow: hidden;
}
.tai-hoc-header {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 14px;
  cursor: pointer;
  user-select: none;
  background: #eef2f8;
  flex-wrap: wrap;
}
.tai-hoc-header:hover { background: #e4eaf5; }
.tai-hoc-title { font-weight: 600; color: #344; margin-right: 4px; }
.tai-hoc-summary { display: flex; gap: 4px; flex-wrap: wrap; flex: 1; }
.tai-hoc-toggle { margin-left: auto; color: #888; font-size: 11px; }
.tai-canh-bao-chung {
  display: flex;
  flex-direction: column;
  gap: 3px;
  padding: 8px 14px;
  background: #fff8e1;
  border-bottom: 1px solid #f0c040;
}
.tai-cb-item { color: #7a4a00; font-size: 12px; }
.tai-tong-hop {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 6px 14px;
  font-size: 12px;
  color: #445;
  flex-wrap: wrap;
  border-bottom: 1px solid #e0e6ef;
}
.tai-nguong { color: #888; margin-left: auto; }
.tai-hoc-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 12px;
}
.tai-hoc-table th {
  background: #e8edf5;
  padding: 5px 10px;
  text-align: left;
  font-weight: 600;
  color: #344;
  border-bottom: 1px solid #ccd4e0;
}
.tai-hoc-table td { padding: 5px 10px; border-bottom: 1px solid #eef0f4; }
.tai-row:last-child td { border-bottom: none; }
.tai-row-qua_tai { background: #fff0f0; }
.tai-row-nhe_tai { background: #fffaf0; }
.tai-row-chua_tao_ky { background: #f5f5f5; color: #aaa; }
.tai-badge {
  display: inline-block;
  padding: 1px 8px;
  border-radius: 10px;
  font-size: 11px;
  font-weight: 600;
  white-space: nowrap;
}
.tai-binh_thuong { background: #d4f5d4; color: #1a6b1a; border: 1px solid #7ec87e; }
.tai-qua_tai     { background: #ffd6d6; color: #a00;    border: 1px solid #f99; }
.tai-nhe_tai     { background: #fff0cc; color: #7a4a00; border: 1px solid #f0c060; }
.tai-chua_tao_ky { background: #f0f0f0; color: #888;    border: 1px dashed #bbb; }
.tai-chua_xep_ky { background: #e8f0ff; color: #3355aa; border: 1px solid #99b3ee; }
/* ===== END CẢNH BÁO TẢI HỌC PANEL ===== */

.xay-dung-page {
  box-sizing: border-box;
  width: 100%;
  max-width: 100%;
  min-width: 0;
  min-height: calc(100vh - 56px);
  padding: 12px;
  background: #f3f4f6;
  color: #111827;
  font-family: Arial, Helvetica, sans-serif;
  overflow-x: hidden;
}

.xay-dung-page *,
.xay-dung-page *::before,
.xay-dung-page *::after {
  box-sizing: border-box;
}

.page-head,
.page-foot {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  padding: 12px;
  border: 1px solid #d1d5db;
  border-radius: 4px;
  background: #ffffff;
  margin-bottom: 10px;
}

.page-head h2 {
  margin: 0;
  font-size: 18px;
  font-weight: 700;
}

.page-head p {
  margin: 4px 0 0;
  color: #6b7280;
  font-size: 12px;
}

.head-actions {
  display: flex;
  gap: 6px;
}

.btn {
  min-height: 28px;
  border: 1px solid #cbd5e1;
  border-radius: 3px;
  background: #ffffff;
  color: #111827;
  padding: 4px 10px;
  font-size: 12px;
  line-height: 16px;
  cursor: pointer;
}

.btn:hover {
  background: #f8fafc;
}

.btn.primary {
  border-color: #2563eb;
  background: #2563eb;
  color: #ffffff;
}

.notice {
  margin-bottom: 10px;
  padding: 8px 10px;
  border: 1px solid #bbf7d0;
  border-radius: 4px;
  background: #f0fdf4;
  color: #166534;
  font-size: 13px;
}

.notice.error {
  border-color: #fecaca;
  background: #fef2f2;
  color: #b91c1c;
}

.flow-summary {
  position: sticky;
  top: calc(var(--header-height) + 12px);
  z-index: 20;

  display: flex;
  flex-wrap: wrap;
  gap: 6px 10px;
  overflow-x: auto;
  margin-bottom: 10px;
  padding: 8px;
  border: 1px solid #bfdbfe;
  border-radius: 4px;
  background: #eff6ff;
  font-size: 12px;
  white-space: normal;
  box-shadow: 0 2px 6px rgba(15, 23, 42, 0.08);
}

.breadcrumb-flow {
  align-items: center;
}

.breadcrumb-pill {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  max-width: 260px;
  border: 1px solid #bfdbfe;
  border-radius: 999px;
  background: #ffffff;
  color: #1e40af;
  padding: 5px 9px;
  font-size: 12px;
  line-height: 1.25;
  cursor: pointer;
  transition: background .15s ease, border-color .15s ease, color .15s ease, transform .15s ease;
}

.breadcrumb-pill span {
  color: #475569;
  font-weight: 600;
  white-space: nowrap;
}

.breadcrumb-pill b {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.breadcrumb-pill:hover:not(:disabled) {
  border-color: #2563eb;
  background: #dbeafe;
  color: #1e3a8a;
  transform: translateY(-1px);
}

.breadcrumb-pill.active:not(:disabled) {
  border-color: #1d4ed8;
  background: #bfdbfe;
  color: #1e3a8a;
}

.breadcrumb-pill.disabled,
.breadcrumb-pill:disabled {
  border-color: #e5e7eb;
  background: #f8fafc;
  color: #94a3b8;
  cursor: not-allowed;
}

.breadcrumb-pill.disabled span,
.breadcrumb-pill:disabled span {
  color: #94a3b8;
}

.flow-group {
  max-width: 100%;
  min-width: 0;
  margin-bottom: 10px;
  padding: 10px;
  border: 1px solid #cbd5e1;
  border-radius: 4px;
  background: #e5e7eb;
}

.group-title {
  max-width: 100%;
  min-width: 0;
  margin-bottom: 8px;
  padding: 8px 10px;
  border: 1px solid #d1d5db;
  border-radius: 4px;
  background: #ffffff;
}

.group-title h3 {
  margin: 0;
  font-size: 14px;
}

.group-title p {
  margin: 4px 0 0;
  color: #4b5563;
  font-size: 12px;
}

.mini-scroll {
  scrollbar-width: thin;
  scrollbar-color: #9ca3af #f3f4f6;
}

.mini-scroll::-webkit-scrollbar {
  height: 6px;
  width: 6px;
}

.mini-scroll::-webkit-scrollbar-thumb {
  background: #9ca3af;
  border-radius: 999px;
}

.mini-scroll::-webkit-scrollbar-track {
  background: #f3f4f6;
}

@media (max-width: 800px) {
  .page-head,
  .page-foot {
    align-items: flex-start;
    flex-direction: column;
  }
}

.bang-chuong-trinh-mon-tang-7 :deep(.col-action-wide) {
  width: 120px;
  min-width: 120px;
  max-width: 120px;
  white-space: nowrap;
}

.dao-tao-xem-page .flow-group.mau-xam :deep(.col-action-wide) {
  width: 155px;
  min-width: 155px;
  max-width: 155px;
  white-space: nowrap;
}
</style>


<style scoped>


.tang-nav {
  display: flex;
  flex-wrap: nowrap;
  gap: 8px;
  overflow-x: auto;
  margin-bottom: 10px;
  padding: 8px;
  border: 1px solid #dbeafe;
  border-radius: 6px;
  background: #ffffff;
}

.tang-btn {
  flex: 0 0 135px;
  min-height: 58px;
  border: 1px solid #d1d5db;
  border-radius: 6px;
  background: #f8fafc;
  color: #334155;
  padding: 8px;
  text-align: left;
  cursor: pointer;
}

.tang-btn b,
.tang-btn span {
  display: block;
}

.tang-btn b {
  font-size: 11px;
}

.tang-btn span {
  margin-top: 3px;
  font-size: 12px;
}

.tang-btn.active {
  border-color: #2563eb;
  background: #dbeafe;
  color: #1e3a8a;
}

.tang-current {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  margin-bottom: 10px;
  padding: 12px;
  border: 1px solid #bfdbfe;
  border-radius: 6px;
  background: #eff6ff;
}

.tang-current h3 {
  margin: 0;
  font-size: 15px;
}

.tang-current p {
  margin: 4px 0 0;
  color: #475569;
  font-size: 12px;
}

.continue-panel {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  margin: 10px 8px;
  padding: 10px 12px;
  border: 1px solid #bfdbfe;
  border-radius: 6px;
  background: #eff6ff;
}

.continue-panel div {
  display: flex;
  flex-direction: column;
  gap: 3px;
  color: #1e3a8a;
  font-size: 12px;
}

.continue-panel b {
  font-size: 13px;
}

.continue-actions {
  display: flex;
  flex-wrap: wrap;
  justify-content: flex-end;
  gap: 8px;
}

.continue-panel .deep-action {
  min-width: 180px;
  border-color: #1d4ed8;
  background: #1d4ed8;
  color: #ffffff;
  font-weight: 700;
}

.continue-panel .deep-action:hover:not(:disabled) {
  border-color: #1e40af;
  background: #1e40af;
}

.continue-panel .secondary-action {
  border-color: #cbd5e1;
  background: #ffffff;
  color: #0f172a;
  font-weight: 600;
}

.continue-panel .secondary-action:hover:not(:disabled) {
  border-color: #2563eb;
  background: #eff6ff;
  color: #1d4ed8;
}

.tang-actions {
  display: flex;
  gap: 6px;
}

.tang-note {
  max-width: 360px;
  color: #475569;
  font-size: 12px;
  text-align: right;
}

.flow-group.mau-xanh {
  background: #e0f2fe;
  border-color: #bae6fd;
}

.flow-group.mau-tim {
  background: #ede9fe;
  border-color: #ddd6fe;
}

.flow-group.mau-luc {
  background: #dcfce7;
  border-color: #bbf7d0;
}

.flow-group.mau-cam {
  background: #ffedd5;
  border-color: #fed7aa;
}

.flow-group.mau-hong {
  background: #fce7f3;
  border-color: #fbcfe8;
}

.flow-group.mau-vang {
  background: #fef9c3;
  border-color: #fde68a;
}

.flow-group.mau-nmau {
  background: #ccfbf1;
  border-color: #99f6e4;
}

.flow-group.mau-troi {
  background: #dbeafe;
  border-color: #bfdbfe;
}

.flow-group.mau-xam {
  background: #e5e7eb;
  border-color: #cbd5e1;
}


.bang-da-gan-tang-2 {
  margin: 12px 0 14px;
  border: 1px solid #c4b5fd;
  border-radius: 6px;
  background: #ffffff;
  overflow: hidden;
}

.form-nganh-he {
  display: grid;
  grid-template-columns: 1fr 1fr 80px 80px 1fr 1fr;
  gap: 8px 10px;
  padding: 12px 14px 10px;
  align-items: end;
  border-bottom: 1px solid #e5e7eb;
}

.form-nganh-he label {
  display: flex;
  flex-direction: column;
  gap: 3px;
  font-size: 12px;
  color: #6b7280;
}

.form-nganh-he input,
.form-nganh-he select {
  border: 1px solid #d1d5db;
  border-radius: 4px;
  padding: 4px 7px;
  font-size: 13px;
}

.hang-nut-nganh-he {
  display: flex;
  gap: 6px;
  align-items: flex-end;
  padding-bottom: 1px;
}

.trang-thai-on {
  color: #16a34a;
  font-size: 11px;
  background: #f0fdf4;
  padding: 2px 6px;
  border-radius: 4px;
}

.trang-thai-off {
  color: #9ca3af;
  font-size: 11px;
  background: #f3f4f6;
  padding: 2px 6px;
  border-radius: 4px;
}

.bang-da-gan-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  padding: 10px 12px;
  border-bottom: 1px solid #ede9fe;
  background: #f5f3ff;
}

.bang-da-gan-head h4 {
  margin: 0;
  color: #4c1d95;
  font-size: 14px;
}

.bang-da-gan-head p {
  margin: 4px 0 0;
  color: #5b21b6;
  font-size: 12px;
}

.bang-da-gan-tang-2 table {
  width: 100%;
  min-width: 900px;
  border-collapse: collapse;
  font-size: 12px;
}

.bang-da-gan-tang-2 th,
.bang-da-gan-tang-2 td {
  border-top: 1px solid #e5e7eb;
  padding: 7px 8px;
  text-align: left;
  vertical-align: top;
}

.bang-da-gan-tang-2 th {
  background: #f3f4f6;
  color: #374151;
  font-size: 11px;
  font-weight: 700;
}

.pill-da-gan {
  display: inline-flex;
  align-items: center;
  border: 1px solid #86efac;
  border-radius: 999px;
  background: #dcfce7;
  color: #166534;
  padding: 2px 8px;
  font-size: 11px;
  font-weight: 700;
}


.vi-tri-hien-tai {
  margin: 10px 0 12px;
  padding: 9px 12px;
  border: 1px solid #bfdbfe;
  border-radius: 6px;
  background: #eff6ff;
  color: #1d4ed8;
  font-size: 13px;
}

.vi-tri-hien-tai b {
  margin-right: 6px;
}

@media (max-width: 1000px) {
  .tang-nav {
    /* flex layout — không cần override grid-template-columns */
  }

  .tang-current,
  .continue-panel {
    align-items: flex-start;
    flex-direction: column;
  }

  .continue-actions {
    justify-content: flex-start;
    width: 100%;
  }

  .continue-panel .deep-action,
  .continue-panel .secondary-action {
    min-width: 0;
  }
}

.notice {
  margin-bottom: 12px;
  padding: 10px 12px;
  border-radius: 10px;
  font-weight: 600;
}

.notice.success {
  color: #166534;
  background: #dcfce7;
  border: 1px solid #86efac;
}

.notice.error {
  color: #991b1b;
  background: #fee2e2;
  border: 1px solid #fca5a5;
}

.table-message {
  margin: 8px 0 10px;
  padding: 8px 10px;
  border-radius: 8px;
  font-size: 13px;
  font-weight: 600;
}

.table-message.success {
  color: #166534;
  background: #dcfce7;
  border: 1px solid #86efac;
}

.table-message.error {
  color: #991b1b;
  background: #fee2e2;
  border: 1px solid #fca5a5;
}

.mon-hoc-tang-7-filter {
  display: flex;
  align-items: end;
  justify-content: space-between;
  gap: 10px;
  padding: 10px 12px;
  border: 1px solid #dbeafe;
  border-bottom: 0;
  border-radius: 4px 4px 0 0;
  background: #eff6ff;
}

.mon-hoc-tang-7-filter label {
  display: grid;
  gap: 4px;
  min-width: 320px;
}

.mon-hoc-tang-7-filter span {
  color: #1e3a8a;
  font-size: 12px;
  font-weight: 700;
}

.mon-hoc-tang-7-filter input {
  min-height: 30px;
  border: 1px solid #93c5fd;
  border-radius: 4px;
  padding: 5px 8px;
  background: #ffffff;
  color: #111827;
  font-size: 13px;
}


.bang-tang-wrapper {
  width: 100%;
}

.mon-hoc-tang-7-filter {
  display: flex;
  align-items: end;
  justify-content: space-between;
  gap: 10px;
  padding: 10px 12px;
  border: 1px solid #dbeafe;
  border-bottom: 0;
  border-radius: 4px 4px 0 0;
  background: #eff6ff;
}

.mon-hoc-tang-7-filter label {
  display: grid;
  gap: 4px;
  min-width: 320px;
}

.mon-hoc-tang-7-filter span {
  color: #1e3a8a;
  font-size: 12px;
  font-weight: 700;
}

.mon-hoc-tang-7-filter input {
  min-height: 30px;
  border: 1px solid #93c5fd;
  border-radius: 4px;
  padding: 5px 8px;
  background: #ffffff;
  color: #111827;
  font-size: 13px;
}

.mon-hoc-tang-7-filter small {
  color: #475569;
  font-size: 12px;
}

.bang-tang-wrapper {
  width: 100%;
}

.bang-mon-hoc-tang-7-scroll {
  border: 1px solid #dbeafe;
  border-radius: 0 0 4px 4px;
  background: #ffffff;
  overflow: visible;
}

/* Chỉ cuộn vùng dữ liệu bảng môn học mẫu, không cắt còn 10 dòng */
.bang-mon-hoc-tang-7-scroll :deep(.table-wrap),
.bang-mon-hoc-tang-7-scroll :deep(.bang-table-wrap),
.bang-mon-hoc-tang-7-scroll :deep(.data-table-wrap) {
  max-height: 420px;
  overflow-y: auto;
  overflow-x: auto;
}

/* Giữ header/cột tiêu đề đứng yên khi cuộn hàng */
.bang-mon-hoc-tang-7-scroll :deep(thead th) {
  position: sticky;
  top: 0;
  z-index: 5;
  background: #f8fafc;
}


/* Layout fix: giữ trang trong viewport, bảng rộng thì cuộn ngang trong vùng bảng */
.dao-tao-xem-page {
  max-width: 100%;
  min-width: 0;
  overflow-x: clip;
  overflow-y: visible;
}

.dao-tao-xem-page .tang-nav,
.dao-tao-xem-page .flow-summary,
.dao-tao-xem-page .flow-group,
.dao-tao-xem-page .group-title,
.dao-tao-xem-page .bang-tang-wrapper,
.dao-tao-xem-page .bang-da-gan-tang-2,
.dao-tao-xem-page .continue-panel,
.dao-tao-xem-page .mon-hoc-tang-7-filter {
  max-width: 100%;
  min-width: 0;
}

.dao-tao-xem-page .tang-nav,
.dao-tao-xem-page .flow-summary {
  overflow-x: auto;
  overflow-y: hidden;
}

.dao-tao-xem-page .bang-tang-wrapper {
  overflow-x: auto;
  overflow-y: visible;
}

.dao-tao-xem-page :deep(.bang-them-nghiep-vu),
.dao-tao-xem-page :deep(.bang-head),
.dao-tao-xem-page :deep(.bang-form),
.dao-tao-xem-page :deep(.readonly-info-form),
.dao-tao-xem-page :deep(.parent-text),
.dao-tao-xem-page :deep(.disabled-text),
.dao-tao-xem-page :deep(.table-title),
.dao-tao-xem-page :deep(.table-message),
.dao-tao-xem-page :deep(.save-status),
.dao-tao-xem-page :deep(.table-wrap) {
  max-width: 100%;
  min-width: 0;
}

.dao-tao-xem-page :deep(.table-wrap) {
  display: block;
  width: 100%;
  overflow-x: auto;
  overflow-y: auto;
}

.dao-tao-xem-page :deep(table),
.dao-tao-xem-page .table-wrap table {
  width: max-content;
  min-width: 100%;
  max-width: none;
  table-layout: auto;
}

.dao-tao-xem-page :deep(th),
.dao-tao-xem-page :deep(td),
.dao-tao-xem-page .table-wrap th,
.dao-tao-xem-page .table-wrap td {
  max-width: 360px;
  overflow-wrap: anywhere;
  word-break: break-word;
}

.dao-tao-xem-page :deep(.content-cell) {
  min-width: 320px;
  max-width: 520px;
}

.dao-tao-xem-page :deep(.actions-cell),
.dao-tao-xem-page :deep(.action-pair),
.dao-tao-xem-page .actions-cell {
  width: max-content;
  min-width: max-content;
  white-space: nowrap;
}

.dao-tao-xem-page .bang-da-gan-tang-2 {
  overflow: hidden;
}

.dao-tao-xem-page .bang-da-gan-tang-2 .table-wrap {
  display: block;
  width: 100%;
  max-width: 100%;
  overflow-x: auto;
  overflow-y: auto;
}

.dao-tao-xem-page .bang-da-gan-tang-2 table {
  width: 100%;
  min-width: 900px;
  max-width: none;
  table-layout: fixed;
}

.bang-da-gan-tang-2 th:nth-child(1) { width: 14%; }
.bang-da-gan-tang-2 th:nth-child(2) { width: 18%; }
.bang-da-gan-tang-2 th:nth-child(3) { width: 11%; }
.bang-da-gan-tang-2 th:nth-child(4) { width: 8%; }
.bang-da-gan-tang-2 th:nth-child(5) { width: 20%; }
.bang-da-gan-tang-2 th:nth-child(6) { width: 10%; }
.bang-da-gan-tang-2 th:nth-child(7) { width: 19%; }

.bang-da-gan-tang-2 td {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}


.syllabus-ap-dung-toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  margin: 8px 0 10px;
  padding: 10px 12px;
  border: 1px solid #bfdbfe;
  border-radius: 8px;
  background: #eff6ff;
}

.syllabus-ap-dung-toolbar b {
  display: block;
  color: #1e3a8a;
  margin-bottom: 2px;
}

.syllabus-ap-dung-toolbar span {
  color: #475569;
  font-size: 12px;
}

.syllabus-ap-dung-toolbar-actions {
  display: flex;
  align-items: center;
  gap: 8px;
  white-space: nowrap;
}

@media (max-width: 900px) {
  .syllabus-ap-dung-toolbar {
    align-items: stretch;
    flex-direction: column;
  }

  .syllabus-ap-dung-toolbar-actions {
    justify-content: flex-start;
    flex-wrap: wrap;
    white-space: normal;
  }
}

@media (max-width: 900px) {
  .dao-tao-xem-page .mon-hoc-tang-7-filter,
  .bang-da-gan-head {
    align-items: stretch;
    flex-direction: column;
  }

  .dao-tao-xem-page .mon-hoc-tang-7-filter label {
    width: 100%;
    min-width: 0;
  }
}
.actions-cell .view-btn {
  background: #16a34a;
  color: #fff;
  border-color: #16a34a;
}

.actions-cell .view-btn:hover {
  background: #15803d;
  border-color: #15803d;
}

/* Không dùng padding-top nữa vì summary dùng sticky, không fixed */
.dao-tao-xem-page {
  padding-top: 0;
}

/* Thanh tầng nằm bình thường dưới tab Ngành/Chương trình, cuộn thì mất */
.dao-tao-xem-page > .tang-nav {
  margin-bottom: 2px;
}

/* Bỏ chữ Tầng 1, Tầng 2... cho gọn */
.dao-tao-xem-page > .tang-nav .tang-btn b {
  display: none;
}

.dao-tao-xem-page > .tang-nav .tang-btn {
  min-height: 26px;
  padding: 4px 10px;
}

.dao-tao-xem-page > .tang-nav .tang-btn span {
  font-size: 12px;
  line-height: 16px;
  white-space: nowrap;
}

/* Summary nằm bình thường dưới thanh tầng.
   Khi cuộn xuống mới dính ở top, không đè tab quản lý đào tạo. */
.dao-tao-sticky-flow-header {
  position: sticky;
  top: 60px;
  z-index: 999;
  padding: 0;
  margin: 0 0 4px;
  background: #f8fafc;
  border-bottom: 1px solid #bfdbfe;
  box-shadow: 0 3px 10px rgba(15, 23, 42, 0.12);
}

/* Thanh summary gọn lại */
.dao-tao-sticky-flow-header .flow-summary.breadcrumb-flow {
  margin: 0;
  padding: 2px 6px;
  min-height: 24px;
  max-width: 100%;
  overflow-x: auto;
  overflow-y: hidden;
  background: #eaf4ff;
  border: 1px solid #93c5fd;
}

/* Pill trong summary gọn lại */
.dao-tao-sticky-flow-header .breadcrumb-pill {
  min-height: 22px;
  padding: 3px 8px;
  font-size: 11px;
  line-height: 15px;
  white-space: nowrap;
}

/* Nội dung ngay dưới summary, không cách quá xa */
.dao-tao-xem-page > .flow-group:first-of-type {
  margin-top: 6px;
}

/* Tầng 5 (tong-quan): thu nhỏ cột trạng thái ở các bảng mẫu */
.dao-tao-xem-page .flow-group.mau-hong :deep(.col-action-wide) {
  width: 90px;
  min-width: 90px;
  max-width: 90px;
  white-space: nowrap;
}

/* ===== FILE SYLLABUS PANEL ===== */
.bang-header-row {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 6px 8px;
  background: #f1f5f9;
  border: 1px solid #cbd5e1;
  border-bottom: none;
  border-radius: 4px 4px 0 0;
}

.bang-header-title {
  font-size: 0.85rem;
  font-weight: 600;
  color: #374151;
  flex: 1;
}

.file-loading-text {
  font-size: 0.78rem;
  color: #64748b;
  font-style: italic;
}

.bang-tieu-de {
  font-size: 0.85rem;
  font-weight: 600;
  color: #374151;
  padding: 6px 8px;
  background: #f1f5f9;
  border: 1px solid #cbd5e1;
  border-bottom: none;
  border-radius: 4px 4px 0 0;
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 4px;
}

.bang-rong-text {
  font-size: 0.82rem;
  color: #94a3b8;
  padding: 8px 10px;
  border: 1px solid #cbd5e1;
  background: #fff;
  border-radius: 0 0 4px 4px;
}

.bang-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 0.83rem;
  border: 1px solid #cbd5e1;
  border-radius: 0 0 4px 4px;
  overflow: hidden;
}

.bang-table th {
  background: #f8fafc;
  font-weight: 600;
  padding: 5px 8px;
  border-bottom: 1px solid #cbd5e1;
  text-align: left;
  font-size: 0.8rem;
  color: #4b5563;
}

.bang-table td {
  padding: 5px 8px;
  border-bottom: 1px solid #e5e7eb;
  vertical-align: middle;
}

.bang-table tbody tr:last-child td {
  border-bottom: none;
}

.bang-table tbody tr:hover {
  background: #f8fafc;
}

.file-ten-col {
  max-width: 220px;
  word-break: break-word;
}

.file-action-cell {
  display: flex;
  gap: 4px;
  white-space: nowrap;
}

.badge-da-gan {
  font-size: 0.78rem;
  color: #16a34a;
  font-weight: 600;
  background: #dcfce7;
  padding: 2px 7px;
  border-radius: 10px;
  border: 1px solid #bbf7d0;
}

.btn.danger {
  background: #ef4444;
  color: #fff;
  border-color: #ef4444;
}

.btn.danger:hover:not(:disabled) {
  background: #dc2626;
}
/* Ví dụ cho nút có class .save-status */
/* Ví dụ cho nút có class .save-status */
.save-status {
  display: inline-block;       /* để nó co theo nội dung */
  background-color: #2563eb;   /* xanh dương */
  color: #fff;
  border: none;
  border-radius: 8px;          /* bo tròn góc */
  padding: 4px 10px;           /* giảm padding cho gọn */
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: background-color 0.2s ease;
  width: auto;                 /* bỏ width 100% nếu có */
  min-width: unset;            /* bỏ ép min-width */
}

.save-status:hover {
  background-color: #1d4ed8;   /* xanh đậm hơn khi hover */
}

/* ===== END FILE SYLLABUS PANEL ===== */
</style>