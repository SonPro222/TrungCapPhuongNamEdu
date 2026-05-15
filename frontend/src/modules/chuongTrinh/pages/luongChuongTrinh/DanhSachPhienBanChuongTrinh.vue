<template>
  <section class="ct-screen">
    <div class="ct-shell">
      <header class="ct-page-head">
        <div class="ct-head-main">
          <div class="ct-kicker">Phiên bản chương trình</div>
          <h1 class="ct-title">{{ title }}</h1>
          <p class="ct-desc">Quản lý các phiên bản áp dụng của chương trình đào tạo.</p>
        </div>
        <div class="ct-actions">
          <RouterLink class="ct-btn light" :to="`/quy-trinh/chuong-trinh/${chuongTrinhId}`">Chi tiết chương trình</RouterLink>
          <button class="ct-btn" type="button" @click="openCreate">Thêm phiên bản</button>
        </div>
      </header>

      <section class="ct-panel">
        <div class="ct-panel-head"><div><h2 class="ct-panel-title">Danh sách phiên bản</h2><p class="ct-panel-subtitle">Mở version để quản lý mục tiêu, chuẩn đầu ra và khung chương trình.</p></div><span class="ct-badge">{{ rows.length }} bản ghi</span></div>
        <div v-if="loading" class="ct-loading">Đang tải phiên bản...</div>
        <div v-else-if="error" class="ct-error">{{ error }}</div>
        <div v-else class="ct-table-wrap">
          <table class="ct-table">
            <thead><tr><th>Mã version</th><th>Tên version</th><th>Áp dụng</th><th>Quyết định</th><th>Tín chỉ</th><th>Trạng thái</th><th class="ct-right">Thao tác</th></tr></thead>
            <tbody>
              <tr v-for="item in rows" :key="layIdBanGhi(item)" class="ct-clickable-row" @dblclick="goDetail(item)">
                <td><span class="ct-badge">{{ item.maVersion || '—' }}</span></td>
                <td class="ct-strong">{{ item.tenVersion || '—' }}</td>
                <td>{{ item.ngayApDung || '—' }}</td>
                <td>{{ item.soQuyetDinh || '—' }}</td>
                <td>{{ item.tongTinChi || '—' }}</td>
                <td><span :class="['ct-badge', item.laHienHanh ? 'ok' : 'warn']">{{ item.laHienHanh ? 'Hiện hành' : 'Lưu trữ' }}</span></td>
                <td class="ct-right"><span class="ct-row-actions"><RouterLink class="ct-btn small ghost" :to="`/quy-trinh/chuong-trinh/${chuongTrinhId}/phien-ban/${layIdBanGhi(item)}`">Mở</RouterLink><button class="ct-btn small light" type="button" @click="openEdit(item)">Sửa</button><button class="ct-btn small danger" type="button" @click="removeRow(item)">Xóa</button></span></td>
              </tr>
              <tr v-if="!rows.length"><td colspan="7" class="ct-empty">Chưa có phiên bản.</td></tr>
            </tbody>
          </table>
        </div>
      </section>
    </div>
    <BieuMauQuanLyModal v-model="form" :open="modalOpen" :mode="formMode" :fields="fields" :saving="saving" title="Phiên bản chương trình" @close="closeModal" @submit="saveForm" />
  </section>
</template>
<script setup>
import { computed, onMounted, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import './luongChuongTrinh.css';
import BieuMauQuanLyModal from '@/modules/chuongTrinh/components/modal/BieuMauQuanLyModal.vue';
import { CHUONG_TRINH_RESOURCE_MAP } from '@/modules/chuongTrinh/constants/trangThaiChuongTrinh.js';
import { API_LUONG_CHUONG_TRINH, capNhatBanGhi, layChiTietChuongTrinh, layDanhSachPhienBan, layIdBanGhi, locTheoGiaTri, taoBanGhi, taoNhanChuongTrinh, xoaBanGhi } from '@/modules/chuongTrinh/services/luongChuongTrinhDaoTaoService.js';
const route = useRoute(); const router = useRouter();
const chuongTrinhId = computed(() => route.params.chuongTrinhId); const title = ref('Chương trình đào tạo'); const rows = ref([]); const loading = ref(false); const saving = ref(false); const error = ref(''); const modalOpen = ref(false); const formMode = ref('create'); const form = ref({}); const fields = CHUONG_TRINH_RESOURCE_MAP['chuong-trinh-version'].fields;
const goDetail = (item) => { const id = layIdBanGhi(item); if (id) router.push(`/quy-trinh/chuong-trinh/${chuongTrinhId.value}/phien-ban/${id}`); };
const loadData = async () => { loading.value = true; error.value = ''; try { const [ct, list] = await Promise.all([layChiTietChuongTrinh(chuongTrinhId.value), layDanhSachPhienBan({ chuongTrinhId: chuongTrinhId.value })]); title.value = taoNhanChuongTrinh(ct); rows.value = locTheoGiaTri(list, 'chuongTrinhId', chuongTrinhId.value); } catch (err) { error.value = err?.message || 'Không tải được danh sách phiên bản.'; } finally { loading.value = false; } };
const openCreate = () => { formMode.value = 'create'; form.value = { chuongTrinhId: Number(chuongTrinhId.value) }; modalOpen.value = true; };
const openEdit = (item) => { formMode.value = 'edit'; form.value = { ...item }; modalOpen.value = true; };
const closeModal = () => { modalOpen.value = false; form.value = {}; };
const saveForm = async () => { saving.value = true; try { const id = layIdBanGhi(form.value); if (formMode.value === 'edit' && id) await capNhatBanGhi(API_LUONG_CHUONG_TRINH.PHIEN_BAN, id, form.value); else await taoBanGhi(API_LUONG_CHUONG_TRINH.PHIEN_BAN, form.value); closeModal(); await loadData(); } finally { saving.value = false; } };
const removeRow = async (item) => { const id = layIdBanGhi(item); if (id && confirm('Xóa phiên bản này?')) { await xoaBanGhi(API_LUONG_CHUONG_TRINH.PHIEN_BAN, id); await loadData(); } };
onMounted(loadData);
</script>
