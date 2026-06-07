import { sinhVienApi } from '../api/sinhVienApi'

function layNoiDungPage(response) {
    if (Array.isArray(response)) return response
    if (Array.isArray(response?.content)) return response.content
    if (Array.isArray(response?.data?.content)) return response.data.content
    if (Array.isArray(response?.data)) return response.data
    return []
}

function taiBlobExcel(response, tenFileMacDinh) {
    const fileData = response?.data ?? response

    if (!fileData) {
        throw new Error('Không nhận được dữ liệu file Excel từ server')
    }

    const blob = fileData instanceof Blob
        ? fileData
        : new Blob([fileData], {
            type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
        })

    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')

    link.href = url

    const contentDisposition = response?.headers?.['content-disposition']
    const tenFileTuHeader = layTenFileTuContentDisposition(contentDisposition)

    link.download = tenFileTuHeader || tenFileMacDinh

    document.body.appendChild(link)
    link.click()

    link.remove()
    window.URL.revokeObjectURL(url)
}

function layTenFileTuContentDisposition(contentDisposition) {
    if (!contentDisposition) return ''

    const utf8Match = contentDisposition.match(/filename\*=UTF-8''([^;]+)/i)
    if (utf8Match?.[1]) {
        return decodeURIComponent(utf8Match[1])
    }

    const normalMatch = contentDisposition.match(/filename="?([^"]+)"?/i)
    if (normalMatch?.[1]) {
        return normalMatch[1]
    }

    return ''
}

const PAGE_KHUNG = { page: 0, size: 200 }
const PAGE_DU_LIEU_LON = { page: 0, size: 100 }

