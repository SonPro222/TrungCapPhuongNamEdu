import apiClient from '@/core/api/apiClient'
import { ENV } from '@/core/config/env'

function layDuLieuUpload(response) {
    if (!response) return null

    if (response.success !== undefined && response.data !== undefined) {
        return response.data
    }

    if (response.data?.success !== undefined && response.data?.data !== undefined) {
        return response.data.data
    }

    if (response.data?.id) {
        return response.data
    }

    return response
}

function layBackendOrigin() {
    return String(ENV.API_BASE_URL || '')
        .replace(/\/api\/?$/, '')
        .replace(/\/$/, '')
}

export const tepDinhKemUploadService = {
    async upload({
                     file,
                     module,
                     nghiepVu,
                     doiTuongId,

                     nguoiGuiLoai = 'ADMIN',
                     nguoiGuiId = 1,
                     nguoiGuiTen = 'Admin',

                     moTa,

                     nganhId,
                     chuongTrinhId,
                     chuongTrinhVersionId,

                     namHocId,
                     hocKyId,
                     lopHanhChinhId,
                     lopHocPhanId,

                     monHocId,
                     chuongTrinhMonId,

                     giangVienId,
                     sinhVienId,

                     baiHocId,
                     baiTapId,
                     labId,
                     baiNopId,
                     lanNop,

                     syllabusChuongTrinhId,
                     syllabusMonHocId,
                     taiLieuGocId,
                     tangNghiepVu
                 }) {
        if (!file) {
            throw new Error('Chưa chọn file để tải lên.')
        }

        const data = {
            module,
            nghiepVu,
            doiTuongId,

            nguoiGuiLoai,
            nguoiGuiId,
            nguoiGuiTen,

            moTa: moTa || '',

            nganhId,
            chuongTrinhId,
            chuongTrinhVersionId,

            namHocId,
            hocKyId,
            lopHanhChinhId,
            lopHocPhanId,

            monHocId,
            chuongTrinhMonId,

            giangVienId,
            sinhVienId,

            baiHocId,
            baiTapId,
            labId,
            baiNopId,
            lanNop,

            syllabusChuongTrinhId,
            syllabusMonHocId,
            taiLieuGocId,
            tangNghiepVu
        }

        const formData = new FormData()
        formData.append('file', file)
        formData.append(
            'data',
            new Blob([JSON.stringify(data)], {
                type: 'application/json'
            })
        )

        const response = await apiClient.post('/he-thong/tep-dinh-kem/upload', formData, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        })

        return layDuLieuUpload(response)
    },

    async uploadMany({
                         files,
                         module,
                         nghiepVu,
                         doiTuongId,

                         nguoiGuiLoai = 'ADMIN',
                         nguoiGuiId = 1,
                         nguoiGuiTen = 'Admin',

                         moTa,

                         nganhId,
                         chuongTrinhId,
                         chuongTrinhVersionId,

                         namHocId,
                         hocKyId,
                         lopHanhChinhId,
                         lopHocPhanId,

                         monHocId,
                         chuongTrinhMonId,

                         giangVienId,
                         sinhVienId,

                         baiHocId,
                         baiTapId,
                         labId,
                         baiNopId,
                         lanNop,

                         syllabusChuongTrinhId,
                         syllabusMonHocId,
                         taiLieuGocId,
                         tangNghiepVu
                     }) {
        const danhSachFile = Array.isArray(files) ? files : []

        if (!danhSachFile.length) {
            throw new Error('Chưa chọn file để tải lên.')
        }

        const ketQua = []

        for (const file of danhSachFile) {
            const moTaFile = file.webkitRelativePath || file.name || moTa || ''

            const fileInfo = await this.upload({
                file,
                module,
                nghiepVu,
                doiTuongId,

                nguoiGuiLoai,
                nguoiGuiId,
                nguoiGuiTen,

                moTa: moTaFile,

                nganhId,
                chuongTrinhId,
                chuongTrinhVersionId,

                namHocId,
                hocKyId,
                lopHanhChinhId,
                lopHocPhanId,

                monHocId,
                chuongTrinhMonId,

                giangVienId,
                sinhVienId,

                baiHocId,
                baiTapId,
                labId,
                baiNopId,
                lanNop,

                syllabusChuongTrinhId,
                syllabusMonHocId,
                taiLieuGocId,
                tangNghiepVu
            })

            ketQua.push({
                ...fileInfo,
                tenHienThi: moTaFile,
                fileName: file.name,
                duongDanThuMuc: file.webkitRelativePath || '',
                contentType: file.type || fileInfo?.contentType || '',
                dungLuong: file.size || fileInfo?.dungLuong || 0
            })
        }

        return ketQua
    },

    taoDuongDanTaiFile(fileInfo) {
        const id = fileInfo?.id || fileInfo?.tepDinhKemId

        if (id) {
            return `${layBackendOrigin()}/api/he-thong/tep-dinh-kem/${id}/download`
        }

        const duongDan = fileInfo?.duongDanTuongDoi || fileInfo?.duongDanDayDu || ''

        if (String(duongDan).startsWith('/api/')) {
            return `${layBackendOrigin()}${duongDan}`
        }

        return duongDan
    }
}