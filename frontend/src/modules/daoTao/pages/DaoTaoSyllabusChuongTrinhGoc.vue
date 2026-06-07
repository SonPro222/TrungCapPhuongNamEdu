<template>
  <div class="dao-tao-syllabus-chuong-trinh-goc">
    <div v-if="thongBao" :class="['thong-bao', loaiThongBao]">
      {{ thongBao }}
    </div>

    <section class="khoi-dau">
      <div>
        <h2>Quản lý Syllabus chương trình gốc / mẫu</h2>
        <p>
          CRUD syllabus chương trình dùng lại cho nhiều ngành/chương trình/version. Khi vào tầng 5 Ngành / Chương trình chỉ chọn mẫu này để đưa dữ liệu vào khung áp dụng.
        </p>
      </div>

      <button type="button" class="nut" :disabled="dangTai" @click="taiDuLieu">
        Tải lại dữ liệu
      </button>
    </section>

    <section class="the bo-loc-tang">
      <div class="tieu-de-the">
        <div>
          <h3>Bộ lọc tái sử dụng</h3>
          <p>Lọc theo Ngành → Chương trình → Version để tìm mẫu gốc đang được dùng.</p>
        </div>
      </div>

      <div class="bo-loc">
        <label>
          <span>Ngành</span>
          <select v-model="boLoc.nganhId" @change="doiNganh">
            <option value="">Tất cả ngành</option>
            <option v-for="nganh in danhSachNganh" :key="nganh.id" :value="nganh.id">
              {{ nganh.maNganh || nganh.ma || '' }} - {{ nganh.tenNganh || nganh.ten || nganh.tenNganhDaoTao || nganh.id }}
            </option>
          </select>
        </label>

        <label>
          <span>Chương trình</span>
          <select v-model="boLoc.chuongTrinhId" :disabled="!boLoc.nganhId" @change="doiChuongTrinh">
            <option value="">Tất cả chương trình</option>
            <option v-for="ct in danhSachChuongTrinhTheoNganh" :key="ct.id" :value="ct.id">
              {{ ct.maChuongTrinh || ct.ma || '' }} - {{ ct.tenChuongTrinh || ct.ten || ct.id }}
            </option>
          </select>
        </label>

        <label>
          <span>Version</span>
          <select v-model="boLoc.versionId" :disabled="!boLoc.chuongTrinhId">
            <option value="">Tất cả version</option>
            <option v-for="version in danhSachVersionTheoChuongTrinh" :key="version.id" :value="version.id">
              {{ version.maVersion || version.ma || '' }} - {{ version.tenVersion || version.ten || version.id }}
            </option>
          </select>
        </label>

      </div>
    </section>

    <section class="luoi-chinh">
      <div class="the">
        <div class="tieu-de-the">
          <div>
            <h3>{{ form.id ? 'Cập nhật syllabus chương trình gốc' : 'Thêm syllabus chương trình gốc' }}</h3>
          </div>
        </div>

        <form class="form-syllabus" @submit.prevent="luuSyllabus">
          <label>
            <span>Mã <b>*</b></span>
            <input
                v-model.trim="form.ma"
                :class="{ 'co-loi': loiForm.ma }"
                type="text"
                maxlength="100"
                placeholder="vd: syl-ct-btscmb-tc-2026"
                @input="xoaLoi('ma')"
            >
            <small v-if="loiForm.ma" class="loi-truong">{{ loiForm.ma }}</small>
          </label>

          <label>
            <span>Tên <b>*</b></span>
            <input
                v-model.trim="form.ten"
                :class="{ 'co-loi': loiForm.ten }"
                type="text"
                maxlength="255"
                placeholder="Tên syllabus chương trình gốc/mẫu"
                @input="xoaLoi('ten')"
            >
            <small v-if="loiForm.ten" class="loi-truong">{{ loiForm.ten }}</small>
          </label>

          <label class="cot-rong">
            <span>Mục tiêu</span>
            <textarea v-model.trim="form.mucTieu" rows="4"></textarea>
          </label>

          <label class="cot-rong">
            <span>Đối tượng tuyển sinh</span>
            <textarea v-model.trim="form.doiTuongTuyenSinh" rows="3"></textarea>
          </label>

          <label>
            <span>Thời gian đào tạo</span>
            <input v-model.trim="form.thoiGianDaoTao" type="text" placeholder="vd: 1.5 năm">
          </label>

          <label>
            <span>Khối lượng kiến thức</span>
            <input v-model.trim="form.khoiLuongKienThuc" type="text" placeholder="vd: 65 tín chỉ / 1593 giờ">
          </label>

          <label class="cot-rong">
            <span>Điều kiện tốt nghiệp</span>
            <textarea v-model.trim="form.dieuKienTotNghiep" rows="3"></textarea>
          </label>

          <label class="cot-rong">
            <span>Phương pháp đào tạo</span>
            <textarea v-model.trim="form.phuongPhapDaoTao" rows="3"></textarea>
          </label>

          <label class="cot-rong">
            <span>Phương pháp đánh giá</span>
            <textarea v-model.trim="form.phuongPhapDanhGia" rows="3"></textarea>
          </label>

          <label class="cot-rong">
            <span>Hướng dẫn thực hiện</span>
            <textarea v-model.trim="form.huongDanThucHien" rows="3"></textarea>
          </label>

          <label class="cot-rong">
            <span>Ghi chú</span>
            <textarea v-model.trim="form.ghiChu" rows="3"></textarea>
          </label>

          <div class="cot-rong khoi-tep">
            <label>
              <span>Đường dẫn tệp đã lưu</span>
              <textarea v-model.trim="form.duongDan" rows="3" placeholder="Tự sinh sau khi upload file; có thể chứa nhiều link hoặc JSON tệp đính kèm."></textarea>
            </label>

            <div class="hang-nut">
              <input ref="fileInput" type="file" multiple @change="chonTep">
              <button type="button" class="nut" :disabled="dangUpload || !danhSachFile.length" @click="luuTep">
                {{ dangUpload ? 'Đang lưu tệp...' : 'Lưu tệp' }}
              </button>
              <button type="button" class="nut" :disabled="!form.id" @click="xemTep(form)">
                Xem tệp
              </button>
            </div>

            <small v-if="danhSachFile.length">Đã chọn {{ danhSachFile.length }} tệp. Bấm “Lưu tệp” để upload và đưa link vào ô đường dẫn.</small>
          </div>

          <div class="hang-nut cot-rong">
            <button type="submit" class="nut chinh" :disabled="dangLuu">
              {{ form.id ? 'Cập nhật syllabus' : 'Thêm syllabus' }}
            </button>

            <button type="button" class="nut" :disabled="dangLuu" @click="resetForm">
              Làm mới form
            </button>
          </div>
        </form>
      </div>

      <div class="the">
        <div class="tieu-de-the">
          <div>
            <h3>Danh sách syllabus chương trình gốc / mẫu</h3>
            <p>Đang hiển thị: {{ danhSachSyllabusLoc.length }} / {{ danhSachSyllabusGoc.length }}</p>
          </div>
        </div>

        <div class="bo-loc bo-loc-nho">
          <label>
            <span>Tìm kiếm</span>
            <input v-model.trim="tuKhoa" type="text" placeholder="Tìm mã, tên, mục tiêu, ghi chú...">
          </label>

          <label>
            <span>Mã syllabus</span>
            <input v-model.trim="maLoc" type="text" placeholder="Lọc theo mã">
          </label>
        </div>

        <div class="bang-boc">
          <table>
            <thead>
            <tr>
              <th>Mã</th>
              <th>Tên</th>
              <th>Thông tin chính</th>
              <th>Tệp</th>
              <th class="cot-thao-tac">Thao tác</th>
            </tr>
            </thead>

            <tbody>
            <tr v-if="!danhSachSyllabusLoc.length">
              <td colspan="5" class="rong">Chưa có syllabus chương trình gốc phù hợp bộ lọc.</td>
            </tr>

            <tr
                v-for="item in danhSachSyllabusLoc"
                :key="item.id"
                :class="{ 'dang-chon': String(item.id || '') === String(form.id || '') }"
            >
              <td>
                <b>{{ item.ma || '-' }}</b>
                <small>ID: {{ item.id }}</small>
              </td>

              <td>{{ item.ten || '-' }}</td>

              <td>
                <div><b>Thời gian:</b> {{ item.thoiGianDaoTao || '-' }}</div>
                <div><b>Khối lượng:</b> {{ item.khoiLuongKienThuc || '-' }}</div>
                <small>{{ rutGon(item.mucTieu || item.ghiChu, 120) }}</small>
              </td>

              <td>
                <button type="button" class="nut nho" :disabled="!item.id" @click="xemTep(item)">
                  Xem tệp
                </button>
              </td>

              <td class="thao-tac">
                <button type="button" class="nut nho chinh" @click="suaSyllabus(item)">
                  Sửa
                </button>

                <button type="button" class="nut nho nguy-hiem" :disabled="dangLuu" @click="xoaSyllabus(item)">
                  Xóa
                </button>
              </td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { computed, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useDaoTaoXemChuongTrinh } from '@/modules/daoTao/composables/useDaoTaoXemChuongTrinh'
