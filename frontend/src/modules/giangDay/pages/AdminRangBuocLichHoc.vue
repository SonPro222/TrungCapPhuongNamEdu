<template>
  <section class="page">
    <header class="page-head">
      <div>
        <h1>Ràng buộc xếp lịch</h1>
        <p>Quản lý ngày nghỉ và thời gian giáo viên không khả dụng. Auto/gợi ý lịch sẽ dựa vào dữ liệu này để tránh xếp sai.</p>
      </div>
      <button type="button" class="btn" @click="taiDuLieu">Tải lại</button>
    </header>

    <div v-if="loi" class="alert error">{{ loi }}</div>
    <div v-if="thongBao" class="alert success">{{ thongBao }}</div>

    <section class="grid-2">
      <article class="table-card">
        <div class="table-head">
          <div>
            <h2>Giáo viên khả dụng / bận</h2>
            <p>Để kha dụng = Không nếu muốn chặn giáo viên trong ca/thứ/khoảng ngày.</p>
          </div>
        </div>

        <div class="form-grid">
          <label><span>Giáo viên</span><select v-model.number="formGv.giaoVienId"><option :value="null">-- Chọn --</option><option v-for="gv in danhSachGiaoVien" :key="gv.id" :value="gv.id">{{ hienThiGiaoVien(gv) }}</option></select></label>
          <label><span>Thứ</span><select v-model.number="formGv.thuTrongTuan"><option :value="null">Tất cả</option><option v-for="thu in dsThu" :key="thu.value" :value="thu.value">{{ thu.label }}</option></select></label>
          <label><span>Ca</span><select v-model.number="formGv.caHocId"><option :value="null">Tất cả</option><option v-for="ca in danhSachCaHoc" :key="ca.id" :value="ca.id">{{ hienThiCaHoc(ca) }}</option></select></label>
          <label><span>Từ ngày</span><input v-model="formGv.tuNgay" type="date" /></label>
          <label><span>Đến ngày</span><input v-model="formGv.denNgay" type="date" /></label>
          <label><span>Trạng thái</span><select v-model="formGv.khaDung"><option :value="false">Không khả dụng / Bận</option><option :value="true">Khả dụng</option></select></label>
          <label class="wide"><span>Ghi chú</span><input v-model="formGv.ghiChu" placeholder="VD: dạy trường khác, nghỉ phép..." /></label>
        </div>

        <div class="actions"><button type="button" class="btn primary" @click="luuGiaoVienKhaDung">{{ formGv.id ? 'Cập nhật' : 'Thêm ràng buộc' }}</button><button type="button" class="btn" @click="resetFormGv">Làm mới</button></div>

        <div class="table-wrap">
          <table class="data-table">
            <thead><tr><th>Giáo viên</th><th>Thứ</th><th>Ca</th><th>Khoảng ngày</th><th>Trạng thái</th><th></th></tr></thead>
            <tbody>
              <tr v-if="!danhSachGvKhaDung.length"><td colspan="6" class="empty">Chưa có ràng buộc giáo viên.</td></tr>
              <tr v-for="item in danhSachGvKhaDung" :key="item.id">
                <td>{{ tenGiaoVienTheoId(item.giaoVienId) }}</td>
                <td>{{ tenThu(item.thuTrongTuan) }}</td>
                <td>{{ tenCaTheoId(item.caHocId) }}</td>
                <td>{{ item.tuNgay || '...' }} → {{ item.denNgay || '...' }}</td>
                <td><span :class="['status', item.khaDung ? 'done' : 'cancel']">{{ item.khaDung ? 'Khả dụng' : 'Không khả dụng' }}</span></td>
                <td class="row-actions"><button class="btn" @click="suaGiaoVienKhaDung(item)">Sửa</button><button class="btn danger" @click="xoaGiaoVienKhaDung(item)">Xóa</button></td>
              </tr>
            </tbody>
          </table>
        </div>
      </article>

      <article class="table-card">
        <div class="table-head"><div><h2>Ngày nghỉ</h2><p>Auto/gợi ý lịch sẽ bỏ qua những ngày này.</p></div></div>

        <div class="form-grid one">
          <label><span>Ngày</span><input v-model="formNgayNghi.ngay" type="date" /></label>
          <label><span>Tên ngày nghỉ</span><input v-model="formNgayNghi.tenNgayNghi" placeholder="VD: Lễ, nghỉ trường..." /></label>
          <label><span>Ghi chú</span><input v-model="formNgayNghi.ghiChu" /></label>
        </div>

        <div class="actions"><button type="button" class="btn primary" @click="luuNgayNghi">{{ formNgayNghi.id ? 'Cập nhật' : 'Thêm ngày nghỉ' }}</button><button type="button" class="btn" @click="resetFormNgayNghi">Làm mới</button></div>

        <div class="table-wrap">
          <table class="data-table">
            <thead><tr><th>Ngày</th><th>Tên</th><th>Ghi chú</th><th></th></tr></thead>
            <tbody>
              <tr v-if="!danhSachNgayNghi.length"><td colspan="4" class="empty">Chưa có ngày nghỉ.</td></tr>
              <tr v-for="item in danhSachNgayNghi" :key="item.id"><td>{{ item.ngay }}</td><td>{{ item.tenNgayNghi || '-' }}</td><td>{{ item.ghiChu || '-' }}</td><td class="row-actions"><button class="btn" @click="suaNgayNghi(item)">Sửa</button><button class="btn danger" @click="xoaNgayNghi(item)">Xóa</button></td></tr>
            </tbody>
          </table>
        </div>
      </article>
    </section>
  </section>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { giangDayService } from '../services/giangDayService'

