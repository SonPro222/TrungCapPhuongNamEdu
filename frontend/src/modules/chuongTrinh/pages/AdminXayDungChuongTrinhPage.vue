<template>
  <div class="build-page">
    <aside class="build-sidebar">
      <div class="build-sidebar-header">
        <h3>Danh sách chương trình đào tạo</h3>

        <FormInput
            v-model="keyword"
            placeholder="Tìm kiếm chương trình..."
        />
      </div>

      <div class="program-list">
        <button
            v-for="ct in filteredChuongTrinhs"
            :key="ct.id"
            type="button"
            class="program-card"
            :class="{ active: buildState.chuongTrinh.selectedId === ct.id }"
            @click="selectChuongTrinh(ct)"
        >
          <strong>{{ ct.tenChuongTrinh }}</strong>
          <span>{{ ct.maChuongTrinh }}</span>
          <small>{{ ct.tenNganh }} - {{ ct.tenTrinhDo }} - {{ ct.tenLoaiChuongTrinh }}</small>
        </button>
      </div>
    </aside>

    <section class="build-content">
      <div class="build-toolbar">
        <h2>Xây dựng chương trình đào tạo</h2>

        <div class="toolbar-actions">
          <BaseButton label="Hủy" @click="resetAll" />
          <BaseButton
              label="Lưu toàn bộ"
              variant="primary"
              :loading="saving"
              @click="saveAll"
          />
        </div>
      </div>

      <BuildSection
          title="1. Ngành"
          description="Chọn ngành làm nền cho chương trình"
          :locked="buildState.nganh.locked"
          @save="saveStage('nganh')"
          @toggle-lock="toggleStageLock('nganh')"
      >
        <div class="readonly-box">
          {{ selectedChuongTrinh?.tenNganh || 'Chọn chương trình ở danh sách bên trái' }}
        </div>
      </BuildSection>

      <template v-if="buildState.nganh.locked">
        <BuildSection
            title="2. Trình độ đào tạo + Loại chương trình"
            description="Xác nhận trình độ và loại chương trình"
            :locked="buildState.trinhDoLoai.locked"
            @save="saveStage('trinhDoLoai')"
            @toggle-lock="toggleStageLock('trinhDoLoai')"
        >
          <DataTable
              :columns="trinhDoLoaiColumns"
              :items="selectedChuongTrinh ? [selectedChuongTrinh] : []"
              :loading="loading"
          />
        </BuildSection>
      </template>

      <template v-if="buildState.trinhDoLoai.locked">
        <BuildSection
            title="3. Chương trình đào tạo"
            description="Xác nhận chương trình đào tạo"
            :locked="buildState.chuongTrinh.locked"
            @save="saveStage('chuongTrinh')"
            @toggle-lock="toggleStageLock('chuongTrinh')"
        >
          <DataTable
              :columns="chuongTrinhColumns"
              :items="selectedChuongTrinh ? [selectedChuongTrinh] : []"
              :loading="loading"
          />
        </BuildSection>
      </template>

      <template v-if="buildState.chuongTrinh.locked">
        <BuildSection
            title="4. Phiên bản chương trình"
            description="Chọn một phiên bản để xây dựng chi tiết"
            :locked="buildState.version.locked"
            @save="saveStage('version')"
            @toggle-lock="toggleStageLock('version')"
        >
          <DataTable
              :columns="versionColumns"
              :items="versions"
              :loading="loading"
          >
            <template #actions="{ item }">
              <input
                  type="radio"
                  name="selectedVersion"
                  :checked="buildState.version.selectedId === item.id"
                  :disabled="buildState.version.locked"
                  @change="selectVersion(item)"
              />
            </template>
          </DataTable>
        </BuildSection>
      </template>

      <template v-if="buildState.version.locked">
        <BuildSection
            title="5. Khung kỳ / học kỳ"
            description="Chọn các học kỳ thuộc phiên bản đã khóa"
            :locked="buildState.khungKy.locked"
            @save="saveStage('khungKy')"
            @toggle-lock="toggleStageLock('khungKy')"
        >
          <DataTable
              :columns="khungKyColumns"
              :items="khungKys"
              :loading="loading"
          >
            <template #actions="{ item }">
              <input
                  type="checkbox"
                  :checked="isChecked('khungKy', item.id)"
                  :disabled="buildState.khungKy.locked"
                  @change="toggleCheck('khungKy', item.id)"
              />
            </template>
          </DataTable>
        </BuildSection>
      </template>

      <template v-if="buildState.khungKy.locked">
        <BuildSection
            title="6. Nhóm kiến thức"
            description="Chọn nhóm kiến thức thuộc phiên bản"
            :locked="buildState.nhomKienThuc.locked"
            @save="saveStage('nhomKienThuc')"
            @toggle-lock="toggleStageLock('nhomKienThuc')"
        >
          <DataTable
              :columns="nhomKienThucColumns"
              :items="nhomKienThucs"
              :loading="loading"
          >
            <template #actions="{ item }">
              <input
                  type="checkbox"
                  :checked="isChecked('nhomKienThuc', item.id)"
                  :disabled="buildState.nhomKienThuc.locked"
                  @change="toggleCheck('nhomKienThuc', item.id)"
              />
            </template>
          </DataTable>
        </BuildSection>
      </template>

      <template v-if="buildState.nhomKienThuc.locked">
        <BuildSection
            title="7. Môn học trong chương trình"
            description="Chọn môn học theo khung kỳ và nhóm kiến thức"
            :locked="buildState.chuongTrinhMon.locked"
            @save="saveStage('chuongTrinhMon')"
            @toggle-lock="toggleStageLock('chuongTrinhMon')"
        >
          <DataTable
              :columns="chuongTrinhMonColumns"
              :items="chuongTrinhMons"
              :loading="loading"
          >
            <template #actions="{ item }">
              <input
                  type="checkbox"
                  :checked="isChecked('chuongTrinhMon', item.id)"
                  :disabled="buildState.chuongTrinhMon.locked"
                  @change="toggleCheck('chuongTrinhMon', item.id)"
              />
            </template>
          </DataTable>
        </BuildSection>
      </template>

      <template v-if="buildState.chuongTrinhMon.locked">
        <BuildSection
            title="8. Môn tự chọn / môn tiên quyết / điều kiện môn"
            description="Các bảng liên quan trực tiếp đến môn trong chương trình"
            :locked="buildState.monLienQuan.locked"
            @save="saveStage('monLienQuan')"
            @toggle-lock="toggleStageLock('monLienQuan')"
        >
          <DataTable
              :columns="monTuChonColumns"
              :items="monTuChons"
              :loading="loading"
          />

          <DataTable
              :columns="monTienQuyetColumns"
              :items="monTienQuyets"
              :loading="loading"
          />

          <DataTable
              :columns="dieuKienMonHocColumns"
              :items="dieuKienMonHocs"
              :loading="loading"
          />

          <DataTable
              :columns="quyDoiDiemColumns"
              :items="quyDoiDiems"
              :loading="loading"
          />
        </BuildSection>
      </template>

      <template v-if="buildState.monLienQuan.locked">
        <BuildSection
            title="9. Syllabus môn học"
            description="Syllabus theo từng môn trong chương trình"
            :locked="buildState.syllabusMonHoc.locked"
            @save="saveStage('syllabusMonHoc')"
            @toggle-lock="toggleStageLock('syllabusMonHoc')"
        >
          <DataTable
              :columns="syllabusMonHocColumns"
              :items="syllabusMonHocs"
              :loading="loading"
          />
        </BuildSection>
      </template>

      <template v-if="buildState.syllabusMonHoc.locked">
        <BuildSection
            title="10. Chương bài + tài liệu"
            description="Chi tiết chương bài và tài liệu theo syllabus môn"
            :locked="buildState.syllabusChiTiet.locked"
            @save="saveStage('syllabusChiTiet')"
            @toggle-lock="toggleStageLock('syllabusChiTiet')"
        >
          <DataTable
              :columns="syllabusChuongBaiColumns"
              :items="syllabusChuongBais"
              :loading="loading"
          />

          <DataTable
              :columns="syllabusTaiLieuColumns"
              :items="syllabusTaiLieus"
              :loading="loading"
          />
        </BuildSection>
      </template>

      <template v-if="buildState.syllabusChiTiet.locked">
        <BuildSection
            title="11. Mục tiêu / năng lực đầu ra / vị trí việc làm / điều kiện tốt nghiệp"
            description="Nhóm chuẩn đầu ra và điều kiện tốt nghiệp của phiên bản"
            :locked="buildState.chuanDauRa.locked"
            @save="saveStage('chuanDauRa')"
            @toggle-lock="toggleStageLock('chuanDauRa')"
        >
          <DataTable
              :columns="mucTieuColumns"
              :items="mucTieus"
              :loading="loading"
          />

          <DataTable
              :columns="nangLucColumns"
              :items="nangLucDauRas"
              :loading="loading"
          />

          <DataTable
              :columns="viTriColumns"
              :items="viTriViecLams"
              :loading="loading"
          />

          <DataTable
              :columns="dieuKienColumns"
              :items="dieuKienTotNghieps"
              :loading="loading"
          />
        </BuildSection>
      </template>

      <template v-if="buildState.chuanDauRa.locked">
        <BuildSection
            title="12. Syllabus chương trình"
            description="Syllabus tổng cấp phiên bản chương trình"
            :locked="buildState.syllabusChuongTrinh.locked"
            @save="saveStage('syllabusChuongTrinh')"
            @toggle-lock="toggleStageLock('syllabusChuongTrinh')"
        >
          <DataTable
              :columns="syllabusChuongTrinhColumns"
              :items="syllabusChuongTrinhs"
              :loading="loading"
          />
        </BuildSection>
      </template>

      <template v-if="buildState.syllabusChuongTrinh.locked">
        <BuildSection
            title="13. Khóa đào tạo + Lớp hành chính"
            description="Tổ chức lớp hành chính theo khóa đào tạo"
            :locked="buildState.toChucDaoTao.locked"
            @save="saveStage('toChucDaoTao')"
            @toggle-lock="toggleStageLock('toChucDaoTao')"
        >
          <DataTable
              :columns="lopColumns"
              :items="lopHanhChinhs"
              :loading="loading"
          />
        </BuildSection>
      </template>
    </section>
  </div>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'

