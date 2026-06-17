import { authApi } from '../api/authApi'
import { tokenService } from '@/core/services/tokenService.js'
import { ROLE_HOME_PATH } from '@/core/constants/roles.js'

function normalizeRole(role) {
    if (!role) return ''

    if (typeof role === 'string') {
        return role.replace('ROLE_', '').toUpperCase()
    }

    if (typeof role === 'object') {
        const value =
            role.maVaiTro ||
            role.tenVaiTro ||
            role.name ||
            role.role ||
            role.authority ||
            role.loaiTaiKhoan ||
            role.code ||
            ''

        return String(value).replace('ROLE_', '').toUpperCase()
    }

    return String(role).replace('ROLE_', '').toUpperCase()
}

function layRolesTuLoginResponse(res) {
    const rawRoles = [
        ...(Array.isArray(res?.roles) ? res.roles : []),
        ...(Array.isArray(res?.authorities) ? res.authorities : []),
        ...(Array.isArray(res?.quyen) ? res.quyen : [])
    ]

    if (res?.role) rawRoles.push(res.role)
    if (res?.vaiTro) rawRoles.push(res.vaiTro)
    if (res?.loaiTaiKhoan) rawRoles.push(res.loaiTaiKhoan)
    if (res?.roleName) rawRoles.push(res.roleName)
    if (res?.authority) rawRoles.push(res.authority)
    if (res?.maVaiTro) rawRoles.push(res.maVaiTro)
    if (res?.tenVaiTro) rawRoles.push(res.tenVaiTro)

    return rawRoles
        .map(normalizeRole)
        .filter(Boolean)
}

export const authModuleService = {
    async login(payload) {
        const res = await authApi.login(payload)

        const accessToken = res?.accessToken
        const refreshToken = res?.refreshToken
        const roles = layRolesTuLoginResponse(res)

        const user = {
            taiKhoanId: res?.taiKhoanId,
            email: res?.email,
            hoTen: res?.hoTen,
            username: res?.username,
            loaiTaiKhoan: res?.loaiTaiKhoan,
            trangThai: res?.trangThai,
            roles,
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
    const normalizedRoles = roles.map(normalizeRole)

    if (normalizedRoles.includes('ADMIN')) return ROLE_HOME_PATH.ADMIN
    if (normalizedRoles.includes('DAO_TAO')) return ROLE_HOME_PATH.DAO_TAO
    if (normalizedRoles.includes('SINH_VIEN')) return ROLE_HOME_PATH.SINH_VIEN
    if (normalizedRoles.includes('GIAO_VIEN')) return ROLE_HOME_PATH.GIAO_VIEN
    if (normalizedRoles.includes('KE_TOAN')) return ROLE_HOME_PATH.KE_TOAN

    return '/admin'
}