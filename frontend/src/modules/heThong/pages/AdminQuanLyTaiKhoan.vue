<template>
  <section class="page">
    <header class="page-head">
      <div class="page-title">
        <h1>Quản lý tài khoản</h1>
        <p>Quản lý tài khoản nội bộ, giáo viên và sinh viên.</p>
      </div>

      <div class="head-right">
        <nav class="tabs">
          <button
              type="button"
              class="tab"
              :class="{ active: tabDangChon === 'noiBo' }"
              @click="chonTab('noiBo')"
          >
            Nội bộ
          </button>

          <button
              type="button"
              class="tab"
              :class="{ active: tabDangChon === 'giaoVien' }"
              @click="chonTab('giaoVien')"
          >
            Giáo viên
          </button>

          <button
              type="button"
              class="tab"
              :class="{ active: tabDangChon === 'sinhVien' }"
              @click="chonTab('sinhVien')"
          >
            Sinh viên
          </button>
        </nav>

        <button type="button" class="btn compact" @click="taiTatCa">
          Tải lại
        </button>
      </div>
    </header>

    <AdminTaiKhoanSinhVien v-if="tabDangChon === 'sinhVien'" />

    <section v-else class="card">
      <div class="toolbar">
        <label class="search-field">
          <span>Tìm kiếm</span>
          <input
              v-model="boLocTaiKhoan.keyword"
              type="text"
              placeholder="Email, loại tài khoản, trạng thái"
              @keyup.enter="taiDanhSachTaiKhoan"
          />
        </label>

        <label>
          <span>Loại</span>
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

        <div class="toolbar-actions">
          <button type="button" class="btn primary compact" @click="taiDanhSachTaiKhoan">
            Lọc
          </button>

          <button type="button" class="btn compact" @click="xoaLocTaiKhoan">
            Xóa
          </button>
        </div>
      </div>

      <form class="create-bar" @submit.prevent="luuTaiKhoan">
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
              placeholder="Mật khẩu ban đầu"
          />
        </label>

        <label>
          <span>Loại tài khoản</span>
          <select v-model="formTaiKhoan.loaiTaiKhoan" required>
            <option value="">-- Chọn --</option>
            <option v-for="loai in loaiTaiKhoanTheoTab" :key="loai.value" :value="loai.value">
              {{ loai.label }}
            </option>
          </select>
        </label>

        <label>
          <span>Trạng thái</span>
          <select v-model="formTaiKhoan.trangThai" required>
            <option value="">-- Chọn --</option>
            <option value="cho_kich_hoat">Chờ kích hoạt</option>
            <option value="da_kich_hoat">Đã kích hoạt</option>
            <option value="bi_khoa">Bị khóa</option>
          </select>
        </label>

        <label>
          <span>Vai trò khi tạo</span>
          <select v-model="formTaiKhoan.vaiTroId">
            <option value="">-- Chọn vai trò --</option>
            <option v-for="role in vaiTroList" :key="role.id" :value="role.id">
              {{ role.maVaiTro }} - {{ role.tenVaiTro }}
            </option>
          </select>
        </label>

        <div class="create-actions">
          <button type="submit" class="btn primary compact">
            Thêm
          </button>

          <button type="button" class="btn compact" @click="resetFormTaiKhoan">
            Làm mới
          </button>
        </div>
      </form>

      <div v-if="thongBao" class="alert success">
        {{ thongBao }}
      </div>

      <div v-if="loi" class="alert error">
        {{ loi }}
      </div>

      <div class="table-card">
        <div class="table-toolbar">
          <div class="table-stats">
            <span>Hiển thị: <strong>{{ taiKhoanHienThi.length }}</strong></span>
            <span>Tài khoản: <strong>{{ taiKhoanList.length }}</strong></span>
            <span>Vai trò: <strong>{{ vaiTroList.length }}</strong></span>
            <span>Quyền: <strong>{{ quyenList.length }}</strong></span>
          </div>
        </div>

        <div class="table-wrap">
          <table>
            <thead>
            <tr>
              <th class="col-id">ID</th>
              <th class="col-email">Email</th>
              <th class="col-type">Loại</th>
              <th class="col-status">Trạng thái</th>
              <th class="col-roles">Vai trò hiện có</th>
              <th class="col-permissions">Quyền hiện có</th>
              <th class="col-assign-role">Gán vai trò</th>
              <th class="col-assign-permission">Gán quyền vào vai trò</th>
              <th class="col-action">Thao tác</th>
            </tr>
            </thead>

            <tbody>
            <tr v-if="dangTai">
              <td colspan="9" class="empty-cell">
                Đang tải dữ liệu...
              </td>
            </tr>

            <tr v-else-if="!taiKhoanHienThi.length">
              <td colspan="9" class="empty-cell">
                Chưa có dữ liệu
              </td>
            </tr>

            <tr v-for="item in taiKhoanHienThi" v-else :key="item.id">
              <td class="mono">
                #{{ item.id }}
              </td>

              <td>
                <div class="email-cell">
                  <strong>{{ item.email }}</strong>
                </div>
              </td>

              <td>
                <span class="type-text">
                  {{ item.loaiTaiKhoan }}
                </span>
              </td>

              <td>
                <span class="badge" :class="item.trangThai">
                  {{ item.trangThai }}
                </span>
              </td>

              <td>
                <div class="compact-chip-list">
                  <template v-if="layBanGhiVaiTroCuaTaiKhoan(item).length">
                    <span
                        v-for="record in layBanGhiVaiTroCuaTaiKhoan(item).slice(0, 2)"
                        :key="record.id"
                        class="role-pill"
                        :title="record.tenVaiTro || record.maVaiTro"
                    >
                      <span class="pill-text">{{ record.maVaiTro }}</span>

                      <button
                          type="button"
                          class="pill-remove"
                          title="Gỡ vai trò"
                          @click="xoaVaiTroKhoiTaiKhoan(item, record)"
                      >
                        ×
                      </button>
                    </span>

                    <details
                        v-if="layBanGhiVaiTroCuaTaiKhoan(item).length > 2"
                        class="more-popover"
                    >
                      <summary class="more-pill">
                        +{{ layBanGhiVaiTroCuaTaiKhoan(item).length - 2 }}
                      </summary>

                      <div class="popover-panel">
                        <div class="popover-title">Vai trò còn lại</div>

                        <div
                            v-for="record in layBanGhiVaiTroCuaTaiKhoan(item).slice(2)"
                            :key="record.id"
                            class="popover-item"
                        >
                          <div>
                            <strong>{{ record.maVaiTro }}</strong>
                            <span>{{ record.tenVaiTro || 'Vai trò' }}</span>
                          </div>

                          <button
                              type="button"
                              class="popover-remove"
                              title="Gỡ vai trò"
                              @click="xoaVaiTroKhoiTaiKhoan(item, record)"
                          >
                            ×
                          </button>
                        </div>
                      </div>
                    </details>
                  </template>

                  <span v-else class="empty-inline">
                    Chưa có
                  </span>
                </div>
              </td>

              <td>
                <div class="compact-chip-list">
                  <template v-if="layBanGhiQuyenCuaTaiKhoan(item).length">
                    <span
                        v-for="record in layBanGhiQuyenCuaTaiKhoan(item).slice(0, 2)"
                        :key="record.id"
                        class="permission-pill"
                        :title="`${record.maVaiTro} / ${record.maQuyen}`"
                    >
                      <span class="pill-prefix">{{ record.maVaiTro }}</span>
                      <span class="pill-divider">/</span>
                      <span class="pill-text">{{ record.maQuyen }}</span>

                      <button
                          type="button"
                          class="pill-remove"
                          title="Gỡ quyền"
                          @click="xoaQuyenKhoiVaiTro(item, record)"
                      >
                        ×
                      </button>
                    </span>

                    <details
                        v-if="layBanGhiQuyenCuaTaiKhoan(item).length > 2"
                        class="more-popover"
                    >
                      <summary class="more-pill">
                        +{{ layBanGhiQuyenCuaTaiKhoan(item).length - 2 }}
                      </summary>

                      <div class="popover-panel permission-panel">
                        <div class="popover-title">Quyền còn lại</div>

                        <div
                            v-for="record in layBanGhiQuyenCuaTaiKhoan(item).slice(2)"
                            :key="record.id"
                            class="popover-item"
                        >
                          <div>
                            <strong>{{ record.maQuyen }}</strong>
                            <span>{{ record.maVaiTro }}</span>
                          </div>

                          <button
                              type="button"
                              class="popover-remove"
                              title="Gỡ quyền"
                              @click="xoaQuyenKhoiVaiTro(item, record)"
                          >
                            ×
                          </button>
                        </div>
                      </div>
                    </details>
                  </template>

                  <span v-else class="empty-inline">
                    Chưa có
                  </span>
                </div>
              </td>

              <td>
                <select class="table-select" @change="ganVaiTro(item, $event)">
                  <option value="">-- Chọn vai trò --</option>
                  <option v-for="role in vaiTroList" :key="role.id" :value="role.id">
                    {{ role.maVaiTro }} - {{ role.tenVaiTro }}
                  </option>
                </select>
              </td>

              <td>
                <div class="inline-selects">
                  <select class="table-select" v-model="chonVaiTroGanQuyen[item.id]">
                    <option value="">-- Vai trò --</option>
                    <option
                        v-for="role in layVaiTroCuaTaiKhoan(item)"
                        :key="role.id"
                        :value="role.id"
                    >
                      {{ role.maVaiTro }}
                    </option>
                  </select>

                  <select class="table-select" @change="ganQuyenChoVaiTro(item, $event)">
                    <option value="">-- Quyền --</option>
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
.page {
  --primary: #077149;
  --primary-hover: #055436;
  --primary-light: #e6f1ec;
  --primary-glow: rgba(7, 113, 73, 0.14);

  --danger: #dc2626;
  --danger-light: #fef2f2;
  --danger-border: #fecaca;

  --warning-light: #fef9c3;
  --warning-text: #854d0e;

  --text-main: #111827;
  --text-normal: #334155;
  --text-muted: #64748b;

  --bg-card: #ffffff;
  --bg-soft: #f8fafc;

  --border-color: #e2e8f0;
  --border-strong: #cbd5e1;

  display: flex;
  flex-direction: column;
  gap: 10px;
  width: 100%;
  color: var(--text-main);
  font-family: Roboto, system-ui, -apple-system, BlinkMacSystemFont, sans-serif;
}

