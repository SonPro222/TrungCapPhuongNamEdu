<template>
  <div>
    <PageHeader title="Loại chương trình" />

    <form class="module-form" @submit.prevent="saveItem">
      <FormInput
          v-model="form.maLoai"
          label="Mã loại"
          placeholder="Nhập mã loại"
          required
      />

      <FormInput
          v-model="form.tenLoai"
          label="Tên loại"
          placeholder="Nhập tên loại"
          required
      />

      <FormInput
          v-model="form.soThang"
          type="number"
          label="Số tháng"
          placeholder="Nhập số tháng"
          required
      />

      <FormInput
          v-model="form.soKy"
          type="number"
          label="Số kỳ"
          placeholder="Nhập số kỳ"
          required
      />

      <FormInput
          v-model="form.moTa"
          label="Mô tả"
          placeholder="Nhập mô tả"
      />

      <BaseButton
          type="submit"
          variant="primary"
          :loading="saving"
          :label="editingId ? 'Cập nhật' : 'Thêm mới'"
      />

      <BaseButton
          label="Làm mới"
          @click="resetForm"
      />
    </form>

    <p v-if="errorMessage" class="error-text">
      {{ errorMessage }}
    </p>

    <DataTable
        :columns="columns"
        :items="items"
        :loading="loading"
    >
      <template #actions="{ item }">
        <BaseButton
            label="Sửa"
            @click="editItem(item)"
        />

        <BaseButton
            label="Xóa"
            variant="danger"
            @click="deleteItem(item.id)"
        />
      </template>
    </DataTable>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'

import PageHeader from '@/shared/components/PageHeader.vue'
import DataTable from '@/shared/components/DataTable.vue'
import FormInput from '@/shared/components/FormInput.vue'
import BaseButton from '@/shared/components/BaseButton.vue'

import { daoTaoService } from '../services/daoTaoService'
import { useDaoTaoCrud } from '../composables/useDaoTaoCrud'

const columns = [
  { key: 'maLoai', label: 'Mã loại' },
  { key: 'tenLoai', label: 'Tên loại' },
  { key: 'soThang', label: 'Số tháng' },
  { key: 'soKy', label: 'Số kỳ' },
  { key: 'moTa', label: 'Mô tả' }
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
} = useDaoTaoCrud(daoTaoService.loaiChuongTrinh, {
  maLoai: '',
  tenLoai: '',
  soThang: null,
  soKy: null,
  moTa: ''
})

onMounted(fetchItems)
</script>