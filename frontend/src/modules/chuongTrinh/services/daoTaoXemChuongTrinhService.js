import { daoTaoService } from '@/modules/daoTao/services/daoTaoService'
import { chuongTrinhService } from './chuongTrinhService'

export const crudServices = {
  nganh: daoTaoService.nganh,
  trinhDoDaoTao: daoTaoService.trinhDoDaoTao,
  loaiChuongTrinh: daoTaoService.loaiChuongTrinh,
  khungKy: daoTaoService.khungKy,

  chuongTrinh: chuongTrinhService.chuongTrinh,
  chuongTrinhVersion: chuongTrinhService.chuongTrinhVersion,
  nhomKienThuc: chuongTrinhService.nhomKienThuc,
  syllabusChuongTrinh: chuongTrinhService.syllabusChuongTrinh,
  nhomTuChon: chuongTrinhService.nhomTuChon,
  monTuChon: chuongTrinhService.monTuChon,
  chuongTrinhMon: chuongTrinhService.chuongTrinhMon,
  monHoc: chuongTrinhService.monHoc,
  monTienQuyet: chuongTrinhService.monTienQuyet,
  quyDoiDiem: chuongTrinhService.quyDoiDiem,
  quyDoiDiemMau: chuongTrinhService.quyDoiDiemMau,
  chuongTrinhMonQuyDoiDiemMau: chuongTrinhService.chuongTrinhMonQuyDoiDiemMau,
  syllabusMonHoc: chuongTrinhService.syllabusMonHoc,
  dieuKienMonHoc: chuongTrinhService.dieuKienMonHoc,
  syllabusChuongBai: chuongTrinhService.syllabusChuongBai,
  syllabusTaiLieu: chuongTrinhService.syllabusTaiLieu,

  mucTieuChuongTrinhGoc: chuongTrinhService.mucTieuChuongTrinhGoc,
  nangLucDauRaGoc: chuongTrinhService.nangLucDauRaGoc,
  viTriViecLamGoc: chuongTrinhService.viTriViecLamGoc,
  dieuKienTotNghiepGoc: chuongTrinhService.dieuKienTotNghiepGoc,
  chuongTrinhVersionMucTieu: chuongTrinhService.chuongTrinhVersionMucTieu,
  chuongTrinhVersionNangLuc: chuongTrinhService.chuongTrinhVersionNangLuc,
  chuongTrinhVersionViTriViecLam: chuongTrinhService.chuongTrinhVersionViTriViecLam,
  chuongTrinhVersionDieuKienTotNghiep: chuongTrinhService.chuongTrinhVersionDieuKienTotNghiep
}

export const luaChonDaoTao = {
  loaiMucTieu: [
    { id: 'chung', ten: 'Chung' },
    { id: 'kien_thuc', ten: 'Kiến thức' },
    { id: 'ky_nang', ten: 'Kỹ năng' },
    { id: 'nang_luc_tu_chu_trach_nhiem', ten: 'Năng lực tự chủ trách nhiệm' }
  ],
  loaiNangLuc: [
    { id: 'co_ban', ten: 'Cơ bản' },
    { id: 'cot_loi', ten: 'Cốt lõi' },
    { id: 'nang_cao', ten: 'Nâng cao' }
  ],
  loaiNhom: [
    { id: 'chung', ten: 'Chung' },
    { id: 'co_so', ten: 'Cơ sở' },
    { id: 'chuyen_mon', ten: 'Chuyên môn' },
    { id: 'tu_chon', ten: 'Tự chọn' }
  ],
  loaiMon: [
    { id: 'bat_buoc', ten: 'Bắt buộc' },
    { id: 'tu_chon', ten: 'Tự chọn' }
  ],
  loaiHocPhan: [
    { id: 'mon_hoc', ten: 'Môn học' },
    { id: 'mo_dun', ten: 'Mô đun' }
  ],
  loaiDieuKien: [
    { id: 'tien_quyet', ten: 'Tiên quyết' },
    { id: 'song_hanh', ten: 'Song hành' },
    { id: 'hoc_truoc', ten: 'Học trước' }
  ],
  ketQua: [
    { id: 'dat', ten: 'Đạt' },
    { id: 'khong_dat', ten: 'Không đạt' }
  ],
  donViDiem: [
    { id: 'thang_10', ten: 'Thang 10' },
    { id: 'phan_tram', ten: 'Phần trăm' }
  ],
  loaiDieuKienMonHoc: [
    { id: 'phong_hoc', ten: 'Phòng học' },
    { id: 'thiet_bi', ten: 'Thiết bị' },
    { id: 'hoc_lieu', ten: 'Học liệu' },
    { id: 'dung_cu', ten: 'Dụng cụ' },
    { id: 'nguyen_vat_lieu', ten: 'Nguyên vật liệu' },
    { id: 'khac', ten: 'Khác' }
  ],
  loaiTaiLieu: [
    { id: 'GIAO_TRINH', ten: 'Giáo trình' },
    { id: 'TAI_LIEU_THAM_KHAO', ten: 'Tài liệu tham khảo' },
    { id: 'BAI_GIANG', ten: 'Bài giảng' },
    { id: 'KHAC', ten: 'Khác' }
  ]
}

