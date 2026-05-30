<template>
  <div class="xem-tai-lieu">
    <!-- Khối 1: Tài liệu syllabus trực tiếp -->
    <section class="khoi">
      <h3 class="khoi-title">Tài liệu syllabus trực tiếp</h3>
      <div class="bang-wrap">
        <table v-if="syllabusTaiLieu.length" class="bang">
          <thead>
            <tr>
              <th>Tên</th>
              <th>Loại tài liệu</th>
              <th>Tác giả</th>
              <th>Năm XB</th>
              <th>Nhà XB</th>
              <th>Đường dẫn</th>
              <th>Ghi chú</th>
              <th>Tệp</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(row, index) in syllabusTaiLieu" :key="row.id || index">
              <td>{{ dinhDang(row.ten) }}</td>
              <td>{{ dinhDang(row.loaiTaiLieu) }}</td>
              <td>{{ dinhDang(row.tacGia) }}</td>
              <td>{{ dinhDang(row.namXuatBan) }}</td>
              <td>{{ dinhDang(row.nhaXuatBan) }}</td>
              <td class="duong-dan">{{ dinhDang(row.duongDan) }}</td>
              <td>{{ dinhDang(row.ghiChu) }}</td>
              <td class="cot-thao-tac">
                <button v-if="coTep(row)" type="button" class="btn btn-xem" @click="xemTep(row)">Xem tệp</button>
                <button v-if="coDuongDanTai(row)" type="button" class="btn btn-tai" @click="taiTep(row)">Tải tệp</button>
                <span v-if="!coTep(row) && !coDuongDanTai(row)" class="khong-tep">-</span>
              </td>
            </tr>
          </tbody>
        </table>
        <p v-else class="trong-text">Syllabus môn này chưa có tài liệu trực tiếp đã gán.</p>
      </div>
    </section>

    <!-- Khối 2: Tài liệu đã gán qua bảng nối -->
    <section class="khoi">
      <h3 class="khoi-title">Tài liệu đã gán qua bảng nối</h3>
      <div class="bang-wrap">
        <table v-if="taiLieuNoiMap.length" class="bang">
          <thead>
            <tr>
              <th>Tên tài liệu gốc</th>
              <th>Loại tài liệu</th>
              <th>Tác giả</th>
              <th>Năm XB</th>
              <th>Nhà XB</th>
              <th>Đường dẫn</th>
              <th>Thứ tự</th>
              <th>Ghi chú</th>
              <th>Tệp</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(row, index) in taiLieuNoiMap" :key="row.id || index">
              <td>{{ dinhDang(row.tenTaiLieuGoc) }}</td>
              <td>{{ dinhDang(row.loaiTaiLieu) }}</td>
              <td>{{ dinhDang(row.tacGia) }}</td>
              <td>{{ dinhDang(row.namXuatBan) }}</td>
              <td>{{ dinhDang(row.nhaXuatBan) }}</td>
              <td class="duong-dan">{{ dinhDang(row.duongDan) }}</td>
              <td>{{ dinhDang(row.thuTu) }}</td>
              <td>{{ dinhDang(row.ghiChu) }}</td>
              <td class="cot-thao-tac">
                <button v-if="coTep(row)" type="button" class="btn btn-xem" @click="xemTep(row)">Xem tệp</button>
                <button v-if="coDuongDanTai(row)" type="button" class="btn btn-tai" @click="taiTep(row)">Tải tệp</button>
                <span v-if="!coTep(row) && !coDuongDanTai(row)" class="khong-tep">-</span>
              </td>
            </tr>
          </tbody>
        </table>
        <p v-else class="trong-text">Syllabus môn này chưa có tài liệu gán qua bảng nối.</p>
      </div>
    </section>

    <!-- Khối 3: Khu vực cập nhật tệp (chỉ khi có quyền) -->
    <section v-if="duocCapNhat" class="khoi khoi-upload">
      <h3 class="khoi-title">Cập nhật tài liệu syllabus môn</h3>
      <p class="upload-note">
        Chọn tệp để thêm/cập nhật tài liệu cho syllabus môn này. Dùng đúng dịch vụ tải lên hiện có của hệ thống.
      </p>
      <div class="upload-row">
        <input ref="inputTep" type="file" @change="chonTep" />
        <button type="button" class="btn btn-upload" :disabled="!tepChon || dangTaiLen" @click="taiLenTep">
          {{ dangTaiLen ? 'Đang tải lên...' : 'Tải tệp lên' }}
        </button>
      </div>
      <p v-if="thongBaoUpload" class="upload-msg" :class="loaiThongBaoUpload">{{ thongBaoUpload }}</p>
    </section>

    <!-- Modal xem tệp trong trang -->
    <div v-if="dangMoModal" class="modal" @click.self="dongModal">
      <div class="modal-box">
        <div class="modal-head">
          <span class="modal-title">{{ tenTepDangXem }}</span>
          <div class="modal-actions">
            <button v-if="previewUrl" type="button" class="btn btn-xem" @click="moTabMoi">Mở tab mới</button>
            <button type="button" class="btn btn-tai" @click="dongModal">Đóng</button>
          </div>
        </div>
        <div class="modal-body">
          <p v-if="dangTaiTep" class="trong-text">Đang tải tệp...</p>
          <p v-else-if="loiPreview" class="upload-msg error">{{ loiPreview }}</p>
          <img v-else-if="loaiPreview === 'image'" :src="previewUrl" alt="preview" class="preview-img" />
          <video v-else-if="loaiPreview === 'video'" :src="previewUrl" controls class="preview-media" />
          <audio v-else-if="loaiPreview === 'audio'" :src="previewUrl" controls class="preview-audio" />
          <iframe v-else-if="previewUrl" :src="previewUrl" class="preview-frame" title="preview" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, ref, onBeforeUnmount } from 'vue'
