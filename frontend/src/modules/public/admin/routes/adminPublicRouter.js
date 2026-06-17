import AdminPublic from '@/modules/public/admin/pages/AdminPublic.vue'
import AdminThongTinTruong from '@/modules/public/admin/pages/AdminThongTinTruong.vue'
import AdminMangXaHoi from '@/modules/public/admin/pages/AdminMangXaHoi.vue'
import AdminNganhDaoTaoPublic from '@/modules/public/admin/pages/AdminNganhDaoTaoPublic.vue'
import AdminTinTucPublic from '@/modules/public/admin/pages/AdminTinTucPublic.vue'
import AdminCamNhanHocVien from '@/modules/public/admin/pages/AdminCamNhanHocVien.vue'
import AdminTuyenSinhPublic from '@/modules/public/admin/pages/AdminTuyenSinhPublic.vue'
import AdminLienHePublic from '@/modules/public/admin/pages/AdminLienHePublic.vue'
import AdminBannerTrangChuPublic from '@/modules/public/admin/pages/AdminBannerTrangChuPublic.vue'

const adminPublicRoutes = [
    {
        path: 'public',
        name: 'Admin.Public',
        component: AdminPublic,
        meta: {
            title: 'Quản lý website public'
        }
    },
    {
        path: 'public/thong-tin-truong',
        name: 'Admin.Public.ThongTinTruong',
        component: AdminThongTinTruong,
        meta: {
            title: 'Thông tin trường'
        }
    },
    {
        path: 'public/mang-xa-hoi',
        name: 'Admin.Public.MangXaHoi',
        component: AdminMangXaHoi,
        meta: {
            title: 'Mạng xã hội'
        }
    },
    {
        path: 'public/nganh-dao-tao',
        name: 'Admin.Public.NganhDaoTao',
        component: AdminNganhDaoTaoPublic,
        meta: {
            title: 'Ngành đào tạo public'
        }
    },
    {
        path: 'public/tin-tuc',
        name: 'Admin.Public.TinTuc',
        component: AdminTinTucPublic,
        meta: {
            title: 'Tin tức public'
        }
    },
    {
        path: 'public/cam-nhan-hoc-vien',
        name: 'Admin.Public.CamNhanHocVien',
        component: AdminCamNhanHocVien,
        meta: {
            title: 'Cảm nhận học viên'
        }
    },
    {
        path: 'public/tuyen-sinh',
        name: 'Admin.Public.TuyenSinh',
        component: AdminTuyenSinhPublic,
        meta: {
            title: 'Tuyển sinh public'
        }
    },
    {
        path: 'public/lien-he',
        name: 'Admin.Public.LienHe',
        component: AdminLienHePublic,
        meta: {
            title: 'Liên hệ public'
        }
    },
    {
        path: 'public/banner',
        name: 'Admin.Public.BannerTrangChu',
        component: AdminBannerTrangChuPublic,
        meta: {
            title: 'Banner trang chủ'
        }
    }
]

export default adminPublicRoutes