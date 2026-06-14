<template>
  <div class="xem-tep-syllabus-page">
    <div class="page-head">
      <div class="head-actions">
        <button type="button" class="btn" @click="quayLaiSyllabus">
          ← Quay lại
        </button>

        <button type="button" class="btn primary" :disabled="loading" @click="taiDuLieu">
          Tải lại
        </button>
      </div>
    </div>

    <div v-if="thongBao" :class="['notice', loaiThongBao]">
      {{ thongBao }}
    </div>



    <!-- Bảng danh sách tệp (luôn hiển thị) -->
    <section class="table-card">
      <div class="table-title">
        <div>
          <h3>{{ tieuDeBangTep }}</h3>
          <p>Danh sách tệp đã lưu. Bấm "Xem" để xem trước trực tiếp.</p>
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
              :class="{ 'row-dang-xem': tepDangXemIndex === index }"
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
                  :disabled="loadingTep"
                  @click="moXemTepInline(index)"
              >
                {{ tepDangXemIndex === index ? 'Đang xem' : 'Xem' }}
              </button>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </section>

    <!-- Viewer inline (read-only) — hiện bên dưới bảng khi bấm Xem -->
    <section v-if="tepDangXemIndex !== null" class="viewer-full-page">
      <div class="viewer-toolbar">
        <div>
          <h3>{{ tepDangXem?.__tenTep || tepDangXem?.ten || 'Tệp đang xem' }}</h3>
          <p>
            {{ layNhanLoaiFile(tepDangXem) }}
            <span v-if="tepDangXem?.__dungLuong">| {{ formatDungLuong(tepDangXem.__dungLuong) }}</span>
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
              class="btn primary"
              :disabled="loadingTep"
              @click="taiVeTep"
          >
            Tải về
          </button>

          <button
              type="button"
              class="btn"
              @click="dongViewer"
          >
            ✕ Đóng
          </button>
        </div>
      </div>

      <div v-if="loadingTep" class="preview-loading">
        Đang tải tệp...
      </div>

      <div v-else-if="previewError" class="preview-error">
        <p>{{ previewError }}</p>
        <button type="button" class="btn primary" style="margin-top:10px" @click="taiVeTep">
          Tải về thay thế
        </button>
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
const thongBao = ref('')
const loaiThongBao = ref('success')

// Local state cho viewer inline (không dùng URL fileIndex nữa)
const tepDangXemIndex = ref(null)

const syllabusMonHoc = ref(null)
const chuongTrinhMon = ref(null)
const danhSachTaiLieu = ref([])
const taiLieumauDangXem = ref(null)
const syllabusChuongTrinhmauDangXem = ref(null)
const syllabusChuongTrinhDangXem = ref(null)
const mucTieuChuongTrinhmauDangXem = ref(null)
const nangLucDauRamauDangXem = ref(null)
const viTriViecLammauDangXem = ref(null)
const syllabusMonHocFileDangXem = ref(null)
const tepDinhKemSyllabusChuongTrinh = ref([])
const tepDinhKemSyllabusChuongTrinhmau = ref([])
const previewUrl = ref('')
const previewError = ref('')
const loaiPreview = ref('')

const syllabusMonId = computed(() => route.query.syllabusMonId || route.params.syllabusMonId || null)
const chuongTrinhMonId = computed(() => {
  const val = route.params.chuongTrinhMonId || route.query.chuongTrinhMonId || null
  return val && String(val) !== '0' ? val : null
})

const chuongTrinhIdCanXem = computed(() => {
  const val = route.params.chuongTrinhId || route.query.chuongTrinhId || null
  return val && String(val) !== '0' ? val : null
})

const versionIdCanXem = computed(() => {
  const val =
      route.params.versionId ||
      route.query.versionId ||
      route.query.chuongTrinhVersionId ||
      null

  return val && String(val) !== '0' ? val : null
})

const tepNguonCanXem = computed(() => {
  const value = String(route.query.tepNguon || '')

  if (value === 'taiLieumau') return 'taiLieumau'
  if (value === 'syllabusChuongTrinhmau') return 'syllabusChuongTrinhmau'
  if (value === 'syllabusChuongTrinh') return 'syllabusChuongTrinh'
  if (value === 'mucTieuChuongTrinhmau') return 'mucTieuChuongTrinhmau'
  if (value === 'nangLucDauRamau') return 'nangLucDauRamau'
  if (value === 'viTriViecLammau') return 'viTriViecLammau'
  if (value === 'syllabusMonHocFile') return 'syllabusMonHocFile'

  return 'syllabusTaiLieu'
})