.page-head {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 12px;
  padding: 10px 12px;
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-top: 3px solid var(--primary);
  border-radius: 10px;
}

.page-title h1 {
  margin: 0;
  color: var(--primary);
  font-size: 19px;
  font-weight: 700;
}

.page-title p {
  margin: 2px 0 0;
  color: var(--text-muted);
  font-size: 12.5px;
}

.head-right {
  display: flex;
  align-items: center;
  gap: 8px;
}

.tabs {
  display: inline-flex;
  gap: 3px;
  padding: 4px;
  background: #e2e8f0;
  border-radius: 9px;
}

.tab {
  border: none;
  background: transparent;
  color: var(--text-muted);
  border-radius: 7px;
  padding: 6px 10px;
  cursor: pointer;
  font-family: inherit;
  font-size: 12.5px;
  font-weight: 600;
  white-space: nowrap;
  transition: all 0.16s ease;
}

.tab:hover {
  color: var(--text-main);
  background: rgba(255, 255, 255, 0.55);
}

.tab.active {
  background: #ffffff;
  color: var(--primary);
  box-shadow: 0 1px 2px rgba(15, 23, 42, 0.12);
}

.card {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.toolbar {
  display: grid;
  grid-template-columns:
    minmax(220px, 1.3fr)
    minmax(150px, 0.8fr)
    minmax(150px, 0.8fr)
    minmax(190px, 1fr)
    auto;
  gap: 8px;
  align-items: end;
  padding: 10px 12px;
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: 10px;
}

.toolbar label,
.create-bar label {
  display: flex;
  flex-direction: column;
  gap: 4px;
  min-width: 0;
}

.toolbar span,
.create-bar span {
  color: var(--text-normal);
  font-size: 11.5px;
  font-weight: 700;
}

.search-field {
  min-width: 0;
}

.toolbar-actions {
  display: flex;
  gap: 6px;
}

.create-bar {
  display: grid;
  grid-template-columns:
    minmax(220px, 1.2fr)
    minmax(150px, 0.8fr)
    minmax(150px, 0.8fr)
    minmax(150px, 0.8fr)
    minmax(190px, 1fr)
    auto;
  gap: 8px;
  align-items: end;
  padding: 10px 12px;
  background: #ffffff;
  border: 1px solid var(--border-color);
  border-radius: 10px;
}

.create-actions {
  display: flex;
  gap: 6px;
  white-space: nowrap;
}

input,
select {
  width: 100%;
  min-height: 31px;
  box-sizing: border-box;
  border: 1px solid var(--border-strong);
  border-radius: 7px;
  padding: 6px 9px;
  background: #ffffff;
  color: var(--text-main);
  outline: none;
  font-family: inherit;
  font-size: 12.5px;
  transition: border-color 0.16s ease, box-shadow 0.16s ease;
}

input:hover,
select:hover {
  border-color: #94a3b8;
}

input:focus,
select:focus {
  border-color: var(--primary);
  box-shadow: 0 0 0 3px var(--primary-glow);
}

.btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-height: 31px;
  border: 1px solid var(--border-strong);
  border-radius: 7px;
  padding: 6px 10px;
  background: #ffffff;
  color: var(--text-normal);
  cursor: pointer;
  font-family: inherit;
  font-size: 12.5px;
  font-weight: 600;
  line-height: 1;
  white-space: nowrap;
  transition: all 0.16s ease;
}

