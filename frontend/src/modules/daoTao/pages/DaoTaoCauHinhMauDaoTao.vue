<template>
  <div class="cau-hinh-mau-dao-tao">
    <div v-if="thongBao" :class="['thong-bao', loaiThongBao]">{{ thongBao }}</div>

    <section class="khoi-dau">
      <div>
        <h2>Cấu hình mẫu đào tạo</h2>
        <p>Quản lý danh mục Trình độ đào tạo và Loại chương trình dùng chung toàn hệ thống.</p>
      </div>
    </section>

    <div class="luoi-2-cot">
      <!-- ===== TRÌNH ĐỘ ĐÀO TẠO ===== -->
      <div class="the">
        <div class="tieu-de-the">
          <h3>{{ formTrinhDo.id ? 'Cập nhật trình độ' : 'Thêm trình độ đào tạo' }}</h3>
        </div>
        <form class="form-nho" @submit.prevent="luuTrinhDo">
          <label>
            <span>Mã trình độ <b>*</b></span>
            <input v-model.trim="formTrinhDo.maTrinhDo" placeholder="VD: TC, CD, DH">
          </label>
          <label>
            <span>Tên trình độ <b>*</b></span>
            <input v-model.trim="formTrinhDo.tenTrinhDo" placeholder="VD: Trung cấp">
          </label>
          <label class="cot-rong">
            <span>Mô tả</span>
            <textarea v-model.trim="formTrinhDo.moTa" rows="2"></textarea>
          </label>
          <div class="hang-nut cot-rong">
            <button type="submit" class="nut chinh" :disabled="dangLuu">
              {{ formTrinhDo.id ? 'Cập nhật' : 'Thêm' }}
            </button>
            <button type="button" class="nut" @click="resetTrinhDo">Làm mới</button>
          </div>
        </form>

        <div class="bang-boc">
          <table>
            <thead>
              <tr>
                <th>Mã</th>
                <th>Tên trình độ</th>
                <th>Mô tả</th>
                <th class="cot-thao-tac">Thao tác</th>
              </tr>
            </thead>
            <tbody>
              <tr v-if="!danhSachTrinhDo.length">
                <td colspan="4" class="rong">Chưa có trình độ nào.</td>
              </tr>
              <tr v-for="td in danhSachTrinhDo" :key="td.id" :class="{ 'dang-chon': td.id === formTrinhDo.id }">
                <td>{{ td.maTrinhDo }}</td>
                <td><b>{{ td.tenTrinhDo }}</b><small>ID: {{ td.id }}</small></td>
                <td>{{ td.moTa || '-' }}</td>
                <td class="cot-thao-tac">
                  <button class="nut-bang sua" @click="suaTrinhDo(td)">Sửa</button>
                  <button class="nut-bang xoa" @click="xoaTrinhDo(td.id)">Xóa</button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <!-- ===== LOẠI CHƯƠNG TRÌNH ===== -->
      <div class="the">
        <div class="tieu-de-the">
          <h3>{{ formLoai.id ? 'Cập nhật loại chương trình' : 'Thêm loại chương trình' }}</h3>
        </div>
        <form class="form-nho" @submit.prevent="luuLoai">
          <label>
            <span>Mã loại <b>*</b></span>
            <input v-model.trim="formLoai.maLoai" placeholder="VD: CQ, VLVH, LT">
          </label>
          <label>
            <span>Tên loại <b>*</b></span>
            <input v-model.trim="formLoai.tenLoai" placeholder="VD: Chính quy">
          </label>
          <label>
            <span>Số tháng <b>*</b></span>
            <input v-model.number="formLoai.soThang" type="number" min="1" placeholder="24">
          </label>
          <label>
            <span>Số kỳ <b>*</b></span>
            <input v-model.number="formLoai.soKy" type="number" min="1" placeholder="4">
          </label>
          <label class="cot-rong">
            <span>Mô tả</span>
            <textarea v-model.trim="formLoai.moTa" rows="2"></textarea>
          </label>
          <div class="hang-nut cot-rong">
            <button type="submit" class="nut chinh" :disabled="dangLuu">
              {{ formLoai.id ? 'Cập nhật' : 'Thêm' }}
            </button>
            <button type="button" class="nut" @click="resetLoai">Làm mới</button>
          </div>
        </form>

        <div class="bang-boc">
          <table>
            <thead>
              <tr>
                <th>Mã</th>
                <th>Tên loại</th>
                <th>Tháng/Kỳ</th>
                <th class="cot-thao-tac">Thao tác</th>
              </tr>
            </thead>
            <tbody>
              <tr v-if="!danhSachLoai.length">
                <td colspan="4" class="rong">Chưa có loại chương trình nào.</td>
              </tr>
              <tr v-for="lct in danhSachLoai" :key="lct.id" :class="{ 'dang-chon': lct.id === formLoai.id }">
                <td>{{ lct.maLoai }}</td>
                <td><b>{{ lct.tenLoai }}</b><small>ID: {{ lct.id }}</small></td>
                <td>{{ lct.soThang }} tháng / {{ lct.soKy }} kỳ</td>
                <td class="cot-thao-tac">
                  <button class="nut-bang sua" @click="suaLoai(lct)">Sửa</button>
                  <button class="nut-bang xoa" @click="xoaLoai(lct.id)">Xóa</button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { daoTaoApi } from '../api/daoTaoApi'

