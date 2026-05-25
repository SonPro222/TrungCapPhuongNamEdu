<template>
  <section class="page">
    <header class="page-head">
      <div>
        <h1>Quản lý tài khoản</h1>
        <p>Quản lý tài khoản nội bộ, giáo viên và sinh viên.</p>
      </div>

      <button type="button" class="btn" @click="taiTatCa">
        Tải lại
      </button>
    </header>

    <nav class="tabs">
      <button
          type="button"
          class="tab"
          :class="{ active: tabDangChon === 'noiBo' }"
          @click="chonTab('noiBo')"
      >
        Tài khoản nội bộ
      </button>

      <button
          type="button"
          class="tab"
          :class="{ active: tabDangChon === 'giaoVien' }"
          @click="chonTab('giaoVien')"
      >
        Tài khoản giáo viên
      </button>

      <button
          type="button"
          class="tab"
          :class="{ active: tabDangChon === 'sinhVien' }"
          @click="chonTab('sinhVien')"
      >
        Tài khoản sinh viên
      </button>
    </nav>

    <AdminTaiKhoanSinhVien v-if="tabDangChon === 'sinhVien'" />

    <section v-else class="card">
      <section class="filter-card">
        <label>
          <span>Tìm kiếm</span>
          <input
              v-model="boLocTaiKhoan.keyword"
              type="text"
              placeholder="Email, loại tài khoản, trạng thái"
              @keyup.enter="taiDanhSachTaiKhoan"
          />
        </label>

        <label>
          <span>Loại tài khoản</span>
          <select v-model="boLocTaiKhoan.loaiTaiKhoan" @change="taiDanhSachTaiKhoan">
            <option value="">Tất cả</option>
            <option v-for="loai in loaiTaiKhoanTheoTab" :key="loai.value" :value="loai.value">
              {{ loai.label }}
            </option>
          </select>
        </label>

        <label>
          <span>Trạng thái</span>
          <select v-model="boLocTaiKhoan.trangThai" @change="taiDanhSachTaiKhoan">
            <option value="">Tất cả</option>
            <option value="cho_kich_hoat">Chờ kích hoạt</option>
            <option value="da_kich_hoat">Đã kích hoạt</option>
            <option value="bi_khoa">Bị khóa</option>
          </select>
        </label>

        <label>
          <span>Vai trò</span>
          <select v-model="boLocTaiKhoan.vaiTro" @change="taiDanhSachTaiKhoan">
            <option value="">Tất cả</option>
            <option v-for="role in vaiTroList" :key="role.id" :value="role.maVaiTro">
              {{ role.maVaiTro }} - {{ role.tenVaiTro }}
            </option>
          </select>
        </label>

        <div class="filter-actions">
          <button type="button" class="btn primary" @click="taiDanhSachTaiKhoan">
            Lọc
          </button>

          <button type="button" class="btn" @click="xoaLocTaiKhoan">
            Xóa lọc
          </button>
        </div>
      </section>

      <form class="form-card" @submit.prevent="luuTaiKhoan">
        <label>
          <span>Email</span>
          <input v-model="formTaiKhoan.email" type="email" required />
        </label>

        <label>
          <span>Mật khẩu</span>
          <input
              v-model="formTaiKhoan.matKhau"
              type="password"
              required
              placeholder="Nhập mật khẩu ban đầu"
          />
        </label>

        <label>
          <span>Loại tài khoản</span>
          <select v-model="formTaiKhoan.loaiTaiKhoan" required>
            <option value="">-- Chọn loại tài khoản --</option>
            <option v-for="loai in loaiTaiKhoanTheoTab" :key="loai.value" :value="loai.value">
              {{ loai.label }}
            </option>
          </select>
        </label>

        <label>
          <span>Trạng thái</span>
          <select v-model="formTaiKhoan.trangThai" required>
            <option value="">-- Chọn trạng thái --</option>
            <option value="cho_kich_hoat">Chờ kích hoạt</option>
            <option value="da_kich_hoat">Đã kích hoạt</option>
            <option value="bi_khoa">Bị khóa</option>
          </select>
        </label>

        <label>
          <span>Vai trò khi tạo tài khoản</span>
          <select v-model="formTaiKhoan.vaiTroId">
            <option value="">-- Chọn vai trò --</option>
            <option v-for="role in vaiTroList" :key="role.id" :value="role.id">
              {{ role.maVaiTro }} - {{ role.tenVaiTro }}
            </option>
          </select>
        </label>

        <div class="form-actions">
          <button type="submit" class="btn primary">
            Thêm tài khoản
          </button>

          <button type="button" class="btn" @click="resetFormTaiKhoan">
            Làm mới
          </button>
        </div>
      </form>

      <div v-if="thongBao" class="alert success">{{ thongBao }}</div>
      <div v-if="loi" class="alert error">{{ loi }}</div>

      <div class="table-wrap">
        <table>
          <thead>
          <tr>
            <th>ID</th>
            <th>Email</th>
            <th>Loại</th>
            <th>Trạng thái</th>
            <th>Vai trò hiện có</th>
            <th>Quyền hiện có</th>
            <th>Gán vai trò</th>
            <th>Gán quyền vào vai trò</th>
            <th>Thao tác</th>
          </tr>
          </thead>

          <tbody>
          <tr v-if="dangTai">
            <td colspan="9">Đang tải dữ liệu...</td>
          </tr>

          <tr v-else-if="!taiKhoanHienThi.length">
            <td colspan="9">Chưa có dữ liệu</td>
          </tr>

          <tr v-for="item in taiKhoanHienThi" v-else :key="item.id">
            <td>{{ item.id }}</td>
            <td>{{ item.email }}</td>
            <td>{{ item.loaiTaiKhoan }}</td>
            <td>
              <span class="badge" :class="item.trangThai">{{ item.trangThai }}</span>
            </td>

            <td>
              <div class="chip-list">
                  <span
                      v-for="record in layBanGhiVaiTroCuaTaiKhoan(item)"
                      :key="record.id"
                      class="chip"
                  >
                    {{ record.maVaiTro }}
                    <button type="button" @click="xoaVaiTroKhoiTaiKhoan(item, record)">
                      ×
                    </button>
                  </span>

                <span v-if="!layBanGhiVaiTroCuaTaiKhoan(item).length">-</span>
              </div>
            </td>

            <td>
              <div class="chip-list">
                  <span
                      v-for="record in layBanGhiQuyenCuaTaiKhoan(item)"
                      :key="record.id"
                      class="chip"
                  >
                    {{ record.maVaiTro }} / {{ record.maQuyen }}
                    <button type="button" @click="xoaQuyenKhoiVaiTro(item, record)">
                      ×
                    </button>
                  </span>

                <span v-if="!layBanGhiQuyenCuaTaiKhoan(item).length">-</span>
              </div>
            </td>

            <td>
              <select @change="ganVaiTro(item, $event)">
                <option value="">-- Chọn vai trò --</option>
                <option v-for="role in vaiTroList" :key="role.id" :value="role.id">
                  {{ role.maVaiTro }} - {{ role.tenVaiTro }}
                </option>
              </select>
            </td>

            <td>
              <div class="inline-selects">
                <select v-model="chonVaiTroGanQuyen[item.id]">
                  <option value="">-- Chọn vai trò --</option>
                  <option
                      v-for="role in layVaiTroCuaTaiKhoan(item)"
                      :key="role.id"
                      :value="role.id"
                  >
                    {{ role.maVaiTro }}
                  </option>
                </select>

                <select @change="ganQuyenChoVaiTro(item, $event)">
                  <option value="">-- Chọn quyền --</option>
                  <option v-for="quyen in quyenList" :key="quyen.id" :value="quyen.id">
                    {{ quyen.maQuyen }} - {{ quyen.tenQuyen }}
                  </option>
                </select>
              </div>
            </td>

            <td class="actions">
              <button type="button" class="btn small danger" @click="xoaTaiKhoan(item)">
                Xóa
              </button>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </section>
  </section>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { heThongApi } from '../api/heThongApi'
