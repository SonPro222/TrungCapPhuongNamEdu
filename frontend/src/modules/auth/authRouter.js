import LoginPage from './pages/LoginPage.vue'
import OAuthRedirectPage from './pages/OAuthRedirectPage.vue'

export const authRoutes = [
    {
        path: 'login',
        name: 'Auth.Login',
        component: LoginPage,
        meta: {
            public: true,
            title: 'Đăng nhập'
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