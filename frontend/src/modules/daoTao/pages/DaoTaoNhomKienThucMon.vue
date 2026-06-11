<template>
  <div class="admin-nhom-kien-thuc-mon">
    <div v-if="thongBao" :class="['thong-bao', loaiThongBao]">
      {{ thongBao }}
    </div>

    <section class="khoi-dau">
      <div>
        <h2>Quản lý nhóm kiến thức và môn</h2>
        <p>
          Chọn ngành, chọn version, xem môn học mẫu, copy môn mẫu vào version, CRUD nhóm kiến thức và gán môn vào nhóm.
        </p>
      </div>

      <button type="button" class="nut" :disabled="dangTai" @click="taiDuLieu">
        Tải lại dữ liệu
      </button>
    </section>

    <section class="bo-loc">
      <label>
        <span>Ngành</span>
        <select v-model="nganhIdDangChon">
          <option value="">-- Chọn ngành --</option>
          <option
              v-for="nganh in danhSachNganh"
              :key="nganh.id"
              :value="nganh.id"
          >
            {{ nganh.maNganh || nganh.ma || nganh.id }} - {{ nganh.tenNganh || nganh.ten || nganh.id }}
          </option>
        </select>
      </label>

      <label>
        <span>Version chương trình</span>
        <select v-model="versionIdDangChon" :disabled="!nganhIdDangChon">
          <option value="">-- Chọn version --</option>
          <option
              v-for="version in danhSachVersionTheoNganh"
              :key="version.id"
              :value="version.id"
          >
            {{ hienThiVersion(version) }}
          </option>
        </select>
      </label>
    </section>

    <section v-if="!nganhIdDangChon" class="hop-rong">
      Chọn ngành để bắt đầu.
    </section>

    <section v-else-if="!versionIdDangChon" class="hop-rong">
      Chọn version chương trình của ngành.
    </section>

    <template v-else>
      <section class="tong-quan">
        <div>
          <b>Ngành</b>
          <span>{{ tenNganhDangChon }}</span>
        </div>

        <div>
          <b>Version</b>
          <span>{{ tenVersionDangChon }}</span>
        </div>

        <div>
          <b>Nhóm kiến thức</b>
          <span>{{ danhSachNhomKienThucTheoVersion.length }}</span>
        </div>

        <div>
          <b>Môn trong version</b>
          <span>{{ danhSachMonTheoVersion.length }}</span>
        </div>
      </section>

      <section class="the">
        <div class="tieu-de-the">
          <div>
            <h3>Danh sách môn học mẫu</h3>
            <p>
              Môn học mẫu dùng chung toàn hệ thống, không thuộc ngành, không thuộc version, không có kỳ.
            </p>
          </div>
        </div>

        <div class="bo-loc-mon-mau">
          <label>
            <span>Tìm môn mẫu</span>
            <input v-model.trim="tuKhoaMonmau" type="text" placeholder="Nhập mã môn hoặc tên môn">
          </label>
        </div>

        <div class="bang-boc bang-mon-mau-cuon">
          <table>
            <thead>
            <tr>
              <th>Mã môn</th>
              <th>Tên môn</th>
              <th>Mô tả</th>
              <th>Trạng thái trong version</th>
              <th class="cot-thao-tac">Thao tác</th>
            </tr>
            </thead>

            <tbody>
            <tr v-if="!danhSachMonHocmauLoc.length">
              <td colspan="5" class="rong">Không có môn mẫu phù hợp.</td>
            </tr>

            <tr
                v-for="monHoc in danhSachMonHocmauLoc"
                :key="monHoc.id"
                :class="{ 'dang-chon': daCoMonHocTrongVersion(monHoc.id) }"
            >
              <td>{{ monHoc.maMon || monHoc.ma || '-' }}</td>

              <td>
                <b>{{ monHoc.tenMon || monHoc.ten || '-' }}</b>
                <small>ID: {{ monHoc.id }}</small>
              </td>

              <td>{{ monHoc.moTa || '-' }}</td>

              <td>
                <span v-if="daCoMonHocTrongVersion(monHoc.id)" class="nhan da-gan">
                  Đã có trong version
                </span>
                <span v-else class="nhan chua-gan">
                  Chưa gán
                </span>
              </td>

              <td class="thao-tac">
                <button
                    type="button"
                    class="nut nho chinh"
                    :disabled="dangLuu || daCoMonHocTrongVersion(monHoc.id)"
                    @click="copyMonmauVaoVersion(monHoc)"
                >
                  Gán vào version
                </button>
              </td>
            </tr>
            </tbody>
          </table>
        </div>
      </section>

      <section class="luoi-chinh">
        <div class="the">
          <div class="tieu-de-the">
            <div>
              <h3>CRUD nhóm kiến thức</h3>
              <p>Nhóm kiến thức được tạo theo đúng version đang chọn.</p>
            </div>
          </div>

          <form class="form-nhom" @submit.prevent="luuNhomKienThuc">
            <label>
              <span>Mã nhóm</span>
              <input v-model.trim="formNhom.ma" type="text" placeholder="VD: NKT-CHUNG">
            </label>

            <label>
              <span>Tên nhóm <b>*</b></span>
              <input v-model.trim="formNhom.ten" type="text" placeholder="VD: Các môn học chung">
            </label>

            <label>
              <span>Loại nhóm <b>*</b></span>
              <select v-model="formNhom.loaiNhom">
                <option value="chung">Chung</option>
                <option value="co_so">Cơ sở</option>
                <option value="chuyen_mon">Chuyên môn</option>
                <option value="tu_chon">Tự chọn</option>
              </select>
            </label>

            <label>
              <span>Thứ tự</span>
              <input v-model="formNhom.thuTu" type="number" min="0">
            </label>


            <label>
              <span>Nhóm mẫu</span>
              <select v-model="formNhom.nhomKienThucmauId">
                <option value="">Không chọn</option>
                <option
                    v-for="nhommau in danhSachNhomKienThucmau"
                    :key="nhommau.id"
                    :value="nhommau.id"
                >
                  {{ nhommau.ma || nhommau.id }} - {{ nhommau.ten || nhommau.id }}
                </option>
              </select>
            </label>

            <label class="cot-rong">
              <span>Mô tả</span>
              <textarea v-model.trim="formNhom.moTa" rows="3"></textarea>
            </label>

            <div class="hang-nut cot-rong">
              <button type="submit" class="nut chinh" :disabled="dangLuu">
                {{ formNhom.id ? 'Cập nhật nhóm' : 'Thêm nhóm' }}
              </button>

              <button type="button" class="nut" :disabled="dangLuu" @click="resetFormNhom">
                Làm mới form
              </button>
            </div>
          </form>

          <div class="bang-boc">
            <table>
              <thead>
              <tr>
                <th>Mã</th>
                <th>Tên nhóm</th>
                <th>Loại</th>
                <th>Số môn</th>
                <th>Thứ tự</th>
                <th class="cot-thao-tac">Thao tác</th>
              </tr>
              </thead>

              <tbody>
              <tr v-if="!danhSachNhomKienThucTheoVersion.length">
                <td colspan="6" class="rong">Version này chưa có nhóm kiến thức.</td>
              </tr>

              <tr
                  v-for="nhom in danhSachNhomKienThucTheoVersion"
                  :key="nhom.id"
                  :class="{ 'dang-chon': String(nhom.id) === String(nhomKienThucIdDangChon) }"
              >
                <td>{{ nhom.ma || '-' }}</td>

                <td>
                  <b>{{ nhom.ten || '-' }}</b>
                  <small>{{ nhom.moTa || '' }}</small>
                </td>

                <td>{{ hienThiLoaiNhom(nhom.loaiNhom) }}</td>

                <td>
                  <b>{{ demSoMonTrongNhom(nhom.id) }}</b>
                </td>

                <td>{{ nhom.thuTu ?? '-' }}</td>

                <td class="thao-tac">
                  <button type="button" class="nut nho chinh" @click="chonNhomKienThuc(nhom)">
                    Chọn
                  </button>

                  <button type="button" class="nut nho" @click="suaNhomKienThuc(nhom)">
                    Sửa
                  </button>

                  <button type="button" class="nut nho nguy-hiem" :disabled="dangLuu" @click="xoaNhomKienThuc(nhom)">
                    Xóa
                  </button>
                </td>
              </tr>
              </tbody>
            </table>
          </div>
        </div>

        <div class="the">
          <div class="tieu-de-the">
            <div>
              <h3>Môn đã nằm trong version</h3>
              <p>Đổi loại phạm vi hoặc nhóm hiện tại là hệ thống lưu ngay.</p>
            </div>

            <div class="nhom-dang-chon">
              <span>Nhóm đang chọn:</span>
              <b>{{ tenNhomDangChon }}</b>
              <small>Số môn: {{ soMonTrongNhomDangChon }}</small>
            </div>
          </div>

          <div class="bang-boc bang-mon-cuon">
            <table>
              <thead>
              <tr>
                <th>Mã môn CT</th>
                <th>Môn học</th>
                <th>Loại phạm vi</th>
                <th>Nhóm hiện tại</th>
                <th class="cot-thao-tac">Thao tác</th>
              </tr>
              </thead>

              <tbody>
              <tr v-if="!danhSachMonTheoVersion.length">
                <td colspan="5" class="rong">
                  Version này chưa có môn trong chương trình.
                </td>
              </tr>

              <tr
                  v-for="mon in danhSachMonTheoVersion"
                  :key="mon.id"
                  :class="{ 'dang-chon': String(mon.nhomKienThucId || '') === String(nhomKienThucIdDangChon || '') }"
              >
                <td>{{ mon.maMonTrongCt || '-' }}</td>

                <td>
                  <b>{{ mon.tenMon || tenMonHoc(mon.monHocId) }}</b>
                  <small>Môn ID: {{ mon.monHocId || '-' }}</small>
                </td>

                <td>
                  <select
                      class="chon-nhanh"
                      :value="mon.loaiPhamVi || 'mon_chuyen_nganh'"
                      :disabled="dangLuu"
                      @change="capNhatLoaiPhamVi(mon, $event.target.value)"
                  >
                    <option value="mon_chung">Môn chung</option>
                    <option value="mon_co_so">Môn cơ sở</option>
                    <option value="mon_chuyen_nganh">Môn chuyên ngành</option>
                    <option value="mon_tu_chon_nang_cao">Môn tự chọn nâng cao</option>
                  </select>
                </td>

                <td>
                  <select
                      class="chon-nhanh"
                      :value="mon.nhomKienThucId || ''"
                      :disabled="dangLuu"
                      @change="capNhatNhomKienThucChoMon(mon, $event.target.value)"
                  >
                    <option value="">Chưa gán</option>
                    <option
                        v-for="nhom in danhSachNhomKienThucTheoVersion"
                        :key="nhom.id"
                        :value="nhom.id"
                    >
                      {{ nhom.ma || nhom.id }} - {{ nhom.ten || nhom.id }}
                    </option>
                  </select>
                </td>

                <td class="thao-tac">
                  <button
                      type="button"
                      class="nut nho chinh"
                      :disabled="!nhomKienThucIdDangChon || dangLuu"
                      @click="ganMonVaoNhom(mon)"
                  >
                    Gán nhóm chọn
                  </button>

                  <button
                      type="button"
                      class="nut nho nguy-hiem"
                      :disabled="!mon.nhomKienThucId || dangLuu"
                      @click="boMonKhoiNhom(mon)"
                  >
                    Bỏ nhóm
                  </button>

                  <button
                      type="button"
                      class="nut nho nguy-hiem"
                      :disabled="dangLuu"
                      @click="boGanMonKhoiVersion(mon)"
                  >
                    Bỏ gán môn
                  </button>
                </td>
              </tr>
              </tbody>
            </table>
          </div>
        </div>
      </section>
    </template>
  </div>
