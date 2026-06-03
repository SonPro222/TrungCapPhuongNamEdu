<template>
  <div class="xem-ctdt">
    <h1 class="xem-ctdt__title">Xem chương trình đào tạo</h1>

    <!-- Breadcrumb luồng đang chọn -->
    <nav class="breadcrumb">
      <button type="button" class="breadcrumb__item" :class="{ active: capHienTai === 1 }" @click="quayLai(1)">
        Ngành
      </button>
      <template v-if="nganhChon">
        <span class="breadcrumb__sep">→</span>
        <button type="button" class="breadcrumb__item" :class="{ active: capHienTai === 2 }" @click="quayLai(2)">
          {{ tenNganh }}
        </button>
      </template>
      <template v-if="chuongTrinhChon">
        <span class="breadcrumb__sep">→</span>
        <button type="button" class="breadcrumb__item" :class="{ active: capHienTai === 3 }" @click="quayLai(3)">
          {{ tenChuongTrinh }}
        </button>
      </template>
      <template v-if="versionChon">
        <span class="breadcrumb__sep">→</span>
        <button type="button" class="breadcrumb__item" :class="{ active: capHienTai === 4 }" @click="quayLai(4)">
          {{ tenVersion }}
        </button>
      </template>
      <template v-if="kyChon">
        <span class="breadcrumb__sep">→</span>
        <button type="button" class="breadcrumb__item" :class="{ active: capHienTai === 5 }" @click="quayLai(5)">
          {{ tenKy }}
        </button>
      </template>
      <template v-if="monChon">
        <span class="breadcrumb__sep">→</span>
        <button type="button" class="breadcrumb__item active">{{ tenMon }}</button>
      </template>
    </nav>

    <p v-if="loi" class="trang-thai trang-thai--loi">{{ loi }}</p>

    <!-- Vùng nội dung chính -->
    <div class="noi-dung">
      <!-- Đang tải danh sách -->
      <p v-if="dangTai" class="trang-thai">Đang tải dữ liệu...</p>

      <!-- Cấp 1: Ngành -->
      <template v-else-if="capHienTai === 1">
        <h2 class="cap-title">Chọn ngành</h2>
        <p v-if="!nganhList.length" class="trang-thai">Chưa có ngành nào để hiển thị.</p>
        <div v-else class="card-grid">
          <button
            v-for="item in nganhList"
            :key="item.id"
            type="button"
            class="card-nut"
            @click="chonNganh(item)"
          >
            <span class="card-nut__ten">{{ ten(item, ['tenNganh', 'ten', 'maNganh']) }}</span>
            <span v-if="item.maNganh" class="card-nut__phu">{{ item.maNganh }}</span>
          </button>
        </div>
      </template>

      <!-- Cấp 2: Chương trình -->
      <template v-else-if="capHienTai === 2">
        <h2 class="cap-title">Chương trình đào tạo của ngành</h2>
        <p v-if="!chuongTrinhList.length" class="trang-thai">Ngành này chưa có chương trình đào tạo.</p>
        <div v-else class="card-grid">
          <button
            v-for="item in chuongTrinhList"
            :key="item.id"
            type="button"
            class="card-nut"
            @click="chonChuongTrinh(item)"
          >
            <span class="card-nut__ten">{{ ten(item, ['tenChuongTrinh', 'ten', 'maChuongTrinh']) }}</span>
            <span v-if="item.maChuongTrinh" class="card-nut__phu">{{ item.maChuongTrinh }}</span>
          </button>
        </div>
      </template>

      <!-- Cấp 3: Version -->
      <template v-else-if="capHienTai === 3">
        <h2 class="cap-title">Version chương trình</h2>
        <p v-if="!versionList.length" class="trang-thai">Chương trình này chưa có version.</p>
        <div v-else class="card-grid">
          <button
            v-for="item in versionList"
            :key="item.id"
            type="button"
            class="card-nut"
            @click="chonVersion(item)"
          >
            <span class="card-nut__ten">{{ ten(item, ['tenVersion', 'maVersion', 'namBanHanh']) }}</span>
            <span class="card-nut__phu">
              <template v-if="item.maVersion">{{ item.maVersion }}</template>
              <template v-if="item.trangThai"> · {{ item.trangThai }}</template>
            </span>
          </button>
        </div>
      </template>

      <!-- Cấp 4: Kỳ -->
      <template v-else-if="capHienTai === 4">
        <h2 class="cap-title">Kỳ học</h2>
        <p v-if="!kyList.length" class="trang-thai">Version này chưa có kỳ học.</p>
        <div v-else class="card-grid">
          <button
            v-for="item in kyList"
            :key="item.id"
            type="button"
            class="card-nut"
            @click="chonKy(item)"
          >
            <span class="card-nut__ten">{{ ten(item, ['tenKy', 'maKy', 'tenKhungKyGoc']) }}</span>
            <span v-if="item.thuTu" class="card-nut__phu">Thứ tự: {{ item.thuTu }}</span>
          </button>
        </div>
      </template>

      <!-- Cấp 5: Môn trong kỳ -->
      <template v-else-if="capHienTai === 5">
        <h2 class="cap-title">Môn học trong kỳ</h2>
        <p v-if="!monList.length" class="trang-thai">Kỳ này chưa có môn học.</p>
        <div v-else class="card-grid">
          <button
            v-for="item in monList"
            :key="item.id"
            type="button"
            class="card-nut"
            @click="chonMon(item)"
          >
            <span class="card-nut__ten">{{ ten(item, ['tenMon', 'tenChuongTrinhMon', 'maMonTrongCt']) }}</span>
            <span class="card-nut__phu">
              <template v-if="item.maMonTrongCt || item.maMon">{{ item.maMonTrongCt || item.maMon }}</template>
              <template v-if="nhanBatBuoc(item)"> · {{ nhanBatBuoc(item) }}</template>
            </span>
          </button>
        </div>
      </template>

      <!-- Cấp 6 + 7: Chi tiết môn / Syllabus / Chương bài / Tài liệu -->
      <template v-else-if="capHienTai === 6">
        <p v-if="dangTaiChiTiet" class="trang-thai">Đang tải chi tiết môn...</p>
        <div v-else-if="chiTiet" class="chi-tiet">
          <h2 class="chi-tiet__ten">{{ tenMon }}</h2>
          <p v-if="maMonChiTiet" class="chi-tiet__ma">Mã môn: {{ maMonChiTiet }}</p>

          <!-- Syllabus -->
          <section class="khoi">
            <h3 class="khoi__title">Syllabus môn học áp dụng</h3>
            <p v-if="!chiTiet.syllabus" class="trang-thai">Môn này chưa có syllabus áp dụng.</p>
            <p v-else class="khoi__line">
              {{ ten(chiTiet.syllabus, ['ten', 'tenSyllabusMonHocGoc', 'maSyllabusMonHocGoc', 'mucTieu']) }}
            </p>
          </section>

          <!-- Chương bài -->
          <section class="khoi">
            <h3 class="khoi__title">Chương bài</h3>
            <p v-if="!chiTiet.syllabus" class="trang-thai">Chưa có syllabus nên chưa có chương bài.</p>
            <p v-else-if="!chiTiet.chuongBai.length" class="trang-thai">Syllabus chưa có chương bài.</p>
            <ul v-else class="ds">
              <li v-for="(cb, i) in chiTiet.chuongBai" :key="cb.id || i" class="ds__item">
                <span class="ds__ten">{{ ten(cb, ['tieuDe', 'tenChuong', 'tenBai', 'ten']) }}</span>
                <span v-if="cb.moTa || cb.noiDung" class="ds__phu">{{ cb.moTa || cb.noiDung }}</span>
              </li>
            </ul>
          </section>

          <!-- Tài liệu -->
          <section class="khoi">
            <h3 class="khoi__title">Tài liệu</h3>
            <p v-if="!chiTiet.syllabus" class="trang-thai">Chưa có syllabus nên chưa có tài liệu.</p>
            <p v-else-if="!danhSachTaiLieu.length" class="trang-thai">Syllabus chưa có tài liệu.</p>
            <ul v-else class="ds">
              <li v-for="(tl, i) in danhSachTaiLieu" :key="tl.id || i" class="ds__item ds__item--row">
                <span class="ds__tl">
                  <span class="ds__ten">{{ ten(tl, ['ten', 'tenTaiLieuGoc', 'tenFile', 'duongDan']) }}</span>
                  <span v-if="tl.loaiTaiLieu" class="ds__phu">{{ tl.loaiTaiLieu }}</span>
                </span>
                <span class="ds__actions">
                  <a v-if="coTep(tl)" class="link-xem" :href="urlXem(tl)" target="_blank" rel="noopener">Xem</a>
                  <a v-if="coTep(tl)" class="link-tai" :href="urlTai(tl)" target="_blank" rel="noopener">Tải</a>
                </span>
              </li>
            </ul>
          </section>

          <!-- Điều kiện môn học -->
          <section v-if="chiTiet.dieuKien.length" class="khoi">
            <h3 class="khoi__title">Điều kiện môn học</h3>
            <ul class="ds">
              <li v-for="(dk, i) in chiTiet.dieuKien" :key="dk.id || i" class="ds__item">
                <span class="ds__ten">{{ ten(dk, ['tenDieuKienGoc', 'loaiDieuKien', 'noiDung', 'ten']) }}</span>
                <span v-if="dk.noiDung && dk.noiDung !== ten(dk, ['tenDieuKienGoc', 'loaiDieuKien', 'noiDung', 'ten'])" class="ds__phu">
                  {{ dk.noiDung }}
                </span>
              </li>
            </ul>
          </section>

          <!-- Môn tiên quyết -->
          <section v-if="chiTiet.monTienQuyet.length" class="khoi">
            <h3 class="khoi__title">Môn tiên quyết</h3>
            <ul class="ds">
              <li v-for="(mtq, i) in chiTiet.monTienQuyet" :key="mtq.id || i" class="ds__item">
                <span class="ds__ten">{{ ten(mtq, ['tenMonDieuKien', 'tenMon', 'ten']) }}</span>
              </li>
            </ul>
          </section>
        </div>
      </template>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { ENV } from '@/core/config/env'
