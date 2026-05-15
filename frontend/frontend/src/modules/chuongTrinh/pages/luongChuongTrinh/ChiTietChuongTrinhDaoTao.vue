<template>
  <section class="ct-screen">
    <div class="ct-shell">
      <header class="ct-page-head">
        <div class="ct-head-main">
          <div class="ct-kicker">Chi tiết chương trình</div>
          <h1 class="ct-title">{{ title }}</h1>
          <p class="ct-desc">Thông tin chương trình và các phiên bản đào tạo đang áp dụng.</p>
        </div>
        <div class="ct-actions">
          <RouterLink class="ct-btn light" to="/quy-trinh/chuong-trinh">Quay lại chương trình đào tạo</RouterLink>
        </div>
      </header>

      <main class="ct-main">
        <div v-if="loading" class="ct-loading">Đang tải chi tiết chương trình...</div>
        <div v-else-if="error" class="ct-error">{{ error }}</div>
        <template v-else>
          <section class="ct-panel">
            <div class="ct-panel-head compact">
              <div>
                <h2 class="ct-panel-title">Thông tin chương trình</h2>
                <p class="ct-panel-subtitle">Thông tin chính của chương trình đào tạo.</p>
              </div>
              <button class="ct-btn small light" type="button" @click="openEditChuongTrinh">Sửa</button>
            </div>
            <div class="ct-info-grid padded">
              <article class="ct-info"><b>Mã chương trình</b><span>{{ detail.maChuongTrinh || '—' }}</span></article>
              <article class="ct-info"><b>Ngành</b><span>{{ detail.tenNganh || detail.nganhId || '—' }}</span></article>
              <article class="ct-info"><b>Trình độ</b><span>{{ detail.tenTrinhDo || detail.trinhDoId || '—' }}</span></article>
              <article class="ct-info"><b>Thời gian</b><span>{{ detail.thoiGianDaoTao || '—' }}</span></article>
            </div>
          </section>

          <section class="ct-panel">
            <div class="ct-panel-head">
              <div>
                <h2 class="ct-panel-title">Phiên bản chương trình</h2>
                <p class="ct-panel-subtitle">Mỗi phiên bản có khung chương trình, mục tiêu và chuẩn đầu ra riêng.</p>
              </div>
              <button class="ct-btn" type="button" @click="openCreateVersion">Thêm phiên bản</button>
            </div>
            <div class="ct-table-wrap">
              <table class="ct-table">
                <thead><tr><th>Mã version</th><th>Tên version</th><th>Ngày áp dụng</th><th>Tổng tín chỉ</th><th>Trạng thái</th><th class="ct-right">Thao tác</th></tr></thead>
                <tbody>
                  <tr v-for="item in phienBanRows" :key="layIdBanGhi(item)" class="ct-clickable-row" @dblclick="goVersion(item)">
                    <td><span class="ct-badge">{{ item.maVersion || '—' }}</span></td>
                    <td class="ct-strong">{{ item.tenVersion || '—' }}</td>
                    <td>{{ item.ngayApDung || '—' }}</td>
                    <td>{{ item.tongTinChi || '—' }}</td>
                    <td><span :class="['ct-badge', item.laHienHanh ? 'ok' : 'warn']">{{ item.laHienHanh ? 'Hiện hành' : 'Lưu trữ' }}</span></td>
                    <td class="ct-right">
                      <span class="ct-row-actions">
                        <RouterLink class="ct-btn small ghost" :to="`/quy-trinh/chuong-trinh/${chuongTrinhId}/phien-ban/${layIdBanGhi(item)}`">Mở</RouterLink>
                        <button class="ct-btn small light" type="button" @click="openEditVersion(item)">Sửa</button>
                        <button class="ct-btn small danger" type="button" @click="removeVersion(item)">Xóa</button>
                      </span>
                    </td>
                  </tr>
                  <tr v-if="!phienBanRows.length"><td colspan="6" class="ct-empty">Chưa có phiên bản thuộc chương trình này.</td></tr>
                </tbody>
              </table>
            </div>
          </section>
        </template>
      </main>
    </div>

    <BieuMauQuanLyModal v-model="chuongTrinhForm" :open="chuongTrinhModal" mode="edit" :fields="chuongTrinhFields" :saving="saving" title="Chương trình đào tạo" @close="chuongTrinhModal=false" @submit="saveChuongTrinh" />
    <BieuMauQuanLyModal v-model="versionForm" :open="versionModal" :mode="versionMode" :fields="versionFields" :saving="saving" title="Phiên bản chương trình" @close="versionModal=false" @submit="saveVersion" />
  </section>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import './luongChuongTrinh.css';
