import { onBeforeUnmount, ref } from 'vue'

const KEY_LIEN_HE_PUBLIC = 'PUBLIC_ADMIN_LIEN_HE'

const docLocalStorage = () => {
    try {
        const duLieu = localStorage.getItem(KEY_LIEN_HE_PUBLIC)

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
    localStorage.setItem(KEY_LIEN_HE_PUBLIC, JSON.stringify(danhSach))

    window.dispatchEvent(
        new CustomEvent('public-noi-dung-thay-doi', {
            detail: {
                key: KEY_LIEN_HE_PUBLIC,
                giaTri: danhSach
            }
        })
    )
}

const taoId = () => {
    return `lien-he-${Date.now()}-${Math.random().toString(36).slice(2, 8)}`
}

export const useLienHePublic = () => {
    const danhSachLienHe = ref(docLocalStorage())

    const napLaiDuLieu = () => {
        danhSachLienHe.value = docLocalStorage()
    }

    const themLienHe = (duLieu) => {
        const lienHeMoi = {
            id: taoId(),
            hoTen: duLieu.hoTen || '',
            soDienThoai: duLieu.soDienThoai || '',
            email: duLieu.email || '',
            nganhQuanTam: duLieu.nganhQuanTam || '',
            noiDung: duLieu.noiDung || '',
            trangThai: 'moi',
            thoiGianGui: new Date().toISOString()
        }

        const danhSachMoi = [
            lienHeMoi,
            ...danhSachLienHe.value
        ]

        danhSachLienHe.value = danhSachMoi
        ghiLocalStorage(danhSachMoi)

        return lienHeMoi
    }

    const xuLyStorage = (event) => {
        if (event.key === KEY_LIEN_HE_PUBLIC) {
            napLaiDuLieu()
        }
    }

    const xuLyPublicNoiDungThayDoi = (event) => {
        if (!event?.detail?.key || event.detail.key === KEY_LIEN_HE_PUBLIC) {
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
        danhSachLienHe,
        themLienHe,
        napLaiDuLieu
    }
}

export const PUBLIC_LIEN_HE_KEY = KEY_LIEN_HE_PUBLIC