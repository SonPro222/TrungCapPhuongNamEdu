<template>
  <div class="dao-tao-mon-hoc-goc">
    <div v-if="thongBao" :class="['thong-bao', loaiThongBao]">
      {{ thongBao }}
    </div>

    <section class="khoi-dau">
      <div>
        <h2>Quản lý môn gốc</h2>
        <p>
          CRUD danh mục môn học dùng chung toàn hệ thống. Môn gốc không thuộc ngành, không thuộc chương trình, không thuộc version.
        </p>
      </div>

      <button type="button" class="nut" :disabled="dangTai" @click="taiDuLieu">
        Tải lại dữ liệu
      </button>
    </section>

    <section class="luoi-chinh">
      <div class="the">
        <div class="tieu-de-the">
          <div>
            <h3>{{ formMon.id ? 'Cập nhật môn gốc' : 'Thêm môn gốc' }}</h3>
            <p>Tạo môn một lần, các ngành/version sẽ dùng lại môn này khi đưa vào chương trình.</p>
          </div>
        </div>

        <form class="form-mon" @submit.prevent="luuMonHoc">
          <label>
            <span>Mã môn</span>
            <input v-model.trim="formMon.maMon" type="text" placeholder="VD: MH01">
          </label>

          <label>
            <span>Tên môn <b>*</b></span>
            <input v-model.trim="formMon.tenMon" type="text" placeholder="VD: Tiếng Anh">
          </label>

          <label class="cot-rong">
            <span>Mô tả</span>
            <textarea v-model.trim="formMon.moTa" rows="4" placeholder="Ghi chú/mô tả môn học"></textarea>
          </label>

          <div class="hang-nut cot-rong">
            <button type="submit" class="nut chinh" :disabled="dangLuu">
              {{ formMon.id ? 'Cập nhật môn' : 'Thêm môn' }}
            </button>

            <button type="button" class="nut" :disabled="dangLuu" @click="resetFormMon">
              Làm mới form
            </button>
          </div>
        </form>
      </div>

      <div class="the">
        <div class="tieu-de-the">
          <div>
            <h3>Danh sách môn gốc</h3>
            <p>Tổng số môn đang tải: {{ danhSachMonHocGocLoc.length }}</p>
          </div>
        </div>

        <div class="bo-loc">
          <label>
            <span>Tìm môn</span>
            <input v-model.trim="tuKhoa" type="text" placeholder="Nhập mã môn hoặc tên môn">
          </label>
        </div>

        <div class="bang-boc">
          <table>
            <thead>
            <tr>
              <th>Mã môn</th>
              <th>Tên môn</th>
              <th>Mô tả</th>
              <th class="cot-thao-tac">Thao tác</th>
            </tr>
            </thead>

            <tbody>
            <tr v-if="!danhSachMonHocGocLoc.length">
              <td colspan="4" class="rong">Chưa có môn gốc phù hợp.</td>
            </tr>

            <tr
                v-for="mon in danhSachMonHocGocLoc"
                :key="mon.id"
                :class="{ 'dang-chon': String(mon.id || '') === String(formMon.id || '') }"
            >
              <td>{{ mon.maMon || mon.ma || '-' }}</td>

              <td>
                <b>{{ mon.tenMon || mon.ten || '-' }}</b>
                <small>ID: {{ mon.id }}</small>
              </td>

              <td>{{ mon.moTa || '-' }}</td>

              <td class="thao-tac">
                <button type="button" class="nut nho chinh" @click="suaMonHoc(mon)">
                  Sửa
                </button>

                <button type="button" class="nut nho nguy-hiem" :disabled="dangLuu" @click="xoaMonHoc(mon)">
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
import { useDaoTaoXemChuongTrinh } from '@/modules/daoTao/composables/useDaoTaoXemChuongTrinh'
import { layThongBaoLoi } from '@/modules/daoTao/utils/layThongBaoLoi'

const {
  duLieu,
  services,
  taiDuLieuCoSanTatCaBang
} = useDaoTaoXemChuongTrinh()

const thongBao = ref('')
const loaiThongBao = ref('success')
const dangLuu = ref(false)
const dangTai = ref(false)
const tuKhoa = ref('')

const formMon = reactive({
  id: null,
  maMon: '',
  tenMon: '',
  moTa: ''
})

const danhSachMonHocGoc = computed(() => {
  return [...(duLieu.value.monHoc || [])]
      .sort((a, b) => String(a.tenMon || a.ten || '').localeCompare(String(b.tenMon || b.ten || ''), 'vi'))
})