import DataTable from '@/shared/components/DataTable.vue'
import BaseButton from '@/shared/components/BaseButton.vue'
import FormInput from '@/shared/components/FormInput.vue'

import { daoTaoService } from '@/modules/daoTao/services/daoTaoService'
import { chuongTrinhService } from '../services/chuongTrinhService'
import BuildSection from '../components/BuildSection.vue'

const loading = ref(false)
const saving = ref(false)
const keyword = ref('')

const chuongTrinhs = ref([])
const versions = ref([])
const khungKys = ref([])
const nhomKienThucs = ref([])
const chuongTrinhMons = ref([])
const monTuChons = ref([])
const monTienQuyets = ref([])
const dieuKienMonHocs = ref([])
const quyDoiDiems = ref([])
const mucTieus = ref([])
const nangLucDauRas = ref([])
const viTriViecLams = ref([])
const dieuKienTotNghieps = ref([])
const syllabusChuongTrinhs = ref([])
const syllabusMonHocs = ref([])
const syllabusChuongBais = ref([])
const syllabusTaiLieus = ref([])
const lopHanhChinhs = ref([])

const selectedChuongTrinh = ref(null)
const selectedVersion = ref(null)

const buildState = reactive({
  nganh: { selectedId: null, selectedIds: [], locked: false },
  trinhDoLoai: { selectedId: null, selectedIds: [], locked: false },
  chuongTrinh: { selectedId: null, selectedIds: [], locked: false },
  version: { selectedId: null, selectedIds: [], locked: false },
  khungKy: { selectedId: null, selectedIds: [], locked: false },
  nhomKienThuc: { selectedId: null, selectedIds: [], locked: false },
  chuongTrinhMon: { selectedId: null, selectedIds: [], locked: false },
  monLienQuan: { selectedId: null, selectedIds: [], locked: false },
  syllabusMonHoc: { selectedId: null, selectedIds: [], locked: false },
  syllabusChiTiet: { selectedId: null, selectedIds: [], locked: false },
  chuanDauRa: { selectedId: null, selectedIds: [], locked: false },
  syllabusChuongTrinh: { selectedId: null, selectedIds: [], locked: false },
  toChucDaoTao: { selectedId: null, selectedIds: [], locked: false }
})

