<template>
  <DaoTaoPageShell title="Chi tiết ngành" description="Đi sâu theo id ngành, từ đây chuyển tiếp sang chương trình đào tạo của ngành.">
    <template #breadcrumb>
      <RouterLink :to="{ name: 'dao-tao-nganh' }">Ngành</RouterLink>
      <span> / ID {{ nganhId }}</span>
    </template>

    <div v-if="loading" class="card">Đang tải ngành...</div>
    <div v-else-if="error" class="card error">{{ error }}</div>
    <div v-else class="detail-grid">
      <div class="card">
        <h2>{{ item.tenNganh || 'Ngành đào tạo' }}</h2>
        <p><b>ID:</b> {{ item.id }}</p>
        <p><b>Mã ngành:</b> {{ item.maNganh }}</p>
        <p><b>Mô tả:</b> {{ item.moTa || '-' }}</p>
      </div>
      <div class="card action-card">
        <h3>Đi sâu nghiệp vụ</h3>
        <RouterLink class="primary-link" :to="{ name: 'dao-tao-nganh-chuong-trinh', params: { nganhId } }">
          Xem chương trình đào tạo của ngành
        </RouterLink>
      </div>
    </div>
  </DaoTaoPageShell>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';
import DaoTaoPageShell from './shared/DaoTaoPageShell.vue';
import { getNganhById } from '@/api/daoTao/ApiRespone/NganhController';
import { getErrorMessage, unwrapApiData } from '@/api/apiResponse.js';

const route = useRoute();
const nganhId = route.params.nganhId;
const item = ref({});
const loading = ref(false);
const error = ref('');

const fetchItem = async () => {
  loading.value = true;
  try {
    item.value = unwrapApiData(await getNganhById(nganhId)) || {};
  } catch (err) {
    error.value = getErrorMessage(err);
  } finally {
    loading.value = false;
  }
};

onMounted(fetchItem);
</script>

<style scoped>
.detail-grid {
  display: grid;
  grid-template-columns: 1.3fr 0.7fr;
  gap: 16px;
}
.card {
  padding: 18px;
  background: #fff;
  border: 1px solid #e2e8f0;
  border-radius: 16px;
}
.error { color: #dc2626; }
.primary-link {
  display: inline-block;
  padding: 10px 14px;
  color: #fff;
  background: #2563eb;
  border-radius: 10px;
  text-decoration: none;
  font-weight: 800;
}
@media (max-width: 800px) { .detail-grid { grid-template-columns: 1fr; } }
</style>