import { tepDinhKemUploadService } from '@/modules/daoTao/services/tepDinhKemUploadService'
import { layThongBaoLoi } from '@/modules/daoTao/utils/layThongBaoLoi'

const router = useRouter()

const {
  duLieu,
  services,
  taiDuLieuCoSanTatCaBang
} = useDaoTaoXemChuongTrinh()

const thongBao = ref('')
const loaiThongBao = ref('success')
const dangLuu = ref(false)
const dangTai = ref(false)
const dangUpload = ref(false)
const tuKhoa = ref('')
const maLoc = ref('')
const fileInput = ref(null)
const danhSachFile = ref([])

const boLoc = reactive({
  nganhId: '',
  chuongTrinhId: '',
  versionId: ''
})

const form = reactive({
  id: null,
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
  ghiChu: '',
  duongDan: ''
})

const loiForm = reactive({
  ma: '',
  ten: ''
})

const danhSachNganh = computed(() => [...(duLieu.value.nganh || [])]
    .sort((a, b) => String(a.tenNganh || a.ten || '').localeCompare(String(b.tenNganh || b.ten || ''), 'vi')))

const danhSachChuongTrinhTheoNganh = computed(() => {
  return (duLieu.value.chuongTrinh || [])
      .filter((ct) => !boLoc.nganhId || String(ct.nganhId || '') === String(boLoc.nganhId))
      .sort((a, b) => String(a.tenChuongTrinh || a.ten || '').localeCompare(String(b.tenChuongTrinh || b.ten || ''), 'vi'))
})

