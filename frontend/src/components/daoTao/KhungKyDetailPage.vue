<template>
  <DaoTaoPageShell title="Chi tiết khung kỳ" description="Xem chi tiết khung kỳ theo id.">
    <template #breadcrumb>
      <RouterLink :to="{ name: 'dao-tao-loai-chuong-trinh' }">Loại chương trình</RouterLink>
      <span> / </span>
      <RouterLink :to="backRoute">Khung kỳ</RouterLink>
      <span> / Chi tiết {{ khungKyId }}</span>
    </template>

    <div v-if="loading" class="card state">Đang tải dữ liệu...</div>
    <div v-else-if="error" class="card state error">{{ error }}</div>
    <div v-else-if="!item" class="card state">Không có dữ liệu</div>

    <div v-else class="card">
      <div class="detail-grid">
        <div>
          <span>ID</span>
          <strong>{{ item.id }}</strong>
        </div>
        <div>
          <span>ID loại chương trình</span>
          <strong>{{ item.loaiChuongTrinhId ?? '-' }}</strong>
        </div>
        <div>
          <span>Mã kỳ</span>
          <strong>{{ item.maKy ?? '-' }}</strong>
        </div>
        <div>
          <span>Tên kỳ</span>
          <strong>{{ item.tenKy ?? '-' }}</strong>
        </div>
        <div>
          <span>Thứ tự</span>
          <strong>{{ item.thuTu ?? '-' }}</strong>
        </div>
      </div>

      <div class="actions">
        <button type="button" class="btn secondary" @click="router.push(backRoute)">Quay lại</button>
      </div>
    </div>
  </DaoTaoPageShell>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import DaoTaoPageShell from '@/components/shared/daoTao/DaoTaoPageShell.vue';
import { getKhungKyById } from '@/api/daoTao/ApiRespone/KhungKyController';
import { getErrorMessage, unwrapApiData } from '../../api/apiResponse.js';

const route = useRoute();
const router = useRouter();

const khungKyId = computed(() => route.params.khungKyId)
const item = ref(null);
const loading = ref(false);
const error = ref('');

const backRoute = computed(() => {
  const loaiChuongTrinhId = item.value?.loaiChuongTrinhId;

  if (loaiChuongTrinhId) {
    return {
      name: 'dao-tao-loai-chuong-trinh-khung-ky',
      params: { loaiChuongTrinhId },
    };
  }

  return { name: 'dao-tao-khung-ky' };
});

const fetchDetail = async () => {
  loading.value = true;
  error.value = '';

  try {
    item.value = unwrapApiData(await getKhungKyById(khungKyId.value));
  } catch (err) {
    error.value = getErrorMessage(err);
  } finally {
    loading.value = false;
  }
};

onMounted(fetchDetail);
</script>

<style scoped>
.card {
  padding: 18px;
  background: #fff;
  border: 1px solid #e2e8f0;
  border-radius: 16px;
}

.state {
  color: #64748b;
  text-align: center;
}

.error {
  color: #dc2626;
}

.detail-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: 14px;
}

.detail-grid div {
  padding: 14px;
  background: #f8fafc;
  border-radius: 12px;
}

.detail-grid span {
  display: block;
  margin-bottom: 6px;
  color: #64748b;
  font-size: 13px;
  font-weight: 700;
}

.detail-grid strong {
  color: #0f172a;
  font-size: 16px;
}

.actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 18px;
}

.btn {
  cursor: pointer;
  border: 0;
  border-radius: 10px;
  padding: 10px 14px;
  font-weight: 700;
}

.secondary {
  color: #0f172a;
  background: #e2e8f0;
}
</style>