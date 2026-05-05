<template>
  <section class="system-page">
    <header class="page-head">
      <div>
        <h1>{{ currentConfig.title }}</h1>
        <p>{{ currentConfig.description }}</p>
      </div>
      <button class="btn secondary small" type="button" @click="loadAll">Tải lại</button>
    </header>

    <div v-if="message" class="message">{{ message }}</div>
    <div v-if="error" class="error">{{ error }}</div>

    <section v-if="isAccountView" class="grid-form">
      <div class="card">
        <h2>{{ editingAccountId ? 'Cập nhật tài khoản' : 'Thêm tài khoản' }}</h2>
        <div class="form-grid">
          <label>Email<input v-model.trim="accountForm.email" type="email" /></label>
          <label>Mật khẩu<input v-model="accountForm.matKhau" type="password" :placeholder="editingAccountId ? 'Bỏ trống nếu không đổi' : ''" /></label>
          <label>Loại tài khoản
            <select v-model="accountForm.loaiTaiKhoan">
              <option v-for="item in currentConfig.accountTypes" :key="item" :value="item">{{ labelAccountType(item) }}</option>
            </select>
          </label>
          <label>Trạng thái
            <select v-model="accountForm.trangThai" @change="editingAccountId && saveAccount()">
              <option value="cho_kich_hoat">Chờ kích hoạt</option>
              <option value="da_kich_hoat">Đã kích hoạt</option>
              <option value="bi_khoa">Bị khóa</option>
            </select>
          </label>
          <label class="wide">Vai trò
            <select v-model="accountForm.roles" multiple>
              <option v-for="role in vaiTroItems" :key="role.id" :value="role.maVaiTro">{{ role.maVaiTro }} - {{ role.tenVaiTro }}</option>
            </select>
          </label>
        </div>
        <div class="actions">
          <button class="btn primary" type="button" @click="saveAccount">{{ editingAccountId ? 'Lưu tài khoản' : 'Thêm tài khoản' }}</button>
          <button class="btn ghost" type="button" @click="resetAccountForm">Hủy</button>
        </div>
      </div>

      <div v-if="view === 'sinhVien'" class="card">
        <h2>{{ editingProfileId ? 'Cập nhật sinh viên' : 'Liên kết sinh viên' }}</h2>
        <div class="form-grid">
          <label>Mã sinh viên<input v-model.trim="studentForm.maSinhVien" /></label>
          <label>Họ tên<input v-model.trim="studentForm.hoTen" /></label>
          <label>Email<input v-model.trim="studentForm.email" type="email" /></label>
          <label>Tài khoản ID<input v-model.number="studentForm.taiKhoanId" type="number" /></label>
          <label>Giới tính
            <select v-model="studentForm.gioiTinh">
              <option value="">Không chọn</option>
              <option value="nam">Nam</option>
              <option value="nu">Nữ</option>
              <option value="khac">Khác</option>
            </select>
          </label>
          <label>Trạng thái
            <select v-model="studentForm.trangThai" @change="editingProfileId && saveStudent()">
              <option value="dang_hoc">Đang học</option>
              <option value="bao_luu">Bảo lưu</option>
              <option value="tot_nghiep">Tốt nghiệp</option>
              <option value="nghi_hoc">Nghỉ học</option>
            </select>
          </label>
          <label>Số điện thoại<input v-model.trim="studentForm.soDienThoai" /></label>
          <label>Ngày sinh<input v-model="studentForm.ngaySinh" type="date" /></label>
          <label class="wide">Địa chỉ<input v-model.trim="studentForm.diaChi" /></label>
        </div>
        <div class="actions">
          <button class="btn primary" type="button" @click="saveStudent">{{ editingProfileId ? 'Lưu sinh viên' : 'Thêm sinh viên' }}</button>
          <button class="btn ghost" type="button" @click="resetStudentForm">Hủy</button>
        </div>
      </div>
    </section>

    <section v-if="view === 'nhanVien' || view === 'daoTaoAdmin'" class="card">
      <h2>{{ editingProfileId ? 'Cập nhật nhân viên' : 'Thêm nhân viên' }}</h2>
      <div class="form-grid">
        <label>Tài khoản ID<input v-model.number="staffForm.taiKhoanId" type="number" /></label>
        <label>Mã nhân viên<input v-model.trim="staffForm.maNhanVien" /></label>
        <label>Họ tên<input v-model.trim="staffForm.hoTen" /></label>
        <label>Email<input v-model.trim="staffForm.email" type="email" /></label>
        <label>Số điện thoại<input v-model.trim="staffForm.soDienThoai" /></label>
        <label>Phòng ban<input v-model.trim="staffForm.phongBan" /></label>
        <label>Chức vụ<input v-model.trim="staffForm.chucVu" /></label>
        <label>Trạng thái
          <select v-model="staffForm.trangThai" @change="editingProfileId && saveStaff()">
            <option value="dang_lam">Đang làm</option>
            <option value="nghi_viec">Nghỉ việc</option>
            <option value="tam_nghi">Tạm nghỉ</option>
          </select>
        </label>
      </div>
      <div class="actions">
        <button class="btn primary" type="button" @click="saveStaff">{{ editingProfileId ? 'Lưu nhân viên' : 'Thêm nhân viên' }}</button>
        <button class="btn ghost" type="button" @click="resetStaffForm">Hủy</button>
      </div>
    </section>

    <section v-if="view === 'vaiTroQuyen'" class="grid-form">
      <div class="card">
        <h2>{{ editingRoleId ? 'Cập nhật vai trò' : 'Thêm vai trò' }}</h2>
        <div class="form-grid">
          <label>Mã vai trò<input v-model.trim="roleForm.maVaiTro" /></label>
          <label>Tên vai trò<input v-model.trim="roleForm.tenVaiTro" /></label>
          <label class="wide">Mô tả<input v-model.trim="roleForm.moTa" /></label>
        </div>
        <div class="actions"><button class="btn primary" type="button" @click="saveRole">Lưu vai trò</button><button class="btn ghost" type="button" @click="resetRoleForm">Hủy</button></div>
      </div>
      <div class="card">
        <h2>{{ editingPermissionId ? 'Cập nhật quyền' : 'Thêm quyền' }}</h2>
        <div class="form-grid">
          <label>Mã quyền<input v-model.trim="permissionForm.maQuyen" /></label>
          <label>Tên quyền<input v-model.trim="permissionForm.tenQuyen" /></label>
          <label class="wide">Mô tả<input v-model.trim="permissionForm.moTa" /></label>
        </div>
        <div class="actions"><button class="btn primary" type="button" @click="savePermission">Lưu quyền</button><button class="btn ghost" type="button" @click="resetPermissionForm">Hủy</button></div>
      </div>
    </section>

    <section v-if="view === 'lienKet'" class="grid-form">
      <div class="card">
        <h2>Liên kết tài khoản - vai trò</h2>
        <div class="form-grid">
          <label>Tài khoản
            <select v-model.number="accountRoleForm.taiKhoanId"><option value="">Chọn</option><option v-for="acc in accountOptions" :key="acc.id" :value="acc.id">#{{ acc.id }} - {{ acc.email }}</option></select>
          </label>
          <label>Vai trò
            <select v-model.number="accountRoleForm.vaiTroId"><option value="">Chọn</option><option v-for="role in vaiTroItems" :key="role.id" :value="role.id">{{ role.maVaiTro }} - {{ role.tenVaiTro }}</option></select>
          </label>
        </div>
        <div class="actions"><button class="btn primary" type="button" @click="saveAccountRole">Lưu liên kết</button><button class="btn ghost" type="button" @click="resetAccountRoleForm">Hủy</button></div>
      </div>
      <div class="card">
        <h2>Liên kết vai trò - quyền</h2>
        <div class="form-grid">
          <label>Vai trò
            <select v-model.number="rolePermissionForm.vaiTroId"><option value="">Chọn</option><option v-for="role in vaiTroItems" :key="role.id" :value="role.id">{{ role.maVaiTro }} - {{ role.tenVaiTro }}</option></select>
          </label>
          <label>Quyền
            <select v-model.number="rolePermissionForm.quyenId"><option value="">Chọn</option><option v-for="permission in quyenItems" :key="permission.id" :value="permission.id">{{ permission.maQuyen }} - {{ permission.tenQuyen }}</option></select>
          </label>
        </div>
        <div class="actions"><button class="btn primary" type="button" @click="saveRolePermission">Lưu liên kết</button><button class="btn ghost" type="button" @click="resetRolePermissionForm">Hủy</button></div>
      </div>
    </section>

    <section class="toolbar card">
      <label>Tìm kiếm<input v-model.trim="filters.keyword" placeholder="Email, mã, họ tên..." @keyup.enter="reloadMain" /></label>
      <label v-if="isAccountView || view === 'nhanVien' || view === 'daoTaoAdmin'">Trạng thái
        <select v-model="filters.trangThai" @change="reloadMain">
          <option value="">Tất cả</option>
          <option v-for="item in currentConfig.statuses" :key="item.value" :value="item.value">{{ item.label }}</option>
        </select>
      </label>
      <label v-if="isAccountView">Vai trò<input v-model.trim="filters.vaiTro" placeholder="ADMIN, SINH_VIEN..." @keyup.enter="reloadMain" /></label>
      <label>Số dòng
        <select v-model.number="pageState.size" @change="changeSize">
          <option :value="10">10</option>
          <option :value="20">20</option>
          <option :value="50">50</option>
        </select>
      </label>
      <button class="btn primary small" type="button" @click="reloadMain">Lọc</button>
      <button class="btn ghost small" type="button" @click="resetFilter">Xóa lọc</button>
    </section>

    <section class="table-card">
      <div class="table-head">
        <h2>{{ currentConfig.tableTitle }}</h2>
        <span>{{ pageState.totalElements }} dòng</span>
      </div>
      <div class="table-scroll">
        <table>
          <thead><tr><th v-for="col in currentColumns" :key="col.key">{{ col.label }}</th><th>Thao tác</th></tr></thead>
          <tbody>
            <tr v-if="loading"><td :colspan="currentColumns.length + 1">Đang tải dữ liệu...</td></tr>
            <tr v-else-if="currentRows.length === 0"><td :colspan="currentColumns.length + 1">Không có dữ liệu</td></tr>
            <tr v-for="row in currentRows" :key="row._rowKey">
              <td v-for="col in currentColumns" :key="col.key">
                <select v-if="col.edit === 'status'" :value="row[col.key] || ''" @change="quickChange(row, col.key, $event.target.value)">
                  <option v-for="item in currentConfig.statuses" :key="item.value" :value="item.value">{{ item.label }}</option>
                </select>
                <select v-else-if="col.edit === 'accountStatus'" :value="row.trangThai || ''" @change="quickChangeAccountStatus(row, $event.target.value)">
                  <option value="cho_kich_hoat">Chờ kích hoạt</option>
                  <option value="da_kich_hoat">Đã kích hoạt</option>
                  <option value="bi_khoa">Bị khóa</option>
                </select>
                <span v-else>{{ formatCell(row[col.key]) }}</span>
              </td>
              <td class="row-actions">
                <button class="btn ghost tiny" type="button" @click="editRow(row)">Sửa</button>
                <button class="btn danger tiny" type="button" @click="deleteRow(row)">Xóa</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <div class="pagination">
        <button class="btn ghost small" :disabled="pageState.page <= 0" type="button" @click="changePage(pageState.page - 1)">Trước</button>
        <span>Trang {{ pageState.page + 1 }} / {{ Math.max(pageState.totalPages, 1) }}</span>
        <button class="btn ghost small" :disabled="pageState.page + 1 >= pageState.totalPages" type="button" @click="changePage(pageState.page + 1)">Sau</button>
      </div>
    </section>
  </section>
