
import {computed, onMounted, reactive, ref, watch} from 'vue'
import {daoTaoXemChuongTrinhService} from '../services/daoTaoXemChuongTrinhService'
import {layThongBaoLoi} from '../utils/layThongBaoLoi'

export const luaChonEnum = {
    loaiMucTieu: [
        {id: 'chung', ten: 'Chung'},
        {id: 'kien_thuc', ten: 'Kiến thức'},
        {id: 'ky_nang', ten: 'Kỹ năng'},
        {id: 'nang_luc_tu_chu_trach_nhiem', ten: 'Năng lực tự chủ trách nhiệm'}
    ],

    loaiNangLuc: [
        {id: 'co_ban', ten: 'Cơ bản'},
        {id: 'cot_loi', ten: 'Cốt lõi'},
        {id: 'nang_cao', ten: 'Nâng cao'}
    ],
    loaiNhom: [
        {id: 'chung', ten: 'Chung'},
        {id: 'co_so', ten: 'Cơ sở'},
        {id: 'chuyen_mon', ten: 'Chuyên môn'},
        {id: 'tu_chon', ten: 'Tự chọn'}
    ],
    loaiMon: [
        {id: 'bat_buoc', ten: 'Bắt buộc'},
        {id: 'tu_chon', ten: 'Tự chọn'}
    ],
    loaiHocPhan: [
        {id: 'mon_hoc', ten: 'Môn học'},
        {id: 'mo_dun', ten: 'Mô đun'}
    ],
    trangThaiChuongTrinhVersion: [
        {id: 'DANG_SOAN',    ten: 'Đang soạn'},
        {id: 'CHO_AP_DUNG',  ten: 'Chờ áp dụng'},
        {id: 'HIEN_HANH',    ten: 'Hiện hành'},
        {id: 'KHOA',         ten: 'Khóa'},
        {id: 'HET_HIEU_LUC', ten: 'Hết hiệu lực'},
        {id: 'HUY',          ten: 'Hủy'}
    ],
    loaiPhamViMon: [
        {id: 'mon_chuyen_nganh', ten: 'Môn chuyên ngành'},
        {id: 'mon_chung', ten: 'Môn học chung'}
    ],
    loaiDieuKien: [
        {id: 'tien_quyet', ten: 'Tiên quyết'},
        {id: 'song_hanh', ten: 'Song hành'},
        {id: 'hoc_truoc', ten: 'Học trước'}
    ],
    loaiDieuKienMonHoc: [
        {id: 'phong_hoc', ten: 'Phòng học'},
        {id: 'thiet_bi', ten: 'Thiết bị'},
        {id: 'hoc_lieu', ten: 'Học liệu'},
        {id: 'dung_cu', ten: 'Dụng cụ'},
        {id: 'nguyen_vat_lieu', ten: 'Nguyên vật liệu'},
        {id: 'khac', ten: 'Khác'}
    ],
    ketQua: [
        {id: 'dat', ten: 'Đạt'},
        {id: 'khong_dat', ten: 'Không đạt'}
    ],
    loaiTaiLieu: [
        {id: 'GIAO_TRINH', ten: 'Giáo trình'},
        {id: 'TAI_LIEU_THAM_KHAO', ten: 'Tài liệu tham khảo'},
        {id: 'BAI_GIANG', ten: 'Bài giảng'},
        {id: 'KHAC', ten: 'Khác'}
    ],
    donViDiem: [
        {id: 'thang_10', ten: 'Thang 10'},
        {id: 'phan_tram', ten: 'Phần trăm'}
    ],
    loaiDiemDanhGia: [
        {id: 'chuyen_can', ten: 'Chuyên cần'},
        {id: 'bai_tap', ten: 'Bài tập'},
        {id: 'giua_ky', ten: 'Giữa kỳ'},
        {id: 'cuoi_ky', ten: 'Cuối kỳ'},
        {id: 'thuc_hanh', ten: 'Thực hành / Lab'},
        {id: 'do_an', ten: 'Đồ án / ASM'},
        {id: 'khac', ten: 'Khác'}
    ]
}


const bangLayDuLieuCoSan = [
    'nganh',
    'trinhDoDaoTao',
    'loaiChuongTrinh',
    'nganhTrinhDoDaoTao',
    'nganhLoaiChuongTrinh',
    'nganhHeDaoTao',
    'chuongTrinh',
    'chuongTrinhVersion',
    'khungKy',
    'khungKymau',
    'nhomKienThuc',
    'nhomKienThucmau',
    'nhomTuChon',
    'nhomTuChonmau',
    'monHoc',
    'chuongTrinhMon',
    'monTuChon',
    'syllabusMonHoc',
    'syllabusMonHocmau',
    'mucTieuChuongTrinhmau',
    'mucTieuChuongTrinh',
    'nangLucDauRamau',
    'nangLucDauRa',
    'viTriViecLammau',
    'viTriViecLam',
    'dieuKienTotNghiepmau',
    'dieuKienTotNghiep',
    'chuongTrinhVersionMucTieu',
    'chuongTrinhVersionNangLuc',
    'chuongTrinhVersionViTriViecLam',
    'chuongTrinhVersionDieuKienTotNghiep',
    'syllabusChuongTrinh',
    'syllabusChuongTrinhmau',
    'monTienQuyet',
    // Các bảng theo syllabus được tải riêng trong taiDuLieuTheoSyllabusMonHocApDung để BE không trả toàn ngành.
    // syllabusMonHocmauTaiLieu, syllabusMonHocmauDieuKien, syllabusMonHocmauChuongBai cũng thuộc nhóm này —
    // không đưa vào đây vì taiDuLieuCoSanTatCaBang sẽ load toàn bộ và ghi đè dữ liệu đã lọc.
    'dieuKienMonHoc',
    'dieuKienMonHocmau',
    'syllabusMonHocDieuKien',
    'taiLieumau',
    'syllabusMonHocTaiLieu',
    'syllabusChuongBai',
    'syllabusTaiLieu'
]

function layDanhSachTuKetQua(result) {
    if (Array.isArray(result)) return result
    if (Array.isArray(result?.items)) return result.items
    if (Array.isArray(result?.content)) return result.content
    if (Array.isArray(result?.data)) return result.data
    if (Array.isArray(result?.data?.items)) return result.data.items
    if (Array.isArray(result?.data?.content)) return result.data.content
    return []
}

function taoBanDo(list = []) {
    return new Map(list.map((item) => [item.id, item]))
}

function layTen(map, id, keys = ['ten', 'tenMon', 'tenNganh', 'tenChuongTrinh', 'tenVersion']) {
    const item = map.get(id) || map.get(String(id))
    if (!item) return id || '-'

    for (const key of keys) {
        if (item[key]) return item[key]
    }

    return item.ma || item.id || '-'
}

function layGiaTriTheoKhoa(item, keys = [], fallback = null) {
    if (!item) return fallback

    for (const key of keys) {
        if (!key) continue
        const value = item[key]
        if (value !== null && value !== undefined && value !== '') return value
    }

    return fallback
}

function layDoiTuongLienKet(row, map, idKeys = [], objectKeys = []) {
    for (const key of objectKeys) {
        const value = row?.[key]
        if (value && typeof value === 'object') return value
    }

    const id = layGiaTriTheoKhoa(row, idKeys, null)
    if (id !== null && id !== undefined) {
        return map.get(id) || map.get(String(id)) || {}
    }

    return {}
}