export function layItems(result) {
  if (Array.isArray(result)) return result
  if (Array.isArray(result?.items)) return result.items
  if (Array.isArray(result?.content)) return result.content
  if (Array.isArray(result?.data?.content)) return result.data.content
  if (Array.isArray(result?.data)) return result.data
  return []
}

export async function taiBang(key, params = {}) {
  const service = crudServices[key]
  if (!service?.getAll) return []
  const res = await service.getAll({ size: 1000, ...params })
  return layItems(res)
}

export async function taiNhieuBang(keys = []) {
  const pairs = await Promise.all(keys.map(async (key) => [key, await taiBang(key).catch(() => [])]))
  return Object.fromEntries(pairs)
}

export function taoMap(list = []) {
  return new Map(list.map((item) => [item.id, item]))
}

export function sapXepTheoThuTu(list = []) {
  return [...list].sort((a, b) => Number(a.thuTu ?? 999999) - Number(b.thuTu ?? 999999) || Number(a.id ?? 0) - Number(b.id ?? 0))
}

export function tenCua(item, keys = ['ten', 'tenNganh', 'tenChuongTrinh', 'tenVersion', 'tenMon', 'tenKy', 'tenNhom', 'noiDung', 'ma']) {
  if (!item) return '-'
  for (const key of keys) {
    if (item[key]) return item[key]
  }
  return item.id || '-'
}

export function withNames(rows = [], maps = {}, defs = []) {
  return rows.map((row) => {
    const out = { ...row }
    defs.forEach((def) => {
      const item = maps[def.map]?.get(row[def.idKey])
      out[def.nameKey] = tenCua(item, def.keys)
    })
    return out
  })
}

const number01 = { type: 'number', step: 0.1, min: 0, max: 999999.9 }
const number6 = { type: 'number', step: 0.1, min: 0, max: 9999.9 }
const numberPoint = { type: 'number', step: 0.01, min: 0, max: 999.99 }
const thuTu = { key: 'thuTu', label: 'Thứ tự', type: 'number', step: 1, min: 0 }