const danhSachVersionTheoChuongTrinh = computed(() => {
  return (duLieu.value.chuongTrinhVersion || [])
      .filter((version) => !boLoc.chuongTrinhId || String(version.chuongTrinhId || '') === String(boLoc.chuongTrinhId))
      .sort((a, b) => String(a.maVersion || a.tenVersion || '').localeCompare(String(b.maVersion || b.tenVersion || ''), 'vi'))
})

const idGocDuocLocTheoTang = computed(() => {
  const ids = new Set()

  if (!boLoc.nganhId && !boLoc.chuongTrinhId && !boLoc.versionId) {
    return null
  }

  const chuongTrinhHopLe = new Set(danhSachChuongTrinhTheoNganh.value.map((item) => String(item.id)))
  const versionHopLe = new Set(danhSachVersionTheoChuongTrinh.value.map((item) => String(item.id)))

  ;(duLieu.value.syllabusChuongTrinh || []).forEach((syllabus) => {
    if (boLoc.versionId) {
      if (String(syllabus.chuongTrinhVersionId || '') !== String(boLoc.versionId)) return
    } else if (boLoc.chuongTrinhId) {
      if (!versionHopLe.has(String(syllabus.chuongTrinhVersionId || ''))) return
    } else if (boLoc.nganhId) {
      const version = (duLieu.value.chuongTrinhVersion || []).find((item) => String(item.id || '') === String(syllabus.chuongTrinhVersionId || ''))
      if (!version || !chuongTrinhHopLe.has(String(version.chuongTrinhId || ''))) return
    }

    if (syllabus.syllabusChuongTrinhGocId) ids.add(String(syllabus.syllabusChuongTrinhGocId))
  })

  return ids
})

