import { daoTaoApi } from '../api/daoTaoApi'

function normalizePage(res) {
    if (Array.isArray(res)) {
        return {
            items: res,
            totalElements: res.length,
            totalPages: 1,
            page: 0,
            size: res.length
        }
    }

    if (Array.isArray(res?.content)) {
        return {
            items: res.content,
            totalElements: res.totalElements || 0,
            totalPages: res.totalPages || 0,
            page: res.number || 0,
            size: res.size || 10
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
            return await api.create(payload)
        },

        async update(id, payload) {
            return await api.update(id, payload)
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
    khoaDaoTao: createCrudService(daoTaoApi.khoaDaoTao),
    khungKy: createCrudService(daoTaoApi.khungKy),
    lopHanhChinh: createCrudService(daoTaoApi.lopHanhChinh)
}