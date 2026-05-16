<template>
  <div>
    <PageHeader title="Môn tự chọn" />

    <form class="module-form" @submit.prevent="handleSave">
      <FormSelect
          v-model="form.nhomId"
          label="Nhóm tự chọn"
          placeholder="Chọn nhóm tự chọn"
          :options="nhomTuChons"
          value-key="id"
          label-key="ten"
          required
      />

      <FormSelect
          v-model="form.chuongTrinhMonId"
          label="Môn trong chương trình"
          placeholder="Chọn môn trong chương trình"
          :options="chuongTrinhMons"
          value-key="id"
          label-key="tenHienThi"
          required
      />

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
import FormSelect from '@/shared/components/FormSelect.vue'
import BaseButton from '@/shared/components/BaseButton.vue'

import { chuongTrinhService } from '../services/chuongTrinhService'
import { useChuongTrinhCrud } from '../composables/useChuongTrinhCrud'

const nhomTuChons = ref([])
const chuongTrinhMons = ref([])

const columns = [
  { key: 'tenNhomTuChon', label: 'Nhóm tự chọn' },
  { key: 'tenChuongTrinhMon', label: 'Môn trong CT' }
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
} = useChuongTrinhCrud(chuongTrinhService.monTuChon, {
  nhomId: null,
  chuongTrinhMonId: null
})

function getTenMonHoc(mon) {
  return mon?.tenMon || mon?.tenMonHoc || mon?.ten || mon?.maMon || mon?.maMonHoc
}

async function fetchSelectData() {
  const nhomRes = await chuongTrinhService.nhomTuChon.getAll()
  nhomTuChons.value = nhomRes.items

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
    const nhom = nhomTuChons.value.find((x) => x.id === item.nhomId)
    const ctm = chuongTrinhMons.value.find((x) => x.id === item.chuongTrinhMonId)

    return {
      ...item,
      tenNhomTuChon: nhom?.ten || item.nhomId,
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