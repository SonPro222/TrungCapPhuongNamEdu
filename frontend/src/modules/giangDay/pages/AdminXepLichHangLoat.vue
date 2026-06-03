<template>
  <section class="page">
    <header class="page-head">
      <div>
        <h1>Xếp lịch hàng loạt</h1>
        <p>Cấp 3: chọn nhiều lớp học phần, hệ thống tự lấy giảng viên chính đã phân công rồi tìm phòng/ca phù hợp.</p>
      </div>
      <button type="button" class="btn" @click="taiDuLieu">Tải lại</button>
    </header>

    <div v-if="loi" class="alert error">{{ loi }}</div>
    <div v-if="thongBao" class="alert success">{{ thongBao }}</div>

    <section class="table-card">
      <div class="form-grid">
        <label><span>Từ ngày</span><input v-model="form.tuNgay" type="date" /></label>
        <label><span>Đến ngày</span><input v-model="form.denNgay" type="date" /></label>
        <label><span>Số phương án / lớp</span><input v-model.number="form.soPhuongAn" type="number" min="1" max="20" /></label>
      </div>

      <div class="weekday-card">
        <strong>Thứ được phép xếp</strong>
        <div class="weekday-row">
          <label v-for="thu in dsThu" :key="thu.value"><input type="checkbox" :value="thu.value" v-model="form.thuTrongTuan" /> {{ thu.label }}</label>
        </div>
      </div>

      <div class="actions">
        <button type="button" class="btn primary" :disabled="dangXuLy" @click="xepHangLoat(true)">Preview hàng loạt</button>
        <button v-if="ketQua?.soLopThanhCong" type="button" class="btn success" :disabled="dangXuLy" @click="xepHangLoat(false)">Lưu các lớp hợp lệ</button>
      </div>
    </section>

    <section class="table-card">
      <div class="table-head"><div><h2>Chọn lớp học phần</h2><p>Chỉ chọn các lớp đã có giảng viên chính và có thời gian học hợp lệ.</p></div><strong>{{ lopDaChon.length }} lớp đã chọn</strong></div>
      <div class="table-wrap">
        <table class="data-table">
          <thead><tr><th><input type="checkbox" :checked="chonTatCa" @change="toggleTatCa" /></th><th>Mã lớp</th><th>Tên lớp</th><th>Thời gian</th><th>Số buổi</th><th>Sĩ số</th></tr></thead>
          <tbody>
            <tr v-if="!danhSachLopHocPhan.length"><td colspan="6" class="empty">Chưa có lớp học phần.</td></tr>
            <tr v-for="lop in danhSachLopHocPhan" :key="lop.id">
              <td><input type="checkbox" :value="lop.id" v-model="lopDaChon" /></td>
              <td>{{ lop.maLop || lop.id }}</td>
              <td>{{ lop.tenLop || '-' }}</td>
              <td>{{ lop.ngayBatDau || '...' }} → {{ lop.ngayKetThuc || '...' }}</td>
              <td>{{ lop.soBuoiHoc || '-' }}</td>
              <td>{{ lop.soLuongHienTai || 0 }}/{{ lop.soLuongToiDa || '-' }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </section>

    <section v-if="ketQua" class="table-card">
      <div class="table-head">
        <div><h2>Kết quả hàng loạt</h2><p>Thành công {{ ketQua.soLopThanhCong }}/{{ ketQua.tongLop }}, lỗi {{ ketQua.soLopLoi }}.</p></div>
        <span :class="['status', ketQua.soLopLoi ? 'pending' : 'done']">{{ ketQua.chiPreview ? 'Preview' : 'Đã lưu' }}</span>
      </div>

      <div class="result-grid">
        <article v-for="item in ketQua.items" :key="item.lopHocPhanId" class="result-card">
          <div class="result-top"><strong>{{ item.maLop || item.lopHocPhanId }}</strong><span :class="['status', item.thanhCong ? 'done' : 'cancel']">{{ item.thanhCong ? 'OK' : 'Lỗi' }}</span></div>
          <p>{{ item.thongBao }}</p>
          <template v-if="item.phuongAnTotNhat">
            <span><b>Phòng:</b> {{ tenPhongTheoId(item.phuongAnTotNhat.phongHocId) }}</span>
            <span><b>Ca:</b> {{ tenCaTheoId(item.phuongAnTotNhat.caHocId) }}</span>
            <span><b>Xếp:</b> {{ item.phuongAnTotNhat.soBuoiXepDuoc }}/{{ item.phuongAnTotNhat.soBuoiCanXep }}</span>
          </template>
        </article>
      </div>
    </section>
  </section>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { giangDayService } from '../services/giangDayService'

const loi = ref('')
const thongBao = ref('')
const dangXuLy = ref(false)
const danhSachLopHocPhan = ref([])
const danhSachPhongHoc = ref([])
const danhSachCaHoc = ref([])
const lopDaChon = ref([])
const ketQua = ref(null)
const dsThu = [1,2,3,4,5,6].map((value) => ({ value, label: value === 6 ? 'Thứ 7' : `Thứ ${value + 1}` }))
const form = ref({ tuNgay: '', denNgay: '', thuTrongTuan: [1,2,3,4,5,6], soPhuongAn: 5, tuDongGanNoiDungSyllabus: true })

const chonTatCa = computed(() => danhSachLopHocPhan.value.length > 0 && lopDaChon.value.length === danhSachLopHocPhan.value.length)

onMounted(() => taiDuLieu())

async function taiDuLieu() {
  loi.value = ''
  try {
    const [lop, phong, ca] = await Promise.all([
      giangDayService.layDanhSachLopHocPhan(),
      giangDayService.layDanhSachPhongHoc(),
      giangDayService.layDanhSachCaHoc()
    ])
    danhSachLopHocPhan.value = lop
    danhSachPhongHoc.value = phong
    danhSachCaHoc.value = ca
  } catch (error) { loi.value = layLoi(error, 'Không tải được dữ liệu xếp lịch hàng loạt') }
}

async function xepHangLoat(chiPreview) {
  loi.value = ''
  thongBao.value = ''
  ketQua.value = null
  if (!lopDaChon.value.length) { loi.value = 'Phải chọn ít nhất một lớp học phần'; return }
  if (!form.value.tuNgay || !form.value.denNgay) { loi.value = 'Phải nhập từ ngày và đến ngày'; return }
  dangXuLy.value = true
  try {
    ketQua.value = await giangDayService.xepLichHangLoat(lopDaChon.value[0], {
      ...form.value,
      lopHocPhanIds: lopDaChon.value,
      chiPreview
    })
    thongBao.value = chiPreview ? 'Đã preview xếp lịch hàng loạt' : 'Đã lưu các lịch hợp lệ'
  } catch (error) { loi.value = layLoi(error, 'Không xếp lịch hàng loạt được') }
  finally { dangXuLy.value = false }
}

function toggleTatCa(event) { lopDaChon.value = event.target.checked ? danhSachLopHocPhan.value.map((lop) => lop.id) : [] }
function tenPhongTheoId(id) { const item = danhSachPhongHoc.value.find((x) => Number(x.id) === Number(id)); return item ? `${item.maPhong || item.id} - ${item.tenPhong || ''}` : id || '-' }
function tenCaTheoId(id) { const item = danhSachCaHoc.value.find((x) => Number(x.id) === Number(id)); return item ? `${item.maCa || item.id} - ${item.tenCa || ''}` : id || '-' }
function layLoi(error, fallback) { return error?.response?.data?.message || error?.message || fallback }
</script>

<style scoped>
.page { display: flex; flex-direction: column; gap: 14px; color: #344054; font-size: 14px; }
.page-head, .table-head { display: flex; justify-content: space-between; gap: 12px; align-items: flex-start; }
h1, h2 { margin: 0; color: #101828; }
p { margin: 6px 0 0; color: #667085; }
.table-card { border: 1px solid #eaecf0; border-radius: 12px; background: #fff; padding: 14px; }
.form-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(180px, 1fr)); gap: 12px; }
label { display: grid; gap: 6px; font-weight: 650; }
input { border: 1px solid #d0d5dd; border-radius: 10px; padding: 9px 11px; font-size: 14px; }
.weekday-card { margin-top: 12px; border: 1px dashed #d0d5dd; border-radius: 12px; background: #f9fafb; padding: 12px; display: grid; gap: 8px; }
.weekday-row, .actions { display: flex; flex-wrap: wrap; gap: 10px; align-items: center; }
.weekday-row label { display: inline-flex; flex-direction: row; gap: 6px; align-items: center; }
.btn { border: 1px solid #d0d5dd; border-radius: 10px; background: #fff; color: #344054; padding: 9px 13px; cursor: pointer; font-weight: 500; }
.btn.primary { background: #2563eb; border-color: #2563eb; color: #fff; }
.btn.success { background: #16a34a; border-color: #16a34a; color: #fff; }
.alert { padding: 10px 12px; border-radius: 10px; font-weight: 500; }
.alert.error { background: #fef3f2; color: #b42318; }
.alert.success { background: #ecfdf3; color: #027a48; }
.table-wrap { overflow: auto; margin-top: 12px; }
.data-table { width: 100%; min-width: 850px; border-collapse: collapse; }
.data-table th, .data-table td { border-bottom: 1px solid #eaecf0; padding: 9px 10px; text-align: left; }
.data-table th { background: #f9fafb; color: #475467; font-size: 12px; text-transform: uppercase; }
.result-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(240px, 1fr)); gap: 12px; margin-top: 12px; }
.result-card { border: 1px solid #eaecf0; border-radius: 12px; padding: 12px; display: grid; gap: 5px; }
.result-top { display: flex; justify-content: space-between; gap: 8px; align-items: center; }
.status { border-radius: 999px; padding: 4px 8px; font-size: 12px; font-weight: 600; background: #f2f4f7; color: #475467; }
.status.done { background: #ecfdf3; color: #027a48; }
.status.cancel { background: #fef3f2; color: #b42318; }
.status.pending { background: #fffaeb; color: #b54708; }
.empty { text-align: center; color: #667085; font-weight: 500; }
</style>
