<template>
  <div>
    <PageHeader title="Môn trong chương trình" />

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

      <FormSelect
          v-model="form.monHocId"
          label="Môn học"
          placeholder="Chọn môn học"
          :options="monHocs"
          value-key="id"
          label-key="tenMon"
          required
      />

      <FormInput v-model="form.maMonTrongCt" label="Mã môn trong CT" />

      <FormSelect
          v-model="form.khungKyId"
          label="Khung kỳ"
          placeholder="Chọn khung kỳ"
          :options="khungKys"
          value-key="id"
          label-key="tenKy"
          required
      />

      <FormSelect
          v-model="form.nhomKienThucId"
          label="Nhóm kiến thức"
          placeholder="Chọn nhóm kiến thức"
          :options="nhomKienThucs"
          value-key="id"
          label-key="ten"
      />

      <FormInput v-model="form.loai" label="Loại" />
      <FormInput v-model="form.loaiHocPhan" label="Loại học phần" />
      <FormInput v-model="form.thuTu" type="number" label="Thứ tự" />
      <FormInput v-model="form.soTinChi" type="number" label="Số tín chỉ" />
      <FormInput v-model="form.tongGio" type="number" label="Tổng giờ" />
      <FormInput v-model="form.gioLyThuyet" type="number" label="Giờ lý thuyết" />
      <FormInput v-model="form.gioThucHanh" type="number" label="Giờ thực hành" />
      <FormInput v-model="form.gioKiemTra" type="number" label="Giờ kiểm tra" />
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

import { daoTaoService } from '@/modules/daoTao/services/daoTaoService'
import { chuongTrinhService } from '../services/chuongTrinhService'
import { useChuongTrinhCrud } from '../composables/useChuongTrinhCrud'

const versions = ref([])
const monHocs = ref([])
const khungKys = ref([])
const nhomKienThucs = ref([])

const columns = [
  { key: 'maMonTrongCt', label: 'Mã môn CT' },
  { key: 'tenVersion', label: 'Phiên bản' },
  { key: 'tenMon', label: 'Môn học' },
  { key: 'tenKy', label: 'Kỳ' },
  { key: 'tenNhomKienThuc', label: 'Nhóm kiến thức' },
  { key: 'soTinChi', label: 'Tín chỉ' },
  { key: 'tongGio', label: 'Tổng giờ' }
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
} = useChuongTrinhCrud(chuongTrinhService.chuongTrinhMon, {
  chuongTrinhVersionId: null,
  monHocId: null,
  maMonTrongCt: '',
  khungKyId: null,
  nhomKienThucId: null,
  loai: '',
  loaiHocPhan: '',
  batBuoc: true,
  laMonDieuKien: false,
  thuTu: null,
  soTinChi: null,
  tongGio: null,
  gioLyThuyet: null,
  gioThucHanh: null,
  gioKiemTra: null,
  ghiChu: ''
})

function getTenMon(mon) {
  return mon?.tenMon || mon?.tenMonHoc || mon?.ten || mon?.maMon || mon?.maMonHoc
}

async function fetchSelectData() {
  const versionRes = await chuongTrinhService.chuongTrinhVersion.getAll()
  versions.value = versionRes.items

  const monHocRes = await chuongTrinhService.monHoc.getAll()
  monHocs.value = monHocRes.items.map((item) => ({
    ...item,
    tenMon: getTenMon(item)
  }))

  const khungKyRes = await daoTaoService.khungKy.getAll()
  khungKys.value = khungKyRes.items

  const nhomRes = await chuongTrinhService.nhomKienThuc.getAll()
  nhomKienThucs.value = nhomRes.items
}

async function loadPageData() {
  await fetchSelectData()
  await fetchItems()

  items.value = items.value.map((item) => {
    const version = versions.value.find((x) => x.id === item.chuongTrinhVersionId)
    const mon = monHocs.value.find((x) => x.id === item.monHocId)
    const ky = khungKys.value.find((x) => x.id === item.khungKyId)
    const nhom = nhomKienThucs.value.find((x) => x.id === item.nhomKienThucId)

    return {
      ...item,
      tenVersion: version?.tenVersion || item.chuongTrinhVersionId,
      tenMon: mon?.tenMon || item.monHocId,
      tenKy: ky?.tenKy || item.khungKyId,
      tenNhomKienThuc: nhom?.ten || item.nhomKienThucId
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