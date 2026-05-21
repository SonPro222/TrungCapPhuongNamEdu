import { chuongTrinhApi } from '../api/chuongTrinhApi'

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
        if (value === '') {
            out[key] = null
            return
        }
        out[key] = value
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

        async update(id, payload) {
            return await api.update(id, cleanPayload(payload))
        },

        async delete(id) {
            return await api.delete(id)
        }
    }
}

export const chuongTrinhService = {
    chuongTrinh: createCrudService(chuongTrinhApi.chuongTrinh),
    chuongTrinhVersion: createCrudService(chuongTrinhApi.chuongTrinhVersion),
    chuongTrinhMon: createCrudService(chuongTrinhApi.chuongTrinhMon),

    monHoc: createCrudService(chuongTrinhApi.monHoc),
    nhomKienThuc: createCrudService(chuongTrinhApi.nhomKienThuc),
    nhomTuChon: createCrudService(chuongTrinhApi.nhomTuChon),
    monTuChon: createCrudService(chuongTrinhApi.monTuChon),
    monTienQuyet: createCrudService(chuongTrinhApi.monTienQuyet),
    quyDoiDiem: createCrudService(chuongTrinhApi.quyDoiDiem),
    quyDoiDiemMau: createCrudService(chuongTrinhApi.quyDoiDiemMau),
    chuongTrinhMonQuyDoiDiemMau: createCrudService(chuongTrinhApi.chuongTrinhMonQuyDoiDiemMau),

    mucTieuChuongTrinh: createCrudService(chuongTrinhApi.mucTieuChuongTrinh),
    nangLucDauRa: createCrudService(chuongTrinhApi.nangLucDauRa),
    viTriViecLam: createCrudService(chuongTrinhApi.viTriViecLam),
    dieuKienTotNghiep: createCrudService(chuongTrinhApi.dieuKienTotNghiep),

    mucTieuChuongTrinhGoc: createCrudService(chuongTrinhApi.mucTieuChuongTrinhGoc),
    nangLucDauRaGoc: createCrudService(chuongTrinhApi.nangLucDauRaGoc),
    viTriViecLamGoc: createCrudService(chuongTrinhApi.viTriViecLamGoc),
    dieuKienTotNghiepGoc: createCrudService(chuongTrinhApi.dieuKienTotNghiepGoc),

    chuongTrinhVersionMucTieu: createCrudService(chuongTrinhApi.chuongTrinhVersionMucTieu),
    chuongTrinhVersionNangLuc: createCrudService(chuongTrinhApi.chuongTrinhVersionNangLuc),
    chuongTrinhVersionViTriViecLam: createCrudService(chuongTrinhApi.chuongTrinhVersionViTriViecLam),
    chuongTrinhVersionDieuKienTotNghiep: createCrudService(chuongTrinhApi.chuongTrinhVersionDieuKienTotNghiep),

    syllabusChuongTrinh: createCrudService(chuongTrinhApi.syllabusChuongTrinh),
    syllabusMonHoc: createCrudService(chuongTrinhApi.syllabusMonHoc),
    dieuKienMonHoc: createCrudService(chuongTrinhApi.dieuKienMonHoc),
    syllabusChuongBai: createCrudService(chuongTrinhApi.syllabusChuongBai),
    syllabusTaiLieu: createCrudService(chuongTrinhApi.syllabusTaiLieu)
}
