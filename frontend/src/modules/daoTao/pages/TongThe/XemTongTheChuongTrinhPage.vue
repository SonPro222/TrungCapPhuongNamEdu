<template>
  <div class="dao-tao-tong-quan-page" @click="dongMenuVersion">
    <section class="page-header">
      <div>
        <p class="eyebrow">Chương trình đào tạo</p>
        <h1>Tổng quan chương trình đào tạo</h1>
        <p class="subtitle">
          Xem cấu trúc phân cấp Ngành → Chương trình đào tạo → Version.
          Chọn version để xem kỳ hoặc syllabus chương trình.
        </p>
      </div>

      <div class="header-actions">
        <button type="button" class="btn" :disabled="loading" @click.stop="taiDuLieuTongQuan">
          Tải lại
        </button>
      </div>
    </section>

    <section v-if="errorMessage" class="state-card error">
      <h3>Không tải được dữ liệu</h3>
      <p>{{ errorMessage }}</p>
      <button type="button" class="btn primary" @click.stop="taiDuLieuTongQuan">
        Thử lại
      </button>
    </section>

    <section v-if="thongBao" class="notice-card">
      {{ thongBao }}
    </section>

    <section class="filter-card">
      <div class="filter-grid">
        <label>
          <span>Ngành</span>
          <select v-model="boLoc.nganhId">
            <option value="">Tất cả ngành</option>
            <option
                v-for="nganh in danhSachNganh"
                :key="layId(nganh)"
                :value="String(layId(nganh))"
            >
              {{ layTenNganh(nganh) }}
            </option>
          </select>
        </label>

        <label>
          <span>Tìm kiếm</span>
          <input
              v-model.trim="boLoc.keyword"
              type="text"
              placeholder="Nhập ngành, chương trình hoặc version..."
          />
        </label>

        <label>
          <span>Hiện hành</span>
          <select v-model="boLoc.hienHanh">
            <option value="">Tất cả</option>
            <option value="co">Có</option>
            <option value="khong">Không</option>
            <option value="khong-ro">Không rõ</option>
          </select>
        </label>
      </div>
    </section>

    <section class="overview-section">
      <div class="section-title">
        <div>
          <h2>Danh sách theo ngành</h2>
          <p>Mỗi ngành gồm các chương trình đào tạo, mỗi chương trình có danh sách version riêng.</p>
        </div>

        <span class="badge neutral">{{ danhSachNganhGroupDaLoc.length }} ngành</span>
      </div>

      <div v-if="loading" class="state-card loading-card">
        <div class="spinner"></div>
        <div>
          <h3>Đang tải tổng quan chương trình...</h3>
          <p>Hệ thống đang lấy danh sách ngành, chương trình đào tạo và version.</p>
        </div>
      </div>

      <div v-else-if="!danhSachNganhGroupDaLoc.length" class="empty-card">
        Chưa có dữ liệu phù hợp với bộ lọc hiện tại.
      </div>

      <div v-else class="industry-list">
        <article
            v-for="nganhGroup in danhSachNganhGroupDaLoc"
            :key="nganhGroup.rowKey"
            class="industry-card"
        >
          <header class="industry-header">
            <div>
              <p class="industry-label">Ngành</p>
              <h3>{{ nganhGroup.tenNganh }}</h3>
            </div>

            <span class="program-count">
              {{ nganhGroup.chuongTrinhList.length }} chương trình đào tạo
            </span>
          </header>

          <div class="program-list">
            <div v-if="!nganhGroup.chuongTrinhList.length" class="program-empty">
              Ngành này chưa có chương trình đào tạo.
            </div>

            <section
                v-for="chuongTrinh in nganhGroup.chuongTrinhList"
                v-else
                :key="chuongTrinh.rowKey"
                class="program-item"
            >
              <button
                  type="button"
                  class="program-toggle"
                  @click.stop="toggleChuongTrinh(chuongTrinh.rowKey)"
              >
                <span class="toggle-icon">
                  {{ chuongTrinhDangMo.has(chuongTrinh.rowKey) ? '▾' : '▸' }}
                </span>

                <span class="program-name">
                  {{ chuongTrinh.tenChuongTrinh }}
                </span>

                <span class="version-count">
                  {{ chuongTrinh.versionList.length }} version
                </span>
              </button>

              <div v-if="chuongTrinhDangMo.has(chuongTrinh.rowKey)" class="version-box">
                <div class="version-box-title">
                  Version
                </div>

                <div v-if="!chuongTrinh.versionList.length" class="version-empty">
                  Chương trình này chưa có version.
                </div>

                <div
                    v-for="version in chuongTrinh.versionList"
                    v-else
                    :key="version.rowKey"
                    class="version-row"
                >
                  <div class="version-info">
                    <strong>{{ version.tenVersion }}</strong>

                    <span :class="['current-badge', version.hienHanhClass]">
                      Hiện hành: {{ version.hienHanhText }}
                    </span>
                  </div>

                  <div class="version-actions" @click.stop>
                    <button
                        type="button"
                        class="icon-btn"
                        title="Thao tác"
                        @click="toggleMenuVersion(version.rowKey)"
                    >
                      ⋮
                    </button>

                    <div v-if="menuVersionDangMo === version.rowKey" class="menu-popover">
                      <button type="button" @click="diDenTrangKy(chuongTrinh, version)">
                        Xem kỳ
                      </button>
                      <button type="button" @click="diDenSyllabusChuongTrinh(nganhGroup, chuongTrinh, version)">
                        Xem syllabus chương trình
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            </section>
          </div>
        </article>
      </div>
    </section>
  </div>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { daoTaoService } from '../../services/daoTaoService.js'
