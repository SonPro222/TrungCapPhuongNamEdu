<template>
  <div class="cau-hinh-nganh-he">
    <div v-if="thongBao" :class="['thong-bao', loaiThongBao]">{{ thongBao }}</div>

    <section class="khoi-dau">
      <div>
        <h2>Cấu hình ngành hệ đào tạo</h2>
        <p>Mỗi ngành hệ đào tạo = Ngành + Trình độ + Loại chương trình + Số tháng + Số kỳ.</p>
      </div>
      <button type="button" class="nut" :disabled="dangTai" @click="taiDuLieu">Tải lại</button>
    </section>

    <!-- Bộ lọc ngành -->
    <div class="bo-loc-ngang">
      <label>
        <span>Lọc theo ngành</span>
        <select v-model="locNganhId" @change="taiDanhSach">
          <option :value="null">— Tất cả —</option>
          <option v-for="n in danhSachNganh" :key="n.id" :value="n.id">{{ n.maNganh }} – {{ n.tenNganh }}</option>
        </select>
      </label>
    </div>

    <div class="luoi-2-cot">
      <!-- Form thêm/sửa -->
      <div class="the">
        <div class="tieu-de-the">
          <h3>{{ form.id ? 'Cập nhật ngành hệ đào tạo' : 'Thêm ngành hệ đào tạo' }}</h3>
        </div>
        <form class="form-nho" @submit.prevent="luu">
          <label class="cot-rong">
            <span>Ngành <b>*</b></span>
            <select v-model="form.nganhId">
              <option :value="null">— Chọn ngành —</option>
              <option v-for="n in danhSachNganh" :key="n.id" :value="n.id">{{ n.maNganh }} – {{ n.tenNganh }}</option>
            </select>
          </label>
          <label>
            <span>Trình độ <b>*</b></span>
            <select v-model="form.trinhDoId">
              <option :value="null">— Chọn trình độ —</option>
              <option v-for="td in danhSachTrinhDo" :key="td.id" :value="td.id">{{ td.maTrinhDo }} – {{ td.tenTrinhDo }}</option>
            </select>
          </label>
          <label>
            <span>Loại chương trình <b>*</b></span>
            <select v-model="form.loaiChuongTrinhId">
              <option :value="null">— Chọn loại —</option>
              <option v-for="lct in danhSachLoai" :key="lct.id" :value="lct.id">{{ lct.maLoai }} – {{ lct.tenLoai }}</option>
            </select>
          </label>
          <label>
            <span>Số tháng</span>
            <input v-model.number="form.soThang" type="number" min="1" placeholder="24">
          </label>
          <label>
            <span>Số kỳ</span>
            <input v-model.number="form.soKy" type="number" min="1" placeholder="4">
          </label>
          <label>
            <span>Mã hệ</span>
            <input v-model.trim="form.maHe" placeholder="VD: TC_CQ">
          </label>
          <label>
            <span>Tên hệ</span>
            <input v-model.trim="form.tenHe" placeholder="VD: Trung cấp chính quy">
          </label>
          <label>
            <span>Trạng thái</span>
            <select v-model="form.trangThai">
              <option value="dang_su_dung">Đang sử dụng</option>
              <option value="ngung_su_dung">Ngừng sử dụng</option>
            </select>
          </label>
          <label class="cot-rong">
            <span>Ghi chú</span>
            <textarea v-model.trim="form.ghiChu" rows="2"></textarea>
          </label>
          <div class="hang-nut cot-rong">
            <button type="submit" class="nut chinh" :disabled="dangLuu">{{ form.id ? 'Cập nhật' : 'Thêm' }}</button>
            <button type="button" class="nut" @click="resetForm">Làm mới</button>
          </div>
        </form>
      </div>

      <!-- Danh sách -->
      <div class="the">
        <div class="tieu-de-the">
          <h3>Danh sách ngành hệ đào tạo <span class="badge">{{ danhSach.length }}</span></h3>
        </div>
        <div class="bang-boc">
          <table>
            <thead>
              <tr>
                <th>Ngành</th>
                <th>Trình độ</th>
                <th>Loại</th>
                <th>Tháng/Kỳ</th>
                <th>Trạng thái</th>
                <th class="cot-thao-tac">Thao tác</th>
              </tr>
            </thead>
            <tbody>
              <tr v-if="!danhSach.length">
                <td colspan="6" class="rong">Chưa có ngành hệ đào tạo nào.</td>
              </tr>
              <tr v-for="item in danhSach" :key="item.id" :class="{ 'dang-chon': item.id === form.id }">
                <td>
                  <b>{{ item.tenNganh || item.nganhId }}</b>
                  <small>{{ item.maNganh }}</small>
                </td>
                <td>{{ item.tenTrinhDo || item.trinhDoId }}</td>
                <td>{{ item.tenLoai || item.loaiChuongTrinhId }}</td>
                <td>{{ item.soThang ? item.soThang + 't' : '-' }} / {{ item.soKy ? item.soKy + 'k' : '-' }}</td>
                <td>
                  <span :class="item.trangThai === 'dang_su_dung' ? 'trang-thai-on' : 'trang-thai-off'">
                    {{ item.trangThai === 'dang_su_dung' ? 'Đang dùng' : 'Ngừng' }}
                  </span>
                </td>
                <td class="cot-thao-tac">
                  <button class="nut-bang sua" @click="suaItem(item)">Sửa</button>
                  <button class="nut-bang xoa" @click="xoaItem(item.id)">Xóa</button>
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
import { ref, onMounted } from 'vue'
import { daoTaoApi } from '../api/daoTaoApi'

