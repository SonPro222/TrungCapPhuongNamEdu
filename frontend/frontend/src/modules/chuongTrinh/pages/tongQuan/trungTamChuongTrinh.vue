<script setup>
import { computed, onMounted, ref } from 'vue';
import { DU_LIEU_CHUONG_TRINH, PHAN_HE_HE_THONG, QUY_TRINH_CHUONG_TRINH } from '@/modules/chuongTrinh/config/cauTrucHeThong.js';
import { layDanhSachChuongTrinh, layDanhSachMonHoc, layDanhSachPhienBan } from '@/modules/chuongTrinh/services/luongChuongTrinhDaoTaoService.js';

const activeSystemKey = ref('chuong-trinh');
const counts = ref({ chuongTrinh: 0, phienBan: 0, monHoc: 0 });
const loading = ref(false);

const activeSystem = computed(() => PHAN_HE_HE_THONG.find((item) => item.key === activeSystemKey.value) || PHAN_HE_HE_THONG[0]);

const loadCounts = async () => {
  loading.value = true;
  try {
    const [chuongTrinh, phienBan, monHoc] = await Promise.all([
      layDanhSachChuongTrinh().catch(() => []),
      layDanhSachPhienBan().catch(() => []),
      layDanhSachMonHoc().catch(() => []),
    ]);
    counts.value = { chuongTrinh: chuongTrinh.length, phienBan: phienBan.length, monHoc: monHoc.length };
  } finally {
    loading.value = false;
  }
};

onMounted(loadCounts);
</script>

<template>
  <section class="ct-home">
    <header class="ct-home-head">
      <div>
        <p>Trung tâm quản lý</p>
        <h1>Chương trình</h1>
        <span>Chọn nhanh, làm theo bước hoặc quản lý dữ liệu.</span>
      </div>
      <RouterLink class="ct-home-action" to="/chuong-trinh/quy-trinh">Mở</RouterLink>
    </header>

    <div class="ct-home-grid">
      <section class="ct-home-card ct-system-card">
        <div class="ct-card-title">
          <span>1</span>
          <div>
            <h2>Hệ thống</h2>
            <p>Rê chuột để xem bên trong.</p>
          </div>
        </div>

        <div class="ct-system-layout">
          <div class="ct-system-list">
            <RouterLink
              v-for="item in PHAN_HE_HE_THONG"
              :key="item.key"
              :to="item.to"
              :class="['ct-system-item', { active: item.key === activeSystemKey }]"
              @mouseenter="activeSystemKey = item.key"
              @focus="activeSystemKey = item.key"
            >
              <b>{{ item.label }}</b>
              <small>{{ item.hint }}</small>
            </RouterLink>
          </div>

          <div class="ct-system-detail">
            <h3>{{ activeSystem.label }}</h3>
            <p>{{ activeSystem.hint }}</p>
            <div class="ct-child-list">
              <RouterLink v-for="child in activeSystem.children" :key="child.label" :to="child.to">
                {{ child.label }}
              </RouterLink>
            </div>
          </div>
        </div>
      </section>

      <section class="ct-home-card">
        <div class="ct-card-title">
          <span>2</span>
          <div>
            <h2>Quy trình</h2>
            <p>Làm từ ngoài vào trong.</p>
          </div>
        </div>

        <div class="ct-flow-list">
          <RouterLink v-for="(step, index) in QUY_TRINH_CHUONG_TRINH" :key="step.label" :to="step.to" class="ct-flow-item">
            <i>{{ index + 1 }}</i>
            <div>
              <b>{{ step.label }}</b>
              <small>{{ step.hint }}</small>
            </div>
            <em>Tiếp</em>
          </RouterLink>
        </div>
      </section>

      <section class="ct-home-card">
        <div class="ct-card-title">
          <span>3</span>
          <div>
            <h2>Dữ liệu</h2>
            <p>Xem, thêm, sửa, xóa.</p>
          </div>
        </div>

        <div class="ct-data-summary">
          <div><b>{{ loading ? '...' : counts.chuongTrinh }}</b><small>Chương trình</small></div>
          <div><b>{{ loading ? '...' : counts.phienBan }}</b><small>Phiên bản</small></div>
          <div><b>{{ loading ? '...' : counts.monHoc }}</b><small>Môn học</small></div>
        </div>

        <div class="ct-data-list">
          <RouterLink v-for="item in DU_LIEU_CHUONG_TRINH" :key="item.to" :to="item.to">
            <b>{{ item.label }}</b>
            <small>{{ item.hint }}</small>
          </RouterLink>
        </div>
      </section>
    </div>
  </section>
</template>