export const sinhVienService = {
    async layNganh() {
        return layNoiDungPage(await sinhVienApi.nganh.getAll(PAGE_KHUNG))
    },

    async layLopHanhChinh() {
        return layNoiDungPage(await sinhVienApi.lopHanhChinh.getAll(PAGE_KHUNG))
    },

    async layLopHanhChinhTheoVersion(chuongTrinhVersionId) {
        if (!chuongTrinhVersionId) return []
        return layNoiDungPage(await sinhVienApi.lopHanhChinh.getTheoVersion(chuongTrinhVersionId, PAGE_KHUNG))
    },

    async layChuongTrinh() {
        return layNoiDungPage(await sinhVienApi.chuongTrinh.getAll(PAGE_KHUNG))
    },

    async layVersion() {
        return layNoiDungPage(await sinhVienApi.chuongTrinhVersion.getAll(PAGE_KHUNG))
    },

    async laySinhVienChuongTrinh(params = {}) {
        return layNoiDungPage(await sinhVienApi.sinhVienChuongTrinh.getAll({ ...PAGE_DU_LIEU_LON, ...params }))
    },

    async laySinhVienTheoNganhChuongTrinhVersion(nganhId, chuongTrinhId, chuongTrinhVersionId, page = 0, size = 100) {
        const params = { page, size }

        if (nganhId) params.nganhId = nganhId
        if (chuongTrinhId) params.chuongTrinhId = chuongTrinhId
        if (chuongTrinhVersionId) params.chuongTrinhVersionId = chuongTrinhVersionId

        return layNoiDungPage(await sinhVienApi.sinhVien.getTheoNganhChuongTrinhVersion(params))
    },

    async tiepNhanSinhVien(payload) {
        return sinhVienApi.sinhVien.tiepNhan(payload)
    },

    async tiepNhanSinhVienCoFile(payload, files) {
        return sinhVienApi.sinhVien.tiepNhanCoFile(payload, files)
    },

    async capNhatSinhVien(id, payload) {
        return sinhVienApi.sinhVien.update(id, payload)
    },

    async xoaSinhVien(id) {
        return sinhVienApi.sinhVien.delete(id)
    },

    layUrlTaiTep(id) {
        if (!id) return ''
        return sinhVienApi.tepDinhKem.downloadUrl(id)
    },

    layUrlXemTep(id) {
        if (!id) return ''
        return sinhVienApi.tepDinhKem.previewUrl(id)
    },

    async uploadTepSinhVien(file, sinhVienId, nghiepVu, moTa, nguoiGuiTen) {
        if (!file || !sinhVienId) return null

        return sinhVienApi.tepDinhKem.upload(file, {
            module: 'sinh_vien',
            nghiepVu,
            doiTuongId: sinhVienId,
            nguoiGuiLoai: 'NHAN_VIEN',
            nguoiGuiId: 0,
            nguoiGuiTen: nguoiGuiTen || 'Nhân viên tiếp nhận',
            moTa
        })
    },

    async layLopHocPhan(params = {}) {
        return layNoiDungPage(await sinhVienApi.lopHocPhan.getAll({ ...PAGE_DU_LIEU_LON, ...params }))
    },

    async layLopHocPhanChuongTrinhMon(params = {}) {
        return layNoiDungPage(await sinhVienApi.lopHocPhanChuongTrinhMon.getAll({ ...PAGE_DU_LIEU_LON, ...params }))
    },

    async taoLopHocPhan(payload) {
        return sinhVienApi.lopHocPhan.create(payload)
    },

    async taoLopHocPhanChuongTrinhMon(payload) {
        return sinhVienApi.lopHocPhanChuongTrinhMon.create(payload)
    },

    async laySinhVienLopHocPhan(params = {}) {
        return layNoiDungPage(await sinhVienApi.sinhVienLopHocPhan.getAll({ ...PAGE_DU_LIEU_LON, ...params }))
    },

    async layChuongTrinhMon() {
        return layNoiDungPage(await sinhVienApi.chuongTrinhMon.getAll(PAGE_KHUNG))
    },

    async laySyllabusMonHoc(params = {}) {
        return layNoiDungPage(await sinhVienApi.syllabusMonHoc.getAll({ ...PAGE_DU_LIEU_LON, ...params }))
    },

    async dangKyLopHocPhan(payload) {
        return sinhVienApi.sinhVienLopHocPhan.create(payload)
    },

    async layMienTruBaoLuu(params = {}) {
        return layNoiDungPage(await sinhVienApi.mienTruBaoLuu.getAll({ ...PAGE_DU_LIEU_LON, ...params }))
    },

    async taoMienTruBaoLuu(payload) {
        return sinhVienApi.mienTruBaoLuu.create(payload)
    },

    async taoLopHanhChinh(payload) {
        return sinhVienApi.lopHanhChinh.create(payload)
    },

    async capNhatLopHanhChinh(id, payload) {
        return sinhVienApi.lopHanhChinh.update(id, payload)
    },

    async xoaLopHanhChinh(id) {
        return sinhVienApi.lopHanhChinh.delete(id)
    },

    async capNhatLopHocPhan(id, payload) {
        return sinhVienApi.lopHocPhan.update(id, payload)
    },

    async xoaLopHocPhan(id) {
        return sinhVienApi.lopHocPhan.delete(id)
    },

    async layKhungKy() {
        return layNoiDungPage(await sinhVienApi.khungKy.getAll(PAGE_KHUNG))
    },

    async layBaoLuu(params = {}) {
        return layNoiDungPage(await sinhVienApi.baoLuu.getAll({ ...PAGE_DU_LIEU_LON, ...params }))
    },

    async taoBaoLuu(payload) {
        return sinhVienApi.baoLuu.create(payload)
    },

    async choSinhVienDiHocLai(id, payload) {
        return sinhVienApi.baoLuu.diHocLai(id, payload)
    },

    async huyBaoLuu(id) {
        return sinhVienApi.baoLuu.huy(id)
    },
}

// ── CÁC PHƯƠNG THỨC MỚI CHO DANH SÁCH & CHI TIẾT SINH VIÊN ──────────────────

