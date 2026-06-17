import { onBeforeUnmount, ref } from 'vue'
import { danhSachChiTietTuyenSinhMacDinh } from '@/modules/public/data/tuyenSinhChiTietData.js'

const KEY_TUYEN_SINH = 'PUBLIC_ADMIN_TUYEN_SINH'

const docLocalStorage = () => {
    try {
        const duLieu = localStorage.getItem(KEY_TUYEN_SINH)

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

export const useTuyenSinhPublic = () => {
    const danhSachTuyenSinh = ref(docLocalStorage())

    const napLaiDuLieu = () => {
        danhSachTuyenSinh.value = docLocalStorage()
    }

    const xuLyStorage = (event) => {
        if (event.key === KEY_TUYEN_SINH) {
            napLaiDuLieu()
        }
    }

    const xuLyPublicNoiDungThayDoi = (event) => {
        if (!event?.detail?.key || event.detail.key === KEY_TUYEN_SINH) {
            napLaiDuLieu()
        }
    }

    window.addEventListener('storage', xuLyStorage)
    window.addEventListener('public-noi-dung-thay-doi', xuLyPublicNoiDungThayDoi)

    onBeforeUnmount(() => {
        window.removeEventListener('storage', xuLyStorage)
        window.removeEventListener('public-noi-dung-thay-doi', xuLyPublicNoiDungThayDoi)
    })

    return {
        danhSachTuyenSinh,
        napLaiDuLieu
    }
}

export const PUBLIC_TUYEN_SINH_KEY = KEY_TUYEN_SINH