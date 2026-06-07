import { lmsApi, lmsCrudRegistry } from '../api/lmsApi'

export function unwrap(result) {
    return result?.data?.data ?? result?.data ?? result
}

export function toPage(data) {
    const d = unwrap(data)
    if (Array.isArray(d)) return { content: d, totalElements: d.length, totalPages: 1, number: 0 }
    if (Array.isArray(d?.content)) return d
    if (Array.isArray(d?.data)) return { content: d.data, totalElements: d.data.length, totalPages: 1, number: 0 }
    return { content: [], totalElements: 0, totalPages: 0, number: 0 }
}

export function asList(data) {
    return toPage(data).content || []
}

export async function apiPage(apiFn, fallback = []) {
    try {
        return toPage(await apiFn())
    } catch (error) {
        console.error('[LMS] API list error:', error)
        return { content: fallback, totalElements: fallback.length, totalPages: 1, number: 0 }
    }
}

export async function apiOne(apiFn, fallback = null) {
    try {
        return unwrap(await apiFn())
    } catch (error) {
        console.error('[LMS] API detail error:', error)
        return fallback
    }
}

export function byId(list, id) {
    if (id === undefined || id === null || id === '') return undefined
    return (list || []).find(x => String(x?.id) === String(id))
}

export function pick(obj, keys) {
    for (const k of keys) {
        if (obj?.[k] !== undefined && obj?.[k] !== null && obj?.[k] !== '') return obj[k]
    }
    return undefined
}

export function normalizeDateTimeInput(value) {
    if (!value) return null
    if (typeof value === 'string' && value.length === 16) return `${value}:00`
    return value
}

export function tenNganh(n) { return pick(n, ['tenNganh', 'ten', 'name']) || `Ngành #${n?.id ?? ''}` }
export function maNganh(n) { return pick(n, ['maNganh', 'ma', 'code']) || n?.id }
export function tenChuongTrinh(c) { return pick(c, ['tenChuongTrinh', 'ten', 'name']) || `Chương trình #${c?.id ?? ''}` }
export function maChuongTrinh(c) { return pick(c, ['maChuongTrinh', 'ma', 'code']) || c?.id }
export function tenMonHoc(m) { return pick(m, ['tenMonHoc', 'tenMon', 'ten', 'name']) || `Môn #${m?.id ?? ''}` }
export function maMonHoc(m) { return pick(m, ['maMonHoc', 'maMon', 'ma', 'code']) || m?.id }
export function tenLop(l) { return pick(l, ['tenLopHocPhan', 'tenLop', 'maLopHocPhan', 'maLop', 'ten', 'name']) || `Lớp #${l?.id ?? ''}` }
export function maLop(l) { return pick(l, ['maLopHocPhan', 'maLop', 'ma', 'code']) || l?.id }
export function tenChuDe(c) { return pick(c, ['tenChuDe', 'tenChuongBai', 'ten', 'name', 'tieuDe']) || `Chủ đề #${c?.id ?? ''}` }

let danhMucCache = null

export const lmsDanhMucService = {
    clearCache() { danhMucCache = null },
    async layNganh(params = {}) {
        const list = asList(await lmsApi.danhMuc.nganh.getAll({ size: 1000, ...params }))
        return list.map(n => ({ ...n, maNganh: maNganh(n), tenNganh: tenNganh(n) }))
    },
    async layChuongTrinh(nganhId) {
        const list = asList(await lmsApi.danhMuc.chuongTrinh.getAll({ size: 1000 }))
        return list
            .filter(c => !nganhId || String(c.nganhId) === String(nganhId))
            .map(c => ({ ...c, maChuongTrinh: maChuongTrinh(c), tenChuongTrinh: tenChuongTrinh(c) }))
    },
    async layVersion(chuongTrinhId) {
        const list = asList(await lmsApi.danhMuc.chuongTrinhVersion.getAll({ size: 1000 }))
        return list.filter(v => !chuongTrinhId || String(v.chuongTrinhId) === String(chuongTrinhId))
    },
    async layChuongTrinhMon(chuongTrinhVersionId) {
        const list = asList(await lmsApi.danhMuc.chuongTrinhMon.getAll({ size: 1000 }))
        return list.filter(m => !chuongTrinhVersionId || String(m.chuongTrinhVersionId) === String(chuongTrinhVersionId))
    },
    async layMonHocGoc() {
        const list = asList(await lmsApi.danhMuc.monHoc.getAll({ size: 1000 }))
        return list.map(m => ({ ...m, maMonHoc: maMonHoc(m), tenMonHoc: tenMonHoc(m) }))
    },
    async layMonHoc(chuongTrinhVersionId) {
        const [ctms, mons] = await Promise.all([this.layChuongTrinhMon(chuongTrinhVersionId), this.layMonHocGoc()])
        return ctms.map(ctm => {
            const mon = byId(mons, ctm.monHocId)
            return { ...ctm, id: ctm.id, chuongTrinhMonId: ctm.id, monHocId: ctm.monHocId, maMonHoc: maMonHoc(mon), tenMonHoc: tenMonHoc(mon) }
        })
    },
    async layChuDe(chuongTrinhMonId) {
        const list = asList(await lmsApi.danhMuc.syllabusChuongBai.getAll({ size: 1000 }))
        return list
            .filter(c => {
                if (!chuongTrinhMonId) return true
                return String(c.chuongTrinhMonId ?? c.syllabusMonHocId ?? c.syllabusMonId) === String(chuongTrinhMonId)
            })
            .map(c => ({ ...c, tenChuDe: tenChuDe(c) }))
    },
    async layLopHocPhan(params = {}) {
        const list = asList(await lmsApi.danhMuc.lopHocPhan.getAll({ size: 1000, ...params }))
        return list.map(l => ({ ...l, maLop: maLop(l), tenLopHocPhan: tenLop(l), tenLop: tenLop(l) }))
    },
    async layCauHinhDanhGia(lopHocPhanId) {
        const list = asList(await lmsApi.danhMuc.cauHinhDanhGia.getAll({ size: 1000 }))
        return list.filter(c => !lopHocPhanId || String(c.lopHocPhanId) === String(lopHocPhanId))
    },
    async laySinhVien(params = {}) {
        return asList(await lmsApi.danhMuc.sinhVien.getAll({ size: 1000, ...params }))
    },
}