const filteredChuongTrinhs = computed(() => {
  if (!keyword.value) return chuongTrinhs.value

  return chuongTrinhs.value.filter((item) => {
    const text = `${item.maChuongTrinh} ${item.tenChuongTrinh}`.toLowerCase()
    return text.includes(keyword.value.toLowerCase())
  })
})

const trinhDoLoaiColumns = [
  { key: 'tenTrinhDo', label: 'Trình độ đào tạo' },
  { key: 'tenLoaiChuongTrinh', label: 'Loại chương trình' }
]

const chuongTrinhColumns = [
  { key: 'maChuongTrinh', label: 'Mã chương trình' },
  { key: 'tenChuongTrinh', label: 'Tên chương trình' },
  { key: 'tenNganh', label: 'Ngành' }
]

const versionColumns = [
  { key: 'maVersion', label: 'Mã version' },
  { key: 'tenVersion', label: 'Tên version' },
  { key: 'ngayApDung', label: 'Ngày áp dụng' }
]

const khungKyColumns = [
  { key: 'maKy', label: 'Mã kỳ' },
  { key: 'tenKy', label: 'Tên kỳ' },
  { key: 'thuTu', label: 'Thứ tự' }
]

const nhomKienThucColumns = [
  { key: 'ma', label: 'Mã nhóm' },
  { key: 'ten', label: 'Tên nhóm' },
  { key: 'tongTinChi', label: 'Số tín chỉ' }
]