import { layThongBaoLoi } from '../../utils/layThongBaoLoi.js'

const router = useRouter()

const TEN_ROUTE_TRANG_KY = 'dao-tao-chuong-trinh-ky'
const TEN_ROUTE_SYLLABUS_CHUONG_TRINH = 'dao-tao-syllabus-chuong-trinh-xem'

const loading = ref(false)
const errorMessage = ref('')
const thongBao = ref('')

const danhSachNganh = ref([])
const danhSachChuongTrinh = ref([])
const danhSachVersion = ref([])

const chuongTrinhDangMo = ref(new Set())
const menuVersionDangMo = ref(null)

const boLoc = reactive({
  nganhId: '',
  keyword: '',
  hienHanh: ''
})

const danhSachNganhGroup = computed(() => {
  const nganhMap = taoMapTheoId(danhSachNganh.value)
  const chuongTrinhTheoNganh = gomChuongTrinhTheoNganh(danhSachChuongTrinh.value)
  const versionTheoChuongTrinh = gomVersionTheoChuongTrinh(danhSachVersion.value)

  const groups = []

  nganhMap.forEach((nganh, nganhKey) => {
    const chuongTrinhRawList = chuongTrinhTheoNganh.get(nganhKey) || []

    groups.push({
      rowKey: `nganh-${nganhKey}`,
      nganhId: layId(nganh),
      rawNganh: nganh,
      tenNganh: layTenNganh(nganh),
      chuongTrinhList: chuongTrinhRawList.map((chuongTrinh, index) => {
        const chuongTrinhId = layId(chuongTrinh)
        const versions = versionTheoChuongTrinh.get(String(chuongTrinhId)) || []

        return {
          rowKey: `ct-${chuongTrinhId || index}`,
          chuongTrinhId,
          rawChuongTrinh: chuongTrinh,
          tenChuongTrinh: layTenChuongTrinh(chuongTrinh),
          versionList: versions.map((version, versionIndex) =>
              normalizeVersion(version, chuongTrinhId, versionIndex)
          )
        }
      })
    })
  })

  const chuongTrinhKhongCoNganh = danhSachChuongTrinh.value.filter((chuongTrinh) => {
    const nganhId = layTruong(chuongTrinh, ['nganhId', 'nganh_id', 'nganh.id'])
    return !nganhId || !nganhMap.has(String(nganhId))
  })

  if (chuongTrinhKhongCoNganh.length) {
    groups.push({
      rowKey: 'nganh-chua-xac-dinh',
      nganhId: null,
      rawNganh: null,
      tenNganh: 'Chưa xác định ngành',
      chuongTrinhList: chuongTrinhKhongCoNganh.map((chuongTrinh, index) => {
        const chuongTrinhId = layId(chuongTrinh)
        const versions = versionTheoChuongTrinh.get(String(chuongTrinhId)) || []

        return {
          rowKey: `ct-khong-nganh-${chuongTrinhId || index}`,
          chuongTrinhId,
          rawChuongTrinh: chuongTrinh,
          tenChuongTrinh: layTenChuongTrinh(chuongTrinh),
          versionList: versions.map((version, versionIndex) =>
              normalizeVersion(version, chuongTrinhId, versionIndex)
          )
        }
      })
    })
  }

  return groups
})