</template>

<script setup>
import { computed, onMounted, reactive, ref, watch } from 'vue'
import { useDaoTaoXemChuongTrinh } from '@/modules/daoTao/composables/useDaoTaoXemChuongTrinh'
import { layThongBaoLoi } from '@/modules/daoTao/utils/layThongBaoLoi'

const {
  duLieu,
  services,
  taiDuLieuCoSanTatCaBang
} = useDaoTaoXemChuongTrinh()

const nganhIdDangChon = ref('')
const versionIdDangChon = ref('')
const nhomKienThucIdDangChon = ref('')
const thongBao = ref('')
const loaiThongBao = ref('success')
const dangLuu = ref(false)
const dangTai = ref(false)
const tuKhoaMonmau = ref('')

const formNhom = reactive({
  id: null,
  ma: '',
  ten: '',
  loaiNhom: 'chung',
  thuTu: '',
  nhomKienThucmauId: '',
  moTa: ''
})

const danhSachNganh = computed(() => {
  return [...(duLieu.value.nganh || [])]
      .sort((a, b) => String(a.tenNganh || a.ten || '').localeCompare(String(b.tenNganh || b.ten || ''), 'vi'))
})

const danhSachChuongTrinhTheoNganh = computed(() => {
  if (!nganhIdDangChon.value) return []

  return (duLieu.value.chuongTrinh || [])
      .filter((item) => String(item.nganhId || '') === String(nganhIdDangChon.value))
})

