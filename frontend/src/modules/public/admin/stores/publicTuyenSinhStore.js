import { reactive } from 'vue'
import { danhSachChiTietTuyenSinhMacDinh } from '@/modules/public/data/tuyenSinhChiTietData.js'
import { PUBLIC_TUYEN_SINH_KEY } from '@/modules/public/composables/useTuyenSinhPublic.js'

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

const docLocalStorage = () => {
    try {
        const duLieu = localStorage.getItem(PUBLIC_TUYEN_SINH_KEY)

        if (!duLieu) {
            return [...danhSachChiTietTuyenSinhMacDinh]
        }

        const duLieuDaParse = JSON.parse(duLieu)

        return Array.isArray(duLieuDaParse)
            ? duLieuDaParse
            : [...danhSachChiTietTuyenSinhMacDinh]
    } catch (error) {
        console.error('Không đọc được dữ liệu tuyển sinh public:', error)
        return [...danhSachChiTietTuyenSinhMacDinh]
    }
}

const ghiLocalStorage = (danhSach) => {
    localStorage.setItem(PUBLIC_TUYEN_SINH_KEY, JSON.stringify(danhSach))

    window.dispatchEvent(
        new CustomEvent('public-noi-dung-thay-doi', {
            detail: {
                key: PUBLIC_TUYEN_SINH_KEY,
                giaTri: danhSach
            }
        })
    )
}

const state = reactive({
    danhSachTuyenSinh: docLocalStorage()
})

export const publicTuyenSinhStore = {
    state,

    layDanhSachTuyenSinh() {
        state.danhSachTuyenSinh = docLocalStorage()
        return state.danhSachTuyenSinh
    },

    luuDanhSachTuyenSinh(danhSach) {
        state.danhSachTuyenSinh = Array.isArray(danhSach) ? danhSach : []
        ghiLocalStorage(state.danhSachTuyenSinh)
        return state.danhSachTuyenSinh
    },

    themTuyenSinh(duLieu) {
        const slugMoi = duLieu.slug || taoSlug(duLieu.tieuDe)

        const itemMoi = {
            slug: slugMoi || `tuyen-sinh-${Date.now()}`,
            tieuDe: duLieu.tieuDe || '',
            moTa: duLieu.moTa || '',
            icon: duLieu.icon || 'fa-solid fa-circle-info',
            thongTinNhanh: duLieu.thongTinNhanh || [],
            noiDungChiTiet: duLieu.noiDungChiTiet || [],
            trangThaiHienThi: duLieu.trangThaiHienThi !== false
        }

        return this.luuDanhSachTuyenSinh([
            itemMoi,
            ...state.danhSachTuyenSinh
        ])
    },

    suaTuyenSinh(slug, duLieu) {
        const danhSachMoi = state.danhSachTuyenSinh.map((item) => {
            if (item.slug !== slug) {
                return item
            }

            return {
                ...item,
                ...duLieu,
                slug
            }
        })

        return this.luuDanhSachTuyenSinh(danhSachMoi)
    },

    xoaTuyenSinh(slug) {
        return this.luuDanhSachTuyenSinh(
            state.danhSachTuyenSinh.filter((item) => item.slug !== slug)
        )
    },

    khoiPhucMacDinh() {
        state.danhSachTuyenSinh = [...danhSachChiTietTuyenSinhMacDinh]
        localStorage.removeItem(PUBLIC_TUYEN_SINH_KEY)

        window.dispatchEvent(
            new CustomEvent('public-noi-dung-thay-doi', {
                detail: {
                    key: PUBLIC_TUYEN_SINH_KEY,
                    giaTri: state.danhSachTuyenSinh
                }
            })
        )

        return state.danhSachTuyenSinh
    }
}