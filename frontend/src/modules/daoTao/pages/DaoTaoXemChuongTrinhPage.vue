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

    <section class="tang-current">
      <div>
        <h3>Tầng {{ thongTinTangHienTai.so }}: {{ thongTinTangHienTai.ten }}</h3>
        <p>{{ thongTinTangHienTai.moTa }}</p>
      </div>
      <div class="tang-note">Dùng thanh điều hướng nhanh bên dưới để đi tiếp hoặc quay lại tầng trước.</div>
    </section>

    <section v-if="tangHienTai >= 6 && tangHienTai <= 8" class="vi-tri-hien-tai">
      <b>Đang ở:</b>
      <span>{{ viTriHienTaiText }}</span>
    </section>


    <section v-for="group in groups" :key="group.key" :class="['flow-group', group.mau]">
      <div class="group-title">
        <h3>{{ group.title }}</h3>
        <p>{{ group.description }}</p>
      </div>

      <template v-for="bang in group.tables" :key="bang.key">
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
            @select="chonDongBang(bang, $event)"
            @view="xemDongBang(bang, $event)"
            @toggle-save="luuDongBang(bang, $event)"
            @saved="sauKhiLuu(bang.key, $event)"
            @deleted="sauKhiXoa(bang.key, $event)"
            @notify="xuLyThongBaoBang(bang, $event)"
        />

        <div v-if="tangHienTai === 2 && bang.key === 'trinhDoDaoTao'" class="bang-da-gan-tang-2">
          <div class="bang-da-gan-head">
            <div>
              <h4>2.1.1. Trình độ đào tạo theo ngành</h4>
              <p>Danh sách trình độ đào tạo đã được gán cho ngành đang xem.</p>
            </div>
            <button type="button" class="btn tiny" @click="taiDuLieuCoSanTatCaBang">Tải lại</button>
          </div>

          <div v-if="tableMessages.nganhTrinhDoDaoTao?.message"
               :class="['table-message', tableMessages.nganhTrinhDoDaoTao.type]">
            {{ tableMessages.nganhTrinhDoDaoTao.message }}
          </div>

          <div class="table-wrap mini-scroll">
            <table>
              <thead>
              <tr>
                <th>Ngành</th>
                <th>Mã trình độ</th>
                <th>Trình độ đào tạo</th>
                <th>Mô tả trình độ</th>
                <th>Trạng thái</th>
                <th>Ghi chú</th>
                <th>Ngày tạo</th>
                <th>Ngày cập nhật</th>
                <th class="col-action">Thao tác</th>
              </tr>
              </thead>
              <tbody>
              <tr v-if="!cauHinhTrinhDoTheoNganh.length">
                <td colspan="9" class="empty-cell">Chưa có trình độ đào tạo nào được gán cho ngành này.</td>
              </tr>
              <tr v-for="item in cauHinhTrinhDoTheoNganh" :key="`nganh-trinh-do-${item.id}`">
                <td>{{ item.tenNganh || selected.nganh?.tenNganh || item.nganhId }}</td>
                <td>{{ item.maTrinhDo || maTrinhDoTheoId(item.trinhDoId) }}</td>
                <td>{{ item.tenTrinhDo || tenTrinhDoTheoId(item.trinhDoId) }}</td>
                <td>{{ item.moTaTrinhDo || '-' }}</td>
                <td>{{ item.trangThai || '-' }}</td>
                <td>{{ item.ghiChu || '-' }}</td>
                <td>{{ item.createdAt || '-' }}</td>
                <td>{{ item.updatedAt || '-' }}</td>
                <td class="actions-cell">
                  <button type="button" class="btn tiny view-btn" @click="xemTrinhDoTheoNganh(item)">Xem</button>
                  <button type="button" class="btn tiny danger" @click="xoaTrinhDoTheoNganh(item)">Xóa</button>
                </td>
              </tr>
              </tbody>
            </table>
          </div>
        </div>

        <div v-if="tangHienTai === 2 && bang.key === 'loaiChuongTrinh'" class="bang-da-gan-tang-2">
          <div class="bang-da-gan-head">
            <div>
              <h4>2.2.1. Loại chương trình theo ngành</h4>
              <p>Danh sách loại chương trình đã được gán cho ngành đang xem.</p>
            </div>
            <button type="button" class="btn tiny" @click="taiDuLieuCoSanTatCaBang">Tải lại</button>
          </div>

          <div v-if="tableMessages.nganhLoaiChuongTrinh?.message"
               :class="['table-message', tableMessages.nganhLoaiChuongTrinh.type]">
            {{ tableMessages.nganhLoaiChuongTrinh.message }}
          </div>

          <div class="table-wrap mini-scroll">
            <table>
              <thead>
              <tr>
                <th>Ngành</th>
                <th>Mã loại</th>
                <th>Loại chương trình</th>
                <th>Số tháng</th>
                <th>Số kỳ</th>
                <th>Mô tả loại</th>
                <th>Trạng thái</th>
                <th>Ghi chú</th>
                <th>Ngày tạo</th>
                <th>Ngày cập nhật</th>
                <th class="col-action">Thao tác</th>
              </tr>
              </thead>
              <tbody>
              <tr v-if="!cauHinhLoaiChuongTrinhTheoNganh.length">
                <td colspan="11" class="empty-cell">Chưa có loại chương trình nào được gán cho ngành này.</td>
              </tr>
              <tr v-for="item in cauHinhLoaiChuongTrinhTheoNganh" :key="`nganh-loai-ct-${item.id}`">
                <td>{{ item.tenNganh || selected.nganh?.tenNganh || item.nganhId }}</td>
                <td>{{ item.maLoai || maLoaiChuongTrinhTheoId(item.loaiChuongTrinhId) }}</td>
                <td>{{ item.tenLoaiChuongTrinh || tenLoaiChuongTrinhTheoId(item.loaiChuongTrinhId) }}</td>
                <td>{{ item.soThang || '-' }}</td>
                <td>{{ item.soKy || '-' }}</td>
                <td>{{ item.moTaLoai || '-' }}</td>
                <td>{{ item.trangThai || '-' }}</td>
                <td>{{ item.ghiChu || '-' }}</td>
                <td>{{ item.createdAt || '-' }}</td>
                <td>{{ item.updatedAt || '-' }}</td>
                <td class="actions-cell">
                  <button type="button" class="btn tiny view-btn" @click="xemLoaiChuongTrinhTheoNganh(item)">Xem
                  </button>
                  <button type="button" class="btn tiny danger" @click="xoaLoaiChuongTrinhTheoNganh(item)">Xóa</button>
                </td>
              </tr>
              </tbody>
            </table>
          </div>
        </div>
      </template>
    </section>

    <div v-if="cacNutDieuHuongTang.length && tangHienTai !== 2" class="continue-panel continue-panel-bottom">
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
import {computed, nextTick, watch} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import BangThemNghiepVu from '../components/BangThemNghiepVu.vue'
import {useDaoTaoXemChuongTrinh} from '../composables/useDaoTaoXemChuongTrinh'
import {layThongBaoLoi} from '../utils/layThongBaoLoi'

const route = useRoute()
const router = useRouter()

const {
  thongBao,
  loaiThongBao,
  tableMessages,
  selected,
  viewed,
  duLieu,
  quyDoiDiemMauGocRows,
  lookups,
  services,
  baoTin,
  selectEntity,
  viewEntity,
  chonBangPhuVaoLuong,
  toggleLuuBangPhu,
  taiDuLieuCoSanTatCaBang,
  sauKhiLuu,
  sauKhiXoa,
  lamMoiLuon,
  luuChuongTrinhTong
} = useDaoTaoXemChuongTrinh()

const chuongTrinhNganhLoaiDangChon = computed(() => selected.nganhLoaiChuongTrinh || null)

const chuongTrinhTrinhDoDangChon = computed(() => {
  if (selected.trinhDoDaoTao?.id) return selected.trinhDoDaoTao

  if (cauHinhTrinhDoTheoNganh.value.length === 1) {
    return timTrinhDoTheoId(cauHinhTrinhDoTheoNganh.value[0].trinhDoId)
  }

  return null
})

const chuongTrinhParent = computed(() => {
  const nganhLoai = chuongTrinhNganhLoaiDangChon.value
  const trinhDo = chuongTrinhTrinhDoDangChon.value

  return {
    nganhLoaiChuongTrinhId: nganhLoai?.id || null,
    nganhId: nganhLoai?.nganhId || selected.nganh?.id || route.params.nganhId || null,
    trinhDoId: trinhDo?.id || null,
    loaiChuongTrinhId: nganhLoai?.loaiChuongTrinhId || selected.loaiChuongTrinh?.id || route.query.loaiChuongTrinhId || null
  }
})

const chuongTrinhParentText = computed(() => {
  const nganhLoai = chuongTrinhNganhLoaiDangChon.value
  const trinhDo = chuongTrinhTrinhDoDangChon.value
  const tenNganh = selected.nganh?.tenNganh || nganhLoai?.tenNganh || '-'
  const tenTrinhDo = trinhDo?.tenTrinhDo || trinhDo?.ten || '-'
  const tenLoai = selected.loaiChuongTrinh?.tenLoai || nganhLoai?.tenLoaiChuongTrinh || '-'

  return `Đang chọn: Ngành ${tenNganh} | Trình độ ${tenTrinhDo} | Loại chương trình ${tenLoai}`
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
  syllabusChuongTrinhGocId: selected.syllabusChuongTrinhGoc?.id || null
}))

const syllabusChuongTrinhParentText = computed(() => {
  return `${versionParentText.value} | Syllabus chương trình gốc ${selected.syllabusChuongTrinhGoc?.ma || '-'} - ${selected.syllabusChuongTrinhGoc?.ten || '-'}`
})

const nhomKienThucParent = computed(() => ({
  chuongTrinhVersionId: selected.chuongTrinhVersion?.id || null,
  nhomKienThucGocId: selected.nhomKienThucGoc?.id || null
}))

const nhomKienThucParentText = computed(() => {
  return `${versionParentText.value} | Nhóm kiến thức gốc ${selected.nhomKienThucGoc?.ma || '-'} - ${selected.nhomKienThucGoc?.ten || '-'}`
})

const nhomTuChonParent = computed(() => ({
  chuongTrinhVersionId: selected.chuongTrinhVersion?.id || null,
  nhomTuChonGocId: selected.nhomTuChonGoc?.id || null
}))

const nhomTuChonParentText = computed(() => {
  return `${versionParentText.value} | Nhóm tự chọn gốc ${selected.nhomTuChonGoc?.ma || '-'} - ${selected.nhomTuChonGoc?.ten || '-'}`
})

const khungKyParent = computed(() => ({
  chuongTrinhVersionId: selected.chuongTrinhVersion?.id || null,
  loaiChuongTrinhId: selected.loaiChuongTrinh?.id || selected.chuongTrinh?.loaiChuongTrinhId || null,
  khungKyGocId: selected.khungKyGoc?.id || null,
  maKy: selected.khungKyGoc?.maKy || '',
  tenKy: selected.khungKyGoc?.tenKy || '',
  thuTu: selected.khungKyGoc?.thuTu || null
}))

const khungKyParentText = computed(() => {
  return `${versionParentText.value} | Loại CT ${selected.loaiChuongTrinh?.tenLoai || selected.chuongTrinh?.tenLoaiChuongTrinh || selected.chuongTrinh?.loaiChuongTrinhId || '-'} | Khung kỳ gốc ${selected.khungKyGoc?.maKy || '-'} - ${selected.khungKyGoc?.tenKy || '-'}`
})

const tang6ManHinh = computed(() => route.query.manHinhTang6 === 'nhom' ? 'nhom' : 'ky')

const khungKyDangChonText = computed(() => {
  return selected.khungKy?.id
      ? `${selected.khungKy.maKy || '-'} - ${selected.khungKy.tenKy || '-'}`
      : 'Chưa chọn kỳ học'
})

const nhomDangChonText = computed(() => {
  if (selected.nhomKienThuc?.id) {
    return `Nhóm kiến thức ${selected.nhomKienThuc.ma || '-'} - ${selected.nhomKienThuc.ten || '-'}`
  }

  return 'Chưa chọn nhóm kiến thức'
})

function layGiaTriHienThi(...values) {
  const value = values.find((item) => item !== null && item !== undefined && item !== '')
  return value || '-'
}

function taoOThongTinNhomTheoKy(loaiNhom) {
  const nhomGocLabel = loaiNhom === 'tuChon' ? 'Nhóm tự chọn' : 'Nhóm KT gốc'
  const nhomGocValue = loaiNhom === 'tuChon'
      ? layGiaTriHienThi(selected.nhomTuChonGoc?.ten, selected.nhomTuChonGoc?.ma)
      : layGiaTriHienThi(selected.nhomKienThucGoc?.ten, selected.nhomKienThucGoc?.ma)

  return [
    {
      key: 'infoNganh',
      label: 'Ngành',
      value: layGiaTriHienThi(selected.nganh?.tenNganh, selected.chuongTrinh?.tenNganh)
    },
    {
      key: 'infoTrinhDo',
      label: 'Trình độ',
      value: layGiaTriHienThi(selected.trinhDoDaoTao?.tenTrinhDo, selected.chuongTrinh?.tenTrinhDo)
    },
    {
      key: 'infoLoaiChuongTrinh',
      label: 'Loại CT',
      value: layGiaTriHienThi(selected.loaiChuongTrinh?.tenLoai, selected.chuongTrinh?.tenLoaiChuongTrinh)
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
      key: 'infoNhomGoc',
      label: nhomGocLabel,
      value: nhomGocValue
    }
  ]
}

const viTriHienTaiText = computed(() => {
  if (tangHienTai.value === 6) {
    return `${versionParentText.value} | Màn hình Kỳ học`
  }

  if (tangHienTai.value === 7) {
    return `${versionParentText.value} | Kỳ ${khungKyDangChonText.value} | Màn hình Nhóm kiến thức của kỳ`
  }

  if (tangHienTai.value === 8) {
    return `${versionParentText.value} | Kỳ ${khungKyDangChonText.value} | ${nhomDangChonText.value} | Màn hình Môn trong nhóm của kỳ`
  }

  return versionParentText.value
})