async function loadDanhMucCache() {
    if (danhMucCache) return danhMucCache
    const [nganh, chuongTrinh, version, chuongTrinhMon, monHoc, chuDe, lopHocPhan, cauHinhDanhGia, sinhVien] = await Promise.all([
        lmsDanhMucService.layNganh(),
        lmsDanhMucService.layChuongTrinh(),
        lmsDanhMucService.layVersion(),
        lmsDanhMucService.layChuongTrinhMon(),
        lmsDanhMucService.layMonHocGoc(),
        lmsDanhMucService.layChuDe(),
        lmsDanhMucService.layLopHocPhan(),
        lmsDanhMucService.layCauHinhDanhGia(),
        lmsDanhMucService.laySinhVien().catch(() => []),
    ])
    danhMucCache = { nganh, chuongTrinh, version, chuongTrinhMon, monHoc, chuDe, lopHocPhan, cauHinhDanhGia, sinhVien }
    return danhMucCache
}

function enrichTheoCtm(item, dm, chuongTrinhMonId = item.chuongTrinhMonId) {
    const ctm = byId(dm.chuongTrinhMon, chuongTrinhMonId)
    const mon = byId(dm.monHoc, item.monHocId ?? ctm?.monHocId)
    const ver = byId(dm.version, item.chuongTrinhVersionId ?? ctm?.chuongTrinhVersionId)
    const ct = byId(dm.chuongTrinh, item.chuongTrinhId ?? ver?.chuongTrinhId)
    const nganh = byId(dm.nganh, item.nganhId ?? ct?.nganhId)
    return {
        ...item,
        chuongTrinhMonId: ctm?.id ?? chuongTrinhMonId,
        monHocId: mon?.id ?? item.monHocId,
        tenMonHoc: item.tenMonHoc || tenMonHoc(mon),
        maMonHoc: item.maMonHoc || maMonHoc(mon),
        tenChuongTrinh: item.tenChuongTrinh || tenChuongTrinh(ct),
        maChuongTrinh: item.maChuongTrinh || maChuongTrinh(ct),
        tenNganh: item.tenNganh || tenNganh(nganh),
        maNganh: item.maNganh || maNganh(nganh),
    }
}

function enrichCauHoiVersion(item, { dm, gocs = [], apDungs = [], dapAns = [], rubrics = [] }) {
    const goc = byId(gocs, item.cauHoiGocId) || item.cauHoiGoc || {}
    const mon = byId(dm.monHoc, goc.monHocId ?? item.monHocId)
    const apps = apDungs.filter(a => String(a.cauHoiVersionId) === String(item.id))
    const firstApp = apps[0] || {}
    const base = enrichTheoCtm({ ...item, ...goc, cauHoiVersionId: item.id }, dm, firstApp.chuongTrinhMonId)
    return {
        ...base,
        id: item.id,
        cauHoiVersionId: item.id,
        cauHoiGocId: item.cauHoiGocId,
        monHocId: mon?.id ?? goc.monHocId,
        tenMonHoc: item.tenMonHoc || tenMonHoc(mon),
        maMonHoc: item.maMonHoc || maMonHoc(mon),
        maCauHoi: goc.maCauHoi || `CH-${item.cauHoiGocId || item.id}`,
        trangThaiGoc: goc.trangThai,
        apDungs: apps,
        soVersionApDung: apps.length,
        dapAns: dapAns.filter(d => String(d.cauHoiVersionId) === String(item.id)).sort((a, b) => (a.thuTu || 0) - (b.thuTu || 0)),
        rubrics: rubrics.filter(d => String(d.cauHoiVersionId) === String(item.id)).sort((a, b) => (a.thuTu || 0) - (b.thuTu || 0)),
        cauHoiGoc: goc,
    }
}

function enrichBaiTapOnline(item, dm, cauHoiRows = []) {
    const base = enrichTheoCtm(item, dm, item.chuongTrinhMonId)
    return {
        ...base,
        tenBaiTap: item.tieuDe || item.tenBaiTap || `Bài tập #${item.id}`,
        soCauHoi: cauHoiRows.filter(x => String(x.baiTapOnlineId) === String(item.id)).length,
    }
}

function enrichBaiTapLop(item, dm, baiTapOnlineList = []) {
    const baiTap = byId(baiTapOnlineList, item.baiTapOnlineId) || item.baiTapOnline || {}
    const lhp = byId(dm.lopHocPhan, item.lopHocPhanId)
    return {
        ...item,
        baiTapOnline: baiTap,
        tenBaiTap: item.tenBaiTap || baiTap.tieuDe || baiTap.tenBaiTap || `Bài tập lớp #${item.id}`,
        tieuDe: item.tieuDe || baiTap.tieuDe,
        loaiBai: item.loaiBai || baiTap.loaiBai,
        tongDiem: item.tongDiem || baiTap.tongDiem,
        tenMonHoc: item.tenMonHoc || baiTap.tenMonHoc,
        tenLopHocPhan: item.tenLopHocPhan || tenLop(lhp),
        maLop: maLop(lhp),
        thoiLuongPhut: item.thoiLuongLamBaiPhut,
        soLanDaLam: item.soLanDaLam || 0,
    }
}

async function layTatCaCauHoiNguon() {
    const [versions, gocs, apDungs, dapAns, rubrics, dm] = await Promise.all([
        apiPage(() => lmsApi.cauHoiVersion.getAll({ size: 1000 })),
        apiPage(() => lmsApi.cauHoiGoc.getAll({ size: 1000 })),
        apiPage(() => lmsApi.cauHoiApDung.getAll({ size: 1000 })),
        apiPage(() => lmsApi.dapAnCauHoiVersion.getAll({ size: 2000 })),
        apiPage(() => lmsApi.rubricCauHoiVersion.getAll({ size: 1000 })),
        loadDanhMucCache(),
    ])
    return { versions: versions.content, gocs: gocs.content, apDungs: apDungs.content, dapAns: dapAns.content, rubrics: rubrics.content, dm }
}

