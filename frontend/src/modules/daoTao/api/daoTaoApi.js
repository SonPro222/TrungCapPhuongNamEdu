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
    trinhDoDaoTao: createCrudApi(DAO_TAO_URL, 'trinh-do-dao-tao'),
    loaiChuongTrinh: createCrudApi(DAO_TAO_URL, 'loai-chuong-trinh'),
    nganhTrinhDoDaoTao: createCrudApi(DAO_TAO_URL, 'nganh-trinh-do-dao-tao'),
    nganhLoaiChuongTrinh: createCrudApi(DAO_TAO_URL, 'nganh-loai-chuong-trinh'),
    khungKy: createCrudApi(DAO_TAO_URL, 'khung-ky'),
    khungKyGoc: createCrudApi(DAO_TAO_URL, 'khung-ky-goc'),

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
    chuongTrinhVersion: createCrudApi(CHUONG_TRINH_URL, 'chuong-trinh-version'),
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

    mucTieuChuongTrinhGoc: createCrudApi(CHUONG_TRINH_URL, 'muc-tieu-chuong-trinh-goc'),
    nangLucDauRaGoc: createCrudApi(CHUONG_TRINH_URL, 'nang-luc-dau-ra-goc'),
    viTriViecLamGoc: createCrudApi(CHUONG_TRINH_URL, 'vi-tri-viec-lam-goc'),
    dieuKienTotNghiepGoc: createCrudApi(CHUONG_TRINH_URL, 'dieu-kien-tot-nghiep-goc'),

    chuongTrinhVersionMucTieu: createCrudApi(CHUONG_TRINH_URL, 'chuong-trinh-version-muc-tieu'),
    chuongTrinhVersionNangLuc: createCrudApi(CHUONG_TRINH_URL, 'chuong-trinh-version-nang-luc'),
    chuongTrinhVersionViTriViecLam: createCrudApi(CHUONG_TRINH_URL, 'chuong-trinh-version-vi-tri-viec-lam'),
    chuongTrinhVersionDieuKienTotNghiep: createCrudApi(CHUONG_TRINH_URL, 'chuong-trinh-version-dieu-kien-tot-nghiep'),

    syllabusChuongTrinh: createCrudApi(CHUONG_TRINH_URL, 'syllabus-chuong-trinh'),
    syllabusChuongTrinhGoc: createCrudApi(CHUONG_TRINH_URL, 'syllabus-chuong-trinh-goc'),
    nhomKienThucGoc: createCrudApi(CHUONG_TRINH_URL, 'nhom-kien-thuc-goc'),
    nhomTuChonGoc: createCrudApi(CHUONG_TRINH_URL, 'nhom-tu-chon-goc'),

    syllabusMonHoc: {
        ...createCrudApi(CHUONG_TRINH_URL, 'syllabus-mon-hoc'),

        getChiTietXem(id) {
            return apiClient.get(`${CHUONG_TRINH_URL}/syllabus-mon-hoc/${id}/xem`)
        }
    },
    syllabusMonHocGoc: createCrudApi(CHUONG_TRINH_URL, 'syllabus-mon-hoc-goc'),
    syllabusMonHocGocChuongBai: createCrudApi(CHUONG_TRINH_URL, 'syllabus-mon-hoc-goc-chuong-bai'),
    syllabusMonHocGocDieuKien: createCrudApi(CHUONG_TRINH_URL, 'syllabus-mon-hoc-goc-dieu-kien'),
    syllabusMonHocGocTaiLieu: createCrudApi(CHUONG_TRINH_URL, 'syllabus-mon-hoc-goc-tai-lieu'),

    dieuKienMonHoc: createCrudApi(CHUONG_TRINH_URL, 'dieu-kien-mon-hoc'),
    dieuKienMonHocGoc: createCrudApi(CHUONG_TRINH_URL, 'dieu-kien-mon-hoc-goc'),
    syllabusMonHocDieuKien: createCrudApi(CHUONG_TRINH_URL, 'syllabus-mon-hoc-dieu-kien'),

    taiLieuGoc: createCrudApi(CHUONG_TRINH_URL, 'tai-lieu-goc'),
    syllabusMonHocTaiLieu: createCrudApi(CHUONG_TRINH_URL, 'syllabus-mon-hoc-tai-lieu'),
    syllabusChuongBai: createCrudApi(CHUONG_TRINH_URL, 'syllabus-chuong-bai'),
    syllabusTaiLieu: createCrudApi(CHUONG_TRINH_URL, 'syllabus-tai-lieu')
}