</template>

<script setup>
import { computed, onMounted, reactive, ref, watch } from 'vue';

import { getAllTaiKhoan, getPageTaiKhoan } from '@/api/heThong/ApiRespone/TaiKhoanController.js';
import { createTaiKhoan, deleteTaiKhoan, updateTaiKhoan } from '@/api/heThong/ApiRequest/TaiKhoanController.js';
import { getAllVaiTro } from '@/api/heThong/ApiRespone/VaiTroController.js';
import { createVaiTro, deleteVaiTro, updateVaiTro } from '@/api/heThong/ApiRequest/VaiTroController.js';
import { getAllQuyen } from '@/api/heThong/ApiRespone/QuyenController.js';
import { createQuyen, deleteQuyen, updateQuyen } from '@/api/heThong/ApiRequest/QuyenController.js';
import { getAllTaiKhoanVaiTro } from '@/api/heThong/ApiRespone/TaiKhoanVaiTroController.js';
import { createTaiKhoanVaiTro, deleteTaiKhoanVaiTro, updateTaiKhoanVaiTro } from '@/api/heThong/ApiRequest/TaiKhoanVaiTroController.js';
import { getAllVaiTroQuyen } from '@/api/heThong/ApiRespone/VaiTroQuyenController.js';
import { createVaiTroQuyen, deleteVaiTroQuyen, updateVaiTroQuyen } from '@/api/heThong/ApiRequest/VaiTroQuyenController.js';
import { getAllNhanVien } from '@/api/heThong/ApiRespone/NhanVienController.js';
import { createNhanVien, deleteNhanVien, updateNhanVien } from '@/api/heThong/ApiRequest/NhanVienController.js';
import { getPageSinhVien } from '@/api/sinhVien/response/fetchResponse_SinhVien.js';
import { createSinhVien, deleteSinhVien, updateSinhVien } from '@/api/sinhVien/request/fetchRequest_SinhVien.js';
import { getAllGiaoVien } from '@/api/giangDay/response/fetchResponse_GiaoVien.js';