const monHocParentText = computed(() => {
  return `Đang chọn: Version ${selected.chuongTrinhVersion?.tenVersion || '-'} | Tạo môn học gốc trước khi tạo Chương trình môn`
})

const syllabusMonHocGocParent = computed(() => ({
  monHocId: selected.monHoc?.id || null
}))

const syllabusMonHocGocFilter = computed(() => ({
  monHocId: dongDangDung('monHoc')?.id || null
}))

const syllabusMonHocGocParentText = computed(() => {
  return `Đang chọn: Môn học gốc ${selected.monHoc?.maMon || '-'} - ${selected.monHoc?.tenMon || '-'}`
})

const syllabusMonHocGocChiTietParent = computed(() => ({
  syllabusMonHocGocId: selected.syllabusMonHocGoc?.id || null
}))

const syllabusMonHocGocChiTietFilter = computed(() => ({
  syllabusMonHocGocId: dongDangDung('syllabusMonHocGoc')?.id || null
}))

const syllabusMonHocGocChiTietText = computed(() => {
  return `Đang chọn: Syllabus môn học gốc ${selected.syllabusMonHocGoc?.ma || '-'} - ${selected.syllabusMonHocGoc?.ten || '-'}`
})

const chuongTrinhMonParent = computed(() => ({
  chuongTrinhVersionId: selected.chuongTrinhVersion?.id || null,
  monHocId: selected.monHoc?.id || null,
  khungKyId: selected.khungKy?.id || null,
  nhomKienThucId: selected.nhomKienThuc?.id || null
}))

const chuongTrinhMonTheoNhomFilter = computed(() => ({
  chuongTrinhVersionId: selected.chuongTrinhVersion?.id || null,
  khungKyId: selected.khungKy?.id || null,
  nhomKienThucId: selected.nhomKienThuc?.id || null
}))

const chuongTrinhMonParentText = computed(() => {
  return `Đang chọn: Version ${selected.chuongTrinhVersion?.tenVersion || '-'} | Môn học ${selected.monHoc?.tenMon || '-'} | Kỳ ${selected.khungKy?.tenKy || '-'} | Nhóm KT ${selected.nhomKienThuc?.ten || '-'}`
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
  syllabusMonHocGocId: selected.syllabusMonHocGoc?.id || null
}))

const chuongTrinhMonOnlyParentText = computed(() => {
  return `Đang chọn: Môn trong chương trình ${selected.chuongTrinhMon?.maMonTrongCt || '-'} - ${selected.chuongTrinhMon?.tenMon || '-'}`
})

const monTienQuyetParent = computed(() => ({
  monId: selected.chuongTrinhMon?.id || null
}))

const syllabusMonParent = computed(() => ({
  syllabusMonId: selected.syllabusMonHoc?.id || null
}))

const syllabusMonParentText = computed(() => {
  const tenSyllabus =
      selected.syllabusMonHoc?.ten ||
      selected.syllabusMonHoc?.tenSyllabusMonHocGoc ||
      selected.syllabusMonHoc?.ma ||
      selected.syllabusMonHoc?.maSyllabusMonHocGoc ||
      selected.syllabusMonHoc?.mucTieu ||
      '-'

  return `Đang chọn: Syllabus môn học ${tenSyllabus} | Môn CT ${selected.chuongTrinhMon?.maMonTrongCt || selected.chuongTrinhMon?.tenMon || '-'}`
})

function dongDangDung(key) {
  return viewed[key] || selected[key] || null
}

const chuongTrinhFilter = computed(() => ({
  nganhLoaiChuongTrinhId: dongDangDung('nganhLoaiChuongTrinh')?.id || route.query.nganhLoaiChuongTrinhId || null
}))

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
  khungKyId: dongDangDung('khungKy')?.id || null,
  nhomKienThucId: dongDangDung('nhomKienThuc')?.id || null
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

  const bangGocNoiVersion = {
    nangLucDauRaGoc: {joinKey: 'chuongTrinhVersionNangLuc', gocIdKey: 'nangLucGocId'},
    viTriViecLamGoc: {joinKey: 'chuongTrinhVersionViTriViecLam', gocIdKey: 'viTriGocId'},
  }
  if (key === 'mucTieuChuongTrinhGoc') {
    const chuongTrinhVersionId = filterValues.chuongTrinhVersionId || parentValues.chuongTrinhVersionId || selected.chuongTrinhVersion?.id
    if (!chuongTrinhVersionId) return []

    const maDaLuu = new Set(
        (duLieu.value.mucTieuChuongTrinh || [])
            .filter((row) => String(row.chuongTrinhVersionId || '') === String(chuongTrinhVersionId || ''))
            .map((row) => String(row.ma || ''))
            .filter(Boolean)
    )

    return (duLieu.value.mucTieuChuongTrinhGoc || [])
        .filter((row) => maDaLuu.has(String(row.ma || '')))
        .map((row) => row.id)
        .filter((id) => id !== null && id !== undefined && id !== '')
  }
  if (key === 'dieuKienTotNghiepGoc') {
    const chuongTrinhVersionId = filterValues.chuongTrinhVersionId || parentValues.chuongTrinhVersionId || selected.chuongTrinhVersion?.id
    if (!chuongTrinhVersionId) return []

    const maDaLuu = new Set(
        (duLieu.value.dieuKienTotNghiep || [])
            .filter((row) => String(row.chuongTrinhVersionId || '') === String(chuongTrinhVersionId || ''))
            .map((row) => String(row.ma || ''))
            .filter(Boolean)
    )

    return (duLieu.value.dieuKienTotNghiepGoc || [])
        .filter((row) => maDaLuu.has(String(row.ma || '')))
        .map((row) => row.id)
        .filter((id) => id !== null && id !== undefined && id !== '')
  }
  if (key === 'syllabusChuongTrinhGoc') {
    const chuongTrinhVersionId = filterValues.chuongTrinhVersionId || parentValues.chuongTrinhVersionId || selected.chuongTrinhVersion?.id
    if (!chuongTrinhVersionId) return []
    return (duLieu.value.syllabusChuongTrinh || [])
        .filter((row) => String(row.chuongTrinhVersionId || '') === String(chuongTrinhVersionId || ''))
        .map((row) => row.syllabusChuongTrinhGocId)
        .filter((id) => id !== null && id !== undefined && id !== '')
  }

  if (key === 'khungKyGoc') {
    const chuongTrinhVersionId = filterValues.chuongTrinhVersionId || selected.chuongTrinhVersion?.id
    if (!chuongTrinhVersionId) return []
    return (duLieu.value.khungKy || [])
        .filter((row) => String(row.chuongTrinhVersionId || '') === String(chuongTrinhVersionId || ''))
        .map((row) => row.khungKyGocId)
        .filter((id) => id !== null && id !== undefined && id !== '')
  }

  if (key === 'nhomKienThucGoc') {
    const chuongTrinhVersionId = filterValues.chuongTrinhVersionId || selected.chuongTrinhVersion?.id
    if (!chuongTrinhVersionId) return []
    return (duLieu.value.nhomKienThuc || [])
        .filter((row) => String(row.chuongTrinhVersionId || '') === String(chuongTrinhVersionId || ''))
        .map((row) => row.nhomKienThucGocId)
        .filter((id) => id !== null && id !== undefined && id !== '')
  }

  if (key === 'nhomTuChonGoc') {
    const chuongTrinhVersionId = filterValues.chuongTrinhVersionId || selected.chuongTrinhVersion?.id
    if (!chuongTrinhVersionId) return []
    return (duLieu.value.nhomTuChon || [])
        .filter((row) => String(row.chuongTrinhVersionId || '') === String(chuongTrinhVersionId || ''))
        .map((row) => row.nhomTuChonGocId)
        .filter((id) => id !== null && id !== undefined && id !== '')
  }

  if (key === 'dieuKienMonHocGoc') {
    const syllabusMonId = filterValues.syllabusMonId || parentValues.syllabusMonId || selected.syllabusMonHoc?.id
    if (!syllabusMonId) return []

    const maDaLuu = new Set(
        (duLieu.value.dieuKienMonHoc || [])
            .filter((row) => String(row.syllabusMonId || '') === String(syllabusMonId || ''))
            .map((row) => String(row.ma || ''))
            .filter(Boolean)
    )

    return (duLieu.value.dieuKienMonHocGoc || [])
        .filter((row) => maDaLuu.has(String(row.ma || '')))
        .map((row) => row.id)
        .filter((id) => id !== null && id !== undefined && id !== '')
  }

  if (key === 'taiLieuGoc') {
    const syllabusMonId = filterValues.syllabusMonId || parentValues.syllabusMonId || selected.syllabusMonHoc?.id
    if (!syllabusMonId) return []

    const maDaLuu = new Set(
        (duLieu.value.syllabusTaiLieu || [])
            .filter((row) => String(row.syllabusMonId || '') === String(syllabusMonId || ''))
            .map((row) => String(row.ma || ''))
            .filter(Boolean)
    )

    return (duLieu.value.taiLieuGoc || [])
        .filter((row) => maDaLuu.has(String(row.ma || '')))
        .map((row) => row.id)
        .filter((id) => id !== null && id !== undefined && id !== '')
  }

  if (key === 'syllabusMonHocGoc') {
    const chuongTrinhMonId = filterValues.chuongTrinhMonId || parentValues.chuongTrinhMonId || selected.chuongTrinhMon?.id
    if (!chuongTrinhMonId) return []

    return (duLieu.value.syllabusMonHoc || [])
        .filter((row) => String(row.chuongTrinhMonId || '') === String(chuongTrinhMonId || ''))
        .map((row) => row.syllabusMonHocGocId)
        .filter((id) => id !== null && id !== undefined && id !== '')
  }

  if (bangGocNoiVersion[key]) {
    const config = bangGocNoiVersion[key]
    const chuongTrinhVersionId = filterValues.chuongTrinhVersionId || parentValues.chuongTrinhVersionId || selected.chuongTrinhVersion?.id

    if (!chuongTrinhVersionId) return []

    return (duLieu.value[config.joinKey] || [])
        .filter((row) => String(row.chuongTrinhVersionId || '') === String(chuongTrinhVersionId || ''))
        .map((row) => row[config.gocIdKey])
        .filter((id) => id !== null && id !== undefined && id !== '')
  }

  if (key === 'quyDoiDiemMau') {
    // Chỉ lấy danh sách ID đã gắn với Chương trình môn để tô trạng thái Đã lưu.
    // Không dùng đoạn này để lọc dữ liệu hiển thị vì quyDoiDiemMau là bảng gốc/tái sử dụng dùng chung.
    const chuongTrinhMonId = filterValues.chuongTrinhMonId || parentValues.chuongTrinhMonId || selected.chuongTrinhMon?.id
    if (!chuongTrinhMonId) return []

    return (duLieu.value.chuongTrinhMonQuyDoiDiemMau || [])
        .filter((row) => String(row.chuongTrinhMonId || '') === String(chuongTrinhMonId || ''))
        .map((row) => row.quyDoiDiemMauId)
        .filter((id) => id !== null && id !== undefined && id !== '')
  }

  if (key === 'nganh') {
    return []
  }

  if (key === 'monHoc') {
    const chuongTrinhVersionId = filterValues.chuongTrinhVersionId || parentValues.chuongTrinhVersionId || selected.chuongTrinhVersion?.id
    const khungKyId = filterValues.khungKyId || parentValues.khungKyId || selected.khungKy?.id
    const nhomKienThucId = filterValues.nhomKienThucId || parentValues.nhomKienThucId || selected.nhomKienThuc?.id || null

    return (duLieu.value.chuongTrinhMon || [])
        .filter((row) => String(row.chuongTrinhVersionId || '') === String(chuongTrinhVersionId || ''))
        .filter((row) => !khungKyId || String(row.khungKyId || '') === String(khungKyId || ''))
        .filter((row) => nhomKienThucId ? String(row.nhomKienThucId || '') === String(nhomKienThucId || '') : true)
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
  // Các bảng gốc/kho mẫu dùng chung phải luôn hiện toàn bộ dữ liệu từ API gốc.
  // Việc chọn Chương trình môn chỉ dùng cho nút Lưu để gắn qua bảng nối, không được làm mất dữ liệu mẫu.
  'quyDoiDiemMau'
])

const bangGocMauKeys = new Set([
  'mucTieuChuongTrinhGoc',
  'nangLucDauRaGoc',
  'viTriViecLamGoc',
  'dieuKienTotNghiepGoc',
  'syllabusChuongTrinhGoc',
  'khungKyGoc',
  'nhomKienThucGoc',
  'nhomTuChonGoc',
  'monHoc',
  'syllabusMonHocGoc',
  'dieuKienMonHocGoc',
  'taiLieuGoc',
  'quyDoiDiemMau'
])

const nhanNutXemTheoBang = {
  nganh: 'Xem chương trình',
  chuongTrinh: 'Xem version',
  chuongTrinhVersion: 'Xem chi tiết version',
  monHoc: 'Xem syllabus gốc',
  chuongTrinhMon: 'Xem syllabus áp dụng',
  syllabusMonHoc: 'Xem chi tiết syllabus',
  nhomKienThuc: 'Xem môn trong nhóm',
  nhomTuChon: 'Xem môn tự chọn'
}

const bangKhongCanChon = new Set([
  'syllabusChuongTrinh',
  'chuongTrinhVersionMucTieu',
  'mucTieuChuongTrinh',
  'chuongTrinhVersionNangLuc',
  'nangLucDauRa',
  'chuongTrinhVersionViTriViecLam',
  'viTriViecLam',
  'chuongTrinhVersionDieuKienTotNghiep',
  'dieuKienTotNghiep',
  'monTuChon',
  'monTienQuyet',
  'chuongTrinhMonQuyDoiDiemMau',
  'quyDoiDiem',
  'syllabusMonHocGocChuongBai',
  'syllabusMonHocGocDieuKien',
  'syllabusMonHocGocTaiLieu',
  'syllabusMonHocDieuKien',
  'syllabusMonHocTaiLieu',
])

