<template>
  <div>
    <PageHeader title="Ngành đào tạo" />

    <form class="module-form" @submit.prevent="saveItem">
      <FormInput
          v-model="form.maNganh"
          label="Mã ngành"
          placeholder="Nhập mã ngành"
          required
      />

      <FormInput
          v-model="form.tenNganh"
          label="Tên ngành"
          placeholder="Nhập tên ngành"
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
  { key: 'maNganh', label: 'Mã ngành' },
  { key: 'tenNganh', label: 'Tên ngành' },
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
} = useDaoTaoCrud(daoTaoService.nganh, {
  maNganh: '',
  tenNganh: '',
  moTa: ''
})

onMounted(fetchItems)
</script>