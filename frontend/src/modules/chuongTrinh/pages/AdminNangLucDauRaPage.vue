<template>
  <div>
    <PageHeader title="Năng lực đầu ra" />

    <form class="module-form" @submit.prevent="handleSave">
      <FormSelect
          v-model="form.chuongTrinhVersionId"
          label="Phiên bản chương trình"
          placeholder="Chọn phiên bản"
          :options="versions"
          value-key="id"
          label-key="tenVersion"
          required
      />

      <FormInput v-model="form.ma" label="Mã" required />
      <FormInput v-model="form.noiDung" label="Nội dung" required />
      <FormInput v-model="form.loai" label="Loại" />
      <FormInput v-model="form.thuTu" type="number" label="Thứ tự" />

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
import { onMounted, ref } from 'vue'

import PageHeader from '@/shared/components/PageHeader.vue'
import DataTable from '@/shared/components/DataTable.vue'
import FormInput from '@/shared/components/FormInput.vue'
import FormSelect from '@/shared/components/FormSelect.vue'
import BaseButton from '@/shared/components/BaseButton.vue'

import { chuongTrinhService } from '../services/chuongTrinhService'
import { useChuongTrinhCrud } from '../composables/useChuongTrinhCrud'

const versions = ref([])

const columns = [
  { key: 'tenVersion', label: 'Phiên bản' },
  { key: 'ma', label: 'Mã' },
  { key: 'noiDung', label: 'Nội dung' },
  { key: 'loai', label: 'Loại' },
  { key: 'thuTu', label: 'Thứ tự' }
]

const {
  items,
  form,
  loading,
  saving,
  errorMessage,
  editingId,
  fetchItems,
  editItem,
  saveItem,
  deleteItem,
  resetForm
} = useChuongTrinhCrud(chuongTrinhService.nangLucDauRa, {
  chuongTrinhVersionId: null,
  ma: '',
  noiDung: '',
  loai: '',
  thuTu: null
})

async function fetchSelectData() {
  const res = await chuongTrinhService.chuongTrinhVersion.getAll()
  versions.value = res.items
}

async function loadPageData() {
  await fetchSelectData()
  await fetchItems()

  items.value = items.value.map((item) => {
    const version = versions.value.find((x) => x.id === item.chuongTrinhVersionId)

    return {
      ...item,
      tenVersion: version?.tenVersion || item.chuongTrinhVersionId
    }
  })
}

async function handleSave() {
  await saveItem()
  await loadPageData()
}

async function handleDelete(id) {
  await deleteItem(id)
  await loadPageData()
}

onMounted(loadPageData)
</script>