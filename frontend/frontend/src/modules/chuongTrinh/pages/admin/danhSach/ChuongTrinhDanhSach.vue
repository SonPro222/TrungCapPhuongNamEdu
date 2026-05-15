<script setup>
import { computed } from 'vue';
import ChuongTrinhCard from '@/modules/chuongTrinh/components/card/chuongTrinhCard.vue';
import ChuongTrinhForm from '@/modules/chuongTrinh/components/form/chuongTrinhForm.vue';
import XacNhanXoaModal from '@/modules/chuongTrinh/components/modal/XacNhanXoaModal.vue';
import ChuongTrinhTable from '@/modules/chuongTrinh/components/table/chuongTrinhTable.vue';
import { useChuongTrinh } from '@/modules/chuongTrinh/composables/useChuongTrinh.js';

const {
  activeResource,
  activeResourceKey,
  applyFilters,
  cancelDelete,
  changeResource,
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
  resources,
  saveForm,
  saving,
  selectRecordInFlow,
} = useChuongTrinh();

const totalLabel = computed(() => loading.value ? '...' : filteredRows.value.length);
</script>

<template>
  <section class="ct-page">
    <aside class="ct-sidebar">
      <div class="ct-brand">
        <span class="ct-kicker">Module</span>
        <h2>Chương trình</h2>
        <p>Quản lý chương trình đào tạo, phiên bản, môn học và môn trong chương trình.</p>
      </div>

      <button
        v-for="resource in resources"
        :key="resource.key"
        type="button"
        :class="['ct-menu-item', { active: resource.key === activeResourceKey }]"
        @click="changeResource(resource)"
      >
        <span>{{ resource.title }}</span>
        <small>{{ resource.segment }}</small>
      </button>
    </aside>

    <main class="ct-main">
      <header class="ct-header">
        <div>
          <span class="ct-kicker">Quản lý hệ thống trường học</span>
          <h1>{{ activeResource.title }}</h1>
          <p>{{ activeResource.description }}</p>
        </div>
        <button type="button" class="ct-primary-btn" @click="openCreate">+ Thêm mới</button>
      </header>

      <div class="ct-stats">
        <ChuongTrinhCard title="Tổng bản ghi" :value="totalLabel" hint="Theo bộ lọc hiện tại" />
        <ChuongTrinhCard title="Controller" :value="activeResource.endpoint" hint="API đang sử dụng" />
      </div>

      <section class="ct-filter-card">
        <label class="ct-search">
          <span>Tìm kiếm nhanh</span>
          <input v-model.trim="keyword" type="search" placeholder="Nhập mã, tên, nội dung..." />
        </label>

        <label v-for="filter in activeResource.filters" :key="filter.key" class="ct-filter-field">
          <span>{{ filter.label }}</span>
          <input v-model.trim="filters[filter.key]" :type="filter.type || 'text'" />
        </label>

        <div class="ct-filter-actions">
          <button type="button" class="ct-light-btn" @click="clearFilters">Xóa lọc</button>
          <button type="button" class="ct-primary-btn ct-primary-btn--small" @click="applyFilters">Lọc API</button>
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

    <div v-if="isFormOpen" class="ct-modal-backdrop" @click.self="closeForm">
      <section class="ct-form-modal">
        <header class="ct-form-head">
          <div>
            <span class="ct-kicker">{{ form.id ? 'Cập nhật' : 'Biểu mẫu' }}</span>
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
      title="Xóa bản ghi chương trình"
      message="Thao tác này sẽ gọi đúng controller xóa của resource hiện tại. Bạn có chắc chắn muốn tiếp tục?"
      @cancel="cancelDelete"
      @confirm="confirmDelete"
    />
  </section>
</template>

