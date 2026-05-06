<template>
  <section class="crud-page">
    <header class="hero">
      <div>
        <p class="eyebrow">Module chương trình</p>
        <h1>{{ pageConfig.title }}</h1>
        <p class="hero-text">{{ pageConfig.subtitle }}</p>
      </div>

      <div class="hero-actions">
        <button class="ghost-btn" type="button" @click="reloadAll" :disabled="loading">Tải lại</button>
        <button class="primary-btn" type="button" @click="openCreate">Thêm mới</button>
      </div>
    </header>

    <section class="filters">
      <label class="field wide">
        <span>Tìm kiếm</span>
        <input v-model.trim="keyword" type="text" :placeholder="`Tìm trong ${pageConfig.title.toLowerCase()}...`" />
      </label>

      <label v-if="showProgramFilter" class="field">
        <span>Chương trình</span>
        <select v-model="filters.programId">
          <option value="">Tất cả</option>
          <option v-for="item in programs" :key="item.id" :value="String(item.id)">
            {{ programLabel(item) }}
          </option>
        </select>
      </label>

      <label v-if="showVersionFilter" class="field">
        <span>Version</span>
        <select v-model="filters.versionId">
          <option value="">Tất cả</option>
          <option v-for="item in filteredVersionOptions" :key="item.id" :value="String(item.id)">
            {{ versionLabel(item) }}
          </option>
        </select>
      </label>

      <label v-if="showCourseFilter" class="field">
        <span>Môn trong CT</span>
        <select v-model="filters.courseId">
          <option value="">Tất cả</option>
          <option v-for="item in filteredCourseOptions" :key="item.id" :value="String(item.id)">
            {{ courseLabel(item) }}
          </option>
        </select>
      </label>

      <label v-if="showSyllabusFilter" class="field">
        <span>Syllabus môn học</span>
        <select v-model="filters.syllabusMonId">
          <option value="">Tất cả</option>
          <option v-for="item in filteredSyllabusOptions" :key="item.id" :value="String(item.id)">
            {{ syllabusMonLabel(item) }}
          </option>
        </select>
      </label>
    </section>

    <section class="stats">
      <article class="stat-card">
        <span class="stat-label">Tuyến đang mở</span>
        <strong>{{ route.path }}</strong>
      </article>
      <article class="stat-card">
        <span class="stat-label">Tổng bản ghi</span>
        <strong>{{ records.length }}</strong>
      </article>
      <article class="stat-card">
        <span class="stat-label">Khớp bộ lọc</span>
        <strong>{{ filteredRecords.length }}</strong>
      </article>
    </section>

    <div v-if="error" class="feedback error">{{ error }}</div>
    <div v-if="success" class="feedback success">{{ success }}</div>

    <section class="content">
      <article class="table-panel">
        <div class="panel-head">
          <div>
            <p class="mini-label">Dữ liệu</p>
            <h2>{{ pageConfig.title }}</h2>
          </div>
        </div>

        <div class="table-wrap">
          <table>
            <thead>
            <tr>
              <th v-for="column in pageConfig.columns" :key="column.key">{{ column.label }}</th>
              <th class="actions-col">Thao tác</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="item in filteredRecords" :key="item.id">
              <td v-for="column in pageConfig.columns" :key="column.key">
                {{ formatCell(item, column) }}
              </td>
              <td class="row-actions">
                <button class="table-btn" type="button" @click="openEdit(item)">Sửa</button>
                <button class="table-btn danger" type="button" @click="removeItem(item)">Xóa</button>
              </td>
            </tr>
            <tr v-if="!filteredRecords.length">
              <td :colspan="pageConfig.columns.length + 1" class="empty-row">
                Không có dữ liệu phù hợp với bộ lọc hiện tại.
              </td>
            </tr>
            </tbody>
          </table>
        </div>
      </article>
    </section>

    <div v-if="showForm" class="modal-backdrop" @click.self="closeForm">
      <div class="modal">
        <div class="modal-head">
          <div>
            <p class="mini-label">Biểu mẫu</p>
            <h2>{{ editingId ? `Cập nhật ${pageConfig.title.toLowerCase()}` : `Thêm ${pageConfig.title.toLowerCase()}` }}</h2>
          </div>
          <button class="icon-close" type="button" @click="closeForm">Đóng</button>
        </div>

        <form class="form-grid" @submit.prevent="saveItem">
          <label
              v-for="field in pageConfig.fields"
              :key="field.key"
              class="field"
              :class="{ wide: field.wide, checkbox: field.type === 'checkbox' }"
          >
            <template v-if="field.type === 'checkbox'">
              <input v-model="form[field.key]" type="checkbox" />
              <span>{{ field.label }}</span>
            </template>

            <template v-else>
              <span>{{ field.label }}</span>

              <select
                  v-if="field.type === 'select'"
                  v-model="form[field.key]"
                  :required="field.required"
              >
                <option value="">Chọn {{ field.label.toLowerCase() }}</option>
                <option
                    v-for="option in getFieldOptions(field)"
                    :key="option.value"
                    :value="option.value"
                >
                  {{ option.label }}
                </option>
              </select>

              <textarea
                  v-else-if="field.type === 'textarea'"
                  v-model.trim="form[field.key]"
                  :required="field.required"
                  rows="4"
              />

              <input
                  v-else
                  v-model="form[field.key]"
                  :type="field.type === 'number' ? 'number' : field.type === 'date' ? 'date' : 'text'"
                  :step="field.type === 'number' ? '0.1' : undefined"
                  :required="field.required"
              />
            </template>
          </label>

          <div class="modal-actions wide">
            <button class="primary-btn" type="submit" :disabled="saving">
              {{ saving ? 'Đang lưu...' : 'Lưu dữ liệu' }}
            </button>
            <button class="ghost-btn" type="button" @click="closeForm">Hủy</button>
          </div>
        </form>
      </div>
    </div>
  </section>
