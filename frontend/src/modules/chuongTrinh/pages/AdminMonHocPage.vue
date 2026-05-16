<template>
  <div>
    <PageHeader title="Môn học" />

    <form class="module-form" @submit.prevent="handleSave">
      <FormInput v-model="form.maMon" label="Mã môn" />
      <FormInput v-model="form.tenMon" label="Tên môn" required />
      <FormInput v-model="form.moTa" label="Mô tả" />

      <BaseButton type="submit" variant="primary" :loading="saving" :label="editingId ? 'Cập nhật' : 'Thêm mới'" />
      <BaseButton label="Làm mới" @click="resetForm" />
    </form>

    <p v-if="errorMessage" class="error-text">{{ errorMessage }}</p>

    <DataTable :columns="columns" :items="items" :loading="loading">
      <template #actions="{ item }">
        <BaseButton label="Sửa" @click="editItem(item)" />
        <BaseButton label="Xóa" variant="danger" @click="handleDelete(item.id)" />
      </template>
    </DataTable>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import PageHeader from '@/shared/components/PageHeader.vue'
import DataTable from '@/shared/components/DataTable.vue'
import FormInput from '@/shared/components/FormInput.vue'
import BaseButton from '@/shared/components/BaseButton.vue'
import { chuongTrinhService } from '../services/chuongTrinhService'
import { useChuongTrinhCrud } from '../composables/useChuongTrinhCrud'

const columns = [
  { key: 'maMon', label: 'Mã môn' },
  { key: 'tenMon', label: 'Tên môn' },
  { key: 'moTa', label: 'Mô tả' }
]

const {
  items, form, loading, saving, errorMessage, editingId,
  fetchItems, editItem, saveItem, deleteItem, resetForm
} = useChuongTrinhCrud(chuongTrinhService.monHoc, {
  maMon: '',
  tenMon: '',
  moTa: ''
})

async function handleSave() {
  await saveItem()
  await fetchItems()
}

async function handleDelete(id) {
  await deleteItem(id)
  await fetchItems()
}

onMounted(fetchItems)
</script>