<template>
  <div>
    <PageHeader title="Syllabus chương bài" />

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

      <FormInput v-model="form.ten" label="Tên chương/bài" required />
      <FormInput v-model="form.tongGio" type="number" label="Tổng giờ" />
      <FormInput v-model="form.gioLyThuyet" type="number" label="Giờ lý thuyết" />
      <FormInput v-model="form.gioThucHanh" type="number" label="Giờ thực hành" />
      <FormInput v-model="form.gioKiemTra" type="number" label="Giờ kiểm tra" />
      <FormInput v-model="form.noiDung" label="Nội dung" />
      <FormInput v-model="form.mucTieu" label="Mục tiêu" />
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

const syllabusMonHocs = ref([])

const columns = [
  { key: 'tenSyllabusMon', label: 'Syllabus môn học' },
  { key: 'ten', label: 'Tên chương/bài' },
  { key: 'tongGio', label: 'Tổng giờ' },
  { key: 'thuTu', label: 'Thứ tự' }
]

const {
  items, form, loading, saving, errorMessage, editingId,
  fetchItems, editItem, saveItem, deleteItem, resetForm
} = useChuongTrinhCrud(chuongTrinhService.syllabusChuongBai, {
  syllabusMonId: null,
  ten: '',
  tongGio: null,
  gioLyThuyet: null,
  gioThucHanh: null,
  gioKiemTra: null,
  noiDung: '',
  mucTieu: '',
  thuTu: null
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