const danhSachSyllabusGoc = computed(() => [...(duLieu.value.syllabusChuongTrinhGoc || [])]
    .sort((a, b) => String(a.ten || '').localeCompare(String(b.ten || ''), 'vi')))

const danhSachSyllabusLoc = computed(() => {
  const keyword = tuKhoa.value.trim().toLowerCase()
  const ma = maLoc.value.trim().toLowerCase()
  const idsTheoTang = idGocDuocLocTheoTang.value

  return danhSachSyllabusGoc.value.filter((item) => {
    if (idsTheoTang && !idsTheoTang.has(String(item.id || ''))) return false
    if (ma && !String(item.ma || '').toLowerCase().includes(ma)) return false

    if (!keyword) return true

    const text = [
      item.ma,
      item.ten,
      item.mucTieu,
      item.doiTuongTuyenSinh,
      item.thoiGianDaoTao,
      item.khoiLuongKienThuc,
      item.dieuKienTotNghiep,
      item.phuongPhapDaoTao,
      item.phuongPhapDanhGia,
      item.huongDanThucHien,
      item.ghiChu,
      item.duongDan
    ].filter(Boolean).join(' ').toLowerCase()

    return text.includes(keyword)
  })
})

function baoTin(message, type = 'success') {
  thongBao.value = message
  loaiThongBao.value = type

  setTimeout(() => {
    if (thongBao.value === message) thongBao.value = ''
  }, 5000)
}

async function taiDuLieu() {
  dangTai.value = true

  try {
    await taiDuLieuCoSanTatCaBang()
    baoTin('Đã tải lại dữ liệu syllabus chương trình gốc.')
  } catch (error) {
    baoTin(layThongBaoLoi(error, 'Không tải được dữ liệu syllabus chương trình gốc.'), 'error')
  } finally {
    dangTai.value = false
  }
}

function doiNganh() {
  boLoc.chuongTrinhId = ''
  boLoc.versionId = ''
}

function doiChuongTrinh() {
  boLoc.versionId = ''
}


function resetForm() {
  Object.assign(form, {
    id: null,
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
    ghiChu: '',
    duongDan: ''
  })
  danhSachFile.value = []
  xoaLoi()
  if (fileInput.value) fileInput.value.value = ''
}

function suaSyllabus(item) {
  Object.assign(form, {
    id: item.id || null,
    ma: item.ma || '',
    ten: item.ten || '',
    mucTieu: item.mucTieu || '',
    doiTuongTuyenSinh: item.doiTuongTuyenSinh || '',
    thoiGianDaoTao: item.thoiGianDaoTao || '',
    khoiLuongKienThuc: item.khoiLuongKienThuc || '',
    dieuKienTotNghiep: item.dieuKienTotNghiep || '',
    phuongPhapDaoTao: item.phuongPhapDaoTao || '',
    phuongPhapDanhGia: item.phuongPhapDanhGia || '',
    huongDanThucHien: item.huongDanThucHien || '',
    ghiChu: item.ghiChu || '',
    duongDan: chuanHoaDuongDan(item.duongDan || '')
  })
}


function giaTriTrim(value) {
  return String(value || '').trim()
}

function xoaLoi(field) {
  if (field && Object.prototype.hasOwnProperty.call(loiForm, field)) {
    loiForm[field] = ''
    return
  }

  Object.keys(loiForm).forEach((key) => {
    loiForm[key] = ''
  })
}