async function replaceChildren(api, oldRows, newRows) {
    for (const row of oldRows || []) {
        if (row.id) await api.delete(row.id).catch(() => null)
    }
    const out = []
    for (const row of newRows || []) {
        out.push(unwrap(await api.create(row)))
    }
    return out
}

function normalizeDapAns(cauHoiVersionId, dapAns = [], loaiCauHoi) {
    if (loaiCauHoi === 'tu_luan') return []
    return (dapAns || [])
        .filter(d => d?.noiDung?.trim())
        .map((d, index) => ({
            cauHoiVersionId,
            noiDung: d.noiDung.trim(),
            laDapAnDung: !!d.laDapAnDung,
            thuTu: index + 1,
        }))
}

function normalizeRubrics(cauHoiVersionId, rubrics = []) {
    return (rubrics || [])
        .filter(r => r?.tieuChi?.trim())
        .map((r, index) => ({
            cauHoiVersionId,
            tieuChi: r.tieuChi.trim(),
            moTa: r.moTa || '',
            diemToiDa: Number(r.diemToiDa || 0),
            thuTu: index + 1,
        }))
}

export const cauHoiVersionService = {
    async layDanhSach(params = {}) {
        const src = await layTatCaCauHoiNguon()
        let content = src.versions.map(v => enrichCauHoiVersion(v, src))
        if (params.monHocId) content = content.filter(x => String(x.monHocId) === String(params.monHocId))
        if (params.chuongTrinhMonId) content = content.filter(x => x.apDungs.some(a => String(a.chuongTrinhMonId) === String(params.chuongTrinhMonId)))
        if (params.trangThai) content = content.filter(x => x.trangThai === params.trangThai)
        if (params.mucDo) content = content.filter(x => x.mucDo === params.mucDo)
        return { content, totalElements: content.length, totalPages: 1, number: 0 }
    },
    async layTheoId(id) {
        const [item, src] = await Promise.all([apiOne(() => lmsApi.cauHoiVersion.getById(id)), layTatCaCauHoiNguon()])
        return item ? enrichCauHoiVersion(item, src) : null
    },
    async tao(payload) {
        const goc = unwrap(await lmsApi.cauHoiGoc.create({
            monHocId: Number(payload.monHocId),
            maCauHoi: payload.maCauHoi || null,
            nguoiTaoTaiKhoanId: Number(payload.nguoiTaoTaiKhoanId || 1),
            trangThai: payload.trangThaiGoc || 'dang_su_dung',
            donViSoHuuId: payload.donViSoHuuId || null,
        }))
        const version = unwrap(await lmsApi.cauHoiVersion.create({
            cauHoiGocId: goc.id,
            versionNo: 1,
            noiDung: payload.noiDung,
            loaiCauHoi: payload.loaiCauHoi,
            mucDo: payload.mucDo || 'trung_binh',
            diemMacDinh: Number(payload.diemMacDinh || 1),
            giaiThichDapAn: payload.giaiThichDapAn || payload.huongDanCham || '',
            trangThai: payload.trangThai || 'nhap',
            nangLucDauRaId: payload.nangLucDauRaId || null,
            nguoiDuyetTaiKhoanId: payload.trangThai === 'dang_su_dung' ? Number(payload.nguoiDuyetTaiKhoanId || payload.nguoiTaoTaiKhoanId || 1) : (payload.nguoiDuyetTaiKhoanId || null),
            thoiGianDuyet: payload.trangThai === 'dang_su_dung' ? (payload.thoiGianDuyet || new Date().toISOString()) : (payload.thoiGianDuyet || null),
            noiDungHash: payload.noiDungHash || null,
            isLocked: false,
        }))
        await replaceChildren(lmsApi.dapAnCauHoiVersion, [], normalizeDapAns(version.id, payload.dapAns, payload.loaiCauHoi))
        await replaceChildren(lmsApi.rubricCauHoiVersion, [], normalizeRubrics(version.id, payload.rubrics))
        if (payload.chuongTrinhMonId) {
            await lmsApi.cauHoiApDung.create({
                cauHoiVersionId: version.id,
                chuongTrinhMonId: Number(payload.chuongTrinhMonId),
                syllabusChuongBaiId: payload.syllabusChuongBaiId || null,
                trangThai: payload.trangThaiApDung || 'can_ra_soat',
                ghiChu: payload.ghiChuApDung || '',
            })
        }
        return this.layTheoId(version.id)
    },
    async capNhat(id, payload) {
        const current = await this.layTheoId(id)
        if (!current) throw new Error('Không tìm thấy câu hỏi version')
        const taoVersionMoi = Boolean(payload.taoVersionMoi || current.isLocked)
        if (payload.monHocId || payload.maCauHoi) {
            await lmsApi.cauHoiGoc.update(current.cauHoiGocId, {
                ...current.cauHoiGoc,
                monHocId: Number(payload.monHocId || current.monHocId),
                maCauHoi: payload.maCauHoi || current.maCauHoi,
                trangThai: payload.trangThaiGoc || current.trangThaiGoc || 'dang_su_dung',
                nguoiTaoTaiKhoanId: payload.nguoiTaoTaiKhoanId || current.cauHoiGoc?.nguoiTaoTaiKhoanId || 1,
            }).catch(() => null)
        }
        const basePayload = {
            cauHoiGocId: current.cauHoiGocId,
            versionNo: taoVersionMoi ? Number(current.versionNo || 1) + 1 : Number(current.versionNo || 1),
            noiDung: payload.noiDung,
            loaiCauHoi: payload.loaiCauHoi,
            mucDo: payload.mucDo || 'trung_binh',
            diemMacDinh: Number(payload.diemMacDinh || 1),
            giaiThichDapAn: payload.giaiThichDapAn || payload.huongDanCham || '',
            trangThai: payload.trangThai || (taoVersionMoi ? 'nhap' : current.trangThai),
            nangLucDauRaId: payload.nangLucDauRaId || null,
            nguoiDuyetTaiKhoanId: payload.trangThai === 'dang_su_dung' ? Number(payload.nguoiDuyetTaiKhoanId || payload.nguoiTaoTaiKhoanId || 1) : (payload.nguoiDuyetTaiKhoanId || null),
            thoiGianDuyet: payload.trangThai === 'dang_su_dung' ? (payload.thoiGianDuyet || new Date().toISOString()) : (payload.thoiGianDuyet || null),
            noiDungHash: payload.noiDungHash || null,
            isLocked: false,
        }
        const version = taoVersionMoi
            ? unwrap(await lmsApi.cauHoiVersion.create(basePayload))
            : unwrap(await lmsApi.cauHoiVersion.update(id, { ...basePayload, isLocked: current.isLocked || false }))
        const oldAns = taoVersionMoi ? [] : current.dapAns
        const oldRubrics = taoVersionMoi ? [] : current.rubrics
        await replaceChildren(lmsApi.dapAnCauHoiVersion, oldAns, normalizeDapAns(version.id, payload.dapAns, payload.loaiCauHoi))
        await replaceChildren(lmsApi.rubricCauHoiVersion, oldRubrics, normalizeRubrics(version.id, payload.rubrics))
        if (payload.chuongTrinhMonId) {
            const app = (taoVersionMoi ? [] : current.apDungs).find(a => String(a.chuongTrinhMonId) === String(payload.chuongTrinhMonId))
            const appPayload = {
                cauHoiVersionId: version.id,
                chuongTrinhMonId: Number(payload.chuongTrinhMonId),
                syllabusChuongBaiId: payload.syllabusChuongBaiId || null,
                trangThai: payload.trangThaiApDung || 'can_ra_soat',
                ghiChu: payload.ghiChuApDung || '',
            }
            if (app?.id) await lmsApi.cauHoiApDung.update(app.id, appPayload)
            else await lmsApi.cauHoiApDung.create(appPayload)
        }
        return this.layTheoId(version.id)
    },
    async xoa(id) { return unwrap(await lmsApi.cauHoiVersion.delete(id)) },
}