</template>

<script setup>
import { computed, onMounted, reactive, ref, watch } from 'vue';
import { useRoute } from 'vue-router';
import { getAllLoaiChuongTrinh } from '@/api/daoTao/ApiRespone/LoaiChuongTrinhController.js';
import { getAllNganh } from '@/api/daoTao/ApiRespone/NganhController.js';
import { getAllTrinhDoDaoTao } from '@/api/daoTao/ApiRespone/TrinhDoDaoTaoController.js';
import { getAllKhungKy } from '@/api/daoTao/ApiRespone/KhungKyController.js';
import { getErrorMessage, matchKeyword, toNumberOrNull, unwrapApiList } from '@/api/apiResponse.js';
import { CHUONG_TRINH_DATA_SOURCES, CHUONG_TRINH_PAGE_CONFIG } from '@/config/chuongTrinh/chuongTrinhCrudConfig.js';

const route = useRoute();

const pageConfig = computed(() => CHUONG_TRINH_PAGE_CONFIG[route.meta.pageKey] || CHUONG_TRINH_PAGE_CONFIG['chuong-trinh']);

const loading = ref(false);
const saving = ref(false);
const error = ref('');
const success = ref('');
const keyword = ref('');
const showForm = ref(false);
const editingId = ref(null);

const filters = reactive({
  programId: '',
  versionId: '',
  courseId: '',
  syllabusMonId: '',
});

const form = reactive({});

const dataStore = reactive({
  programs: [],
  versions: [],
  syllabusPrograms: [],
  objectives: [],
  competencies: [],
  jobPositions: [],
  graduationConditions: [],
  subjects: [],
  groups: [],
  courses: [],
  prerequisites: [],
  electiveGroups: [],
  electiveCourses: [],
  syllabusSubjects: [],
  syllabusLessons: [],
  syllabusMaterials: [],
  courseConditions: [],
  scoreConversions: [],
  nganhList: [],
  trinhDoList: [],
  loaiChuongTrinhList: [],
  khungKyList: [],
});

