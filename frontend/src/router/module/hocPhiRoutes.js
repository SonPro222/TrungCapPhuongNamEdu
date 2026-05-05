import HocPhiLayout from '@/components/hocPhi/HocPhiLayout.vue';
import HocPhiPage from '@/components/hocPhi/HocPhiPage.vue';
import GiaoDichHocPhiPage from '@/components/hocPhi/GiaoDichHocPhiPage.vue';

const hocPhiRoutes = [
    {
        path: '/hoc-phi',
        component: HocPhiLayout,
        redirect: '/hoc-phi/hoc-phi',
        children: [
        { path: 'hoc-phi', name: 'hocPhi-hoc-phi', component: HocPhiPage },
        { path: 'giao-dich-hoc-phi', name: 'hocPhi-giao-dich-hoc-phi', component: GiaoDichHocPhiPage },
        ],
    },
];

export default hocPhiRoutes;