export const nganHangCauHoiService = cauHoiVersionService

export const cauHoiApDungService = {
    async layDanhSach(params = {}) {
        const [apps, src] = await Promise.all([apiPage(() => lmsApi.cauHoiApDung.getAll({ size: 1000 })), layTatCaCauHoiNguon()])
        let content = apps.content.map(a => {
            const cauHoi = enrichCauHoiVersion(byId(src.versions, a.cauHoiVersionId) || {}, src)
            const chuDe = byId(src.dm.chuDe, a.syllabusChuongBaiId)
            return { ...a, cauHoi, noiDung: cauHoi.noiDung, maCauHoi: cauHoi.maCauHoi, tenMonHoc: cauHoi.tenMonHoc, tenChuDe: tenChuDe(chuDe), ...enrichTheoCtm(a, src.dm, a.chuongTrinhMonId) }
        })
        if (params.chuongTrinhMonId) content = content.filter(x => String(x.chuongTrinhMonId) === String(params.chuongTrinhMonId))
        if (params.trangThai) content = content.filter(x => x.trangThai === params.trangThai)
        return { content, totalElements: content.length, totalPages: 1, number: 0 }
    },
    async capNhat(id, payload) { return unwrap(await lmsApi.cauHoiApDung.update(id, payload)) },
    async tao(payload) { return unwrap(await lmsApi.cauHoiApDung.create(payload)) },
    async xoa(id) { return unwrap(await lmsApi.cauHoiApDung.delete(id)) },
}

export const maTranDeService = {
    async layDanhSach(params = {}) {
        const [page, details, dm] = await Promise.all([
            apiPage(() => lmsApi.maTranDe.getAll({ size: 1000, ...params })),
            apiPage(() => lmsApi.maTranDeChiTiet.getAll({ size: 2000 })),
            loadDanhMucCache(),
        ])
        const content = page.content.map(m => ({ ...enrichTheoCtm(m, dm, m.chuongTrinhMonId), chiTiets: details.content.filter(d => String(d.maTranDeId) === String(m.id)) }))
        return { ...page, content }
    },
    async layTheoId(id) {
        const [item, all] = await Promise.all([apiOne(() => lmsApi.maTranDe.getById(id)), this.layDanhSach()])
        return all.content.find(x => String(x.id) === String(id)) || item
    },
    async tao(payload) {
        const details = payload.chiTiets || []
        const m = unwrap(await lmsApi.maTranDe.create({
            tenMaTran: payload.tenMaTran,
            chuongTrinhMonId: Number(payload.chuongTrinhMonId),
            tongDiem: Number(payload.tongDiem || 0),
            thoiGianLamBai: Number(payload.thoiGianLamBai || 0),
            trangThai: payload.trangThai || 'nhap',
        }))
        await replaceChildren(lmsApi.maTranDeChiTiet, [], details.map((d, i) => ({
            maTranDeId: m.id,
            syllabusChuongBaiId: d.syllabusChuongBaiId || null,
            mucDo: d.mucDo || 'trung_binh',
            loaiCauHoi: d.loaiCauHoi || null,
            soCau: Number(d.soCau || 1),
            diemMoiCau: Number(d.diemMoiCau || 1),
            thuTu: i + 1,
        })))
        return this.layTheoId(m.id)
    },
    async capNhat(id, payload) {
        const current = await this.layTheoId(id)
        const m = unwrap(await lmsApi.maTranDe.update(id, {
            tenMaTran: payload.tenMaTran,
            chuongTrinhMonId: Number(payload.chuongTrinhMonId),
            tongDiem: Number(payload.tongDiem || 0),
            thoiGianLamBai: Number(payload.thoiGianLamBai || 0),
            trangThai: payload.trangThai || 'nhap',
        }))
        await replaceChildren(lmsApi.maTranDeChiTiet, current?.chiTiets || [], (payload.chiTiets || []).map(d => ({
            maTranDeId: m.id,
            syllabusChuongBaiId: d.syllabusChuongBaiId || null,
            mucDo: d.mucDo || 'trung_binh',
            loaiCauHoi: d.loaiCauHoi || null,
            soCau: Number(d.soCau || 1),
            diemMoiCau: Number(d.diemMoiCau || 1),
        })))
        return this.layTheoId(m.id)
    },
    async xoa(id) { return unwrap(await lmsApi.maTranDe.delete(id)) },
}