const danhSachVersionTheoNganh = computed(() => {
  if (!nganhIdDangChon.value) return []

  const chuongTrinhIds = new Set(
      danhSachChuongTrinhTheoNganh.value.map((item) => String(item.id))
  )

  return (duLieu.value.chuongTrinhVersion || [])
      .filter((version) => chuongTrinhIds.has(String(version.chuongTrinhId || '')))
      .sort((a, b) => String(a.maVersion || a.id).localeCompare(String(b.maVersion || b.id), 'vi'))
})

const danhSachNhomKienThucTheoVersion = computed(() => {
  if (!versionIdDangChon.value) return []

  return (duLieu.value.nhomKienThuc || [])
      .filter((item) => String(item.chuongTrinhVersionId || '') === String(versionIdDangChon.value))
      .sort((a, b) => Number(a.thuTu || 9999) - Number(b.thuTu || 9999))
})

const danhSachNhomKienThucmau = computed(() => {
  return [...(duLieu.value.nhomKienThucmau || [])]
      .sort((a, b) => Number(a.thuTu || 9999) - Number(b.thuTu || 9999))
})

const danhSachMonTheoVersion = computed(() => {
  if (!versionIdDangChon.value) return []

  return (duLieu.value.chuongTrinhMon || [])
      .filter((item) => String(item.chuongTrinhVersionId || '') === String(versionIdDangChon.value))
      .sort((a, b) => Number(a.thuTu || 9999) - Number(b.thuTu || 9999))
})

