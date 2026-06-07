<template>
  <div class="xem-tep-syllabus-page">
    <div class="page-head">
      <div class="head-actions">
        <button type="button" class="btn" @click="quayLaiSyllabus">
          ← Quay lại
        </button>

        <button
            v-if="dangMoToanManHinh"
            type="button"
            class="btn"
            @click="quayLaiDanhSachTep"
        >
          ← Danh sách tệp
        </button>

        <button type="button" class="btn primary" :disabled="loading" @click="taiDuLieu">
          Tải lại
        </button>
      </div>
    </div>

    <div v-if="thongBao" :class="['notice', loaiThongBao]">
      {{ thongBao }}
    </div>



    <section v-if="!dangMoToanManHinh" class="table-card">
      <div class="table-title">
        <div>
          <h3>{{ tieuDeBangTep }}</h3>
          <p>Danh sách tệp đã lưu. Bấm “Xem” để mở trang đọc toàn màn hình.</p>
        </div>
      </div>

      <div class="table-wrap">
        <table>
          <thead>
          <tr>
            <th class="col-stt">STT</th>
            <th>Tên tệp</th>
            <th>Mã tài liệu</th>
            <th>Tên tài liệu</th>
            <th>Loại</th>
            <th>Tác giả</th>
            <th>Năm</th>
            <th>Dung lượng</th>
            <th class="col-action">Thao tác</th>
          </tr>
          </thead>

          <tbody>
          <tr v-if="loading">
            <td colspan="9" class="empty-cell">Đang tải dữ liệu...</td>
          </tr>

          <tr v-else-if="!danhSachTep.length">
            <td colspan="9" class="empty-cell">
              {{ thongBaoRong }}
            </td>
          </tr>

          <tr
              v-for="(item, index) in danhSachTep"
              v-else
              :key="`${item.__tepNguon || 'syllabusTaiLieu'}-${item.id}-${item.__fileIndex || 0}`"
          >
            <td class="col-stt">{{ index + 1 }}</td>
            <td>
              <div class="file-name">
                <b>{{ item.__tenTep || item.ten || '-' }}</b>
                <small>{{ layNhanLoaiFile(item) }}</small>
              </div>
            </td>
            <td>{{ item.ma || '-' }}</td>
            <td>{{ item.ten || '-' }}</td>
            <td>{{ item.loai || '-' }}</td>
            <td>{{ item.tacGia || '-' }}</td>
            <td>{{ item.namXuatBan || '-' }}</td>
            <td>{{ formatDungLuong(item.__dungLuong) }}</td>
            <td class="col-action">
              <button
                  type="button"
                  class="btn tiny primary"
                  :disabled="!item.duongDan || loadingTep"
                  @click="moTrangXemTep(index)"
              >
                Xem
              </button>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </section>

    <section v-else class="viewer-full-page">
      <div class="viewer-toolbar">
        <div>
          <h3>{{ tepDangXem?.__tenTep || tepDangXem?.ten || 'Tệp đang xem' }}</h3>
          <p>
            {{ tepDangXem?.ma || '-' }}
            <span>|</span>
            {{ tepDangXem?.loai || layNhanLoaiFile(tepDangXem) }}
            <span>|</span>
            {{ formatDungLuong(tepDangXem?.__dungLuong) }}
          </p>
        </div>

        <div class="viewer-actions">
          <button
              type="button"
              class="btn"
              :disabled="loadingTep"
              @click="moTepBangTrinhDuyet"
          >
            Mở tab riêng
          </button>

          <button
              type="button"
              class="btn danger"
              :disabled="loadingTep || dangXoaTep"
              @click="xoaTepDangXem"
          >
            Xóa tệp đã lưu
          </button>
        </div>
      </div>

      <div v-if="loadingTep" class="preview-loading">
        Đang tải tệp...
      </div>

      <div v-else-if="previewError" class="preview-error">
        {{ previewError }}
      </div>

      <div v-else class="preview-body">
        <img
            v-if="loaiPreview === 'image'"
            :src="previewUrl"
            class="preview-image"
            alt="Tệp hình ảnh"
        />

        <video
            v-else-if="loaiPreview === 'video'"
            :src="previewUrl"
            class="preview-video"
            controls
        />

        <audio
            v-else-if="loaiPreview === 'audio'"
            :src="previewUrl"
            class="preview-audio"
            controls
        />

        <iframe
            v-else
            :src="previewUrl"
            class="preview-frame"
            title="Xem tệp syllabus"
        />
      </div>
    </section>
  </div>
</template>