function validateSyllabusForm() {
  xoaLoi()

  const ma = giaTriTrim(form.ma)
  const ten = giaTriTrim(form.ten)

  if (!ma) {
    loiForm.ma = 'Cần nhập mã syllabus chương trình gốc.'
  } else if (ma.length > 100) {
    loiForm.ma = 'Mã syllabus chương trình gốc tối đa 100 ký tự.'
  } else {
    const trungMa = danhSachSyllabusGoc.value.some((item) => {
      return String(item.ma || '').trim().toLowerCase() === ma.toLowerCase()
          && String(item.id || '') !== String(form.id || '')
    })

    if (trungMa) {
      loiForm.ma = `Mã syllabus chương trình gốc đã tồn tại: ${ma}`
    }
  }

  if (!ten) {
    loiForm.ten = 'Cần nhập tên syllabus chương trình gốc.'
  } else if (ten.length > 255) {
    loiForm.ten = 'Tên syllabus chương trình gốc tối đa 255 ký tự.'
  }

  const loiDauTien = Object.values(loiForm).find(Boolean)
  if (loiDauTien) {
    baoTin(loiDauTien, 'error')
    return false
  }

  return true
}

function giaTriHoacNull(value) {
  const text = giaTriTrim(value)
  return text || null
}

function taoPayload() {
  return {
    ma: giaTriTrim(form.ma),
    ten: giaTriTrim(form.ten),
    mucTieu: giaTriHoacNull(form.mucTieu),
    doiTuongTuyenSinh: giaTriHoacNull(form.doiTuongTuyenSinh),
    thoiGianDaoTao: giaTriHoacNull(form.thoiGianDaoTao),
    khoiLuongKienThuc: giaTriHoacNull(form.khoiLuongKienThuc),
    dieuKienTotNghiep: giaTriHoacNull(form.dieuKienTotNghiep),
    phuongPhapDaoTao: giaTriHoacNull(form.phuongPhapDaoTao),
    phuongPhapDanhGia: giaTriHoacNull(form.phuongPhapDanhGia),
    huongDanThucHien: giaTriHoacNull(form.huongDanThucHien),
    ghiChu: giaTriHoacNull(form.ghiChu),
    duongDan: giaTriHoacNull(form.duongDan)
  }
}

async function luuSyllabus() {
  if (!validateSyllabusForm()) return

  dangLuu.value = true

  try {
    if (form.id) {
      await services.syllabusChuongTrinhGoc.update(form.id, taoPayload())
      baoTin('Đã cập nhật syllabus chương trình gốc.')
    } else {
      await services.syllabusChuongTrinhGoc.create(taoPayload())
      baoTin('Đã thêm syllabus chương trình gốc.')
    }

    await taiDuLieuCoSanTatCaBang()
    resetForm()
  } catch (error) {
    baoTin(layThongBaoLoi(error, 'Không lưu được syllabus chương trình gốc.'), 'error')
  } finally {
    dangLuu.value = false
  }
}

async function xoaSyllabus(item) {
  if (!item?.id) return

  const dangDung = (duLieu.value.syllabusChuongTrinh || []).some((syllabus) => String(syllabus.syllabusChuongTrinhGocId || '') === String(item.id))
  const noiDungCanhBao = dangDung
      ? `Syllabus gốc "${item.ten || item.ma || item.id}" đang được version sử dụng. Vẫn xóa?`
      : `Xóa syllabus gốc "${item.ten || item.ma || item.id}"?`

  if (!window.confirm(noiDungCanhBao)) return

  dangLuu.value = true

  try {
    await services.syllabusChuongTrinhGoc.delete(item.id)
    await taiDuLieuCoSanTatCaBang()
    if (String(form.id || '') === String(item.id || '')) resetForm()
    baoTin('Đã xóa syllabus chương trình gốc.')
  } catch (error) {
    baoTin(layThongBaoLoi(error, 'Không xóa được. Kiểm tra syllabus gốc có đang được chương trình sử dụng không.'), 'error')
  } finally {
    dangLuu.value = false
  }
}