function themTenLienKet(duLieu) {
    const nganhMap = taoBanDo(duLieu.nganh)
    const trinhDoMap = taoBanDo(duLieu.trinhDoDaoTao)
    const loaiCtMap = taoBanDo(duLieu.loaiChuongTrinh)
    const nganhLoaiCtMap = taoBanDo(duLieu.nganhLoaiChuongTrinh)
    const chuongTrinhMap = taoBanDo(duLieu.chuongTrinh)
    const versionMap = taoBanDo(duLieu.chuongTrinhVersion)
    const monHocMap = taoBanDo(duLieu.monHoc)
    const khungKyMap = taoBanDo(duLieu.khungKy)
    const khungKymauMap = taoBanDo(duLieu.khungKymau)
    const nhomKienThucMap = taoBanDo(duLieu.nhomKienThuc)
    const nhomKienThucmauMap = taoBanDo(duLieu.nhomKienThucmau)
    const nhomTuChonMap = taoBanDo(duLieu.nhomTuChon)
    const nhomTuChonmauMap = taoBanDo(duLieu.nhomTuChonmau)
    const chuongTrinhMonMap = taoBanDo(duLieu.chuongTrinhMon)
    const quyDoiDiemMauMap = taoBanDo(duLieu.quyDoiDiemMau)
    const syllabusMonMap = taoBanDo(duLieu.syllabusMonHoc)
    const syllabusChuongTrinhmauMap = taoBanDo(duLieu.syllabusChuongTrinhmau)
    const syllabusChuongTrinhMap = taoBanDo(duLieu.syllabusChuongTrinh)
    const syllabusMonHocmauMap = taoBanDo(duLieu.syllabusMonHocmau)
    const dieuKienMonHocmauMap = taoBanDo(duLieu.dieuKienMonHocmau)
    const taiLieumauMap = taoBanDo(duLieu.taiLieumau)
    const mucTieumauMap = taoBanDo(duLieu.mucTieuChuongTrinhmau)
    const nangLucmauMap = taoBanDo(duLieu.nangLucDauRamau)
    const viTrimauMap = taoBanDo(duLieu.viTriViecLammau)
    const dieuKienTotNghiepmauMap = taoBanDo(duLieu.dieuKienTotNghiepmau)

    return {
        ...duLieu,
        nganhTrinhDoDaoTao: duLieu.nganhTrinhDoDaoTao.map((item) => ({
            ...item,
            tenNganh: layTen(nganhMap, item.nganhId, ['tenNganh']),
            maTrinhDo: layTen(trinhDoMap, item.trinhDoId, ['maTrinhDo', 'ma']),
            tenTrinhDo: layTen(trinhDoMap, item.trinhDoId, ['tenTrinhDo', 'ten']),
            moTaTrinhDo: layTen(trinhDoMap, item.trinhDoId, ['moTa'])
        })),
        nganhLoaiChuongTrinh: duLieu.nganhLoaiChuongTrinh.map((item) => ({
            ...item,
            tenNganh: layTen(nganhMap, item.nganhId, ['tenNganh']),
            maLoai: layTen(loaiCtMap, item.loaiChuongTrinhId, ['maLoai', 'ma']),
            tenLoaiChuongTrinh: layTen(loaiCtMap, item.loaiChuongTrinhId, ['tenLoai', 'ten']),
            soThang: layTen(loaiCtMap, item.loaiChuongTrinhId, ['soThang']),
            soKy: layTen(loaiCtMap, item.loaiChuongTrinhId, ['soKy']),
            moTaLoai: layTen(loaiCtMap, item.loaiChuongTrinhId, ['moTa'])
        })),
        chuongTrinh: duLieu.chuongTrinh.map((item) => {
            const nganhLoai = nganhLoaiCtMap.get(item.nganhLoaiChuongTrinhId) || nganhLoaiCtMap.get(String(item.nganhLoaiChuongTrinhId)) || {}
            const nganhId = item.nganhId || nganhLoai.nganhId
            const loaiChuongTrinhId = item.loaiChuongTrinhId || nganhLoai.loaiChuongTrinhId

            return {
                ...item,
                nganhId,
                loaiChuongTrinhId,
                tenNganh: layTen(nganhMap, nganhId, ['tenNganh']),
                tenTrinhDo: layTen(trinhDoMap, item.trinhDoId, ['tenTrinhDo']),
                tenLoaiChuongTrinh: layTen(loaiCtMap, loaiChuongTrinhId, ['tenLoai']),
                tenKhungKymau: layTen(khungKymauMap, item.khungKymauId, ['maKy', 'tenKy'])
            }
        }),
        chuongTrinhVersion: duLieu.chuongTrinhVersion.map((item) => ({
            ...item,
            tenChuongTrinh: layTen(chuongTrinhMap, item.chuongTrinhId, ['tenChuongTrinh'])
        })),
        khungKy: duLieu.khungKy.map((item) => ({
            ...item,
            tenVersion: layTen(versionMap, item.chuongTrinhVersionId, ['tenVersion']),
            tenLoaiChuongTrinh: layTen(loaiCtMap, item.loaiChuongTrinhId, ['tenLoai']),
            tenKhungKymau: layTen(khungKymauMap, item.khungKymauId, ['maKy', 'tenKy'])
        })),
        mucTieuChuongTrinh: duLieu.mucTieuChuongTrinh.map((item) => {
            const syllabusChuongTrinh = syllabusChuongTrinhMap.get(item.syllabusChuongTrinhId) || syllabusChuongTrinhMap.get(String(item.syllabusChuongTrinhId)) || {}
            const chuongTrinhVersionId = item.chuongTrinhVersionId || syllabusChuongTrinh.chuongTrinhVersionId
            return {
                ...item,
                chuongTrinhVersionId,
                tenVersion: layTen(versionMap, chuongTrinhVersionId, ['maVersion', 'tenVersion'])
            }
        }),
        nangLucDauRa: duLieu.nangLucDauRa.map((item) => {
            const syllabusChuongTrinh = syllabusChuongTrinhMap.get(item.syllabusChuongTrinhId) || syllabusChuongTrinhMap.get(String(item.syllabusChuongTrinhId)) || {}
            const chuongTrinhVersionId = item.chuongTrinhVersionId || syllabusChuongTrinh.chuongTrinhVersionId
            return {
                ...item,
                chuongTrinhVersionId,
                tenVersion: layTen(versionMap, chuongTrinhVersionId, ['maVersion', 'tenVersion'])
            }
        }),
        viTriViecLam: duLieu.viTriViecLam.map((item) => {
            const syllabusChuongTrinh = syllabusChuongTrinhMap.get(item.syllabusChuongTrinhId) || syllabusChuongTrinhMap.get(String(item.syllabusChuongTrinhId)) || {}
            const chuongTrinhVersionId = item.chuongTrinhVersionId || syllabusChuongTrinh.chuongTrinhVersionId
            return {
                ...item,
                chuongTrinhVersionId,
                tenVersion: layTen(versionMap, chuongTrinhVersionId, ['maVersion', 'tenVersion'])
            }
        }),
        dieuKienTotNghiep: duLieu.dieuKienTotNghiep.map((item) => {
            const syllabusChuongTrinh = syllabusChuongTrinhMap.get(item.syllabusChuongTrinhId) || syllabusChuongTrinhMap.get(String(item.syllabusChuongTrinhId)) || {}
            const chuongTrinhVersionId = item.chuongTrinhVersionId || syllabusChuongTrinh.chuongTrinhVersionId
            return {
                ...item,
                chuongTrinhVersionId,
                tenVersion: layTen(versionMap, chuongTrinhVersionId, ['maVersion', 'tenVersion'])
            }
        }),
        chuongTrinhVersionMucTieu: duLieu.chuongTrinhVersionMucTieu.map((item) => ({
            ...item,
            tenVersion: layTen(versionMap, item.chuongTrinhVersionId, ['tenVersion']),
            tenMucTieumau: layTen(mucTieumauMap, item.mucTieumauId, ['ma', 'noiDung'])
        })),
        chuongTrinhVersionNangLuc: duLieu.chuongTrinhVersionNangLuc.map((item) => ({
            ...item,
            tenVersion: layTen(versionMap, item.chuongTrinhVersionId, ['tenVersion']),
            tenNangLucmau: layTen(nangLucmauMap, item.nangLucmauId, ['ma', 'noiDung'])
        })),
        chuongTrinhVersionViTriViecLam: duLieu.chuongTrinhVersionViTriViecLam.map((item) => ({
            ...item,
            tenVersion: layTen(versionMap, item.chuongTrinhVersionId, ['tenVersion']),
            tenViTrimau: layTen(viTrimauMap, item.viTrimauId, ['ma', 'ten'])
        })),
        chuongTrinhVersionDieuKienTotNghiep: duLieu.chuongTrinhVersionDieuKienTotNghiep.map((item) => ({
            ...item,
            tenVersion: layTen(versionMap, item.chuongTrinhVersionId, ['tenVersion']),
            tenDieuKienmau: layTen(dieuKienTotNghiepmauMap, item.dieuKienmauId, ['ma', 'noiDung'])
        })),
        syllabusChuongTrinhmau: duLieu.syllabusChuongTrinhmau.map((item) => ({...item})),
        syllabusChuongTrinh: duLieu.syllabusChuongTrinh.map((item) => ({
            ...item,
            tenVersion: layTen(versionMap, item.chuongTrinhVersionId, ['maVersion', 'tenVersion']),
            tenSyllabusChuongTrinhmau: layTen(syllabusChuongTrinhmauMap, item.syllabusChuongTrinhmauId || item.syllabusChuongTrinhMauId, ['ma', 'ten'])
        })),
        nhomKienThucmau: duLieu.nhomKienThucmau.map((item) => ({...item})),
        nhomKienThuc: duLieu.nhomKienThuc.map((item) => ({
            ...item,
            tenVersion: layTen(versionMap, item.chuongTrinhVersionId, ['tenVersion']),
            tenNhomKienThucmau: layTen(nhomKienThucmauMap, item.nhomKienThucmauId, ['ma', 'ten'])
        })),
        nhomTuChonmau: duLieu.nhomTuChonmau.map((item) => ({...item})),
        nhomTuChon: duLieu.nhomTuChon.map((item) => ({
            ...item,
            tenVersion: layTen(versionMap, item.chuongTrinhVersionId, ['tenVersion']),
            tenNhomTuChonmau: layTen(nhomTuChonmauMap, item.nhomTuChonmauId, ['ma', 'ten'])
        })),
        chuongTrinhMon: duLieu.chuongTrinhMon.map((item) => ({
            ...item,
            tenVersion: layTen(versionMap, item.chuongTrinhVersionId, ['tenVersion']),
            tenMon: layTen(monHocMap, item.monHocId, ['tenMon']),
            tenKy: layTen(khungKyMap, item.khungKyId, ['tenKy']),
            tenNhomKienThuc: layTen(nhomKienThucMap, item.nhomKienThucId, ['ten'])
        })),
        monTuChon: duLieu.monTuChon.map((item) => ({
            ...item,
            tenNhomTuChon: layTen(nhomTuChonMap, item.nhomId, ['ten']),
            tenChuongTrinhMon: layTen(chuongTrinhMonMap, item.chuongTrinhMonId, ['maMonTrongCt', 'tenMon'])
        })),
        monTienQuyet: duLieu.monTienQuyet.map((item) => ({
            ...item,
            tenMon: layTen(chuongTrinhMonMap, item.monId, ['maMonTrongCt', 'tenMon']),
            tenMonDieuKien: layTen(chuongTrinhMonMap, item.monDieuKienId, ['maMonTrongCt', 'tenMon'])
        })),
        quyDoiDiem: duLieu.quyDoiDiem.map((item) => ({
            ...item,
            tenChuongTrinhMon: layTen(chuongTrinhMonMap, item.chuongTrinhMonId, ['maMonTrongCt', 'tenMon']),
            tenSyllabusMonHoc: layTen(syllabusMonMap, item.syllabusMonHocId, [
                'ten',
                'tenSyllabusMonHocmau',
                'ma',
                'maSyllabusMonHocmau',
                'mucTieu'
            ]),
            tenCotDiemMau: layGiaTriTheoKhoa(
                item,
                ['tenCotDiemMau', 'ten_cot_diem_mau', 'tenCotDiem', 'ten_cot_diem', 'ghiChu', 'ghi_chu'],
                ''
            )
        })),
        quyDoiDiemMau: duLieu.quyDoiDiemMau.map((item) => ({...item})),
        cauHinhDanhGiaMau: duLieu.cauHinhDanhGiaMau.map((item) => ({
            ...item,
            tenSyllabusMonHocmau: layTen(syllabusMonHocmauMap, item.syllabusMonHocMauId, ['ma', 'ten'])
        })),
        cauHinhDanhGia: duLieu.cauHinhDanhGia.map((item) => ({
            ...item,
            tenSyllabusMonHoc: layTen(syllabusMonMap, item.syllabusMonHocId, [
                'ten',
                'tenSyllabusMonHocmau',
                'ma',
                'maSyllabusMonHocmau',
                'mucTieu'
            ])
        })),
        chuongTrinhMonQuyDoiDiemMau: duLieu.chuongTrinhMonQuyDoiDiemMau.map((item) => {
            const quyDoiDiemMauId = layGiaTriTheoKhoa(item, ['quyDoiDiemMauId', 'quy_doi_diem_mau_id', 'mauId'], null)
            const chuongTrinhMonId = layGiaTriTheoKhoa(item, ['chuongTrinhMonId', 'chuong_trinh_mon_id'], null)
            const syllabusMonHocId = layGiaTriTheoKhoa(item, ['syllabusMonHocId', 'syllabus_mon_hoc_id'], null)
            const mau = layDoiTuongLienKet(
                item,
                quyDoiDiemMauMap,
                ['quyDoiDiemMauId', 'quy_doi_diem_mau_id', 'mauId'],
                ['quyDoiDiemMau', 'quy_doi_diem_mau', 'mau', 'quyDoiDiemMauDto', 'quyDoiDiemMauDTO']
            )
            const maMau = layGiaTriTheoKhoa(mau, ['ma', 'maMau', 'ma_mau'], '')
            const tenMau = layGiaTriTheoKhoa(mau, ['ten', 'tenMau', 'ten_mau', 'tenCotDiem', 'ten_cot_diem'], maMau || '-')

            return {
                ...item,
                quyDoiDiemMauId,
                chuongTrinhMonId,
                syllabusMonHocId,
                tenSyllabusMonHoc: layTen(syllabusMonMap, syllabusMonHocId, [
                    'ten',
                    'tenSyllabusMonHocmau',
                    'ma',
                    'maSyllabusMonHocmau',
                    'mucTieu'
                ]),
                maQuyDoiDiemMau: maMau,
                maMau,
                tenQuyDoiDiemMau: tenMau,
                tenMau,
                loaiMau: layGiaTriTheoKhoa(mau, ['loaiMau', 'loai_mau', 'loai'], layGiaTriTheoKhoa(item, ['loaiMau', 'loai_mau', 'loai'], '')),
                nguongTu: layGiaTriTheoKhoa(mau, ['nguongTu', 'nguong_tu', 'tuDiem', 'tu_diem'], layGiaTriTheoKhoa(item, ['nguongTu', 'nguong_tu', 'tuDiem', 'tu_diem'], null)),
                nguongDen: layGiaTriTheoKhoa(mau, ['nguongDen', 'nguong_den', 'denDiem', 'den_diem'], layGiaTriTheoKhoa(item, ['nguongDen', 'nguong_den', 'denDiem', 'den_diem'], null)),
                diemQuyDoi: layGiaTriTheoKhoa(mau, ['diemQuyDoi', 'diem_quy_doi', 'diem', 'diemKetQua', 'diem_ket_qua'], layGiaTriTheoKhoa(item, ['diemQuyDoi', 'diem_quy_doi', 'diem', 'diemKetQua', 'diem_ket_qua'], null)),
                ketQua: layGiaTriTheoKhoa(mau, ['ketQua', 'ket_qua'], layGiaTriTheoKhoa(item, ['ketQua', 'ket_qua'], '')),
                tyLe: layGiaTriTheoKhoa(mau, ['tyLe', 'ty_le', 'tiLe', 'ti_le'], layGiaTriTheoKhoa(item, ['tyLe', 'ty_le', 'tiLe', 'ti_le'], null)),
                diemToiDa: layGiaTriTheoKhoa(mau, ['diemToiDa', 'diem_toi_da'], layGiaTriTheoKhoa(item, ['diemToiDa', 'diem_toi_da'], null)),
                thuTu: layGiaTriTheoKhoa(mau, ['thuTu', 'thu_tu'], layGiaTriTheoKhoa(item, ['thuTu', 'thu_tu'], null)),
                batBuoc: layGiaTriTheoKhoa(mau, ['batBuoc', 'bat_buoc'], layGiaTriTheoKhoa(item, ['batBuoc', 'bat_buoc'], null)),
                congThuc: layGiaTriTheoKhoa(mau, ['congThuc', 'cong_thuc'], layGiaTriTheoKhoa(item, ['congThuc', 'cong_thuc'], '')),
                ghiChuMau: layGiaTriTheoKhoa(mau, ['ghiChu', 'ghi_chu'], ''),
                tenChuongTrinhMon: layTen(chuongTrinhMonMap, chuongTrinhMonId, ['maMonTrongCt', 'tenMon'])
            }
        }),
        syllabusMonHoc: duLieu.syllabusMonHoc.map((item) => {
            const syllabusMonHocMauId = item.syllabusMonHocMauId ?? item.syllabusMonHocMauId ?? null
            return {
                ...item,
                syllabusMonHocMauId,
                tenChuongTrinhMon: layTen(chuongTrinhMonMap, item.chuongTrinhMonId, ['maMonTrongCt', 'tenMon']),
                tenSyllabusMonHocmau: layTen(syllabusMonHocmauMap, syllabusMonHocMauId, ['ten', 'ma']),
                maSyllabusMonHocmau: layTen(syllabusMonHocmauMap, syllabusMonHocMauId, ['ma']),
                tenMonHoc: layTen(monHocMap, item.monHocId, ['maMon', 'tenMon'])
            }
        }),
        syllabusMonHocmau: duLieu.syllabusMonHocmau.map((item) => ({
            ...item,
            tenMonHoc: layTen(monHocMap, item.monHocId, ['maMon', 'tenMon'])
        })),
        syllabusMonHocmauChuongBai: duLieu.syllabusMonHocmauChuongBai.map((item) => {
            const syllabusMonHocMauId = item.syllabusMonHocMauId ?? item.syllabusMonHocMauId ?? null
            return {
                ...item,
                syllabusMonHocMauId,
                tenSyllabusMonHocmau: layTen(syllabusMonHocmauMap, syllabusMonHocMauId, ['ma', 'ten'])
            }
        }),
        syllabusMonHocmauDieuKien: duLieu.syllabusMonHocmauDieuKien.map((item) => {
            const syllabusMonHocMauId = item.syllabusMonHocMauId ?? item.syllabusMonHocMauId ?? null
            const dieuKienmauId = item.dieuKienMauId ?? item.dieuKienmauId ?? null
            return {
                ...item,
                syllabusMonHocMauId,
                dieuKienmauId,
                tenSyllabusMonHocmau: layTen(syllabusMonHocmauMap, syllabusMonHocMauId, ['ma', 'ten']),
                tenDieuKienmau: layTen(dieuKienMonHocmauMap, dieuKienmauId, ['ma', 'noiDung'])
            }
        }),
        syllabusMonHocmauTaiLieu: duLieu.syllabusMonHocmauTaiLieu.map((item) => {
            const syllabusMonHocMauId = item.syllabusMonHocMauId ?? item.syllabusMonHocMauId ?? null
            const taiLieumauId = item.taiLieuMauId ?? item.taiLieumauId ?? null
            const taiLieu = taiLieumauMap.get(String(taiLieumauId || '')) || {}
            const ma = item.ma || item.maTaiLieu || taiLieu.ma || ''
            const ten = item.ten || item.tenTaiLieu || item.tenTaiLieumau || taiLieu.ten || ''
            const loai = item.loai || item.loaiTaiLieu || taiLieu.loai || ''
            const tacGia = item.tacGia || item.tacGiaTaiLieu || taiLieu.tacGia || ''
            const nhaXuatBan = item.nhaXuatBan || item.nhaXuatBanTaiLieu || taiLieu.nhaXuatBan || ''
            const namXuatBan = item.namXuatBan ?? item.namXuatBanTaiLieu ?? taiLieu.namXuatBan ?? null
            const duongDan = item.duongDan || item.duongDanTaiLieu || taiLieu.duongDan || ''
            return {
                ...item,
                syllabusMonHocMauId,
                taiLieumauId,
                ma,
                ten,
                loai,
                tacGia,
                nhaXuatBan,
                namXuatBan,
                duongDan,
                maTaiLieu: ma,
                tenTaiLieu: ten,
                loaiTaiLieu: loai,
                tacGiaTaiLieu: tacGia,
                nhaXuatBanTaiLieu: nhaXuatBan,
                namXuatBanTaiLieu: namXuatBan,
                duongDanTaiLieu: duongDan,
                tenSyllabusMonHocmau: layTen(syllabusMonHocmauMap, syllabusMonHocMauId, ['ma', 'ten']),
                tenTaiLieumau: ten || layTen(taiLieumauMap, taiLieumauId, ['ma', 'ten'])
            }
        }),
        dieuKienMonHoc: duLieu.dieuKienMonHoc.map((item) => ({
            ...item,
            tenSyllabusMon: layTen(syllabusMonMap, item.syllabusMonId, [
                'ten',
                'tenSyllabusMonHocmau',
                'ma',
                'maSyllabusMonHocmau',
                'mucTieu'
            ])
        })),
        dieuKienMonHocmau: duLieu.dieuKienMonHocmau.map((item) => ({...item})),
        syllabusMonHocDieuKien: duLieu.syllabusMonHocDieuKien.map((item) => ({
            ...item,
            tenSyllabusMon: layTen(syllabusMonMap, item.syllabusMonId, ['viTri', 'tinhChat', 'mucTieu']),
            tenDieuKienmau: layTen(dieuKienMonHocmauMap, item.dieuKienmauId, ['ma', 'noiDung'])
        })),
        taiLieumau: duLieu.taiLieumau.map((item) => ({...item})),
        syllabusMonHocTaiLieu: duLieu.syllabusMonHocTaiLieu.map((item) => ({
            ...item,
            tenSyllabusMon: layTen(syllabusMonMap, item.syllabusMonId, ['viTri', 'tinhChat', 'mucTieu']),
            tenTaiLieumau: layTen(taiLieumauMap, item.taiLieumauId, ['ma', 'ten'])
        })),
        syllabusChuongBai: duLieu.syllabusChuongBai.map((item) => ({
            ...item,
            tenSyllabusMon: layTen(syllabusMonMap, item.syllabusMonId, [
                'ten',
                'tenSyllabusMonHocmau',
                'ma',
                'maSyllabusMonHocmau',
                'mucTieu'
            ])
        })),
        syllabusTaiLieu: duLieu.syllabusTaiLieu.map((item) => ({
            ...item,
            tenSyllabusMon: layTen(syllabusMonMap, item.syllabusMonId, [
                'ten',
                'tenSyllabusMonHocmau',
                'ma',
                'maSyllabusMonHocmau',
                'mucTieu'
            ])
        }))
    }
}