const chuongTrinhMonColumns = [
  { key: 'maMonTrongCt', label: 'Mã môn CT' },
  { key: 'tenMon', label: 'Tên môn học' },
  { key: 'tenNhomKienThuc', label: 'Nhóm kiến thức' },
  { key: 'tenKy', label: 'Học kỳ' },
  { key: 'soTinChi', label: 'Số tín chỉ' }
]

const monTuChonColumns = [
  { key: 'tenNhomTuChon', label: 'Nhóm tự chọn' },
  { key: 'tenChuongTrinhMon', label: 'Môn tự chọn' }
]

const monTienQuyetColumns = [
  { key: 'tenMon', label: 'Môn' },
  { key: 'tenMonDieuKien', label: 'Môn điều kiện' },
  { key: 'loai', label: 'Loại' }
]

const dieuKienMonHocColumns = [
  { key: 'tenSyllabusMon', label: 'Syllabus môn' },
  { key: 'loai', label: 'Loại' },
  { key: 'noiDung', label: 'Nội dung' }
]

const quyDoiDiemColumns = [
  { key: 'tenChuongTrinhMon', label: 'Môn trong CT' },
  { key: 'nguongTu', label: 'Ngưỡng từ' },
  { key: 'nguongDen', label: 'Ngưỡng đến' },
  { key: 'diemQuyDoi', label: 'Điểm quy đổi' }
]

const mucTieuColumns = [
  { key: 'loai', label: 'Loại' },
  { key: 'noiDung', label: 'Nội dung' }
]

const nangLucColumns = [
  { key: 'ma', label: 'Mã' },
  { key: 'noiDung', label: 'Nội dung' },
  { key: 'loai', label: 'Loại' }
]

const viTriColumns = [
  { key: 'ten', label: 'Vị trí việc làm' },
  { key: 'moTa', label: 'Mô tả' }
]

const dieuKienColumns = [
  { key: 'noiDung', label: 'Điều kiện tốt nghiệp' }
]

const syllabusChuongTrinhColumns = [
  { key: 'moTaTongQuan', label: 'Mô tả tổng quan' },
  { key: 'mucDich', label: 'Mục đích' },
  { key: 'yeuCauDaoTao', label: 'Yêu cầu đào tạo' }
]

const syllabusMonHocColumns = [
  { key: 'tenChuongTrinhMon', label: 'Môn học' },
  { key: 'viTri', label: 'Vị trí' },
  { key: 'tinhChat', label: 'Tính chất' }
]

const syllabusChuongBaiColumns = [
  { key: 'tenSyllabusMon', label: 'Syllabus môn' },
  { key: 'ten', label: 'Tên chương/bài' },
  { key: 'tongGio', label: 'Tổng giờ' }
]

const syllabusTaiLieuColumns = [
  { key: 'tenSyllabusMon', label: 'Syllabus môn' },
  { key: 'ten', label: 'Tài liệu' },
  { key: 'tacGia', label: 'Tác giả' }
]

const lopColumns = [
  { key: 'maLop', label: 'Mã lớp' },
  { key: 'tenLop', label: 'Tên lớp' },
  { key: 'tenKhoa', label: 'Khóa đào tạo' },
  { key: 'trangThai', label: 'Trạng thái' }
]

