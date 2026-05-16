<template>
  <div>
    <PageHeader title="Môn tiên quyết" />

    <form class="module-form" @submit.prevent="handleSave">
      <FormSelect
          v-model="form.monId"
          label="Môn"
          placeholder="Chọn môn"
          :options="chuongTrinhMons"
          value-key="id"
          label-key="tenHienThi"
          required
      />

      <FormSelect
          v-model="form.monDieuKienId"
          label="Môn điều kiện"
          placeholder="Chọn môn điều kiện"
          :options="chuongTrinhMons"
          value-key="id"
          label-key="tenHienThi"
          required
      />

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

const chuongTrinhMons = ref([])

const columns = [
  { key: 'tenMon', label: 'Môn' },
  { key: 'tenMonDieuKien', label: 'Môn điều kiện' },
  { key: 'loai', label: 'Loại' },
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
} = useChuongTrinhCrud(chuongTrinhService.monTienQuyet, {
  monId: null,
  monDieuKienId: null,
  loai: '',
  ghiChu: ''
})

function getTenMonHoc(mon) {
  return mon?.tenMon || mon?.tenMonHoc || mon?.ten || mon?.maMon || mon?.maMonHoc
}

async function fetchSelectData() {
  const monHocRes = await chuongTrinhService.monHoc.getAll()
  const monHocs = monHocRes.items

  const ctmRes = await chuongTrinhService.chuongTrinhMon.getAll()
  chuongTrinhMons.value = ctmRes.items.map((item) => {
    const monHoc = monHocs.find((x) => x.id === item.monHocId)

    return {
      ...item,
      tenHienThi: item.maMonTrongCt || getTenMonHoc(monHoc) || item.id
    }
  })
}

async function loadPageData() {
  await fetchSelectData()
  await fetchItems()

  items.value = items.value.map((item) => {
    const mon = chuongTrinhMons.value.find((x) => x.id === item.monId)
    const monDieuKien = chuongTrinhMons.value.find((x) => x.id === item.monDieuKienId)

    return {
      ...item,
      tenMon: mon?.tenHienThi || item.monId,
      tenMonDieuKien: monDieuKien?.tenHienThi || item.monDieuKienId
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