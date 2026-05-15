<template>
  <section class="ct-screen">
    <div class="ct-shell">
      <header class="ct-page-head">
        <div class="ct-head-main">
          <div class="ct-kicker">Chương trình đào tạo</div>
          <h1 class="ct-title">Danh sách chương trình</h1>
          <p class="ct-desc">Quản lý chương trình đào tạo theo ngành, trình độ, loại chương trình và thời gian đào tạo.</p>
        </div>
        <div class="ct-actions">
          <RouterLink class="ct-btn light" to="/quy-trinh/chuong-trinh">Quay lại</RouterLink>
          <button class="ct-btn" type="button" @click="openCreate">Thêm chương trình</button>
        </div>
      </header>

      <section class="ct-filter">
        <input v-model.trim="keyword" class="ct-input" placeholder="Tìm mã, tên chương trình, đối tượng tuyển sinh..." />
        <input v-model.trim="filters.nganhId" class="ct-input" placeholder="Ngành ID" />
        <input v-model.trim="filters.trinhDoId" class="ct-input" placeholder="Trình độ ID" />
        <input v-model.trim="filters.loaiChuongTrinhId" class="ct-input" placeholder="Loại CT ID" />
        <button class="ct-btn light" type="button" @click="loadData">Tải lại</button>
      </section>

      <section class="ct-panel">
        <div class="ct-panel-head">
          <div>
            <h2 class="ct-panel-title">Chương trình đào tạo</h2>
            <p class="ct-panel-subtitle">Bấm mở để xem phiên bản và khung chương trình.</p>
          </div>
          <span class="ct-badge">{{ filteredRows.length }} bản ghi</span>
        </div>
        <div v-if="loading" class="ct-loading">Đang tải dữ liệu...</div>
        <div v-else-if="error" class="ct-error">{{ error }}</div>
        <div v-else class="ct-table-wrap">
          <table class="ct-table">
            <thead><tr><th>Mã CT</th><th>Tên chương trình</th><th>Ngành</th><th>Trình độ</th><th>Loại</th><th>Thời gian</th><th class="ct-right">Thao tác</th></tr></thead>
            <tbody>
              <tr v-for="item in filteredRows" :key="layIdBanGhi(item)" class="ct-clickable-row" @dblclick="goDetail(item)">
                <td><span class="ct-badge">{{ item.maChuongTrinh || '—' }}</span></td>
                <td class="ct-strong">{{ item.tenChuongTrinh || '—' }}</td>
                <td>{{ item.tenNganh || item.nganhId || '—' }}</td>
                <td>{{ item.tenTrinhDo || item.trinhDoId || '—' }}</td>
                <td>{{ item.tenLoaiChuongTrinh || item.loaiChuongTrinhId || '—' }}</td>
                <td>{{ item.thoiGianDaoTao || '—' }}</td>
                <td class="ct-right">
                  <span class="ct-row-actions">
                    <RouterLink class="ct-btn small ghost" :to="`/quy-trinh/chuong-trinh/${layIdBanGhi(item)}`">Mở</RouterLink>
                    <button class="ct-btn small light" type="button" @click="openEdit(item)">Sửa</button>
                    <button class="ct-btn small danger" type="button" @click="removeRow(item)">Xóa</button>
                  </span>
                </td>
              </tr>
              <tr v-if="!filteredRows.length"><td colspan="7" class="ct-empty">Không có chương trình phù hợp.</td></tr>
            </tbody>
          </table>
        </div>
      </section>
    </div>

    <BieuMauQuanLyModal
      v-model="form"
      :open="modalOpen"
      :mode="formMode"
      :fields="fields"
      :saving="saving"
      title="Chương trình đào tạo"
      @close="closeModal"
      @submit="saveForm"
    />
  </section>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue';
import { useRouter } from 'vue-router';
import './luongChuongTrinh.css';
import BieuMauQuanLyModal from '@/modules/chuongTrinh/components/modal/BieuMauQuanLyModal.vue';
import { CHUONG_TRINH_RESOURCE_MAP } from '@/modules/chuongTrinh/constants/trangThaiChuongTrinh.js';
import { API_LUONG_CHUONG_TRINH, capNhatBanGhi, layDanhSachChuongTrinh, layIdBanGhi, taoBanGhi, xoaBanGhi } from '@/modules/chuongTrinh/services/luongChuongTrinhDaoTaoService.js';

const router = useRouter();
const rows = ref([]);
const loading = ref(false);
const saving = ref(false);
const error = ref('');
const keyword = ref('');
const filters = reactive({ nganhId: '', trinhDoId: '', loaiChuongTrinhId: '' });
const modalOpen = ref(false);
const formMode = ref('create');
const form = ref({});
const fields = CHUONG_TRINH_RESOURCE_MAP['chuong-trinh'].fields;

const filteredRows = computed(() => rows.value.filter((item) => {
  const haystack = [item.maChuongTrinh, item.tenChuongTrinh, item.doiTuongTuyenSinh, item.thoiGianDaoTao].join(' ').toLowerCase();
  const okKeyword = !keyword.value || haystack.includes(keyword.value.toLowerCase());
  const okNganh = !filters.nganhId || String(item.nganhId) === String(filters.nganhId);
  const okTrinhDo = !filters.trinhDoId || String(item.trinhDoId) === String(filters.trinhDoId);
  const okLoai = !filters.loaiChuongTrinhId || String(item.loaiChuongTrinhId) === String(filters.loaiChuongTrinhId);
  return okKeyword && okNganh && okTrinhDo && okLoai;
}));

const loadData = async () => {
  loading.value = true;
  error.value = '';
  try { rows.value = await layDanhSachChuongTrinh(); } catch (err) { error.value = err?.message || 'Không tải được danh sách chương trình.'; } finally { loading.value = false; }
};
const goDetail = (item) => { const id = layIdBanGhi(item); if (id) router.push(`/quy-trinh/chuong-trinh/${id}`); };
const openCreate = () => { formMode.value = 'create'; form.value = {}; modalOpen.value = true; };
const openEdit = (item) => { formMode.value = 'edit'; form.value = { ...item }; modalOpen.value = true; };
const closeModal = () => { modalOpen.value = false; form.value = {}; };
const saveForm = async () => {
  saving.value = true;
  try {
    const id = layIdBanGhi(form.value);
    if (formMode.value === 'edit' && id) await capNhatBanGhi(API_LUONG_CHUONG_TRINH.CHUONG_TRINH, id, form.value);
    else await taoBanGhi(API_LUONG_CHUONG_TRINH.CHUONG_TRINH, form.value);
    closeModal(); await loadData();
  } finally { saving.value = false; }
};
const removeRow = async (item) => { const id = layIdBanGhi(item); if (id && confirm('Xóa chương trình này?')) { await xoaBanGhi(API_LUONG_CHUONG_TRINH.CHUONG_TRINH, id); await loadData(); } };

onMounted(loadData);
</script>
