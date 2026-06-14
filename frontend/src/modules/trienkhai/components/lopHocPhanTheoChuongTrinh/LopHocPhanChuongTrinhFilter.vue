<template>
  <section class="tk-filter-shell">
    <div class="tk-filter-head">
      <div>
        <p class="eyebrow">Bộ lọc triển khai</p>
        <h2>Đi từ ngành đến lớp học phần</h2>
      </div>
      <button class="ghost-button" type="button" @click="$emit('refresh')" :disabled="loading">
        <span>↻</span>
        Tải lại
      </button>
    </div>

    <div class="filter-grid">
      <label class="field-card">
        <span class="label">Ngành</span>
        <select :value="modelValue.nganhId" @change="update('nganhId', $event.target.value)">
          <option value="">Chọn ngành</option>
          <option v-for="item in nganhs" :key="item.id" :value="item.id">
            {{ item.maNganh || item.ma || item.code }} - {{ item.tenNganh || item.ten || item.name }}
          </option>
        </select>
      </label>

      <label class="field-card">
        <span class="label">Ngành hệ đào tạo</span>
        <select :value="modelValue.nganhHeDaoTaoId" :disabled="!modelValue.nganhId || loadingHe" @change="update('nganhHeDaoTaoId', $event.target.value)">
          <option value="">Chọn hệ đào tạo</option>
          <option v-for="item in nganhHeDaoTaos" :key="item.id" :value="item.id">
            {{ hienThiHeDaoTao(item) }}
          </option>
        </select>
      </label>

      <label class="field-card">
        <span class="label">Chương trình</span>
        <select :value="modelValue.chuongTrinhId" :disabled="!modelValue.nganhHeDaoTaoId || loadingChuongTrinh" @change="update('chuongTrinhId', $event.target.value)">
          <option value="">Chọn chương trình</option>
          <option v-for="item in chuongTrinhs" :key="item.id" :value="item.id">
            {{ item.maChuongTrinh || item.ma || 'CT' + item.id }} - {{ item.tenChuongTrinh || item.ten || item.name }}
          </option>
        </select>
      </label>

      <label class="field-card highlight">
        <span class="label">Version hiện hành</span>
        <select :value="modelValue.versionId" :disabled="!versions.length || loadingVersion" @change="update('versionId', $event.target.value)">
          <option value="">Chọn version</option>
          <option v-for="item in versions" :key="item.id" :value="item.id">
            {{ (item.maVersion || item.tenVersion || item.version || 'Version ' + item.id) + (item.laHienHanh ? ' - hiện hành' : '') }}
          </option>
        </select>
      </label>

      <label class="field-card">
        <span class="label">Kỳ học</span>
        <select :value="modelValue.khungKyId" :disabled="!modelValue.versionId || loadingKy" @change="update('khungKyId', $event.target.value)">
          <option value="">Chọn kỳ để tải lớp</option>
          <option v-for="item in khungKys" :key="item.id" :value="item.id">
            {{ item.tenKy || item.tenKhungKy || ('Kỳ ' + (item.soThuTu || item.thuTu || item.id)) }}
          </option>
        </select>
      </label>

      <label class="field-card search-card">
        <span class="label">Tìm lớp / môn</span>
        <input :value="modelValue.keyword" type="search" placeholder="Nhập mã lớp, tên lớp, tên môn..." @input="update('keyword', $event.target.value)" />
      </label>
    </div>

    <div class="filter-route">
      <span :class="{ done: modelValue.nganhId }">Ngành</span>
      <i>→</i>
      <span :class="{ done: modelValue.nganhHeDaoTaoId }">Hệ</span>
      <i>→</i>
      <span :class="{ done: modelValue.chuongTrinhId }">Chương trình</span>
      <i>→</i>
      <span :class="{ done: modelValue.versionId }">Version</span>
      <i>→</i>
      <span :class="{ done: modelValue.khungKyId }">Kỳ</span>
      <i>→</i>
      <span :class="{ done: coTheTaiLop }">Lớp học phần</span>
    </div>
  </section>
</template>

