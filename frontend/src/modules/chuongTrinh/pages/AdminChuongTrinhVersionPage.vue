<template>
  <div>
    <PageHeader title="Phiên bản chương trình" />

    <form class="module-form" @submit.prevent="handleSave">
      <FormSelect
          v-model="form.chuongTrinhId"
          label="Chương trình"
          placeholder="Chọn chương trình"
          :options="chuongTrinhs"
          value-key="id"
          label-key="tenChuongTrinh"
          required
      />

      <FormInput v-model="form.maVersion" label="Mã version" required />
      <FormInput v-model="form.tenVersion" label="Tên version" required />
      <FormInput v-model="form.ngayApDung" type="date" label="Ngày áp dụng" />
      <FormInput v-model="form.ngayHetHieuLuc" type="date" label="Ngày hết hiệu lực" />
      <FormInput v-model="form.soQuyetDinh" label="Số quyết định" />
      <FormInput v-model="form.ngayQuyetDinh" type="date" label="Ngày quyết định" />
      <FormInput v-model="form.nguoiKy" label="Người ký" />
      <FormInput v-model="form.coQuanBanHanh" label="Cơ quan ban hành" />
      <FormInput v-model="form.fileQuyetDinh" label="File quyết định" />
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

const chuongTrinhs = ref([])

const columns = [
  { key: 'maVersion', label: 'Mã version' },
  { key: 'tenVersion', label: 'Tên version' },
  { key: 'tenChuongTrinh', label: 'Chương trình' },
  { key: 'ngayApDung', label: 'Ngày áp dụng' },
  { key: 'tongTinChi', label: 'Tổng tín chỉ' }
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
} = useChuongTrinhCrud(chuongTrinhService.chuongTrinhVersion, {
  chuongTrinhId: null,
  maVersion: '',
  tenVersion: '',
  ngayApDung: '',
  ngayHetHieuLuc: '',
  soQuyetDinh: '',
  ngayQuyetDinh: '',
  nguoiKy: '',
  coQuanBanHanh: '',
  fileQuyetDinh: '',
  tongTinChi: null,
  tongSoGio: null,
  tongGioLyThuyet: null,
  tongGioThucHanh: null,
  tongGioKiemTra: null,
  laHienHanh: false
})

async function fetchSelectData() {
  const res = await chuongTrinhService.chuongTrinh.getAll()
  chuongTrinhs.value = res.items
}

async function loadPageData() {
  await fetchSelectData()
  await fetchItems()

  items.value = items.value.map((item) => {
    const chuongTrinh = chuongTrinhs.value.find((x) => x.id === item.chuongTrinhId)

    return {
      ...item,
      tenChuongTrinh: chuongTrinh?.tenChuongTrinh || item.chuongTrinhId
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