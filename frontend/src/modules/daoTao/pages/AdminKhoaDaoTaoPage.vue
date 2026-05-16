<template>
  <div>
    <PageHeader title="Khóa đào tạo" />

    <form class="module-form" @submit.prevent="saveItem">
      <FormInput
          v-model="form.maKhoa"
          label="Mã khóa"
          placeholder="Nhập mã khóa"
          required
      />

      <FormInput
          v-model="form.tenKhoa"
          label="Tên khóa"
          placeholder="Nhập tên khóa"
      />

      <FormInput
          v-model="form.namBatDau"
          type="number"
          label="Năm bắt đầu"
          placeholder="Nhập năm bắt đầu"
      />

      <FormInput
          v-model="form.namKetThuc"
          type="number"
          label="Năm kết thúc"
          placeholder="Nhập năm kết thúc"
      />

      <FormInput
          v-model="form.ghiChu"
          label="Ghi chú"
          placeholder="Nhập ghi chú"
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
  { key: 'maKhoa', label: 'Mã khóa' },
  { key: 'tenKhoa', label: 'Tên khóa' },
  { key: 'namBatDau', label: 'Năm bắt đầu' },
  { key: 'namKetThuc', label: 'Năm kết thúc' },
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
} = useDaoTaoCrud(daoTaoService.khoaDaoTao, {
  maKhoa: '',
  tenKhoa: '',
  namBatDau: null,
  namKetThuc: null,
  ghiChu: ''
})

onMounted(fetchItems)
</script>