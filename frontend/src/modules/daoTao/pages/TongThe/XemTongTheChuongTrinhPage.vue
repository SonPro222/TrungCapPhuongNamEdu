<template>
  <div class="dao-tao-tong-quan-page">
    <section class="page-header">
      <div class="page-title">
        <p class="eyebrow">Chương trình đào tạo</p>
        <h1>Tổng quan chương trình đào tạo</h1>
        <p class="subtitle">
          Xem nhanh Ngành → Chương trình đào tạo → Version. Bấm vào chương trình đào tạo để mở danh sách version.
        </p>
      </div>

      <div class="header-actions">
        <button type="button" class="btn" :disabled="loading" @click.stop="taiDuLieuTongQuan">
          Tải lại
        </button>
      </div>
    </section>

    <section v-if="errorMessage" class="state-card error">
      <div>
        <h3>Không tải được dữ liệu</h3>
        <p>{{ errorMessage }}</p>
      </div>

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

        <label class="search-field">
          <span>Tìm kiếm</span>
          <input
              v-model.trim="boLoc.keyword"
              type="text"
              placeholder="Tìm ngành, chương trình hoặc version..."
          />
        </label>

        <label v-if="laAdmin">
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
          <p>
            Mỗi ngành là một nhóm nhỏ. Chương trình đào tạo và version được hiển thị gọn để dễ theo dõi.
          </p>
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
            <div class="industry-title">
              <p class="industry-label">Ngành</p>
              <h3>{{ nganhGroup.tenNganh }}</h3>
            </div>

            <span class="program-count">
              {{ nganhGroup.chuongTrinhList.length }} CTĐT
            </span>
          </header>

          <div class="program-list">
            <div v-if="!nganhGroup.chuongTrinhList.length" class="program-empty">
              Chưa có chương trình đào tạo.
            </div>

            <section
                v-for="chuongTrinh in nganhGroup.chuongTrinhList"
                v-else
                :key="chuongTrinh.rowKey"
                class="program-item"
                :class="{ active: dangMoChuongTrinh(chuongTrinh.rowKey) }"
            >
              <button
                  type="button"
                  class="program-toggle"
                  @click="toggleChuongTrinh(chuongTrinh.rowKey)"
              >
                <span class="program-main">
                  <span class="program-name">
                    {{ chuongTrinh.tenChuongTrinh }}
                  </span>

                  <span class="program-meta">
                    {{ chuongTrinh.versionList.length }} version
                  </span>
                </span>

                <span class="toggle-icon">
                  {{ dangMoChuongTrinh(chuongTrinh.rowKey) ? '−' : '+' }}
                </span>
              </button>

              <div v-if="dangMoChuongTrinh(chuongTrinh.rowKey)" class="version-box">
                <div v-if="!chuongTrinh.versionList.length" class="version-empty">
                  Không có version phù hợp quyền xem hiện tại.
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
                      {{ version.hienHanhText }}
                    </span>
                  </div>

                  <div class="version-actions">
                    <button
                        type="button"
                        class="action-btn soft"
                        @click="diDenTrangKy(chuongTrinh, version)"
                    >
                      Xem kỳ
                    </button>

                    <button
                        type="button"
                        class="action-btn primary"
                        @click="diDenSyllabusChuongTrinh(nganhGroup, chuongTrinh, version)"
                    >
                      Xem syllabus
                    </button>
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
import { authService } from '@/core/services/authService.js'
import { ROLES } from '@/core/constants/roles.js'

const router = useRouter()

const TEN_ROUTE_TRANG_KY = 'dao-tao-chuong-trinh-ky'
const TEN_ROUTE_SYLLABUS_CHUONG_TRINH = 'dao-tao-syllabus-chuong-trinh-xem'

const loading = ref(false)
const errorMessage = ref('')
const thongBao = ref('')

const danhSachNganh = ref([])
const danhSachChuongTrinh = ref([])
const danhSachVersion = ref([])

const chuongTrinhDangMo = ref(null)