const loi = ref('')
const thongBao = ref('')
const danhSachGiaoVien = ref([])
const danhSachCaHoc = ref([])
const danhSachGvKhaDung = ref([])
const danhSachNgayNghi = ref([])
const dsThu = [
  { value: 1, label: 'Thứ 2' }, { value: 2, label: 'Thứ 3' }, { value: 3, label: 'Thứ 4' },
  { value: 4, label: 'Thứ 5' }, { value: 5, label: 'Thứ 6' }, { value: 6, label: 'Thứ 7' }, { value: 7, label: 'Chủ nhật' }
]

const formGv = ref(taoFormGv())
const formNgayNghi = ref(taoFormNgayNghi())

onMounted(() => taiDuLieu())

async function taiDuLieu() {
  loi.value = ''
  try {
    const [gv, ca, gvkd, ngayNghi] = await Promise.all([
      giangDayService.layDanhSachGiaoVien(),
      giangDayService.layDanhSachCaHoc(),
      giangDayService.layTrangGiaoVienKhaDung(),
      giangDayService.layTrangNgayNghi()
    ])
    danhSachGiaoVien.value = gv
    danhSachCaHoc.value = ca
    danhSachGvKhaDung.value = gvkd.content
    danhSachNgayNghi.value = ngayNghi.content
  } catch (error) {
    loi.value = layLoi(error, 'Không tải được ràng buộc lịch học')
  }
}

async function luuGiaoVienKhaDung() {
  try {
    if (!formGv.value.giaoVienId) throw new Error('Phải chọn giáo viên')
    if (formGv.value.id) await giangDayService.capNhatGiaoVienKhaDung(formGv.value.id, formGv.value)
    else await giangDayService.taoGiaoVienKhaDung(formGv.value)
    thongBao.value = 'Đã lưu ràng buộc giáo viên'
    resetFormGv()
    await taiDuLieu()
  } catch (error) { loi.value = layLoi(error, 'Không lưu được ràng buộc giáo viên') }
}

async function xoaGiaoVienKhaDung(item) {
  if (!window.confirm('Xóa ràng buộc giáo viên này?')) return
  await giangDayService.xoaGiaoVienKhaDung(item.id)
  await taiDuLieu()
}

function suaGiaoVienKhaDung(item) { formGv.value = { ...item } }
function resetFormGv() { formGv.value = taoFormGv() }
function taoFormGv() { return { id: null, giaoVienId: null, thuTrongTuan: null, caHocId: null, tuNgay: '', denNgay: '', khaDung: false, ghiChu: '' } }