async function syncBaiTapCauHoi(baiTapOnlineId, cauHoiRows = []) {
    const old = asList(await lmsApi.baiTapOnlineCauHoi.getAll({ size: 2000 }))
        .filter(x => String(x.baiTapOnlineId) === String(baiTapOnlineId))
    await replaceChildren(lmsApi.baiTapOnlineCauHoi, old, cauHoiRows.map((c, index) => ({
        baiTapOnlineId,
        cauHoiVersionId: Number(c.cauHoiVersionId || c.id),
        diem: Number(c.diem || c.diemMacDinh || 1),
        thuTu: Number(c.thuTu || index + 1),
        batBuoc: c.batBuoc !== false,
    })))
}

export const baiTapOnlineService = {
    async layDanhSach(params = {}) {
        const [page, dm, rows] = await Promise.all([
            apiPage(() => lmsApi.baiTapOnline.getAll({ size: 1000, ...params })),
            loadDanhMucCache(),
            apiPage(() => lmsApi.baiTapOnlineCauHoi.getAll({ size: 3000 })),
        ])
        const content = page.content.map(item => enrichBaiTapOnline(item, dm, rows.content))
        return { ...page, content }
    },
    async layTheoId(id) {
        const [item, dm, rows] = await Promise.all([
            apiOne(() => lmsApi.baiTapOnline.getById(id)),
            loadDanhMucCache(),
            apiPage(() => lmsApi.baiTapOnlineCauHoi.getAll({ size: 3000 })),
        ])
        return item ? { ...enrichBaiTapOnline(item, dm, rows.content), cauHoiRows: rows.content.filter(x => String(x.baiTapOnlineId) === String(id)).sort((a,b)=>(a.thuTu||0)-(b.thuTu||0)) } : null
    },
    async tao(payload) {
        const cauHoiDaChon = payload.cauHoiDaChon || []
        const bai = unwrap(await lmsApi.baiTapOnline.create({
            chuongTrinhMonId: Number(payload.chuongTrinhMonId),
            nguoiTaoTaiKhoanId: Number(payload.nguoiTaoTaiKhoanId || 1),
            nguonTao: payload.nguonTao || 'giao_vien',
            tieuDe: payload.tieuDe || payload.tenBaiTap,
            moTa: payload.moTa || '',
            loaiBai: payload.loaiBai || 'quiz',
            tongDiem: Number(payload.tongDiem || 10),
            trangThai: payload.trangThai || 'nhap',
            tronCauHoi: !!payload.tronCauHoi,
            tronDapAn: !!payload.tronDapAn,
            sinhDeTuDong: !!payload.sinhDeTuDong,
            hienDapAnSauKhiNop: !!payload.hienDapAnSauKhiNop,
            hienGiaiThichSauKhiNop: !!payload.hienGiaiThichSauKhiNop,
            maTranDeId: payload.sinhDeTuDong ? (payload.maTranDeId || null) : null,
        }))
        if (cauHoiDaChon.length) await syncBaiTapCauHoi(bai.id, cauHoiDaChon)
        return this.layTheoId(bai.id)
    },
    async capNhat(id, payload) {
        const current = await this.layTheoId(id)
        const bai = unwrap(await lmsApi.baiTapOnline.update(id, {
            ...current,
            chuongTrinhMonId: Number(payload.chuongTrinhMonId),
            nguoiTaoTaiKhoanId: Number(payload.nguoiTaoTaiKhoanId || current?.nguoiTaoTaiKhoanId || 1),
            nguonTao: payload.nguonTao || current?.nguonTao || 'giao_vien',
            tieuDe: payload.tieuDe || payload.tenBaiTap,
            moTa: payload.moTa || '',
            loaiBai: payload.loaiBai || 'quiz',
            tongDiem: Number(payload.tongDiem || 10),
            trangThai: payload.trangThai || 'nhap',
            tronCauHoi: !!payload.tronCauHoi,
            tronDapAn: !!payload.tronDapAn,
            sinhDeTuDong: !!payload.sinhDeTuDong,
            hienDapAnSauKhiNop: !!payload.hienDapAnSauKhiNop,
            hienGiaiThichSauKhiNop: !!payload.hienGiaiThichSauKhiNop,
            maTranDeId: payload.sinhDeTuDong ? (payload.maTranDeId || null) : null,
        }))
        if ((payload.cauHoiDaChon || []).length) await syncBaiTapCauHoi(bai.id, payload.cauHoiDaChon || [])
        return this.layTheoId(bai.id)
    },
    async xoa(id) { return unwrap(await lmsApi.baiTapOnline.delete(id)) },
    async saoChep(id) {
        const old = await this.layTheoId(id)
        if (!old) return null
        return this.tao({ ...old, tieuDe: `${old.tieuDe || old.tenBaiTap} - Bản sao`, trangThai: 'nhap', cauHoiDaChon: old.cauHoiRows || [] })
    },
    async saoCheep(id) { return this.saoChep(id) },
}

