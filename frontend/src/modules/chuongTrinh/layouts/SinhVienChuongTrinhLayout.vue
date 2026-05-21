<template>
  <section class="dao-tao-chuong-trinh-layout">
    <div class="dao-tao-head">
      <div class="head-main">
        <h2>Đào tạo xem chương trình đào tạo</h2>
        <p class="flow-title">Luồng đang đi</p>
        <nav class="breadcrumb" aria-label="Đường dẫn chương trình đào tạo">
          <template v-for="(item, index) in breadcrumbItems" :key="item.key">
            <RouterLink v-if="item.to && index < breadcrumbItems.length - 1" class="breadcrumb-link" :to="item.to">{{ item.label }}</RouterLink>
            <span v-else class="breadcrumb-current">{{ item.label }}</span>
            <span v-if="index < breadcrumbItems.length - 1" class="breadcrumb-separator">›</span>
          </template>
        </nav>
      </div>
      <RouterLink class="home-link" :to="{ name: 'ChuongTrinh.DaoTaoNganhDaoTao' }">Về danh sách ngành</RouterLink>
    </div>
<!--    <DaoTaoFlowXuongSong />-->
    <RouterView />
  </section>
</template>
<script setup>
import { computed } from 'vue'
import DaoTaoFlowXuongSong from '../components/DaoTaoFlowXuongSong.vue'
import { useRoute } from 'vue-router'
const route = useRoute()
function text(value, fallback = '') { return Array.isArray(value) ? value[0] || fallback : value || fallback }
function label(prefix, ten, id) { const v = text(ten); return v ? `${prefix}: ${v}` : (id ? `${prefix} #${id}` : prefix) }
const breadcrumbItems = computed(() => {
  const q = route.query || {}; const p = route.params || {}
  const items = [{ key: 'nganh-list', label: 'Danh sách ngành', to: { name: 'ChuongTrinh.DaoTaoNganhDaoTao' } }]
  const nganhId = p.nganhId || q.nganhId
  if (nganhId || q.nganhTen) items.push({ key: 'nganh', label: label('Ngành', q.nganhTen, nganhId), to: nganhId ? { name: 'ChuongTrinh.DaoTaoChuongTrinhTheoNganh', params: { nganhId }, query: { nganhTen: q.nganhTen } } : null })
  const chuongTrinhId = p.chuongTrinhId || q.chuongTrinhId
  if (chuongTrinhId || q.chuongTrinhTen) items.push({ key: 'chuong-trinh', label: label('Chương trình', q.chuongTrinhTen, chuongTrinhId), to: chuongTrinhId ? { name: 'ChuongTrinh.DaoTaoVersionTheoChuongTrinh', params: { chuongTrinhId }, query: { nganhId: q.nganhId, nganhTen: q.nganhTen, chuongTrinhTen: q.chuongTrinhTen } } : null })
  const versionId = p.versionId || q.versionId
  if (versionId || q.versionTen) items.push({ key: 'version', label: label('Version', q.versionTen, versionId), to: versionId ? { name: 'ChuongTrinh.DaoTaoChiTietVersion', params: { versionId }, query: { ...q, versionTen: q.versionTen } } : null })

  const khungKyId = q.khungKyId
  const khungKyTen = q.khungKyTen || (q.khungKyId && !q.nhomKienThucId ? q.boLocTen : '')
  if (khungKyId || khungKyTen) {
    items.push({
      key: 'khung-ky',
      label: label('Kỳ', khungKyTen, khungKyId),
      to: versionId && khungKyId
        ? { name: 'ChuongTrinh.DaoTaoMonTheoVersion', params: { versionId }, query: { ...q, khungKyId, khungKyTen, boLocTen: khungKyTen || q.boLocTen } }
        : null
    })
  }

  if (route.name === 'ChuongTrinh.DaoTaoMonTheoVersion') items.push({ key: 'mon-list', label: q.nhomKienThucId ? (q.boLocTen ? `Môn học theo nhóm: ${q.boLocTen}` : 'Danh sách môn học theo nhóm') : 'Danh sách môn học trong kỳ' })
  if (p.chuongTrinhMonId || q.monTen) items.push({ key: 'mon-detail', label: label('Môn học', q.monTen, p.chuongTrinhMonId) })
  return items.filter((item) => item.label)
})
</script>
<style scoped>
.dao-tao-chuong-trinh-layout {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.dao-tao-head {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 12px;
  padding: 14px 16px;
  border: 1px solid #bfdbfe;
  border-radius: var(--radius);
  background: #eff6ff;
  box-shadow: 0 1px 2px rgba(37, 99, 235, 0.08);
}

.head-main {
  min-width: 0;
}

.dao-tao-head h2 {
  margin: 0;
  font-size: 18px;
  font-weight: 700;
  color: #1e3a8a;
}

.flow-title {
  margin: 4px 0 0;
  color: #1d4ed8;
  font-size: 12px;
  line-height: 1.45;
  font-weight: 700;
  text-transform: uppercase;
}

.breadcrumb {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 6px;
  margin-top: 10px;
  padding: 9px 10px;
  border: 1px solid #bfdbfe;
  border-radius: 4px;
  background: #dbeafe;
  color: #1e40af;
  font-size: 12px;
}

.breadcrumb-link,
.breadcrumb-current {
  max-width: 280px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.breadcrumb-link {
  color: #1d4ed8;
  font-weight: 700;
  text-decoration: none;
}

.breadcrumb-link:hover {
  text-decoration: underline;
}

.breadcrumb-current {
  color: #0f172a;
  font-weight: 700;
}

.breadcrumb-separator {
  color: #2563eb;
  font-weight: 700;
}

.home-link {
  flex: 0 0 auto;
  padding: 7px 10px;
  border: 1px solid #93c5fd;
  border-radius: var(--radius);
  background: #ffffff;
  color: #1e40af;
  font-size: 13px;
  font-weight: 700;
  text-decoration: none;
}

.home-link:hover {
  background: #dbeafe;
}

@media(max-width:760px) {
  .dao-tao-head {
    flex-direction: column;
  }
}
</style>
