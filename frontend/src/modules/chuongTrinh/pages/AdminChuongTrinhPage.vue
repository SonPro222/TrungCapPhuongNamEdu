<template>
  <div>
    <PageHeader title="Chương trình" />

    <form class="module-form" @submit.prevent="handleSave">
      <FormSelect
          v-model="form.nganhId"
          label="Ngành"
          placeholder="Chọn ngành"
          :options="nganhs"
          value-key="id"
          label-key="tenNganh"
          required
      />

      <FormSelect
          v-model="form.trinhDoId"
          label="Trình độ"
          placeholder="Chọn trình độ"
          :options="trinhDos"
          value-key="id"
          label-key="tenTrinhDo"
          required
      />

      <FormSelect
          v-model="form.loaiChuongTrinhId"
          label="Loại chương trình"
          placeholder="Chọn loại chương trình"
          :options="loaiChuongTrinhs"
          value-key="id"
          label-key="tenLoai"
          required
      />

      <FormInput v-model="form.maChuongTrinh" label="Mã chương trình" required />
      <FormInput v-model="form.tenChuongTrinh" label="Tên chương trình" required />
      <FormInput v-model="form.doiTuongTuyenSinh" label="Đối tượng tuyển sinh" />
      <FormInput v-model="form.thoiGianDaoTao" label="Thời gian đào tạo" />

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

import { daoTaoService } from '@/modules/daoTao/services/daoTaoService'
import { chuongTrinhService } from '../services/chuongTrinhService'
import { useChuongTrinhCrud } from '../composables/useChuongTrinhCrud'

const nganhs = ref([])
const trinhDos = ref([])
const loaiChuongTrinhs = ref([])

const columns = [
  { key: 'maChuongTrinh', label: 'Mã CT' },
  { key: 'tenChuongTrinh', label: 'Tên chương trình' },
  { key: 'tenNganh', label: 'Ngành' },
  { key: 'tenTrinhDo', label: 'Trình độ' },
  { key: 'tenLoaiChuongTrinh', label: 'Loại chương trình' }
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
} = useChuongTrinhCrud(chuongTrinhService.chuongTrinh, {
  nganhId: null,
  trinhDoId: null,
  loaiChuongTrinhId: null,
  maChuongTrinh: '',
  tenChuongTrinh: '',
  doiTuongTuyenSinh: '',
  thoiGianDaoTao: ''
})

async function fetchSelectData() {
  const nganhRes = await daoTaoService.nganh.getAll()
  nganhs.value = nganhRes.items

  const trinhDoRes = await daoTaoService.trinhDoDaoTao.getAll()
  trinhDos.value = trinhDoRes.items

  const loaiRes = await daoTaoService.loaiChuongTrinh.getAll()
  loaiChuongTrinhs.value = loaiRes.items
}

async function loadPageData() {
  await fetchSelectData()
  await fetchItems()

  items.value = items.value.map((item) => {
    const nganh = nganhs.value.find((x) => x.id === item.nganhId)
    const trinhDo = trinhDos.value.find((x) => x.id === item.trinhDoId)
    const loai = loaiChuongTrinhs.value.find((x) => x.id === item.loaiChuongTrinhId)

    return {
      ...item,
      tenNganh: nganh?.tenNganh || item.nganhId,
      tenTrinhDo: trinhDo?.tenTrinhDo || item.trinhDoId,
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