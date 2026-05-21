import { daoTaoApi } from '../api/daoTaoApi'
import { normalizePage } from '@/modules/chuongTrinh/services/chuongTrinhService'

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
    khungKy: createCrudService(daoTaoApi.khungKy),
    khoaDaoTao: createCrudService(daoTaoApi.khoaDaoTao),
    lopHanhChinh: createCrudService(daoTaoApi.lopHanhChinh)
}