const danhSachMonHocGocLoc = computed(() => {
  const keyword = tuKhoa.value.trim().toLowerCase()

  if (!keyword) return danhSachMonHocGoc.value

  return danhSachMonHocGoc.value.filter((mon) => {
    const text = [
      mon.maMon,
      mon.ma,
      mon.tenMon,
      mon.ten,
      mon.moTa
    ].filter(Boolean).join(' ').toLowerCase()

    return text.includes(keyword)
  })
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
    baoTin(layThongBaoLoi(error, 'Không tải được dữ liệu môn gốc.'), 'error')
  } finally {
    dangTai.value = false
  }
}

function resetFormMon() {
  formMon.id = null
  formMon.maMon = ''
  formMon.tenMon = ''
  formMon.moTa = ''
}

function suaMonHoc(mon) {
  formMon.id = mon.id
  formMon.maMon = mon.maMon || mon.ma || ''
  formMon.tenMon = mon.tenMon || mon.ten || ''
  formMon.moTa = mon.moTa || ''
}

function taoPayloadMonHoc() {
  return {
    maMon: formMon.maMon || null,
    tenMon: formMon.tenMon,
    moTa: formMon.moTa || ''
  }
}

async function luuMonHoc() {
  if (!formMon.tenMon) {
    baoTin('Cần nhập tên môn.', 'error')
    return
  }

  dangLuu.value = true

  try {
    if (formMon.id) {
      await services.monHoc.update(formMon.id, taoPayloadMonHoc())
      baoTin('Đã cập nhật môn gốc.')
    } else {
      await services.monHoc.create(taoPayloadMonHoc())
      baoTin('Đã thêm môn gốc.')
    }

    await taiDuLieuCoSanTatCaBang()
    resetFormMon()
  } catch (error) {
    baoTin(layThongBaoLoi(error, 'Không lưu được môn gốc.'), 'error')
  } finally {
    dangLuu.value = false
  }
}

async function xoaMonHoc(mon) {
  if (!mon?.id) return

  const dongY = window.confirm(`Xóa môn gốc "${mon.tenMon || mon.ten || mon.maMon || mon.id}"?`)

  if (!dongY) return

  dangLuu.value = true

  try {
    await services.monHoc.delete(mon.id)
    await taiDuLieuCoSanTatCaBang()

    if (String(formMon.id || '') === String(mon.id || '')) {
      resetFormMon()
    }

    baoTin('Đã xóa môn gốc.')
  } catch (error) {
    baoTin(layThongBaoLoi(error, 'Không xóa được môn gốc. Kiểm tra môn này có đang được chương trình sử dụng không.'), 'error')
  } finally {
    dangLuu.value = false
  }
}
</script>

<style scoped>
.dao-tao-mon-hoc-goc {
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
.the {
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

.luoi-chinh {
  display: grid;
  grid-template-columns: minmax(320px, 0.75fr) minmax(520px, 1.25fr);
  gap: 12px;
}

.tieu-de-the {
  padding: 12px;
  border-bottom: 1px solid #e5e7eb;
  background: #f9fafb;
}

.form-mon,
.bo-loc {
  display: grid;
  gap: 10px;
  padding: 12px;
}

.form-mon {
  grid-template-columns: repeat(2, minmax(160px, 1fr));
}

.form-mon label,
.bo-loc label {
  display: grid;
  gap: 4px;
}

.form-mon span,
.bo-loc span {
  color: #374151;
  font-size: 12px;
  font-weight: 700;
}

.form-mon input,
.form-mon textarea,
.bo-loc input {
  min-height: 32px;
  border: 1px solid #cbd5e1;
  border-radius: 4px;
  padding: 5px 8px;
  font-size: 13px;
  font-family: Roboto, Arial, sans-serif;
  background: #ffffff;
  box-sizing: border-box;
}

.form-mon textarea {
  resize: vertical;
}

.form-mon b {
  color: #dc2626;
}

.cot-rong {
  grid-column: 1 / -1;
}

.hang-nut {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.bang-boc {
  max-height: 720px;
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

.cot-thao-tac {
  width: 150px;
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

@media (max-width: 1100px) {
  .luoi-chinh {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 700px) {
  .khoi-dau {
    flex-direction: column;
  }

  .form-mon {
    grid-template-columns: 1fr;
  }
}
</style>