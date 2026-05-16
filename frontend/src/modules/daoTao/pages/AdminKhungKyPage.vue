<template>
  <div>
    <PageHeader title="Khung kỳ" />

    <form class="module-form" @submit.prevent="handleSave">
      <FormSelect
          v-model="form.chuongTrinhVersionId"
          label="Phiên bản chương trình"
          placeholder="Chọn phiên bản chương trình"
          :options="chuongTrinhVersions"
          value-key="id"
          label-key="tenVersion"
          required
      />

      <FormSelect
          v-model="form.loaiChuongTrinhId"
          label="Loại chương trình"
          placeholder="Chọn loại chương trình"
          :options="loaiChuongTrinhs"
          value-key="id"
          label-key="tenLoai"
      />

      <FormInput v-model="form.maKy" label="Mã kỳ" placeholder="Nhập mã kỳ" required />
      <FormInput v-model="form.tenKy" label="Tên kỳ" placeholder="Nhập tên kỳ" required />
      <FormInput v-model="form.thuTu" type="number" label="Thứ tự" placeholder="Nhập thứ tự" required />

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

import { daoTaoService } from '../services/daoTaoService'
import { chuongTrinhService } from '@/modules/chuongTrinh/services/chuongTrinhService'
import { useDaoTaoCrud } from '../composables/useDaoTaoCrud'

const loaiChuongTrinhs = ref([])
const chuongTrinhVersions = ref([])

const columns = [
  { key: 'tenVersion', label: 'Phiên bản chương trình' },
  { key: 'tenLoaiChuongTrinh', label: 'Loại chương trình' },
  { key: 'maKy', label: 'Mã kỳ' },
  { key: 'tenKy', label: 'Tên kỳ' },
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
} = useDaoTaoCrud(daoTaoService.khungKy, {
  chuongTrinhVersionId: null,
  loaiChuongTrinhId: null,
  maKy: '',
  tenKy: '',
  thuTu: null
})

async function fetchSelectData() {
  const loaiRes = await daoTaoService.loaiChuongTrinh.getAll()
  loaiChuongTrinhs.value = loaiRes.items

  const versionRes = await chuongTrinhService.chuongTrinhVersion.getAll()
  chuongTrinhVersions.value = versionRes.items
}

async function loadPageData() {
  await fetchSelectData()
  await fetchItems()

  items.value = items.value.map((item) => {
    const version = chuongTrinhVersions.value.find(
        (x) => x.id === item.chuongTrinhVersionId
    )

    const loai = loaiChuongTrinhs.value.find(
        (x) => x.id === item.loaiChuongTrinhId
    )

    return {
      ...item,
      tenVersion: version?.tenVersion || item.chuongTrinhVersionId,
      tenLoaiChuongTrinh: loai?.tenLoai || item.loaiChuongTrinhId
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