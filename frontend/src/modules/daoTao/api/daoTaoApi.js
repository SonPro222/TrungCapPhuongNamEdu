import apiClient from '@/core/api/apiClient'

const DAO_TAO_URL = '/dao-tao'
const CHUONG_TRINH_URL = '/chuongTrinh'
const DIEM_URL = '/diem'

function createCrudApi(baseUrl, path) {
    return {
        getAll(params = {}) {
            return apiClient.get(`${baseUrl}/${path}`, { params })
        },

        getById(id) {
            return apiClient.get(`${baseUrl}/${path}/${id}`)
        },

        create(payload) {
            return apiClient.post(`${baseUrl}/${path}`, payload)
        },

        luu(payload) {
            return apiClient.post(`${baseUrl}/${path}/luu`, payload)
        },

        update(id, payload) {
            return apiClient.put(`${baseUrl}/${path}/${id}`, payload)
        },

        delete(id) {
            return apiClient.delete(`${baseUrl}/${path}/${id}`)
        }
    }
}

export const daoTaoApi = {
    nganh: createCrudApi(DAO_TAO_URL, 'nganh'),
    trinhDoDaoTao: {
        ...createCrudApi(DAO_TAO_URL, 'trinh-do-dao-tao'),
        getOptions() { return apiClient.get(`${DAO_TAO_URL}/trinh-do-dao-tao/options`) }
    },
    loaiChuongTrinh: {
        ...createCrudApi(DAO_TAO_URL, 'loai-chuong-trinh'),
        getOptions() { return apiClient.get(`${DAO_TAO_URL}/loai-chuong-trinh/options`) }
    },
    nganhTrinhDoDaoTao: createCrudApi(DAO_TAO_URL, 'nganh-trinh-do-dao-tao'),
    nganhLoaiChuongTrinh: createCrudApi(DAO_TAO_URL, 'nganh-loai-chuong-trinh'),
    nganhHeDaoTao: {
        ...createCrudApi(DAO_TAO_URL, 'nganh-he-dao-tao'),
        getOptions(nganhId) { return apiClient.get(`${DAO_TAO_URL}/nganh-he-dao-tao/options`, { params: { nganhId } }) }
    },
    khungKy: {
        ...createCrudApi(DAO_TAO_URL, 'khung-ky'),
        goiYTheoVersion(versionId) {
            return apiClient.get(`${DAO_TAO_URL}/khung-ky/theo-version/${versionId}/goi-y`)
        },
        taoDuKyConThieu(versionId, payload = {}) {
            return apiClient.post(`${DAO_TAO_URL}/khung-ky/theo-version/${versionId}/tao-du-ky-con-thieu`, payload)
        },
        canhBaoTaiHoc(versionId) {
            return apiClient.get(`${DAO_TAO_URL}/khung-ky/theo-version/${versionId}/canh-bao-tai-hoc`)
        },
        layNghiChuyenKyTheoVersion(versionId) {
            return apiClient.get(`${DAO_TAO_URL}/khung-ky/theo-version/${versionId}/nghi-chuyen-ky`)
        },
        luuNghiChuyenKyTheoVersion(versionId, payload) {
            return apiClient.put(`${DAO_TAO_URL}/khung-ky/theo-version/${versionId}/nghi-chuyen-ky`, payload)
        },
        goiYSinhTuDong(versionId, payload) {
            return apiClient.post(`${DAO_TAO_URL}/khung-ky/theo-version/${versionId}/goi-y-sinh-tu-dong`, payload)
        }
    },
    khungKymau: createCrudApi(DAO_TAO_URL, 'khung-ky-mau'),

    chuongTrinh: {
        ...createCrudApi(CHUONG_TRINH_URL, 'chuong-trinh'),

        getTongThe(chuongTrinhId, versionId = null, options = {}) {
            const params = {}

            if (versionId !== null && versionId !== undefined && versionId !== '') {
                params.versionId = versionId
            }

            if (options?.khungKyId !== null && options?.khungKyId !== undefined && options?.khungKyId !== '') {
                params.khungKyId = options.khungKyId
            }

            if (options?.includeSyllabusDetail !== null && options?.includeSyllabusDetail !== undefined) {
                params.includeSyllabusDetail = Boolean(options.includeSyllabusDetail)
            }

            return apiClient.get(`${CHUONG_TRINH_URL}/chuong-trinh/${chuongTrinhId}/tong-the`, { params })
        }
    },
    chuongTrinhVersion: {
        ...createCrudApi(CHUONG_TRINH_URL, 'chuong-trinh-version'),
        chuyenTrangThai(id, trangThai) {
            return apiClient.patch(`${CHUONG_TRINH_URL}/chuong-trinh-version/${id}/trang-thai`, { trangThai })
        }
    },
    chuongTrinhMon: createCrudApi(CHUONG_TRINH_URL, 'chuong-trinh-mon'),

    monHoc: createCrudApi(CHUONG_TRINH_URL, 'mon-hoc'),
    nhomKienThuc: createCrudApi(CHUONG_TRINH_URL, 'nhom-kien-thuc'),
    nhomTuChon: createCrudApi(CHUONG_TRINH_URL, 'nhom-tu-chon'),
    monTuChon: createCrudApi(CHUONG_TRINH_URL, 'mon-tu-chon'),
    monTienQuyet: createCrudApi(CHUONG_TRINH_URL, 'mon-tien-quyet'),
    quyDoiDiem: createCrudApi(CHUONG_TRINH_URL, 'quy-doi-diem'),
    quyDoiDiemMau: createCrudApi(CHUONG_TRINH_URL, 'quy-doi-diem-mau'),
    chuongTrinhMonQuyDoiDiemMau: createCrudApi(CHUONG_TRINH_URL, 'chuong-trinh-mon-quy-doi-diem-mau'),
    cauHinhDanhGiaMau: createCrudApi(CHUONG_TRINH_URL, 'cau-hinh-danh-gia-mau'),
    cauHinhDanhGia: createCrudApi(DIEM_URL, 'cau-hinh-danh-gia'),

    mucTieuChuongTrinh: createCrudApi(CHUONG_TRINH_URL, 'muc-tieu-chuong-trinh'),
    nangLucDauRa: createCrudApi(CHUONG_TRINH_URL, 'nang-luc-dau-ra'),
    viTriViecLam: createCrudApi(CHUONG_TRINH_URL, 'vi-tri-viec-lam'),
    dieuKienTotNghiep: createCrudApi(CHUONG_TRINH_URL, 'dieu-kien-tot-nghiep'),

    mucTieuChuongTrinhmau: createCrudApi(CHUONG_TRINH_URL, 'muc-tieu-chuong-trinh-mau'),
    nangLucDauRamau: createCrudApi(CHUONG_TRINH_URL, 'nang-luc-dau-ra-mau'),
    viTriViecLammau: createCrudApi(CHUONG_TRINH_URL, 'vi-tri-viec-lam-mau'),
    dieuKienTotNghiepmau: createCrudApi(CHUONG_TRINH_URL, 'dieu-kien-tot-nghiep-mau'),

    chuongTrinhVersionMucTieu: createCrudApi(CHUONG_TRINH_URL, 'chuong-trinh-version-muc-tieu'),
    chuongTrinhVersionNangLuc: createCrudApi(CHUONG_TRINH_URL, 'chuong-trinh-version-nang-luc'),
    chuongTrinhVersionViTriViecLam: createCrudApi(CHUONG_TRINH_URL, 'chuong-trinh-version-vi-tri-viec-lam'),
    chuongTrinhVersionDieuKienTotNghiep: createCrudApi(CHUONG_TRINH_URL, 'chuong-trinh-version-dieu-kien-tot-nghiep'),

    syllabusChuongTrinh: {
        ...createCrudApi(CHUONG_TRINH_URL, 'syllabus-chuong-trinh'),

        dongBoTuMau(payload) {
            return apiClient.post(`${CHUONG_TRINH_URL}/syllabus-chuong-trinh/dong-bo-tu-mau`, payload)
        }
    },
    syllabusChuongTrinhmau: createCrudApi(CHUONG_TRINH_URL, 'syllabus-chuong-trinh-mau'),
    nhomKienThucmau: createCrudApi(CHUONG_TRINH_URL, 'nhom-kien-thuc-mau'),
    nhomTuChonmau: createCrudApi(CHUONG_TRINH_URL, 'nhom-tu-chon-mau'),

    syllabusMonHoc: {
        ...createCrudApi(CHUONG_TRINH_URL, 'syllabus-mon-hoc'),

        getChiTietXem(id) {
            return apiClient.get(`${CHUONG_TRINH_URL}/syllabus-mon-hoc/${id}/xem`)
        }
    },
    syllabusMonHocmau: {
        ...createCrudApi(CHUONG_TRINH_URL, 'syllabus-mon-hoc-mau'),
        /** Tạo syllabus mẫu đầy đủ (thông tin + chương/bài + điều kiện + tài liệu + đánh giá + quy đổi) */
        createFull(payload) {
            return apiClient.post(`${CHUONG_TRINH_URL}/syllabus-mon-hoc-mau/full`, payload)
        },
        /** Cập nhật syllabus mẫu đầy đủ */
        updateFull(id, payload) {
            return apiClient.put(`${CHUONG_TRINH_URL}/syllabus-mon-hoc-mau/${id}/full`, payload)
        },
        /** Lấy syllabus mẫu đầy đủ kèm file list */
        getFull(id) {
            return apiClient.get(`${CHUONG_TRINH_URL}/syllabus-mon-hoc-mau/${id}/full`)
        }
    },
    syllabusMonHocmauChuongBai: createCrudApi(CHUONG_TRINH_URL, 'syllabus-mon-hoc-mau-chuong-bai'),
    syllabusMonHocmauDieuKien: createCrudApi(CHUONG_TRINH_URL, 'syllabus-mon-hoc-mau-dieu-kien'),
    syllabusMonHocmauTaiLieu: createCrudApi(CHUONG_TRINH_URL, 'syllabus-mon-hoc-mau-tai-lieu'),
    syllabusMonHocMauFile: {
        /** Upload file gốc cho syllabus mẫu — dùng FormData */
        upload(syllabusMonHocMauId, file, loaiTaiLieu = 'FILE_NGUON_IMPORT', laFileNguon = true, ghiChu = '') {
            const form = new FormData()
            form.append('file', file)
            form.append('loaiTaiLieu', loaiTaiLieu)
            form.append('laFileNguon', String(laFileNguon))
            if (ghiChu) form.append('ghiChu', ghiChu)
            return apiClient.post(
                `${CHUONG_TRINH_URL}/syllabus-mon-hoc-mau/${syllabusMonHocMauId}/files`,
                form,
                { headers: { 'Content-Type': 'multipart/form-data' } }
            )
        },
        /** Danh sách file theo syllabusMonHocMauId */
        list(syllabusMonHocMauId) {
            return apiClient.get(`${CHUONG_TRINH_URL}/syllabus-mon-hoc-mau/${syllabusMonHocMauId}/files`)
        },
        /** Parse preview — trả text + cảnh báo, KHÔNG lưu vào DB nghiệp vụ */
        parsePreview(fileId) {
            return apiClient.post(`${CHUONG_TRINH_URL}/syllabus-mon-hoc-mau-files/${fileId}/parse-preview`)
        },
        /**
         * Lấy binary blob để xem inline (giữ Bearer token).
         * Caller tự tạo objectURL rồi mở window.open / revoke sau khi dùng.
         */
        view(fileId) {
            return apiClient.get(
                `${CHUONG_TRINH_URL}/syllabus-mon-hoc-mau-files/${fileId}/view`,
                { responseType: 'blob' }
            )
        },
        /** Lấy binary blob để download */
        download(fileId) {
            return apiClient.get(
                `${CHUONG_TRINH_URL}/syllabus-mon-hoc-mau-files/${fileId}/download`,
                { responseType: 'blob' }
            )
        },
        /** Xóa file */
        delete(fileId) {
            return apiClient.delete(`${CHUONG_TRINH_URL}/syllabus-mon-hoc-mau-files/${fileId}`)
        },
        /**
         * Upload file theo monHocId — tự tạo syllabus tối thiểu nếu môn chưa có syllabus.
         * Response kèm noiDungText + canhBao để FE populate form (preview only).
         */
        uploadByMonHocId(monHocId, file, loaiTaiLieu = 'FILE_NGUON_IMPORT', laFileNguon = true, ghiChu = '') {
            const form = new FormData()
            form.append('file', file)
            form.append('loaiTaiLieu', loaiTaiLieu)
            form.append('laFileNguon', String(laFileNguon))
            if (ghiChu) form.append('ghiChu', ghiChu)
            return apiClient.post(
                `${CHUONG_TRINH_URL}/syllabus-mon-hoc-mau/by-mon-hoc/${monHocId}/files`,
                form,
                { headers: { 'Content-Type': 'multipart/form-data' } }
            )
        },
        /**
         * Import từ file: upload + parse + lưu DB theo syllabusId.
         * Trả về SyllabusMonHocMauFullResponse để FE load lên form.
         */
        importFileBySyllabusId(syllabusId, file) {
            const form = new FormData()
            form.append('file', file)
            return apiClient.post(
                `${CHUONG_TRINH_URL}/syllabus-mon-hoc-mau/${syllabusId}/import-file`,
                form,
                { headers: { 'Content-Type': 'multipart/form-data' } }
            )
        },
        /**
         * Import từ file: upload + parse + lưu DB theo monHocId.
         * Tự tạo syllabus tối thiểu nếu chưa có.
         */
        importFileByMonHocId(monHocId, file) {
            const form = new FormData()
            form.append('file', file)
            return apiClient.post(
                `${CHUONG_TRINH_URL}/syllabus-mon-hoc-mau/by-mon-hoc/${monHocId}/import-file`,
                form,
                { headers: { 'Content-Type': 'multipart/form-data' } }
            )
        },
        /**
         * Re-import từ file đã lưu trong DB (theo fileId).
         * Parse lại + cập nhật dữ liệu syllabus.
         */
        importByFileId(fileId) {
            return apiClient.post(`${CHUONG_TRINH_URL}/syllabus-mon-hoc-mau-files/${fileId}/import`)
        }
    },

    syllabusMonHocFile: {
        /** Upload file mới cho syllabus môn học áp dụng */
        upload(syllabusMonHocId, file, loaiTaiLieu = 'FILE_NGUON_IMPORT', laFileNguon = true, ghiChu = '') {
            const form = new FormData()
            form.append('file', file)
            form.append('loaiTaiLieu', loaiTaiLieu)
            form.append('laFileNguon', String(laFileNguon))
            if (ghiChu) form.append('ghiChu', ghiChu)
            return apiClient.post(
                `${CHUONG_TRINH_URL}/syllabus-mon-hoc/${syllabusMonHocId}/files`,
                form,
                { headers: { 'Content-Type': 'multipart/form-data' } }
            )
        },
        /** Danh sách file đã gán vào syllabus môn học áp dụng */
        list(syllabusMonHocId) {
            return apiClient.get(`${CHUONG_TRINH_URL}/syllabus-mon-hoc/${syllabusMonHocId}/files`)
        },
        /** Copy file từ syllabus mẫu sang syllabus môn học áp dụng */
        copyFromMau(syllabusMonHocId, mauFileId) {
            return apiClient.post(
                `${CHUONG_TRINH_URL}/syllabus-mon-hoc/${syllabusMonHocId}/copy-from-mau-file/${mauFileId}`
            )
        },
        /** Xem file inline (blob) */
        view(fileId) {
            return apiClient.get(
                `${CHUONG_TRINH_URL}/syllabus-mon-hoc-files/${fileId}/view`,
                { responseType: 'blob' }
            )
        },
        /** Download file về máy (blob) */
        download(fileId) {
            return apiClient.get(
                `${CHUONG_TRINH_URL}/syllabus-mon-hoc-files/${fileId}/download`,
                { responseType: 'blob' }
            )
        },
        /** Thay thế file (replace) — giữ nguyên id, cập nhật binary */
        replace(fileId, file, loaiTaiLieu = 'FILE_NGUON_IMPORT', ghiChu = '') {
            const form = new FormData()
            form.append('file', file)
            form.append('loaiTaiLieu', loaiTaiLieu)
            if (ghiChu) form.append('ghiChu', ghiChu)
            return apiClient.post(
                `${CHUONG_TRINH_URL}/syllabus-mon-hoc-files/${fileId}/replace`,
                form,
                { headers: { 'Content-Type': 'multipart/form-data' } }
            )
        },
        /** Xóa file */
        delete(fileId) {
            return apiClient.delete(`${CHUONG_TRINH_URL}/syllabus-mon-hoc-files/${fileId}`)
        },
        /** Thông tin file theo fileId (metadata, không trả binary) */
        getInfo(fileId) {
            return apiClient.get(`${CHUONG_TRINH_URL}/syllabus-mon-hoc-files/${fileId}`)
        }
    },

    dieuKienMonHoc: createCrudApi(CHUONG_TRINH_URL, 'dieu-kien-mon-hoc'),
    dieuKienMonHocmau: createCrudApi(CHUONG_TRINH_URL, 'dieu-kien-mon-hoc-mau'),
    syllabusMonHocDieuKien: createCrudApi(CHUONG_TRINH_URL, 'syllabus-mon-hoc-dieu-kien'),

    taiLieumau: createCrudApi(CHUONG_TRINH_URL, 'tai-lieu-mau'),
    syllabusMonHocTaiLieu: createCrudApi(CHUONG_TRINH_URL, 'syllabus-mon-hoc-tai-lieu'),
    syllabusChuongBai: createCrudApi(CHUONG_TRINH_URL, 'syllabus-chuong-bai'),
    syllabusTaiLieu: createCrudApi(CHUONG_TRINH_URL, 'syllabus-tai-lieu')
}


