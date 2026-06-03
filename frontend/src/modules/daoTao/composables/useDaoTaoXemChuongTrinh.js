// import {computed, onMounted, reactive, ref, watch} from 'vue'
// import {daoTaoXemChuongTrinhService} from '../services/daoTaoXemChuongTrinhService'
// import {layThongBaoLoi} from '../utils/layThongBaoLoi'
//
// export const luaChonEnum = {
//     loaiMucTieu: [
//         {id: 'chung', ten: 'Chung'},
//         {id: 'kien_thuc', ten: 'Kiến thức'},
//         {id: 'ky_nang', ten: 'Kỹ năng'},
//         {id: 'nang_luc_tu_chu_trach_nhiem', ten: 'Năng lực tự chủ trách nhiệm'}
//     ],
//
//     loaiNangLuc: [
//         {id: 'co_ban', ten: 'Cơ bản'},
//         {id: 'cot_loi', ten: 'Cốt lõi'},
//         {id: 'nang_cao', ten: 'Nâng cao'}
//     ],
//     loaiNhom: [
//         {id: 'chung', ten: 'Chung'},
//         {id: 'co_so', ten: 'Cơ sở'},
//         {id: 'chuyen_mon', ten: 'Chuyên môn'},
//         {id: 'tu_chon', ten: 'Tự chọn'}
//     ],
//     loaiMon: [
//         {id: 'bat_buoc', ten: 'Bắt buộc'},
//         {id: 'tu_chon', ten: 'Tự chọn'}
//     ],
//     loaiHocPhan: [
//         {id: 'mon_hoc', ten: 'Môn học'},
//         {id: 'mo_dun', ten: 'Mô đun'}
//     ],
//     loaiPhamViMon: [
//         {id: 'mon_chuyen_nganh', ten: 'Môn chuyên ngành'},
//         {id: 'mon_chung', ten: 'Môn học chung'}
//     ],
//     loaiDieuKien: [
//         {id: 'tien_quyet', ten: 'Tiên quyết'},
//         {id: 'song_hanh', ten: 'Song hành'},
//         {id: 'hoc_truoc', ten: 'Học trước'}
//     ],
//     loaiDieuKienMonHoc: [
//         {id: 'phong_hoc', ten: 'Phòng học'},
//         {id: 'thiet_bi', ten: 'Thiết bị'},
//         {id: 'hoc_lieu', ten: 'Học liệu'},
//         {id: 'dung_cu', ten: 'Dụng cụ'},
//         {id: 'nguyen_vat_lieu', ten: 'Nguyên vật liệu'},
//         {id: 'khac', ten: 'Khác'}
//     ],
//     ketQua: [
//         {id: 'dat', ten: 'Đạt'},
//         {id: 'khong_dat', ten: 'Không đạt'}
//     ],
//     loaiTaiLieu: [
//         {id: 'GIAO_TRINH', ten: 'Giáo trình'},
//         {id: 'TAI_LIEU_THAM_KHAO', ten: 'Tài liệu tham khảo'},
//         {id: 'BAI_GIANG', ten: 'Bài giảng'},
//         {id: 'KHAC', ten: 'Khác'}
//     ],
//     donViDiem: [
//         {id: 'thang_10', ten: 'Thang 10'},
//         {id: 'phan_tram', ten: 'Phần trăm'}
//     ],
//     loaiDiemDanhGia: [
//         {id: 'chuyen_can', ten: 'Chuyên cần'},
//         {id: 'bai_tap', ten: 'Bài tập'},
//         {id: 'giua_ky', ten: 'Giữa kỳ'},
//         {id: 'cuoi_ky', ten: 'Cuối kỳ'},
//         {id: 'thuc_hanh', ten: 'Thực hành / Lab'},
//         {id: 'do_an', ten: 'Đồ án / ASM'},
//         {id: 'khac', ten: 'Khác'}
//     ]
// }
//
//
// const bangLayDuLieuCoSan = [
//     'nganh',
//     'trinhDoDaoTao',
//     'loaiChuongTrinh',
//     'nganhTrinhDoDaoTao',
//     'nganhLoaiChuongTrinh',
//     'chuongTrinh',
//     'chuongTrinhVersion',
//     'khungKy',
//     'khungKyGoc',
//     'nhomKienThuc',
//     'nhomKienThucGoc',
//     'nhomTuChon',
//     'nhomTuChonGoc',
//     'monHoc',
//     'chuongTrinhMon',
//     'monTuChon',
//     'syllabusMonHoc',
//     'syllabusMonHocGocTaiLieu',
//     'syllabusMonHocGocDieuKien',
//     'syllabusMonHocGocChuongBai',
//     'syllabusMonHocGoc',
//     'mucTieuChuongTrinhGoc',
//     'mucTieuChuongTrinh',
//     'nangLucDauRaGoc',
//     'nangLucDauRa',
//     'viTriViecLamGoc',
//     'viTriViecLam',
//     'dieuKienTotNghiepGoc',
//     'dieuKienTotNghiep',
//     'chuongTrinhVersionMucTieu',
//     'chuongTrinhVersionNangLuc',
//     'chuongTrinhVersionViTriViecLam',
//     'chuongTrinhVersionDieuKienTotNghiep',
//     'syllabusChuongTrinh',
//     'syllabusChuongTrinhGoc',
//     'monTienQuyet',
//     'quyDoiDiem',
//     'quyDoiDiemMau',
//     'chuongTrinhMonQuyDoiDiemMau',
//     'cauHinhDanhGiaMau',
//     'dieuKienMonHoc',
//     'dieuKienMonHocGoc',
//     'syllabusMonHocDieuKien',
//     'taiLieuGoc',
//     'syllabusMonHocTaiLieu',
//     'syllabusChuongBai',
//     'syllabusTaiLieu'
// ]
//
// function layDanhSachTuKetQua(result) {
//     if (Array.isArray(result)) return result
//     if (Array.isArray(result?.items)) return result.items
//     if (Array.isArray(result?.content)) return result.content
//     if (Array.isArray(result?.data)) return result.data
//     if (Array.isArray(result?.data?.items)) return result.data.items
//     if (Array.isArray(result?.data?.content)) return result.data.content
//     return []
// }
//
// function taoBanDo(list = []) {
//     return new Map(list.map((item) => [item.id, item]))
// }
//
// function layTen(map, id, keys = ['ten', 'tenMon', 'tenNganh', 'tenChuongTrinh', 'tenVersion']) {
//     const item = map.get(id) || map.get(String(id))
//     if (!item) return id || '-'
//
//     for (const key of keys) {
//         if (item[key]) return item[key]
//     }
//
//     return item.ma || item.id || '-'
// }
//
// function layGiaTriTheoKhoa(item, keys = [], fallback = null) {
//     if (!item) return fallback
//
//     for (const key of keys) {
//         if (!key) continue
//         const value = item[key]
//         if (value !== null && value !== undefined && value !== '') return value
//     }
//
//     return fallback
// }
//
// function layDoiTuongLienKet(row, map, idKeys = [], objectKeys = []) {
//     for (const key of objectKeys) {
//         const value = row?.[key]
//         if (value && typeof value === 'object') return value
//     }
//
//     const id = layGiaTriTheoKhoa(row, idKeys, null)
//     if (id !== null && id !== undefined) {
//         return map.get(id) || map.get(String(id)) || {}
//     }
//
//     return {}
// }
//
// function themTenLienKet(duLieu) {
//     const nganhMap = taoBanDo(duLieu.nganh)
//     const trinhDoMap = taoBanDo(duLieu.trinhDoDaoTao)
//     const loaiCtMap = taoBanDo(duLieu.loaiChuongTrinh)
//     const nganhLoaiCtMap = taoBanDo(duLieu.nganhLoaiChuongTrinh)
//     const chuongTrinhMap = taoBanDo(duLieu.chuongTrinh)
//     const versionMap = taoBanDo(duLieu.chuongTrinhVersion)
//     const monHocMap = taoBanDo(duLieu.monHoc)
//     const khungKyMap = taoBanDo(duLieu.khungKy)
//     const khungKyGocMap = taoBanDo(duLieu.khungKyGoc)
//     const nhomKienThucMap = taoBanDo(duLieu.nhomKienThuc)
//     const nhomKienThucGocMap = taoBanDo(duLieu.nhomKienThucGoc)
//     const nhomTuChonMap = taoBanDo(duLieu.nhomTuChon)
//     const nhomTuChonGocMap = taoBanDo(duLieu.nhomTuChonGoc)
//     const chuongTrinhMonMap = taoBanDo(duLieu.chuongTrinhMon)
//     const quyDoiDiemMauMap = taoBanDo(duLieu.quyDoiDiemMau)
//     const syllabusMonMap = taoBanDo(duLieu.syllabusMonHoc)
//     const syllabusChuongTrinhGocMap = taoBanDo(duLieu.syllabusChuongTrinhGoc)
//     const syllabusMonHocGocMap = taoBanDo(duLieu.syllabusMonHocGoc)
//     const dieuKienMonHocGocMap = taoBanDo(duLieu.dieuKienMonHocGoc)
//     const taiLieuGocMap = taoBanDo(duLieu.taiLieuGoc)
//     const mucTieuGocMap = taoBanDo(duLieu.mucTieuChuongTrinhGoc)
//     const nangLucGocMap = taoBanDo(duLieu.nangLucDauRaGoc)
//     const viTriGocMap = taoBanDo(duLieu.viTriViecLamGoc)
//     const dieuKienTotNghiepGocMap = taoBanDo(duLieu.dieuKienTotNghiepGoc)
//
//     return {
//         ...duLieu,
//         nganhTrinhDoDaoTao: duLieu.nganhTrinhDoDaoTao.map((item) => ({
//             ...item,
//             tenNganh: layTen(nganhMap, item.nganhId, ['tenNganh']),
//             maTrinhDo: layTen(trinhDoMap, item.trinhDoId, ['maTrinhDo', 'ma']),
//             tenTrinhDo: layTen(trinhDoMap, item.trinhDoId, ['tenTrinhDo', 'ten']),
//             moTaTrinhDo: layTen(trinhDoMap, item.trinhDoId, ['moTa'])
//         })),
//         nganhLoaiChuongTrinh: duLieu.nganhLoaiChuongTrinh.map((item) => ({
//             ...item,
//             tenNganh: layTen(nganhMap, item.nganhId, ['tenNganh']),
//             maLoai: layTen(loaiCtMap, item.loaiChuongTrinhId, ['maLoai', 'ma']),
//             tenLoaiChuongTrinh: layTen(loaiCtMap, item.loaiChuongTrinhId, ['tenLoai', 'ten']),
//             soThang: layTen(loaiCtMap, item.loaiChuongTrinhId, ['soThang']),
//             soKy: layTen(loaiCtMap, item.loaiChuongTrinhId, ['soKy']),
//             moTaLoai: layTen(loaiCtMap, item.loaiChuongTrinhId, ['moTa'])
//         })),
//         chuongTrinh: duLieu.chuongTrinh.map((item) => {
//             const nganhLoai = nganhLoaiCtMap.get(item.nganhLoaiChuongTrinhId) || nganhLoaiCtMap.get(String(item.nganhLoaiChuongTrinhId)) || {}
//             const nganhId = item.nganhId || nganhLoai.nganhId
//             const loaiChuongTrinhId = item.loaiChuongTrinhId || nganhLoai.loaiChuongTrinhId
//
//             return {
//                 ...item,
//                 nganhId,
//                 loaiChuongTrinhId,
//                 tenNganh: layTen(nganhMap, nganhId, ['tenNganh']),
//                 tenTrinhDo: layTen(trinhDoMap, item.trinhDoId, ['tenTrinhDo']),
//                 tenLoaiChuongTrinh: layTen(loaiCtMap, loaiChuongTrinhId, ['tenLoai']),
//                 tenKhungKyGoc: layTen(khungKyGocMap, item.khungKyGocId, ['maKy', 'tenKy'])
//             }
//         }),
//         chuongTrinhVersion: duLieu.chuongTrinhVersion.map((item) => ({
//             ...item,
//             tenChuongTrinh: layTen(chuongTrinhMap, item.chuongTrinhId, ['tenChuongTrinh'])
//         })),
//         khungKy: duLieu.khungKy.map((item) => ({
//             ...item,
//             tenVersion: layTen(versionMap, item.chuongTrinhVersionId, ['tenVersion']),
//             tenLoaiChuongTrinh: layTen(loaiCtMap, item.loaiChuongTrinhId, ['tenLoai']),
//             tenKhungKyGoc: layTen(khungKyGocMap, item.khungKyGocId, ['maKy', 'tenKy'])
//         })),
//         mucTieuChuongTrinh: duLieu.mucTieuChuongTrinh.map((item) => ({
//             ...item,
//             tenVersion: layTen(versionMap, item.chuongTrinhVersionId, ['tenVersion'])
//         })),
//         nangLucDauRa: duLieu.nangLucDauRa.map((item) => ({
//             ...item,
//             tenVersion: layTen(versionMap, item.chuongTrinhVersionId, ['tenVersion'])
//         })),
//         viTriViecLam: duLieu.viTriViecLam.map((item) => ({
//             ...item,
//             tenVersion: layTen(versionMap, item.chuongTrinhVersionId, ['tenVersion'])
//         })),
//         dieuKienTotNghiep: duLieu.dieuKienTotNghiep.map((item) => ({
//             ...item,
//             tenVersion: layTen(versionMap, item.chuongTrinhVersionId, ['tenVersion'])
//         })),
//         chuongTrinhVersionMucTieu: duLieu.chuongTrinhVersionMucTieu.map((item) => ({
//             ...item,
//             tenVersion: layTen(versionMap, item.chuongTrinhVersionId, ['tenVersion']),
//             tenMucTieuGoc: layTen(mucTieuGocMap, item.mucTieuGocId, ['ma', 'noiDung'])
//         })),
//         chuongTrinhVersionNangLuc: duLieu.chuongTrinhVersionNangLuc.map((item) => ({
//             ...item,
//             tenVersion: layTen(versionMap, item.chuongTrinhVersionId, ['tenVersion']),
//             tenNangLucGoc: layTen(nangLucGocMap, item.nangLucGocId, ['ma', 'noiDung'])
//         })),
//         chuongTrinhVersionViTriViecLam: duLieu.chuongTrinhVersionViTriViecLam.map((item) => ({
//             ...item,
//             tenVersion: layTen(versionMap, item.chuongTrinhVersionId, ['tenVersion']),
//             tenViTriGoc: layTen(viTriGocMap, item.viTriGocId, ['ma', 'ten'])
//         })),
//         chuongTrinhVersionDieuKienTotNghiep: duLieu.chuongTrinhVersionDieuKienTotNghiep.map((item) => ({
//             ...item,
//             tenVersion: layTen(versionMap, item.chuongTrinhVersionId, ['tenVersion']),
//             tenDieuKienGoc: layTen(dieuKienTotNghiepGocMap, item.dieuKienGocId, ['ma', 'noiDung'])
//         })),
//         syllabusChuongTrinhGoc: duLieu.syllabusChuongTrinhGoc.map((item) => ({...item})),
//         syllabusChuongTrinh: duLieu.syllabusChuongTrinh.map((item) => ({
//             ...item,
//             tenVersion: layTen(versionMap, item.chuongTrinhVersionId, ['tenVersion']),
//             tenSyllabusChuongTrinhGoc: layTen(syllabusChuongTrinhGocMap, item.syllabusChuongTrinhGocId, ['ma', 'ten'])
//         })),
//         nhomKienThucGoc: duLieu.nhomKienThucGoc.map((item) => ({...item})),
//         nhomKienThuc: duLieu.nhomKienThuc.map((item) => ({
//             ...item,
//             tenVersion: layTen(versionMap, item.chuongTrinhVersionId, ['tenVersion']),
//             tenNhomKienThucGoc: layTen(nhomKienThucGocMap, item.nhomKienThucGocId, ['ma', 'ten'])
//         })),
//         nhomTuChonGoc: duLieu.nhomTuChonGoc.map((item) => ({...item})),
//         nhomTuChon: duLieu.nhomTuChon.map((item) => ({
//             ...item,
//             tenVersion: layTen(versionMap, item.chuongTrinhVersionId, ['tenVersion']),
//             tenNhomTuChonGoc: layTen(nhomTuChonGocMap, item.nhomTuChonGocId, ['ma', 'ten'])
//         })),
//         chuongTrinhMon: duLieu.chuongTrinhMon.map((item) => ({
//             ...item,
//             tenVersion: layTen(versionMap, item.chuongTrinhVersionId, ['tenVersion']),
//             tenMon: layTen(monHocMap, item.monHocId, ['tenMon']),
//             tenKy: layTen(khungKyMap, item.khungKyId, ['tenKy']),
//             tenNhomKienThuc: layTen(nhomKienThucMap, item.nhomKienThucId, ['ten'])
//         })),
//         monTuChon: duLieu.monTuChon.map((item) => ({
//             ...item,
//             tenNhomTuChon: layTen(nhomTuChonMap, item.nhomId, ['ten']),
//             tenChuongTrinhMon: layTen(chuongTrinhMonMap, item.chuongTrinhMonId, ['maMonTrongCt', 'tenMon'])
//         })),
//         monTienQuyet: duLieu.monTienQuyet.map((item) => ({
//             ...item,
//             tenMon: layTen(chuongTrinhMonMap, item.monId, ['maMonTrongCt', 'tenMon']),
//             tenMonDieuKien: layTen(chuongTrinhMonMap, item.monDieuKienId, ['maMonTrongCt', 'tenMon'])
//         })),
//         quyDoiDiem: duLieu.quyDoiDiem.map((item) => ({
//             ...item,
//             tenChuongTrinhMon: layTen(chuongTrinhMonMap, item.chuongTrinhMonId, ['maMonTrongCt', 'tenMon']),
//             tenCotDiemMau: layGiaTriTheoKhoa(
//                 item,
//                 ['tenCotDiemMau', 'ten_cot_diem_mau', 'tenCotDiem', 'ten_cot_diem', 'ghiChu', 'ghi_chu'],
//                 ''
//             )
//         })),
//         quyDoiDiemMau: duLieu.quyDoiDiemMau.map((item) => ({...item})),
//         cauHinhDanhGiaMau: duLieu.cauHinhDanhGiaMau.map((item) => ({
//             ...item,
//             tenChuongTrinhMon: layTen(chuongTrinhMonMap, item.chuongTrinhMonId, ['maMonTrongCt', 'tenMon'])
//         })),
//         chuongTrinhMonQuyDoiDiemMau: duLieu.chuongTrinhMonQuyDoiDiemMau.map((item) => {
//             const quyDoiDiemMauId = layGiaTriTheoKhoa(item, ['quyDoiDiemMauId', 'quy_doi_diem_mau_id', 'mauId'], null)
//             const chuongTrinhMonId = layGiaTriTheoKhoa(item, ['chuongTrinhMonId', 'chuong_trinh_mon_id'], null)
//             const mau = layDoiTuongLienKet(
//                 item,
//                 quyDoiDiemMauMap,
//                 ['quyDoiDiemMauId', 'quy_doi_diem_mau_id', 'mauId'],
//                 ['quyDoiDiemMau', 'quy_doi_diem_mau', 'mau', 'quyDoiDiemMauDto', 'quyDoiDiemMauDTO']
//             )
//             const maMau = layGiaTriTheoKhoa(mau, ['ma', 'maMau', 'ma_mau'], '')
//             const tenMau = layGiaTriTheoKhoa(mau, ['ten', 'tenMau', 'ten_mau', 'tenCotDiem', 'ten_cot_diem'], maMau || '-')
//
//             return {
//                 ...item,
//                 quyDoiDiemMauId,
//                 chuongTrinhMonId,
//                 maQuyDoiDiemMau: maMau,
//                 maMau,
//                 tenQuyDoiDiemMau: tenMau,
//                 tenMau,
//                 loaiMau: layGiaTriTheoKhoa(mau, ['loaiMau', 'loai_mau', 'loai'], layGiaTriTheoKhoa(item, ['loaiMau', 'loai_mau', 'loai'], '')),
//                 nguongTu: layGiaTriTheoKhoa(mau, ['nguongTu', 'nguong_tu', 'tuDiem', 'tu_diem'], layGiaTriTheoKhoa(item, ['nguongTu', 'nguong_tu', 'tuDiem', 'tu_diem'], null)),
//                 nguongDen: layGiaTriTheoKhoa(mau, ['nguongDen', 'nguong_den', 'denDiem', 'den_diem'], layGiaTriTheoKhoa(item, ['nguongDen', 'nguong_den', 'denDiem', 'den_diem'], null)),
//                 diemQuyDoi: layGiaTriTheoKhoa(mau, ['diemQuyDoi', 'diem_quy_doi', 'diem', 'diemKetQua', 'diem_ket_qua'], layGiaTriTheoKhoa(item, ['diemQuyDoi', 'diem_quy_doi', 'diem', 'diemKetQua', 'diem_ket_qua'], null)),
//                 ketQua: layGiaTriTheoKhoa(mau, ['ketQua', 'ket_qua'], layGiaTriTheoKhoa(item, ['ketQua', 'ket_qua'], '')),
//                 tyLe: layGiaTriTheoKhoa(mau, ['tyLe', 'ty_le', 'tiLe', 'ti_le'], layGiaTriTheoKhoa(item, ['tyLe', 'ty_le', 'tiLe', 'ti_le'], null)),
//                 diemToiDa: layGiaTriTheoKhoa(mau, ['diemToiDa', 'diem_toi_da'], layGiaTriTheoKhoa(item, ['diemToiDa', 'diem_toi_da'], null)),
//                 thuTu: layGiaTriTheoKhoa(mau, ['thuTu', 'thu_tu'], layGiaTriTheoKhoa(item, ['thuTu', 'thu_tu'], null)),
//                 batBuoc: layGiaTriTheoKhoa(mau, ['batBuoc', 'bat_buoc'], layGiaTriTheoKhoa(item, ['batBuoc', 'bat_buoc'], null)),
//                 congThuc: layGiaTriTheoKhoa(mau, ['congThuc', 'cong_thuc'], layGiaTriTheoKhoa(item, ['congThuc', 'cong_thuc'], '')),
//                 ghiChuMau: layGiaTriTheoKhoa(mau, ['ghiChu', 'ghi_chu'], ''),
//                 tenChuongTrinhMon: layTen(chuongTrinhMonMap, chuongTrinhMonId, ['maMonTrongCt', 'tenMon'])
//             }
//         }),
//         syllabusMonHoc: duLieu.syllabusMonHoc.map((item) => ({
//             ...item,
//             tenChuongTrinhMon: layTen(chuongTrinhMonMap, item.chuongTrinhMonId, ['maMonTrongCt', 'tenMon']),
//             tenSyllabusMonHocGoc: layTen(syllabusMonHocGocMap, item.syllabusMonHocGocId, ['ten', 'ma']),
//             maSyllabusMonHocGoc: layTen(syllabusMonHocGocMap, item.syllabusMonHocGocId, ['ma'])
//         })),
//         syllabusMonHocGoc: duLieu.syllabusMonHocGoc.map((item) => ({
//             ...item,
//             tenMonHoc: layTen(monHocMap, item.monHocId, ['maMon', 'tenMon'])
//         })),
//         syllabusMonHocGocChuongBai: duLieu.syllabusMonHocGocChuongBai.map((item) => ({
//             ...item,
//             tenSyllabusMonHocGoc: layTen(syllabusMonHocGocMap, item.syllabusMonHocGocId, ['ma', 'ten'])
//         })),
//         syllabusMonHocGocDieuKien: duLieu.syllabusMonHocGocDieuKien.map((item) => ({
//             ...item,
//             tenSyllabusMonHocGoc: layTen(syllabusMonHocGocMap, item.syllabusMonHocGocId, ['ma', 'ten']),
//             tenDieuKienGoc: layTen(dieuKienMonHocGocMap, item.dieuKienGocId, ['ma', 'noiDung'])
//         })),
//         syllabusMonHocGocTaiLieu: duLieu.syllabusMonHocGocTaiLieu.map((item) => ({
//             ...item,
//             tenSyllabusMonHocGoc: layTen(syllabusMonHocGocMap, item.syllabusMonHocGocId, ['ma', 'ten']),
//             tenTaiLieuGoc: layTen(taiLieuGocMap, item.taiLieuGocId, ['ma', 'ten'])
//         })),
//         dieuKienMonHoc: duLieu.dieuKienMonHoc.map((item) => ({
//             ...item,
//             tenSyllabusMon: layTen(syllabusMonMap, item.syllabusMonId, [
//                 'ten',
//                 'tenSyllabusMonHocGoc',
//                 'ma',
//                 'maSyllabusMonHocGoc',
//                 'mucTieu'
//             ])
//         })),
//         dieuKienMonHocGoc: duLieu.dieuKienMonHocGoc.map((item) => ({...item})),
//         syllabusMonHocDieuKien: duLieu.syllabusMonHocDieuKien.map((item) => ({
//             ...item,
//             tenSyllabusMon: layTen(syllabusMonMap, item.syllabusMonId, ['viTri', 'tinhChat', 'mucTieu']),
//             tenDieuKienGoc: layTen(dieuKienMonHocGocMap, item.dieuKienGocId, ['ma', 'noiDung'])
//         })),
//         taiLieuGoc: duLieu.taiLieuGoc.map((item) => ({...item})),
//         syllabusMonHocTaiLieu: duLieu.syllabusMonHocTaiLieu.map((item) => ({
//             ...item,
//             tenSyllabusMon: layTen(syllabusMonMap, item.syllabusMonId, ['viTri', 'tinhChat', 'mucTieu']),
//             tenTaiLieuGoc: layTen(taiLieuGocMap, item.taiLieuGocId, ['ma', 'ten'])
//         })),
//         syllabusChuongBai: duLieu.syllabusChuongBai.map((item) => ({
//             ...item,
//             tenSyllabusMon: layTen(syllabusMonMap, item.syllabusMonId, [
//                 'ten',
//                 'tenSyllabusMonHocGoc',
//                 'ma',
//                 'maSyllabusMonHocGoc',
//                 'mucTieu'
//             ])
//         })),
//         syllabusTaiLieu: duLieu.syllabusTaiLieu.map((item) => ({
//             ...item,
//             tenSyllabusMon: layTen(syllabusMonMap, item.syllabusMonId, [
//                 'ten',
//                 'tenSyllabusMonHocGoc',
//                 'ma',
//                 'maSyllabusMonHocGoc',
//                 'mucTieu'
//             ])
//         }))
//     }
// }
//
// function taoDuLieuRong() {
//     return {
//         nganh: [],
//         trinhDoDaoTao: [],
//         loaiChuongTrinh: [],
//         nganhTrinhDoDaoTao: [],
//         nganhLoaiChuongTrinh: [],
//         chuongTrinh: [],
//         chuongTrinhVersion: [],
//         khungKy: [],
//         khungKyGoc: [],
//         monHoc: [],
//         chuongTrinhMon: [],
//         nhomKienThuc: [],
//         nhomKienThucGoc: [],
//         nhomTuChon: [],
//         nhomTuChonGoc: [],
//         monTuChon: [],
//         mucTieuChuongTrinhGoc: [],
//         mucTieuChuongTrinh: [],
//         nangLucDauRaGoc: [],
//         nangLucDauRa: [],
//         viTriViecLamGoc: [],
//         viTriViecLam: [],
//         dieuKienTotNghiepGoc: [],
//         dieuKienTotNghiep: [],
//         chuongTrinhVersionMucTieu: [],
//         chuongTrinhVersionNangLuc: [],
//         chuongTrinhVersionViTriViecLam: [],
//         chuongTrinhVersionDieuKienTotNghiep: [],
//         syllabusChuongTrinh: [],
//         syllabusChuongTrinhGoc: [],
//         monTienQuyet: [],
//         quyDoiDiem: [],
//         quyDoiDiemMau: [],
//         chuongTrinhMonQuyDoiDiemMau: [],
//         cauHinhDanhGiaMau: [],
//         syllabusMonHoc: [],
//         syllabusMonHocGoc: [],
//         syllabusMonHocGocChuongBai: [],
//         syllabusMonHocGocDieuKien: [],
//         syllabusMonHocGocTaiLieu: [],
//         dieuKienMonHoc: [],
//         dieuKienMonHocGoc: [],
//         syllabusMonHocDieuKien: [],
//         taiLieuGoc: [],
//         syllabusMonHocTaiLieu: [],
//         syllabusChuongBai: [],
//         syllabusTaiLieu: []
//     }
// }
//
// function taoSelectedRong() {
//     return {
//         nganh: null,
//         trinhDoDaoTao: null,
//         loaiChuongTrinh: null,
//         nganhLoaiChuongTrinh: null,
//         chuongTrinh: null,
//         chuongTrinhVersion: null,
//         syllabusChuongTrinhGoc: null,
//         mucTieuChuongTrinhGoc: null,
//         mucTieuChuongTrinh: null,
//         nangLucDauRaGoc: null,
//         nangLucDauRa: null,
//         viTriViecLamGoc: null,
//         viTriViecLam: null,
//         dieuKienTotNghiepGoc: null,
//         dieuKienTotNghiep: null,
//         khungKy: null,
//         khungKyGoc: null,
//         nhomKienThuc: null,
//         nhomKienThucGoc: null,
//         nhomTuChon: null,
//         nhomTuChonGoc: null,
//         monHoc: null,
//         chuongTrinhMon: null,
//         quyDoiDiemMau: null,
//         cauHinhDanhGiaMau: null,
//         syllabusMonHoc: null,
//         syllabusMonHocGoc: null,
//         dieuKienMonHocGoc: null,
//         dieuKienMonHoc: null,
//         taiLieuGoc: null,
//         syllabusTaiLieu: null
//     }
// }
//
// function layDuLieuLuu(saved) {
//     if (!saved) return null
//
//     if (saved.id) {
//         return saved
//     }
//
//     if (saved.success !== undefined && saved.data) {
//         return saved.data
//     }
//
//     if (saved.data?.success !== undefined && saved.data?.data) {
//         return saved.data.data
//     }
//
//     if (saved.data?.data?.id) {
//         return saved.data.data
//     }
//
//     if (saved.data?.id) {
//         return saved.data
//     }
//
//     return saved
// }
//
// export function useDaoTaoXemChuongTrinh() {
//     const thongBao = ref('')
//     const loaiThongBao = ref('success')
//
//     const selected = reactive(taoSelectedRong())
//     const viewed = reactive(taoSelectedRong())
//     const rawData = reactive(taoDuLieuRong())
//     const tableMessages = reactive({})
//
//
//     const khoQuyDoiDiemMauGoc = ref([])
//
//     const duLieu = computed(() => themTenLienKet(rawData))
//
//     const quyDoiDiemMauGocRows = computed(() => {
//         const rowsDangCo = duLieu.value.quyDoiDiemMau || []
//         if (rowsDangCo.length) return rowsDangCo
//         return khoQuyDoiDiemMauGoc.value || []
//     })
//
//     const lookups = computed(() => ({
//         ...duLieu.value,
//         ...luaChonEnum
//     }))
//
//     function baoTinBang(key, message, type = 'success') {
//         if (key) {
//             tableMessages[key] = {message, type, at: Date.now()}
//             setTimeout(() => {
//                 if (tableMessages[key]?.message === message) delete tableMessages[key]
//             }, 5000)
//         }
//         baoTin(message, type)
//     }
//
//     function baoTin(message, type = 'success') {
//         thongBao.value = message
//         loaiThongBao.value = type
//
//         setTimeout(() => {
//             if (thongBao.value === message) thongBao.value = ''
//         }, 5000)
//     }
//
//
//     async function taiDuLieuCoSanTatCaBang() {
//         for (const key of bangLayDuLieuCoSan) {
//             const service = daoTaoXemChuongTrinhService[key]
//             if (!service?.getAll) continue
//
//             try {
//                 const result = await service.getAll({size: 200})
//                 const list = layDanhSachTuKetQua(result)
//                 rawData[key] = list
//                 if (key === 'quyDoiDiemMau' && list.length) {
//                     khoQuyDoiDiemMauGoc.value = list.map((item) => ({...item}))
//                 }
//             } catch (error) {
//                 console.warn(`Không tải được dữ liệu có sẵn của bảng ${key}`, error)
//             }
//         }
//     }
//     async function taiDuLieuBangTheoParams(key, params = {}) {
//         const service = daoTaoXemChuongTrinhService[key]
//         if (!service?.getAll) return
//
//         try {
//             const result = await service.getAll({
//                 size: 200,
//                 ...params
//             })
//
//             rawData[key] = layDanhSachTuKetQua(result)
//         } catch (error) {
//             console.warn(`Không tải được dữ liệu bảng ${key} theo params`, error)
//         }
//     }
//
//     async function taiDuLieuTheoMonTrongChuongTrinh(chuongTrinhMonId) {
//         if (!chuongTrinhMonId) {
//             rawData.chuongTrinhMonQuyDoiDiemMau = []
//             rawData.quyDoiDiem = []
//             return
//         }
//
//         await Promise.all([
//             taiDuLieuBangTheoParams('chuongTrinhMonQuyDoiDiemMau', {chuongTrinhMonId}),
//             taiDuLieuBangTheoParams('quyDoiDiem', {chuongTrinhMonId})
//         ])
//     }
//
//     watch(
//         () => selected.chuongTrinhMon?.id || null,
//         (chuongTrinhMonId) => {
//             taiDuLieuTheoMonTrongChuongTrinh(chuongTrinhMonId)
//         }
//     )
//     onMounted(() => {
//         taiDuLieuCoSanTatCaBang()
//     })
//
//     const selectionChildren = {
//         nganh: ['trinhDoDaoTao', 'loaiChuongTrinh', 'nganhLoaiChuongTrinh', 'chuongTrinh', 'chuongTrinhVersion', 'khungKy', 'nhomKienThuc', 'nhomTuChon', 'monHoc', 'chuongTrinhMon', 'syllabusMonHoc'],
//         trinhDoDaoTao: ['chuongTrinh', 'chuongTrinhVersion', 'khungKy', 'nhomKienThuc', 'nhomTuChon', 'monHoc', 'chuongTrinhMon', 'syllabusMonHoc'],
//         loaiChuongTrinh: ['nganhLoaiChuongTrinh', 'chuongTrinh', 'chuongTrinhVersion', 'khungKy', 'nhomKienThuc', 'nhomTuChon', 'monHoc', 'chuongTrinhMon', 'syllabusMonHoc'],
//         nganhLoaiChuongTrinh: ['chuongTrinh', 'chuongTrinhVersion', 'khungKy', 'nhomKienThuc', 'nhomTuChon', 'monHoc', 'chuongTrinhMon', 'syllabusMonHoc'],
//         chuongTrinh: ['chuongTrinhVersion', 'khungKy', 'nhomKienThuc', 'nhomTuChon', 'monHoc', 'chuongTrinhMon', 'syllabusMonHoc'],
//         chuongTrinhVersion: ['syllabusChuongTrinhGoc', 'syllabusChuongTrinh', 'mucTieuChuongTrinhGoc', 'mucTieuChuongTrinh', 'nangLucDauRaGoc', 'nangLucDauRa', 'viTriViecLamGoc', 'viTriViecLam', 'dieuKienTotNghiepGoc', 'dieuKienTotNghiep', 'khungKyGoc', 'khungKy', 'nhomKienThucGoc', 'nhomKienThuc', 'nhomTuChonGoc', 'nhomTuChon', 'monHoc', 'chuongTrinhMon', 'monTuChon', 'monTienQuyet', 'quyDoiDiemMau', 'quyDoiDiem', 'chuongTrinhMonQuyDoiDiemMau', 'syllabusMonHocGoc', 'syllabusMonHoc'],
//         khungKyGoc: ['khungKy', 'chuongTrinhMon'],
//         khungKy: ['chuongTrinhMon', 'syllabusMonHoc'],
//         nhomKienThucGoc: ['nhomKienThuc', 'chuongTrinhMon'],
//         nhomKienThuc: ['chuongTrinhMon', 'syllabusMonHoc'],
//         nhomTuChonGoc: ['nhomTuChon'],
//         nhomTuChon: [],
//         monHoc: ['syllabusMonHocGoc', 'chuongTrinhMon', 'syllabusMonHoc'],
//         chuongTrinhMon: ['quyDoiDiemMau', 'quyDoiDiem', 'chuongTrinhMonQuyDoiDiemMau', 'cauHinhDanhGiaMau', 'monTienQuyet', 'monTuChon', 'syllabusMonHoc'],
//         syllabusMonHocGoc: ['syllabusMonHoc'],
//         syllabusMonHoc: ['dieuKienMonHocGoc', 'taiLieuGoc'],
//         dieuKienMonHocGoc: [],
//         taiLieuGoc: []
//     }
//
//     function xoaLuaChonCon(key) {
//         const children = selectionChildren[key] || []
//         children.forEach((childKey) => {
//             if (childKey in selected) selected[childKey] = null
//         })
//     }
//
//     function xoaXemCon(key) {
//         const children = selectionChildren[key] || []
//         children.forEach((childKey) => {
//             if (childKey in viewed) viewed[childKey] = null
//         })
//     }
//
//     function selectEntity(key, item) {
//         const currentId = selected[key]?.id || null
//         const nextId = item?.id || null
//
//         selected[key] = item
//
//         if (String(currentId || '') !== String(nextId || '')) {
//             if (key in viewed) viewed[key] = null
//             xoaLuaChonCon(key)
//             xoaXemCon(key)
//         }
//     }
//
//     function viewEntity(key, item) {
//         const currentId = viewed[key]?.id || null
//         const nextId = item?.id || null
//
//         if (!item || String(currentId || '') === String(nextId || '')) {
//             if (key in viewed) viewed[key] = null
//             xoaXemCon(key)
//             return
//         }
//
//         viewed[key] = item
//         xoaXemCon(key)
//     }
//
//
//     function capNhatDongTrongRawData(key, row) {
//         if (!row?.id || !Array.isArray(rawData[key])) return row
//
//         const index = rawData[key].findIndex((item) => String(item.id || '') === String(row.id || ''))
//         if (index >= 0) rawData[key].splice(index, 1, row)
//         else rawData[key].push(row)
//
//         if (key === 'quyDoiDiemMau') {
//             const cacheIndex = khoQuyDoiDiemMauGoc.value.findIndex((item) => String(item.id || '') === String(row.id || ''))
//             if (cacheIndex >= 0) khoQuyDoiDiemMauGoc.value.splice(cacheIndex, 1, {...row})
//             else khoQuyDoiDiemMauGoc.value.push({...row})
//         }
//
//         return duLieu.value[key]?.find((item) => String(item.id || '') === String(row.id || '')) || row
//     }
//
//
//     function xoaDongTrongRawData(key, id) {
//         if (!id || !Array.isArray(rawData[key])) return
//         const index = rawData[key].findIndex((item) => String(item.id || '') === String(id || ''))
//         if (index >= 0) rawData[key].splice(index, 1)
//     }
//
//     function coGiaTri(value) {
//         return value !== null && value !== undefined && value !== ''
//     }
//
//     const bangGocGanVersion = {
//
//         nangLucDauRaGoc: {
//             joinKey: 'chuongTrinhVersionNangLuc',
//             gocIdKey: 'nangLucGocId',
//             serviceKey: 'chuongTrinhVersionNangLuc',
//             tenBang: 'Năng lực đầu ra'
//         },
//         viTriViecLamGoc: {
//             joinKey: 'chuongTrinhVersionViTriViecLam',
//             gocIdKey: 'viTriGocId',
//             serviceKey: 'chuongTrinhVersionViTriViecLam',
//             tenBang: 'Vị trí việc làm'
//         },
//
//     }
//
//     const bangGocTaoApDungVersion = {
//
//         dieuKienTotNghiepGoc: {
//             joinKey: 'dieuKienTotNghiep',
//             gocIdKey: 'ma',
//             matchBy: {rowKey: 'ma', itemKey: 'ma'},
//             serviceKey: 'dieuKienTotNghiep',
//             tenBang: 'Điều kiện tốt nghiệp gốc',
//             buildPayload: (item, chuongTrinhVersionId) => ({
//                 chuongTrinhVersionId,
//                 ma: item.ma || '',
//                 noiDung: item.noiDung || '',
//                 thuTu: layThuTuGanTiepTheo('dieuKienTotNghiep', chuongTrinhVersionId),
//                 ghiChu: item.ghiChu || ''
//             })
//         },
//
//         mucTieuChuongTrinhGoc: {
//             joinKey: 'mucTieuChuongTrinh',
//             gocIdKey: 'ma',
//             matchBy: {rowKey: 'ma', itemKey: 'ma'},
//             serviceKey: 'mucTieuChuongTrinh',
//             tenBang: 'Mục tiêu chương trình gốc',
//             buildPayload: (item, chuongTrinhVersionId) => ({
//                 chuongTrinhVersionId,
//                 ma: item.ma || '',
//                 loai: item.loai || 'chung',
//                 noiDung: item.noiDung || '',
//                 thuTu: layThuTuGanTiepTheo('mucTieuChuongTrinh', chuongTrinhVersionId),
//                 ghiChu: item.ghiChu || ''
//             })
//         },
//
//         syllabusChuongTrinhGoc: {
//             joinKey: 'syllabusChuongTrinh',
//             gocIdKey: 'syllabusChuongTrinhGocId',
//             matchBy: {rowKey: 'syllabusChuongTrinhGocId', itemKey: 'id'},
//             serviceKey: 'syllabusChuongTrinh',
//             tenBang: 'Syllabus chương trình gốc',
//             buildPayload: (item, chuongTrinhVersionId) => ({
//                 chuongTrinhVersionId,
//                 syllabusChuongTrinhGocId: item.id,
//                 ma: item.ma || '',
//                 ten: item.ten || '',
//                 mucTieu: item.mucTieu || '',
//                 doiTuongTuyenSinh: item.doiTuongTuyenSinh || '',
//                 thoiGianDaoTao: item.thoiGianDaoTao || '',
//                 khoiLuongKienThuc: item.khoiLuongKienThuc || '',
//                 dieuKienTotNghiep: item.dieuKienTotNghiep || '',
//                 phuongPhapDaoTao: item.phuongPhapDaoTao || '',
//                 phuongPhapDanhGia: item.phuongPhapDanhGia || '',
//                 huongDanThucHien: item.huongDanThucHien || '',
//                 duongDan: item.duongDan || '',
//                 ghiChu: item.ghiChu || '',
//                 moTaTongQuan: item.mucTieu || '',
//                 mucDich: item.mucTieu || '',
//                 yeuCauDaoTao: item.doiTuongTuyenSinh || ''
//             })
//         },
//         khungKyGoc: {
//             joinKey: 'khungKy',
//             gocIdKey: 'khungKyGocId',
//             serviceKey: 'khungKy',
//             tenBang: 'Khung kỳ gốc',
//             buildPayload: (item, chuongTrinhVersionId) => ({
//                 chuongTrinhVersionId,
//                 loaiChuongTrinhId: layLoaiChuongTrinhDangChon(),
//                 khungKyGocId: item.id,
//                 maKy: item.maKy || item.ma || '',
//                 tenKy: item.tenKy || item.ten || '',
//                 thuTu: item.thuTu || layThuTuGanTiepTheo('khungKy', chuongTrinhVersionId)
//             })
//         },
//         nhomKienThucGoc: {
//             joinKey: 'nhomKienThuc',
//             gocIdKey: 'nhomKienThucGocId',
//             serviceKey: 'nhomKienThuc',
//             tenBang: 'Nhóm kiến thức gốc',
//             buildPayload: (item, chuongTrinhVersionId) => ({
//                 chuongTrinhVersionId,
//                 nhomKienThucGocId: item.id,
//                 ma: item.ma || '',
//                 ten: item.ten || '',
//                 loaiNhom: item.loaiNhom || 'chung',
//                 thuTu: item.thuTu || layThuTuGanTiepTheo('nhomKienThuc', chuongTrinhVersionId),
//                 tongTinChi: item.tongTinChi ?? null,
//                 tongSoGio: item.tongSoGio ?? null,
//                 tongGioLyThuyet: item.tongGioLyThuyet ?? null,
//                 tongGioThucHanh: item.tongGioThucHanh ?? null,
//                 tongGioKiemTra: item.tongGioKiemTra ?? null,
//                 moTa: item.moTa || ''
//             })
//         },
//         nhomTuChonGoc: {
//             joinKey: 'nhomTuChon',
//             gocIdKey: 'nhomTuChonGocId',
//             serviceKey: 'nhomTuChon',
//             tenBang: 'Nhóm tự chọn gốc',
//             buildPayload: (item, chuongTrinhVersionId) => ({
//                 chuongTrinhVersionId,
//                 nhomTuChonGocId: item.id,
//                 ma: item.ma || '',
//                 ten: item.ten || '',
//                 soMonChon: item.soMonChon ?? null,
//                 soTinChiCanDat: item.soTinChiCanDat ?? null,
//                 ghiChu: item.ghiChu || '',
//                 moTa: item.moTa || ''
//             })
//         },
//     }
//
//     const bangMauGanMon = {
//         quyDoiDiemMau: {
//             joinKey: 'chuongTrinhMonQuyDoiDiemMau',
//             gocIdKey: 'quyDoiDiemMauId',
//             monIdKey: 'chuongTrinhMonId',
//             serviceKey: 'chuongTrinhMonQuyDoiDiemMau',
//             tenBang: 'Quy đổi điểm mẫu'
//         }
//     }
//
//     function timChuongTrinhMonTheoMonHocGoc(item, parentValues = {}) {
//         const chuongTrinhVersionId = parentValues.chuongTrinhVersionId || selected.chuongTrinhVersion?.id
//         const khungKyId = parentValues.khungKyId || selected.khungKy?.id
//         const nhomKienThucId = parentValues.nhomKienThucId || selected.nhomKienThuc?.id || null
//
//         return (rawData.chuongTrinhMon || []).find((row) => {
//             const cungVersion = String(row.chuongTrinhVersionId || '') === String(chuongTrinhVersionId || '')
//             const cungMonHoc = String(row.monHocId || row.monId || '') === String(item?.id || '')
//             const cungKhungKy = String(row.khungKyId || '') === String(khungKyId || '')
//             const cungNhomKienThuc = nhomKienThucId
//                 ? String(row.nhomKienThucId || '') === String(nhomKienThucId || '')
//                 : !row.nhomKienThucId
//
//             return cungVersion && cungMonHoc && cungKhungKy && cungNhomKienThuc
//         }) || null
//     }
//
//     function layThuTuChuongTrinhMonTiepTheo(chuongTrinhVersionId) {
//         const list = rawData.chuongTrinhMon || []
//         const thuTuLonNhat = list
//             .filter((row) => String(row.chuongTrinhVersionId || '') === String(chuongTrinhVersionId || ''))
//             .map((row) => Number(row.thuTu || 0))
//             .filter((value) => !Number.isNaN(value))
//             .reduce((max, value) => Math.max(max, value), 0)
//
//         return thuTuLonNhat + 1
//     }
//
//     function laySoTuNhieuTen(item, keys = [], fallback = null) {
//         for (const key of keys) {
//             const value = item?.[key]
//             if (value !== null && value !== undefined && value !== '') {
//                 const numberValue = Number(value)
//                 return Number.isNaN(numberValue) ? value : numberValue
//             }
//         }
//
//         return fallback
//     }
//
//     function taoPayloadChuongTrinhMonTuMonHocGoc(item, parentValues = {}) {
//         const chuongTrinhVersionId = parentValues.chuongTrinhVersionId || selected.chuongTrinhVersion?.id
//         const khungKyId = parentValues.khungKyId || selected.khungKy?.id
//         const nhomKienThucId = parentValues.nhomKienThucId || selected.nhomKienThuc?.id || null
//
//         return {
//             chuongTrinhVersionId,
//             monHocId: item.id,
//             maMonTrongCt: item.maMon || item.ma || `MON_${item.id}`,
//             khungKyId,
//             nhomKienThucId,
//
//             loai: item.loai || 'bat_buoc',
//             loaiHocPhan: item.loaiHocPhan || item.loai_hoc_phan || 'mon_hoc',
//             loaiPhamVi: item.loaiPhamVi || item.loai_pham_vi || 'mon_chuyen_nganh',
//             batBuoc: item.batBuoc ?? item.bat_buoc ?? true,
//             laMonDieuKien: item.laMonDieuKien ?? item.la_mon_dieu_kien ?? false,
//
//             thuTu: layThuTuChuongTrinhMonTiepTheo(chuongTrinhVersionId),
//
//             soTinChi: laySoTuNhieuTen(item, ['soTinChi', 'so_tin_chi', 'tinChi', 'tin_chi']),
//             tongGio: laySoTuNhieuTen(item, ['tongGio', 'tong_gio', 'soGio', 'so_gio', 'tongSoGio', 'tong_so_gio']),
//             gioLyThuyet: laySoTuNhieuTen(item, ['gioLyThuyet', 'gio_ly_thuyet', 'lyThuyet', 'ly_thuyet']),
//             gioThucHanh: laySoTuNhieuTen(item, ['gioThucHanh', 'gio_thuc_hanh', 'thucHanh', 'thuc_hanh']),
//             gioKiemTra: laySoTuNhieuTen(item, ['gioKiemTra', 'gio_kiem_tra', 'kiemTra', 'kiem_tra']),
//
//             ghiChu: item.ghiChu || item.ghi_chu || item.moTa || item.mo_ta || ''
//         }
//     }
//
//     async function toggleGanMonHocGocVaoChuongTrinhMon(item, parentValues = {}) {
//         const chuongTrinhVersionId = parentValues.chuongTrinhVersionId || selected.chuongTrinhVersion?.id
//         const khungKyId = parentValues.khungKyId || selected.khungKy?.id
//
//         if (!chuongTrinhVersionId) {
//             baoTinBang('monHoc', 'Cần chọn Version trước khi gắn Môn học gốc vào Chương trình môn.', 'error')
//             return
//         }
//
//         if (!khungKyId) {
//             baoTinBang('monHoc', 'Cần chọn Khung kỳ trước khi gắn Môn học gốc vào Chương trình môn.', 'error')
//             return
//         }
//
//         const serviceChuongTrinhMon = daoTaoXemChuongTrinhService.chuongTrinhMon
//         if (!serviceChuongTrinhMon?.create) {
//             baoTinBang('monHoc', 'Chưa khai báo API tạo chương trình môn.', 'error')
//             return
//         }
//
//         const existing = timChuongTrinhMonTheoMonHocGoc(item, parentValues)
//
//         try {
//             if (existing?.id) {
//                 if (!serviceChuongTrinhMon.delete) {
//                     baoTinBang('monHoc', 'Chưa khai báo API xóa chương trình môn để bỏ gắn môn học gốc.', 'error')
//                     return
//                 }
//
//                 await serviceChuongTrinhMon.delete(existing.id)
//                 xoaDongTrongRawData('chuongTrinhMon', existing.id)
//                 if (selected.chuongTrinhMon?.id === existing.id) selectEntity('chuongTrinhMon', null)
//                 if (selected.monHoc?.id === item.id) selectEntity('monHoc', null)
//                 baoTinBang('monHoc', 'Đã bỏ gắn Môn học gốc khỏi Chương trình môn hiện tại.')
//                 return
//             }
//
//             const payload = taoPayloadChuongTrinhMonTuMonHocGoc(item, parentValues)
//             const saved = await serviceChuongTrinhMon.create(payload)
//             const row = layDuLieuLuu(saved) || payload
//             const rowDaLuu = capNhatDongTrongRawData('chuongTrinhMon', row)
//
//             selectEntity('monHoc', item)
//             selectEntity('chuongTrinhMon', rowDaLuu)
//             baoTinBang('monHoc', 'Đã gắn Môn học gốc vào Chương trình môn qua chuong_trinh_mon.')
//         } catch (error) {
//             const message = layThongBaoLoi(error, 'Không gắn/bỏ gắn được Môn học gốc vào Chương trình môn.')
//             baoTinBang('monHoc', message, 'error')
//         }
//     }
//
//
//     function chuanHoaGiaTriQuyDoi(value) {
//         if (value === null || value === undefined || value === '') return null
//         const numberValue = Number(value)
//         if (Number.isNaN(numberValue)) return String(value)
//         return Number(numberValue.toFixed(2))
//     }
//
//     function taoPayloadQuyDoiDiemTuMau(item, chuongTrinhMonId) {
//         return {
//             chuongTrinhMonId,
//             loaiMau: item.loaiMau || 'COT_DIEM',
//             nguongTu: item.nguongTu ?? null,
//             nguongDen: item.nguongDen ?? null,
//             diemQuyDoi: item.diemQuyDoi ?? null,
//             ketQua: item.ketQua || 'dat',
//             tyLe: item.tyLe ?? null,
//             diemToiDa: item.diemToiDa ?? 10,
//             thuTu: item.thuTu ?? null,
//             batBuoc: item.batBuoc !== false,
//             congThuc: item.congThuc || '',
//             ghiChu: item.ghiChu || item.ten || item.ma || ''
//         }
//     }
//
//     function khopQuyDoiDiemTuMau(row, payload, chuongTrinhMonId) {
//         return String(row.chuongTrinhMonId || '') === String(chuongTrinhMonId || '')
//             && String(row.loaiMau || 'COT_DIEM') === String(payload.loaiMau || 'COT_DIEM')
//             && String(chuanHoaGiaTriQuyDoi(row.nguongTu)) === String(chuanHoaGiaTriQuyDoi(payload.nguongTu))
//             && String(chuanHoaGiaTriQuyDoi(row.nguongDen)) === String(chuanHoaGiaTriQuyDoi(payload.nguongDen))
//             && String(chuanHoaGiaTriQuyDoi(row.diemQuyDoi)) === String(chuanHoaGiaTriQuyDoi(payload.diemQuyDoi))
//             && String(row.ketQua || 'dat') === String(payload.ketQua || 'dat')
//             && String(chuanHoaGiaTriQuyDoi(row.tyLe)) === String(chuanHoaGiaTriQuyDoi(payload.tyLe))
//             && String(chuanHoaGiaTriQuyDoi(row.diemToiDa)) === String(chuanHoaGiaTriQuyDoi(payload.diemToiDa))
//             && String(row.thuTu || '') === String(payload.thuTu || '')
//             && String(row.batBuoc !== false) === String(payload.batBuoc !== false)
//             && String(row.congThuc || '') === String(payload.congThuc || '')
//     }
//
//     function timCacQuyDoiDiemDaCoTuMau(item, chuongTrinhMonId) {
//         const payload = taoPayloadQuyDoiDiemTuMau(item, chuongTrinhMonId)
//         const list = rawData.quyDoiDiem || []
//         return list.filter((row) => khopQuyDoiDiemTuMau(row, payload, chuongTrinhMonId))
//     }
//
//     function timQuyDoiDiemDaCoTuMau(item, chuongTrinhMonId) {
//         return timCacQuyDoiDiemDaCoTuMau(item, chuongTrinhMonId)[0] || null
//     }
//
//     async function dayQuyDoiDiemMauXuongQuyDoiDiem(item, chuongTrinhMonId) {
//         if (!item?.id || !chuongTrinhMonId) return {created: false, existed: false}
//
//         const serviceQuyDoiDiem = daoTaoXemChuongTrinhService.quyDoiDiem
//         if (!serviceQuyDoiDiem?.create) {
//             throw new Error('Chưa khai báo API tạo Quy đổi điểm.')
//         }
//
//         const existing = timQuyDoiDiemDaCoTuMau(item, chuongTrinhMonId)
//         if (existing?.id) {
//             capNhatDongTrongRawData('quyDoiDiem', existing)
//             return {created: false, existed: true, row: existing}
//         }
//
//         const payload = taoPayloadQuyDoiDiemTuMau(item, chuongTrinhMonId)
//         const saved = await serviceQuyDoiDiem.create(payload)
//         const row = layDuLieuLuu(saved) || payload
//         capNhatDongTrongRawData('quyDoiDiem', row)
//         return {created: true, existed: false, row}
//     }
//
//     async function xoaQuyDoiDiemDaDayTuMau(item, chuongTrinhMonId) {
//         if (!item?.id || !chuongTrinhMonId) return {deleted: 0}
//
//         const serviceQuyDoiDiem = daoTaoXemChuongTrinhService.quyDoiDiem
//         if (!serviceQuyDoiDiem?.delete) {
//             throw new Error('Chưa khai báo API xóa Quy đổi điểm.')
//         }
//
//         const rowsCanXoa = timCacQuyDoiDiemDaCoTuMau(item, chuongTrinhMonId).filter((row) => row?.id)
//         let deleted = 0
//
//         for (const row of rowsCanXoa) {
//             await serviceQuyDoiDiem.delete(row.id)
//             xoaDongTrongRawData('quyDoiDiem', row.id)
//             deleted += 1
//         }
//
//         return {deleted}
//     }
//     function taoPayloadQuyDoiDiemTuCotDiemMau(item, chuongTrinhMonId) {
//         return {
//             chuongTrinhMonId,
//             loaiMau: item.loaiDiem || 'COT_DIEM',
//             nguongTu: null,
//             nguongDen: null,
//             diemQuyDoi: null,
//             ketQua: 'dat',
//             tyLe: item.tyLe ?? null,
//             diemToiDa: item.diemToiDa ?? 10,
//             thuTu: item.thuTu ?? null,
//             batBuoc: item.batBuoc !== false,
//             congThuc: '',
//             ghiChu: item.tenCotDiem || item.ghiChu || ''
//         }
//     }
//
//     function khopQuyDoiDiemTuCotDiemMau(row, payload, chuongTrinhMonId) {
//         return String(row.chuongTrinhMonId || '') === String(chuongTrinhMonId || '')
//             && String(row.ma || '') === String(payload.ma || '')
//             && String(row.ten || '') === String(payload.ten || '')
//             && String(row.loaiMau || 'COT_DIEM') === String(payload.loaiMau || 'COT_DIEM')
//             && String(chuanHoaGiaTriQuyDoi(row.tyLe)) === String(chuanHoaGiaTriQuyDoi(payload.tyLe))
//             && String(chuanHoaGiaTriQuyDoi(row.diemToiDa)) === String(chuanHoaGiaTriQuyDoi(payload.diemToiDa))
//             && String(row.thuTu || '') === String(payload.thuTu || '')
//             && String(row.batBuoc !== false) === String(payload.batBuoc !== false)
//     }
//
//     function timQuyDoiDiemDaCoTuCotDiemMau(item, chuongTrinhMonId) {
//         const payload = taoPayloadQuyDoiDiemTuCotDiemMau(item, chuongTrinhMonId)
//
//         return (rawData.quyDoiDiem || []).find((row) => {
//             return khopQuyDoiDiemTuCotDiemMau(row, payload, chuongTrinhMonId)
//         }) || null
//     }
//
//     async function toggleCopyCotDiemMauVaoQuyDoiDiem(item, parentValues = {}) {
//         const chuongTrinhMonId = selected.chuongTrinhMon?.id || parentValues.chuongTrinhMonId
//
//         if (!chuongTrinhMonId) {
//             baoTinBang('cauHinhDanhGiaMau', 'Cần chọn Môn trong chương trình trước khi gán Cột điểm mẫu.', 'error')
//             return
//         }
//
//         const serviceQuyDoiDiem = daoTaoXemChuongTrinhService.quyDoiDiem
//
//         if (!serviceQuyDoiDiem?.create) {
//             baoTinBang('cauHinhDanhGiaMau', 'Chưa khai báo API tạo Quy đổi điểm.', 'error')
//             return
//         }
//
//         const existing = timQuyDoiDiemDaCoTuCotDiemMau(item, chuongTrinhMonId)
//
//         try {
//             if (existing?.id) {
//                 if (!serviceQuyDoiDiem.delete) {
//                     baoTinBang('cauHinhDanhGiaMau', 'Chưa khai báo API xóa Quy đổi điểm.', 'error')
//                     return
//                 }
//
//                 await serviceQuyDoiDiem.delete(existing.id)
//                 xoaDongTrongRawData('quyDoiDiem', existing.id)
//
//                 if (selected.quyDoiDiem?.id === existing.id) {
//                     selectEntity('quyDoiDiem', null)
//                 }
//
//                 baoTinBang('cauHinhDanhGiaMau', 'Đã bỏ gán Cột điểm mẫu khỏi Quy đổi điểm của môn đang chọn.')
//                 return
//             }
//
//             const payload = taoPayloadQuyDoiDiemTuCotDiemMau(item, chuongTrinhMonId)
//             const saved = await serviceQuyDoiDiem.create(payload)
//             const row = layDuLieuLuu(saved) || payload
//             const tenCotDiemMau = item.tenCotDiem || row.tenCotDiemMau || row.ghiChu || ''
//
//             const rowDaLuu = capNhatDongTrongRawData('quyDoiDiem', {
//                 ...row,
//                 tenCotDiemMau,
//                 ghiChu: row.ghiChu || tenCotDiemMau
//             })
//
//             selectEntity('cauHinhDanhGiaMau', item)
//             selectEntity('quyDoiDiem', rowDaLuu)
//
//             baoTinBang('cauHinhDanhGiaMau', 'Đã copy Cột điểm mẫu xuống Quy đổi điểm đã lưu cho môn trong chương trình.')
//         } catch (error) {
//             const message = layThongBaoLoi(error, 'Không gán được Cột điểm mẫu vào Quy đổi điểm.')
//             baoTinBang('cauHinhDanhGiaMau', message, 'error')
//         }
//     }
//     const bangGocGanSyllabus = {
//         dieuKienMonHocGoc: {
//             joinKey: 'syllabusMonHocDieuKien',
//             gocIdKey: 'dieuKienGocId',
//             syllabusIdKey: 'syllabusMonId',
//             serviceKey: 'syllabusMonHocDieuKien',
//             tenBang: 'Điều kiện môn học gốc'
//         },
//         taiLieuGoc: {
//             joinKey: 'syllabusMonHocTaiLieu',
//             gocIdKey: 'taiLieuGocId',
//             syllabusIdKey: 'syllabusMonId',
//             serviceKey: 'syllabusMonHocTaiLieu',
//             tenBang: 'Tài liệu gốc'
//         }
//     }
//
//     function timDongNoiMon(config, item, chuongTrinhMonId) {
//         const list = rawData[config.joinKey] || []
//         return list.find((row) => {
//             return String(row[config.monIdKey] || '') === String(chuongTrinhMonId || '')
//                 && String(row[config.gocIdKey] || '') === String(item?.id || '')
//         })
//     }
//
//
//     function timDongNoiSyllabus(config, item, syllabusMonId) {
//         const list = rawData[config.joinKey] || []
//         return list.find((row) => {
//             return String(row[config.syllabusIdKey] || '') === String(syllabusMonId || '')
//                 && String(row[config.gocIdKey] || '') === String(item?.id || '')
//         })
//     }
//
//     function layThuTuGanSyllabusTiepTheo(joinKey, syllabusMonId) {
//         const list = rawData[joinKey] || []
//         const thuTuLonNhat = list
//             .filter((row) => String(row.syllabusMonId || '') === String(syllabusMonId || ''))
//             .map((row) => Number(row.thuTu || 0))
//             .filter((value) => !Number.isNaN(value))
//             .reduce((max, value) => Math.max(max, value), 0)
//
//         return thuTuLonNhat + 1
//     }
//
//     function layThuTuGanTiepTheo(joinKey, chuongTrinhVersionId) {
//         const list = rawData[joinKey] || []
//         const thuTuLonNhat = list
//             .filter((row) => String(row.chuongTrinhVersionId || '') === String(chuongTrinhVersionId || ''))
//             .map((row) => Number(row.thuTu || 0))
//             .filter((value) => !Number.isNaN(value))
//             .reduce((max, value) => Math.max(max, value), 0)
//
//         return thuTuLonNhat + 1
//     }
//
//     function layThuTuGanTheoChaTiepTheo(joinKey, parentKey, parentId) {
//         const list = rawData[joinKey] || []
//         const thuTuLonNhat = list
//             .filter((row) => String(row[parentKey] || '') === String(parentId || ''))
//             .map((row) => Number(row.thuTu || 0))
//             .filter((value) => !Number.isNaN(value))
//             .reduce((max, value) => Math.max(max, value), 0)
//
//         return thuTuLonNhat + 1
//     }
//
//     function layLoaiChuongTrinhDangChon() {
//         return selected.loaiChuongTrinh?.id || selected.chuongTrinh?.loaiChuongTrinhId || null
//     }
//
//     function timDongNoiVersion(config, item, chuongTrinhVersionId) {
//         const list = rawData[config.joinKey] || []
//         return list.find((row) => {
//             return String(row.chuongTrinhVersionId || '') === String(chuongTrinhVersionId || '')
//                 && String(row[config.gocIdKey] || '') === String(item?.id || '')
//         })
//     }
//
//     async function ganBangGocVaoVersion(key, item) {
//         const config = bangGocGanVersion[key]
//         if (!config) return false
//
//         if (!item) {
//             selectEntity(key, null)
//             return true
//         }
//
//         const chuongTrinhVersionId = selected.chuongTrinhVersion?.id
//         if (!chuongTrinhVersionId) {
//             baoTinBang(key, `Cần chọn Version trước khi gán ${config.tenBang}.`, 'error')
//             return true
//         }
//
//         const existing = timDongNoiVersion(config, item, chuongTrinhVersionId)
//         if (existing) {
//             selectEntity(key, item)
//             baoTinBang(key, `Đã chọn ${config.tenBang} đã được gán trong Version hiện tại.`)
//             return true
//         }
//
//         const service = daoTaoXemChuongTrinhService[config.serviceKey]
//         if (!service?.create) {
//             baoTinBang(key, `Chưa khai báo service bảng nối cho ${config.tenBang}.`, 'error')
//             return true
//         }
//
//         try {
//             const payload = {
//                 chuongTrinhVersionId,
//                 [config.gocIdKey]: item.id,
//                 thuTu: layThuTuGanTiepTheo(config.joinKey, chuongTrinhVersionId),
//                 ghiChu: item.ghiChu || ''
//             }
//
//             const saved = await service.create(payload)
//             const row = layDuLieuLuu(saved) || payload
//             capNhatDongTrongRawData(config.joinKey, row)
//             selectEntity(key, item)
//             baoTinBang(key, `Đã gán ${config.tenBang} vào Version qua bảng nối.`)
//         } catch (error) {
//             const message = layThongBaoLoi(error, `Không gán được ${config.tenBang} vào Version.`)
//             baoTinBang(key, message, 'error')
//         }
//
//         return true
//     }
//
//     const bangCoNutLuuLienKet = new Set([
//         'chuongTrinhMon',
//         'mucTieuChuongTrinhGoc',
//         'mucTieuChuongTrinh',
//         'nangLucDauRaGoc',
//         'nangLucDauRa',
//         'viTriViecLamGoc',
//         'viTriViecLam',
//         'dieuKienTotNghiepGoc',
//         'dieuKienTotNghiep',
//         'monTienQuyet',
//         'quyDoiDiemMau',
//         'dieuKienMonHocGoc',
//         'taiLieuGoc',
//         'dieuKienMonHoc',
//         'syllabusChuongBai',
//         'syllabusTaiLieu'
//     ])
//
//     function timDongNoiBangGoc(key, item, chuongTrinhVersionId) {
//         const config = bangGocGanVersion[key]
//         if (!config) return null
//         return timDongNoiVersion(config, item, chuongTrinhVersionId)
//     }
//
//     function timDongApDungVersion(key, item, chuongTrinhVersionId) {
//         const config = bangGocTaoApDungVersion[key]
//         if (!config) return null
//
//         const list = rawData[config.joinKey] || []
//         const rowKey = config.matchBy?.rowKey || config.gocIdKey
//         const itemKey = config.matchBy?.itemKey || 'id'
//
//         return list.find((row) => {
//             return String(row.chuongTrinhVersionId || '') === String(chuongTrinhVersionId || '')
//                 && String(row[rowKey] || '') === String(item?.[itemKey] || '')
//         })
//     }
//
//     function layConfigGanSyllabusTheoNguCanh(key, parentValues = {}) {
//         if (key !== 'dieuKienMonHocGoc' && key !== 'taiLieuGoc') return null
//
//         const gocIdKey = key === 'dieuKienMonHocGoc' ? 'dieuKienGocId' : 'taiLieuGocId'
//         const tenBang = key === 'dieuKienMonHocGoc' ? 'Điều kiện môn học gốc' : 'Tài liệu gốc'
//
//         const syllabusMonHocGocId = parentValues.syllabusMonHocGocId || selected.syllabusMonHocGoc?.id
//         if (syllabusMonHocGocId) {
//             return {
//                 joinKey: key === 'dieuKienMonHocGoc' ? 'syllabusMonHocGocDieuKien' : 'syllabusMonHocGocTaiLieu',
//                 gocIdKey,
//                 syllabusIdKey: 'syllabusMonHocGocId',
//                 syllabusId: syllabusMonHocGocId,
//                 serviceKey: key === 'dieuKienMonHocGoc' ? 'syllabusMonHocGocDieuKien' : 'syllabusMonHocGocTaiLieu',
//                 tenBang
//             }
//         }
//
//         const syllabusMonId = parentValues.syllabusMonId || selected.syllabusMonHoc?.id
//         if (syllabusMonId) {
//             return {
//                 joinKey: key === 'dieuKienMonHocGoc' ? 'syllabusMonHocDieuKien' : 'syllabusMonHocTaiLieu',
//                 gocIdKey,
//                 syllabusIdKey: 'syllabusMonId',
//                 syllabusId: syllabusMonId,
//                 serviceKey: key === 'dieuKienMonHocGoc' ? 'syllabusMonHocDieuKien' : 'syllabusMonHocTaiLieu',
//                 tenBang
//             }
//         }
//
//         return null
//     }
//
//     function timDieuKienMonHocDaCopyTuGoc(item, syllabusMonId) {
//         return (rawData.dieuKienMonHoc || []).find((row) => {
//             return String(row.syllabusMonId || '') === String(syllabusMonId || '')
//                 && String(row.ma || '') === String(item?.ma || '')
//         }) || null
//     }
//
//     function taoPayloadDieuKienMonHocTuGoc(item, syllabusMonId) {
//         return {
//             syllabusMonId,
//             ma: item.ma || '',
//             loai: item.loai || 'phong_hoc',
//             ten: item.ten || item.noiDung || item.ma || 'Điều kiện môn học',
//             noiDung: item.noiDung || item.ten || item.ma || '',
//             soLuong: item.soLuong ?? null,
//             yeuCau: item.yeuCau || item.noiDung || item.ten || item.ma || '',
//             thuTu: layThuTuGanTheoChaTiepTheo('dieuKienMonHoc', 'syllabusMonId', syllabusMonId),
//             ghiChu: item.ghiChu || ''
//         }
//     }
//
//     function timSyllabusTaiLieuDaCopyTuGoc(item, syllabusMonId) {
//         return (rawData.syllabusTaiLieu || []).find((row) => {
//             return String(row.syllabusMonId || '') === String(syllabusMonId || '')
//                 && String(row.ma || '') === String(item?.ma || '')
//         }) || null
//     }
//
//     function taoPayloadSyllabusTaiLieuTuGoc(item, syllabusMonId) {
//         return {
//             syllabusMonId,
//             ma: item.ma || '',
//             ten: item.ten || '',
//             loai: item.loai || 'GIAO_TRINH',
//             tacGia: item.tacGia || '',
//             nhaXuatBan: item.nhaXuatBan || '',
//             namXuatBan: item.namXuatBan ?? null,
//             duongDan: item.duongDan || '',
//             ghiChu: item.ghiChu || ''
//         }
//     }
//
//     async function toggleCopyDieuKienMonHocGocVaoSyllabus(item, parentValues = {}) {
//         const syllabusMonId = parentValues.syllabusMonId || selected.syllabusMonHoc?.id
//
//         if (!syllabusMonId) {
//             baoTinBang('dieuKienMonHocGoc', 'Cần chọn Syllabus môn học áp dụng trước khi gán điều kiện môn học gốc.', 'error')
//             return
//         }
//
//         const serviceDieuKien = daoTaoXemChuongTrinhService.dieuKienMonHoc
//
//         if (!serviceDieuKien?.create) {
//             baoTinBang('dieuKienMonHocGoc', 'Chưa khai báo API tạo Điều kiện môn học đã lưu vào syllabus.', 'error')
//             return
//         }
//
//         const existing = timDieuKienMonHocDaCopyTuGoc(item, syllabusMonId)
//
//         try {
//             if (existing?.id) {
//                 if (!serviceDieuKien.delete) {
//                     baoTinBang('dieuKienMonHocGoc', 'Chưa khai báo API xóa Điều kiện môn học đã lưu vào syllabus.', 'error')
//                     return
//                 }
//
//                 await serviceDieuKien.delete(existing.id)
//                 xoaDongTrongRawData('dieuKienMonHoc', existing.id)
//
//                 if (selected.dieuKienMonHoc?.id === existing.id) {
//                     selectEntity('dieuKienMonHoc', null)
//                 }
//
//                 baoTinBang('dieuKienMonHocGoc', 'Đã bỏ gán điều kiện môn học khỏi syllabus.')
//                 return
//             }
//
//             const payload = taoPayloadDieuKienMonHocTuGoc(item, syllabusMonId)
//             const saved = await serviceDieuKien.create(payload)
//             const row = layDuLieuLuu(saved) || payload
//
//             capNhatDongTrongRawData('dieuKienMonHoc', row)
//             selectEntity('dieuKienMonHocGoc', item)
//
//             baoTinBang('dieuKienMonHocGoc', 'Đã copy điều kiện môn học gốc vào Điều kiện môn học đã lưu vào syllabus.')
//         } catch (error) {
//             const message = layThongBaoLoi(error, 'Không gán được điều kiện môn học gốc vào syllabus.')
//             baoTinBang('dieuKienMonHocGoc', message, 'error')
//         }
//     }
//
//     async function toggleCopyTaiLieuGocVaoSyllabus(item, parentValues = {}) {
//         const syllabusMonId = parentValues.syllabusMonId || selected.syllabusMonHoc?.id
//
//         if (!syllabusMonId) {
//             baoTinBang('taiLieuGoc', 'Cần chọn Syllabus môn học áp dụng trước khi gán tài liệu gốc.', 'error')
//             return
//         }
//
//         const serviceTaiLieu = daoTaoXemChuongTrinhService.syllabusTaiLieu
//
//         if (!serviceTaiLieu?.create) {
//             baoTinBang('taiLieuGoc', 'Chưa khai báo API tạo Tài liệu đã lưu vào syllabus.', 'error')
//             return
//         }
//
//         const existing = timSyllabusTaiLieuDaCopyTuGoc(item, syllabusMonId)
//
//         try {
//             if (existing?.id) {
//                 if (!serviceTaiLieu.delete) {
//                     baoTinBang('taiLieuGoc', 'Chưa khai báo API xóa Tài liệu đã lưu vào syllabus.', 'error')
//                     return
//                 }
//
//                 await serviceTaiLieu.delete(existing.id)
//                 xoaDongTrongRawData('syllabusTaiLieu', existing.id)
//
//                 if (selected.syllabusTaiLieu?.id === existing.id) {
//                     selectEntity('syllabusTaiLieu', null)
//                 }
//
//                 baoTinBang('taiLieuGoc', 'Đã bỏ gán tài liệu khỏi syllabus.')
//                 return
//             }
//
//             const payload = taoPayloadSyllabusTaiLieuTuGoc(item, syllabusMonId)
//             const saved = await serviceTaiLieu.create(payload)
//             const row = layDuLieuLuu(saved) || payload
//
//             capNhatDongTrongRawData('syllabusTaiLieu', row)
//             selectEntity('taiLieuGoc', item)
//
//             baoTinBang('taiLieuGoc', 'Đã copy tài liệu gốc vào Tài liệu đã lưu vào syllabus.')
//         } catch (error) {
//             const message = layThongBaoLoi(error, 'Không gán được tài liệu gốc vào syllabus.')
//             baoTinBang('taiLieuGoc', message, 'error')
//         }
//     }
//
//     function timDongNoiSyllabusTheoConfig(config, item) {
//         if (!config?.syllabusId) return null
//         const list = rawData[config.joinKey] || []
//         return list.find((row) => {
//             return String(row[config.syllabusIdKey] || '') === String(config.syllabusId || '')
//                 && String(row[config.gocIdKey] || '') === String(item?.id || '')
//         })
//     }
//
//     function daLuuTrucTiep(item, parentValues = {}) {
//         if (!item) return false
//         const parentHopLe = Object.entries(parentValues || {}).filter(([, value]) => coGiaTri(value))
//         if (!parentHopLe.length) return false
//         return parentHopLe.every(([parentKey, value]) => String(item?.[parentKey] ?? '') === String(value))
//     }
//
//     const bangXoaDongKhiBoLuu = new Set([
//         'monTienQuyet',
//         'quyDoiDiemMau',
//         'dieuKienMonHocGoc',
//         'taiLieuGoc',
//         'dieuKienMonHoc',
//         'syllabusChuongBai',
//         'syllabusTaiLieu'
//     ])
//
//     async function toggleLuuBangPhu(key, item, parentValues = {}, service = null) {
//         if (!item?.id) {
//             baoTinBang(key, 'Không tìm thấy dòng dữ liệu để lưu hoặc bỏ lưu.', 'error')
//             return
//         }
//
//         if (key === 'monHoc') {
//             await toggleGanMonHocGocVaoChuongTrinhMon(item, parentValues)
//             return
//         }
//         if (key === 'dieuKienMonHocGoc') {
//             await toggleCopyDieuKienMonHocGocVaoSyllabus(item, parentValues)
//             return
//         }
//
//         if (key === 'taiLieuGoc') {
//             await toggleCopyTaiLieuGocVaoSyllabus(item, parentValues)
//             return
//         }
//         if (key === 'cauHinhDanhGiaMau') {
//             await toggleCopyCotDiemMauVaoQuyDoiDiem(item, parentValues)
//             return
//         }
//         const configApDungVersion = bangGocTaoApDungVersion[key]
//
//         if (configApDungVersion) {
//             const chuongTrinhVersionId = selected.chuongTrinhVersion?.id || parentValues.chuongTrinhVersionId
//             if (!chuongTrinhVersionId) {
//                 baoTinBang(key, `Cần chọn Version trước khi lưu ${configApDungVersion.tenBang}.`, 'error')
//                 return
//             }
//
//             const existing = timDongApDungVersion(key, item, chuongTrinhVersionId)
//             const serviceApDung = daoTaoXemChuongTrinhService[configApDungVersion.serviceKey]
//
//             if (!serviceApDung) {
//                 baoTinBang(key, `Chưa khai báo API bảng áp dụng cho ${configApDungVersion.tenBang}.`, 'error')
//                 return
//             }
//
//             try {
//                 if (existing?.id) {
//                     if (!serviceApDung.delete) {
//                         baoTinBang(key, `Chưa khai báo API xóa để bỏ lưu ${configApDungVersion.tenBang}.`, 'error')
//                         return
//                     }
//
//                     await serviceApDung.delete(existing.id)
//                     xoaDongTrongRawData(configApDungVersion.joinKey, existing.id)
//                     if (selected[key]?.id === item.id) selectEntity(key, null)
//                     baoTinBang(key, `Đã bỏ lưu ${configApDungVersion.tenBang} khỏi Version hiện tại. Dòng gốc đã chuyển về trạng thái có thể Lưu.`)
//                     return
//                 }
//
//                 if (!serviceApDung.create) {
//                     baoTinBang(key, `Chưa khai báo API tạo bảng áp dụng cho ${configApDungVersion.tenBang}.`, 'error')
//                     return
//                 }
//
//                 const payload = configApDungVersion.buildPayload(item, chuongTrinhVersionId)
//                 const saved = await serviceApDung.create(payload)
//                 const row = layDuLieuLuu(saved) || payload
//                 capNhatDongTrongRawData(configApDungVersion.joinKey, row)
//                 selectEntity(key, item)
//                 baoTinBang(key, `Đã lưu ${configApDungVersion.tenBang} vào Version hiện tại.`)
//             } catch (error) {
//                 const message = layThongBaoLoi(error, `Không lưu/bỏ lưu được ${configApDungVersion.tenBang}.`)
//                 baoTinBang(key, message, 'error')
//             }
//
//             return
//         }
//
//         const config = bangGocGanVersion[key]
//
//         if (config) {
//             const chuongTrinhVersionId = selected.chuongTrinhVersion?.id || parentValues.chuongTrinhVersionId
//             if (!chuongTrinhVersionId) {
//                 baoTinBang(key, `Cần chọn Version trước khi lưu ${config.tenBang}.`, 'error')
//                 return
//             }
//
//             const existing = timDongNoiBangGoc(key, item, chuongTrinhVersionId)
//             const serviceNoi = daoTaoXemChuongTrinhService[config.serviceKey]
//
//             if (!serviceNoi) {
//                 baoTinBang(key, `Chưa khai báo API bảng nối cho ${config.tenBang}.`, 'error')
//                 return
//             }
//
//             try {
//                 if (existing?.id) {
//                     await serviceNoi.delete(existing.id)
//                     xoaDongTrongRawData(config.joinKey, existing.id)
//                     if (selected[key]?.id === item.id) selectEntity(key, null)
//                     baoTinBang(key, `Đã bỏ lưu ${config.tenBang} khỏi Version hiện tại.`)
//                     return
//                 }
//
//                 const payload = {
//                     chuongTrinhVersionId,
//                     [config.gocIdKey]: item.id,
//                     thuTu: layThuTuGanTiepTheo(config.joinKey, chuongTrinhVersionId),
//                     ghiChu: item.ghiChu || ''
//                 }
//
//                 const saved = await serviceNoi.create(payload)
//                 const row = layDuLieuLuu(saved) || payload
//                 capNhatDongTrongRawData(config.joinKey, row)
//                 selectEntity(key, item)
//                 baoTinBang(key, `Đã lưu ${config.tenBang} vào Version hiện tại.`)
//             } catch (error) {
//                 const message = layThongBaoLoi(error, `Không lưu/bỏ lưu được ${config.tenBang}.`)
//                 baoTinBang(key, message, 'error')
//             }
//
//             return
//         }
//
//         const configMau = bangMauGanMon[key]
//         if (configMau) {
//             const chuongTrinhMonId = selected.chuongTrinhMon?.id || parentValues.chuongTrinhMonId
//             if (!chuongTrinhMonId) {
//                 baoTinBang(key, `Cần chọn Môn trong chương trình trước khi lưu ${configMau.tenBang}.`, 'error')
//                 return
//             }
//
//             const existing = timDongNoiMon(configMau, item, chuongTrinhMonId)
//             const serviceNoi = daoTaoXemChuongTrinhService[configMau.serviceKey]
//
//             if (!serviceNoi) {
//                 baoTinBang(key, `Chưa khai báo API bảng nối cho ${configMau.tenBang}.`, 'error')
//                 return
//             }
//
//             try {
//                 if (existing?.id) {
//                     await serviceNoi.delete(existing.id)
//                     xoaDongTrongRawData(configMau.joinKey, existing.id)
//
//                     await xoaQuyDoiDiemDaDayTuMau(item, chuongTrinhMonId)
//
//                     if (selected[key]?.id === item.id) selectEntity(key, null)
//                     baoTinBang(key, `Đã bỏ gán ${configMau.tenBang} khỏi môn trong chương trình và xóa quy đổi điểm đã copy.`)
//                     return
//                 }
//
//                 const payload = {
//                     [configMau.monIdKey]: chuongTrinhMonId,
//                     [configMau.gocIdKey]: item.id,
//                     ghiChu: item.ghiChu || ''
//                 }
//
//                 const saved = await serviceNoi.create(payload)
//                 const row = layDuLieuLuu(saved) || payload
//                 capNhatDongTrongRawData(configMau.joinKey, row)
//
//                 await dayQuyDoiDiemMauXuongQuyDoiDiem(item, chuongTrinhMonId)
//
//                 selectEntity(key, item)
//                 baoTinBang(key, `Đã gán ${configMau.tenBang} vào môn trong chương trình và copy xuống bảng Quy đổi điểm.`)
//             } catch (error) {
//                 const message = layThongBaoLoi(error, `Không lưu/bỏ lưu được ${configMau.tenBang}.`)
//                 baoTinBang(key, message, 'error')
//             }
//
//             return
//         }
//
//
//         if (key === 'syllabusMonHocGoc') {
//             const chuongTrinhMonId = selected.chuongTrinhMon?.id || parentValues.chuongTrinhMonId
//             if (!chuongTrinhMonId) {
//                 baoTinBang(key, 'Cần chọn Môn trong chương trình trước khi gắn Syllabus môn học gốc.', 'error')
//                 return
//             }
//
//             const serviceApDung = daoTaoXemChuongTrinhService.syllabusMonHoc
//             if (!serviceApDung) {
//                 baoTinBang(key, 'Chưa khai báo API syllabus_mon_hoc.', 'error')
//                 return
//             }
//
//             const existing = (rawData.syllabusMonHoc || []).find((row) => {
//                 return String(row.chuongTrinhMonId || '') === String(chuongTrinhMonId || '')
//                     && String(row.syllabusMonHocGocId || '') === String(item.id || '')
//             })
//
//             try {
//                 if (existing?.id) {
//                     if (!serviceApDung.delete) {
//                         baoTinBang(key, 'Chưa khai báo API xóa để bỏ gắn Syllabus môn học gốc.', 'error')
//                         return
//                     }
//
//                     await serviceApDung.delete(existing.id)
//                     xoaDongTrongRawData('syllabusMonHoc', existing.id)
//                     if (selected.syllabusMonHoc?.id === existing.id) selectEntity('syllabusMonHoc', null)
//                     if (selected[key]?.id === item.id) selectEntity(key, null)
//                     baoTinBang(key, 'Đã bỏ gắn Syllabus môn học gốc khỏi Môn trong chương trình hiện tại.')
//                     return
//                 }
//
//                 if (!serviceApDung.create) {
//                     baoTinBang(key, 'Chưa khai báo API tạo syllabus_mon_hoc.', 'error')
//                     return
//                 }
//
//                 const payload = {
//                     chuongTrinhMonId,
//                     syllabusMonHocGocId: item.id,
//
//                     monHocId: item.monHocId || selected.monHoc?.id || null,
//                     ma: item.ma || '',
//                     ten: item.ten || '',
//
//                     viTri: item.viTri || '',
//                     tinhChat: item.tinhChat || '',
//                     mucTieu: item.mucTieu || '',
//                     phuongPhapDanhGia: item.phuongPhapDanhGia || '',
//                     dieuKienHoanThanh: item.dieuKienHoanThanh || '',
//                     huongDan: item.huongDan || '',
//                     diemDatToiThieu: item.diemDatToiThieu ?? null,
//                     donViDiem: item.donViDiem || 'thang_10',
//                     tyLeChuyenCanToiThieu: item.tyLeChuyenCanToiThieu ?? null,
//                     batBuocDuThi: item.batBuocDuThi ?? false,
//                     congThucQuyDoi: item.congThucQuyDoi || '',
//                     ghiChu: item.ghiChu || ''
//                 }
//
//                 const saved = await serviceApDung.create(payload)
//                 const row = layDuLieuLuu(saved) || payload
//                 const rowDaLuu = capNhatDongTrongRawData('syllabusMonHoc', row)
//                 selectEntity(key, item)
//                 selectEntity('syllabusMonHoc', rowDaLuu)
//                 baoTinBang(key, 'Đã gắn Syllabus môn học gốc vào Môn trong chương trình hiện tại qua syllabus_mon_hoc.')
//             } catch (error) {
//                 const message = layThongBaoLoi(error, 'Không gắn/bỏ gắn được Syllabus môn học gốc.')
//                 baoTinBang(key, message, 'error')
//             }
//
//             return
//         }
//
//         const configSyllabus = layConfigGanSyllabusTheoNguCanh(key, parentValues)
//         if (configSyllabus) {
//             const existing = timDongNoiSyllabusTheoConfig(configSyllabus, item)
//             const serviceNoi = daoTaoXemChuongTrinhService[configSyllabus.serviceKey]
//
//             if (!serviceNoi) {
//                 baoTinBang(key, `Chưa khai báo API bảng nối cho ${configSyllabus.tenBang}.`, 'error')
//                 return
//             }
//
//             try {
//                 if (existing?.id) {
//                     await serviceNoi.delete(existing.id)
//                     xoaDongTrongRawData(configSyllabus.joinKey, existing.id)
//                     if (selected[key]?.id === item.id) selectEntity(key, null)
//                     baoTinBang(key, `Đã bỏ lưu ${configSyllabus.tenBang} khỏi Syllabus hiện tại. Dòng gốc đã chuyển về trạng thái có thể Lưu.`)
//                     return
//                 }
//
//                 const payload = {
//                     [configSyllabus.syllabusIdKey]: configSyllabus.syllabusId,
//                     [configSyllabus.gocIdKey]: item.id,
//                     thuTu: layThuTuGanTheoChaTiepTheo(configSyllabus.joinKey, configSyllabus.syllabusIdKey, configSyllabus.syllabusId),
//                     ghiChu: item.ghiChu || ''
//                 }
//
//                 const saved = await serviceNoi.create(payload)
//                 const row = layDuLieuLuu(saved) || payload
//                 capNhatDongTrongRawData(configSyllabus.joinKey, row)
//                 selectEntity(key, item)
//                 baoTinBang(key, `Đã lưu ${configSyllabus.tenBang} vào Syllabus hiện tại.`)
//             } catch (error) {
//                 const message = layThongBaoLoi(error, `Không lưu/bỏ lưu được ${configSyllabus.tenBang}.`)
//                 baoTinBang(key, message, 'error')
//             }
//
//             return
//         }
//
//         const parentHopLe = Object.entries(parentValues || {}).filter(([, value]) => coGiaTri(value))
//         if (!parentHopLe.length) {
//             const dangChon = String(selected[key]?.id || '') === String(item.id || '')
//             if (dangChon) {
//                 selectEntity(key, null)
//                 baoTinBang(key, 'Đã chuyển dòng này về trạng thái không lưu trong luồng hiện tại.')
//             } else {
//                 selectEntity(key, item)
//                 baoTinBang(key, 'Đã lưu/chọn dòng này vào luồng hiện tại.')
//             }
//             return
//         }
//
//         if (!service?.update) {
//             baoTinBang(key, 'Chưa khai báo API cập nhật cho bảng này.', 'error')
//             return
//         }
//
//         const dangLuu = daLuuTrucTiep(item, parentValues)
//
//         if (dangLuu && key === 'chuongTrinhMon') {
//             baoTinBang(key, 'Chương trình môn là bảng xương sống nên không bỏ lưu bằng nút này. Nếu cần xóa khỏi chương trình, dùng nút Xóa.', 'error')
//             return
//         }
//
//         if (dangLuu && bangXoaDongKhiBoLuu.has(key)) {
//             if (!service?.delete) {
//                 baoTinBang(key, 'Chưa khai báo API xóa để bỏ lưu dữ liệu này.', 'error')
//                 return
//             }
//
//             try {
//                 await service.delete(item.id)
//                 xoaDongTrongRawData(key, item.id)
//                 if (selected[key]?.id === item.id) selectEntity(key, null)
//                 baoTinBang(key, 'Đã hủy lưu dữ liệu khỏi luồng hiện tại.')
//             } catch (error) {
//                 const message = layThongBaoLoi(error, 'Không hủy lưu được dữ liệu. Kiểm tra ràng buộc BE.')
//                 baoTinBang(key, message, 'error')
//             }
//             return
//         }
//
//         const payload = {
//             ...item,
//             ...Object.fromEntries(parentHopLe.map(([parentKey, value]) => [parentKey, dangLuu ? null : value]))
//         }
//
//         try {
//             const saved = await service.update(item.id, payload)
//             const row = layDuLieuLuu(saved) || payload
//             const rowDaCapNhat = capNhatDongTrongRawData(key, row)
//             if (dangLuu) {
//                 if (selected[key]?.id === item.id) selectEntity(key, null)
//                 baoTinBang(key, 'Đã bỏ lưu dữ liệu khỏi luồng hiện tại.')
//             } else {
//                 selectEntity(key, rowDaCapNhat)
//                 baoTinBang(key, 'Đã lưu dữ liệu vào luồng hiện tại.')
//             }
//         } catch (error) {
//             const message = layThongBaoLoi(error, 'Không lưu/bỏ lưu được dữ liệu. Kiểm tra ràng buộc BE.')
//             baoTinBang(key, message, 'error')
//         }
//     }
//
//     async function chonBangPhuVaoLuong(key, item, parentValues = {}, service = null) {
//         if (bangGocGanVersion[key]) {
//             const handled = await ganBangGocVaoVersion(key, item)
//             if (handled) return
//         }
//
//         if (!item) {
//             selectEntity(key, null)
//             return
//         }
//
//         const configMau = bangMauGanMon[key]
//         if (configMau) {
//             const chuongTrinhMonId = selected.chuongTrinhMon?.id || parentValues.chuongTrinhMonId
//             if (!chuongTrinhMonId) {
//                 baoTinBang(key, `Cần chọn Môn trong chương trình trước khi lưu ${configMau.tenBang}.`, 'error')
//                 return
//             }
//
//             const existing = timDongNoiMon(configMau, item, chuongTrinhMonId)
//             const serviceNoi = daoTaoXemChuongTrinhService[configMau.serviceKey]
//
//             if (!serviceNoi) {
//                 baoTinBang(key, `Chưa khai báo API bảng nối cho ${configMau.tenBang}.`, 'error')
//                 return
//             }
//
//             try {
//                 if (existing?.id) {
//                     await serviceNoi.delete(existing.id)
//                     xoaDongTrongRawData(configMau.joinKey, existing.id)
//                     if (selected[key]?.id === item.id) selectEntity(key, null)
//                     baoTinBang(key, `Đã bỏ gắn ${configMau.tenBang} khỏi bảng 5.5 Quy đổi điểm.`)
//                     return
//                 }
//
//                 const payload = {
//                     [configMau.monIdKey]: chuongTrinhMonId,
//                     [configMau.gocIdKey]: item.id,
//                     ghiChu: item.ghiChu || ''
//                 }
//
//                 const saved = await serviceNoi.create(payload)
//                 const row = layDuLieuLuu(saved) || payload
//                 capNhatDongTrongRawData(configMau.joinKey, row)
//                 selectEntity(key, item)
//                 baoTinBang(key, `Đã gắn ${configMau.tenBang} vào bảng 5.5 Quy đổi điểm qua bảng nối.`)
//             } catch (error) {
//                 const message = layThongBaoLoi(error, `Không lưu/bỏ lưu được ${configMau.tenBang}.`)
//                 baoTinBang(key, message, 'error')
//             }
//
//             return
//         }
//
//
//         if (key === 'syllabusMonHocGoc') {
//             const chuongTrinhMonId = selected.chuongTrinhMon?.id || parentValues.chuongTrinhMonId
//             if (!chuongTrinhMonId) {
//                 baoTinBang(key, 'Cần chọn Môn trong chương trình trước khi gắn Syllabus môn học gốc.', 'error')
//                 return
//             }
//
//             const serviceApDung = daoTaoXemChuongTrinhService.syllabusMonHoc
//             if (!serviceApDung) {
//                 baoTinBang(key, 'Chưa khai báo API syllabus_mon_hoc.', 'error')
//                 return
//             }
//
//             const existing = (rawData.syllabusMonHoc || []).find((row) => {
//                 return String(row.chuongTrinhMonId || '') === String(chuongTrinhMonId || '')
//                     && String(row.syllabusMonHocGocId || '') === String(item.id || '')
//             })
//
//             try {
//                 if (existing?.id) {
//                     if (!serviceApDung.delete) {
//                         baoTinBang(key, 'Chưa khai báo API xóa để bỏ gắn Syllabus môn học gốc.', 'error')
//                         return
//                     }
//
//                     await serviceApDung.delete(existing.id)
//                     xoaDongTrongRawData('syllabusMonHoc', existing.id)
//                     if (selected.syllabusMonHoc?.id === existing.id) selectEntity('syllabusMonHoc', null)
//                     if (selected[key]?.id === item.id) selectEntity(key, null)
//                     baoTinBang(key, 'Đã bỏ gắn Syllabus môn học gốc khỏi Môn trong chương trình hiện tại.')
//                     return
//                 }
//
//                 if (!serviceApDung.create) {
//                     baoTinBang(key, 'Chưa khai báo API tạo syllabus_mon_hoc.', 'error')
//                     return
//                 }
//
//                 const payload = {
//                     chuongTrinhMonId,
//                     syllabusMonHocGocId: item.id,
//                     viTri: item.viTri || '',
//                     tinhChat: item.tinhChat || '',
//                     mucTieu: item.mucTieu || '',
//                     phuongPhapDanhGia: item.phuongPhapDanhGia || '',
//                     dieuKienHoanThanh: item.dieuKienHoanThanh || '',
//                     huongDan: item.huongDan || '',
//                     diemDatToiThieu: item.diemDatToiThieu ?? null,
//                     donViDiem: item.donViDiem || 'thang_10',
//                     tyLeChuyenCanToiThieu: item.tyLeChuyenCanToiThieu ?? null,
//                     batBuocDuThi: item.batBuocDuThi ?? true,
//                     congThucQuyDoi: item.congThucQuyDoi || ''
//                 }
//
//                 const saved = await serviceApDung.create(payload)
//                 const row = layDuLieuLuu(saved) || payload
//                 const rowDaLuu = capNhatDongTrongRawData('syllabusMonHoc', row)
//                 selectEntity(key, item)
//                 selectEntity('syllabusMonHoc', rowDaLuu)
//                 baoTinBang(key, 'Đã gắn Syllabus môn học gốc vào Môn trong chương trình hiện tại qua syllabus_mon_hoc.')
//             } catch (error) {
//                 const message = layThongBaoLoi(error, 'Không gắn/bỏ gắn được Syllabus môn học gốc.')
//                 baoTinBang(key, message, 'error')
//             }
//
//             return
//         }
//
//         const configSyllabus = layConfigGanSyllabusTheoNguCanh(key, parentValues)
//         if (configSyllabus) {
//             await toggleLuuBangPhu(key, item, parentValues, service)
//             return
//         }
//
//         const parentHopLe = Object.entries(parentValues || {}).filter(([, value]) => coGiaTri(value))
//         const canGanCha = parentHopLe.some(([parentKey, value]) => String(item?.[parentKey] ?? '') !== String(value))
//
//         if (!canGanCha || !item.id || !service?.update) {
//             selectEntity(key, item)
//             return
//         }
//
//         try {
//             const saved = await service.update(item.id, {
//                 ...item,
//                 ...parentValues
//             })
//             const row = layDuLieuLuu(saved) || {...item, ...parentValues}
//             const rowDaCapNhat = capNhatDongTrongRawData(key, row)
//             selectEntity(key, rowDaCapNhat)
//             baoTin('Đã chọn dữ liệu có sẵn và gắn vào luồng hiện tại.')
//         } catch (error) {
//             const message = layThongBaoLoi(error, 'Không gắn được dữ liệu có sẵn vào luồng hiện tại.')
//             baoTinBang(key, message, 'error')
//         }
//     }
//
//     function sauKhiLuu(key, saved) {
//         const row = layDuLieuLuu(saved)
//
//         if (!row || !row.id) {
//             baoTin('Đã gọi API nhưng không nhận được dữ liệu trả về hợp lệ.', 'error')
//             return
//         }
//
//         const list = rawData[key]
//
//         if (!Array.isArray(list)) {
//             baoTin(`Không tìm thấy vùng dữ liệu cho bảng ${key}.`, 'error')
//             return
//         }
//
//         const oldSelectedId = selected[key]?.id || null
//         const index = list.findIndex((item) => item.id === row.id)
//
//         if (index >= 0) {
//             list.splice(index, 1, row)
//         } else {
//             list.push(row)
//         }
//
//         // Gán lại mảng để chắc chắn computed rows của bảng cập nhật ngay sau khi POST/PUT thành công.
//         rawData[key] = [...list]
//
//         if (key === 'quyDoiDiemMau') {
//             const cacheIndex = khoQuyDoiDiemMauGoc.value.findIndex((item) => String(item.id || '') === String(row.id || ''))
//             if (cacheIndex >= 0) khoQuyDoiDiemMauGoc.value.splice(cacheIndex, 1, {...row})
//             else khoQuyDoiDiemMauGoc.value.push({...row})
//         }
//
//         selected[key] = duLieu.value[key]?.find((item) => item.id === row.id) || row
//
//         if (String(oldSelectedId || '') !== String(row.id || '')) {
//             xoaLuaChonCon(key)
//         }
//
//         baoTin('Đã lưu và thêm vào dữ liệu vừa tạo trong luồng.')
//     }
//
//     function sauKhiXoa(key, item) {
//         const list = rawData[key]
//         const index = list.findIndex((row) => row.id === item.id)
//         if (index >= 0) list.splice(index, 1)
//
//         if (selected[key]?.id === item.id) {
//             selected[key] = null
//         }
//     }
//
//     function resetLuonNhapKhongThongBao() {
//         Object.assign(selected, taoSelectedRong())
//         Object.assign(viewed, taoSelectedRong())
//         Object.entries(taoDuLieuRong()).forEach(([key, value]) => {
//             rawData[key] = value
//         })
//     }
//
//     async function lamMoiLuon() {
//         const nganhDangNhap = selected.nganh
//
//         if (!nganhDangNhap?.id) {
//             resetLuonNhapKhongThongBao()
//             baoTin('Đã làm mới luồng nhập.')
//             return
//         }
//
//         const dongY = confirm(`Làm mới luồng nhập sẽ gọi API xóa ngành vừa tạo: ${nganhDangNhap.tenNganh || nganhDangNhap.maNganh || nganhDangNhap.id}. Tiếp tục?`)
//         if (!dongY) return
//
//         try {
//             await daoTaoXemChuongTrinhService.nganh.delete(nganhDangNhap.id)
//             resetLuonNhapKhongThongBao()
//             baoTin('Đã xóa ngành vừa tạo và làm mới luồng nhập.')
//         } catch (error) {
//             baoTin(layThongBaoLoi(error, 'Không xóa được ngành vừa tạo. Kiểm tra ràng buộc dữ liệu ở BE.'), 'error')
//         }
//     }
//
//     function luuChuongTrinhTong() {
//         if (!selected.nganh || !selected.chuongTrinh || !selected.chuongTrinhVersion) {
//             baoTin('Cần lưu tối thiểu Ngành, Chương trình và Version trước khi lưu chương trình.', 'error')
//             return
//         }
//
//         baoTin('Đã xác nhận luồng xây dựng chương trình đào tạo.')
//     }
//
//     return {
//         thongBao,
//         loaiThongBao,
//         tableMessages,
//         selected,
//         viewed,
//         duLieu,
//         quyDoiDiemMauGocRows,
//         lookups,
//         services: daoTaoXemChuongTrinhService,
//         baoTin,
//         selectEntity,
//         viewEntity,
//         chonBangPhuVaoLuong,
//         toggleLuuBangPhu,
//         taiDuLieuCoSanTatCaBang,
//         sauKhiLuu,
//         sauKhiXoa,
//         lamMoiLuon,
//         luuChuongTrinhTong
//     }
// }
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
    'chuongTrinh',
    'chuongTrinhVersion',
    'khungKy',
    'khungKyGoc',
    'nhomKienThuc',
    'nhomKienThucGoc',
    'nhomTuChon',
    'nhomTuChonGoc',
    'monHoc',
    'chuongTrinhMon',
    'monTuChon',
    'syllabusMonHoc',
    'syllabusMonHocGocTaiLieu',
    'syllabusMonHocGocDieuKien',
    'syllabusMonHocGocChuongBai',
    'syllabusMonHocGoc',
    'mucTieuChuongTrinhGoc',
    'mucTieuChuongTrinh',
    'nangLucDauRaGoc',
    'nangLucDauRa',
    'viTriViecLamGoc',
    'viTriViecLam',
    'dieuKienTotNghiepGoc',
    'dieuKienTotNghiep',
    'chuongTrinhVersionMucTieu',
    'chuongTrinhVersionNangLuc',
    'chuongTrinhVersionViTriViecLam',
    'chuongTrinhVersionDieuKienTotNghiep',
    'syllabusChuongTrinh',
    'syllabusChuongTrinhGoc',
    'monTienQuyet',
    'quyDoiDiem',
    'quyDoiDiemMau',
    'chuongTrinhMonQuyDoiDiemMau',
    'cauHinhDanhGiaMau',
    'dieuKienMonHoc',
    'dieuKienMonHocGoc',
    'syllabusMonHocDieuKien',
    'taiLieuGoc',
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
    const khungKyGocMap = taoBanDo(duLieu.khungKyGoc)
    const nhomKienThucMap = taoBanDo(duLieu.nhomKienThuc)
    const nhomKienThucGocMap = taoBanDo(duLieu.nhomKienThucGoc)
    const nhomTuChonMap = taoBanDo(duLieu.nhomTuChon)
    const nhomTuChonGocMap = taoBanDo(duLieu.nhomTuChonGoc)
    const chuongTrinhMonMap = taoBanDo(duLieu.chuongTrinhMon)
    const quyDoiDiemMauMap = taoBanDo(duLieu.quyDoiDiemMau)
    const syllabusMonMap = taoBanDo(duLieu.syllabusMonHoc)
    const syllabusChuongTrinhGocMap = taoBanDo(duLieu.syllabusChuongTrinhGoc)
    const syllabusMonHocGocMap = taoBanDo(duLieu.syllabusMonHocGoc)
    const dieuKienMonHocGocMap = taoBanDo(duLieu.dieuKienMonHocGoc)
    const taiLieuGocMap = taoBanDo(duLieu.taiLieuGoc)
    const mucTieuGocMap = taoBanDo(duLieu.mucTieuChuongTrinhGoc)
    const nangLucGocMap = taoBanDo(duLieu.nangLucDauRaGoc)
    const viTriGocMap = taoBanDo(duLieu.viTriViecLamGoc)
    const dieuKienTotNghiepGocMap = taoBanDo(duLieu.dieuKienTotNghiepGoc)

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
                tenKhungKyGoc: layTen(khungKyGocMap, item.khungKyGocId, ['maKy', 'tenKy'])
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
            tenKhungKyGoc: layTen(khungKyGocMap, item.khungKyGocId, ['maKy', 'tenKy'])
        })),
        mucTieuChuongTrinh: duLieu.mucTieuChuongTrinh.map((item) => ({
            ...item,
            tenVersion: layTen(versionMap, item.chuongTrinhVersionId, ['tenVersion'])
        })),
        nangLucDauRa: duLieu.nangLucDauRa.map((item) => ({
            ...item,
            tenVersion: layTen(versionMap, item.chuongTrinhVersionId, ['tenVersion'])
        })),
        viTriViecLam: duLieu.viTriViecLam.map((item) => ({
            ...item,
            tenVersion: layTen(versionMap, item.chuongTrinhVersionId, ['tenVersion'])
        })),
        dieuKienTotNghiep: duLieu.dieuKienTotNghiep.map((item) => ({
            ...item,
            tenVersion: layTen(versionMap, item.chuongTrinhVersionId, ['tenVersion'])
        })),
        chuongTrinhVersionMucTieu: duLieu.chuongTrinhVersionMucTieu.map((item) => ({
            ...item,
            tenVersion: layTen(versionMap, item.chuongTrinhVersionId, ['tenVersion']),
            tenMucTieuGoc: layTen(mucTieuGocMap, item.mucTieuGocId, ['ma', 'noiDung'])
        })),
        chuongTrinhVersionNangLuc: duLieu.chuongTrinhVersionNangLuc.map((item) => ({
            ...item,
            tenVersion: layTen(versionMap, item.chuongTrinhVersionId, ['tenVersion']),
            tenNangLucGoc: layTen(nangLucGocMap, item.nangLucGocId, ['ma', 'noiDung'])
        })),
        chuongTrinhVersionViTriViecLam: duLieu.chuongTrinhVersionViTriViecLam.map((item) => ({
            ...item,
            tenVersion: layTen(versionMap, item.chuongTrinhVersionId, ['tenVersion']),
            tenViTriGoc: layTen(viTriGocMap, item.viTriGocId, ['ma', 'ten'])
        })),
        chuongTrinhVersionDieuKienTotNghiep: duLieu.chuongTrinhVersionDieuKienTotNghiep.map((item) => ({
            ...item,
            tenVersion: layTen(versionMap, item.chuongTrinhVersionId, ['tenVersion']),
            tenDieuKienGoc: layTen(dieuKienTotNghiepGocMap, item.dieuKienGocId, ['ma', 'noiDung'])
        })),
        syllabusChuongTrinhGoc: duLieu.syllabusChuongTrinhGoc.map((item) => ({...item})),
        syllabusChuongTrinh: duLieu.syllabusChuongTrinh.map((item) => ({
            ...item,
            tenVersion: layTen(versionMap, item.chuongTrinhVersionId, ['tenVersion']),
            tenSyllabusChuongTrinhGoc: layTen(syllabusChuongTrinhGocMap, item.syllabusChuongTrinhGocId, ['ma', 'ten'])
        })),
        nhomKienThucGoc: duLieu.nhomKienThucGoc.map((item) => ({...item})),
        nhomKienThuc: duLieu.nhomKienThuc.map((item) => ({
            ...item,
            tenVersion: layTen(versionMap, item.chuongTrinhVersionId, ['tenVersion']),
            tenNhomKienThucGoc: layTen(nhomKienThucGocMap, item.nhomKienThucGocId, ['ma', 'ten'])
        })),
        nhomTuChonGoc: duLieu.nhomTuChonGoc.map((item) => ({...item})),
        nhomTuChon: duLieu.nhomTuChon.map((item) => ({
            ...item,
            tenVersion: layTen(versionMap, item.chuongTrinhVersionId, ['tenVersion']),
            tenNhomTuChonGoc: layTen(nhomTuChonGocMap, item.nhomTuChonGocId, ['ma', 'ten'])
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
            tenCotDiemMau: layGiaTriTheoKhoa(
                item,
                ['tenCotDiemMau', 'ten_cot_diem_mau', 'tenCotDiem', 'ten_cot_diem', 'ghiChu', 'ghi_chu'],
                ''
            )
        })),
        quyDoiDiemMau: duLieu.quyDoiDiemMau.map((item) => ({...item})),
        cauHinhDanhGiaMau: duLieu.cauHinhDanhGiaMau.map((item) => ({
            ...item,
            tenChuongTrinhMon: layTen(chuongTrinhMonMap, item.chuongTrinhMonId, ['maMonTrongCt', 'tenMon'])
        })),
        chuongTrinhMonQuyDoiDiemMau: duLieu.chuongTrinhMonQuyDoiDiemMau.map((item) => {
            const quyDoiDiemMauId = layGiaTriTheoKhoa(item, ['quyDoiDiemMauId', 'quy_doi_diem_mau_id', 'mauId'], null)
            const chuongTrinhMonId = layGiaTriTheoKhoa(item, ['chuongTrinhMonId', 'chuong_trinh_mon_id'], null)
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
        syllabusMonHoc: duLieu.syllabusMonHoc.map((item) => ({
            ...item,
            tenChuongTrinhMon: layTen(chuongTrinhMonMap, item.chuongTrinhMonId, ['maMonTrongCt', 'tenMon']),
            tenSyllabusMonHocGoc: layTen(syllabusMonHocGocMap, item.syllabusMonHocGocId, ['ten', 'ma']),
            maSyllabusMonHocGoc: layTen(syllabusMonHocGocMap, item.syllabusMonHocGocId, ['ma'])
        })),
        syllabusMonHocGoc: duLieu.syllabusMonHocGoc.map((item) => ({
            ...item,
            tenMonHoc: layTen(monHocMap, item.monHocId, ['maMon', 'tenMon'])
        })),
        syllabusMonHocGocChuongBai: duLieu.syllabusMonHocGocChuongBai.map((item) => ({
            ...item,
            tenSyllabusMonHocGoc: layTen(syllabusMonHocGocMap, item.syllabusMonHocGocId, ['ma', 'ten'])
        })),
        syllabusMonHocGocDieuKien: duLieu.syllabusMonHocGocDieuKien.map((item) => ({
            ...item,
            tenSyllabusMonHocGoc: layTen(syllabusMonHocGocMap, item.syllabusMonHocGocId, ['ma', 'ten']),
            tenDieuKienGoc: layTen(dieuKienMonHocGocMap, item.dieuKienGocId, ['ma', 'noiDung'])
        })),
        syllabusMonHocGocTaiLieu: duLieu.syllabusMonHocGocTaiLieu.map((item) => ({
            ...item,
            tenSyllabusMonHocGoc: layTen(syllabusMonHocGocMap, item.syllabusMonHocGocId, ['ma', 'ten']),
            tenTaiLieuGoc: layTen(taiLieuGocMap, item.taiLieuGocId, ['ma', 'ten'])
        })),
        dieuKienMonHoc: duLieu.dieuKienMonHoc.map((item) => ({
            ...item,
            tenSyllabusMon: layTen(syllabusMonMap, item.syllabusMonId, [
                'ten',
                'tenSyllabusMonHocGoc',
                'ma',
                'maSyllabusMonHocGoc',
                'mucTieu'
            ])
        })),
        dieuKienMonHocGoc: duLieu.dieuKienMonHocGoc.map((item) => ({...item})),
        syllabusMonHocDieuKien: duLieu.syllabusMonHocDieuKien.map((item) => ({
            ...item,
            tenSyllabusMon: layTen(syllabusMonMap, item.syllabusMonId, ['viTri', 'tinhChat', 'mucTieu']),
            tenDieuKienGoc: layTen(dieuKienMonHocGocMap, item.dieuKienGocId, ['ma', 'noiDung'])
        })),
        taiLieuGoc: duLieu.taiLieuGoc.map((item) => ({...item})),
        syllabusMonHocTaiLieu: duLieu.syllabusMonHocTaiLieu.map((item) => ({
            ...item,
            tenSyllabusMon: layTen(syllabusMonMap, item.syllabusMonId, ['viTri', 'tinhChat', 'mucTieu']),
            tenTaiLieuGoc: layTen(taiLieuGocMap, item.taiLieuGocId, ['ma', 'ten'])
        })),
        syllabusChuongBai: duLieu.syllabusChuongBai.map((item) => ({
            ...item,
            tenSyllabusMon: layTen(syllabusMonMap, item.syllabusMonId, [
                'ten',
                'tenSyllabusMonHocGoc',
                'ma',
                'maSyllabusMonHocGoc',
                'mucTieu'
            ])
        })),
        syllabusTaiLieu: duLieu.syllabusTaiLieu.map((item) => ({
            ...item,
            tenSyllabusMon: layTen(syllabusMonMap, item.syllabusMonId, [
                'ten',
                'tenSyllabusMonHocGoc',
                'ma',
                'maSyllabusMonHocGoc',
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
        khungKyGoc: [],
        monHoc: [],
        chuongTrinhMon: [],
        nhomKienThuc: [],
        nhomKienThucGoc: [],
        nhomTuChon: [],
        nhomTuChonGoc: [],
        monTuChon: [],
        mucTieuChuongTrinhGoc: [],
        mucTieuChuongTrinh: [],
        nangLucDauRaGoc: [],
        nangLucDauRa: [],
        viTriViecLamGoc: [],
        viTriViecLam: [],
        dieuKienTotNghiepGoc: [],
        dieuKienTotNghiep: [],
        chuongTrinhVersionMucTieu: [],
        chuongTrinhVersionNangLuc: [],
        chuongTrinhVersionViTriViecLam: [],
        chuongTrinhVersionDieuKienTotNghiep: [],
        syllabusChuongTrinh: [],
        syllabusChuongTrinhGoc: [],
        monTienQuyet: [],
        quyDoiDiem: [],
        quyDoiDiemMau: [],
        chuongTrinhMonQuyDoiDiemMau: [],
        cauHinhDanhGiaMau: [],
        syllabusMonHoc: [],
        syllabusMonHocGoc: [],
        syllabusMonHocGocChuongBai: [],
        syllabusMonHocGocDieuKien: [],
        syllabusMonHocGocTaiLieu: [],
        dieuKienMonHoc: [],
        dieuKienMonHocGoc: [],
        syllabusMonHocDieuKien: [],
        taiLieuGoc: [],
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
        chuongTrinh: null,
        chuongTrinhVersion: null,
        syllabusChuongTrinhGoc: null,
        mucTieuChuongTrinhGoc: null,
        mucTieuChuongTrinh: null,
        nangLucDauRaGoc: null,
        nangLucDauRa: null,
        viTriViecLamGoc: null,
        viTriViecLam: null,
        dieuKienTotNghiepGoc: null,
        dieuKienTotNghiep: null,
        khungKy: null,
        khungKyGoc: null,
        nhomKienThuc: null,
        nhomKienThucGoc: null,
        nhomTuChon: null,
        nhomTuChonGoc: null,
        monHoc: null,
        chuongTrinhMon: null,
        quyDoiDiemMau: null,
        cauHinhDanhGiaMau: null,
        syllabusMonHoc: null,
        syllabusMonHocGoc: null,
        dieuKienMonHocGoc: null,
        dieuKienMonHoc: null,
        taiLieuGoc: null,
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

export function useDaoTaoXemChuongTrinh() {
    const thongBao = ref('')
    const loaiThongBao = ref('success')

    const selected = reactive(taoSelectedRong())
    const viewed = reactive(taoSelectedRong())
    const rawData = reactive(taoDuLieuRong())
    const tableMessages = reactive({})


    const khoQuyDoiDiemMauGoc = ref([])

    const duLieu = computed(() => themTenLienKet(rawData))

    const quyDoiDiemMauGocRows = computed(() => {
        const rowsDangCo = duLieu.value.quyDoiDiemMau || []
        if (rowsDangCo.length) return rowsDangCo
        return khoQuyDoiDiemMauGoc.value || []
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
                const result = await service.getAll({size: 200})
                const list = layDanhSachTuKetQua(result)
                rawData[key] = list
                if (key === 'quyDoiDiemMau' && list.length) {
                    khoQuyDoiDiemMauGoc.value = list.map((item) => ({...item}))
                }
            } catch (error) {
                console.warn(`Không tải được dữ liệu có sẵn của bảng ${key}`, error)
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

            rawData[key] = layDanhSachTuKetQua(result)
        } catch (error) {
            console.warn(`Không tải được dữ liệu bảng ${key} theo params`, error)
        }
    }

    async function taiDuLieuTheoMonTrongChuongTrinh(chuongTrinhMonId) {
        if (!chuongTrinhMonId) {
            rawData.chuongTrinhMonQuyDoiDiemMau = []
            rawData.quyDoiDiem = []
            return
        }

        await Promise.all([
            taiDuLieuBangTheoParams('chuongTrinhMonQuyDoiDiemMau', {chuongTrinhMonId}),
            taiDuLieuBangTheoParams('quyDoiDiem', {chuongTrinhMonId})
        ])
    }

    watch(
        () => selected.chuongTrinhMon?.id || null,
        (chuongTrinhMonId) => {
            taiDuLieuTheoMonTrongChuongTrinh(chuongTrinhMonId)
        }
    )
    onMounted(() => {
        taiDuLieuCoSanTatCaBang()
    })

    const selectionChildren = {
        nganh: ['trinhDoDaoTao', 'loaiChuongTrinh', 'nganhLoaiChuongTrinh', 'chuongTrinh', 'chuongTrinhVersion', 'khungKy', 'nhomKienThuc', 'nhomTuChon', 'monHoc', 'chuongTrinhMon', 'syllabusMonHoc'],
        trinhDoDaoTao: ['chuongTrinh', 'chuongTrinhVersion', 'khungKy', 'nhomKienThuc', 'nhomTuChon', 'monHoc', 'chuongTrinhMon', 'syllabusMonHoc'],
        loaiChuongTrinh: ['nganhLoaiChuongTrinh', 'chuongTrinh', 'chuongTrinhVersion', 'khungKy', 'nhomKienThuc', 'nhomTuChon', 'monHoc', 'chuongTrinhMon', 'syllabusMonHoc'],
        nganhLoaiChuongTrinh: ['chuongTrinh', 'chuongTrinhVersion', 'khungKy', 'nhomKienThuc', 'nhomTuChon', 'monHoc', 'chuongTrinhMon', 'syllabusMonHoc'],
        chuongTrinh: ['chuongTrinhVersion', 'khungKy', 'nhomKienThuc', 'nhomTuChon', 'monHoc', 'chuongTrinhMon', 'syllabusMonHoc'],
        chuongTrinhVersion: ['syllabusChuongTrinhGoc', 'syllabusChuongTrinh', 'mucTieuChuongTrinhGoc', 'mucTieuChuongTrinh', 'nangLucDauRaGoc', 'nangLucDauRa', 'viTriViecLamGoc', 'viTriViecLam', 'dieuKienTotNghiepGoc', 'dieuKienTotNghiep', 'khungKyGoc', 'khungKy', 'nhomKienThucGoc', 'nhomKienThuc', 'nhomTuChonGoc', 'nhomTuChon', 'monHoc', 'chuongTrinhMon', 'monTuChon', 'monTienQuyet', 'quyDoiDiemMau', 'quyDoiDiem', 'chuongTrinhMonQuyDoiDiemMau', 'syllabusMonHocGoc', 'syllabusMonHoc'],
        khungKyGoc: ['khungKy', 'chuongTrinhMon'],
        khungKy: ['chuongTrinhMon', 'syllabusMonHoc'],
        nhomKienThucGoc: ['nhomKienThuc', 'chuongTrinhMon'],
        nhomKienThuc: ['chuongTrinhMon', 'syllabusMonHoc'],
        nhomTuChonGoc: ['nhomTuChon'],
        nhomTuChon: [],
        monHoc: ['syllabusMonHocGoc', 'chuongTrinhMon', 'syllabusMonHoc'],
        chuongTrinhMon: ['quyDoiDiemMau', 'quyDoiDiem', 'chuongTrinhMonQuyDoiDiemMau', 'cauHinhDanhGiaMau', 'monTienQuyet', 'monTuChon', 'syllabusMonHoc'],
        syllabusMonHocGoc: ['syllabusMonHoc'],
        syllabusMonHoc: ['dieuKienMonHocGoc', 'taiLieuGoc'],
        dieuKienMonHocGoc: [],
        taiLieuGoc: []
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
            const cacheIndex = khoQuyDoiDiemMauGoc.value.findIndex((item) => String(item.id || '') === String(row.id || ''))
            if (cacheIndex >= 0) khoQuyDoiDiemMauGoc.value.splice(cacheIndex, 1, {...row})
            else khoQuyDoiDiemMauGoc.value.push({...row})
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

    const bangGocGanVersion = {

        nangLucDauRaGoc: {
            joinKey: 'chuongTrinhVersionNangLuc',
            gocIdKey: 'nangLucGocId',
            serviceKey: 'chuongTrinhVersionNangLuc',
            tenBang: 'Năng lực đầu ra'
        },
        viTriViecLamGoc: {
            joinKey: 'chuongTrinhVersionViTriViecLam',
            gocIdKey: 'viTriGocId',
            serviceKey: 'chuongTrinhVersionViTriViecLam',
            tenBang: 'Vị trí việc làm'
        },

    }

    const bangGocTaoApDungVersion = {

        dieuKienTotNghiepGoc: {
            joinKey: 'dieuKienTotNghiep',
            gocIdKey: 'ma',
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

        mucTieuChuongTrinhGoc: {
            joinKey: 'mucTieuChuongTrinh',
            gocIdKey: 'ma',
            matchBy: {rowKey: 'ma', itemKey: 'ma'},
            serviceKey: 'mucTieuChuongTrinh',
            tenBang: 'Mục tiêu chương trình gốc',
            buildPayload: (item, chuongTrinhVersionId) => ({
                chuongTrinhVersionId,
                ma: item.ma || '',
                loai: item.loai || 'chung',
                noiDung: item.noiDung || '',
                thuTu: layThuTuGanTiepTheo('mucTieuChuongTrinh', chuongTrinhVersionId),
                ghiChu: item.ghiChu || ''
            })
        },

        syllabusChuongTrinhGoc: {
            joinKey: 'syllabusChuongTrinh',
            gocIdKey: 'syllabusChuongTrinhGocId',
            matchBy: {rowKey: 'syllabusChuongTrinhGocId', itemKey: 'id'},
            serviceKey: 'syllabusChuongTrinh',
            tenBang: 'Syllabus chương trình gốc',
            buildPayload: (item, chuongTrinhVersionId) => ({
                chuongTrinhVersionId,
                syllabusChuongTrinhGocId: item.id,
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
        khungKyGoc: {
            joinKey: 'khungKy',
            gocIdKey: 'khungKyGocId',
            serviceKey: 'khungKy',
            tenBang: 'Khung kỳ gốc',
            buildPayload: (item, chuongTrinhVersionId) => ({
                chuongTrinhVersionId,
                loaiChuongTrinhId: layLoaiChuongTrinhDangChon(),
                khungKyGocId: item.id,
                maKy: item.maKy || item.ma || '',
                tenKy: item.tenKy || item.ten || '',
                thuTu: item.thuTu || layThuTuGanTiepTheo('khungKy', chuongTrinhVersionId)
            })
        },
        nhomKienThucGoc: {
            joinKey: 'nhomKienThuc',
            gocIdKey: 'nhomKienThucGocId',
            serviceKey: 'nhomKienThuc',
            tenBang: 'Nhóm kiến thức gốc',
            buildPayload: (item, chuongTrinhVersionId) => ({
                chuongTrinhVersionId,
                nhomKienThucGocId: item.id,
                ma: item.ma || '',
                ten: item.ten || '',
                loaiNhom: item.loaiNhom || 'chung',
                thuTu: item.thuTu || layThuTuGanTiepTheo('nhomKienThuc', chuongTrinhVersionId),
                tongTinChi: item.tongTinChi ?? null,
                tongSoGio: item.tongSoGio ?? null,
                tongGioLyThuyet: item.tongGioLyThuyet ?? null,
                tongGioThucHanh: item.tongGioThucHanh ?? null,
                tongGioKiemTra: item.tongGioKiemTra ?? null,
                moTa: item.moTa || ''
            })
        },
        nhomTuChonGoc: {
            joinKey: 'nhomTuChon',
            gocIdKey: 'nhomTuChonGocId',
            serviceKey: 'nhomTuChon',
            tenBang: 'Nhóm tự chọn gốc',
            buildPayload: (item, chuongTrinhVersionId) => ({
                chuongTrinhVersionId,
                nhomTuChonGocId: item.id,
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
            gocIdKey: 'quyDoiDiemMauId',
            monIdKey: 'chuongTrinhMonId',
            serviceKey: 'chuongTrinhMonQuyDoiDiemMau',
            tenBang: 'Quy đổi điểm mẫu'
        }
    }

    function timChuongTrinhMonTheoMonHocGoc(item, parentValues = {}) {
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

    function taoPayloadChuongTrinhMonTuMonHocGoc(item, parentValues = {}) {
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
    async function toggleGanMonHocGocVaoChuongTrinhMon(item, parentValues = {}) {
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

    function taoPayloadQuyDoiDiemTuMau(item, chuongTrinhMonId) {
        return {
            chuongTrinhMonId,
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

    function khopQuyDoiDiemTuMau(row, payload, chuongTrinhMonId) {
        return String(row.chuongTrinhMonId || '') === String(chuongTrinhMonId || '')
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

    function timCacQuyDoiDiemDaCoTuMau(item, chuongTrinhMonId) {
        const payload = taoPayloadQuyDoiDiemTuMau(item, chuongTrinhMonId)
        const list = rawData.quyDoiDiem || []
        return list.filter((row) => khopQuyDoiDiemTuMau(row, payload, chuongTrinhMonId))
    }

    function timQuyDoiDiemDaCoTuMau(item, chuongTrinhMonId) {
        return timCacQuyDoiDiemDaCoTuMau(item, chuongTrinhMonId)[0] || null
    }

    async function dayQuyDoiDiemMauXuongQuyDoiDiem(item, chuongTrinhMonId) {
        if (!item?.id || !chuongTrinhMonId) return {created: false, existed: false}

        const serviceQuyDoiDiem = daoTaoXemChuongTrinhService.quyDoiDiem
        if (!serviceQuyDoiDiem?.create) {
            throw new Error('Chưa khai báo API tạo Quy đổi điểm.')
        }

        const existing = timQuyDoiDiemDaCoTuMau(item, chuongTrinhMonId)
        if (existing?.id) {
            capNhatDongTrongRawData('quyDoiDiem', existing)
            return {created: false, existed: true, row: existing}
        }

        const payload = taoPayloadQuyDoiDiemTuMau(item, chuongTrinhMonId)
        const saved = await serviceQuyDoiDiem.create(payload)
        const row = layDuLieuLuu(saved) || payload
        capNhatDongTrongRawData('quyDoiDiem', row)
        return {created: true, existed: false, row}
    }

    async function xoaQuyDoiDiemDaDayTuMau(item, chuongTrinhMonId) {
        if (!item?.id || !chuongTrinhMonId) return {deleted: 0}

        const serviceQuyDoiDiem = daoTaoXemChuongTrinhService.quyDoiDiem
        if (!serviceQuyDoiDiem?.delete) {
            throw new Error('Chưa khai báo API xóa Quy đổi điểm.')
        }

        const rowsCanXoa = timCacQuyDoiDiemDaCoTuMau(item, chuongTrinhMonId).filter((row) => row?.id)
        let deleted = 0

        for (const row of rowsCanXoa) {
            await serviceQuyDoiDiem.delete(row.id)
            xoaDongTrongRawData('quyDoiDiem', row.id)
            deleted += 1
        }

        return {deleted}
    }
    function taoPayloadQuyDoiDiemTuCotDiemMau(item, chuongTrinhMonId) {
        return {
            chuongTrinhMonId,
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

    function khopQuyDoiDiemTuCotDiemMau(row, payload, chuongTrinhMonId) {
        return String(row.chuongTrinhMonId || '') === String(chuongTrinhMonId || '')
            && String(row.ma || '') === String(payload.ma || '')
            && String(row.ten || '') === String(payload.ten || '')
            && String(row.loaiMau || 'COT_DIEM') === String(payload.loaiMau || 'COT_DIEM')
            && String(chuanHoaGiaTriQuyDoi(row.tyLe)) === String(chuanHoaGiaTriQuyDoi(payload.tyLe))
            && String(chuanHoaGiaTriQuyDoi(row.diemToiDa)) === String(chuanHoaGiaTriQuyDoi(payload.diemToiDa))
            && String(row.thuTu || '') === String(payload.thuTu || '')
            && String(row.batBuoc !== false) === String(payload.batBuoc !== false)
    }

    function timQuyDoiDiemDaCoTuCotDiemMau(item, chuongTrinhMonId) {
        const payload = taoPayloadQuyDoiDiemTuCotDiemMau(item, chuongTrinhMonId)

        return (rawData.quyDoiDiem || []).find((row) => {
            return khopQuyDoiDiemTuCotDiemMau(row, payload, chuongTrinhMonId)
        }) || null
    }

    async function toggleCopyCotDiemMauVaoQuyDoiDiem(item, parentValues = {}) {
        const chuongTrinhMonId = selected.chuongTrinhMon?.id || parentValues.chuongTrinhMonId

        if (!chuongTrinhMonId) {
            baoTinBang('cauHinhDanhGiaMau', 'Cần chọn Môn trong chương trình trước khi gán Cột điểm mẫu.', 'error')
            return
        }

        const serviceQuyDoiDiem = daoTaoXemChuongTrinhService.quyDoiDiem

        if (!serviceQuyDoiDiem?.create) {
            baoTinBang('cauHinhDanhGiaMau', 'Chưa khai báo API tạo Quy đổi điểm.', 'error')
            return
        }

        const existing = timQuyDoiDiemDaCoTuCotDiemMau(item, chuongTrinhMonId)

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

                baoTinBang('cauHinhDanhGiaMau', 'Đã bỏ gán Cột điểm mẫu khỏi Quy đổi điểm của môn đang chọn.')
                return
            }

            const payload = taoPayloadQuyDoiDiemTuCotDiemMau(item, chuongTrinhMonId)
            const saved = await serviceQuyDoiDiem.create(payload)
            const row = layDuLieuLuu(saved) || payload
            const tenCotDiemMau = item.tenCotDiem || row.tenCotDiemMau || row.ghiChu || ''

            const rowDaLuu = capNhatDongTrongRawData('quyDoiDiem', {
                ...row,
                tenCotDiemMau,
                ghiChu: row.ghiChu || tenCotDiemMau
            })

            selectEntity('cauHinhDanhGiaMau', item)
            selectEntity('quyDoiDiem', rowDaLuu)

            baoTinBang('cauHinhDanhGiaMau', 'Đã copy Cột điểm mẫu xuống Quy đổi điểm đã lưu cho môn trong chương trình.')
        } catch (error) {
            const message = layThongBaoLoi(error, 'Không gán được Cột điểm mẫu vào Quy đổi điểm.')
            baoTinBang('cauHinhDanhGiaMau', message, 'error')
        }
    }
    const bangGocGanSyllabus = {
        dieuKienMonHocGoc: {
            joinKey: 'syllabusMonHocDieuKien',
            gocIdKey: 'dieuKienGocId',
            syllabusIdKey: 'syllabusMonId',
            serviceKey: 'syllabusMonHocDieuKien',
            tenBang: 'Điều kiện môn học gốc'
        },
        taiLieuGoc: {
            joinKey: 'syllabusMonHocTaiLieu',
            gocIdKey: 'taiLieuGocId',
            syllabusIdKey: 'syllabusMonId',
            serviceKey: 'syllabusMonHocTaiLieu',
            tenBang: 'Tài liệu gốc'
        }
    }

    function timDongNoiMon(config, item, chuongTrinhMonId) {
        const list = rawData[config.joinKey] || []
        return list.find((row) => {
            return String(row[config.monIdKey] || '') === String(chuongTrinhMonId || '')
                && String(row[config.gocIdKey] || '') === String(item?.id || '')
        })
    }


    function timDongNoiSyllabus(config, item, syllabusMonId) {
        const list = rawData[config.joinKey] || []
        return list.find((row) => {
            return String(row[config.syllabusIdKey] || '') === String(syllabusMonId || '')
                && String(row[config.gocIdKey] || '') === String(item?.id || '')
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

    function layLoaiChuongTrinhDangChon() {
        return selected.loaiChuongTrinh?.id || selected.chuongTrinh?.loaiChuongTrinhId || null
    }

    function timDongNoiVersion(config, item, chuongTrinhVersionId) {
        const list = rawData[config.joinKey] || []
        return list.find((row) => {
            return String(row.chuongTrinhVersionId || '') === String(chuongTrinhVersionId || '')
                && String(row[config.gocIdKey] || '') === String(item?.id || '')
        })
    }

    async function ganBangGocVaoVersion(key, item) {
        const config = bangGocGanVersion[key]
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
                [config.gocIdKey]: item.id,
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
        'mucTieuChuongTrinhGoc',
        'mucTieuChuongTrinh',
        'nangLucDauRaGoc',
        'nangLucDauRa',
        'viTriViecLamGoc',
        'viTriViecLam',
        'dieuKienTotNghiepGoc',
        'dieuKienTotNghiep',
        'monTienQuyet',
        'quyDoiDiemMau',
        'dieuKienMonHocGoc',
        'taiLieuGoc',
        'dieuKienMonHoc',
        'syllabusChuongBai',
        'syllabusTaiLieu'
    ])

    function timDongNoiBangGoc(key, item, chuongTrinhVersionId) {
        const config = bangGocGanVersion[key]
        if (!config) return null
        return timDongNoiVersion(config, item, chuongTrinhVersionId)
    }

    function timDongApDungVersion(key, item, chuongTrinhVersionId) {
        const config = bangGocTaoApDungVersion[key]
        if (!config) return null

        const list = rawData[config.joinKey] || []
        const rowKey = config.matchBy?.rowKey || config.gocIdKey
        const itemKey = config.matchBy?.itemKey || 'id'

        return list.find((row) => {
            return String(row.chuongTrinhVersionId || '') === String(chuongTrinhVersionId || '')
                && String(row[rowKey] || '') === String(item?.[itemKey] || '')
        })
    }

    function layConfigGanSyllabusTheoNguCanh(key, parentValues = {}) {
        if (key !== 'dieuKienMonHocGoc' && key !== 'taiLieuGoc') return null

        const gocIdKey = key === 'dieuKienMonHocGoc' ? 'dieuKienGocId' : 'taiLieuGocId'
        const tenBang = key === 'dieuKienMonHocGoc' ? 'Điều kiện môn học gốc' : 'Tài liệu gốc'

        const syllabusMonHocGocId = parentValues.syllabusMonHocGocId || selected.syllabusMonHocGoc?.id
        if (syllabusMonHocGocId) {
            return {
                joinKey: key === 'dieuKienMonHocGoc' ? 'syllabusMonHocGocDieuKien' : 'syllabusMonHocGocTaiLieu',
                gocIdKey,
                syllabusIdKey: 'syllabusMonHocGocId',
                syllabusId: syllabusMonHocGocId,
                serviceKey: key === 'dieuKienMonHocGoc' ? 'syllabusMonHocGocDieuKien' : 'syllabusMonHocGocTaiLieu',
                tenBang
            }
        }

        const syllabusMonId = parentValues.syllabusMonId || selected.syllabusMonHoc?.id
        if (syllabusMonId) {
            return {
                joinKey: key === 'dieuKienMonHocGoc' ? 'syllabusMonHocDieuKien' : 'syllabusMonHocTaiLieu',
                gocIdKey,
                syllabusIdKey: 'syllabusMonId',
                syllabusId: syllabusMonId,
                serviceKey: key === 'dieuKienMonHocGoc' ? 'syllabusMonHocDieuKien' : 'syllabusMonHocTaiLieu',
                tenBang
            }
        }

        return null
    }

    function timDieuKienMonHocDaCopyTuGoc(item, syllabusMonId) {
        return (rawData.dieuKienMonHoc || []).find((row) => {
            return String(row.syllabusMonId || '') === String(syllabusMonId || '')
                && String(row.ma || '') === String(item?.ma || '')
        }) || null
    }

    function taoPayloadDieuKienMonHocTuGoc(item, syllabusMonId) {
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

    function timSyllabusTaiLieuDaCopyTuGoc(item, syllabusMonId) {
        return (rawData.syllabusTaiLieu || []).find((row) => {
            return String(row.syllabusMonId || '') === String(syllabusMonId || '')
                && String(row.ma || '') === String(item?.ma || '')
        }) || null
    }

    function taoPayloadSyllabusTaiLieuTuGoc(item, syllabusMonId) {
        return {
            syllabusMonId,
            ma: item.ma || '',
            ten: item.ten || '',
            loai: item.loai || 'GIAO_TRINH',
            tacGia: item.tacGia || '',
            nhaXuatBan: item.nhaXuatBan || '',
            namXuatBan: item.namXuatBan ?? null,
            duongDan: item.duongDan || '',
            ghiChu: item.ghiChu || ''
        }
    }

    async function toggleCopyDieuKienMonHocGocVaoSyllabus(item, parentValues = {}) {
        const syllabusMonId = parentValues.syllabusMonId || selected.syllabusMonHoc?.id

        if (!syllabusMonId) {
            baoTinBang('dieuKienMonHocGoc', 'Cần chọn Syllabus môn học áp dụng trước khi gán điều kiện môn học gốc.', 'error')
            return
        }

        const serviceDieuKien = daoTaoXemChuongTrinhService.dieuKienMonHoc

        if (!serviceDieuKien?.create) {
            baoTinBang('dieuKienMonHocGoc', 'Chưa khai báo API tạo Điều kiện môn học đã lưu vào syllabus.', 'error')
            return
        }

        const existing = timDieuKienMonHocDaCopyTuGoc(item, syllabusMonId)

        try {
            if (existing?.id) {
                if (!serviceDieuKien.delete) {
                    baoTinBang('dieuKienMonHocGoc', 'Chưa khai báo API xóa Điều kiện môn học đã lưu vào syllabus.', 'error')
                    return
                }

                await serviceDieuKien.delete(existing.id)
                xoaDongTrongRawData('dieuKienMonHoc', existing.id)

                if (selected.dieuKienMonHoc?.id === existing.id) {
                    selectEntity('dieuKienMonHoc', null)
                }

                baoTinBang('dieuKienMonHocGoc', 'Đã bỏ gán điều kiện môn học khỏi syllabus.')
                return
            }

            const payload = taoPayloadDieuKienMonHocTuGoc(item, syllabusMonId)
            const saved = await serviceDieuKien.create(payload)
            const row = layDuLieuLuu(saved) || payload

            capNhatDongTrongRawData('dieuKienMonHoc', row)
            selectEntity('dieuKienMonHocGoc', item)

            baoTinBang('dieuKienMonHocGoc', 'Đã copy điều kiện môn học gốc vào Điều kiện môn học đã lưu vào syllabus.')
        } catch (error) {
            const message = layThongBaoLoi(error, 'Không gán được điều kiện môn học gốc vào syllabus.')
            baoTinBang('dieuKienMonHocGoc', message, 'error')
        }
    }

    async function toggleCopyTaiLieuGocVaoSyllabus(item, parentValues = {}) {
        const syllabusMonId = parentValues.syllabusMonId || selected.syllabusMonHoc?.id

        if (!syllabusMonId) {
            baoTinBang('taiLieuGoc', 'Cần chọn Syllabus môn học áp dụng trước khi gán tài liệu gốc.', 'error')
            return
        }

        const serviceTaiLieu = daoTaoXemChuongTrinhService.syllabusTaiLieu

        if (!serviceTaiLieu?.create) {
            baoTinBang('taiLieuGoc', 'Chưa khai báo API tạo Tài liệu đã lưu vào syllabus.', 'error')
            return
        }

        const existing = timSyllabusTaiLieuDaCopyTuGoc(item, syllabusMonId)

        try {
            if (existing?.id) {
                if (!serviceTaiLieu.delete) {
                    baoTinBang('taiLieuGoc', 'Chưa khai báo API xóa Tài liệu đã lưu vào syllabus.', 'error')
                    return
                }

                await serviceTaiLieu.delete(existing.id)
                xoaDongTrongRawData('syllabusTaiLieu', existing.id)

                if (selected.syllabusTaiLieu?.id === existing.id) {
                    selectEntity('syllabusTaiLieu', null)
                }

                baoTinBang('taiLieuGoc', 'Đã bỏ gán tài liệu khỏi syllabus.')
                return
            }

            const payload = taoPayloadSyllabusTaiLieuTuGoc(item, syllabusMonId)
            const saved = await serviceTaiLieu.create(payload)
            const row = layDuLieuLuu(saved) || payload

            capNhatDongTrongRawData('syllabusTaiLieu', row)
            selectEntity('taiLieuGoc', item)

            baoTinBang('taiLieuGoc', 'Đã copy tài liệu gốc vào Tài liệu đã lưu vào syllabus.')
        } catch (error) {
            const message = layThongBaoLoi(error, 'Không gán được tài liệu gốc vào syllabus.')
            baoTinBang('taiLieuGoc', message, 'error')
        }
    }

    function timDongNoiSyllabusTheoConfig(config, item) {
        if (!config?.syllabusId) return null
        const list = rawData[config.joinKey] || []
        return list.find((row) => {
            return String(row[config.syllabusIdKey] || '') === String(config.syllabusId || '')
                && String(row[config.gocIdKey] || '') === String(item?.id || '')
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
        'dieuKienMonHocGoc',
        'taiLieuGoc',
        'dieuKienMonHoc',
        'syllabusChuongBai',
        'syllabusTaiLieu'
    ])

    async function toggleLuuBangPhu(key, item, parentValues = {}, service = null) {
        if (!item?.id) {
            baoTinBang(key, 'Không tìm thấy dòng dữ liệu để lưu hoặc bỏ lưu.', 'error')
            return
        }

        if (key === 'monHoc') {
            await toggleGanMonHocGocVaoChuongTrinhMon(item, parentValues)
            return
        }
        if (key === 'dieuKienMonHocGoc') {
            await toggleCopyDieuKienMonHocGocVaoSyllabus(item, parentValues)
            return
        }

        if (key === 'taiLieuGoc') {
            await toggleCopyTaiLieuGocVaoSyllabus(item, parentValues)
            return
        }
        if (key === 'cauHinhDanhGiaMau') {
            await toggleCopyCotDiemMauVaoQuyDoiDiem(item, parentValues)
            return
        }
        const configApDungVersion = bangGocTaoApDungVersion[key]

        if (configApDungVersion) {
            const chuongTrinhVersionId = selected.chuongTrinhVersion?.id || parentValues.chuongTrinhVersionId
            if (!chuongTrinhVersionId) {
                baoTinBang(key, `Cần chọn Version trước khi lưu ${configApDungVersion.tenBang}.`, 'error')
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
                        baoTinBang(key, `Chưa khai báo API xóa để bỏ lưu ${configApDungVersion.tenBang}.`, 'error')
                        return
                    }

                    await serviceApDung.delete(existing.id)
                    xoaDongTrongRawData(configApDungVersion.joinKey, existing.id)
                    if (selected[key]?.id === item.id) selectEntity(key, null)
                    baoTinBang(key, `Đã bỏ lưu ${configApDungVersion.tenBang} khỏi Version hiện tại. Dòng gốc đã chuyển về trạng thái có thể Lưu.`)
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
                baoTinBang(key, `Đã lưu ${configApDungVersion.tenBang} vào Version hiện tại.`)
            } catch (error) {
                const message = layThongBaoLoi(error, `Không lưu/bỏ lưu được ${configApDungVersion.tenBang}.`)
                baoTinBang(key, message, 'error')
            }

            return
        }

        const config = bangGocGanVersion[key]

        if (config) {
            const chuongTrinhVersionId = selected.chuongTrinhVersion?.id || parentValues.chuongTrinhVersionId
            if (!chuongTrinhVersionId) {
                baoTinBang(key, `Cần chọn Version trước khi lưu ${config.tenBang}.`, 'error')
                return
            }

            const existing = timDongNoiBangGoc(key, item, chuongTrinhVersionId)
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
                    baoTinBang(key, `Đã bỏ lưu ${config.tenBang} khỏi Version hiện tại.`)
                    return
                }

                const payload = {
                    chuongTrinhVersionId,
                    [config.gocIdKey]: item.id,
                    thuTu: layThuTuGanTiepTheo(config.joinKey, chuongTrinhVersionId),
                    ghiChu: item.ghiChu || ''
                }

                const saved = await serviceNoi.create(payload)
                const row = layDuLieuLuu(saved) || payload
                capNhatDongTrongRawData(config.joinKey, row)
                selectEntity(key, item)
                baoTinBang(key, `Đã lưu ${config.tenBang} vào Version hiện tại.`)
            } catch (error) {
                const message = layThongBaoLoi(error, `Không lưu/bỏ lưu được ${config.tenBang}.`)
                baoTinBang(key, message, 'error')
            }

            return
        }

        const configMau = bangMauGanMon[key]
        if (configMau) {
            const chuongTrinhMonId = selected.chuongTrinhMon?.id || parentValues.chuongTrinhMonId
            if (!chuongTrinhMonId) {
                baoTinBang(key, `Cần chọn Môn trong chương trình trước khi lưu ${configMau.tenBang}.`, 'error')
                return
            }

            const existing = timDongNoiMon(configMau, item, chuongTrinhMonId)
            const serviceNoi = daoTaoXemChuongTrinhService[configMau.serviceKey]

            if (!serviceNoi) {
                baoTinBang(key, `Chưa khai báo API bảng nối cho ${configMau.tenBang}.`, 'error')
                return
            }

            try {
                if (existing?.id) {
                    await serviceNoi.delete(existing.id)
                    xoaDongTrongRawData(configMau.joinKey, existing.id)

                    await xoaQuyDoiDiemDaDayTuMau(item, chuongTrinhMonId)

                    if (selected[key]?.id === item.id) selectEntity(key, null)
                    baoTinBang(key, `Đã bỏ gán ${configMau.tenBang} khỏi môn trong chương trình và xóa quy đổi điểm đã copy.`)
                    return
                }

                const payload = {
                    [configMau.monIdKey]: chuongTrinhMonId,
                    [configMau.gocIdKey]: item.id,
                    ghiChu: item.ghiChu || ''
                }

                const saved = await serviceNoi.create(payload)
                const row = layDuLieuLuu(saved) || payload
                capNhatDongTrongRawData(configMau.joinKey, row)

                await dayQuyDoiDiemMauXuongQuyDoiDiem(item, chuongTrinhMonId)

                selectEntity(key, item)
                baoTinBang(key, `Đã gán ${configMau.tenBang} vào môn trong chương trình và copy xuống bảng Quy đổi điểm.`)
            } catch (error) {
                const message = layThongBaoLoi(error, `Không lưu/bỏ lưu được ${configMau.tenBang}.`)
                baoTinBang(key, message, 'error')
            }

            return
        }


        if (key === 'syllabusMonHocGoc') {
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
                    && String(row.syllabusMonHocGocId || '') === String(item.id || '')
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
                    syllabusMonHocGocId: item.id,

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
                    [configSyllabus.gocIdKey]: item.id,
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
        if (bangGocGanVersion[key]) {
            const handled = await ganBangGocVaoVersion(key, item)
            if (handled) return
        }

        if (!item) {
            selectEntity(key, null)
            return
        }

        const configMau = bangMauGanMon[key]
        if (configMau) {
            const chuongTrinhMonId = selected.chuongTrinhMon?.id || parentValues.chuongTrinhMonId
            if (!chuongTrinhMonId) {
                baoTinBang(key, `Cần chọn Môn trong chương trình trước khi lưu ${configMau.tenBang}.`, 'error')
                return
            }

            const existing = timDongNoiMon(configMau, item, chuongTrinhMonId)
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
                    baoTinBang(key, `Đã bỏ gắn ${configMau.tenBang} khỏi bảng 5.5 Quy đổi điểm.`)
                    return
                }

                const payload = {
                    [configMau.monIdKey]: chuongTrinhMonId,
                    [configMau.gocIdKey]: item.id,
                    ghiChu: item.ghiChu || ''
                }

                const saved = await serviceNoi.create(payload)
                const row = layDuLieuLuu(saved) || payload
                capNhatDongTrongRawData(configMau.joinKey, row)
                selectEntity(key, item)
                baoTinBang(key, `Đã gắn ${configMau.tenBang} vào bảng 5.5 Quy đổi điểm qua bảng nối.`)
            } catch (error) {
                const message = layThongBaoLoi(error, `Không lưu/bỏ lưu được ${configMau.tenBang}.`)
                baoTinBang(key, message, 'error')
            }

            return
        }


        if (key === 'syllabusMonHocGoc') {
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
                    && String(row.syllabusMonHocGocId || '') === String(item.id || '')
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
                    syllabusMonHocGocId: item.id,
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
                    congThucQuyDoi: item.congThucQuyDoi || ''
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
            const cacheIndex = khoQuyDoiDiemMauGoc.value.findIndex((item) => String(item.id || '') === String(row.id || ''))
            if (cacheIndex >= 0) khoQuyDoiDiemMauGoc.value.splice(cacheIndex, 1, {...row})
            else khoQuyDoiDiemMauGoc.value.push({...row})
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

    return {
        thongBao,
        loaiThongBao,
        tableMessages,
        selected,
        viewed,
        duLieu,
        quyDoiDiemMauGocRows,
        lookups,
        services: daoTaoXemChuongTrinhService,
        baoTin,
        selectEntity,
        viewEntity,
        chonBangPhuVaoLuong,
        toggleLuuBangPhu,
        taiDuLieuCoSanTatCaBang,
        sauKhiLuu,
        sauKhiXoa,
        lamMoiLuon,
        luuChuongTrinhTong
    }
}