function isChecked(type, id) {
  return buildState[type].selectedIds.includes(id)
}

function toggleCheck(type, id) {
  if (buildState[type].locked) return

  if (buildState[type].selectedIds.includes(id)) {
    buildState[type].selectedIds = buildState[type].selectedIds.filter((x) => x !== id)
    return
  }

  buildState[type].selectedIds.push(id)
}

function ensureStageCanLock(stage) {
  if (stage === 'nganh') return Boolean(buildState.nganh.selectedId)
  if (stage === 'trinhDoLoai') return Boolean(selectedChuongTrinh.value)
  if (stage === 'chuongTrinh') return Boolean(buildState.chuongTrinh.selectedId)
  if (stage === 'version') return Boolean(buildState.version.selectedId)

  if (Array.isArray(buildState[stage].selectedIds)) {
    return buildState[stage].selectedIds.length > 0 || stageAllowsEmpty(stage)
  }

  return true
}

function stageAllowsEmpty(stage) {
  return [
    'monLienQuan',
    'syllabusMonHoc',
    'syllabusChiTiet',
    'chuanDauRa',
    'syllabusChuongTrinh',
    'toChucDaoTao'
  ].includes(stage)
}

async function saveStage(stage) {
  const payload = {
    stage,
    chuongTrinhId: buildState.chuongTrinh.selectedId,
    versionId: buildState.version.selectedId,
    selectedId: buildState[stage].selectedId,
    selectedIds: buildState[stage].selectedIds
  }

  console.log('SAVE_STAGE', payload)
}

async function toggleStageLock(stage) {
  if (!buildState[stage].locked && !ensureStageCanLock(stage)) {
    alert('Phải chọn dữ liệu trước khi khóa bước này.')
    return
  }

  buildState[stage].locked = !buildState[stage].locked
}

async function loadChuongTrinhs() {
  const [ctRes, nganhRes, trinhDoRes, loaiRes] = await Promise.all([
    chuongTrinhService.chuongTrinh.getAll(),
    daoTaoService.nganh.getAll(),
    daoTaoService.trinhDoDaoTao.getAll(),
    daoTaoService.loaiChuongTrinh.getAll()
  ])

  chuongTrinhs.value = ctRes.items.map((ct) => {
    const nganh = nganhRes.items.find((x) => x.id === ct.nganhId)
    const trinhDo = trinhDoRes.items.find((x) => x.id === ct.trinhDoId)
    const loai = loaiRes.items.find((x) => x.id === ct.loaiChuongTrinhId)

    return {
      ...ct,
      tenNganh: nganh?.tenNganh || ct.nganhId,
      tenTrinhDo: trinhDo?.tenTrinhDo || ct.trinhDoId,
      tenLoaiChuongTrinh: loai?.tenLoai || ct.loaiChuongTrinhId
    }
  })
}

async function selectChuongTrinh(chuongTrinh) {
  selectedChuongTrinh.value = chuongTrinh
  selectedVersion.value = null

  resetAfterChuongTrinh()

  buildState.nganh.selectedId = chuongTrinh.nganhId
  buildState.trinhDoLoai.selectedId = `${chuongTrinh.trinhDoId}-${chuongTrinh.loaiChuongTrinhId}`
  buildState.chuongTrinh.selectedId = chuongTrinh.id

  const res = await chuongTrinhService.chuongTrinhVersion.getAll()
  versions.value = res.items.filter((item) => item.chuongTrinhId === chuongTrinh.id)
}

async function selectVersion(version) {
  selectedVersion.value = version
  buildState.version.selectedId = version.id
  resetAfterVersion()
  await loadVersionData(version.id)
}

function resetAfterChuongTrinh() {
  Object.keys(buildState).forEach((key) => {
    buildState[key].locked = false
    buildState[key].selectedIds = []
  })

  buildState.version.selectedId = null
  resetChildData()
}

function resetAfterVersion() {
  buildState.khungKy.locked = false
  buildState.nhomKienThuc.locked = false
  buildState.chuongTrinhMon.locked = false
  buildState.monLienQuan.locked = false
  buildState.syllabusMonHoc.locked = false
  buildState.syllabusChiTiet.locked = false
  buildState.chuanDauRa.locked = false
  buildState.syllabusChuongTrinh.locked = false
  buildState.toChucDaoTao.locked = false

  buildState.khungKy.selectedIds = []
  buildState.nhomKienThuc.selectedIds = []
  buildState.chuongTrinhMon.selectedIds = []
}