const danhSachMonHocmau = computed(() => {
  return [...(duLieu.value.monHoc || [])]
      .sort((a, b) => String(a.tenMon || a.ten || '').localeCompare(String(b.tenMon || b.ten || ''), 'vi'))
})

const danhSachMonHocmauLoc = computed(() => {
  const keyword = tuKhoaMonmau.value.trim().toLowerCase()

  if (!keyword) return danhSachMonHocmau.value

  return danhSachMonHocmau.value.filter((monHoc) => {
    const text = [
      monHoc.maMon,
      monHoc.ma,
      monHoc.tenMon,
      monHoc.ten,
      monHoc.moTa
    ].filter(Boolean).join(' ').toLowerCase()

    return text.includes(keyword)
  })
})

const nganhDangChon = computed(() => {
  return (duLieu.value.nganh || [])
      .find((item) => String(item.id || '') === String(nganhIdDangChon.value)) || null
})

const versionDangChon = computed(() => {
  return (duLieu.value.chuongTrinhVersion || [])
      .find((item) => String(item.id || '') === String(versionIdDangChon.value)) || null
})

const nhomKienThucDangChon = computed(() => {
  return danhSachNhomKienThucTheoVersion.value
      .find((item) => String(item.id || '') === String(nhomKienThucIdDangChon.value)) || null
})

const tenNganhDangChon = computed(() => {
  if (!nganhDangChon.value) return '-'
  return `${nganhDangChon.value.maNganh || nganhDangChon.value.ma || nganhDangChon.value.id} - ${nganhDangChon.value.tenNganh || nganhDangChon.value.ten || nganhDangChon.value.id}`
})

const tenVersionDangChon = computed(() => {
  if (!versionDangChon.value) return '-'
  return hienThiVersion(versionDangChon.value)
})

const tenNhomDangChon = computed(() => {
  if (!nhomKienThucDangChon.value) return 'Chưa chọn'
  return `${nhomKienThucDangChon.value.ma || nhomKienThucDangChon.value.id} - ${nhomKienThucDangChon.value.ten || nhomKienThucDangChon.value.id}`
})

const soMonTrongNhomDangChon = computed(() => {
  if (!nhomKienThucIdDangChon.value) return 0
  return demSoMonTrongNhom(nhomKienThucIdDangChon.value)
})

watch(nganhIdDangChon, () => {
  versionIdDangChon.value = ''
  nhomKienThucIdDangChon.value = ''
  resetFormNhom()
})

watch(versionIdDangChon, () => {
  nhomKienThucIdDangChon.value = ''
  resetFormNhom()
})

function baoTin(message, type = 'success') {
  thongBao.value = message
  loaiThongBao.value = type

  setTimeout(() => {
    if (thongBao.value === message) {
      thongBao.value = ''
    }
  }, 5000)
}

