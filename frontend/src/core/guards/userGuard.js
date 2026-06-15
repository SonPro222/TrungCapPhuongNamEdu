import { authService } from '../services/authService'
import { ROLES } from '../constants/roles'

const USER_ROLES = [ROLES.SINH_VIEN, ROLES.GIAO_VIEN, 'GIANG_VIEN']

export function requireUser() {
    if (!authService.isLoggedIn()) {
        return '/auth/login'
    }

    if (!authService.hasAnyRole(USER_ROLES)) {
        return '/dashboard'
    }

    return true
}
