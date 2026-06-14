import { onBeforeUnmount, ref } from 'vue'
import { bannerTrangChuMacDinh } from '@/modules/public/data/bannerTrangChuData.js'

const KEY_BANNER_TRANG_CHU = 'PUBLIC_ADMIN_BANNER_TRANG_CHU'

const docLocalStorage = () => {
    try {
        const duLieu = localStorage.getItem(KEY_BANNER_TRANG_CHU)

        if (!duLieu) {
            return {
                ...bannerTrangChuMacDinh,
                thongKe: [...bannerTrangChuMacDinh.thongKe]
            }
        }

        return {
            ...bannerTrangChuMacDinh,
            ...JSON.parse(duLieu)
        }
    } catch (error) {
        console.error('Không đọc được dữ liệu banner trang chủ:', error)

        return {
            ...bannerTrangChuMacDinh,
            thongKe: [...bannerTrangChuMacDinh.thongKe]
        }
    }
}

export const useBannerTrangChuPublic = () => {
    const bannerTrangChu = ref(docLocalStorage())

    const napLaiDuLieu = () => {
        bannerTrangChu.value = docLocalStorage()
    }

    const xuLyStorage = (event) => {
        if (event.key === KEY_BANNER_TRANG_CHU) {
            napLaiDuLieu()
        }
    }

    const xuLyPublicNoiDungThayDoi = (event) => {
        if (!event?.detail?.key || event.detail.key === KEY_BANNER_TRANG_CHU) {
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
        bannerTrangChu,
        napLaiDuLieu
    }
}

export const PUBLIC_BANNER_TRANG_CHU_KEY = KEY_BANNER_TRANG_CHU