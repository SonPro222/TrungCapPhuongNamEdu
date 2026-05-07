import ModuleDashboard from '@/components/module/ModuleDashboard.vue';

export default [
  { path: '/chuong-trinh', redirect: '/chuong-trinh/nganh' },
  { path: '/chuong-trinh/nganh', name: 'chuong-trinh', component: ModuleDashboard, meta: { moduleKey: 'chuongTrinh' } },
];