const danhSachNganhGroupDaLoc = computed(() => {
  const keyword = boLoc.keyword.trim().toLowerCase()

  return danhSachNganhGroup.value
      .map((nganhGroup) => {
        if (boLoc.nganhId && String(nganhGroup.nganhId || '') !== String(boLoc.nganhId)) {
          return null
        }

        const matchNganh = keyword
            ? nganhGroup.tenNganh.toLowerCase().includes(keyword)
            : true

        const chuongTrinhList = nganhGroup.chuongTrinhList
            .map((chuongTrinh) => {
              const matchChuongTrinh = keyword
                  ? [
                    nganhGroup.tenNganh,
                    chuongTrinh.tenChuongTrinh
                  ].join(' ').toLowerCase().includes(keyword)
                  : true

              const versionList = chuongTrinh.versionList.filter((version) => {
                if (boLoc.hienHanh === 'co' && version.hienHanhText !== 'Có') return false
                if (boLoc.hienHanh === 'khong' && version.hienHanhText !== 'Không') return false
                if (boLoc.hienHanh === 'khong-ro' && version.hienHanhText !== '—') return false

                if (keyword) {
                  const text = [
                    nganhGroup.tenNganh,
                    chuongTrinh.tenChuongTrinh,
                    version.tenVersion,
                    version.hienHanhText
                  ].join(' ').toLowerCase()

                  if (!text.includes(keyword) && !matchChuongTrinh) return false
                }

                return true
              })

              if (!matchChuongTrinh && !versionList.length) return null

              return {
                ...chuongTrinh,
                versionList: matchChuongTrinh ? chuongTrinh.versionList : versionList
              }
            })
            .filter(Boolean)

        /*
          Vẫn hiển thị ngành mới tạo dù chưa có chương trình đào tạo.
          Chỉ ẩn ngành rỗng khi đang lọc theo trạng thái hiện hành,
          vì ngành rỗng không có version để lọc Có/Không/Không rõ.
        */
        if (!chuongTrinhList.length) {
          if (boLoc.hienHanh) return null
          if (!matchNganh) return null

          return {
            ...nganhGroup,
            chuongTrinhList: []
          }
        }

        return {
          ...nganhGroup,
          chuongTrinhList
        }
      })
      .filter(Boolean)
})

onMounted(() => {
  taiDuLieuTongQuan()
})

async function taiDuLieuTongQuan() {
  loading.value = true
  errorMessage.value = ''
  thongBao.value = ''
  menuVersionDangMo.value = null

  try {
    const [nganhRes, chuongTrinhRes, versionRes] = await Promise.all([
      daoTaoService.nganh.getAll({ size: 1000 }),
      daoTaoService.chuongTrinh.getAll({ size: 1000 }),
      daoTaoService.chuongTrinhVersion.getAll({ size: 1000 })
    ])

    danhSachNganh.value = layDanhSachTuResponse(nganhRes)
    danhSachChuongTrinh.value = layDanhSachTuResponse(chuongTrinhRes)
    danhSachVersion.value = layDanhSachTuResponse(versionRes)

    moChuongTrinhDauTien()
  } catch (error) {
    errorMessage.value = layThongBaoLoi(error, 'Không tải được tổng quan chương trình đào tạo.')
  } finally {
    loading.value = false
  }
}

