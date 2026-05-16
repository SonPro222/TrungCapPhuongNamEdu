import { authService } from '../services/authService'

export function requireAuth() {
    if (!authService.isLoggedIn()) {
        return '/auth/login'
    }

    return true
}