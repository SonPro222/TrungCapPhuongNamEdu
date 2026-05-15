<template>
  <section class="ct-screen">
    <div class="ct-shell">
      <header class="ct-page-head compact">
        <div class="ct-head-main">
          <div class="ct-kicker">{{ nhomHienThi }}</div>
          <h1 class="ct-title">{{ resource.title }}</h1>
          <p class="ct-desc">{{ resource.description || 'Quản lý nội dung theo luồng chương trình đào tạo.' }}</p>
        </div>
        <div class="ct-actions">
          <RouterLink class="ct-btn light" to="/quy-trinh/chuong-trinh">Chương trình</RouterLink>
          <button class="ct-btn" type="button" @click="openCreate">Thêm mới</button>
        </div>
      </header>

      <section class="ct-panel">
        <div class="ct-panel-head compact">
          <div>
            <h2 class="ct-panel-title">{{ tieuDeBang }}</h2>
            <p class="ct-panel-subtitle">{{ rows.length }} bản ghi. Click Mở để vào đúng trang làm việc nếu bản ghi có liên kết.</p>
          </div>
          <button class="ct-btn small light" type="button" @click="loadData">Tải lại</button>
        </div>

        <div class="ct-filter two">
          <input v-model.trim="keyword" class="ct-input" type="search" :placeholder="`Tìm ${resource.title.toLowerCase()}...`" />
          <span class="ct-filter-note">Dữ liệu lấy từ API đang có, không tạo trường ngoài backend.</span>
        </div>

        <div v-if="loading" class="ct-loading">Đang tải dữ liệu...</div>
        <div v-else-if="error" class="ct-error">{{ error }}</div>
        <div v-else class="ct-table-wrap">
          <table class="ct-table compact">
            <thead>
              <tr>
                <th v-for="col in columns" :key="col.key" :style="styleCot(col)">{{ col.label }}</th>
                <th class="ct-right">Thao tác</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="item in filteredRows" :key="layIdBanGhi(item)">
                <td v-for="col in columns" :key="col.key" :class="{ 'ct-strong': col.key === cotChinh }">
                  <span v-if="col.key === cotMa" class="ct-badge">{{ hienThiCot(item, col) }}</span>
                  <span v-else>{{ hienThiCot(item, col) }}</span>
                </td>
                <td class="ct-right">
                  <span class="ct-row-actions">
                    <RouterLink v-if="linkMo(item)" class="ct-btn small ghost" :to="linkMo(item)">Mở</RouterLink>
                    <button class="ct-btn small light" type="button" @click="openEdit(item)">Sửa</button>
                    <button class="ct-btn small danger" type="button" @click="remove(item)">Xóa</button>
                  </span>
                </td>
              </tr>
              <tr v-if="!filteredRows.length"><td :colspan="columns.length + 1" class="ct-empty">Chưa có dữ liệu.</td></tr>
            </tbody>
          </table>
        </div>
      </section>
    </div>

    <BieuMauQuanLyModal
      v-model="form"
      :open="modalOpen"
      :mode="mode"
      :fields="resource.fields || []"
      :saving="saving"
      :title="resource.title"
      @close="modalOpen = false"
      @submit="save"
    />
  </section>
</template>

<script setup>
import { computed, onMounted, ref, watch } from 'vue';
import { useRoute } from 'vue-router';
import './luongChuongTrinh.css';
import BieuMauQuanLyModal from '@/modules/chuongTrinh/components/modal/BieuMauQuanLyModal.vue';
import { CHUONG_TRINH_RESOURCE_MAP } from '@/modules/chuongTrinh/constants/trangThaiChuongTrinh.js';
import { capNhatBanGhi, layIdBanGhi, taoBanGhi, xoaBanGhi } from '@/modules/chuongTrinh/services/luongChuongTrinhDaoTaoService.js';
import { chuongTrinhApi } from '@/modules/chuongTrinh/api/chuongTrinhApi.js';