<script setup>
import { computed, nextTick, onBeforeUnmount, onMounted, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import apiClient from '@/core/api/apiClient'
import { ENV } from '@/core/config/env'
import { daoTaoXemChuongTrinhService } from '../services/daoTaoXemChuongTrinhService'
import { layThongBaoLoi } from '../utils/layThongBaoLoi'

const route = useRoute()
const router = useRouter()

const loading = ref(false)
const loadingTep = ref(false)
const dangXoaTep = ref(false)
const thongBao = ref('')
const loaiThongBao = ref('success')

const syllabusMonHoc = ref(null)
const chuongTrinhMon = ref(null)
const danhSachTaiLieu = ref([])
const taiLieuGocDangXem = ref(null)
const syllabusChuongTrinhGocDangXem = ref(null)
const syllabusChuongTrinhDangXem = ref(null)
const mucTieuChuongTrinhGocDangXem = ref(null)
const nangLucDauRaGocDangXem = ref(null)
const viTriViecLamGocDangXem = ref(null)
const previewUrl = ref('')
const previewError = ref('')
const loaiPreview = ref('')

const syllabusMonId = computed(() => route.query.syllabusMonId || route.params.syllabusMonId || null)
const chuongTrinhMonId = computed(() => {
  const val = route.params.chuongTrinhMonId || route.query.chuongTrinhMonId || null
  return val && String(val) !== '0' ? val : null
})

const tepNguonCanXem = computed(() => {
  const value = String(route.query.tepNguon || '')

  if (value === 'taiLieuGoc') return 'taiLieuGoc'
  if (value === 'syllabusChuongTrinhGoc') return 'syllabusChuongTrinhGoc'
  if (value === 'syllabusChuongTrinh') return 'syllabusChuongTrinh'
  if (value === 'mucTieuChuongTrinhGoc') return 'mucTieuChuongTrinhGoc'
  if (value === 'nangLucDauRaGoc') return 'nangLucDauRaGoc'
  if (value === 'viTriViecLamGoc') return 'viTriViecLamGoc'

  return 'syllabusTaiLieu'
})

const taiLieuGocIdCanXem = computed(() => route.query.taiLieuGocId || null)
const syllabusTaiLieuIdCanXem = computed(() => route.query.syllabusTaiLieuId || null)
const syllabusChuongTrinhGocIdCanXem = computed(() => route.query.syllabusChuongTrinhGocId || null)
const syllabusChuongTrinhIdCanXem = computed(() => route.query.syllabusChuongTrinhId || null)
const mucTieuChuongTrinhGocIdCanXem = computed(() => route.query.mucTieuChuongTrinhGocId || null)
const nangLucDauRaGocIdCanXem = computed(() => route.query.nangLucDauRaGocId || null)
const viTriViecLamGocIdCanXem = computed(() => route.query.viTriViecLamGocId || null)

const fileIndexDangXem = computed(() => {
  const value = Number(route.query.fileIndex)
  return Number.isInteger(value) && value >= 0 ? value : null
})

const duongDanQuayLai = computed(() => {
  const value = route.query.quayLaiPath || route.query.returnPath || route.query.from

  if (!value) return ''

  try {
    return decodeURIComponent(String(value))
  } catch (error) {
    return String(value)
  }
})

const dangMoToanManHinh = computed(() => fileIndexDangXem.value !== null)

const tepDangXem = computed(() => {
  if (fileIndexDangXem.value === null) return null
  return danhSachTep.value[fileIndexDangXem.value] || null
})

const tenMonTrongChuongTrinh = computed(() => {
  const mon = chuongTrinhMon.value
  if (!mon) return chuongTrinhMonId.value || '-'

  return [
    mon.maMonTrongCt,
    mon.tenMon,
    mon.tenMonHoc
  ].filter(Boolean).join(' - ') || mon.id || '-'
})

const tenSyllabusMon = computed(() => {
  const syllabus = syllabusMonHoc.value
  if (!syllabus) return syllabusMonId.value || '-'

  return [
    syllabus.ma,
    syllabus.ten,
    syllabus.tenSyllabusMonHocGoc,
    syllabus.maSyllabusMonHocGoc,
    syllabus.mucTieu
  ].filter(Boolean)[0] || syllabus.id || '-'
})

const tieuDeBangTep = computed(() => {
  if (tepNguonCanXem.value === 'taiLieuGoc') return 'Danh sách tệp tài liệu gốc / mẫu'
  if (tepNguonCanXem.value === 'syllabusChuongTrinhGoc') return 'Danh sách tệp syllabus chương trình gốc / mẫu'
  if (tepNguonCanXem.value === 'syllabusChuongTrinh') return 'Danh sách tệp syllabus chương trình'
  if (tepNguonCanXem.value === 'mucTieuChuongTrinhGoc') return 'Danh sách tệp mục tiêu chương trình gốc / mẫu'
  if (tepNguonCanXem.value === 'nangLucDauRaGoc') return 'Danh sách tệp năng lực đầu ra gốc / mẫu'
  if (tepNguonCanXem.value === 'viTriViecLamGoc') return 'Danh sách tệp vị trí việc làm gốc / mẫu'

  return 'Danh sách tệp tài liệu syllabus'
})

const nhanSoTep = computed(() => {
  if (tepNguonCanXem.value === 'taiLieuGoc') return 'Số tệp tài liệu gốc'
  if (tepNguonCanXem.value === 'syllabusChuongTrinhGoc') return 'Số tệp syllabus chương trình gốc'
  if (tepNguonCanXem.value === 'syllabusChuongTrinh') return 'Số tệp syllabus chương trình'

  return 'Số tệp tài liệu syllabus'
})

const thongBaoRong = computed(() => {
  if (tepNguonCanXem.value === 'taiLieuGoc') return 'Tài liệu gốc / mẫu này chưa có tệp đã lưu.'
  if (tepNguonCanXem.value === 'syllabusChuongTrinhGoc') return 'Syllabus chương trình gốc / mẫu này chưa có tệp đã lưu.'
  if (tepNguonCanXem.value === 'syllabusChuongTrinh') return 'Syllabus chương trình này chưa có tệp đã lưu.'
  if (tepNguonCanXem.value === 'mucTieuChuongTrinhGoc') return 'Mục tiêu chương trình gốc / mẫu này chưa có tệp đã lưu.'
  if (tepNguonCanXem.value === 'nangLucDauRaGoc') return 'Năng lực đầu ra gốc / mẫu này chưa có tệp đã lưu.'
  if (tepNguonCanXem.value === 'viTriViecLamGoc') return 'Vị trí việc làm gốc / mẫu này chưa có tệp đã lưu.'

  return 'Tài liệu syllabus này chưa có tệp đã lưu.'
})

const danhSachTep = computed(() => {
  if (tepNguonCanXem.value === 'taiLieuGoc') {
    if (!taiLieuGocDangXem.value?.duongDan) return []
    return taoDanhSachTepTuDongTaiLieu(taiLieuGocDangXem.value, 'taiLieuGoc')
  }

  if (tepNguonCanXem.value === 'syllabusChuongTrinhGoc') {
    if (!syllabusChuongTrinhGocDangXem.value?.duongDan) return []
    return taoDanhSachTepTuDongTaiLieu(syllabusChuongTrinhGocDangXem.value, 'syllabusChuongTrinhGoc')
  }

  if (tepNguonCanXem.value === 'syllabusChuongTrinh') {
    if (!syllabusChuongTrinhDangXem.value?.duongDan) return []
    return taoDanhSachTepTuDongTaiLieu(syllabusChuongTrinhDangXem.value, 'syllabusChuongTrinh')
  }

  if (tepNguonCanXem.value === 'mucTieuChuongTrinhGoc') {
    if (!mucTieuChuongTrinhGocDangXem.value?.duongDan) return []
    return taoDanhSachTepTuDongTaiLieu(mucTieuChuongTrinhGocDangXem.value, 'mucTieuChuongTrinhGoc')
  }

  if (tepNguonCanXem.value === 'nangLucDauRaGoc') {
    if (!nangLucDauRaGocDangXem.value?.duongDan) return []
    return taoDanhSachTepTuDongTaiLieu(nangLucDauRaGocDangXem.value, 'nangLucDauRaGoc')
  }

  if (tepNguonCanXem.value === 'viTriViecLamGoc') {
    if (!viTriViecLamGocDangXem.value?.duongDan) return []
    return taoDanhSachTepTuDongTaiLieu(viTriViecLamGocDangXem.value, 'viTriViecLamGoc')
  }

  return (danhSachTaiLieu.value || [])
      .filter((item) => {
        return String(item?.id || '') === String(syllabusTaiLieuIdCanXem.value || '')
            && item?.duongDan !== null
            && item?.duongDan !== undefined
            && item?.duongDan !== ''
      })
      .flatMap((item) => taoDanhSachTepTuDongTaiLieu(item, 'syllabusTaiLieu'))
})

function tachDanhSachDuongDanTep(duongDan) {
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
                  duongDan: item,
                  tenGoc: layTenFileTuDuongDan(item),
                  tenHienThi: layTenFileTuDuongDan(item),
                  contentType: '',
                  dungLuong: 0
                }
              }

              return {
                id: item?.id || item?.tepDinhKemId || null,
                duongDan: item?.duongDan || '',
                tenGoc: item?.tenGoc || item?.fileName || layTenFileTuDuongDan(item?.duongDan || ''),
                tenHienThi: item?.tenHienThi || item?.tenGoc || layTenFileTuDuongDan(item?.duongDan || ''),
                contentType: item?.contentType || '',
                dungLuong: item?.dungLuong || 0
              }
            })
            .filter((item) => item.duongDan)
      }
    } catch (error) {
      return [{
        id: null,
        duongDan: text,
        tenGoc: layTenFileTuDuongDan(text),
        tenHienThi: layTenFileTuDuongDan(text),
        contentType: '',
        dungLuong: 0
      }]
    }
  }

  return [{
    id: null,
    duongDan: text,
    tenGoc: layTenFileTuDuongDan(text),
    tenHienThi: layTenFileTuDuongDan(text),
    contentType: '',
    dungLuong: 0
  }]
}