async function taiDuLieu() {
  dangTai.value = true

  try {
    await taiDuLieuCoSanTatCaBang()
    baoTin('Đã tải lại dữ liệu.')
  } catch (error) {
    baoTin(layThongBaoLoi(error, 'Không tải được dữ liệu.'), 'error')
  } finally {
    dangTai.value = false
  }
}

function hienThiVersion(version) {
  const chuongTrinh = (duLieu.value.chuongTrinh || [])
      .find((item) => String(item.id || '') === String(version.chuongTrinhId || ''))

  const tenChuongTrinh = version.tenChuongTrinh || chuongTrinh?.tenChuongTrinh || chuongTrinh?.ten || ''

  return `${version.maVersion || version.id} - ${version.tenVersion || version.ten || version.id}${tenChuongTrinh ? ` | ${tenChuongTrinh}` : ''}`
}

function hienThiLoaiNhom(loaiNhom) {
  const map = {
    chung: 'Chung',
    co_so: 'Cơ sở',
    chuyen_mon: 'Chuyên môn',
    tu_chon: 'Tự chọn'
  }

  return map[loaiNhom] || loaiNhom || '-'
}

function timTheoId(key, id) {
  if (!id) return null
  return (duLieu.value[key] || [])
      .find((item) => String(item.id || '') === String(id || '')) || null
}

function tenMonHoc(monHocId) {
  const mon = timTheoId('monHoc', monHocId)
  return mon?.tenMon || mon?.ten || mon?.maMon || monHocId || '-'
}

function demSoMonTrongNhom(nhomKienThucId) {
  if (!nhomKienThucId) return 0

  return danhSachMonTheoVersion.value.filter((mon) => {
    return String(mon.nhomKienThucId || '') === String(nhomKienThucId || '')
  }).length
}

function daCoMonHocTrongVersion(monHocId) {
  return danhSachMonTheoVersion.value.some((mon) => {
    return String(mon.monHocId || mon.monId || '') === String(monHocId || '')
  })
}

function taoPayloadCopyMonmauVaoVersion(monHoc) {
  return {
    chuongTrinhVersionId: versionIdDangChon.value,
    monHocId: monHoc.id,
    maMonTrongCt: monHoc.maMon || monHoc.ma || `MON_${monHoc.id}`,
    khungKyId: null,
    nhomKienThucId: null,

    loai: 'bat_buoc',
    loaiHocPhan: 'mon_hoc',
    loaiPhamVi: 'mon_chuyen_nganh',
    batBuoc: true,
    laMonDieuKien: false,

    thuTu: null,
    ghiChu: ''
  }
}

async function copyMonmauVaoVersion(monHoc) {
  if (!versionIdDangChon.value) {
    baoTin('Cần chọn version chương trình.', 'error')
    return
  }

  if (!monHoc?.id) {
    baoTin('Không xác định được môn mẫu.', 'error')
    return
  }

  if (daCoMonHocTrongVersion(monHoc.id)) {
    baoTin('Môn này đã có trong version đang chọn.', 'error')
    return
  }

  dangLuu.value = true

  try {
    await services.chuongTrinhMon.create(taoPayloadCopyMonmauVaoVersion(monHoc))
    await taiDuLieuCoSanTatCaBang()
    baoTin('Đã copy môn mẫu vào version. Hãy gán nhóm kiến thức ở bảng "Môn đã nằm trong version".')
  } catch (error) {
    baoTin(layThongBaoLoi(error, 'Không copy được môn mẫu vào version.'), 'error')
  } finally {
    dangLuu.value = false
  }
}

function giaTriSo(value) {
  if (value === '' || value === null || value === undefined) return null
  return Number(value)
}

function taoPayloadNhomKienThuc() {
  return {
    chuongTrinhVersionId: versionIdDangChon.value,
    nhomKienThucmauId: formNhom.nhomKienThucmauId || null,
    ma: formNhom.ma || null,
    ten: formNhom.ten,
    loaiNhom: formNhom.loaiNhom,
    thuTu: giaTriSo(formNhom.thuTu),
    moTa: formNhom.moTa || ''
  }
}

function resetFormNhom() {
  formNhom.id = null
  formNhom.ma = ''
  formNhom.ten = ''
  formNhom.loaiNhom = 'chung'
  formNhom.thuTu = ''
  formNhom.nhomKienThucmauId = ''
  formNhom.moTa = ''
}

