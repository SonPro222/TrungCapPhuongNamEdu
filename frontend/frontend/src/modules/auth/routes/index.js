import AuthLayout from '@/layouts/AuthLayout.vue';
import LoginPage from '@/modules/auth/pages/LoginPage.vue';
import RegisterPage from '@/modules/auth/pages/RegisterPage.vue';

export default [
  {
    path: '/',
    component: AuthLayout,
    meta: { public: true },
    children: [
      { path: 'login', name: 'auth-login', component: LoginPage, meta: { public: true } },
      { path: 'register', name: 'auth-register', component: RegisterPage, meta: { public: true } },
    ],
  },
];
