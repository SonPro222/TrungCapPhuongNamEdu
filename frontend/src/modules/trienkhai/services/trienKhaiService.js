import { trienKhaiApi } from '../api/trienKhaiApi'

function unwrap(res) {
    if (res?.success !== undefined && res?.data !== undefined) return res.data
    if (res?.data?.success !== undefined && res?.data?.data !== undefined) return res.data.data
    if (res?.data !== undefined) return res.data
    return res
}

export function normalizePage(res) {
    const data = unwrap(res)

    if (Array.isArray(data)) {
        return { items: data, totalElements: data.length, totalPages: 1, page: 0, size: data.length }
    }

    if (Array.isArray(data?.content)) {
        return {
            items: data.content,
            totalElements: data.totalElements ?? data.content.length,
            totalPages: data.totalPages ?? 1,
            page: data.number ?? 0,
            size: data.size ?? data.content.length
        }
    }

    if (Array.isArray(data?.items)) {
        return {
            items: data.items,
            totalElements: data.totalElements ?? data.items.length,
            totalPages: data.totalPages ?? 1,
            page: data.page ?? 0,
            size: data.size ?? data.items.length
        }
    }

    return { items: [], totalElements: 0, totalPages: 0, page: 0, size: 10 }
}

function cleanParams(params = {}) {
    const out = {}
    Object.entries(params).forEach(([key, value]) => {
        if (value !== '' && value !== null && value !== undefined) out[key] = value
    })
    return out
}

function createReadService(api) {
    return {
        async getAll(params = {}) {
            return normalizePage(await api.getAll(cleanParams(params)))
        }
    }
}

async function safeGetAll(service, params = {}) {
    try {
        return await service.getAll(params)
    } catch (error) {
        console.warn('[trienKhai] Không tải được dữ liệu', params, error)
        return normalizePage([])
    }
}

export const trienKhaiService = {
    nganh: createReadService(trienKhaiApi.nganh),
    nganhHeDaoTao: createReadService(trienKhaiApi.nganhHeDaoTao),
    chuongTrinh: createReadService(trienKhaiApi.chuongTrinh),
    chuongTrinhVersion: createReadService(trienKhaiApi.chuongTrinhVersion),
    chuongTrinhMon: createReadService(trienKhaiApi.chuongTrinhMon),
    lopHocPhan: createReadService(trienKhaiApi.lopHocPhan),
    sinhVienLopHocPhan: createReadService(trienKhaiApi.sinhVienLopHocPhan),
    phanCongGiangDay: createReadService(trienKhaiApi.phanCongGiangDay),
    lichHoc: createReadService(trienKhaiApi.lichHoc),
    ketQuaLopHocPhan: createReadService(trienKhaiApi.ketQuaLopHocPhan),

    async getKhungKyTheoVersion(chuongTrinhVersionId, params = {}) {
        return normalizePage(await trienKhaiApi.khungKy.getByVersion(chuongTrinhVersionId, cleanParams(params)))
    },

    async getVersionHienHanh(chuongTrinhId) {
        const hienHanh = await safeGetAll(this.chuongTrinhVersion, {
            chuongTrinhId,
            laHienHanh: true,
            page: 0,
            size: 10,
            sortBy: 'id',
            sortDir: 'desc'
        })

        if (hienHanh.items.length) return hienHanh.items[0]

        const tatCa = await safeGetAll(this.chuongTrinhVersion, {
            chuongTrinhId,
            page: 0,
            size: 100,
            sortBy: 'id',
            sortDir: 'desc'
        })

        return tatCa.items.find(item => item.laHienHanh || item.trangThai === 'DANG_AP_DUNG') || tatCa.items[0] || null
    },

    async getLopHocPhanTheoChuongTrinhMon(chuongTrinhMonList = [], extraParams = {}) {
        const ketQua = []

        for (const mon of chuongTrinhMonList) {
            if (!mon?.id) continue
            const page = await safeGetAll(this.lopHocPhan, {
                chuongTrinhMonId: mon.id,
                page: 0,
                size: 1000,
                sortBy: 'ngayBatDau',
                sortDir: 'asc',
                ...extraParams
            })

            page.items.forEach(lop => {
                ketQua.push({
                    ...lop,
                    khungKyId: mon.khungKyId,
                    tenKhungKy: mon.tenKhungKy || mon.tenKy || mon.tenHocKy,
                    chuongTrinhMon: mon
                })
            })
        }

        return ketQua
    }
}