import AdminTaiKhoanSinhVien from './AdminTaiKhoanSinhVien.vue'

const tabDangChon = ref('noiBo')
const dangTai = ref(false)
const loi = ref('')
const thongBao = ref('')

const taiKhoanList = ref([])
const vaiTroList = ref([])
const quyenList = ref([])
const taiKhoanVaiTroList = ref([])
const vaiTroQuyenList = ref([])

const chonVaiTroGanQuyen = reactive({})

const boLocTaiKhoan = reactive({
  keyword: '',
  loaiTaiKhoan: '',
  trangThai: '',
  vaiTro: ''
})

const formTaiKhoan = reactive({
  email: '',
  matKhau: '',
  loaiTaiKhoan: '',
  trangThai: '',
  vaiTroId: ''
})

const loaiNoiBo = [
  { value: 'admin', label: 'Admin' },
  { value: 'dao_tao', label: 'Phòng đào tạo' },
  { value: 'nhan_vien', label: 'Nhân viên' }
]

const loaiGiaoVien = [
  { value: 'giao_vien', label: 'Giáo viên' }
]

const loaiTaiKhoanTheoTab = computed(() => {
  return tabDangChon.value === 'giaoVien' ? loaiGiaoVien : loaiNoiBo
})

const taiKhoanHienThi = computed(() => {
  const loaiHopLe = loaiTaiKhoanTheoTab.value.map((item) => item.value)

  return taiKhoanList.value.filter((item) => {
    const keyword = boLocTaiKhoan.keyword.trim().toLowerCase()
    const dungTab = loaiHopLe.includes(item.loaiTaiKhoan)
    const dungKeyword = !keyword
        || String(item.email || '').toLowerCase().includes(keyword)
        || String(item.loaiTaiKhoan || '').toLowerCase().includes(keyword)
        || String(item.trangThai || '').toLowerCase().includes(keyword)
    const dungLoai = !boLocTaiKhoan.loaiTaiKhoan || item.loaiTaiKhoan === boLocTaiKhoan.loaiTaiKhoan
    const dungTrangThai = !boLocTaiKhoan.trangThai || item.trangThai === boLocTaiKhoan.trangThai
    const roles = Array.isArray(item.roles) ? item.roles : []
    const dungVaiTro = !boLocTaiKhoan.vaiTro || roles.includes(boLocTaiKhoan.vaiTro)

    return dungTab && dungKeyword && dungLoai && dungTrangThai && dungVaiTro
  })
})

