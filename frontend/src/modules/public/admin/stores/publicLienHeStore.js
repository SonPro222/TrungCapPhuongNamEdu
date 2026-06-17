import { reactive } from 'vue'
import { PUBLIC_LIEN_HE_KEY } from '@/modules/public/composables/useLienHePublic.js'

const docLocalStorage = () => {
    try {
        const duLieu = localStorage.getItem(PUBLIC_LIEN_HE_KEY)

        if (!duLieu) {
            return []
        }

        const duLieuDaParse = JSON.parse(duLieu)

        return Array.isArray(duLieuDaParse) ? duLieuDaParse : []
    } catch (error) {
        console.error('Không đọc được dữ liệu liên hệ public:', error)
        return []
    }
}

const ghiLocalStorage = (danhSach) => {
    localStorage.setItem(PUBLIC_LIEN_HE_KEY, JSON.stringify(danhSach))

    window.dispatchEvent(
        new CustomEvent('public-noi-dung-thay-doi', {
            detail: {
                key: PUBLIC_LIEN_HE_KEY,
                giaTri: danhSach
            }
        })
    )
}

const state = reactive({
    danhSachLienHe: docLocalStorage()
})

export const publicLienHeStore = {
    state,

    layDanhSachLienHe() {
        state.danhSachLienHe = docLocalStorage()
        return state.danhSachLienHe
    },

    luuDanhSachLienHe(danhSach) {
        state.danhSachLienHe = Array.isArray(danhSach) ? danhSach : []
        ghiLocalStorage(state.danhSachLienHe)
        return state.danhSachLienHe
    },

    capNhatTrangThai(id, trangThai) {
        const danhSachMoi = state.danhSachLienHe.map((item) => {
            if (item.id !== id) {
                return item
            }

            return {
                ...item,
                trangThai
            }
        })

        return this.luuDanhSachLienHe(danhSachMoi)
    },

    xoaLienHe(id) {
        return this.luuDanhSachLienHe(
            state.danhSachLienHe.filter((item) => item.id !== id)
        )
    },

    xoaTatCa() {
        return this.luuDanhSachLienHe([])
    }
}