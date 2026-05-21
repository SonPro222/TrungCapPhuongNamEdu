<template>
  <section class="flow-xuong-song" aria-label="Luồng xương sống đào tạo xem">
    <div class="flow-head">
      <div>
        <h3>Luồng xương sống đào tạo xem</h3>
        <p>Phần trên là luồng chính. Phần dưới là các bảng cùng tầng để CRUD/xem chi tiết.</p>
      </div>
      <span class="flow-current">Đang ở: {{ activeLabel }}</span>
    </div>

    <div class="flow-body mini-scroll">
      <div class="flow-main-line">
        <div :class="['flow-node', { active: isActive('nganh') }]">1. nganh</div>
        <span class="flow-arrow">→</span>
        <div :class="['flow-node', { active: isActive('chuong_trinh') }]">2. chuong_trinh</div>
        <span class="flow-arrow">→</span>
        <div :class="['flow-node', { active: isActive('chuong_trinh_version') }]">3. chuong_trinh_version</div>
        <span class="flow-arrow">→</span>
        <div :class="['flow-node', { active: isActive('khung_ky') }]">4. khung_ky</div>
        <span class="flow-arrow">→</span>
        <div :class="['flow-node', { active: isActive('chuong_trinh_mon') }]">5. chuong_trinh_mon</div>
        <span class="flow-arrow">→</span>
        <div :class="['flow-node', { active: isActive('syllabus_mon_hoc') }]">6. syllabus_mon_hoc</div>
      </div>

      <div class="flow-tang-grid">
        <div class="flow-tang">
          <h4>Tầng chuong_trinh</h4>
          <div class="branch-list">
            <span>trinh_do_dao_tao</span>
            <span>loai_chuong_trinh</span>
          </div>
        </div>

        <div class="flow-tang">
          <h4>Tầng chuong_trinh_version</h4>
          <div class="branch-list">
            <span :class="{ active: isActive('khung_ky') }">khung_ky</span>
            <span :class="{ active: isActive('nhom_kien_thuc') }">nhom_kien_thuc</span>
            <span :class="{ active: isActive('syllabus_chuong_trinh') }">syllabus_chuong_trinh</span>
            <span>nhom_tu_chon → mon_tu_chon</span>
            <span>muc_tieu_chuong_trinh_goc → chuong_trinh_version_muc_tieu</span>
            <span>nang_luc_dau_ra_goc → chuong_trinh_version_nang_luc</span>
            <span>vi_tri_viec_lam_goc → chuong_trinh_version_vi_tri_viec_lam</span>
            <span>dieu_kien_tot_nghiep_goc → chuong_trinh_version_dieu_kien_tot_nghiep</span>
          </div>
        </div>

        <div class="flow-tang">
          <h4>Tầng chuong_trinh_mon</h4>
          <div class="branch-list">
            <span>mon_hoc</span>
            <span>khung_ky</span>
            <span>nhom_kien_thuc</span>
            <span>mon_tien_quyet: mon_hien_tai + mon_dieu_kien</span>
            <span>quy_doi_diem</span>
            <span>quy_doi_diem_mau → chuong_trinh_mon_quy_doi_diem_mau</span>
            <span>mon_tu_chon</span>
          </div>
        </div>

        <div class="flow-tang">
          <h4>Tầng syllabus_mon_hoc</h4>
          <div class="branch-list">
            <span>dieu_kien_mon_hoc</span>
            <span>syllabus_chuong_bai</span>
            <span>syllabus_tai_lieu</span>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()

const activeKey = computed(() => {
  const name = String(route.name || '')
  if (name.includes('DaoTaoChuongTrinhTheoNganh')) return 'chuong_trinh'
  if (name.includes('DaoTaoVersionTheoChuongTrinh')) return 'chuong_trinh_version'
  if (name.includes('DaoTaoChiTietVersion')) return 'khung_ky'
  if (name.includes('DaoTaoMonTheoVersion')) return 'chuong_trinh_mon'
  if (name.includes('DaoTaoChiTietMon')) return 'syllabus_mon_hoc'
  return 'nganh'
})

