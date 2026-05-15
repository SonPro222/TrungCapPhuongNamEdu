<template>
  <section class="ct-screen ct-dashboard-screen">
    <div class="ct-shell">
      <header class="ct-page-head ct-hero-head">
        <div class="ct-head-main">
          <div class="ct-kicker">Module Chương trình</div>
          <h1 class="ct-title">Quản lý chương trình đào tạo</h1>
          <p class="ct-desc">Bắt đầu từ bảng chính Chương trình đào tạo, sau đó đi tiếp theo bảng tổng, flow nghiệp vụ hoặc module explorer để cấu hình đầy đủ phiên bản, môn học, syllabus và điều kiện tốt nghiệp.</p>
        </div>
        <div class="ct-actions">
          <RouterLink class="ct-btn ghost" to="/chuong-trinh/bang-tong/chuong-trinh">Bảng tổng</RouterLink>
          <RouterLink class="ct-btn" to="/chuong-trinh/flow">Flow nghiệp vụ</RouterLink>
        </div>
      </header>

      <section class="ct-mode-grid">
        <article
          v-for="mode in modeCards"
          :key="mode.key"
          :class="['ct-mode-card', { active: activeMode === mode.key }]"
          @mouseenter="activeMode = mode.key"
          @click="goMode(mode)"
        >
          <span class="ct-mode-icon">{{ mode.icon }}</span>
          <div>
            <h2>{{ mode.title }}</h2>
            <p>{{ mode.description }}</p>
          </div>
        </article>
      </section>

      <section class="ct-dashboard-layout">
        <aside class="ct-sidebar ct-module-explorer">
          <div class="ct-sidebar-title">Bảng tổng theo module</div>
          <div class="ct-module-group" v-for="group in resourceGroups" :key="group.key">
            <button
              class="ct-module-head"
              type="button"
              @mouseenter="hoverGroup = group.key"
              @click="toggleGroup(group.key)"
            >
              <span>{{ group.title }}</span>
              <small>{{ group.items.length }} bảng</small>
            </button>
            <div v-show="expandedGroups.includes(group.key) || hoverGroup === group.key" class="ct-module-children">
              <RouterLink
                v-for="item in group.items"
                :key="item.key"
                class="ct-module-child"
                :to="`/chuong-trinh/bang-tong/${item.segment}`"
              >
                <b>{{ item.title }}</b>
                <small>{{ item.segment }}</small>
              </RouterLink>
            </div>
          </div>
        </aside>

        <main class="ct-main">
          <section class="ct-panel ct-main-table-panel">
            <div class="ct-panel-head compact">
              <div>
                <h2 class="ct-panel-title">Bảng chính: Chương trình đào tạo</h2>
                <p class="ct-panel-subtitle">Chọn một chương trình để đi vào flow phiên bản, khung chương trình và môn học.</p>
              </div>
              <div class="ct-actions">
                <button class="ct-btn light" type="button" @click="loadData">Tải lại</button>
                <RouterLink class="ct-btn" to="/chuong-trinh/bang-tong/chuong-trinh">CRUD bảng này</RouterLink>
              </div>
            </div>

            <div class="ct-filter two">
              <input v-model.trim="keyword" class="ct-input" type="search" placeholder="Tìm mã chương trình, tên chương trình, thời gian đào tạo..." />
              <RouterLink class="ct-btn ghost" to="/chuong-trinh/flow">Xem flow</RouterLink>
            </div>

            <div v-if="loading" class="ct-loading">Đang tải dữ liệu chương trình...</div>
            <div v-else-if="error" class="ct-error">{{ error }}</div>
            <div v-else class="ct-table-wrap">
              <table class="ct-table compact">
                <thead>
                  <tr>
                    <th>Mã CT</th>
                    <th>Tên chương trình</th>
                    <th>Ngành ID</th>
                    <th>Trình độ ID</th>
                    <th>Loại CT ID</th>
                    <th>Thời gian</th>
                    <th class="ct-right">Thao tác</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="item in filteredRows" :key="layIdBanGhi(item)" class="ct-clickable-row" @dblclick="goFlow(item)">
                    <td><span class="ct-badge">{{ item.maChuongTrinh || '—' }}</span></td>
                    <td class="ct-strong">{{ item.tenChuongTrinh || '—' }}</td>
                    <td>{{ item.nganhId || '—' }}</td>
                    <td>{{ item.trinhDoId || '—' }}</td>
                    <td>{{ item.loaiChuongTrinhId || '—' }}</td>
                    <td>{{ item.thoiGianDaoTao || '—' }}</td>
                    <td class="ct-right">
                      <span class="ct-row-actions">
                        <RouterLink class="ct-btn small ghost" :to="`/chuong-trinh/flow/${layIdBanGhi(item)}`">Mở flow</RouterLink>
                        <RouterLink class="ct-btn small light" to="/chuong-trinh/bang-tong/chuong-trinh">Sửa bảng</RouterLink>
                      </span>
                    </td>
                  </tr>
                  <tr v-if="!filteredRows.length"><td colspan="7" class="ct-empty">Chưa có dữ liệu chương trình phù hợp.</td></tr>
                </tbody>
              </table>
            </div>
          </section>
        </main>
      </section>
    </div>
  </section>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import './luongChuongTrinh.css';