.btn:hover:not(:disabled) {
  background: var(--bg-soft);
  border-color: #94a3b8;
  color: var(--text-main);
}

.btn.primary {
  background: var(--primary);
  border-color: var(--primary);
  color: #ffffff;
}

.btn.primary:hover:not(:disabled) {
  background: var(--primary-hover);
  border-color: var(--primary-hover);
}

.btn.compact {
  min-height: 29px;
  padding: 5px 9px;
  font-size: 12px;
}

.btn.small {
  min-height: 27px;
  padding: 5px 8px;
  font-size: 12px;
}

.btn.danger {
  color: var(--danger);
  background: var(--danger-light);
  border-color: var(--danger-border);
}

.btn.danger:hover:not(:disabled) {
  color: #ffffff;
  background: var(--danger);
  border-color: var(--danger);
}

.alert {
  padding: 8px 11px;
  border-radius: 8px;
  font-size: 13px;
  font-weight: 600;
}

.alert.success {
  background: #ecfdf5;
  color: #065f46;
  border: 1px solid #a7f3d0;
}

.alert.error {
  background: var(--danger-light);
  color: #991b1b;
  border: 1px solid var(--danger-border);
}

.table-card {
  overflow: visible;
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: 10px;
}

.table-toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 10px;
  padding: 7px 10px;
  background: #ffffff;
  border-bottom: 1px solid var(--border-color);
  border-radius: 10px 10px 0 0;
}