const programs = computed(() => dataStore.programs);
const versions = computed(() => dataStore.versions);
const groups = computed(() => dataStore.groups);
const courses = computed(() => dataStore.courses);
const syllabusSubjects = computed(() => dataStore.syllabusSubjects);
const electiveGroups = computed(() => dataStore.electiveGroups);

const records = computed(() => dataStore[pageConfig.value.dataKey] || []);

const showProgramFilter = computed(() => ['program', 'version', 'course', 'syllabus'].includes(pageConfig.value.scope));
const showVersionFilter = computed(() => ['version', 'course', 'syllabus'].includes(pageConfig.value.scope));
const showCourseFilter = computed(() => ['course', 'syllabus'].includes(pageConfig.value.scope));
const showSyllabusFilter = computed(() => pageConfig.value.scope === 'syllabus');

const filteredVersionOptions = computed(() => {
  if (!filters.programId) return versions.value;
  return versions.value.filter((item) => Number(item.chuongTrinhId) === Number(filters.programId));
});

const filteredCourseOptions = computed(() => {
  if (!filters.versionId) return courses.value;
  return courses.value.filter((item) => Number(item.chuongTrinhVersionId) === Number(filters.versionId));
});

const filteredSyllabusOptions = computed(() => {
  if (!filters.courseId) return syllabusSubjects.value;
  return syllabusSubjects.value.filter((item) => Number(item.chuongTrinhMonId) === Number(filters.courseId));
});

const filteredRecords = computed(() => {
  const fields = pageConfig.value.searchFields || [];

  return records.value
      .filter((item) => applyScopeFilter(item))
      .filter((item) => matchKeyword(item, keyword.value, fields));
});

watch(() => route.meta.pageKey, () => {
  resetFilters();
  closeForm();
});

watch(() => filters.programId, (value) => {
  if (!value) {
    filters.versionId = '';
    filters.courseId = '';
    filters.syllabusMonId = '';
    return;
  }

  if (filters.versionId && !filteredVersionOptions.value.some((item) => Number(item.id) === Number(filters.versionId))) {
    filters.versionId = '';
  }
});

watch(() => filters.versionId, (value) => {
  if (!value) {
    filters.courseId = '';
    filters.syllabusMonId = '';
    return;
  }

  if (filters.courseId && !filteredCourseOptions.value.some((item) => Number(item.id) === Number(filters.courseId))) {
    filters.courseId = '';
  }
});

watch(() => filters.courseId, (value) => {
  if (!value) {
    filters.syllabusMonId = '';
    return;
  }

  if (filters.syllabusMonId && !filteredSyllabusOptions.value.some((item) => Number(item.id) === Number(filters.syllabusMonId))) {
    filters.syllabusMonId = '';
  }
});

onMounted(async () => {
  await reloadAll();
});

async function reloadAll() {
  loading.value = true;
  error.value = '';

  try {
    const [moduleResponses, nganhRes, trinhDoRes, loaiRes, khungKyRes] = await Promise.all([
      Promise.all(Object.values(CHUONG_TRINH_DATA_SOURCES).map((loader) => loader())),
      getAllNganh(),
      getAllTrinhDoDaoTao(),
      getAllLoaiChuongTrinh(),
      getAllKhungKy(),
    ]);

    Object.keys(CHUONG_TRINH_DATA_SOURCES).forEach((key, index) => {
      dataStore[key] = unwrapApiList(moduleResponses[index]);
    });

    dataStore.nganhList = unwrapApiList(nganhRes);
    dataStore.trinhDoList = unwrapApiList(trinhDoRes);
    dataStore.loaiChuongTrinhList = unwrapApiList(loaiRes);
    dataStore.khungKyList = unwrapApiList(khungKyRes);
  } catch (err) {
    error.value = getErrorMessage(err);
  } finally {
    loading.value = false;
  }
}