const thongBao = ref('')
const loaiThongBao = ref('thanh-cong')
const dangLuu = ref(false)

// === Trình độ ===
const danhSachTrinhDo = ref([])
const formTrinhDo = ref({ id: null, maTrinhDo: '', tenTrinhDo: '', moTa: '' })

async function taiTrinhDo() {
  try {
    const res = await daoTaoApi.trinhDoDaoTao.getAll({ size: 500 })
    danhSachTrinhDo.value = res?.content ?? (Array.isArray(res) ? res : [])
  } catch { danhSachTrinhDo.value = [] }
}

function suaTrinhDo(td) {
  formTrinhDo.value = { id: td.id, maTrinhDo: td.maTrinhDo, tenTrinhDo: td.tenTrinhDo, moTa: td.moTa || '' }
}

function resetTrinhDo() {
  formTrinhDo.value = { id: null, maTrinhDo: '', tenTrinhDo: '', moTa: '' }
}

async function luuTrinhDo() {
  if (!formTrinhDo.value.maTrinhDo || !formTrinhDo.value.tenTrinhDo) {
    hienThongBao('Vui lòng nhập mã và tên trình độ.', 'loi'); return
  }
  dangLuu.value = true
  try {
    const payload = { maTrinhDo: formTrinhDo.value.maTrinhDo, tenTrinhDo: formTrinhDo.value.tenTrinhDo, moTa: formTrinhDo.value.moTa }
    if (formTrinhDo.value.id) {
      await daoTaoApi.trinhDoDaoTao.update(formTrinhDo.value.id, payload)
    } else {
      await daoTaoApi.trinhDoDaoTao.create(payload)
    }
    hienThongBao('Lưu trình độ thành công!')
    resetTrinhDo()
    await taiTrinhDo()
  } catch (e) {
    hienThongBao(e.response?.data?.message || 'Lỗi khi lưu trình độ.', 'loi')
  } finally { dangLuu.value = false }
}

async function xoaTrinhDo(id) {
  if (!confirm('Xóa trình độ này?')) return
  try {
    await daoTaoApi.trinhDoDaoTao.delete(id)
    hienThongBao('Đã xóa trình độ.')
    await taiTrinhDo()
  } catch (e) {
    hienThongBao(e.response?.data?.message || 'Không thể xóa.', 'loi')
  }
}

// === Loại chương trình ===
const danhSachLoai = ref([])
const formLoai = ref({ id: null, maLoai: '', tenLoai: '', soThang: null, soKy: null, moTa: '' })

async function taiLoai() {
  try {
    const res = await daoTaoApi.loaiChuongTrinh.getAll({ size: 500 })
    danhSachLoai.value = res?.content ?? (Array.isArray(res) ? res : [])
  } catch { danhSachLoai.value = [] }
}

function suaLoai(lct) {
  formLoai.value = { id: lct.id, maLoai: lct.maLoai, tenLoai: lct.tenLoai, soThang: lct.soThang, soKy: lct.soKy, moTa: lct.moTa || '' }
}

