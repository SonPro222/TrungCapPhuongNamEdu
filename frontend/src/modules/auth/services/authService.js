import { tokenService } from './tokenService'
import { ROLES } from '../constants/roles'

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
            role.ma ||
            role.ten ||
            ''

        return String(value).replace('ROLE_', '').toUpperCase()
    }

    return String(role).replace('ROLE_', '').toUpperCase()
}

function layRoleTuUser(user) {
    if (!user) return []

    const rawRoles = [
        ...(Array.isArray(user.roles) ? user.roles : []),
        ...(Array.isArray(user.authorities) ? user.authorities : []),
        ...(Array.isArray(user.quyen) ? user.quyen : []),
        ...(Array.isArray(user.vaiTroList) ? user.vaiTroList : []),
        ...(Array.isArray(user.danhSachVaiTro) ? user.danhSachVaiTro : [])
    ]

    if (user.role) rawRoles.push(user.role)
    if (user.vaiTro) rawRoles.push(user.vaiTro)
    if (user.loaiTaiKhoan) rawRoles.push(user.loaiTaiKhoan)
    if (user.roleName) rawRoles.push(user.roleName)
    if (user.authority) rawRoles.push(user.authority)
    if (user.maVaiTro) rawRoles.push(user.maVaiTro)
    if (user.tenVaiTro) rawRoles.push(user.tenVaiTro)

    return rawRoles
        .map(normalizeRole)
        .filter(Boolean)
}

export const authService = {
    logout() {
        tokenService.clearAuth()
    },

    getCurrentUser() {
        return tokenService.getUser()
    },

    isLoggedIn() {
        const token = tokenService.getToken()

        if (!token) return false

        if (tokenService.isTokenExpired()) {
            tokenService.clearAuth()
            return false
        }

        return true
    },

    getRoles() {
        const user = tokenService.getUser()
        return layRoleTuUser(user)
    },

    hasRole(role) {
        const roles = this.getRoles()
        const targetRole = normalizeRole(role)

        if (roles.includes(ROLES.ADMIN)) {
            return true
        }

        return roles.includes(targetRole)
    },

    hasAnyRole(requiredRoles = []) {
        if (!requiredRoles.length) return true

        if (this.hasRole(ROLES.ADMIN)) {
            return true
        }

        return requiredRoles.some((role) => this.hasRole(role))
    }
}