<style scoped>
.ct-page{display:grid;grid-template-columns:248px minmax(0,1fr);gap:16px;min-height:100vh;padding:16px;background:#f5f7fb;color:#0f172a;font-family:Inter,system-ui,-apple-system,BlinkMacSystemFont,"Segoe UI",sans-serif}.ct-sidebar{position:sticky;top:16px;height:calc(100vh - 32px);overflow:auto;border:1px solid #e5e7eb;border-radius:14px;background:#fff;padding:14px;box-shadow:0 8px 24px rgba(15,23,42,.05)}.ct-brand{padding:4px 4px 12px;border-bottom:1px solid #eef2f7;margin-bottom:8px}.ct-kicker{display:inline-flex;font-size:11px;font-weight:800;text-transform:uppercase;letter-spacing:.08em;color:#2563eb}.ct-brand h2,.ct-header h1,.ct-form-head h3{margin:4px 0;color:#0f172a;letter-spacing:-.02em}.ct-brand h2{font-size:20px}.ct-brand p,.ct-header p{margin:0;color:#64748b;font-size:12.5px;line-height:1.5}.ct-menu-item{display:grid;width:100%;gap:2px;border:0;border-radius:10px;background:transparent;padding:9px 10px;text-align:left;cursor:pointer;color:#334155}.ct-menu-item span{font-size:13px;font-weight:750}.ct-menu-item small{font-size:11px;color:#94a3b8}.ct-menu-item:hover,.ct-menu-item.active{background:#eff6ff;color:#1d4ed8}.ct-main{min-width:0;display:grid;align-content:start;gap:12px}.ct-header{display:flex;align-items:flex-start;justify-content:space-between;gap:14px;border:1px solid #e5e7eb;border-radius:14px;background:#fff;padding:16px;box-shadow:0 8px 24px rgba(15,23,42,.05)}.ct-header h1{font-size:24px}.ct-primary-btn,.ct-light-btn{border:0;border-radius:9px;padding:8px 12px;font-size:13px;font-weight:750;cursor:pointer;white-space:nowrap}.ct-primary-btn{background:#2563eb;color:#fff}.ct-primary-btn--small{padding:7px 11px}.ct-light-btn{background:#f1f5f9;color:#334155}.ct-stats{display:grid;grid-template-columns:180px minmax(0,1fr);gap:10px}.ct-filter-card{display:flex;align-items:end;gap:10px;flex-wrap:wrap;border:1px solid #e5e7eb;border-radius:14px;background:#fff;padding:12px;box-shadow:0 8px 24px rgba(15,23,42,.04)}.ct-search{flex:1 1 260px}.ct-filter-field{width:150px}.ct-search,.ct-filter-field{display:grid;gap:5px}.ct-search span,.ct-filter-field span{font-size:12px;font-weight:750;color:#475569}.ct-search input,.ct-filter-field input{border:1px solid #dbe3ef;border-radius:9px;padding:8px 10px;font-size:13px;outline:none}.ct-search input:focus,.ct-filter-field input:focus{border-color:#2563eb;box-shadow:0 0 0 3px rgba(37,99,235,.1)}.ct-filter-actions{display:flex;gap:8px}.ct-modal-backdrop{position:fixed;inset:0;z-index:50;display:grid;place-items:center;background:rgba(15,23,42,.42);padding:18px}.ct-form-modal{width:min(760px,100%);max-height:calc(100vh - 36px);overflow:auto;border-radius:14px;background:#fff;padding:16px;box-shadow:0 22px 60px rgba(15,23,42,.22)}.ct-form-head{display:flex;align-items:flex-start;justify-content:space-between;margin-bottom:14px}.ct-form-head h3{font-size:19px}.ct-close-btn{border:0;background:#f1f5f9;color:#334155;border-radius:9px;width:32px;height:32px;font-size:22px;line-height:1;cursor:pointer}@media(max-width:980px){.ct-page{grid-template-columns:1fr}.ct-sidebar{position:static;height:auto}.ct-stats{grid-template-columns:1fr}}@media(max-width:640px){.ct-page{padding:10px}.ct-header,.ct-filter-card{align-items:stretch;flex-direction:column}.ct-filter-field{width:100%}.ct-filter-actions{justify-content:flex-end}}
</style>