export const configs = {
  nganh: {
    title: 'Ngành',
    defaultForm: { maNganh: '', tenNganh: '', moTa: '' },
    fields: [
      { key: 'maNganh', label: 'Mã ngành', required: true },
      { key: 'tenNganh', label: 'Tên ngành', required: true },
      { key: 'moTa', label: 'Mô tả', type: 'textarea', wide: true }
    ],
    columns: [
      { key: 'maNganh', label: 'Mã ngành' },
      { key: 'tenNganh', label: 'Tên ngành' },
      { key: 'moTa', label: 'Mô tả' }
    ]
  },
  trinhDoDaoTao: {
    title: 'Trình độ đào tạo',
    defaultForm: { maTrinhDo: '', tenTrinhDo: '', moTa: '' },
    fields: [
      { key: 'maTrinhDo', label: 'Mã trình độ', required: true },
      { key: 'tenTrinhDo', label: 'Tên trình độ', required: true },
      { key: 'moTa', label: 'Mô tả', type: 'textarea', wide: true }
    ],
    columns: [
      { key: 'maTrinhDo', label: 'Mã' },
      { key: 'tenTrinhDo', label: 'Tên trình độ' },
      { key: 'moTa', label: 'Mô tả' }
    ]
  },
  loaiChuongTrinh: {
    title: 'Loại chương trình',
    defaultForm: { maLoai: '', tenLoai: '', soThang: null, soKy: null, moTa: '' },
    fields: [
      { key: 'maLoai', label: 'Mã loại', required: true },
      { key: 'tenLoai', label: 'Tên loại', required: true },
      { key: 'soThang', label: 'Số tháng', type: 'number', step: 1, min: 0 },
      { key: 'soKy', label: 'Số kỳ', type: 'number', step: 1, min: 0 },
      { key: 'moTa', label: 'Mô tả', type: 'textarea', wide: true }
    ],
    columns: [
      { key: 'maLoai', label: 'Mã' },
      { key: 'tenLoai', label: 'Tên loại' },
      { key: 'soThang', label: 'Số tháng' },
      { key: 'soKy', label: 'Số kỳ' },
      { key: 'moTa', label: 'Mô tả' }
    ]
  },
  chuongTrinh: {
    title: 'Chương trình đào tạo',
    defaultForm: { nganhId: null, trinhDoId: null, loaiChuongTrinhId: null, maChuongTrinh: '', tenChuongTrinh: '', doiTuongTuyenSinh: '', thoiGianDaoTao: '' },
    fields: [
      { key: 'nganhId', label: 'Ngành', type: 'select', lookup: 'nganh', labelKey: ['maNganh', 'tenNganh'], locked: true },
      { key: 'trinhDoId', label: 'Trình độ', type: 'select', lookup: 'trinhDoDaoTao', labelKey: ['maTrinhDo', 'tenTrinhDo'] },
      { key: 'loaiChuongTrinhId', label: 'Loại CT', type: 'select', lookup: 'loaiChuongTrinh', labelKey: ['maLoai', 'tenLoai'] },
      { key: 'maChuongTrinh', label: 'Mã CTĐT', required: true },
      { key: 'tenChuongTrinh', label: 'Tên CTĐT', required: true },
      { key: 'doiTuongTuyenSinh', label: 'Đối tượng tuyển sinh' },
      { key: 'thoiGianDaoTao', label: 'Thời gian đào tạo' }
    ],
    columns: [
      { key: 'maChuongTrinh', label: 'Mã CTĐT' },
      { key: 'tenChuongTrinh', label: 'Tên CTĐT' },
      { key: 'tenTrinhDo', label: 'Trình độ' },
      { key: 'tenLoaiChuongTrinh', label: 'Loại CT' },
      { key: 'doiTuongTuyenSinh', label: 'Đối tượng' },
      { key: 'thoiGianDaoTao', label: 'Thời gian' }
    ]
  },
  chuongTrinhVersion: {
    title: 'Version chương trình',
    defaultForm: { chuongTrinhId: null, maVersion: '', tenVersion: '', ngayApDung: '', ngayHetHieuLuc: '', soQuyetDinh: '', ngayQuyetDinh: '', nguoiKy: '', coQuanBanHanh: '', tongTinChi: null, tongSoGio: null, tongGioLyThuyet: null, tongGioThucHanh: null, tongGioKiemTra: null, hienHanh: false, laHienHanh: false, deleted: false },
    fields: [
      { key: 'chuongTrinhId', label: 'Chương trình', type: 'select', lookup: 'chuongTrinh', labelKey: ['maChuongTrinh', 'tenChuongTrinh'], locked: true },
      { key: 'maVersion', label: 'Mã version', required: true },
      { key: 'tenVersion', label: 'Tên version', required: true },
      { key: 'ngayApDung', label: 'Ngày áp dụng', type: 'date' },
      { key: 'ngayHetHieuLuc', label: 'Ngày hết hiệu lực', type: 'date' },
      { key: 'soQuyetDinh', label: 'Số quyết định' },
      { key: 'ngayQuyetDinh', label: 'Ngày quyết định', type: 'date' },
      { key: 'nguoiKy', label: 'Người ký' },
      { key: 'coQuanBanHanh', label: 'Cơ quan ban hành' },
      { key: 'tongTinChi', label: 'Tổng tín chỉ', ...number6 },
      { key: 'tongSoGio', label: 'Tổng giờ', ...number01 },
      { key: 'tongGioLyThuyet', label: 'Giờ LT', ...number01 },
      { key: 'tongGioThucHanh', label: 'Giờ TH', ...number01 },
      { key: 'tongGioKiemTra', label: 'Giờ KT', ...number01 },
      { key: 'hienHanh', label: 'Hiện hành', type: 'boolean' }
    ],
    columns: [
      { key: 'maVersion', label: 'Mã' },
      { key: 'tenVersion', label: 'Tên version' },
      { key: 'ngayApDung', label: 'Ngày áp dụng' },
      { key: 'tongTinChi', label: 'TC' },
      { key: 'tongSoGio', label: 'Giờ' },
      { key: 'hienHanh', label: 'Hiện hành' }
    ]
  },
  khungKy: {
    title: 'Khung kỳ',
    defaultForm: { chuongTrinhVersionId: null, loaiChuongTrinhId: null, maKy: '', tenKy: '', thuTu: null },
    fields: [
      { key: 'chuongTrinhVersionId', label: 'Version', type: 'select', lookup: 'chuongTrinhVersion', labelKey: ['maVersion', 'tenVersion'], locked: true },
      { key: 'loaiChuongTrinhId', label: 'Loại CT', type: 'select', lookup: 'loaiChuongTrinh', labelKey: ['maLoai', 'tenLoai'], locked: true },
      { key: 'maKy', label: 'Mã kỳ', required: true },
      { key: 'tenKy', label: 'Tên kỳ', required: true },
      thuTu
    ],
    columns: [
      { key: 'maKy', label: 'Mã kỳ' },
      { key: 'tenKy', label: 'Tên kỳ' },
      { key: 'thuTu', label: 'Thứ tự' }
    ]
  },
  nhomKienThuc: {
    title: 'Nhóm kiến thức',
    defaultForm: { chuongTrinhVersionId: null, ma: '', ten: '', loaiNhom: 'chung', thuTu: null, tongTinChi: null, tongSoGio: null, tongGioLyThuyet: null, tongGioThucHanh: null, tongGioKiemTra: null },
    fields: [
      { key: 'chuongTrinhVersionId', label: 'Version', type: 'select', lookup: 'chuongTrinhVersion', labelKey: ['maVersion', 'tenVersion'], locked: true },
      { key: 'ma', label: 'Mã nhóm', required: true },
      { key: 'ten', label: 'Tên nhóm', required: true },
      { key: 'loaiNhom', label: 'Loại nhóm', type: 'select', lookup: 'loaiNhom' },
      thuTu,
      { key: 'tongTinChi', label: 'Tổng tín chỉ', ...number6 },
      { key: 'tongSoGio', label: 'Tổng giờ', ...number01 },
      { key: 'tongGioLyThuyet', label: 'Giờ LT', ...number01 },
      { key: 'tongGioThucHanh', label: 'Giờ TH', ...number01 },
      { key: 'tongGioKiemTra', label: 'Giờ KT', ...number01 }
    ],
    columns: [
      { key: 'ma', label: 'Mã' },
      { key: 'ten', label: 'Tên nhóm' },
      { key: 'loaiNhom', label: 'Loại' },
      { key: 'tongTinChi', label: 'TC' },
      { key: 'tongSoGio', label: 'Giờ' },
      { key: 'thuTu', label: 'Thứ tự' }
    ]
  },
  syllabusChuongTrinh: {
    title: 'Syllabus chương trình',
    defaultForm: { chuongTrinhVersionId: null, moTaTongQuan: '', mucDich: '', yeuCauDaoTao: '', phuongPhapDaoTao: '', ghiChu: '' },
    fields: [
      { key: 'chuongTrinhVersionId', label: 'Version', type: 'select', lookup: 'chuongTrinhVersion', labelKey: ['maVersion', 'tenVersion'], locked: true },
      { key: 'moTaTongQuan', label: 'Mô tả tổng quan', type: 'textarea', wide: true },
      { key: 'mucDich', label: 'Mục đích', type: 'textarea', wide: true },
      { key: 'yeuCauDaoTao', label: 'Yêu cầu đào tạo', type: 'textarea', wide: true },
      { key: 'phuongPhapDaoTao', label: 'Phương pháp đào tạo', type: 'textarea', wide: true },
      { key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true }
    ],
    columns: [
      { key: 'moTaTongQuan', label: 'Mô tả' },
      { key: 'mucDich', label: 'Mục đích' },
      { key: 'phuongPhapDaoTao', label: 'Phương pháp' },
      { key: 'ghiChu', label: 'Ghi chú' }
    ]
  },
  nhomTuChon: {
    title: 'Nhóm tự chọn',
    defaultForm: { chuongTrinhVersionId: null, ten: '', soMonChon: null, soTinChiCanDat: null, ghiChu: '' },
    fields: [
      { key: 'chuongTrinhVersionId', label: 'Version', type: 'select', lookup: 'chuongTrinhVersion', labelKey: ['maVersion', 'tenVersion'], locked: true },
      { key: 'ten', label: 'Tên nhóm', required: true },
      { key: 'soMonChon', label: 'Số môn chọn', type: 'number', step: 1 },
      { key: 'soTinChiCanDat', label: 'Số tín chỉ cần đạt', ...number6 },
      { key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true }
    ],
    columns: [
      { key: 'ten', label: 'Tên nhóm' },
      { key: 'soMonChon', label: 'Số môn chọn' },
      { key: 'soTinChiCanDat', label: 'TC cần đạt' },
      { key: 'ghiChu', label: 'Ghi chú' }
    ]
  }
}