function resetLoai() {
  formLoai.value = { id: null, maLoai: '', tenLoai: '', soThang: null, soKy: null, moTa: '' }
}

async function luuLoai() {
  if (!formLoai.value.maLoai || !formLoai.value.tenLoai) {
    hienThongBao('Vui lòng nhập mã và tên loại.', 'loi'); return
  }
  dangLuu.value = true
  try {
    const payload = { maLoai: formLoai.value.maLoai, tenLoai: formLoai.value.tenLoai, soThang: formLoai.value.soThang, soKy: formLoai.value.soKy, moTa: formLoai.value.moTa }
    if (formLoai.value.id) {
      await daoTaoApi.loaiChuongTrinh.update(formLoai.value.id, payload)
    } else {
      await daoTaoApi.loaiChuongTrinh.create(payload)
    }
    hienThongBao('Lưu loại chương trình thành công!')
    resetLoai()
    await taiLoai()
  } catch (e) {
    hienThongBao(e.response?.data?.message || 'Lỗi khi lưu loại chương trình.', 'loi')
  } finally { dangLuu.value = false }
}

async function xoaLoai(id) {
  if (!confirm('Xóa loại chương trình này?')) return
  try {
    await daoTaoApi.loaiChuongTrinh.delete(id)
    hienThongBao('Đã xóa loại chương trình.')
    await taiLoai()
  } catch (e) {
    hienThongBao(e.response?.data?.message || 'Không thể xóa.', 'loi')
  }
}

function hienThongBao(msg, loai = 'thanh-cong') {
  thongBao.value = msg
  loaiThongBao.value = loai
  setTimeout(() => { thongBao.value = '' }, 3000)
}

onMounted(() => { taiTrinhDo(); taiLoai() })
</script>

<style scoped>
.cau-hinh-mau-dao-tao { display: grid; gap: 16px; }
.khoi-dau { display: flex; justify-content: space-between; align-items: flex-start; gap: 16px; }
.luoi-2-cot { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; }
.the { background: #fff; border: 1px solid #e5e7eb; border-radius: 8px; padding: 16px; display: grid; gap: 12px; }
.tieu-de-the h3 { margin: 0; font-size: 15px; color: #111827; }
.form-nho { display: grid; grid-template-columns: 1fr 1fr; gap: 10px; }
.form-nho label { display: flex; flex-direction: column; gap: 4px; font-size: 13px; }
.form-nho label span { color: #6b7280; }
.form-nho input, .form-nho textarea, .form-nho select { border: 1px solid #d1d5db; border-radius: 5px; padding: 5px 8px; font-size: 13px; }
.cot-rong { grid-column: span 2; }
.hang-nut { display: flex; gap: 8px; }
.nut { padding: 6px 14px; border: 1px solid #d1d5db; border-radius: 5px; cursor: pointer; font-size: 13px; background: #f9fafb; }
.nut.chinh { background: #2563eb; color: #fff; border-color: #2563eb; }
.nut:disabled { opacity: 0.5; cursor: not-allowed; }
.bang-boc { overflow-x: auto; }
table { width: 100%; border-collapse: collapse; font-size: 13px; }
th, td { border: 1px solid #e5e7eb; padding: 6px 10px; text-align: left; }
th { background: #f3f4f6; font-weight: 600; }
td small { display: block; color: #9ca3af; font-size: 11px; }
tr.dang-chon { background: #eff6ff; }
.rong { color: #9ca3af; font-style: italic; text-align: center; }
.cot-thao-tac { width: 100px; }
.nut-bang { padding: 3px 8px; border-radius: 4px; cursor: pointer; font-size: 12px; border: 1px solid; }
.nut-bang.sua { background: #eff6ff; color: #2563eb; border-color: #bfdbfe; }
.nut-bang.xoa { background: #fef2f2; color: #dc2626; border-color: #fecaca; margin-left: 4px; }
.thong-bao { padding: 10px 14px; border-radius: 6px; font-size: 13px; }
.thong-bao.thanh-cong { background: #f0fdf4; color: #16a34a; border: 1px solid #bbf7d0; }
.thong-bao.loi { background: #fef2f2; color: #dc2626; border: 1px solid #fecaca; }
@media (max-width: 900px) { .luoi-2-cot { grid-template-columns: 1fr; } }
</style>
