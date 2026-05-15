<template>
  <section class="ct-screen">
    <div class="ct-shell">
      <header class="ct-page-head version-head">
        <div class="ct-head-main">
          <div class="ct-kicker">Phiên bản</div>
          <h1 class="ct-title">{{ title }}</h1>
          <p class="ct-desc">Hoàn thiện khung đào tạo, nội dung học, chuẩn đầu ra và điều kiện theo phiên bản.</p>
        </div>
        <div class="ct-actions">
          <RouterLink class="ct-btn light" :to="`/quy-trinh/chuong-trinh/${chuongTrinhId}`">Quay lại</RouterLink>
          <button class="ct-btn light" type="button" @click="openEditVersion">Sửa</button>
        </div>
      </header>

      <main class="ct-main">
        <div v-if="loading" class="ct-loading">Đang tải dữ liệu...</div>
        <div v-else-if="error" class="ct-error">{{ error }}</div>
        <template v-else>
          <section class="version-summary">
            <article><span>Mã phiên bản</span><b>{{ detail.maVersion || '—' }}</b></article>
            <article><span>Ngày áp dụng</span><b>{{ detail.ngayApDung || '—' }}</b></article>
            <article><span>Tín chỉ</span><b>{{ detail.tongTinChi || '—' }}</b></article>
            <article><span>Trạng thái</span><b>{{ detail.laHienHanh ? 'Hiện hành' : 'Lưu trữ' }}</b></article>
          </section>

          <section class="version-workspace">
            <aside class="version-steps">
              <div class="version-steps-head">
                <h2>Hoàn thiện</h2>
                <p>Chọn phần cần làm. Nội dung hiện ngay bên phải.</p>
              </div>
              <button
                v-for="step in steps"
                :key="step.key"
                type="button"
                :class="['version-step', { active: activeStep === step.key }]"
                @click="activeStep = step.key"
              >
                <b>{{ step.label }}</b>
                <small>{{ step.hint }}</small>
              </button>
            </aside>

            <section class="version-content">
              <header class="version-content-head">
                <div>
                  <span>{{ currentStep.group }}</span>
                  <h2>{{ currentStep.label }}</h2>
                  <p>{{ currentStep.description }}</p>
                </div>
                <div class="ct-actions">
                  <button v-if="activeStep === 'khung'" class="ct-btn light" type="button" @click="openCreateNhom">Thêm nhóm</button>
                  <button v-if="activeStep === 'khung'" class="ct-btn" type="button" @click="openCreateMon">Thêm môn</button>
                  <button v-else-if="currentResourceKey" class="ct-btn" type="button" @click="openCreateRelated(currentResourceKey)">Thêm</button>
                </div>
              </header>

              <div v-if="activeStep === 'khung'" class="khung-inline">
                <aside class="khung-groups">
                  <button :class="['khung-group',{active:!activeNhomId}]" type="button" @click="activeNhomId=''">
                    <b>Tất cả nhóm</b>
                    <span>{{ monRows.length }} môn</span>
                  </button>
                  <button
                    v-for="nhom in nhomRows"
                    :key="layIdBanGhi(nhom)"
                    :class="['khung-group',{active:String(activeNhomId)===String(layIdBanGhi(nhom))}]"
                    type="button"
                    @click="activeNhomId=layIdBanGhi(nhom)"
                  >
                    <b>{{ nhom.ten || nhom.tenNhom || nhom.ma || 'Nhóm kiến thức' }}</b>
                    <span>{{ demMonTheoNhom(layIdBanGhi(nhom)) }} môn</span>
                  </button>
                </aside>

                <div class="khung-table-card">
                  <div class="khung-table-head">
                    <div>
                      <h3>Môn trong chương trình</h3>
                      <p>Mở môn để quản lý syllabus, chương bài và tài liệu.</p>
                    </div>
                    <strong>{{ monRowsLoc.length }} môn</strong>
                  </div>
                  <div class="ct-table-wrap compact">
                    <table class="ct-table">
                      <thead>
                        <tr><th>Mã môn</th><th>Môn học</th><th>Nhóm</th><th>Tín chỉ</th><th>Giờ</th><th>Tính chất</th><th class="ct-right">Thao tác</th></tr>
                      </thead>
                      <tbody>
                        <tr v-for="mon in monRowsLoc" :key="layIdBanGhi(mon)">
                          <td><span class="ct-badge">{{ mon.maMonTrongCt || '—' }}</span></td>
                          <td class="ct-strong">{{ tenMonHoc(mon) }}</td>
                          <td>{{ tenNhom(mon.nhomKienThucId) }}</td>
                          <td>{{ mon.soTinChi || '—' }}</td>
                          <td>{{ mon.tongGio || '—' }}</td>
                          <td><span :class="['ct-badge', mon.batBuoc ? 'ok' : 'warn']">{{ mon.batBuoc ? 'Bắt buộc' : 'Tự chọn' }}</span></td>
                          <td class="ct-right">
                            <span class="ct-row-actions">
                              <RouterLink class="ct-btn small ghost" :to="`/quy-trinh/chuong-trinh/${chuongTrinhId}/phien-ban/${versionId}/mon/${layIdBanGhi(mon)}`">Mở</RouterLink>
                              <button class="ct-btn small light" type="button" @click="openEditMon(mon)">Sửa</button>
                              <button class="ct-btn small danger" type="button" @click="removeMon(mon)">Xóa</button>
                            </span>
                          </td>
                        </tr>
                        <tr v-if="!monRowsLoc.length"><td colspan="7" class="ct-empty">Chưa có môn trong nhóm này.</td></tr>
                      </tbody>
                    </table>
                  </div>
                </div>
              </div>

              <div v-else-if="activeStep === 'noiDung'" class="version-grid-list">
                <RelatedBlock title="Syllabus chương trình" :rows="rows.syllabusCt" resource-key="syllabus-chuong-trinh" @create="openCreateRelated" @edit="openEditRelated" @remove="removeRelated" />
                <RelatedBlock title="Syllabus môn học" :rows="rows.syllabusMon" resource-key="syllabus-mon-hoc" @create="openCreateRelated" @edit="openEditRelated" @remove="removeRelated" />
                <RelatedBlock title="Chương bài" :rows="rows.chuongBai" resource-key="syllabus-chuong-bai" @create="openCreateRelated" @edit="openEditRelated" @remove="removeRelated" />
                <RelatedBlock title="Tài liệu" :rows="rows.taiLieu" resource-key="syllabus-tai-lieu" @create="openCreateRelated" @edit="openEditRelated" @remove="removeRelated" />
              </div>

              <div v-else-if="activeStep === 'tuChon'" class="version-grid-list">
                <RelatedBlock title="Nhóm tự chọn" :rows="rows.nhomTuChon" resource-key="nhom-tu-chon" @create="openCreateRelated" @edit="openEditRelated" @remove="removeRelated" />
                <RelatedBlock title="Môn tự chọn" :rows="rows.monTuChon" resource-key="mon-tu-chon" @create="openCreateRelated" @edit="openEditRelated" @remove="removeRelated" />
              </div>

              <div v-else-if="activeStep === 'rangBuoc'" class="version-grid-list">
                <RelatedBlock title="Môn tiên quyết" :rows="rows.monTienQuyet" resource-key="mon-tien-quyet" @create="openCreateRelated" @edit="openEditRelated" @remove="removeRelated" />
                <RelatedBlock title="Điều kiện môn học" :rows="rows.dieuKienMon" resource-key="dieu-kien-mon-hoc" @create="openCreateRelated" @edit="openEditRelated" @remove="removeRelated" />
                <RelatedBlock title="Quy đổi điểm" :rows="rows.quyDoi" resource-key="quy-doi-diem" @create="openCreateRelated" @edit="openEditRelated" @remove="removeRelated" />
              </div>

              <div v-else class="ct-list clean-list">
                <article v-for="item in currentRows" :key="layIdBanGhi(item)" class="ct-list-item">
                  <b>{{ labelRelated(item) }}</b>
                  <div class="ct-muted">{{ item.noiDung || item.moTa || item.ten || item.ma || item.viTri || '—' }}</div>
                  <div class="ct-actions mini-actions">
                    <button class="ct-btn small light" type="button" @click="openEditRelated(currentResourceKey, item)">Sửa</button>
                    <button class="ct-btn small danger" type="button" @click="removeRelated(currentResourceKey, item)">Xóa</button>
                  </div>
                </article>
                <div v-if="!currentRows.length" class="ct-empty">Chưa có dữ liệu.</div>
              </div>
            </section>
          </section>
        </template>
      </main>
    </div>

    <BieuMauQuanLyModal v-model="versionForm" :open="versionModal" mode="edit" :fields="versionFields" :saving="saving" title="Phiên bản chương trình" @close="versionModal=false" @submit="saveVersion" />
    <BieuMauQuanLyModal v-model="relatedForm" :open="relatedModal" :mode="relatedMode" :fields="currentFields" :saving="saving" :title="currentTitle" @close="relatedModal=false" @submit="saveRelated" />
  </section>
