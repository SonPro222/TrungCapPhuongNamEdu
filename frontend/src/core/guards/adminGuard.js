import { authService } from '../services/authService'
import { ROLES } from '../constants/roles'

export function requireAdmin(to) {
    if (!authService.isLoggedIn()) {
        return '/auth/login'
    }

    const requiredRoles = to.meta?.roles || []

    // Nếu route chưa khai báo quyền thì chỉ cần login là được vào.
    if (!requiredRoles.length) {
        return true
    }

    // Admin được vào tất cả.
    if (authService.hasRole(ROLES.ADMIN)) {
        return true
    }

    if (!authService.hasAnyRole(requiredRoles)) {
        return '/admin'
    }

    return true
}