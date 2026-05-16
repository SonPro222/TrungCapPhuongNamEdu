<template>
  <div>
    <PageHeader title="Lớp hành chính" />

    <form class="module-form" @submit.prevent="handleSave">
      <FormInput v-model="form.maLop"
                 label="Mã lớp"
                 placeholder="Nhập mã lớp"
                 required />
      <FormInput v-model="form.tenLop" label="Tên lớp" placeholder="Nhập tên lớp" required />

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
          v-model="form.khoaDaoTaoId"
          label="Khóa đào tạo"
          placeholder="Chọn khóa đào tạo"
          :options="khoaDaoTaos"
          value-key="id"
          label-key="tenKhoa"
      />

      <FormInput v-model="form.siSo" type="number" label="Sĩ số" placeholder="Nhập sĩ số" />
      <FormSelect
          v-model="form.trangThai"
          label="Trạng thái"
          placeholder="Chọn trạng thái"
          :options="trangThaiOptions"
          value-key="value"
          label-key="label"
          value-type="string"
      />
      <FormInput v-model="form.ghiChu" label="Ghi chú" placeholder="Nhập ghi chú" />

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

const khoaDaoTaos = ref([])
const chuongTrinhVersions = ref([])
const trangThaiOptions = [
  { value: 'du_kien', label: 'Dự kiến' },
  { value: 'dang_hoc', label: 'Đang học' },
  { value: 'tam_dung', label: 'Tạm dừng' },
  { value: 'da_tot_nghiep', label: 'Đã tốt nghiệp' },
  { value: 'huy', label: 'Hủy' }
]
const columns = [
  { key: 'maLop', label: 'Mã lớp' },
  { key: 'tenLop', label: 'Tên lớp' },
  { key: 'tenVersion', label: 'Phiên bản chương trình' },
  { key: 'tenKhoa', label: 'Khóa đào tạo' },
  { key: 'siSo', label: 'Sĩ số' },
  { key: 'trangThai', label: 'Trạng thái' }
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
} = useDaoTaoCrud(daoTaoService.lopHanhChinh, {
  maLop: '',
  tenLop: '',
  chuongTrinhVersionId: null,
  khoaDaoTaoId: null,
  siSo: 0,
  trangThai: 'dang_hoc',
  ghiChu: ''
})

async function fetchSelectData() {
  const khoaRes = await daoTaoService.khoaDaoTao.getAll()
  khoaDaoTaos.value = khoaRes.items

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

    const khoa = khoaDaoTaos.value.find(
        (x) => x.id === item.khoaDaoTaoId
    )

    return {
      ...item,
      tenVersion: version?.tenVersion || item.chuongTrinhVersionId,
      tenKhoa: khoa?.tenKhoa || item.khoaDaoTaoId
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