function taoDuLieuRong() {
    return {
        nganh: [],
        trinhDoDaoTao: [],
        loaiChuongTrinh: [],
        nganhTrinhDoDaoTao: [],
        nganhLoaiChuongTrinh: [],
        chuongTrinh: [],
        chuongTrinhVersion: [],
        khungKy: [],
        khungKymau: [],
        monHoc: [],
        chuongTrinhMon: [],
        nhomKienThuc: [],
        nhomKienThucmau: [],
        nhomTuChon: [],
        nhomTuChonmau: [],
        monTuChon: [],
        mucTieuChuongTrinhmau: [],
        mucTieuChuongTrinh: [],
        nangLucDauRamau: [],
        nangLucDauRa: [],
        viTriViecLammau: [],
        viTriViecLam: [],
        dieuKienTotNghiepmau: [],
        dieuKienTotNghiep: [],
        chuongTrinhVersionMucTieu: [],
        chuongTrinhVersionNangLuc: [],
        chuongTrinhVersionViTriViecLam: [],
        chuongTrinhVersionDieuKienTotNghiep: [],
        syllabusChuongTrinh: [],
        syllabusChuongTrinhmau: [],
        monTienQuyet: [],
        quyDoiDiem: [],
        quyDoiDiemMau: [],
        chuongTrinhMonQuyDoiDiemMau: [],
        cauHinhDanhGiaMau: [],
        cauHinhDanhGia: [],
        syllabusMonHoc: [],
        syllabusMonHocmau: [],
        syllabusMonHocmauChuongBai: [],
        syllabusMonHocmauDieuKien: [],
        syllabusMonHocmauTaiLieu: [],
        syllabusMonHocmauFile: [],
        syllabusMonHocFile: [],
        dieuKienMonHoc: [],
        dieuKienMonHocmau: [],
        syllabusMonHocDieuKien: [],
        taiLieumau: [],
        syllabusMonHocTaiLieu: [],
        syllabusChuongBai: [],
        syllabusTaiLieu: []
    }
}

function taoSelectedRong() {
    return {
        nganh: null,
        trinhDoDaoTao: null,
        loaiChuongTrinh: null,
        nganhLoaiChuongTrinh: null,
        nganhHeDaoTao: null,
        chuongTrinh: null,
        chuongTrinhVersion: null,
        syllabusChuongTrinhmau: null,
        mucTieuChuongTrinhmau: null,
        mucTieuChuongTrinh: null,
        nangLucDauRamau: null,
        nangLucDauRa: null,
        viTriViecLammau: null,
        viTriViecLam: null,
        dieuKienTotNghiepmau: null,
        dieuKienTotNghiep: null,
        khungKy: null,
        khungKymau: null,
        nhomKienThuc: null,
        nhomKienThucmau: null,
        nhomTuChon: null,
        nhomTuChonmau: null,
        monHoc: null,
        chuongTrinhMon: null,
        quyDoiDiemMau: null,
        cauHinhDanhGiaMau: null,
        syllabusMonHoc: null,
        syllabusMonHocmau: null,
        dieuKienMonHocmau: null,
        dieuKienMonHoc: null,
        taiLieumau: null,
        syllabusTaiLieu: null
    }
}

function layDuLieuLuu(saved) {
    if (!saved) return null

    if (saved.id) {
        return saved
    }

    if (saved.success !== undefined && saved.data) {
        return saved.data
    }

    if (saved.data?.success !== undefined && saved.data?.data) {
        return saved.data.data
    }

    if (saved.data?.data?.id) {
        return saved.data.data
    }

    if (saved.data?.id) {
        return saved.data
    }

    return saved
}