.table-stats {
  display: flex;
  align-items: center;
  gap: 12px;
  color: var(--text-muted);
  font-size: 12px;
  flex-wrap: wrap;
}

.table-stats strong {
  color: var(--text-main);
}

.table-wrap {
  max-height: calc(100vh - 255px);
  overflow: auto;
}

table {
  width: 100%;
  min-width: 1380px;
  border-collapse: collapse;
}

thead th {
  position: sticky;
  top: 0;
  z-index: 5;
}

th,
td {
  position: relative;
  border-bottom: 1px solid #eef2f7;
  padding: 7px 9px;
  text-align: left;
  vertical-align: middle;
  color: var(--text-normal);
  font-size: 12.5px;
}

th {
  background: #f8fafc;
  color: var(--text-muted);
  font-size: 11px;
  font-weight: 700;
  letter-spacing: 0.035em;
  text-transform: uppercase;
  white-space: nowrap;
}

tbody tr:hover {
  background: var(--bg-soft);
}

.col-id {
  width: 70px;
}

.col-email {
  width: 230px;
}

.col-type {
  width: 120px;
}

.col-status {
  width: 130px;
}

.col-roles {
  width: 200px;
}

.col-permissions {
  width: 300px;
}

.col-assign-role {
  width: 190px;
}

.col-assign-permission {
  width: 300px;
}

.col-action {
  width: 90px;
  text-align: right;
}

.empty-cell {
  padding: 22px 12px !important;
  text-align: center;
  color: var(--text-muted);
}

.mono {
  color: var(--text-muted);
  font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, monospace;
  font-size: 12px;
}

.email-cell strong {
  display: block;
  color: var(--text-main);
  font-weight: 700;
  line-height: 1.25;
  word-break: break-word;
}

.type-text {
  color: var(--text-normal);
  font-size: 12px;
}

.badge {
  display: inline-flex;
  align-items: center;
  padding: 3px 8px;
  border-radius: 999px;
  font-size: 11.5px;
  font-weight: 700;
  background: #f1f5f9;
  color: #475467;
  white-space: nowrap;
}

.badge.da_kich_hoat {
  background: #dcfce7;
  color: #166534;
}

.badge.cho_kich_hoat {
  background: var(--warning-light);
  color: var(--warning-text);
}

.badge.bi_khoa {
  background: var(--danger-light);
  color: #991b1b;
}

.compact-chip-list {
  display: flex;
  align-items: center;
  gap: 5px;
  flex-wrap: nowrap;
  max-width: 100%;
  overflow: visible;
}

.role-pill,
.permission-pill,
.more-pill {
  display: inline-flex;
  align-items: center;
  height: 24px;
  min-width: 0;
  border-radius: 999px;
  font-size: 11.5px;
  font-weight: 700;
  line-height: 1;
  white-space: nowrap;
}

