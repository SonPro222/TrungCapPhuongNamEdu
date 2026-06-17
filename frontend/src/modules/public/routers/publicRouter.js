import TrangChuPublic from '@/modules/public/pages/TrangChuPublic.vue'
import GioiThieuPublic from '@/modules/public/pages/GioiThieuPublic.vue'
import NganhDaoTaoPublic from '@/modules/public/pages/NganhDaoTaoPublic.vue'
import ChiTietNganhDaoTaoPublic from '@/modules/public/pages/ChiTietNganhDaoTaoPublic.vue'
import TuyenSinhPublic from '@/modules/public/pages/TuyenSinhPublic.vue'
import ChiTietTuyenSinhPublic from '@/modules/public/pages/ChiTietTuyenSinhPublic.vue'
import TinTucPublic from '@/modules/public/pages/TinTucPublic.vue'
import ChiTietTinTucPublic from '@/modules/public/pages/ChiTietTinTucPublic.vue'
import LienHePublic from '@/modules/public/pages/LienHePublic.vue'
import DangNhapPublic from '@/modules/public/pages/DangNhapPublic.vue'
import ChiTietCoSoVatChatPublic from '@/modules/public/pages/ChiTietCoSoVatChatPublic.vue'
import ChiTietCamNhanHocVienPublic from '@/modules/public/pages/ChiTietCamNhanHocVienPublic.vue'

const publicRoutes = [
    {
        path: '',
        name: 'Public.TrangChu',
        component: TrangChuPublic,
        meta: {
            title: 'Trang chủ'
        }
    },
    {
        path: 'gioi-thieu',
        name: 'Public.GioiThieu',
        component: GioiThieuPublic,
        meta: {
            title: 'Giới thiệu'
        }
    },
    {
        path: 'nganh-dao-tao',
        name: 'Public.NganhDaoTao',
        component: NganhDaoTaoPublic,
        meta: {
            title: 'Ngành đào tạo'
        }
    },
    {
        path: 'nganh-dao-tao/:id',
        name: 'Public.ChiTietNganhDaoTao',
        component: ChiTietNganhDaoTaoPublic,
        meta: {
            title: 'Chi tiết ngành đào tạo'
        }
    },
    {
        path: 'co-so-vat-chat/:id',
        name: 'Public.ChiTietCoSoVatChat',
        component: ChiTietCoSoVatChatPublic,
        meta: {
            title: 'Chi tiết cơ sở vật chất'
        }
    },
    {
        path: 'tuyen-sinh',
        name: 'Public.TuyenSinh',
        component: TuyenSinhPublic,
        meta: {
            title: 'Tuyển sinh'
        }
    },
    {
        path: 'tuyen-sinh/:slug',
        name: 'Public.ChiTietTuyenSinh',
        component: ChiTietTuyenSinhPublic,
        meta: {
            title: 'Chi tiết tuyển sinh'
        }
    },
    {
        path: 'cam-nhan-hoc-vien/:slug',
        name: 'Public.ChiTietCamNhanHocVien',
        component: ChiTietCamNhanHocVienPublic,
        meta: {
            title: 'Cảm nhận học viên'
        }
    },
    {
        path: 'tin-tuc',
        name: 'Public.TinTuc',
        component: TinTucPublic,
        meta: {
            title: 'Tin tức'
        }
    },
    {
        path: 'tin-tuc/:id',
        name: 'Public.ChiTietTinTuc',
        component: ChiTietTinTucPublic,
        meta: {
            title: 'Chi tiết tin tức'
        }
    },
    {
        path: 'lien-he',
        name: 'Public.LienHe',
        component: LienHePublic,
        meta: {
            title: 'Liên hệ'
        }
    },
    {
        path: 'dang-nhap',
        name: 'Public.DangNhap',
        component: DangNhapPublic,
        meta: {
            title: 'Đăng nhập',
            anLayoutPublic: true
        }
    }
]

export default publicRoutes