import BieuMauQuanLyModal from '@/modules/chuongTrinh/components/modal/BieuMauQuanLyModal.vue';
import { CHUONG_TRINH_RESOURCE_MAP } from '@/modules/chuongTrinh/constants/trangThaiChuongTrinh.js';
import { API_LUONG_CHUONG_TRINH, capNhatBanGhi, layChiTietChuongTrinh, layDanhSachPhienBan, layIdBanGhi, locTheoGiaTri, taoBanGhi, taoNhanChuongTrinh, xoaBanGhi } from '@/modules/chuongTrinh/services/luongChuongTrinhDaoTaoService.js';

const route = useRoute();
const router = useRouter();
const chuongTrinhId = computed(() => route.params.chuongTrinhId);
const detail = ref({});
const phienBanRows = ref([]);
const loading = ref(false);
const saving = ref(false);
const error = ref('');
const title = computed(() => taoNhanChuongTrinh(detail.value));
const chuongTrinhFields = CHUONG_TRINH_RESOURCE_MAP['chuong-trinh'].fields;
const versionFields = CHUONG_TRINH_RESOURCE_MAP['chuong-trinh-version'].fields;
const chuongTrinhModal = ref(false);
const versionModal = ref(false);
const versionMode = ref('create');
const chuongTrinhForm = ref({});
const versionForm = ref({});

const goVersion = (item) => { const id = layIdBanGhi(item); if (id) router.push(`/quy-trinh/chuong-trinh/${chuongTrinhId.value}/phien-ban/${id}`); };
const loadData = async () => {
  loading.value = true; error.value = '';
  try {
    const [ct, versions] = await Promise.all([layChiTietChuongTrinh(chuongTrinhId.value), layDanhSachPhienBan({ chuongTrinhId: chuongTrinhId.value })]);
    detail.value = ct || {};
    phienBanRows.value = locTheoGiaTri(versions, 'chuongTrinhId', chuongTrinhId.value);
  } catch (err) { error.value = err?.message || 'Không tải được chi tiết chương trình.'; } finally { loading.value = false; }
};
const openEditChuongTrinh = () => { chuongTrinhForm.value = { ...detail.value }; chuongTrinhModal.value = true; };
const saveChuongTrinh = async () => { saving.value = true; try { await capNhatBanGhi(API_LUONG_CHUONG_TRINH.CHUONG_TRINH, chuongTrinhId.value, chuongTrinhForm.value); chuongTrinhModal.value = false; await loadData(); } finally { saving.value = false; } };
const openCreateVersion = () => { versionMode.value = 'create'; versionForm.value = { chuongTrinhId: Number(chuongTrinhId.value) }; versionModal.value = true; };
const openEditVersion = (item) => { versionMode.value = 'edit'; versionForm.value = { ...item }; versionModal.value = true; };
const saveVersion = async () => { saving.value = true; try { const id = layIdBanGhi(versionForm.value); if (versionMode.value === 'edit' && id) await capNhatBanGhi(API_LUONG_CHUONG_TRINH.PHIEN_BAN, id, versionForm.value); else await taoBanGhi(API_LUONG_CHUONG_TRINH.PHIEN_BAN, versionForm.value); versionModal.value = false; await loadData(); } finally { saving.value = false; } };
const removeVersion = async (item) => { const id = layIdBanGhi(item); if (id && confirm('Xóa phiên bản này?')) { await xoaBanGhi(API_LUONG_CHUONG_TRINH.PHIEN_BAN, id); await loadData(); } };

onMounted(loadData);
</script>