function layTenFileTuDuongDan(duongDan) {
  const text = String(duongDan || '').split('?')[0]
  const parts = text.split('/')
  return decodeURIComponent(parts[parts.length - 1] || 'Tệp đã lưu')
}

function taoDanhSachTepTuDongTaiLieu(item, tepNguon) {
  return tachDanhSachDuongDanTep(item?.duongDan)
      .map((tep, index) => ({
        ...item,
        duongDan: tep.duongDan,
        __tepDinhKemId: tep.id,
        __duongDanGoc: item.duongDan,
        __tepNguon: tepNguon,
        __fileIndex: index,
        __tenTep: tep.tenHienThi || tep.tenGoc || `${item?.ten || 'Tệp'} ${index + 1}`,
        __contentType: tep.contentType || '',
        __dungLuong: tep.dungLuong || 0
      }))
}

function baoTin(message, type = 'success') {
  thongBao.value = message
  loaiThongBao.value = type

  setTimeout(() => {
    if (thongBao.value === message) thongBao.value = ''
  }, 5000)
}

function layMotDong(result) {
  if (!result) return null
  if (result?.id) return result
  if (result?.data?.id) return result.data
  if (result?.data?.data?.id) return result.data.data
  return result?.data || result
}

function layBackendOrigin() {
  return String(ENV.API_BASE_URL || '')
      .replace(/\/api\/?$/, '')
      .replace(/\/$/, '')
}