function resetChildData() {
  versions.value = []
  khungKys.value = []
  nhomKienThucs.value = []
  chuongTrinhMons.value = []
  monTuChons.value = []
  monTienQuyets.value = []
  dieuKienMonHocs.value = []
  quyDoiDiems.value = []
  mucTieus.value = []
  nangLucDauRas.value = []
  viTriViecLams.value = []
  dieuKienTotNghieps.value = []
  syllabusChuongTrinhs.value = []
  syllabusMonHocs.value = []
  syllabusChuongBais.value = []
  syllabusTaiLieus.value = []
  lopHanhChinhs.value = []
}

async function loadVersionData(versionId) {
  loading.value = true

  try {
    const [
      khungKyRes,
      nhomRes,
      ctmRes,
      monHocRes,
      monTuChonRes,
      monTienQuyetRes,
      dieuKienMonHocRes,
      quyDoiDiemRes,
      mucTieuRes,
      nangLucRes,
      viTriRes,
      dieuKienRes,
      syllabusCtRes,
      syllabusMonRes,
      chuongBaiRes,
      taiLieuRes,
      lopRes,
      khoaRes
    ] = await Promise.all([
      daoTaoService.khungKy.getAll(),
      chuongTrinhService.nhomKienThuc.getAll(),
      chuongTrinhService.chuongTrinhMon.getAll(),
      chuongTrinhService.monHoc.getAll(),
      chuongTrinhService.monTuChon.getAll(),
      chuongTrinhService.monTienQuyet.getAll(),
      chuongTrinhService.dieuKienMonHoc.getAll(),
      chuongTrinhService.quyDoiDiem.getAll(),
      chuongTrinhService.mucTieuChuongTrinh.getAll(),
      chuongTrinhService.nangLucDauRa.getAll(),
      chuongTrinhService.viTriViecLam.getAll(),
      chuongTrinhService.dieuKienTotNghiep.getAll(),
      chuongTrinhService.syllabusChuongTrinh.getAll(),
      chuongTrinhService.syllabusMonHoc.getAll(),
      chuongTrinhService.syllabusChuongBai.getAll(),
      chuongTrinhService.syllabusTaiLieu.getAll(),
      daoTaoService.lopHanhChinh.getAll(),
      daoTaoService.khoaDaoTao.getAll()
    ])

    const monHocs = monHocRes.items

    khungKys.value = khungKyRes.items.filter((x) => x.chuongTrinhVersionId === versionId)
    nhomKienThucs.value = nhomRes.items.filter((x) => x.chuongTrinhVersionId === versionId)

    chuongTrinhMons.value = ctmRes.items
        .filter((x) => x.chuongTrinhVersionId === versionId)
        .map((item) => {
          const mon = monHocs.find((x) => x.id === item.monHocId)
          const nhom = nhomKienThucs.value.find((x) => x.id === item.nhomKienThucId)
          const ky = khungKys.value.find((x) => x.id === item.khungKyId)

          return {
            ...item,
            tenMon: mon?.tenMon || mon?.tenMonHoc || item.monHocId,
            tenNhomKienThuc: nhom?.ten || item.nhomKienThucId,
            tenKy: ky?.tenKy || item.khungKyId
          }
        })

    monTuChons.value = monTuChonRes.items
        .filter((item) => chuongTrinhMons.value.some((ctm) => ctm.id === item.chuongTrinhMonId))
        .map((item) => {
          const ctm = chuongTrinhMons.value.find((x) => x.id === item.chuongTrinhMonId)

          return {
            ...item,
            tenChuongTrinhMon: ctm?.tenMon || item.chuongTrinhMonId
          }
        })

    monTienQuyets.value = monTienQuyetRes.items
        .filter((item) => chuongTrinhMons.value.some((ctm) => ctm.id === item.monId))
        .map((item) => {
          const mon = chuongTrinhMons.value.find((x) => x.id === item.monId)
          const dieuKien = chuongTrinhMons.value.find((x) => x.id === item.monDieuKienId)

          return {
            ...item,
            tenMon: mon?.tenMon || item.monId,
            tenMonDieuKien: dieuKien?.tenMon || item.monDieuKienId
          }
        })

    syllabusMonHocs.value = syllabusMonRes.items
        .filter((x) => chuongTrinhMons.value.some((ctm) => ctm.id === x.chuongTrinhMonId))
        .map((item) => {
          const ctm = chuongTrinhMons.value.find((x) => x.id === item.chuongTrinhMonId)

          return {
            ...item,
            tenChuongTrinhMon: ctm?.tenMon || item.chuongTrinhMonId
          }
        })

    dieuKienMonHocs.value = dieuKienMonHocRes.items
        .filter((item) => syllabusMonHocs.value.some((sm) => sm.id === item.syllabusMonId))
        .map((item) => ({
          ...item,
          tenSyllabusMon: item.syllabusMonId
        }))

    quyDoiDiems.value = quyDoiDiemRes.items
        .filter((item) => chuongTrinhMons.value.some((ctm) => ctm.id === item.chuongTrinhMonId))
        .map((item) => {
          const ctm = chuongTrinhMons.value.find((x) => x.id === item.chuongTrinhMonId)

          return {
            ...item,
            tenChuongTrinhMon: ctm?.tenMon || item.chuongTrinhMonId
          }
        })

    mucTieus.value = mucTieuRes.items.filter((x) => x.chuongTrinhVersionId === versionId)
    nangLucDauRas.value = nangLucRes.items.filter((x) => x.chuongTrinhVersionId === versionId)
    viTriViecLams.value = viTriRes.items.filter((x) => x.chuongTrinhVersionId === versionId)
    dieuKienTotNghieps.value = dieuKienRes.items.filter((x) => x.chuongTrinhVersionId === versionId)
    syllabusChuongTrinhs.value = syllabusCtRes.items.filter((x) => x.chuongTrinhVersionId === versionId)

    syllabusChuongBais.value = chuongBaiRes.items
        .filter((item) => syllabusMonHocs.value.some((sm) => sm.id === item.syllabusMonId))
        .map((item) => ({
          ...item,
          tenSyllabusMon: item.syllabusMonId
        }))

    syllabusTaiLieus.value = taiLieuRes.items
        .filter((item) => syllabusMonHocs.value.some((sm) => sm.id === item.syllabusMonId))
        .map((item) => ({
          ...item,
          tenSyllabusMon: item.syllabusMonId
        }))

    lopHanhChinhs.value = lopRes.items
        .filter((x) => x.chuongTrinhVersionId === versionId)
        .map((item) => {
          const khoa = khoaRes.items.find((x) => x.id === item.khoaDaoTaoId)

          return {
            ...item,
            tenKhoa: khoa?.tenKhoa || item.khoaDaoTaoId
          }
        })

    buildState.khungKy.selectedIds = khungKys.value.map((x) => x.id)
    buildState.nhomKienThuc.selectedIds = nhomKienThucs.value.map((x) => x.id)
    buildState.chuongTrinhMon.selectedIds = chuongTrinhMons.value.map((x) => x.id)
  } finally {
    loading.value = false
  }
}

