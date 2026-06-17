import { onBeforeUnmount, ref } from 'vue'
import { danhSachNganhDaoTao as danhSachNganhDaoTaoMacDinh } from '@/modules/public/data/nganhDaoTaoData.js'
import { danhSachTinTuc as danhSachTinTucMacDinh } from '@/modules/public/data/tinTucData.js'
import { danhSachCamNhanHocVien as danhSachCamNhanHocVienMacDinh } from '@/modules/public/data/camNhanHocVienData.js'

const KEY_THONG_TIN_TRUONG = 'PUBLIC_ADMIN_THONG_TIN_TRUONG'
const KEY_MANG_XA_HOI = 'PUBLIC_ADMIN_MANG_XA_HOI'
const KEY_NGANH_DAO_TAO = 'PUBLIC_ADMIN_NGANH_DAO_TAO'
const KEY_TIN_TUC = 'PUBLIC_ADMIN_TIN_TUC'
const KEY_CAM_NHAN_HOC_VIEN = 'PUBLIC_ADMIN_CAM_NHAN_HOC_VIEN'

const thongTinTruongMacDinh = {
    tenNgan: 'Trường Trung Cấp Phương Nam',
    tenDong1: 'TRƯỜNG TRUNG CẤP',
    tenDong2: 'PHƯƠNG NAM',
    slogan: 'Tri thức vững vàng – Tương lai rộng mở',
    hotline: '1900 633 088',
    dienThoai: '(028) 38 680 886',
    email: 'tuyensinh@phuongnam.edu.vn',
    website: 'www.phuongnam.edu.vn',
    diaChi: '123 Đường Giáo Dục, P. An Lạc, Q. Bình Tân, TP. Hồ Chí Minh',
    moTa: 'Trường Trung cấp Phương Nam cam kết đào tạo nguồn nhân lực chất lượng cao, đáp ứng nhu cầu xã hội và đồng hành cùng học viên trên con đường lập thân – lập nghiệp.'
}

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

export const useNoiDungPublic = () => {
    const thongTinTruong = ref(docLocalStorage(KEY_THONG_TIN_TRUONG, thongTinTruongMacDinh))
    const mangXaHoi = ref(docLocalStorage(KEY_MANG_XA_HOI, mangXaHoiMacDinh))
    const danhSachNganhDaoTao = ref(docLocalStorage(KEY_NGANH_DAO_TAO, danhSachNganhDaoTaoMacDinh))
    const danhSachTinTuc = ref(docLocalStorage(KEY_TIN_TUC, danhSachTinTucMacDinh))
    const danhSachCamNhanHocVien = ref(docLocalStorage(KEY_CAM_NHAN_HOC_VIEN, danhSachCamNhanHocVienMacDinh))

    const napLaiDuLieu = () => {
        thongTinTruong.value = docLocalStorage(KEY_THONG_TIN_TRUONG, thongTinTruongMacDinh)
        mangXaHoi.value = docLocalStorage(KEY_MANG_XA_HOI, mangXaHoiMacDinh)
        danhSachNganhDaoTao.value = docLocalStorage(KEY_NGANH_DAO_TAO, danhSachNganhDaoTaoMacDinh)
        danhSachTinTuc.value = docLocalStorage(KEY_TIN_TUC, danhSachTinTucMacDinh)
        danhSachCamNhanHocVien.value = docLocalStorage(KEY_CAM_NHAN_HOC_VIEN, danhSachCamNhanHocVienMacDinh)
    }

    const xuLyStorage = (event) => {
        if (
            event.key === KEY_THONG_TIN_TRUONG ||
            event.key === KEY_MANG_XA_HOI ||
            event.key === KEY_NGANH_DAO_TAO ||
            event.key === KEY_TIN_TUC ||
            event.key === KEY_CAM_NHAN_HOC_VIEN
        ) {
            napLaiDuLieu()
        }
    }

    const xuLyPublicNoiDungThayDoi = () => {
        napLaiDuLieu()
    }

    window.addEventListener('storage', xuLyStorage)
    window.addEventListener('public-noi-dung-thay-doi', xuLyPublicNoiDungThayDoi)

    onBeforeUnmount(() => {
        window.removeEventListener('storage', xuLyStorage)
        window.removeEventListener('public-noi-dung-thay-doi', xuLyPublicNoiDungThayDoi)
    })

    return {
        thongTinTruong,
        mangXaHoi,
        danhSachNganhDaoTao,
        danhSachTinTuc,
        danhSachCamNhanHocVien,
        napLaiDuLieu
    }
}