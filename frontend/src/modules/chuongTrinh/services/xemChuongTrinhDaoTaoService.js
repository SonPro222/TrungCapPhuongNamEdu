import apiClient from '@/core/api/apiClient'

function layData(res) {
  if (res?.success !== undefined && res?.data !== undefined) return res.data
  if (res?.data?.success !== undefined && res?.data?.data !== undefined) return res.data.data
  return res?.data ?? res
}

function normalizeList(res) {
  const data = layData(res)
  if (Array.isArray(data)) return data
  if (Array.isArray(data?.content)) return data.content
  if (Array.isArray(data?.items)) return data.items
  return []
}

async function getList(url, params = {}) {
  const res = await apiClient.get(url, { params: { size: 1000, ...params } })
  return normalizeList(res)
}

async function getOne(url) {
  const res = await apiClient.get(url)
  return layData(res)
}

function taoMap(list = []) {
  return new Map(list.map((item) => [item.id, item]))
}

function sapXepTheoThuTu(list = []) {
  return [...list].sort((a, b) => Number(a.thuTu ?? 9999) - Number(b.thuTu ?? 9999) || Number(a.id ?? 0) - Number(b.id ?? 0))
}

function ghepTheoBangNoi(bangNoi = [], bangGoc = [], gocIdKey) {
  const mapGoc = taoMap(bangGoc)
  return sapXepTheoThuTu(bangNoi).map((link) => ({
    ...mapGoc.get(link[gocIdKey]),
    lienKetId: link.id,
    chuongTrinhVersionId: link.chuongTrinhVersionId,
    thuTuGan: link.thuTu,
    ghiChuGan: link.ghiChu,
    bangNoi: link
  })).filter((item) => item.id)
}