import { tepDinhKemUploadService } from '@/modules/daoTao/services/tepDinhKemUploadService'
import { xemChuongTrinhService, layTenHienThi } from '../services/xemChuongTrinhService'

const dangTai = ref(false)
const dangTaiChiTiet = ref(false)
const loi = ref('')

const nganhList = ref([])
const chuongTrinhList = ref([])
const versionList = ref([])
const kyList = ref([])
const monList = ref([])

const nganhChon = ref(null)
const chuongTrinhChon = ref(null)
const versionChon = ref(null)
const kyChon = ref(null)
const monChon = ref(null)
const chiTiet = ref(null)

const capHienTai = computed(() => {
  if (monChon.value) return 6
  if (kyChon.value) return 5
  if (versionChon.value) return 4
  if (chuongTrinhChon.value) return 3
  if (nganhChon.value) return 2
  return 1
})

function ten(item, keys) {
  return layTenHienThi(item, keys)
}

const tenNganh = computed(() => (nganhChon.value ? ten(nganhChon.value, ['tenNganh', 'ten', 'maNganh']) : ''))
const tenChuongTrinh = computed(() =>
  chuongTrinhChon.value ? ten(chuongTrinhChon.value, ['tenChuongTrinh', 'ten', 'maChuongTrinh']) : ''
)
const tenVersion = computed(() =>
  versionChon.value ? ten(versionChon.value, ['tenVersion', 'maVersion', 'namBanHanh']) : ''
)
const tenKy = computed(() => (kyChon.value ? ten(kyChon.value, ['tenKy', 'maKy', 'tenKhungKyGoc']) : ''))
const tenMon = computed(() => {
  if (chiTiet.value?.monHoc) return ten(chiTiet.value.monHoc, ['tenMon', 'maMon'])
  if (monChon.value) return ten(monChon.value, ['tenMon', 'tenChuongTrinhMon', 'maMonTrongCt'])
  return ''
})
const maMonChiTiet = computed(() => chiTiet.value?.monHoc?.maMon || chiTiet.value?.chuongTrinhMon?.maMonTrongCt || '')

