<template>
  <div class="dao-tao-mau-goc-crud">
    <div v-if="thongBao" :class="['thong-bao', loaiThongBao]">
      {{ thongBao }}
    </div>

    <section class="khoi-dau">
      <div>
        <h2>{{ config.tieuDeTrang }}</h2>
        <p>{{ config.moTaTrang }}</p>
      </div>

      <button type="button" class="nut" :disabled="dangTai" @click="taiDuLieu">
        Tải lại dữ liệu
      </button>
    </section>

    <section class="the bo-loc-tang">
      <div class="tieu-de-the">
        <div>
          <h3>{{ canLocVersion ? 'Bộ lọc tái sử dụng' : 'Bộ lọc chương trình' }}</h3>
          <p v-if="canLocVersion">Lọc theo Ngành → Chương trình → Version để xem dữ liệu đang liên quan đến version.</p>
          <p v-else>Chọn Ngành → Chương trình để quản lý dữ liệu gốc/mẫu thuộc chương trình.</p>
        </div>
      </div>

      <div :class="['bo-loc', { 'bo-loc-co-version': canLocVersion }]">
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

        <label v-if="canLocVersion">
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
            <h3>{{ form.id ? config.tieuDeCapNhat : config.tieuDeThem }}</h3>
            <p v-if="canGanChuongTrinh">Dùng Bộ lọc chương trình phía trên để chọn Ngành → Chương trình. Khi bấm thêm/cập nhật, dữ liệu sẽ thuộc chương trình đang chọn.</p>
            <p v-else>CRUD dữ liệu gốc/mẫu. Sau này vào khung chương trình chỉ chọn mẫu để copy/gán vào Version.</p>
          </div>
        </div>

        <div v-if="canGanChuongTrinh" class="canh-bao-cha">
          <b>Cha hiện tại:</b>
          <span v-if="boLoc.chuongTrinhId">{{ tenNganhDangChon }} → {{ tenChuongTrinhDangChon }}</span>
          <span v-else>Chưa chọn chương trình. Chọn Ngành → Chương trình ở bộ lọc phía trên trước khi thêm/cập nhật.</span>
        </div>

        <form class="form-mau" @submit.prevent="luuDong">
          <label>
            <span>Mã <b>*</b></span>
            <input
                v-model.trim="form.ma"
                :class="{ 'co-loi': loiForm.ma }"
                type="text"
                maxlength="50"
                placeholder="vd: mt01"
                @input="xoaLoi('ma')"
            >
            <small v-if="loiForm.ma" class="loi-truong">{{ loiForm.ma }}</small>
          </label>

          <label v-if="config.coTen">
            <span>Tên <b>*</b></span>
            <input
                v-model.trim="form.ten"
                :class="{ 'co-loi': loiForm.ten }"
                type="text"
                maxlength="255"
                placeholder="tên vị trí việc làm"
                @input="xoaLoi('ten')"
            >
            <small v-if="loiForm.ten" class="loi-truong">{{ loiForm.ten }}</small>
          </label>

          <label v-if="config.coLoai">
            <span>Loại <b>*</b></span>
            <select v-model="form.loai" :class="{ 'co-loi': loiForm.loai }" @change="xoaLoi('loai')">
              <option v-for="loai in config.dsLoai" :key="loai.id" :value="loai.id">
                {{ loai.ten }}
              </option>
            </select>
            <small v-if="loiForm.loai" class="loi-truong">{{ loiForm.loai }}</small>
          </label>

          <label v-if="config.coNoiDung" class="cot-rong">
            <span>Nội dung <b>*</b></span>
            <textarea
                v-model.trim="form.noiDung"
                :class="{ 'co-loi': loiForm.noiDung }"
                rows="5"
                @input="xoaLoi('noiDung')"
            ></textarea>
            <small v-if="loiForm.noiDung" class="loi-truong">{{ loiForm.noiDung }}</small>
          </label>

          <label v-if="config.coMoTa" class="cot-rong">
            <span>Mô tả</span>
            <textarea v-model.trim="form.moTa" rows="5"></textarea>
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
                {{ dangUpload ? 'Đang lưu tệp...' : 'Gán tệp' }}
              </button>
              <button type="button" class="nut" :disabled="!form.id" @click="xemTep(form)">
                Xem tệp
              </button>
            </div>

            <small v-if="danhSachFile.length">Đã chọn {{ danhSachFile.length }} tệp. Bấm “Gán tệp” để upload và đưa link vào ô đường dẫn.</small>
          </div>

          <div class="hang-nut cot-rong">
            <button type="submit" class="nut chinh" :disabled="dangLuu">
              {{ form.id ? config.nutCapNhat : config.nutThem }}
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
            <h3>{{ config.tieuDeDanhSach }}</h3>
            <p>Đang hiển thị: {{ danhSachLoc.length }} / {{ danhSachGoc.length }}</p>
          </div>
        </div>

        <div class="bo-loc bo-loc-nho">
          <label>
            <span>Tìm kiếm</span>
            <input v-model.trim="tuKhoa" type="text" placeholder="Tìm mã, nội dung, ghi chú...">
          </label>

          <label>
            <span>Mã</span>
            <input v-model.trim="maLoc" type="text" placeholder="lọc theo mã">
          </label>
        </div>

        <div class="bang-boc">
          <table>
            <thead>
            <tr>
              <th>Mã</th>
              <th v-if="config.coTen">Tên</th>
              <th v-if="config.coLoai">Loại</th>
              <th>{{ config.nhanNoiDungBang }}</th>
              <th>Tệp</th>
              <th class="cot-thao-tac">Thao tác</th>
            </tr>
            </thead>

            <tbody>
            <tr v-if="!danhSachLoc.length">
              <td :colspan="soCotBang" class="rong">Chưa có dữ liệu phù hợp bộ lọc.</td>
            </tr>

            <tr
                v-for="item in danhSachLoc"
                :key="item.id"
                :class="{ 'dang-chon': String(item.id || '') === String(form.id || '') }"
            >
              <td>
                <b>{{ item.ma || '-' }}</b>
                <small>ID: {{ item.id }}</small>
              </td>

              <td v-if="config.coTen">{{ item.ten || '-' }}</td>
              <td v-if="config.coLoai">{{ tenLoai(item.loai) }}</td>

              <td>
                <div>{{ rutGon(config.coNoiDung ? item.noiDung : item.moTa, 180) || '-' }}</div>
                <small>{{ rutGon(item.ghiChu, 120) }}</small>
              </td>

              <td>
                <div class="duong-dan-ngan">{{ rutGon(chuanHoaDuongDan(item.duongDan), 80) }}</div>
                <button type="button" class="nut nho" :disabled="!item.id" @click="xemTep(item)">
                  Xem tệp
                </button>
              </td>

              <td class="thao-tac">
                <button type="button" class="nut nho chinh" @click="suaDong(item)">
                  Sửa
                </button>

                <button type="button" class="nut nho nguy-hiem" :disabled="dangLuu" @click="xoaDong(item)">
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