Object.assign(sinhVienService, {

    // Tải toàn bộ sinh viên không lọc để hiển thị danh sách toàn trường
    async layTatCaSinhVien() {
        return layNoiDungPage(await sinhVienApi.sinhVien.getAll({ page: 0, size: 1000 }))
    },

    // Xuất Excel danh sách sinh viên toàn trường
    async xuatExcelSinhVienToanTruong() {
        const response = await sinhVienApi.sinhVien.exportExcelToanTruong()
        taiBlobExcel(response, 'danh-sach-sinh-vien-toan-truong.xlsx')
    },

    // Xuất Excel danh sách sinh viên theo ngành / chương trình / version
    async xuatExcelSinhVienTheoNganhVersion(params = {}) {
        const response = await sinhVienApi.sinhVien.exportExcelTheoNganhVersion(params)
        taiBlobExcel(response, 'danh-sach-sinh-vien-theo-nganh-version.xlsx')
    },

    // Lấy sinh viên theo ID, trả object, không phải array
    async laySinhVienTheoId(id) {
        const res = await sinhVienApi.sinhVien.getById(id)
        return res?.data?.data ?? res?.data ?? res
    },

    // Lấy danh sách SinhVienChuongTrinh theo sinhVienId
    // Endpoint: GET /api/sinh-vien/chuong-trinh?sinhVienId=X&page=0&size=50
    async laySinhVienChuongTrinhTheoSinhVienId(sinhVienId) {
        if (!sinhVienId) return []

        return layNoiDungPage(
            await sinhVienApi.sinhVienChuongTrinh.getAll({
                sinhVienId,
                page: 0,
                size: 50
            })
        )
    },

    // Lấy các lớp học phần của một sinh viên
    // Endpoint: GET /api/giang-day/sinh-vien-lop-hoc-phan?sinhVienId=X&page=0&size=200
    async laySinhVienLopHocPhanTheoSinhVien(sinhVienId) {
        if (!sinhVienId) return []

        return layNoiDungPage(
            await sinhVienApi.sinhVienLopHocPhan.getAll({
                sinhVienId,
                page: 0,
                size: 200
            })
        )
    },

    // Lấy kết quả điểm của một sinh viên
    // Endpoint: GET /api/diem/ket-qua-lop-hoc-phan?sinhVienId=X&page=0&size=200
    async layKetQuaLopHocPhanTheoSinhVien(sinhVienId) {
        if (!sinhVienId) return []

        return layNoiDungPage(
            await sinhVienApi.ketQuaLopHocPhan.getAll({
                sinhVienId,
                page: 0,
                size: 200
            })
        )
    },

    // Lấy lịch học theo lớp học phần
    // Endpoint: GET /api/giang-day/lich-hoc?lopHocPhanId=X&page=0&size=200
    async layLichHocTheoLop(lopHocPhanId) {
        if (!lopHocPhanId) return []

        return layNoiDungPage(
            await sinhVienApi.lichHoc.getAll({
                lopHocPhanId,
                page: 0,
                size: 200,
                sortBy: 'ngayHoc',
                sortDir: 'asc'
            })
        )
    },
})

// ── CHI TIẾT ĐIỂM + ĐIỂM DANH ────────────────────────────────────────────────

Object.assign(sinhVienService, {

    // Cấu hình cột điểm của lớp học phần
    // GET /api/diem/cau-hinh-danh-gia?lopHocPhanId=X&page=0&size=50
    async layCauHinhDanhGiaTheoLop(lopHocPhanId) {
        if (!lopHocPhanId) return []

        return layNoiDungPage(
            await sinhVienApi.cauHinhDanhGia.getAll({
                lopHocPhanId,
                page: 0,
                size: 50
            })
        )
    },

    // Bài kiểm tra của lớp học phần
    // GET /api/diem/bai-kiem-tra?lopHocPhanId=X&page=0&size=50
    async layBaiKiemTraTheoLop(lopHocPhanId) {
        if (!lopHocPhanId) return []

        return layNoiDungPage(
            await sinhVienApi.baiKiemTra.getAll({
                lopHocPhanId,
                page: 0,
                size: 50
            })
        )
    },

    // Điểm chi tiết từng cột của sinh viên trong lớp
    // GET /api/diem/diem-chi-tiet?sinhVienId=X&lopHocPhanId=X&page=0&size=100
    async layDiemChiTietTheoSVvaLop(sinhVienId, lopHocPhanId) {
        if (!sinhVienId || !lopHocPhanId) return []

        return layNoiDungPage(
            await sinhVienApi.diemChiTiet.getAll({
                sinhVienId,
                lopHocPhanId,
                page: 0,
                size: 100
            })
        )
    },

    // Điểm danh của sinh viên trong lớp
    // GET /api/giang-day/diem-danh?sinhVienId=X&lopHocPhanId=X&page=0&size=200
    async layDiemDanhTheoSVvaLop(sinhVienId, lopHocPhanId) {
        if (!sinhVienId || !lopHocPhanId) return []

        return layNoiDungPage(
            await sinhVienApi.diemDanh.getAll({
                sinhVienId,
                lopHocPhanId,
                page: 0,
                size: 200,
                sortBy: 'id',
                sortDir: 'asc'
            })
        )
    },
})