const route = useRoute();
const rows = ref([]);
const loading = ref(false);
const saving = ref(false);
const error = ref('');
const keyword = ref('');
const modalOpen = ref(false);
const mode = ref('create');
const form = ref({});

const segment = computed(() => route.params.resourceSegment || 'chuong-trinh');
const resource = computed(() => CHUONG_TRINH_RESOURCE_MAP[segment.value] || CHUONG_TRINH_RESOURCE_MAP['chuong-trinh']);
const columns = computed(() => (resource.value.columns || []).filter((col) => col.key !== 'id').slice(0, 6));
const cotMa = computed(() => columns.value.find((col) => /ma|code/i.test(col.key))?.key || columns.value[0]?.key);
const cotChinh = computed(() => columns.value.find((col) => /ten|noiDung|mucTieu/i.test(col.key))?.key || columns.value[1]?.key || columns.value[0]?.key);
const nhomHienThi = computed(() => {
  const key = segment.value;
  if (['nganh', 'trinh-do-dao-tao', 'loai-chuong-trinh', 'khung-ky', 'mon-hoc'].includes(key)) return 'Danh mục';
  if (['chuong-trinh-version', 'chuong-trinh-mon', 'nhom-kien-thuc'].includes(key)) return 'Khung đào tạo';
  if (['syllabus-chuong-trinh', 'syllabus-mon-hoc', 'syllabus-chuong-bai', 'syllabus-tai-lieu'].includes(key)) return 'Nội dung học';
  return 'Chuẩn & ràng buộc';
});
const tieuDeBang = computed(() => `Danh sách ${resource.value.title.toLowerCase()}`);

const filteredRows = computed(() => {
  const search = keyword.value.toLowerCase();
  if (!search) return rows.value;
  const fields = resource.value.searchFields || columns.value.map((col) => col.key);
  return rows.value.filter((item) => fields.some((key) => String(item?.[key] ?? '').toLowerCase().includes(search)));
});

const styleCot = (col) => ({ width: col.width, minWidth: col.minWidth, textAlign: col.align });
const hienThiCot = (item, col) => {
  const value = item?.[col.key];
  if (value === true) return 'Có';
  if (value === false) return 'Không';
  return value ?? '—';
};

const loadData = async () => {
  loading.value = true;
  error.value = '';
  try {
    rows.value = await chuongTrinhApi.getList(resource.value.endpoint);
  } catch (err) {
    error.value = err?.response?.data?.message || err?.message || 'Không tải được dữ liệu.';
    rows.value = [];
  } finally {
    loading.value = false;
  }
};

const linkMo = (item) => {
  const id = layIdBanGhi(item);
  if (!id) return '';
  if (segment.value === 'chuong-trinh-version' && item.chuongTrinhId) return `/quy-trinh/chuong-trinh/${item.chuongTrinhId}/phien-ban/${id}`;
  if (segment.value === 'chuong-trinh') return `/quy-trinh/chuong-trinh/${id}`;
  return '';
};

const openCreate = () => {
  mode.value = 'create';
  form.value = {};
  modalOpen.value = true;
};
const openEdit = (item) => {
  mode.value = 'edit';
  form.value = { ...item };
  modalOpen.value = true;
};
const save = async () => {
  saving.value = true;
  try {
    const id = layIdBanGhi(form.value);
    if (mode.value === 'edit' && id) await capNhatBanGhi(resource.value.endpoint, id, form.value);
    else await taoBanGhi(resource.value.endpoint, form.value);
    modalOpen.value = false;
    await loadData();
  } finally {
    saving.value = false;
  }
};
const remove = async (item) => {
  const id = layIdBanGhi(item);
  if (id && confirm(`Xóa ${resource.value.title.toLowerCase()} này?`)) {
    await xoaBanGhi(resource.value.endpoint, id);
    await loadData();
  }
};

watch(segment, () => {
  keyword.value = '';
  loadData();
});
onMounted(loadData);
</script>