.role-pill {
  gap: 4px;
  max-width: 115px;
  padding: 0 4px 0 8px;
  border: 1px solid #bfe4d2;
  background: #ecfdf5;
  color: #047857;
}

.permission-pill {
  gap: 4px;
  max-width: 220px;
  padding: 0 4px 0 8px;
  border: 1px solid #dbe4f0;
  background: #f8fafc;
  color: #334155;
}

.pill-prefix {
  flex-shrink: 0;
  color: var(--primary);
  font-weight: 800;
}

.pill-divider {
  flex-shrink: 0;
  color: #94a3b8;
}

.pill-text {
  min-width: 0;
  overflow: hidden;
  text-overflow: ellipsis;
}

.pill-remove {
  flex-shrink: 0;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 17px;
  height: 17px;
  margin-left: 1px;
  border: none;
  border-radius: 999px;
  background: transparent;
  color: #94a3b8;
  cursor: pointer;
  font-size: 13px;
  font-weight: 900;
  line-height: 1;
  transition: all 0.15s ease;
}

.pill-remove:hover {
  background: var(--danger-light);
  color: var(--danger);
}

.more-popover {
  position: relative;
  flex-shrink: 0;
}

.more-popover summary {
  list-style: none;
}

.more-popover summary::-webkit-details-marker {
  display: none;
}

.more-pill {
  flex-shrink: 0;
  padding: 0 8px;
  border: 1px solid #dbe4f0;
  background: #f1f5f9;
  color: #64748b;
  cursor: pointer;
  transition: all 0.15s ease;
}

.more-pill:hover {
  background: #e2e8f0;
  color: #334155;
}

.more-popover[open] .more-pill {
  background: var(--primary);
  color: #ffffff;
  border-color: var(--primary);
}

.popover-panel {
  position: absolute;
  top: calc(100% + 6px);
  right: 0;
  z-index: 30;
  width: 240px;
  max-height: 260px;
  overflow: auto;
  padding: 8px;
  border: 1px solid #dbe4f0;
  border-radius: 10px;
  background: #ffffff;
  box-shadow: 0 16px 32px rgba(15, 23, 42, 0.16);
}

.permission-panel {
  width: 300px;
}

.popover-title {
  padding: 4px 6px 8px;
  color: #64748b;
  font-size: 11px;
  font-weight: 800;
  text-transform: uppercase;
  letter-spacing: 0.04em;
}

.popover-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 8px;
  padding: 7px 8px;
  border-radius: 8px;
  color: #334155;
}

.popover-item:hover {
  background: #f8fafc;
}

.popover-item div {
  min-width: 0;
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.popover-item strong {
  color: #111827;
  font-size: 12px;
  font-weight: 800;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.popover-item span {
  color: #64748b;
  font-size: 11.5px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.popover-remove {
  flex-shrink: 0;
  width: 22px;
  height: 22px;
  border: none;
  border-radius: 999px;
  background: #f1f5f9;
  color: #94a3b8;
  cursor: pointer;
  font-size: 15px;
  font-weight: 900;
  line-height: 1;
}

.popover-remove:hover {
  background: var(--danger-light);
  color: var(--danger);
}

.empty-inline {
  color: #94a3b8;
  font-size: 12px;
  font-style: italic;
}

.table-select {
  min-height: 28px;
  padding: 5px 7px;
  font-size: 12px;
}

.inline-selects {
  display: grid;
  grid-template-columns: minmax(92px, 0.7fr) minmax(145px, 1.3fr);
  gap: 5px;
}

.actions {
  display: flex;
  justify-content: flex-end;
  gap: 5px;
}

@media (max-width: 1280px) {
  .page-head {
    align-items: flex-start;
    flex-direction: column;
  }

  .head-right {
    width: 100%;
    justify-content: space-between;
  }

  .toolbar,
  .create-bar {
    grid-template-columns: repeat(3, minmax(0, 1fr));
  }

  .toolbar-actions,
  .create-actions {
    grid-column: 1 / -1;
  }

  .table-wrap {
    max-height: calc(100vh - 330px);
  }
}

@media (max-width: 768px) {
  .tabs {
    width: 100%;
    overflow-x: auto;
  }

  .tab {
    flex: 1;
  }

  .toolbar,
  .create-bar {
    grid-template-columns: 1fr;
  }

  .toolbar-actions,
  .create-actions {
    flex-direction: column;
  }

  .toolbar-actions .btn,
  .create-actions .btn,
  .head-right .btn {
    width: 100%;
  }

  .head-right {
    align-items: stretch;
    flex-direction: column;
  }

  .table-wrap {
    max-height: none;
  }

  thead th {
    position: static;
  }
}
</style>