const syllabusMonHocFileIdCanXem = computed(() => route.query.syllabusMonHocFileId || null)
const taiLieumauIdCanXem = computed(() => route.query.taiLieumauId || null)
const syllabusTaiLieuIdCanXem = computed(() => route.query.syllabusTaiLieuId || null)
const syllabusChuongTrinhmauIdCanXem = computed(() => route.query.syllabusChuongTrinhmauId || null)
const syllabusChuongTrinhIdCanXem = computed(() => route.query.syllabusChuongTrinhId || null)
const mucTieuChuongTrinhmauIdCanXem = computed(() => route.query.mucTieuChuongTrinhmauId || null)
const nangLucDauRamauIdCanXem = computed(() => route.query.nangLucDauRamauId || null)
const viTriViecLammauIdCanXem = computed(() => route.query.viTriViecLammauId || null)

const duongDanQuayLai = computed(() => {
  const value = route.query.quayLaiPath || route.query.returnPath || route.query.from

  if (!value) return ''

  try {
    return decodeURIComponent(String(value))
  } catch (error) {
    return String(value)
  }
})

const tepDangXem = computed(() => {
  if (tepDangXemIndex.value === null) return null
  return danhSachTep.value[tepDangXemIndex.value] || null
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
    syllabus.tenSyllabusMonHocmau,
    syllabus.maSyllabusMonHocmau,
    syllabus.mucTieu
  ].filter(Boolean)[0] || syllabus.id || '-'
})

const tieuDeBangTep = computed(() => {
  if (tepNguonCanXem.value === 'taiLieumau') return 'Danh sách tệp tài liệu gốc / mẫu'
  if (tepNguonCanXem.value === 'syllabusChuongTrinhmau') return 'Danh sách tệp syllabus chương trình gốc / mẫu'
  if (tepNguonCanXem.value === 'syllabusChuongTrinh') return 'Danh sách tệp syllabus chương trình'
  if (tepNguonCanXem.value === 'mucTieuChuongTrinhmau') return 'Danh sách tệp mục tiêu chương trình gốc / mẫu'
  if (tepNguonCanXem.value === 'nangLucDauRamau') return 'Danh sách tệp năng lực đầu ra gốc / mẫu'
  if (tepNguonCanXem.value === 'viTriViecLammau') return 'Danh sách tệp vị trí việc làm gốc / mẫu'
  if (tepNguonCanXem.value === 'syllabusMonHocFile') return 'File syllabus môn học áp dụng'

  return 'Danh sách tệp tài liệu syllabus'
})

const nhanSoTep = computed(() => {
  if (tepNguonCanXem.value === 'taiLieumau') return 'Số tệp tài liệu gốc'
  if (tepNguonCanXem.value === 'syllabusChuongTrinhmau') return 'Số tệp syllabus chương trình gốc'
  if (tepNguonCanXem.value === 'syllabusChuongTrinh') return 'Số tệp syllabus chương trình'

  return 'Số tệp tài liệu syllabus'
})

const thongBaoRong = computed(() => {
  if (tepNguonCanXem.value === 'taiLieumau') return 'Tài liệu gốc / mẫu này chưa có tệp đã lưu.'
  if (tepNguonCanXem.value === 'syllabusChuongTrinhmau') return 'Syllabus chương trình gốc / mẫu này chưa có tệp đã lưu.'
  if (tepNguonCanXem.value === 'syllabusChuongTrinh') return 'Syllabus chương trình này chưa có tệp đã lưu.'
  if (tepNguonCanXem.value === 'mucTieuChuongTrinhmau') return 'Mục tiêu chương trình gốc / mẫu này chưa có tệp đã lưu.'
  if (tepNguonCanXem.value === 'nangLucDauRamau') return 'Năng lực đầu ra gốc / mẫu này chưa có tệp đã lưu.'
  if (tepNguonCanXem.value === 'viTriViecLammau') return 'Vị trí việc làm gốc / mẫu này chưa có tệp đã lưu.'
  if (tepNguonCanXem.value === 'syllabusMonHocFile') return 'Không tìm thấy file syllabus môn học này.'

  return 'Tài liệu syllabus này chưa có tệp đã lưu.'
})