const props = defineProps({ view: { type: String, default: 'sinhVien' } });

const view = computed(() => props.view);
const loading = ref(false);
const error = ref('');
const message = ref('');

const rows = ref([]);
const taiKhoanItems = ref([]);
const vaiTroItems = ref([]);
const quyenItems = ref([]);
const taiKhoanVaiTroItems = ref([]);
const vaiTroQuyenItems = ref([]);

const filters = reactive({ keyword: '', trangThai: '', vaiTro: '' });
const pageState = reactive({ page: 0, size: 20, totalPages: 0, totalElements: 0 });

const editingAccountId = ref(null);
const editingProfileId = ref(null);
const editingRoleId = ref(null);
const editingPermissionId = ref(null);
const editingAccountRoleId = ref(null);
const editingRolePermissionId = ref(null);

const accountForm = reactive({ email: '', matKhau: '', loaiTaiKhoan: 'sinh_vien', trangThai: 'cho_kich_hoat', roles: [] });
const studentForm = reactive({ maSinhVien: '', hoTen: '', ngaySinh: '', gioiTinh: '', email: '', soDienThoai: '', diaChi: '', trangThai: 'dang_hoc', taiKhoanId: null });
const staffForm = reactive({ taiKhoanId: null, maNhanVien: '', hoTen: '', email: '', soDienThoai: '', phongBan: '', chucVu: '', trangThai: 'dang_lam' });
const roleForm = reactive({ maVaiTro: '', tenVaiTro: '', moTa: '' });
const permissionForm = reactive({ maQuyen: '', tenQuyen: '', moTa: '' });
const accountRoleForm = reactive({ taiKhoanId: '', vaiTroId: '' });
const rolePermissionForm = reactive({ vaiTroId: '', quyenId: '' });