function suaNhomKienThuc(nhom) {
  formNhom.id = nhom.id
  formNhom.ma = nhom.ma || ''
  formNhom.ten = nhom.ten || ''
  formNhom.loaiNhom = nhom.loaiNhom || 'chung'
  formNhom.thuTu = nhom.thuTu ?? ''
  formNhom.nhomKienThucmauId = nhom.nhomKienThucmauId || ''
  formNhom.moTa = nhom.moTa || ''
}

function chonNhomKienThuc(nhom) {
  nhomKienThucIdDangChon.value = nhom.id

  baoTin(`Đã chọn nhóm: ${nhom.ten || nhom.ma || nhom.id}`)
}

async function luuNhomKienThuc() {
  if (!versionIdDangChon.value) {
    baoTin('Cần chọn version chương trình.', 'error')
    return
  }

  if (!formNhom.ten) {
    baoTin('Cần nhập tên nhóm kiến thức.', 'error')
    return
  }

  if (!formNhom.loaiNhom) {
    baoTin('Cần chọn loại nhóm.', 'error')
    return
  }

  dangLuu.value = true

  try {
    const payload = taoPayloadNhomKienThuc()

    if (formNhom.id) {
      await services.nhomKienThuc.update(formNhom.id, payload)
      baoTin('Đã cập nhật nhóm kiến thức.')
    } else {
      await services.nhomKienThuc.create(payload)
      baoTin('Đã thêm nhóm kiến thức.')
    }

    await taiDuLieuCoSanTatCaBang()
    resetFormNhom()
  } catch (error) {
    baoTin(layThongBaoLoi(error, 'Không lưu được nhóm kiến thức.'), 'error')
  } finally {
    dangLuu.value = false
  }
}

async function xoaNhomKienThuc(nhom) {
  if (!nhom?.id) return

  const dongY = window.confirm(`Xóa nhóm kiến thức "${nhom.ten || nhom.ma || nhom.id}"?`)

  if (!dongY) return

  dangLuu.value = true

  try {
    await services.nhomKienThuc.delete(nhom.id)

    if (String(nhomKienThucIdDangChon.value || '') === String(nhom.id || '')) {
      nhomKienThucIdDangChon.value = ''
    }

    await taiDuLieuCoSanTatCaBang()
    baoTin('Đã xóa nhóm kiến thức.')
  } catch (error) {
    baoTin(layThongBaoLoi(error, 'Không xóa được nhóm kiến thức. Kiểm tra nhóm này có đang được môn sử dụng không.'), 'error')
  } finally {
    dangLuu.value = false
  }
}

function taoPayloadCapNhatChuongTrinhMon(mon, duLieuCapNhat = {}) {
  return {
    chuongTrinhVersionId: mon.chuongTrinhVersionId,
    monHocId: mon.monHocId || mon.monId,
    maMonTrongCt: mon.maMonTrongCt,
    khungKyId: mon.khungKyId ?? null,
    nhomKienThucId: duLieuCapNhat.nhomKienThucId !== undefined ? duLieuCapNhat.nhomKienThucId : mon.nhomKienThucId || null,

    loai: mon.loai || 'bat_buoc',
    loaiHocPhan: mon.loaiHocPhan || 'mon_hoc',
    loaiPhamVi: duLieuCapNhat.loaiPhamVi !== undefined ? duLieuCapNhat.loaiPhamVi : mon.loaiPhamVi || 'mon_chuyen_nganh',
    batBuoc: mon.batBuoc !== false,
    laMonDieuKien: mon.laMonDieuKien === true,

    thuTu: mon.thuTu ?? null,
    ghiChu: mon.ghiChu || ''
  }
}

async function capNhatLoaiPhamVi(mon, loaiPhamVi) {
  if (!mon?.id) {
    baoTin('Không xác định được môn trong chương trình.', 'error')
    return
  }

  dangLuu.value = true

  try {
    await services.chuongTrinhMon.update(
        mon.id,
        taoPayloadCapNhatChuongTrinhMon(mon, { loaiPhamVi })
    )

    await taiDuLieuCoSanTatCaBang()
    baoTin('Đã cập nhật loại phạm vi môn.')
  } catch (error) {
    await taiDuLieuCoSanTatCaBang()
    baoTin(layThongBaoLoi(error, 'Không cập nhật được loại phạm vi môn.'), 'error')
  } finally {
    dangLuu.value = false
  }
}

