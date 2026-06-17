import { reactive } from 'vue'
import { thongTinTruong as thongTinTruongMacDinh } from '@/modules/public/data/thongTinTruongData.js'
import { danhSachNganhDaoTao as danhSachNganhDaoTaoMacDinh } from '@/modules/public/data/nganhDaoTaoData.js'
import { danhSachTinTuc as danhSachTinTucMacDinh } from '@/modules/public/data/tinTucData.js'
import { danhSachCamNhanHocVien as danhSachCamNhanHocVienMacDinh } from '@/modules/public/data/camNhanHocVienData.js'

const KEY_THONG_TIN_TRUONG = 'PUBLIC_ADMIN_THONG_TIN_TRUONG'
const KEY_MANG_XA_HOI = 'PUBLIC_ADMIN_MANG_XA_HOI'
const KEY_NGANH_DAO_TAO = 'PUBLIC_ADMIN_NGANH_DAO_TAO'
const KEY_TIN_TUC = 'PUBLIC_ADMIN_TIN_TUC'
const KEY_CAM_NHAN_HOC_VIEN = 'PUBLIC_ADMIN_CAM_NHAN_HOC_VIEN'

const mangXaHoiMacDinh = {
    facebook: 'https://www.facebook.com/',
    youtube: 'https://www.youtube.com/',
    tiktok: 'https://www.tiktok.com/',
    zalo: 'https://zalo.me/'
}

const docLocalStorage = (key, giaTriMacDinh) => {
    try {
        const duLieu = localStorage.getItem(key)

        if (!duLieu) {
            return Array.isArray(giaTriMacDinh)
                ? [...giaTriMacDinh]
                : { ...giaTriMacDinh }
        }

        const duLieuDaParse = JSON.parse(duLieu)

        if (Array.isArray(giaTriMacDinh)) {
            return Array.isArray(duLieuDaParse)
                ? duLieuDaParse
                : [...giaTriMacDinh]
        }

        return {
            ...giaTriMacDinh,
            ...duLieuDaParse
        }
    } catch (error) {
        console.error('Không đọc được dữ liệu public từ localStorage:', error)

        return Array.isArray(giaTriMacDinh)
            ? [...giaTriMacDinh]
            : { ...giaTriMacDinh }
    }
}

const ghiLocalStorage = (key, giaTri) => {
    localStorage.setItem(key, JSON.stringify(giaTri))

    window.dispatchEvent(
        new CustomEvent('public-noi-dung-thay-doi', {
            detail: {
                key,
                giaTri
            }
        })
    )
}

const taoSlug = (chuoi) => {
    return String(chuoi || '')
        .normalize('NFD')
        .replace(/[\u0300-\u036f]/g, '')
        .replace(/đ/g, 'd')
        .replace(/Đ/g, 'D')
        .toLowerCase()
        .trim()
        .replace(/[^a-z0-9]+/g, '-')
        .replace(/^-+|-+$/g, '')
}

const taoIdNganh = (tenNganh) => {
    const slug = taoSlug(tenNganh)

    if (slug) {
        return slug
    }

    return `nganh-${Date.now()}`
}

const taoIdTinTuc = (tieuDe) => {
    const slug = taoSlug(tieuDe)

    if (slug) {
        return slug
    }

    return `tin-tuc-${Date.now()}`
}

const taoSlugCamNhan = (tenHocVien) => {
    const slug = taoSlug(tenHocVien)

    if (slug) {
        return slug
    }

    return `hoc-vien-${Date.now()}`
}

const state = reactive({
    thongTinTruong: docLocalStorage(KEY_THONG_TIN_TRUONG, thongTinTruongMacDinh),
    mangXaHoi: docLocalStorage(KEY_MANG_XA_HOI, mangXaHoiMacDinh),
    danhSachNganhDaoTao: docLocalStorage(KEY_NGANH_DAO_TAO, danhSachNganhDaoTaoMacDinh),
    danhSachTinTuc: docLocalStorage(KEY_TIN_TUC, danhSachTinTucMacDinh),
    danhSachCamNhanHocVien: docLocalStorage(KEY_CAM_NHAN_HOC_VIEN, danhSachCamNhanHocVienMacDinh)
})