export const baiTapLopService = {
    async layDanhSach(params = {}) {
        const [page, dm, baiTap] = await Promise.all([
            apiPage(() => lmsApi.baiTapLop.getAll({ size: 1000, ...params })),
            loadDanhMucCache(),
            baiTapOnlineService.layDanhSach(),
        ])
        const content = page.content.map(item => enrichBaiTapLop(item, dm, baiTap.content))
        return { ...page, content }
    },
    async layTheoId(id) {
        const all = await this.layDanhSach()
        return all.content.find(x => String(x.id) === String(id)) || apiOne(() => lmsApi.baiTapLop.getById(id))
    },
    async giaoBai(payload) {
        return unwrap(await lmsApi.baiTapLop.create({
            dotGiaoBaiOnlineId: payload.dotGiaoBaiOnlineId || null,
            baiTapOnlineId: Number(payload.baiTapOnlineId),
            lopHocPhanId: Number(payload.lopHocPhanId),
            cauHinhDanhGiaId: payload.cauHinhDanhGiaId || null,
            nguoiGiaoTaiKhoanId: Number(payload.nguoiGiaoTaiKhoanId || 1),
            thoiGianMo: normalizeDateTimeInput(payload.thoiGianMo),
            thoiGianDong: normalizeDateTimeInput(payload.thoiGianDong),
            thoiLuongLamBaiPhut: Number(payload.thoiLuongLamBaiPhut || 45),
            soLanLamToiDa: Number(payload.soLanLamToiDa || 1),
            cachTinhDiem: payload.cachTinhDiem || 'lay_diem_cao_nhat',
            choPhepXemDiem: !!payload.choPhepXemDiem,
            choPhepXemDapAn: !!payload.choPhepXemDapAn,
            tronCauHoi: !!payload.tronCauHoi,
            tronDapAn: !!payload.tronDapAn,
            choPhepLamQuaHan: !!payload.choPhepLamQuaHan,
            giaoVienDuocChinhSua: payload.giaoVienDuocChinhSua !== false,
            trangThai: payload.trangThai || 'chua_mo',
            ghiChu: payload.ghiChu || '',
            diemToiDa: Number(payload.diemToiDa || 10),
            yeuCauToanManHinh: !!payload.yeuCauToanManHinh,
            gioiHanRoiTab: payload.gioiHanRoiTab === '' || payload.gioiHanRoiTab == null ? null : Number(payload.gioiHanRoiTab),
            tuDongNopKhiViPham: !!payload.tuDongNopKhiViPham,
            matKhauLamBai: payload.matKhauLamBai || null,
        }))
    },
    async capNhat(id, payload) { return unwrap(await lmsApi.baiTapLop.update(id, payload)) },
    async giaHan(id, payload) { const c = await this.layTheoId(id); return this.capNhat(id, { ...c, ...payload }) },
    async doiSoLanLam(id, payload) { const c = await this.layTheoId(id); return this.capNhat(id, { ...c, ...payload }) },
    async tamDong(id) { const c = await this.layTheoId(id); return this.capNhat(id, { ...c, trangThai: 'tam_dong' }) },
    async moLai(id) { const c = await this.layTheoId(id); return this.capNhat(id, { ...c, trangThai: 'dang_mo' }) },
    async chotDiem(id) { const c = await this.layTheoId(id); return this.capNhat(id, { ...c, trangThai: 'da_chot' }) },
    async moChot(id) { const c = await this.layTheoId(id); return this.capNhat(id, { ...c, trangThai: 'da_dong' }) },
    async daySangBangDiem(id, payload = {}) {
        return unwrap(await lmsApi.baiTapLop.dayDiem(id, {
            cauHinhDanhGiaId: Number(payload.cauHinhDanhGiaId),
            nguoiThucHienId: payload.nguoiThucHienId ? Number(payload.nguoiThucHienId) : null,
            ghiChu: payload.ghiChu || payload.cotDiem || ''
        }))
    },
}

export const ketQuaService = {
    async layTheoLop(baiTapLopId, params = {}) {
        const page = await apiPage(() => lmsApi.ketQuaBaiTapLop.getAll({ size: 1000, ...params }))
        const dm = await loadDanhMucCache()
        const content = page.content
            .filter(x => !baiTapLopId || String(x.baiTapLopId) === String(baiTapLopId))
            .map(x => ({ ...x, sinhVien: byId(dm.sinhVien, x.sinhVienId), tenSinhVien: pick(byId(dm.sinhVien, x.sinhVienId), ['hoTen', 'tenSinhVien', 'ten', 'name']) || `SV #${x.sinhVienId}` }))
        return { ...page, content, totalElements: content.length }
    },
    async tinhLai(baiTapLopId) {
        // BE hien tai khong co endpoint tinh lai hang loat; FE reload bang ket qua.
        return this.layTheoLop(baiTapLopId)
    },
    async chinhDiem(id, payload) {
        const current = await apiOne(() => lmsApi.ketQuaBaiTapLop.getById(id))
        return unwrap(await lmsApi.ketQuaBaiTapLop.update(id, { ...current, diemGiaoVienChinhSua: Number(payload.diemMoi ?? payload.diemGiaoVienChinhSua), diemCuoiCung: Number(payload.diemMoi ?? payload.diemCuoiCung), nhanXet: payload.lyDo || payload.nhanXet || current?.nhanXet }))
    },
}

async function buildLanLamChiTiet(lanLamId) {
    const [lanLam, cauHois, dapAns, traLois, chons] = await Promise.all([
        apiOne(() => lmsApi.lanLamBaiOnline.getById(lanLamId)),
        apiPage(() => lmsApi.lanLamBaiCauHoi.getAll({ size: 3000 })),
        apiPage(() => lmsApi.lanLamBaiDapAn.getAll({ size: 5000 })),
        apiPage(() => lmsApi.cauTraLoiSinhVien.getAll({ size: 5000 })),
        apiPage(() => lmsApi.cauTraLoiChonDapAn.getAll({ size: 5000 })),
    ])
    if (!lanLam) return null
    const qs = cauHois.content
        .filter(q => String(q.lanLamBaiId) === String(lanLamId))
        .sort((a, b) => (a.thuTu || 0) - (b.thuTu || 0))
        .map(q => {
            const tl = traLois.content.find(t => String(t.lanLamBaiCauHoiId) === String(q.id))
            const selected = tl ? chons.content.filter(c => String(c.cauTraLoiId) === String(tl.id)).map(c => c.lanLamBaiDapAnId) : []
            return {
                ...q,
                dapAns: dapAns.content.filter(d => String(d.lanLamBaiCauHoiId) === String(q.id)).sort((a,b)=>(a.thuTu||0)-(b.thuTu||0)),
                cauTraLoi: tl,
                dapAnDaChonIds: selected,
            }
        })
    const cauTraLois = qs.map(q => ({
        cauHoiId: q.id,
        lanLamBaiCauHoiId: q.id,
        noiDung: q.noiDungSnapshot,
        loaiCauHoi: q.loaiCauHoiSnapshot,
        diemToiDa: q.diemSnapshot,
        diem: q.cauTraLoi?.diem ?? 0,
        diemGiaoVien: q.cauTraLoi?.diem ?? null,
        nhanXet: q.cauTraLoi?.nhanXet || '',
        noiDungTraLoi: q.cauTraLoi?.noiDungTraLoi || '',
        dapAnChon: q.dapAns.filter(d => q.dapAnDaChonIds.includes(d.id)).map(d => d.noiDungSnapshot),
        dapAnDung: q.dapAns.filter(d => d.laDapAnDungSnapshot).map(d => d.noiDungSnapshot),
        huongDanCham: '',
        trangThaiCham: q.cauTraLoi?.diem != null ? 'da_cham' : 'chua_cham',
    }))
    return { ...lanLam, cauHois: qs, cauTraLois, hoTen: `SV #${lanLam.sinhVienId}`, maSV: lanLam.sinhVienId }
}

