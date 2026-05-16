import { authService } from '../services/authService'

export function requireRole(role) {
    return () => {
        if (!authService.isLoggedIn()) {
            return '/auth/login'
        }

        if (!authService.hasRole(role)) {
            return '/admin'
        }

        return true
    }
}