const laAdmin = computed(() => {
  return authService.hasAnyRole([ROLES.ADMIN, ROLES.DAO_TAO])
})

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
                /*
                  Phân quyền xem version:
                  - Admin / Đào tạo: xem được toàn bộ version, có thể lọc Có / Không / Không rõ.
                  - Vai trò khác: chỉ xem được version đang hiện hành Có.
                */
                if (!laAdmin.value && version.hienHanhText !== 'Có') return false

                if (laAdmin.value && boLoc.hienHanh === 'co' && version.hienHanhText !== 'Có') return false
                if (laAdmin.value && boLoc.hienHanh === 'khong' && version.hienHanhText !== 'Không') return false
                if (laAdmin.value && boLoc.hienHanh === 'khong-ro' && version.hienHanhText !== '—') return false

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
                versionList
              }
            })
            .filter(Boolean)

        /*
          Vẫn hiển thị ngành mới tạo dù chưa có chương trình đào tạo.
          Chỉ ẩn ngành rỗng khi đang lọc theo trạng thái hiện hành,
          vì ngành rỗng không có version để lọc Có / Không / Không rõ.
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
  chuongTrinhDangMo.value = null

  try {
    const [nganhRes, chuongTrinhRes, versionRes] = await Promise.all([
      daoTaoService.nganh.getAll({ size: 1000 }),
      daoTaoService.chuongTrinh.getAll({ size: 1000 }),
      daoTaoService.chuongTrinhVersion.getAll({ size: 1000 })
    ])

    danhSachNganh.value = layDanhSachTuResponse(nganhRes)
    danhSachChuongTrinh.value = layDanhSachTuResponse(chuongTrinhRes)
    danhSachVersion.value = layDanhSachTuResponse(versionRes)
  } catch (error) {
    errorMessage.value = layThongBaoLoi(error, 'Không tải được tổng quan chương trình đào tạo.')
  } finally {
    loading.value = false
  }
}

function toggleChuongTrinh(rowKey) {
  chuongTrinhDangMo.value = chuongTrinhDangMo.value === rowKey ? null : rowKey
}

function dangMoChuongTrinh(rowKey) {
  return chuongTrinhDangMo.value === rowKey
}

function diDenTrangKy(chuongTrinh, version) {
  if (!chuongTrinh?.chuongTrinhId) {
    thongBao.value = 'Không xác định được chương trình đào tạo để xem kỳ.'
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
    return
  }

  if (!chuongTrinhId) {
    thongBao.value = 'Không xác định được chương trình đào tạo để xem syllabus chương trình.'
    return
  }

  if (!versionId) {
    thongBao.value = 'Không xác định được version chương trình để xem syllabus chương trình.'
    return
  }

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
  --primary: #007a4d;
  --primary-dark: #005f3b;
  --primary-deep: #00452c;
  --primary-light: #12a66a;
  --primary-soft: #e8f7ef;
  --primary-soft-2: #f4fbf7;
  --primary-border: #93d9b8;
  --primary-border-soft: #ccebdd;

  --surface: #ffffff;
  --surface-soft: #fbfffd;
  --page-bg: #eef8f3;

  --text-main: #102019;
  --text-soft: #263f34;
  --text-muted: #667a70;

  --danger-bg: #fef2f2;
  --danger-border: #fecaca;
  --danger-text: #991b1b;

  --warning-bg: #fff8e6;
  --warning-border: #f3d58a;
  --warning-text: #7c5608;

  min-height: calc(100vh - var(--header-height, 60px));
  padding: 14px;
  background:
      radial-gradient(circle at top left, rgba(0, 122, 77, 0.16), transparent 26%),
      radial-gradient(circle at bottom right, rgba(18, 166, 106, 0.09), transparent 30%),
      linear-gradient(180deg, #f3fbf7 0%, #eaf6f0 100%);
  color: var(--text-main);
  font-family: 'Roboto', Arial, Helvetica, sans-serif;
}

/* =========================
   COMMON
========================= */

.page-header,
.filter-card,
.state-card,
.empty-card,
.industry-card {
  border: 1px solid var(--primary-border-soft);
  border-radius: 16px;
  background: var(--surface);
  box-shadow: 0 10px 26px rgba(0, 95, 59, 0.09);
}

button,
input,
select {
  font-family: 'Roboto', Arial, Helvetica, sans-serif;
}

/* =========================
   HEADER
========================= */

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 14px;
  padding: 16px 18px;
  margin-bottom: 12px;
  border-color: rgba(0, 122, 77, 0.24);
  background:
      linear-gradient(135deg, rgba(255, 255, 255, 0.96) 0%, rgba(232, 247, 239, 0.96) 100%);
  position: relative;
  overflow: hidden;
}

.page-header::before {
  content: '';
  position: absolute;
  inset: 0 auto 0 0;
  width: 6px;
  background: linear-gradient(180deg, var(--primary) 0%, var(--primary-light) 100%);
}

