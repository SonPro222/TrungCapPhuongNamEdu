<template>
  <div>
    <PageHeader title="Syllabus chương trình" />

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

      <FormInput v-model="form.moTaTongQuan" label="Mô tả tổng quan" />
      <FormInput v-model="form.mucDich" label="Mục đích" />
      <FormInput v-model="form.yeuCauDaoTao" label="Yêu cầu đào tạo" />
      <FormInput v-model="form.phuongPhapDaoTao" label="Phương pháp đào tạo" />
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

const versions = ref([])

const columns = [
  { key: 'tenVersion', label: 'Phiên bản' },
  { key: 'moTaTongQuan', label: 'Mô tả tổng quan' },
  { key: 'mucDich', label: 'Mục đích' },
  { key: 'yeuCauDaoTao', label: 'Yêu cầu đào tạo' }
]

const {
  items, form, loading, saving, errorMessage, editingId,
  fetchItems, editItem, saveItem, deleteItem, resetForm
} = useChuongTrinhCrud(chuongTrinhService.syllabusChuongTrinh, {
  chuongTrinhVersionId: null,
  moTaTongQuan: '',
  mucDich: '',
  yeuCauDaoTao: '',
  phuongPhapDaoTao: '',
  ghiChu: ''
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