function resetFilters() {
  keyword.value = '';
  filters.programId = '';
  filters.versionId = '';
  filters.courseId = '';
  filters.syllabusMonId = '';
}

function applyScopeFilter(item) {
  const scope = pageConfig.value.scope;
  if (scope === 'none') return true;

  const programId = resolveProgramId(item);
  const versionId = resolveVersionId(item);
  const courseId = resolveCourseId(item);
  const syllabusMonId = resolveSyllabusMonId(item);

  if (showProgramFilter.value && filters.programId && Number(programId) !== Number(filters.programId)) return false;
  if (showVersionFilter.value && filters.versionId && Number(versionId) !== Number(filters.versionId)) return false;
  if (showCourseFilter.value && filters.courseId && Number(courseId) !== Number(filters.courseId)) return false;
  if (showSyllabusFilter.value && filters.syllabusMonId && Number(syllabusMonId) !== Number(filters.syllabusMonId)) return false;

  return true;
}

function resolveProgramId(item) {
  if (item.nganhId !== undefined && item.maChuongTrinh !== undefined) return item.id;
  if (item.chuongTrinhId !== undefined) return item.chuongTrinhId;
  if (item.chuongTrinhVersionId !== undefined) {
    return versions.value.find((version) => Number(version.id) === Number(item.chuongTrinhVersionId))?.chuongTrinhId ?? null;
  }
  if (item.chuongTrinhMonId !== undefined) {
    const course = courses.value.find((entry) => Number(entry.id) === Number(item.chuongTrinhMonId));
    return course ? resolveProgramId(course) : null;
  }
  if (item.syllabusMonId !== undefined) {
    const syllabus = syllabusSubjects.value.find((entry) => Number(entry.id) === Number(item.syllabusMonId));
    return syllabus ? resolveProgramId(syllabus) : null;
  }
  if (item.monId !== undefined) {
    const course = courses.value.find((entry) => Number(entry.id) === Number(item.monId));
    return course ? resolveProgramId(course) : null;
  }
  if (item.nhomId !== undefined) {
    const group = electiveGroups.value.find((entry) => Number(entry.id) === Number(item.nhomId));
    return group ? resolveProgramId(group) : null;
  }
  return null;
}

function resolveVersionId(item) {
  if (item.chuongTrinhVersionId !== undefined) return item.chuongTrinhVersionId;
  if (item.chuongTrinhId !== undefined) return item.id;
  if (item.chuongTrinhMonId !== undefined) {
    const course = courses.value.find((entry) => Number(entry.id) === Number(item.chuongTrinhMonId));
    return course?.chuongTrinhVersionId ?? null;
  }
  if (item.syllabusMonId !== undefined) {
    const syllabus = syllabusSubjects.value.find((entry) => Number(entry.id) === Number(item.syllabusMonId));
    return syllabus ? resolveVersionId(syllabus) : null;
  }
  if (item.monId !== undefined) {
    const course = courses.value.find((entry) => Number(entry.id) === Number(item.monId));
    return course?.chuongTrinhVersionId ?? null;
  }
  if (item.nhomId !== undefined) {
    const group = electiveGroups.value.find((entry) => Number(entry.id) === Number(item.nhomId));
    return group?.chuongTrinhVersionId ?? null;
  }
  return null;
}

function resolveCourseId(item) {
  if (item.chuongTrinhMonId !== undefined) return item.chuongTrinhMonId;
  if (item.monId !== undefined) return item.monId;
  if (item.id !== undefined && records.value === courses.value) return item.id;
  if (item.syllabusMonId !== undefined) {
    const syllabus = syllabusSubjects.value.find((entry) => Number(entry.id) === Number(item.syllabusMonId));
    return syllabus?.chuongTrinhMonId ?? null;
  }
  return null;
}