<style scoped>
.ct-home{min-height:100%;padding:18px;background:#f5f7fb;color:#0f172a}.ct-home-head{display:flex;align-items:flex-start;justify-content:space-between;gap:14px;margin-bottom:16px;border:1px solid #e4eaf3;border-radius:18px;background:linear-gradient(135deg,#ffffff,#eef5ff);padding:18px 20px;box-shadow:0 14px 34px rgba(15,23,42,.06)}.ct-home-head p{margin:0 0 5px;color:#2563eb;font-size:12px;font-weight:900;text-transform:uppercase;letter-spacing:.08em}.ct-home-head h1{margin:0;color:#0f172a;font-size:30px;letter-spacing:-.04em}.ct-home-head span{display:block;margin-top:5px;color:#64748b;font-size:14px}.ct-home-action{display:inline-flex;align-items:center;justify-content:center;height:38px;border-radius:999px;background:#2563eb;color:#fff;padding:0 18px;text-decoration:none;font-size:14px;font-weight:850}.ct-home-grid{display:grid;grid-template-columns:minmax(360px,1.15fr) minmax(270px,.9fr) minmax(310px,1fr);gap:14px;align-items:start}.ct-home-card{border:1px solid #e4eaf3;border-radius:18px;background:#fff;padding:14px;box-shadow:0 12px 30px rgba(15,23,42,.05);min-width:0}.ct-card-title{display:flex;align-items:center;gap:10px;margin-bottom:12px}.ct-card-title>span{width:34px;height:34px;border-radius:12px;display:grid;place-items:center;background:#eff6ff;color:#1d4ed8;font-weight:950}.ct-card-title h2{margin:0;font-size:20px;letter-spacing:-.03em}.ct-card-title p{margin:2px 0 0;color:#64748b;font-size:13px}.ct-system-layout{display:grid;grid-template-columns:170px minmax(0,1fr);gap:10px}.ct-system-list{display:grid;gap:7px}.ct-system-item{display:grid;gap:2px;border:1px solid transparent;border-radius:12px;padding:10px;text-decoration:none;color:#334155;background:#f8fafc}.ct-system-item b{font-size:14px}.ct-system-item small{font-size:11px;color:#64748b;line-height:1.35}.ct-system-item:hover,.ct-system-item.active{border-color:#bfdbfe;background:#eff6ff;color:#1d4ed8}.ct-system-detail{border:1px solid #e5edf8;border-radius:14px;background:#fbfdff;padding:13px;min-height:280px}.ct-system-detail h3{margin:0;font-size:18px}.ct-system-detail p{margin:5px 0 12px;color:#64748b;font-size:13px;line-height:1.45}.ct-child-list{display:grid;gap:8px}.ct-child-list a{border:1px solid #e2e8f0;border-radius:11px;background:#fff;padding:9px 10px;text-decoration:none;color:#0f172a;font-weight:800;font-size:13px}.ct-child-list a:hover{border-color:#93c5fd;color:#1d4ed8}.ct-flow-list,.ct-data-list{display:grid;gap:8px}.ct-flow-item{display:grid;grid-template-columns:30px minmax(0,1fr) auto;align-items:center;gap:9px;border:1px solid #e2e8f0;border-radius:13px;background:#fff;padding:10px;text-decoration:none;color:#0f172a}.ct-flow-item:hover{border-color:#93c5fd;background:#f8fbff}.ct-flow-item i{width:30px;height:30px;border-radius:10px;display:grid;place-items:center;background:#f1f5f9;color:#2563eb;font-style:normal;font-weight:950}.ct-flow-item b,.ct-data-list b{display:block;font-size:14px}.ct-flow-item small,.ct-data-list small{display:block;margin-top:2px;color:#64748b;font-size:12px;line-height:1.35}.ct-flow-item em{font-style:normal;color:#2563eb;font-size:12px;font-weight:900}.ct-data-summary{display:grid;grid-template-columns:repeat(3,1fr);gap:8px;margin-bottom:12px}.ct-data-summary div{border:1px solid #e2e8f0;border-radius:13px;background:#f8fafc;padding:10px}.ct-data-summary b{display:block;font-size:22px;line-height:1.1}.ct-data-summary small{color:#64748b;font-size:11px;font-weight:800}.ct-data-list{max-height:516px;overflow:auto;padding-right:2px}.ct-data-list a{border:1px solid #e2e8f0;border-radius:13px;background:#fff;padding:10px;text-decoration:none;color:#0f172a}.ct-data-list a:hover{border-color:#93c5fd;background:#f8fbff;color:#1d4ed8}@media(max-width:1180px){.ct-home-grid{grid-template-columns:1fr}.ct-system-layout{grid-template-columns:220px minmax(0,1fr)}}@media(max-width:720px){.ct-home{padding:10px}.ct-home-head{display:grid}.ct-system-layout{grid-template-columns:1fr}.ct-data-summary{grid-template-columns:1fr}}
</style>