async function capNhatNhomKienThucChoMon(mon, nhomKienThucId) {
  if (!mon?.id) {
    baoTin('Không xác định được môn trong chương trình.', 'error')
    return
  }

  const nhomIdMoi = nhomKienThucId || null

  if (nhomIdMoi) {
    const nhom = danhSachNhomKienThucTheoVersion.value.find((item) => {
      return String(item.id || '') === String(nhomIdMoi || '')
    })

    if (!nhom) {
      baoTin('Nhóm kiến thức không thuộc version đang chọn.', 'error')
      await taiDuLieuCoSanTatCaBang()
      return
    }

    if (String(mon.chuongTrinhVersionId || '') !== String(nhom.chuongTrinhVersionId || '')) {
      baoTin('Nhóm kiến thức không thuộc version của môn này.', 'error')
      await taiDuLieuCoSanTatCaBang()
      return
    }
  }

  dangLuu.value = true

  try {
    await services.chuongTrinhMon.update(
        mon.id,
        taoPayloadCapNhatChuongTrinhMon(mon, { nhomKienThucId: nhomIdMoi })
    )

    await taiDuLieuCoSanTatCaBang()
    baoTin(nhomIdMoi ? 'Đã gán môn vào nhóm kiến thức.' : 'Đã bỏ nhóm kiến thức khỏi môn.')
  } catch (error) {
    await taiDuLieuCoSanTatCaBang()
    baoTin(layThongBaoLoi(error, 'Không cập nhật được nhóm kiến thức của môn.'), 'error')
  } finally {
    dangLuu.value = false
  }
}

async function ganMonVaoNhom(mon) {
  if (!mon?.id) {
    baoTin('Không xác định được môn trong chương trình.', 'error')
    return
  }

  if (!nhomKienThucDangChon.value?.id) {
    baoTin('Cần chọn nhóm kiến thức trước.', 'error')
    return
  }

  await capNhatNhomKienThucChoMon(mon, nhomKienThucDangChon.value.id)
}

async function boMonKhoiNhom(mon) {
  await capNhatNhomKienThucChoMon(mon, '')
}

async function boGanMonKhoiVersion(mon) {
  if (!mon?.id) {
    baoTin('Không xác định được môn trong chương trình.', 'error')
    return
  }

  const tenMon = mon.tenMon || tenMonHoc(mon.monHocId)
  const dongY = window.confirm(`Bỏ gán môn "${tenMon}" khỏi version đang chọn?`)

  if (!dongY) return

  dangLuu.value = true

  try {
    await services.chuongTrinhMon.delete(mon.id)
    await taiDuLieuCoSanTatCaBang()
    baoTin('Đã bỏ gán môn khỏi version.')
  } catch (error) {
    await taiDuLieuCoSanTatCaBang()
    baoTin(layThongBaoLoi(error, 'Không bỏ gán được môn khỏi version.'), 'error')
  } finally {
    dangLuu.value = false
  }
}

onMounted(taiDuLieu)
</script>

<style scoped>
.admin-nhom-kien-thuc-mon {
  display: grid;
  gap: 12px;
  padding: 12px;
  font-family: Roboto, Arial, sans-serif;
}

.thong-bao {
  padding: 10px 12px;
  border: 1px solid #bbf7d0;
  border-radius: 4px;
  background: #f0fdf4;
  color: #166534;
  font-size: 13px;
}

.thong-bao.error {
  border-color: #fecaca;
  background: #fef2f2;
  color: #b91c1c;
}

.khoi-dau,
.bo-loc,
.tong-quan,
.the,
.hop-rong {
  border: 1px solid #d1d5db;
  border-radius: 5px;
  background: #ffffff;
}

.khoi-dau {
  display: flex;
  justify-content: space-between;
  gap: 12px;
  padding: 12px;
}

.khoi-dau h2,
.tieu-de-the h3 {
  margin: 0;
  color: #111827;
}

.khoi-dau p,
.tieu-de-the p {
  margin: 4px 0 0;
  color: #6b7280;
  font-size: 13px;
}

.bo-loc {
  display: grid;
  grid-template-columns: minmax(0, 3fr) minmax(0, 7fr);
  gap: 10px;
  padding: 12px;
  max-width: 100%;
  overflow: hidden;
}

.bo-loc label {
  min-width: 0;
}

.bo-loc select {
  width: 100%;
  max-width: 100%;
  min-width: 0;
  box-sizing: border-box;
}

