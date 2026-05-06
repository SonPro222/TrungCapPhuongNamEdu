import DaoTaoLayout from '@/components/daoTao/DaoTaoLayout.vue';
import NganhPage from '@/components/daoTao/NganhPage.vue';
import NganhDetailPage from '@/components/daoTao/NganhDetailPage.vue';
import TrinhDoDaoTaoPage from '@/components/daoTao/TrinhDoDaoTaoPage.vue';
import LoaiChuongTrinhPage from '@/components/daoTao/LoaiChuongTrinhPage.vue';
import KhungKyPage from '@/components/daoTao/KhungKyPage.vue';
import KhungKyDetailPage from '@/components/daoTao/KhungKyDetailPage.vue';
import KhoaDaoTaoPage from '@/components/daoTao/KhoaDaoTaoPage.vue';
import LopHanhChinhPage from '@/components/daoTao/LopHanhChinhPage.vue';
import LopHanhChinhDetailPage from '@/components/daoTao/LopHanhChinhDetailPage.vue';
import DaoTaoDetailPage from '@/components/daoTao/DaoTaoDetailPage.vue';
import ChuongTrinhPage from '@/components/chuongTrinh/ChuongTrinhPage.vue';

const daoTaoChildren = [
    {
        path: '',
        redirect: '/dao-tao/nganh',
    },
    {
        path: 'nganh',
        name: 'dao-tao-nganh',
        component: NganhPage,
    },
    {
        path: 'nganh/:nganhId',
        name: 'dao-tao-nganh-detail',
        component: NganhDetailPage,
        props: true,
    },
    {
        path: 'nganh/:nganhId/chuong-trinh',
        name: 'dao-tao-nganh-chuong-trinh',
        component: ChuongTrinhPage,
        props: true,
    },
    {
        path: 'trinh-do-dao-tao',
        name: 'dao-tao-trinh-do',
        component: TrinhDoDaoTaoPage,
    },
    {
        path: 'trinh-do-dao-tao/:id',
        name: 'dao-tao-trinh-do-detail',
        component: DaoTaoDetailPage,
        meta: {
            title: 'Chi tiết trình độ đào tạo',
            backTo: { name: 'dao-tao-trinh-do' },
        },
    },
    {
        path: 'loai-chuong-trinh',
        name: 'dao-tao-loai-chuong-trinh',
        component: LoaiChuongTrinhPage,
    },
    {
        path: 'loai-chuong-trinh/:id',
        name: 'dao-tao-loai-chuong-trinh-detail',
        component: DaoTaoDetailPage,
        meta: {
            title: 'Chi tiết loại chương trình',
            backTo: { name: 'dao-tao-loai-chuong-trinh' },
        },
    },
    {
        path: 'loai-chuong-trinh/:loaiChuongTrinhId/khung-ky',
        name: 'dao-tao-loai-chuong-trinh-khung-ky',
        component: KhungKyPage,
        props: true,
    },
    {
        path: 'khung-ky',
        name: 'dao-tao-khung-ky',
        component: KhungKyPage,
    },
    {
        path: 'khung-ky/:khungKyId',
        name: 'dao-tao-khung-ky-detail',
        component: KhungKyDetailPage,
        props: true,
    },
    {
        path: 'khoa-dao-tao',
        name: 'dao-tao-khoa-dao-tao',
        component: KhoaDaoTaoPage,
    },
    {
        path: 'khoa-dao-tao/:id',
        name: 'dao-tao-khoa-dao-tao-detail',
        component: DaoTaoDetailPage,
        meta: {
            title: 'Chi tiết khóa đào tạo',
            backTo: { name: 'dao-tao-khoa-dao-tao' },
        },
    },
    {
        path: 'khoa-dao-tao/:khoaDaoTaoId/lop-hanh-chinh',
        name: 'dao-tao-khoa-lop-hanh-chinh',
        component: LopHanhChinhPage,
        props: true,
    },
    {
        path: 'lop-hanh-chinh',
        name: 'dao-tao-lop-hanh-chinh',
        component: LopHanhChinhPage,
    },
    {
        path: 'lop-hanh-chinh/:lopHanhChinhId',
        name: 'dao-tao-lop-hanh-chinh-detail',
        component: LopHanhChinhDetailPage,
        props: true,
    },
];

const daoTaoRoutes = [
    {
        path: '/dao-tao',
        component: DaoTaoLayout,
        children: daoTaoChildren,
    },
];

export default daoTaoRoutes;