<script setup>
const props = defineProps({
  modelValue: { type: Object, required: true },
  nganhs: { type: Array, default: () => [] },
  nganhHeDaoTaos: { type: Array, default: () => [] },
  chuongTrinhs: { type: Array, default: () => [] },
  versions: { type: Array, default: () => [] },
  khungKys: { type: Array, default: () => [] },
  loading: Boolean,
  loadingHe: Boolean,
  loadingChuongTrinh: Boolean,
  loadingVersion: Boolean,
  loadingKy: Boolean,
  coTheTaiLop: Boolean
})

const emit = defineEmits(['update:modelValue', 'change', 'refresh'])

function update(key, value) {
  const next = { ...props.modelValue, [key]: value }
  emit('update:modelValue', next)
  emit('change', { key, value, model: next })
}

function hienThiHeDaoTao(item) {
  const trinhDo = item.tenTrinhDo || item.trinhDoDaoTaoTen || item.trinhDoDaoTao?.ten || item.trinhDo?.ten || item.trinhDo
  const loai = item.tenLoaiChuongTrinh || item.loaiChuongTrinhTen || item.loaiChuongTrinh?.ten || item.loaiChuongTrinh
  const thang = item.soThangDaoTao || item.soThang || item.thoiGianDaoTao
  const ky = item.soKy || item.soHocKy
  return [trinhDo, loai, thang ? `${thang} tháng` : '', ky ? `${ky} kỳ` : ''].filter(Boolean).join(' - ') || item.ten || `Hệ ${item.id}`
}
</script>

<style scoped>
.tk-filter-shell {
  position: relative;
  overflow: hidden;
  border: 1px solid rgba(15, 23, 42, .08);
  border-radius: 28px;
  padding: 24px;
  background:
    radial-gradient(circle at top left, rgba(59, 130, 246, .18), transparent 34%),
    linear-gradient(135deg, #ffffff 0%, #f8fbff 48%, #f4f7ff 100%);
  box-shadow: 0 22px 60px rgba(15, 23, 42, .08);
}
.tk-filter-head { display: flex; align-items: flex-start; justify-content: space-between; gap: 18px; margin-bottom: 22px; }
.eyebrow { margin: 0 0 6px; color: #2563eb; text-transform: uppercase; font-size: 12px; font-weight: 800; letter-spacing: .12em; }
h2 { margin: 0; color: #0f172a; font-size: 28px; line-height: 1.15; }
.ghost-button { border: 1px solid rgba(37, 99, 235, .2); background: rgba(255,255,255,.75); color: #1d4ed8; border-radius: 999px; padding: 10px 16px; display: inline-flex; gap: 8px; align-items: center; font-weight: 800; cursor: pointer; box-shadow: 0 10px 24px rgba(37,99,235,.08); }
.ghost-button:disabled { opacity: .55; cursor: not-allowed; }
.filter-grid { display: grid; grid-template-columns: repeat(12, 1fr); gap: 14px; }
.field-card { grid-column: span 4; min-width: 0; border: 1px solid rgba(148, 163, 184, .28); background: rgba(255,255,255,.82); border-radius: 20px; padding: 12px 14px; box-shadow: 0 12px 34px rgba(15,23,42,.04); }
.field-card.highlight { border-color: rgba(37, 99, 235, .25); background: linear-gradient(180deg, rgba(239,246,255,.95), rgba(255,255,255,.9)); }
.search-card { grid-column: span 4; }
.label { display: block; margin-bottom: 8px; color: #64748b; font-size: 12px; font-weight: 900; text-transform: uppercase; letter-spacing: .08em; }
select, input { width: 100%; border: 0; outline: 0; background: transparent; color: #0f172a; font-size: 14px; font-weight: 700; }
select:disabled { color: #94a3b8; }
.filter-route { margin-top: 18px; display: flex; flex-wrap: wrap; align-items: center; gap: 8px; color: #94a3b8; font-size: 13px; font-weight: 900; }
.filter-route span { padding: 7px 11px; border-radius: 999px; background: rgba(148,163,184,.13); }
.filter-route span.done { color: #047857; background: rgba(16,185,129,.13); }
.filter-route i { color: #cbd5e1; font-style: normal; }
@media (max-width: 1100px) { .field-card { grid-column: span 6; } }
@media (max-width: 720px) { .tk-filter-head { flex-direction: column; } .field-card { grid-column: span 12; } }
</style>
