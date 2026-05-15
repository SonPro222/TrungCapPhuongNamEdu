<script setup>
import { computed } from 'vue';
import ChuongTrinhTable from '@/modules/chuongTrinh/components/table/chuongTrinhTable.vue';
import ChuongTrinhForm from '@/modules/chuongTrinh/components/form/chuongTrinhForm.vue';
import XacNhanXoaModal from '@/modules/chuongTrinh/components/modal/XacNhanXoaModal.vue';
import { useChuongTrinh } from '@/modules/chuongTrinh/composables/useChuongTrinh.js';
import './luongChuongTrinh.css';

const {
  activeResource,
  applyFilters,
  cancelDelete,
  clearFilters,
  closeForm,
  confirmDelete,
  deleteTarget,
  error,
  filteredRows,
  filters,
  form,
  formatCell,
  isFormOpen,
  keyword,
  loading,
  openCreate,
  openEdit,
  requestDelete,
  saveForm,
  saving,
  selectRecordInFlow,
} = useChuongTrinh();

const totalLabel = computed(() => (loading.value ? '...' : filteredRows.value.length));
</script>

<template>
  <section class="ct-screen">
    <div class="ct-shell">
      <header class="ct-page-head soft">
        <div class="ct-head-main">
          <div class="ct-kicker">Làm việc</div>
          <h1 class="ct-title">{{ activeResource.title }}</h1>
          <p class="ct-desc">{{ activeResource.description }}</p>
        </div>
        <div class="ct-actions">
          <RouterLink class="ct-btn light" to="/quy-trinh/chuong-trinh">Chương trình</RouterLink>
          <button class="ct-btn" type="button" @click="openCreate">Thêm</button>
        </div>
      </header>

      <main class="ct-main compact-flow">
        <section class="ct-summary-strip">
          <article>
            <span>Bản ghi</span>
            <strong>{{ totalLabel }}</strong>
          </article>
          <article>
            <span>API</span>
            <strong>{{ activeResource.endpoint }}</strong>
          </article>
        </section>

        <section class="ct-filter-card flow-filter">
          <label class="ct-search">
            <span>Tìm nhanh</span>
            <input v-model.trim="keyword" type="search" placeholder="Nhập mã, tên, nội dung..." />
          </label>
          <label v-for="filter in activeResource.filters" :key="filter.key" class="ct-filter-field">
            <span>{{ filter.label }}</span>
            <input v-model.trim="filters[filter.key]" :type="filter.type || 'text'" />
          </label>
          <div class="ct-filter-actions">
            <button type="button" class="ct-btn light small" @click="clearFilters">Xóa</button>
            <button type="button" class="ct-btn small" @click="applyFilters">Lọc</button>
          </div>
        </section>

        <ChuongTrinhTable
          :columns="activeResource.columns"
          :error="error"
          :format-cell="formatCell"
          :loading="loading"
          :rows="filteredRows"
          @delete="requestDelete"
          @edit="openEdit"
          @select="selectRecordInFlow"
        />
      </main>
    </div>

    <div v-if="isFormOpen" class="ct-modal-backdrop" @click.self="closeForm">
      <section class="ct-form-modal">
        <header class="ct-form-head">
          <div>
            <span class="ct-kicker">{{ form.id ? 'Sửa' : 'Thêm' }}</span>
            <h3>{{ activeResource.title }}</h3>
          </div>
          <button type="button" class="ct-close-btn" @click="closeForm">×</button>
        </header>
        <ChuongTrinhForm
          :fields="activeResource.fields"
          :model-value="form"
          :saving="saving"
          @cancel="closeForm"
          @submit="saveForm"
          @update:model-value="Object.assign(form, $event)"
        />
      </section>
    </div>

    <XacNhanXoaModal
      :open="Boolean(deleteTarget)"
      title="Xóa bản ghi"
      message="Bạn có chắc chắn muốn xóa bản ghi này?"
      @cancel="cancelDelete"
      @confirm="confirmDelete"
    />
  </section>
</template>
