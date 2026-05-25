import LoginPage from './pages/LoginPage.vue'
import OAuthRedirectPage from './pages/OAuthRedirectPage.vue'
import DoiMatKhauSinhVienPage from './pages/DoiMatKhauSinhVienPage.vue'

export const authRoutes = [
    {
        path: 'login',
        name: 'Auth.Login',
        component: LoginPage,
        meta: {
            public: true,
            title: 'Đăng nhập'
        }
    },
    {
        path: 'doi-mat-khau-sinh-vien',
        name: 'Auth.DoiMatKhauSinhVien',
        component: DoiMatKhauSinhVienPage,
        meta: {
            public: true,
            title: 'Đổi mật khẩu sinh viên'
        }
    }
]

export const oauthRoutes = [
    {
        path: '/oauth2/redirect',
        name: 'Auth.OAuthRedirect',
        component: OAuthRedirectPage,
        meta: {
            public: true,
            title: 'Google Redirect'
        }
    }
]