function resolveSyllabusMonId(item) {
  if (item.syllabusMonId !== undefined) return item.syllabusMonId;
  if (item.id !== undefined && records.value === syllabusSubjects.value) return item.id;
  return null;
}

function openCreate() {
  editingId.value = null;
  resetForm();
  applyFilterDefaultsToForm();
  showForm.value = true;
}

function openEdit(item) {
  editingId.value = item.id;
  resetForm();
  pageConfig.value.fields.forEach((field) => {
    form[field.key] = normalizeFormValue(item[field.key], field.type);
  });
  syncFiltersFromItem(item);
  showForm.value = true;
}

function closeForm() {
  showForm.value = false;
  editingId.value = null;
  resetForm();
}

function resetForm() {
  pageConfig.value.fields.forEach((field) => {
    form[field.key] = field.type === 'checkbox' ? false : '';
  });
}

function applyFilterDefaultsToForm() {
  if ('chuongTrinhId' in form && filters.programId) form.chuongTrinhId = filters.programId;
  if ('chuongTrinhVersionId' in form && filters.versionId) form.chuongTrinhVersionId = filters.versionId;
  if ('chuongTrinhMonId' in form && filters.courseId) form.chuongTrinhMonId = filters.courseId;
  if ('syllabusMonId' in form && filters.syllabusMonId) form.syllabusMonId = filters.syllabusMonId;
  if ('nhomId' in form) {
    const firstGroup = getFieldOptions({ optionKey: 'electiveGroup' })[0];
    if (firstGroup) form.nhomId = firstGroup.value;
  }
}

function syncFiltersFromItem(item) {
  const programId = resolveProgramId(item);
  const versionId = resolveVersionId(item);
  const courseId = resolveCourseId(item);
  const syllabusMonId = resolveSyllabusMonId(item);

  if (programId) filters.programId = String(programId);
  if (versionId) filters.versionId = String(versionId);
  if (courseId) filters.courseId = String(courseId);
  if (syllabusMonId) filters.syllabusMonId = String(syllabusMonId);
}

async function saveItem() {
  saving.value = true;
  error.value = '';
  success.value = '';

  try {
    const payload = buildPayload();
    if (editingId.value) {
      await pageConfig.value.update(editingId.value, payload);
      success.value = `Đã cập nhật ${pageConfig.value.title.toLowerCase()}.`;
    } else {
      await pageConfig.value.create(payload);
      success.value = `Đã thêm ${pageConfig.value.title.toLowerCase()}.`;
    }

    closeForm();
    await reloadAll();
  } catch (err) {
    error.value = getErrorMessage(err);
  } finally {
    saving.value = false;
  }
}

async function removeItem(item) {
  if (!item?.id) return;
  if (!window.confirm(`Xóa bản ghi trong ${pageConfig.value.title.toLowerCase()}?`)) return;

  try {
    await pageConfig.value.remove(item.id);
    success.value = `Đã xóa dữ liệu trong ${pageConfig.value.title.toLowerCase()}.`;
    await reloadAll();
  } catch (err) {
    error.value = getErrorMessage(err);
  }
}

function buildPayload() {
  return Object.fromEntries(
      pageConfig.value.fields
          .map((field) => [field.key, convertFieldValue(form[field.key], field.type)])
          .filter(([, value]) => value !== '' && value !== undefined)
  );
}

function normalizeFormValue(value, type) {
  if (type === 'checkbox') return Boolean(value);
  if (value === null || value === undefined) return '';
  return String(value);
}

function convertFieldValue(value, type) {
  if (type === 'checkbox') return Boolean(value);
  if (type === 'number') return toNumberOrNull(value);
  if (type === 'select') return toNumberOrNull(value) ?? value;
  if (type === 'date') return value || null;
  return String(value ?? '').trim();
}

