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
  border: 1px solid #d1e7d9;
  border-radius: 10px;
  padding: 18px 20px;
  background: #fff;
  box-shadow: 0 1px 6px rgba(26,92,54,0.07);
  font-family: 'Roboto', Arial, sans-serif;
}

.tk-filter-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  margin-bottom: 16px;
}

.eyebrow {
  margin: 0 0 2px;
  color: #1a5c36;
  text-transform: uppercase;
  font-size: 11px;
  font-weight: 500;
  letter-spacing: 0.1em;
}

h2 {
  margin: 0;
  color: #1a2e1f;
  font-size: 16px;
  font-weight: 600;
  line-height: 1.3;
}

.ghost-button {
  border: 1px solid #d1e7d9;
  background: #fff;
  color: #1a5c36;
  border-radius: 6px;
  padding: 7px 14px;
  display: inline-flex;
  gap: 6px;
  align-items: center;
  font-size: 13px;
  font-weight: 500;
  cursor: pointer;
  transition: background 0.15s;
  font-family: 'Roboto', Arial, sans-serif;
}

.ghost-button:hover { background: #f0f9f3; }
.ghost-button:disabled { opacity: 0.5; cursor: not-allowed; }

.filter-grid {
  display: grid;
  grid-template-columns: repeat(12, 1fr);
  gap: 10px;
}

.field-card {
  grid-column: span 4;
  min-width: 0;
  border: 1px solid #e2ede6;
  background: #fafcfa;
  border-radius: 7px;
  padding: 10px 12px;
}

.field-card.highlight {
  border-color: #2d8a55;
  background: #f0f9f3;
}

.search-card { grid-column: span 4; }

.label {
  display: block;
  margin-bottom: 5px;
  color: #5a7a63;
  font-size: 11px;
  font-weight: 500;
  text-transform: uppercase;
  letter-spacing: 0.07em;
}

select, input {
  width: 100%;
  border: 0;
  outline: 0;
  background: transparent;
  color: #1a2e1f;
  font-size: 13px;
  font-weight: 500;
  font-family: 'Roboto', Arial, sans-serif;
}

select:disabled { color: #aab8ae; }

.filter-route {
  margin-top: 14px;
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 6px;
  font-size: 12px;
  font-weight: 400;
  color: #9ab3a0;
}

.filter-route span {
  padding: 4px 10px;
  border-radius: 4px;
  background: #f0f4f1;
  color: #7a9a82;
}

.filter-route span.done {
  color: #1a5c36;
  background: #d6f0e0;
  font-weight: 500;
}

.filter-route i {
  color: #c8d8cc;
  font-style: normal;
}

@media (max-width: 1100px) { .field-card { grid-column: span 6; } }
@media (max-width: 720px) {
  .tk-filter-head { flex-direction: column; align-items: flex-start; }
  .field-card { grid-column: span 12; }
}
</style>