.page-title {
  min-width: 0;
  padding-left: 4px;
}

.eyebrow {
  margin: 0 0 4px;
  color: var(--primary);
  font-size: 11px;
  font-weight: 900;
  text-transform: uppercase;
  letter-spacing: 0.08em;
}

.page-header h1 {
  margin: 0;
  color: var(--text-main);
  font-size: 21px;
  line-height: 1.25;
  font-weight: 900;
}

.subtitle {
  max-width: 780px;
  margin: 5px 0 0;
  color: var(--text-muted);
  font-size: 12px;
  line-height: 1.45;
}

.header-actions {
  display: flex;
  gap: 8px;
  flex-shrink: 0;
}

.btn {
  min-height: 34px;
  border: 1px solid var(--primary);
  border-radius: 12px;
  background: #ffffff;
  color: var(--primary-dark);
  padding: 8px 14px;
  font-size: 12px;
  font-weight: 900;
  cursor: pointer;
  white-space: nowrap;
  transition:
      background 0.16s ease,
      border-color 0.16s ease,
      color 0.16s ease,
      box-shadow 0.16s ease,
      transform 0.16s ease;
}

.btn:hover {
  background: var(--primary);
  color: #ffffff;
  box-shadow: 0 8px 18px rgba(0, 122, 77, 0.2);
  transform: translateY(-1px);
}

.btn:disabled {
  cursor: not-allowed;
  opacity: 0.65;
  transform: none;
  box-shadow: none;
}

.btn.primary {
  border-color: var(--primary);
  background: var(--primary);
  color: #ffffff;
}

.btn.primary:hover {
  border-color: var(--primary-dark);
  background: var(--primary-dark);
}

/* =========================
   FILTER
========================= */

.filter-card {
  padding: 12px;
  margin-bottom: 14px;
  border-color: rgba(0, 122, 77, 0.18);
  background:
      linear-gradient(180deg, rgba(255, 255, 255, 0.98), rgba(244, 251, 247, 0.98));
}

.filter-grid {
  display: grid;
  grid-template-columns: 240px minmax(260px, 1fr) 170px;
  gap: 10px;
  align-items: end;
}

.filter-grid label {
  display: grid;
  gap: 5px;
}

.filter-grid span {
  color: var(--primary-dark);
  font-size: 11px;
  font-weight: 900;
}

.filter-grid input,
.filter-grid select {
  width: 100%;
  min-height: 36px;
  border: 1px solid #bfe4d4;
  border-radius: 11px;
  background: #ffffff;
  color: var(--text-main);
  padding: 8px 11px;
  font-size: 12px;
  outline: none;
  transition:
      border-color 0.16s ease,
      box-shadow 0.16s ease,
      background 0.16s ease;
}

.filter-grid input::placeholder {
  color: #8aa098;
}

.filter-grid input:focus,
.filter-grid select:focus {
  border-color: var(--primary);
  box-shadow: 0 0 0 4px rgba(0, 122, 77, 0.13);
}

/* =========================
   STATE / NOTICE
========================= */

.notice-card {
  margin-bottom: 12px;
  padding: 10px 13px;
  border: 1px solid var(--warning-border);
  border-radius: 13px;
  background: var(--warning-bg);
  color: var(--warning-text);
  font-size: 12px;
  font-weight: 800;
}

.state-card {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 14px;
}

.state-card h3 {
  margin: 0 0 4px;
  color: var(--text-main);
  font-size: 15px;
  font-weight: 900;
}

.state-card p {
  margin: 0;
  color: var(--text-muted);
  font-size: 12px;
  line-height: 1.45;
}

.state-card.error {
  display: flex;
  justify-content: space-between;
  margin-bottom: 12px;
  border-color: var(--danger-border);
  background: var(--danger-bg);
}

.state-card.error p {
  color: var(--danger-text);
}

.loading-card {
  min-height: 88px;
}

.empty-card {
  padding: 18px;
  color: var(--text-muted);
  font-size: 13px;
  font-weight: 800;
  text-align: center;
}

.spinner {
  width: 26px;
  height: 26px;
  border: 3px solid #d5f0e4;
  border-top-color: var(--primary);
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
  gap: 11px;
}

.section-title {
  display: flex;
  justify-content: space-between;
  gap: 12px;
  align-items: flex-start;
  padding: 0 2px;
}

.section-title h2 {
  margin: 0;
  color: var(--text-main);
  font-size: 16px;
  font-weight: 900;
}