import apiClient from '@/core/api/apiClient'
import { ENV } from '@/core/config/env'
import { tepDinhKemUploadService } from '@/modules/daoTao/services/tepDinhKemUploadService'
import { layThongBaoLoi } from '@/modules/daoTao/utils/layThongBaoLoi'

const props = defineProps({
  syllabusMonId: { type: [String, Number], default: null },
  cheDoXem: { type: String, default: 'sinhVien' },
  taiLieuData: {
    type: Object,
    default: () => ({ syllabusTaiLieu: [], syllabusMonHocTaiLieu: [], taiLieuGoc: [] })
  }
})

const emit = defineEmits(['reload'])

const duocCapNhat = computed(() =>
  ['giangVien', 'daoTao', 'admin'].includes(props.cheDoXem)
)

const syllabusTaiLieu = computed(() => props.taiLieuData?.syllabusTaiLieu || [])

// map syllabusMonHocTaiLieu với taiLieuGoc để lấy tên/thông tin gốc
const taiLieuNoiMap = computed(() => {
  const goc = props.taiLieuData?.taiLieuGoc || []
  return (props.taiLieuData?.syllabusMonHocTaiLieu || []).map(item => {
    const gocItem =
      goc.find(g => String(g.id || '') === String(item.taiLieuGocId || '')) || {}
    return {
      ...item,
      tenTaiLieuGoc: item.tenTaiLieuGoc || gocItem.ten || gocItem.tenTaiLieuGoc || gocItem.tenFile || '-',
      loaiTaiLieu: item.loaiTaiLieu || gocItem.loaiTaiLieu,
      tacGia: item.tacGia || gocItem.tacGia,
      namXuatBan: item.namXuatBan || gocItem.namXuatBan,
      nhaXuatBan: item.nhaXuatBan || gocItem.nhaXuatBan,
      duongDan: item.duongDan || gocItem.duongDan
    }
  })
})

/* ---------- Tệp đính kèm ---------- */

function coTep(row) {
  return Boolean(row?.tepDinhKemId || row?.duongDan)
}