export const giaoVienLmsService = {
    async layDanhSachNop(baiTapLopId, params = {}) {
        const [page, dm] = await Promise.all([
            apiPage(() => lmsApi.lanLamBaiOnline.getAll({ size: 1000, ...params })),
            loadDanhMucCache(),
        ])
        const content = page.content
            .filter(x => !baiTapLopId || String(x.baiTapLopId) === String(baiTapLopId))
            .map(x => {
                const sv = byId(dm.sinhVien, x.sinhVienId)
                const hoTen = pick(sv, ['hoTen', 'tenSinhVien', 'ten', 'name']) || `SV #${x.sinhVienId}`
                return {
                    ...x,
                    lanLamBaiId: x.id,
                    sinhVien: sv,
                    tenSinhVien: hoTen,
                    hoTen,
                    maSV: pick(sv, ['maSinhVien', 'maSV', 'ma', 'code']) || x.sinhVienId,
                    lop: pick(sv, ['tenLop', 'lop', 'lopDanhNghia']) || '',
                    soLanLam: x.lanThu || 1,
                    lanDuocChon: x.lanThu,
                    thoiGianNopGanNhat: x.thoiGianNop,
                    diemTuDong: x.diemTuDong,
                    diemGiaoVien: x.diemGiaoVienCham,
                    diemCuoiCung: x.diemCuoiCung,
                }
            })
        return { ...page, content, totalElements: content.length }
    },
    async getChiTietLanLam(lanLamBaiId) { return buildLanLamChiTiet(lanLamBaiId) },
    async chamTuLuan(lanLamBaiId, payload) {
        const current = await apiOne(() => lmsApi.lanLamBaiOnline.getById(lanLamBaiId))
        return unwrap(await lmsApi.lanLamBaiOnline.update(lanLamBaiId, { ...current, diemGiaoVienCham: Number(payload.diemGiaoVienCham ?? payload.diemCuoiCung ?? current?.diemGiaoVienCham ?? 0), diemCuoiCung: Number(payload.diemCuoiCung ?? payload.diemGiaoVienCham ?? current?.diemCuoiCung ?? 0), trangThai: 'da_cham', ghiChu: payload.nhanXet || payload.ghiChu || current?.ghiChu }))
    },
}

export const sinhVienLmsService = {
    async layBaiDuocGiao(params = {}) {
        return baiTapLopService.layDanhSach({ ...params })
    },
    async batDauLamBai(baiTapLopId) { return unwrap(await lmsApi.sinhVien.batDauLamBai(baiTapLopId)) },
    async getLanLamBai(lanLamBaiId) { return buildLanLamChiTiet(lanLamBaiId) },
    async nopBai(lanLamBaiId, payload) { return unwrap(await lmsApi.sinhVien.nopBai(lanLamBaiId, payload)) },
    async layKetQua(baiTapLopId) {
        const res = await ketQuaService.layTheoLop(baiTapLopId)
        return res.content[0] || null
    },
    async layLichSuLamBai(baiTapLopId) {
        const res = await giaoVienLmsService.layDanhSachNop(baiTapLopId)
        return res.content
    },
}

export const baiNopLmsService = {
    async layDanhSach(params = {}) { return apiPage(() => lmsApi.baiNopLms.getAll({ size: 1000, ...params })) },
    async tao(payload) { return unwrap(await lmsApi.baiNopLms.create(payload)) },
    async capNhat(id, payload) { return unwrap(await lmsApi.baiNopLms.update(id, payload)) },
    async xoa(id) { return unwrap(await lmsApi.baiNopLms.delete(id)) },
}

export const nhatKyLamBaiService = {
    async layDanhSach(params = {}) { return apiPage(() => lmsApi.nhatKyLamBaiOnline.getAll({ size: 2000, ...params })) },
}

export const lichSuLmsService = {
    async layDanhSach(params = {}) {
        const [baiTap, cauHoi, dayDiem, chinhDiem, nhatKy] = await Promise.all([
            apiPage(() => lmsApi.lichSuBaiTapLop.getAll({ size: 1000, ...params })),
            apiPage(() => lmsApi.lichSuCauHoi.getAll({ size: 1000, ...params })),
            apiPage(() => lmsApi.lichSuDayDiem.getAll({ size: 1000, ...params })),
            apiPage(() => lmsApi.lichSuChinhDiem.getAll({ size: 1000, ...params })),
            apiPage(() => lmsApi.nhatKyLamBaiOnline.getAll({ size: 1000, ...params })),
        ])
        const content = [
            ...baiTap.content.map(x => ({ ...x, loaiLog: 'Bài tập lớp', thoiGianHienThi: x.createdAt || x.thoiGian, noiDung: x.hanhDong || x.ghiChu })),
            ...cauHoi.content.map(x => ({ ...x, loaiLog: 'Câu hỏi', thoiGianHienThi: x.thoiGian || x.createdAt, noiDung: x.hanhDong || x.ghiChu })),
            ...dayDiem.content.map(x => ({ ...x, loaiLog: 'Đẩy điểm', thoiGianHienThi: x.createdAt || x.thoiGian, noiDung: x.hanhDong || x.ghiChu })),
            ...chinhDiem.content.map(x => ({ ...x, loaiLog: 'Chỉnh điểm', thoiGianHienThi: x.createdAt || x.thoiGian, noiDung: x.lyDo || x.ghiChu })),
            ...nhatKy.content.map(x => ({ ...x, loaiLog: 'Nhật ký làm bài', thoiGianHienThi: x.createdAt, noiDung: x.hanhDong })),
        ].sort((a, b) => new Date(b.thoiGianHienThi || 0) - new Date(a.thoiGianHienThi || 0))
        return { content, totalElements: content.length, totalPages: 1, number: 0 }
    },
}