export const xemChuongTrinhDaoTaoService = {
  async getNganh() {
    return getList('/dao-tao/nganh')
  },

  async getChuongTrinhTheoNganh(nganhId) {
    return getList(`/chuongTrinh/chuong-trinh/theo-nganh/${nganhId}`)
  },

  async getChuongTrinh(id) {
    return getOne(`/chuongTrinh/chuong-trinh/${id}`)
  },

  async getVersionTheoChuongTrinh(chuongTrinhId) {
    return getList(`/chuongTrinh/chuong-trinh-version/theo-chuong-trinh/${chuongTrinhId}`)
  },

  async getVersion(id) {
    return getOne(`/chuongTrinh/chuong-trinh-version/${id}`)
  },

  async getMonHoc(id) {
    return getOne(`/chuongTrinh/mon-hoc/${id}`)
  },

  async getChuongTrinhMon(id) {
    return getOne(`/chuongTrinh/chuong-trinh-mon/${id}`)
  },

  async getChiTietVersion(versionId) {
    const [
      version,
      khungKy,
      nhomKienThuc,
      syllabusChuongTrinh,
      nhomTuChon,
      chuongTrinhMon,
      monHoc,
      mucTieuGoc,
      mucTieuNoi,
      nangLucGoc,
      nangLucNoi,
      viTriGoc,
      viTriNoi,
      dieuKienGoc,
      dieuKienNoi
    ] = await Promise.all([
      this.getVersion(versionId),
      getList(`/dao-tao/khung-ky/theo-version/${versionId}`),
      getList(`/chuongTrinh/nhom-kien-thuc/theo-version/${versionId}`),
      getList(`/chuongTrinh/syllabus-chuong-trinh/theo-version/${versionId}`),
      getList(`/chuongTrinh/nhom-tu-chon/theo-version/${versionId}`),
      getList(`/chuongTrinh/chuong-trinh-mon/theo-version/${versionId}`),
      getList('/chuongTrinh/mon-hoc'),
      getList('/chuongTrinh/muc-tieu-chuong-trinh-goc'),
      getList('/chuongTrinh/chuong-trinh-version-muc-tieu', { chuongTrinhVersionId: versionId }),
      getList('/chuongTrinh/nang-luc-dau-ra-goc'),
      getList('/chuongTrinh/chuong-trinh-version-nang-luc', { chuongTrinhVersionId: versionId }),
      getList('/chuongTrinh/vi-tri-viec-lam-goc'),
      getList('/chuongTrinh/chuong-trinh-version-vi-tri-viec-lam', { chuongTrinhVersionId: versionId }),
      getList('/chuongTrinh/dieu-kien-tot-nghiep-goc'),
      getList('/chuongTrinh/chuong-trinh-version-dieu-kien-tot-nghiep', { chuongTrinhVersionId: versionId })
    ])

    const monHocMap = taoMap(monHoc)
    const monTrongCt = sapXepTheoThuTu(chuongTrinhMon).map((mon) => ({
      ...mon,
      monHoc: monHocMap.get(mon.monHocId),
      tenMon: monHocMap.get(mon.monHocId)?.tenMon || mon.maMonTrongCt,
      maMon: monHocMap.get(mon.monHocId)?.maMon || mon.maMonTrongCt
    }))

    const monTuChonTheoNhom = {}
    await Promise.all((nhomTuChon || []).map(async (nhom) => {
      const dsMonTuChon = await getList(`/chuongTrinh/mon-tu-chon/theo-nhom/${nhom.id}`)
      monTuChonTheoNhom[nhom.id] = dsMonTuChon.map((item) => {
        const ctm = monTrongCt.find((mon) => mon.id === item.chuongTrinhMonId)
        return { ...item, chuongTrinhMon: ctm, monHoc: ctm?.monHoc }
      })
    }))

    return {
      version,
      khungKy: sapXepTheoThuTu(khungKy),
      nhomKienThuc: sapXepTheoThuTu(nhomKienThuc),
      syllabusChuongTrinh,
      mucTieu: ghepTheoBangNoi(mucTieuNoi, mucTieuGoc, 'mucTieuGocId'),
      nangLuc: ghepTheoBangNoi(nangLucNoi, nangLucGoc, 'nangLucGocId'),
      viTriViecLam: ghepTheoBangNoi(viTriNoi, viTriGoc, 'viTriGocId'),
      dieuKienTotNghiep: ghepTheoBangNoi(dieuKienNoi, dieuKienGoc, 'dieuKienGocId'),
      nhomTuChon: sapXepTheoThuTu(nhomTuChon).map((nhom) => ({ ...nhom, monTuChon: monTuChonTheoNhom[nhom.id] || [] })),
      chuongTrinhMon: monTrongCt
    }
  },

  async getMonTheoVersion(versionId, boLoc = {}) {
    const [monTrongCt, monHoc, khungKy, nhomKienThuc] = await Promise.all([
      getList(`/chuongTrinh/chuong-trinh-mon/theo-version/${versionId}`),
      getList('/chuongTrinh/mon-hoc'),
      getList(`/dao-tao/khung-ky/theo-version/${versionId}`),
      getList(`/chuongTrinh/nhom-kien-thuc/theo-version/${versionId}`)
    ])
    const monHocMap = taoMap(monHoc)
    const kyMap = taoMap(khungKy)
    const nhomMap = taoMap(nhomKienThuc)
    return sapXepTheoThuTu(monTrongCt)
      .filter((mon) => !boLoc.khungKyId || String(mon.khungKyId) === String(boLoc.khungKyId))
      .filter((mon) => !boLoc.nhomKienThucId || String(mon.nhomKienThucId) === String(boLoc.nhomKienThucId))
      .map((mon) => ({
        ...mon,
        monHoc: monHocMap.get(mon.monHocId),
        khungKy: kyMap.get(mon.khungKyId),
        nhomKienThuc: nhomMap.get(mon.nhomKienThucId),
        tenMon: monHocMap.get(mon.monHocId)?.tenMon || mon.maMonTrongCt,
        maMon: monHocMap.get(mon.monHocId)?.maMon || mon.maMonTrongCt
      }))
  },

  async getChiTietMon(chuongTrinhMonId) {
    const monCt = await this.getChuongTrinhMon(chuongTrinhMonId)
    const [monHoc, tienQuyet, syllabusList, quyDoiDiem, quyDoiDiemMauNoi, quyDoiDiemMau] = await Promise.all([
      monCt?.monHocId ? this.getMonHoc(monCt.monHocId) : null,
      getList(`/chuongTrinh/mon-tien-quyet/theo-mon/${chuongTrinhMonId}`),
      getList(`/chuongTrinh/syllabus-mon-hoc/theo-mon/${chuongTrinhMonId}`),
      getList(`/chuongTrinh/quy-doi-diem/theo-mon/${chuongTrinhMonId}`),
      getList('/chuongTrinh/chuong-trinh-mon-quy-doi-diem-mau', { chuongTrinhMonId }),
      getList('/chuongTrinh/quy-doi-diem-mau')
    ])

    const monDieuKienIds = tienQuyet.map((item) => item.monDieuKienId).filter(Boolean)
    const monDieuKien = await Promise.all(monDieuKienIds.map((id) => this.getChuongTrinhMon(id).catch(() => null)))
    const monDieuKienMap = taoMap(monDieuKien.filter(Boolean))
    const allMonHocIds = monDieuKien.filter(Boolean).map((item) => item.monHocId).filter(Boolean)
    const allMonHoc = await Promise.all(allMonHocIds.map((id) => this.getMonHoc(id).catch(() => null)))
    const allMonHocMap = taoMap(allMonHoc.filter(Boolean))

    const syllabus = syllabusList[0] || null
    const [dieuKienMonHoc, chuongBai, taiLieu] = await Promise.all([
      syllabus?.id ? getList(`/chuongTrinh/dieu-kien-mon-hoc/theo-syllabus/${syllabus.id}`) : [],
      syllabus?.id ? getList(`/chuongTrinh/syllabus-chuong-bai/theo-syllabus/${syllabus.id}`) : [],
      syllabus?.id ? getList(`/chuongTrinh/syllabus-tai-lieu/theo-syllabus/${syllabus.id}`) : []
    ])

    const mauMap = taoMap(quyDoiDiemMau)
    return {
      chuongTrinhMon: { ...monCt, monHoc },
      monHoc,
      monTienQuyet: tienQuyet.map((item) => {
        const monDieuKienCt = monDieuKienMap.get(item.monDieuKienId)
        return { ...item, monDieuKienCt, monDieuKienGoc: allMonHocMap.get(monDieuKienCt?.monHocId) }
      }),
      syllabusMonHoc: syllabus,
      dieuKienMonHoc: sapXepTheoThuTu(dieuKienMonHoc),
      syllabusChuongBai: sapXepTheoThuTu(chuongBai),
      syllabusTaiLieu: taiLieu,
      quyDoiDiem,
      quyDoiDiemMau: quyDoiDiemMauNoi.map((item) => ({ ...item, mau: mauMap.get(item.quyDoiDiemMauId) })).filter((item) => item.mau)
    }
  }
}
