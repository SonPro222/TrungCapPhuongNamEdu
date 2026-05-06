<template>
  <DaoTaoPageShell title="Chi tiết lớp hành chính" description="Xem thông tin chi tiết lớp hành chính.">
    <template #breadcrumb>
      <RouterLink :to="{ name: 'dao-tao-khoa-dao-tao' }">Khóa đào tạo</RouterLink>
      <span> / </span>
      <RouterLink :to="backRoute">Lớp hành chính</RouterLink>
      <span> / Chi tiết {{ lopHanhChinhId }}</span>
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
          <span>ID khóa đào tạo</span>
          <strong>{{ item.khoaDaoTaoId ?? '-' }}</strong>
        </div>

        <div>
          <span>Mã lớp</span>
          <strong>{{ item.maLop ?? '-' }}</strong>
        </div>

        <div>
          <span>Tên lớp</span>
          <strong>{{ item.tenLop ?? '-' }}</strong>
        </div>

        <div>
          <span>Sĩ số</span>
          <strong>{{ item.siSo ?? '-' }}</strong>
        </div>

        <div>
          <span>Trạng thái</span>
          <strong>{{ item.trangThai ?? '-' }}</strong>
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
import { RouterLink, useRoute, useRouter } from 'vue-router';
import DaoTaoPageShell from '@/components/shared/daoTao/DaoTaoPageShell.vue';
import { getLopHanhChinhById } from '@/api/daoTao/ApiRespone/LopHanhChinhController';
import { getErrorMessage, unwrapApiData } from '../../api/apiResponse.js';

const route = useRoute();
const router = useRouter();

const lopHanhChinhId = computed(() => route.params.lopHanhChinhId);
const item = ref(window.history.state?.lopHanhChinh || null);
const loading = ref(false);
const error = ref('');

const backRoute = computed(() => {
  const khoaDaoTaoId = item.value?.khoaDaoTaoId;

  if (khoaDaoTaoId) {
    return {
      name: 'dao-tao-khoa-lop-hanh-chinh',
      params: { khoaDaoTaoId },
    };
  }

  return { name: 'dao-tao-lop-hanh-chinh' };
});

const fetchDetail = async () => {
  if (item.value) return;

  loading.value = true;
  error.value = '';

  try {
    item.value = unwrapApiData(await getLopHanhChinhById(lopHanhChinhId.value));
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