function getFieldOptions(field) {
  switch (field.optionKey) {
    case 'nganh':
      return dataStore.nganhList.map((item) => ({ value: String(item.id), label: `${item.maNganh} - ${item.tenNganh}` }));
    case 'trinhDo':
      return dataStore.trinhDoList.map((item) => ({ value: String(item.id), label: `${item.maTrinhDo} - ${item.tenTrinhDo}` }));
    case 'loaiChuongTrinh':
      return dataStore.loaiChuongTrinhList.map((item) => ({ value: String(item.id), label: `${item.maLoai} - ${item.tenLoai}` }));
    case 'program':
      return programs.value.map((item) => ({ value: String(item.id), label: programLabel(item) }));
    case 'version':
      return filteredVersionOptions.value.map((item) => ({ value: String(item.id), label: versionLabel(item) }));
    case 'subject':
      return dataStore.subjects.map((item) => ({ value: String(item.id), label: subjectLabel(item) }));
    case 'khungKy':
      return dataStore.khungKyList.map((item) => ({ value: String(item.id), label: `${item.maKy} - ${item.tenKy}` }));
    case 'group':
      return groups.value
          .filter((item) => !filters.versionId || Number(item.chuongTrinhVersionId) === Number(filters.versionId))
          .map((item) => ({ value: String(item.id), label: groupLabel(item) }));
    case 'course':
      return filteredCourseOptions.value.map((item) => ({ value: String(item.id), label: courseLabel(item) }));
    case 'syllabusMon':
      return filteredSyllabusOptions.value.map((item) => ({ value: String(item.id), label: syllabusMonLabel(item) }));
    case 'electiveGroup':
      return electiveGroups.value
          .filter((item) => !filters.versionId || Number(item.chuongTrinhVersionId) === Number(filters.versionId))
          .map((item) => ({ value: String(item.id), label: electiveGroupLabel(item) }));
    case 'mucTieuLoai':
      return enumOptions(['chung', 'kien_thuc', 'ky_nang', 'nang_luc_tu_chu_trach_nhiem']);
    case 'nangLucLoai':
      return enumOptions(['co_ban', 'cot_loi', 'nang_cao']);
    case 'loaiNhom':
      return enumOptions(['chung', 'co_so', 'chuyen_mon', 'tu_chon']);
    case 'loaiHocPhanCt':
      return enumOptions(['bat_buoc', 'tu_chon']);
    case 'loaiHocPhanType':
      return enumOptions(['mon_hoc', 'mo_dun']);
    case 'monTienQuyetLoai':
      return enumOptions(['tien_quyet', 'song_hanh', 'hoc_truoc']);
    case 'donViDiem':
      return enumOptions(['thang_10', 'phan_tram']);
    case 'dieuKienMonLoai':
      return enumOptions(['phong_hoc', 'thiet_bi', 'hoc_lieu', 'dung_cu', 'nguyen_vat_lieu', 'khac']);
    case 'ketQuaQuyDoi':
      return enumOptions(['dat', 'khong_dat']);
    default:
      return [];
  }
}

function enumOptions(values) {
  return values.map((value) => ({ value, label: value.replaceAll('_', ' ') }));
}

function formatCell(item, column) {
  const value = item[column.key];

  switch (column.format) {
    case 'date':
      return formatDate(value);
    case 'number':
      return formatNumber(value);
    case 'boolean':
      return value ? 'Có' : 'Không';
    case 'program':
      return programLabelById(value);
    case 'version':
      return versionLabelById(value);
    case 'subject':
      return subjectLabelById(value);
    case 'group':
      return groupLabelById(value);
    case 'course':
      return courseLabelById(value);
    case 'syllabusMon':
      return syllabusMonLabelById(value);
    case 'nganh':
      return nganhLabelById(value);
    case 'trinhDo':
      return trinhDoLabelById(value);
    case 'loaiChuongTrinh':
      return loaiLabelById(value);
    case 'khungKy':
      return khungKyLabelById(value);
    case 'electiveGroup':
      return electiveGroupLabelById(value);
    default:
      return value ?? '-';
  }
}