function resetAll() {
  selectedChuongTrinh.value = null
  selectedVersion.value = null

  Object.keys(buildState).forEach((key) => {
    buildState[key].selectedId = null
    buildState[key].selectedIds = []
    buildState[key].locked = false
  })

  resetChildData()
}

async function saveAll() {
  saving.value = true

  try {
    console.log('SAVE_ALL_BUILD', buildState)
  } finally {
    saving.value = false
  }
}

onMounted(loadChuongTrinhs)
</script>

<style scoped>
.build-page {
  display: grid;
  grid-template-columns: 360px 1fr;
  min-height: calc(100vh - var(--header-height) - 48px);
  background: var(--color-white);
  border: 1px solid var(--color-border);
}

.build-sidebar {
  border-right: 1px solid var(--color-border);
  padding: 16px;
  overflow: auto;
}

.build-sidebar-header {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.program-list {
  margin-top: 16px;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.program-card {
  text-align: left;
  border: 1px solid var(--color-border);
  border-radius: var(--radius);
  background: var(--color-white);
  padding: 14px;
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.program-card.active {
  background: #eef5ff;
  border-color: var(--color-primary);
}

.build-content {
  padding: 16px;
  overflow: auto;
}

.build-toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 12px;
}

.toolbar-actions {
  display: flex;
  gap: 10px;
}

.readonly-box {
  border: 1px solid var(--color-border);
  border-radius: var(--radius);
  padding: 10px 12px;
  background: var(--color-white);
}
</style>