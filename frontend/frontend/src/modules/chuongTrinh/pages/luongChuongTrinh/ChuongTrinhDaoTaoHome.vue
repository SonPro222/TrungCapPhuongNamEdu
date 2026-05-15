<template>
  <section class="ct-screen">
    <div class="ct-shell">
      <header class="ct-page-head compact">
        <div class="ct-head-main">
          <div class="ct-kicker">Chương trình đào tạo</div>
          <h1 class="ct-title">Quản lý chương trình đào tạo</h1>
          <p class="ct-desc">Danh sách chương trình, phiên bản và khung đào tạo được quản lý theo dữ liệu backend.</p>
        </div>
      </header>

      <section class="ct-panel">
        <div class="ct-panel-head compact">
          <div>
            <h2 class="ct-panel-title">Danh sách chương trình</h2>
            <p class="ct-panel-subtitle">Mở chương trình để quản lý phiên bản, khung chương trình và môn học.</p>
          </div>
          <button class="ct-btn small" type="button" @click="openCreateChuongTrinh">Thêm mới</button>
        </div>

        <div v-if="loading" class="ct-loading">Đang tải dữ liệu...</div>
        <div v-else-if="error" class="ct-error">{{ error }}</div>
        <div v-else class="ct-table-wrap">
          <table class="ct-table compact">
            <thead>
              <tr>
                <th>Mã CT</th>
                <th>Tên chương trình</th>
                <th>Ngành</th>
                <th>Trình độ</th>
                <th>Thời gian</th>
                <th class="ct-right">Thao tác</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="item in chuongTrinhRows" :key="layIdBanGhi(item)">
                <td><span class="ct-badge">{{ item.maChuongTrinh || '—' }}</span></td>
                <td class="ct-strong">{{ item.tenChuongTrinh || '—' }}</td>
                <td>{{ item.tenNganh || item.nganhId || '—' }}</td>
                <td>{{ item.tenTrinhDo || item.trinhDoId || '—' }}</td>
                <td>{{ item.thoiGianDaoTao || '—' }}</td>
                <td class="ct-right">
                  <span class="ct-row-actions">
                    <RouterLink class="ct-btn small ghost" :to="`/quy-trinh/chuong-trinh/${layIdBanGhi(item)}`">Mở</RouterLink>
                    <button class="ct-btn small light" type="button" @click="openEditChuongTrinh(item)">Sửa</button>
                    <button class="ct-btn small danger" type="button" @click="removeChuongTrinh(item)">Xóa</button>
                  </span>
                </td>
              </tr>
              <tr v-if="!chuongTrinhRows.length"><td colspan="6" class="ct-empty">Chưa có dữ liệu chương trình.</td></tr>
            </tbody>
          </table>
        </div>
      </section>
    </div>

    <BieuMauQuanLyModal
      v-model="chuongTrinhForm"
      :open="chuongTrinhModal"
      :mode="chuongTrinhMode"
      :fields="chuongTrinhFields"
      :saving="saving"
      title="Chương trình đào tạo"
      @close="chuongTrinhModal = false"
      @submit="saveChuongTrinh"
    />
  </section>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import './luongChuongTrinh.css';
import BieuMauQuanLyModal from '@/modules/chuongTrinh/components/modal/BieuMauQuanLyModal.vue';
import { CHUONG_TRINH_RESOURCE_MAP } from '@/modules/chuongTrinh/constants/trangThaiChuongTrinh.js';
import {
  API_LUONG_CHUONG_TRINH,
  capNhatBanGhi,
  layDanhSachChuongTrinh,
  layIdBanGhi,
  taoBanGhi,
  xoaBanGhi,
} from '@/modules/chuongTrinh/services/luongChuongTrinhDaoTaoService.js';

const chuongTrinhRows = ref([]);
const loading = ref(false);
const saving = ref(false);
const error = ref('');
const chuongTrinhModal = ref(false);
const chuongTrinhMode = ref('create');
const chuongTrinhForm = ref({});
const chuongTrinhFields = CHUONG_TRINH_RESOURCE_MAP['chuong-trinh'].fields;

const loadData = async () => {
  loading.value = true;
  error.value = '';

  try {
    chuongTrinhRows.value = await layDanhSachChuongTrinh();
  } catch (err) {
    error.value = err?.message || 'Không tải được dữ liệu chương trình.';
  } finally {
    loading.value = false;
  }
};

const openCreateChuongTrinh = () => {
  chuongTrinhMode.value = 'create';
  chuongTrinhForm.value = {};
  chuongTrinhModal.value = true;
};

const openEditChuongTrinh = (item) => {
  chuongTrinhMode.value = 'edit';
  chuongTrinhForm.value = { ...item };
  chuongTrinhModal.value = true;
};

const saveChuongTrinh = async () => {
  saving.value = true;

  try {
    const id = layIdBanGhi(chuongTrinhForm.value);

    if (chuongTrinhMode.value === 'edit' && id) {
      await capNhatBanGhi(API_LUONG_CHUONG_TRINH.CHUONG_TRINH, id, chuongTrinhForm.value);
    } else {
      await taoBanGhi(API_LUONG_CHUONG_TRINH.CHUONG_TRINH, chuongTrinhForm.value);
    }

    chuongTrinhModal.value = false;
    await loadData();
  } finally {
    saving.value = false;
  }
};

const removeChuongTrinh = async (item) => {
  const id = layIdBanGhi(item);
  if (!id) return;

  if (confirm('Xóa chương trình đào tạo này?')) {
    await xoaBanGhi(API_LUONG_CHUONG_TRINH.CHUONG_TRINH, id);
    await loadData();
  }
};

onMounted(loadData);
</script>