const configs = {
  sinhVien: {
    title: 'Tài khoản sinh viên',
    description: 'Quản lý tài khoản sinh viên, vai trò, quyền và liên kết tới hồ sơ sinh viên.',
    tableTitle: 'Danh sách tài khoản sinh viên',
    accountTypes: ['sinh_vien'],
    statuses: [{ value: 'dang_hoc', label: 'Đang học' }, { value: 'bao_luu', label: 'Bảo lưu' }, { value: 'tot_nghiep', label: 'Tốt nghiệp' }, { value: 'nghi_hoc', label: 'Nghỉ học' }],
  },
  giangVien: {
    title: 'Tài khoản giảng viên',
    description: 'Quản lý tài khoản giảng viên, vai trò, quyền và trạng thái tài khoản.',
    tableTitle: 'Danh sách tài khoản giảng viên',
    accountTypes: ['giao_vien', 'giang_vien'],
    statuses: [{ value: 'cho_kich_hoat', label: 'Chờ kích hoạt' }, { value: 'da_kich_hoat', label: 'Đã kích hoạt' }, { value: 'bi_khoa', label: 'Bị khóa' }],
  },
  nhanVien: {
    title: 'Nhân viên',
    description: 'Quản lý hồ sơ nhân viên và tài khoản phân quyền.',
    tableTitle: 'Danh sách nhân viên',
    accountTypes: ['nhan_vien'],
    statuses: [{ value: 'dang_lam', label: 'Đang làm' }, { value: 'tam_nghi', label: 'Tạm nghỉ' }, { value: 'nghi_viec', label: 'Nghỉ việc' }],
  },
  daoTaoAdmin: {
    title: 'Đào tạo và admin',
    description: 'Quản lý nhân sự phòng đào tạo, tài khoản admin và phân quyền hệ thống.',
    tableTitle: 'Danh sách đào tạo / admin',
    accountTypes: ['dao_tao', 'admin'],
    statuses: [{ value: 'dang_lam', label: 'Đang làm' }, { value: 'tam_nghi', label: 'Tạm nghỉ' }, { value: 'nghi_viec', label: 'Nghỉ việc' }],
  },
  vaiTroQuyen: {
    title: 'Vai trò / Quyền',
    description: 'Quản lý danh mục vai trò và quyền trong hệ thống.',
    tableTitle: 'Danh sách vai trò và quyền',
    accountTypes: [],
    statuses: [],
  },
  lienKet: {
    title: 'Liên kết phân quyền',
    description: 'Gán vai trò cho tài khoản và gán quyền cho vai trò.',
    tableTitle: 'Danh sách liên kết phân quyền',
    accountTypes: [],
    statuses: [],
  },
};

const currentConfig = computed(() => configs[view.value] || configs.sinhVien);
const isAccountView = computed(() => view.value === 'sinhVien' || view.value === 'giangVien');
const accountOptions = computed(() => taiKhoanItems.value);

const accountColumns = [
  { key: 'id', label: 'ID' },
  { key: 'email', label: 'Email' },
  { key: 'loaiTaiKhoan', label: 'Loại tài khoản' },
  { key: 'trangThai', label: 'Trạng thái TK', edit: 'accountStatus' },
  { key: 'rolesText', label: 'Vai trò' },
  { key: 'permissionsText', label: 'Quyền' },
  { key: 'profileId', label: 'Hồ sơ ID' },
  { key: 'profileName', label: 'Tên liên kết' },
  { key: 'profileStatus', label: 'Trạng thái hồ sơ', edit: 'status' },
];

const staffColumns = [
  { key: 'id', label: 'ID' },
  { key: 'taiKhoanId', label: 'Tài khoản ID' },
  { key: 'maNhanVien', label: 'Mã nhân viên' },
  { key: 'hoTen', label: 'Họ tên' },
  { key: 'email', label: 'Email' },
  { key: 'phongBan', label: 'Phòng ban' },
  { key: 'chucVu', label: 'Chức vụ' },
  { key: 'trangThai', label: 'Trạng thái', edit: 'status' },
];

const rolePermissionColumns = [
  { key: 'kind', label: 'Loại' },
  { key: 'id', label: 'ID' },
  { key: 'code', label: 'Mã' },
  { key: 'name', label: 'Tên' },
  { key: 'description', label: 'Mô tả' },
];

const linkColumns = [
  { key: 'kind', label: 'Loại liên kết' },
  { key: 'id', label: 'ID' },
  { key: 'leftId', label: 'ID trái' },
  { key: 'leftName', label: 'Tên trái' },
  { key: 'rightId', label: 'ID phải' },
  { key: 'rightName', label: 'Tên phải' },
];

const currentColumns = computed(() => {
  if (view.value === 'vaiTroQuyen') return rolePermissionColumns;
  if (view.value === 'lienKet') return linkColumns;
  if (view.value === 'nhanVien' || view.value === 'daoTaoAdmin') return staffColumns;
  return accountColumns;
});

const currentRows = computed(() => rows.value.map((row, index) => ({ ...row, _rowKey: `${view.value}-${row.kind || 'row'}-${row.id || index}` })));

watch(view, async () => {
  resetState();
  await loadAll();
});

onMounted(loadAll);

async function loadAll() {
  clearNotice();
  loading.value = true;
  try {
    await loadBaseData();
    await reloadMain();
  } catch (e) {
    error.value = getErrorMessage(e);
  } finally {
    loading.value = false;
  }
}