async function luuNgayNghi() {
  try {
    if (!formNgayNghi.value.ngay) throw new Error('Phải chọn ngày nghỉ')
    if (formNgayNghi.value.id) await giangDayService.capNhatNgayNghi(formNgayNghi.value.id, formNgayNghi.value)
    else await giangDayService.taoNgayNghi(formNgayNghi.value)
    thongBao.value = 'Đã lưu ngày nghỉ'
    resetFormNgayNghi()
    await taiDuLieu()
  } catch (error) { loi.value = layLoi(error, 'Không lưu được ngày nghỉ') }
}

async function xoaNgayNghi(item) {
  if (!window.confirm('Xóa ngày nghỉ này?')) return
  await giangDayService.xoaNgayNghi(item.id)
  await taiDuLieu()
}

function suaNgayNghi(item) { formNgayNghi.value = { ...item } }
function resetFormNgayNghi() { formNgayNghi.value = taoFormNgayNghi() }
function taoFormNgayNghi() { return { id: null, ngay: '', tenNgayNghi: '', ghiChu: '' } }

function hienThiGiaoVien(item) { return item ? `${item.maGiaoVien ? item.maGiaoVien + ' - ' : ''}${item.hoTen || item.tenGiaoVien || item.id}` : '-' }
function hienThiCaHoc(item) { return item ? `${item.maCa || item.id} - ${item.tenCa || ''}` : '-' }
function tenGiaoVienTheoId(id) { return hienThiGiaoVien(danhSachGiaoVien.value.find((item) => Number(item.id) === Number(id))) }
function tenCaTheoId(id) { return id ? hienThiCaHoc(danhSachCaHoc.value.find((item) => Number(item.id) === Number(id))) : 'Tất cả ca' }
function tenThu(value) { return dsThu.find((item) => Number(item.value) === Number(value))?.label || 'Tất cả thứ' }
function layLoi(error, fallback) { return error?.response?.data?.message || error?.message || fallback }
</script>

<style scoped>
.page { display: flex; flex-direction: column; gap: 14px; color: #344054; font-size: 14px; }
.page-head, .table-head { display: flex; justify-content: space-between; gap: 12px; align-items: flex-start; }
h1, h2 { margin: 0; color: #101828; }
p { margin: 6px 0 0; color: #667085; }
.grid-2 { display: grid; grid-template-columns: minmax(0, 1.4fr) minmax(320px, .8fr); gap: 14px; align-items: start; }
.table-card { border: 1px solid #eaecf0; border-radius: 12px; background: #fff; padding: 14px; }
.form-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(150px, 1fr)); gap: 10px; margin-top: 12px; }
.form-grid.one { grid-template-columns: 1fr; }
.wide { grid-column: 1 / -1; }
label { display: grid; gap: 6px; font-weight: 650; }
input, select { border: 1px solid #d0d5dd; border-radius: 10px; padding: 9px 11px; font-size: 14px; }
.actions, .row-actions { display: flex; flex-wrap: wrap; gap: 8px; margin-top: 12px; }
.btn { border: 1px solid #d0d5dd; border-radius: 10px; background: #fff; color: #344054; padding: 8px 11px; cursor: pointer; font-weight: 500; }
.btn.primary { background: #2563eb; border-color: #2563eb; color: #fff; }
.btn.danger { color: #b42318; border-color: #fecdca; }
.alert { padding: 10px 12px; border-radius: 10px; font-weight: 500; }
.alert.error { background: #fef3f2; color: #b42318; }
.alert.success { background: #ecfdf3; color: #027a48; }
.table-wrap { overflow: auto; margin-top: 12px; }
.data-table { width: 100%; min-width: 680px; border-collapse: collapse; }
.data-table th, .data-table td { border-bottom: 1px solid #eaecf0; padding: 9px 10px; text-align: left; vertical-align: top; }
.data-table th { background: #f9fafb; color: #475467; font-size: 12px; text-transform: uppercase; }
.status { border-radius: 999px; padding: 4px 8px; font-size: 12px; font-weight: 600; }
.status.done { background: #ecfdf3; color: #027a48; }
.status.cancel { background: #fef3f2; color: #b42318; }
.empty { text-align: center; color: #667085; font-weight: 500; }
@media (max-width: 1100px) { .grid-2 { grid-template-columns: 1fr; } .page-head, .table-head { flex-direction: column; } }
</style>
