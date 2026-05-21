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
    'nhomKienThuc',
    'nhomTuChon',
    'monHoc',
    'chuongTrinhMon',
    'monTuChon',
    'syllabusMonHoc',
    'mucTieuChuongTrinh',
    'nangLucDauRa',
    'viTriViecLam',
    'dieuKienTotNghiep',
    'chuongTrinhVersionMucTieu',
    'chuongTrinhVersionNangLuc',
    'chuongTrinhVersionViTriViecLam',
    'chuongTrinhVersionDieuKienTotNghiep',
    'syllabusChuongTrinh',
    'monTienQuyet',
    'quyDoiDiem',
    'dieuKienMonHoc',
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
    const nhomKienThucMap = taoBanDo(duLieu.nhomKienThuc)
    const nhomTuChonMap = taoBanDo(duLieu.nhomTuChon)
    const chuongTrinhMonMap = taoBanDo(duLieu.chuongTrinhMon)
    const syllabusMonMap = taoBanDo(duLieu.syllabusMonHoc)

    return {
        ...duLieu,
        chuongTrinh: duLieu.chuongTrinh.map((item) => ({
            ...item,
            tenNganh: layTen(nganhMap, item.nganhId, ['tenNganh']),
            tenTrinhDo: layTen(trinhDoMap, item.trinhDoId, ['tenTrinhDo']),
            tenLoaiChuongTrinh: layTen(loaiCtMap, item.loaiChuongTrinhId, ['tenLoai'])
        })),
        chuongTrinhVersion: duLieu.chuongTrinhVersion.map((item) => ({
            ...item,
            tenChuongTrinh: layTen(chuongTrinhMap, item.chuongTrinhId, ['tenChuongTrinh'])
        })),
        khungKy: duLieu.khungKy.map((item) => ({
            ...item,
            tenVersion: layTen(versionMap, item.chuongTrinhVersionId, ['tenVersion']),
            tenLoaiChuongTrinh: layTen(loaiCtMap, item.loaiChuongTrinhId, ['tenLoai'])
        })),
        mucTieuChuongTrinh: duLieu.mucTieuChuongTrinh.map((item) => ({ ...item, tenVersion: layTen(versionMap, item.chuongTrinhVersionId, ['tenVersion']) })),
        nangLucDauRa: duLieu.nangLucDauRa.map((item) => ({ ...item, tenVersion: layTen(versionMap, item.chuongTrinhVersionId, ['tenVersion']) })),
        viTriViecLam: duLieu.viTriViecLam.map((item) => ({ ...item, tenVersion: layTen(versionMap, item.chuongTrinhVersionId, ['tenVersion']) })),
        dieuKienTotNghiep: duLieu.dieuKienTotNghiep.map((item) => ({ ...item, tenVersion: layTen(versionMap, item.chuongTrinhVersionId, ['tenVersion']) })),
        syllabusChuongTrinh: duLieu.syllabusChuongTrinh.map((item) => ({ ...item, tenVersion: layTen(versionMap, item.chuongTrinhVersionId, ['tenVersion']) })),
        nhomKienThuc: duLieu.nhomKienThuc.map((item) => ({ ...item, tenVersion: layTen(versionMap, item.chuongTrinhVersionId, ['tenVersion']) })),
        nhomTuChon: duLieu.nhomTuChon.map((item) => ({ ...item, tenVersion: layTen(versionMap, item.chuongTrinhVersionId, ['tenVersion']) })),
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
        syllabusMonHoc: duLieu.syllabusMonHoc.map((item) => ({ ...item, tenChuongTrinhMon: layTen(chuongTrinhMonMap, item.chuongTrinhMonId, ['maMonTrongCt', 'tenMon']) })),
        dieuKienMonHoc: duLieu.dieuKienMonHoc.map((item) => ({ ...item, tenSyllabusMon: layTen(syllabusMonMap, item.syllabusMonId, ['viTri', 'tinhChat', 'mucTieu']) })),
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
        monHoc: [],
        chuongTrinhMon: [],
        nhomKienThuc: [],
        nhomTuChon: [],
        monTuChon: [],
        mucTieuChuongTrinh: [],
        nangLucDauRa: [],
        viTriViecLam: [],
        dieuKienTotNghiep: [],
        chuongTrinhVersionMucTieu: [],
        chuongTrinhVersionNangLuc: [],
        chuongTrinhVersionViTriViecLam: [],
        chuongTrinhVersionDieuKienTotNghiep: [],
        syllabusChuongTrinh: [],
        monTienQuyet: [],
        quyDoiDiem: [],
        syllabusMonHoc: [],
        dieuKienMonHoc: [],
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
        mucTieuChuongTrinh: null,
        nangLucDauRa: null,
        viTriViecLam: null,
        dieuKienTotNghiep: null,
        khungKy: null,
        nhomKienThuc: null,
        nhomTuChon: null,
        monHoc: null,
        chuongTrinhMon: null,
        syllabusMonHoc: null
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

    const duLieu = computed(() => themTenLienKet(rawData))

    const lookups = computed(() => ({
        ...duLieu.value,
        ...luaChonEnum
    }))

    function baoTin(message, type = 'success') {
        thongBao.value = message
        loaiThongBao.value = type
        if (type === 'error') errorMessage.value = message
        if (type !== 'error') errorMessage.value = ''

        setTimeout(() => {
            if (thongBao.value === message) thongBao.value = ''
        }, 2800)
    }


    async function taiDuLieuCoSanTatCaBang() {
        for (const key of bangLayDuLieuCoSan) {
            const service = xayDungChuongTrinhService[key]
            if (!service?.getAll) continue

            try {
                const result = await service.getAll({ size: 1000 })
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
        chuongTrinhVersion: ['mucTieuChuongTrinh', 'nangLucDauRa', 'viTriViecLam', 'dieuKienTotNghiep', 'khungKy', 'nhomKienThuc', 'nhomTuChon', 'monHoc', 'chuongTrinhMon', 'syllabusMonHoc'],
        khungKy: ['chuongTrinhMon', 'syllabusMonHoc'],
        nhomKienThuc: ['chuongTrinhMon', 'syllabusMonHoc'],
        nhomTuChon: [],
        monHoc: ['chuongTrinhMon', 'syllabusMonHoc'],
        chuongTrinhMon: ['syllabusMonHoc'],
        syllabusMonHoc: []
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
        mucTieuChuongTrinh: {
            joinKey: 'chuongTrinhVersionMucTieu',
            gocIdKey: 'mucTieuGocId',
            serviceKey: 'chuongTrinhVersionMucTieu',
            tenBang: 'Mục tiêu chương trình'
        },
        nangLucDauRa: {
            joinKey: 'chuongTrinhVersionNangLuc',
            gocIdKey: 'nangLucGocId',
            serviceKey: 'chuongTrinhVersionNangLuc',
            tenBang: 'Năng lực đầu ra'
        },
        viTriViecLam: {
            joinKey: 'chuongTrinhVersionViTriViecLam',
            gocIdKey: 'viTriGocId',
            serviceKey: 'chuongTrinhVersionViTriViecLam',
            tenBang: 'Vị trí việc làm'
        },
        dieuKienTotNghiep: {
            joinKey: 'chuongTrinhVersionDieuKienTotNghiep',
            gocIdKey: 'dieuKienGocId',
            serviceKey: 'chuongTrinhVersionDieuKienTotNghiep',
            tenBang: 'Điều kiện tốt nghiệp'
        }
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
            baoTin(`Cần chọn Version trước khi gán ${config.tenBang}.`, 'error')
            return true
        }

        const existing = timDongNoiVersion(config, item, chuongTrinhVersionId)
        if (existing) {
            selectEntity(key, item)
            baoTin(`Đã chọn ${config.tenBang} đã được gán trong Version hiện tại.`)
            return true
        }

        const service = xayDungChuongTrinhService[config.serviceKey]
        if (!service?.create) {
            baoTin(`Chưa khai báo service bảng nối cho ${config.tenBang}.`, 'error')
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
            baoTin(`Đã gán ${config.tenBang} vào Version qua bảng nối.`)
        } catch (error) {
            const message = layThongBaoLoi(error, `Không gán được ${config.tenBang} vào Version.`)
            baoTin(message, 'error')
        }

        return true
    }

    const bangCoNutLuuLienKet = new Set([
        'chuongTrinhMon',
        'mucTieuChuongTrinh',
        'nangLucDauRa',
        'viTriViecLam',
        'dieuKienTotNghiep',
        'monTienQuyet',
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
        'dieuKienMonHoc',
        'syllabusChuongBai',
        'syllabusTaiLieu'
    ])

    async function toggleLuuBangPhu(key, item, parentValues = {}, service = null) {
        if (!item?.id) {
            baoTin('Không tìm thấy dòng dữ liệu để lưu hoặc bỏ lưu.', 'error')
            return
        }

        if (!bangCoNutLuuLienKet.has(key)) {
            baoTin('Bảng này chưa bật chức năng lưu/bỏ lưu liên kết.', 'error')
            return
        }

        const config = bangGocGanVersion[key]

        if (config) {
            const chuongTrinhVersionId = selected.chuongTrinhVersion?.id || parentValues.chuongTrinhVersionId
            if (!chuongTrinhVersionId) {
                baoTin(`Cần chọn Version trước khi lưu ${config.tenBang}.`, 'error')
                return
            }

            const existing = timDongNoiBangGoc(key, item, chuongTrinhVersionId)
            const serviceNoi = xayDungChuongTrinhService[config.serviceKey]

            if (!serviceNoi) {
                baoTin(`Chưa khai báo API bảng nối cho ${config.tenBang}.`, 'error')
                return
            }

            try {
                if (existing?.id) {
                    await serviceNoi.delete(existing.id)
                    xoaDongTrongRawData(config.joinKey, existing.id)
                    if (selected[key]?.id === item.id) selectEntity(key, null)
                    baoTin(`Đã bỏ lưu ${config.tenBang} khỏi Version hiện tại.`)
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
                baoTin(`Đã lưu ${config.tenBang} vào Version hiện tại.`)
            } catch (error) {
                const message = layThongBaoLoi(error, `Không lưu/bỏ lưu được ${config.tenBang}.`)
                baoTin(message, 'error')
            }

            return
        }

        const parentHopLe = Object.entries(parentValues || {}).filter(([, value]) => coGiaTri(value))
        if (!parentHopLe.length) {
            baoTin('Cần chọn dữ liệu cấp trên trước khi lưu liên kết.', 'error')
            return
        }

        if (!service?.update) {
            baoTin('Chưa khai báo API cập nhật cho bảng này.', 'error')
            return
        }

        const dangLuu = daLuuTrucTiep(item, parentValues)

        if (dangLuu && key === 'chuongTrinhMon') {
            baoTin('Chương trình môn là bảng xương sống nên không bỏ lưu bằng nút này. Nếu cần xóa khỏi chương trình, dùng nút Xóa.', 'error')
            return
        }

        if (dangLuu && bangXoaDongKhiBoLuu.has(key)) {
            if (!service?.delete) {
                baoTin('Chưa khai báo API xóa để bỏ lưu dữ liệu này.', 'error')
                return
            }

            try {
                await service.delete(item.id)
                xoaDongTrongRawData(key, item.id)
                if (selected[key]?.id === item.id) selectEntity(key, null)
                baoTin('Đã hủy lưu dữ liệu khỏi luồng hiện tại.')
            } catch (error) {
                const message = layThongBaoLoi(error, 'Không hủy lưu được dữ liệu. Kiểm tra ràng buộc BE.')
                baoTin(message, 'error')
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
                baoTin('Đã bỏ lưu dữ liệu khỏi luồng hiện tại.')
            } else {
                selectEntity(key, rowDaCapNhat)
                baoTin('Đã lưu dữ liệu vào luồng hiện tại.')
            }
        } catch (error) {
            const message = layThongBaoLoi(error, 'Không lưu/bỏ lưu được dữ liệu. Kiểm tra ràng buộc BE.')
            baoTin(message, 'error')
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
            baoTin(message, 'error')
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
