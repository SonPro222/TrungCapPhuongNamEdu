import { reactive } from 'vue'
import { bannerTrangChuMacDinh } from '@/modules/public/data/bannerTrangChuData.js'
import { PUBLIC_BANNER_TRANG_CHU_KEY } from '@/modules/public/composables/useBannerTrangChuPublic.js'

const docLocalStorage = () => {
    try {
        const duLieu = localStorage.getItem(PUBLIC_BANNER_TRANG_CHU_KEY)

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

const ghiLocalStorage = (duLieu) => {
    localStorage.setItem(PUBLIC_BANNER_TRANG_CHU_KEY, JSON.stringify(duLieu))

    window.dispatchEvent(
        new CustomEvent('public-noi-dung-thay-doi', {
            detail: {
                key: PUBLIC_BANNER_TRANG_CHU_KEY,
                giaTri: duLieu
            }
        })
    )
}

const state = reactive({
    bannerTrangChu: docLocalStorage()
})

export const publicBannerTrangChuStore = {
    state,

    layBannerTrangChu() {
        state.bannerTrangChu = docLocalStorage()
        return state.bannerTrangChu
    },

    luuBannerTrangChu(duLieu) {
        const duLieuMoi = {
            ...state.bannerTrangChu,
            ...duLieu
        }

        state.bannerTrangChu = duLieuMoi
        ghiLocalStorage(duLieuMoi)

        return duLieuMoi
    },

    khoiPhucMacDinh() {
        state.bannerTrangChu = {
            ...bannerTrangChuMacDinh,
            thongKe: [...bannerTrangChuMacDinh.thongKe]
        }

        localStorage.removeItem(PUBLIC_BANNER_TRANG_CHU_KEY)

        window.dispatchEvent(
            new CustomEvent('public-noi-dung-thay-doi', {
                detail: {
                    key: PUBLIC_BANNER_TRANG_CHU_KEY,
                    giaTri: state.bannerTrangChu
                }
            })
        )

        return state.bannerTrangChu
    }
}