const props = defineProps({
  entityKey: {
    type: String,
    required: true
  }
})

const router = useRouter()

const {
  duLieu,
  services,
  taiDuLieuCoSanTatCaBang
} = useDaoTaoXemChuongTrinh()

const CAU_HINH = {
  mucTieuChuongTrinhGoc: {
    serviceKey: 'mucTieuChuongTrinhGoc',
    joinKey: 'mucTieuChuongTrinh',
    matchBy: 'ma',
    nghiepVu: 'MUC_TIEU_CHUONG_TRINH_GOC',
    tepNguon: 'mucTieuChuongTrinhGoc',
    idQueryKey: 'mucTieuChuongTrinhGocId',
    tieuDeTrang: 'Mục tiêu chương trình gốc / mẫu',
    moTaTrang: 'CRUD kho mục tiêu chương trình gốc/mẫu để tái sử dụng cho nhiều ngành, chương trình, version.',
    tieuDeThem: 'Thêm mục tiêu chương trình gốc / mẫu',
    tieuDeCapNhat: 'Cập nhật mục tiêu chương trình gốc / mẫu',
    tieuDeDanhSach: 'Danh sách mục tiêu chương trình gốc / mẫu',
    nutThem: 'Thêm mục tiêu',
    nutCapNhat: 'Cập nhật mục tiêu',
    coLoai: true,
    coNoiDung: true,
    coTen: false,
    coMoTa: false,
    nhanNoiDungBang: 'Nội dung',
    dsLoai: [
      { id: 'chung', ten: 'Chung' },
      { id: 'kien_thuc', ten: 'Kiến thức' },
      { id: 'ky_nang', ten: 'Kỹ năng' },
      { id: 'nang_luc_tu_chu_trach_nhiem', ten: 'Năng lực tự chủ trách nhiệm' }
    ],
    defaultForm: { ma: '', loai: 'chung', noiDung: '', ten: '', moTa: '', ghiChu: '', duongDan: '' }
  },
  nangLucDauRaGoc: {
    serviceKey: 'nangLucDauRaGoc',
    joinKey: 'chuongTrinhVersionNangLuc',
    joinIdKey: 'nangLucGocId',
    nghiepVu: 'NANG_LUC_DAU_RA_GOC',
    tepNguon: 'nangLucDauRaGoc',
    idQueryKey: 'nangLucDauRaGocId',
    tieuDeTrang: 'Năng lực đầu ra gốc / mẫu',
    moTaTrang: 'CRUD năng lực đầu ra gốc/mẫu theo Chương trình. Khi thêm/sửa phải chọn Ngành → Chương trình ở Bộ lọc chương trình.',
    tieuDeThem: 'Thêm năng lực đầu ra gốc / mẫu',
    tieuDeCapNhat: 'Cập nhật năng lực đầu ra gốc / mẫu',
    tieuDeDanhSach: 'Danh sách năng lực đầu ra gốc / mẫu',
    nutThem: 'Thêm năng lực',
    nutCapNhat: 'Cập nhật năng lực',
    coLoai: true,
    coNoiDung: true,
    coTen: false,
    coMoTa: false,
    nhanNoiDungBang: 'Nội dung',
    dsLoai: [
      { id: 'co_ban', ten: 'Cơ bản' },
      { id: 'cot_loi', ten: 'Cốt lõi' },
      { id: 'nang_cao', ten: 'Nâng cao' }
    ],
    defaultForm: { ma: '', loai: 'co_ban', noiDung: '', ten: '', moTa: '', ghiChu: '', duongDan: '' }
  },
  viTriViecLamGoc: {
    serviceKey: 'viTriViecLamGoc',
    joinKey: 'chuongTrinhVersionViTriViecLam',
    joinIdKey: 'viTriGocId',
    nghiepVu: 'VI_TRI_VIEC_LAM_GOC',
    tepNguon: 'viTriViecLamGoc',
    idQueryKey: 'viTriViecLamGocId',
    tieuDeTrang: 'Vị trí việc làm gốc / mẫu',
    moTaTrang: 'CRUD vị trí việc làm gốc/mẫu theo chương trình. Version sẽ gán lại vị trí này để tái sử dụng khi nâng cấp.',
    tieuDeThem: 'Thêm vị trí việc làm gốc / mẫu',
    tieuDeCapNhat: 'Cập nhật vị trí việc làm gốc / mẫu',
    tieuDeDanhSach: 'Danh sách vị trí việc làm gốc / mẫu',
    nutThem: 'Thêm vị trí',
    nutCapNhat: 'Cập nhật vị trí',
    coLoai: false,
    coNoiDung: false,
    coTen: true,
    coMoTa: true,
    nhanNoiDungBang: 'Mô tả',
    dsLoai: [],
    defaultForm: { ma: '', loai: '', noiDung: '', ten: '', moTa: '', ghiChu: '', duongDan: '' }
  }
}

