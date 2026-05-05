import ThiLayout from '@/components/thi/ThiLayout.vue';
import LichThiPage from '@/components/thi/LichThiPage.vue';
import SinhVienDuThiPage from '@/components/thi/SinhVienDuThiPage.vue';
import DeThiPage from '@/components/thi/DeThiPage.vue';
import BaiThiSinhVienPage from '@/components/thi/BaiThiSinhVienPage.vue';

const thiRoutes = [
    {
        path: '/thi',
        component: ThiLayout,
        redirect: '/thi/lich-thi',
        children: [
        { path: 'lich-thi', name: 'thi-lich-thi', component: LichThiPage },
        { path: 'sinh-vien-du-thi', name: 'thi-sinh-vien-du-thi', component: SinhVienDuThiPage },
        { path: 'de-thi', name: 'thi-de-thi', component: DeThiPage },
        { path: 'bai-thi-sinh-vien', name: 'thi-bai-thi-sinh-vien', component: BaiThiSinhVienPage },
        ],
    },
];

export default thiRoutes;
