import { daoTaoXemChuongTrinhService } from '@/modules/daoTao/services/daoTaoXemChuongTrinhService'

function layItems(result) {
  if (Array.isArray(result)) return result
  if (Array.isArray(result?.items)) return result.items
  if (Array.isArray(result?.content)) return result.content
  if (Array.isArray(result?.data)) return result.data
  if (Array.isArray(result?.data?.items)) return result.data.items
  if (Array.isArray(result?.data?.content)) return result.data.content
  return []
}

function layMotDong(result) {
  if (!result) return null
  if (result?.id) return result
  if (result?.data?.data?.id) return result.data.data
  if (result?.data?.id) return result.data
  return result?.data?.data || result?.data || result
}

export function layTenHienThi(item, keys = []) {
  for (const key of keys) {
    const value = item?.[key]
    if (value !== null && value !== undefined && value !== '') return value
  }
  return (
    item?.ten ||
    item?.tenMon ||
    item?.tenNganh ||
    item?.tenChuongTrinh ||
    item?.tenVersion ||
    item?.ma ||
    item?.id ||
    '-'
  )
}

export const xemChuongTrinhService = {
  layItems,
  layMotDong,

  async layDanhSachNganh() {
    return layItems(await daoTaoXemChuongTrinhService.nganh.getAll({ size: 200 }))
  },

  async layNganhById(nganhId) {
    if (!nganhId) return null
    return layMotDong(await daoTaoXemChuongTrinhService.nganh.getById(nganhId))
  },

  async layChuongTrinhById(chuongTrinhId) {
    if (!chuongTrinhId) return null
    return layMotDong(await daoTaoXemChuongTrinhService.chuongTrinh.getById(chuongTrinhId))
  },

  async layVersionById(versionId) {
    if (!versionId) return null
    return layMotDong(await daoTaoXemChuongTrinhService.chuongTrinhVersion.getById(versionId))
  },

  async layKyById(khungKyId) {
    if (!khungKyId) return null
    return layMotDong(await daoTaoXemChuongTrinhService.khungKy.getById(khungKyId))
  },

  async layNhomKienThucById(nhomKienThucId) {
    if (!nhomKienThucId) return null
    return layMotDong(await daoTaoXemChuongTrinhService.nhomKienThuc.getById(nhomKienThucId))
  },

  async laySyllabusMonById(syllabusMonId) {
    if (!syllabusMonId) return null
    return layMotDong(await daoTaoXemChuongTrinhService.syllabusMonHoc.getById(syllabusMonId))
  },

  async layChuongTrinhTheoNganh(nganhId) {
    const items = layItems(
      await daoTaoXemChuongTrinhService.chuongTrinh.getAll({ size: 200, nganhId })
    )
    return items.filter(item => String(item.nganhId || '') === String(nganhId || ''))
  },

  async layVersionTheoChuongTrinh(chuongTrinhId) {
    const items = layItems(
      await daoTaoXemChuongTrinhService.chuongTrinhVersion.getAll({ size: 200, chuongTrinhId })
    )
    return items.filter(item => String(item.chuongTrinhId || '') === String(chuongTrinhId || ''))
  },

  async layKyTheoVersion(versionId) {
    const items = layItems(
      await daoTaoXemChuongTrinhService.khungKy.getAll({
        size: 200,
        chuongTrinhVersionId: versionId,
        versionId
      })
    )
    return items.filter(
      item => String(item.chuongTrinhVersionId || item.versionId || '') === String(versionId || '')
    )
  },

  async layNhomKienThucTheoVersion(versionId) {
    const items = layItems(
      await daoTaoXemChuongTrinhService.nhomKienThuc.getAll({
        size: 200,
        chuongTrinhVersionId: versionId,
        versionId
      })
    )
    return items.filter(
      item => String(item.chuongTrinhVersionId || item.versionId || '') === String(versionId || '')
    )
  },

  async layMonTheoKyVaNhom(versionId, khungKyId, nhomKienThucId) {
    const items = layItems(
      await daoTaoXemChuongTrinhService.chuongTrinhMon.getAll({
        size: 200,
        chuongTrinhVersionId: versionId,
        versionId,
        khungKyId,
        nhomKienThucId
      })
    )

    return items.filter(
      item =>
        String(item.chuongTrinhVersionId || item.versionId || '') === String(versionId || '') &&
        String(item.khungKyId || '') === String(khungKyId || '') &&
        String(item.nhomKienThucId || '') === String(nhomKienThucId || '')
    )
  },

  async layMonHocTheoChuongTrinhMon(chuongTrinhMonId) {
    const chuongTrinhMon = await daoTaoXemChuongTrinhService.chuongTrinhMon.getById(chuongTrinhMonId)
    const row = chuongTrinhMon?.data?.data || chuongTrinhMon?.data || chuongTrinhMon
    const monHocId = row?.monHocId
    if (!monHocId) return { chuongTrinhMon: row, monHoc: null }
    const monHocRes = await daoTaoXemChuongTrinhService.monHoc.getById(monHocId)
    const monHoc = monHocRes?.data?.data || monHocRes?.data || monHocRes
    return { chuongTrinhMon: row, monHoc }
  },

  async laySyllabusTheoChuongTrinhMon(chuongTrinhMonId) {
    const items = layItems(
      await daoTaoXemChuongTrinhService.syllabusMonHoc.getAll({ size: 200, chuongTrinhMonId })
    )
    return items.filter(item => String(item.chuongTrinhMonId || '') === String(chuongTrinhMonId || ''))
  },

  async layChiTietMonDiem(chuongTrinhMonId, syllabusMonId) {
    const [
      monInfo,
      monTienQuyet,
      quyDoiDiem,
      quyDoiDiemMauGan,
      quyDoiDiemMau,
      cauHinhDanhGiaMau,
      syllabusChuongBai,
      dieuKienMonHoc,
      syllabusMonHocDieuKien,
      dieuKienMonHocGoc
    ] = await Promise.all([
      this.layMonHocTheoChuongTrinhMon(chuongTrinhMonId),
      daoTaoXemChuongTrinhService.monTienQuyet.getAll({ size: 200, monId: chuongTrinhMonId, chuongTrinhMonId }),
      daoTaoXemChuongTrinhService.quyDoiDiem.getAll({ size: 200, chuongTrinhMonId }),
      daoTaoXemChuongTrinhService.chuongTrinhMonQuyDoiDiemMau.getAll({ size: 200, chuongTrinhMonId }),
      daoTaoXemChuongTrinhService.quyDoiDiemMau.getAll({ size: 200 }),
      daoTaoXemChuongTrinhService.cauHinhDanhGiaMau.getAll({ size: 200, chuongTrinhMonId }),
      daoTaoXemChuongTrinhService.syllabusChuongBai.getAll({ size: 200, syllabusMonId }),
      daoTaoXemChuongTrinhService.dieuKienMonHoc.getAll({ size: 200, syllabusMonId }),
      daoTaoXemChuongTrinhService.syllabusMonHocDieuKien.getAll({ size: 200, syllabusMonId }),
      daoTaoXemChuongTrinhService.dieuKienMonHocGoc.getAll({ size: 200 })
    ])

    return {
      monInfo,
      monTienQuyet: layItems(monTienQuyet),
      quyDoiDiem: layItems(quyDoiDiem),
      quyDoiDiemMauGan: layItems(quyDoiDiemMauGan),
      quyDoiDiemMau: layItems(quyDoiDiemMau),
      cauHinhDanhGiaMau: layItems(cauHinhDanhGiaMau),
      syllabusChuongBai: layItems(syllabusChuongBai),
      dieuKienMonHoc: layItems(dieuKienMonHoc),
      syllabusMonHocDieuKien: layItems(syllabusMonHocDieuKien),
      dieuKienMonHocGoc: layItems(dieuKienMonHocGoc)
    }
  },

  async layTaiLieuTheoSyllabusMon(syllabusMonId) {
    const [syllabusTaiLieu, syllabusMonHocTaiLieu, taiLieuGoc] = await Promise.all([
      daoTaoXemChuongTrinhService.syllabusTaiLieu.getAll({ size: 200, syllabusMonId }),
      daoTaoXemChuongTrinhService.syllabusMonHocTaiLieu.getAll({ size: 200, syllabusMonId }),
      daoTaoXemChuongTrinhService.taiLieuGoc.getAll({ size: 200 })
    ])

    return {
      syllabusTaiLieu: layItems(syllabusTaiLieu).filter(
        item => String(item.syllabusMonId || '') === String(syllabusMonId || '')
      ),
      syllabusMonHocTaiLieu: layItems(syllabusMonHocTaiLieu).filter(
        item => String(item.syllabusMonId || '') === String(syllabusMonId || '')
      ),
      taiLieuGoc: layItems(taiLieuGoc)
    }
  }
}
