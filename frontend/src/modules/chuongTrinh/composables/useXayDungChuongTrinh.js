import { computed, onMounted, reactive, ref } from 'vue'
import { xayDungChuongTrinhService } from '../services/xayDungChuongTrinhService'
import { layThongBaoLoi } from '../utils/layThongBaoLoi'

export const luaChonEnum = {
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
    loaiPhamViMon: [
        { id: 'mon_chuyen_nganh', ten: 'Môn chuyên ngành' },
        { id: 'mon_chung', ten: 'Môn học chung' }
    ],
    loaiDieuKien: [
        { id: 'tien_quyet', ten: 'Tiên quyết' },
        { id: 'song_hanh', ten: 'Song hành' },
        { id: 'hoc_truoc', ten: 'Học trước' }
    ],
    loaiDieuKienMonHoc: [
        { id: 'phong_hoc', ten: 'Phòng học' },
        { id: 'thiet_bi', ten: 'Thiết bị' },
        { id: 'hoc_lieu', ten: 'Học liệu' },
        { id: 'dung_cu', ten: 'Dụng cụ' },
        { id: 'nguyen_vat_lieu', ten: 'Nguyên vật liệu' },
        { id: 'khac', ten: 'Khác' }
    ],
    ketQua: [
        { id: 'dat', ten: 'Đạt' },
        { id: 'khong_dat', ten: 'Không đạt' }
    ],
    loaiTaiLieu: [
        { id: 'GIAO_TRINH', ten: 'Giáo trình' },
        { id: 'TAI_LIEU_THAM_KHAO', ten: 'Tài liệu tham khảo' },
        { id: 'BAI_GIANG', ten: 'Bài giảng' },
        { id: 'KHAC', ten: 'Khác' }
    ],
    donViDiem: [
        { id: 'thang_10', ten: 'Thang 10' },
        { id: 'phan_tram', ten: 'Phần trăm' }
    ]
}


const bangLayDuLieuCoSan = [
    'nganh',
    'trinhDoDaoTao',
    'loaiChuongTrinh',
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
    const item = map.get(id)
    if (!item) return id || '-'

    for (const key of keys) {
        if (item[key]) return item[key]
    }

    return item.ma || item.id || '-'
}