const thongBao = ref('')
const loaiThongBao = ref('thanh-cong')
const dangTai = ref(false)
const dangLuu = ref(false)
const locNganhId = ref(null)

const danhSach = ref([])
const danhSachNganh = ref([])
const danhSachTrinhDo = ref([])
const danhSachLoai = ref([])

const defaultForm = () => ({ id: null, nganhId: null, trinhDoId: null, loaiChuongTrinhId: null, soThang: null, soKy: null, maHe: '', tenHe: '', trangThai: 'dang_su_dung', ghiChu: '' })
const form = ref(defaultForm())

async function taiDuLieu() {
  dangTai.value = true
  try {
    const [rNganh, rTrinhDo, rLoai] = await Promise.all([
      daoTaoApi.nganh.getAll({ size: 500 }),
      daoTaoApi.trinhDoDaoTao.getOptions(),
      daoTaoApi.loaiChuongTrinh.getOptions()
    ])
    danhSachNganh.value = rNganh?.content ?? (Array.isArray(rNganh) ? rNganh : [])
    danhSachTrinhDo.value = Array.isArray(rTrinhDo) ? rTrinhDo : []
    danhSachLoai.value = Array.isArray(rLoai) ? rLoai : []
    await taiDanhSach()
  } catch (e) {
    hienThongBao('Lỗi khi tải dữ liệu.', 'loi')
  } finally { dangTai.value = false }
}

async function taiDanhSach() {
  try {
    const params = { size: 500 }
    if (locNganhId.value) params.nganhId = locNganhId.value
    const res = await daoTaoApi.nganhHeDaoTao.getAll(params)
    danhSach.value = res?.content ?? (Array.isArray(res) ? res : [])
  } catch { danhSach.value = [] }
}

function suaItem(item) {
  form.value = {
    id: item.id,
    nganhId: item.nganhId,
    trinhDoId: item.trinhDoId,
    loaiChuongTrinhId: item.loaiChuongTrinhId,
    soThang: item.soThang,
    soKy: item.soKy,
    maHe: item.maHe || '',
    tenHe: item.tenHe || '',
    trangThai: item.trangThai || 'dang_su_dung',
    ghiChu: item.ghiChu || ''
  }
}

function resetForm() {
  form.value = defaultForm()
}

async function luu() {
  if (!form.value.nganhId || !form.value.trinhDoId || !form.value.loaiChuongTrinhId) {
    hienThongBao('Vui lòng chọn đủ Ngành, Trình độ và Loại chương trình.', 'loi'); return
  }
  dangLuu.value = true
  try {
    const payload = { ...form.value }
    delete payload.id
    if (form.value.id) {
      await daoTaoApi.nganhHeDaoTao.update(form.value.id, payload)
    } else {
      await daoTaoApi.nganhHeDaoTao.create(payload)
    }
    hienThongBao('Lưu thành công!')
    resetForm()
    await taiDanhSach()
  } catch (e) {
    hienThongBao(e.response?.data?.message || 'Lỗi khi lưu.', 'loi')
  } finally { dangLuu.value = false }
}

async function xoaItem(id) {
  if (!confirm('Xóa ngành hệ đào tạo này?')) return
  try {
    await daoTaoApi.nganhHeDaoTao.delete(id)
    hienThongBao('Đã xóa.')
    await taiDanhSach()
  } catch (e) {
    hienThongBao(e.response?.data?.message || 'Không thể xóa.', 'loi')
  }
}

function hienThongBao(msg, loai = 'thanh-cong') {
  thongBao.value = msg
  loaiThongBao.value = loai
  setTimeout(() => { thongBao.value = '' }, 3500)
}

onMounted(taiDuLieu)
</script>

<style scoped>
.cau-hinh-nganh-he { display: grid; gap: 16px; }
.khoi-dau { display: flex; justify-content: space-between; align-items: flex-start; gap: 16px; }
.bo-loc-ngang { display: flex; gap: 16px; }
.bo-loc-ngang label { display: flex; flex-direction: column; gap: 4px; font-size: 13px; min-width: 260px; }
.bo-loc-ngang label span { color: #6b7280; }
.bo-loc-ngang select { border: 1px solid #d1d5db; border-radius: 5px; padding: 5px 8px; font-size: 13px; }
.luoi-2-cot { display: grid; grid-template-columns: 1fr 1.4fr; gap: 16px; align-items: start; }
.the { background: #fff; border: 1px solid #e5e7eb; border-radius: 8px; padding: 16px; display: grid; gap: 12px; }
.tieu-de-the h3 { margin: 0; font-size: 15px; color: #111827; display: flex; align-items: center; gap: 8px; }
.badge { background: #e5e7eb; color: #374151; font-size: 12px; padding: 1px 7px; border-radius: 10px; }
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
.trang-thai-on { color: #16a34a; background: #f0fdf4; padding: 2px 6px; border-radius: 4px; font-size: 11px; }
.trang-thai-off { color: #9ca3af; background: #f3f4f6; padding: 2px 6px; border-radius: 4px; font-size: 11px; }
.thong-bao { padding: 10px 14px; border-radius: 6px; font-size: 13px; }
.thong-bao.thanh-cong { background: #f0fdf4; color: #16a34a; border: 1px solid #bbf7d0; }
.thong-bao.loi { background: #fef2f2; color: #dc2626; border: 1px solid #fecaca; }
@media (max-width: 900px) { .luoi-2-cot { grid-template-columns: 1fr; } }
</style>