const danhSachTep = computed(() => {
  if (tepNguonCanXem.value === 'taiLieumau') {
    if (!taiLieumauDangXem.value?.duongDan) return []
    return taoDanhSachTepTuDongTaiLieu(taiLieumauDangXem.value, 'taiLieumau')
  }

  if (tepNguonCanXem.value === 'syllabusChuongTrinhmau') {
    // Ưu tiên TepDinhKem (upload mới) trước, fallback về duongDan cũ nếu không có
    if (tepDinhKemSyllabusChuongTrinhmau.value.length > 0) {
      const mau = syllabusChuongTrinhmauDangXem.value || {}
      return tepDinhKemSyllabusChuongTrinhmau.value.map((file, index) => ({
        ...mau,
        id: mau.id,
        duongDan: `/he-thong/tep-dinh-kem/${file.id}/preview`,
        __tepNguon: 'syllabusChuongTrinhmau',
        __tepDinhKemId: file.id,
        __duongDanmau: mau.duongDan || '',
        __fileIndex: index,
        __tenTep: file.tenMau || file.tenLuu || `File ${index + 1}`,
        __contentType: file.contentType || '',
        __dungLuong: file.dungLuong || 0
      }))
    }
    if (!syllabusChuongTrinhmauDangXem.value?.duongDan) return []
    return taoDanhSachTepTuDongTaiLieu(syllabusChuongTrinhmauDangXem.value, 'syllabusChuongTrinhmau')
  }

  if (tepNguonCanXem.value === 'syllabusChuongTrinh') {
    // Ưu tiên dùng TepDinhKem (tránh trường hợp duongDan chỉ lưu tên file thuần, không có id)
    if (tepDinhKemSyllabusChuongTrinh.value.length > 0) {
      const syllabus = syllabusChuongTrinhDangXem.value || {}
      return tepDinhKemSyllabusChuongTrinh.value.map((file, index) => ({
        ...syllabus,
        id: syllabus.id,
        duongDan: `/he-thong/tep-dinh-kem/${file.id}/preview`,
        __tepNguon: 'syllabusChuongTrinh',
        __tepDinhKemId: file.id,
        __duongDanmau: syllabus.duongDan || '',
        __fileIndex: index,
        __tenTep: file.tenMau || file.tenLuu || `File ${index + 1}`,
        __contentType: file.contentType || '',
        __dungLuong: file.dungLuong || 0
      }))
    }
    if (!syllabusChuongTrinhDangXem.value?.duongDan) return []
    return taoDanhSachTepTuDongTaiLieu(syllabusChuongTrinhDangXem.value, 'syllabusChuongTrinh')
  }

  if (tepNguonCanXem.value === 'mucTieuChuongTrinhmau') {
    if (!mucTieuChuongTrinhmauDangXem.value?.duongDan) return []
    return taoDanhSachTepTuDongTaiLieu(mucTieuChuongTrinhmauDangXem.value, 'mucTieuChuongTrinhmau')
  }

  if (tepNguonCanXem.value === 'nangLucDauRamau') {
    if (!nangLucDauRamauDangXem.value?.duongDan) return []
    return taoDanhSachTepTuDongTaiLieu(nangLucDauRamauDangXem.value, 'nangLucDauRamau')
  }

  if (tepNguonCanXem.value === 'viTriViecLammau') {
    if (!viTriViecLammauDangXem.value?.duongDan) return []
    return taoDanhSachTepTuDongTaiLieu(viTriViecLammauDangXem.value, 'viTriViecLammau')
  }

  if (tepNguonCanXem.value === 'syllabusMonHocFile') {
    const file = syllabusMonHocFileDangXem.value
    if (!file?.id) return []
    return [{
      id: file.id,
      duongDan: `/api/chuongTrinh/syllabus-mon-hoc-files/${file.id}/view`,
      __tepNguon: 'syllabusMonHocFile',
      __tenTep: file.tenFile || 'File syllabus',
      __contentType: file.loaiFile || '',
      __dungLuong: file.kichThuoc || 0,
      __fileIndex: 0,
      tenFile: file.tenFile,
      loaiFile: file.loaiFile,
      kichThuoc: file.kichThuoc,
      createdAt: file.createdAt
    }]
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
                  tenmau: layTenFileTuDuongDan(item),
                  tenHienThi: layTenFileTuDuongDan(item),
                  contentType: '',
                  dungLuong: 0
                }
              }

              return {
                id: item?.id || item?.tepDinhKemId || null,
                duongDan: item?.duongDan || '',
                tenmau: item?.tenmau || item?.fileName || layTenFileTuDuongDan(item?.duongDan || ''),
                tenHienThi: item?.tenHienThi || item?.tenmau || layTenFileTuDuongDan(item?.duongDan || ''),
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
        tenmau: layTenFileTuDuongDan(text),
        tenHienThi: layTenFileTuDuongDan(text),
        contentType: '',
        dungLuong: 0
      }]
    }
  }

  return [{
    id: null,
    duongDan: text,
    tenmau: layTenFileTuDuongDan(text),
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
        __duongDanmau: item.duongDan,
        __tepNguon: tepNguon,
        __fileIndex: index,
        __tenTep: tep.tenHienThi || tep.tenmau || `${item?.ten || 'Tệp'} ${index + 1}`,
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

  // syllabusMonHocFile dùng endpoint /view trực tiếp, không qua doiViewThanhDownload
  if (item?.__tepNguon === 'syllabusMonHocFile' && item?.id) {
    return `/chuongTrinh/syllabus-mon-hoc-files/${item.id}/view`
  }

  const apiPath = taoApiPathTuDuongDan(item?.duongDan || '')
  if (!apiPath) return null

  // Chỉ chấp nhận path bắt đầu bằng '/' (API path hợp lệ) hoặc URL đầy đủ
  // Nếu là plain relative path như 'pdf/...' hoặc 'uploads/...' → không hợp lệ → trả null
  const result = String(apiPath).replace(/\/download(?=($|[?#]))/, '/preview')
  if (!result.startsWith('/') && !result.startsWith('http://') && !result.startsWith('https://')) {
    return null
  }

  return result
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

function taoDownloadApiPath(item) {
  if (item?.__tepDinhKemId) {
    return `/he-thong/tep-dinh-kem/${item.__tepDinhKemId}/download`
  }
  if (item?.__tepNguon === 'syllabusMonHocFile' && item?.id) {
    return `/chuongTrinh/syllabus-mon-hoc-files/${item.id}/download`
  }
  const apiPath = taoApiPathTuDuongDan(item?.duongDan || '')
  if (!apiPath) return null
  if (!apiPath.startsWith('/') && !apiPath.startsWith('http://') && !apiPath.startsWith('https://')) return null
  return apiPath
}

async function xemTepToanManHinh() {
  const item = tepDangXem.value

  xoaPreviewUrlCu()
  previewError.value = ''
  loaiPreview.value = ''

  if (!item) return

  const apiPath = taoPreviewApiPath(item)

  if (!apiPath) {
    previewError.value = 'Không tìm thấy file vật lý hoặc đường dẫn không hợp lệ. Hãy tải lên lại file qua giao diện quản lý.'
    return
  }

  loadingTep.value = true

  try {
    const blob = await apiClient.get(apiPath, {
      responseType: 'blob'
    })

    loaiPreview.value = xacDinhLoaiPreview(item, blob)
    previewUrl.value = URL.createObjectURL(blob)
  } catch (error) {
    previewError.value = layThongBaoLoi(error, 'Không xem được tệp. Thử tải về để xem.')
  } finally {
    loadingTep.value = false
  }
}

async function taiVeTep() {
  const item = tepDangXem.value
  if (!item) return

  const apiPath = taoDownloadApiPath(item)
  if (!apiPath) {
    baoTin('Không tìm thấy đường dẫn tải về.', 'error')
    return
  }

  try {
    const blob = await apiClient.get(apiPath, { responseType: 'blob' })
    const url = URL.createObjectURL(blob)
    const a = document.createElement('a')
    a.href = url
    a.download = item.__tenTep || item.ten || 'tai-lieu'
    document.body.appendChild(a)
    a.click()
    document.body.removeChild(a)
    setTimeout(() => URL.revokeObjectURL(url), 2000)
  } catch (error) {
    // fallback: mở tab nếu có URL đầy đủ
    const fullUrl = taoUrlDayDu(item?.duongDan || '')
    if (fullUrl && (fullUrl.startsWith('http://') || fullUrl.startsWith('https://'))) {
      window.open(doiViewThanhDownload(fullUrl), '_blank')
    } else {
      baoTin(layThongBaoLoi(error, 'Không tải được tệp.'), 'error')
    }
  }
}

async function moXemTepInline(index) {
  if (tepDangXemIndex.value === index) {
    // Bấm lại → đóng viewer
    dongViewer()
    return
  }
  tepDangXemIndex.value = index
  xoaPreviewUrlCu()
  previewError.value = ''
  loaiPreview.value = ''
  await nextTick()
  await xemTepToanManHinh()
}

function dongViewer() {
  tepDangXemIndex.value = null
  xoaPreviewUrlCu()
  previewError.value = ''
  loaiPreview.value = ''
}

function moTepBangTrinhDuyet() {
  if (previewUrl.value) {
    window.open(previewUrl.value, '_blank')
    return
  }
  const item = tepDangXem.value
  if (!item) return
  const apiPath = taoPreviewApiPath(item)
  if (apiPath) {
    const fullUrl = apiPath.startsWith('/') ? `${layBackendOrigin()}/api${apiPath}` : apiPath
    window.open(fullUrl, '_blank')
    return
  }
  const fullUrl = taoUrlDayDu(item.duongDan || '')
  if (fullUrl) window.open(fullUrl, '_blank')
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

async function taiTaiLieumauCanXem() {
  taiLieumauDangXem.value = null

  if (tepNguonCanXem.value !== 'taiLieumau') return
  if (!taiLieumauIdCanXem.value) return

  const result = await daoTaoXemChuongTrinhService.taiLieumau.getById(taiLieumauIdCanXem.value)
  taiLieumauDangXem.value = layMotDong(result)
}

async function taiSyllabusChuongTrinhmauCanXem() {
  syllabusChuongTrinhmauDangXem.value = null
  tepDinhKemSyllabusChuongTrinhmau.value = []

  if (tepNguonCanXem.value !== 'syllabusChuongTrinhmau') return
  if (!syllabusChuongTrinhmauIdCanXem.value) return

  const result = await daoTaoXemChuongTrinhService.syllabusChuongTrinhmau.getById(syllabusChuongTrinhmauIdCanXem.value)
  syllabusChuongTrinhmauDangXem.value = layMotDong(result)

  // Tải TepDinhKem theo doiTuongId = syllabusChuongTrinhmauId
  // DaoTaoSyllabusChuongTrinhMau.vue upload với nghiepVu: 'SYLLABUS_CHUONG_TRINH_mau'
  try {
    const tepRes = await apiClient.get('/he-thong/tep-dinh-kem/doi-tuong', {
      params: {
        module: 'dao-tao',
        nghiepVu: 'SYLLABUS_CHUONG_TRINH_mau',
        doiTuongId: syllabusChuongTrinhmauIdCanXem.value,
        page: 0,
        size: 50
      }
    })
    const tepData = tepRes?.data?.data ?? tepRes?.data ?? tepRes
    const items = Array.isArray(tepData?.content)
      ? tepData.content
      : Array.isArray(tepData)
        ? tepData
        : []
    tepDinhKemSyllabusChuongTrinhmau.value = items
  } catch {
    tepDinhKemSyllabusChuongTrinhmau.value = []
  }
}

async function taiSyllabusChuongTrinhCanXem() {
  syllabusChuongTrinhDangXem.value = null
  tepDinhKemSyllabusChuongTrinh.value = []

  if (tepNguonCanXem.value !== 'syllabusChuongTrinh') return
  if (!syllabusChuongTrinhIdCanXem.value) return

  const result = await daoTaoXemChuongTrinhService.syllabusChuongTrinh.getById(syllabusChuongTrinhIdCanXem.value)
  syllabusChuongTrinhDangXem.value = layMotDong(result)

  // Tải TepDinhKem theo doiTuongId = syllabusChuongTrinhId để lấy ID thực của file
  try {
    const tepRes = await apiClient.get('/he-thong/tep-dinh-kem/doi-tuong', {
      params: {
        module: 'dao-tao',
        nghiepVu: 'syllabus_chuong_trinh',
        doiTuongId: syllabusChuongTrinhIdCanXem.value,
        page: 0,
        size: 50
      }
    })
    const tepData = tepRes?.data?.data ?? tepRes?.data ?? tepRes
    const items = Array.isArray(tepData?.content)
      ? tepData.content
      : Array.isArray(tepData)
        ? tepData
        : []
    tepDinhKemSyllabusChuongTrinh.value = items
  } catch {
    tepDinhKemSyllabusChuongTrinh.value = []
  }
}

async function taiMucTieuChuongTrinhmauCanXem() {
  mucTieuChuongTrinhmauDangXem.value = null

  if (tepNguonCanXem.value !== 'mucTieuChuongTrinhmau') return
  if (!mucTieuChuongTrinhmauIdCanXem.value) return

  const result = await daoTaoXemChuongTrinhService.mucTieuChuongTrinhmau.getById(mucTieuChuongTrinhmauIdCanXem.value)
  mucTieuChuongTrinhmauDangXem.value = layMotDong(result)
}

async function taiNangLucDauRamauCanXem() {
  nangLucDauRamauDangXem.value = null

  if (tepNguonCanXem.value !== 'nangLucDauRamau') return
  if (!nangLucDauRamauIdCanXem.value) return

  const result = await daoTaoXemChuongTrinhService.nangLucDauRamau.getById(nangLucDauRamauIdCanXem.value)
  nangLucDauRamauDangXem.value = layMotDong(result)
}

async function taiViTriViecLammauCanXem() {
  viTriViecLammauDangXem.value = null

  if (tepNguonCanXem.value !== 'viTriViecLammau') return
  if (!viTriViecLammauIdCanXem.value) return

  const result = await daoTaoXemChuongTrinhService.viTriViecLammau.getById(viTriViecLammauIdCanXem.value)
  viTriViecLammauDangXem.value = layMotDong(result)
}

async function taiSyllabusMonHocFileCanXem() {
  syllabusMonHocFileDangXem.value = null

  if (tepNguonCanXem.value !== 'syllabusMonHocFile') return
  if (!syllabusMonHocFileIdCanXem.value) return

  const result = await daoTaoXemChuongTrinhService.syllabusMonHocFile.getInfo(syllabusMonHocFileIdCanXem.value)
  syllabusMonHocFileDangXem.value = layMotDong(result)
}

async function taiDuLieu() {
  loading.value = true

  try {
    await Promise.all([
      taiSyllabusMonHoc(),
      taiChuongTrinhMon(),
      taiTaiLieuSyllabus(),
      taiTaiLieumauCanXem(),
      taiSyllabusChuongTrinhmauCanXem(),
      taiSyllabusChuongTrinhCanXem(),
      taiMucTieuChuongTrinhmauCanXem(),
      taiNangLucDauRamauCanXem(),
      taiViTriViecLammauCanXem(),
      taiSyllabusMonHocFileCanXem()
    ])

    // Reset viewer khi tải lại dữ liệu (tránh hiển thị viewer cũ)
    dongViewer()
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

  if (tepNguon === 'syllabusChuongTrinhmau') {
    router.push({ name: 'DaoTao.SyllabusChuongTrinhMau' })
    return
  }

  if (tepNguon === 'syllabusChuongTrinh') {
    router.push({
      name: 'DaoTao.XemChuongTrinh.Syllabusmau',
      params: {
        nganhId: route.params.nganhId || 0,
        chuongTrinhId: chuongTrinhIdCanXem.value || 0,
        versionId: versionIdCanXem.value || 0
      },
      query: {
        ...route.query,
        chuongTrinhId: chuongTrinhIdCanXem.value || route.query.chuongTrinhId,
        versionId: versionIdCanXem.value || route.query.versionId,
        chuongTrinhVersionId: versionIdCanXem.value || route.query.chuongTrinhVersionId
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

.table-card {
  border: 1px solid #d1d5db;
  border-radius: 14px;
  background: #ffffff;
  overflow: hidden;
  box-shadow: 0 10px 24px rgba(15, 23, 42, 0.06);
  margin-bottom: 16px;
}

.viewer-full-page {
  border: 1px solid #d1d5db;
  border-radius: 14px;
  background: #ffffff;
  box-shadow: 0 10px 24px rgba(15, 23, 42, 0.06);
  margin-bottom: 16px;
  overflow: hidden;
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
  max-height: 480px;
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

tbody tr.row-dang-xem {
  background: #eff6ff;
}

.col-stt {
  width: 56px;
  text-align: center;
}

.col-action {
  width: 80px;
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
  min-height: 500px;
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
  overflow-x: auto;
}

.preview-frame {
  display: block;
  width: 100%;
  min-width: 860px;
  height: calc(100vh - 200px);
  min-height: 1200px;
  border: 0;
  background: #ffffff;
}

.preview-image {
  display: block;
  width: 100%;
  height: calc(100vh - 280px);
  min-height: 600px;
  object-fit: contain;
  background: #111827;
}

.preview-video {
  width: 100%;
  height: calc(100vh - 280px);
  min-height: 600px;
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

.btn.tiny {
  min-height: 22px;
  padding: 2px 6px;
  font-size: 10px;
  white-space: nowrap;
  flex-shrink: 0;
}

@media (max-width: 1100px) {
  .page-head,
  .viewer-toolbar {
    flex-direction: column;
  }

  .preview-frame,
  .preview-image,
  .preview-video {
    height: 820px;
    min-height: 820px;
  }
}
</style>