.bo-loc label,
.form-nhom label,
.form-gan-version label {
  display: grid;
  gap: 4px;
}

.bo-loc span,
.form-nhom span,
.form-gan-version span {
  color: #374151;
  font-size: 12px;
  font-weight: 700;
}

.bo-loc select,
.form-nhom input,
.form-nhom select,
.form-nhom textarea,
.form-gan-version select,
.bo-loc-mon-mau input,
.chon-nhanh {
  min-height: 32px;
  border: 1px solid #cbd5e1;
  border-radius: 4px;
  padding: 5px 8px;
  font-size: 13px;
  font-family: Roboto, Arial, sans-serif;
  background: #ffffff;
  box-sizing: border-box;
}

.chon-nhanh {
  width: 100%;
  min-width: 170px;
}

.chon-nhanh:disabled {
  cursor: not-allowed;
  opacity: 0.65;
}

.form-nhom textarea {
  resize: vertical;
}

.form-nhom b,
.form-gan-version b {
  color: #dc2626;
}

.hop-rong {
  padding: 18px;
  color: #6b7280;
  text-align: center;
}

.tong-quan {
  display: grid;
  grid-template-columns: repeat(4, minmax(140px, 1fr));
  gap: 8px;
  padding: 12px;
}

.tong-quan div {
  display: grid;
  gap: 4px;
  padding: 8px;
  border-radius: 4px;
  background: #f8fafc;
}

.tong-quan b {
  color: #475569;
  font-size: 12px;
}

.tong-quan span {
  color: #111827;
  font-size: 13px;
  font-weight: 700;
}

.luoi-chinh {
  display: grid;
  grid-template-columns: minmax(420px, 0.95fr) minmax(520px, 1.2fr);
  gap: 12px;
}

.tieu-de-the {
  display: flex;
  justify-content: space-between;
  gap: 10px;
  padding: 12px;
  border-bottom: 1px solid #e5e7eb;
  background: #f9fafb;
}

.nhom-dang-chon {
  display: grid;
  gap: 3px;
  align-self: start;
  padding: 6px 8px;
  border-radius: 4px;
  background: #eff6ff;
  color: #1d4ed8;
  font-size: 12px;
}

.nhom-dang-chon small {
  color: #475569;
  font-weight: 700;
}

.form-nhom {
  display: grid;
  grid-template-columns: repeat(2, minmax(160px, 1fr));
  gap: 10px;
  padding: 12px;
}

.form-gan-version {
  display: grid;
  grid-template-columns: minmax(180px, 1fr) minmax(220px, 1fr) minmax(180px, 1fr) auto;
  gap: 10px;
  padding: 12px;
  align-items: end;
}

.cot-rong {
  grid-column: 1 / -1;
}

.hang-nut {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.bo-loc-mon-mau {
  display: grid;
  gap: 10px;
  padding: 12px;
  border-bottom: 1px solid #e5e7eb;
}

.bo-loc-mon-mau label {
  display: grid;
  gap: 4px;
}

.bo-loc-mon-mau span {
  color: #374151;
  font-size: 12px;
  font-weight: 700;
}

.bang-boc {
  overflow: auto;
}

.bang-mon-mau-cuon {
  max-height: 460px;
  overflow: auto;
}

.bang-mon-cuon {
  max-height: 900px;
  overflow: auto;
}

table {
  width: 100%;
  min-width: 720px;
  border-collapse: collapse;
  font-size: 13px;
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
  color: #64748b;
}

tr.dang-chon td {
  background: #eff6ff;
}

.rong {
  color: #6b7280;
  text-align: center;
}

.nhan {
  display: inline-flex;
  align-items: center;
  min-height: 24px;
  padding: 3px 8px;
  border-radius: 999px;
  font-size: 11px;
  font-weight: 700;
}

.nhan.da-gan {
  background: #dcfce7;
  color: #166534;
}

.nhan.chua-gan {
  background: #f1f5f9;
  color: #475569;
}

.cot-thao-tac {
  width: 170px;
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

@media (max-width: 1200px) {
  .luoi-chinh {
    grid-template-columns: 1fr;
  }

  .tong-quan {
    grid-template-columns: repeat(2, minmax(140px, 1fr));
  }

  .form-gan-version {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 700px) {
  .khoi-dau {
    flex-direction: column;
  }

  .bo-loc,
  .tong-quan,
  .form-nhom,
  .form-gan-version {
    grid-template-columns: 1fr;
  }
}
</style>