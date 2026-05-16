import { authApi } from '../api/authApi'
import { tokenService } from '@/core/services/tokenService.js'
import { ROLE_HOME_PATH } from '@/core/constants/roles.js'

export const authModuleService = {
    async login(payload) {
        const res = await authApi.login(payload)

        const accessToken = res?.accessToken
        const refreshToken = res?.refreshToken

        const user = {
            taiKhoanId: res?.taiKhoanId,
            email: res?.email,
            loaiTaiKhoan: res?.loaiTaiKhoan,
            trangThai: res?.trangThai,
            roles: res?.roles || [],
            permissions: res?.permissions || []
        }

        if (!accessToken) {
            throw new Error('BE chưa trả accessToken')
        }

        tokenService.setToken(accessToken)

        if (refreshToken) {
            tokenService.setRefreshToken(refreshToken)
        }

        tokenService.setUser(user)

        return {
            user,
            accessToken,
            homePath: getHomePathByRoles(user.roles)
        }
    },

    logout() {
        tokenService.clearAuth()
    }
}

function getHomePathByRoles(roles = []) {
    if (roles.includes('ADMIN')) return ROLE_HOME_PATH.ADMIN
    if (roles.includes('DAO_TAO')) return ROLE_HOME_PATH.DAO_TAO
    if (roles.includes('SINH_VIEN')) return ROLE_HOME_PATH.SINH_VIEN
    if (roles.includes('GIAO_VIEN')) return ROLE_HOME_PATH.GIAO_VIEN
    if (roles.includes('KE_TOAN')) return ROLE_HOME_PATH.KE_TOAN

    return '/admin'
}