function moChuongTrinhDauTien() {
  const firstGroup = danhSachNganhGroup.value[0]
  const firstProgram = firstGroup?.chuongTrinhList?.[0]

  if (!firstProgram) return

  chuongTrinhDangMo.value = new Set([firstProgram.rowKey])
}

function toggleChuongTrinh(rowKey) {
  const next = new Set(chuongTrinhDangMo.value)

  if (next.has(rowKey)) {
    next.delete(rowKey)
  } else {
    next.add(rowKey)
  }

  chuongTrinhDangMo.value = next
  menuVersionDangMo.value = null
}

function toggleMenuVersion(rowKey) {
  menuVersionDangMo.value = menuVersionDangMo.value === rowKey ? null : rowKey
}

function dongMenuVersion() {
  menuVersionDangMo.value = null
}

function diDenTrangKy(chuongTrinh, version) {
  if (!chuongTrinh?.chuongTrinhId) {
    thongBao.value = 'Không xác định được chương trình đào tạo để xem kỳ.'
    menuVersionDangMo.value = null
    return
  }

  const routePayload = {
    name: TEN_ROUTE_TRANG_KY,
    params: {
      chuongTrinhId: chuongTrinh.chuongTrinhId
    },
    query: {}
  }

  if (version?.versionId) {
    routePayload.query.versionId = version.versionId
  }

  menuVersionDangMo.value = null

  router.push(routePayload).catch(() => {
    thongBao.value = 'Route xem kỳ chưa được cấu hình. Vui lòng kiểm tra tongTheRoutes.'
  })
}
function diDenSyllabusChuongTrinh(nganhGroup, chuongTrinh, version) {
  const nganhId = nganhGroup?.nganhId || layId(nganhGroup?.rawNganh)
  const chuongTrinhId = chuongTrinh?.chuongTrinhId || layId(chuongTrinh?.rawChuongTrinh)
  const versionId = version?.versionId || layId(version?.rawVersion)

  if (!nganhId) {
    thongBao.value = 'Không xác định được ngành để xem syllabus chương trình.'
    menuVersionDangMo.value = null
    return
  }

  if (!chuongTrinhId) {
    thongBao.value = 'Không xác định được chương trình đào tạo để xem syllabus chương trình.'
    menuVersionDangMo.value = null
    return
  }

  if (!versionId) {
    thongBao.value = 'Không xác định được version chương trình để xem syllabus chương trình.'
    menuVersionDangMo.value = null
    return
  }

  menuVersionDangMo.value = null

  router.push({
    name: TEN_ROUTE_SYLLABUS_CHUONG_TRINH,
    params: {
      chuongTrinhId
    },
    query: {
      nganhId,
      versionId,
      chuongTrinhVersionId: versionId
    }
  }).catch(() => {
    thongBao.value = 'Route xem syllabus chương trình chưa được cấu hình. Vui lòng kiểm tra tongTheRoutes.'
  })
}


function baoChucNangSau(tenChucNang) {
  thongBao.value = `Chức năng "${tenChucNang}" sẽ được bổ sung sau.`
  menuVersionDangMo.value = null
}

function normalizeVersion(version, chuongTrinhId, index) {
  const versionId = layId(version)
  const hienHanhText = mapHienHanh(version)
  const tenVersion = layTenVersion(version)

  return {
    rowKey: `ct-${chuongTrinhId || 'none'}-v-${versionId || index}`,
    versionId,
    rawVersion: version,
    tenVersion,
    hienHanhText,
    hienHanhClass: hienHanhText === 'Có' ? 'success' : hienHanhText === 'Không' ? 'muted' : 'neutral'
  }
}