const danhSachTaiLieu = computed(() => {
  if (!chiTiet.value) return []
  return [...(chiTiet.value.taiLieuTrucTiep || []), ...(chiTiet.value.taiLieuNoiMap || [])]
})

function nhanBatBuoc(item) {
  if (item?.batBuoc === true) return 'Bắt buộc'
  if (item?.batBuoc === false) return 'Tự chọn'
  if (item?.loaiMon) return item.loaiMon
  return ''
}

/* ----- Tệp đính kèm (dùng lại service hiện có, không bịa API) ----- */
function coTep(tl) {
  return Boolean(tl?.tepDinhKemId || tl?.duongDan)
}
function layBackendOrigin() {
  return String(ENV.API_BASE_URL || '').replace(/\/api\/?$/, '').replace(/\/$/, '')
}
function urlTai(tl) {
  return tepDinhKemUploadService.taoDuongDanTaiFile({
    id: tl?.tepDinhKemId,
    tepDinhKemId: tl?.tepDinhKemId,
    duongDanDayDu: tl?.duongDan,
    duongDanTuongDoi: tl?.duongDan
  })
}
function urlXem(tl) {
  if (tl?.tepDinhKemId) {
    return `${layBackendOrigin()}/api/he-thong/tep-dinh-kem/${tl.tepDinhKemId}/preview`
  }
  return urlTai(tl)
}

/* ----- Chọn từng cấp ----- */
async function chonNganh(item) {
  nganhChon.value = item
  chuongTrinhChon.value = null
  versionChon.value = null
  kyChon.value = null
  monChon.value = null
  chiTiet.value = null
  await taiList(() => xemChuongTrinhService.layChuongTrinhTheoNganh(item.id), chuongTrinhList)
}

async function chonChuongTrinh(item) {
  chuongTrinhChon.value = item
  versionChon.value = null
  kyChon.value = null
  monChon.value = null
  chiTiet.value = null
  await taiList(() => xemChuongTrinhService.layVersionTheoChuongTrinh(item.id), versionList)
}