async function loadBaseData() {
  const [accountsRes, rolesRes, permissionsRes, accountRolesRes, rolePermissionsRes] = await Promise.all([
    getAllTaiKhoan({ page: 0, size: 1000 }),
    getAllVaiTro({ page: 0, size: 1000 }),
    getAllQuyen({ page: 0, size: 1000 }),
    getAllTaiKhoanVaiTro({ page: 0, size: 1000 }),
    getAllVaiTroQuyen({ page: 0, size: 1000 }),
  ]);
  taiKhoanItems.value = getList(accountsRes);
  vaiTroItems.value = getList(rolesRes);
  quyenItems.value = getList(permissionsRes);
  taiKhoanVaiTroItems.value = getList(accountRolesRes);
  vaiTroQuyenItems.value = getList(rolePermissionsRes);
}

async function reloadMain() {
  clearNotice();
  loading.value = true;
  try {
    if (view.value === 'sinhVien') await loadStudentAccounts();
    if (view.value === 'giangVien') await loadTeacherAccounts();
    if (view.value === 'nhanVien' || view.value === 'daoTaoAdmin') await loadStaff();
    if (view.value === 'vaiTroQuyen') loadRolePermissionRows();
    if (view.value === 'lienKet') loadLinkRows();
  } catch (e) {
    error.value = getErrorMessage(e);
  } finally {
    loading.value = false;
  }
}

async function loadStudentAccounts() {
  const [accountsRes, studentsRes] = await Promise.all([
    getPageTaiKhoan({ keyword: filters.keyword, loaiTaiKhoan: 'sinh_vien', trangThai: '', vaiTro: filters.vaiTro, page: pageState.page, size: pageState.size }),
    getPageSinhVien({ keyword: filters.keyword, trangThai: filters.trangThai, page: pageState.page, size: pageState.size }),
  ]);
  const accounts = getList(accountsRes);
  const students = getList(studentsRes);
  const pageInfo = getPageInfo(studentsRes, students.length);
  rows.value = students.map(student => {
    const account = accounts.find(item => Number(item.id) === Number(student.taiKhoanId)) || taiKhoanItems.value.find(item => Number(item.id) === Number(student.taiKhoanId)) || {};
    return mapAccountRow(account, student);
  });
  setPageInfo(pageInfo);
}

async function loadTeacherAccounts() {
  const [accountsRes, teachersRes] = await Promise.all([
    getPageTaiKhoan({ keyword: filters.keyword, loaiTaiKhoan: 'giao_vien', trangThai: filters.trangThai, vaiTro: filters.vaiTro, page: pageState.page, size: pageState.size }),
    getAllGiaoVien({ page: 0, size: 1000 }),
  ]);
  const accounts = getList(accountsRes);
  const teachers = getList(teachersRes);
  rows.value = accounts.map(account => {
    const profile = teachers.find(item => Number(item.taiKhoanId) === Number(account.id)) || {};
    return mapAccountRow(account, profile);
  });
  setPageInfo(getPageInfo(accountsRes, rows.value.length));
}

async function loadStaff() {
  const res = await getAllNhanVien({ page: pageState.page, size: pageState.size });
  let data = getList(res);
  if (view.value === 'daoTaoAdmin') {
    data = data.filter(item => includesAny([item.phongBan, item.chucVu, item.email], ['dao tao', 'đào tạo', 'admin']));
  }
  if (filters.keyword) data = data.filter(item => includesAny([item.maNhanVien, item.hoTen, item.email, item.phongBan, item.chucVu], [filters.keyword]));
  if (filters.trangThai) data = data.filter(item => String(item.trangThai || '') === filters.trangThai);
  rows.value = data;
  setPageInfo(getPageInfo(res, data.length));
}

function loadRolePermissionRows() {
  const roleRows = vaiTroItems.value.map(item => ({ kind: 'Vai trò', id: item.id, code: item.maVaiTro, name: item.tenVaiTro, description: item.moTa, source: item }));
  const permissionRows = quyenItems.value.map(item => ({ kind: 'Quyền', id: item.id, code: item.maQuyen, name: item.tenQuyen, description: item.moTa, source: item }));
  rows.value = filterByKeyword([...roleRows, ...permissionRows]);
  setClientPage(rows.value.length);
}

function loadLinkRows() {
  const accountRoleRows = taiKhoanVaiTroItems.value.map(item => {
    const acc = taiKhoanItems.value.find(account => Number(account.id) === Number(item.taiKhoanId));
    const role = vaiTroItems.value.find(vaiTro => Number(vaiTro.id) === Number(item.vaiTroId));
    return { kind: 'Tài khoản - vai trò', id: item.id, leftId: item.taiKhoanId, leftName: acc?.email, rightId: item.vaiTroId, rightName: role?.maVaiTro || role?.tenVaiTro, source: item };
  });
  const rolePermissionRows = vaiTroQuyenItems.value.map(item => {
    const role = vaiTroItems.value.find(vaiTro => Number(vaiTro.id) === Number(item.vaiTroId));
    const permission = quyenItems.value.find(quyen => Number(quyen.id) === Number(item.quyenId));
    return { kind: 'Vai trò - quyền', id: item.id, leftId: item.vaiTroId, leftName: role?.maVaiTro || role?.tenVaiTro, rightId: item.quyenId, rightName: permission?.maQuyen || permission?.tenQuyen, source: item };
  });
  rows.value = filterByKeyword([...accountRoleRows, ...rolePermissionRows]);
  setClientPage(rows.value.length);
}