function mapHienHanh(version) {
  const value =
      version?.laHienHanh ??
      version?.hienHanh ??
      version?.isCurrent ??
      version?.current ??
      null

  if (value === true || value === 1) return 'Có'
  if (value === false || value === 0) return 'Không'

  if (typeof value === 'string') {
    const normalized = value.trim().toLowerCase()

    if (['true', '1', 'co', 'có', 'hien_hanh', 'hiện hành', 'dang_ap_dung', 'đang áp dụng'].includes(normalized)) {
      return 'Có'
    }

    if (['false', '0', 'khong', 'không', 'khong_hien_hanh', 'không hiện hành'].includes(normalized)) {
      return 'Không'
    }
  }

  return '—'
}

function layDanhSachTuResponse(res) {
  const data = layDataTuApiResponse(res)

  if (Array.isArray(data)) return data
  if (Array.isArray(data?.content)) return data.content
  if (Array.isArray(data?.items)) return data.items
  if (Array.isArray(data?.data)) return data.data
  if (Array.isArray(res?.content)) return res.content
  if (Array.isArray(res?.items)) return res.items

  return []
}

function layDataTuApiResponse(res) {
  if (res?.success !== undefined && res?.data !== undefined) return res.data
  if (res?.data?.success !== undefined && res?.data?.data !== undefined) return res.data.data
  if (res?.data !== undefined) return res.data
  return res
}

function taoMapTheoId(items) {
  const map = new Map()

  items.forEach((item) => {
    const id = layId(item)
    if (id !== null && id !== undefined) map.set(String(id), item)
  })

  return map
}

function gomChuongTrinhTheoNganh(chuongTrinhList) {
  const map = new Map()

  chuongTrinhList.forEach((chuongTrinh) => {
    const nganhId =
        layTruong(chuongTrinh, ['nganhId', 'nganh_id']) ||
        chuongTrinh?.nganh?.id

    if (!nganhId) return

    const key = String(nganhId)

    if (!map.has(key)) map.set(key, [])

    map.get(key).push(chuongTrinh)
  })

  return map
}

function gomVersionTheoChuongTrinh(versions) {
  const map = new Map()

  versions.forEach((version) => {
    const chuongTrinhId =
        version?.chuongTrinhId ||
        version?.chuong_trinh_id ||
        version?.chuongTrinh?.id

    if (!chuongTrinhId) return

    const key = String(chuongTrinhId)

    if (!map.has(key)) map.set(key, [])

    map.get(key).push(version)
  })

  map.forEach((items, key) => {
    map.set(key, [...items].sort(soSanhVersionMoiNhat))
  })

  return map
}

function soSanhVersionMoiNhat(a, b) {
  const aCurrent = mapHienHanh(a) === 'Có' ? 1 : 0
  const bCurrent = mapHienHanh(b) === 'Có' ? 1 : 0

  if (aCurrent !== bCurrent) return bCurrent - aCurrent

  const aTime = layGiaTriThoiGianVersion(a)
  const bTime = layGiaTriThoiGianVersion(b)

  if (aTime !== bTime) return bTime - aTime

  return Number(b?.id || 0) - Number(a?.id || 0)
}

function layGiaTriThoiGianVersion(version) {
  const value = version?.ngayApDung || version?.updatedAt || version?.createdAt || null

  if (!value) return 0

  const time = new Date(value).getTime()

  return Number.isNaN(time) ? 0 : time
}