Object.assign(configs, {
  monHoc: {
    title: 'Môn học gốc',
    defaultForm: { maMon: '', tenMon: '', moTa: '' },
    fields: [
      { key: 'maMon', label: 'Mã môn', required: true },
      { key: 'tenMon', label: 'Tên môn', required: true },
      { key: 'moTa', label: 'Mô tả', type: 'textarea', wide: true }
    ],
    columns: [
      { key: 'maMon', label: 'Mã môn' },
      { key: 'tenMon', label: 'Tên môn' },
      { key: 'moTa', label: 'Mô tả' }
    ]
  },
  chuongTrinhMon: {
    title: 'Chương trình môn',
    defaultForm: { chuongTrinhVersionId: null, monHocId: null, khungKyId: null, nhomKienThucId: null, maMonTrongCt: '', loai: 'bat_buoc', loaiHocPhan: 'mon_hoc', batBuoc: true, monDieuKien: false, laMonDieuKien: false, thuTu: null, soTinChi: null, tongGio: null, gioLyThuyet: null, gioThucHanh: null, gioKiemTra: null, ghiChu: '' },
    fields: [
      { key: 'chuongTrinhVersionId', label: 'Version', type: 'select', lookup: 'chuongTrinhVersion', labelKey: ['maVersion', 'tenVersion'], locked: true },
      { key: 'monHocId', label: 'Môn học', type: 'select', lookup: 'monHoc', labelKey: ['maMon', 'tenMon'] },
      { key: 'khungKyId', label: 'Khung kỳ', type: 'select', lookup: 'khungKy', labelKey: ['maKy', 'tenKy'] },
      { key: 'nhomKienThucId', label: 'Nhóm kiến thức', type: 'select', lookup: 'nhomKienThuc', labelKey: ['ma', 'ten'] },
      { key: 'maMonTrongCt', label: 'Mã môn trong CT', required: true },
      { key: 'loai', label: 'Loại môn', type: 'select', lookup: 'loaiMon' },
      { key: 'loaiHocPhan', label: 'Loại học phần', type: 'select', lookup: 'loaiHocPhan' },
      { key: 'batBuoc', label: 'Bắt buộc', type: 'boolean' },
      { key: 'monDieuKien', label: 'Môn điều kiện', type: 'boolean' },
      thuTu,
      { key: 'soTinChi', label: 'Số tín chỉ', ...number6 },
      { key: 'tongGio', label: 'Tổng giờ', ...number01 },
      { key: 'gioLyThuyet', label: 'Giờ LT', ...number01 },
      { key: 'gioThucHanh', label: 'Giờ TH', ...number01 },
      { key: 'gioKiemTra', label: 'Giờ KT', ...number01 },
      { key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true }
    ],
    columns: [
      { key: 'maMonTrongCt', label: 'Mã môn CT' },
      { key: 'tenMonHoc', label: 'Môn học' },
      { key: 'tenKhungKy', label: 'Kỳ' },
      { key: 'tenNhomKienThuc', label: 'Nhóm KT' },
      { key: 'loai', label: 'Loại' },
      { key: 'soTinChi', label: 'TC' },
      { key: 'tongGio', label: 'Giờ' },
      { key: 'thuTu', label: 'Thứ tự' }
    ]
  },
  monTuChon: {
    title: 'Môn tự chọn',
    defaultForm: { nhomId: null, chuongTrinhMonId: null },
    fields: [
      { key: 'nhomId', label: 'Nhóm tự chọn', type: 'select', lookup: 'nhomTuChon', labelKey: ['ten'], locked: true },
      { key: 'chuongTrinhMonId', label: 'Môn CT', type: 'select', lookup: 'chuongTrinhMon', labelKey: ['maMonTrongCt', 'tenMonHoc'] }
    ],
    columns: [
      { key: 'tenNhomTuChon', label: 'Nhóm tự chọn' },
      { key: 'tenChuongTrinhMon', label: 'Môn CT' }
    ]
  },
  monTienQuyet: {
    title: 'Môn tiên quyết',
    defaultForm: { monId: null, monDieuKienId: null, loai: 'tien_quyet', ghiChu: '' },
    fields: [
      { key: 'monId', label: 'Môn hiện tại', type: 'select', lookup: 'chuongTrinhMon', labelKey: ['maMonTrongCt', 'tenMonHoc'], locked: true },
      { key: 'monDieuKienId', label: 'Môn điều kiện', type: 'select', lookup: 'chuongTrinhMon', labelKey: ['maMonTrongCt', 'tenMonHoc'] },
      { key: 'loai', label: 'Loại điều kiện', type: 'select', lookup: 'loaiDieuKien' },
      { key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true }
    ],
    columns: [
      { key: 'tenMonHienTai', label: 'Môn hiện tại' },
      { key: 'tenMonDieuKien', label: 'Môn điều kiện' },
      { key: 'loai', label: 'Loại' },
      { key: 'ghiChu', label: 'Ghi chú' }
    ]
  },
  quyDoiDiem: {
    title: 'Quy đổi điểm',
    defaultForm: { chuongTrinhMonId: null, nguongTu: null, nguongDen: null, diemQuyDoi: null, ketQua: 'dat', congThuc: '', ghiChu: '' },
    fields: [
      { key: 'chuongTrinhMonId', label: 'Môn CT', type: 'select', lookup: 'chuongTrinhMon', labelKey: ['maMonTrongCt', 'tenMonHoc'], locked: true },
      { key: 'nguongTu', label: 'Ngưỡng từ', ...numberPoint },
      { key: 'nguongDen', label: 'Ngưỡng đến', ...numberPoint },
      { key: 'diemQuyDoi', label: 'Điểm quy đổi', ...numberPoint },
      { key: 'ketQua', label: 'Kết quả', type: 'select', lookup: 'ketQua' },
      { key: 'congThuc', label: 'Công thức' },
      { key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true }
    ],
    columns: [
      { key: 'nguongTu', label: 'Từ' },
      { key: 'nguongDen', label: 'Đến' },
      { key: 'diemQuyDoi', label: 'Điểm' },
      { key: 'ketQua', label: 'Kết quả' },
      { key: 'congThuc', label: 'Công thức' }
    ]
  },
  quyDoiDiemMau: {
    title: 'Quy đổi điểm mẫu',
    defaultForm: { ma: '', ten: '', nguongTu: null, nguongDen: null, diemQuyDoi: null, ketQua: 'dat', congThuc: '', ghiChu: '' },
    fields: [
      { key: 'ma', label: 'Mã mẫu' },
      { key: 'ten', label: 'Tên mẫu' },
      { key: 'nguongTu', label: 'Ngưỡng từ', ...numberPoint },
      { key: 'nguongDen', label: 'Ngưỡng đến', ...numberPoint },
      { key: 'diemQuyDoi', label: 'Điểm quy đổi', ...numberPoint },
      { key: 'ketQua', label: 'Kết quả', type: 'select', lookup: 'ketQua' },
      { key: 'congThuc', label: 'Công thức' },
      { key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true }
    ],
    columns: [
      { key: 'ma', label: 'Mã' },
      { key: 'ten', label: 'Tên mẫu' },
      { key: 'nguongTu', label: 'Từ' },
      { key: 'nguongDen', label: 'Đến' },
      { key: 'diemQuyDoi', label: 'Điểm' },
      { key: 'ketQua', label: 'Kết quả' }
    ]
  },
  chuongTrinhMonQuyDoiDiemMau: {
    title: 'Gán quy đổi điểm mẫu vào môn',
    defaultForm: { chuongTrinhMonId: null, quyDoiDiemMauId: null, ghiChu: '' },
    fields: [
      { key: 'chuongTrinhMonId', label: 'Môn CT', type: 'select', lookup: 'chuongTrinhMon', labelKey: ['maMonTrongCt', 'tenMonHoc'], locked: true },
      { key: 'quyDoiDiemMauId', label: 'Mẫu quy đổi', type: 'select', lookup: 'quyDoiDiemMau', labelKey: ['ma', 'ten'] },
      { key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true }
    ],
    columns: [
      { key: 'tenQuyDoiDiemMau', label: 'Mẫu quy đổi' },
      { key: 'ghiChu', label: 'Ghi chú' }
    ]
  }
})

