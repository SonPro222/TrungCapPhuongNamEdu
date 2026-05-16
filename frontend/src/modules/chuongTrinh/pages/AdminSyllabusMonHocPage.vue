<template>
  <div>
    <PageHeader title="Syllabus môn học" />

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

      <FormInput v-model="form.viTri" label="Vị trí" />
      <FormInput v-model="form.tinhChat" label="Tính chất" />
      <FormInput v-model="form.mucTieu" label="Mục tiêu" />
      <FormInput v-model="form.phuongPhapDanhGia" label="Phương pháp đánh giá" />
      <FormInput v-model="form.dieuKienHoanThanh" label="Điều kiện hoàn thành" />
      <FormInput v-model="form.huongDan" label="Hướng dẫn" />
      <FormInput v-model="form.diemDatToiThieu" type="number" label="Điểm đạt tối thiểu" />
      <FormInput v-model="form.donViDiem" label="Đơn vị điểm" />
      <FormInput v-model="form.tyLeChuyenCanToiThieu" type="number" label="Tỷ lệ chuyên cần tối thiểu" />
      <FormInput v-model="form.congThucQuyDoi" label="Công thức quy đổi" />

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
  { key: 'viTri', label: 'Vị trí' },
  { key: 'tinhChat', label: 'Tính chất' },
  { key: 'diemDatToiThieu', label: 'Điểm đạt tối thiểu' }
]

const {
  items, form, loading, saving, errorMessage, editingId,
  fetchItems, editItem, saveItem, deleteItem, resetForm
} = useChuongTrinhCrud(chuongTrinhService.syllabusMonHoc, {
  chuongTrinhMonId: null,
  viTri: '',
  tinhChat: '',
  mucTieu: '',
  phuongPhapDanhGia: '',
  dieuKienHoanThanh: '',
  huongDan: '',
  diemDatToiThieu: null,
  donViDiem: '',
  tyLeChuyenCanToiThieu: null,
  batBuocDuThi: false,
  congThucQuyDoi: ''
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