function formatNumber(value) {
  if (value === null || value === undefined || value === '') return '-';
  return Number(value);
}

function formatDate(value) {
  if (!value) return '-';
  return new Date(value).toLocaleDateString('vi-VN');
}

function programLabel(item) {
  return `${item.maChuongTrinh || '---'} - ${item.tenChuongTrinh || 'Chưa có tên'}`;
}

function versionLabel(item) {
  return `${item.maVersion || '---'} - ${item.tenVersion || programLabelById(item.chuongTrinhId)}`;
}

function subjectLabel(item) {
  return `${item.maMon || '---'} - ${item.tenMon || 'Chưa có tên môn'}`;
}

function groupLabel(item) {
  return `${item.ma || '---'} - ${item.ten || 'Chưa có tên nhóm'}`;
}

function courseLabel(item) {
  const subject = dataStore.subjects.find((entry) => Number(entry.id) === Number(item.monHocId));
  return `${item.maMonTrongCt || '---'} - ${subject?.tenMon || 'Chưa có tên môn'}`;
}

function syllabusMonLabel(item) {
  const course = courses.value.find((entry) => Number(entry.id) === Number(item.chuongTrinhMonId));
  return `${course?.maMonTrongCt || '---'} - ${item.viTri || item.tinhChat || 'Syllabus môn học'}`;
}

function electiveGroupLabel(item) {
  return `${item.ten || 'Nhóm tự chọn'}${item.soMonChon ? ` (${item.soMonChon} môn)` : ''}`;
}

function programLabelById(id) {
  const item = programs.value.find((entry) => Number(entry.id) === Number(id));
  return item ? programLabel(item) : '-';
}

function versionLabelById(id) {
  const item = versions.value.find((entry) => Number(entry.id) === Number(id));
  return item ? versionLabel(item) : '-';
}

function subjectLabelById(id) {
  const item = dataStore.subjects.find((entry) => Number(entry.id) === Number(id));
  return item ? subjectLabel(item) : '-';
}

function groupLabelById(id) {
  const item = groups.value.find((entry) => Number(entry.id) === Number(id));
  return item ? groupLabel(item) : '-';
}

function courseLabelById(id) {
  const item = courses.value.find((entry) => Number(entry.id) === Number(id));
  return item ? courseLabel(item) : '-';
}

function syllabusMonLabelById(id) {
  const item = syllabusSubjects.value.find((entry) => Number(entry.id) === Number(id));
  return item ? syllabusMonLabel(item) : '-';
}

function nganhLabelById(id) {
  const item = dataStore.nganhList.find((entry) => Number(entry.id) === Number(id));
  return item ? `${item.maNganh} - ${item.tenNganh}` : '-';
}

function trinhDoLabelById(id) {
  const item = dataStore.trinhDoList.find((entry) => Number(entry.id) === Number(id));
  return item ? `${item.maTrinhDo} - ${item.tenTrinhDo}` : '-';
}

function loaiLabelById(id) {
  const item = dataStore.loaiChuongTrinhList.find((entry) => Number(entry.id) === Number(id));
  return item ? `${item.maLoai} - ${item.tenLoai}` : '-';
}

function khungKyLabelById(id) {
  const item = dataStore.khungKyList.find((entry) => Number(entry.id) === Number(id));
  return item ? `${item.maKy} - ${item.tenKy}` : '-';
}

function electiveGroupLabelById(id) {
  const item = electiveGroups.value.find((entry) => Number(entry.id) === Number(id));
  return item ? electiveGroupLabel(item) : '-';
}
</script>

<style scoped>
.crud-page {
  padding: 24px;
  display: grid;
  gap: 18px;
  color: #123046;
}

.hero,
.filters,
.stats,
.table-panel,
.modal {
  background: rgba(255, 255, 255, 0.9);
  border: 1px solid rgba(18, 72, 104, 0.08);
  border-radius: 20px;
  box-shadow: 0 16px 40px rgba(19, 57, 79, 0.08);
}