Object.assign(configs, {
  mucTieuChuongTrinhGoc: {
    title: 'Mục tiêu chương trình gốc',
    defaultForm: { loai: 'chung', noiDung: '', thuTu: null },
    fields: [
      { key: 'loai', label: 'Loại', type: 'select', lookup: 'loaiMucTieu' },
      { key: 'noiDung', label: 'Nội dung', type: 'textarea', wide: true },
      thuTu
    ],
    columns: [
      { key: 'loai', label: 'Loại' },
      { key: 'noiDung', label: 'Nội dung' },
      { key: 'thuTu', label: 'Thứ tự' }
    ]
  },
  chuongTrinhVersionMucTieu: {
    title: 'Mục tiêu gán vào version',
    defaultForm: { chuongTrinhVersionId: null, mucTieuGocId: null, thuTu: null, ghiChu: '' },
    fields: [
      { key: 'chuongTrinhVersionId', label: 'Version', type: 'select', lookup: 'chuongTrinhVersion', labelKey: ['maVersion', 'tenVersion'], locked: true },
      { key: 'mucTieuGocId', label: 'Mục tiêu gốc', type: 'select', lookup: 'mucTieuChuongTrinhGoc', labelKey: ['loai', 'noiDung'] },
      thuTu,
      { key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true }
    ],
    columns: [
      { key: 'tenMucTieuGoc', label: 'Mục tiêu gốc' },
      { key: 'thuTu', label: 'Thứ tự' },
      { key: 'ghiChu', label: 'Ghi chú' }
    ]
  },
  nangLucDauRaGoc: {
    title: 'Năng lực đầu ra gốc',
    defaultForm: { ma: '', loai: 'co_ban', noiDung: '', thuTu: null },
    fields: [
      { key: 'ma', label: 'Mã' },
      { key: 'loai', label: 'Loại', type: 'select', lookup: 'loaiNangLuc' },
      { key: 'noiDung', label: 'Nội dung', type: 'textarea', wide: true },
      thuTu
    ],
    columns: [
      { key: 'ma', label: 'Mã' },
      { key: 'loai', label: 'Loại' },
      { key: 'noiDung', label: 'Nội dung' },
      { key: 'thuTu', label: 'Thứ tự' }
    ]
  },
  chuongTrinhVersionNangLuc: {
    title: 'Năng lực gán vào version',
    defaultForm: { chuongTrinhVersionId: null, nangLucGocId: null, thuTu: null, ghiChu: '' },
    fields: [
      { key: 'chuongTrinhVersionId', label: 'Version', type: 'select', lookup: 'chuongTrinhVersion', labelKey: ['maVersion', 'tenVersion'], locked: true },
      { key: 'nangLucGocId', label: 'Năng lực gốc', type: 'select', lookup: 'nangLucDauRaGoc', labelKey: ['ma', 'noiDung'] },
      thuTu,
      { key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true }
    ],
    columns: [
      { key: 'tenNangLucGoc', label: 'Năng lực gốc' },
      { key: 'thuTu', label: 'Thứ tự' },
      { key: 'ghiChu', label: 'Ghi chú' }
    ]
  },
  viTriViecLamGoc: {
    title: 'Vị trí việc làm gốc',
    defaultForm: { ma: '', ten: '', moTa: '', thuTu: null },
    fields: [
      { key: 'ma', label: 'Mã' },
      { key: 'ten', label: 'Tên vị trí' },
      { key: 'moTa', label: 'Mô tả', type: 'textarea', wide: true },
      thuTu
    ],
    columns: [
      { key: 'ma', label: 'Mã' },
      { key: 'ten', label: 'Vị trí' },
      { key: 'moTa', label: 'Mô tả' },
      { key: 'thuTu', label: 'Thứ tự' }
    ]
  },
  chuongTrinhVersionViTriViecLam: {
    title: 'Vị trí việc làm gán vào version',
    defaultForm: { chuongTrinhVersionId: null, viTriViecLamGocId: null, thuTu: null, ghiChu: '' },
    fields: [
      { key: 'chuongTrinhVersionId', label: 'Version', type: 'select', lookup: 'chuongTrinhVersion', labelKey: ['maVersion', 'tenVersion'], locked: true },
      { key: 'viTriViecLamGocId', label: 'Vị trí gốc', type: 'select', lookup: 'viTriViecLamGoc', labelKey: ['ma', 'ten'] },
      thuTu,
      { key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true }
    ],
    columns: [
      { key: 'tenViTriViecLamGoc', label: 'Vị trí gốc' },
      { key: 'thuTu', label: 'Thứ tự' },
      { key: 'ghiChu', label: 'Ghi chú' }
    ]
  },
  dieuKienTotNghiepGoc: {
    title: 'Điều kiện tốt nghiệp gốc',
    defaultForm: { ma: '', noiDung: '', thuTu: null, ghiChu: '' },
    fields: [
      { key: 'ma', label: 'Mã' },
      { key: 'noiDung', label: 'Nội dung', type: 'textarea', wide: true },
      thuTu,
      { key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true }
    ],
    columns: [
      { key: 'ma', label: 'Mã' },
      { key: 'noiDung', label: 'Nội dung' },
      { key: 'thuTu', label: 'Thứ tự' },
      { key: 'ghiChu', label: 'Ghi chú' }
    ]
  },
  chuongTrinhVersionDieuKienTotNghiep: {
    title: 'Điều kiện tốt nghiệp gán vào version',
    defaultForm: { chuongTrinhVersionId: null, dieuKienTotNghiepGocId: null, thuTu: null, ghiChu: '' },
    fields: [
      { key: 'chuongTrinhVersionId', label: 'Version', type: 'select', lookup: 'chuongTrinhVersion', labelKey: ['maVersion', 'tenVersion'], locked: true },
      { key: 'dieuKienTotNghiepGocId', label: 'Điều kiện gốc', type: 'select', lookup: 'dieuKienTotNghiepGoc', labelKey: ['ma', 'noiDung'] },
      thuTu,
      { key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true }
    ],
    columns: [
      { key: 'tenDieuKienTotNghiepGoc', label: 'Điều kiện gốc' },
      { key: 'thuTu', label: 'Thứ tự' },
      { key: 'ghiChu', label: 'Ghi chú' }
    ]
  }
})