function chonTep(event) {
  danhSachFile.value = Array.from(event.target.files || [])
}

async function luuTep() {
  if (!danhSachFile.value.length) return baoTin('Cần chọn tệp trước khi lưu.', 'error')

  dangUpload.value = true

  try {
    const danhSachFileInfo = await tepDinhKemUploadService.uploadMany({
      files: danhSachFile.value,
      module: 'DAO_TAO',
      nghiepVu: 'SYLLABUS_CHUONG_TRINH_GOC',
      doiTuongId: form.id || null,
      moTa: `Tệp syllabus chương trình gốc ${form.ma || form.ten || ''}`.trim(),
      tangNghiepVu: 'GOC'
    })

    const duongDanMoi = danhSachFileInfo
        .map((fileInfo, index) => tepDinhKemUploadService.taoDuongDanTaiFile(fileInfo) || danhSachFile.value[index]?.name || '')
        .filter(Boolean)

    const hienTai = tachDuongDan(form.duongDan)
    form.duongDan = Array.from(new Set([...hienTai, ...duongDanMoi])).join('\n')

    if (form.id) {
      await services.syllabusChuongTrinhGoc.update(form.id, taoPayload())
      await taiDuLieuCoSanTatCaBang()
      baoTin(`Đã upload và lưu ${duongDanMoi.length} tệp vào syllabus.`)
    } else {
      baoTin(`Đã upload ${duongDanMoi.length} tệp. Bấm “Thêm syllabus” để lưu đường dẫn vào bản ghi.`)
    }

    danhSachFile.value = []
    if (fileInput.value) fileInput.value.value = ''
  } catch (error) {
    baoTin(layThongBaoLoi(error, 'Không upload/lưu được tệp.'), 'error')
  } finally {
    dangUpload.value = false
  }
}

function chuanHoaDuongDan(duongDan) {
  if (!duongDan) return ''

  if (typeof duongDan !== 'string') return String(duongDan || '')

  const value = duongDan.trim()
  if (!value) return ''

  try {
    const parsed = JSON.parse(value)
    if (Array.isArray(parsed)) {
      return parsed.map((item) => item?.duongDan || item?.url || item?.downloadUrl || '').filter(Boolean).join('\n') || value
    }
  } catch (error) {
    return value
  }

  return value
}

function tachDuongDan(duongDan) {
  const value = chuanHoaDuongDan(duongDan)
  return String(value || '').split(/\r?\n/).map((item) => item.trim()).filter(Boolean)
}

function xemTep(item) {
  if (!item?.id) {
    baoTin('Cần lưu syllabus trước khi xem tệp bằng trang xem tệp chung.', 'error')
    return
  }

  router.push({
    name: 'DaoTao.XemChuongTrinh.SyllabusTep',
    params: {
      nganhId: boLoc.nganhId || 0,
      chuongTrinhId: boLoc.chuongTrinhId || 0,
      versionId: boLoc.versionId || 0,
      chuongTrinhMonId: 0
    },
    query: {
      tepNguon: 'syllabusChuongTrinhGoc',
      syllabusChuongTrinhGocId: item.id
    }
  })
}

function rutGon(value, max = 100) {
  const text = String(value || '').trim()
  if (text.length <= max) return text
  return `${text.slice(0, max)}...`
}
</script>

<style scoped>
.dao-tao-syllabus-chuong-trinh-goc {
  display: grid;
  gap: 14px;
  font-family: Roboto, Arial, sans-serif;
  color: #111827;
}

.khoi-dau,
.tieu-de-the {
  display: flex;
  justify-content: space-between;
  gap: 12px;
  align-items: flex-start;
}

.khoi-dau {
  padding: 14px;
  border: 1px solid #d1d5db;
  border-radius: 6px;
  background: #ffffff;
}

h2,
h3,
p {
  margin: 0;
}

h2 {
  font-size: 20px;
}

h3 {
  font-size: 16px;
}