function mapAccountRow(account, profile = {}) {
  return {
    id: account.id || profile.taiKhoanId,
    email: account.email || profile.email,
    loaiTaiKhoan: account.loaiTaiKhoan,
    trangThai: account.trangThai,
    rolesText: Array.isArray(account.roles) ? account.roles.join(', ') : '',
    permissionsText: Array.isArray(account.permissions) ? account.permissions.join(', ') : '',
    profileId: profile.id,
    profileName: profile.hoTen || profile.tenGiaoVien,
    profileStatus: profile.trangThai,
    account,
    profile,
  };
}

async function saveAccount() {
  clearNotice();
  const payload = { email: accountForm.email, matKhau: accountForm.matKhau || undefined, loaiTaiKhoan: accountForm.loaiTaiKhoan, trangThai: accountForm.trangThai, roles: accountForm.roles };
  try {
    if (editingAccountId.value) await updateTaiKhoan(editingAccountId.value, payload);
    else await createTaiKhoan(payload);
    message.value = 'Đã lưu tài khoản';
    resetAccountForm();
    await loadAll();
  } catch (e) { error.value = getErrorMessage(e); }
}

async function saveStudent() {
  clearNotice();
  const payload = cleanPayload(studentForm);
  try {
    if (editingProfileId.value) await updateSinhVien(editingProfileId.value, payload);
    else await createSinhVien(payload);
    message.value = 'Đã lưu sinh viên';
    resetStudentForm();
    await loadAll();
  } catch (e) { error.value = getErrorMessage(e); }
}

async function saveStaff() {
  clearNotice();
  const payload = cleanPayload(staffForm);
  try {
    if (editingProfileId.value) await updateNhanVien(editingProfileId.value, payload);
    else await createNhanVien(payload);
    message.value = 'Đã lưu nhân viên';
    resetStaffForm();
    await loadAll();
  } catch (e) { error.value = getErrorMessage(e); }
}

async function saveRole() {
  clearNotice();
  try {
    const payload = cleanPayload(roleForm);
    if (editingRoleId.value) await updateVaiTro(editingRoleId.value, payload);
    else await createVaiTro(payload);
    message.value = 'Đã lưu vai trò';
    resetRoleForm();
    await loadAll();
  } catch (e) { error.value = getErrorMessage(e); }
}

async function savePermission() {
  clearNotice();
  try {
    const payload = cleanPayload(permissionForm);
    if (editingPermissionId.value) await updateQuyen(editingPermissionId.value, payload);
    else await createQuyen(payload);
    message.value = 'Đã lưu quyền';
    resetPermissionForm();
    await loadAll();
  } catch (e) { error.value = getErrorMessage(e); }
}

async function saveAccountRole() {
  clearNotice();
  try {
    const payload = cleanPayload(accountRoleForm);
    if (editingAccountRoleId.value) await updateTaiKhoanVaiTro(editingAccountRoleId.value, payload);
    else await createTaiKhoanVaiTro(payload);
    message.value = 'Đã lưu liên kết tài khoản - vai trò';
    resetAccountRoleForm();
    await loadAll();
  } catch (e) { error.value = getErrorMessage(e); }
}

async function saveRolePermission() {
  clearNotice();
  try {
    const payload = cleanPayload(rolePermissionForm);
    if (editingRolePermissionId.value) await updateVaiTroQuyen(editingRolePermissionId.value, payload);
    else await createVaiTroQuyen(payload);
    message.value = 'Đã lưu liên kết vai trò - quyền';
    resetRolePermissionForm();
    await loadAll();
  } catch (e) { error.value = getErrorMessage(e); }
}

function editRow(row) {
  if (isAccountView.value) {
    const account = row.account || row;
    editingAccountId.value = account.id || row.id;
    Object.assign(accountForm, { email: account.email || '', matKhau: '', loaiTaiKhoan: account.loaiTaiKhoan || currentConfig.value.accountTypes[0], trangThai: account.trangThai || 'cho_kich_hoat', roles: Array.isArray(account.roles) ? [...account.roles] : [] });
    if (view.value === 'sinhVien' && row.profile?.id) {
      editingProfileId.value = row.profile.id;
      Object.assign(studentForm, { ...defaultStudentForm(), ...row.profile });
    }
    return;
  }
  if (view.value === 'nhanVien' || view.value === 'daoTaoAdmin') {
    editingProfileId.value = row.id;
    Object.assign(staffForm, { ...defaultStaffForm(), ...row });
    return;
  }
  if (view.value === 'vaiTroQuyen') {
    if (row.kind === 'Vai trò') {
      editingRoleId.value = row.id;
      Object.assign(roleForm, { maVaiTro: row.source.maVaiTro || '', tenVaiTro: row.source.tenVaiTro || '', moTa: row.source.moTa || '' });
    } else {
      editingPermissionId.value = row.id;
      Object.assign(permissionForm, { maQuyen: row.source.maQuyen || '', tenQuyen: row.source.tenQuyen || '', moTa: row.source.moTa || '' });
    }
    return;
  }
  if (view.value === 'lienKet') {
    if (row.kind === 'Tài khoản - vai trò') {
      editingAccountRoleId.value = row.id;
      Object.assign(accountRoleForm, { taiKhoanId: row.source.taiKhoanId || '', vaiTroId: row.source.vaiTroId || '' });
    } else {
      editingRolePermissionId.value = row.id;
      Object.assign(rolePermissionForm, { vaiTroId: row.source.vaiTroId || '', quyenId: row.source.quyenId || '' });
    }
  }
}