Object.assign(configs, {
  syllabusMonHoc: {
    title: 'Syllabus môn học',
    defaultForm: { chuongTrinhMonId: null, viTri: '', tinhChat: '', mucTieu: '', phuongPhapDanhGia: '', dieuKienHoanThanh: '', diemDatToiThieu: null, donViDiem: 'thang_10', tyLeChuyenCanToiThieu: null, batBuocDuThi: true, congThucQuyDoi: '', huongDan: '' },
    fields: [
      { key: 'chuongTrinhMonId', label: 'Môn CT', type: 'select', lookup: 'chuongTrinhMon', labelKey: ['maMonTrongCt', 'tenMonHoc'], locked: true },
      { key: 'viTri', label: 'Vị trí', type: 'textarea', wide: true },
      { key: 'tinhChat', label: 'Tính chất', type: 'textarea', wide: true },
      { key: 'mucTieu', label: 'Mục tiêu', type: 'textarea', wide: true },
      { key: 'phuongPhapDanhGia', label: 'Phương pháp đánh giá', type: 'textarea', wide: true },
      { key: 'dieuKienHoanThanh', label: 'Điều kiện hoàn thành', type: 'textarea', wide: true },
      { key: 'diemDatToiThieu', label: 'Điểm đạt tối thiểu', ...numberPoint },
      { key: 'donViDiem', label: 'Đơn vị điểm', type: 'select', lookup: 'donViDiem' },
      { key: 'tyLeChuyenCanToiThieu', label: 'Tỷ lệ chuyên cần tối thiểu', ...numberPoint },
      { key: 'batBuocDuThi', label: 'Bắt buộc dự thi', type: 'boolean' },
      { key: 'congThucQuyDoi', label: 'Công thức quy đổi' },
      { key: 'huongDan', label: 'Hướng dẫn', type: 'textarea', wide: true }
    ],
    columns: [
      { key: 'viTri', label: 'Vị trí' },
      { key: 'tinhChat', label: 'Tính chất' },
      { key: 'diemDatToiThieu', label: 'Điểm đạt' },
      { key: 'donViDiem', label: 'Đơn vị điểm' },
      { key: 'batBuocDuThi', label: 'Bắt buộc thi' }
    ]
  },
  dieuKienMonHoc: {
    title: 'Điều kiện môn học',
    defaultForm: { syllabusMonId: null, loai: 'khac', noiDung: '', thuTu: null },
    fields: [
      { key: 'syllabusMonId', label: 'Syllabus môn', type: 'select', lookup: 'syllabusMonHoc', labelKey: ['viTri', 'mucTieu'], locked: true },
      { key: 'loai', label: 'Loại', type: 'select', lookup: 'loaiDieuKienMonHoc' },
      { key: 'noiDung', label: 'Nội dung', type: 'textarea', wide: true },
      thuTu
    ],
    columns: [
      { key: 'loai', label: 'Loại' },
      { key: 'noiDung', label: 'Nội dung' },
      { key: 'thuTu', label: 'Thứ tự' }
    ]
  },
  syllabusChuongBai: {
    title: 'Syllabus chương bài',
    defaultForm: { syllabusMonId: null, ten: '', tongGio: null, gioLyThuyet: null, gioThucHanh: null, gioKiemTra: null, noiDung: '', mucTieu: '', thuTu: null },
    fields: [
      { key: 'syllabusMonId', label: 'Syllabus môn', type: 'select', lookup: 'syllabusMonHoc', labelKey: ['viTri', 'mucTieu'], locked: true },
      { key: 'ten', label: 'Tên chương bài' },
      thuTu,
      { key: 'tongGio', label: 'Tổng giờ', ...number6 },
      { key: 'gioLyThuyet', label: 'Giờ LT', ...number6 },
      { key: 'gioThucHanh', label: 'Giờ TH', ...number6 },
      { key: 'gioKiemTra', label: 'Giờ KT', ...number6 },
      { key: 'noiDung', label: 'Nội dung', type: 'textarea', wide: true },
      { key: 'mucTieu', label: 'Mục tiêu', type: 'textarea', wide: true }
    ],
    columns: [
      { key: 'ten', label: 'Tên chương bài' },
      { key: 'thuTu', label: 'Thứ tự' },
      { key: 'tongGio', label: 'Tổng giờ' },
      { key: 'gioLyThuyet', label: 'LT' },
      { key: 'gioThucHanh', label: 'TH' },
      { key: 'gioKiemTra', label: 'KT' }
    ]
  },
  syllabusTaiLieu: {
    title: 'Syllabus tài liệu',
    defaultForm: { syllabusMonId: null, ten: '', tacGia: '', namXuatBan: null, nhaXuatBan: '', loai: 'GIAO_TRINH', ghiChu: '' },
    fields: [
      { key: 'syllabusMonId', label: 'Syllabus môn', type: 'select', lookup: 'syllabusMonHoc', labelKey: ['viTri', 'mucTieu'], locked: true },
      { key: 'ten', label: 'Tên tài liệu' },
      { key: 'tacGia', label: 'Tác giả' },
      { key: 'namXuatBan', label: 'Năm xuất bản', type: 'number', step: 1, min: 0 },
      { key: 'nhaXuatBan', label: 'Nhà xuất bản' },
      { key: 'loai', label: 'Loại tài liệu', type: 'select', lookup: 'loaiTaiLieu' },
      { key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true }
    ],
    columns: [
      { key: 'ten', label: 'Tên tài liệu' },
      { key: 'tacGia', label: 'Tác giả' },
      { key: 'namXuatBan', label: 'Năm' },
      { key: 'nhaXuatBan', label: 'NXB' },
      { key: 'loai', label: 'Loại' }
    ]
  }
})
