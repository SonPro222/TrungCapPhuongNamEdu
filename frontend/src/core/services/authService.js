import { tokenService } from './tokenService'
import { ROLES } from '../constants/roles'

function normalizeRole(role) {
    return String(role || '').replace('ROLE_', '').toUpperCase()
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
        const roles = user?.roles || user?.authorities || user?.quyen || []

        return roles.map(normalizeRole)
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

        return requiredRoles.some((role) => this.hasRole(role))
    }
}