onMounted(() => {
  taiTatCa()
  resetFormTaiKhoan()
})

async function taiTatCa() {
  loi.value = ''
  thongBao.value = ''
  dangTai.value = true

  try {
    const [
      taiKhoanResult,
      vaiTroResult,
      quyenResult,
      taiKhoanVaiTroResult,
      vaiTroQuyenResult
    ] = await Promise.all([
      heThongApi.taiKhoan.getAll({ page: 0, size: 1000 }),
      heThongApi.vaiTro.getAll({ page: 0, size: 1000 }),
      heThongApi.quyen.getAll({ page: 0, size: 1000 }),
      heThongApi.taiKhoanVaiTro.getAll({ page: 0, size: 1000 }),
      heThongApi.vaiTroQuyen.getAll({ page: 0, size: 1000 })
    ])

    taiKhoanList.value = layDanhSach(taiKhoanResult)
    vaiTroList.value = layDanhSach(vaiTroResult)
    quyenList.value = layDanhSach(quyenResult)
    taiKhoanVaiTroList.value = layDanhSach(taiKhoanVaiTroResult)
    vaiTroQuyenList.value = layDanhSach(vaiTroQuyenResult)
  } catch (error) {
    loi.value = error?.message || 'Không tải được dữ liệu tài khoản'
  } finally {
    dangTai.value = false
  }
}

async function taiDanhSachTaiKhoan() {
  await taiTatCa()
}

function chonTab(tab) {
  tabDangChon.value = tab
  xoaLocTaiKhoan()
  resetFormTaiKhoan()
}

function layDanhSach(result) {
  if (Array.isArray(result)) return result
  if (Array.isArray(result?.content)) return result.content
  if (Array.isArray(result?.data?.content)) return result.data.content
  if (Array.isArray(result?.data)) return result.data
  return []
}

async function luuTaiKhoan() {
  loi.value = ''
  thongBao.value = ''

  const roles = layRolesGuiLen()

  if (!roles.length) {
    loi.value = 'Tài khoản phải có ít nhất một vai trò'
    return
  }

  const payload = {
    email: formTaiKhoan.email,
    loaiTaiKhoan: formTaiKhoan.loaiTaiKhoan,
    trangThai: formTaiKhoan.trangThai,
    roles
  }

  payload.matKhau = formTaiKhoan.matKhau

  try {
    await heThongApi.taiKhoan.create(payload)
    thongBao.value = 'Thêm tài khoản thành công'
    resetFormTaiKhoan()
    await taiTatCa()
  } catch (error) {
    loi.value = error?.message || 'Thêm tài khoản thất bại'
  }
}