const bangChiLuuGanKhongChon = new Set([
  'syllabusChuongTrinhGoc',
  'mucTieuChuongTrinhGoc',
  'nangLucDauRaGoc',
  'viTriViecLamGoc',
  'dieuKienTotNghiepGoc',
  'khungKyGoc',
  'nhomKienThucGoc',
  'nhomTuChonGoc',
  'monHoc',
  'syllabusMonHocGoc',
  'quyDoiDiemMau',
  'dieuKienMonHocGoc',
  'taiLieuGoc'
])

const bangCoNutLuuBoLuuMacDinh = new Set([
  'syllabusChuongTrinhGoc',
  'mucTieuChuongTrinhGoc',
  'nangLucDauRaGoc',
  'viTriViecLamGoc',
  'dieuKienTotNghiepGoc',
  'khungKyGoc',
  'nhomKienThucGoc',
  'nhomTuChonGoc',
  'syllabusMonHocGoc',
  'quyDoiDiemMau',
  'dieuKienMonHocGoc',
  'taiLieuGoc'
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
  'syllabusChuongTrinh',
  'chuongTrinhVersionMucTieu',
  'mucTieuChuongTrinh',
  'chuongTrinhVersionNangLuc',
  'nangLucDauRa',
  'chuongTrinhVersionViTriViecLam',
  'viTriViecLam',
  'chuongTrinhVersionDieuKienTotNghiep',
  'dieuKienTotNghiep',
  'monTuChon',
  'monTienQuyet',
  'chuongTrinhMonQuyDoiDiemMau',
  'quyDoiDiem',
  'syllabusMonHocGocChuongBai',
  'syllabusMonHocGocDieuKien',
  'syllabusMonHocGocTaiLieu',
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
      params: {nganhId: item.nganhId || selected.nganh?.id, chuongTrinhId: item.id}
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
      name: 'DaoTao.XemChuongTrinh.KhungCauTruc',
      params: {
        nganhId: selected.nganh?.id,
        chuongTrinhId: selected.chuongTrinh?.id,
        versionId: selected.chuongTrinhVersion?.id
      },
      query: {
        manHinhTang6: 'nhom',
        khungKyId: item.id
      }
    })
    return
  }

  if (bang.key === 'nhomKienThuc') {
    router.push({
      name: 'DaoTao.XemChuongTrinh.Mon',
      params: {
        nganhId: selected.nganh?.id,
        chuongTrinhId: selected.chuongTrinh?.id,
        versionId: selected.chuongTrinhVersion?.id
      },
      query: {
        nhomKienThucId: item.id,
        khungKyId: selected.khungKy?.id || undefined
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
        khungKyId: selected.khungKy?.id || chuongTrinhMon?.khungKyId,
        nhomKienThucId: selected.nhomKienThuc?.id || chuongTrinhMon?.nhomKienThucId
      })
    })
    return
  }

  if (bang.key === 'monHoc') {
    router.push({
      name: 'DaoTao.XemChuongTrinh.SyllabusGoc',
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
      name: 'DaoTao.XemChuongTrinh.SyllabusGoc',
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


const bangCoNutLuuLienKet = new Set([
  'syllabusChuongTrinhGoc',
  'khungKyGoc',
  'nhomKienThucGoc',
  'nhomTuChonGoc',
  'syllabusMonHocGoc',
  'mucTieuChuongTrinhGoc',
  'nangLucDauRaGoc',
  'viTriViecLamGoc',
  'dieuKienTotNghiepGoc',
  'monHoc',
  'chuongTrinhMon',
  'monTienQuyet',
  'quyDoiDiemMau',
  'dieuKienMonHocGoc',
  'taiLieuGoc',
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

function luuDongBang(bang, item) {
  const parentValues = bang.linkParentValues || bang.parentValues || {}

  if (bang.key === 'trinhDoDaoTao') {
    ganTrinhDoVaoNganh(item)
    return
  }

  if (bang.key === 'loaiChuongTrinh') {
    ganLoaiChuongTrinhVaoNganh(item)
    return
  }

  if (bang.key === 'quyDoiDiemMau' && !parentValues.chuongTrinhMonId) {
    baoTin('Cần chọn Môn trong chương trình trước khi lưu/gắn Quy đổi điểm mẫu.', 'error')
    return
  }

  toggleLuuBangPhu(bang.key, item, parentValues, bang.service)
}

const bangPhuHienDuLieuDayDu = new Set([
  'mucTieuChuongTrinhGoc',
  'nangLucDauRaGoc',
  'viTriViecLamGoc',
  'dieuKienTotNghiepGoc',
  'syllabusChuongTrinhGoc',
  'khungKyGoc',
  'nhomKienThucGoc',
  'nhomTuChonGoc',
  'syllabusChuongTrinh',
  'monTienQuyet',
  'quyDoiDiemMau',
  'dieuKienMonHocGoc',
  'taiLieuGoc',
  'dieuKienMonHoc',
  'syllabusChuongBai',
  'syllabusTaiLieu'
])

function taoBang(key, options = {}) {
  const parentValues = options.parentValues || {}
  const linkParentValues = options.linkParentValues || parentValues
  const filterValues = options.filterValues || parentValues
  const allRows = key === 'quyDoiDiemMau'
      ? (quyDoiDiemMauGocRows.value || [])
      : (duLieu.value[key] || [])

  const loaiBang = bangGocMauKeys.has(key) ? 'goc-mau' : (bangXuongSongKeys.has(key) ? 'xuong-song' : 'phu')
  const hienDayDuBangPhu = loaiBang === 'phu' && bangPhuHienDuLieuDayDu.has(key) && !options.forceFilter
  const hienDayDuBangGocMau = loaiBang === 'goc-mau'
  const khongLocTheoCha = bangDocLapKhongLocTheoCha.has(key)

  const rows = khongLocTheoCha || hienDayDuBangGocMau || hienDayDuBangPhu ? allRows : locDongTheoCha(allRows, filterValues)

  const coNutLuuBoLuu = options.canToggleSave === undefined ? bangCoNutLuuBoLuuMacDinh.has(key) : Boolean(options.canToggleSave)
  const coNutChon = options.canSelect === undefined ? !bangKhongCanChon.has(key) && !bangChiLuuGanKhongChon.has(key) : Boolean(options.canSelect)
  const coHienTrangThaiDaLuu = Boolean(options.canShowSavedStatus) || coNutLuuBoLuu || bangChiHienTrangThaiDaLuuMacDinh.has(key)
  const idsDaLuuTheoNguCanh = coHienTrangThaiDaLuu ? laySavedIdsTheoBang(key, allRows, rows, linkParentValues, linkParentValues) : []
  const idsHienThiDaLuu = coHienTrangThaiDaLuu && !coNutLuuBoLuu && idsDaLuuTheoNguCanh.length === 0 && !['trinhDoDaoTao', 'loaiChuongTrinh'].includes(key)
      ? rows.map((row) => row?.id).filter((id) => id !== null && id !== undefined && id !== '')
      : idsDaLuuTheoNguCanh
  const idsLienQuanTheoNguCanh = coNutChon ? laySelectedIdsTheoBang(key, allRows, rows, linkParentValues, linkParentValues) : []

  return {
    key,
    config: {
      ...configs[key],
      title: options.title || configs[key]?.title,
      description: options.description || configs[key]?.description,
      canView: Boolean(options.canView),
      viewLabel: options.viewLabel || nhanNutXemTheoBang[key] || 'Xem thêm'
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
    canView: Boolean(options.canView),
    viewLabel: options.viewLabel || nhanNutXemTheoBang[key] || 'Xem thêm',
    tableMessage: tableMessages?.[key] || null,
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
    disabledText: options.disabledText || ''
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
    ten: 'Nhóm kiến thức của kỳ',
    route: 'DaoTao.XemChuongTrinh.KhungCauTruc',
    moTa: 'Chọn/gán nhóm kiến thức và nhóm tự chọn theo kỳ đã chọn.'
  },
  {
    so: 8,
    ten: 'Môn trong nhóm của kỳ',
    route: 'DaoTao.XemChuongTrinh.Mon',
    moTa: 'Môn trong chương trình được lọc theo Version, Kỳ và Nhóm đang chọn.'
  },
  {
    so: 9,
    ten: 'Gán syllabus gốc vào môn',
    route: 'DaoTao.XemChuongTrinh.SyllabusGoc',
    moTa: 'Chọn syllabus gốc/mẫu và gán vào môn trong chương trình.'
  },
  {
    so: 10,
    ten: 'Chi tiết syllabus áp dụng',
    route: 'DaoTao.XemChuongTrinh.SyllabusApDung',
    moTa: 'Quản lý chương bài, điều kiện và tài liệu của syllabus đã áp dụng.'
  }
]

const tangHienTai = computed(() => {
  if (route.name === 'DaoTao.XemChuongTrinh.KhungCauTruc') {
    return route.query.manHinhTang6 === 'nhom' ? 7 : 6
  }

  if (route.name === 'DaoTao.XemChuongTrinh.Mon') return 8
  if (route.name === 'DaoTao.XemChuongTrinh.SyllabusGoc') return 9
  if (route.name === 'DaoTao.XemChuongTrinh.SyllabusApDung') return 10

  return cacTangDaoTao.find((tang) => tang.route === route.name)?.so || 1
})
const thongTinTangHienTai = computed(() => cacTangDaoTao.find((tang) => tang.so === tangHienTai.value) || cacTangDaoTao[0])

const coTheVeTangTongQuan = computed(() => Boolean(selected.nganh?.id && selected.chuongTrinh?.id && selected.chuongTrinhVersion?.id))
const coTheTiepTucTang2 = computed(() => Boolean(selected.nganh?.id && (selected.trinhDoDaoTao?.id || selected.loaiChuongTrinh?.id)))
const coTheTiepTucTang6 = computed(() => coTheVeTangTongQuan.value)
const hienNutTiepTucChung = computed(() => tangHienTai.value === 2 || tangHienTai.value === 6)
const tieuDeNutTiepTuc = computed(() => tangHienTai.value === 2 ? 'Tiếp tục xem Chương trình' : 'Tiếp tục xem Môn trong chương trình')
const coTheBamNutTiepTuc = computed(() => tangHienTai.value === 2 ? coTheTiepTucTang2.value : coTheTiepTucTang6.value)
const moTaNutTiepTuc = computed(() => {
  if (tangHienTai.value === 2) {
    return coTheTiepTucTang2.value
        ? 'Đã có dữ liệu theo ngành. Bấm Xem ở bảng đã gán để chuyển sang tầng Chương trình.'
        : 'Cần gán ít nhất một trình độ đào tạo hoặc một loại chương trình cho ngành.'
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
    key: 'trinhDoDaoTao',
    label: 'Trình độ',
    value: selected.trinhDoDaoTao?.tenTrinhDo || '',
    tang: 2,
    tenTang: 'Tầng 2 - Cấu hình chương trình',
    enabled: Boolean(selected.nganh?.id)
  },
  {
    key: 'loaiChuongTrinh',
    label: 'Loại CT',
    value: selected.loaiChuongTrinh?.tenLoai || '',
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
    key: 'nhomKienThuc',
    label: 'Nhóm kiến thức',
    value: selected.nhomKienThuc?.ten || selected.nhomKienThuc?.ma || '',
    tang: 7,
    tenTang: 'Tầng 7 - Nhóm kiến thức của kỳ',
    enabled: Boolean(coTheVeTangTongQuan.value && selected.khungKy?.id)
  },
  {
    key: 'chuongTrinhMon',
    label: 'Môn trong CT',
    value: selected.chuongTrinhMon?.maMonTrongCt || selected.chuongTrinhMon?.tenMon || '',
    tang: 8,
    tenTang: 'Tầng 8 - Môn trong nhóm của kỳ',
    enabled: Boolean(coTheVeTangTongQuan.value && selected.khungKy?.id && selected.nhomKienThuc?.id)
  },
  {
    key: 'monHoc',
    label: 'Môn học',
    value: selected.monHoc?.tenMon || selected.monHoc?.maMon || '',
    tang: 8,
    tenTang: 'Tầng 8 - Môn trong nhóm của kỳ',
    enabled: Boolean(coTheVeTangTongQuan.value && selected.chuongTrinhMon?.id)
  },
  {
    key: 'syllabusMonHoc',
    label: 'Syllabus môn',
    value:
        selected.syllabusMonHoc?.ten ||
        selected.syllabusMonHoc?.tenSyllabusMonHocGoc ||
        selected.syllabusMonHoc?.ma ||
        selected.syllabusMonHoc?.maSyllabusMonHocGoc ||
        '',
    tang: 9,
    tenTang: 'Tầng 9 - Gán syllabus gốc vào môn',
    enabled: Boolean(coTheVeTangTongQuan.value && selected.chuongTrinhMon?.id)
  },
  {
    key: 'syllabusTaiLieu',
    label: 'Tài liệu syllabus',
    value: selected.syllabusTaiLieu?.ten || selected.syllabusTaiLieu?.ma || '',
    tang: 10,
    tenTang: 'Tầng 10 - Chi tiết syllabus áp dụng',
    enabled: Boolean(coTheVeTangTongQuan.value && selected.syllabusMonHoc?.id)
  }
])

function diChuyenTheoTomTat(muc) {
  if (!muc?.enabled) return
  veTang(muc.tang)
}

const coTheVeTang8 = computed(() => Boolean(
    coTheVeTangTongQuan.value &&
    selected.khungKy?.id &&
    selected.nhomKienThuc?.id
))
const coTheVeTang9 = computed(() => Boolean(coTheVeTangTongQuan.value && (selected.monHoc?.id || selected.chuongTrinhMon?.monHocId || selected.chuongTrinhMon?.monId)))
const coTheVeTang10 = computed(() => Boolean(coTheVeTangTongQuan.value && selected.chuongTrinhMon?.id))

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
    taoNut('ve-tong-quan', '← Về Tổng quan Version', 5, coTheVeTangTongQuan.value)
  ]
  if (so === 7) return [
    taoNut('ve-ky-hoc', '← Về Kỳ học', 6, coTheVeTangTongQuan.value),
    taoNut('tiep-mon', 'Xem môn theo nhóm kiến thức của kỳ →', 8, coTheVeTang8.value, true)
  ]
  if (so === 8) return [
    taoNut('ve-nhom', '← Về Nhóm kiến thức của kỳ', 7, Boolean(coTheVeTangTongQuan.value && selected.khungKy?.id)),
    taoNut('gan-syllabus-goc', 'Gán syllabus gốc cho môn →', 9, coTheVeTang9.value, true)
  ]
  if (so === 9) return [
    taoNut('ve-mon', '← Về Môn trong nhóm của kỳ', 8, coTheVeTangTongQuan.value),
    taoNut('xem-chi-tiet-syllabus', 'Xem chi tiết syllabus →', 10, coTheVeTang10.value, true)
  ]
  return [
    taoNut('ve-syllabus-goc', '← Về Syllabus gốc', 9, coTheVeTangTongQuan.value),
    taoNut('ve-mon', '← Về Môn trong nhóm của kỳ', 8, coTheVeTangTongQuan.value)
  ]
})

const moTaDieuHuongTang = computed(() => {
  if (tangHienTai.value === 1) return selected.nganh?.id ? 'Đã chọn ngành. Có thể đi tiếp sang cấu hình chương trình.' : 'Chọn một ngành trước khi đi tiếp.'
  if (tangHienTai.value === 2) return moTaNutTiepTuc.value
  if (tangHienTai.value === 5) return 'Tầng tổng quan dài, nút chuyển tầng được đặt cả trên và dưới để không cần kéo lên đầu trang.'
  if (tangHienTai.value === 6) return 'Chọn một kỳ rồi bấm Xem nhóm kiến thức để đi đúng flow Version → Kỳ → Nhóm.'
  if (tangHienTai.value === 7) return 'Đang xem nhóm kiến thức của kỳ đã chọn. Chọn nhóm kiến thức rồi bấm Xem môn để đi tiếp.'
  if (tangHienTai.value === 8) return 'Đang xem môn theo Version/Kỳ/Nhóm đã chọn. Bấm Chi tiết ở môn trong chương trình để xem chi tiết môn.'
  if (tangHienTai.value === 9) return coTheVeTang10.value ? 'Có thể sang syllabus áp dụng của Môn trong CT đang chọn.' : 'Muốn sang tầng 10 cần chọn Môn trong chương trình ở tầng 8.'
  if (tangHienTai.value === 10) return 'Đây là tầng cuối. Có thể quay lại tầng 9 hoặc tầng 8 để kiểm tra dữ liệu.'
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

  const chuongTrinh = timDongTheoId('chuongTrinh', route.params.chuongTrinhId)
  if (chuongTrinh && String(selected.chuongTrinh?.id || '') !== String(chuongTrinh.id)) {
    selectEntity('chuongTrinh', chuongTrinh)

    const trinhDo = timDongTheoId('trinhDoDaoTao', chuongTrinh.trinhDoId)
    if (trinhDo) selectEntity('trinhDoDaoTao', trinhDo)

    const loai = timDongTheoId('loaiChuongTrinh', chuongTrinh.loaiChuongTrinhId)
    if (loai) selectEntity('loaiChuongTrinh', loai)

    const nganhLoai = timDongTheoId('nganhLoaiChuongTrinh', chuongTrinh.nganhLoaiChuongTrinhId)
    if (nganhLoai) selectEntity('nganhLoaiChuongTrinh', nganhLoai)
  }
  const khungKyTheoQuery = timDongTheoId('khungKy', route.query.khungKyId)
  if (khungKyTheoQuery && String(selected.khungKy?.id || '') !== String(khungKyTheoQuery.id)) {
    selectEntity('khungKy', khungKyTheoQuery)
  }

  const nhomKienThucTheoQuery = timDongTheoId('nhomKienThuc', route.query.nhomKienThucId)
  if (nhomKienThucTheoQuery && String(selected.nhomKienThuc?.id || '') !== String(nhomKienThucTheoQuery.id)) {
    selectEntity('nhomKienThuc', nhomKienThucTheoQuery)
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

    const nhomKienThucCuaMon = timDongTheoId('nhomKienThuc', chuongTrinhMon.nhomKienThucId)
    if (nhomKienThucCuaMon && String(selected.nhomKienThuc?.id || '') !== String(nhomKienThucCuaMon.id)) {
      selectEntity('nhomKienThuc', nhomKienThucCuaMon)
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
        trinhDoId: selected.trinhDoDaoTao?.id || undefined,
        loaiChuongTrinhId: selected.loaiChuongTrinh?.id || undefined
      }
    })
    return
  }

  if (soTang === 4 && selected.nganh?.id && selected.chuongTrinh?.id) {
    router.push({
      name: 'DaoTao.XemChuongTrinh.Version',
      params: {nganhId: selected.nganh.id, chuongTrinhId: selected.chuongTrinh.id}
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
      query: {manHinhTang6: 'ky'}
    })
    return
  }

  if (soTang === 7 && coTheVeTangTongQuan.value) {
    router.push({
      name: 'DaoTao.XemChuongTrinh.KhungCauTruc',
      params: {
        nganhId: selected.nganh.id,
        chuongTrinhId: selected.chuongTrinh.id,
        versionId: selected.chuongTrinhVersion.id
      },
      query: {manHinhTang6: 'nhom', khungKyId: selected.khungKy?.id || undefined}
    })
    return
  }

  if (soTang === 8 && coTheVeTangTongQuan.value) {
    router.push({
      name: 'DaoTao.XemChuongTrinh.Mon',
      params: {
        nganhId: selected.nganh.id,
        chuongTrinhId: selected.chuongTrinh.id,
        versionId: selected.chuongTrinhVersion.id
      },
      query: {
        khungKyId: selected.khungKy?.id || undefined,
        nhomKienThucId: selected.nhomKienThuc?.id || undefined
      }
    })
    return
  }

  if (soTang === 9 && coTheVeTangTongQuan.value) {
    const monHocId = selected.monHoc?.id || selected.chuongTrinhMon?.monHocId || selected.chuongTrinhMon?.monId || null
    router.push({
      name: 'DaoTao.XemChuongTrinh.SyllabusGoc',
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

  if (soTang === 10 && coTheVeTangTongQuan.value) {
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
        nhomKienThucId: selected.nhomKienThuc?.id || selected.chuongTrinhMon?.nhomKienThucId || null
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
    query: {manHinhTang6: 'ky'}
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
    return [{
      key: 'tang-2-cau-hinh',
      title: 'Tầng 2: Cấu hình chương trình',
      description: 'Hai bảng 2.1 và 2.2 là dữ liệu gốc. Bấm Gán vào để lưu từng dữ liệu vào ngành; bảng theo ngành nằm ngay bên dưới từng bảng gốc.',
      mau: 'mau-tim',
      tables: [
        taoBang('trinhDoDaoTao', {
          canView: false,
          canSelect: false,
          canToggleSave: true,
          canShowSavedStatus: true,
          toggleSaveLabel: 'Gán vào',
          toggleSavedLabel: 'Đã gán',
          statusSavedLabel: '✓ Đã gán',
          statusUnsavedLabel: '+ Chưa gán',
          saveStatusText: 'Bảng gốc Trình độ đào tạo. Bấm Gán vào để lưu trình độ này cho ngành đang xem.',
          parentText: `Ngành đang xem: ${selected.nganh?.tenNganh || '-'}`
        }),
        taoBang('loaiChuongTrinh', {
          canView: false,
          canSelect: false,
          canToggleSave: true,
          canShowSavedStatus: true,
          toggleSaveLabel: 'Gán vào',
          toggleSavedLabel: 'Đã gán',
          statusSavedLabel: '✓ Đã gán',
          statusUnsavedLabel: '+ Chưa gán',
          saveStatusText: 'Bảng gốc Loại chương trình. Bấm Gán vào để lưu loại chương trình này cho ngành đang xem.',
          parentText: `Ngành đang xem: ${selected.nganh?.tenNganh || '-'}`
        })
      ]
    }]
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
          disabled: !selected.nganhLoaiChuongTrinh,
          disabledText: 'Cần bấm Xem ở bảng 2.2.1 Loại chương trình theo ngành trước.'
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
      description: 'Các bảng gốc dùng chung và bảng áp dụng vào version. Dữ liệu được lọc theo version đang chọn.',
      mau: 'mau-hong',
      tables: [
        taoBang('syllabusChuongTrinhGoc', {
          linkParentValues: versionParent.value,
          parentText: `${versionParentText.value} | Kho syllabus chương trình gốc dùng chung. Click dòng gốc để tạo/lưu syllabus_chuong_trinh cho Version hiện tại.`,
          disabled: !selected.chuongTrinhVersion,
          disabledText: 'Cần chọn Version trước.'
        }),
        taoBang('syllabusChuongTrinh', {
          parentValues: versionParent.value,
          filterValues: versionFilter.value,
          forceFilter: true,
          readOnly: true,
          canSelect: false,
          canToggleSave: false,
          title: '4.3.2. Syllabus chương trình đã lưu vào Version',
          description: 'Chỉ hiển thị syllabus chương trình đã lưu từ bảng gốc vào Version hiện tại.',
          tableTitle: 'Syllabus chương trình đã lưu/gắn vào Version',
          parentText: versionParentText.value,
          disabled: !selected.chuongTrinhVersion,
          disabledText: 'Cần chọn Version trước.'
        }),
        taoBang('mucTieuChuongTrinhGoc', {
          linkParentValues: versionParent.value,
          parentText: `${versionParentText.value} | Kho mục tiêu gốc dùng chung. Bấm Gán vào để copy mục tiêu gốc sang bảng Mục tiêu chương trình đã lưu vào Version.`,
          disabled: !selected.chuongTrinhVersion,
          disabledText: 'Cần chọn Version trước.'
        }),
        taoBang('mucTieuChuongTrinh', {
          parentValues: versionParent.value,
          filterValues: versionFilter.value,
          forceFilter: true,
          readOnly: true,
          canSelect: false,
          canToggleSave: false,
          title: '10. Mục tiêu chương trình đã lưu vào Version',
          description: 'Chỉ hiển thị mục tiêu chương trình đã được copy từ bảng gốc/mẫu vào Version.',
          tableTitle: 'Mục tiêu chương trình đã lưu vào Version',
          parentText: versionParentText.value,
          disabled: !selected.chuongTrinhVersion,
          disabledText: 'Cần chọn Version trước.'
        }),
        taoBang('nangLucDauRaGoc', {
          linkParentValues: versionParent.value,
          parentText: `${versionParentText.value} | Kho năng lực gốc dùng chung. Click dòng gốc để lưu vào chuong_trinh_version_nang_luc.`,
          disabled: !selected.chuongTrinhVersion,
          disabledText: 'Cần chọn Version trước.'
        }),
        taoBang('chuongTrinhVersionNangLuc', {
          parentValues: versionParent.value,
          filterValues: versionFilter.value,
          forceFilter: true,
          readOnly: true,
          canSelect: false,
          canToggleSave: false,
          title: '13. Năng lực đầu ra đã lưu vào Version',
          description: 'Thay cho bảng nhập riêng. Chỉ hiển thị các năng lực gốc đã được lưu qua bảng chuong_trinh_version_nang_luc.',
          tableTitle: 'Năng lực đầu ra đã lưu/gắn vào Version',
          parentText: versionParentText.value,
          disabled: !selected.chuongTrinhVersion,
          disabledText: 'Cần chọn Version trước.'
        }),
        taoBang('viTriViecLamGoc', {
          linkParentValues: versionParent.value,
          parentText: `${versionParentText.value} | Kho vị trí việc làm gốc dùng chung. Click dòng gốc để lưu vào chuong_trinh_version_vi_tri_viec_lam.`,
          disabled: !selected.chuongTrinhVersion,
          disabledText: 'Cần chọn Version trước.'
        }),
        taoBang('chuongTrinhVersionViTriViecLam', {
          parentValues: versionParent.value,
          filterValues: versionFilter.value,
          forceFilter: true,
          readOnly: true,
          canSelect: false,
          canToggleSave: false,
          title: '16. Vị trí việc làm đã lưu vào Version',
          description: 'Thay cho bảng nhập riêng. Chỉ hiển thị các vị trí gốc đã được lưu qua bảng chuong_trinh_version_vi_tri_viec_lam.',
          tableTitle: 'Vị trí việc làm đã lưu/gắn vào Version',
          parentText: versionParentText.value,
          disabled: !selected.chuongTrinhVersion,
          disabledText: 'Cần chọn Version trước.'
        }),
        taoBang('dieuKienTotNghiepGoc', {
          linkParentValues: versionParent.value,
          parentText: `${versionParentText.value} | Kho điều kiện tốt nghiệp gốc dùng chung. Bấm Gán vào để copy điều kiện tốt nghiệp gốc sang bảng Điều kiện tốt nghiệp đã lưu vào Version.`,
          disabled: !selected.chuongTrinhVersion,
          disabledText: 'Cần chọn Version trước.'
        }),
        taoBang('dieuKienTotNghiep', {
          parentValues: versionParent.value,
          filterValues: versionFilter.value,
          forceFilter: true,
          readOnly: true,
          canSelect: false,
          canToggleSave: false,
          title: '19. Điều kiện tốt nghiệp đã lưu vào Version',
          description: 'Chỉ hiển thị điều kiện tốt nghiệp đã được copy từ bảng gốc/mẫu vào Version.',
          tableTitle: 'Điều kiện tốt nghiệp đã lưu vào Version',
          parentText: versionParentText.value,
          disabled: !selected.chuongTrinhVersion,
          disabledText: 'Cần chọn Version trước.'
        })
      ]
    }]
  }

  if (tangHienTai.value === 6) {
    return [{
      key: 'tang-6-ky-hoc',
      title: 'Tầng 6: Kỳ học',
      description: 'Tạo/chọn kỳ học của Version trước. Bấm Xem nhóm kiến thức ở một kỳ để đi sang tầng 7.',
      mau: 'mau-vang',
      tables: [
        taoBang('khungKyGoc', {
          linkParentValues: khungKyParent.value,
          parentText: `${versionParentText.value} | Kho khung kỳ gốc dùng chung.`,
          disabled: !selected.chuongTrinhVersion,
          disabledText: 'Cần chọn Version trước.'
        }),
        taoBang('khungKy', {
          parentValues: khungKyParent.value,
          filterValues: khungKyFilter.value,
          parentText: khungKyParentText.value,
          canView: true,
          viewLabel: 'Xem nhóm kiến thức',
          disabled: !selected.chuongTrinhVersion,
          disabledText: 'Cần chọn Version trước.'
        })
      ]
    }]
  }

  if (tangHienTai.value === 7) {
    return [{
      key: 'tang-7-nhom-kien-thuc-theo-ky',
      title: 'Tầng 7: Nhóm kiến thức của kỳ',
      description: 'Đã chọn kỳ học. Bây giờ gán/chọn nhóm kiến thức cho Version. Các loại nhóm gồm: chung, cơ sở, chuyên môn, tự chọn. Sau đó bấm Xem môn để sang tầng 8.',
      mau: 'mau-vang',
      tables: [
        taoBang('nhomKienThucGoc', {
          linkParentValues: versionParent.value,
          parentText: `${viTriHienTaiText.value} | Kho nhóm kiến thức gốc dùng chung. Bấm Gán vào để copy nhóm kiến thức gốc sang Version.`,
          disabled: !selected.chuongTrinhVersion || !selected.khungKy,
          disabledText: 'Cần chọn Version và Kỳ học trước.'
        }),
        taoBang('nhomKienThuc', {
          parentValues: nhomKienThucParent.value,
          filterValues: versionFilter.value,
          parentText: '',
          readonlyInfoFields: taoOThongTinNhomTheoKy('kienThuc'),
          canView: true,
          viewLabel: 'Xem môn',
          disabled: !selected.chuongTrinhVersion || !selected.khungKy,
          disabledText: 'Cần chọn Version và Kỳ học trước.'
        })
      ]
    }]
  }

  if (tangHienTai.value === 8) {
    return [{
      key: 'tang-8-mon-trong-nhom-cua-ky',
      title: 'Tầng 8: Môn trong nhóm của kỳ',
      description: 'Chỉ hiển thị và gán môn theo đúng Version/Kỳ/Nhóm kiến thức đang chọn. Bấm Chi tiết để đi vào chi tiết môn trong chương trình.',
      mau: 'mau-ngoc',
      tables: [
        taoBang('monHoc', {
          parentValues: {},
          filterValues: {},
          linkParentValues: chuongTrinhMonParent.value,
          parentText: `${viTriHienTaiText.value} | Môn học gốc dùng chung. Bấm Gán vào để lưu môn vào Version/Kỳ/Nhóm kiến thức đang chọn.`,
          canToggleSave: true,
          canSelect: false,
          canShowSavedStatus: true,
          disabled: !selected.chuongTrinhVersion || !selected.khungKy || !selected.nhomKienThuc,
          disabledText: 'Cần chọn Version, Kỳ học và Nhóm kiến thức trước.'
        }),
        taoBang('chuongTrinhMon', {
          parentValues: chuongTrinhMonTheoNhomFilter.value,
          filterValues: chuongTrinhMonTheoNhomFilter.value,
          parentText: viTriHienTaiText.value,
          canView: true,
          viewLabel: 'Chi tiết',
          readOnly: false,
          canSelect: true,
          canToggleSave: false,
          title: 'Môn đã lưu vào nhóm kiến thức của kỳ',
          description: 'Chỉ hiển thị các môn học đã được gắn vào Version/Kỳ/Nhóm kiến thức hiện tại qua chuong_trinh_mon.',
          tableTitle: 'Môn học đã gắn vào nhóm kiến thức của kỳ',
          emptyText: 'Chưa có môn học nào được gắn vào nhóm kiến thức của kỳ hiện tại.',
          disabled: !selected.chuongTrinhVersion || !selected.khungKy || !selected.nhomKienThuc,
          disabledText: 'Cần chọn Version, Kỳ học và Nhóm kiến thức trước.'
        })
      ]
    }]
  }


  if (tangHienTai.value === 9) {
    return [{
      key: 'tang-9-gan-syllabus-goc-vao-mon',
      title: 'Tầng 9: Gán syllabus gốc vào môn trong chương trình',
      description: 'Chọn syllabus môn học gốc/mẫu, bấm Gán vào để tạo syllabus môn học áp dụng cho môn trong chương trình đang chọn.',
      mau: 'mau-troi',
      tables: [
        taoBang('syllabusMonHocGoc', {
          parentValues: syllabusMonHocGocParent.value,
          filterValues: syllabusMonHocGocFilter.value,
          linkParentValues: syllabusMonHocApDungParent.value,
          parentText: `${chuongTrinhMonOnlyParentText.value} | Kho syllabus môn học gốc. Bấm Gán vào để copy syllabus gốc sang syllabus môn học áp dụng.`,
          canToggleSave: true,
          canSelect: false,
          canShowSavedStatus: true,
          disabled: !selected.chuongTrinhMon,
          disabledText: 'Cần chọn Môn trong chương trình ở tầng 8 trước.'
        }),
        taoBang('syllabusMonHoc', {
          parentValues: chuongTrinhMonOnlyParent.value,
          filterValues: chuongTrinhMonOnlyFilter.value,
          parentText: chuongTrinhMonOnlyParentText.value,
          readOnly: false,
          canSelect: true,
          canToggleSave: false,
          canView: true,
          viewLabel: 'Xem chi tiết syllabus',
          title: 'Syllabus đã gán vào môn trong chương trình',
          description: 'Chỉ hiển thị syllabus môn học đã được tạo từ syllabus gốc cho môn trong chương trình đang chọn.',
          tableTitle: 'Syllabus áp dụng của môn trong chương trình',
          emptyText: 'Chưa có syllabus nào được gán cho môn trong chương trình này.',
          disabled: !selected.chuongTrinhMon,
          disabledText: 'Cần chọn Môn trong chương trình ở tầng 8 trước.'
        })
      ]
    }]
  }

  return [{
    key: 'tang-10-chi-tiet-syllabus-ap-dung',
    title: 'Tầng 10: Chi tiết syllabus áp dụng',
    description: 'Quản lý chương/bài, điều kiện thực hiện môn học và tài liệu của syllabus môn học đã áp dụng.',
    mau: 'mau-xam',
    tables: [
      taoBang('syllabusChuongBai', {
        parentValues: syllabusMonParent.value,
        filterValues: syllabusMonFilter.value,
        parentText: syllabusMonParentText.value,
        title: 'Chương/bài của syllabus',
        description: 'Danh sách chương/bài, số giờ, nội dung và mục tiêu của syllabus môn học áp dụng.',
        tableTitle: 'Chương/bài đã lưu trong syllabus',
        emptyText: 'Chưa có chương/bài nào trong syllabus này.',
        disabled: !selected.syllabusMonHoc,
        disabledText: 'Cần chọn Syllabus môn học áp dụng ở tầng 9 trước.'
      }),
      taoBang('dieuKienMonHocGoc', {
        linkParentValues: syllabusMonParent.value,
        parentText: `${syllabusMonParentText.value} | Kho điều kiện môn học gốc/mẫu dùng chung.`,
        title: 'Điều kiện môn học gốc / mẫu',
        description: 'Kho điều kiện gốc dùng chung như phòng học, thiết bị, học liệu, dụng cụ, nguyên vật liệu.',
        tableTitle: 'Điều kiện môn học gốc / mẫu',
        toggleSaveLabel: 'Gán vào syllabus',
        toggleSavedLabel: 'Đã gán',
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
      taoBang('taiLieuGoc', {
        linkParentValues: syllabusMonParent.value,
        parentText: `${syllabusMonParentText.value} | Kho tài liệu gốc/mẫu dùng chung.`,
        title: 'Tài liệu gốc / mẫu',
        description: 'Kho tài liệu gốc dùng chung để gán/copy vào syllabus môn học áp dụng.',
        tableTitle: 'Tài liệu gốc / mẫu',
        toggleSaveLabel: 'Gán vào syllabus',
        toggleSavedLabel: 'Đã gán',
        disabled: !selected.syllabusMonHoc,
        disabledText: 'Cần chọn Syllabus môn học áp dụng ở tầng 9 trước.'
      }),
      taoBang('syllabusTaiLieu', {
        parentValues: syllabusMonParent.value,
        filterValues: syllabusMonFilter.value,
        parentText: syllabusMonParentText.value,
        title: 'Tài liệu đã lưu vào syllabus',
        description: 'Tài liệu đã được lưu riêng cho syllabus môn học áp dụng.',
        tableTitle: 'Tài liệu đã lưu vào syllabus',
        emptyText: 'Chưa có tài liệu nào được lưu vào syllabus này.',
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
      nganhLoaiChuongTrinhId: null,
      nganhId: null,
      trinhDoId: null,
      loaiChuongTrinhId: null,
      maChuongTrinh: '',
      tenChuongTrinh: '',
      doiTuongTuyenSinh: '',
      thoiGianDaoTao: ''
    },
    fields: [
      {key: 'nganhLoaiChuongTrinhId', required: true, hidden: true},
      {
        key: 'nganhId',
        required: true,
        label: 'Ngành',
        type: 'select',
        lookup: 'nganh',
        labelKey: ['maNganh', 'tenNganh'],
        locked: true
      },
      {
        key: 'trinhDoId',
        required: false,
        label: 'Trình độ',
        type: 'select',
        lookup: 'trinhDoDaoTao',
        labelKey: ['maTrinhDo', 'tenTrinhDo'],
        locked: true
      },
      {
        key: 'loaiChuongTrinhId',
        required: true,
        label: 'Loại CT',
        type: 'select',
        lookup: 'loaiChuongTrinh',
        labelKey: ['maLoai', 'tenLoai'],
        locked: true
      },
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
      {key: 'tenTrinhDo', label: 'Trình độ'},
      {key: 'tenLoaiChuongTrinh', label: 'Loại CT'}
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
      {key: 'ngayApDung', label: 'Ngày áp dụng', type: 'date'},
      {key: 'ngayHetHieuLuc', label: 'Ngày hết hiệu lực', type: 'date'},
      {key: 'soQuyetDinh', label: 'Số quyết định'},
      {key: 'ngayQuyetDinh', label: 'Ngày quyết định', type: 'date'},
      {key: 'nguoiKy', label: 'Người ký'},
      {key: 'coQuanBanHanh', label: 'Cơ quan ban hành'},
      {key: 'tongTinChi', label: 'Tổng tín chỉ', type: 'number', max: 9999.9, step: 0.1},
      {key: 'tongSoGio', label: 'Tổng giờ', type: 'number', max: 999999.9, step: 0.1},
      {key: 'tongGioLyThuyet', label: 'Giờ LT', type: 'number', max: 999999.9, step: 0.1},
      {key: 'tongGioThucHanh', label: 'Giờ TH', type: 'number', max: 999999.9, step: 0.1},
      {key: 'tongGioKiemTra', label: 'Giờ KT', type: 'number', max: 999999.9, step: 0.1},
      {key: 'laHienHanh', label: 'Hiện hành', type: 'boolean'}
    ],
    uniqueRules: [
      {field: 'maVersion', scopeKeys: ['chuongTrinhId'], message: 'Mã version đã tồn tại trong Chương trình đang chọn.'}
    ],
    columns: [
      {key: 'maVersion', label: 'Mã version'},
      {key: 'tenVersion', label: 'Tên version'},
      {key: 'tenChuongTrinh', label: 'Chương trình'},
      {key: 'ngayApDung', label: 'Ngày áp dụng'},
      {key: 'tongTinChi', label: 'Tín chỉ'},
      {key: 'laHienHanh', label: 'Hiện hành'}
    ]
  },
  mucTieuChuongTrinhGoc: {
    title: 'Mục tiêu chương trình gốc / mẫu',
    description: 'Kho mục tiêu gốc dùng chung. Khi bấm ✓, hệ thống gán mục tiêu gốc vào Version qua bảng chuong_trinh_version_muc_tieu.',
    defaultForm: {ma: '', loai: 'chung', noiDung: '', ghiChu: ''},
    fields: [
      {key: 'ma', label: 'Mã mục tiêu', required: true},
      {key: 'loai', label: 'Loại', type: 'select', lookup: 'loaiMucTieu', required: true},
      {key: 'noiDung', label: 'Nội dung', type: 'textarea', required: true, wide: true},
      {key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true}
    ],
    uniqueRules: [
      {field: 'ma', message: 'Mã mục tiêu gốc đã tồn tại.'}
    ],
    columns: [
      {key: 'ma', label: 'Mã'},
      {key: 'loai', label: 'Loại'},
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
      {key: 'ma', label: 'Mã mục tiêu gốc'},
      {key: 'loai', label: 'Loại', type: 'select', lookup: 'loaiMucTieu', required: true},
      {key: 'noiDung', label: 'Nội dung', type: 'textarea', required: true, wide: true},
      {key: 'thuTu', label: 'Thứ tự', type: 'number', step: 1},
      {key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true}
    ],
    columns: [
      {key: 'tenVersion', label: 'Version'},
      {key: 'ma', label: 'Mã mục tiêu gốc'},
      {key: 'loai', label: 'Loại'},
      {key: 'noiDung', label: 'Nội dung'},
      {key: 'thuTu', label: 'Thứ tự'},
      {key: 'ghiChu', label: 'Ghi chú'},
      {key: 'createdAt', label: 'Ngày tạo'},
      {key: 'updatedAt', label: 'Ngày cập nhật'}
    ]
  },
  nangLucDauRaGoc: {
    title: ' Năng lực đầu ra gốc / mẫu',
    description: 'Kho năng lực gốc dùng chung. Khi bấm ✓, hệ thống gán năng lực gốc vào Version qua bảng chuong_trinh_version_nang_luc.',
    defaultForm: {ma: '', loai: 'co_ban', noiDung: '', ghiChu: ''},
    fields: [
      {key: 'ma', label: 'Mã năng lực', required: true},
      {key: 'loai', label: 'Loại', type: 'select', lookup: 'loaiNangLuc', required: true},
      {key: 'noiDung', label: 'Nội dung', type: 'textarea', required: true, wide: true},
      {key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true}
    ],
    uniqueRules: [
      {field: 'ma', scopeKeys: ['loai'], message: 'Mã năng lực gốc đã tồn tại với loại năng lực này.'},
      {field: 'loai', scopeKeys: ['ma'], message: 'Loại năng lực đã tồn tại với mã năng lực này.'}
    ],
    columns: [
      {key: 'ma', label: 'Mã'},
      {key: 'loai', label: 'Loại'},
      {key: 'noiDung', label: 'Nội dung'},
      {key: 'thuTu', label: 'Thứ tự'},
      {key: 'ghiChu', label: 'Ghi chú'},
      {key: 'createdAt', label: 'Ngày tạo'},
      {key: 'updatedAt', label: 'Ngày cập nhật'}
    ]
  },
  nangLucDauRa: {
    title: ' Năng lực đầu ra nhập riêng - KHÔNG XÀI GỐC',
    description: 'Bảng nhập riêng trực tiếp theo Version/chương trình, không lấy từ bảng gốc và không dùng cơ chế tái sử dụng.',
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
      {key: 'loai', label: 'Loại'},
      {key: 'noiDung', label: 'Nội dung'},
      {key: 'thuTu', label: 'Thứ tự'},
      {key: 'ghiChu', label: 'Ghi chú'}
    ]
  },
  viTriViecLamGoc: {
    title: 'Vị trí việc làm gốc / mẫu',
    description: 'Kho vị trí việc làm gốc dùng chung. Khi bấm ✓, hệ thống gán vị trí vào Version qua bảng chuong_trinh_version_vi_tri_viec_lam.',
    defaultForm: {ma: '', ten: '', moTa: '', ghiChu: ''},
    fields: [
      {key: 'ma', label: 'Mã vị trí', required: true},
      {key: 'ten', label: 'Tên vị trí', required: true},
      {key: 'moTa', label: 'Mô tả', type: 'textarea', wide: true},
      {key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true}
    ],
    uniqueRules: [
      {field: 'ma', message: 'Mã vị trí việc làm gốc đã tồn tại.'}
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
      {key: 'thuTu', label: 'Thứ tự'},
      {key: 'ghiChu', label: 'Ghi chú'},
      {key: 'createdAt', label: 'Ngày tạo'},
      {key: 'updatedAt', label: 'Ngày cập nhật'}
    ]
  }, dieuKienTotNghiepGoc: {
    title: 'Điều kiện tốt nghiệp gốc / mẫu',
    description: 'Kho điều kiện tốt nghiệp gốc dùng chung. Bấm Gán vào để copy điều kiện gốc sang bảng Điều kiện tốt nghiệp đã lưu vào Version.',
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
      {field: 'ma', message: 'Mã điều kiện tốt nghiệp gốc đã tồn tại.'}
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
    description: 'Bảng điều kiện tốt nghiệp đã được copy từ điều kiện tốt nghiệp gốc/mẫu vào Version.',
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
      {key: 'ma', label: 'Mã điều kiện gốc'},
      {key: 'noiDung', label: 'Nội dung', type: 'textarea', required: true, wide: true},
      {key: 'thuTu', label: 'Thứ tự', type: 'number', step: 1},
      {key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true}
    ],
    columns: [
      {key: 'tenVersion', label: 'Version'},
      {key: 'ma', label: 'Mã điều kiện gốc'},
      {key: 'noiDung', label: 'Nội dung'},
      {key: 'thuTu', label: 'Thứ tự'},
      {key: 'ghiChu', label: 'Ghi chú'},
      {key: 'createdAt', label: 'Ngày tạo'},
      {key: 'updatedAt', label: 'Ngày cập nhật'}
    ]
  },
  chuongTrinhVersionMucTieu: {
    title: 'Mục tiêu gán vào Version - TÁI SỬ DỤNG',
    description: 'Bảng nối dùng để chọn mục tiêu gốc và gán/tái sử dụng vào Version, không phải bảng nhập riêng.',
    defaultForm: {chuongTrinhVersionId: null, mucTieuGocId: null, thuTu: null, ghiChu: ''},
    fields: [{
      key: 'chuongTrinhVersionId',
      label: 'Version',
      type: 'select',
      lookup: 'chuongTrinhVersion',
      labelKey: ['maVersion', 'tenVersion'],
      locked: true
    }, {
      key: 'mucTieuGocId',
      label: 'Mục tiêu gốc',
      type: 'select',
      lookup: 'mucTieuChuongTrinhGoc',
      labelKey: ['ma', 'noiDung'],
      required: true
    }, {key: 'thuTu', label: 'Thứ tự', type: 'number', step: 1}, {
      key: 'ghiChu',
      label: 'Ghi chú',
      type: 'textarea',
      wide: true
    }],
    columns: [{key: 'tenVersion', label: 'Version'}, {key: 'tenMucTieuGoc', label: 'Mục tiêu gốc'}, {
      key: 'thuTu',
      label: 'Thứ tự'
    }, {key: 'ghiChu', label: 'Ghi chú'}]
  },
  chuongTrinhVersionNangLuc: {
    title: 'Năng lực gán vào Version - TÁI SỬ DỤNG',
    description: 'Bảng nối dùng để chọn năng lực gốc và gán/tái sử dụng vào Version, không phải bảng nhập riêng.',
    defaultForm: {chuongTrinhVersionId: null, nangLucGocId: null, thuTu: null, ghiChu: ''},
    fields: [{
      key: 'chuongTrinhVersionId',
      label: 'Version',
      type: 'select',
      lookup: 'chuongTrinhVersion',
      labelKey: ['maVersion', 'tenVersion'],
      locked: true
    }, {
      key: 'nangLucGocId',
      label: 'Năng lực gốc',
      type: 'select',
      lookup: 'nangLucDauRaGoc',
      labelKey: ['ma', 'noiDung'],
      required: true
    }, {key: 'thuTu', label: 'Thứ tự', type: 'number', step: 1}, {
      key: 'ghiChu',
      label: 'Ghi chú',
      type: 'textarea',
      wide: true
    }],
    columns: [{key: 'tenVersion', label: 'Version'}, {key: 'tenNangLucGoc', label: 'Năng lực gốc'}, {
      key: 'thuTu',
      label: 'Thứ tự'
    }, {key: 'ghiChu', label: 'Ghi chú'}]
  },
  chuongTrinhVersionViTriViecLam: {
    title: 'Vị trí việc làm gán vào Version - TÁI SỬ DỤNG',
    description: 'Bảng nối dùng để chọn vị trí việc làm gốc và gán/tái sử dụng vào Version, không phải bảng nhập riêng.',
    defaultForm: {chuongTrinhVersionId: null, viTriGocId: null, thuTu: null, ghiChu: ''},
    fields: [{
      key: 'chuongTrinhVersionId',
      label: 'Version',
      type: 'select',
      lookup: 'chuongTrinhVersion',
      labelKey: ['maVersion', 'tenVersion'],
      locked: true
    }, {
      key: 'viTriGocId',
      label: 'Vị trí gốc',
      type: 'select',
      lookup: 'viTriViecLamGoc',
      labelKey: ['ma', 'ten'],
      required: true
    }, {key: 'thuTu', label: 'Thứ tự', type: 'number', step: 1}, {
      key: 'ghiChu',
      label: 'Ghi chú',
      type: 'textarea',
      wide: true
    }],
    columns: [{key: 'tenVersion', label: 'Version'}, {key: 'tenViTriGoc', label: 'Vị trí gốc'}, {
      key: 'thuTu',
      label: 'Thứ tự'
    }, {key: 'ghiChu', label: 'Ghi chú'}]
  },
  chuongTrinhVersionDieuKienTotNghiep: {
    title: 'Điều kiện tốt nghiệp gán vào Version - TÁI SỬ DỤNG',
    description: 'Bảng nối dùng để chọn điều kiện tốt nghiệp gốc và gán/tái sử dụng vào Version, không phải bảng nhập riêng.',
    defaultForm: {chuongTrinhVersionId: null, dieuKienGocId: null, thuTu: null, ghiChu: ''},
    fields: [{
      key: 'chuongTrinhVersionId',
      label: 'Version',
      type: 'select',
      lookup: 'chuongTrinhVersion',
      labelKey: ['maVersion', 'tenVersion'],
      locked: true
    }, {
      key: 'dieuKienGocId',
      label: 'Điều kiện gốc',
      type: 'select',
      lookup: 'dieuKienTotNghiepGoc',
      labelKey: ['ma', 'noiDung'],
      required: true
    }, {key: 'thuTu', label: 'Thứ tự', type: 'number', step: 1}, {
      key: 'ghiChu',
      label: 'Ghi chú',
      type: 'textarea',
      wide: true
    }],
    columns: [{key: 'tenVersion', label: 'Version'}, {key: 'tenDieuKienGoc', label: 'Điều kiện gốc'}, {
      key: 'thuTu',
      label: 'Thứ tự'
    }, {key: 'ghiChu', label: 'Ghi chú'}]
  },
  syllabusChuongTrinhGoc: {
    title: 'Syllabus chương trình gốc / mẫu',
    description: 'Bảng gốc/mẫu dùng chung để chọn, tái sử dụng.',
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
    }, {key: 'huongDanThucHien', label: 'Hướng dẫn thực hiện', type: 'textarea', wide: true}, {
      key: 'ghiChu',
      label: 'Ghi chú',
      type: 'textarea',
      wide: true
    }],
    uniqueRules: [{field: 'ma', message: 'Mã syllabus chương trình gốc đã tồn tại.'}],

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
      syllabusChuongTrinhGocId: null,
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
        key: 'syllabusChuongTrinhGocId',
        label: 'Syllabus gốc',
        type: 'select',
        lookup: 'syllabusChuongTrinhGoc',
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
      {key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true}
    ],
    columns: [
      {key: 'tenVersion', label: 'Version'},
      {key: 'tenSyllabusChuongTrinhGoc', label: 'Syllabus gốc'},
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
      {key: 'ghiChu', label: 'Ghi chú'},
      {key: 'createdAt', label: 'Ngày tạo'},
      {key: 'updatedAt', label: 'Ngày cập nhật'}
    ]
  },
  nhomKienThucGoc: {
    title: 'Nhóm kiến thức gốc / mẫu',
    description: 'Bảng gốc/mẫu dùng chung để chọn, tái sử dụng.',
    defaultForm: {ma: '', ten: '', loaiNhom: 'chung', moTa: ''},
    fields: [{key: 'ma', label: 'Mã nhóm gốc', required: true}, {
      key: 'ten',
      label: 'Tên nhóm gốc',
      required: true
    }, {key: 'loaiNhom', label: 'Loại nhóm', type: 'select', lookup: 'loaiNhom'}, {
      key: 'moTa',
      label: 'Mô tả',
      type: 'textarea',
      wide: true
    }],
    uniqueRules: [{field: 'ma', message: 'Mã nhóm kiến thức gốc đã tồn tại.'}],
    columns: [{key: 'ma', label: 'Mã'}, {key: 'ten', label: 'Tên nhóm gốc'}, {
      key: 'loaiNhom',
      label: 'Loại'
    }, {key: 'moTa', label: 'Mô tả'}, {key: 'createdAt', label: 'Ngày tạo'}, {key: 'updatedAt', label: 'Ngày cập nhật'}]
  },
  nhomKienThuc: {
    title: 'Nhóm kiến thức gán vào Version',
    description: 'Bảng nhóm kiến thức đã được copy từ nhóm kiến thức gốc/mẫu vào Version.',
    defaultForm: {
      chuongTrinhVersionId: null,
      nhomKienThucGocId: null,
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
        key: 'nhomKienThucGocId',
        label: 'Nhóm kiến thức gốc',
        type: 'select',
        lookup: 'nhomKienThucGoc',
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
        field: 'nhomKienThucGocId',
        scopeKeys: ['chuongTrinhVersionId'],
        message: 'Nhóm kiến thức gốc này đã được gán vào Version đang chọn.'
      }
    ],
    columns: [
      {key: 'tenVersion', label: 'Version'},
      {key: 'tenNhomKienThucGoc', label: 'Nhóm kiến thức gốc'},
      {key: 'ma', label: 'Mã'},
      {key: 'ten', label: 'Tên nhóm'},
      {key: 'loaiNhom', label: 'Loại nhóm'},
      {key: 'thuTu', label: 'Thứ tự'},
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
  nhomTuChonGoc: {
    title: 'Nhóm tự chọn gốc / mẫu',
    description: 'Bảng gốc/mẫu dùng chung để chọn, tái sử dụng.',
    defaultForm: {ma: '', ten: '', moTa: ''},
    fields: [{key: 'ma', label: 'Mã nhóm tự chọn gốc', required: true}, {
      key: 'ten',
      label: 'Tên nhóm tự chọn gốc',
      required: true
    }, {key: 'moTa', label: 'Mô tả', type: 'textarea', wide: true}],
    uniqueRules: [{field: 'ma', message: 'Mã nhóm tự chọn gốc đã tồn tại.'}],
    columns: [{key: 'ma', label: 'Mã'}, {key: 'ten', label: 'Tên nhóm tự chọn gốc'}, {
      key: 'moTa',
      label: 'Mô tả'
    }, {key: 'createdAt', label: 'Ngày tạo'}, {key: 'updatedAt', label: 'Ngày cập nhật'}]
  },
  nhomTuChon: {
    title: 'Nhóm tự chọn gán vào Version - TÁI SỬ DỤNG',
    description: 'Bảng nhóm tự chọn đã được copy từ nhóm tự chọn gốc/mẫu vào Version.',
    defaultForm: {
      chuongTrinhVersionId: null,
      nhomTuChonGocId: null,
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
        key: 'nhomTuChonGocId',
        label: 'Nhóm tự chọn gốc',
        type: 'select',
        lookup: 'nhomTuChonGoc',
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
        field: 'nhomTuChonGocId',
        scopeKeys: ['chuongTrinhVersionId'],
        message: 'Nhóm tự chọn gốc này đã được gán vào Version đang chọn.'
      }
    ],
    columns: [
      {key: 'tenVersion', label: 'Version'},
      {key: 'tenNhomTuChonGoc', label: 'Nhóm tự chọn gốc'},
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
  khungKyGoc: {
    title: 'Khung kỳ gốc / mẫu',
    description: 'Bảng gốc/mẫu dùng chung để chọn, tái sử dụng.',
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
    uniqueRules: [{field: 'maKy', message: 'Mã kỳ gốc đã tồn tại.'}, {
      field: 'thuTu',
      message: 'Thứ tự kỳ gốc đã tồn tại.'
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
      loaiChuongTrinhId: null,
      khungKyGocId: null,
      maKy: '',
      tenKy: '',
      thuTu: null
    },
    fields: [{
      key: 'chuongTrinhVersionId',
      label: 'Version',
      type: 'select',
      lookup: 'chuongTrinhVersion',
      labelKey: ['maVersion', 'tenVersion'],
      locked: true
    }, {
      key: 'loaiChuongTrinhId',
      label: 'Loại CT',
      type: 'select',
      lookup: 'loaiChuongTrinh',
      labelKey: ['maLoai', 'tenLoai'],
      locked: true
    }, {
      key: 'khungKyGocId',
      label: 'Khung kỳ gốc',
      type: 'select',
      lookup: 'khungKyGoc',
      labelKey: ['maKy', 'tenKy'],
      locked: true
    }, {key: 'maKy', label: 'Mã kỳ', required: true}, {key: 'tenKy', label: 'Tên kỳ', required: true}, {
      key: 'thuTu',
      label: 'Thứ tự',
      type: 'number',
      step: 1,
      required: true
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
      field: 'khungKyGocId',
      scopeKeys: ['chuongTrinhVersionId'],
      message: 'Khung kỳ gốc này đã được gán vào Version đang chọn.'
    }],
    columns: [{key: 'tenVersion', label: 'Version'}, {key: 'tenKhungKyGoc', label: 'Kỳ gốc'}, {
      key: 'maKy',
      label: 'Mã kỳ'
    }, {key: 'tenKy', label: 'Tên kỳ'}, {key: 'thuTu', label: 'Thứ tự'}]
  },
  monHoc: {
    title: 'Môn học gốc',
    allowToggleSelect: true,
    defaultForm: {maMon: '', tenMon: '', moTa: ''},
    fields: [
      {key: 'maMon', label: 'Mã môn', required: true},
      {key: 'tenMon', label: 'Tên môn', required: true},
      {key: 'moTa', label: 'Mô tả', type: 'textarea', wide: true}
    ],
    uniqueRules: [
      {field: 'maMon', message: 'Mã môn học gốc đã tồn tại.'}
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
      soTinChi: null,
      tongGio: null,
      gioLyThuyet: null,
      gioThucHanh: null,
      gioKiemTra: null,
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
      {key: 'soTinChi', label: 'Số tín chỉ', type: 'number', max: 9999.9, step: 0.1},
      {key: 'tongGio', label: 'Tổng giờ', type: 'number', max: 999999.9, step: 0.1},
      {key: 'gioLyThuyet', label: 'Giờ LT', type: 'number', max: 999999.9, step: 0.1},
      {key: 'gioThucHanh', label: 'Giờ TH', type: 'number', max: 999999.9, step: 0.1},
      {key: 'gioKiemTra', label: 'Giờ KT', type: 'number', max: 999999.9, step: 0.1},
      {key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true}
    ],
    requiredParentKeys: [
      {key: 'chuongTrinhVersionId', label: 'Version chương trình'},
      {key: 'monHocId', label: 'Môn học gốc'},
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
      {key: 'loai', label: 'Loại'},
      {key: 'loaiHocPhan', label: 'Loại học phần'},
      {key: 'loaiPhamVi', label: 'Phạm vi'},
      {key: 'batBuoc', label: 'Bắt buộc'},
      {key: 'laMonDieuKien', label: 'Môn điều kiện'},
      {key: 'thuTu', label: 'Thứ tự'},
      {key: 'soTinChi', label: 'Số tín chỉ'},
      {key: 'tongGio', label: 'Tổng giờ'},
      {key: 'gioLyThuyet', label: 'Giờ lý thuyết'},
      {key: 'gioThucHanh', label: 'Giờ thực hành'},
      {key: 'gioKiemTra', label: 'Giờ kiểm tra'},
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
      {key: 'loai', label: 'Loại'},
      {key: 'ghiChu', label: 'Ghi chú'}
    ]
  },
  quyDoiDiem: {
    title: '5.5. Quy đổi điểm',
    defaultForm: {
      chuongTrinhMonId: null,
      loaiMau: 'COT_DIEM',
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
      {key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true}
    ],
    columns: [
      {key: 'tenChuongTrinhMon', label: 'Môn CT'},
      {key: 'loaiMau', label: 'Loại mẫu'},
      {key: 'nguongTu', label: 'Từ'},
      {key: 'nguongDen', label: 'Đến'},
      {key: 'diemQuyDoi', label: 'Điểm'},
      {key: 'ketQua', label: 'Kết quả'},
      {key: 'tyLe', label: 'Tỷ lệ %'},
      {key: 'diemToiDa', label: 'Điểm tối đa'},
      {key: 'thuTu', label: 'Thứ tự'},
      {key: 'batBuoc', label: 'Bắt buộc'},
      {key: 'congThuc', label: 'Công thức'},
      {key: 'ghiChu', label: 'Ghi chú'}
    ]
  },
  quyDoiDiemMau: {
    title: 'Quy đổi điểm mẫu gốc - TÁI SỬ DỤNG',
    defaultForm: {
      ma: '',
      ten: '',
      loaiMau: 'COT_DIEM',
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
      {key: 'ma', label: 'Mã mẫu', required: true},
      {key: 'ten', label: 'Tên mẫu / Tên cột điểm', required: true},
      // { key: 'loaiMau', label: 'Loại mẫu', readonly: true },
      {key: 'nguongTu', label: 'Ngưỡng từ', type: 'number', max: 999.99, step: 0.01},
      {key: 'nguongDen', label: 'Ngưỡng đến', type: 'number', max: 999.99, step: 0.01},
      {key: 'diemQuyDoi', label: 'Điểm quy đổi', type: 'number', max: 999.99, step: 0.01},
      {key: 'ketQua', label: 'Kết quả', type: 'select', lookup: 'ketQua'},
      {key: 'tyLe', label: 'Tỷ lệ %', type: 'number', max: 100, step: 0.01},
      {key: 'diemToiDa', label: 'Điểm tối đa', type: 'number', max: 999.99, step: 0.01},
      {key: 'thuTu', label: 'Thứ tự', type: 'number', step: 1},
      {key: 'batBuoc', label: 'Bắt buộc', type: 'checkbox'},
      {key: 'congThuc', label: 'Công thức'},
      {key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true}
    ],
    uniqueRules: [
      {field: 'ma', message: 'Mã quy đổi điểm mẫu đã tồn tại.'}
    ],
    columns: [
      {key: 'ma', label: 'Mã mẫu'},
      {key: 'ten', label: 'Tên mẫu / Cột điểm'},
      {key: 'loaiMau', label: 'Loại mẫu'},
      {key: 'nguongTu', label: 'Từ'},
      {key: 'nguongDen', label: 'Đến'},
      {key: 'diemQuyDoi', label: 'Điểm'},
      {key: 'ketQua', label: 'Kết quả'},
      {key: 'tyLe', label: 'Tỷ lệ %'},
      {key: 'diemToiDa', label: 'Điểm tối đa'},
      {key: 'thuTu', label: 'Thứ tự'},
      {key: 'batBuoc', label: 'Bắt buộc'},
      {key: 'congThuc', label: 'Công thức'},
      {key: 'ghiChu', label: 'Ghi chú'}
    ]
  },

  chuongTrinhMonQuyDoiDiemMau: {
    title: 'Quy đổi điểm',
    defaultForm: {chuongTrinhMonId: null, quyDoiDiemMauId: null, ghiChu: ''},
    fields: [],
    columns: [
      {key: 'tenChuongTrinhMon', label: 'Môn CT'},
      {key: 'maQuyDoiDiemMau', label: 'Mã mẫu'},
      {key: 'tenQuyDoiDiemMau', label: 'Tên mẫu / Cột điểm'},
      {key: 'loaiMau', label: 'Loại mẫu'},
      {key: 'nguongTu', label: 'Từ'},
      {key: 'nguongDen', label: 'Đến'},
      {key: 'diemQuyDoi', label: 'Điểm'},
      {key: 'ketQua', label: 'Kết quả'},
      {key: 'tyLe', label: 'Tỷ lệ %'},
      {key: 'diemToiDa', label: 'Điểm tối đa'},
      {key: 'thuTu', label: 'Thứ tự'},
      {key: 'batBuoc', label: 'Bắt buộc'},
      {key: 'congThuc', label: 'Công thức'},
      {key: 'ghiChu', label: 'Ghi chú gắn'}
    ]
  },

  syllabusMonHocGoc: {
    title: 'Syllabus môn học gốc - TÁI SỬ DỤNG',
    defaultForm: {
      monHocId: null,
      ma: '',
      ten: '',
      viTri: '',
      tinhChat: '',
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
        label: 'Môn học gốc',
        type: 'select',
        lookup: 'monHoc',
        labelKey: ['maMon', 'tenMon'],
        locked: true
      },
      {key: 'ma', label: 'Mã syllabus gốc', required: true},
      {key: 'ten', label: 'Tên syllabus gốc', required: true},
      {key: 'viTri', label: 'Vị trí', type: 'textarea', wide: true},
      {key: 'tinhChat', label: 'Tính chất', type: 'textarea', wide: true},
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
      {field: 'ma', message: 'Mã syllabus môn học gốc đã tồn tại.'}
    ],
    columns: [
      {key: 'ma', label: 'Mã'},
      {key: 'ten', label: 'Tên syllabus gốc'},
      {key: 'tenMonHoc', label: 'Môn học'},
      {key: 'diemDatToiThieu', label: 'Điểm đạt'},
      {key: 'donViDiem', label: 'Đơn vị điểm'},
      {key: 'ghiChu', label: 'Ghi chú'}
    ]
  },
  syllabusMonHocGocChuongBai: {
    title: 'Chương/bài của syllabus gốc ',
    defaultForm: {
      syllabusMonHocGocId: null,
      ten: '',
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
        key: 'syllabusMonHocGocId',
        label: 'Syllabus gốc',
        type: 'select',
        lookup: 'syllabusMonHocGoc',
        labelKey: ['ma', 'ten'],
        locked: true
      },
      {key: 'ten', label: 'Tên chương/bài', required: true},
      {key: 'noiDung', label: 'Nội dung', type: 'textarea', wide: true},
      {key: 'thuTu', label: 'Thứ tự', type: 'number', step: 1},
      {key: 'tongGio', label: 'Tổng giờ', type: 'number', max: 9999.99, step: 0.01},
      {key: 'gioLyThuyet', label: 'Giờ LT', type: 'number', max: 9999.99, step: 0.01},
      {key: 'gioThucHanh', label: 'Giờ TH', type: 'number', max: 9999.99, step: 0.01},
      {key: 'gioKiemTra', label: 'Giờ KT', type: 'number', max: 9999.99, step: 0.01},
      {key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true}
    ],
    columns: [
      {key: 'tenSyllabusMonHocGoc', label: 'Syllabus gốc'},
      {key: 'ten', label: 'Tên chương/bài'},
      {key: 'thuTu', label: 'Thứ tự'},
      {key: 'tongGio', label: 'Tổng giờ'},
      {key: 'ghiChu', label: 'Ghi chú'}
    ]
  },
  syllabusMonHocGocDieuKien: {
    title: 'Điều kiện của syllabus gốc ',
    defaultForm: {syllabusMonHocGocId: null, dieuKienGocId: null, thuTu: null, ghiChu: ''},
    fields: [
      {
        key: 'syllabusMonHocGocId',
        label: 'Syllabus gốc',
        type: 'select',
        lookup: 'syllabusMonHocGoc',
        labelKey: ['ma', 'ten'],
        locked: true
      },
      {
        key: 'dieuKienGocId',
        label: 'Điều kiện gốc',
        type: 'select',
        lookup: 'dieuKienMonHocGoc',
        labelKey: ['ma', 'noiDung'],
        required: true
      },
      {key: 'thuTu', label: 'Thứ tự', type: 'number', step: 1},
      {key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true}
    ],
    columns: [
      {key: 'tenSyllabusMonHocGoc', label: 'Syllabus gốc'},
      {key: 'tenDieuKienGoc', label: 'Điều kiện gốc'},
      {key: 'thuTu', label: 'Thứ tự'},
      {key: 'ghiChu', label: 'Ghi chú'}
    ]
  },
  syllabusMonHocGocTaiLieu: {
    title: 'Tài liệu của syllabus gốc',
    defaultForm: {syllabusMonHocGocId: null, taiLieuGocId: null, thuTu: null, batBuoc: false, ghiChu: ''},
    fields: [
      {
        key: 'syllabusMonHocGocId',
        label: 'Syllabus gốc',
        type: 'select',
        lookup: 'syllabusMonHocGoc',
        labelKey: ['ma', 'ten'],
        locked: true
      },
      {
        key: 'taiLieuGocId',
        label: 'Tài liệu gốc',
        type: 'select',
        lookup: 'taiLieuGoc',
        labelKey: ['ma', 'ten'],
        required: true
      },
      {key: 'thuTu', label: 'Thứ tự', type: 'number', step: 1},
      {key: 'batBuoc', label: 'Bắt buộc', type: 'boolean'},
      {key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true}
    ],
    columns: [
      {key: 'tenSyllabusMonHocGoc', label: 'Syllabus gốc'},
      {key: 'tenTaiLieuGoc', label: 'Tài liệu gốc'},
      {key: 'thuTu', label: 'Thứ tự'},
      {key: 'batBuoc', label: 'Bắt buộc'},
      {key: 'ghiChu', label: 'Ghi chú'}
    ]
  },
  syllabusMonHoc: {
    title: 'Syllabus môn học đã gán vào môn trong chương trình',
    description: 'Bảng syllabus môn học áp dụng cho môn trong chương trình.',
    defaultForm: {
      chuongTrinhMonId: null,
      syllabusMonHocGocId: null,
      viTri: '',
      tinhChat: '',
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
        key: 'syllabusMonHocGocId',
        label: 'Syllabus môn học gốc',
        type: 'select',
        lookup: 'syllabusMonHocGoc',
        labelKey: ['ma', 'ten'],
        locked: true
      },
      { key: 'viTri', label: 'Vị trí', type: 'textarea', wide: true },
      { key: 'tinhChat', label: 'Tính chất', type: 'textarea', wide: true },
      { key: 'mucTieu', label: 'Mục tiêu', type: 'textarea', wide: true },
      { key: 'phuongPhapDanhGia', label: 'Phương pháp đánh giá', type: 'textarea', wide: true },
      { key: 'dieuKienHoanThanh', label: 'Điều kiện hoàn thành', type: 'textarea', wide: true },
      { key: 'huongDan', label: 'Hướng dẫn', type: 'textarea', wide: true },
      { key: 'diemDatToiThieu', label: 'Điểm đạt tối thiểu', type: 'number', step: 0.01 },
      { key: 'donViDiem', label: 'Đơn vị điểm', type: 'select', lookup: 'donViDiem' },
      { key: 'tyLeChuyenCanToiThieu', label: 'Tỷ lệ chuyên cần tối thiểu', type: 'number', step: 0.01 },
      { key: 'batBuocDuThi', label: 'Bắt buộc dự thi', type: 'boolean' },
      { key: 'congThucQuyDoi', label: 'Công thức quy đổi', type: 'textarea', wide: true },
      { key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true }
    ],
    columns: [
      { key: 'tenChuongTrinhMon', label: 'Môn trong CT' },
      { key: 'tenSyllabusMonHocGoc', label: 'Syllabus gốc' },
      { key: 'viTri', label: 'Vị trí' },
      { key: 'tinhChat', label: 'Tính chất' },
      { key: 'mucTieu', label: 'Mục tiêu' },
      { key: 'phuongPhapDanhGia', label: 'Phương pháp đánh giá' },
      { key: 'dieuKienHoanThanh', label: 'Điều kiện hoàn thành' },
      { key: 'huongDan', label: 'Hướng dẫn' },
      { key: 'diemDatToiThieu', label: 'Điểm đạt tối thiểu' },
      { key: 'donViDiem', label: 'Đơn vị điểm' },
      { key: 'tyLeChuyenCanToiThieu', label: 'Tỷ lệ chuyên cần tối thiểu' },
      { key: 'batBuocDuThi', label: 'Bắt buộc dự thi' },
      { key: 'congThucQuyDoi', label: 'Công thức quy đổi' },
      { key: 'ghiChu', label: 'Ghi chú' },
      { key: 'createdAt', label: 'Ngày tạo' },
      { key: 'updatedAt', label: 'Ngày cập nhật' }
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
        labelKey: ['ten', 'tenSyllabusMonHocGoc', 'ma'],
        locked: true
      },
      { key: 'loai', label: 'Loại điều kiện', type: 'select', lookup: 'loaiDieuKienMonHoc', required: true },
      { key: 'ten', label: 'Tên điều kiện', required: true },
      { key: 'noiDung', label: 'Nội dung', type: 'textarea', required: true, wide: true },
      { key: 'soLuong', label: 'Số lượng', type: 'number', step: 1 },
      { key: 'yeuCau', label: 'Yêu cầu', type: 'textarea', wide: true },
      { key: 'thuTu', label: 'Thứ tự', type: 'number', step: 1 },
      { key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true }
    ],
    columns: [
      { key: 'tenSyllabusMon', label: 'Syllabus môn' },
      { key: 'loai', label: 'Loại' },
      { key: 'ten', label: 'Tên điều kiện' },
      { key: 'noiDung', label: 'Nội dung' },
      { key: 'soLuong', label: 'Số lượng' },
      { key: 'yeuCau', label: 'Yêu cầu' },
      { key: 'thuTu', label: 'Thứ tự' },
      { key: 'ghiChu', label: 'Ghi chú' },
      { key: 'createdAt', label: 'Ngày tạo' },
      { key: 'updatedAt', label: 'Ngày cập nhật' }
    ]
  },
  dieuKienMonHocGoc: {
    title: 'Điều kiện môn học gốc / mẫu',
    description: 'Kho điều kiện thực hiện môn học dùng chung.',
    defaultForm: {
      ma: '',
      loai: 'phong_hoc',
      noiDung: '',
      ghiChu: ''
    },
    fields: [
      { key: 'ma', label: 'Mã điều kiện', required: true },
      { key: 'loai', label: 'Loại điều kiện', type: 'select', lookup: 'loaiDieuKienMonHoc', required: true },
      { key: 'noiDung', label: 'Nội dung', type: 'textarea', required: true, wide: true },
      { key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true }
    ],
    columns: [
      { key: 'ma', label: 'Mã' },
      { key: 'loai', label: 'Loại điều kiện' },
      { key: 'noiDung', label: 'Nội dung' },
      { key: 'ghiChu', label: 'Ghi chú' },
      { key: 'createdAt', label: 'Ngày tạo' },
      { key: 'updatedAt', label: 'Ngày cập nhật' }
    ]
  },
  syllabusMonHocDieuKien: {
    title: 'Điều kiện gán vào syllabus môn - TÁI SỬ DỤNG',
    defaultForm: {syllabusMonId: null, dieuKienGocId: null, thuTu: null, ghiChu: ''},
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
        key: 'dieuKienGocId',
        label: 'Điều kiện gốc',
        type: 'select',
        lookup: 'dieuKienMonHocGoc',
        labelKey: ['ma', 'noiDung'],
        required: true
      },
      {key: 'thuTu', label: 'Thứ tự', type: 'number', step: 1},
      {key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true}
    ],
    uniqueRules: [
      {
        field: 'dieuKienGocId',
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
      {key: 'tenDieuKienGoc', label: 'Điều kiện gốc'},
      {key: 'thuTu', label: 'Thứ tự'},
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
      tongGio: null,
      gioLyThuyet: null,
      gioThucHanh: null,
      gioKiemTra: null,
      thuTu: null,
      ghiChu: ''
    },
    fields: [
      {
        key: 'syllabusMonId',
        label: 'Syllabus môn',
        type: 'select',
        lookup: 'syllabusMonHoc',
        labelKey: ['ten', 'tenSyllabusMonHocGoc', 'ma'],
        locked: true
      },
      { key: 'maChuong', label: 'Mã chương', required: true },
      { key: 'ten', label: 'Tên chương/bài', required: true },
      { key: 'mucTieu', label: 'Mục tiêu', type: 'textarea', wide: true },
      { key: 'noiDung', label: 'Nội dung', type: 'textarea', wide: true },
      { key: 'tongGio', label: 'Tổng giờ', type: 'number', step: 0.1 },
      { key: 'gioLyThuyet', label: 'Giờ lý thuyết', type: 'number', step: 0.1 },
      { key: 'gioThucHanh', label: 'Giờ thực hành', type: 'number', step: 0.1 },
      { key: 'gioKiemTra', label: 'Giờ kiểm tra', type: 'number', step: 0.1 },
      { key: 'thuTu', label: 'Thứ tự', type: 'number', step: 1 },
      { key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true }
    ],
    columns: [
      { key: 'tenSyllabusMon', label: 'Syllabus môn' },
      { key: 'maChuong', label: 'Mã chương' },
      { key: 'ten', label: 'Tên chương/bài' },
      { key: 'mucTieu', label: 'Mục tiêu' },
      { key: 'noiDung', label: 'Nội dung' },
      { key: 'tongGio', label: 'Tổng giờ' },
      { key: 'gioLyThuyet', label: 'Giờ lý thuyết' },
      { key: 'gioThucHanh', label: 'Giờ thực hành' },
      { key: 'gioKiemTra', label: 'Giờ kiểm tra' },
      { key: 'thuTu', label: 'Thứ tự' },
      { key: 'ghiChu', label: 'Ghi chú' },
      { key: 'createdAt', label: 'Ngày tạo' },
      { key: 'updatedAt', label: 'Ngày cập nhật' }
    ]
  },
  taiLieuGoc: {
    title: 'Tài liệu gốc / mẫu',
    description: 'Kho tài liệu gốc dùng chung cho syllabus môn học.',
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
      { key: 'ma', label: 'Mã tài liệu', required: true },
      { key: 'ten', label: 'Tên tài liệu', required: true },
      { key: 'loai', label: 'Loại tài liệu', type: 'select', lookup: 'loaiTaiLieu' },
      { key: 'tacGia', label: 'Tác giả' },
      { key: 'nhaXuatBan', label: 'Nhà xuất bản' },
      { key: 'namXuatBan', label: 'Năm xuất bản', type: 'number' },
      { key: 'duongDan', label: 'Đường dẫn' },
      { key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true }
    ],
    columns: [
      { key: 'ma', label: 'Mã' },
      { key: 'ten', label: 'Tên tài liệu' },
      { key: 'loai', label: 'Loại' },
      { key: 'tacGia', label: 'Tác giả' },
      { key: 'nhaXuatBan', label: 'Nhà xuất bản' },
      { key: 'namXuatBan', label: 'Năm xuất bản' },
      { key: 'duongDan', label: 'Đường dẫn' },
      { key: 'ghiChu', label: 'Ghi chú' },
      { key: 'createdAt', label: 'Ngày tạo' },
      { key: 'updatedAt', label: 'Ngày cập nhật' }
    ]
  },
  syllabusMonHocTaiLieu: {
    title: 'Tài liệu gán vào syllabus môn - TÁI SỬ DỤNG',
    defaultForm: {syllabusMonId: null, taiLieuGocId: null, thuTu: null, batBuoc: false, ghiChu: ''},
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
        key: 'taiLieuGocId',
        label: 'Tài liệu gốc',
        type: 'select',
        lookup: 'taiLieuGoc',
        labelKey: ['ma', 'ten'],
        required: true
      },
      {key: 'thuTu', label: 'Thứ tự', type: 'number', step: 1},
      {key: 'batBuoc', label: 'Bắt buộc', type: 'boolean'},
      {key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true}
    ],
    uniqueRules: [
      {
        field: 'taiLieuGocId',
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
      {key: 'tenTaiLieuGoc', label: 'Tài liệu gốc'},
      {key: 'thuTu', label: 'Thứ tự'},
      {key: 'batBuoc', label: 'Bắt buộc'},
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
        labelKey: ['ten', 'tenSyllabusMonHocGoc', 'ma'],
        locked: true
      },
      { key: 'ma', label: 'Mã tài liệu', required: true },
      { key: 'ten', label: 'Tên tài liệu', required: true },
      { key: 'loai', label: 'Loại tài liệu', type: 'select', lookup: 'loaiTaiLieu' },
      { key: 'tacGia', label: 'Tác giả' },
      { key: 'nhaXuatBan', label: 'Nhà xuất bản' },
      { key: 'namXuatBan', label: 'Năm xuất bản', type: 'number' },
      { key: 'duongDan', label: 'Đường dẫn' },
      { key: 'batBuoc', label: 'Bắt buộc', type: 'boolean' },
      { key: 'thuTu', label: 'Thứ tự', type: 'number', step: 1 },
      { key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true }
    ],
    columns: [
      { key: 'tenSyllabusMon', label: 'Syllabus môn' },
      { key: 'ma', label: 'Mã tài liệu' },
      { key: 'ten', label: 'Tên tài liệu' },
      { key: 'loai', label: 'Loại' },
      { key: 'tacGia', label: 'Tác giả' },
      { key: 'nhaXuatBan', label: 'Nhà xuất bản' },
      { key: 'namXuatBan', label: 'Năm xuất bản' },
      { key: 'duongDan', label: 'Đường dẫn' },
      { key: 'batBuoc', label: 'Bắt buộc' },
      { key: 'thuTu', label: 'Thứ tự' },
      { key: 'ghiChu', label: 'Ghi chú' },
      { key: 'createdAt', label: 'Ngày tạo' },
      { key: 'updatedAt', label: 'Ngày cập nhật' }
    ]
  },
}

</script>
<style scoped>
.xay-dung-page {
  width: 100%;
  min-height: calc(100vh - 56px);
  padding: 12px;
  background: #f3f4f6;
  color: #111827;
  font-family: Arial, Helvetica, sans-serif;
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
  top: 0;
  z-index: 50;

  display: flex;
  flex-wrap: wrap;
  gap: 6px 10px;
  overflow-x: visible;
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
  margin-bottom: 10px;
  padding: 10px;
  border: 1px solid #cbd5e1;
  border-radius: 4px;
  background: #e5e7eb;
}

.group-title {
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
</style>


<style scoped>
.dao-tao-xem-page .eyebrow {
  margin: 0 0 4px;
  color: #1d4ed8;
  font-size: 11px;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: .04em;
}

.tang-nav {
  display: grid;
  grid-template-columns: repeat(9, minmax(135px, 1fr));
  gap: 8px;
  overflow-x: auto;
  margin-bottom: 10px;
  padding: 8px;
  border: 1px solid #dbeafe;
  border-radius: 6px;
  background: #ffffff;
}

.tang-btn {
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

.flow-group.mau-ngoc {
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
    grid-template-columns: repeat(9, 150px);
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
</style>