const labels = {
  nganh: 'Ngành',
  chuong_trinh: 'Chương trình đào tạo',
  chuong_trinh_version: 'Version chương trình',
  khung_ky: 'Chi tiết version',
  nhom_kien_thuc: 'Nhóm kiến thức',
  syllabus_chuong_trinh: 'Syllabus chương trình',
  chuong_trinh_mon: 'Chương trình môn',
  syllabus_mon_hoc: 'Syllabus môn học'
}

const activeLabel = computed(() => labels[activeKey.value] || 'Đào tạo xem')

function isActive(key) {
  if (activeKey.value === key) return true
  if (activeKey.value === 'khung_ky' && ['khung_ky', 'nhom_kien_thuc', 'syllabus_chuong_trinh'].includes(key)) return true
  return false
}
</script>

<style scoped>
.flow-xuong-song {
  border: 1px solid #bfdbfe;
  border-radius: 6px;
  background: #eff6ff;
  color: #0f172a;
  overflow: hidden;
}

.flow-head {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 12px;
  padding: 10px 12px;
  border-bottom: 1px solid #bfdbfe;
  background: #dbeafe;
}

.flow-head h3 {
  margin: 0;
  color: #1e3a8a;
  font-size: 15px;
  font-weight: 800;
}

.flow-head p {
  margin: 4px 0 0;
  color: #1d4ed8;
  font-size: 12px;
}

.flow-current {
  flex: 0 0 auto;
  border: 1px solid #93c5fd;
  border-radius: 999px;
  background: #ffffff;
  color: #1e40af;
  padding: 5px 10px;
  font-size: 12px;
  font-weight: 800;
}

.flow-body {
  padding: 10px 12px 12px;
  overflow-x: auto;
}

.flow-main-line {
  display: flex;
  align-items: stretch;
  gap: 8px;
  min-width: 900px;
}

.flow-node {
  min-width: 140px;
  border: 1px solid #cbd5e1;
  border-radius: 5px;
  background: #ffffff;
  color: #334155;
  padding: 8px 10px;
  font-size: 12px;
  font-weight: 800;
  text-align: center;
}

.flow-node small {
  display: block;
  margin-top: 3px;
  color: #64748b;
  font-size: 10px;
  font-weight: 700;
}

.flow-node.active {
  border-color: #2563eb;
  background: #2563eb;
  color: #ffffff;
}

.flow-node.active small {
  color: #dbeafe;
}

.flow-arrow {
  display: flex;
  align-items: center;
  color: #1d4ed8;
  font-weight: 900;
}

.flow-tang-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(220px, 1fr));
  gap: 8px;
  margin-top: 10px;
  min-width: 900px;
}

.flow-tang {
  border: 1px solid #bfdbfe;
  border-radius: 5px;
  background: #ffffff;
  padding: 8px;
}

.flow-tang h4 {
  margin: 0 0 6px;
  color: #1e40af;
  font-size: 12px;
  font-weight: 800;
}

.branch-list {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.branch-list span {
  border-left: 2px solid #bfdbfe;
  padding-left: 6px;
  color: #334155;
  font-size: 11px;
  line-height: 1.35;
}

.branch-list span.active {
  border-left-color: #2563eb;
  color: #1d4ed8;
  font-weight: 800;
}

.mini-scroll {
  scrollbar-width: thin;
  scrollbar-color: #93c5fd #eff6ff;
}

.mini-scroll::-webkit-scrollbar {
  height: 6px;
  width: 6px;
}

.mini-scroll::-webkit-scrollbar-thumb {
  background: #93c5fd;
  border-radius: 999px;
}

.mini-scroll::-webkit-scrollbar-track {
  background: #eff6ff;
}

@media(max-width: 900px) {
  .flow-head {
    flex-direction: column;
  }
}
</style>