function layRolesGuiLen() {
  if (formTaiKhoan.vaiTroId) {
    const role = vaiTroList.value.find((item) => Number(item.id) === Number(formTaiKhoan.vaiTroId))
    return role?.maVaiTro ? [role.maVaiTro] : []
  }

  return []
}

async function xoaTaiKhoan(item) {
  if (!window.confirm(`Xóa tài khoản ${item.email}?`)) return

  try {
    await heThongApi.taiKhoan.delete(item.id)
    thongBao.value = 'Xóa tài khoản thành công'
    await taiTatCa()
  } catch (error) {
    loi.value = error?.message || 'Xóa tài khoản thất bại'
  }
}

function resetFormTaiKhoan() {
  formTaiKhoan.email = ''
  formTaiKhoan.matKhau = ''
  formTaiKhoan.loaiTaiKhoan = ''
  formTaiKhoan.trangThai = ''
  formTaiKhoan.vaiTroId = ''
}

function xoaLocTaiKhoan() {
  boLocTaiKhoan.keyword = ''
  boLocTaiKhoan.loaiTaiKhoan = ''
  boLocTaiKhoan.trangThai = ''
  boLocTaiKhoan.vaiTro = ''
}

async function ganVaiTro(taiKhoan, event) {
  const vaiTroId = event.target.value

  if (!vaiTroId) return

  const role = vaiTroList.value.find((item) => Number(item.id) === Number(vaiTroId))
  const roles = Array.isArray(taiKhoan.roles) ? taiKhoan.roles : []

  if (role?.maVaiTro && roles.includes(role.maVaiTro)) {
    thongBao.value = 'Tài khoản đã có vai trò này'
    event.target.value = ''
    return
  }

  try {
    await heThongApi.taiKhoanVaiTro.create({
      taiKhoanId: taiKhoan.id,
      vaiTroId: Number(vaiTroId)
    })

    thongBao.value = 'Gán vai trò thành công'
    event.target.value = ''
    await taiTatCa()
  } catch (error) {
    loi.value = error?.message || 'Gán vai trò thất bại'
  }
}

async function xoaVaiTroKhoiTaiKhoan(taiKhoan, record) {
  if (!window.confirm(`Gỡ vai trò ${record.maVaiTro} khỏi tài khoản ${taiKhoan.email}?`)) return

  try {
    await heThongApi.taiKhoanVaiTro.delete(record.id)
    thongBao.value = 'Gỡ vai trò khỏi tài khoản thành công'
    await taiTatCa()
  } catch (error) {
    loi.value = error?.message || 'Gỡ vai trò thất bại'
  }
}

async function ganQuyenChoVaiTro(taiKhoan, event) {
  const quyenId = event.target.value
  const vaiTroId = chonVaiTroGanQuyen[taiKhoan.id]

  if (!vaiTroId || !quyenId) {
    event.target.value = ''
    return
  }

  const daCoQuyen = vaiTroQuyenList.value.some((item) => {
    return Number(item.vaiTroId) === Number(vaiTroId)
        && Number(item.quyenId) === Number(quyenId)
  })

  if (daCoQuyen) {
    thongBao.value = 'Vai trò đã có quyền này'
    event.target.value = ''
    chonVaiTroGanQuyen[taiKhoan.id] = ''
    return
  }

  try {
    await heThongApi.vaiTroQuyen.create({
      vaiTroId: Number(vaiTroId),
      quyenId: Number(quyenId)
    })

    thongBao.value = 'Gán quyền cho vai trò thành công'
    event.target.value = ''
    chonVaiTroGanQuyen[taiKhoan.id] = ''
    await taiTatCa()
  } catch (error) {
    loi.value = error?.message || 'Gán quyền thất bại'
  }
}

async function xoaQuyenKhoiVaiTro(taiKhoan, record) {
  if (!window.confirm(`Gỡ quyền ${record.maQuyen} khỏi vai trò ${record.maVaiTro}?`)) return

  try {
    await heThongApi.vaiTroQuyen.delete(record.id)
    thongBao.value = 'Gỡ quyền khỏi vai trò thành công'
    await taiTatCa()
  } catch (error) {
    loi.value = error?.message || 'Gỡ quyền thất bại'
  }
}

function layVaiTroCuaTaiKhoan(taiKhoan) {
  const roles = Array.isArray(taiKhoan.roles) ? taiKhoan.roles : []

  return vaiTroList.value.filter((role) => roles.includes(role.maVaiTro))
}