p,
small {
  color: #64748b;
  line-height: 1.5;
}

.luoi-chinh {
  display: grid;
  grid-template-columns: minmax(380px, 0.95fr) minmax(520px, 1.25fr);
  gap: 14px;
  align-items: start;
}

.the {
  padding: 14px;
  border: 1px solid #d1d5db;
  border-radius: 6px;
  background: #ffffff;
}

.form-syllabus,
.bo-loc {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 10px;
  margin-top: 12px;
}

.bo-loc-tang .bo-loc {
  grid-template-columns: repeat(3, minmax(0, 1fr));
}

.bo-loc-nho {
  grid-template-columns: 1.5fr 1fr;
}

label {
  display: grid;
  gap: 5px;
  font-size: 12px;
  font-weight: 700;
  color: #374151;
}

label b {
  color: #dc2626;
}

input,
select,
textarea {
  width: 100%;
  box-sizing: border-box;
  border: 1px solid #cbd5e1;
  border-radius: 4px;
  padding: 7px 9px;
  font: inherit;
  font-size: 13px;
  background: #ffffff;
}

textarea {
  resize: vertical;
}

input.co-loi,
textarea.co-loi,
select.co-loi {
  border-color: #dc2626;
  background: #fff1f2;
}

.loi-truong {
  color: #dc2626;
  font-weight: 500;
}

.cot-rong {
  grid-column: 1 / -1;
}

.khoi-tep {
  display: grid;
  gap: 8px;
  padding: 10px;
  border: 1px dashed #cbd5e1;
  border-radius: 6px;
  background: #f8fafc;
}

.hang-nut {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  align-items: center;
}

.bang-boc {
  margin-top: 12px;
  max-height: 680px;
  overflow: auto;
  border: 1px solid #e5e7eb;
  border-radius: 6px;
}

table {
  width: 100%;
  border-collapse: collapse;
  font-size: 12px;
}

th,
td {
  border-top: 1px solid #e5e7eb;
  padding: 8px 10px;
  text-align: left;
  vertical-align: top;
}

th {
  position: sticky;
  top: 0;
  z-index: 5;
  background: #f3f4f6;
  color: #374151;
  font-size: 12px;
  font-weight: 700;
}

td small {
  display: block;
  margin-top: 3px;
}

tr.dang-chon td {
  background: #eff6ff;
}

.rong {
  color: #6b7280;
  text-align: center;
}

.cot-thao-tac {
  width: 130px;
}

.thao-tac {
  display: flex;
  flex-wrap: wrap;
  gap: 5px;
}

.nut {
  min-height: 30px;
  border: 1px solid #cbd5e1;
  border-radius: 4px;
  background: #ffffff;
  color: #111827;
  padding: 5px 10px;
  font-size: 12px;
  cursor: pointer;
  font-family: Roboto, Arial, sans-serif;
}

.nut:hover {
  background: #f8fafc;
}

.nut:disabled {
  cursor: not-allowed;
  opacity: 0.65;
}

.nut.chinh {
  border-color: #2563eb;
  background: #2563eb;
  color: #ffffff;
}

.nut.nguy-hiem {
  border-color: #fecaca;
  background: #fff1f2;
  color: #b91c1c;
}

.nut.nho {
  min-height: 26px;
  padding: 3px 8px;
  font-size: 11px;
}

.thong-bao {
  padding: 10px 12px;
  border-radius: 6px;
  border: 1px solid #bbf7d0;
  background: #f0fdf4;
  color: #166534;
  font-size: 13px;
  font-weight: 600;
}

.thong-bao.error {
  border-color: #fecaca;
  background: #fff1f2;
  color: #b91c1c;
}

@media (max-width: 1200px) {
  .luoi-chinh,
  .bo-loc-tang .bo-loc {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 700px) {
  .khoi-dau,
  .tieu-de-the {
    flex-direction: column;
  }

  .form-syllabus,
  .bo-loc,
  .bo-loc-nho {
    grid-template-columns: 1fr;
  }
}
</style>
