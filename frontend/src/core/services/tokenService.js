import { storageService } from './storageService'

const TOKEN_KEY = 'access_token'
const REFRESH_TOKEN_KEY = 'refresh_token'
const USER_KEY = 'auth_user'

function decodeJwt(token) {
    try {
        const payload = token.split('.')[1]
        return JSON.parse(atob(payload))
    } catch {
        return null
    }
}

export const tokenService = {
    getToken() {
        return storageService.get(TOKEN_KEY)
    },

    setToken(token) {
        storageService.set(TOKEN_KEY, token)
    },

    getRefreshToken() {
        return storageService.get(REFRESH_TOKEN_KEY)
    },

    setRefreshToken(token) {
        storageService.set(REFRESH_TOKEN_KEY, token)
    },

    getUser() {
        const raw = storageService.get(USER_KEY)
        return raw ? JSON.parse(raw) : null
    },

    setUser(user) {
        storageService.set(USER_KEY, JSON.stringify(user))
    },

    isTokenExpired() {
        const token = this.getToken()

        if (!token) return true

        const decoded = decodeJwt(token)

        if (!decoded?.exp) return true

        const now = Math.floor(Date.now() / 1000)

        return decoded.exp <= now
    },

    clearAuth() {
        storageService.remove(TOKEN_KEY)
        storageService.remove(REFRESH_TOKEN_KEY)
        storageService.remove(USER_KEY)
    }
}