<template>
  <div>
    <PageHeader title="Syllabus tài liệu" />

    <form class="module-form" @submit.prevent="handleSave">
      <FormSelect
          v-model="form.syllabusMonId"
          label="Syllabus môn học"
          placeholder="Chọn syllabus môn học"
          :options="syllabusMonHocs"
          value-key="id"
          label-key="tenHienThi"
          required
      />

      <FormInput v-model="form.ten" label="Tên tài liệu" required />
      <FormInput v-model="form.tacGia" label="Tác giả" />
      <FormInput v-model="form.namXuatBan" type="number" label="Năm xuất bản" />
      <FormInput v-model="form.nhaXuatBan" label="Nhà xuất bản" />
      <FormInput v-model="form.loai" label="Loại" />
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

const syllabusMonHocs = ref([])

const columns = [
  { key: 'tenSyllabusMon', label: 'Syllabus môn học' },
  { key: 'ten', label: 'Tên tài liệu' },
  { key: 'tacGia', label: 'Tác giả' },
  { key: 'namXuatBan', label: 'Năm XB' },
  { key: 'loai', label: 'Loại' }
]

const {
  items, form, loading, saving, errorMessage, editingId,
  fetchItems, editItem, saveItem, deleteItem, resetForm
} = useChuongTrinhCrud(chuongTrinhService.syllabusTaiLieu, {
  syllabusMonId: null,
  ten: '',
  tacGia: '',
  namXuatBan: null,
  nhaXuatBan: '',
  loai: '',
  ghiChu: ''
})

async function fetchSelectData() {
  const res = await chuongTrinhService.syllabusMonHoc.getAll()
  syllabusMonHocs.value = res.items.map((item) => ({
    ...item,
    tenHienThi: `Syllabus môn #${item.id}`
  }))
}

async function loadPageData() {
  await fetchSelectData()
  await fetchItems()

  items.value = items.value.map((item) => {
    const syllabus = syllabusMonHocs.value.find((x) => x.id === item.syllabusMonId)

    return {
      ...item,
      tenSyllabusMon: syllabus?.tenHienThi || item.syllabusMonId
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