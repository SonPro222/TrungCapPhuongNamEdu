<template>
  <div>
    <PageHeader title="Nhóm tự chọn" />

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

      <FormInput v-model="form.ten" label="Tên nhóm" required />
      <FormInput v-model="form.soMonChon" type="number" label="Số môn chọn" />
      <FormInput v-model="form.soTinChiCanDat" type="number" label="Số tín chỉ cần đạt" />
      <FormInput v-model="form.ghiChu" label="Ghi chú" />

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
  { key: 'ten', label: 'Tên nhóm' },
  { key: 'soMonChon', label: 'Số môn chọn' },
  { key: 'soTinChiCanDat', label: 'Tín chỉ cần đạt' },
  { key: 'ghiChu', label: 'Ghi chú' }
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
} = useChuongTrinhCrud(chuongTrinhService.nhomTuChon, {
  chuongTrinhVersionId: null,
  ten: '',
  soMonChon: null,
  soTinChiCanDat: null,
  ghiChu: ''
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