.section-title p {
  margin: 3px 0 0;
  color: var(--text-muted);
  font-size: 12px;
  line-height: 1.4;
}

.badge,
.program-count,
.current-badge {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  border-radius: 999px;
  font-weight: 900;
  white-space: nowrap;
}

.badge.neutral {
  min-height: 26px;
  border: 1px solid var(--primary-border);
  background: var(--primary);
  color: #ffffff;
  padding: 5px 11px;
  font-size: 11px;
  box-shadow: 0 7px 18px rgba(0, 122, 77, 0.16);
}

/* =========================
   INDUSTRY GRID
========================= */

.industry-list {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 12px;
  align-items: start;
}

.industry-card {
  min-width: 0;
  overflow: hidden;
  border-color: rgba(0, 122, 77, 0.22);
  background: #ffffff;
  transition:
      border-color 0.16s ease,
      box-shadow 0.16s ease,
      transform 0.16s ease;
}

.industry-card:hover {
  transform: translateY(-2px);
  border-color: var(--primary);
  box-shadow: 0 16px 32px rgba(0, 95, 59, 0.15);
}

.industry-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 10px;
  padding: 12px 13px;
  background:
      linear-gradient(135deg, var(--primary-deep) 0%, var(--primary-dark) 46%, var(--primary) 100%);
  position: relative;
}

.industry-header::after {
  content: '';
  position: absolute;
  right: -32px;
  top: -36px;
  width: 96px;
  height: 96px;
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.09);
}

.industry-title {
  min-width: 0;
  position: relative;
  z-index: 1;
}

.industry-label {
  margin: 0 0 3px;
  color: rgba(255, 255, 255, 0.78);
  font-size: 10px;
  font-weight: 900;
  text-transform: uppercase;
  letter-spacing: 0.08em;
}

.industry-header h3 {
  margin: 0;
  color: #ffffff;
  font-size: 15px;
  line-height: 1.28;
  font-weight: 900;
  word-break: break-word;
}

.program-count {
  position: relative;
  z-index: 1;
  flex-shrink: 0;
  min-height: 25px;
  border: 1px solid rgba(255, 255, 255, 0.78);
  background: rgba(255, 255, 255, 0.96);
  color: var(--primary-dark);
  padding: 5px 9px;
  font-size: 10px;
  box-shadow: 0 7px 16px rgba(0, 69, 44, 0.18);
}

/* =========================
   PROGRAM LIST
========================= */