async function deleteRow(row) {
  clearNotice();
  try {
    if (isAccountView.value) await deleteTaiKhoan(row.id);
    else if (view.value === 'nhanVien' || view.value === 'daoTaoAdmin') await deleteNhanVien(row.id);
    else if (view.value === 'vaiTroQuyen' && row.kind === 'Vai trò') await deleteVaiTro(row.id);
    else if (view.value === 'vaiTroQuyen') await deleteQuyen(row.id);
    else if (view.value === 'lienKet' && row.kind === 'Tài khoản - vai trò') await deleteTaiKhoanVaiTro(row.id);
    else if (view.value === 'lienKet') await deleteVaiTroQuyen(row.id);
    message.value = 'Đã xóa dữ liệu';
    await loadAll();
  } catch (e) { error.value = getErrorMessage(e); }
}

async function quickChange(row, key, value) {
  if (view.value === 'sinhVien' && row.profile?.id) {
    await updateSinhVien(row.profile.id, { ...row.profile, trangThai: value });
  } else if ((view.value === 'nhanVien' || view.value === 'daoTaoAdmin') && row.id) {
    await updateNhanVien(row.id, { ...row, [key]: value });
  }
  await loadAll();
}

async function quickChangeAccountStatus(row, value) {
  if (!row.id) return;
  const account = row.account || row;
  await updateTaiKhoan(row.id, { email: account.email, matKhau: undefined, loaiTaiKhoan: account.loaiTaiKhoan, trangThai: value, roles: account.roles || [] });
  await loadAll();
}

function changePage(page) {
  pageState.page = page;
  reloadMain();
}

function changeSize() {
  pageState.page = 0;
  reloadMain();
}

function resetFilter() {
  Object.assign(filters, { keyword: '', trangThai: '', vaiTro: '' });
  pageState.page = 0;
  reloadMain();
}

function resetState() {
  rows.value = [];
  Object.assign(filters, { keyword: '', trangThai: '', vaiTro: '' });
  Object.assign(pageState, { page: 0, size: 20, totalPages: 0, totalElements: 0 });
  resetAccountForm();
  resetStudentForm();
  resetStaffForm();
  resetRoleForm();
  resetPermissionForm();
  resetAccountRoleForm();
  resetRolePermissionForm();
}

function resetAccountForm() {
  editingAccountId.value = null;
  Object.assign(accountForm, { email: '', matKhau: '', loaiTaiKhoan: currentConfig.value.accountTypes[0] || 'sinh_vien', trangThai: 'cho_kich_hoat', roles: [] });
}

function resetStudentForm() {
  editingProfileId.value = null;
  Object.assign(studentForm, defaultStudentForm());
}

function resetStaffForm() {
  editingProfileId.value = null;
  Object.assign(staffForm, defaultStaffForm());
}

function resetRoleForm() {
  editingRoleId.value = null;
  Object.assign(roleForm, { maVaiTro: '', tenVaiTro: '', moTa: '' });
}

function resetPermissionForm() {
  editingPermissionId.value = null;
  Object.assign(permissionForm, { maQuyen: '', tenQuyen: '', moTa: '' });
}

function resetAccountRoleForm() {
  editingAccountRoleId.value = null;
  Object.assign(accountRoleForm, { taiKhoanId: '', vaiTroId: '' });
}

function resetRolePermissionForm() {
  editingRolePermissionId.value = null;
  Object.assign(rolePermissionForm, { vaiTroId: '', quyenId: '' });
}

function defaultStudentForm() {
  return { maSinhVien: '', hoTen: '', ngaySinh: '', gioiTinh: '', email: '', soDienThoai: '', diaChi: '', trangThai: 'dang_hoc', taiKhoanId: null };
}

function defaultStaffForm() {
  return { taiKhoanId: null, maNhanVien: '', hoTen: '', email: '', soDienThoai: '', phongBan: '', chucVu: '', trangThai: 'dang_lam' };
}

function getList(res) {
  const payload = res?.data ?? res;
  const data = payload?.data ?? payload;
  if (Array.isArray(data)) return data;
  if (Array.isArray(data?.content)) return data.content;
  if (Array.isArray(payload?.content)) return payload.content;
  return [];
}

function getPageInfo(res, fallbackTotal) {
  const payload = res?.data ?? res;
  const data = payload?.data ?? payload;
  return {
    page: Number(data?.number ?? data?.pageable?.pageNumber ?? pageState.page),
    size: Number(data?.size ?? pageState.size),
    totalPages: Number(data?.totalPages ?? 1),
    totalElements: Number(data?.totalElements ?? fallbackTotal ?? 0),
  };
}

function setPageInfo(info) {
  Object.assign(pageState, info);
}