function themTenLienKet(duLieu) {
    const nganhMap = taoBanDo(duLieu.nganh)
    const trinhDoMap = taoBanDo(duLieu.trinhDoDaoTao)
    const loaiCtMap = taoBanDo(duLieu.loaiChuongTrinh)
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
        chuongTrinh: duLieu.chuongTrinh.map((item) => ({
            ...item,
            tenNganh: layTen(nganhMap, item.nganhId, ['tenNganh']),
            tenTrinhDo: layTen(trinhDoMap, item.trinhDoId, ['tenTrinhDo']),
            tenLoaiChuongTrinh: layTen(loaiCtMap, item.loaiChuongTrinhId, ['tenLoai']),
            tenKhungKyGoc: layTen(khungKyGocMap, item.khungKyGocId, ['maKy', 'tenKy'])
        })),
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
        mucTieuChuongTrinh: duLieu.mucTieuChuongTrinh.map((item) => ({ ...item, tenVersion: layTen(versionMap, item.chuongTrinhVersionId, ['tenVersion']) })),
        nangLucDauRa: duLieu.nangLucDauRa.map((item) => ({ ...item, tenVersion: layTen(versionMap, item.chuongTrinhVersionId, ['tenVersion']) })),
        viTriViecLam: duLieu.viTriViecLam.map((item) => ({ ...item, tenVersion: layTen(versionMap, item.chuongTrinhVersionId, ['tenVersion']) })),
        dieuKienTotNghiep: duLieu.dieuKienTotNghiep.map((item) => ({ ...item, tenVersion: layTen(versionMap, item.chuongTrinhVersionId, ['tenVersion']) })),
        chuongTrinhVersionMucTieu: duLieu.chuongTrinhVersionMucTieu.map((item) => ({ ...item, tenVersion: layTen(versionMap, item.chuongTrinhVersionId, ['tenVersion']), tenMucTieuGoc: layTen(mucTieuGocMap, item.mucTieuGocId, ['ma', 'noiDung']) })),
        chuongTrinhVersionNangLuc: duLieu.chuongTrinhVersionNangLuc.map((item) => ({ ...item, tenVersion: layTen(versionMap, item.chuongTrinhVersionId, ['tenVersion']), tenNangLucGoc: layTen(nangLucGocMap, item.nangLucGocId, ['ma', 'noiDung']) })),
        chuongTrinhVersionViTriViecLam: duLieu.chuongTrinhVersionViTriViecLam.map((item) => ({ ...item, tenVersion: layTen(versionMap, item.chuongTrinhVersionId, ['tenVersion']), tenViTriGoc: layTen(viTriGocMap, item.viTriGocId, ['ma', 'ten']) })),
        chuongTrinhVersionDieuKienTotNghiep: duLieu.chuongTrinhVersionDieuKienTotNghiep.map((item) => ({ ...item, tenVersion: layTen(versionMap, item.chuongTrinhVersionId, ['tenVersion']), tenDieuKienGoc: layTen(dieuKienTotNghiepGocMap, item.dieuKienGocId, ['ma', 'noiDung']) })),
        syllabusChuongTrinhGoc: duLieu.syllabusChuongTrinhGoc.map((item) => ({ ...item })),
        syllabusChuongTrinh: duLieu.syllabusChuongTrinh.map((item) => ({ ...item, tenVersion: layTen(versionMap, item.chuongTrinhVersionId, ['tenVersion']), tenSyllabusChuongTrinhGoc: layTen(syllabusChuongTrinhGocMap, item.syllabusChuongTrinhGocId, ['ma', 'ten']) })),
        nhomKienThucGoc: duLieu.nhomKienThucGoc.map((item) => ({ ...item })),
        nhomKienThuc: duLieu.nhomKienThuc.map((item) => ({ ...item, tenVersion: layTen(versionMap, item.chuongTrinhVersionId, ['tenVersion']), tenNhomKienThucGoc: layTen(nhomKienThucGocMap, item.nhomKienThucGocId, ['ma', 'ten']) })),
        nhomTuChonGoc: duLieu.nhomTuChonGoc.map((item) => ({ ...item })),
        nhomTuChon: duLieu.nhomTuChon.map((item) => ({ ...item, tenVersion: layTen(versionMap, item.chuongTrinhVersionId, ['tenVersion']), tenNhomTuChonGoc: layTen(nhomTuChonGocMap, item.nhomTuChonGocId, ['ma', 'ten']) })),
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
        quyDoiDiem: duLieu.quyDoiDiem.map((item) => ({ ...item, tenChuongTrinhMon: layTen(chuongTrinhMonMap, item.chuongTrinhMonId, ['maMonTrongCt', 'tenMon']) })),
        quyDoiDiemMau: duLieu.quyDoiDiemMau.map((item) => ({ ...item })),
        chuongTrinhMonQuyDoiDiemMau: duLieu.chuongTrinhMonQuyDoiDiemMau.map((item) => ({
            ...item,
            tenChuongTrinhMon: layTen(chuongTrinhMonMap, item.chuongTrinhMonId, ['maMonTrongCt', 'tenMon']),
            tenQuyDoiDiemMau: layTen(quyDoiDiemMauMap, item.quyDoiDiemMauId, ['ten', 'ma'])
        })),
        syllabusMonHoc: duLieu.syllabusMonHoc.map((item) => ({ ...item, tenChuongTrinhMon: layTen(chuongTrinhMonMap, item.chuongTrinhMonId, ['maMonTrongCt', 'tenMon']), tenSyllabusMonHocGoc: layTen(syllabusMonHocGocMap, item.syllabusMonHocGocId, ['ma', 'ten']) })),
        syllabusMonHocGoc: duLieu.syllabusMonHocGoc.map((item) => ({ ...item, tenMonHoc: layTen(monHocMap, item.monHocId, ['maMon', 'tenMon']) })),
        syllabusMonHocGocChuongBai: duLieu.syllabusMonHocGocChuongBai.map((item) => ({ ...item, tenSyllabusMonHocGoc: layTen(syllabusMonHocGocMap, item.syllabusMonHocGocId, ['ma', 'ten']) })),
        syllabusMonHocGocDieuKien: duLieu.syllabusMonHocGocDieuKien.map((item) => ({ ...item, tenSyllabusMonHocGoc: layTen(syllabusMonHocGocMap, item.syllabusMonHocGocId, ['ma', 'ten']), tenDieuKienGoc: layTen(dieuKienMonHocGocMap, item.dieuKienGocId, ['ma', 'noiDung']) })),
        syllabusMonHocGocTaiLieu: duLieu.syllabusMonHocGocTaiLieu.map((item) => ({ ...item, tenSyllabusMonHocGoc: layTen(syllabusMonHocGocMap, item.syllabusMonHocGocId, ['ma', 'ten']), tenTaiLieuGoc: layTen(taiLieuGocMap, item.taiLieuGocId, ['ma', 'ten']) })),
        dieuKienMonHoc: duLieu.dieuKienMonHoc.map((item) => ({ ...item, tenSyllabusMon: layTen(syllabusMonMap, item.syllabusMonId, ['viTri', 'tinhChat', 'mucTieu']) })),
        dieuKienMonHocGoc: duLieu.dieuKienMonHocGoc.map((item) => ({ ...item })),
        syllabusMonHocDieuKien: duLieu.syllabusMonHocDieuKien.map((item) => ({
            ...item,
            tenSyllabusMon: layTen(syllabusMonMap, item.syllabusMonId, ['viTri', 'tinhChat', 'mucTieu']),
            tenDieuKienGoc: layTen(dieuKienMonHocGocMap, item.dieuKienGocId, ['ma', 'noiDung'])
        })),
        taiLieuGoc: duLieu.taiLieuGoc.map((item) => ({ ...item })),
        syllabusMonHocTaiLieu: duLieu.syllabusMonHocTaiLieu.map((item) => ({
            ...item,
            tenSyllabusMon: layTen(syllabusMonMap, item.syllabusMonId, ['viTri', 'tinhChat', 'mucTieu']),
            tenTaiLieuGoc: layTen(taiLieuGocMap, item.taiLieuGocId, ['ma', 'ten'])
        })),
        syllabusChuongBai: duLieu.syllabusChuongBai.map((item) => ({ ...item, tenSyllabusMon: layTen(syllabusMonMap, item.syllabusMonId, ['viTri', 'tinhChat', 'mucTieu']) })),
        syllabusTaiLieu: duLieu.syllabusTaiLieu.map((item) => ({ ...item, tenSyllabusMon: layTen(syllabusMonMap, item.syllabusMonId, ['viTri', 'tinhChat', 'mucTieu']) }))
    }
}

