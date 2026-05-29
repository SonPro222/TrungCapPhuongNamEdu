import { daoTaoApi } from '../api/daoTaoApi'

function layDataTuApiResponse(res) {
    if (res?.success !== undefined && res?.data !== undefined) return res.data
    if (res?.data?.success !== undefined && res?.data?.data !== undefined) return res.data.data
    return res
}

export function normalizePage(res) {
    const data = layDataTuApiResponse(res)

    if (Array.isArray(data)) {
        return {
            items: data,
            totalElements: data.length,
            totalPages: 1,
            page: 0,
            size: data.length
        }
    }

    if (Array.isArray(data?.content)) {
        return {
            items: data.content,
            totalElements: data.totalElements || data.content.length || 0,
            totalPages: data.totalPages || 0,
            page: data.number || 0,
            size: data.size || data.content.length || 10
        }
    }

    if (Array.isArray(data?.items)) {
        return {
            items: data.items,
            totalElements: data.totalElements || data.items.length || 0,
            totalPages: data.totalPages || 1,
            page: data.page || 0,
            size: data.size || data.items.length || 10
        }
    }

    return {
        items: [],
        totalElements: 0,
        totalPages: 0,
        page: 0,
        size: 10
    }
}

function cleanPayload(payload = {}) {
    const out = {}

    Object.entries(payload).forEach(([key, value]) => {
        out[key] = value === '' ? null : value
    })

    return out
}

function createCrudService(api) {
    return {
        async getAll(params = {}) {
            const res = await api.getAll(params)
            return normalizePage(res)
        },

        async getById(id) {
            return await api.getById(id)
        },

        async create(payload) {
            return await api.create(cleanPayload(payload))
        },

        async luu(payload) {
            return await api.luu(cleanPayload(payload))
        },

        async update(id, payload) {
            return await api.update(id, cleanPayload(payload))
        },

        async delete(id) {
            return await api.delete(id)
        }
    }
}

export const daoTaoService = {
    nganh: createCrudService(daoTaoApi.nganh),
    trinhDoDaoTao: createCrudService(daoTaoApi.trinhDoDaoTao),
    loaiChuongTrinh: createCrudService(daoTaoApi.loaiChuongTrinh),
    nganhTrinhDoDaoTao: createCrudService(daoTaoApi.nganhTrinhDoDaoTao),
    nganhLoaiChuongTrinh: createCrudService(daoTaoApi.nganhLoaiChuongTrinh),
    khungKy: createCrudService(daoTaoApi.khungKy),
    khungKyGoc: createCrudService(daoTaoApi.khungKyGoc),

    chuongTrinh: createCrudService(daoTaoApi.chuongTrinh),
    chuongTrinhVersion: createCrudService(daoTaoApi.chuongTrinhVersion),
    chuongTrinhMon: createCrudService(daoTaoApi.chuongTrinhMon),

    monHoc: createCrudService(daoTaoApi.monHoc),
    nhomKienThuc: createCrudService(daoTaoApi.nhomKienThuc),
    nhomTuChon: createCrudService(daoTaoApi.nhomTuChon),
    monTuChon: createCrudService(daoTaoApi.monTuChon),
    monTienQuyet: createCrudService(daoTaoApi.monTienQuyet),
    quyDoiDiem: createCrudService(daoTaoApi.quyDoiDiem),
    quyDoiDiemMau: createCrudService(daoTaoApi.quyDoiDiemMau),
    chuongTrinhMonQuyDoiDiemMau: createCrudService(daoTaoApi.chuongTrinhMonQuyDoiDiemMau),
    cauHinhDanhGiaMau: createCrudService(daoTaoApi.cauHinhDanhGiaMau),

    mucTieuChuongTrinh: createCrudService(daoTaoApi.mucTieuChuongTrinh),
    nangLucDauRa: createCrudService(daoTaoApi.nangLucDauRa),
    viTriViecLam: createCrudService(daoTaoApi.viTriViecLam),
    dieuKienTotNghiep: createCrudService(daoTaoApi.dieuKienTotNghiep),

    mucTieuChuongTrinhGoc: createCrudService(daoTaoApi.mucTieuChuongTrinhGoc),
    nangLucDauRaGoc: createCrudService(daoTaoApi.nangLucDauRaGoc),
    viTriViecLamGoc: createCrudService(daoTaoApi.viTriViecLamGoc),
    dieuKienTotNghiepGoc: createCrudService(daoTaoApi.dieuKienTotNghiepGoc),

    chuongTrinhVersionMucTieu: createCrudService(daoTaoApi.chuongTrinhVersionMucTieu),
    chuongTrinhVersionNangLuc: createCrudService(daoTaoApi.chuongTrinhVersionNangLuc),
    chuongTrinhVersionViTriViecLam: createCrudService(daoTaoApi.chuongTrinhVersionViTriViecLam),
    chuongTrinhVersionDieuKienTotNghiep: createCrudService(daoTaoApi.chuongTrinhVersionDieuKienTotNghiep),

    syllabusChuongTrinh: createCrudService(daoTaoApi.syllabusChuongTrinh),
    syllabusChuongTrinhGoc: createCrudService(daoTaoApi.syllabusChuongTrinhGoc),
    nhomKienThucGoc: createCrudService(daoTaoApi.nhomKienThucGoc),
    nhomTuChonGoc: createCrudService(daoTaoApi.nhomTuChonGoc),

    syllabusMonHoc: createCrudService(daoTaoApi.syllabusMonHoc),
    syllabusMonHocGoc: createCrudService(daoTaoApi.syllabusMonHocGoc),
    syllabusMonHocGocChuongBai: createCrudService(daoTaoApi.syllabusMonHocGocChuongBai),
    syllabusMonHocGocDieuKien: createCrudService(daoTaoApi.syllabusMonHocGocDieuKien),
    syllabusMonHocGocTaiLieu: createCrudService(daoTaoApi.syllabusMonHocGocTaiLieu),

    dieuKienMonHoc: createCrudService(daoTaoApi.dieuKienMonHoc),
    dieuKienMonHocGoc: createCrudService(daoTaoApi.dieuKienMonHocGoc),
    syllabusMonHocDieuKien: createCrudService(daoTaoApi.syllabusMonHocDieuKien),

    taiLieuGoc: createCrudService(daoTaoApi.taiLieuGoc),
    syllabusMonHocTaiLieu: createCrudService(daoTaoApi.syllabusMonHocTaiLieu),
    syllabusChuongBai: createCrudService(daoTaoApi.syllabusChuongBai),
    syllabusTaiLieu: createCrudService(daoTaoApi.syllabusTaiLieu)
}