function layBanGhiVaiTroCuaTaiKhoan(taiKhoan) {
  return taiKhoanVaiTroList.value
      .filter((item) => Number(item.taiKhoanId) === Number(taiKhoan.id))
      .map((item) => {
        const role = vaiTroList.value.find((roleItem) => Number(roleItem.id) === Number(item.vaiTroId))

        return {
          id: item.id,
          taiKhoanId: item.taiKhoanId,
          vaiTroId: item.vaiTroId,
          maVaiTro: role?.maVaiTro || item.maVaiTro || item.vaiTroId,
          tenVaiTro: role?.tenVaiTro || item.tenVaiTro || ''
        }
      })
}

function layBanGhiQuyenCuaTaiKhoan(taiKhoan) {
  const vaiTroIds = layBanGhiVaiTroCuaTaiKhoan(taiKhoan).map((item) => Number(item.vaiTroId))

  return vaiTroQuyenList.value
      .filter((item) => vaiTroIds.includes(Number(item.vaiTroId)))
      .map((item) => {
        const role = vaiTroList.value.find((roleItem) => Number(roleItem.id) === Number(item.vaiTroId))
        const quyen = quyenList.value.find((quyenItem) => Number(quyenItem.id) === Number(item.quyenId))

        return {
          id: item.id,
          vaiTroId: item.vaiTroId,
          quyenId: item.quyenId,
          maVaiTro: role?.maVaiTro || item.maVaiTro || item.vaiTroId,
          maQuyen: quyen?.maQuyen || item.maQuyen || item.quyenId,
          tenQuyen: quyen?.tenQuyen || item.tenQuyen || ''
        }
      })
}
</script>

<style scoped>
.page,
.card {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.page-head {
  display: flex;
  justify-content: space-between;
  gap: 16px;
}

.page-head h1 {
  margin: 0;
}

.page-head p {
  margin: 6px 0 0;
  color: #64748b;
}

.tabs {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.tab,
.btn {
  border: 1px solid #cbd5e1;
  background: #fff;
  color: #0f172a;
  border-radius: 8px;
  padding: 8px 12px;
  cursor: pointer;
}

.tab.active,
.btn.primary {
  background: #0f172a;
  color: #fff;
  border-color: #0f172a;
}

.btn.small {
  padding: 5px 8px;
  font-size: 13px;
}

.btn.danger {
  color: #b91c1c;
  border-color: #fecaca;
}

.filter-card,
.form-card {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 12px;
  padding: 16px;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  background: #fff;
}

.filter-card label,
.form-card label {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.filter-card span,
.form-card span {
  font-size: 13px;
  font-weight: 700;
  color: #334155;
}

input,
select {
  border: 1px solid #cbd5e1;
  border-radius: 8px;
  padding: 8px 10px;
}

.filter-actions,
.form-actions {
  display: flex;
  align-items: flex-end;
  gap: 8px;
}

.form-actions {
  grid-column: 1 / -1;
}

.table-wrap {
  overflow: auto;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
}

table {
  width: 100%;
  border-collapse: collapse;
  min-width: 1450px;
}

th,
td {
  border-bottom: 1px solid #e2e8f0;
  padding: 10px;
  text-align: left;
  vertical-align: top;
}

th {
  background: #f8fafc;
}

.actions,
.inline-selects {
  display: flex;
  gap: 6px;
  flex-wrap: wrap;
}

.alert {
  padding: 10px 12px;
  border-radius: 8px;
}

.alert.success {
  background: #dcfce7;
  color: #166534;
}

.alert.error {
  background: #fee2e2;
  color: #991b1b;
}

.badge {
  display: inline-flex;
  padding: 4px 8px;
  border-radius: 999px;
  font-size: 12px;
  font-weight: 700;
  background: #e2e8f0;
}

.badge.da_kich_hoat {
  background: #dcfce7;
  color: #166534;
}

.badge.cho_kich_hoat {
  background: #fef9c3;
  color: #854d0e;
}

.badge.bi_khoa {
  background: #fee2e2;
  color: #991b1b;
}

.chip-list {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
}

.chip {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  border: 1px solid #cbd5e1;
  background: #f8fafc;
  border-radius: 999px;
  padding: 4px 8px;
  font-size: 12px;
  font-weight: 700;
}

.chip button {
  border: 0;
  background: transparent;
  color: #b91c1c;
  cursor: pointer;
  font-weight: 900;
  padding: 0;
}
</style>