async function chonVersion(item) {
  versionChon.value = item
  kyChon.value = null
  monChon.value = null
  chiTiet.value = null
  await taiList(() => xemChuongTrinhService.layKyTheoVersion(item.id), kyList)
}

async function chonKy(item) {
  kyChon.value = item
  monChon.value = null
  chiTiet.value = null
  await taiList(() => xemChuongTrinhService.layMonTheoKy(versionChon.value.id, item.id), monList)
}

async function chonMon(item) {
  monChon.value = item
  chiTiet.value = null
  loi.value = ''
  dangTaiChiTiet.value = true
  try {
    chiTiet.value = await xemChuongTrinhService.layChiTietMonXemChuongTrinh(item.id)
  } catch (e) {
    loi.value = e?.message || 'Không tải được chi tiết môn học.'
  } finally {
    dangTaiChiTiet.value = false
  }
}

/* ----- Quay lại cấp qua breadcrumb ----- */
function quayLai(cap) {
  if (cap <= 5) monChon.value = null
  if (cap <= 4) kyChon.value = null
  if (cap <= 3) versionChon.value = null
  if (cap <= 2) chuongTrinhChon.value = null
  if (cap <= 1) nganhChon.value = null
  chiTiet.value = null
}

/* ----- Helper tải danh sách ----- */
async function taiList(fn, target) {
  loi.value = ''
  dangTai.value = true
  try {
    target.value = await fn()
  } catch (e) {
    target.value = []
    loi.value = e?.message || 'Không tải được dữ liệu.'
  } finally {
    dangTai.value = false
  }
}

onMounted(async () => {
  await taiList(() => xemChuongTrinhService.layDanhSachNganhXemChuongTrinh(), nganhList)
})
</script>

<style scoped>
.xem-ctdt {
  padding: 24px;
}
.xem-ctdt__title {
  font-size: 20px;
  font-weight: 700;
  color: #1e293b;
  margin: 0 0 16px;
}

.breadcrumb {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 6px;
  background: #eff6ff;
  border: 1px solid #bfdbfe;
  border-radius: 10px;
  padding: 10px 14px;
  margin-bottom: 20px;
}
.breadcrumb__item {
  border: none;
  background: transparent;
  color: #1d4ed8;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  padding: 2px 4px;
}
.breadcrumb__item.active {
  color: #1e293b;
  cursor: default;
}
.breadcrumb__sep {
  color: #93c5fd;
}

.cap-title {
  font-size: 16px;
  font-weight: 700;
  color: #1e293b;
  margin: 0 0 14px;
}

.card-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  gap: 12px;
}
/* Nút card màu xanh dương theo yêu cầu */
.card-nut {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 4px;
  text-align: left;
  background: #2563eb;
  border: 1px solid #1d4ed8;
  border-radius: 12px;
  padding: 16px;
  cursor: pointer;
  transition: background 0.15s ease, transform 0.15s ease;
}
.card-nut:hover {
  background: #1d4ed8;
  transform: translateY(-1px);
}
.card-nut__ten {
  font-size: 15px;
  font-weight: 600;
  color: #ffffff;
}
.card-nut__phu {
  font-size: 12px;
  color: #dbeafe;
}

.trang-thai {
  color: #64748b;
  font-size: 14px;
  margin: 8px 0;
}
.trang-thai--loi {
  color: #dc2626;
}

.chi-tiet__ten {
  font-size: 18px;
  font-weight: 700;
  color: #1e293b;
  margin: 0 0 4px;
}
.chi-tiet__ma {
  color: #64748b;
  font-size: 13px;
  margin: 0 0 16px;
}
.khoi {
  background: #ffffff;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  padding: 16px;
  margin-bottom: 14px;
}
.khoi__title {
  font-size: 15px;
  font-weight: 700;
  color: #1d4ed8;
  margin: 0 0 10px;
}
.khoi__line {
  margin: 0;
  color: #1e293b;
  font-size: 14px;
}
.ds {
  list-style: none;
  margin: 0;
  padding: 0;
  display: flex;
  flex-direction: column;
  gap: 8px;
}
.ds__item {
  display: flex;
  flex-direction: column;
  gap: 2px;
  background: #f8fafc;
  border-radius: 8px;
  padding: 8px 12px;
}
.ds__item--row {
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
}
.ds__tl {
  display: flex;
  flex-direction: column;
  gap: 2px;
}
.ds__ten {
  font-size: 14px;
  font-weight: 500;
  color: #1e293b;
}
.ds__phu {
  font-size: 12px;
  color: #94a3b8;
}
.ds__actions {
  display: flex;
  gap: 10px;
}
.link-xem,
.link-tai {
  font-size: 13px;
  font-weight: 600;
  text-decoration: none;
  color: #2563eb;
}
.link-tai {
  color: #15803d;
}
</style>