export const publicNoiDungStore = {
    state,

    layThongTinTruong() {
        state.thongTinTruong = docLocalStorage(KEY_THONG_TIN_TRUONG, thongTinTruongMacDinh)
        return state.thongTinTruong
    },

    luuThongTinTruong(duLieu) {
        const duLieuMoi = {
            ...state.thongTinTruong,
            ...duLieu
        }

        state.thongTinTruong = duLieuMoi
        ghiLocalStorage(KEY_THONG_TIN_TRUONG, duLieuMoi)

        return duLieuMoi
    },

    khoiPhucThongTinTruongMacDinh() {
        state.thongTinTruong = {
            ...thongTinTruongMacDinh
        }

        localStorage.removeItem(KEY_THONG_TIN_TRUONG)

        window.dispatchEvent(
            new CustomEvent('public-noi-dung-thay-doi', {
                detail: {
                    key: KEY_THONG_TIN_TRUONG,
                    giaTri: state.thongTinTruong
                }
            })
        )

        return state.thongTinTruong
    },

    layMangXaHoi() {
        state.mangXaHoi = docLocalStorage(KEY_MANG_XA_HOI, mangXaHoiMacDinh)
        return state.mangXaHoi
    },

    luuMangXaHoi(duLieu) {
        const duLieuMoi = {
            ...state.mangXaHoi,
            ...duLieu
        }

        state.mangXaHoi = duLieuMoi
        ghiLocalStorage(KEY_MANG_XA_HOI, duLieuMoi)

        return duLieuMoi
    },

    khoiPhucMangXaHoiMacDinh() {
        state.mangXaHoi = {
            ...mangXaHoiMacDinh
        }

        localStorage.removeItem(KEY_MANG_XA_HOI)

        window.dispatchEvent(
            new CustomEvent('public-noi-dung-thay-doi', {
                detail: {
                    key: KEY_MANG_XA_HOI,
                    giaTri: state.mangXaHoi
                }
            })
        )

        return state.mangXaHoi
    },

    layDanhSachNganhDaoTao() {
        state.danhSachNganhDaoTao = docLocalStorage(KEY_NGANH_DAO_TAO, danhSachNganhDaoTaoMacDinh)
        return state.danhSachNganhDaoTao
    },

    luuDanhSachNganhDaoTao(danhSach) {
        state.danhSachNganhDaoTao = Array.isArray(danhSach) ? danhSach : []
        ghiLocalStorage(KEY_NGANH_DAO_TAO, state.danhSachNganhDaoTao)
        return state.danhSachNganhDaoTao
    },

    themNganhDaoTao(duLieu) {
        const idMoi = duLieu.id || taoIdNganh(duLieu.title || duLieu.tenNganh)

        const nganhMoi = {
            id: idMoi,
            title: duLieu.title || duLieu.tenNganh || '',
            category: duLieu.category || duLieu.khoiNganh || '',
            desc: duLieu.desc || duLieu.moTaNgan || '',
            image: duLieu.image || '',
            icon: duLieu.icon || 'fa-solid fa-graduation-cap',
            thoiGianDaoTao: duLieu.thoiGianDaoTao || '',
            doiTuongTuyenSinh: duLieu.doiTuongTuyenSinh || '',
            trangThaiHienThi: duLieu.trangThaiHienThi !== false
        }

        return this.luuDanhSachNganhDaoTao([
            nganhMoi,
            ...state.danhSachNganhDaoTao
        ])
    },

    suaNganhDaoTao(id, duLieu) {
        const danhSachMoi = state.danhSachNganhDaoTao.map((item) => {
            if (item.id !== id) {
                return item
            }

            return {
                ...item,
                ...duLieu,
                id
            }
        })

        return this.luuDanhSachNganhDaoTao(danhSachMoi)
    },

    xoaNganhDaoTao(id) {
        return this.luuDanhSachNganhDaoTao(
            state.danhSachNganhDaoTao.filter((item) => item.id !== id)
        )
    },

    khoiPhucNganhDaoTaoMacDinh() {
        state.danhSachNganhDaoTao = [...danhSachNganhDaoTaoMacDinh]
        localStorage.removeItem(KEY_NGANH_DAO_TAO)

        window.dispatchEvent(
            new CustomEvent('public-noi-dung-thay-doi', {
                detail: {
                    key: KEY_NGANH_DAO_TAO,
                    giaTri: state.danhSachNganhDaoTao
                }
            })
        )

        return state.danhSachNganhDaoTao
    },

    layDanhSachTinTuc() {
        state.danhSachTinTuc = docLocalStorage(KEY_TIN_TUC, danhSachTinTucMacDinh)
        return state.danhSachTinTuc
    },

    luuDanhSachTinTuc(danhSach) {
        state.danhSachTinTuc = Array.isArray(danhSach) ? danhSach : []
        ghiLocalStorage(KEY_TIN_TUC, state.danhSachTinTuc)
        return state.danhSachTinTuc
    },

    themTinTuc(duLieu) {
        const idMoi = duLieu.id || taoIdTinTuc(duLieu.title || duLieu.tieuDe)

        const tinMoi = {
            id: idMoi,
            title: duLieu.title || duLieu.tieuDe || '',
            category: duLieu.category || duLieu.danhMuc || '',
            desc: duLieu.desc || duLieu.moTaNgan || '',
            image: duLieu.image || '',
            day: duLieu.day || new Date().getDate().toString().padStart(2, '0'),
            month: duLieu.month || `TH${String(new Date().getMonth() + 1).padStart(2, '0')}`,
            author: duLieu.author || 'Ban truyền thông',
            noiDung: duLieu.noiDung || '',
            noiDungChiTiet: duLieu.noiDungChiTiet || [],
            trangThaiHienThi: duLieu.trangThaiHienThi !== false
        }

        return this.luuDanhSachTinTuc([
            tinMoi,
            ...state.danhSachTinTuc
        ])
    },

    suaTinTuc(id, duLieu) {
        const danhSachMoi = state.danhSachTinTuc.map((item) => {
            if (item.id !== id) {
                return item
            }

            return {
                ...item,
                ...duLieu,
                id
            }
        })

        return this.luuDanhSachTinTuc(danhSachMoi)
    },

    xoaTinTuc(id) {
        return this.luuDanhSachTinTuc(
            state.danhSachTinTuc.filter((item) => item.id !== id)
        )
    },

    khoiPhucTinTucMacDinh() {
        state.danhSachTinTuc = [...danhSachTinTucMacDinh]
        localStorage.removeItem(KEY_TIN_TUC)

        window.dispatchEvent(
            new CustomEvent('public-noi-dung-thay-doi', {
                detail: {
                    key: KEY_TIN_TUC,
                    giaTri: state.danhSachTinTuc
                }
            })
        )

        return state.danhSachTinTuc
    },

    layDanhSachCamNhanHocVien() {
        state.danhSachCamNhanHocVien = docLocalStorage(KEY_CAM_NHAN_HOC_VIEN, danhSachCamNhanHocVienMacDinh)
        return state.danhSachCamNhanHocVien
    },

    luuDanhSachCamNhanHocVien(danhSach) {
        state.danhSachCamNhanHocVien = Array.isArray(danhSach) ? danhSach : []
        ghiLocalStorage(KEY_CAM_NHAN_HOC_VIEN, state.danhSachCamNhanHocVien)
        return state.danhSachCamNhanHocVien
    },

    themCamNhanHocVien(duLieu) {
        const slugMoi = duLieu.slug || taoSlugCamNhan(duLieu.ten)

        const camNhanMoi = {
            slug: slugMoi,
            ten: duLieu.ten || '',
            khoa: duLieu.khoa || '',
            avatar: duLieu.avatar || '',
            danhGia: Number(duLieu.danhGia || 5),
            cauNoiNgan: duLieu.cauNoiNgan || '',
            tieuDe: duLieu.tieuDe || '',
            namHoc: duLieu.namHoc || '',
            hienTai: duLieu.hienTai || '',
            noiDungChiTiet: duLieu.noiDungChiTiet || [],
            diemNoiBat: duLieu.diemNoiBat || [],
            trangThaiHienThi: duLieu.trangThaiHienThi !== false
        }

        return this.luuDanhSachCamNhanHocVien([
            camNhanMoi,
            ...state.danhSachCamNhanHocVien
        ])
    },

    suaCamNhanHocVien(slug, duLieu) {
        const danhSachMoi = state.danhSachCamNhanHocVien.map((item) => {
            if (item.slug !== slug) {
                return item
            }

            return {
                ...item,
                ...duLieu,
                slug
            }
        })

        return this.luuDanhSachCamNhanHocVien(danhSachMoi)
    },

    xoaCamNhanHocVien(slug) {
        return this.luuDanhSachCamNhanHocVien(
            state.danhSachCamNhanHocVien.filter((item) => item.slug !== slug)
        )
    },

    khoiPhucCamNhanHocVienMacDinh() {
        state.danhSachCamNhanHocVien = [...danhSachCamNhanHocVienMacDinh]
        localStorage.removeItem(KEY_CAM_NHAN_HOC_VIEN)

        window.dispatchEvent(
            new CustomEvent('public-noi-dung-thay-doi', {
                detail: {
                    key: KEY_CAM_NHAN_HOC_VIEN,
                    giaTri: state.danhSachCamNhanHocVien
                }
            })
        )

        return state.danhSachCamNhanHocVien
    }
}

export const PUBLIC_ADMIN_KEYS = {
    THONG_TIN_TRUONG: KEY_THONG_TIN_TRUONG,
    MANG_XA_HOI: KEY_MANG_XA_HOI,
    NGANH_DAO_TAO: KEY_NGANH_DAO_TAO,
    TIN_TUC: KEY_TIN_TUC,
    CAM_NHAN_HOC_VIEN: KEY_CAM_NHAN_HOC_VIEN
}