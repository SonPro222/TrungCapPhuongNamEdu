<template>
  <div>
    <PageHeader title="Nhóm kiến thức" />

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

      <FormInput v-model="form.ma" label="Mã nhóm" required />
      <FormInput v-model="form.ten" label="Tên nhóm" required />
      <FormInput v-model="form.thuTu" type="number" label="Thứ tự" />
      <FormInput v-model="form.loaiNhom" label="Loại nhóm" />
      <FormInput v-model="form.tongTinChi" type="number" label="Tổng tín chỉ" />
      <FormInput v-model="form.tongSoGio" type="number" label="Tổng số giờ" />
      <FormInput v-model="form.tongGioLyThuyet" type="number" label="Tổng giờ lý thuyết" />
      <FormInput v-model="form.tongGioThucHanh" type="number" label="Tổng giờ thực hành" />
      <FormInput v-model="form.tongGioKiemTra" type="number" label="Tổng giờ kiểm tra" />

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
  { key: 'ma', label: 'Mã' },
  { key: 'ten', label: 'Tên nhóm' },
  { key: 'tenVersion', label: 'Phiên bản' },
  { key: 'loaiNhom', label: 'Loại nhóm' },
  { key: 'tongTinChi', label: 'Tín chỉ' }
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
} = useChuongTrinhCrud(chuongTrinhService.nhomKienThuc, {
  chuongTrinhVersionId: null,
  ma: '',
  ten: '',
  thuTu: null,
  loaiNhom: '',
  tongTinChi: null,
  tongSoGio: null,
  tongGioLyThuyet: null,
  tongGioThucHanh: null,
  tongGioKiemTra: null
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