.hero {
  display: flex;
  justify-content: space-between;
  gap: 16px;
  padding: 24px;
}

.eyebrow,
.mini-label {
  margin: 0 0 8px;
  font-size: 12px;
  font-weight: 800;
  letter-spacing: 0.08em;
  text-transform: uppercase;
  color: #27739e;
}

.hero h1,
.panel-head h2,
.modal-head h2 {
  margin: 0;
}

.hero-text {
  max-width: 760px;
  margin: 10px 0 0;
  line-height: 1.6;
  color: #4d6780;
}

.hero-actions,
.modal-actions,
.row-actions {
  display: flex;
  gap: 10px;
}

.filters,
.stats {
  padding: 18px;
}

.filters {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 14px;
}

.stats {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 14px;
}

.stat-card {
  padding: 16px;
  border-radius: 16px;
  background: linear-gradient(180deg, #f7fbfe 0%, #eef6fb 100%);
}

.stat-label {
  display: block;
  color: #587085;
  font-size: 12px;
}

.stat-card strong {
  display: block;
  margin-top: 8px;
  word-break: break-word;
}

.field {
  display: grid;
  gap: 8px;
}

.field span {
  font-size: 13px;
  font-weight: 700;
  color: #2e4b60;
}

.field input,
.field select,
.field textarea {
  width: 100%;
  border: 1px solid #c9d9e6;
  border-radius: 14px;
  padding: 11px 13px;
  font: inherit;
  background: #fff;
  color: #123046;
}

.field.wide {
  grid-column: 1 / -1;
}

.field.checkbox {
  grid-template-columns: auto 1fr;
  align-items: center;
}

.field.checkbox input {
  width: 18px;
  height: 18px;
}

.feedback {
  padding: 14px 16px;
  border-radius: 14px;
  font-weight: 600;
}

.feedback.error {
  background: #fff1f2;
  color: #b42318;
}

.feedback.success {
  background: #ecfdf3;
  color: #027a48;
}

.panel-head,
.modal-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}

.table-panel {
  padding: 20px;
}

.table-wrap {
  margin-top: 16px;
  overflow: auto;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th,
td {
  padding: 14px 12px;
  border-bottom: 1px solid #e5edf3;
  text-align: left;
  vertical-align: top;
}

th {
  font-size: 13px;
  color: #496176;
  background: #f7fbfe;
  position: sticky;
  top: 0;
}

.actions-col,
.row-actions {
  white-space: nowrap;
}

.empty-row {
  text-align: center;
  color: #60788c;
}

.primary-btn,
.ghost-btn,
.table-btn,
.icon-close {
  border: 0;
  border-radius: 12px;
  padding: 10px 14px;
  font: inherit;
  cursor: pointer;
}

.primary-btn {
  background: #0f766e;
  color: #fff;
}

.ghost-btn,
.table-btn,
.icon-close {
  background: #e9f3fa;
  color: #123046;
}

.table-btn.danger {
  background: #fff1f2;
  color: #b42318;
}

.modal-backdrop {
  position: fixed;
  inset: 0;
  background: rgba(10, 28, 40, 0.42);
  display: grid;
  place-items: center;
  padding: 20px;
  z-index: 30;
}

.modal {
  width: min(920px, 100%);
  max-height: 90vh;
  overflow: auto;
  padding: 22px;
}

.form-grid {
  margin-top: 18px;
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 14px;
}

.modal-actions {
  margin-top: 4px;
}

@media (max-width: 1100px) {
  .filters {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }

  .stats {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 720px) {
  .crud-page {
    padding: 16px;
  }

  .hero,
  .panel-head,
  .modal-head {
    flex-direction: column;
    align-items: stretch;
  }

  .filters,
  .form-grid {
    grid-template-columns: 1fr;
  }
}
</style>