function taoDuLieuRong() {
    return {
        nganh: [],
        trinhDoDaoTao: [],
        loaiChuongTrinh: [],
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
        syllabusMonHoc: null,
        syllabusMonHocGoc: null,
        dieuKienMonHocGoc: null,
        taiLieuGoc: null
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
export function useXayDungChuongTrinh() {
    const thongBao = ref('')
    const loaiThongBao = ref('success')
    const errorMessage = ref('')

    const selected = reactive(taoSelectedRong())
    const viewed = reactive(taoSelectedRong())
    const rawData = reactive(taoDuLieuRong())
    const tableMessages = reactive({})

    const duLieu = computed(() => themTenLienKet(rawData))

    const lookups = computed(() => ({
        ...duLieu.value,
        ...luaChonEnum
    }))

    function baoTinBang(key, message, type = 'success') {
        if (key) {
            tableMessages[key] = { message, type, at: Date.now() }
            setTimeout(() => {
                if (tableMessages[key]?.message === message) delete tableMessages[key]
            }, 5000)
        }
        baoTin(message, type)
    }

    function baoTin(message, type = 'success') {
        thongBao.value = message
        loaiThongBao.value = type
        if (type === 'error') errorMessage.value = message
        if (type !== 'error') errorMessage.value = ''

        setTimeout(() => {
            if (thongBao.value === message) thongBao.value = ''
        }, 5000)
    }


    async function taiDuLieuCoSanTatCaBang() {
        for (const key of bangLayDuLieuCoSan) {
            const service = xayDungChuongTrinhService[key]
            if (!service?.getAll) continue

            try {
                const result = await service.getAll({ size:200 })
                rawData[key] = layDanhSachTuKetQua(result)
            } catch (error) {
                console.warn(`Không tải được dữ liệu có sẵn của bảng ${key}`, error)
            }
        }
    }

    onMounted(() => {
        taiDuLieuCoSanTatCaBang()
    })

    const selectionChildren = {
        nganh: ['chuongTrinh', 'chuongTrinhVersion', 'khungKy', 'nhomKienThuc', 'nhomTuChon', 'monHoc', 'chuongTrinhMon', 'syllabusMonHoc'],
        trinhDoDaoTao: ['chuongTrinh', 'chuongTrinhVersion', 'khungKy', 'nhomKienThuc', 'nhomTuChon', 'monHoc', 'chuongTrinhMon', 'syllabusMonHoc'],
        loaiChuongTrinh: ['chuongTrinh', 'chuongTrinhVersion', 'khungKy', 'nhomKienThuc', 'nhomTuChon', 'monHoc', 'chuongTrinhMon', 'syllabusMonHoc'],
        chuongTrinh: ['chuongTrinhVersion', 'khungKy', 'nhomKienThuc', 'nhomTuChon', 'monHoc', 'chuongTrinhMon', 'syllabusMonHoc'],
        chuongTrinhVersion: ['syllabusChuongTrinhGoc', 'mucTieuChuongTrinhGoc', 'mucTieuChuongTrinh', 'nangLucDauRaGoc', 'nangLucDauRa', 'viTriViecLamGoc', 'viTriViecLam', 'dieuKienTotNghiepGoc', 'dieuKienTotNghiep', 'khungKyGoc', 'khungKy', 'nhomKienThucGoc', 'nhomKienThuc', 'nhomTuChonGoc', 'nhomTuChon', 'monHoc', 'chuongTrinhMon', 'syllabusMonHoc'],
        khungKyGoc: ['khungKy', 'chuongTrinhMon'],
        khungKy: ['chuongTrinhMon', 'syllabusMonHoc'],
        nhomKienThucGoc: ['nhomKienThuc', 'chuongTrinhMon'],
        nhomKienThuc: ['chuongTrinhMon', 'syllabusMonHoc'],
        nhomTuChonGoc: ['nhomTuChon'],
        nhomTuChon: [],
        monHoc: ['syllabusMonHocGoc', 'chuongTrinhMon', 'syllabusMonHoc'],
        chuongTrinhMon: ['quyDoiDiemMau', 'syllabusMonHoc'],
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
        mucTieuChuongTrinhGoc: {
            joinKey: 'chuongTrinhVersionMucTieu',
            gocIdKey: 'mucTieuGocId',
            serviceKey: 'chuongTrinhVersionMucTieu',
            tenBang: 'Mục tiêu chương trình'
        },
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
        dieuKienTotNghiepGoc: {
            joinKey: 'chuongTrinhVersionDieuKienTotNghiep',
            gocIdKey: 'dieuKienGocId',
            serviceKey: 'chuongTrinhVersionDieuKienTotNghiep',
            tenBang: 'Điều kiện tốt nghiệp'
        }
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

    function timQuyDoiDiemDaCoTuMau(item, chuongTrinhMonId) {
        const payload = taoPayloadQuyDoiDiemTuMau(item, chuongTrinhMonId)
        const list = rawData.quyDoiDiem || []
        return list.find((row) => {
            return String(row.chuongTrinhMonId || '') === String(chuongTrinhMonId || '')
                && String(chuanHoaGiaTriQuyDoi(row.nguongTu)) === String(chuanHoaGiaTriQuyDoi(payload.nguongTu))
                && String(chuanHoaGiaTriQuyDoi(row.nguongDen)) === String(chuanHoaGiaTriQuyDoi(payload.nguongDen))
                && String(chuanHoaGiaTriQuyDoi(row.diemQuyDoi)) === String(chuanHoaGiaTriQuyDoi(payload.diemQuyDoi))
                && String(row.ketQua || 'dat') === String(payload.ketQua || 'dat')
                && String(row.congThuc || '') === String(payload.congThuc || '')
        })
    }

    async function dayQuyDoiDiemMauXuongQuyDoiDiem(item, chuongTrinhMonId) {
        if (!item?.id || !chuongTrinhMonId) return { created: false, existed: false }

        const serviceQuyDoiDiem = xayDungChuongTrinhService.quyDoiDiem
        if (!serviceQuyDoiDiem?.create) {
            throw new Error('Chưa khai báo API tạo Quy đổi điểm.')
        }

        const existing = timQuyDoiDiemDaCoTuMau(item, chuongTrinhMonId)
        if (existing?.id) {
            capNhatDongTrongRawData('quyDoiDiem', existing)
            return { created: false, existed: true, row: existing }
        }

        const payload = taoPayloadQuyDoiDiemTuMau(item, chuongTrinhMonId)
        const saved = await serviceQuyDoiDiem.create(payload)
        const row = layDuLieuLuu(saved) || payload
        capNhatDongTrongRawData('quyDoiDiem', row)
        return { created: true, existed: false, row }
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

        const service = xayDungChuongTrinhService[config.serviceKey]
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

        const config = bangGocGanVersion[key]

        if (config) {
            const chuongTrinhVersionId = selected.chuongTrinhVersion?.id || parentValues.chuongTrinhVersionId
            if (!chuongTrinhVersionId) {
                baoTinBang(key, `Cần chọn Version trước khi lưu ${config.tenBang}.`, 'error')
                return
            }

            const existing = timDongNoiBangGoc(key, item, chuongTrinhVersionId)
            const serviceNoi = xayDungChuongTrinhService[config.serviceKey]

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
            const serviceNoi = xayDungChuongTrinhService[configMau.serviceKey]

            if (!serviceNoi) {
                baoTinBang(key, `Chưa khai báo API bảng nối cho ${configMau.tenBang}.`, 'error')
                return
            }

            try {
                if (existing?.id) {
                    await serviceNoi.delete(existing.id)
                    xoaDongTrongRawData(configMau.joinKey, existing.id)
                    if (selected[key]?.id === item.id) selectEntity(key, null)
                    baoTinBang(key, `Đã bỏ lưu ${configMau.tenBang} khỏi Môn trong chương trình hiện tại.`)
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
                const ketQuaDayMau = await dayQuyDoiDiemMauXuongQuyDoiDiem(item, chuongTrinhMonId)
                selectEntity(key, item)
                if (ketQuaDayMau.created) {
                    baoTinBang(key, `Đã lưu ${configMau.tenBang} và đẩy dữ liệu mẫu xuống bảng 5.5 Quy đổi điểm.`)
                } else if (ketQuaDayMau.existed) {
                    baoTinBang(key, `Đã lưu ${configMau.tenBang}. Bảng 5.5 Quy đổi điểm đã có dòng tương ứng nên không tạo trùng.`)
                } else {
                    baoTinBang(key, `Đã lưu ${configMau.tenBang} vào Môn trong chương trình hiện tại.`)
                }
            } catch (error) {
                const message = layThongBaoLoi(error, `Không lưu/bỏ lưu được ${configMau.tenBang}.`)
                baoTinBang(key, message, 'error')
            }

            return
        }

        const configSyllabus = bangGocGanSyllabus[key]
        if (configSyllabus) {
            const syllabusMonId = selected.syllabusMonHoc?.id || parentValues.syllabusMonId
            if (!syllabusMonId) {
                baoTinBang(key, `Cần chọn Syllabus môn học trước khi lưu ${configSyllabus.tenBang}.`, 'error')
                return
            }

            const existing = timDongNoiSyllabus(configSyllabus, item, syllabusMonId)
            const serviceNoi = xayDungChuongTrinhService[configSyllabus.serviceKey]

            if (!serviceNoi) {
                baoTinBang(key, `Chưa khai báo API bảng nối cho ${configSyllabus.tenBang}.`, 'error')
                return
            }

            try {
                if (existing?.id) {
                    await serviceNoi.delete(existing.id)
                    xoaDongTrongRawData(configSyllabus.joinKey, existing.id)
                    if (selected[key]?.id === item.id) selectEntity(key, null)
                    baoTinBang(key, `Đã bỏ lưu ${configSyllabus.tenBang} khỏi Syllabus môn hiện tại.`)
                    return
                }

                const payload = {
                    [configSyllabus.syllabusIdKey]: syllabusMonId,
                    [configSyllabus.gocIdKey]: item.id,
                    thuTu: layThuTuGanSyllabusTiepTheo(configSyllabus.joinKey, syllabusMonId),
                    ghiChu: item.ghiChu || ''
                }

                const saved = await serviceNoi.create(payload)
                const row = layDuLieuLuu(saved) || payload
                capNhatDongTrongRawData(configSyllabus.joinKey, row)
                selectEntity(key, item)
                baoTinBang(key, `Đã lưu ${configSyllabus.tenBang} vào Syllabus môn hiện tại.`)
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
            const serviceNoi = xayDungChuongTrinhService[configMau.serviceKey]

            if (!serviceNoi) {
                baoTinBang(key, `Chưa khai báo API bảng nối cho ${configMau.tenBang}.`, 'error')
                return
            }

            try {
                if (existing?.id) {
                    await serviceNoi.delete(existing.id)
                    xoaDongTrongRawData(configMau.joinKey, existing.id)
                    if (selected[key]?.id === item.id) selectEntity(key, null)
                    baoTinBang(key, `Đã bỏ lưu ${configMau.tenBang} khỏi Môn trong chương trình hiện tại.`)
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
                const ketQuaDayMau = await dayQuyDoiDiemMauXuongQuyDoiDiem(item, chuongTrinhMonId)
                selectEntity(key, item)
                if (ketQuaDayMau.created) {
                    baoTinBang(key, `Đã lưu ${configMau.tenBang} và đẩy dữ liệu mẫu xuống bảng 5.5 Quy đổi điểm.`)
                } else if (ketQuaDayMau.existed) {
                    baoTinBang(key, `Đã lưu ${configMau.tenBang}. Bảng 5.5 Quy đổi điểm đã có dòng tương ứng nên không tạo trùng.`)
                } else {
                    baoTinBang(key, `Đã lưu ${configMau.tenBang} vào Môn trong chương trình hiện tại.`)
                }
            } catch (error) {
                const message = layThongBaoLoi(error, `Không lưu/bỏ lưu được ${configMau.tenBang}.`)
                baoTinBang(key, message, 'error')
            }

            return
        }

        const configSyllabus = bangGocGanSyllabus[key]
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
            const row = layDuLieuLuu(saved) || { ...item, ...parentValues }
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
            await xayDungChuongTrinhService.nganh.delete(nganhDangNhap.id)
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
        errorMessage,
        tableMessages,
        selected,
        viewed,
        duLieu,
        lookups,
        services: xayDungChuongTrinhService,
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