function doiViewThanhDownload(duongDan) {
  return String(duongDan || '').replace(/\/view(?=($|[?#]))/, '/download')
}

function taoUrlDayDu(duongDan) {
  if (!duongDan) return ''

  const url = doiViewThanhDownload(duongDan)

  if (url.startsWith('http://') || url.startsWith('https://')) return url

  if (url.startsWith('/api/')) {
    return `${layBackendOrigin()}${url}`
  }

  if (url.startsWith('/public/')) {
    return `${layBackendOrigin()}${url}`
  }

  return url
}

function taoApiPathTuDuongDan(duongDan) {
  if (!duongDan) return ''

  const url = doiViewThanhDownload(duongDan)

  if (url.startsWith('/api/')) {
    return url.replace(/^\/api/, '')
  }

  const apiBase = String(ENV.API_BASE_URL || '').replace(/\/$/, '')

  if (url.startsWith(apiBase)) {
    return url.slice(apiBase.length)
  }

  const origin = layBackendOrigin()
  const fullApiPrefix = `${origin}/api`

  if (url.startsWith(fullApiPrefix)) {
    return url.slice(fullApiPrefix.length)
  }

  return url
}

function taoPreviewApiPath(item) {
  if (item?.__tepDinhKemId) {
    return `/he-thong/tep-dinh-kem/${item.__tepDinhKemId}/preview`
  }

  const apiPath = taoApiPathTuDuongDan(item?.duongDan || '')
  return String(apiPath || '').replace(/\/download(?=($|[?#]))/, '/preview')
}

function layExtension(item) {
  const source = [
    item?.duongDan,
    item?.__tenTep,
    item?.ten,
    item?.ma
  ].filter(Boolean).join(' ')

  const clean = String(source).split('?')[0].toLowerCase()
  const match = clean.match(/\.([a-z0-9]+)(?:$|[/?#\s])/)

  return match?.[1] || ''
}

function xacDinhLoaiPreview(item, blob) {
  const contentType = String(item?.__contentType || blob?.type || '').toLowerCase()
  const extension = layExtension(item)

  if (contentType.startsWith('image/') || ['jpg', 'jpeg', 'png', 'webp', 'gif', 'bmp', 'svg'].includes(extension)) {
    return 'image'
  }

  if (contentType.startsWith('video/') || ['mp4', 'mov', 'avi', 'mkv', 'webm'].includes(extension)) {
    return 'video'
  }

  if (contentType.startsWith('audio/') || ['mp3', 'wav', 'm4a', 'ogg'].includes(extension)) {
    return 'audio'
  }

  return 'document'
}

function layNhanLoaiFile(item) {
  const extension = layExtension(item)

  if (!extension) return 'Tệp tài liệu'
  if (['doc', 'docx'].includes(extension)) return 'Word'
  if (['xls', 'xlsx'].includes(extension)) return 'Excel'
  if (['ppt', 'pptx'].includes(extension)) return 'PowerPoint'
  if (extension === 'pdf') return 'PDF'
  if (['jpg', 'jpeg', 'png', 'webp', 'gif', 'bmp', 'svg'].includes(extension)) return 'Hình ảnh'
  if (['mp4', 'mov', 'avi', 'mkv', 'webm'].includes(extension)) return 'Video'
  if (['mp3', 'wav', 'm4a', 'ogg'].includes(extension)) return 'Âm thanh'
  if (['txt', 'csv'].includes(extension)) return 'Văn bản'

  return extension.toUpperCase()
}

function formatDungLuong(size) {
  const value = Number(size || 0)

  if (!value) return '-'
  if (value < 1024) return `${value} B`
  if (value < 1024 * 1024) return `${(value / 1024).toFixed(1)} KB`
  if (value < 1024 * 1024 * 1024) return `${(value / 1024 / 1024).toFixed(1)} MB`

  return `${(value / 1024 / 1024 / 1024).toFixed(1)} GB`
}

function xoaPreviewUrlCu() {
  if (previewUrl.value && previewUrl.value.startsWith('blob:')) {
    URL.revokeObjectURL(previewUrl.value)
  }

  previewUrl.value = ''
}

async function xemTepToanManHinh() {
  const item = tepDangXem.value

  if (!item?.duongDan) {
    xoaPreviewUrlCu()
    previewError.value = ''
    loaiPreview.value = ''
    return
  }

  loadingTep.value = true
  previewError.value = ''
  loaiPreview.value = ''
  xoaPreviewUrlCu()

  try {
    const apiPath = taoPreviewApiPath(item)

    const blob = await apiClient.get(apiPath, {
      responseType: 'blob'
    })

    loaiPreview.value = xacDinhLoaiPreview(item, blob)
    previewUrl.value = URL.createObjectURL(blob)
  } catch (error) {
    previewError.value = layThongBaoLoi(error, 'Không xem được tệp trong trang.')
  } finally {
    loadingTep.value = false
  }
}

function moTrangXemTep(index) {
  router.push({
    name: route.name,
    params: route.params,
    query: {
      ...route.query,
      fileIndex: index
    }
  })
}

function quayLaiDanhSachTep() {
  const query = { ...route.query }
  delete query.fileIndex

  router.push({
    name: route.name,
    params: route.params,
    query
  })
}

function moTepBangTrinhDuyet() {
  if (previewUrl.value) {
    window.open(previewUrl.value, '_blank')
    return
  }

  if (!tepDangXem.value?.duongDan) return
  window.open(taoUrlDayDu(tepDangXem.value.duongDan), '_blank')
}

async function xoaTepDangXem() {
  if (!tepDangXem.value?.id) return

  const dongY = window.confirm('Xóa tệp đã lưu đang chọn? Dữ liệu tài liệu vẫn giữ, chỉ xóa tệp này khỏi danh sách.')
  if (!dongY) return

  dangXoaTep.value = true

  try {
    const danhSachCu = tachDanhSachDuongDanTep(tepDangXem.value.__duongDanGoc || tepDangXem.value.duongDan)
    const danhSachMoi = danhSachCu.filter((item, index) => index !== tepDangXem.value.__fileIndex)

    const duongDanMoi = danhSachMoi.length === 0
        ? null
        : danhSachMoi.length === 1
            ? danhSachMoi[0].duongDan
            : JSON.stringify(danhSachMoi)

    const payload = {
      ...tepDangXem.value,
      duongDan: duongDanMoi
    }

    delete payload.__tepNguon
    delete payload.__fileIndex
    delete payload.__duongDanGoc
    delete payload.__tenTep
    delete payload.__contentType
    delete payload.__dungLuong
    delete payload.__tepDinhKemId

    if (tepNguonCanXem.value === 'taiLieuGoc') {
      await daoTaoXemChuongTrinhService.taiLieuGoc.update(tepDangXem.value.id, payload)
    } else if (tepNguonCanXem.value === 'syllabusChuongTrinhGoc') {
      await daoTaoXemChuongTrinhService.syllabusChuongTrinhGoc.update(tepDangXem.value.id, payload)
    } else if (tepNguonCanXem.value === 'syllabusChuongTrinh') {
      await daoTaoXemChuongTrinhService.syllabusChuongTrinh.update(tepDangXem.value.id, payload)
    } else if (tepNguonCanXem.value === 'mucTieuChuongTrinhGoc') {
      await daoTaoXemChuongTrinhService.mucTieuChuongTrinhGoc.update(tepDangXem.value.id, payload)
    } else if (tepNguonCanXem.value === 'nangLucDauRaGoc') {
      await daoTaoXemChuongTrinhService.nangLucDauRaGoc.update(tepDangXem.value.id, payload)
    } else if (tepNguonCanXem.value === 'viTriViecLamGoc') {
      await daoTaoXemChuongTrinhService.viTriViecLamGoc.update(tepDangXem.value.id, payload)
    } else {
      await daoTaoXemChuongTrinhService.syllabusTaiLieu.update(tepDangXem.value.id, payload)
    }

    baoTin('Đã xóa tệp đã lưu đang chọn.')

    quayLaiDanhSachTep()
    xoaPreviewUrlCu()

    await taiDuLieu()
  } catch (error) {
    baoTin(layThongBaoLoi(error, 'Không xóa được tệp đã lưu.'), 'error')
  } finally {
    dangXoaTep.value = false
  }
}

async function taiSyllabusMonHoc() {
  if (!syllabusMonId.value) {
    syllabusMonHoc.value = null
    return
  }

  const result = await daoTaoXemChuongTrinhService.syllabusMonHoc.getById(syllabusMonId.value)
  syllabusMonHoc.value = layMotDong(result)
}

async function taiChuongTrinhMon() {
  if (!chuongTrinhMonId.value) {
    chuongTrinhMon.value = null
    return
  }

  const result = await daoTaoXemChuongTrinhService.chuongTrinhMon.getById(chuongTrinhMonId.value)
  chuongTrinhMon.value = layMotDong(result)
}

async function taiTaiLieuSyllabus() {
  danhSachTaiLieu.value = []

  if (tepNguonCanXem.value !== 'syllabusTaiLieu') return
  if (!syllabusTaiLieuIdCanXem.value) return

  const result = await daoTaoXemChuongTrinhService.syllabusTaiLieu.getById(syllabusTaiLieuIdCanXem.value)
  const item = layMotDong(result)

  danhSachTaiLieu.value = item ? [item] : []
}

async function taiTaiLieuGocCanXem() {
  taiLieuGocDangXem.value = null

  if (tepNguonCanXem.value !== 'taiLieuGoc') return
  if (!taiLieuGocIdCanXem.value) return

  const result = await daoTaoXemChuongTrinhService.taiLieuGoc.getById(taiLieuGocIdCanXem.value)
  taiLieuGocDangXem.value = layMotDong(result)
}

async function taiSyllabusChuongTrinhGocCanXem() {
  syllabusChuongTrinhGocDangXem.value = null

  if (tepNguonCanXem.value !== 'syllabusChuongTrinhGoc') return
  if (!syllabusChuongTrinhGocIdCanXem.value) return

  const result = await daoTaoXemChuongTrinhService.syllabusChuongTrinhGoc.getById(syllabusChuongTrinhGocIdCanXem.value)
  syllabusChuongTrinhGocDangXem.value = layMotDong(result)
}

async function taiSyllabusChuongTrinhCanXem() {
  syllabusChuongTrinhDangXem.value = null

  if (tepNguonCanXem.value !== 'syllabusChuongTrinh') return
  if (!syllabusChuongTrinhIdCanXem.value) return

  const result = await daoTaoXemChuongTrinhService.syllabusChuongTrinh.getById(syllabusChuongTrinhIdCanXem.value)
  syllabusChuongTrinhDangXem.value = layMotDong(result)
}

async function taiMucTieuChuongTrinhGocCanXem() {
  mucTieuChuongTrinhGocDangXem.value = null

  if (tepNguonCanXem.value !== 'mucTieuChuongTrinhGoc') return
  if (!mucTieuChuongTrinhGocIdCanXem.value) return

  const result = await daoTaoXemChuongTrinhService.mucTieuChuongTrinhGoc.getById(mucTieuChuongTrinhGocIdCanXem.value)
  mucTieuChuongTrinhGocDangXem.value = layMotDong(result)
}

async function taiNangLucDauRaGocCanXem() {
  nangLucDauRaGocDangXem.value = null

  if (tepNguonCanXem.value !== 'nangLucDauRaGoc') return
  if (!nangLucDauRaGocIdCanXem.value) return

  const result = await daoTaoXemChuongTrinhService.nangLucDauRaGoc.getById(nangLucDauRaGocIdCanXem.value)
  nangLucDauRaGocDangXem.value = layMotDong(result)
}

async function taiViTriViecLamGocCanXem() {
  viTriViecLamGocDangXem.value = null

  if (tepNguonCanXem.value !== 'viTriViecLamGoc') return
  if (!viTriViecLamGocIdCanXem.value) return

  const result = await daoTaoXemChuongTrinhService.viTriViecLamGoc.getById(viTriViecLamGocIdCanXem.value)
  viTriViecLamGocDangXem.value = layMotDong(result)
}

async function taiDuLieu() {
  loading.value = true

  try {
    await Promise.all([
      taiSyllabusMonHoc(),
      taiChuongTrinhMon(),
      taiTaiLieuSyllabus(),
      taiTaiLieuGocCanXem(),
      taiSyllabusChuongTrinhGocCanXem(),
      taiSyllabusChuongTrinhCanXem(),
      taiMucTieuChuongTrinhGocCanXem(),
      taiNangLucDauRaGocCanXem(),
      taiViTriViecLamGocCanXem()
    ])

    if (dangMoToanManHinh.value) {
      await nextTick()
      await xemTepToanManHinh()
    } else {
      xoaPreviewUrlCu()
      previewError.value = ''
      loaiPreview.value = ''
    }
  } catch (error) {
    baoTin(layThongBaoLoi(error, 'Không tải được dữ liệu tệp.'), 'error')
  } finally {
    loading.value = false
  }
}

function quayLaiSyllabus() {
  if (duongDanQuayLai.value) {
    router.push(duongDanQuayLai.value)
    return
  }

  if (window.history.length > 1) {
    router.back()
    return
  }

  const tepNguon = tepNguonCanXem.value

  if (tepNguon === 'syllabusChuongTrinhGoc') {
    router.push({ name: 'DaoTao.SyllabusChuongTrinhGoc' })
    return
  }

  if (tepNguon === 'syllabusChuongTrinh') {
    router.push({
      name: 'DaoTao.XemChuongTrinh.SyllabusGoc',
      params: {
        nganhId: route.params.nganhId || 0,
        chuongTrinhId: route.params.chuongTrinhId || 0,
        versionId: route.params.versionId || 0
      }
    })
    return
  }

  router.push({
    name: 'DaoTao.XemChuongTrinh.SyllabusApDung',
    params: {
      nganhId: route.params.nganhId || 0,
      chuongTrinhId: route.params.chuongTrinhId || 0,
      versionId: route.params.versionId || 0,
      chuongTrinhMonId: route.params.chuongTrinhMonId || 0
    },
    query: {
      syllabusMonId: syllabusMonId.value,
      khungKyId: route.query.khungKyId,
      nhomKienThucId: route.query.nhomKienThucId
    }
  })
}

watch(
    () => route.query.fileIndex,
    async () => {
      if (dangMoToanManHinh.value) {
        await nextTick()
        await xemTepToanManHinh()
      } else {
        xoaPreviewUrlCu()
        previewError.value = ''
        loaiPreview.value = ''
      }
    }
)

onMounted(() => {
  taiDuLieu()
})

onBeforeUnmount(() => {
  xoaPreviewUrlCu()
})
</script>

<style scoped>
.xem-tep-syllabus-page {
  padding: 24px;
  min-height: calc(100vh - 80px);
  background: #f8fafc;
}

.page-head {
  display: flex;
  align-items: flex-start;
  justify-content: flex-end;
  gap: 16px;
  margin-bottom: 16px;
}

.head-actions {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.notice {
  margin-bottom: 12px;
  padding: 10px 12px;
  border: 1px solid #bbf7d0;
  border-radius: 10px;
  background: #f0fdf4;
  color: #166534;
  font-size: 13px;
}

.notice.error {
  border-color: #fecaca;
  background: #fef2f2;
  color: #b91c1c;
}

.info-box {
  display: grid;
  grid-template-columns: 1.4fr 1.4fr 0.5fr;
  gap: 10px;
  margin-bottom: 14px;
}

.info-box div {
  border: 1px solid #bfdbfe;
  border-radius: 12px;
  background: #eff6ff;
  padding: 12px;
}

.info-box span {
  display: block;
  color: #1d4ed8;
  font-size: 12px;
  margin-bottom: 4px;
}

.info-box b {
  color: #111827;
  font-size: 13px;
}

.table-card,
.viewer-full-page {
  border: 1px solid #d1d5db;
  border-radius: 14px;
  background: #ffffff;
  overflow: hidden;
  box-shadow: 0 10px 24px rgba(15, 23, 42, 0.06);
}

.table-title {
  padding: 14px 16px;
  border-bottom: 1px solid #e5e7eb;
  background: linear-gradient(135deg, #f8fafc, #eff6ff);
}

.table-title h3 {
  margin: 0;
  color: #111827;
  font-size: 16px;
}

.table-title p {
  margin: 4px 0 0;
  color: #64748b;
  font-size: 12px;
}

.table-wrap {
  width: 100%;
  max-height: 680px;
  overflow: auto;
}

table {
  width: 100%;
  min-width: 1050px;
  border-collapse: collapse;
  font-size: 12px;
}

th,
td {
  border-top: 1px solid #e5e7eb;
  padding: 9px 10px;
  text-align: left;
  vertical-align: top;
}

th {
  position: sticky;
  top: 0;
  z-index: 1;
  background: #f3f4f6;
  color: #374151;
  font-size: 11px;
  font-weight: 700;
}

tbody tr:hover {
  background: #f8fafc;
}

.col-stt {
  width: 56px;
  text-align: center;
}

.col-action {
  width: 100px;
  text-align: center;
  white-space: nowrap;
}

.file-name {
  display: grid;
  gap: 4px;
}

.file-name b {
  color: #0f172a;
  font-size: 13px;
}

.file-name small {
  color: #2563eb;
  font-size: 11px;
}

.empty-cell {
  text-align: center;
  color: #6b7280;
  padding: 28px;
}

.viewer-full-page {
  min-height: calc(100vh - 230px);
  display: flex;
  flex-direction: column;
}

.viewer-toolbar {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 12px;
  padding: 14px 16px;
  border-bottom: 1px solid #e5e7eb;
  background: linear-gradient(135deg, #f8fafc, #eef2ff);
}

.viewer-toolbar h3 {
  margin: 0;
  color: #111827;
  font-size: 17px;
}

.viewer-toolbar p {
  margin: 5px 0 0;
  color: #64748b;
  font-size: 12px;
}

.viewer-toolbar span {
  margin: 0 6px;
  color: #cbd5e1;
}

.viewer-actions {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-wrap: wrap;
}

.preview-body {
  flex: 1;
  min-height: 0;
  background: #e5e7eb;
}

.preview-frame {
  width: 100%;
  height: calc(100vh - 280px);
  min-height: 680px;
  border: 0;
  background: #ffffff;
}

.preview-image {
  display: block;
  width: 100%;
  height: calc(100vh - 280px);
  min-height: 680px;
  object-fit: contain;
  background: #111827;
}

.preview-video {
  width: 100%;
  height: calc(100vh - 280px);
  min-height: 680px;
  background: #000000;
}

.preview-audio {
  width: calc(100% - 48px);
  margin: 48px 24px;
}

.preview-loading,
.preview-error {
  padding: 48px;
  color: #6b7280;
  font-size: 14px;
  text-align: center;
}

.preview-error {
  color: #b91c1c;
}

.btn {
  min-height: 32px;
  border: 1px solid #cbd5e1;
  border-radius: 8px;
  background: #ffffff;
  color: #111827;
  padding: 6px 12px;
  font-size: 12px;
  cursor: pointer;
  white-space: nowrap;
}

.btn:hover {
  background: #f8fafc;
}

.btn:disabled {
  cursor: not-allowed;
  opacity: 0.65;
}

.btn.primary {
  border-color: #2563eb;
  background: #2563eb;
  color: #ffffff;
}

.btn.danger {
  background: #dc2626;
  border-color: #dc2626;
  color: #ffffff;
}

.btn.danger:hover:not(:disabled) {
  background: #b91c1c;
  border-color: #b91c1c;
}

.btn.tiny {
  min-height: 26px;
  padding: 4px 10px;
  font-size: 11px;
}

@media (max-width: 1100px) {
  .info-box {
    grid-template-columns: 1fr;
  }

  .page-head,
  .viewer-toolbar {
    flex-direction: column;
  }

  .preview-frame,
  .preview-image,
  .preview-video {
    height: 620px;
    min-height: 620px;
  }
}
</style>