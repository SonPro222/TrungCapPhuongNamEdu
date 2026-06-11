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
    nganhHeDaoTao: createCrudService(daoTaoApi.nganhHeDaoTao),
    khungKy: {
        ...createCrudService(daoTaoApi.khungKy),
        async goiYTheoVersion(versionId) {
            return await daoTaoApi.khungKy.goiYTheoVersion(versionId)
        },
        async taoDuKyConThieu(versionId) {
            return await daoTaoApi.khungKy.taoDuKyConThieu(versionId)
        }
    },
    khungKymau: createCrudService(daoTaoApi.khungKymau),

    chuongTrinh: {
        ...createCrudService(daoTaoApi.chuongTrinh),

        async getTongThe(chuongTrinhId, versionId = null, options = {}) {
            if (chuongTrinhId === null || chuongTrinhId === undefined || chuongTrinhId === '') {
                throw new Error('Thiếu chuongTrinhId để xem tổng thể chương trình đào tạo')
            }

            return await daoTaoApi.chuongTrinh.getTongThe(chuongTrinhId, versionId, options)
        }
    },
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
    cauHinhDanhGia: createCrudService(daoTaoApi.cauHinhDanhGia),

    mucTieuChuongTrinh: createCrudService(daoTaoApi.mucTieuChuongTrinh),
    nangLucDauRa: createCrudService(daoTaoApi.nangLucDauRa),
    viTriViecLam: createCrudService(daoTaoApi.viTriViecLam),
    dieuKienTotNghiep: createCrudService(daoTaoApi.dieuKienTotNghiep),

    mucTieuChuongTrinhmau: createCrudService(daoTaoApi.mucTieuChuongTrinhmau),
    nangLucDauRamau: createCrudService(daoTaoApi.nangLucDauRamau),
    viTriViecLammau: createCrudService(daoTaoApi.viTriViecLammau),
    dieuKienTotNghiepmau: createCrudService(daoTaoApi.dieuKienTotNghiepmau),

    chuongTrinhVersionMucTieu: createCrudService(daoTaoApi.chuongTrinhVersionMucTieu),
    chuongTrinhVersionNangLuc: createCrudService(daoTaoApi.chuongTrinhVersionNangLuc),
    chuongTrinhVersionViTriViecLam: createCrudService(daoTaoApi.chuongTrinhVersionViTriViecLam),
    chuongTrinhVersionDieuKienTotNghiep: createCrudService(daoTaoApi.chuongTrinhVersionDieuKienTotNghiep),

    syllabusChuongTrinh: {
        ...createCrudService(daoTaoApi.syllabusChuongTrinh),

        async dongBoTuMau(payload) {
            return await daoTaoApi.syllabusChuongTrinh.dongBoTuMau(cleanPayload(payload))
        }
    },
    syllabusChuongTrinhmau: createCrudService(daoTaoApi.syllabusChuongTrinhmau),
    nhomKienThucmau: createCrudService(daoTaoApi.nhomKienThucmau),
    nhomTuChonmau: createCrudService(daoTaoApi.nhomTuChonmau),

    syllabusMonHoc: {
        ...createCrudService(daoTaoApi.syllabusMonHoc),

        async getChiTietXem(id) {
            if (id === null || id === undefined || id === '') {
                throw new Error('Thiếu syllabusMonHocId để xem chi tiết syllabus')
            }

            return await daoTaoApi.syllabusMonHoc.getChiTietXem(id)
        }
    },
    syllabusMonHocmau: createCrudService(daoTaoApi.syllabusMonHocmau),
    syllabusMonHocmauChuongBai: createCrudService(daoTaoApi.syllabusMonHocmauChuongBai),
    syllabusMonHocmauDieuKien: createCrudService(daoTaoApi.syllabusMonHocmauDieuKien),
    syllabusMonHocmauTaiLieu: createCrudService(daoTaoApi.syllabusMonHocmauTaiLieu),

    dieuKienMonHoc: createCrudService(daoTaoApi.dieuKienMonHoc),
    dieuKienMonHocmau: createCrudService(daoTaoApi.dieuKienMonHocmau),
    syllabusMonHocDieuKien: createCrudService(daoTaoApi.syllabusMonHocDieuKien),

    taiLieumau: createCrudService(daoTaoApi.taiLieumau),
    syllabusMonHocTaiLieu: createCrudService(daoTaoApi.syllabusMonHocTaiLieu),
    syllabusChuongBai: createCrudService(daoTaoApi.syllabusChuongBai),
    syllabusTaiLieu: createCrudService(daoTaoApi.syllabusTaiLieu)
}