function setClientPage(total) {
  pageState.totalElements = total;
  pageState.totalPages = Math.max(Math.ceil(total / pageState.size), 1);
}

function filterByKeyword(data) {
  if (!filters.keyword) return data;
  return data.filter(item => includesAny(Object.values(item), [filters.keyword]));
}

function includesAny(values, terms) {
  const haystack = values.map(value => normalize(value)).join(' ');
  return terms.some(term => haystack.includes(normalize(term)));
}

function normalize(value) {
  return String(value ?? '').toLowerCase().normalize('NFD').replace(/[\u0300-\u036f]/g, '');
}

function cleanPayload(source) {
  return Object.fromEntries(Object.entries({ ...source }).filter(([, value]) => value !== '' && value !== undefined));
}

function formatCell(value) {
  if (Array.isArray(value)) return value.join(', ');
  return value ?? '';
}

function labelAccountType(value) {
  const labels = { sinh_vien: 'Sinh viên', giao_vien: 'Giáo viên', giang_vien: 'Giảng viên', nhan_vien: 'Nhân viên', dao_tao: 'Đào tạo', admin: 'Admin' };
  return labels[value] || value;
}

function clearNotice() {
  error.value = '';
  message.value = '';
}

function getErrorMessage(e) {
  return e?.response?.data?.message || e?.message || 'Có lỗi xảy ra';
}
</script>

<style scoped>
.system-page {
  min-width: 0;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.page-head,
.card,
.table-card {
  background: #fff;
  border: 1px solid #e5e7eb;
  border-radius: 12px;
  box-shadow: 0 8px 24px rgba(15, 23, 42, 0.06);
}

.page-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  padding: 14px 16px;
}

.page-head h1,
.card h2,
.table-head h2 {
  margin: 0;
  color: #0f172a;
}

.page-head h1 {
  font-size: 22px;
}

.page-head p {
  margin: 4px 0 0;
  color: #64748b;
  font-size: 13px;
}

.card {
  padding: 14px;
}

.card h2,
.table-head h2 {
  font-size: 16px;
}

.grid-form {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 12px;
}

.form-grid,
.toolbar {
  display: grid;
  grid-template-columns: repeat(4, minmax(150px, 1fr));
  gap: 10px;
  align-items: end;
}

.form-grid label,
.toolbar label {
  display: flex;
  flex-direction: column;
  gap: 5px;
  font-size: 12px;
  font-weight: 700;
  color: #334155;
}

.wide {
  grid-column: span 2;
}

input,
select {
  width: 100%;
  min-height: 34px;
  border: 1px solid #cbd5e1;
  border-radius: 8px;
  padding: 6px 9px;
  color: #0f172a;
  background: #fff;
  outline: none;
}

select[multiple] {
  min-height: 82px;
}

input:focus,
select:focus {
  border-color: #2563eb;
  box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.12);
}

.actions,
.pagination,
.table-head {
  display: flex;
  align-items: center;
  gap: 8px;
}

.actions {
  margin-top: 12px;
}

.table-head {
  justify-content: space-between;
  padding: 12px 14px;
  border-bottom: 1px solid #e5e7eb;
}

.table-head span,
.pagination span {
  color: #64748b;
  font-size: 13px;
  font-weight: 700;
}

.table-scroll {
  width: 100%;
  overflow: auto;
}

table {
  width: max-content;
  min-width: 100%;
  border-collapse: collapse;
  table-layout: fixed;
}

th,
td {
  min-width: 150px;
  max-width: 260px;
  border: 1px solid rgba(148, 163, 184, 0.35);
  padding: 8px 10px;
  vertical-align: middle;
  font-size: 13px;
  color: #0f172a;
  background: #fff;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

th {
  position: sticky;
  top: 0;
  z-index: 2;
  background: #f8fafc;
  color: #334155;
  font-weight: 800;
}

tbody tr:nth-child(even) td {
  background: #fbfdff;
}

tbody tr:hover td {
  background: #eff6ff;
}

.row-actions {
  position: sticky;
  right: 0;
  z-index: 1;
  min-width: 130px;
  background: #fff;
}

.pagination {
  justify-content: flex-end;
  padding: 12px 14px;
  border-top: 1px solid #e5e7eb;
}

.btn {
  border: 0;
  border-radius: 8px;
  padding: 8px 12px;
  font-weight: 800;
  cursor: pointer;
}

.btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.primary {
  background: #2563eb;
  color: #fff;
}

.secondary {
  background: #0f172a;
  color: #fff;
}

.ghost {
  background: #e2e8f0;
  color: #0f172a;
}

.danger {
  background: #fee2e2;
  color: #b91c1c;
}

.small {
  padding: 7px 10px;
  font-size: 12px;
}

.tiny {
  padding: 5px 8px;
  font-size: 12px;
}

.message,
.error {
  padding: 10px 12px;
  border-radius: 10px;
  font-size: 13px;
  font-weight: 700;
}

.message {
  background: #dcfce7;
  color: #166534;
}

.error {
  background: #fee2e2;
  color: #991b1b;
}

@media (max-width: 1100px) {
  .grid-form,
  .form-grid,
  .toolbar {
    grid-template-columns: 1fr;
  }

  .wide {
    grid-column: span 1;
  }
}
</style>