const config = computed(() => CAU_HINH[props.entityKey] || CAU_HINH.mucTieuChuongTrinhGoc)
const canGanChuongTrinh = computed(() => ['nangLucDauRaGoc', 'viTriViecLamGoc'].includes(props.entityKey))
const canLocVersion = computed(() => props.entityKey === 'nhomKienThucMon')

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
  loai: '',
  noiDung: '',
  ten: '',
  moTa: '',
  ghiChu: '',
  duongDan: ''
})

const loiForm = reactive({
  ma: '',
  loai: '',
  noiDung: '',
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

const chuongTrinhDangChon = computed(() => {
  if (!boLoc.chuongTrinhId) return null
  return (duLieu.value.chuongTrinh || []).find((ct) => String(ct.id || '') === String(boLoc.chuongTrinhId || '')) || null
})

const nganhDangChon = computed(() => {
  if (!boLoc.nganhId) return null
  return (duLieu.value.nganh || []).find((nganh) => String(nganh.id || '') === String(boLoc.nganhId || '')) || null
})

const tenChuongTrinhDangChon = computed(() => {
  const ct = chuongTrinhDangChon.value
  if (!ct) return 'Chưa chọn chương trình'
  return `${ct.maChuongTrinh || ct.ma || ''}${ct.maChuongTrinh || ct.ma ? ' - ' : ''}${ct.tenChuongTrinh || ct.ten || ct.id}`
})

const tenNganhDangChon = computed(() => {
  const nganh = nganhDangChon.value
  if (!nganh) return 'Chưa chọn ngành'
  return `${nganh.maNganh || nganh.ma || ''}${nganh.maNganh || nganh.ma ? ' - ' : ''}${nganh.tenNganh || nganh.ten || nganh.tenNganhDaoTao || nganh.id}`
})

const danhSachGoc = computed(() => [...(duLieu.value[config.value.serviceKey] || [])]
    .sort((a, b) => String(a.ma || a.ten || '').localeCompare(String(b.ma || b.ten || ''), 'vi')))

const versionIdsTheoBoLoc = computed(() => {
  if (!boLoc.nganhId && !boLoc.chuongTrinhId && (!canLocVersion.value || !boLoc.versionId)) return null

  const ids = new Set()
  const chuongTrinhHopLe = new Set(danhSachChuongTrinhTheoNganh.value.map((item) => String(item.id)))

  ;(duLieu.value.chuongTrinhVersion || []).forEach((version) => {
    if (canLocVersion.value && boLoc.versionId && String(version.id || '') !== String(boLoc.versionId)) return

    if (boLoc.chuongTrinhId) {
      if (String(version.chuongTrinhId || '') !== String(boLoc.chuongTrinhId)) return
    } else if (boLoc.nganhId) {
      if (!chuongTrinhHopLe.has(String(version.chuongTrinhId || ''))) return
    }

    ids.add(String(version.id))
  })

  return ids
})

const khoaGocTheoBoLoc = computed(() => {
  const versionIds = versionIdsTheoBoLoc.value
  if (!versionIds) return null

  const keys = new Set()
  const cfg = config.value

  ;(duLieu.value[cfg.joinKey] || []).forEach((row) => {
    if (!versionIds.has(String(row.chuongTrinhVersionId || ''))) return

    if (cfg.joinIdKey && row[cfg.joinIdKey]) keys.add(String(row[cfg.joinIdKey]))
    if (cfg.matchBy && row[cfg.matchBy]) keys.add(String(row[cfg.matchBy]).trim().toLowerCase())
  })

  return keys
})

const danhSachLoc = computed(() => {
  const keyword = tuKhoa.value.trim().toLowerCase()
  const ma = maLoc.value.trim().toLowerCase()
  const keysTheoTang = khoaGocTheoBoLoc.value
  const cfg = config.value

  return danhSachGoc.value.filter((item) => {
    if (canGanChuongTrinh.value) {
      if (boLoc.chuongTrinhId) {
        if (String(item.chuongTrinhId || '') !== String(boLoc.chuongTrinhId || '')) return false
      } else if (boLoc.nganhId) {
        const ct = (duLieu.value.chuongTrinh || []).find((chuongTrinh) => String(chuongTrinh.id || '') === String(item.chuongTrinhId || ''))
        if (String(ct?.nganhId || '') !== String(boLoc.nganhId || '')) return false
      }
    } else if (keysTheoTang) {
      const key = cfg.joinIdKey ? String(item.id || '') : String(item[cfg.matchBy] || '').trim().toLowerCase()
      if (!keysTheoTang.has(key)) return false
    }

    if (ma && !String(item.ma || '').toLowerCase().includes(ma)) return false
    if (!keyword) return true

    const text = [
      item.ma,
      item.loai,
      item.noiDung,
      item.ten,
      item.moTa,
      item.ghiChu,
      item.duongDan
    ].filter(Boolean).join(' ').toLowerCase()

    return text.includes(keyword)
  })
})

const soCotBang = computed(() => {
  let count = 4
  if (config.value.coTen) count += 1
  if (config.value.coLoai) count += 1
  return count
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
    baoTin(`Đã tải lại dữ liệu ${config.value.tieuDeDanhSach.toLowerCase()}.`)
  } catch (error) {
    baoTin(layThongBaoLoi(error, 'Không tải được dữ liệu.'), 'error')
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

function giaTriTrim(value) {
  return String(value || '').trim()
}

function giaTriHoacNull(value) {
  const text = giaTriTrim(value)
  return text || null
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

function resetForm() {
  Object.assign(form, {
    id: null,
    ...config.value.defaultForm
  })
  danhSachFile.value = []
  xoaLoi()
  if (fileInput.value) fileInput.value.value = ''
}

function suaDong(item) {
  if (canGanChuongTrinh.value && item?.chuongTrinhId) {
    const chuongTrinh = (duLieu.value.chuongTrinh || [])
        .find((ct) => String(ct.id || '') === String(item.chuongTrinhId || ''))

    if (chuongTrinh?.nganhId) boLoc.nganhId = String(chuongTrinh.nganhId)
    boLoc.chuongTrinhId = String(item.chuongTrinhId)
    boLoc.versionId = ''
  }

  Object.assign(form, {
    id: item.id || null,
    ma: item.ma || '',
    loai: item.loai || config.value.defaultForm.loai || '',
    noiDung: item.noiDung || '',
    ten: item.ten || '',
    moTa: item.moTa || '',
    ghiChu: item.ghiChu || '',
    duongDan: chuanHoaDuongDan(item.duongDan || '')
  })
  xoaLoi()
}

function validateForm() {
  xoaLoi()
  const cfg = config.value
  const ma = giaTriTrim(form.ma)

  if (!ma) loiForm.ma = 'Cần nhập mã.'
  else if (ma.length > 50) loiForm.ma = 'Mã tối đa 50 ký tự.'

  if (cfg.coLoai && !giaTriTrim(form.loai)) loiForm.loai = 'Cần chọn loại.'
  if (cfg.coNoiDung && !giaTriTrim(form.noiDung)) loiForm.noiDung = 'Cần nhập nội dung.'
  if (cfg.coTen && !giaTriTrim(form.ten)) loiForm.ten = 'Cần nhập tên.'
  else if (cfg.coTen && giaTriTrim(form.ten).length > 255) loiForm.ten = 'Tên tối đa 255 ký tự.'

  if (canGanChuongTrinh.value && !boLoc.chuongTrinhId) {
    baoTin('Cần chọn Ngành → Chương trình ở Bộ lọc chương trình trước khi thêm/cập nhật.', 'error')
    return false
  }

  const trungMa = danhSachGoc.value.some((item) => {
    const cungMa = String(item.ma || '').trim().toLowerCase() === ma.toLowerCase()
    const khacDong = String(item.id || '') !== String(form.id || '')

    if (canGanChuongTrinh.value) {
      const cungChuongTrinh = String(item.chuongTrinhId || '') === String(boLoc.chuongTrinhId || '')
      return cungMa && khacDong && cungChuongTrinh
    }

    return cungMa && khacDong
  })

  if (!loiForm.ma && trungMa) {
    loiForm.ma = canGanChuongTrinh.value
        ? 'Mã đã tồn tại trong chương trình đang chọn.'
        : 'Mã đã tồn tại trong kho gốc/mẫu.'
  }

  const loiDauTien = Object.values(loiForm).find(Boolean)
  if (loiDauTien) {
    baoTin(loiDauTien, 'error')
    return false
  }

  return true
}

function taoPayload() {
  const cfg = config.value
  const payload = {
    ma: giaTriTrim(form.ma),
    ghiChu: giaTriHoacNull(form.ghiChu),
    duongDan: giaTriHoacNull(form.duongDan)
  }

  if (canGanChuongTrinh.value) {
    payload.chuongTrinhId = Number(boLoc.chuongTrinhId)
  }

  if (cfg.coLoai) payload.loai = giaTriTrim(form.loai)
  if (cfg.coNoiDung) payload.noiDung = giaTriTrim(form.noiDung)
  if (cfg.coTen) payload.ten = giaTriTrim(form.ten)
  if (cfg.coMoTa) payload.moTa = giaTriHoacNull(form.moTa)

  return payload
}

async function luuDong() {
  if (!validateForm()) return

  dangLuu.value = true

  try {
    const service = services[config.value.serviceKey]

    if (form.id) {
      await service.update(form.id, taoPayload())
      baoTin('Đã cập nhật dữ liệu gốc/mẫu.')
    } else {
      await service.create(taoPayload())
      baoTin('Đã thêm dữ liệu gốc/mẫu.')
    }

    await taiDuLieuCoSanTatCaBang()
    resetForm()
  } catch (error) {
    baoTin(layThongBaoLoi(error, 'Không lưu được dữ liệu. Kiểm tra BE đã hỗ trợ cột đường dẫn tệp chưa.'), 'error')
  } finally {
    dangLuu.value = false
  }
}

async function xoaDong(item) {
  if (!item?.id) return
  if (!window.confirm(`Xóa dữ liệu gốc/mẫu "${item.ma || item.ten || item.id}"?`)) return

  dangLuu.value = true

  try {
    await services[config.value.serviceKey].delete(item.id)
    await taiDuLieuCoSanTatCaBang()
    if (String(form.id || '') === String(item.id || '')) resetForm()
    baoTin('Đã xóa dữ liệu gốc/mẫu.')
  } catch (error) {
    baoTin(layThongBaoLoi(error, 'Không xóa được. Kiểm tra dữ liệu có đang được Version sử dụng không.'), 'error')
  } finally {
    dangLuu.value = false
  }
}

function chonTep(event) {
  danhSachFile.value = Array.from(event.target.files || [])
}

async function luuTep() {
  if (!danhSachFile.value.length) return baoTin('Cần chọn tệp trước khi gán.', 'error')

  dangUpload.value = true

  try {
    const danhSachFileInfo = await tepDinhKemUploadService.uploadMany({
      files: danhSachFile.value,
      module: 'DAO_TAO',
      nghiepVu: config.value.nghiepVu,
      doiTuongId: form.id || null,
      moTa: `Tệp ${config.value.tieuDeTrang} ${form.ma || form.ten || ''}`.trim(),
      tangNghiepVu: 'GOC'
    })

    const duongDanMoi = danhSachFileInfo
        .map((fileInfo, index) => tepDinhKemUploadService.taoDuongDanTaiFile(fileInfo) || danhSachFile.value[index]?.name || '')
        .filter(Boolean)

    const hienTai = tachDuongDan(form.duongDan)
    form.duongDan = Array.from(new Set([...hienTai, ...duongDanMoi])).join('\n')

    if (form.id) {
      await services[config.value.serviceKey].update(form.id, taoPayload())
      await taiDuLieuCoSanTatCaBang()
      baoTin(`Đã upload và gán ${duongDanMoi.length} tệp.`)
    } else {
      baoTin(`Đã upload ${duongDanMoi.length} tệp. Bấm lưu để gán đường dẫn vào bản ghi.`)
    }

    danhSachFile.value = []
    if (fileInput.value) fileInput.value.value = ''
  } catch (error) {
    baoTin(layThongBaoLoi(error, 'Không upload/gán được tệp.'), 'error')
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
    baoTin('Cần lưu bản ghi trước khi xem tệp.', 'error')
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
      tepNguon: config.value.tepNguon,
      [config.value.idQueryKey]: item.id,
      quayLaiPath: router.currentRoute.value.fullPath
    }
  })
}

function tenLoai(value) {
  const found = config.value.dsLoai.find((item) => String(item.id) === String(value))
  return found?.ten || value || '-'
}

function rutGon(value, max = 100) {
  const text = String(value || '').trim()
  if (text.length <= max) return text
  return `${text.slice(0, max)}...`
}

resetForm()
</script>

<style scoped>
.dao-tao-mau-goc-crud {
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

.khoi-dau,
.the {
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

.form-mau,
.bo-loc {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 10px;
  margin-top: 12px;
}

.bo-loc-tang .bo-loc {
  grid-template-columns: repeat(2, minmax(0, 1fr));
}

.bo-loc-tang .bo-loc.bo-loc-co-version {
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

.canh-bao-cha {
  margin-top: 12px;
  padding: 10px 12px;
  border: 1px solid #bfdbfe;
  border-radius: 6px;
  background: #eff6ff;
  color: #1e3a8a;
  font-size: 13px;
  line-height: 1.5;
}

.canh-bao-cha b {
  margin-right: 6px;
}

.hang-nut,
.thao-tac {
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

.duong-dan-ngan {
  max-width: 180px;
  color: #64748b;
  margin-bottom: 5px;
  word-break: break-word;
}

.cot-thao-tac {
  width: 130px;
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

  .form-mau,
  .bo-loc,
  .bo-loc-nho {
    grid-template-columns: 1fr;
  }
}
</style>