export function useDaoTaoXemChuongTrinh(options = {}) {
    const {
        autoLoad = false
    } = options
    const thongBao = ref('')
    const loaiThongBao = ref('success')

    const selected = reactive(taoSelectedRong())
    const viewed = reactive(taoSelectedRong())
    const rawData = reactive(taoDuLieuRong())
    const tableMessages = reactive({})


    const khoQuyDoiDiemMaumau = ref([])

    const duLieu = computed(() => themTenLienKet(rawData))

    function laMauQuyDoiKetQua(row) {
        return String(row?.loaiMau || row?.loai_mau || '').trim().toUpperCase() === 'QUY_DOI_KET_QUA'
    }

    const quyDoiDiemMaumauRows = computed(() => {
        // Chỉ dùng dữ liệu BE trả về theo syllabusMonHocMauId đang chọn.
        // Không fallback sang kho toàn hệ thống để tránh bảng 3 hiển thị toàn ngành.
        return (duLieu.value.quyDoiDiemMau || []).filter(laMauQuyDoiKetQua)
    })

    const lookups = computed(() => ({
        ...duLieu.value,
        ...luaChonEnum
    }))

    function baoTinBang(key, message, type = 'success') {
        if (key) {
            tableMessages[key] = {message, type, at: Date.now()}
            setTimeout(() => {
                if (tableMessages[key]?.message === message) delete tableMessages[key]
            }, 5000)
        }
        baoTin(message, type)
    }

    function baoTin(message, type = 'success') {
        thongBao.value = message
        loaiThongBao.value = type

        setTimeout(() => {
            if (thongBao.value === message) thongBao.value = ''
        }, 5000)
    }


    async function taiDuLieuCoSanTatCaBang() {
        for (const key of bangLayDuLieuCoSan) {
            const service = daoTaoXemChuongTrinhService[key]
            if (!service?.getAll) continue

            try {
                const result = await service.getAll({size: 1000})
                const list = layDanhSachTuKetQua(result)
                rawData[key] = list
            } catch (error) {
                console.warn(`Không tải được dữ liệu có sẵn của bảng ${key}`, error)
            }
        }
    }
    async function taiDuLieuMauCrud(serviceKey) {
        const keys = [
            'nganh',
            'chuongTrinh',
            'syllabusChuongTrinhmau',
            serviceKey
        ]

        for (const key of keys) {
            const service = daoTaoXemChuongTrinhService[key]
            if (!service?.getAll) continue

            try {
                const result = await service.getAll({ size: 1000 })
                rawData[key] = layDanhSachTuKetQua(result)
            } catch (error) {
                console.warn(`Không tải được dữ liệu CRUD mẫu của bảng ${key}`, error)
            }
        }
    }
    async function taiDuLieuBangTheoParams(key, params = {}) {
        const service = daoTaoXemChuongTrinhService[key]
        if (!service?.getAll) return

        try {
            const result = await service.getAll({
                size: 200,
                ...params
            })

            const list = layDanhSachTuKetQua(result)

            // cau_hinh_danh_gia_mau vừa dùng để hiển thị cột điểm của syllabus đang áp dụng,
            // vừa cần giữ cột điểm của syllabus nguồn/gốc để copy. Vì vậy khi load theo
            // syllabusMonHocId thì không được ghi đè toàn bộ cache, chỉ thay phần cùng scope.
            if (key === 'cauHinhDanhGia' && params?.syllabusMonHocId) {
                const scopeId = String(params.syllabusMonHocId || '')
                rawData[key] = [
                    ...(rawData[key] || []).filter((row) => String(row.syllabusMonHocId || '') !== scopeId),
                    ...list
                ]
                return
            }

            rawData[key] = list
        } catch (error) {
            console.warn(`Không tải được dữ liệu bảng ${key} theo params`, error)
        }
    }

    async function taiDuLieuTheoMonTrongChuongTrinh(chuongTrinhMonId) {
        if (!chuongTrinhMonId) {
            rawData.chuongTrinhMonQuyDoiDiemMau = []
            rawData.quyDoiDiem = []
            rawData.quyDoiDiemMau = []
            rawData.cauHinhDanhGiaMau = []
            rawData.cauHinhDanhGia = []
            return
        }

        // Sau khi chuyển nghiệp vụ sang syllabus:
        // Môn trong chương trình chỉ dùng để tìm/chọn syllabus áp dụng.
        // Quy đổi điểm / cột điểm mẫu / mẫu quy đổi đã gán sẽ được load theo selected.syllabusMonHoc.
        if (!selected.syllabusMonHoc?.id) {
            rawData.chuongTrinhMonQuyDoiDiemMau = []
            rawData.quyDoiDiem = []
            rawData.quyDoiDiemMau = []
            rawData.cauHinhDanhGiaMau = []
            rawData.cauHinhDanhGia = []
        }
    }


    async function taiDuLieuTongQuanVersion(chuongTrinhVersionId = null) {
        const versionId = chuongTrinhVersionId || selected.chuongTrinhVersion?.id
        if (!versionId) return

        const syllabusResult = await daoTaoXemChuongTrinhService.syllabusChuongTrinh.getAll({
            size: 20,
            chuongTrinhVersionId: versionId
        })
        const syllabusRows = syllabusResult?.items || []
        rawData.syllabusChuongTrinh = [
            ...(rawData.syllabusChuongTrinh || []).filter((row) => String(row.chuongTrinhVersionId || '') !== String(versionId || '')),
            ...syllabusRows
        ]

        const syllabusChuongTrinhId = syllabusRows[0]?.id || null
        if (!syllabusChuongTrinhId) {
            rawData.mucTieuChuongTrinh = []
            rawData.nangLucDauRa = []
            rawData.viTriViecLam = []
            rawData.dieuKienTotNghiep = []
            return
        }

        const bangCon = [
            'mucTieuChuongTrinh',
            'nangLucDauRa',
            'viTriViecLam',
            'dieuKienTotNghiep'
        ]

        await Promise.all(bangCon.map(async (key) => {
            const service = daoTaoXemChuongTrinhService[key]
            if (!service?.getAll) return

            const result = await service.getAll({
                size: 1000,
                syllabusChuongTrinhId
            })
            const rows = result?.items || []

            rawData[key] = [
                ...(rawData[key] || []).filter((row) => String(row.syllabusChuongTrinhId || '') !== String(syllabusChuongTrinhId || '')),
                ...rows
            ]
        }))
    }

    function layMangResponse(res) {
        if (Array.isArray(res)) return res

        const payload = res?.data

        if (Array.isArray(payload)) return payload
        if (Array.isArray(payload?.data)) return payload.data
        if (Array.isArray(payload?.content)) return payload.content
        if (Array.isArray(payload?.items)) return payload.items
        if (Array.isArray(payload?.result)) return payload.result
        if (Array.isArray(payload?.data?.content)) return payload.data.content
        if (Array.isArray(payload?.data?.items)) return payload.data.items

        return []
    }
    async function taiDuLieuTheoSyllabusMonHocApDung(syllabusMonHoc = null) {
        const syllabusMonId = syllabusMonHoc?.id || null
        const syllabusMonHocMauId =
            syllabusMonHoc?.syllabusMonHocMauId ||
            syllabusMonHoc?.syllabus_mon_hoc_mau_id ||
            syllabusMonHoc?.mauId ||
            syllabusMonHoc?.syllabusMauId ||
            selected.syllabusMonHocmau?.id ||
            null

        if (!syllabusMonId) {
            rawData.syllabusChuongBai = []
            rawData.dieuKienMonHoc = []
            rawData.syllabusTaiLieu = []
            rawData.syllabusMonHocDieuKien = []
            rawData.syllabusMonHocTaiLieu = []
            rawData.syllabusMonHocFile = []
            rawData.syllabusMonHocmauFile = []

            rawData.chuongTrinhMonQuyDoiDiemMau = []
            rawData.quyDoiDiem = []
            rawData.quyDoiDiemMau = []
            rawData.cauHinhDanhGiaMau = []
            rawData.cauHinhDanhGia = []
            return
        }

        const jobs = [
            taiDuLieuBangTheoParams('syllabusChuongBai', {syllabusMonId}),
            taiDuLieuBangTheoParams('dieuKienMonHoc', {syllabusMonId}),
            taiDuLieuBangTheoParams('syllabusTaiLieu', {syllabusMonId}),
            taiDuLieuBangTheoParams('syllabusMonHocDieuKien', {syllabusMonId}),
            taiDuLieuBangTheoParams('syllabusMonHocTaiLieu', {syllabusMonId}),

            // BE mới nhận syllabusMonHocId cho 3 nhóm này.
            taiDuLieuBangTheoParams('cauHinhDanhGia', {syllabusMonHocId: syllabusMonId}),
            taiDuLieuBangTheoParams('quyDoiDiem', {syllabusMonHocId: syllabusMonId}),
            taiDuLieuBangTheoParams('chuongTrinhMonQuyDoiDiemMau', {syllabusMonHocId: syllabusMonId}),

            taiDuLieuBangTheoParams('taiLieumau', {size: 1000}),
            taiDuLieuBangTheoParams('dieuKienMonHocmau', {size: 1000})
        ]

        if (syllabusMonHocMauId) {
            // Hai bảng này bắt buộc load theo syllabus_mon_hoc_mau_id.
            // Không gọi GET không tham số vì BE đã chặn trả toàn ngành.
            jobs.push(taiDuLieuBangTheoParams('quyDoiDiemMau', {syllabusMonHocMauId: syllabusMonHocMauId}))
            jobs.push(taiDuLieuBangTheoParams('cauHinhDanhGiaMau', {syllabusMonHocMauId: syllabusMonHocMauId}))
            jobs.push(taiDuLieuBangTheoParams('syllabusMonHocmauChuongBai', {syllabusMonHocMauId: syllabusMonHocMauId}))
            jobs.push(taiDuLieuBangTheoParams('syllabusMonHocmauDieuKien', {syllabusMonHocMauId: syllabusMonHocMauId}))
            jobs.push(taiDuLieuBangTheoParams('syllabusMonHocmauTaiLieu', {syllabusMonHocMauId: syllabusMonHocMauId}))
            jobs.push(
                daoTaoXemChuongTrinhService.syllabusMonHocMauFile.list(syllabusMonHocMauId)
                    .then(res => { rawData.syllabusMonHocmauFile = layMangResponse(res) })
                    .catch(() => { rawData.syllabusMonHocmauFile = [] })
            )
        } else {
            rawData.quyDoiDiemMau = []
            rawData.cauHinhDanhGiaMau = []
            rawData.syllabusMonHocmauChuongBai = []
            rawData.syllabusMonHocmauDieuKien = []
            rawData.syllabusMonHocmauTaiLieu = []
            rawData.syllabusMonHocmauFile = []
        }

        jobs.push(
            daoTaoXemChuongTrinhService.syllabusMonHocFile.list(syllabusMonId)
                .then(res => { rawData.syllabusMonHocFile = layMangResponse(res) })
                .catch(() => { rawData.syllabusMonHocFile = [] })
        )

        await Promise.all(jobs)

        const sortThuTu = (a, b) => (a.thuTu ?? 999999) - (b.thuTu ?? 999999) || (a.id ?? 0) - (b.id ?? 0)
        rawData.syllabusChuongBai = [...(rawData.syllabusChuongBai || [])].sort(sortThuTu)
        rawData.syllabusMonHocmauChuongBai = [...(rawData.syllabusMonHocmauChuongBai || [])].sort(sortThuTu)
    }

    watch(
        () => selected.chuongTrinhMon?.id || null,
        (chuongTrinhMonId) => {
            taiDuLieuTheoMonTrongChuongTrinh(chuongTrinhMonId)
        }
    )

    watch(
        () => selected.syllabusMonHoc ? `${selected.syllabusMonHoc.id || ''}|${selected.syllabusMonHoc.syllabusMonHocMauId || ''}` : '',
        () => {
            taiDuLieuTheoSyllabusMonHocApDung(selected.syllabusMonHoc)
        }
    )


    const selectionChildren = {
        nganh: ['trinhDoDaoTao', 'loaiChuongTrinh', 'nganhLoaiChuongTrinh', 'nganhHeDaoTao', 'chuongTrinh', 'chuongTrinhVersion', 'khungKy', 'nhomKienThuc', 'nhomTuChon', 'monHoc', 'chuongTrinhMon', 'syllabusMonHoc'],
        trinhDoDaoTao: ['chuongTrinh', 'chuongTrinhVersion', 'khungKy', 'nhomKienThuc', 'nhomTuChon', 'monHoc', 'chuongTrinhMon', 'syllabusMonHoc'],
        loaiChuongTrinh: ['nganhLoaiChuongTrinh', 'chuongTrinh', 'chuongTrinhVersion', 'khungKy', 'nhomKienThuc', 'nhomTuChon', 'monHoc', 'chuongTrinhMon', 'syllabusMonHoc'],
        nganhLoaiChuongTrinh: ['chuongTrinh', 'chuongTrinhVersion', 'khungKy', 'nhomKienThuc', 'nhomTuChon', 'monHoc', 'chuongTrinhMon', 'syllabusMonHoc'],
        nganhHeDaoTao: ['chuongTrinh', 'chuongTrinhVersion', 'khungKy', 'nhomKienThuc', 'nhomTuChon', 'monHoc', 'chuongTrinhMon', 'syllabusMonHoc'],
        chuongTrinh: ['chuongTrinhVersion', 'khungKy', 'nhomKienThuc', 'nhomTuChon', 'monHoc', 'chuongTrinhMon', 'syllabusMonHoc'],
        chuongTrinhVersion: ['syllabusChuongTrinhmau', 'syllabusChuongTrinh', 'mucTieuChuongTrinhmau', 'mucTieuChuongTrinh', 'nangLucDauRamau', 'nangLucDauRa', 'viTriViecLammau', 'viTriViecLam', 'dieuKienTotNghiepmau', 'dieuKienTotNghiep', 'khungKymau', 'khungKy', 'nhomKienThucmau', 'nhomKienThuc', 'nhomTuChonmau', 'nhomTuChon', 'monHoc', 'chuongTrinhMon', 'monTuChon', 'monTienQuyet', 'quyDoiDiemMau', 'quyDoiDiem', 'chuongTrinhMonQuyDoiDiemMau', 'syllabusMonHocmau', 'syllabusMonHoc'],
        khungKymau: ['khungKy', 'chuongTrinhMon'],
        khungKy: ['chuongTrinhMon', 'syllabusMonHoc'],
        nhomKienThucmau: ['nhomKienThuc', 'chuongTrinhMon'],
        nhomKienThuc: ['chuongTrinhMon', 'syllabusMonHoc'],
        nhomTuChonmau: ['nhomTuChon'],
        nhomTuChon: [],
        monHoc: ['syllabusMonHocmau', 'chuongTrinhMon', 'syllabusMonHoc'],
        chuongTrinhMon: ['quyDoiDiemMau', 'quyDoiDiem', 'chuongTrinhMonQuyDoiDiemMau', 'cauHinhDanhGiaMau', 'cauHinhDanhGia', 'monTienQuyet', 'monTuChon', 'syllabusMonHoc'],
        syllabusMonHocmau: ['syllabusMonHoc'],
        syllabusMonHoc: ['dieuKienMonHocmau', 'taiLieumau'],
        dieuKienMonHocmau: [],
        taiLieumau: []
    }

    function xoaLuaChonCon(key) {
        const children = selectionChildren[key] || []
        children.forEach((childKey) => {
            if (childKey in selected) selected[childKey] = null
        })
    }

    function xoaXemCon(key) {
        const children = selectionChildren[key] || []
        children.forEach((childKey) => {
            if (childKey in viewed) viewed[childKey] = null
        })
    }

    function selectEntity(key, item) {
        const currentId = selected[key]?.id || null
        const nextId = item?.id || null

        selected[key] = item

        if (String(currentId || '') !== String(nextId || '')) {
            if (key in viewed) viewed[key] = null
            xoaLuaChonCon(key)
            xoaXemCon(key)
        }
    }

    function viewEntity(key, item) {
        const currentId = viewed[key]?.id || null
        const nextId = item?.id || null

        if (!item || String(currentId || '') === String(nextId || '')) {
            if (key in viewed) viewed[key] = null
            xoaXemCon(key)
            return
        }

        viewed[key] = item
        xoaXemCon(key)
    }


    function capNhatDongTrongRawData(key, row) {
        if (!row?.id || !Array.isArray(rawData[key])) return row

        const index = rawData[key].findIndex((item) => String(item.id || '') === String(row.id || ''))
        if (index >= 0) rawData[key].splice(index, 1, row)
        else rawData[key].push(row)

        if (key === 'quyDoiDiemMau') {
            const cacheIndex = khoQuyDoiDiemMaumau.value.findIndex((item) => String(item.id || '') === String(row.id || ''))
            if (cacheIndex >= 0) khoQuyDoiDiemMaumau.value.splice(cacheIndex, 1, {...row})
            else khoQuyDoiDiemMaumau.value.push({...row})
        }

        return duLieu.value[key]?.find((item) => String(item.id || '') === String(row.id || '')) || row
    }


    function xoaDongTrongRawData(key, id) {
        if (!id || !Array.isArray(rawData[key])) return
        const index = rawData[key].findIndex((item) => String(item.id || '') === String(id || ''))
        if (index >= 0) rawData[key].splice(index, 1)
    }

    function coGiaTri(value) {
        return value !== null && value !== undefined && value !== ''
    }

    function laySyllabusMonHocIdDangChon(parentValues = {}) {
        return selected.syllabusMonHoc?.id
            || parentValues.syllabusMonHocId
            || parentValues.syllabusMonId
            || null
    }

    const bangmauGanVersion = {

        nangLucDauRamau: {
            joinKey: 'chuongTrinhVersionNangLuc',
            mauIdKey: 'nangLucmauId',
            serviceKey: 'chuongTrinhVersionNangLuc',
            tenBang: 'Năng lực đầu ra'
        },
        viTriViecLammau: {
            joinKey: 'chuongTrinhVersionViTriViecLam',
            mauIdKey: 'viTrimauId',
            serviceKey: 'chuongTrinhVersionViTriViecLam',
            tenBang: 'Vị trí việc làm'
        },

    }

    const bangmauTaoApDungVersion = {

        dieuKienTotNghiepmau: {
            joinKey: 'dieuKienTotNghiep',
            mauIdKey: 'ma',
            matchBy: {rowKey: 'ma', itemKey: 'ma'},
            serviceKey: 'dieuKienTotNghiep',
            tenBang: 'Điều kiện tốt nghiệp gốc',
            buildPayload: (item, chuongTrinhVersionId) => ({
                chuongTrinhVersionId,
                ma: item.ma || '',
                noiDung: item.noiDung || '',
                thuTu: layThuTuGanTiepTheo('dieuKienTotNghiep', chuongTrinhVersionId),
                ghiChu: item.ghiChu || ''
            })
        },

        mucTieuChuongTrinhmau: {
            joinKey: 'mucTieuChuongTrinh',
            mauIdKey: 'ma',
            matchBy: {rowKey: 'ma', itemKey: 'ma'},
            serviceKey: 'mucTieuChuongTrinh',
            tenBang: 'Mục tiêu chương trình gốc',
            buildPayload: (item, chuongTrinhVersionId) => ({
                syllabusChuongTrinhId: laySyllabusChuongTrinhIdTheoVersion(chuongTrinhVersionId),
                chuongTrinhVersionId,
                ma: item.ma || '',
                loai: item.loai || 'chung',
                noiDung: item.noiDung || '',
                thuTu: layThuTuGanTiepTheo('mucTieuChuongTrinh', chuongTrinhVersionId),
                ghiChu: item.ghiChu || ''
            })
        },

        nangLucDauRamau: {
            joinKey: 'nangLucDauRa',
            mauIdKey: 'ma',
            matchBy: {rowKey: 'ma', itemKey: 'ma'},
            serviceKey: 'nangLucDauRa',
            tenBang: 'Năng lực đầu ra gốc',
            buildPayload: (item, chuongTrinhVersionId) => ({
                syllabusChuongTrinhId: laySyllabusChuongTrinhIdTheoVersion(chuongTrinhVersionId),
                chuongTrinhVersionId,
                ma: item.ma || '',
                loai: item.loai || 'co_ban',
                noiDung: item.noiDung || '',
                thuTu: layThuTuGanTiepTheo('nangLucDauRa', chuongTrinhVersionId),
                ghiChu: item.ghiChu || ''
            })
        },

        viTriViecLammau: {
            joinKey: 'viTriViecLam',
            mauIdKey: 'ma',
            matchBy: {rowKey: 'ma', itemKey: 'ma'},
            serviceKey: 'viTriViecLam',
            tenBang: 'Vị trí việc làm gốc',
            buildPayload: (item, chuongTrinhVersionId) => ({
                syllabusChuongTrinhId: laySyllabusChuongTrinhIdTheoVersion(chuongTrinhVersionId),
                chuongTrinhVersionId,
                ma: item.ma || '',
                ten: item.ten || '',
                moTa: item.moTa || '',
                thuTu: layThuTuGanTiepTheo('viTriViecLam', chuongTrinhVersionId),
                ghiChu: item.ghiChu || ''
            })
        },

        syllabusChuongTrinhmau: {
            joinKey: 'syllabusChuongTrinh',
            mauIdKey: 'syllabusChuongTrinhmauId',
            matchBy: {rowKey: 'syllabusChuongTrinhmauId', itemKey: 'id'},
            serviceKey: 'syllabusChuongTrinh',
            tenBang: 'Syllabus chương trình gốc',
            buildPayload: (item, chuongTrinhVersionId) => ({
                chuongTrinhVersionId,
                syllabusChuongTrinhmauId: item.id,
                ma: item.ma || '',
                ten: item.ten || '',
                mucTieu: item.mucTieu || '',
                doiTuongTuyenSinh: item.doiTuongTuyenSinh || '',
                thoiGianDaoTao: item.thoiGianDaoTao || '',
                khoiLuongKienThuc: item.khoiLuongKienThuc || '',
                dieuKienTotNghiep: item.dieuKienTotNghiep || '',
                phuongPhapDaoTao: item.phuongPhapDaoTao || '',
                phuongPhapDanhGia: item.phuongPhapDanhGia || '',
                huongDanThucHien: item.huongDanThucHien || '',
                duongDan: item.duongDan || '',
                ghiChu: item.ghiChu || '',
                moTaTongQuan: item.mucTieu || '',
                mucDich: item.mucTieu || '',
                yeuCauDaoTao: item.doiTuongTuyenSinh || ''
            })
        },
        khungKymau: {
            joinKey: 'khungKy',
            mauIdKey: 'khungKymauId',
            serviceKey: 'khungKy',
            tenBang: 'Khung kỳ gốc',
            buildPayload: (item, chuongTrinhVersionId) => ({
                chuongTrinhVersionId,
                loaiChuongTrinhId: layLoaiChuongTrinhDangChon(),
                khungKymauId: item.id,
                maKy: item.maKy || item.ma || '',
                tenKy: item.tenKy || item.ten || '',
                thuTu: item.thuTu || layThuTuGanTiepTheo('khungKy', chuongTrinhVersionId)
            })
        },
        nhomKienThucmau: {
            joinKey: 'nhomKienThuc',
            mauIdKey: 'nhomKienThucmauId',
            serviceKey: 'nhomKienThuc',
            tenBang: 'Nhóm kiến thức gốc',
            buildPayload: (item, chuongTrinhVersionId) => ({
                chuongTrinhVersionId,
                nhomKienThucmauId: item.id,
                ma: item.ma || '',
                ten: item.ten || '',
                loaiNhom: item.loaiNhom || 'chung',
                thuTu: item.thuTu || layThuTuGanTiepTheo('nhomKienThuc', chuongTrinhVersionId),
                moTa: item.moTa || ''
            })
        },
        nhomTuChonmau: {
            joinKey: 'nhomTuChon',
            mauIdKey: 'nhomTuChonmauId',
            serviceKey: 'nhomTuChon',
            tenBang: 'Nhóm tự chọn gốc',
            buildPayload: (item, chuongTrinhVersionId) => ({
                chuongTrinhVersionId,
                nhomTuChonmauId: item.id,
                ma: item.ma || '',
                ten: item.ten || '',
                soMonChon: item.soMonChon ?? null,
                soTinChiCanDat: item.soTinChiCanDat ?? null,
                ghiChu: item.ghiChu || '',
                moTa: item.moTa || ''
            })
        },
    }

    const bangMauGanMon = {
        quyDoiDiemMau: {
            joinKey: 'chuongTrinhMonQuyDoiDiemMau',
            mauIdKey: 'quyDoiDiemMauId',
            syllabusIdKey: 'syllabusMonHocId',
            serviceKey: 'chuongTrinhMonQuyDoiDiemMau',
            tenBang: 'Mẫu quy đổi kết quả'
        }
    }

    function timChuongTrinhMonTheoMonHocmau(item, parentValues = {}) {
        const chuongTrinhVersionId = parentValues.chuongTrinhVersionId || selected.chuongTrinhVersion?.id
        const khungKyId = parentValues.khungKyId || selected.khungKy?.id
        const nhomKienThucId = parentValues.nhomKienThucId || selected.nhomKienThuc?.id || null

        return (rawData.chuongTrinhMon || []).find((row) => {
            const cungVersion = String(row.chuongTrinhVersionId || '') === String(chuongTrinhVersionId || '')
            const cungMonHoc = String(row.monHocId || row.monId || '') === String(item?.id || '')
            const cungKhungKy = String(row.khungKyId || '') === String(khungKyId || '')
            const cungNhomKienThuc = nhomKienThucId
                ? String(row.nhomKienThucId || '') === String(nhomKienThucId || '')
                : !row.nhomKienThucId

            return cungVersion && cungMonHoc && cungKhungKy && cungNhomKienThuc
        }) || null
    }

    function layThuTuChuongTrinhMonTiepTheo(chuongTrinhVersionId) {
        const list = rawData.chuongTrinhMon || []
        const thuTuLonNhat = list
            .filter((row) => String(row.chuongTrinhVersionId || '') === String(chuongTrinhVersionId || ''))
            .map((row) => Number(row.thuTu || 0))
            .filter((value) => !Number.isNaN(value))
            .reduce((max, value) => Math.max(max, value), 0)

        return thuTuLonNhat + 1
    }

    function laySoTuNhieuTen(item, keys = [], fallback = null) {
        for (const key of keys) {
            const value = item?.[key]
            if (value !== null && value !== undefined && value !== '') {
                const numberValue = Number(value)
                return Number.isNaN(numberValue) ? value : numberValue
            }
        }

        return fallback
    }

    function taoPayloadChuongTrinhMonTuMonHocmau(item, parentValues = {}) {
        const chuongTrinhVersionId = parentValues.chuongTrinhVersionId || selected.chuongTrinhVersion?.id
        const khungKyId = parentValues.khungKyId || selected.khungKy?.id
        const nhomKienThucId = parentValues.nhomKienThucId || selected.nhomKienThuc?.id || null

        return {
            chuongTrinhVersionId,
            monHocId: item.id,
            maMonTrongCt: item.maMon || item.ma || `MON_${item.id}`,
            khungKyId,
            nhomKienThucId,

            loai: item.loai || 'bat_buoc',
            loaiHocPhan: item.loaiHocPhan || item.loai_hoc_phan || 'mon_hoc',
            loaiPhamVi: item.loaiPhamVi || item.loai_pham_vi || 'mon_chuyen_nganh',
            batBuoc: item.batBuoc ?? item.bat_buoc ?? true,
            laMonDieuKien: item.laMonDieuKien ?? item.la_mon_dieu_kien ?? false,

            thuTu: layThuTuChuongTrinhMonTiepTheo(chuongTrinhVersionId),

            soTinChi: laySoTuNhieuTen(item, ['soTinChi', 'so_tin_chi', 'tinChi', 'tin_chi']),
            tongGio: laySoTuNhieuTen(item, ['tongGio', 'tong_gio', 'soGio', 'so_gio', 'tongSoGio', 'tong_so_gio']),
            gioLyThuyet: laySoTuNhieuTen(item, ['gioLyThuyet', 'gio_ly_thuyet', 'lyThuyet', 'ly_thuyet']),
            gioThucHanh: laySoTuNhieuTen(item, ['gioThucHanh', 'gio_thuc_hanh', 'thucHanh', 'thuc_hanh']),
            gioKiemTra: laySoTuNhieuTen(item, ['gioKiemTra', 'gio_kiem_tra', 'kiemTra', 'kiem_tra']),

            ghiChu: item.ghiChu || item.ghi_chu || item.moTa || item.mo_ta || ''
        }
    }

    // Tầng 7: dùng MÔN ĐÃ NẰM TRONG VERSION (chuong_trinh_mon) rồi gán vào kỳ.
    // "item" là môn học gốc (item.id = monHocId). Môn đã được copy vào version từ trước,
    // nên ở đây chỉ cập nhật khung_ky_id của dòng chuong_trinh_mon đã có (không tạo dòng mới).
    async function toggleGanMonHocmauVaoChuongTrinhMon(item, parentValues = {}) {
        const chuongTrinhVersionId = parentValues.chuongTrinhVersionId || selected.chuongTrinhVersion?.id
        const khungKyId = parentValues.khungKyId || selected.khungKy?.id

        if (!chuongTrinhVersionId) {
            baoTinBang('monHoc', 'Cần chọn Version trước khi gán môn vào kỳ.', 'error')
            return
        }

        if (!khungKyId) {
            baoTinBang('monHoc', 'Cần chọn Khung kỳ trước khi gán môn vào kỳ.', 'error')
            return
        }

        const serviceChuongTrinhMon = daoTaoXemChuongTrinhService.chuongTrinhMon
        if (!serviceChuongTrinhMon?.update) {
            baoTinBang('monHoc', 'Chưa khai báo API cập nhật chương trình môn.', 'error')
            return
        }

        // Tìm môn đã nằm trong version (bất kể đang ở kỳ nào) theo monHocId.
        const monTrongVersion = (rawData.chuongTrinhMon || []).find((row) => {
            const cungVersion = String(row.chuongTrinhVersionId || '') === String(chuongTrinhVersionId || '')
            const cungMonHoc = String(row.monHocId || row.monId || '') === String(item?.id || '')
            return cungVersion && cungMonHoc
        })

        if (!monTrongVersion?.id) {
            baoTinBang('monHoc', 'Môn này chưa nằm trong version. Hãy copy môn vào version ở trang "Nhóm kiến thức và môn" trước.', 'error')
            return
        }

        const dangOKyNay = String(monTrongVersion.khungKyId || '') === String(khungKyId || '')
        const khungKyIdMoi = dangOKyNay ? null : khungKyId

        const payload = {
            chuongTrinhVersionId: monTrongVersion.chuongTrinhVersionId,
            monHocId: monTrongVersion.monHocId || monTrongVersion.monId,
            maMonTrongCt: monTrongVersion.maMonTrongCt,
            khungKyId: khungKyIdMoi,
            nhomKienThucId: monTrongVersion.nhomKienThucId ?? null,
            loai: monTrongVersion.loai || 'bat_buoc',
            loaiHocPhan: monTrongVersion.loaiHocPhan || 'mon_hoc',
            loaiPhamVi: monTrongVersion.loaiPhamVi || 'mon_chuyen_nganh',
            batBuoc: monTrongVersion.batBuoc !== false,
            laMonDieuKien: monTrongVersion.laMonDieuKien === true,
            thuTu: monTrongVersion.thuTu ?? null,
            soTinChi: monTrongVersion.soTinChi ?? null,
            tongGio: monTrongVersion.tongGio ?? null,
            gioLyThuyet: monTrongVersion.gioLyThuyet ?? null,
            gioThucHanh: monTrongVersion.gioThucHanh ?? null,
            gioKiemTra: monTrongVersion.gioKiemTra ?? null,
            ghiChu: monTrongVersion.ghiChu || ''
        }

        try {
            const saved = await serviceChuongTrinhMon.update(monTrongVersion.id, payload)
            const row = layDuLieuLuu(saved) || {...monTrongVersion, khungKyId: khungKyIdMoi}
            const rowDaLuu = capNhatDongTrongRawData('chuongTrinhMon', row)

            selectEntity('monHoc', item)
            selectEntity('chuongTrinhMon', rowDaLuu)
            baoTinBang('monHoc', dangOKyNay ? 'Đã bỏ môn khỏi kỳ hiện tại.' : 'Đã gán môn vào kỳ.')
        } catch (error) {
            const message = layThongBaoLoi(error, 'Không gán/bỏ gán được môn vào kỳ.')
            baoTinBang('monHoc', message, 'error')
        }
    }


    function chuanHoaGiaTriQuyDoi(value) {
        if (value === null || value === undefined || value === '') return null
        const numberValue = Number(value)
        if (Number.isNaN(numberValue)) return String(value)
        return Number(numberValue.toFixed(2))
    }

    function taoPayloadQuyDoiDiemTuMau(item, syllabusMonHocId) {
        return {
            syllabusMonHocId,
            loaiMau: item.loaiMau || 'COT_DIEM',
            nguongTu: item.nguongTu ?? null,
            nguongDen: item.nguongDen ?? null,
            diemQuyDoi: item.diemQuyDoi ?? null,
            ketQua: item.ketQua || 'dat',
            tyLe: item.tyLe ?? null,
            diemToiDa: item.diemToiDa ?? 10,
            thuTu: item.thuTu ?? null,
            batBuoc: item.batBuoc !== false,
            congThuc: item.congThuc || '',
            ghiChu: item.ghiChu || item.ten || item.ma || ''
        }
    }

    function khopQuyDoiDiemTuMau(row, payload, syllabusMonHocId) {
        return String(row.syllabusMonHocId || '') === String(syllabusMonHocId || '')
            && String(row.loaiMau || 'COT_DIEM') === String(payload.loaiMau || 'COT_DIEM')
            && String(chuanHoaGiaTriQuyDoi(row.nguongTu)) === String(chuanHoaGiaTriQuyDoi(payload.nguongTu))
            && String(chuanHoaGiaTriQuyDoi(row.nguongDen)) === String(chuanHoaGiaTriQuyDoi(payload.nguongDen))
            && String(chuanHoaGiaTriQuyDoi(row.diemQuyDoi)) === String(chuanHoaGiaTriQuyDoi(payload.diemQuyDoi))
            && String(row.ketQua || 'dat') === String(payload.ketQua || 'dat')
            && String(chuanHoaGiaTriQuyDoi(row.tyLe)) === String(chuanHoaGiaTriQuyDoi(payload.tyLe))
            && String(chuanHoaGiaTriQuyDoi(row.diemToiDa)) === String(chuanHoaGiaTriQuyDoi(payload.diemToiDa))
            && String(row.thuTu || '') === String(payload.thuTu || '')
            && String(row.batBuoc !== false) === String(payload.batBuoc !== false)
            && String(row.congThuc || '') === String(payload.congThuc || '')
    }

    function timCacQuyDoiDiemDaCoTuMau(item, syllabusMonHocId) {
        const payload = taoPayloadQuyDoiDiemTuMau(item, syllabusMonHocId)
        const list = rawData.quyDoiDiem || []
        return list.filter((row) => khopQuyDoiDiemTuMau(row, payload, syllabusMonHocId))
    }

    function timQuyDoiDiemDaCoTuMau(item, syllabusMonHocId) {
        return timCacQuyDoiDiemDaCoTuMau(item, syllabusMonHocId)[0] || null
    }

    async function dayQuyDoiDiemMauXuongQuyDoiDiem(item, syllabusMonHocId) {
        if (!item?.id || !syllabusMonHocId) return {created: false, existed: false}

        const serviceQuyDoiDiem = daoTaoXemChuongTrinhService.quyDoiDiem
        if (!serviceQuyDoiDiem?.create) {
            throw new Error('Chưa khai báo API tạo Quy đổi điểm.')
        }

        const existing = timQuyDoiDiemDaCoTuMau(item, syllabusMonHocId)
        if (existing?.id) {
            capNhatDongTrongRawData('quyDoiDiem', existing)
            return {created: false, existed: true, row: existing}
        }

        const payload = taoPayloadQuyDoiDiemTuMau(item, syllabusMonHocId)
        const saved = await serviceQuyDoiDiem.create(payload)
        const row = layDuLieuLuu(saved) || payload
        capNhatDongTrongRawData('quyDoiDiem', row)
        return {created: true, existed: false, row}
    }

    async function xoaQuyDoiDiemDaDayTuMau(item, syllabusMonHocId) {
        if (!item?.id || !syllabusMonHocId) return {deleted: 0}

        const serviceQuyDoiDiem = daoTaoXemChuongTrinhService.quyDoiDiem
        if (!serviceQuyDoiDiem?.delete) {
            throw new Error('Chưa khai báo API xóa Quy đổi điểm.')
        }

        const rowsCanXoa = timCacQuyDoiDiemDaCoTuMau(item, syllabusMonHocId).filter((row) => row?.id)
        let deleted = 0

        for (const row of rowsCanXoa) {
            await serviceQuyDoiDiem.delete(row.id)
            xoaDongTrongRawData('quyDoiDiem', row.id)
            deleted += 1
        }

        return {deleted}
    }
    function taoPayloadQuyDoiDiemTuCotDiemMau(item, syllabusMonHocId) {
        return {
            syllabusMonHocId,
            loaiMau: item.loaiDiem || 'COT_DIEM',
            nguongTu: null,
            nguongDen: null,
            diemQuyDoi: null,
            ketQua: 'dat',
            tyLe: item.tyLe ?? null,
            diemToiDa: item.diemToiDa ?? 10,
            thuTu: item.thuTu ?? null,
            batBuoc: item.batBuoc !== false,
            congThuc: '',
            ghiChu: item.tenCotDiem || item.ghiChu || ''
        }
    }

    function khopQuyDoiDiemTuCotDiemMau(row, payload, syllabusMonHocId) {
        return String(row.syllabusMonHocId || '') === String(syllabusMonHocId || '')
            && String(row.ma || '') === String(payload.ma || '')
            && String(row.ten || '') === String(payload.ten || '')
            && String(row.loaiMau || 'COT_DIEM') === String(payload.loaiMau || 'COT_DIEM')
            && String(chuanHoaGiaTriQuyDoi(row.tyLe)) === String(chuanHoaGiaTriQuyDoi(payload.tyLe))
            && String(chuanHoaGiaTriQuyDoi(row.diemToiDa)) === String(chuanHoaGiaTriQuyDoi(payload.diemToiDa))
            && String(row.thuTu || '') === String(payload.thuTu || '')
            && String(row.batBuoc !== false) === String(payload.batBuoc !== false)
    }

    function timQuyDoiDiemDaCoTuCotDiemMau(item, syllabusMonHocId) {
        const payload = taoPayloadQuyDoiDiemTuCotDiemMau(item, syllabusMonHocId)

        return (rawData.quyDoiDiem || []).find((row) => {
            return khopQuyDoiDiemTuCotDiemMau(row, payload, syllabusMonHocId)
        }) || null
    }

    async function toggleCopyCotDiemMauVaoQuyDoiDiem(item, parentValues = {}) {
        const syllabusMonHocId = laySyllabusMonHocIdDangChon(parentValues)

        if (!syllabusMonHocId) {
            baoTinBang('cauHinhDanhGiaMau', 'Cần chọn Syllabus môn học áp dụng trước khi gán Cột điểm mẫu.', 'error')
            return
        }

        const serviceQuyDoiDiem = daoTaoXemChuongTrinhService.quyDoiDiem

        if (!serviceQuyDoiDiem?.create) {
            baoTinBang('cauHinhDanhGiaMau', 'Chưa khai báo API tạo Quy đổi điểm.', 'error')
            return
        }

        const existing = timQuyDoiDiemDaCoTuCotDiemMau(item, syllabusMonHocId)

        try {
            if (existing?.id) {
                if (!serviceQuyDoiDiem.delete) {
                    baoTinBang('cauHinhDanhGiaMau', 'Chưa khai báo API xóa Quy đổi điểm.', 'error')
                    return
                }

                await serviceQuyDoiDiem.delete(existing.id)
                xoaDongTrongRawData('quyDoiDiem', existing.id)

                if (selected.quyDoiDiem?.id === existing.id) {
                    selectEntity('quyDoiDiem', null)
                }

                baoTinBang('cauHinhDanhGiaMau', 'Đã bỏ gán Cột điểm mẫu khỏi Quy đổi điểm của syllabus môn học đang chọn.')
                return
            }

            const payload = taoPayloadQuyDoiDiemTuCotDiemMau(item, syllabusMonHocId)
            const saved = await serviceQuyDoiDiem.create(payload)
            const row = layDuLieuLuu(saved) || payload
            const tenCotDiemMau = item.tenCotDiem || row.tenCotDiemMau || row.ghiChu || ''

            const rowDaLuu = capNhatDongTrongRawData('quyDoiDiem', {
                ...row,
                syllabusMonHocId,
                tenCotDiemMau,
                ghiChu: row.ghiChu || tenCotDiemMau
            })

            selectEntity('cauHinhDanhGiaMau', item)
            selectEntity('quyDoiDiem', rowDaLuu)

            baoTinBang('cauHinhDanhGiaMau', 'Đã copy Cột điểm mẫu xuống Quy đổi điểm đã lưu theo syllabus môn học.')
        } catch (error) {
            const message = layThongBaoLoi(error, 'Không gán được Cột điểm mẫu vào Quy đổi điểm.')
            baoTinBang('cauHinhDanhGiaMau', message, 'error')
        }
    }
    const bangmauGanSyllabus = {
        dieuKienMonHocmau: {
            joinKey: 'syllabusMonHocDieuKien',
            mauIdKey: 'dieuKienmauId',
            syllabusIdKey: 'syllabusMonId',
            serviceKey: 'syllabusMonHocDieuKien',
            tenBang: 'Điều kiện môn học gốc'
        },
        taiLieumau: {
            joinKey: 'syllabusMonHocTaiLieu',
            mauIdKey: 'taiLieumauId',
            syllabusIdKey: 'syllabusMonId',
            serviceKey: 'syllabusMonHocTaiLieu',
            tenBang: 'Tài liệu gốc'
        }
    }

    // function timDongNoiMon(config, item, chuongTrinhMonId) {
    //     const list = rawData[config.joinKey] || []
    //     return list.find((row) => {
    //         return String(row[config.monIdKey] || '') === String(chuongTrinhMonId || '')
    //             && String(row[config.mauIdKey] || '') === String(item?.id || '')
    //     })
    // }


    function timDongNoiSyllabus(config, item, syllabusMonId) {
        const list = rawData[config.joinKey] || []
        return list.find((row) => {
            return String(row[config.syllabusIdKey] || '') === String(syllabusMonId || '')
                && String(row[config.mauIdKey] || '') === String(item?.id || '')
        })
    }

    function layThuTuGanSyllabusTiepTheo(joinKey, syllabusMonId) {
        const list = rawData[joinKey] || []
        const thuTuLonNhat = list
            .filter((row) => String(row.syllabusMonId || '') === String(syllabusMonId || ''))
            .map((row) => Number(row.thuTu || 0))
            .filter((value) => !Number.isNaN(value))
            .reduce((max, value) => Math.max(max, value), 0)

        return thuTuLonNhat + 1
    }

    function layThuTuGanTiepTheo(joinKey, chuongTrinhVersionId) {
        const list = rawData[joinKey] || []
        const thuTuLonNhat = list
            .filter((row) => String(row.chuongTrinhVersionId || '') === String(chuongTrinhVersionId || ''))
            .map((row) => Number(row.thuTu || 0))
            .filter((value) => !Number.isNaN(value))
            .reduce((max, value) => Math.max(max, value), 0)

        return thuTuLonNhat + 1
    }

    function layThuTuGanTheoChaTiepTheo(joinKey, parentKey, parentId) {
        const list = rawData[joinKey] || []
        const thuTuLonNhat = list
            .filter((row) => String(row[parentKey] || '') === String(parentId || ''))
            .map((row) => Number(row.thuTu || 0))
            .filter((value) => !Number.isNaN(value))
            .reduce((max, value) => Math.max(max, value), 0)

        return thuTuLonNhat + 1
    }


    function laySyllabusChuongTrinhIdTheoVersion(chuongTrinhVersionId) {
        return selected.syllabusChuongTrinh?.id
            || (rawData.syllabusChuongTrinh || []).find((row) => String(row.chuongTrinhVersionId || '') === String(chuongTrinhVersionId || ''))?.id
            || null
    }

    function layLoaiChuongTrinhDangChon() {
        return selected.loaiChuongTrinh?.id || selected.chuongTrinh?.loaiChuongTrinhId || null
    }

    function timDongNoiVersion(config, item, chuongTrinhVersionId) {
        const list = rawData[config.joinKey] || []
        return list.find((row) => {
            return String(row.chuongTrinhVersionId || '') === String(chuongTrinhVersionId || '')
                && String(row[config.mauIdKey] || '') === String(item?.id || '')
        })
    }

    async function ganBangmauVaoVersion(key, item) {
        const config = bangmauGanVersion[key]
        if (!config) return false

        if (!item) {
            selectEntity(key, null)
            return true
        }

        const chuongTrinhVersionId = selected.chuongTrinhVersion?.id
        if (!chuongTrinhVersionId) {
            baoTinBang(key, `Cần chọn Version trước khi gán ${config.tenBang}.`, 'error')
            return true
        }

        const existing = timDongNoiVersion(config, item, chuongTrinhVersionId)
        if (existing) {
            selectEntity(key, item)
            baoTinBang(key, `Đã chọn ${config.tenBang} đã được gán trong Version hiện tại.`)
            return true
        }

        const service = daoTaoXemChuongTrinhService[config.serviceKey]
        if (!service?.create) {
            baoTinBang(key, `Chưa khai báo service bảng nối cho ${config.tenBang}.`, 'error')
            return true
        }

        try {
            const payload = {
                chuongTrinhVersionId,
                [config.mauIdKey]: item.id,
                thuTu: layThuTuGanTiepTheo(config.joinKey, chuongTrinhVersionId),
                ghiChu: item.ghiChu || ''
            }

            const saved = await service.create(payload)
            const row = layDuLieuLuu(saved) || payload
            capNhatDongTrongRawData(config.joinKey, row)
            selectEntity(key, item)
            baoTinBang(key, `Đã gán ${config.tenBang} vào Version qua bảng nối.`)
        } catch (error) {
            const message = layThongBaoLoi(error, `Không gán được ${config.tenBang} vào Version.`)
            baoTinBang(key, message, 'error')
        }

        return true
    }

    const bangCoNutLuuLienKet = new Set([
        'chuongTrinhMon',
        'mucTieuChuongTrinhmau',
        'mucTieuChuongTrinh',
        'nangLucDauRamau',
        'nangLucDauRa',
        'viTriViecLammau',
        'viTriViecLam',
        'dieuKienTotNghiepmau',
        'dieuKienTotNghiep',
        'monTienQuyet',
        'quyDoiDiemMau',
        'dieuKienMonHocmau',
        'taiLieumau',
        'dieuKienMonHoc',
        'syllabusChuongBai',
        'syllabusTaiLieu'
    ])

    function timDongNoiBangmau(key, item, chuongTrinhVersionId) {
        const config = bangmauGanVersion[key]
        if (!config) return null
        return timDongNoiVersion(config, item, chuongTrinhVersionId)
    }

    function timDongApDungVersion(key, item, chuongTrinhVersionId) {
        const config = bangmauTaoApDungVersion[key]
        if (!config) return null

        const list = rawData[config.joinKey] || []
        const rowKey = config.matchBy?.rowKey || config.mauIdKey
        const itemKey = config.matchBy?.itemKey || 'id'

        return list.find((row) => {
            return String(row.chuongTrinhVersionId || '') === String(chuongTrinhVersionId || '')
                && String(row[rowKey] || '') === String(item?.[itemKey] || '')
        })
    }

    function layConfigGanSyllabusTheoNguCanh(key, parentValues = {}) {
        if (key !== 'dieuKienMonHocmau' && key !== 'taiLieumau' && key !== 'syllabusMonHocmauTaiLieu') return null

        const mauIdKey = key === 'dieuKienMonHocmau' ? 'dieuKienmauId' : (key === 'syllabusMonHocmauTaiLieu' ? 'id' : 'taiLieumauId')
        const tenBang = key === 'dieuKienMonHocmau' ? 'Điều kiện môn học gốc' : 'Tài liệu gốc của syllabus'

        const syllabusMonHocMauId = parentValues.syllabusMonHocMauId || selected.syllabusMonHocmau?.id
        if (syllabusMonHocMauId) {
            return {
                joinKey: key === 'dieuKienMonHocmau' ? 'syllabusMonHocmauDieuKien' : 'syllabusMonHocmauTaiLieu',
                mauIdKey,
                syllabusIdKey: 'syllabusMonHocMauId',
                syllabusId: syllabusMonHocMauId,
                serviceKey: key === 'dieuKienMonHocmau' ? 'syllabusMonHocmauDieuKien' : 'syllabusMonHocmauTaiLieu',
                tenBang
            }
        }

        const syllabusMonId = parentValues.syllabusMonId || selected.syllabusMonHoc?.id
        if (syllabusMonId) {
            return {
                joinKey: key === 'dieuKienMonHocmau' ? 'syllabusMonHocDieuKien' : 'syllabusMonHocTaiLieu',
                mauIdKey,
                syllabusIdKey: 'syllabusMonId',
                syllabusId: syllabusMonId,
                serviceKey: key === 'dieuKienMonHocmau' ? 'syllabusMonHocDieuKien' : 'syllabusMonHocTaiLieu',
                tenBang
            }
        }

        return null
    }

    function timSyllabusChuongBaiDaCopyTumau(item, syllabusMonId) {
        return (rawData.syllabusChuongBai || []).find((row) => {
            if (String(row.syllabusMonId || '') !== String(syllabusMonId || '')) return false

            const cungMaChuong = row.maChuong && item?.maChuong && String(row.maChuong || '') === String(item.maChuong || '')
            const cungTen = row.ten && item?.ten && String(row.ten || '') === String(item.ten || '')
            const cungThuTu = item?.thuTu !== null && item?.thuTu !== undefined && row.thuTu !== null && row.thuTu !== undefined
                && String(row.thuTu || '') === String(item.thuTu || '')

            return cungMaChuong
                || cungThuTu
                || (cungTen && !row.maChuong && !item?.maChuong && row.thuTu == null && item?.thuTu == null)
        }) || null
    }

    function layMaChuongGanTiepTheo(item, syllabusMonId) {
        const mamau = String(item?.maChuong || '').trim()
        if (!mamau) return null

        const maDaCo = new Set(
            (rawData.syllabusChuongBai || [])
                .filter((row) => String(row.syllabusMonId || '') === String(syllabusMonId || ''))
                .map((row) => String(row.maChuong || '').trim())
                .filter(Boolean)
        )

        if (!maDaCo.has(mamau)) return mamau

        let index = 2
        while (maDaCo.has(`${mamau}-${index}`)) index += 1
        return `${mamau}-${index}`
    }

    function taoPayloadSyllabusChuongBaiTumau(item, syllabusMonId) {
        const thuTumau = item.thuTu === null || item.thuTu === undefined || item.thuTu === '' ? null : Number(item.thuTu)
        const thuTuDaCo = (rawData.syllabusChuongBai || []).some((row) => {
            return String(row.syllabusMonId || '') === String(syllabusMonId || '')
                && thuTumau !== null
                && String(row.thuTu || '') === String(thuTumau || '')
        })

        return {
            syllabusMonId,
            maChuong: layMaChuongGanTiepTheo(item, syllabusMonId),
            ten: item.ten || 'Chương/bài',
            mucTieu: item.mucTieu || null,
            noiDung: item.noiDung || null,
            tongGio: item.tongGio ?? null,
            gioLyThuyet: item.gioLyThuyet ?? null,
            gioThucHanh: item.gioThucHanh ?? null,
            gioKiemTra: item.gioKiemTra ?? null,
            thuTu: thuTuDaCo || thuTumau === null ? layThuTuGanTheoChaTiepTheo('syllabusChuongBai', 'syllabusMonId', syllabusMonId) : thuTumau,
            ghiChu: item.ghiChu || null
        }
    }

    async function toggleCopyChuongBaimauVaoSyllabus(item, parentValues = {}) {
        const syllabusMonId = parentValues.syllabusMonId || selected.syllabusMonHoc?.id

        if (!syllabusMonId) {
            baoTinBang('syllabusMonHocmauChuongBai', 'Cần chọn Syllabus môn học áp dụng trước khi gán chương/bài gốc.', 'error')
            return
        }

        const serviceChuongBai = daoTaoXemChuongTrinhService.syllabusChuongBai

        if (!serviceChuongBai?.create) {
            baoTinBang('syllabusMonHocmauChuongBai', 'Chưa khai báo API tạo Chương/bài đã lưu trong syllabus áp dụng.', 'error')
            return
        }

        const existing = timSyllabusChuongBaiDaCopyTumau(item, syllabusMonId)

        try {
            if (existing?.id) {
                selectEntity('syllabusMonHocmauChuongBai', null)
                selectEntity('syllabusChuongBai', existing)
                baoTinBang('syllabusMonHocmauChuongBai', 'Đã bỏ chọn dòng mẫu. Chương/bài đã copy vào syllabus vẫn được giữ nguyên.')
                return
            }

            const payload = taoPayloadSyllabusChuongBaiTumau(item, syllabusMonId)
            const saved = await serviceChuongBai.create(payload)
            const row = layDuLieuLuu(saved) || payload

            capNhatDongTrongRawData('syllabusChuongBai', row)
            await taiDuLieuTheoSyllabusMonHocApDung(selected.syllabusMonHoc)
            selectEntity('syllabusMonHocmauChuongBai', item)

            baoTinBang('syllabusMonHocmauChuongBai', 'Đã copy chương/bài gốc vào Chương/bài đã lưu trong syllabus áp dụng.')
        } catch (error) {
            const message = layThongBaoLoi(error, 'Không gán được chương/bài gốc vào syllabus áp dụng.')
            baoTinBang('syllabusMonHocmauChuongBai', message, 'error')
        }
    }

    function timDieuKienMonHocDaCopyTumau(item, syllabusMonId) {
        return (rawData.dieuKienMonHoc || []).find((row) => {
            return String(row.syllabusMonId || '') === String(syllabusMonId || '')
                && String(row.ma || '') === String(item?.ma || '')
        }) || null
    }

    function taoPayloadDieuKienMonHocTumau(item, syllabusMonId) {
        return {
            syllabusMonId,
            ma: item.ma || '',
            loai: item.loai || 'phong_hoc',
            ten: item.ten || item.noiDung || item.ma || 'Điều kiện môn học',
            noiDung: item.noiDung || item.ten || item.ma || '',
            soLuong: item.soLuong ?? null,
            yeuCau: item.yeuCau || item.noiDung || item.ten || item.ma || '',
            thuTu: layThuTuGanTheoChaTiepTheo('dieuKienMonHoc', 'syllabusMonId', syllabusMonId),
            ghiChu: item.ghiChu || ''
        }
    }

    function timSyllabusTaiLieuDaCopyTumau(item, syllabusMonId) {
        return (rawData.syllabusTaiLieu || []).find((row) => {
            if (String(row.syllabusMonId || '') !== String(syllabusMonId || '')) return false
            const cungMa = row.ma && item?.ma && String(row.ma || '') === String(item.ma || '')
            const cungTen = row.ten && item?.ten && String(row.ten || '') === String(item.ten || '')
            const cungTaiLieumau = row.taiLieumauId && item?.taiLieumauId && String(row.taiLieumauId || '') === String(item.taiLieumauId || '')
            return cungMa || cungTen || cungTaiLieumau
        }) || null
    }

    function taoPayloadSyllabusTaiLieuTumau(item, syllabusMonId) {
        return {
            syllabusMonId,
            ma: item.ma || item.maTaiLieu || '',
            ten: item.ten || item.tenTaiLieu || item.tenTaiLieumau || '',
            loai: item.loai || item.loaiTaiLieu || 'GIAO_TRINH',
            tacGia: item.tacGia || item.tacGiaTaiLieu || '',
            nhaXuatBan: item.nhaXuatBan || item.nhaXuatBanTaiLieu || '',
            namXuatBan: item.namXuatBan ?? item.namXuatBanTaiLieu ?? null,
            duongDan: item.duongDan || item.duongDanTaiLieu || '',
            batBuoc: Boolean(item.batBuoc),
            thuTu: item.thuTu ?? layThuTuGanTheoChaTiepTheo('syllabusTaiLieu', 'syllabusMonId', syllabusMonId),
            ghiChu: item.ghiChu || ''
        }
    }

    async function toggleCopyDieuKienMonHocmauVaoSyllabus(item, parentValues = {}) {
        const syllabusMonId = parentValues.syllabusMonId || selected.syllabusMonHoc?.id

        if (!syllabusMonId) {
            baoTinBang('dieuKienMonHocmau', 'Cần chọn Syllabus môn học áp dụng trước khi gán điều kiện môn học gốc.', 'error')
            return
        }

        const serviceDieuKien = daoTaoXemChuongTrinhService.dieuKienMonHoc

        if (!serviceDieuKien?.create) {
            baoTinBang('dieuKienMonHocmau', 'Chưa khai báo API tạo Điều kiện môn học đã lưu vào syllabus.', 'error')
            return
        }

        const existing = timDieuKienMonHocDaCopyTumau(item, syllabusMonId)

        try {
            if (existing?.id) {
                selectEntity('dieuKienMonHocmau', null)
                selectEntity('dieuKienMonHoc', existing)
                baoTinBang('dieuKienMonHocmau', 'Đã bỏ chọn dòng mẫu. Điều kiện môn học đã copy vào syllabus vẫn được giữ nguyên.')
                return
            }

            const payload = taoPayloadDieuKienMonHocTumau(item, syllabusMonId)
            const saved = await serviceDieuKien.create(payload)
            const row = layDuLieuLuu(saved) || payload

            capNhatDongTrongRawData('dieuKienMonHoc', row)
            selectEntity('dieuKienMonHocmau', item)

            baoTinBang('dieuKienMonHocmau', 'Đã copy điều kiện môn học gốc vào Điều kiện môn học đã lưu vào syllabus.')
        } catch (error) {
            const message = layThongBaoLoi(error, 'Không gán được điều kiện môn học gốc vào syllabus.')
            baoTinBang('dieuKienMonHocmau', message, 'error')
        }
    }

    async function toggleCopyTaiLieumauVaoSyllabus(item, parentValues = {}) {
        const syllabusMonId = parentValues.syllabusMonId || selected.syllabusMonHoc?.id

        if (!syllabusMonId) {
            baoTinBang('syllabusMonHocmauTaiLieu', 'Cần chọn Syllabus môn học áp dụng trước khi gán tài liệu gốc.', 'error')
            return
        }

        const serviceTaiLieu = daoTaoXemChuongTrinhService.syllabusTaiLieu

        if (!serviceTaiLieu?.create) {
            baoTinBang('syllabusMonHocmauTaiLieu', 'Chưa khai báo API tạo Tài liệu đã lưu vào syllabus.', 'error')
            return
        }

        const existing = timSyllabusTaiLieuDaCopyTumau(item, syllabusMonId)

        try {
            if (existing?.id) {
                selectEntity('syllabusMonHocmauTaiLieu', null)
                selectEntity('syllabusTaiLieu', existing)
                baoTinBang('syllabusMonHocmauTaiLieu', 'Đã bỏ chọn dòng mẫu. Tài liệu đã copy vào syllabus vẫn được giữ nguyên.')
                return
            }

            const payload = taoPayloadSyllabusTaiLieuTumau(item, syllabusMonId)
            const saved = await serviceTaiLieu.create(payload)
            const row = layDuLieuLuu(saved) || payload

            capNhatDongTrongRawData('syllabusTaiLieu', row)
            await taiDuLieuTheoSyllabusMonHocApDung(selected.syllabusMonHoc)
            selectEntity('syllabusMonHocmauTaiLieu', item)

            baoTinBang('syllabusMonHocmauTaiLieu', 'Đã copy tài liệu gốc vào Tài liệu đã lưu vào syllabus.')
        } catch (error) {
            const message = layThongBaoLoi(error, 'Không gán được tài liệu gốc vào syllabus.')
            baoTinBang('syllabusMonHocmauTaiLieu', message, 'error')
        }
    }

    function timDongNoiSyllabusTheoConfig(config, item) {
        if (!config?.syllabusId) return null
        const list = rawData[config.joinKey] || []
        return list.find((row) => {
            return String(row[config.syllabusIdKey] || '') === String(config.syllabusId || '')
                && String(row[config.mauIdKey] || '') === String(item?.id || '')
        })
    }

    function daLuuTrucTiep(item, parentValues = {}) {
        if (!item) return false
        const parentHopLe = Object.entries(parentValues || {}).filter(([, value]) => coGiaTri(value))
        if (!parentHopLe.length) return false
        return parentHopLe.every(([parentKey, value]) => String(item?.[parentKey] ?? '') === String(value))
    }

    const bangXoaDongKhiBoLuu = new Set([
        'monTienQuyet',
        'quyDoiDiemMau',
        'dieuKienMonHocmau',
        'taiLieumau',
        'dieuKienMonHoc',
        'syllabusChuongBai',
        'syllabusTaiLieu'
    ])

    async function toggleLuuBangPhu(key, item, parentValues = {}, service = null) {
        if (!item?.id) {
            baoTinBang(key, 'Không tìm thấy dòng dữ liệu để lưu hoặc bỏ lưu.', 'error')
            return
        }

        if (key === 'syllabusChuongTrinhmau') {
            const chuongTrinhVersionId = selected.chuongTrinhVersion?.id || parentValues.chuongTrinhVersionId
            if (!chuongTrinhVersionId) {
                baoTinBang(key, 'Cần chọn Version trước khi gán syllabus chương trình mẫu.', 'error')
                return
            }

            const serviceSyllabus = daoTaoXemChuongTrinhService.syllabusChuongTrinh
            if (!serviceSyllabus?.dongBoTuMau) {
                baoTinBang(key, 'Chưa khai báo API đồng bộ syllabus chương trình từ mẫu.', 'error')
                return
            }

            try {
                const rowDangGan = (rawData.syllabusChuongTrinh || []).find((dong) => {
                    const syllabusMauId = dong.syllabusChuongTrinhmauId || dong.syllabusChuongTrinhMauId
                    return String(dong.chuongTrinhVersionId || '') === String(chuongTrinhVersionId || '')
                        && String(syllabusMauId || '') === String(item.id || '')
                })

                if (rowDangGan?.id) {
                    const dongY = confirm('Syllabus chương trình mẫu này đã được gán vào Version hiện tại. Bấm OK để hủy gán và xóa các dữ liệu đã copy theo syllabus này khỏi Version.')
                    if (!dongY) return

                    await Promise.all([
                        ...(rawData.mucTieuChuongTrinh || []).filter((row) => String(row.syllabusChuongTrinhId || '') === String(rowDangGan.id || '')).map((row) => daoTaoXemChuongTrinhService.mucTieuChuongTrinh.delete(row.id)),
                        ...(rawData.nangLucDauRa || []).filter((row) => String(row.syllabusChuongTrinhId || '') === String(rowDangGan.id || '')).map((row) => daoTaoXemChuongTrinhService.nangLucDauRa.delete(row.id)),
                        ...(rawData.viTriViecLam || []).filter((row) => String(row.syllabusChuongTrinhId || '') === String(rowDangGan.id || '')).map((row) => daoTaoXemChuongTrinhService.viTriViecLam.delete(row.id)),
                        ...(rawData.dieuKienTotNghiep || []).filter((row) => String(row.syllabusChuongTrinhId || '') === String(rowDangGan.id || '')).map((row) => daoTaoXemChuongTrinhService.dieuKienTotNghiep.delete(row.id))
                    ])

                    await serviceSyllabus.delete(rowDangGan.id)
                    await taiDuLieuTongQuanVersion(chuongTrinhVersionId)
                    selectEntity('syllabusChuongTrinhmau', null)
                    selectEntity('syllabusChuongTrinh', null)
                    baoTinBang(key, 'Đã hủy gán syllabus chương trình mẫu khỏi Version. Dữ liệu mẫu/gốc vẫn được giữ ở trang CRUD gốc.')
                    return
                }

                const saved = await serviceSyllabus.dongBoTuMau({
                    chuongTrinhVersionId,
                    syllabusChuongTrinhMauId: item.id
                })
                const row = layDuLieuLuu(saved) || null

                await taiDuLieuTongQuanVersion(chuongTrinhVersionId)

                selectEntity('syllabusChuongTrinhmau', item)
                if (row?.id) {
                    selectEntity('syllabusChuongTrinh', row)
                } else {
                    const rowDaGan = (rawData.syllabusChuongTrinh || []).find((dong) => {
                        const syllabusMauId = dong.syllabusChuongTrinhmauId || dong.syllabusChuongTrinhMauId
                        return String(dong.chuongTrinhVersionId || '') === String(chuongTrinhVersionId || '')
                            && String(syllabusMauId || '') === String(item.id || '')
                    })
                    if (rowDaGan) selectEntity('syllabusChuongTrinh', rowDaGan)
                }

                baoTinBang(key, 'Đã đồng bộ syllabus chương trình mẫu vào Version. Các bảng con tầng 5 đã được tải lại theo đúng syllabus vừa gán.')
            } catch (error) {
                const message = layThongBaoLoi(error, 'Không đồng bộ được syllabus chương trình mẫu vào Version.')
                baoTinBang(key, message, 'error')
            }
            return
        }

        if (key === 'monHoc') {
            await toggleGanMonHocmauVaoChuongTrinhMon(item, parentValues)
            return
        }
        if (key === 'syllabusMonHocmauChuongBai') {
            await toggleCopyChuongBaimauVaoSyllabus(item, parentValues)
            return
        }

        if (key === 'dieuKienMonHocmau') {
            await toggleCopyDieuKienMonHocmauVaoSyllabus(item, parentValues)
            return
        }

        if (key === 'taiLieumau' || key === 'syllabusMonHocmauTaiLieu') {
            await toggleCopyTaiLieumauVaoSyllabus(item, parentValues)
            return
        }
        if (key === 'cauHinhDanhGiaMau') {
            baoTinBang(key, 'Cột điểm của syllabus phải lưu trực tiếp ở bảng cau_hinh_danh_gia_mau. Không copy qua Quy đổi điểm.', 'error')
            return
        }
        const configApDungVersion = bangmauTaoApDungVersion[key]

        if (configApDungVersion) {
            const chuongTrinhVersionId = selected.chuongTrinhVersion?.id || parentValues.chuongTrinhVersionId
            if (!chuongTrinhVersionId) {
                baoTinBang(key, `Cần chọn Version trước khi gán ${configApDungVersion.tenBang}.`, 'error')
                return
            }

            const existing = timDongApDungVersion(key, item, chuongTrinhVersionId)
            const serviceApDung = daoTaoXemChuongTrinhService[configApDungVersion.serviceKey]

            if (!serviceApDung) {
                baoTinBang(key, `Chưa khai báo API bảng áp dụng cho ${configApDungVersion.tenBang}.`, 'error')
                return
            }

            try {
                if (existing?.id) {
                    if (!serviceApDung.delete) {
                        baoTinBang(key, `Chưa khai báo API xóa bản đã lưu để bỏ gán ${configApDungVersion.tenBang} khỏi Version.`, 'error')
                        return
                    }

                    await serviceApDung.delete(existing.id)
                    xoaDongTrongRawData(configApDungVersion.joinKey, existing.id)
                    if (selected[key]?.id === item.id) selectEntity(key, null)
                    baoTinBang(key, `Đã bỏ gán ${configApDungVersion.tenBang} khỏi bảng đã lưu vào Version. Dữ liệu gốc/mẫu vẫn được giữ nguyên.`)
                    return
                }

                if (!serviceApDung.create) {
                    baoTinBang(key, `Chưa khai báo API tạo bảng áp dụng cho ${configApDungVersion.tenBang}.`, 'error')
                    return
                }

                const payload = configApDungVersion.buildPayload(item, chuongTrinhVersionId)
                const saved = await serviceApDung.create(payload)
                const row = layDuLieuLuu(saved) || payload
                capNhatDongTrongRawData(configApDungVersion.joinKey, row)
                selectEntity(key, item)
                baoTinBang(key, `Đã gán/copy ${configApDungVersion.tenBang} vào bảng đã lưu của Version hiện tại.`)
            } catch (error) {
                const message = layThongBaoLoi(error, `Không gán/bỏ gán được ${configApDungVersion.tenBang} vào Version.`)
                baoTinBang(key, message, 'error')
            }

            return
        }

        const config = bangmauGanVersion[key]

        if (config) {
            const chuongTrinhVersionId = selected.chuongTrinhVersion?.id || parentValues.chuongTrinhVersionId
            if (!chuongTrinhVersionId) {
                baoTinBang(key, `Cần chọn Version trước khi gán ${config.tenBang}.`, 'error')
                return
            }

            const existing = timDongNoiBangmau(key, item, chuongTrinhVersionId)
            const serviceNoi = daoTaoXemChuongTrinhService[config.serviceKey]

            if (!serviceNoi) {
                baoTinBang(key, `Chưa khai báo API bảng nối cho ${config.tenBang}.`, 'error')
                return
            }

            try {
                if (existing?.id) {
                    await serviceNoi.delete(existing.id)
                    xoaDongTrongRawData(config.joinKey, existing.id)
                    if (selected[key]?.id === item.id) selectEntity(key, null)
                    baoTinBang(key, `Đã bỏ gán ${config.tenBang} khỏi bảng đã lưu vào Version. Dữ liệu gốc/mẫu vẫn được giữ nguyên.`)
                    return
                }

                const payload = {
                    chuongTrinhVersionId,
                    [config.mauIdKey]: item.id,
                    thuTu: layThuTuGanTiepTheo(config.joinKey, chuongTrinhVersionId),
                    ghiChu: item.ghiChu || ''
                }

                const saved = await serviceNoi.create(payload)
                const row = layDuLieuLuu(saved) || payload
                capNhatDongTrongRawData(config.joinKey, row)
                selectEntity(key, item)
                baoTinBang(key, `Đã gán ${config.tenBang} vào bảng đã lưu của Version hiện tại.`)
            } catch (error) {
                const message = layThongBaoLoi(error, `Không gán/bỏ gán được ${config.tenBang} vào Version.`)
                baoTinBang(key, message, 'error')
            }

            return
        }

        const configMau = bangMauGanMon[key]
        if (configMau) {
            const syllabusMonHocId = laySyllabusMonHocIdDangChon(parentValues)

            if (!syllabusMonHocId) {
                baoTinBang(key, `Cần chọn Syllabus môn học áp dụng trước khi lưu ${configMau.tenBang}.`, 'error')
                return
            }

            if (key === 'quyDoiDiemMau' && !laMauQuyDoiKetQua(item)) {
                baoTinBang(key, 'Chỉ được gán mẫu loại QUY_DOI_KET_QUA vào syllabus. Cột điểm không gán qua bảng mẫu quy đổi.', 'error')
                return
            }

            const existing = timDongNoiSyllabus(configMau, item, syllabusMonHocId)
            const serviceNoi = daoTaoXemChuongTrinhService[configMau.serviceKey]

            if (!serviceNoi) {
                baoTinBang(key, `Chưa khai báo API bảng nối cho ${configMau.tenBang}.`, 'error')
                return
            }

            try {
                if (existing?.id) {
                    selectEntity(key, null)
                    baoTinBang(key, `Đã bỏ chọn dòng mẫu. ${configMau.tenBang} đã gán vào syllabus vẫn được giữ nguyên.`)
                    return
                }

                const payload = {
                    [configMau.syllabusIdKey]: syllabusMonHocId,
                    [configMau.mauIdKey]: item.id,
                    ghiChu: item.ghiChu || ''
                }

                const saved = await serviceNoi.create(payload)
                const row = layDuLieuLuu(saved) || payload
                capNhatDongTrongRawData(configMau.joinKey, row)

                selectEntity(key, item)
                baoTinBang(key, `Đã gán ${configMau.tenBang} vào syllabus môn học.`)
            } catch (error) {
                const message = layThongBaoLoi(error, `Không lưu/bỏ lưu được ${configMau.tenBang}.`)
                baoTinBang(key, message, 'error')
            }

            return
        }


        if (key === 'syllabusMonHocmau') {
            const chuongTrinhMonId = selected.chuongTrinhMon?.id || parentValues.chuongTrinhMonId
            if (!chuongTrinhMonId) {
                baoTinBang(key, 'Cần chọn Môn trong chương trình trước khi gắn Syllabus môn học gốc.', 'error')
                return
            }

            const serviceApDung = daoTaoXemChuongTrinhService.syllabusMonHoc
            if (!serviceApDung) {
                baoTinBang(key, 'Chưa khai báo API syllabus_mon_hoc.', 'error')
                return
            }

            const existing = (rawData.syllabusMonHoc || []).find((row) => {
                return String(row.chuongTrinhMonId || '') === String(chuongTrinhMonId || '')
                    && String(row.syllabusMonHocMauId || '') === String(item.id || '')
            })

            try {
                if (existing?.id) {
                    if (!serviceApDung.delete) {
                        baoTinBang(key, 'Chưa khai báo API xóa để bỏ gắn Syllabus môn học gốc.', 'error')
                        return
                    }

                    await serviceApDung.delete(existing.id)
                    xoaDongTrongRawData('syllabusMonHoc', existing.id)
                    if (selected.syllabusMonHoc?.id === existing.id) selectEntity('syllabusMonHoc', null)
                    if (selected[key]?.id === item.id) selectEntity(key, null)
                    baoTinBang(key, 'Đã bỏ gắn Syllabus môn học gốc khỏi Môn trong chương trình hiện tại.')
                    return
                }

                if (!serviceApDung.create) {
                    baoTinBang(key, 'Chưa khai báo API tạo syllabus_mon_hoc.', 'error')
                    return
                }

                const payload = {
                    chuongTrinhMonId,
                    syllabusMonHocMauId: item.id,

                    monHocId: item.monHocId || selected.monHoc?.id || null,
                    ma: item.ma || '',
                    ten: item.ten || '',

                    viTri: item.viTri || '',
                    tinhChat: item.tinhChat || '',
                    mucTieu: item.mucTieu || '',
                    phuongPhapDanhGia: item.phuongPhapDanhGia || '',
                    dieuKienHoanThanh: item.dieuKienHoanThanh || '',
                    huongDan: item.huongDan || '',
                    diemDatToiThieu: item.diemDatToiThieu ?? null,
                    donViDiem: item.donViDiem || 'thang_10',
                    tyLeChuyenCanToiThieu: item.tyLeChuyenCanToiThieu ?? null,
                    batBuocDuThi: item.batBuocDuThi ?? false,
                    congThucQuyDoi: item.congThucQuyDoi || '',
                    ghiChu: item.ghiChu || ''
                }

                const saved = await serviceApDung.create(payload)
                const row = layDuLieuLuu(saved) || payload
                const rowDaLuu = capNhatDongTrongRawData('syllabusMonHoc', row)
                selectEntity(key, item)
                selectEntity('syllabusMonHoc', rowDaLuu)
                baoTinBang(key, 'Đã gắn Syllabus môn học gốc vào Môn trong chương trình hiện tại qua syllabus_mon_hoc.')
            } catch (error) {
                const message = layThongBaoLoi(error, 'Không gắn/bỏ gắn được Syllabus môn học gốc.')
                baoTinBang(key, message, 'error')
            }

            return
        }

        const configSyllabus = layConfigGanSyllabusTheoNguCanh(key, parentValues)
        if (configSyllabus) {
            const existing = timDongNoiSyllabusTheoConfig(configSyllabus, item)
            const serviceNoi = daoTaoXemChuongTrinhService[configSyllabus.serviceKey]

            if (!serviceNoi) {
                baoTinBang(key, `Chưa khai báo API bảng nối cho ${configSyllabus.tenBang}.`, 'error')
                return
            }

            try {
                if (existing?.id) {
                    await serviceNoi.delete(existing.id)
                    xoaDongTrongRawData(configSyllabus.joinKey, existing.id)
                    if (selected[key]?.id === item.id) selectEntity(key, null)
                    baoTinBang(key, `Đã bỏ lưu ${configSyllabus.tenBang} khỏi Syllabus hiện tại. Dòng gốc đã chuyển về trạng thái có thể Lưu.`)
                    return
                }

                const payload = {
                    [configSyllabus.syllabusIdKey]: configSyllabus.syllabusId,
                    [configSyllabus.mauIdKey]: item.id,
                    thuTu: layThuTuGanTheoChaTiepTheo(configSyllabus.joinKey, configSyllabus.syllabusIdKey, configSyllabus.syllabusId),
                    ghiChu: item.ghiChu || ''
                }

                const saved = await serviceNoi.create(payload)
                const row = layDuLieuLuu(saved) || payload
                capNhatDongTrongRawData(configSyllabus.joinKey, row)
                selectEntity(key, item)
                baoTinBang(key, `Đã lưu ${configSyllabus.tenBang} vào Syllabus hiện tại.`)
            } catch (error) {
                const message = layThongBaoLoi(error, `Không lưu/bỏ lưu được ${configSyllabus.tenBang}.`)
                baoTinBang(key, message, 'error')
            }

            return
        }

        const parentHopLe = Object.entries(parentValues || {}).filter(([, value]) => coGiaTri(value))
        if (!parentHopLe.length) {
            const dangChon = String(selected[key]?.id || '') === String(item.id || '')
            if (dangChon) {
                selectEntity(key, null)
                baoTinBang(key, 'Đã chuyển dòng này về trạng thái không lưu trong luồng hiện tại.')
            } else {
                selectEntity(key, item)
                baoTinBang(key, 'Đã lưu/chọn dòng này vào luồng hiện tại.')
            }
            return
        }

        if (!service?.update) {
            baoTinBang(key, 'Chưa khai báo API cập nhật cho bảng này.', 'error')
            return
        }

        const dangLuu = daLuuTrucTiep(item, parentValues)

        if (dangLuu && key === 'chuongTrinhMon') {
            baoTinBang(key, 'Chương trình môn là bảng xương sống nên không bỏ lưu bằng nút này. Nếu cần xóa khỏi chương trình, dùng nút Xóa.', 'error')
            return
        }

        if (dangLuu && bangXoaDongKhiBoLuu.has(key)) {
            if (!service?.delete) {
                baoTinBang(key, 'Chưa khai báo API xóa để bỏ lưu dữ liệu này.', 'error')
                return
            }

            try {
                await service.delete(item.id)
                xoaDongTrongRawData(key, item.id)
                if (selected[key]?.id === item.id) selectEntity(key, null)
                baoTinBang(key, 'Đã hủy lưu dữ liệu khỏi luồng hiện tại.')
            } catch (error) {
                const message = layThongBaoLoi(error, 'Không hủy lưu được dữ liệu. Kiểm tra ràng buộc BE.')
                baoTinBang(key, message, 'error')
            }
            return
        }

        const payload = {
            ...item,
            ...Object.fromEntries(parentHopLe.map(([parentKey, value]) => [parentKey, dangLuu ? null : value]))
        }

        try {
            const saved = await service.update(item.id, payload)
            const row = layDuLieuLuu(saved) || payload
            const rowDaCapNhat = capNhatDongTrongRawData(key, row)
            if (dangLuu) {
                if (selected[key]?.id === item.id) selectEntity(key, null)
                baoTinBang(key, 'Đã bỏ lưu dữ liệu khỏi luồng hiện tại.')
            } else {
                selectEntity(key, rowDaCapNhat)
                baoTinBang(key, 'Đã lưu dữ liệu vào luồng hiện tại.')
            }
        } catch (error) {
            const message = layThongBaoLoi(error, 'Không lưu/bỏ lưu được dữ liệu. Kiểm tra ràng buộc BE.')
            baoTinBang(key, message, 'error')
        }
    }

    async function chonBangPhuVaoLuong(key, item, parentValues = {}, service = null) {
        if (bangmauGanVersion[key]) {
            const handled = await ganBangmauVaoVersion(key, item)
            if (handled) return
        }

        if (!item) {
            selectEntity(key, null)
            return
        }

        const configMau = bangMauGanMon[key]
        if (configMau) {
            const syllabusMonHocId = laySyllabusMonHocIdDangChon(parentValues)

            if (!syllabusMonHocId) {
                baoTinBang(key, `Cần chọn Syllabus môn học áp dụng trước khi lưu ${configMau.tenBang}.`, 'error')
                return
            }

            if (key === 'quyDoiDiemMau' && !laMauQuyDoiKetQua(item)) {
                baoTinBang(key, 'Chỉ được gán mẫu loại QUY_DOI_KET_QUA vào syllabus. Cột điểm không gán qua bảng mẫu quy đổi.', 'error')
                return
            }

            const existing = timDongNoiSyllabus(configMau, item, syllabusMonHocId)
            const serviceNoi = daoTaoXemChuongTrinhService[configMau.serviceKey]

            if (!serviceNoi) {
                baoTinBang(key, `Chưa khai báo API bảng nối cho ${configMau.tenBang}.`, 'error')
                return
            }

            try {
                if (existing?.id) {
                    await serviceNoi.delete(existing.id)
                    xoaDongTrongRawData(configMau.joinKey, existing.id)
                    if (selected[key]?.id === item.id) selectEntity(key, null)
                    baoTinBang(key, `Đã bỏ gắn ${configMau.tenBang} khỏi syllabus môn học.`)
                    return
                }

                const payload = {
                    [configMau.syllabusIdKey]: syllabusMonHocId,
                    [configMau.mauIdKey]: item.id,
                    ghiChu: item.ghiChu || ''
                }

                const saved = await serviceNoi.create(payload)
                const row = layDuLieuLuu(saved) || payload
                capNhatDongTrongRawData(configMau.joinKey, row)
                selectEntity(key, item)
                baoTinBang(key, `Đã gắn ${configMau.tenBang} vào syllabus môn học qua bảng nối.`)
            } catch (error) {
                const message = layThongBaoLoi(error, `Không lưu/bỏ lưu được ${configMau.tenBang}.`)
                baoTinBang(key, message, 'error')
            }

            return
        }


        if (key === 'syllabusMonHocmau') {
            const chuongTrinhMonId = selected.chuongTrinhMon?.id || parentValues.chuongTrinhMonId
            if (!chuongTrinhMonId) {
                baoTinBang(key, 'Cần chọn Môn trong chương trình trước khi gắn Syllabus môn học gốc.', 'error')
                return
            }

            const serviceApDung = daoTaoXemChuongTrinhService.syllabusMonHoc
            if (!serviceApDung) {
                baoTinBang(key, 'Chưa khai báo API syllabus_mon_hoc.', 'error')
                return
            }

            const existing = (rawData.syllabusMonHoc || []).find((row) => {
                return String(row.chuongTrinhMonId || '') === String(chuongTrinhMonId || '')
                    && String(row.syllabusMonHocMauId || '') === String(item.id || '')
            })

            try {
                if (existing?.id) {
                    if (!serviceApDung.delete) {
                        baoTinBang(key, 'Chưa khai báo API xóa để bỏ gắn Syllabus môn học gốc.', 'error')
                        return
                    }

                    await serviceApDung.delete(existing.id)
                    xoaDongTrongRawData('syllabusMonHoc', existing.id)
                    if (selected.syllabusMonHoc?.id === existing.id) selectEntity('syllabusMonHoc', null)
                    if (selected[key]?.id === item.id) selectEntity(key, null)
                    baoTinBang(key, 'Đã bỏ gắn Syllabus môn học gốc khỏi Môn trong chương trình hiện tại.')
                    return
                }

                if (!serviceApDung.create) {
                    baoTinBang(key, 'Chưa khai báo API tạo syllabus_mon_hoc.', 'error')
                    return
                }

                const payload = {
                    chuongTrinhMonId,
                    syllabusMonHocMauId: item.id,
                    monHocId: item.monHocId || selected.monHoc?.id || selected.chuongTrinhMon?.monHocId || null,
                    ma: item.ma || '',
                    ten: item.ten || '',
                    viTri: item.viTri || '',
                    tinhChat: item.tinhChat || '',
                    mucTieu: item.mucTieu || '',
                    phuongPhapDanhGia: item.phuongPhapDanhGia || '',
                    dieuKienHoanThanh: item.dieuKienHoanThanh || '',
                    huongDan: item.huongDan || '',
                    diemDatToiThieu: item.diemDatToiThieu ?? null,
                    donViDiem: item.donViDiem || 'thang_10',
                    tyLeChuyenCanToiThieu: item.tyLeChuyenCanToiThieu ?? null,
                    batBuocDuThi: item.batBuocDuThi ?? true,
                    congThucQuyDoi: item.congThucQuyDoi || '',
                    ghiChu: item.ghiChu || ''
                }

                const saved = await serviceApDung.create(payload)
                const row = layDuLieuLuu(saved) || payload
                const rowDaLuu = capNhatDongTrongRawData('syllabusMonHoc', row)
                selectEntity(key, item)
                selectEntity('syllabusMonHoc', rowDaLuu)
                baoTinBang(key, 'Đã gắn Syllabus môn học gốc vào Môn trong chương trình hiện tại qua syllabus_mon_hoc.')
            } catch (error) {
                const message = layThongBaoLoi(error, 'Không gắn/bỏ gắn được Syllabus môn học gốc.')
                baoTinBang(key, message, 'error')
            }

            return
        }

        const configSyllabus = layConfigGanSyllabusTheoNguCanh(key, parentValues)
        if (configSyllabus) {
            await toggleLuuBangPhu(key, item, parentValues, service)
            return
        }

        const parentHopLe = Object.entries(parentValues || {}).filter(([, value]) => coGiaTri(value))
        const canGanCha = parentHopLe.some(([parentKey, value]) => String(item?.[parentKey] ?? '') !== String(value))

        if (!canGanCha || !item.id || !service?.update) {
            selectEntity(key, item)
            return
        }

        try {
            const saved = await service.update(item.id, {
                ...item,
                ...parentValues
            })
            const row = layDuLieuLuu(saved) || {...item, ...parentValues}
            const rowDaCapNhat = capNhatDongTrongRawData(key, row)
            selectEntity(key, rowDaCapNhat)
            baoTin('Đã chọn dữ liệu có sẵn và gắn vào luồng hiện tại.')
        } catch (error) {
            const message = layThongBaoLoi(error, 'Không gắn được dữ liệu có sẵn vào luồng hiện tại.')
            baoTinBang(key, message, 'error')
        }
    }

    function sauKhiLuu(key, saved) {
        const row = layDuLieuLuu(saved)

        if (!row || !row.id) {
            baoTin('Đã gọi API nhưng không nhận được dữ liệu trả về hợp lệ.', 'error')
            return
        }

        const list = rawData[key]

        if (!Array.isArray(list)) {
            baoTin(`Không tìm thấy vùng dữ liệu cho bảng ${key}.`, 'error')
            return
        }

        const oldSelectedId = selected[key]?.id || null
        const index = list.findIndex((item) => item.id === row.id)

        if (index >= 0) {
            list.splice(index, 1, row)
        } else {
            list.push(row)
        }

        // Gán lại mảng để chắc chắn computed rows của bảng cập nhật ngay sau khi POST/PUT thành công.
        rawData[key] = [...list]

        if (key === 'quyDoiDiemMau') {
            const cacheIndex = khoQuyDoiDiemMaumau.value.findIndex((item) => String(item.id || '') === String(row.id || ''))
            if (cacheIndex >= 0) khoQuyDoiDiemMaumau.value.splice(cacheIndex, 1, {...row})
            else khoQuyDoiDiemMaumau.value.push({...row})
        }

        selected[key] = duLieu.value[key]?.find((item) => item.id === row.id) || row

        if (String(oldSelectedId || '') !== String(row.id || '')) {
            xoaLuaChonCon(key)
        }

        baoTin('Đã lưu và thêm vào dữ liệu vừa tạo trong luồng.')
    }

    function sauKhiXoa(key, item) {
        const list = rawData[key]
        const index = list.findIndex((row) => row.id === item.id)
        if (index >= 0) list.splice(index, 1)

        if (selected[key]?.id === item.id) {
            selected[key] = null
        }
    }

    function resetLuonNhapKhongThongBao() {
        Object.assign(selected, taoSelectedRong())
        Object.assign(viewed, taoSelectedRong())
        Object.entries(taoDuLieuRong()).forEach(([key, value]) => {
            rawData[key] = value
        })
    }

    async function lamMoiLuon() {
        const nganhDangNhap = selected.nganh

        if (!nganhDangNhap?.id) {
            resetLuonNhapKhongThongBao()
            baoTin('Đã làm mới luồng nhập.')
            return
        }

        const dongY = confirm(`Làm mới luồng nhập sẽ gọi API xóa ngành vừa tạo: ${nganhDangNhap.tenNganh || nganhDangNhap.maNganh || nganhDangNhap.id}. Tiếp tục?`)
        if (!dongY) return

        try {
            await daoTaoXemChuongTrinhService.nganh.delete(nganhDangNhap.id)
            resetLuonNhapKhongThongBao()
            baoTin('Đã xóa ngành vừa tạo và làm mới luồng nhập.')
        } catch (error) {
            baoTin(layThongBaoLoi(error, 'Không xóa được ngành vừa tạo. Kiểm tra ràng buộc dữ liệu ở BE.'), 'error')
        }
    }

    function luuChuongTrinhTong() {
        if (!selected.nganh || !selected.chuongTrinh || !selected.chuongTrinhVersion) {
            baoTin('Cần lưu tối thiểu Ngành, Chương trình và Version trước khi lưu chương trình.', 'error')
            return
        }

        baoTin('Đã xác nhận luồng xây dựng chương trình đào tạo.')
    }
    onMounted(() => {
        if (autoLoad) {
            taiDuLieuCoSanTatCaBang()
        }
    })
    return {
        thongBao,
        loaiThongBao,
        tableMessages,
        selected,
        viewed,
        duLieu,
        quyDoiDiemMaumauRows,
        lookups,
        services: daoTaoXemChuongTrinhService,
        baoTin,
        selectEntity,
        viewEntity,
        chonBangPhuVaoLuong,
        toggleLuuBangPhu,
        taiDuLieuCoSanTatCaBang,
        taiDuLieuTongQuanVersion,
        taiDuLieuTheoSyllabusMonHocApDung,
        sauKhiLuu,
        sauKhiXoa,
        lamMoiLuon,
        luuChuongTrinhTong,
        taiDuLieuMauCrud,
    }
}