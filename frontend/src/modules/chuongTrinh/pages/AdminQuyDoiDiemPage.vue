<template>
  <div>
    <PageHeader title="Quy đổi điểm" />

    <form class="module-form" @submit.prevent="handleSave">
      <FormSelect
          v-model="form.chuongTrinhMonId"
          label="Môn trong chương trình"
          placeholder="Chọn môn trong chương trình"
          :options="chuongTrinhMons"
          value-key="id"
          label-key="tenHienThi"
          required
      />

      <FormInput v-model="form.nguongTu" type="number" label="Ngưỡng từ" />
      <FormInput v-model="form.nguongDen" type="number" label="Ngưỡng đến" />
      <FormInput v-model="form.diemQuyDoi" type="number" label="Điểm quy đổi" />
      <FormInput v-model="form.ketQua" label="Kết quả" />
      <FormInput v-model="form.congThuc" label="Công thức" />
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

const chuongTrinhMons = ref([])

const columns = [
  { key: 'tenChuongTrinhMon', label: 'Môn trong CT' },
  { key: 'nguongTu', label: 'Ngưỡng từ' },
  { key: 'nguongDen', label: 'Ngưỡng đến' },
  { key: 'diemQuyDoi', label: 'Điểm quy đổi' },
  { key: 'ketQua', label: 'Kết quả' }
]

const {
  items, form, loading, saving, errorMessage, editingId,
  fetchItems, editItem, saveItem, deleteItem, resetForm
} = useChuongTrinhCrud(chuongTrinhService.quyDoiDiem, {
  chuongTrinhMonId: null,
  nguongTu: null,
  nguongDen: null,
  diemQuyDoi: null,
  ketQua: '',
  congThuc: '',
  ghiChu: ''
})

async function fetchSelectData() {
  const monHocRes = await chuongTrinhService.monHoc.getAll()
  const monHocs = monHocRes.items

  const ctmRes = await chuongTrinhService.chuongTrinhMon.getAll()
  chuongTrinhMons.value = ctmRes.items.map((item) => {
    const monHoc = monHocs.find((x) => x.id === item.monHocId)

    return {
      ...item,
      tenHienThi: item.maMonTrongCt || monHoc?.tenMon || item.id
    }
  })
}

async function loadPageData() {
  await fetchSelectData()
  await fetchItems()

  items.value = items.value.map((item) => {
    const ctm = chuongTrinhMons.value.find((x) => x.id === item.chuongTrinhMonId)

    return {
      ...item,
      tenChuongTrinhMon: ctm?.tenHienThi || item.chuongTrinhMonId
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