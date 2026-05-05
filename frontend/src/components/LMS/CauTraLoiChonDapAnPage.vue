<template>
  <section class="page-wrap">
    <header class="page-header">
      <div>
        <h1>{{ pageTitle }}</h1>
        <p>Quản lý dữ liệu {{ pageTitle.toLowerCase() }}</p>
      </div>
      <button class="btn btn-secondary" type="button" @click="loadData">Tải lại</button>
    </header>

    <form class="card form-grid" @submit.prevent="submitForm">
      <div v-for="field in fields" :key="field.key" class="form-item" :class="{ full: field.type === 'textarea' }">
        <label :for="field.key">{{ field.label }}</label>

        <textarea
          v-if="field.type === 'textarea'"
          :id="field.key"
          v-model="form[field.key]"
          rows="3"
        />

        <input
          v-else-if="field.type === 'checkbox'"
          :id="field.key"
          v-model="form[field.key]"
          type="checkbox"
        />

        <input
          v-else
          :id="field.key"
          v-model="form[field.key]"
          :type="field.type"
        />
      </div>

      <div class="form-actions full">
        <button class="btn btn-primary" type="submit" :disabled="loading">
          {{ editingId ? 'Cập nhật' : 'Thêm mới' }}
        </button>
        <button class="btn btn-secondary" type="button" @click="resetForm">Làm mới</button>
      </div>
    </form>

    <div v-if="error" class="alert">{{ error }}</div>

    <div class="card table-card">
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th v-for="field in fields" :key="field.key">{{ field.label }}</th>
            <th>Thao tác</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="items.length === 0">
            <td :colspan="fields.length + 2">Chưa có dữ liệu</td>
          </tr>
          <tr v-for="item in items" :key="item.id">
            <td>{{ item.id }}</td>
            <td v-for="field in fields" :key="field.key">
              {{ formatValue(item[field.key]) }}
            </td>
            <td class="actions">
              <button class="btn btn-small" type="button" @click="editItem(item)">Sửa</button>
              <button class="btn btn-danger btn-small" type="button" @click="removeItem(item.id)">Xóa</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </section>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue';
import { getAllCauTraLoiChonDapAn } from '@/api/lms/response/fetchResponse_CauTraLoiChonDapAn.js';
import { createCauTraLoiChonDapAn, updateCauTraLoiChonDapAn, deleteCauTraLoiChonDapAn } from '@/api/lms/request/fetchRequest_CauTraLoiChonDapAn.js';

const pageTitle = 'Câu trả lời chọn đáp án';
const fields = [
  {
    "key": "cauTraLoiId",
    "label": "Cau tra loi id",
    "type": "number"
  },
  {
    "key": "dapAnId",
    "label": "Dap an id",
    "type": "number"
  }
];

const items = ref([]);
const loading = ref(false);
const error = ref('');
const editingId = ref(null);

const buildEmptyForm = () => fields.reduce((acc, field) => {
  acc[field.key] = field.type === 'checkbox' ? false : '';
  return acc;
}, {});

const form = reactive(buildEmptyForm());

const unwrapList = (response) => {
  const data = response?.data ?? response;
  if (Array.isArray(data)) return data;
  if (Array.isArray(data?.data)) return data.data;
  if (Array.isArray(data?.content)) return data.content;
  if (Array.isArray(data?.items)) return data.items;
  if (Array.isArray(data?.result)) return data.result;
  return [];
};

const normalizePayload = () => {
  const payload = {};
  fields.forEach((field) => {
    const value = form[field.key];
    if (field.type === 'checkbox') {
      payload[field.key] = Boolean(value);
      return;
    }
    payload[field.key] = value === '' ? null : value;
  });
  return payload;
};

const loadData = async () => {
  loading.value = true;
  error.value = '';
  try {
    const response = await getAllCauTraLoiChonDapAn();
    items.value = unwrapList(response);
  } catch (err) {
    error.value = err?.response?.data?.message || err?.message || 'Không tải được dữ liệu';
  } finally {
    loading.value = false;
  }
};

const submitForm = async () => {
  loading.value = true;
  error.value = '';
  try {
    const payload = normalizePayload();
    if (editingId.value) {
      await updateCauTraLoiChonDapAn(editingId.value, payload);
    } else {
      await createCauTraLoiChonDapAn(payload);
    }
    resetForm();
    await loadData();
  } catch (err) {
    error.value = err?.response?.data?.message || err?.message || 'Không lưu được dữ liệu';
  } finally {
    loading.value = false;
  }
};

const editItem = (item) => {
  editingId.value = item.id;
  fields.forEach((field) => {
    form[field.key] = item[field.key] ?? (field.type === 'checkbox' ? false : '');
  });
};

const removeItem = async (id) => {
  if (!id || !window.confirm('Xóa dữ liệu này?')) return;
  loading.value = true;
  error.value = '';
  try {
    await deleteCauTraLoiChonDapAn(id);
    await loadData();
  } catch (err) {
    error.value = err?.response?.data?.message || err?.message || 'Không xóa được dữ liệu';
  } finally {
    loading.value = false;
  }
};

const resetForm = () => {
  editingId.value = null;
  Object.assign(form, buildEmptyForm());
};

const formatValue = (value) => {
  if (value === true) return 'Có';
  if (value === false) return 'Không';
  return value ?? '';
};

onMounted(loadData);
</script>

<style scoped>
.page-wrap { padding: 24px; }
.page-header { display: flex; justify-content: space-between; gap: 16px; align-items: center; margin-bottom: 16px; }
.page-header h1 { margin: 0; font-size: 24px; }
.page-header p { margin: 6px 0 0; color: #667085; }
.card { background: #fff; border: 1px solid #e5e7eb; border-radius: 12px; padding: 16px; box-shadow: 0 1px 3px rgba(16, 24, 40, 0.08); }
.form-grid { display: grid; grid-template-columns: repeat(3, minmax(0, 1fr)); gap: 14px; margin-bottom: 16px; }
.form-item { display: flex; flex-direction: column; gap: 6px; }
.form-item.full, .form-actions.full { grid-column: 1 / -1; }
label { font-weight: 600; color: #344054; }
input, textarea { border: 1px solid #d0d5dd; border-radius: 8px; padding: 10px 12px; font: inherit; }
input[type="checkbox"] { width: 20px; height: 20px; }
.form-actions { display: flex; gap: 10px; }
.btn { border: 0; border-radius: 8px; padding: 10px 14px; cursor: pointer; font-weight: 600; }
.btn-primary { background: #2563eb; color: #fff; }
.btn-secondary { background: #f2f4f7; color: #344054; }
.btn-danger { background: #dc2626; color: #fff; }
.btn-small { padding: 6px 10px; }
.alert { margin: 12px 0; padding: 12px; border-radius: 8px; background: #fef2f2; color: #b42318; }
.table-card { overflow-x: auto; }
table { width: 100%; border-collapse: collapse; min-width: 900px; }
th, td { border-bottom: 1px solid #e5e7eb; padding: 10px; text-align: left; vertical-align: top; }
th { background: #f9fafb; white-space: nowrap; }
.actions { display: flex; gap: 8px; }
@media (max-width: 900px) { .form-grid { grid-template-columns: 1fr; } .page-header { align-items: flex-start; flex-direction: column; } }
</style>