import { CHUONG_TRINH_RESOURCES } from '@/modules/chuongTrinh/constants/trangThaiChuongTrinh.js';
import { layDanhSachChuongTrinh, layIdBanGhi } from '@/modules/chuongTrinh/services/luongChuongTrinhDaoTaoService.js';

const router = useRouter();
const rows = ref([]);
const loading = ref(false);
const error = ref('');
const keyword = ref('');
const activeMode = ref('bang-tong');
const hoverGroup = ref('cot-loi');
const expandedGroups = ref(['cot-loi']);

const modeCards = [
  { key: 'bang-tong', icon: '01', title: 'Bảng tổng', description: 'CRUD từng bảng đúng API backend, bắt đầu từ Chương trình đào tạo.', to: '/chuong-trinh/bang-tong/chuong-trinh' },
  { key: 'flow', icon: '02', title: 'Flow nghiệp vụ', description: 'Đi theo luồng người dùng thật: chương trình → phiên bản → khung → môn.', to: '/chuong-trinh/flow' },
  { key: 'explorer', icon: '03', title: 'Module explorer', description: 'Di chuột/click module để mở nhanh bảng con liên quan.', to: null },
];

const groupOf = (item) => {
  const key = item.key;
  if (['chuong-trinh', 'chuong-trinh-version', 'chuong-trinh-mon', 'mon-hoc'].includes(key)) return 'cot-loi';
  if (['nhom-kien-thuc', 'nhom-tu-chon', 'mon-tu-chon', 'mon-tien-quyet'].includes(key)) return 'cau-truc';
  if (['syllabus-chuong-trinh', 'syllabus-mon-hoc', 'syllabus-chuong-bai', 'syllabus-tai-lieu', 'dieu-kien-mon-hoc'].includes(key)) return 'de-cuong';
  if (['muc-tieu-chuong-trinh', 'nang-luc-dau-ra', 'dieu-kien-tot-nghiep', 'vi-tri-viec-lam', 'quy-doi-diem'].includes(key)) return 'chuan-dau-ra';
  return 'khac';
};

const groupLabels = {
  'cot-loi': 'Cốt lõi chương trình',
  'cau-truc': 'Cấu trúc học phần',
  'de-cuong': 'Đề cương môn học',
  'chuan-dau-ra': 'Chuẩn đầu ra & điều kiện',
  khac: 'Khác',
};

const resourceGroups = computed(() => {
  const groups = CHUONG_TRINH_RESOURCES.reduce((map, item) => {
    const key = groupOf(item);
    if (!map[key]) map[key] = { key, title: groupLabels[key], items: [] };
    map[key].items.push(item);
    return map;
  }, {});

  return ['cot-loi', 'cau-truc', 'de-cuong', 'chuan-dau-ra', 'khac'].map((key) => groups[key]).filter(Boolean);
});

const filteredRows = computed(() => {
  const search = keyword.value.toLowerCase();
  if (!search) return rows.value;
  return rows.value.filter((item) => [item.maChuongTrinh, item.tenChuongTrinh, item.thoiGianDaoTao, item.doiTuongTuyenSinh]
    .some((value) => String(value || '').toLowerCase().includes(search)));
});

const loadData = async () => {
  loading.value = true;
  error.value = '';
  try {
    rows.value = await layDanhSachChuongTrinh();
  } catch (err) {
    error.value = err?.response?.data?.message || err?.message || 'Không tải được danh sách chương trình.';
    rows.value = [];
  } finally {
    loading.value = false;
  }
};

const toggleGroup = (key) => {
  activeMode.value = 'explorer';
  if (expandedGroups.value.includes(key)) expandedGroups.value = expandedGroups.value.filter((item) => item !== key);
  else expandedGroups.value = [...expandedGroups.value, key];
};

const goMode = (mode) => {
  activeMode.value = mode.key;
  if (mode.to) router.push(mode.to);
};

const goFlow = (item) => {
  const id = layIdBanGhi(item);
  if (id) router.push(`/chuong-trinh/flow/${id}`);
};

onMounted(loadData);
</script>