.program-list {
  display: grid;
  gap: 8px;
  padding: 9px;
  background:
      linear-gradient(180deg, #ffffff 0%, #f5fbf8 100%);
}

.program-item {
  border: 1px solid #d7eee3;
  border-radius: 13px;
  background: #ffffff;
  overflow: hidden;
  transition:
      border-color 0.16s ease,
      box-shadow 0.16s ease,
      background 0.16s ease;
}

.program-item:hover {
  border-color: var(--primary-border);
  box-shadow: 0 8px 18px rgba(0, 122, 77, 0.09);
}

.program-item.active {
  border-color: var(--primary);
  box-shadow: 0 10px 22px rgba(0, 122, 77, 0.13);
}

.program-toggle {
  width: 100%;
  border: 0;
  background: #ffffff;
  padding: 10px 10px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 9px;
  text-align: left;
  cursor: pointer;
  transition: background 0.16s ease;
}

.program-toggle:hover {
  background: var(--primary-soft-2);
}

.program-main {
  min-width: 0;
  flex: 1;
  display: grid;
  grid-template-columns: minmax(0, 1fr) auto;
  align-items: center;
  gap: 8px;
}

.program-name {
  color: var(--text-main);
  font-size: 12px;
  font-weight: 900;
  line-height: 1.38;
  word-break: break-word;
}

.program-meta {
  flex-shrink: 0;
  border: 1px solid #bde6d3;
  border-radius: 999px;
  background: var(--primary-soft);
  color: var(--primary-dark);
  padding: 4px 8px;
  font-size: 10px;
  font-weight: 900;
  white-space: nowrap;
}

.toggle-icon {
  flex-shrink: 0;
  width: 24px;
  height: 24px;
  border: 1px solid var(--primary-border);
  border-radius: 999px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  color: var(--primary-dark);
  background: #ffffff;
  font-size: 16px;
  font-weight: 900;
  line-height: 1;
  transition:
      background 0.16s ease,
      color 0.16s ease,
      border-color 0.16s ease;
}

.program-item.active .toggle-icon {
  border-color: var(--primary);
  background: var(--primary);
  color: #ffffff;
}

/* =========================
   VERSION
========================= */

.version-box {
  display: grid;
  gap: 7px;
  padding: 9px;
  border-top: 1px solid #d7eee3;
  background: #f3faf6;
}

.version-row {
  display: grid;
  grid-template-columns: minmax(0, 1fr) auto;
  align-items: center;
  gap: 9px;
  padding: 9px 10px;
  border: 1px solid #d9eee4;
  border-radius: 12px;
  background: #ffffff;
}

.version-info {
  display: flex;
  align-items: center;
  gap: 7px;
  min-width: 0;
}

.version-info strong {
  min-width: 0;
  color: var(--text-main);
  font-size: 12px;
  line-height: 1.35;
  font-weight: 900;
  word-break: break-word;
}

.current-badge {
  flex-shrink: 0;
  min-height: 22px;
  padding: 3px 8px;
  font-size: 10px;
}

.current-badge.success {
  border: 1px solid #76d09f;
  background: #dcf8e9;
  color: #075f3d;
}

.current-badge.muted {
  border: 1px solid #d1d5db;
  background: #f8fafc;
  color: #64748b;
}

.current-badge.neutral {
  border: 1px solid var(--primary-border);
  background: var(--primary-soft);
  color: var(--primary-dark);
}

.version-actions {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 6px;
  flex-shrink: 0;
}

.action-btn {
  min-height: 28px;
  border-radius: 9px;
  padding: 6px 10px;
  font-size: 11px;
  font-weight: 900;
  cursor: pointer;
  line-height: 1.15;
  transition:
      background 0.16s ease,
      border-color 0.16s ease,
      color 0.16s ease,
      box-shadow 0.16s ease,
      transform 0.16s ease;
  white-space: nowrap;
}

.action-btn:hover {
  transform: translateY(-1px);
}

.action-btn.soft {
  border: 1px solid var(--primary-border);
  background: #ffffff;
  color: var(--primary-dark);
}

.action-btn.soft:hover {
  border-color: var(--primary);
  background: var(--primary-soft);
}

.action-btn.primary {
  border: 1px solid var(--primary);
  background: var(--primary);
  color: #ffffff;
}

.action-btn.primary:hover {
  border-color: var(--primary-dark);
  background: var(--primary-dark);
  box-shadow: 0 6px 16px rgba(0, 122, 77, 0.2);
}

/* =========================
   EMPTY
========================= */

.program-empty,
.version-empty {
  border: 1px dashed var(--primary-border);
  border-radius: 12px;
  background:
      repeating-linear-gradient(
          -45deg,
          rgba(232, 247, 239, 0.8),
          rgba(232, 247, 239, 0.8) 8px,
          rgba(244, 251, 247, 0.8) 8px,
          rgba(244, 251, 247, 0.8) 16px
      );
  color: var(--primary-dark);
  padding: 12px;
  font-size: 12px;
  font-weight: 900;
  text-align: center;
}

/* =========================
   RESPONSIVE
========================= */

@media (max-width: 1320px) {
  .industry-list {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }

  .version-row {
    grid-template-columns: 1fr;
  }

  .version-actions {
    justify-content: flex-start;
    flex-wrap: wrap;
  }
}

@media (max-width: 1040px) {
  .filter-grid {
    grid-template-columns: 1fr 1fr;
  }

  .search-field {
    grid-column: 1 / -1;
  }
}

@media (max-width: 760px) {
  .dao-tao-tong-quan-page {
    padding: 10px;
  }

  .page-header,
  .section-title,
  .state-card {
    flex-direction: column;
    align-items: stretch;
  }

  .header-actions {
    width: 100%;
  }

  .btn {
    width: 100%;
  }

  .filter-grid,
  .industry-list {
    grid-template-columns: 1fr;
  }

  .search-field {
    grid-column: auto;
  }

  .program-main {
    grid-template-columns: 1fr;
    gap: 5px;
  }

  .program-meta {
    width: fit-content;
  }

  .version-info {
    align-items: flex-start;
    flex-wrap: wrap;
  }

  .version-actions {
    justify-content: flex-start;
    flex-wrap: wrap;
  }

  .page-header h1 {
    font-size: 20px;
  }

  .industry-header h3 {
    font-size: 14px;
  }
}
</style>