export const dashboardLmsService = {
    async layThongKe() {
        const [cauHoi, apDung, maTran, baiTap, baiTapLop, ketQua, baiNop] = await Promise.all([
            apiPage(() => lmsApi.cauHoiVersion.getAll({ size: 1 })),
            apiPage(() => lmsApi.cauHoiApDung.getAll({ size: 1000 })),
            apiPage(() => lmsApi.maTranDe.getAll({ size: 1 })),
            apiPage(() => lmsApi.baiTapOnline.getAll({ size: 1 })),
            apiPage(() => lmsApi.baiTapLop.getAll({ size: 1000 })),
            apiPage(() => lmsApi.ketQuaBaiTapLop.getAll({ size: 1000 })),
            apiPage(() => lmsApi.baiNopLms.getAll({ size: 1000 })),
        ])
        const lopItems = baiTapLop.content || []
        const kqItems = ketQua.content || []
        return {
            tongCauHoi: cauHoi.totalElements || 0,
            cauHoiCanRaSoat: apDung.content.filter(x => x.trangThai === 'can_ra_soat').length,
            tongMaTranDe: maTran.totalElements || 0,
            tongBaiTapOnline: baiTap.totalElements || 0,
            baiTapDangMo: lopItems.filter(x => x.trangThai === 'dang_mo').length,
            lopDaGiao: lopItems.length,
            sinhVienDaNop: kqItems.length || baiNop.content.length,
            baiCanCham: kqItems.filter(x => x.trangThai === 'tam_tinh' || x.trangThai === 'chua_cham').length,
            ketQuaDaChot: kqItems.filter(x => x.trangThai === 'da_chot').length,
            diemDaDay: kqItems.filter(x => x.trangThai === 'da_day_sang_diem').length,
        }
    },
    async layBaiTapDangHoatDong() { return baiTapLopService.layDanhSach({ trangThai: 'dang_mo', size: 10 }) },
}

export const lmsCrudService = {
    resources: lmsCrudRegistry,
    async list(resourceKey, params = {}) {
        const item = lmsCrudRegistry[resourceKey]
        if (!item) return { content: [], totalElements: 0 }
        return apiPage(() => item.api.getAll({ size: 1000, ...params }))
    },
    async get(resourceKey, id) {
        const item = lmsCrudRegistry[resourceKey]
        return item ? apiOne(() => item.api.getById(id)) : null
    },
    async create(resourceKey, payload) {
        const item = lmsCrudRegistry[resourceKey]
        if (!item) throw new Error('Resource không hợp lệ')
        return unwrap(await item.api.create(payload))
    },
    async update(resourceKey, id, payload) {
        const item = lmsCrudRegistry[resourceKey]
        if (!item) throw new Error('Resource không hợp lệ')
        return unwrap(await item.api.update(id, payload))
    },
    async delete(resourceKey, id) {
        const item = lmsCrudRegistry[resourceKey]
        if (!item) throw new Error('Resource không hợp lệ')
        return unwrap(await item.api.delete(id))
    },
}

export const bangDiemLopService = {
    async layBangDiem(lopHocPhanId) {
        const [dm, cauHinhs, diemPage, ketQuaPage, sinhViens] = await Promise.all([
            loadDanhMucCache(),
            lmsDanhMucService.layCauHinhDanhGia(lopHocPhanId),
            apiPage(() => lmsApi.diemChiTiet.getAll({ lopHocPhanId, size: 5000 })),
            apiPage(() => lmsApi.ketQuaLopHocPhan.getAll({ lopHocPhanId, size: 5000 })),
            lmsDanhMucService.laySinhVien().catch(() => []),
        ])
        const diemRows = diemPage.content || []
        const ketQuaRows = ketQuaPage.content || []
        const sinhVienIds = [...new Set([
            ...diemRows.map(x => x.sinhVienId),
            ...ketQuaRows.map(x => x.sinhVienId),
        ].filter(Boolean))]
        const rows = sinhVienIds.map(sinhVienId => {
            const sv = byId(sinhViens, sinhVienId) || byId(dm.sinhVien, sinhVienId) || {}
            const ketQua = ketQuaRows.find(x => String(x.sinhVienId) === String(sinhVienId)) || {}
            const diemTheoCot = {}
            cauHinhs.forEach(c => {
                diemTheoCot[c.id] = diemRows.find(d => String(d.sinhVienId) === String(sinhVienId) && String(d.cauHinhDanhGiaId) === String(c.id)) || null
            })
            return {
                sinhVienId,
                maSinhVien: pick(sv, ['maSinhVien', 'maSv', 'ma', 'code']) || sinhVienId,
                tenSinhVien: pick(sv, ['hoTen', 'tenSinhVien', 'ten', 'name']) || `SV #${sinhVienId}`,
                diemTheoCot,
                ketQua,
            }
        })
        return { cauHinhs, rows, ketQuaRows, diemRows }
    },
    async layDiemSinhVien(sinhVienId) {
        const [dm, diemPage, ketQuaPage, cauHinhs, lopHocPhans] = await Promise.all([
            loadDanhMucCache(),
            apiPage(() => lmsApi.diemChiTiet.getAll({ sinhVienId, size: 5000 })),
            apiPage(() => lmsApi.ketQuaLopHocPhan.getAll({ sinhVienId, size: 5000 })),
            lmsDanhMucService.layCauHinhDanhGia(),
            lmsDanhMucService.layLopHocPhan(),
        ])
        const diemRows = diemPage.content || []
        const ketQuaRows = ketQuaPage.content || []
        const lopIds = [...new Set([
            ...diemRows.map(x => x.lopHocPhanId),
            ...ketQuaRows.map(x => x.lopHocPhanId),
        ].filter(Boolean))]
        return lopIds.map(lopHocPhanId => {
            const lop = byId(lopHocPhans, lopHocPhanId) || byId(dm.lopHocPhan, lopHocPhanId) || {}
            const cols = cauHinhs.filter(c => String(c.lopHocPhanId) === String(lopHocPhanId))
            return {
                lopHocPhanId,
                tenLopHocPhan: tenLop(lop),
                cauHinhs: cols,
                diemChiTiet: diemRows.filter(d => String(d.lopHocPhanId) === String(lopHocPhanId)),
                ketQua: ketQuaRows.find(k => String(k.lopHocPhanId) === String(lopHocPhanId)) || {},
            }
        })
    }
}