function layId(item) {
  return item?.id ?? item?.value ?? null
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

function layTenNganh(item) {
  if (!item) return 'Chưa xác định ngành'
  return item.tenNganh || item.ten || item.name || item.maNganh || 'Chưa xác định ngành'
}

function layTenChuongTrinh(item) {
  if (!item) return 'Chưa có tên chương trình'
  return item.tenChuongTrinh || item.ten || item.name || item.maChuongTrinh || 'Chưa có tên chương trình'
}

function layTenVersion(item) {
  if (!item) return 'Chưa có version'

  const ten = item.tenVersion || item.ten || ''
  const ma = item.maVersion || item.ma || ''

  if (ten) return ten
  if (ma) return ma

  return 'Version chưa đặt tên'
}

function coGiaTri(value) {
  return value !== null && value !== undefined && value !== ''
}
</script>

<style scoped>
.dao-tao-tong-quan-page {
  min-height: calc(100vh - var(--header-height, 60px));
  padding: 12px 16px 20px;
  background: #f1f7ff;
  color: #0f172a;
  font-family: 'Roboto', Arial, Helvetica, sans-serif;
}

/* =========================
   HEADER / FILTER / STATE
========================= */

.page-header,
.filter-card,
.state-card,
.empty-card,
.industry-card {
  border: 1px solid #bfdbfe;
  border-radius: 18px;
  background: #ffffff;
  box-shadow: 0 8px 24px rgba(15, 82, 143, 0.10);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 16px;
  padding: 18px;
  margin-bottom: 12px;
  background: linear-gradient(135deg, #ffffff 0%, #eaf4ff 100%);
}

.eyebrow {
  margin: 0 0 4px;
  color: #0b5a92;
  font-size: 12px;
  font-weight: 900;
  text-transform: uppercase;
  letter-spacing: 0.04em;
}

.page-header h1 {
  margin: 0;
  color: #0f172a;
  font-size: 22px;
  line-height: 1.25;
  font-weight: 700;
}

.subtitle {
  margin: 6px 0 0;
  color: #42637f;
  font-size: 13px;
  line-height: 1.45;
}

.header-actions {
  display: flex;
  gap: 8px;
  flex-shrink: 0;
}

.btn {
  min-height: 32px;
  border: 1px solid #9fc5e8;
  border-radius: 12px;
  background: #ffffff;
  color: #0f3d64;
  padding: 7px 12px;
  font-size: 12px;
  font-weight: 800;
  cursor: pointer;
  white-space: nowrap;
  transition: background 0.15s ease, border-color 0.15s ease, color 0.15s ease, box-shadow 0.15s ease;
}

.btn:hover {
  border-color: #0b5a92;
  background: #eaf4ff;
  color: #0b5a92;
  box-shadow: 0 3px 10px rgba(15, 82, 143, 0.12);
}

.btn:disabled {
  cursor: not-allowed;
  opacity: 0.65;
}

.btn.primary {
  border-color: #0b5a92;
  background: #0b5a92;
  color: #ffffff;
}

.btn.primary:hover {
  border-color: #08456f;
  background: #08456f;
  color: #ffffff;
}

.filter-card {
  padding: 12px;
  margin-bottom: 14px;
  background: #ffffff;
}

.filter-grid {
  display: grid;
  grid-template-columns: 260px minmax(240px, 1fr) 200px;
  gap: 10px;
}

.filter-grid label {
  display: grid;
  gap: 5px;
}

.filter-grid span {
  color: #0f3d64;
  font-size: 12px;
  font-weight: 800;
}

.filter-grid input,
.filter-grid select {
  min-height: 34px;
  border: 1px solid #b7d3ec;
  border-radius: 12px;
  background: #ffffff;
  color: #0f172a;
  padding: 7px 10px;
  font-size: 13px;
  outline: none;
  transition: border-color 0.15s ease, box-shadow 0.15s ease;
  font-family: 'Roboto', Arial, Helvetica, sans-serif;
}

.filter-grid input:focus,
.filter-grid select:focus {
  border-color: #0b5a92;
  box-shadow: 0 0 0 3px rgba(11, 90, 146, 0.14);
}

.notice-card {
  margin-bottom: 12px;
  padding: 10px 12px;
  border: 1px solid #fde68a;
  border-radius: 14px;
  background: #fffbeb;
  color: #92400e;
  font-size: 13px;
}

.state-card {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px;
}

.state-card h3 {
  margin: 0 0 4px;
  color: #0f172a;
  font-size: 16px;
}

.state-card p {
  margin: 0;
  color: #42637f;
  font-size: 13px;
  line-height: 1.45;
}

.state-card.error {
  display: block;
  margin-bottom: 12px;
  border-color: #fecaca;
  background: #fef2f2;
}

.state-card.error p {
  margin-bottom: 10px;
  color: #991b1b;
}

.loading-card {
  min-height: 90px;
}

.empty-card {
  padding: 18px;
  color: #42637f;
  font-size: 13px;
  text-align: center;
}

.spinner {
  width: 26px;
  height: 26px;
  border: 3px solid #dbeafe;
  border-top-color: #0b5a92;
  border-radius: 999px;
  animation: spin 0.8s linear infinite;
  flex-shrink: 0;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

/* =========================
   SECTION TITLE
========================= */

.overview-section {
  display: grid;
  gap: 14px;
}

.section-title {
  display: flex;
  justify-content: space-between;
  gap: 12px;
  align-items: flex-start;
}

.section-title h2 {
  margin: 0;
  color: #0f172a;
  font-size: 17px;
  font-weight: 700;
}

.section-title p {
  margin: 4px 0 0;
  color: #42637f;
  font-size: 12px;
}

.badge,
.program-count,
.version-count,
.current-badge {
  display: inline-flex;
  align-items: center;
  min-height: 23px;
  border-radius: 999px;
  padding: 3px 9px;
  font-size: 11px;
  font-weight: 900;
  white-space: nowrap;
}

.badge.neutral,
.version-count,
.current-badge.neutral {
  border: 1px solid #b7d3ec;
  background: #f8fbff;
  color: #0f3d64;
}

/* =========================
   INDUSTRY GRID: 2 / 1
========================= */

.industry-list {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 16px;
  align-items: start;
}

.industry-card {
  min-width: 0;
  overflow: visible;
  border-color: #9fc5e8;
  background: #ffffff;
  box-shadow: 0 10px 28px rgba(15, 82, 143, 0.13);
  transition: transform 0.15s ease, box-shadow 0.15s ease, border-color 0.15s ease;
}

.industry-card:hover {
  transform: translateY(-2px);
  border-color: #0b5a92;
  box-shadow: 0 16px 34px rgba(15, 82, 143, 0.18);
}

.industry-header {
  display: grid;
  grid-template-columns: minmax(0, 1fr);
  gap: 8px;
  padding: 16px;
  border-bottom: 1px solid #9fc5e8;
  border-radius: 18px 18px 0 0;
  background: linear-gradient(135deg, #0b5a92 0%, #106ba8 55%, #2d93d1 100%);
}

.industry-label {
  margin: 0 0 4px;
  color: #d8ecff;
  font-size: 11px;
  font-weight: 900;
  text-transform: uppercase;
  letter-spacing: 0.04em;
}

.industry-header h3 {
  margin: 0;
  color: #ffffff;
  font-size: 17px;
  line-height: 1.35;
  font-weight: 700;
  word-break: break-word;
}

.program-count {
  width: fit-content;
  border: 1px solid #bfdbfe;
  background: #ffffff;
  color: #0b5a92;
}

/* =========================
   PROGRAM ACCORDION
========================= */

.program-list {
  display: grid;
  gap: 8px;
  padding: 12px;
}

.program-item {
  border: 1px solid #d8ecff;
  border-radius: 14px;
  background: #ffffff;
  overflow: visible;
}

.program-toggle {
  display: grid;
  grid-template-columns: 24px minmax(0, 1fr);
  gap: 8px;
  align-items: center;
  width: 100%;
  border: 0;
  background: #ffffff;
  color: #0f172a;
  padding: 12px;
  text-align: left;
  cursor: pointer;
  border-radius: 14px;
  transition: background 0.15s ease;
  font-family: 'Roboto', Arial, Helvetica, sans-serif;
}

.program-toggle:hover {
  background: #eaf4ff;
}

.toggle-icon {
  color: #0b5a92;
  font-size: 16px;
  font-weight: 900;
}

.program-name {
  color: #0f172a;
  font-size: 13px;
  font-weight: 800;
  line-height: 1.35;
  word-break: break-word;
}

.version-count {
  grid-column: 2 / 3;
  width: fit-content;
  background: #f8fbff;
  color: #0f3d64;
}

/* =========================
   VERSION BOX / ROW
========================= */

.version-box {
  margin: 0 10px 12px 36px;
  border: 1px solid #bfdbfe;
  border-radius: 14px;
  background: #ffffff;
  overflow: visible;
}

.version-box-title {
  padding: 10px 12px;
  border-bottom: 1px solid #bfdbfe;
  border-radius: 14px 14px 0 0;
  background: #eaf4ff;
  color: #0b5a92;
  font-size: 12px;
  font-weight: 900;
  text-transform: uppercase;
}

.version-empty {
  padding: 12px;
  color: #42637f;
  font-size: 13px;
}

.version-row {
  position: relative;
  display: grid;
  grid-template-columns: minmax(0, 1fr) 36px;
  gap: 8px;
  align-items: center;
  min-height: 58px;
  padding: 10px;
  border-top: 1px solid #e5effa;
}

.version-row:first-of-type {
  border-top: 0;
}

.version-row:hover {
  background: #f8fbff;
}

.version-info {
  display: grid;
  gap: 5px;
  min-width: 0;
}

.version-info strong {
  color: #0f172a;
  font-size: 13px;
  line-height: 1.35;
  font-weight: 700;
  word-break: break-word;
}

.current-badge.success {
  width: fit-content;
  border: 1px solid #86efac;
  background: #dcfce7;
  color: #166534;
}

.current-badge.muted {
  width: fit-content;
  border: 1px solid #d1d5db;
  background: #f8fafc;
  color: #64748b;
}

.version-actions {
  position: relative;
  justify-self: end;
}

.icon-btn {
  width: 30px;
  height: 30px;
  border: 1px solid #b7d3ec;
  border-radius: 10px;
  background: #ffffff;
  color: #0f3d64;
  font-size: 18px;
  font-weight: 900;
  cursor: pointer;
  line-height: 1;
  transition: background 0.15s ease, border-color 0.15s ease, color 0.15s ease;
  font-family: 'Roboto', Arial, Helvetica, sans-serif;
}

.icon-btn:hover {
  border-color: #0b5a92;
  background: #eaf4ff;
  color: #0b5a92;
}

/* =========================
   VERSION MENU
========================= */

.menu-popover {
  position: absolute;
  top: 34px;
  right: 0;
  z-index: 30;
  display: grid;
  min-width: 190px;
  border: 1px solid #b7d3ec;
  border-radius: 14px;
  background: #ffffff;
  box-shadow: 0 14px 32px rgba(15, 82, 143, 0.20);
  overflow: hidden;
}

.menu-popover button {
  border: 0;
  background: #ffffff;
  color: #0f172a;
  padding: 10px 12px;
  text-align: left;
  font-size: 12px;
  font-weight: 700;
  cursor: pointer;
  font-family: 'Roboto', Arial, Helvetica, sans-serif;
}

.menu-popover button:hover {
  background: #eaf4ff;
  color: #0b5a92;
}

/* =========================
   RESPONSIVE
========================= */

@media (max-width: 1180px) {
  .industry-list {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}

@media (max-width: 860px) {
  .filter-grid {
    grid-template-columns: 1fr;
  }

  .page-header,
  .section-title {
    flex-direction: column;
  }

  .header-actions {
    width: 100%;
  }

  .version-box {
    margin-left: 12px;
  }
}

@media (max-width: 640px) {
  .dao-tao-tong-quan-page {
    padding: 10px;
  }

  .industry-list {
    grid-template-columns: 1fr;
  }

  .page-header h1 {
    font-size: 19px;
  }

  .program-toggle {
    grid-template-columns: 22px minmax(0, 1fr);
  }

  .version-count {
    grid-column: 2 / 3;
    width: fit-content;
  }

  .version-box {
    margin-left: 10px;
    margin-right: 10px;
  }

  .version-row {
    grid-template-columns: minmax(0, 1fr) 34px;
  }

  .menu-popover {
    right: 0;
    min-width: 180px;
  }
}

.program-empty {
  border: 1px dashed #9fc5e8;
  border-radius: 14px;
  background: #f8fbff;
  color: #42637f;
  padding: 12px;
  font-size: 13px;
  font-weight: 700;
  text-align: center;
}
</style>