</template>

<script setup>
import { computed, defineComponent, h, onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';
import './luongChuongTrinh.css';
import BieuMauQuanLyModal from '@/modules/chuongTrinh/components/modal/BieuMauQuanLyModal.vue';
import { CHUONG_TRINH_RESOURCE_MAP } from '@/modules/chuongTrinh/constants/trangThaiChuongTrinh.js';
import {
  API_LUONG_CHUONG_TRINH,
  capNhatBanGhi,
  layChiTietPhienBan,
  layDanhSachDieuKienMonHoc,
  layDanhSachDieuKienTotNghiep,
  layDanhSachMonHoc,
  layDanhSachMonTienQuyet,
  layDanhSachMonTrongChuongTrinh,
  layDanhSachMonTuChon,
  layDanhSachMucTieu,
  layDanhSachNangLucDauRa,
  layDanhSachNhomKienThuc,
  layDanhSachNhomTuChon,
  layDanhSachQuyDoiDiem,
  layDanhSachSyllabusChuongBai,
  layDanhSachSyllabusChuongTrinh,
  layDanhSachSyllabusMonHoc,
  layDanhSachSyllabusTaiLieu,
  layDanhSachViTriViecLam,
  layIdBanGhi,
  locTheoGiaTri,
  taoBanGhi,
  taoNhanMonHoc,
  taoNhanPhienBan,
  xoaBanGhi,
} from '@/modules/chuongTrinh/services/luongChuongTrinhDaoTaoService.js';

const RelatedBlock = defineComponent({
  name: 'RelatedBlock',
  props: { title: String, rows: { type: Array, default: () => [] }, resourceKey: String },
  emits: ['create', 'edit', 'remove'],
  setup(props, { emit }) {
    const label = (item) => item.ma || item.ten || item.loai || item.noiDung || item.viTri || `#${layIdBanGhi(item)}`;
    return () => h('section', { class: 'related-block' }, [
      h('header', [h('div', [h('h3', props.title), h('p', `${props.rows.length} bản ghi`)]), h('button', { class: 'ct-btn small light', type: 'button', onClick: () => emit('create', props.resourceKey) }, 'Thêm')]),
      props.rows.length
        ? h('div', { class: 'related-list' }, props.rows.map((item) => h('article', { class: 'related-item', key: layIdBanGhi(item) }, [
          h('b', label(item)),
          h('p', item.noiDung || item.moTa || item.ten || item.viTri || '—'),
          h('div', { class: 'ct-actions mini-actions' }, [
            h('button', { class: 'ct-btn small light', type: 'button', onClick: () => emit('edit', props.resourceKey, item) }, 'Sửa'),
            h('button', { class: 'ct-btn small danger', type: 'button', onClick: () => emit('remove', props.resourceKey, item) }, 'Xóa'),
          ]),
        ])))
        : h('div', { class: 'ct-empty' }, 'Chưa có dữ liệu.'),
    ]);
  },
});

const route = useRoute();
const chuongTrinhId = computed(() => route.params.chuongTrinhId);
const versionId = computed(() => route.params.versionId);
const detail = ref({});
const loading = ref(false);
const saving = ref(false);
const error = ref('');
const activeStep = ref('khung');
const activeNhomId = ref('');
const versionModal = ref(false);
const relatedModal = ref(false);
const relatedMode = ref('create');
const versionForm = ref({});
const relatedForm = ref({});
const relatedResourceKey = ref('');

const nhomRows = ref([]);
const monRows = ref([]);
const danhMucMon = ref([]);
const rows = ref({
  syllabusCt: [], syllabusMon: [], chuongBai: [], taiLieu: [], mucTieu: [], nangLuc: [], viecLam: [], totNghiep: [],
  nhomTuChon: [], monTuChon: [], monTienQuyet: [], dieuKienMon: [], quyDoi: [],
});

const steps = [
  { key: 'khung', label: 'Khung đào tạo', hint: 'Nhóm kiến thức và môn học', group: 'Khung', description: 'Sắp xếp nhóm kiến thức và môn học theo phiên bản.' },
  { key: 'noiDung', label: 'Nội dung học', hint: 'Syllabus, chương bài, tài liệu', group: 'Nội dung', description: 'Hoàn thiện syllabus chương trình, syllabus môn, chương bài và tài liệu.' },
  { key: 'mucTieu', label: 'Mục tiêu', hint: 'Mục tiêu đào tạo', group: 'Chuẩn đầu ra', description: 'Quản lý mục tiêu đào tạo của phiên bản.' , resource: 'muc-tieu-chuong-trinh' },
  { key: 'nangLuc', label: 'Năng lực', hint: 'Chuẩn đầu ra', group: 'Chuẩn đầu ra', description: 'Quản lý năng lực đầu ra theo phiên bản.' , resource: 'nang-luc-dau-ra' },
  { key: 'viecLam', label: 'Việc làm', hint: 'Vị trí sau đào tạo', group: 'Chuẩn đầu ra', description: 'Quản lý vị trí việc làm sau đào tạo.' , resource: 'vi-tri-viec-lam' },
  { key: 'totNghiep', label: 'Tốt nghiệp', hint: 'Điều kiện hoàn thành', group: 'Điều kiện', description: 'Quản lý điều kiện tốt nghiệp.' , resource: 'dieu-kien-tot-nghiep' },
  { key: 'tuChon', label: 'Tự chọn', hint: 'Nhóm và môn tự chọn', group: 'Tự chọn', description: 'Thiết lập nhóm tự chọn và môn tự chọn.' },
  { key: 'rangBuoc', label: 'Ràng buộc', hint: 'Tiên quyết, điều kiện, quy đổi', group: 'Ràng buộc', description: 'Thiết lập môn tiên quyết, điều kiện môn học và quy đổi điểm.' },
];
const currentStep = computed(() => steps.find((item) => item.key === activeStep.value) || steps[0]);
const currentResourceKey = computed(() => currentStep.value.resource || '');
const resourceToEndpoint = {
  'nhom-kien-thuc': API_LUONG_CHUONG_TRINH.NHOM_KIEN_THUC,
  'chuong-trinh-mon': API_LUONG_CHUONG_TRINH.MON_TRONG_CHUONG_TRINH,
  'muc-tieu-chuong-trinh': API_LUONG_CHUONG_TRINH.MUC_TIEU,
  'nang-luc-dau-ra': API_LUONG_CHUONG_TRINH.NANG_LUC_DAU_RA,
  'vi-tri-viec-lam': API_LUONG_CHUONG_TRINH.VI_TRI_VIEC_LAM,
  'dieu-kien-tot-nghiep': API_LUONG_CHUONG_TRINH.DIEU_KIEN_TOT_NGHIEP,
  'syllabus-chuong-trinh': API_LUONG_CHUONG_TRINH.SYLLABUS_CHUONG_TRINH,
  'syllabus-mon-hoc': API_LUONG_CHUONG_TRINH.SYLLABUS_MON_HOC,
  'syllabus-chuong-bai': API_LUONG_CHUONG_TRINH.SYLLABUS_CHUONG_BAI,
  'syllabus-tai-lieu': API_LUONG_CHUONG_TRINH.SYLLABUS_TAI_LIEU,
  'nhom-tu-chon': API_LUONG_CHUONG_TRINH.NHOM_TU_CHON,
  'mon-tu-chon': API_LUONG_CHUONG_TRINH.MON_TU_CHON,
  'mon-tien-quyet': API_LUONG_CHUONG_TRINH.MON_TIEN_QUYET,
  'dieu-kien-mon-hoc': API_LUONG_CHUONG_TRINH.DIEU_KIEN_MON_HOC,
  'quy-doi-diem': API_LUONG_CHUONG_TRINH.QUY_DOI_DIEM,
};
const keyToRows = {
  'muc-tieu-chuong-trinh': 'mucTieu',
  'nang-luc-dau-ra': 'nangLuc',
  'vi-tri-viec-lam': 'viecLam',
  'dieu-kien-tot-nghiep': 'totNghiep',
  'syllabus-chuong-trinh': 'syllabusCt',
  'syllabus-mon-hoc': 'syllabusMon',
  'syllabus-chuong-bai': 'chuongBai',
  'syllabus-tai-lieu': 'taiLieu',
  'nhom-tu-chon': 'nhomTuChon',
  'mon-tu-chon': 'monTuChon',
  'mon-tien-quyet': 'monTienQuyet',
  'dieu-kien-mon-hoc': 'dieuKienMon',
  'quy-doi-diem': 'quyDoi',
};
const title = computed(() => taoNhanPhienBan(detail.value));
const versionFields = CHUONG_TRINH_RESOURCE_MAP['chuong-trinh-version'].fields;
const currentRows = computed(() => currentResourceKey.value ? rows.value[keyToRows[currentResourceKey.value]] || [] : []);
const currentTitle = computed(() => CHUONG_TRINH_RESOURCE_MAP[relatedResourceKey.value]?.title || CHUONG_TRINH_RESOURCE_MAP[currentResourceKey.value]?.title || currentStep.value.label);
const currentFields = computed(() => CHUONG_TRINH_RESOURCE_MAP[relatedResourceKey.value]?.fields || CHUONG_TRINH_RESOURCE_MAP[currentResourceKey.value]?.fields || []);
const monRowsLoc = computed(() => activeNhomId.value ? locTheoGiaTri(monRows.value, 'nhomKienThucId', activeNhomId.value) : monRows.value);
const labelRelated = (item = {}) => item.ma || item.ten || item.loai || item.noiDung || item.moTa || item.viTri || `#${layIdBanGhi(item)}`;
const tenNhom = (id) => nhomRows.value.find((n) => String(layIdBanGhi(n)) === String(id))?.ten || id || '—';
const tenMonHoc = (mon) => { const dm = danhMucMon.value.find((x) => String(layIdBanGhi(x)) === String(mon.monHocId)); return dm ? taoNhanMonHoc({ ...dm, maMonTrongCt: mon.maMonTrongCt }) : taoNhanMonHoc(mon); };
const demMonTheoNhom = (id) => monRows.value.filter((mon) => String(mon.nhomKienThucId) === String(id)).length;
const filterVersion = (data) => locTheoGiaTri(data, 'chuongTrinhVersionId', versionId.value);

const loadData = async () => {
  loading.value = true; error.value = '';
  try {
    const [v, nhom, mon, dm, syllabusCt, syllabusMon, chuongBai, taiLieu, mt, nl, vl, dk, ntc, mtc, mtq, dkm, qd] = await Promise.all([
      layChiTietPhienBan(versionId.value), layDanhSachNhomKienThuc({ chuongTrinhVersionId: versionId.value }), layDanhSachMonTrongChuongTrinh({ chuongTrinhVersionId: versionId.value }), layDanhSachMonHoc(),
      layDanhSachSyllabusChuongTrinh({ chuongTrinhVersionId: versionId.value }), layDanhSachSyllabusMonHoc(), layDanhSachSyllabusChuongBai(), layDanhSachSyllabusTaiLieu(),
      layDanhSachMucTieu({ chuongTrinhVersionId: versionId.value }), layDanhSachNangLucDauRa({ chuongTrinhVersionId: versionId.value }), layDanhSachViTriViecLam({ chuongTrinhVersionId: versionId.value }), layDanhSachDieuKienTotNghiep({ chuongTrinhVersionId: versionId.value }),
      layDanhSachNhomTuChon({ chuongTrinhVersionId: versionId.value }), layDanhSachMonTuChon(), layDanhSachMonTienQuyet(), layDanhSachDieuKienMonHoc(), layDanhSachQuyDoiDiem(),
    ]);
    detail.value = v || {};
    nhomRows.value = filterVersion(nhom);
    monRows.value = filterVersion(mon);
    danhMucMon.value = dm || [];
    const monIds = new Set(monRows.value.map((item) => String(layIdBanGhi(item))));
    const syllabusMonRows = (syllabusMon || []).filter((item) => monIds.has(String(item.chuongTrinhMonId)));
    const syllabusIds = new Set(syllabusMonRows.map((item) => String(layIdBanGhi(item))));
    const nhomTuChonRows = filterVersion(ntc);
    const nhomTuChonIds = new Set(nhomTuChonRows.map((item) => String(layIdBanGhi(item))));
    rows.value = {
      syllabusCt: filterVersion(syllabusCt),
      syllabusMon: syllabusMonRows,
      chuongBai: (chuongBai || []).filter((item) => syllabusIds.has(String(item.syllabusMonId))),
      taiLieu: (taiLieu || []).filter((item) => syllabusIds.has(String(item.syllabusMonId))),
      mucTieu: filterVersion(mt), nangLuc: filterVersion(nl), viecLam: filterVersion(vl), totNghiep: filterVersion(dk),
      nhomTuChon: nhomTuChonRows,
      monTuChon: (mtc || []).filter((item) => !nhomTuChonIds.size || nhomTuChonIds.has(String(item.nhomId))),
      monTienQuyet: mtq || [], dieuKienMon: dkm || [], quyDoi: qd || [],
    };
  } catch (err) { error.value = err?.message || 'Không tải được chi tiết phiên bản.'; }
  finally { loading.value = false; }
};

const openEditVersion = () => { versionForm.value = { ...detail.value }; versionModal.value = true; };
const saveVersion = async () => { saving.value = true; try { await capNhatBanGhi(API_LUONG_CHUONG_TRINH.PHIEN_BAN, versionId.value, versionForm.value); versionModal.value = false; await loadData(); } finally { saving.value = false; } };
const openCreateNhom = () => { relatedResourceKey.value = 'nhom-kien-thuc'; relatedMode.value = 'create'; relatedForm.value = { chuongTrinhVersionId: Number(versionId.value) }; relatedModal.value = true; };
const openCreateMon = () => { relatedResourceKey.value = 'chuong-trinh-mon'; relatedMode.value = 'create'; relatedForm.value = { chuongTrinhVersionId: Number(versionId.value), nhomKienThucId: activeNhomId.value || '' }; relatedModal.value = true; };
const openEditMon = (item) => { relatedResourceKey.value = 'chuong-trinh-mon'; relatedMode.value = 'edit'; relatedForm.value = { ...item }; relatedModal.value = true; };
const openCreateRelated = (resourceKey = currentResourceKey.value) => { relatedResourceKey.value = resourceKey; relatedMode.value = 'create'; relatedForm.value = { chuongTrinhVersionId: Number(versionId.value) }; relatedModal.value = true; };
const openEditRelated = (resourceKey, item) => { relatedResourceKey.value = resourceKey; relatedMode.value = 'edit'; relatedForm.value = { ...item }; relatedModal.value = true; };
const saveRelated = async () => { const endpoint = resourceToEndpoint[relatedResourceKey.value] || API_LUONG_CHUONG_TRINH[relatedResourceKey.value]; saving.value = true; try { const id = layIdBanGhi(relatedForm.value); if (relatedMode.value === 'edit' && id) await capNhatBanGhi(endpoint, id, relatedForm.value); else await taoBanGhi(endpoint, relatedForm.value); relatedModal.value = false; await loadData(); } finally { saving.value = false; } };
const removeRelated = async (resourceKey, item) => { const endpoint = resourceToEndpoint[resourceKey]; const id = layIdBanGhi(item); if (endpoint && id && confirm('Xóa bản ghi này?')) { await xoaBanGhi(endpoint, id); await loadData(); } };
const removeMon = async (item) => removeRelated('chuong-trinh-mon', item);

onMounted(loadData);
</script>

<style scoped>
.version-head{align-items:center}.version-summary{display:grid;grid-template-columns:repeat(4,minmax(0,1fr));gap:12px}.version-summary article{border:1px solid #dfe7f2;border-radius:16px;background:#fff;padding:14px;display:grid;gap:6px}.version-summary span{font-size:11px;font-weight:850;text-transform:uppercase;color:#667085;letter-spacing:.06em}.version-summary b{font-size:18px;color:#101828}.version-workspace{display:grid;grid-template-columns:300px minmax(0,1fr);gap:14px;align-items:start}.version-steps,.version-content{border:1px solid #dfe7f2;border-radius:18px;background:#fff;box-shadow:0 12px 30px rgba(15,23,42,.05)}.version-steps{padding:12px;position:sticky;top:14px}.version-steps-head{padding:4px 6px 12px;border-bottom:1px solid #eef2f7;margin-bottom:8px}.version-steps-head h2{margin:0 0 4px;font-size:18px}.version-steps-head p{margin:0;color:#667085;font-size:12.5px;line-height:1.45}.version-step{width:100%;display:grid;gap:4px;border:0;background:transparent;text-align:left;border-radius:13px;padding:12px;cursor:pointer;color:#1d2939}.version-step b{font-size:13.5px}.version-step small{font-size:12px;color:#667085}.version-step:hover,.version-step.active{background:#eff6ff;color:#1d4ed8}.version-content{min-height:460px;overflow:hidden}.version-content-head{display:flex;align-items:flex-start;justify-content:space-between;gap:14px;padding:16px 18px;border-bottom:1px solid #eef2f7}.version-content-head span{font-size:11px;font-weight:850;text-transform:uppercase;letter-spacing:.08em;color:#2563eb}.version-content-head h2{margin:4px 0 3px;font-size:22px;letter-spacing:-.03em}.version-content-head p{margin:0;color:#667085;font-size:13px}.khung-inline{display:grid;grid-template-columns:280px minmax(0,1fr);min-height:390px}.khung-groups{border-right:1px solid #eef2f7;padding:12px;background:#fbfcfe}.khung-group{width:100%;border:1px solid transparent;background:transparent;border-radius:12px;display:flex;align-items:center;justify-content:space-between;gap:10px;text-align:left;padding:12px;cursor:pointer;color:#1d2939}.khung-group b{font-size:13.5px}.khung-group span{font-size:12px;color:#667085;white-space:nowrap}.khung-group:hover,.khung-group.active{background:#eff6ff;border-color:#cfe0ff;color:#1d4ed8}.khung-table-card{min-width:0;padding:14px}.khung-table-head{display:flex;justify-content:space-between;align-items:flex-start;gap:14px;margin-bottom:12px}.khung-table-head h3{margin:0 0 4px;font-size:17px}.khung-table-head p{margin:0;color:#667085;font-size:12.5px}.khung-table-head strong{height:28px;border-radius:999px;background:#f2f4f7;display:inline-flex;align-items:center;padding:0 10px;font-size:12px}.compact{border:1px solid #eef2f7;border-radius:14px;overflow:auto}.version-grid-list{display:grid;grid-template-columns:repeat(2,minmax(0,1fr));gap:14px;padding:14px}.related-block{border:1px solid #e4e7ec;border-radius:16px;background:#fff;min-height:190px;padding:14px}.related-block header{display:flex;align-items:flex-start;justify-content:space-between;gap:10px;margin-bottom:10px}.related-block h3{margin:0 0 4px;font-size:16px}.related-block p{margin:0;color:#667085;font-size:12.5px}.related-list{display:grid;gap:8px}.related-item{border:1px solid #eef2f7;border-radius:12px;background:#fbfcfe;padding:10px}.related-item b{font-size:13.5px}.related-item p{margin:5px 0 0;font-size:12.5px;color:#667085;line-height:1.45}.clean-list{padding:14px}.mini-actions{margin-top:10px}@media(max-width:1200px){.version-workspace{grid-template-columns:1fr}.version-steps{position:static}.version-summary{grid-template-columns:repeat(2,1fr)}.khung-inline{grid-template-columns:1fr}.khung-groups{border-right:0;border-bottom:1px solid #eef2f7}.version-grid-list{grid-template-columns:1fr}}@media(max-width:720px){.version-summary{grid-template-columns:1fr}.version-content-head{flex-direction:column}.ct-actions{width:100%;justify-content:flex-start;flex-wrap:wrap}}
</style>