function coDuongDanTai(row) {
  return Boolean(taoDuongDanTai(row))
}

function layBackendOrigin() {
  return String(ENV.API_BASE_URL || '')
    .replace(/\/api\/?$/, '')
    .replace(/\/$/, '')
}

function taoDuongDanTai(row) {
  return tepDinhKemUploadService.taoDuongDanTaiFile({
    id: row?.tepDinhKemId,
    tepDinhKemId: row?.tepDinhKemId,
    duongDanDayDu: row?.duongDan,
    duongDanTuongDoi: row?.duongDan
  })
}

function taoPreviewApiPath(row) {
  if (row?.tepDinhKemId) {
    return `/he-thong/tep-dinh-kem/${row.tepDinhKemId}/preview`
  }
  const duongDan = String(row?.duongDan || '')
  if (duongDan.startsWith('/api/')) {
    return duongDan.replace(/^\/api/, '').replace(/\/download(?=($|[?#]))/, '/preview')
  }
  return duongDan.replace(/\/download(?=($|[?#]))/, '/preview')
}

function layExtension(row) {
  const source = [row?.duongDan, row?.ten, row?.tenTaiLieuGoc].filter(Boolean).join(' ')
  const clean = String(source).split('?')[0].toLowerCase()
  const match = clean.match(/\.([a-z0-9]+)(?:$|[/?#\s])/)
  return match?.[1] || ''
}

function xacDinhLoaiPreview(row, blob) {
  const contentType = String(blob?.type || '').toLowerCase()
  const ext = layExtension(row)
  if (contentType.startsWith('image/') || ['jpg', 'jpeg', 'png', 'webp', 'gif', 'bmp', 'svg'].includes(ext)) return 'image'
  if (contentType.startsWith('video/') || ['mp4', 'mov', 'avi', 'mkv', 'webm'].includes(ext)) return 'video'
  if (contentType.startsWith('audio/') || ['mp3', 'wav', 'm4a', 'ogg'].includes(ext)) return 'audio'
  return 'document'
}

/* ---------- Modal preview ---------- */

const dangMoModal = ref(false)
const dangTaiTep = ref(false)
const previewUrl = ref('')
const loaiPreview = ref('')
const loiPreview = ref('')
const tenTepDangXem = ref('')

function xoaPreviewUrl() {
  if (previewUrl.value && previewUrl.value.startsWith('blob:')) {
    URL.revokeObjectURL(previewUrl.value)
  }
  previewUrl.value = ''
}

async function xemTep(row) {
  dangMoModal.value = true
  dangTaiTep.value = true
  loiPreview.value = ''
  loaiPreview.value = ''
  tenTepDangXem.value = row?.ten || row?.tenTaiLieuGoc || 'Tệp tài liệu'
  xoaPreviewUrl()

  try {
    const apiPath = taoPreviewApiPath(row)
    const blob = await apiClient.get(apiPath, { responseType: 'blob' })
    loaiPreview.value = xacDinhLoaiPreview(row, blob)
    previewUrl.value = URL.createObjectURL(blob)
  } catch (error) {
    loiPreview.value = layThongBaoLoi(error, 'Không xem được tệp trong trang.')
  } finally {
    dangTaiTep.value = false
  }
}

function moTabMoi() {
  if (previewUrl.value) window.open(previewUrl.value, '_blank')
}

function dongModal() {
  dangMoModal.value = false
  xoaPreviewUrl()
}

function taiTep(row) {
  const url = taoDuongDanTai(row)
  if (url) window.open(url, '_blank')
}

/* ---------- Upload ---------- */

const inputTep = ref(null)
const tepChon = ref(null)
const dangTaiLen = ref(false)
const thongBaoUpload = ref('')
const loaiThongBaoUpload = ref('')

function chonTep(event) {
  tepChon.value = event.target.files?.[0] || null
  thongBaoUpload.value = ''
}

async function taiLenTep() {
  if (!tepChon.value || !props.syllabusMonId) return

  dangTaiLen.value = true
  thongBaoUpload.value = ''

  try {
    await tepDinhKemUploadService.upload({
      file: tepChon.value,
      module: 'CHUONG_TRINH',
      nghiepVu: 'SYLLABUS_MON_HOC_TAI_LIEU',
      doiTuongId: props.syllabusMonId,
      syllabusMonHocId: props.syllabusMonId
    })

    thongBaoUpload.value = 'Tải tệp lên thành công.'
    loaiThongBaoUpload.value = 'success'
    tepChon.value = null
    if (inputTep.value) inputTep.value.value = ''
    emit('reload')
  } catch (error) {
    thongBaoUpload.value = layThongBaoLoi(error, 'Không tải được tệp lên.')
    loaiThongBaoUpload.value = 'error'
  } finally {
    dangTaiLen.value = false
  }
}

function dinhDang(value) {
  if (value === null || value === undefined || value === '') return '-'
  if (typeof value === 'boolean') return value ? 'Có' : 'Không'
  return value
}

onBeforeUnmount(() => xoaPreviewUrl())
</script>

<style scoped>
.khoi {
  background: #ffffff;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  padding: 16px;
  margin-bottom: 16px;
}
.khoi-title {
  margin: 0 0 12px;
  font-size: 15px;
  font-weight: 700;
  color: #1e293b;
}
.bang-wrap {
  overflow-x: auto;
}
.bang {
  width: 100%;
  border-collapse: collapse;
  font-size: 13px;
}
.bang th,
.bang td {
  border: 1px solid #e2e8f0;
  padding: 8px 10px;
  text-align: left;
  vertical-align: top;
}
.bang th {
  background: #f1f5f9;
  color: #475569;
  font-weight: 600;
  white-space: nowrap;
}
.duong-dan {
  max-width: 220px;
  word-break: break-all;
}
.cot-thao-tac {
  white-space: nowrap;
}
.trong-text {
  margin: 0;
  color: #94a3b8;
  font-size: 13px;
}
.btn {
  border: none;
  border-radius: 8px;
  padding: 6px 12px;
  font-size: 13px;
  cursor: pointer;
  margin-right: 6px;
}
.btn-xem {
  background: #e0e7ff;
  color: #4338ca;
}
.btn-tai {
  background: #dcfce7;
  color: #15803d;
}
.btn-upload {
  background: #6366f1;
  color: #ffffff;
}
.btn-upload:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}
.khong-tep {
  color: #cbd5e1;
}
.khoi-upload {
  border-color: #c7d2fe;
  background: #eef2ff;
}
.upload-note {
  margin: 0 0 10px;
  font-size: 13px;
  color: #64748b;
}
.upload-row {
  display: flex;
  align-items: center;
  gap: 10px;
  flex-wrap: wrap;
}
.upload-msg {
  margin: 10px 0 0;
  font-size: 13px;
}
.upload-msg.success,
.success {
  color: #15803d;
}
.upload-msg.error,
.error {
  color: #dc2626;
}
.modal {
  position: fixed;
  inset: 0;
  background: rgba(15, 23, 42, 0.55);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  padding: 24px;
}
.modal-box {
  background: #ffffff;
  border-radius: 12px;
  width: min(960px, 100%);
  height: min(80vh, 100%);
  display: flex;
  flex-direction: column;
  overflow: hidden;
}
.modal-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 16px;
  border-bottom: 1px solid #e2e8f0;
}
.modal-title {
  font-weight: 600;
  color: #1e293b;
}
.modal-body {
  flex: 1;
  overflow: auto;
  padding: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
}
.preview-frame {
  width: 100%;
  height: 100%;
  border: none;
}
.preview-img {
  max-width: 100%;
  max-height: 100%;
}
.preview-media {
  max-width: 100%;
  max-height: 100%;
}
.preview-audio {
  width: 100%;
}
</style>
