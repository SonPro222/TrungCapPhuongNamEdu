import { computed, ref } from 'vue';
import { CHUONG_TRINH_TABLE_CONFIG } from '../config/chuongTrinhTable.config';
import { CHUONG_TRINH_API } from '../services/chuongTrinh.service';
import { buildPrefillPayload, normalizePayload } from '../utils/moduleTable.utils';

const getCurrentRoles = () => {
  try {
    const rawUser = localStorage.getItem('user') || localStorage.getItem('authUser');
    const user = rawUser ? JSON.parse(rawUser) : {};
    const roles = user.roles || user.vaiTro || user.role || [];
    return Array.isArray(roles) ? roles : [roles];
  } catch {
    return [];
  }
};

export const useModuleCrud = (resourceKeyRef, contextRef, onDone) => {
  const modalOpen = ref(false);
  const editingRow = ref(null);
  const submitting = ref(false);
  const formError = ref('');

  const config = computed(() => CHUONG_TRINH_TABLE_CONFIG[resourceKeyRef.value]);
  const api = computed(() => CHUONG_TRINH_API[config.value?.apiKey]);

  const can = (action) => {
    const allowed = config.value?.permissions?.[action] || ['ADMIN', 'DAO_TAO'];
    const roles = getCurrentRoles();
    if (!roles.length) return true;
    return roles.some((role) => allowed.includes(String(role).replace('ROLE_', '')) || allowed.includes(String(role)));
  };

  const openCreate = () => {
    editingRow.value = null;
    formError.value = '';
    modalOpen.value = true;
  };

  const openEdit = (row) => {
    editingRow.value = { ...row };
    formError.value = '';
    modalOpen.value = true;
  };

  const closeModal = () => {
    modalOpen.value = false;
    editingRow.value = null;
  };

  const submit = async (values) => {
    submitting.value = true;
    formError.value = '';
    try {
      const prefill = buildPrefillPayload(resourceKeyRef.value, contextRef.value);
      const payload = normalizePayload(config.value.formFields || [], { ...prefill, ...values });
      if (editingRow.value?.id) await api.value.update(editingRow.value.id, payload);
      else await api.value.create(payload);
      closeModal();
      if (typeof onDone === 'function') await onDone();
    } catch (err) {
      formError.value = err?.response?.data?.message || err?.message || 'Có lỗi xảy ra khi lưu dữ liệu.';
    } finally {
      submitting.value = false;
    }
  };

  const remove = async (row) => {
    if (!row?.id || !window.confirm('Xóa bản ghi này?')) return;
    submitting.value = true;
    try {
      await api.value.remove(row.id);
      if (typeof onDone === 'function') await onDone();
    } finally {
      submitting.value = false;
    }
  };

  return { modalOpen, editingRow, submitting, formError, can, openCreate, openEdit, closeModal, submit, remove };
};
